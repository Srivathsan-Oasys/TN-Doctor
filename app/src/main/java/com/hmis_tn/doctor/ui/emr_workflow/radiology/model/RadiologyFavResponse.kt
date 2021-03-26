package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class RadiologyFavResponse(
    var code: Int? = null,
    var message: String? = null,
    var responseContentLength: Int? = null,
    var responseContents: ArrayList<RadiologyFavData?>? = ArrayList()
)