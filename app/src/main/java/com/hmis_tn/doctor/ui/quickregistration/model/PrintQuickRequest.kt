package com.hmis_tn.doctor.ui.quickregistration.model

import android.os.Parcel
import android.os.Parcelable

data class PrintQuickRequest(
    var addressDetails: AddressDetails = AddressDetails(),
    var age: Int = 0,
    var community: String = "",
    var componentName: String = "",
    var department: String = "",
    var dob: String = "",
    var facilityName: String = "",
    var firstName: String = "",
    var gender: String = "",
    var ili: Boolean = false,
    var is_dob_auto_calculate: Int = 1,
    var lastName: String = "",
    var middleName: String = "",
    var mobile: String = "",
    var noSymptom: Boolean = false,
    var period: String = "",
    var professional: String = "",
    var registered_date: String = "",
    var salutation: String = "",
    var sari: Boolean = false,
    var session: String = "",
    var suffixCode: String = "",
    var fatherName: String? = null,
    var aadhaarNumber: String? = null,
    var uhid: String = "",
    var uuid: Int = 0,
    var visitNum: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("addressDetails"),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(age)
        parcel.writeString(community)
        parcel.writeString(componentName)
        parcel.writeString(department)
        parcel.writeString(dob)
        parcel.writeString(facilityName)
        parcel.writeString(firstName)
        parcel.writeString(gender)
        parcel.writeByte(if (ili) 1 else 0)
        parcel.writeInt(is_dob_auto_calculate)
        parcel.writeString(lastName)
        parcel.writeString(middleName)
        parcel.writeString(mobile)
        parcel.writeByte(if (noSymptom) 1 else 0)
        parcel.writeString(period)
        parcel.writeString(professional)
        parcel.writeString(registered_date)
        parcel.writeString(salutation)
        parcel.writeByte(if (sari) 1 else 0)
        parcel.writeString(session)
        parcel.writeString(suffixCode)
        parcel.writeString(uhid)
        parcel.writeInt(uuid)
        parcel.writeString(visitNum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrintQuickRequest> {
        override fun createFromParcel(parcel: Parcel): PrintQuickRequest {
            return PrintQuickRequest(parcel)
        }

        override fun newArray(size: Int): Array<PrintQuickRequest?> {
            return arrayOfNulls(size)
        }
    }
}