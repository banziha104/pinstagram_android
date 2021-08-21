# View Model

- ### BaseActivity에 ViewModel 정의 

```kotlin

abstract class BaseActivity< 
        VIEW_MODEL : ViewModel,  // ViewModel 타입 정의 
        VIEW_BINDING : ViewBinding>(private val layoutId : Int, private val factory : (LayoutInflater) -> VIEW_BINDING) : AppCompatActivity() {

    protected val disposables  by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected val binding: VIEW_BINDING by lazy { factory(layoutInflater) }
    protected abstract val viewModel: VIEW_MODEL // ViewModel 변수 정의 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindLifecycle()
    }

    private fun bindLifecycle(){
        lifecycle += disposables
        lifecycle += viewDisposables
    }

}
```

- ### ViewModel 정의

<br>

```kotlin
@HiltViewModel // Hilt ViewModel로 컴포넌트에서 의존성 주입 
class MainActivityViewModel @Inject constructor(
    application: Application,
    geoCodeManager: GeoCodeManager,
    val locationEventManager: LocationEventManager,
    val contentsService: ContentsService,
    val storageUploader: StorageUploader,
    private val geometrySerivce: GeometrySerivce,
    private val database: LocalDatabase,
    private val jwtManager: JwtManager,
) : AndroidViewModel(application),
    ReverseGeoCoder by geoCodeManager {

    val originContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy { // LiveData
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy {
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentAuthData: MutableLiveData<JwtAuthData?> by lazy {
        MutableLiveData<JwtAuthData?>()
    }

    val currentLocation: MutableLiveData<LatLng> by lazy {
        MutableLiveData<LatLng>()
    }

    val currentLocationName : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    /*...*/

    fun getUserToken(): Single<List<TokenEntity>> =
        database.tokenDao().findToken().subscribeOn(Schedulers.io())

    fun getTokenObserve(): Flowable<List<TokenEntity>> =
        database.tokenDao().observeToken().subscribeOn(Schedulers.io())

    fun deleteToken(): Completable =
        database.tokenDao().delete(TOKEN_ID).subscribeOn(Schedulers.io())
}
```

<br>

- ### 사용처에서 KTX-viewModel의 viewModels() 함수 호출로 의존성 주입 

```kotlin
@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main,
        { ActivityMainBinding.inflate(it) }),
    TalkSendContact,
    ProgressController,
    RequestChangeCurrentLocation {

    @Inject
    internal lateinit var permissionManager: PermissionManager

    override val viewModel: MainActivityViewModel by viewModels() // 해당 부분에서 주입 
    
    /*...*/
}
```