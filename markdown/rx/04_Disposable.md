# Disposable 객체 관리

> Disposable 관리 방법, 아래 두가지 방법을 구현 

1. ### Transfomer 객체를 구현, 이를 compose() 메소드에 전달하고 onComplete를 호출하는 방법
2. ### CompositeDisposable 객체를 사용해  disposable 객체를 dispose 하는 방법

## Transformer 구현해 메소드에 전달, onComplete() 를 호출하도록 Disposable을 관리하는 방법 

- ### Transformer 정의 
  
```kotlin
class LifecycleTransformer<T>(
    private val lifecycleEvent: Lifecycle.Event,
    lifecycleObserver: Observable<Lifecycle.Event>
) : ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    SingleTransformer<T, T>,
    MaybeTransformer<T, T>,
    CompletableTransformer {

    private val observable = lifecycleObserver.filter { it == lifecycleEvent }

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream.takeUntil(observable)

    override fun apply(upstream: Flowable<T>): Publisher<T> =
        upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST))

    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream.takeUntil(observable.firstOrError())

    override fun apply(upstream: Maybe<T>): MaybeSource<T> =
        upstream.takeUntil(observable.firstElement())

    override fun apply(upstream: Completable): CompletableSource =
        upstream.ambWith(observable.concatMapCompletable { Completable.complete() })
}
```

<br>

- ### Lifecycle을 관찰하고 이를 처리할 RxLifecycleObserver 객체 정의 

```kotlin
interface DisposableLifecycleController : LifecycleOwner {
    val disposableLifecycleObserver: RxLifecycleObserver
    
    /*...*/
    
    fun <T> disposeByOnStop(): LifecycleTransformer<T> =
        disposableLifecycleObserver.transformer(disposeBy = Lifecycle.Event.ON_STOP)

    fun <T> disposeByOnPause(): LifecycleTransformer<T> =
        disposableLifecycleObserver.transformer(disposeBy = Lifecycle.Event.ON_PAUSE)

    fun <T> disposeByOnDestroy(): LifecycleTransformer<T> =
        disposableLifecycleObserver.transformer(disposeBy = Lifecycle.Event.ON_DESTROY)
}

class RxLifecycleObserver(
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver {
    
    /*...*/
    
    private val publisher: BehaviorSubject<Lifecycle.Event> = BehaviorSubject.create()

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }
    
    /*...*/

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun any(owner: LifecycleOwner, event: Lifecycle.Event) {
        publisher.onNext(event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            publisher.onComplete()
            lifecycleOwner.lifecycle.removeObserver(this)
        }
        /*...*/
    }
    fun <T> transformer(disposeBy: Lifecycle.Event) =
        LifecycleTransformer<T>(disposeBy, publisher)
}
```

<br>

- ### 확장함수를 통해 compose 호출 및 transfomer 전달 

```kotlin
fun <T> Single<T>.disposeByOnStop(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnStop())

fun <T> Single<T>.disposeByOnPause(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnPause())

fun <T> Single<T>.disposeByOnDestory(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnDestroy())
```


<br>

- ### 사용할 View에서 DisposableLifecycleController를 구현 및 확장함수 호출

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    TalkSendContact,
    ProgressController,
    RequestChangeCurrentLocation,
    DisposableLifecycleController // DisposableLifecycleController 구현
{

    override val disposableLifecycleObserver: RxLifecycleObserver = RxLifecycleObserver(this) // DisposableLifecycleController 에 명시된 RxLifecycleObserver 변수 생성
    
    private fun observeToken() {
        viewModel
            .observeTokenUseCase
            .execute()
            .observeOn(AndroidSchedulers.mainThread())
            .disposeByOnDestory(this) // onDestory 호출시 onComplete 가 호출 
            .subscribe({
                Log.d(testTag, "entity" + it.joinToString(","))
                val hasToken = it.isNotEmpty() && it.first().token.isNotBlank()
                viewModel.currentAuthData.value =
                    if (hasToken) viewModel.parseJwtUseCase.execute(it.first().token) else null
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


<br>

## CompositeDisposable 객체를 사용해  disposable 객체를 dispose 하는 방법

- ### Lifecycle을 관찰하고 CompositeDisposable을 관리할 객체를 정의

```kotlin

interface DisposableLifecycleController : LifecycleOwner {
    val disposableLifecycleObserver: RxLifecycleObserver

    fun add(disposable: Disposable, lifecycleEvent: Lifecycle.Event) =
        disposableLifecycleObserver.add(disposable, lifecycleEvent)
    /*...*/
}

class RxLifecycleObserver(
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver {

    private val onPauseDisposable = CompositeDisposable()
    private val onStopDisposable = CompositeDisposable()
    private val onDestroyDisposable = CompositeDisposable()
    
    /*...*/
    
    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun add(disposable: Disposable, lifecycleEvent: Lifecycle.Event) {
        when (lifecycleEvent) {
            Lifecycle.Event.ON_PAUSE -> onPauseDisposable.add(disposable)
            Lifecycle.Event.ON_STOP -> onStopDisposable.add(disposable)
            Lifecycle.Event.ON_DESTROY -> onDestroyDisposable.add(disposable)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun any(owner: LifecycleOwner, event: Lifecycle.Event) {
        /*...*/
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                onPauseDisposable.clear()
            }
            Lifecycle.Event.ON_STOP -> {
                onStopDisposable.clear()
            }
            Lifecycle.Event.ON_DESTROY -> {
                onDestroyDisposable.dispose()
                onPauseDisposable.dispose()
                onStopDisposable.dispose()
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }
    }
}
```

<br>

- ### Disposable 객체에 확장함수 명시 

```kotlin
fun Disposable.disposedBy(
    lifecycleController: DisposableLifecycleController,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) = lifecycleController.add(this, lifecycleEvent)
```

<br>

- ### 사용할 View에서 DisposableLifecycleController를 구현 및 확장함수 호출

```kotlin
@AndroidEntryPoint
class SignInFragment ():
    Fragment(),
    ProgressController,
    DisposableLifecycleController // DisposableLifecycleController 구현
{
    override val disposableLifecycleObserver: RxLifecycleObserver = RxLifecycleObserver(this) // DisposableLifecycleController 에 명시된 RxLifecycleObserver 변수 생성
    
    /*...*/
    private fun bindBtnSend() {
        binding.signInBtnSend
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .doOnNext { showProgressLayout() }
            .flatMapSingle {
                /*...*/
            }.flatMap {
                /*...*/
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                /*...*/
            }, {
                makeToast(R.string.sign_up_request_fail, true)
                hideProgressLayout()
                it.printStackTrace()
            })
            .disposedBy(this) // 확장함수로 Disposable 객체 전달 및 관리
    }
}
```

