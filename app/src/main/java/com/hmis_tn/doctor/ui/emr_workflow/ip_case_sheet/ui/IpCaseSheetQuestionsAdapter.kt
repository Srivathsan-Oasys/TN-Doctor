package com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSection
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSectionCategory
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSectionCategoryConcept
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSectionCategoryConceptValue
import com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records.observed_values.ResponseContent
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.dialog_comment.*
import kotlinx.android.synthetic.main.item_ip_case_sheet_questions.view.*
import java.util.*
import kotlin.collections.ArrayList

class IpCaseSheetQuestionsAdapter(
    private val profileSection: ProfileSection,
    private val profileSectionCategory: ProfileSectionCategory,
    private val list: List<ProfileSectionCategoryConcept>,
    private val ipCaseSheetChildFragment: IpCaseSheetChildFragment,
    private val observedValueList: ArrayList<ResponseContent>? = null
) :
    RecyclerView.Adapter<IpCaseSheetQuestionsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ip_case_sheet_questions, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val profileSectionCategoryConcept = list[position]

        if (profileSectionCategoryConcept.is_mandatory == true) {
            populateMandatoryQuestion(
                holder.itemView.tvQuestion,
                profileSectionCategoryConcept.name ?: ""
            )
            profileSectionCategoryConcept.profile_section_category_uuid?.let {
                ipCaseSheetChildFragment.mandatoryQuestions[it] = false
            }
        } else {
            holder.itemView.tvQuestion.text = profileSectionCategoryConcept.name
        }

        populateAnswerInputType(holder, profileSectionCategoryConcept)
    }

    private fun populateAnswerInputType(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        when (profileSectionCategoryConcept.value_types?.code) {
            "Free Text" -> populateEditText(holder, profileSectionCategoryConcept)
            "Numeric" -> populateNumericEditText(holder, profileSectionCategoryConcept)
            "Term Based" -> populateTermBased(holder, profileSectionCategoryConcept)
            "Boolean" -> populateSwitch(holder, profileSectionCategoryConcept)
            "Check Box" -> populateCheckBox(holder, profileSectionCategoryConcept)
            "DateTime" -> populateDateTime(holder, profileSectionCategoryConcept)
            "EMail" -> populateEMail(holder, profileSectionCategoryConcept)
            "Notes" -> populateNotes(holder, profileSectionCategoryConcept)
            "Rating" -> populateRating(holder, profileSectionCategoryConcept)
            "Combo" -> populateCombo(holder, profileSectionCategoryConcept)
            "Drop Down" -> populateSpinner(holder, profileSectionCategoryConcept)
            "Radio Button" -> populateRadioButton(holder, profileSectionCategoryConcept)
            "Date" -> populateDate(holder, profileSectionCategoryConcept)
            "BWC" -> populateButtonWithComments(holder, profileSectionCategoryConcept)
            "TWD" -> populateEditTextWithDropdown(holder, profileSectionCategoryConcept)
            "NWD" -> populateNumericEditTextWithDropdown(holder, profileSectionCategoryConcept)
            "BTN" -> populateButton(holder, profileSectionCategoryConcept)
            "CWT" -> populateCheckboxWithEditText(holder, profileSectionCategoryConcept)
            "BATWD" -> populateButtonAndEditTextWithDropdown(holder, profileSectionCategoryConcept)
        }
    }

    private fun populateEditText(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(8, 0, 8, 0)

            val label = TextView(holder.itemView.context)
            label.text = profileSectionCategoryConceptValue.value_name
            ll.addView(label)

            val et = EditText(holder.itemView.context)
            et.inputType = InputType.TYPE_CLASS_TEXT
            et.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            et.maxLines = 1
            ll.addView(et)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    et.setText(responseContent.term_key)
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun populateNumericEditText(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(8, 0, 8, 0)

            val label = TextView(holder.itemView.context)
            label.text = profileSectionCategoryConceptValue.value_name
            ll.addView(label)

            val et = EditText(holder.itemView.context)
            et.inputType = InputType.TYPE_CLASS_NUMBER
            et.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            et.maxLines = 1
            ll.addView(et)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    et.setText(responseContent.term_key)
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun populateTermBased(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val ll = LinearLayout(holder.itemView.context)
        ll.orientation = LinearLayout.VERTICAL
        ll.layoutParams = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        ll.setPadding(8, 0, 8, 0)

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->

            val til = TextInputLayout(holder.itemView.context)
            til.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.addView(til)

//            val label = TextView(holder.itemView.context)
//            label.text = profileSectionCategoryConceptValue.value_name
//            ll.addView(label)

            val et = EditText(holder.itemView.context)
            et.hint = profileSectionCategoryConceptValue.value_name
            et.inputType = InputType.TYPE_CLASS_TEXT
            et.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            et.maxLines = 1
            til.addView(et)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                    responseContent.profile_section_category_concept_uuid
                ) {
                    et.setText(responseContent.term_key)
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }

        holder.itemView.linearLayoutQuestions.addView(ll)
    }

    private fun populateSwitch(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val ll = LinearLayout(holder.itemView.context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.layoutParams = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        ll.setPadding(8, 0, 8, 0)

        val label = TextView(holder.itemView.context)
        profileSectionCategoryConcept.profile_section_category_concept_values?.get(0)
            ?.let { label.text = it.value_name }
            ?: (holder.itemView.context.getString(R.string.no).also { label.text = it })
        ll.addView(label)

        val switch = Switch(holder.itemView.context)
        ll.addView(switch)

        observedValueList?.forEach { responseContent ->
            if (profileSectionCategoryConcept.profile_section_category_concept_values?.get(0)
                    ?.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid
            ) {
//                switch.isChecked = (responseContent.term_key?.toInt() ?: 0 == 1)
                switch.isChecked = (responseContent.term_key != profileSectionCategoryConcept
                    .profile_section_category_concept_values?.get(0)?.value_name)
            }
        }

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                profileSectionCategoryConcept.profile_section_category_concept_values?.get(0)?.let {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        it,
                        profileSectionCategoryConcept.profile_section_category_concept_values?.get(1)?.value_name
                            ?: holder.itemView.context.getString(R.string.yes),
                        false
                    )
                }
            } else {
                profileSectionCategoryConcept.profile_section_category_concept_values?.get(0)?.let {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        it,
                        profileSectionCategoryConcept.profile_section_category_concept_values?.get(0)?.value_name
                            ?: holder.itemView.context.getString(R.string.no),
                        false
                    )
                }
            }
        }

        val label1 = TextView(holder.itemView.context)
        profileSectionCategoryConcept.profile_section_category_concept_values?.get(1)
            ?.let { label1.text = it.value_name }
            ?: (holder.itemView.context.getString(R.string.yes).also { label1.text = it })
        ll.addView(label1)

        holder.itemView.linearLayoutQuestions.addView(ll)
    }

    private fun populateCheckBox(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
//        val hsv = HorizontalScrollView(holder.itemView.context)
//        hsv.isScrollbarFadingEnabled = false
//        hsv.setPadding(8, 0, 8, 0)

        val ll = LinearLayout(holder.itemView.context)
        ll.orientation = LinearLayout.VERTICAL
//        hsv.addView(ll)

        val checkboxList = ArrayList<CheckBox>()

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val checkBox = CheckBox(holder.itemView.context)
            checkBox.text = profileSectionCategoryConceptValue.value_name
            ll.addView(checkBox)
            checkboxList.add(checkBox)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    checkBox.isChecked = responseContent.term_key?.toInt() ?: 0 == 1
                }
            }

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (profileSectionCategoryConcept.is_multiple == true) {
                    if (isChecked) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            buttonView.text.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            buttonView.text.toString(),
                            true
                        )
                    }
                } else {
                    if (isChecked) {
                        removeAllCheck(profileSectionCategoryConcept, checkboxList)
                        buttonView.isChecked = true
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            buttonView.text.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            buttonView.text.toString(),
                            true
                        )
                    }
                }
            }
            profileSectionCategoryConcept.profile_section_category_uuid?.let {
                ipCaseSheetChildFragment.mandatoryQuestions[it] = true
            }
        }
        holder.itemView.linearLayoutQuestions.addView(ll)
    }

    private fun populateDateTime(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(8, 0, 8, 0)

            val label = TextView(holder.itemView.context)
            label.text = profileSectionCategoryConceptValue.value_name
            ll.addView(label)

            val et = EditText(holder.itemView.context)
            et.isFocusable = false
            et.isClickable = false
            et.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0)
            et.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.addView(et)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                    responseContent.profile_section_category_concept_uuid
                ) {
                    val disp = Utils(holder.itemView.context).displayDate(
                        responseContent.term_key ?: "",
                        "yyyy-MM-dd'T'HH:mm:sss'Z'"
                    )
                    et.setText(disp)
                }
            }

            et.setOnClickListener {
                val cal = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(
                    holder.itemView.context,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val timePickerDialog = TimePickerDialog(
                            holder.itemView.context,
                            TimePickerDialog.OnTimeSetListener { view1, hourOfDay, minute ->
                                val date = String.format(
                                    "%02d", dayOfMonth
                                ) + "-" + String.format(
                                    "%02d", month + 1
                                ) + "-" + year
                                val time = String.format(
                                    "%02d", hourOfDay
                                ) + ":" + String.format(
                                    "%02d", minute
                                )
                                val displayDateTime = "$date $time"
                                et.setText(displayDateTime)

                                ipCaseSheetChildFragment.formReqPayload(
                                    profileSection,
                                    profileSectionCategory,
                                    profileSectionCategoryConcept,
                                    profileSectionCategoryConceptValue,
                                    et.text.toString(),
                                    false
                                )
                                profileSectionCategoryConcept.profile_section_category_uuid?.let {
                                    ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                                }
                            },
                            cal[Calendar.HOUR_OF_DAY],
                            cal[Calendar.MINUTE],
                            false
                        )
                        timePickerDialog.show()

//                        val date = "$dayOfMonth - ${month + 1} - $year"
//                        et.setText(date)
//
//                        ipCaseSheetChildFragment.formReqPayload(
//                            profileSection,
//                            profileSectionCategory,
//                            profileSectionCategoryConcept,
//                            profileSectionCategoryConceptValue,
//                            et.text.toString(),
//                            false
//                        )
//                        profileSectionCategoryConcept.profile_section_category_uuid?.let {
//                            ipCaseSheetChildFragment.mandatoryQuestions[it] = true
//                        }
                    }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
                )
                datePickerDialog.show()
            }

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun populateEMail(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(8, 0, 8, 0)

            val label = TextView(holder.itemView.context)
            label.text = profileSectionCategoryConceptValue.value_name
            ll.addView(label)

            val et = EditText(holder.itemView.context)
            et.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            et.maxLines = 1
            et.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.addView(et)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    et.setText(responseContent.term_key)
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun populateNotes(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val llV = LinearLayout(holder.itemView.context)
        llV.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llV.orientation = LinearLayout.VERTICAL
        llV.setPadding(8, 0, 8, 0)

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val llH = LinearLayout(holder.itemView.context)
            llH.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            llH.orientation = LinearLayout.HORIZONTAL
            llV.addView(llH)

            val label = TextView(holder.itemView.context)
            label.text = profileSectionCategoryConceptValue.value_name
            llH.addView(label)

            val et = EditText(holder.itemView.context)
            et.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            et.isVerticalScrollBarEnabled = true
            llH.addView(et)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    et.setText(responseContent.term_key)
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }
        holder.itemView.linearLayoutQuestions.addView(llV)
    }

    private fun populateRating(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val linearLayout = LinearLayout(holder.itemView.context)
            linearLayout.setPadding(8, 0, 8, 0)

            val label = TextView(holder.itemView.context)
            label.text = profileSectionCategoryConceptValue.value_name
            linearLayout.addView(label)

            val hsv = HorizontalScrollView(holder.itemView.context)
            hsv.isScrollbarFadingEnabled = false
            linearLayout.addView(hsv)

            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            hsv.addView(ll)

            val ratingBar =
                RatingBar(holder.itemView.context, null, android.R.attr.ratingBarStyleSmall)
            ratingBar.numStars = 10
            ratingBar.setIsIndicator(false)
            ll.addView(ratingBar)

            val ratingNo = TextView(holder.itemView.context)
            ratingNo.text = "0"
            ll.addView(ratingNo)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    ratingBar.rating = responseContent.term_key?.toFloat() ?: 0f
                    ratingNo.text = ratingBar.rating.toInt().toString()
                }
            }

            ratingBar.setOnRatingBarChangeListener { ratingBar1, rating, fromUser ->
                ipCaseSheetChildFragment.formReqPayload(
                    profileSection,
                    profileSectionCategory,
                    profileSectionCategoryConcept,
                    profileSectionCategoryConceptValue,
                    rating.toString(),
                    false
                )
                profileSectionCategoryConcept.profile_section_category_uuid?.let {
                    ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                }
                ratingNo.text = rating.toInt().toString()
            }
            holder.itemView.linearLayoutQuestions.addView(linearLayout)
        }
    }

    private fun populateCombo(  //sri - combo = radio button
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val hsv = HorizontalScrollView(holder.itemView.context)
        hsv.isScrollbarFadingEnabled = false

        val rg = RadioGroup(holder.itemView.context)
        rg.orientation = LinearLayout.HORIZONTAL
        hsv.addView(rg)

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val rb = RadioButton(holder.itemView.context)
            rb.text = profileSectionCategoryConceptValue.value_name
            rb.setPadding(8, 0, 8, 0)
            rg.addView(rb)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    rb.isChecked = responseContent.term_key?.toInt() ?: 0 == 1
                }
            }

            rb.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValue,
                        buttonView.text.toString(),
                        false
                    )
                } else {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValue,
                        buttonView.text.toString(),
                        true
                    )
                }
                profileSectionCategoryConcept.profile_section_category_uuid?.let {
                    ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                }
            }
        }
        holder.itemView.linearLayoutQuestions.addView(hsv)
    }

    //todo multi select spinner
    @SuppressLint("ClickableViewAccessibility")
    private fun populateSpinner(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        if (profileSectionCategoryConcept.is_multiple == false) {
            val spinner = Spinner(holder.itemView.context)
            spinner.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val list = ArrayList<String?>()
            val profileSectionCategoryConceptValueList =
                ArrayList<ProfileSectionCategoryConceptValue>()
            profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
                profileSectionCategoryConceptValueList.add(profileSectionCategoryConceptValue)
                list.add(profileSectionCategoryConceptValue.value_name)

                observedValueList?.forEach { responseContent ->
                    if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                        responseContent.profile_section_category_concept_uuid
                    ) {
                        spinner.setSelection(getKeyFromValue(responseContent.term_key))
                    }
                }
            }
            spinner.setOnTouchListener { v, event ->
                if (spinner.adapter == null) {
                    val adapter = ArrayAdapter<String>(
                        holder.itemView.context,
                        android.R.layout.simple_spinner_item,
                        list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
                false
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValueList[position],
                        spinner.getItemAtPosition(position).toString(),
                        false
                    )
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                    }
                }
            }

            holder.itemView.linearLayoutQuestions.addView(spinner)
        } else {
            //multi spinner
            var answer = ""

            val multiSpinner = Spinner(holder.itemView.context)
            multiSpinner.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val list = ArrayList<String?>()
            val profileSectionCategoryConceptValueList =
                ArrayList<ProfileSectionCategoryConceptValue>()
            profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
                profileSectionCategoryConceptValueList.add(profileSectionCategoryConceptValue)
                list.add(profileSectionCategoryConceptValue.value_name)

                observedValueList?.forEach { responseContent ->
                    if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                        responseContent.profile_section_category_concept_uuid
                    ) {
                        multiSpinner.setSelection(getKeyFromValue(responseContent.term_key))
                    }
                }
            }
            multiSpinner.setOnTouchListener { v, event ->
                if (multiSpinner.adapter == null) {
                    val adapter = ArrayAdapter<String>(
                        holder.itemView.context,
                        android.R.layout.simple_spinner_item,
                        list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    multiSpinner.adapter = adapter
                }
                false
            }

            multiSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    answer += "," + multiSpinner.getItemAtPosition(position).toString()
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValueList[position],
                        answer,
                        false
                    )
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                    }
                }
            }

            holder.itemView.linearLayoutQuestions.addView(multiSpinner)
        }
    }

    private fun populateRadioButton(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val hsv = HorizontalScrollView(holder.itemView.context)
        hsv.isScrollbarFadingEnabled = false

        val rg = RadioGroup(holder.itemView.context)
        rg.orientation = LinearLayout.VERTICAL
        hsv.addView(rg)

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val rb = RadioButton(holder.itemView.context)
            rb.text = profileSectionCategoryConceptValue.value_name
            rb.setPadding(8, 0, 8, 0)
            rg.addView(rb)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    rb.isChecked = (responseContent.term_key?.toInt() ?: 0 == 1)
                }
            }

            rb.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValue,
                        buttonView.text.toString(),
                        false
                    )
                } else {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValue,
                        buttonView.text.toString(),
                        true
                    )
                }
                profileSectionCategoryConcept.profile_section_category_uuid?.let {
                    ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                }
            }
        }
        holder.itemView.linearLayoutQuestions.addView(hsv)
    }

    private fun populateDate(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(8, 0, 8, 0)

            val label = TextView(holder.itemView.context)
            label.text = profileSectionCategoryConceptValue.value_name
            ll.addView(label)

            val et = EditText(holder.itemView.context)
            et.isFocusable = false
            et.isClickable = false
            et.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0)
            et.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.addView(et)

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                    responseContent.profile_section_category_concept_uuid
                ) {
                    val disp = Utils(holder.itemView.context).convertDateFormat(
                        responseContent.term_key ?: "",
                        "yyyy-MM-dd'T'HH:mm:sss'Z'",
                        "dd-MM-yyyy"
                    )
                    et.setText(disp)
                }
            }

            et.setOnClickListener {
                val cal = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(
                    holder.itemView.context,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val date = "$dayOfMonth - ${month + 1} - $year"
                        et.setText(date)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            et.text.toString(),
                            false
                        )
                        profileSectionCategoryConcept.profile_section_category_uuid?.let {
                            ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                        }
                    }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
                )
                datePickerDialog.show()
            }

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun populateButtonWithComments(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val innerLayoutList = ArrayList<LinearLayout>()

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(0, 0, 8, 0)

            val cv = CardView(holder.itemView.context)
            cv.isClickable = true
            cv.useCompatPadding = true
            cv.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.addView(cv)

            val innerLayout = LinearLayout(holder.itemView.context)
            innerLayout.orientation = LinearLayout.HORIZONTAL
            innerLayout.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            innerLayout.setPadding(4, 4, 4, 4)
            innerLayout.setBackgroundColor(Color.WHITE)
            cv.addView(innerLayout)

            val label = TextView(holder.itemView.context)
            label.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            label.text = profileSectionCategoryConceptValue.value_name
            label.setTextColor(Color.BLACK)
            innerLayout.addView(label)

            val iv = ImageView(holder.itemView.context)
            iv.setImageResource(R.drawable.ic_comment_yellow_24dp)
            innerLayout.addView(iv)

            innerLayoutList.add(innerLayout)

            innerLayout.setOnClickListener { view ->
                if (profileSectionCategoryConcept.is_multiple == true) {
                    if ((view.background as ColorDrawable).color == Color.WHITE) {
                        view.setBackgroundColor(Color.BLUE)
                        label.setTextColor(Color.WHITE)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            false
                        )
                    } else {
                        view.setBackgroundColor(Color.WHITE)
                        label.setTextColor(Color.BLACK)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            true
                        )
                    }
                } else {
                    if ((view.background as ColorDrawable).color == Color.WHITE) {
                        uncheckAllButtons(profileSectionCategoryConcept, innerLayoutList)
                        view.setBackgroundColor(Color.BLUE)
                        label.setTextColor(Color.WHITE)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            false
                        )
                    } else {
                        view.setBackgroundColor(Color.WHITE)
                        label.setTextColor(Color.BLACK)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            true
                        )
                    }
                }
            }

            iv.setOnClickListener {
                if ((innerLayout.background as ColorDrawable).color == Color.BLUE) {
                    val commentDialog = CommentDialog(holder.itemView.context)

                    commentDialog.show()

                    commentDialog.tvTitle.text = "Comments"
                    commentDialog.userNameEditText.isFocusable = true
                    commentDialog.userNameEditText.isFocusableInTouchMode = true
                    commentDialog.userNameEditText.setTextColor(Color.BLACK)

                    observedValueList?.forEach { responseContent ->
                        if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                            responseContent.profile_section_category_concept_uuid
                        ) {
                            commentDialog.userNameEditText.setText(responseContent.term_key)
                        }
                    }

                    commentDialog.saveCardView.setOnClickListener {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            commentDialog.userNameEditText.text.toString(),
                            false
                        )
                        profileSectionCategoryConcept.profile_section_category_uuid?.let {
                            ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                        }
                        commentDialog.userNameEditText.setText("")
                        commentDialog.dismiss()
                    }

                    commentDialog.clearCardView.setOnClickListener {
                        commentDialog.userNameEditText.setText("")
                    }

                    commentDialog.closeImageView.setOnClickListener {
                        commentDialog.dismiss()
                    }
                }
            }

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun populateEditTextWithDropdown(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(8, 0, 8, 0)

            val et = EditText(holder.itemView.context)
            et.inputType = InputType.TYPE_CLASS_TEXT
            et.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            et.maxLines = 1
            ll.addView(et)

            val spinner = Spinner(holder.itemView.context)
            spinner.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            val list = ArrayList<String?>()
            val profileSectionCategoryConceptValueList =
                ArrayList<ProfileSectionCategoryConceptValue>()
            profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
                profileSectionCategoryConceptValueList.add(profileSectionCategoryConceptValue)
                list.add(profileSectionCategoryConceptValue.value_name)

                observedValueList?.forEach { responseContent ->
                    if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                        responseContent.profile_section_category_concept_uuid
                    ) {
                        spinner.setSelection(getKeyFromValue(responseContent.term_key))
                    }
                }
            }
            ll.addView(spinner)

            spinner.setOnTouchListener { v, event ->
                if (spinner.adapter == null) {
                    val adapter = ArrayAdapter<String>(
                        holder.itemView.context,
                        android.R.layout.simple_spinner_item,
                        list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
                false
            }

            // check answer post

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                    et.setText(responseContent.term_key)
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValueList[position],
                        spinner.getItemAtPosition(position).toString(),
                        false
                    )
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                    }
                }
            }

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun populateNumericEditTextWithDropdown(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(8, 0, 8, 0)

            val et = EditText(holder.itemView.context)
            et.inputType = InputType.TYPE_CLASS_NUMBER
            et.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            et.maxLines = 1
            ll.addView(et)

            val spinner = Spinner(holder.itemView.context)
            spinner.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            val list = ArrayList<String?>()
            val profileSectionCategoryConceptValueList =
                ArrayList<ProfileSectionCategoryConceptValue>()
            profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
                profileSectionCategoryConceptValueList.add(profileSectionCategoryConceptValue)
                list.add(profileSectionCategoryConceptValue.value_name)

                observedValueList?.forEach { responseContent ->
                    if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                        spinner.setSelection(getKeyFromValue(responseContent.term_key))
                    }
                }
            }
            ll.addView(spinner)

            spinner.setOnTouchListener { v, event ->
                if (spinner.adapter == null) {
                    val adapter = ArrayAdapter<String>(
                        holder.itemView.context,
                        android.R.layout.simple_spinner_item,
                        list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
                false
            }

            //check answer post

            observedValueList?.forEach { responseContent ->
                if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid ==
                    responseContent.profile_section_category_concept_uuid
                ) {
                    et.setText(responseContent.term_key)
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValueList[position],
                        spinner.getItemAtPosition(position).toString(),
                        false
                    )
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                    }
                }
            }

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun populateButton(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val innerLayoutList = ArrayList<LinearLayout>()

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(0, 0, 8, 0)

            val cv = CardView(holder.itemView.context)
            cv.isClickable = true
            cv.useCompatPadding = true
            cv.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.addView(cv)

            val innerLayout = LinearLayout(holder.itemView.context)
            innerLayout.orientation = LinearLayout.HORIZONTAL
            innerLayout.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            innerLayout.setPadding(4, 4, 4, 4)
            innerLayout.setBackgroundColor(Color.WHITE)
            cv.addView(innerLayout)

            val label = TextView(holder.itemView.context)
            label.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            label.text = profileSectionCategoryConceptValue.value_name
            label.setTextColor(Color.BLACK)
            innerLayout.addView(label)

            innerLayoutList.add(innerLayout)

            innerLayout.setOnClickListener { view ->
                if (profileSectionCategoryConcept.is_multiple == true) {
                    if ((view.background as ColorDrawable).color == Color.WHITE) {
                        view.setBackgroundColor(Color.BLUE)
                        label.setTextColor(Color.WHITE)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            false
                        )
                    } else {
                        view.setBackgroundColor(Color.WHITE)
                        label.setTextColor(Color.BLACK)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            true
                        )
                    }
                } else {
                    if ((view.background as ColorDrawable).color == Color.WHITE) {
                        uncheckAllButtons(profileSectionCategoryConcept, innerLayoutList)
                        view.setBackgroundColor(Color.BLUE)
                        label.setTextColor(Color.WHITE)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            false
                        )
                    } else {
                        view.setBackgroundColor(Color.WHITE)
                        label.setTextColor(Color.BLACK)

                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            label.text.toString(),
                            true
                        )
                    }
                }
            }

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun populateCheckboxWithEditText(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL

            val checkboxList = ArrayList<CheckBox>()

            val checkBox = CheckBox(holder.itemView.context)
            checkBox.text = profileSectionCategoryConceptValue.value_name
            ll.addView(checkBox)
            checkboxList.add(checkBox)

            val et = EditText(holder.itemView.context)
            et.inputType = InputType.TYPE_CLASS_TEXT
            et.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            et.maxLines = 1
            et.isClickable = false
            et.isFocusable = false
            ll.addView(et)

            checkBox.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    et.isClickable = true
                    et.isFocusable = true
                    et.isFocusableInTouchMode = true
                } else {
                    et.isClickable = false
                    et.isFocusable = false
                    et.isFocusableInTouchMode = false
                    et.setText("")
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }
            })

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun populateButtonAndEditTextWithDropdown(
        holder: MyViewHolder,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept
    ) {
        val innerLayoutList = ArrayList<LinearLayout>()

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            val ll = LinearLayout(holder.itemView.context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ll.setPadding(0, 0, 8, 0)

            val cv = CardView(holder.itemView.context)
            cv.isClickable = true
            cv.useCompatPadding = true
            cv.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            ll.addView(cv)

            val innerLayout = LinearLayout(holder.itemView.context)
            innerLayout.orientation = LinearLayout.HORIZONTAL
            innerLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            innerLayout.setPadding(4, 4, 4, 4)
            innerLayout.setBackgroundColor(Color.WHITE)
            cv.addView(innerLayout)

            val label = TextView(holder.itemView.context)
            label.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            label.text = profileSectionCategoryConceptValue.value_name
            label.setTextColor(Color.BLACK)
            innerLayout.addView(label)

            innerLayoutList.add(innerLayout)

            val et = EditText(holder.itemView.context)
            et.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            et.inputType = InputType.TYPE_CLASS_TEXT
            et.isFocusable = false
            et.isClickable = false
            et.maxLines = 1
            ll.addView(et)

            val spinner = Spinner(holder.itemView.context)
            spinner.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
            spinner.isEnabled = false
            val list = ArrayList<String?>()
            val profileSectionCategoryConceptValueList =
                ArrayList<ProfileSectionCategoryConceptValue>()
            profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
                profileSectionCategoryConceptValueList.add(profileSectionCategoryConceptValue)
                list.add(profileSectionCategoryConceptValue.value_name)

                observedValueList?.forEach { responseContent ->
                    if (profileSectionCategoryConceptValue.profile_section_category_concept_uuid == responseContent.profile_section_category_concept_uuid) {
                        spinner.setSelection(getKeyFromValue(responseContent.term_key))
                    }
                }
            }
            ll.addView(spinner)

            spinner.setOnTouchListener { v, event ->
                if (spinner.adapter == null) {
                    val adapter = ArrayAdapter<String>(
                        holder.itemView.context,
                        android.R.layout.simple_spinner_item,
                        list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
                false
            }

            //check answer post

            innerLayout.setOnClickListener { view ->
                if (profileSectionCategoryConcept.is_multiple == true) {
                    if ((view.background as ColorDrawable).color == Color.WHITE) {
                        view.setBackgroundColor(Color.BLUE)
                        label.setTextColor(Color.WHITE)

                        et.isFocusable = true
                        et.isFocusableInTouchMode = true
                        et.isClickable = true
                        spinner.isEnabled = true
                    } else {
                        view.setBackgroundColor(Color.WHITE)
                        label.setTextColor(Color.BLACK)

                        et.isFocusable = false
                        et.isClickable = false
                        spinner.isEnabled = false
                    }
                } else {
                    if ((view.background as ColorDrawable).color == Color.WHITE) {
                        uncheckAllButtons(profileSectionCategoryConcept, innerLayoutList)
                        view.setBackgroundColor(Color.BLUE)
                        label.setTextColor(Color.WHITE)

                        et.isFocusable = true
                        et.isFocusableInTouchMode = true
                        et.isClickable = true
                        spinner.isEnabled = true
                    } else {
                        view.setBackgroundColor(Color.WHITE)
                        label.setTextColor(Color.BLACK)

                        et.isFocusable = false
                        et.isClickable = false
                        spinner.isEnabled = false
                    }
                }
            }

            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            false
                        )
                    } else {
                        ipCaseSheetChildFragment.formReqPayload(
                            profileSection,
                            profileSectionCategory,
                            profileSectionCategoryConcept,
                            profileSectionCategoryConceptValue,
                            s.toString(),
                            true
                        )
                    }
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = s?.isNotEmpty() == true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    ipCaseSheetChildFragment.formReqPayload(
                        profileSection,
                        profileSectionCategory,
                        profileSectionCategoryConcept,
                        profileSectionCategoryConceptValueList[position],
                        spinner.getItemAtPosition(position).toString(),
                        false
                    )
                    profileSectionCategoryConcept.profile_section_category_uuid?.let {
                        ipCaseSheetChildFragment.mandatoryQuestions[it] = true
                    }
                }
            }

            holder.itemView.linearLayoutQuestions.addView(ll)
        }
    }

    private fun removeAllCheck(
        profileSectionCategoryConcept: ProfileSectionCategoryConcept,
        checkBoxList: ArrayList<CheckBox>
    ) {
        checkBoxList.forEach {
            it.isChecked = false
        }
        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            ipCaseSheetChildFragment.formReqPayload(
                profileSection,
                profileSectionCategory,
                profileSectionCategoryConcept,
                profileSectionCategoryConceptValue,
                profileSectionCategoryConceptValue.value_name ?: "",
                true
            )
        }
    }

    private fun uncheckAllButtons(
        profileSectionCategoryConcept: ProfileSectionCategoryConcept,
        innerLayoutList: ArrayList<LinearLayout>
    ) {
        innerLayoutList.forEach { linearLayout ->
            linearLayout.setBackgroundColor(Color.WHITE)
            (linearLayout.getChildAt(0) as? TextView)?.setTextColor(Color.BLACK)

            if (linearLayout.childCount == 3) {
                //BATWD
                (linearLayout.getChildAt(1) as? EditText)?.isFocusable = false
                (linearLayout.getChildAt(1) as? EditText)?.isClickable = false

                (linearLayout.getChildAt(2) as? Spinner)?.isEnabled = false
            }
        }

        profileSectionCategoryConcept.profile_section_category_concept_values?.forEach { profileSectionCategoryConceptValue ->
            innerLayoutList.forEach { linearLayout ->
                ipCaseSheetChildFragment.formReqPayload(
                    profileSection,
                    profileSectionCategory,
                    profileSectionCategoryConcept,
                    profileSectionCategoryConceptValue,
                    (linearLayout.getChildAt(0) as? TextView)?.text.toString(),
                    true
                )
            }
        }
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

    private fun getKeyFromValue(value: String?): Int {
        for (i in 0 until (observedValueList?.size ?: 0)) {
            if (observedValueList?.get(i)?.term_key == value)
                return i
        }
        return 0
    }
}
