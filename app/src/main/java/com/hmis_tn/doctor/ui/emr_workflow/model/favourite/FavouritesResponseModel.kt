package com.hmis_tn.doctor.ui.emr_workflow.model.favourite

data class FavouritesResponseModel(
    var code: Int? = null,
    var message: String? = null,
    var responseContentLength: Int? = null,
    var responseContents: ArrayList<FavouritesModel?>? = ArrayList()
)