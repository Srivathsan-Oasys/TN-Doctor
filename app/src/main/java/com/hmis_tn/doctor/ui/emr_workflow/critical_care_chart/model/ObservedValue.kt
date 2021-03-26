package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class ObservedValue(
    var ventilator_date: String?,
    var abg_date: String?,
    var monitor_date: String?,
    var intake_date: String?,
    var bp_date: String?,
    var diabetes_date: String?,
    var dialysis_date: String?,

    var dList: List<D>?
)