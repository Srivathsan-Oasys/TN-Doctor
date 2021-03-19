package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui

import android.app.DatePickerDialog
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.CriticalCareChart
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.item_critical_care_chart_form.view.*
import java.util.*

class CriticalCareChartFormAdapter(
    private val criticalCareChartChildFragment: CriticalCareChartChildFragment,
    private val list: ArrayList<CriticalCareChart>,
    private val isEdit: Boolean
) : RecyclerView.Adapter<CriticalCareChartFormAdapter.MyViewHolder>() {

    private var utils: Utils? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_critical_care_chart_form, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val criticalCareChart = list[position]

        utils = Utils(holder.itemView.context)

        populateMandatoryQuestion(
            holder.itemView.tvFormQuestion,
            criticalCareChart.name ?: ""
        )

        criticalCareChart.uuid?.let {
            criticalCareChartChildFragment.mandatoryQuestionsMap[it] = false
        }

        when (criticalCareChart.critical_care_concepts?.value_type_uuid) {
            1 -> populateEditText(holder, criticalCareChart)
            2 -> populateNumericEditText(holder, criticalCareChart)
            3 -> populateCheckBox(holder, criticalCareChart)
            4 -> populateSwitch(holder, criticalCareChart)
            5 -> populateCheckBox(holder, criticalCareChart)
            6 -> populateDate(holder, criticalCareChart)
            7 -> populateEmail(holder, criticalCareChart)
            8 -> populateNotes(holder, criticalCareChart)
            9 -> populateRating(holder, criticalCareChart)
            10 -> populateCombo(holder, criticalCareChart)
            11 -> populateDropDown(holder, criticalCareChart)
        }
    }


    private fun populateEditText(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val editText = EditText(holder.itemView.context)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.inputType = InputType.TYPE_CLASS_TEXT
        if (isEdit) {
            editText.setText(criticalCareChart.answer)

            criticalCareChart.uuid?.let {
                if (editText.text.isNotEmpty()) {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true

                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        editText.text.toString(),
                        false
                    )
                }
            }
        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                } else {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                }
                criticalCareChart.uuid?.let {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] =
                        s?.isNotEmpty() == true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        holder.itemView.llRow.addView(editText)
    }

    private fun populateNumericEditText(
        holder: MyViewHolder,
        criticalCareChart: CriticalCareChart
    ) {
        val editText = EditText(holder.itemView.context)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        if (isEdit) {
            editText.setText(criticalCareChart.answer)
            criticalCareChart.uuid?.let {
                if (editText.text.isNotEmpty()) {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true

                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        editText.text.toString(),
                        false
                    )
                }
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                } else {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                }
                criticalCareChart.uuid?.let {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] =
                        s?.isNotEmpty() == true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        holder.itemView.llRow.addView(editText)
    }

    private fun populateCheckBox(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val checkBox = CheckBox(holder.itemView.context)

        checkBox.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        criticalCareChart.uuid?.let {
            criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true
        }

        for (i in criticalCareChart.critical_care_concepts!!.critical_care_concept_values!!.indices) {


            criticalCareChartChildFragment.observedDataForCreateReqCheckBoxPayload(
                criticalCareChart,
                i,
                0,
                false
            )
            criticalCareChartChildFragment.observedDataForUpdateReqCheckBoxPayload(
                criticalCareChart,
                i,
                0,
                false
            )

            val checkBoxadd = CheckBox(holder.itemView.context)

            checkBoxadd.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            checkBoxadd.text =
                criticalCareChart.critical_care_concepts!!.critical_care_concept_values?.get(i)?.concept_value

            checkBoxadd.setOnCheckedChangeListener { buttonView, isChecked ->

                Log.e("clicked", i.toString() + isChecked.toString() + checkBoxadd.text.toString())

                criticalCareChartChildFragment.observedDataForCreateReqCheckBoxPayload(
                    criticalCareChart,
                    i,
                    if (isChecked) 1 else 0,
                    false
                )
                criticalCareChartChildFragment.observedDataForUpdateReqCheckBoxPayload(
                    criticalCareChart,
                    i,
                    if (isChecked) 1 else 0,
                    false
                )
            }

            holder.itemView.llRow.addView(checkBoxadd)
        }

/*        if (isEdit) {
            val answer = try {
                (criticalCareChart.answer ?: "0").toFloat()
            } catch (e: Exception) {
                0f
            }
            checkBox.isChecked = answer == 1f
        }*/
/*
        criticalCareChartChildFragment.observedDataForCreateReqPayload(
            criticalCareChart,
            "false",
            false
        )
        criticalCareChartChildFragment.observedDataForUpdateReqPayload(
            criticalCareChart,
            criticalCareChart.answer ?: "false",
            false
        )*/
        criticalCareChart.uuid?.let {
            criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true
        }

        /*checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            criticalCareChartChildFragment.observedDataForCreateReqPayload(
                criticalCareChart,
                isChecked.toString(),
                false
            )
            criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                criticalCareChart,
                isChecked.toString(),
                false
            )
        }
*/

    }

    private fun populateSwitch(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val ll = LinearLayout(holder.itemView.context)
        ll.orientation = LinearLayout.HORIZONTAL

        val tv = TextView(holder.itemView.context)
        tv.text = "InActive"
        ll.addView(tv)

        val switch = Switch(holder.itemView.context)
        switch.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (isEdit)
            switch.isChecked = criticalCareChart.answer?.toFloat() == 1f
        ll.addView(switch)

        val tv1 = TextView(holder.itemView.context)
        tv1.text = "Active"
        ll.addView(tv1)

        criticalCareChartChildFragment.observedDataForCreateReqPayload(
            criticalCareChart,
            "false",
            false
        )
        criticalCareChartChildFragment.observedDataForUpdateReqPayload(
            criticalCareChart,
            criticalCareChart.answer ?: "false",
            false
        )

        criticalCareChart.uuid?.let {
            criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true
        }

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            criticalCareChartChildFragment.observedDataForCreateReqPayload(
                criticalCareChart,
                isChecked.toString(),
                false
            )
            criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                criticalCareChart,
                isChecked.toString(),
                false
            )
        }

        holder.itemView.llRow.addView(ll)
    }

    private fun populateDate(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val editText = EditText(holder.itemView.context)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.isFocusable = false
        editText.isClickable = false
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0)
        if (isEdit) {
            editText.setText(criticalCareChart.answer)

            criticalCareChart.uuid?.let {
                if (editText.text.isNotEmpty()) {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true

                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        editText.text.toString(),
                        false
                    )
                }
            }
        }

        editText.setOnClickListener {
            val cal = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                holder.itemView.context,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    editText.setText("$dayOfMonth - ${month + 1} - $year")

                    val observedValue = utils?.convertDateFormat(
                        editText.text.toString(),
                        "dd - MM - yyyy",
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    )

                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        observedValue ?: "",
                        false
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        observedValue ?: "",
                        false
                    )
                    criticalCareChart.uuid?.let {
                        criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true
                    }
                }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.show()
        }

        holder.itemView.llRow.addView(editText)
    }

    private fun populateEmail(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val editText = EditText(holder.itemView.context)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        if (isEdit) {
            editText.setText(criticalCareChart.answer)

            criticalCareChart.uuid?.let {
                if (editText.text.isNotEmpty()) {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true

                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        editText.text.toString(),
                        false
                    )
                }
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                } else {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                }
                criticalCareChart.uuid?.let {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] =
                        s?.isNotEmpty() == true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        holder.itemView.llRow.addView(editText)
    }

    private fun populateNotes(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val editText = EditText(holder.itemView.context)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        if (isEdit) {
            editText.setText(criticalCareChart.answer)

            criticalCareChart.uuid?.let {
                if (editText.text.isNotEmpty()) {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true

                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        editText.text.toString(),
                        false
                    )
                }
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                } else {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                }
                criticalCareChart.uuid?.let {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] =
                        s?.isNotEmpty() == true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        holder.itemView.llRow.addView(editText)
    }

    private fun populateRating(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val ratingBar = RatingBar(holder.itemView.context)
        ratingBar.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (isEdit)
            ratingBar.progress = criticalCareChart.answer?.toInt() ?: 0

        criticalCareChartChildFragment.observedDataForCreateReqPayload(
            criticalCareChart,
            0,
            false
        )
        criticalCareChartChildFragment.observedDataForUpdateReqPayload(
            criticalCareChart,
            criticalCareChart.answer ?: 0,
            false
        )
        criticalCareChart.uuid?.let {
            criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true
        }
        ratingBar.setOnRatingBarChangeListener { ratingBar1, rating, fromUser ->
            criticalCareChartChildFragment.observedDataForCreateReqPayload(
                criticalCareChart,
                rating,
                false
            )
            criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                criticalCareChart,
                rating,
                false
            )
        }

        holder.itemView.llRow.addView(ratingBar)
    }

    private fun populateCombo(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {
        val editText = EditText(holder.itemView.context)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.inputType = InputType.TYPE_CLASS_TEXT
        if (isEdit) {
            editText.setText(criticalCareChart.answer)

            criticalCareChart.uuid?.let {
                if (editText.text.isNotEmpty()) {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true

                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        editText.text.toString(),
                        false
                    )
                }
            }
        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        false
                    )
                } else {
                    criticalCareChartChildFragment.observedDataForCreateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                    criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                        criticalCareChart,
                        s.toString(),
                        true
                    )
                }
                criticalCareChart.uuid?.let {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] =
                        s?.isNotEmpty() == true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        holder.itemView.llRow.addView(editText)
    }

    private fun populateDropDown(holder: MyViewHolder, criticalCareChart: CriticalCareChart) {

        val dropDown = Spinner(holder.itemView.context)

        dropDown.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT

        )


        val instructionMap = mutableMapOf<Int?, String?>()

        instructionMap.clear()

        instructionMap.put(0, "")

        val data = criticalCareChart.critical_care_concepts!!.critical_care_concept_values

        instructionMap.putAll(data!!.map { it.uuid to it.concept_value }.toMap().toMutableMap())

        val routeAdapter = ArrayAdapter<String>(
            holder.itemView.context,
            R.layout.spinner_item,
            instructionMap.values.toMutableList()
        )
        routeAdapter.setDropDownViewResource(R.layout.spinner_item)
        dropDown.adapter = routeAdapter

        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()

                val send = instructionMap.filterValues { it == itemValue }.keys.toList()[0]

                criticalCareChart.uuid?.let {
                    criticalCareChartChildFragment.mandatoryQuestionsMap[it] = true
                }

                criticalCareChartChildFragment.observedDataForCreateReqPayload(
                    criticalCareChart,
                    send!!,
                    false
                )
                criticalCareChartChildFragment.observedDataForUpdateReqPayload(
                    criticalCareChart,
                    send,
                    false
                )

                Log.i("listdata", itemValue + send.toString())

            }
        }


        holder.itemView.llRow.addView(dropDown)

    }


    private fun populateMandatoryQuestion(textView: TextView, name: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(
                "<font>$name</font><font color=\"#FF0000\">*</font>",
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            textView.text = Html.fromHtml(
                "<font>$name</font><font color=\"#FF0000\">*</font>"
            )
        }
    }
}
