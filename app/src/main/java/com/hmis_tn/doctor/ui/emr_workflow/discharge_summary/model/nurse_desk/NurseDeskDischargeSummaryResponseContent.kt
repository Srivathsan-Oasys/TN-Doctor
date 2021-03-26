package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NurseDeskDischargeSummaryResponseContent(
    val admission_admission_status_name: String = "",
    val admission_bed_uuid: Int = 0,
    val admission_code: String = "",
    val admission_date: String = "",
    val admission_death_type_uuid: Int? = null,
    val admission_discharge_date: String = "",
    val admission_doctor_uuid: Int = 0,
    val admission_encounter_type_uuid: Int = 0,
    val admission_encounter_uuid: Int = 0,
    val admission_is_casualty: Boolean = false,
    val admission_patient_uuid: Int = 0,
    val admission_reason: String = "",
    val admission_room_uuid: Int = 0,
    val admission_status_uuid: Int = 0,
    val admission_uuid: Int = 0,
    val admission_ward_uuid: Int = 0,
    val created_by_user_name: String = "",
    val department_name: String = "",
    val facility_name: String = "",
    val modified_by_user_name: String = "",
    val patient_age: Int = 0,
    val patient_first_name: String = "",
    val patient_info: String = "",
    val patient_gender_name: String = "",
    val patient_last_name: String = "",
    val patient_middle_name: String = "",
    val patient_mobile: String = "",
    val patient_title_name: String = "",
    val patient_uhid: String = "",
    val patients_uuid: Int = 0,
    val ward_master_name: String = "",
    @SerializedName("ward_transfer_status")
    val ward_transfer_status: WardTransferStatus? = null
) : Parcelable {
    var fromAdmission:Boolean = false
}

@Parcelize
data class WardTransferStatus(
    val uuid: Int = 0,
    val code: String = "",
    val name: String = ""
) : Parcelable