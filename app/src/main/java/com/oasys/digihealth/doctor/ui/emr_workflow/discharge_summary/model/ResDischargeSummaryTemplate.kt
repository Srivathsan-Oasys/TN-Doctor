package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

class ResDischargeSummaryTemplate(
    val message: String = "",
    @SerializedName("responseContents")
    val responseContents: List<ResDischargeSummaryNoteTemplate> = listOf(),
    val statusCode: Int = 0
)

data class ResDischargeSummaryNoteTemplate(
    val nt_uuid: Int? = 0,
    val nt_code: String? = "",
    val nt_name: String? = "",
    val nt_note_template_type_uuid: Int? = 0,
    val nt_facility_uuid: Int? = 0,
    val nt_department_uuid: Int? = 0,
    val nt_data_template: String? = "",
    val nt_is_default: Boolean? = false,
    val nt_revision: Int? = 0,
    val nt_created_by: Int? = 0,
    val nt_modified_by: Int? = 0,
    val nt_created_date: String? = "",
    val modified_date: String? = "",
    val nt_status: Boolean? = false,
    val nt_is_active: Boolean? = false,
    val ntt_uuid: Int? = 0,
    val ntt_name: String? = "",
    val ntt_status: Boolean? = false,
    val ntt_is_active: Boolean? = false,
    val nty_uuid: Int? = 0,
    val nty_name: String? = "",
    val nty_status: Boolean? = false,
    val nty_is_active: Boolean? = false,
    var d_uuid: Int? = 0,
    var d_name: String? = "",
    var d_status: Boolean? = false,
    var d_is_active: Boolean? = false,
    var f_uuid: Int? = 0,
    var f_name: String? = "",
    var f_status: Boolean? = false,
    var f_is_active: Boolean? = false
)