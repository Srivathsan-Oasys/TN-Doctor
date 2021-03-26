package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class FavouriteAddToListResponseModel(
    val responseContents: FavAddToListresponseContents? = FavAddToListresponseContents(),
    val code: Int? = 0,
    val message: String? = "",
    val responseContentLength: Int? = 0
)