package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class InjectionDepartment(
    var gender_uuid: Int = 0,
    var store_code: String = "",
    var store_departments: List<StoreDepartment> = listOf(),
    var store_name: String = "",
    var store_type: StoreType = StoreType(),
    var store_type_uuid: Int = 0,
    var uuid: Int = 0
)