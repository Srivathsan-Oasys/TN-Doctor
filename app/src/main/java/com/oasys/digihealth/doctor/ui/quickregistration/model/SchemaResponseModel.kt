package com.oasys.digihealth.doctor.ui.quickregistration.model

data class SchemaResponseModel(
    var SchemaRequest: SchemaRequest? = null,
    var responseContents: List<SchemaResponse>? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    var totalRecords: Int? = null
)