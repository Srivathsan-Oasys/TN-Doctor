package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.headings

data class ResponseContent(
    val Is_default: Any?,
    val code: String?,
    val color: Any?,
    val created_by: Int?,
    val created_date: Any?,
    val display_order: Any?,
    val is_active: Boolean?,
    val language: Any?,
    val modified_by: Int?,
    val modified_date: Any?,
    var name: String?,
    val revision: Int?,
    val status: Boolean?,
    var uuid: Int?
) {
    var isSelected = false
}