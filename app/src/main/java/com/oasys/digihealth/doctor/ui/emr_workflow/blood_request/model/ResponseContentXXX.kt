package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class ResponseContentXXX(
    @SerializedName("code")
    var code: String?,
    @SerializedName("color")
    var color: Any?,
    @SerializedName("created_by")
    var createdBy: Int?,
    @SerializedName("created_date")
    var createdDate: String?,
    @SerializedName("display_order")
    var displayOrder: Any?,
    @SerializedName("is_active")
    var isActive: Boolean?,
    @SerializedName("Is_default")
    var isDefault: Any?,
    @SerializedName("language")
    var language: Any?,
    @SerializedName("modified_by")
    var modifiedBy: Int?,
    @SerializedName("modified_date")
    var modifiedDate: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("revision")
    var revision: Int?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("uuid")
    var uuid: Int?
) {
    var isChecked: Boolean = false
    var text: String = ""
}