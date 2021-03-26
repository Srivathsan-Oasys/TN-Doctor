package com.hmis_tn.doctor.component.extention

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.listener.OnClickedNo
import com.hmis_tn.doctor.component.listener.OnClickedYes


fun View.hideKeyboardOnClick() {
    setOnClickListener { hideKeyboard() }
}

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
        val currentFocus = (context as AppCompatActivity).currentFocus
        hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
        currentFocus?.clearFocus()
    }
}

fun View.showKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
        toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun TextInputEditText.setOnDoneClickListener(block: (View) -> Unit) {
    this.setOnEditorActionListener { v, keyCode, _ ->
        (keyCode == EditorInfo.IME_ACTION_DONE).also {
            block(v)
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
                this.hideSoftInputFromWindow(
                    windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
                clearFocus()
            }
        }
    }

}


fun View.show() {
    visibility = View.VISIBLE
}


fun View.hide() {
    visibility = View.GONE
}

fun View.isLoading(isLoading: Boolean) {
    visibility = if (isLoading) View.VISIBLE else View.GONE
}


fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isvisible() = visibility == View.VISIBLE

fun Context.alert(
    title: String,
    msg: String,
    yes: OnClickedYes<Unit>,
    no: OnClickedNo<Unit>,
    positiveBtnText: Int = R.string.dialog_action_yes,
    negativeBtnText: Int = R.string.dialog_action_cancel
) {
    val builderA = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
    builderA.setTitle(title)
    builderA.setMessage(msg)
    builderA.setPositiveButton(
        positiveBtnText
    ) { d, _ ->
        d?.dismiss()
        yes.invoke(Unit)
    }
    if (negativeBtnText > 0)
        builderA.setNegativeButton(
            negativeBtnText
        ) { d, _ ->
            no.invoke(Unit)
            d?.dismiss()
        }
    val alert: AlertDialog = builderA.create()
    alert.setOnShowListener {
        val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
        btnPositive.textSize = 16F
        btnPositive.typeface = ResourcesCompat.getFont(this, R.font.poppins)
        val btnNegative: Button = alert.getButton(Dialog.BUTTON_NEGATIVE)
        btnNegative.textSize = 16F
        btnNegative.typeface = ResourcesCompat.getFont(this, R.font.poppins)
    }
    alert.show()
}

fun Activity.alert(
    title: String,
    msg: String,
    yes: OnClickedYes<Unit>,
    no: OnClickedNo<Unit>,
    positiveBtnText: Int = R.string.dialog_action_yes,
    negativeBtnText: Int = R.string.dialog_action_cancel
) {
    val builderA = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
    builderA.setTitle(title)
    builderA.setMessage(msg)
    builderA.setPositiveButton(
        positiveBtnText
    ) { d, _ ->
        d?.dismiss()
        yes.invoke(Unit)
    }
    if (negativeBtnText > 0)
        builderA.setNegativeButton(
            negativeBtnText
        ) { d, _ ->
            no.invoke(Unit)
            d?.dismiss()
        }
    builderA.create()
    builderA.show()
}

fun slideDown(context: Context, v: View) {

    val a = AnimationUtils.loadAnimation(context, R.anim.slide_down)
    if (a != null) {
        a.reset()
        v.clearAnimation()
        v.startAnimation(a)
    }

}


fun isTablet(context: Context): Boolean {
    return context.resources.getBoolean(R.bool.isTablet)
}


fun Context.alert(
    title: Int,
    msg: Int,
    yes: OnClickedYes<Unit>,
    no: OnClickedNo<Unit>,
    positiveBtnText: Int = R.string.dialog_action_yes,
    negativeBtnText: Int = R.string.dialog_action_cancel
) {
    alert(this.getString(title), this.getString(msg), yes, no, positiveBtnText, negativeBtnText)
}



