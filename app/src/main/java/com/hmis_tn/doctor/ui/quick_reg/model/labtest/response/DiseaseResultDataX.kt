package com.hmis_tn.doctor.ui.quick_reg.model.labtest.response

data class DiseaseResultDataX(
    val auth_status_uuid: Int? = 0,
    val qualifier_count: Int? = 0,
    val qualifier_uuid: Int? = 0,
    val result_value: String? = ""
)