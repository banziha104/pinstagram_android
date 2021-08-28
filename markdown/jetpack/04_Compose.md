# Compose

> 패턴은 아직 표준이 없어서, 개인적으로 선호하는 패턴으로 제작하였습니다.


<br>

- ### State 정의 

```kotlin
sealed interface EventFragmentState {
    object Init : EventFragmentState

    class Success(val response: List<EventRetreiveResponse>) : EventFragmentState

    object Fail : EventFragmentState

    object Empty : EventFragmentState
}
```

<br>

- ### Flow 흐름에서 State로 변경 

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

- ### Layout 에서 상태에 맞게 분기 

```kotlin
@ExperimentalAnimationApi
@Composable
internal fun EventFragment.View(context: Context, stateFlow: Flow<EventFragmentState>) {

    val eventState: State<EventFragmentState> =
        stateFlow.collectAsState(initial = EventFragmentState.Init)
    var value = eventState.value

    when (value) {
        is EventFragmentState.Init -> EventInitPage(context).View(value)
        is EventFragmentState.Empty -> EventEmptyPage(context).View(value)
        is EventFragmentState.Fail -> EventErrorPage(context).View(value) 
        is EventFragmentState.Success -> EventMainPage(context).View(value) // EventFragmentState.Success로 암시적 캐스팅 
    }
}

sealed class EventFragmentLayout<T : EventFragmentState>(override val context: Context) : EventLayoutContract<T>
interface EventLayoutContract<T : EventFragmentState> : ComposeLayoutContract<T>
```

<br> 

- ### Compose 뷰 구현 

```kotlin
class EventMainPage(context: Context) : EventFragmentLayout<EventFragmentState.Success>(context) { // EventState가 Success일 떄로 지정.

    @Composable
    @ExperimentalAnimationApi
    override fun View(state: EventFragmentState.Success) {
        var expaneded: Int? by remember { mutableStateOf(null) }
        val bottomNavigationPadding = 56.dp

        Surface(color = Color(0xFFE0E0E0)) { // 배경 화면 변경
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp), // 리스트 간 간격 
                contentPadding = PaddingValues(8.dp), // 콘텐츠의 패딩
                modifier = Modifier.padding(PaddingValues(bottom = bottomNavigationPadding)), // Main의 bottomNavigation만큼 띄어줌
                ) {
                items(state.response.mapIndexed { index, eventRetrieveResponse -> index to eventRetrieveResponse }) { pair -> // 리스트 처리
                    EventCardView(
                        pair = pair,
                        expaneded != null && expaneded == pair.first // 현재 확장해야할 아이템이 있고, 그 아이템이 자신의 인덱스와 같은 경우
                    ) {
                        expaneded = if (it == expaneded) null else it // 클릭된 이벤트가 현재 클릭된 이벤트면 수축, 아닌 경우에는 확장할 인덱스 저장 
                    }
                }
            }

        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun EventCardView(
        pair: Pair<Int, EventRetreiveResponse>,
        isExpanded: Boolean,
        setExpandedIndex: (Int) -> Unit
    ) {
        val (index, response) = pair
        val image = rememberImagePainter(response.picture)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isExpanded) 400.dp else 300.dp) // 확장되었을때 400dp 아닐때 300dp
                .clickable { setExpandedIndex(index) }
        ) {
            Column {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                AnimatedVisibility(isExpanded, modifier = Modifier.height(100.dp)) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(
                            PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        )
                    ) {
                        Text(
                            text = response.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = response.description,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = response.fullAddress,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
```