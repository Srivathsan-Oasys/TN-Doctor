package com.oasys.digihealth.doctor.ui.emr_workflow.certificate.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CertificateResponseContents(
    val admission_status_uuid: Int = 0,
    val approved_on: String = "",
    val aproved_by: String = "",
    val certificate_status_uuid: Int = 0,
    val certified_by: String = "",
    val created_by: String = "",
    val created_date: String = "",
    val data_template: String = "",
    val department_uuid: String = "",
    val discharge_type_uuid: Int = 0,
    val doctor_uuid: String = "",
    val encounter_uuid: Int = 0,
    val facility_uuid: String = "",
    val modified_by: String = "",
    val modified_date: String = "",
    val note_template_uuid: Int = 0,
    val note_type_uuid: Int = 0,
    val patient_uuid: String = "",
    val released_by: String = "",
    val released_to_patient: Int = 0,
    val revision: Int = 0,
    val status: Boolean = false,
    val ward_master_uuid: Int = 0
) : Parcelable