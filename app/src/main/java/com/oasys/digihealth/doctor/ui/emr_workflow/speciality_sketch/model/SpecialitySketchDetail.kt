package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model

import android.graphics.Bitmap

data class SpecialitySketchDetail(
    var created_by: Int? = null,
    var is_active: Boolean? = null,
    var modified_by: Int? = null,
    var sketch_path: String? = null,
    var speciality_sketch_uuid: Int? = null,
    var status: Boolean? = null,
    var bytestream: Bitmap? = null,
    var uuid: Int? = null
)