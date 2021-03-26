package com.hmis_tn.doctor.component.extention

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt

fun Context.dp(value: Number): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        this.resources.displayMetrics
    ).roundToInt()

fun Context.dpF(value: Number): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        this.resources.displayMetrics
    )

fun Context.sp(value: Number): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        value.toFloat(),
        this.resources.displayMetrics
    ).roundToInt()

@JvmOverloads
fun Context.getCompatDrawable(
    @DrawableRes res: Int,
    style: Resources.Theme? = null
) = ResourcesCompat.getDrawable(this.resources, res, style)

fun Context.getCompatColor(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this, colorRes)

fun Context.getCompatColorStateList(@ColorRes colorRes: Int): ColorStateList =
    ColorStateList.valueOf(getCompatColor(colorRes))

fun Context.getCompatFont(@FontRes fontRes: Int): Typeface =
    ResourcesCompat.getFont(this, fontRes) ?: Typeface.DEFAULT

fun Context.getCompatTypefaceSpan(@FontRes fontRes: Int): TypefaceSpan =
    getCompatFont(fontRes).let {
        object : TypefaceSpan(String()) {

            override fun updateDrawState(ds: TextPaint) {
                applyCustomTypeFace(ds, it)
            }

            override fun updateMeasureState(paint: TextPaint) {
                applyCustomTypeFace(paint, it)
            }

            fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
                val fake = paint.typeface?.style ?: 0 and tf.style.inv()
                if (fake and Typeface.BOLD != 0) {
                    paint.isFakeBoldText = true
                }
                if (fake and Typeface.NORMAL != 0) {
                    paint.textSkewX = -0.25f
                }
                paint.typeface = tf
            }

        }
    }

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(res: Int) {
    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
}