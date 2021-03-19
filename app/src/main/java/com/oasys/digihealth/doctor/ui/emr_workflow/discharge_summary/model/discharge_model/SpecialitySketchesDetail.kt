package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class SpecialitySketchesDetail(
    val date: String = "",
    val encounter_uuid: Int = 0,
    val patient_speciality_sketche_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val sketch_path: String? = null,
    val speciality_sketch_code: String = "",
    val speciality_sketch_name: String = "",
    val speciality_sketch_uuid: Int = 0
)