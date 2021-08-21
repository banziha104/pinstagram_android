# Disposable 객체 관리

> RxLifeCycle을 도입할 예정, 현 버전까지는 아래와 같이 구현되어있음.

- ### Disposable 관리 인터페이스 정의 ( 예 : AuthActivatedDisposable 객체 )

```kotlin
typealias DisposableFunction = () -> Disposable?

interface DisposableFunctionAddable : LifecycleObserver {
    fun add(disposable: DisposableFunction?)
}
```

<br>

- ### 연산자 오버라이딩으로 조금 더 직관적으로 표시

```kotlin
operator fun DisposableAddable.plusAssign(disposable: Disposable?) = this.add(disposable)

operator fun DisposableFunctionAddable.plusAssign(disposable: DisposableFunction) =
    this.add(disposable)
```

<br>

- ### Disposable 관리 객체 생성
    - AutoActivatedDisposable : lifecycle에 맞춰 Disposable을 resume에 생성 또는 재생성, pause에 stop
    - AutoClearedDisposable : onStop 또는 onDestory에 맞춰 disposable을 dispose하는 객체

```kotlin
class AutoActivatedDisposable(
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver, DisposableFunctionAddable {

    private val list: MutableList<DisposableFunction> = mutableListOf()
    private val compositionDisposable = CompositeDisposable()
    override fun add(disposable: DisposableFunction?) {
        if (disposable != null) list.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) // Resume때 생성
    fun resume() {
        list.map {
            it.invoke()
        }.forEach { compositionDisposable.add(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP) // Stop때 clear
    fun deactivate() {
        compositionDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)  // Distory에서 dispose
    fun detachSelf() {
        compositionDisposable.dispose()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}

```

<br>

- ### Base Class 에서 정의

```kotlin
abstract class BaseActivity<VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding>(
    private val layoutId: Int,
    private val factory: (LayoutInflater) -> VIEW_BINDING
) : AppCompatActivity() {

    protected val disposables by lazy { AutoClearedDisposable(this) } // Disposable 관리 객체 정의
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) } // Disposable 관리 객체 정의
    protected val binding: VIEW_BINDING by lazy { factory(layoutInflater) }
    protected abstract val viewModel: VIEW_MODEL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindLifecycle()
    }

    private fun bindLifecycle() {
        lifecycle += disposables
        lifecycle += viewDisposables
    }

}
```

<br>

- ### 각 컴포넌트에서 사용

```kotlin

private fun observeEvent() {
    viewDisposables += observeTopTabSelected()
    viewDisposables += observeBottomTabSelected()
    viewDisposables += observeOnceUserLocation()
    viewDisposables += observeFloatingButton()
    viewDisposables += observeAuthButton()
    viewDisposables += observeToken()
    viewDisposables += observeLocationText()
}

```