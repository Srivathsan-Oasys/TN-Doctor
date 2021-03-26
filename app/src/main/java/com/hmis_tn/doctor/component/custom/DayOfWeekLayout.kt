package com.hmis_tn.doctor.component.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.getCompatFont
import com.hmis_tn.doctor.component.listener.OnDayOfWeekClickListener
import com.hmis_tn.doctor.view.dp
import com.hmis_tn.doctor.view.dpF
import com.hmis_tn.doctor.view.getCompatColor
import java.util.*
import kotlin.math.roundToInt

class DayOfWeekLayout : LinearLayout {

    private val calendar = Calendar.getInstance(Locale.getDefault())
    private var isSelectedLayoutMap = mutableMapOf<Int, Boolean>()
    var currentDateSelected = Date()
        private set
    var currentPosition: Int = 0
        private set

    val dateList = List<Date>(7) {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.add(Calendar.DAY_OF_MONTH, it)
        cal.time
    }

    private var _onDayOfWeekClickListener: OnDayOfWeekClickListener? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context) : super(context) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun init() {
        this.elevation = dpF(1)
        this.setBackgroundColor(getCompatColor(R.color.white))
        this.setPadding(dp(12), 0, dp(12), 0)
        this.isFocusable = true
        this.isClickable = true
        for (i in 1..7) {
            calendar.time = dateList[i - 1]
            addView(
                LinearLayout(context).apply {
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        dp(74),
                        1.0f
                    )
                    gravity = Gravity.CENTER_HORIZONTAL
                    orientation = VERTICAL
                    isFocusable = true
                    isClickable = true
                    // Uncomment the line below to apply a ripple effect
                    // background = createRippleDrawable()
                    addView(
                        AppCompatTextView(context).apply {
                            layoutParams = LayoutParams(dp(17), WRAP_CONTENT).also {
                                it.setMargins(
                                    0,
                                    dp(8),
                                    0,
                                    dp(1)
                                )
                            }
                            gravity = Gravity.CENTER
                            isFocusable = false
                            isClickable = false
                            isActivated = false
                            isDuplicateParentStateEnabled = false
                            text = calendar.getDisplayName(
                                Calendar.DAY_OF_WEEK,
                                Calendar.SHORT,
                                Locale.getDefault()
                            ).first().toString().capitalize()
                            textSize = 12f
                            setTextColor(getCompatColor(R.color.grey))
                            typeface = try {
                                context.getCompatFont(R.font.poppins_medium)
                            } catch (e: Exception) {
                                Typeface.DEFAULT_BOLD
                            }
                            if (i == 1) {
                                setTextColor(getCompatColor(R.color.white))
                                background = ShapeDrawable(
                                    RoundRectShape(
                                        floatArrayOf(
                                            dpF(8), dpF(8), dpF(8), dpF(8),
                                            dpF(8), dpF(8), dpF(8), dpF(8)
                                        ),
                                        null, null
                                    )
                                ).apply {
                                    this.paint.apply {
                                        color = getCompatColor(R.color.colorPrimary)
                                    }
                                }
                            }
                        }
                    )
                    addView(
                        AppCompatTextView(context).apply {
                            id = View.generateViewId()
                            isSelectedLayoutMap[id] = i == 1
                            layoutParams = LayoutParams(dp(41), dp(41))
                            gravity = Gravity.CENTER
                            isFocusable = false
                            isClickable = false
                            isActivated = false
                            isDuplicateParentStateEnabled = false
                            text = calendar.get(Calendar.DAY_OF_MONTH).toString()
                            textSize = 16f
                            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            typeface = try {
                                context.getCompatFont(R.font.poppins_medium)
                            } catch (e: Exception) {
                                Typeface.DEFAULT_BOLD
                            }
                            if (isSelectedLayoutMap[id] == true) {
                                setTextColor(getCompatColor(R.color.white))
                                background = ShapeDrawable(OvalShape()).apply {
                                    this.paint.apply {
                                        style = Paint.Style.FILL_AND_STROKE
                                        color = getCompatColor(R.color.colorPrimary)
                                    }
                                }
                            } else {
                                setTextColor(getCompatColor(R.color.grey))
                                background = ShapeDrawable(OvalShape()).apply {
                                    this.paint.apply {
                                        style = Paint.Style.FILL_AND_STROKE
                                        color = getCompatColor(R.color.colorPrimary)
                                        alpha = (255 * 0.2).roundToInt()
                                    }
                                }
                            }
                        }
                    )
                    setOnClickListener {
                        isSelectedLayoutMap = isSelectedLayoutMap.keys.associate { key ->
                            if (isSelectedLayoutMap[key] == true) (parent as LinearLayout).findViewById<AppCompatTextView>(
                                key
                            ).apply {
                                setTextColor(getCompatColor(R.color.grey))
                                background = ShapeDrawable(OvalShape()).apply {
                                    this.paint.apply {
                                        style = Paint.Style.FILL_AND_STROKE
                                        color = getCompatColor(R.color.colorPrimary)
                                        alpha = (255 * 0.2).roundToInt()
                                    }
                                }
                            }
                            key to false
                        }.toMutableMap()
                        (getChildAt(1) as AppCompatTextView).apply {
                            isSelectedLayoutMap[this.id] = true
                            setTextColor(getCompatColor(R.color.white))
                            background = ShapeDrawable(OvalShape()).apply {
                                this.paint.apply {
                                    style = Paint.Style.FILL_AND_STROKE
                                    color = getCompatColor(R.color.colorPrimary)
                                }
                            }
                        }
                        _onDayOfWeekClickListener?.invoke(dateList[i - 1].also {
                            currentDateSelected = it
                            currentPosition = i - 1
                        })
                    }
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Suppress("unused")
    private fun createRippleDrawable(): Drawable {
        return RippleDrawable(
            ColorStateList.valueOf(getCompatColor(R.color.white)),
            null,
            ShapeDrawable(
                RoundRectShape(
                    floatArrayOf(
                        dpF(8), dpF(8), dpF(8), dpF(8),
                        dpF(8), dpF(8), dpF(8), dpF(8)
                    ),
                    null, null
                )
            )
        )
    }

    fun addOnDayOfWeekClickListener(block: OnDayOfWeekClickListener) {
        _onDayOfWeekClickListener = block
    }

    companion object {
        private const val TAG: String = "DayOfWeekLayout"
    }

}