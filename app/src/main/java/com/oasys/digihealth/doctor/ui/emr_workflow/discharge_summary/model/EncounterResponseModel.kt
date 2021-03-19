package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class EncounterResponseModel(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("responseContents")
    val responseContents: List<EncounterResponseContent> = listOf()
)