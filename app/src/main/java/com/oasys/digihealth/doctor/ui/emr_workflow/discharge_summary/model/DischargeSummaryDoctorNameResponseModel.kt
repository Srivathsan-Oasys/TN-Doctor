package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class DischargeSummaryDoctorNameResponseModel(
    val req: Req = Req(),
    @SerializedName("responseContents")
    val responseContents: List<DischargeSummaryDoctorRes> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)

data class DischargeSummaryDoctorRes(
    var uuid: Int? = 0,
    val user_name: String? = null,
    var first_name: String? = null,
    var middle_name: String? = null,
    var last_name: String? = null,
    val facility: Facility? = null,
    val user_type: UserType? = null,
    @SerializedName("username_facilityname")
    val usernameFacilityName: String? = null
)

data class Facility(
    var uuid: Int? = 0,
    val name: String? = null
)

data class UserType(
    var uuid: Int? = 0,
    val code: String? = null,
    val name: String? = null
)