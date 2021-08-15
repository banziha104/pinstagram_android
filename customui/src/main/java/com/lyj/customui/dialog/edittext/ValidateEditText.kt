package com.lyj.customui.dialog.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.textChanges
import com.lyj.customui.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.lang.RuntimeException

typealias IsValidated = Boolean
typealias ErrorMessage = String

class ValidateEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {
    val editText: TextInputEditText
    private val isPasswordType : Boolean
    private val originHint : String
    init {
        setWillNotDraw(false)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ValidateEditText,
            defStyleAttr,
            0
        ).let { typedArray ->
            originHint = typedArray.getString(R.styleable.TextInputLayout_android_hint) ?: throw RuntimeException("ValidateEditText에 Hint가 지정되지 않았습니다.")
            isPasswordType = typedArray.getBoolean(R.styleable.ValidateEditText_isPasswordType,false)
        }

        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        editText = TextInputEditText(getContext())
        editText.setPadding(60, 20, 10, 10)
        editText.layoutParams = layoutParams
        if (isPasswordType){
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            endIconMode =  END_ICON_PASSWORD_TOGGLE
        }
        addView(editText)

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

    fun getText(): String? = editText.text?.toString()

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

class ValidationFailedException(message: String) : RuntimeException("${message}의 형식이 맞지 않습니다")