package com.hmis_tn.doctor.ui.covid.addpatientrequest

data class symptomResponseModel(
    var statusCode: Int = 0,
    var responseContents: ArrayList<Symptomlist> = ArrayList()
)