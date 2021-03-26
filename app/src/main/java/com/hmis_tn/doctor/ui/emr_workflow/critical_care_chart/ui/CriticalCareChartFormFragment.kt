package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentCriticalCareChartFormBinding
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.CriticalCareChart
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.D
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.ResponseContents
import com.hmis_tn.doctor.utils.Utils

class CriticalCareChartFormFragment() : Fragment() {

    private var criticalCareChartChildFragment: CriticalCareChartChildFragment? = null
    private var heading: String = ""
    private var formData: ResponseContents? = null

    constructor(
        criticalCareChartChildFragment: CriticalCareChartChildFragment,
        heading: String,
        formData: ResponseContents?
    ) : this() {
        this.criticalCareChartChildFragment = criticalCareChartChildFragment
        this.heading = heading
        this.formData = formData
    }

    private var binding: FragmentCriticalCareChartFormBinding? = null
    private var utils: Utils? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_critical_care_chart_form,
            container,
            false
        )
        utils = Utils(requireContext())

        initViews(false)
        listeners()

        return binding?.root!!
    }

    private fun initViews(isEdit: Boolean, list: List<D>? = null) {
//        val bundle = arguments
//        bundle?.let {
//            binding?.tvHeading?.text =
//                if (bundle.containsKey("heading")) bundle.getString("heading") else ""
//
//            if (bundle.containsKey("form_data")) {
//                val formData = bundle.getSerializable("form_data")!!
//                val responseContents = formData as ResponseContents
//                if (isEdit) {
//                    var text = binding?.tvHeading?.text?.toString()
//                    text = text?.replace("Add New", "Edit")
//                    binding?.tvHeading?.text = text
//
//                    responseContents.critical_care_charts?.forEach { criticalCareChart: CriticalCareChart ->
//                        list?.forEach { d: D ->
//                            if (criticalCareChart.code == d.ccc_code) {
//                                criticalCareChart.answer =
//                                    when {
//                                        d.ventilator_observed_value != null -> d.ventilator_observed_value
//                                        d.abg_observed_value != null -> d.abg_observed_value
//                                        d.monitor_observed_value != null -> d.monitor_observed_value
//                                        d.intake_observed_value != null -> d.intake_observed_value
//                                        d.bp_observed_value != null -> d.bp_observed_value
//                                        d.diabetes_observed_value != null -> d.diabetes_observed_value
//                                        d.dialysis_observed_value != null -> d.dialysis_observed_value
//                                        else -> ""
//                                    }
//                            }
//                        }
//                    }
//                }
//                binding?.rvFormQuestions?.layoutManager = LinearLayoutManager(context)
//                binding?.rvFormQuestions?.adapter =
//                    CriticalCareChartFormAdapter(
//                        criticalCareChartChildFragment,
//                        ArrayList(responseContents.critical_care_charts!!),
//                        isEdit
//                    )
//            }
//        }

        binding?.tvHeading?.text =
            if (heading != "") heading else ""

        formData?.let { formData ->
            val responseContents = formData
            if (isEdit) {
                var text = binding?.tvHeading?.text?.toString()
                text = text?.replace("Add New", "Edit")
                binding?.tvHeading?.text = text

                responseContents.critical_care_charts?.forEach { criticalCareChart: CriticalCareChart ->
                    list?.forEach { d: D ->
                        if (criticalCareChart.code == d.ccc_code) {
                            criticalCareChart.answer = d.observed_value
//                                when {
//                                    d.ventilator_observed_value != null -> d.ventilator_observed_value
//                                    d.abg_observed_value != null -> d.abg_observed_value
//                                    d.monitor_observed_value != null -> d.monitor_observed_value
//                                    d.intake_observed_value != null -> d.intake_observed_value
//                                    d.bp_observed_value != null -> d.bp_observed_value
//                                    d.diabetes_observed_value != null -> d.diabetes_observed_value
//                                    d.dialysis_observed_value != null -> d.dialysis_observed_value
//                                    else -> ""
//                                }
                        }
                    }
                }
            }
            binding?.rvFormQuestions?.layoutManager = LinearLayoutManager(context)
            binding?.rvFormQuestions?.adapter =
                CriticalCareChartFormAdapter(
                    criticalCareChartChildFragment!!,
                    ArrayList(responseContents.critical_care_charts!!),
                    isEdit
                )
        }

    }

    private fun listeners() {
        binding?.cvAdd?.setOnClickListener {
            criticalCareChartChildFragment!!.mandatoryQuestionsMap.values.forEach {
                if (!it) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Please fill the mandatory fields"
                    )
                    return@setOnClickListener
                }
            }
            criticalCareChartChildFragment!!.isUpdate =
                (binding?.tvAdd?.text?.toString() != getString(R.string.add))
            criticalCareChartChildFragment!!.create()
        }

        binding?.cvClear?.setOnClickListener {
            clearAll(false)
        }
    }

    fun clearAll(isEdit: Boolean, list: List<D>? = null) {
        criticalCareChartChildFragment!!.mandatoryQuestionsMap.clear()
        initViews(isEdit, list)
        binding?.tvAdd?.text = if (isEdit)
            getString(R.string.update)
        else
            getString(R.string.add)
    }
}
