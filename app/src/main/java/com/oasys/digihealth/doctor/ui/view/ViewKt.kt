package com.oasys.digihealth.doctor.view

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import com.oasys.digihealth.doctor.component.extention.*

/** Return the dp value in pixels (Int) */
fun View.dp(value: Number): Int =
    context.dp(value)

/** Return the dp value in pixels (Float) */
fun View.dpF(value: Number): Float =
    context.dpF(value)

/** Return a color from resource */
fun View.getCompatColor(@ColorRes res: Int): Int =
    context.getCompatColor(res)

/** Return a ColorStateList from resource */
fun View.getCompatColorStateList(@ColorRes res: Int): ColorStateList =
    ColorStateList.valueOf(context.getCompatColor(res))

/** Return a Font from resource */
fun View.getCompatFont(@FontRes res: Int): Typeface =
    context.getCompatFont(res)


/** Return the sp value in pixels (Int) */
fun View.sp(value: Number): Int =
    context.sp(value)

/** Return a Drawable from resource */
fun View.getCompatDrawable(@DrawableRes res: Int, theme: Resources.Theme? = null): Drawable =
    context.getCompatDrawable(res, theme) ?: throw IllegalStateException("Drawable is null")

fun View.handleNoResults(hideView: View, count: Int) {
    if (count > 0) {
        this.visibility = View.GONE
        hideView.visibility = View.VISIBLE
    } else {
        this.visibility = View.VISIBLE
        hideView.visibility = View.GONE
    }
}

fun View.backGroundShape(
    roundRectShapeRadius: Int = 0,
    shapeDrawableRadius: Int = 0,
    backgroundRippleColor: Int,
    backgroundShapeColor: Int,
    isDrawable: Boolean = false
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.background = RippleDrawable(
            ColorStateList.valueOf(getCompatColor(backgroundRippleColor)),
            if (isDrawable)
                ContextCompat.getDrawable(context, backgroundShapeColor)
            else
                ShapeDrawable(
                    RoundRectShape(
                        FloatArray(8) { dpF(roundRectShapeRadius) },
                        null,
                        null
                    )
                ).apply {
                    setTint(getCompatColor(backgroundShapeColor))
                },
            ShapeDrawable(RoundRectShape(FloatArray(8) { dpF(shapeDrawableRadius) }, null, null))
        )
    }
}
