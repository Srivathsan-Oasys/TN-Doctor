package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify

data class PatientDiagnosi(
    var new_diagnosis: ArrayList<NewDiagnosi>? = ArrayList(),
    var remove_details: ArrayList<RemoveDetail>? = ArrayList()
)