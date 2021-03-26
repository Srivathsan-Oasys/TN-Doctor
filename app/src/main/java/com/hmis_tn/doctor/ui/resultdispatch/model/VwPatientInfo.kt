package com.hmis_tn.doctor.ui.resultdispatch.model

data class VwPatientInfo(
    var age: String? = "",
    var ageperiod: String? = "",
    var first_name: String? = "",
    var last_name: String? = "",
    var middle_name: String? = "",
    var pattitle: String? = "",
    var uhid: String? = "",
    var uuid: Int? = 0
)