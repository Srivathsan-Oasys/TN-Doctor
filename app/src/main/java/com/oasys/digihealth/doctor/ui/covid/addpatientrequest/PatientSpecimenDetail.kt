package com.oasys.digihealth.doctor.ui.covid.addpatientrequest

data class PatientSpecimenDetail(
    var collection_date: String? = "",
    var is_active: Boolean? = false,
    var label: String? = "",
    var specimen_type_uuid: Int? = 0
)