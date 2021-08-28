# Flow

- ### 이벤트를 정보를 가져오는 서비스와 suspend 함수 정의 

```kotlin
interface EventService {
    @GET("event/{id}")
    suspend fun getById(
        @Path("id") id : Long
    ) : ApiResponse<EventRetreiveResponse>

    @GET("event/")
    suspend fun getByLocation(
        @Query("latlng") latlng : String,
        @Query("limit") limit : Int = 1000
    ) : ApiResponse<List<EventRetreiveResponse>>
}
```

<br>

- ### map을 통한 흐름 변환 


```kotlin
@HiltViewModel
class EventFragmentViewModel @Inject constructor(
    private val eventService: EventService
) : ViewModel() {

    internal fun getEventState(
        lat: Double,
        lng: Double
    ): Flow<EventFragmentState> =
        flow {
            emit(eventService.getByLocation("$lat,$lng"))
        }.map {
            if (!it.isOk){
                EventFragmentState.Fail
            }else{
                if (it.data != null && it.data!!.isNotEmpty()){
                    EventFragmentState.Success(it.data!!)
                }else{
                    EventFragmentState.Empty
                }
            }

        }.catch {
            it.printStackTrace()
        }

}
```

<br>

- ### Compose에 State로 전달 

```kotlin
@AndroidEntryPoint
class EventFragment :
    BaseFragment<EventFragmentViewModel, EventFramgmentBinding>(R.layout.event_framgment,
        { inflater, view -> EventFramgmentBinding.inflate(inflater) }) {
    /*...*/

    @ExperimentalAnimationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.currentLocation.observe(viewLifecycleOwner) {
            binding.root.setContent {
                View(
                    requireContext(),
                    stateFlow = viewModel.getEventState(it.latitude, it.longitude) // 해당부분에서 전달 
                )
            }
        }
    }
}
```

```kotlin
@ExperimentalAnimationApi
@Composable
internal fun EventFragment.View(context: Context, stateFlow: Flow<EventFragmentState>) {

    val eventState: State<EventFragmentState> =
        stateFlow.collectAsState(initial = EventFragmentState.Init) // collectAsState로 State로 변경
    var value = eventState.value

    when (value) {
        is EventFragmentState.Init -> EventInitPage(context).View(value)
        is EventFragmentState.Empty -> EventEmptyPage(context).View(value)
        is EventFragmentState.Fail -> EventErrorPage(context).View(value)
        is EventFragmentState.Success -> EventMainPage(context).View(value)
    }
}
```