package com.lyj.customui.dialog.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.view.focusChanges
import com.lyj.customui.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.lang.RuntimeException

class SendableEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr){

    private val editText: TextInputEditText
    init {
        setWillNotDraw(false)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        editText = TextInputEditText(getContext())
        editText.setPadding(60, 20, 10, 10)
        editText.layoutParams = layoutParams
        endIconMode =  END_ICON_CUSTOM
        endIconDrawable = ContextCompat.getDrawable(context,R.drawable.send)
        addView(editText)

        editText.focusChanges().observeOn(AndroidSchedulers.mainThread()).subscribe { focused ->
            val color = ContextCompat.getColor(
                context,
                if (focused) R.color.borderClick else R.color.borderOrg
            )
            boxStrokeColor = color
            defaultHintTextColor = ColorStateList.valueOf(color)
        }
    }

    fun getText(): String? = editText.text?.toString()
}