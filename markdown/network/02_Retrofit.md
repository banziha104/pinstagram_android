# Retrofit & OKHttp & JWT

<br>

- ### JWT 관리 객체 정의  : jwt 라이브러리가 자바의 Intenger.class를 사용함으로 자바파일로 정의

```java
/**
 * id 파싱시 java Integer 클래스를 요구
 * Kotlin Class 에서 Java Class로 변경
 */
public class JwtManager {
    private static final byte[] SECRET_KEY = "pinstagramsecret".getBytes();

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }

    public JwtAuthData parseJwt(String token){
        Claims claims = getClaims(token);
        return new JwtAuthData(
                claims.get("id",Integer.class),
                claims.get("name",String.class),
                claims.get("email",String.class)
        );
    }
}

```

<br> 

- ### 서비스 정의 

```kotlin
interface ContentsService {

    @GET("contents/{id}") // DetailActivity에서 사용되는 메소드로, 코루틴을 사용해보고자 해당 메소드만 코루틴으로 구현
    suspend fun getById(
        @Path("id") id : Long
    ) : ApiResponse<ContentsRetrieveResponse>

    @GET("contents/")
    fun getByLocation(
        @Query("latlng") latlng : String,
        @Query("limit") limit : Int = 1000
    ) : Single<ApiResponse<List<ContentsRetrieveResponse>>>

    @POST("contents/")
    fun createContents(
        @Header("Authorization") authorization : String,
        @Body request: ContentsCreateRequest
    ) : Single<ApiResponse<Long>>
}


```

- ### ServiceGenerator 인터페이스 및 이를 구현한 ApiBase 생성 

```kotlin

class ApiBase : ServiceGenerator {
    companion object {
        const val ADDRESS_URL =  "https://www.coguri.shop/geometry/address/index.html"
    }
    private val baseUrl = "https://www.coguri.shop"

    override fun <T> generateService(
        service: Class<T>,
        client: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory
    ): T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(callAdapter)
        .addConverterFactory(converter)
        .build()
        .create(service)
}

interface ServiceGenerator {
    fun <T> generateService(
        service: Class<T>,
        client: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory
    ) : T
}

```

<br>

- ### Hilt 모듈에 등록 

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava3CallAdapterFactory.create() // CallAdapter

    @Provides
    @Singleton
    fun providerConvertFactory(): Converter.Factory = GsonConverterFactory // Converter
        .create()

    @Provides
    @Singleton
    fun providerOkHttpClient(): OkHttpClient = OkHttpClient.Builder().let { // Client
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        it
            .addInterceptor(logger)
            .connectTimeout(20, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideServiceGenerator(): ServiceGenerator = ApiBase() // ServiceGenerator
    
    @Provides
    @Singleton
    fun provideContentsApi( // Hilt 합성 
        serviceGenerator: ServiceGenerator,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory,
        client: OkHttpClient
    ): ContentsService = serviceGenerator.generateService(
        ContentsService::class.java,
        client,
        callAdapter,
        converter
    )


    /*...*/
}
```

<br>

- ### 테스트 

```kotlin

// 별도의 테스트서버를 둘 예산이 없어서 생성, 수정, 삭제는 테스트 생략
@RunWith(AndroidJUnit4::class)
class ContentsServiceTests : BaseServiceTest() {
    private lateinit var contentsService : ContentsService

    @Before
    fun `셋업`(){
        contentsService = ApiBase().generateService(ContentsService::class.java,okHttpClient,callAdapter,converterFactory)
    }

    @Test
    @Throws(Exception::class)
    fun `ID_조회`() =  runBlocking { // 코루틴 테스트 
        val result = contentsService
            .getById(2)
        assert(result.isOk)
        assert(result.data != null)
    }

    @Test
    @Throws(Exception::class) 
    fun `위도_경도_조회`(){ // Rx 테스트
        contentsService 
            .getByLocation("37.389019, 127.122628",1000)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data != null && it.data?.isNotEmpty() == true
            }
            .assertComplete()
    }

}
```