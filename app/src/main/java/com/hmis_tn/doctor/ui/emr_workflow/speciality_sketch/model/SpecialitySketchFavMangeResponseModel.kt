package com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.model


data class SpecialitySketchFavMangeResponseModel(
    val statusCode: Int,
    val message: String,
    val responseContents: List<SpecialitySketchFavMangeResponseContents> = listOf()
)





