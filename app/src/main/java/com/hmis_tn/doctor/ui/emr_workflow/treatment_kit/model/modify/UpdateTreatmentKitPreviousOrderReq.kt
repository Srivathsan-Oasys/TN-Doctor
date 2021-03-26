package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.modify

data class UpdateTreatmentKitPreviousOrderReq(
    var patientDiagnosis: ArrayList<PatientDiagnosi>? = ArrayList(),
    var patientInvestigation: PatientInvestigation? = PatientInvestigation(),
    var patientLab: PatientLab? = PatientLab(),
    var patientPrescription: PatientPrescription? = PatientPrescription(),
    var patientRadiology: PatientRadiology? = PatientRadiology(),
    var patientTreatment: PatientTreatment? = PatientTreatment()
)