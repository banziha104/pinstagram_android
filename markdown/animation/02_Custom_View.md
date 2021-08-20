# Custom View

> 특정 뷰가 반복이되거나, 아니면 확장을 해야하는 경우 Custom View를 사용 

<br>

- ### customui모듈의 **ValidateEditText**
    - EditText의 경우, 현재 텍스트가 원하는 형식을 갖추고 있는지 체크해야하는 경우가 많음(예: 이메일 형식, 특수문자 포함등)
    - 문제는 각 EditText 별로 원하는 룰이 다름
    - 추가로 프로젝트가 Rx기반이기 때문에 룰 체크를 Rx객체를 이용하고자함
    - 이를 위해 Material Design의 TextInputLayer를 확장한 클래스 생성
    
<br>

- ### ValidateEditText 클래스 정의 

```kotlin

class ValidateEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {
    val editText: TextInputEditText
    /*...*/
}

```

<br>

- ### attrs.xml에 어트리뷰트셋 명시

```xml
<!-- customui/values/attrs.xml -->

<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="UserInteractionControllableMapView">
        <attr name="canUserInteraction" format="boolean"/>
    </declare-styleable>

    <declare-styleable name="ValidateEditText">
        <attr name="isPasswordType" format="boolean"/>
    </declare-styleable>
</resources>
```

<br>

- ### attrs 파싱 및 초기화 

```kotlin

typealias IsValidated = Boolean
typealias ErrorMessage = String

class ValidateEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {
    val editText: TextInputEditText
    private val isPasswordType: Boolean
    private val originHint: String

    init {
        setWillNotDraw(false)

        // 아래 obtainStyledAttributes을 통해 xml에서 명시한 어트리뷰트셋을 가져옮
        context.theme.obtainStyledAttributes( 
            attrs,
            R.styleable.ValidateEditText,
            defStyleAttr,
            0
        ).let { typedArray ->
            originHint = typedArray.getString(R.styleable.TextInputLayout_android_hint)
                ?: throw RuntimeException("ValidateEditText에 Hint가 지정되지 않았습니다.")
            isPasswordType =
                typedArray.getBoolean(R.styleable.ValidateEditText_isPasswordType, false)
        }

        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        editText = TextInputEditText(getContext())
        editText.setPadding(60, 20, 10, 10)
        editText.layoutParams = layoutParams
        if (isPasswordType) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            endIconMode = END_ICON_PASSWORD_TOGGLE
        }
        addView(editText)
        
        // 현재 포커스 상태, 룰이 지켜졌는지 아닌지 등을 통해 TextInputLayer 색상 변경 
        editText.focusChanges().observeOn(AndroidSchedulers.mainThread()).subscribe { focused ->
            val color = ContextCompat.getColor(
                context,
                if (focused) R.color.borderClick else R.color.borderOrg
            )
            boxStrokeColor = color
            defaultHintTextColor = ColorStateList.valueOf(color)
            if (!focused) hint = originHint
        }
    }
}

/*...*/

```

<br>

- ### 사용하는 객체로 부터 룰을 받아와 Validation을 진행하는 함수 생성  


```kotlin
class ValidateEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {
    fun getText(): String = editText.text.toString()

    /*...*/
    
    fun bindRule(rules: List<ValidateRule>): Observable<Pair<IsValidated, ErrorMessage?>> =
        editText
            .textChanges()
            .skipInitialValue()
            .map {
                val validation = rules.firstOrNull { rule -> !rule.predicate(it.toString()) }
                val isValidated: IsValidated = validation == null
                val errorMessage = validation?.errorMessage
                val color = (ContextCompat.getColor(
                    context,
                    if (isValidated) R.color.borderSuccess else R.color.borderFail
                ))
                hint = if (isValidated) originHint else errorMessage
                boxStrokeColor = color
                defaultHintTextColor = ColorStateList.valueOf(color)

                (validation == null) to errorMessage
            }
}

data class ValidateRule(
    val errorMessage: String,
    val predicate: (String) -> Boolean
)

```

<br>

- ### ValidateEditText를 사용하고자하는 곳에 XML 명시

```xml
    <com.lyj.customui.dialog.edittext.ValidateEditText
        android:id="@+id/signInEditEmail"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="0dp"
        android:layout_height="@dimen/sign_validate_text_height"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:hint="@string/sign_email_text"
        app:layout_constraintBottom_toTopOf="@+id/signInEditPassword"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_chainStyle="spread_inside"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/constraintLayout" />
```

<br>

- ### 사용하고자하는 곳에서 bindRule()함수 호출

```kotlin
// SignInFragment.kt
Observable.combineLatest(
            binding.signInEditEmail.bindRule(viewModel.emailRule),
            binding.signInEditPassword.bindRule(viewModel.passwordRule)
        ) { email, password -> email to password }
            .subscribe({ (email, password) ->
                binding.signInBtnSend.isEnabled = email.first && password.first
            }, {
                it.printStackTrace()
            })

// SignInViewModel.kt

val emailRule: List<ValidateRule> = listOf(
    ValidateRule(resString(R.string.validate_message_empty)) { it.isNotBlank() },
    ValidateRule(resString(R.string.validate_message_length)) { it.length >= 4 },
    ValidateRule(resString(R.string.validate_message_email)) { '@' in it }
)


```