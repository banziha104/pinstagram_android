# Socket

- ### Lifecycle에 맞춰 소켓이 disconnect할수 있도록 SocketLifeCycle 정의

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
        socketContract.disconnect() // DESTROY 이벤트 발생시 제거
    }
}
```

- ### Socket 과 관련된 인터페이스 정의 

```kotlin

interface SocketContract {
    fun connect(): Completable // 연결
    fun sendMessage(talkMessage: TalkMessage) // 메시지 전송 
    fun disconnect(): Completable // 연결 해제
    fun getSayObserver(): Observable<TalkMessage> // 메세지 관찰
}
```

<br>

- ### SocketContract 인터페이스를 구현한 관리 객체 정의 

```kotlin

class SocketManager(
    lifecycle: Lifecycle?,
) : SocketContract{

    private val sayObserver = BehaviorSubject.create<TalkMessage>()

    private val gson : Gson by lazy { Gson() }
    private val socket : Socket by lazy { // 소켓 연결 정의
        val opts = IO.Options.builder() 
            .setPath("/talk/socket")
            .setTransports(arrayOf(WebSocket.NAME))
            .build()
        IO.socket(URI.create("https://www.coguri.shop"), opts) 
    }

    init {
        if(lifecycle != null) SocketLifeCycle(lifecycle,this) // Lifecycle이 있으면 SocketLifeCycle에 등록
    }

    override fun connect(): Completable = Completable.create {  emitter ->
        socket.on("say") {
            sayObserver.onNext(gson.fromJson(it[0].toString(),TalkMessage::class.java))
        }
        socket.on(Socket.EVENT_CONNECT) {
            emitter.onComplete()
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            emitter.onError(RuntimeException(it.joinToString(",")))
        }
        socket.connect()
    }

    override fun sendMessage(talkMessage: TalkMessage) {
        socket.emit("say",gson.toJson(talkMessage))
    }

    override fun disconnect(): Completable = Completable.create{ emitter ->
        val socket = socket.close()
        if (socket.connected()){
            emitter.onError(RuntimeException("Connection is alive"))
        }else{
            emitter.onComplete()
        }
    }

    override fun getSayObserver(): Observable<TalkMessage> = sayObserver

     class Factory{ // 사용하는 곳에 Lifecycle을 받아와야하기 때문에 SocketManger를 직접 제공하는 것이아닌 Factory로 정의 
        fun createSocket(lifecycle: Lifecycle?) : SocketContract =  SocketManager(lifecycle)
    }
}

```

<br>

- ### 소켓 테스트 

```kotlin

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SocketTests {
    private val socketManager = SocketManager(null)

    @Before
    fun `00_셋업`(){

    }

    @Test
    fun `01_연결`(){
        socketManager
            .connect()
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `02_연결_해제`(){
        socketManager
            .disconnect()
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
    }

    @After
    fun `03_종료`(){
        socketManager.disconnect().subscribe{}
    }
}
```
