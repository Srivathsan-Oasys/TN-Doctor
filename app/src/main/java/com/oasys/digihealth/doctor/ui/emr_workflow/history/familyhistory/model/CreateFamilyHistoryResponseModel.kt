package com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.model

data class CreateFamilyHistoryResponseModel(
    val code: Int? = 0,
    val responseContents: responseContents? = responseContents(),
    val message: String? = ""
)