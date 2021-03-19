package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response

data class OtSurgeryNameresponseContent(
    val d_code: String? = "",
    val d_name: String? = "",
    val f_description: String? = "",
    val f_name: String? = "",
    val f_uuid: Int? = 0,
    val opm_department_uuid: Int? = 0,
    val opm_facility_uuid: Int? = 0,
    val opm_is_active: Boolean? = false,
    val opm_status: Boolean? = false,
    val opm_uuid: Int? = 0,
    val p_code: String? = "",
    val p_name: String? = "",
    val p_uuid: Int? = 0
)