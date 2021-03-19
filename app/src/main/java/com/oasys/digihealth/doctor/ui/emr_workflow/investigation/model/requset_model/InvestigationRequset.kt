package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.requset_model

data class InvestigationRequset(
    var details: List<Detail> = listOf(),
    var header: Header = Header()
)