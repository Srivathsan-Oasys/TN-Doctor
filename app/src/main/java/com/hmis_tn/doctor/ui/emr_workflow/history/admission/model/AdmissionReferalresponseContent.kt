package com.hmis_tn.doctor.ui.emr_workflow.history.admission.model

data class AdmissionReferalresponseContent(
    val d_name: String? = "",
    val d_uuid: Int? = 0,
    val f_name: String? = "",
    val f_uuid: Int? = 0,
    val pr_referral_date: String? = "",
    val pr_referral_deptartment_uuid: Int? = 0,
    val pr_uuid: Int? = 0,
    val rd_name: String? = "",
    val rf_name: String? = "",
    val u_first_name: String? = "",
    val u_last_name: String? = "",
    val u_middle_name: String? = ""
)