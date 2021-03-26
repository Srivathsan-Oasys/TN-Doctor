package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.treatmentKitPresModel

import android.os.Parcel
import android.os.Parcelable

data class TKtPrescriptionListData(

    var drug_active: Boolean? = false,
    var drug_id: Int? = 0,
    var drug_name: String? = null,

    var drug_duration: Int? = 0,
    var drug_code: String? = "",
    var drug_quantity: String? = "1",

    var drug_strength: String? = "",

    var drug_frequency_id: Int? = 0,
    var drug_frequency_name: String? = "",
    var drug_frequency_code: String? = "",

    var drug_instruction_code: String? = null,
    var drug_instruction_id: Int? = 0,
    var drug_instruction_name: String? = "",


    var drug_route_code: String? = null,
    var drug_route_id: Int? = 0,
    var drug_route_name: String? = "",

    var drug_period_id: Int? = null,
    var drug_period_name: String? = "",
    var drug_period_code: String? = "",

    var store_master_uuid: Int? = 0,

    var commands: String? = "",

    var collapse: Boolean = true,
    var Update: Boolean = false,
    var drug_is_emar: Boolean? = false,
    var perday_quantity: String? = ""


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(drug_active)
        parcel.writeValue(drug_id)
        parcel.writeString(drug_name)
        parcel.writeValue(drug_duration)
        parcel.writeString(drug_code)
        parcel.writeString(drug_quantity)
        parcel.writeString(drug_strength)
        parcel.writeValue(drug_frequency_id)
        parcel.writeString(drug_frequency_name)
        parcel.writeString(drug_frequency_code)
        parcel.writeString(drug_instruction_code)
        parcel.writeValue(drug_instruction_id)
        parcel.writeString(drug_instruction_name)
        parcel.writeString(drug_route_code)
        parcel.writeValue(drug_route_id)
        parcel.writeString(drug_route_name)
        parcel.writeValue(drug_period_id)
        parcel.writeString(drug_period_name)
        parcel.writeString(drug_period_code)
        parcel.writeValue(store_master_uuid)
        parcel.writeString(commands)
        parcel.writeByte(if (collapse) 1 else 0)
        parcel.writeByte(if (Update) 1 else 0)
        parcel.writeValue(drug_is_emar)
        parcel.writeString(perday_quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TKtPrescriptionData> {
        override fun createFromParcel(parcel: Parcel): TKtPrescriptionData {
            return TKtPrescriptionData(parcel)
        }

        override fun newArray(size: Int): Array<TKtPrescriptionData?> {
            return arrayOfNulls(size)
        }
    }
}