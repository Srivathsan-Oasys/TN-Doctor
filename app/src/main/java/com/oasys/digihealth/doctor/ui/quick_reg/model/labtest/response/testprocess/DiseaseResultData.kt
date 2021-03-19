package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.testprocess

data class DiseaseResultData(
    val auth_status_uuid: Int? = 0,
    val qualifier_count: Int? = 0,
    val qualifier_uuid: Int? = 0,
    val result_value: String? = ""
)