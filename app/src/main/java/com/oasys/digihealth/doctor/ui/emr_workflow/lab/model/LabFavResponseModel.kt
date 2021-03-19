package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model

data class LabFavResponseModel(
    var code: Int? = null,
    var message: String? = null,
    var responseContentLength: Int? = null,
    var responseContents: ArrayList<LabFavModel?>? = ArrayList()
)