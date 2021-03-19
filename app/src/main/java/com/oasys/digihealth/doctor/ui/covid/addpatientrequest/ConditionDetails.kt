package com.oasys.digihealth.doctor.ui.covid.addpatientrequest

data class ConditionDetails(
    var condition_uuid: Int = 0,
    var created_by: Int = 0,
    var created_date: String = "",
    var is_active: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var patient_uuid: Int = 0,
    var revision: Boolean = false,
    var status: Boolean = false,
    var uuid: Int = 0
)