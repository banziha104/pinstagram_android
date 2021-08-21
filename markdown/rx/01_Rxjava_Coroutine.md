# ReactiveX와 Coroutine 비교 

> Contents api에서 일부는 Coroutine으로, 일부는 ReactiveX로 생성후 코드비교 

- ### 정의 

```kotlin
interface ContentsService {

    // 코루틴으로 받는 API, suspend로 설정  
    @GET("contents/{id}")
    suspend fun getById( 
        @Path("id") id: Long
    ): ApiResponse<ContentsRetrieveResponse>

    // ReactiveX로 받는 API, Single로 설정
    @POST("contents/")
    fun createContents(
        @Header("Authorization") authorization : String,
        @Body request: ContentsCreateRequest
    ) : Single<ApiResponse<Long>>
}
```

<br>

- ### 코루틴 사용 

```kotlin

// DetailViewModel.kt
fun requestContents(id: Long, block: (ApiResponse<ContentsRetrieveResponse>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = contentsService.getById(id)
            block(response)
        }
    }

// DetailActivity.kt
private fun bindData(id: Long?) {
    viewModel.requestContents(id ?: return makeWarningToast()) { response ->
        lifecycleScope.launch(Dispatchers.Main) {

            hideProgressLayout()

            if (!response.isOk || response.data == null) {
                makeWarningToast()
                return@launch
            }

            setUpRecyclerView(response.data!!)
        }
    }
}
```

<br>

- ### ReactiveX사용 

```kotlin


// WriteDialogViewModel.kt

fun requestCreateContents(token: String, createRequest: ContentsCreateRequest): Single<ApiResponse<Long>> =
    contentsService.createContents(token, createRequest).subscribeOn(Schedulers.io())

// WriteDialog.kt
viewModel.requestCreateContents(
    token,
    ContentsCreateRequest(
        binding.writeTxtTitle.getText(),
        binding.writeTxtDescription.getText(),
        url,
        resString(viewModel.spinnerItems[binding.writeSpinner.selectedItemPosition].origin),
        lat,
        lng
    )
)
```

<br>

- ### Test

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
    fun `위도_경도_조회`(){ // ReactiveX 테스트
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