package com.hmis_tn.doctor.ui.quick_reg.model

import android.os.Parcel
import android.os.Parcelable

data class PDFRequestModel(
    var addressDetails: AddressDetails? = AddressDetails(),
    var age: Int? = 0,
    var componentName: String? = "",
    var facilityName: String? = "",
    var firstName: String? = "",
    var gender: String? = "",
    var ili: Boolean? = false,
    var mobile: String? = "",
    var noSymptom: Boolean? = false,
    var period: String? = "",
    var registered_date: String? = "",
    var sari: Boolean? = false,
    var testMethod: String? = "",
    var uhid: String? = "",
    var uuid: Int? = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("addressDetails"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(age)
        parcel.writeString(componentName)
        parcel.writeString(facilityName)
        parcel.writeString(firstName)
        parcel.writeString(gender)
        parcel.writeValue(ili)
        parcel.writeString(mobile)
        parcel.writeValue(noSymptom)
        parcel.writeString(period)
        parcel.writeString(registered_date)
        parcel.writeValue(sari)
        parcel.writeString(testMethod)
        parcel.writeString(uhid)
        parcel.writeValue(uuid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PDFRequestModel> {
        override fun createFromParcel(parcel: Parcel): PDFRequestModel {
            return PDFRequestModel(parcel)
        }

        override fun newArray(size: Int): Array<PDFRequestModel?> {
            return arrayOfNulls(size)
        }
    }
}