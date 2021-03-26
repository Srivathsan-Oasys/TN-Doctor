package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class ManageRadiologyFavAddResponseContents(
    val favourite_active: Boolean = false,
    var favourite_display_order: Int = 0,
    var favourite_id: Int = 0,
    val favourite_type_id: Int = 0,
    val profile_master_code: Any = Any(),
    val profile_master_description: Any = Any(),
    val profile_master_id: Any = Any(),
    val profile_master_name: Any = Any(),
    val test_master_code: String = "",
    val test_master_description: String = "",
    val test_master_id: Int = 0,
    var test_master_name: String = ""
)