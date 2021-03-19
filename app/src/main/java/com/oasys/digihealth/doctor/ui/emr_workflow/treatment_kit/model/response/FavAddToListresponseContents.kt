package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response

data class FavAddToListresponseContents(
    val diagnosis_details: List<DiagnosisDetail?>? = listOf(),
    val drug_details: List<DrugDetail?>? = listOf(),
    val favourite_details: FavouriteDetails? = FavouriteDetails(),
    val lab_details: List<LabDetail?>? = listOf(),
    val radiology_details: List<RadiologyDetail?>? = listOf(),
    val investigation_details: List<InvestigationDetail?>? = listOf(),
    val treatment_active: Boolean? = false,
    val treatment_code: String? = "",
    val treatment_id: Int? = 0,
    val treatment_name: String? = ""
)