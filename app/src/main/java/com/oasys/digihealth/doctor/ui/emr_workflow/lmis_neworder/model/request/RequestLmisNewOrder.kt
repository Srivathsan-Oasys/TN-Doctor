package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request

data class RequestLmisNewOrder(
    var details: List<Detail?>? = listOf(),
    var header: Header? = Header()
)