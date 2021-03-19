package com.oasys.digihealth.doctor.ui.quickregistration.model

data class SchemaRequest(
    var code_or_name: Int? = null,
    var pageNo: Int? = null,
    var paginationSize: Int? = null,
    var scheme_type_uuid: Int? = null,
    var search: String? = null,
    var status: Int? = null
)