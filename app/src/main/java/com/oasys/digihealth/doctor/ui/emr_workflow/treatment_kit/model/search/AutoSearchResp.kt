package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search

data class AutoSearchResp(
    val code: Int?,
    val message: String?,
    val responseContents: List<ResponseContent>?,
    val responseLength: Int?
)