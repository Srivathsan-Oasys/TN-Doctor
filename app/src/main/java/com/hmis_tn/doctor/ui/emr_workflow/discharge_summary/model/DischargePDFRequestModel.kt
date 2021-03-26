package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model

import android.os.Parcel
import android.os.Parcelable

data class DischargePDFRequestModel(
    var allergy_uuids: List<Int> = listOf(),
    var chief_complaint_uuids: List<Any> = listOf(),
    val diagnosis_uuids: List<Int> = listOf(),
    var encounter_uuid: Int = 0,
    var investigation_uuids: List<Any> = listOf(),
    var lab_uuids: List<Int> = listOf(),
    var patient_uuid: Int = 0,
    var prescription_uuids: List<Int> = listOf(),
    var radiology_uuids: List<Any> = listOf(),
    var vitals_uuids: List<Int> = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("allergy_uuids"),
        TODO("chief_complaint_uuids"),
        TODO("diagnosis_uuids"),
        parcel.readInt(),
        TODO("investigation_uuids"),
        TODO("lab_uuids"),
        parcel.readInt(),
        TODO("prescription_uuids"),
        TODO("radiology_uuids"),
        TODO("vitals_uuids")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(encounter_uuid)
        parcel.writeInt(patient_uuid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DischargePDFRequestModel> {
        override fun createFromParcel(parcel: Parcel): DischargePDFRequestModel {
            return DischargePDFRequestModel(parcel)
        }

        override fun newArray(size: Int): Array<DischargePDFRequestModel?> {
            return arrayOfNulls(size)
        }
    }

}