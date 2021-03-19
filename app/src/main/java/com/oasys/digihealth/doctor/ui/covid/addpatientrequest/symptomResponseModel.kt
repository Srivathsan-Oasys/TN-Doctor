package com.oasys.digihealth.doctor.ui.covid.addpatientrequest

import com.oasys.digihealth.doctor.ui.covid.addpatientrequest.Symptomlist

data class symptomResponseModel(
    var statusCode: Int = 0,
    var responseContents: ArrayList<Symptomlist> = ArrayList()
)