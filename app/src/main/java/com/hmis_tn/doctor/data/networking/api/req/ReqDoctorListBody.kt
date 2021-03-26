package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqDoctorListBody(
    @SerializedName("search")
    var search: String? = null,
    @SerializedName("sortField")
    var sortField: String? = null,
    @SerializedName("pageNo")
    var pageNo: Int? = null,
    @SerializedName("paginationSize")
    var paginationSize: Int? = null,
    @SerializedName("doc_uuid")
    var doc_uuid: String? = null,
    @SerializedName("department_uuid")
    var department_uuid: String? = null,
    @SerializedName("lab_uuid")
    var lab_uuid: String? = null,
    @SerializedName("rad_uuid")
    var rad_uuid: String? = null
)