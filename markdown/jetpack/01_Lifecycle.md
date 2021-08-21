# Lifecycle 

- ### MapLifeCycle : GoogleMap의 라이프 사이클을 별도로 구현한 클래스, 
    - MapView 각 라이프사이클에 맞춰서 MapView의는 라이프사이클 메소드 호출을 해주어야함.
    - Activity 등에서 해줘도 되지만 불필요하게 onResume(), onStop()등을 MapView만을 위해 상속받아야함
    - 그래서 별도의 LifeCycle 정의

```kotlin
class MapLifeCycle (
    lifecycle: Lifecycle,
    private val map : MapView) : LifecycleObserver{

    init{
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        map.onCreate(null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        map.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        map.onPause()
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        map.onDestroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        map.onStop()
    }
}
```

<br>

- ### AutoActivatedDisposable: ReactiveX의 Disposable 객체를 Lifecycle에 맞춰서 처리
    - 컴포넌트가 활성화 되면 실행하고, 컴포넌트가 비활성화 되면 정지하는 라이프사이클

```kotlin

interface DisposableFunctionAddable : LifecycleObserver{
    fun add(disposable: DisposableFunction?)
}

// 라이프사이클 오너에 등록하는 익스텐션
class AutoActivatedDisposable(
        private val lifecycleOwner: LifecycleOwner
)
    : LifecycleObserver, DisposableFunctionAddable {

    private val list : MutableList<DisposableFunction> = mutableListOf()
    private val compositionDisposable = CompositeDisposable()
    override fun add(disposable: DisposableFunction?) {
        if (disposable != null) list.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activate() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) // Resume이 되면 생 
    fun resume(){
        list.map {
            it.invoke()
        }.forEach { compositionDisposable.add(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP) // Stop이 되면 clear
    fun deactivate() {
        compositionDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)  // Destory이 되면 dispose
    fun detachSelf() {
        compositionDisposable.dispose()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}

```

<br> 

- ### SocketLifeCycle : 소켓이 액티비티가 종료될떄마다 제거되도록 하는 라이프사이클 객체 

```kotlin
class SocketLifeCycle(
    lifecycle: Lifecycle,
    private val socketContract: SocketContract
) : LifecycleObserver {

    init{
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        socketContract.disconnect()
    }
}
```
