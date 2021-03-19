package com.oasys.digihealth.doctor.ui.covid.addpatientrequest

data class Specimenlist(
    var collection_date: String = "",
    var created_by: Int = 0,
    var created_date: String = "",
    var is_active: Boolean = false,
    var label: String = "",
    var modified_by: Int = 0,
    var modified_date: String = "",
    var patient_uuid: Int = 0,
    var specimen_type_uuid: Int = 0,
    var status: Boolean = false,
    var uuid: Int = 0
)