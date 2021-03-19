package com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response

data class LabConsolidatedReportResponseContent(
    val age: Int? = 0,
    val orderstatusid: Int? = 0,
    val pinnumber: String? = "",
    val district: String? = "",
    val institutiontype: String? = "",
    val institutionname: String? = "",
    val labname: String? = "",
    val patientname: String? = "",
    val orderstatusname: String? = "",
    val testcodename: String? = "",
    val periodname: String? = "",
    val gender: String? = "",
    val salutation: String? = ""
)