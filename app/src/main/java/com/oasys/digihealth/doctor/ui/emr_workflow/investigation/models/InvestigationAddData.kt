package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.models

class InvestigationAddData(
    var selectToLocationUUID: Int = 0,
    var selectedLocationName: String = "",
    var selectTypeUUID: Int = 0,
    var selectTypeName: String? = null,
    var isReadyForSave: Boolean? = false,
    var isEditableMode: Boolean? = false,
    var labDataSelected: Boolean? = false,
    var investigation_id: Int? = 0,
    var commands: String? = null,
    var investigation_name: String? = null,
    var investigation_code: String? = null,
    var isFav: Boolean? = false,
    var IsTemp: Boolean? = false,
    var favPos: Int? = 0,
    var tempPos: Int? = 0

)
