package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model

data class OrderSaveTreatmentKitResponseModel(
    val OrderSaveresponseContents: OrderSaveresponseContents? = OrderSaveresponseContents(),
    val code: Int? = 0,
    val message: String? = ""
)