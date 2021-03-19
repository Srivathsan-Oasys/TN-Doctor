package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class ResponseContent(
    @SerializedName("code")
    val code: String?,
    @SerializedName("color")
    val color: Any?,
    @SerializedName("created_by")
    val createdBy: Int?,
    @SerializedName("created_date")
    val createdDate: String?,
    @SerializedName("display_order")
    val displayOrder: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("Is_default")
    val isDefault: Boolean?,
    @SerializedName("language")
    val language: Int?,
    @SerializedName("modified_by")
    val modifiedBy: Int?,
    @SerializedName("modified_date")
    val modifiedDate: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("revision")
    val revision: Int?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("uuid")
    val uuid: Int?
)