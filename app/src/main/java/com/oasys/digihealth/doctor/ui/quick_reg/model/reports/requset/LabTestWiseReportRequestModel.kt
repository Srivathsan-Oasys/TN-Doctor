package com.oasys.digihealth.doctor.ui.quick_reg.model.reports.requset

data class LabTestWiseReportRequestModel(
    var district_Id: MutableList<Int> = mutableListOf(),
    var office_Id: MutableList<Int> = mutableListOf(),
    var institution_Id: MutableList<Int> = mutableListOf(),
    var hud_Id: MutableList<Int> = mutableListOf(),
    var block_Id: MutableList<Int> = mutableListOf(),
    var orderstatus_Id: MutableList<String> = mutableListOf(),
    var is_adult: MutableList<Int> = mutableListOf(),
    var testname_Id: MutableList<Int> = mutableListOf(),
    var test_Id: MutableList<Int> = mutableListOf(),
    var gender_Id: MutableList<Int> = mutableListOf(),
    var startdate: String? = "",
    var enddate: String? = "",
    var pageNo: Int? = 0,
    var paginationSize: Int? = 0
)