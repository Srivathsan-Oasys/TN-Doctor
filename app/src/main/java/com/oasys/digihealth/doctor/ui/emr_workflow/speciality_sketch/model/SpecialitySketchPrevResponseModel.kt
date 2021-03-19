package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model


data class SpecialitySketchPrevResponseModel(
    val statusCode: Int,
    val message: String,
    val responseContents: List<SpecialitySketchPrevContent> = listOf()
)





