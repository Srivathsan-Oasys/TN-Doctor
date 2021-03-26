package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.compare_data

data class ResponseContents(
    var observed_values: List<ObservedValue>?,

    var ventilator_details: VentilatorDetails?,
    var abg_details: AbgDetails?,
    var monitor_details: MonitorDetails?,
    var intake_details: IntakeDetails?,
    var bp_details: BPDetails?,
    var diabetes_details: DiabetesDetails?,
    var dialysis_details: DialysisDetails?
)