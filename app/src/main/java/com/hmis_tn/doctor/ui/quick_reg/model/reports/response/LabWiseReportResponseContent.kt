package com.hmis_tn.doctor.ui.quick_reg.model.reports.response

data class LabWiseReportResponseContent(

    val Institution: String? = "",
    val District: String? = "",
    val labname: String? = "",
    val maleadult: Int? = 0,
    val femaleadult: Int? = 0,
    val transgenderadult: Int? = 0,
    val totaladult: Int? = 0,
    val malechild: Int? = 0,
    val femalechild: Int? = 0,
    val totalchild: Int? = 0,
    val total: Int? = 0
)