package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search

data class ResponseContents(
    val diagnosis_details: List<DiagnosisDetail>?,
    val drug_details: List<DrugDetail>?,
    val lab_details: List<LabDetail>?,
    val radiology_details: List<RadiologyDetail>?,
    val investigation_details: List<InvestigationDetails>?,
    val treatment_active: Boolean?,
    val treatment_code: String?,
    val treatment_id: Int?,
    val treatment_name: String?
)