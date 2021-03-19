package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse

data class FavSearchResponce(
    val responseContents: ArrayList<FavSearch> = ArrayList(),
    val message: String = "",
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)