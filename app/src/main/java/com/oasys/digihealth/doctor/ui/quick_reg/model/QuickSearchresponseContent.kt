package com.oasys.digihealth.doctor.ui.quick_reg.model

import android.os.Parcel
import android.os.Parcelable

data class QuickSearchresponseContent(
    var age: Int? = 0,
    val application_identifier: Any? = Any(),
    val application_type_uuid: Any? = Any(),
    val application_uuid: Any? = Any(),
    val blood_group_uuid: Int? = 0,
    val created_by: Int? = 0,
    val created_date: String? = "",
    val dob: String? = "",
    var first_name: String? = "",
    val gender_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_adult: Boolean? = false,
    val is_dob_auto_calculate: Boolean? = false,
    val last_name: String? = "",
    val middle_name: String? = "",
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val old_pin: Any? = Any(),
    val para_1: Any? = Any(),
    val patient_detail: PatientDetail? = PatientDetail(),
    val patient_type_uuid: Any? = Any(),
    val patient_visits: List<PataintVisits?>? = listOf(),
    val period_uuid: Int? = 0,
    val professional_title_uuid: Int? = 0,
    val registered_date: String? = "",
    val registred_facility_uuid: Int? = 0,
    val revision: Boolean? = false,
    val suffix_uuid: Int? = 0,
    val title_uuid: Int? = 0,
    val package_uuid: Int? = 0,
    val inst_code: Int? = 0,
    var uhid: String? = "",
    val title: String? = "",
    var gender: String? = "",
    val crt_dt: String? = "",
    val crt_uid: String? = "",
    val upd_dt: String? = "",
    var is_maternity: Boolean? = false,
    val uuid: Int? = 0,
    val patient_scheme_uuid:Int?=0
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int

    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(age)
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
        parcel.writeValue(period_uuid)
        parcel.writeValue(professional_title_uuid)
        parcel.writeString(registered_date)
        parcel.writeValue(registred_facility_uuid)
        parcel.writeValue(revision)
        parcel.writeValue(suffix_uuid)
        parcel.writeValue(title_uuid)
        parcel.writeString(uhid)
        parcel.writeValue(uuid)
        parcel.writeValue(patient_scheme_uuid)
    }

    override fun describeContents(): Int {
        return 0
    }



    companion object CREATOR : Parcelable.Creator<QuickSearchresponseContent> {
        override fun createFromParcel(parcel: Parcel): QuickSearchresponseContent {
            return QuickSearchresponseContent(parcel)
        }

        override fun newArray(size: Int): Array<QuickSearchresponseContent?> {
            return arrayOfNulls(size)
        }
    }
}