package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class TemplatesInvest(
    val Invest_details: ArrayList<InvestDetail> = ArrayList(),
    val temp_details: TempDetails = TempDetails(),
    var collapse: Boolean = true
)