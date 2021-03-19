package com.oasys.digihealth.doctor.ui.out_patient.model.search_request_model

data class SearchPatientRequestModel(
    var mobile: String? = null,
    var pageNo: Int? = null,
    var paginationSize: Int? = null,
    var pin: String? = "",
    var sortField: String? = null,
    var sortOrder: String? = null,
    var admission_status_uuid: String = "",
    var department_uuid: String? = "",
    var facility_uuid: String? = "",
    var registeredDate: String? = ""

)

