package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

data class DischargeResult(
    val allergy: Allergy = Allergy(),
    val cheif_complaints: CheifComplaints = CheifComplaints(),
    val diagnosis: Diagnosis = Diagnosis(),
    val investigation: Investigation = Investigation(),
    val lab: Lab = Lab(),
    val patient_info: PatientInfo = PatientInfo(),
    val prescription: Prescription = Prescription(),
    val radiology: Radiology = Radiology(),
    val vitals: Vitals = Vitals()
)