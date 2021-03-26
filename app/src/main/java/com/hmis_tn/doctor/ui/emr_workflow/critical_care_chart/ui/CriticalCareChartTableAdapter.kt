package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Build
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.CriticalCareChart
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.D
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.ObservedValue
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.item_critical_care_chart_table.view.*

class CriticalCareChartTableAdapter(
    private val cccList: ArrayList<CriticalCareChart>,
    private val observedValueList: ArrayList<ObservedValue>,
    private val modifyClick: (List<D>) -> Unit
) :
    RecyclerView.Adapter<CriticalCareChartTableAdapter.MyViewHolder>() {

    private var utils: Utils? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_critical_care_chart_table, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return observedValueList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.llRow.removeAllViews()
        val observedValue = observedValueList[position]
        utils = Utils(holder.itemView.context)
        var layoutPopulated: Boolean
        val typefacePoppins = ResourcesCompat.getFont(holder.itemView.context, R.font.poppins)

        holder.itemView.llRow.addView(
            drawSeparator(
                holder.itemView.context,
                (utils?.convertDpToPixel(2f, holder.itemView.context))?.toInt() ?: 2,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        holder.itemView.llRow.addView(
            drawCheckBox(
                holder.itemView.context,
                (utils?.convertDpToPixel(100f, holder.itemView.context))?.toInt() ?: 100,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ""
            )
        )
        holder.itemView.llRow.addView(
            drawSeparator(
                holder.itemView.context,
                (utils?.convertDpToPixel(2f, holder.itemView.context))?.toInt() ?: 2,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val list = observedValue.dList


        var obj: String = ""


        for (i in cccList.indices) {
            layoutPopulated = false
            for (j in list?.indices!!) {
//                if (cccList[i].code == list[j].ccc_code) {    //correct method but duplicate values when two fields of same name
                if (cccList[i].code == list[j].ccc_code) {

                    if (list[j].ccc_code == "term base") {

                        obj += list[j].observed_value
                    } else {

                        obj = ""
                    }

                    val text = list[j].observed_value
//                        when {
//                            list[j].ventilator_observed_value != null -> list[j].ventilator_observed_value
//                            list[j].abg_observed_value != null -> list[j].abg_observed_value
//                            list[j].monitor_observed_value != null -> list[j].monitor_observed_value
//                            list[j].intake_observed_value != null -> list[j].intake_observed_value
//                            list[j].bp_observed_value != null -> list[j].bp_observed_value
//                            list[j].diabetes_observed_value != null -> list[j].diabetes_observed_value
//                            list[j].dialysis_observed_value != null -> list[j].dialysis_observed_value
//                            else -> ""
//                        }


                    if (j != list.size - 1) {
                        if (cccList[i].code != list[j + 1].ccc_code) {


                            if (list[j].ccc_code == "term base") {

                                obj += list[j].observed_value

                                holder.itemView.llRow.addView(
                                    drawTextView(
                                        holder.itemView.context,
                                        (utils?.convertDpToPixel(
                                            100f,
                                            holder.itemView.context
                                        ))?.toInt()
                                            ?: 100,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        obj,
                                        typefacePoppins!!
                                    )
                                )

                                layoutPopulated = true


                            } else {


                                holder.itemView.llRow.addView(
                                    drawTextView(
                                        holder.itemView.context,
                                        (utils?.convertDpToPixel(
                                            100f,
                                            holder.itemView.context
                                        ))?.toInt()
                                            ?: 100,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        text ?: "",
                                        typefacePoppins!!
                                    )
                                )

                                layoutPopulated = true

                            }
                        }
                    } else {

                        if (list[j].ccc_code == "term base") {

                            obj += list[j].observed_value

                            holder.itemView.llRow.addView(
                                drawTextView(
                                    holder.itemView.context,
                                    (utils?.convertDpToPixel(
                                        100f,
                                        holder.itemView.context
                                    ))?.toInt()
                                        ?: 100,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    obj,
                                    typefacePoppins!!
                                )
                            )

                            layoutPopulated = true


                        } else {
                            holder.itemView.llRow.addView(
                                drawTextView(
                                    holder.itemView.context,
                                    (utils?.convertDpToPixel(
                                        100f,
                                        holder.itemView.context
                                    ))?.toInt()
                                        ?: 100,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    text ?: "",
                                    typefacePoppins!!
                                )
                            )
                            layoutPopulated = true
                        }
                    }
                }
            }
            if (!layoutPopulated) {
                holder.itemView.llRow.addView(
                    drawTextView(
                        holder.itemView.context,
                        (utils?.convertDpToPixel(100f, holder.itemView.context))?.toInt()
                            ?: 100,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        "",
                        typefacePoppins!!
                    )
                )
            }

            holder.itemView.llRow.addView(
                drawSeparator(
                    holder.itemView.context,
                    (utils?.convertDpToPixel(2f, holder.itemView.context))?.toInt() ?: 2,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
        holder.itemView.llRow.addView(
            drawImageView(
                holder.itemView.context,
                (utils?.convertDpToPixel(50f, holder.itemView.context))?.toInt() ?: 50,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                R.drawable.ic_edit_blue,
                observedValue.dList!!
            )
        )
        holder.itemView.llRow.addView(
            drawSeparator(
                holder.itemView.context,
                (utils?.convertDpToPixel(2f, holder.itemView.context))?.toInt() ?: 2,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

    }

    private fun drawLinearLayout(
        context: Context, width: Int, height: Int, orientation: Int
    ): LinearLayout {
        val ll = LinearLayout(context)
        ll.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        ll.orientation = orientation
        return ll
    }

    private fun drawCheckBox(
        context: Context, width: Int, height: Int, text: String
    ): CheckBox {
        val chk = CheckBox(context)
        chk.layoutParams =
            ViewGroup.LayoutParams(
                width,
                height
            )
        chk.gravity = Gravity.CENTER
        chk.text = text
        return chk
    }

    private fun drawSeparator(
        context: Context, width: Int, height: Int
    ): View {
        val viewDivider = View(context)
        viewDivider.layoutParams =
            ViewGroup.LayoutParams(
                width,
                height
            )
        viewDivider.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.view
            )
        )
        return viewDivider
    }

    private fun drawTextView(
        context: Context, width: Int, height: Int, text: String, typeface: Typeface
    ): TextView {
        val tv = TextView(context)
        tv.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        tv.gravity = Gravity.CENTER
        tv.text = text
        tv.typeface = typeface
        return tv
    }

    private fun drawImageView(
        context: Context, width: Int, height: Int, src: Int, dList: List<D>
    ): ImageView {
        val imageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(
            (utils?.convertDpToPixel(width.toFloat(), context))?.toInt() ?: width,
            height
        )
        imageView.setPadding(
            utils?.convertDpToPixel(6f, context)?.toInt() ?: 6,
            utils?.convertDpToPixel(6f, context)?.toInt() ?: 6,
            utils?.convertDpToPixel(6f, context)?.toInt() ?: 6,
            utils?.convertDpToPixel(6f, context)?.toInt() ?: 6
        )
        imageView.setImageResource(src)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        imageView.setOnClickListener { v ->
            modifyClick(dList)
        }
        return imageView
    }

    private fun drawEditText(
        context: Context, width: Int, height: Int, typeface: Typeface
    ): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.typeface = typeface
        editText.maxLines = 1
        return editText
    }

    private fun drawNumericEditText(
        context: Context, width: Int, height: Int, typeface: Typeface
    ): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        editText.typeface = typeface
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.maxLines = 1
        return editText
    }

    private fun drawSwitch(
        context: Context, width: Int, height: Int
    ): Switch {
        val switch = Switch(context)
        switch.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        return switch
    }

    private fun drawDate(
        context: Context, width: Int, height: Int, typeface: Typeface
    ): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.typeface = typeface
        editText.maxLines = 1
        return editText
    }

    private fun drawEmail(
        context: Context, width: Int, height: Int, typeface: Typeface
    ): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        editText.typeface = typeface
        editText.maxLines = 1
        return editText
    }

    private fun drawNotes(
        context: Context, width: Int, height: Int, typeface: Typeface
    ): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.typeface = typeface
        return editText
    }

    private fun drawRating(
        context: Context, width: Int, height: Int
    ): RatingBar {
        val ratingBar = RatingBar(context)
        ratingBar.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        return ratingBar
    }

    private fun drawSpinner(
        context: Context, width: Int, height: Int
    ): Spinner {
        val spinner = Spinner(context)
        spinner.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        return spinner
    }
}
