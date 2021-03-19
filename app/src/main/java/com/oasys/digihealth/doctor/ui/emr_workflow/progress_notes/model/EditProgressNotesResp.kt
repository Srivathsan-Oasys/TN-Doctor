package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class EditProgressNotesResp(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("responseContent")
    var responseContent: ResponseContentXX?
)