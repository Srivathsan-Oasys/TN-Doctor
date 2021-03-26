package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class ResponseContentsX(
    var ventilator_details: VentilatorDetails?,
    var abg_details: AbgDetails?,
    var monitor_details: MonitorDetails?,
    var intake_details: IntakeDetails?,
    var bp_details: BPDetails?,
    var diabetes_details: DiabetesDetails?,
    var dialysis_details: DialysisDetails?,

    var observed_values: List<ObservedValue>?
)