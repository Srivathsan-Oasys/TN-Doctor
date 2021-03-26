package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class SpecialitySketches(
    val speciality_sketches_details: List<SpecialitySketchesDetail> = listOf(),
    val speciality_sketches_headers: SpecialitySketchesHeaders = SpecialitySketchesHeaders()
)