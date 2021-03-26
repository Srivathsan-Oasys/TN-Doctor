package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class ReqContents(
    val treatment_kit: TreatmentKit? = TreatmentKit(),
    val treatment_kit_diagnosis: List<TreatmentKitDiagnosi?>? = listOf(),
    val treatment_kit_drug: List<TreatmentKitDrug?>? = listOf(),
    val treatment_kit_investigation: List<TreatmentKitInvestigation?>? = listOf(),
    val treatment_kit_lab: List<TreatmentKitLab?>? = listOf(),
    val treatment_kit_radiology: List<TreatmentKitRadiology?>? = listOf()
)