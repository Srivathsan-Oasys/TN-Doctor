package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreaFavAddedResponseModel(
    val responseContents: FavAddTreatresponseContents? = FavAddTreatresponseContents(),
    val code: Int? = 0,
    val message: String? = ""
)