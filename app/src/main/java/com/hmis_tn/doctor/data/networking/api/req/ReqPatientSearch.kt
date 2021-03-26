package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

class ReqPatientSearch(
    @SerializedName("searchKeyWord")
    var searchKeyWord: String? = null,
    @SerializedName("mobile")
    var mobile: String? = null,
    @SerializedName("pin")
    var pin: String? = null,
    @SerializedName("pds")
    var pds: String? = null,
    @SerializedName("sortField")
    var sortField: String? = "uhid",
    @SerializedName("sortOrder")
    var sortOrder: String? = "DESC",
    @SerializedName("pageNo")
    var pageNo: Int? = 0,
    @SerializedName("paginationSize")
    var paginationSize: Int? = 10
)