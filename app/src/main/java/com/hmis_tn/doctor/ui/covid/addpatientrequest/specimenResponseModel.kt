package com.hmis_tn.doctor.ui.covid.addpatientrequest

data class specimenResponseModel(
    var responseContents: ArrayList<Specimenlist> = ArrayList(),
    var statusCode: Int = 0
)