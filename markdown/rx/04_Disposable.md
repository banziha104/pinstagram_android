# Disposable 객체 관리

> Disposable 관리, 

- ### Disposable의 Lifecycle을 정의 

```kotlin

data class DisposableLifecycle(
  val entryPoint: EntryPoint, // 시작지점 : 해당 Diposable이 실행될 라이프사이클 이벤트
  val endPoint: EndPoint, // 해당 Dipsoable이 dipose될 라이프사이클 이벤트 
)

enum class EntryPoint(
    val lifecycle: Lifecycle.Event?,
    var isRunImmediately : Boolean = false
) {
    CREATE(Lifecycle.Event.ON_CREATE),
    START(Lifecycle.Event.ON_START),
    RESUME(Lifecycle.Event.ON_RESUME),
    IMMEDIATELY(null,true);

    // 해당 이벤트 (예 : OnCrate) 보다 늦게 호출되어 실행이 안되는 경우, 호출 시 즉시 실행
    fun runImmediately() : EntryPoint{
        this.isRunImmediately = true
        return this
    }
}

enum class EndPoint(
    val lifecycle: Lifecycle.Event
) {
    PAUSE(Lifecycle.Event.ON_PAUSE),
    STOP(Lifecycle.Event.ON_STOP),
    DESTROY(Lifecycle.Event.ON_DESTROY)
}
```


<br>

- ### 실행될 Dispoable 객체와 Lifecycle을 명시한 DisposableSubscription 객체
  

```kotlin

typealias DisposableFunction = () -> Disposable?

interface DisposableAddable : LifecycleObserver {
    fun add(disposable: Disposable?)
}

class DisposableSubscription(
    val lifecycle: DisposableLifecycle,
    val generator: DisposableFunction
)

interface DisposableFunctionAddable : LifecycleObserver {
    fun add(disposableFunction: DisposableSubscription?)
}

```

<br>

- ### 정의된 Lifecycle과 해당 Lifecycle과 동기화할 Scope 객체  

```kotlin

// 해당 라이프사이클과 AutoDisposableController를 통해 bind로 들어오는 Disposable 객체를 관리
class DisposableScope(
    val lifecycle: DisposableLifecycle,
    private val autoDisposableController: AutoDisposableController
) {
    fun bind(generator: DisposableFunction) {
        autoDisposableController += DisposableSubscription(lifecycle, generator)
    }
}

operator fun DisposableScope.plusAssign(generator: DisposableFunction) = bind(generator)

```

<br>


- ### DisposableSubscription을 받아와 관리하는 AutoDisposableController 객체 정의


```kotlin

class AutoDisposableController(
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver, DisposableFunctionAddable {

    private val list: MutableList<DisposableSubscription> = mutableListOf()

    private val onPauseDisposable = CompositeDisposable()
    private val onStopDisposable = CompositeDisposable()
    private val onDestroyDisposable = CompositeDisposable()

    override fun add(disposableSubscription: DisposableSubscription?) {
        if (disposableSubscription != null){
            list.add(disposableSubscription)

            if (disposableSubscription.lifecycle.entryPoint.isRunImmediately || disposableSubscription.lifecycle.entryPoint == EntryPoint.IMMEDIATELY){
                val disposable = disposableSubscription.generator.invoke()
                when(disposableSubscription.lifecycle.endPoint){
                    EndPoint.PAUSE -> onPauseDisposable.add(disposable)
                    EndPoint.STOP -> onStopDisposable.add(disposable)
                    EndPoint.DESTROY -> onDestroyDisposable.add(disposable)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun any(owner : LifecycleOwner, event : Lifecycle.Event){
        when(event){
            Lifecycle.Event.ON_CREATE,Lifecycle.Event.ON_START,Lifecycle.Event.ON_RESUME -> {
                activate(event)
            }
            Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                deactivate(event)
            }
            Lifecycle.Event.ON_DESTROY -> {
                deactivate(event)
                onDestroyDisposable.dispose()
                onPauseDisposable.dispose()
                onStopDisposable.dispose()
                lifecycleOwner.lifecycle.removeObserver(this)
            }

        }
    }

    private fun activate(event: Lifecycle.Event) {
        list
            .filter { it.lifecycle.entryPoint.lifecycle == event }
            .forEach {
                val disposable = it.generator.invoke()
                when(it.lifecycle.endPoint){
                    EndPoint.PAUSE -> onPauseDisposable.add(disposable)
                    EndPoint.STOP -> onStopDisposable.add(disposable)
                    EndPoint.DESTROY -> onDestroyDisposable.add(disposable)
                }
            }
    }

    private fun deactivate(event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_PAUSE -> onPauseDisposable.clear()
            Lifecycle.Event.ON_STOP -> onStopDisposable.clear()
            Lifecycle.Event.ON_DESTROY -> onDestroyDisposable.clear()
        }
    }
}

```

<br>

- ###  Lifecycle 객체에 확장함수로 Scope 등록 

```kotlin

val <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseActivity<VIEW_MODEL, VIEW_BINDING>.fromImmediatelyToDestroyScope
    get() = DisposableScope(EntryPoint.CREATE at EndPoint.DESTROY,disposableController)

val <VIEW_MODEL : ViewModel,  VIEW_BINDING : ViewBinding> BaseActivity<VIEW_MODEL,VIEW_BINDING>.fromCreateToDestroyScope
    get() = DisposableScope(EntryPoint.CREATE at EndPoint.DESTROY,disposableController)

val <VIEW_MODEL : ViewModel,  VIEW_BINDING : ViewBinding> BaseActivity<VIEW_MODEL,VIEW_BINDING>.fromStartToStopScope
    get() = DisposableScope(EntryPoint.START at EndPoint.STOP,disposableController)

val <VIEW_MODEL : ViewModel,  VIEW_BINDING : ViewBinding> BaseActivity<VIEW_MODEL,VIEW_BINDING>.fromResumeToPause
    get() = DisposableScope(EntryPoint.RESUME at EndPoint.PAUSE,disposableController)

fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseActivity<VIEW_MODEL, VIEW_BINDING>.customScope(
    lifecycle: DisposableLifecycle,
) = DisposableScope(lifecycle,disposableController)
```

- ### 각 컴포넌트에서 사용

```kotlin

private fun observeEvent() {
    fromImmediatelyToDestroyScope += observeOnceUserLocation()
    fromImmediatelyToDestroyScope += observeToken()

    fromStartToStopScope += observeTopTabSelected()
    fromStartToStopScope += observeBottomTabSelected()
    fromStartToStopScope += observeFloatingButton()
    fromStartToStopScope += observeAuthButton()
    fromStartToStopScope += observeLocationText()
}

private fun customExample(){
    customScope(EntryPoint.CREATE at EndPoint.DESTROY) += { // Create에서 실행되서 Destory에서 dispose
        viewModel
            .getTokenObserve()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(testTag, "entity" + it.joinToString(","))
                val hasToken = it.isNotEmpty() && it.first().token.isNotBlank()
                viewModel.currentAuthData.value =
                    if (hasToken) viewModel.parseToken(it.first()) else null
                binding.mainBtnAuth.setImageDrawable(
                    resDrawble(
                        if (hasToken) R.drawable.user_icon_login
                        else R.drawable.user_icon
                    )
                )
            }, {
                it.printStackTrace()
            })
    }
}

```