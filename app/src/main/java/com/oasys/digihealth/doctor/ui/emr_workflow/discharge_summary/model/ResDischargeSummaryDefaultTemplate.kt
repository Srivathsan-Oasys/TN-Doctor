package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResDischargeSummaryDefaultTemplate(
    val message: String = "",
    val statusCode: Int = 0,
    @SerializedName("responseContent")
    val responseContents: List<ResDefaultTemplate> = listOf()
)

data class ResDefaultTemplate(
    val uuid: Int? = 0,
    val user_uuid: Int? = 0,
    val template_id: Int? = 0,
    val created_by: Int? = 0,
    val modified_by: Int? = 0,
    val is_active: Boolean? = false,
    val revision: Boolean? = false,
    val created_date: String? = "",
    val modified_date: String? = ""
)