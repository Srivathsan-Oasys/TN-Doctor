package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model

import android.os.Parcel
import android.os.Parcelable

data class ResponseContentslmisorder(
    var age: Int? = 0,
    var application_identifier: Any? = Any(),
    var application_type_uuid: Int? = 0,
    var application_uuid: Any? = Any(),
    var blood_group_uuid: Int? = 0,
    var created_by: Int? = 0,
    var created_date: String? = "",
    var dob: String? = "",
    var first_name: String? = "",
    var gender_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_adult: Boolean? = false,
    var is_dob_auto_calculate: Boolean? = false,
    var last_name: String? = "",
    var middle_name: String? = "",
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var old_pin: Any? = Any(),
    var package_uuid: Any? = Any(),
    var para_1: String? = "",
    var patient_detail: PatientDetail? = PatientDetail(),
    var patient_type_uuid: Int? = 0,
    var patient_visits: List<PatientVisit?>? = listOf(),
    var period_uuid: Int? = 0,
    var professional_title_uuid: Int? = 0,
    var registered_date: String? = "",
    var registred_facility_uuid: Int? = 0,
    var revision: Boolean? = false,
    var suffix_uuid: Int? = 0,
    var title_uuid: Int? = 0,
    var uhid: String? = "",
    var uuid: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        TODO("application_identifier"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        TODO("application_uuid"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        TODO("old_pin"),
        TODO("package_uuid"),
        parcel.readString(),
        TODO("patient_detail"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        TODO("patient_visits"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(age)
        parcel.writeValue(application_type_uuid)
        parcel.writeValue(blood_group_uuid)
        parcel.writeValue(created_by)
        parcel.writeString(created_date)
        parcel.writeString(dob)
        parcel.writeString(first_name)
        parcel.writeValue(gender_uuid)
        parcel.writeValue(is_active)
        parcel.writeValue(is_adult)
        parcel.writeValue(is_dob_auto_calculate)
        parcel.writeString(last_name)
        parcel.writeString(middle_name)
        parcel.writeValue(modified_by)
        parcel.writeString(modified_date)
        parcel.writeString(para_1)
        parcel.writeValue(patient_type_uuid)
        parcel.writeValue(period_uuid)
        parcel.writeValue(professional_title_uuid)
        parcel.writeString(registered_date)
        parcel.writeValue(registred_facility_uuid)
        parcel.writeValue(revision)
        parcel.writeValue(suffix_uuid)
        parcel.writeValue(title_uuid)
        parcel.writeString(uhid)
        parcel.writeValue(uuid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseContentslmisorder> {
        override fun createFromParcel(parcel: Parcel): ResponseContentslmisorder {
            return ResponseContentslmisorder(parcel)
        }

        override fun newArray(size: Int): Array<ResponseContentslmisorder?> {
            return arrayOfNulls(size)
        }
    }
}