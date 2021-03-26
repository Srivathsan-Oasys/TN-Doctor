package com.hmis_tn.doctor.ui.covid.addpatientrequest

data class CovidRegisterPatientResponseModel(
    var responseContents: List<CovidRegisterPatient> = listOf(),
    var statusCode: Int = 0
)