package com.hmis_tn.doctor.ui.emr_workflow.diet.model

data class DietFavMangeResponseContents(
    val details: List<DietDetail>,
    val headers: DietHeaders
)