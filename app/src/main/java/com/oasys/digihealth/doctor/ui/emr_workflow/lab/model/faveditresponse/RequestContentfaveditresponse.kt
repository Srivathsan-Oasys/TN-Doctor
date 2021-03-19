package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse

data class RequestContentfaveditresponse(
    var departmentId: String? = "",
    var favourite_display_order: Int? = 0,
    var favourite_id: Int? = 0,
    var is_active: Boolean? = false,
    var testname: String? = "",
    var favourite_name: String? = "",
    var quantity: Int = 0,
    var diet_frequency_uuid: Int = 0,
    var diet_category_uuid: Int = 0,
    var department_id: Int = 0,
    var is_public: Boolean = false
)