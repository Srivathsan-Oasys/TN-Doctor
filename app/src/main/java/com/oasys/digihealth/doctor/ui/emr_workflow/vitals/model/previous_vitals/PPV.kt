package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.previous_vitals

data class PPV(
    val PV_list: List<PV>? = listOf(),
    val created_by_firstname: String? = "",
    val created_by_lastlename: Any? = Any(),
    val created_by_middlename: Any? = Any(),
    val created_date: String? = "",
    val encounter_type_code: String? = "",
    val encounter_type_name: String? = "",
    val patient_uuid: Int? = 0,
    val salutaion_name: String? = ""
)