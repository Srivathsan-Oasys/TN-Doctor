package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class UpdateProgressNotesResp(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("responseContents")
    var responseContents: List<Int>?
)