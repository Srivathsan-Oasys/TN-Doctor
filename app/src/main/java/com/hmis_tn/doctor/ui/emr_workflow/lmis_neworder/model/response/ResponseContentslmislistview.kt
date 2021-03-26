package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.response

data class ResponseContentslmislistview(
    var code: String? = "",
    var confidential_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_approval_requried: Boolean? = false,
    var is_confidential: Boolean? = false,
    var name: String? = "",
    var uuid: Int? = 0,
    var sample_type_code: String? = "",
    var sample_type_name: String? = "",
    var sample_type_uuid: Int? = 0,
    var status: Boolean? = false,
    var sub_department_uuid: Int? = 0,
    var test_disease_name: Any? = Any(),
    var test_disease_uuid: Any? = Any(),
    var test_diseases_uuid: Int? = 0,
    var type: String? = "",
    var type_of_method_code: Any? = Any(),
    var type_of_method_name: Any? = Any(),
    var type_of_method_uuid: Int? = 0
)