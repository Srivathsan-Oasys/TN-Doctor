package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqBookAppointmentFilter(
    @SerializedName("appointment_date")
    var appointment_date: String? = "2020-07-20 00:00:00",
    @SerializedName("paginationSize")
    var paginationSize: Int? = null,
    @SerializedName("pageNo")
    var pageNo: Int? = null,
    @SerializedName("sortField")
    var sortField: String? = "patient_name",
    @SerializedName("sortOrder")
    var sortOrder: String? = "DESC"
)