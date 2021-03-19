package com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request


import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.Headers


data class RequestDietFavModel(

    var details: List<Detail> = listOf(),
    var headers: Headers = Headers()
)