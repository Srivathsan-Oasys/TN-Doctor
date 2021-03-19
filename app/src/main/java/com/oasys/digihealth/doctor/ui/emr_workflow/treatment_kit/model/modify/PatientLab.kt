package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify

data class PatientLab(
    var existing_details: ArrayList<Any>? = ArrayList(),
    var header: HeaderX? = HeaderX(),
    var new_details: ArrayList<NewDetailX>? = ArrayList(),
    var removed_details: ArrayList<Any>? = ArrayList()
)