package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class RadiologyTypeResponseContent(
    val code: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    var name: String = "",
    val revision: Int = 0,
    val status: Boolean = false,
    var uuid: Int = 0
)