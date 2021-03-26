package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.ResChiefComplaintsData


data class DischargeResult(
    val admission_request_uuid: Int = 0,
    val admission_status_uuid: Int = 0,
    val admission_uuid: Int = 0,
    val cheif_complaints: ResChiefComplaintsData = ResChiefComplaintsData(),
    val allergy: Allergy = Allergy(),
    val diagnosis: Diagnosis = Diagnosis(),
    val radiology: Radiology = Radiology(),
    val vitals: Vitals = Vitals(),
    val investigation: Investigation = Investigation(),
    val prescription: Prescription = Prescription(),
    val department_uuid: Int = 0,
    val doctor_uuid: Int = 0,
    val encounter_type_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val lab: Lab = Lab(),
    val patient_info: PatientInfo = PatientInfo(),
    val patient_uuid: Int = 0,
    val speciality_sketches: SpecialitySketches = SpecialitySketches(),
    val dischrage_medication: DischrageMedication = DischrageMedication()
)