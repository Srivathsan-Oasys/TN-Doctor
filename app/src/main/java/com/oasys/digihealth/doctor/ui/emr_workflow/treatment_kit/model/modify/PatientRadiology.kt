package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify

data class PatientRadiology(
    var existing_details: ArrayList<NewDetailX>? = ArrayList(),
    var new_details: ArrayList<Any>? = ArrayList(),
    var removed_details: ArrayList<Any>? = ArrayList()
)