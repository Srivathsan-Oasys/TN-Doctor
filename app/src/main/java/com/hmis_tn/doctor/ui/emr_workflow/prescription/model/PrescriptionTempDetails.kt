package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionTempDetails(
    val display_order: Int = 0,
    val is_public: Boolean = false,
    val template_department: Int = 0,
    val template_desc: String = "",
    val template_id: Int = 0,
    val template_name: String = "",
    val user_uuid: Int = 0,
    var position: Int? = 0,
    var isSelected: Boolean? = false,
    var itemAppendString: String? = ""
)