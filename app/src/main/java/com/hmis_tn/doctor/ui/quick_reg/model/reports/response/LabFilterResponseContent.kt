package com.hmis_tn.doctor.ui.quick_reg.model.reports.response

data class LabFilterResponseContent(
    val userid: Int? = 0,
    val username: String? = "",
    val districtid: Int? = 0,
    val districtname: String? = "",
    val hudid: Int? = 0,
    val hudname: String? = "",
    val blockid: Int? = 0,
    val blockname: String? = "",
    val uuid: Int? = 0,
    val name: String? = "",
    val orderstatusid: Int? = 0,
    val orderstatusname: String? = "",
    val orderstatuscode: String? = "",
    val officeid: Int? = 0,
    val officename: String? = "",
    val departmentid: Int? = 0,
    val departmentname: String? = "",
    val facilitytypeid: Int? = 0,
    val facilitytypename: String? = "",
    val facilityid: Int? = 0,
    val facilityname: String? = "",
    val testid: Int? = 0,
    val testname: String? = "",
    val testtolocationid: Int? = 0,
    val testtolocationname: String? = ""
)