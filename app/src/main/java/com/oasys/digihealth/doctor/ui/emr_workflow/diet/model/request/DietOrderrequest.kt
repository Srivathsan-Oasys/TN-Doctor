package com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request

data class DietOrderrequest(
    var details: List<DietOrderrequestDetail> = ArrayList(),
    var headers: DietOrderrequestHeaders = DietOrderrequestHeaders()
)


