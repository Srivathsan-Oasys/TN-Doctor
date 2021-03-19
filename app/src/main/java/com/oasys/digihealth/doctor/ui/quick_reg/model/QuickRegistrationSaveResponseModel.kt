package com.oasys.digihealth.doctor.ui.quick_reg.model

data class QuickRegistrationSaveResponseModel(
    val responseContent: QuickRegistrationSaveresponseContent? = QuickRegistrationSaveresponseContent(),
    val statusCode: Int? = 0
)