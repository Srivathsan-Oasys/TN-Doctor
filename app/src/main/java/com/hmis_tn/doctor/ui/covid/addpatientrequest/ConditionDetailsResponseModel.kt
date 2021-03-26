package com.hmis_tn.doctor.ui.covid.addpatientrequest

data class ConditionDetailsResponseModel(
    var responseContents: ArrayList<ConditionDetails> = ArrayList(),
    var statusCode: Int = 0
)