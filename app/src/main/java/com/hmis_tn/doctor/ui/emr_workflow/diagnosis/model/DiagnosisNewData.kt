package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model

import android.os.Parcel
import android.os.Parcelable


data class DiagnosisNewData(
    var diagnosis_code: String? = "",
    var diagnosis_description: String? = "",
    var diagnosis_id: String? = "",
    var diagnosis_name: String? = "",
    var drug_active: Boolean? = false,

    var diagnosisUUiD: Int = 0,
    var favourite_active: Boolean? = false,
    val favourite_code: String? = "",
    val favourite_type_id: Int? = 0,
    var isModifiy: Boolean? = false,
    var isReadyForSave: Boolean? = true,
    var viewDiagnosisstatus: Int = 0,
    var isFavposition: Int = 0,
    var is_snomed: Int = 0,
    var commands: String = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(diagnosis_code)
        parcel.writeString(diagnosis_description)
        parcel.writeString(diagnosis_id)
        parcel.writeString(diagnosis_name)
        parcel.writeValue(drug_active)
        parcel.writeInt(diagnosisUUiD)
        parcel.writeValue(favourite_active)
        parcel.writeString(favourite_code)
        parcel.writeValue(favourite_type_id)
        parcel.writeValue(isModifiy)
        parcel.writeValue(isReadyForSave)
        parcel.writeInt(viewDiagnosisstatus)
        parcel.writeInt(isFavposition)
        parcel.writeInt(is_snomed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DiagnosisNewData> {
        override fun createFromParcel(parcel: Parcel): DiagnosisNewData {
            return DiagnosisNewData(parcel)
        }

        override fun newArray(size: Int): Array<DiagnosisNewData?> {
            return arrayOfNulls(size)
        }
    }
}