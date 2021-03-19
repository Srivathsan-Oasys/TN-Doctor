package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model

data class CriticalCareType(
    var careId: Int?,
    var careName: String?
) {
    var isSelected: Boolean = false
}