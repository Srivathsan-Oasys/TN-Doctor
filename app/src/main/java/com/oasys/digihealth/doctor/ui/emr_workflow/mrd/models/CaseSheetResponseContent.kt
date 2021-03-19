package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

import android.os.Parcel
import android.os.Parcelable

data class CaseSheetResponseContent(
    val created_date: String = "",
    val discharge_date: String = "",
    val en_admission_request_uuid: Int = 0,
    val en_admission_uuid: Int = 0,
    val en_created_by: Int = 0,
    val en_department_uuid: Int = 0,
    val en_doctor_uuid: Int = 0,
    val en_encounter_type_uuid: Int = 0,
    val en_facility_address1: String? = "",
    val en_facility_address2: String? = "",
    val en_facility_district_name: String? = "",
    val en_facility_landline: String? = "",
    val en_facility_name: String? = "",
    val en_facility_pincode: Int = 0,
    val en_facility_state_name: String? = "",
    val en_facility_uuid: Int = 0,
    val en_is_active: Boolean = false,
    val en_modified_by: Int = 0,
    val en_patient_uuid: Int = 0,
    val en_status: Boolean = false,
    val encounter_department_name: String? = "",
    val encounter_doctor_first_name: String? = "",
    val encounter_doctor_last_name: Any = Any(),
    val encounter_doctor_middle_name: Any = Any(),
    val encounter_doctor_title: String? = "",
    val encounter_doctor_user_name: String? = "",
    val encounter_uuid: Int = 0,
    val is_active_encounter: Boolean = false,
    val modified_date: String? = "",
    val patient_mobile: String? = "",
    val patient_pin: String? = "",
    val visit_date: String? = "",
    val visit_type: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        TODO("encounter_doctor_last_name"),
        TODO("encounter_doctor_middle_name"),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CaseSheetResponseContent> {
        override fun createFromParcel(parcel: Parcel): CaseSheetResponseContent {
            return CaseSheetResponseContent(parcel)
        }

        override fun newArray(size: Int): Array<CaseSheetResponseContent?> {
            return arrayOfNulls(size)
        }
    }

}

