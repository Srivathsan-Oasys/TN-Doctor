package com.hmis_tn.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResDepartmentList(
    @SerializedName("statusCode")
    var statusCode: String? = "",
    @SerializedName("responseContents")
    var responseContents: List<DepartmentList>? = null
)

data class DepartmentList(
    val uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val department_uuid: Int? = 0,
    val revision: String? = "",
    val status: Boolean? = false,
    val is_active: Boolean? = false,
    val created_by: String? = "",
    val created_date: String? = "",
    val modified_by: String? = "",
    val modified_date: String? = "",
    @SerializedName("department")
    val department: DepartmentMain? = null
)

data class DepartmentMain(
    val code: String? = "",
    val name: String? = "",
    val uuid: Int? = 0
)