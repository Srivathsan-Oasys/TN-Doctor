package com.oasys.digihealth.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResUserTypeIds(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("totalRecords")
    var totalRecords: Int? = null,
    @SerializedName("responseContents")
    var responseContents: List<ResUserTypeIdsList>? = null
)

data class ResUserTypeIdsList(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("user_name")
    var user_name: String? = null,
    @SerializedName("first_name")
    var first_name: String? = null,
    @SerializedName("middle_name")
    var middle_name: String? = null,
    @SerializedName("last_name")
    var last_name: String? = null,
    @SerializedName("user_type_uuid")
    var user_type_uuid: Int? = null,
    @SerializedName("user_type_name")
    var user_type_name: String? = null,
    @SerializedName("user_department")
    var user_department: List<UserDepartment>? = null,
    @SerializedName("facility_details")
    var facility_details: FacilityDetails? = null

    )

data class UserDepartment(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("user_uuid")
    var user_uuid: Int? = null,
    @SerializedName("department_uuid")
    var department_uuid: Int? = null,
    @SerializedName("department")
    var department: Department? = null
)

data class Department(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("name")
    var name: String? = null
)

data class FacilityDetails(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("name")
    var name: String? = null
)