package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class CreateProgressNotesResp(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("responseContents")
    var responseContents: ArrayList<ResponseContents?>?
)