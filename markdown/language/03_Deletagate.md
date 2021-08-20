# 위임 패턴

> by lazy, by observable 등 변수의 위임이 아닌 클래스 위임 패턴  <br>
> 아래의 상황말고 클래스의 구현을 다른 클래스에 위임하는 대표적인 패턴 등 다양하지만 , 현재는 버전까지는 아래 한가지만 구현되어있음.
 
- ### 해당 프로젝트에서 위임을 사용하는 경우는 다음 한가지
    - 인자로 받을 객체가 다양한 객체를 상속 및 구현하고 있고 
    - 사용하는 객체에서는 그 중 일부만 필요할때
    
- ### LocationManager는 두가지 인터페이스를 구현함
    - OnceLocationGetter : 유저 위치 정보를 요청시 한번만 받아옮 
    - ContinuousLocationGetter : 유저 위치 정보를 지속적으로 받아옮

```kotlin

interface OnceLocationGetter { // 유저 위치 정보를 한번만 가져옮
    fun getUserLocationOnce(activity: Activity): Single<Location>?
}

interface ContinuousLocationGetter { // 유저 위치 정보를 지속적으로 트래킹함
    fun getLocationObserver(activity: Activity): Observable<Location>?
    fun stopUpdateLocation()
}

class LocationEventManager(
    context: Context,
    private val permissionChecker: PermissionChecker
) : OnceLocationGetter, ContinuousLocationGetter {
    /*...*/
}

```

- ### 그러나 WriteDialog에서는 위치 정보는 한번만 요청하면 되기떄문에 위임으로 일부분만 가져옮 

```kotlin

// WriteDialogViewModel.kt
@HiltViewModel
class WriteDialogViewModel @Inject constructor(
    application: Application,
    private val contentsService: ContentsService,
    locationProvider: LocationEventManager,
    private val localDatabase: LocalDatabase,
    private val storageUploader: StorageUploader,
) : AndroidViewModel(application),
    SizeMeasurable,
    OnceLocationGetter by locationProvider { // LocationManager의 OnceLocationGetter인터페이스만 가져옮 
        
        /*...*/
}


// WriteDialog.kt
viewModel
    .getUserLocationOnce(requireActivity()) // ViewModel이 OnceLocationProvider를 가지고 있기 때문에 getUserLocationOnce 사용가능
    ?.subscribe({
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentLocation, 16.5f
            )
        )
    }, {
        it.printStackTrace()
    })

```

