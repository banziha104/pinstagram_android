# 계측 테스트 

> Roboletics 를 활용해 Unit Test 단으로 내릴 수 있지만, 이번 프로젝트에서는 AndroidTest에서 진행 <br>
> ViewModel Layer 테스트는 기술 부채로 달아놓고 추후에 진행 

- ### Database

```kotlin

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 이름 순서대로 테스트 진행 
class RoomTests {
    private lateinit var tokenDao: TokenDao
    private lateinit var database: LocalDatabase
    private val compositeDisposable = CompositeDisposable()

    @Before
    fun `00_테스트_셋업`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,LocalDatabase::class.java
        ).build()
        tokenDao = database.tokenDao()
    }

    @Test
    @Throws(Exception::class)
    fun `01_토큰_생성`(){
        val token = "testToken"
        val request = TokenEntity(1L,token)
        compositeDisposable += tokenDao
            .refreshToken(request)
            .subscribe {  }

        tokenDao
            .findToken()
            .test()
            .assertComplete()
            .assertValue{
                it.size == 1 && it[0].id == 1L && it[0].token == "testToken"
            }
    }

    @Test
    @Throws(Exception::class)
    fun `02_토큰_삭제`(){
        compositeDisposable += tokenDao
            .delete()
            .subscribe {  }

        tokenDao
            .findToken()
            .test()
            .assertComplete()
            .assertValue{
                it.isEmpty()
            }
    }

    @After
    fun `99_테스트_완료처리`() {
        database.close()
        compositeDisposable.clear()
    }
}
```

<br>

- ### Api Test

```kotlin

@RunWith(AndroidJUnit4::class)
class AuthServiceTests : BaseServiceTest() {
    private lateinit var authService : AuthenticationService

    @Before
    fun `셋업`(){
        authService = ApiBase().generateService(AuthenticationService::class.java,okHttpClient,callAdapter,converterFactory)
    }

    @Test
    @Throws(Exception::class)
    fun `로그인`(){
        val request = SignInRequest("test@test.com","test")
        authService
            .signIn(request)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data?.token != null
            }
            .assertComplete()
    }
}
```

<br>

- ### Socket Test

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