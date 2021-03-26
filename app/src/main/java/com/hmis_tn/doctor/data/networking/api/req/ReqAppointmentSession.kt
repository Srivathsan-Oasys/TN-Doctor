package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqAppointmentSession(
    @SerializedName("pageNo")
    var pageNo: Int? = null,
    @SerializedName("paginationSize")
    var paginationSize: Int? = null,
    @SerializedName("sortOrder")
    var sortOrder: String? = null,
    @SerializedName("search")
    var search: String? = null,
    @SerializedName("sortField")
    var sortField: String? = null,
    @SerializedName("department_uuid")
    var department_uuid: Int? = null,
    @SerializedName("doc_uuid")
    var doc_uuid: Int? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = null,
    @SerializedName("lab_firstname")
    var lab_firstname: String? = null,
    @SerializedName("rad_firstname")
    var rad_firstname: String? = null
)