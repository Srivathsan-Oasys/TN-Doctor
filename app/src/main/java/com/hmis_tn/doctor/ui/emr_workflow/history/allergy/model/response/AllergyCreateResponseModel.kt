package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response

data class AllergyCreateResponseModel(
    val responseContents: Allergycreateres? = Allergycreateres(),
    val code: Int? = 0,
    val message: String? = ""
)