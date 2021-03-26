package com.hmis_tn.doctor.ui.emr_workflow.lab.model.labviewresponse

data class ResponseContentsLabView(
    var am_code: Any? = Any(),
    var am_name: Any? = Any(),
    var lq_code: String? = "",
    var lq_name: String? = "",
    var po_doctor_uuid: Int? = 0,
    var po_order_number: Int? = 0,
    var po_patient_uuid: Int? = 0,
    var po_uuid: Int? = 0,
    var pwd_result_value: String? = "",
    var tm_code: String? = "",
    var tm_name: String? = "",
    var trm_max_value: Any? = Any(),
    var trm_min_value: Any? = Any(),
    var um_code: String? = "",
    var um_name: String? = ""
)