package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request

data class StoreMaster(
    var store_code: String = "",
    var store_name: String = "",
    var store_type_uuid: Int = 0,
    var uuid: Int = 0
)