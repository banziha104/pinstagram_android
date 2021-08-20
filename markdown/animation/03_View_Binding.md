# View Binding

> 4.1에서 제거된 Kotlin Android Extensions를 대체 하기 위해 사용한 라이브러리

<br>

- ### ViewBinding 사용 명시 

```groovy
// app/build.gradle
android {
    /*...*/
    viewBinding {
        enabled true
    }
}
```

<br>

- ### BaseActivity 와 BaseFragment에서 ViewBinding 타입 추상화 및 인플레이팅 고차함수 명시

```kotlin
abstract class BaseActivity< 
        VIEW_MODEL : ViewModel, 
        VIEW_BINDING : ViewBinding> // ViewBinding Class 타입 추상화 
         (private val layoutId : Int, 
          private val factory : (LayoutInflater) -> VIEW_BINDING) // High Order Funtion으로 inflate 함수를 받음
    : AppCompatActivity() {

    protected val disposables  by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected val binding: VIEW_BINDING by lazy { factory(layoutInflater) } // 지연 초기화로 inflate 실행
    protected abstract val viewModel: VIEW_MODEL

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

- ### ViewBinding 객체 생성 및 사용 

```kotlin

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main,
        { ActivityMainBinding.inflate(it) }), // inflate 함수 전달 
    TalkSendContact,
    ProgressController,
    RequestChangeCurrentLocation {

    /*...*/

    private fun observeFloatingButton(): DisposableFunction = {
        binding // viewBinding 객체 사용
            .mainBtnFloating
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val auth = viewModel.currentAuthData.value
                val latLng = viewModel.currentLocation.value
                if (auth != null && latLng != null) {
                    WriteDialog(latLng).show(supportFragmentManager, null)
                } else {
                    Toast.makeText(this, R.string.main_needs_auth, Toast.LENGTH_LONG).show()
                }
            }, {
                it.printStackTrace()
            })
    }
    /*...*/
}
```