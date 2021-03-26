package com.hmis_tn.doctor.ui.quick_reg.model

data class QuickRegistrationUpdateresponseContent(
    val age: Int? = 0,
    val dob: String? = "",
  //  val fingerPrintData: FingerPrintDataX? = FingerPrintDataX(),
    val first_name: String? = "",
    val gender_uuid: Int? = 0,
    val is_adult: Int? = 0,
    val is_dob_auto_calculate: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
//    val patientUpdateRes: PatientUpdateRes? = PatientUpdateRes(),
 //   val patient_details: PatientDetailsX? = PatientDetailsX(),
    val patient_visits: PatientVisitsX? = PatientVisitsX(),
    val period_uuid: Int? = 0,
    val registred_facility_uuid: String? = "",
  //  val revision: Int? = 0,
    val uhid: String? = "",
    val uuid: Int? = 0
)