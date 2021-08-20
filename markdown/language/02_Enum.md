# Enum

- ### 인터페이스 상속
    - kotlin의 enum은 abstract메소드를 정의 및 구현하거나 interface를 상속할 수 있음.
    - 구현은 각 enum의 요소별로, 또는 공통으로 할 수 있음

```kotlin
// MainViewModel.kt

enum class MainTabType(
    @StringRes val titleId: Int // Enum 변수
) : MainTabContract {
    HOME(R.string.main_tap_home_title) { // 개별 구현
        override fun getFragment(): Fragment = HomeFragment()
    },
    MAP(R.string.main_tap_map_title) { // 개별 구현
        override fun getFragment(): Fragment = MapFragment()
    },
    TALK(R.string.main_tap_talk_title) { // 개별 구현
        override fun getFragment(): Fragment = TalkFragment()
    };
    // override fun getFragment(): Fragment = Fragment() 통합 구현
}

interface MainTabContract { // 인터페이스 정의
    fun getFragment(): Fragment 
}
```

<br>

- ### Companion Object 사용 
    - 일반 클래스들과 마찬가지로 Companion Object를 사용할 수 있음. 
    - 아래는 문자열을 인자로 받아 context로 string.xml에서 문자열을 받아와 비교하는 함수 
    
```kotlin
enum class ContentsTagType(
    val origin : Int,
    val kor : Int
) {
    @SerializedName("FOOD")
    FOOD(R.string.food_origin,R.string.food_kor),
    @SerializedName("SHOP")
    SHOP(R.string.shop_origin,R.string.shop_kor),
    @SerializedName("PLACE")
    PLACE(R.string.place_origin,R.string.place_kor),
    @SerializedName("SERVICE")
    SERVICE(R.string.service_origin,R.string.service_kor),
    @SerializedName("ETC")
    ETC(R.string.etc_origin,R.string.etc_kor);

    companion object{
        private var stringResourceMap : MutableMap<String, Int>? = null // 이미 계산된 내용을 다시 계산하지 않도록 Map으로 처리
        
        fun findByKoreanTitle(context: Context, kor: String) : ContentsTagType?{ // 해당되는 문자열과 일치하는 요소를 반환하는 클래스 
            if (stringResourceMap == null){
                val map = mutableMapOf<String,Int>()
                values().forEach {
                    map[context.getString(it.kor)] = it.kor
                }
                stringResourceMap = map
            }
            return values().firstOrNull { it.kor == stringResourceMap?.get(kor) }
        }
    }
}
```

