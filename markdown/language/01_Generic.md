# Generic 

- ### Generic 정의 부분 (예 : BaseActivity)

```kotlin
abstract class BaseActivity< 
        VIEW_MODEL : ViewModel, // ViewModel의 하위타입 명시 
        VIEW_BINDING : ViewBinding> // ViewBinding 하위 타입명시
        (private val layoutId : Int,
          private val factory : (LayoutInflater) -> VIEW_BINDING // inflate 하는 함수는 각 타입별로 상이함으로 해당 클래스에서 고차함수로 넘겨줌
) : AppCompatActivity() {

    protected val disposables  by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected val binding: VIEW_BINDING by lazy { factory(layoutInflater) } // infalate 함수를 통한 지연 초기화
    protected abstract val viewModel: VIEW_MODEL // KTX의 viewModels()는 명확한 타입을 받아야하고, reified가 아니어서 런타임에는 알수 없음으로 abstract로 해당 클래스에서 구현하도록 명시
    
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

<br>

- ### Generic 사용부분 : Splash Activity

```kotlin
package com.lyj.pinstagram.view.splash


@AndroidEntryPoint
class SplashActivity : BaseActivity<
        SplashViewModel, // ViewModel의 하위 타입인 SplashViewModel 
        ActivitySplashBinding// ViewBinding의 하위 타입인 ActivitySplashBinding 
        >(R.layout.activity_splash,{ ActivitySplashBinding.inflate(it) }) {

    override val viewModel : SplashViewModel by viewModels() // abstract 변수 구현

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDisposables += observePermission(viewModel.checkAndRequestPermission(this))
    }
    /*...*/

}
```

- ### 공변성 ( 아직 반공변성을 사용할만한 상황이 나오지않아서 공변만 있습니다. )
    - Class< DetailParsedType.StringParsedType>과 Class <DetailParsedType.IntParsedType>타입은 무공변성의 Class<DetailParsedType> 타입의 하위 형이 아님
    - 하위 타입 관계를 유지해주기 위해서는 공변성이 필요.
    
```kotlin

enum class DetailItemType(
    val title: Int?,
    val viewType: DetailAdapterViewType,
    val parsedType: Class<out DetailParsedType> // out 키워드로 공변성임을 명시 
) {
    IMAGE(
        null,
        DetailAdapterViewType.IMAGE,
        DetailParsedType.StringParsedType::class.java // 하위 타입 관계 유지
    ),
    /*...*/
    TAG(
        R.string.detail_item_tag,
        DetailAdapterViewType.TEXT,
        DetailParsedType.IntParsedType::class.java // 하위 타입 관계 유지
    ),
    /*...*/
    ;
 
    @Throws(RuntimeException::class)
    inline fun <reified CASTED_CLASS : DetailParsedType> parseData(
        data: ContentsRetrieveResponse,
    ): CASTED_CLASS {
        if (this.parsedType != CASTED_CLASS::class.java) throw RuntimeException("지원하지 않는 타입")
        return when (this) {
            IMAGE -> DetailParsedType.StringParsedType(data.picture)
            TITLE -> DetailParsedType.StringParsedType(data.title)
            DESCRIPTION -> DetailParsedType.StringParsedType(data.description)
            TAG -> DetailParsedType.IntParsedType(data.tag.kor)
            FULL_ADDRESS -> DetailParsedType.StringParsedType(data.fullAddress)
            MAP -> DetailParsedType.LatLngParsedType(LatLng(data.lat, data.lng))
        } as CASTED_CLASS
    }
}

// 조회된 데이터에서 파싱될 데이터 타입(String, Int, LatLng)을 정의
sealed class DetailParsedType {
    class StringParsedType(val item: String) : DetailParsedType()
    class IntParsedType(val item: Int) : DetailParsedType()
    class LatLngParsedType(val item: LatLng) : DetailParsedType()
}
```