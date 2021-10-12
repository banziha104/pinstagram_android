# 클린 아키텍쳐 적용 

> 프로젝트 전반의 테스트와 확장성을 확보하기 위해 클린아키텍쳐 도입, 멀티 모듈로 구성
> 프로세스의 진행 순서는 Data -> Domain -> Presentation(app) 이나 
> 의존 관계는 Data에 Repository 구현 책임이있어 Domain -> Data -> Presentation(app) 순 

- ### Domain Layer의 모델 정의 
  
```kotlin
interface ReversedGeoModel { // 추상화된 모델 정의
    val address : String?
}
```

<br>

- ### Domain Layer의 Repository 정의 

```kotlin
interface GeometryRepository {
    fun getReverseGeocoding(latLng: String): Single<ApiModel<List<ReversedGeoModel>>>
    fun getGeocoding(address: String): Single<ApiModel<GeoModel>>
}
```

<br>


- ### Data Layer의 Entity 정의 

```kotlin

data class ReversedGeoResponse(/*...*/)

data class AddressComponentsItem(

    @field:SerializedName("types")
    val types: List<String?>? = null,

    @field:SerializedName("short_name")
    val shortName: String? = null,

    @field:SerializedName("long_name")
    val longName: String? = null
) : ReversedGeoModel { // 필요한 부분만 사용하기 위해, Model 을 추상화하고, 해당 하는 엔티티에 구현
    override val address: String?
        get() = longName
}

data class ResultsItem(/*...*/)

data class PlusCode(/*...*/)
```

<br>

- ### Data Layer의 Source 정의 

```kotlin

// Retrofit2 서비스
interface GeometryService {
    @GET("geometry/")
    fun getReverseGeocoding(
        @Query("latlng") latLng : String,
    ) : Single<ApiResponse<ReversedGeoResponse>>

    @GET("geometry/geo")
    fun getGeocoding(
        @Query("address") address : String,
    ) : Single<ApiResponse<GeoResponse>>
}

```

<br>


- ### Data Layer 에서 Repository 구현 

```kotlin
class GeometryRepositoryImpl(
    private val service: GeometryService
) : GeometryRepository {
    override fun getReverseGeocoding(latLng: String): Single<ApiModel<List<ReversedGeoModel>>> =
        service
            .getReverseGeocoding(latLng)
            .map { response ->
                val data = response.data?.results?.get(0)?.addressComponents
                    ?: listOf()
                response.copyWith<List<ReversedGeoModel>>(data.mapNotNull { it })
            }.map(ApiMapper::toModel)


    override fun getGeocoding(address: String): Single<ApiModel<GeoModel>> =
        service.getGeocoding(address).map(ApiMapper::toModel)
}
```

<br>


- ### Data Layer에서 Presentation Layer에서 사용할 UseCase 정의 및 비즈니스 로직 구현 

```kotlin

@Singleton
class RequestReversedGeoCodeUseCase @Inject constructor(
    private val repository: GeometryRepository
) {
    private val regex = arrayOf(Regex("^*[시군]$"), Regex("^*[읍면구]$"), Regex("^*[동리]$"))
    
    fun execute(lat: Double, lng: Double): Single<String?> =
        repository
            .getReverseGeocoding("$lat,$lng")
            .subscribeOn(Schedulers.io())
            .map<String?> { response ->
                val data = response.data
                if (response.isOk && data != null && data.isNotEmpty()) {
                    data
                        .mapNotNull { it.address }
                        .filter { address ->
                            regex.any { regex -> regex.find(address) != null }
                        }
                        .sortedWith { a, b ->
                            regex.indexOfFirst { it.find(a) != null } - regex.indexOfFirst { it.find(b) != null }
                        }
                        .joinToString(" ")
                } else {
                    null
                }
            }
}

```

<br>

- ### Hilt를 이용해 ViewModel에 의존성 주입

```kotlin

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    /*...*/
    val requestReversedGeoCodeUseCase: RequestReversedGeoCodeUseCase
) : AndroidViewModel(application) {
    /*...*/
}
```

<br>

- ### Presentation Layer의 View에서 사용 

```kotlin
 viewModel
        .requestReversedGeoCodeUseCase
        .execute(it.latitude, it.longitude)
        .observeOn(AndroidSchedulers.mainThread())
        .disposeByOnDestory(this)
        .subscribe({ address ->
            if (address != null) {
                binding.mainTxtAddress.text = address
            } else {
                resString(R.string.main_fail_address)
            }
        }, {
            binding.mainTxtAddress.text = resString(R.string.main_fail_address)
            it.printStackTrace()
        })
```


## 테스트 

- ### Source 테스트 

```kotlin
@RunWith(AndroidJUnit4::class)
class GeoServiceTests : BaseServiceTest() {
    private lateinit var geoService: GeometryService

    @Before
    fun `셋업`() {
        geoService = ApiBase().generateService(
            GeometryService::class.java,
            okHttpClient,
            callAdapter,
            converterFactory
        )
    }

    @Test
    @Throws(Exception::class)
    fun `리버스_지오코딩_테스트`() {
        geoService
            .getReverseGeocoding("37.389019,127.122628")
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertComplete()
            .assertValue {
                val result = it.data?.results
                if (!it.isOk || result == null){
                    false
                }else{
                    val validation = result[0]?.addressComponents?.any { item ->
                        val isContain = item?.longName?.contains("서현")
                        isContain != null && isContain
                    }
                    it.isOk && result != null && result.isNotEmpty() && validation != null && validation
                }
            }
    }
}
```

<br>

- ### Repository 구현체 테스트 

```kotlin

@RunWith(AndroidJUnit4::class)
class GeometryRepositoryTests : RepositoryTests(){

    private val geometryRepository: GeometryRepository = GeometryRepositoryImpl(generateService())

    @Test
    fun `리버스지오코딩_테스트`(){
        geometryRepository
            .getReverseGeocoding("37.3889890630627,127.122531112568")
            .test()
            .await()
            .assertValue {
                it.isOk && it.data != null && it.data!!.isNotEmpty()
            }
    }
}
```

<br>


- ### UseCase 테스트 

```kotlin

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [TestConfig.SDK_VERSION])
class RequestReversedGeoCodeUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestReversedGeoCodeUseCase : RequestReversedGeoCodeUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
        requestReversedGeoCodeUseCase
            .execute(37.389019,127.122628)
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertValue {
                it != null && it.isNotBlank() && "서현" in it
            }
    }
}


```

<br>


