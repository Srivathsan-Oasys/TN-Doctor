package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class ResponseContentX(
    @SerializedName("code")
    val code: String?,
    @SerializedName("color")
    val color: Any?,
    @SerializedName("created_by")
    val createdBy: Int?,
    @SerializedName("created_date")
    val createdDate: String?,
    @SerializedName("display_order")
    val displayOrder: Any?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("is_default")
    val isDefault: Any?,
    @SerializedName("language")
    val language: Any?,
    @SerializedName("modified_by")
    val modifiedBy: Int?,
    @SerializedName("modified_date")
    val modifiedDate: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("revision")
    val revision: Boolean?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("uuid")
    val uuid: Int?
)