package com.hmis_tn.doctor.ui.out_patient.model.search_request_model

import com.google.gson.annotations.SerializedName

data class MypatientSearchRequestModel(
    @SerializedName("departmentId")
    var departmentId: String = "",
    @SerializedName("doctor_id")
    var doctor_id: String = "",
    @SerializedName("from_date")
    var from_date: String = "",
    @SerializedName("pageNo")
    var pageNo: Int = 0,
    @SerializedName("paginationSize")
    var paginationSize: Int = 0,
    @SerializedName("pd_mobile")
    var pd_mobile: String? = null,
    @SerializedName("sortField")
    var sortField: String = "",
    @SerializedName("sortOrder")
    var sortOrder: String = "",
    @SerializedName("to_date")
    var to_date: String = ""
)