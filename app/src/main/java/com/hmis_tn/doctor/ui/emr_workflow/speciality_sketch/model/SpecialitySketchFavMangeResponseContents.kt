package com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.model

data class SpecialitySketchFavMangeResponseContents(
    val s_uuid: Int,
    val s_code: String,
    val testemr: String,
    val s_name: String,
    val s_description: String,
    val s_sketch_name: String
)