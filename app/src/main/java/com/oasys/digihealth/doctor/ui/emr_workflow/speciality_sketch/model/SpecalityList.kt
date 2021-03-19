package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model

data class SpecalityList(
    var code: String? = null,
    var created_by: Int? = null,
    var created_date: String? = null,
    var department_name: String? = null,
    var department_uuid: Int? = null,
    var description: String? = null,
    var is_active: Boolean? = null,
    var modified_by: Int? = null,
    var modified_date: String? = null,
    var name: String? = null,
    var revision: Int? = null,
    var sketch_name: String? = null,
    var speciality_sketch_details: List<SpecialitySketchDetail>? = null,
    var status: Boolean? = null,
    var uuid: Int? = null
)