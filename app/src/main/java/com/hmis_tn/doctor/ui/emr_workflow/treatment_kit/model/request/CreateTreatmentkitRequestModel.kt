package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request

data class CreateTreatmentkitRequestModel(
    val treatment_kit: TreatmentKit? = TreatmentKit(),
    var treatment_kit_diagnosis: List<TreatmentKitDiagnosis?>? = listOf(),
    var treatment_kit_drug: ArrayList<TreatmentKitPrescription?>? = ArrayList(),
    var treatment_kit_investigation: List<TreatmentKitInvestigation?>? = listOf(),
    var treatment_kit_lab: ArrayList<TreatmentKitLab?>? = ArrayList(),
    var treatment_kit_radiology: ArrayList<TreatmentKitRadiology>? = ArrayList()
)