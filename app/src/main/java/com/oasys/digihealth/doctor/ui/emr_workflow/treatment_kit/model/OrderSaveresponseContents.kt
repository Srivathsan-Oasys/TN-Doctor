package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

data class OrderSaveresponseContents(
    val labCreated: List<LabCreated?>? = listOf(),
    val patientDgnsCreatedData: List<PatientDgnsCreatedData?>? = listOf(),
    val patientTKCreatedData: PatientTKCreatedData? = PatientTKCreatedData(),
    val prescriptionCreated: PrescriptionCreated? = PrescriptionCreated()
)