package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.nurse_desk

import android.os.Parcel
import android.os.Parcelable

data class BedManagementPatientListResponseContent(
    val admission_admission_status_name: String = "",
    val admission_bed_uuid: Int = 0,
    val admission_date: String? = "",
    val admission_department_uuid: Int = 0,
    val admission_discharge_date: Any = Any(),
    val admission_doctor_uuid:Int = 0,
    val admission_encounter_type_uuid: Int = 0,
    val admission_encounter_uuid: Int = 0,
    val admission_is_active: Boolean = false,
    val admission_is_casualty: Boolean = false,
    val admission_reason: String? = "",
    val admission_room_uuid: Int = 0,
    val admission_status: Boolean = false,
    val admission_status_uuid: Int = 0,
    val admission_uuid: Int = 0,
    val admission_ward_uuid: Int = 0,
    val gender_uuid: Int = 0,
    val patient_age: Int = 0,
    val patient_first_name: String? = "",
    val patient_info: String? = "",
    val patient_gender_code: String? = "",
    val patient_gender_name: String? = "",
    val patient_last_name: String? = "",
    val patient_middle_name: String? = "",
    val patient_mobile: String? = "",
    val patient_title_name: String? = "",
    val patient_uhid: String? = "",
    val patients_uuid: Int = 0,
    val ward_master_name: String? = "",
    val wbm_bed_number: Int = 0,
    val ward_transfer_status: WardTrasanferStatus? =null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        TODO("admission_discharge_date"),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        TODO("ward_transfer_status")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(admission_admission_status_name)
        parcel.writeInt(admission_bed_uuid)
        parcel.writeString(admission_date)
        parcel.writeInt(admission_department_uuid)
        parcel.writeInt(admission_doctor_uuid)
        parcel.writeInt(admission_encounter_type_uuid)
        parcel.writeInt(admission_encounter_uuid)
        parcel.writeByte(if (admission_is_active) 1 else 0)
        parcel.writeByte(if (admission_is_casualty) 1 else 0)
        parcel.writeString(admission_reason)
        parcel.writeInt(admission_room_uuid)
        parcel.writeByte(if (admission_status) 1 else 0)
        parcel.writeInt(admission_status_uuid)
        parcel.writeInt(admission_uuid)
        parcel.writeInt(admission_ward_uuid)
        parcel.writeInt(gender_uuid)
        parcel.writeInt(patient_age)
        parcel.writeString(patient_first_name)
        parcel.writeString(patient_info)
        parcel.writeString(patient_gender_code)
        parcel.writeString(patient_gender_name)
        parcel.writeString(patient_last_name)
        parcel.writeString(patient_middle_name)
        parcel.writeString(patient_mobile)
        parcel.writeString(patient_title_name)
        parcel.writeString(patient_uhid)
        parcel.writeInt(patients_uuid)
        parcel.writeString(ward_master_name)
        parcel.writeInt(wbm_bed_number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BedManagementPatientListResponseContent> {
        override fun createFromParcel(parcel: Parcel): BedManagementPatientListResponseContent {
            return BedManagementPatientListResponseContent(parcel)
        }

        override fun newArray(size: Int): Array<BedManagementPatientListResponseContent?> {
            return arrayOfNulls(size)
        }
    }
}