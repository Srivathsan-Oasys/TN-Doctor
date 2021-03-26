package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request

data class TKOrderRequestModel(
    var patientDiagnosis: List<PatientDiagnosis?>? = listOf(),
    var patientLab: PatientLab? = PatientLab(),
    var patientPrescription: PatientPrescription? = PatientPrescription(),
    var patientRadiology: PatientRadiology? = PatientRadiology(),
    var patientInvestigation: PatientInvestigation? = PatientInvestigation(),
    var patientTreatment: PatientTreatment? = PatientTreatment()
)