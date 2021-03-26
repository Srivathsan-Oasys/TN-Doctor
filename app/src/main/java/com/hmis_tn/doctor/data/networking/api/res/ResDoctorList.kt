package com.hmis_tn.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResDoctorList(
    @SerializedName("statusCode")
    var statusCode: String? = "",
    @SerializedName("responseContents")
    var doctorList: List<DoctorList>? = null,
    @SerializedName("totalRecords")
    var totalRecords: Int? = 0,
    @SerializedName("msg")
    var msg: String? = null
) : Parcelable

@Parcelize
data class DoctorList(
    @SerializedName("as_uuid")
    var as_uuid: Int? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = null,
    @SerializedName("facility_name")
    var facility_name: String? = null,
    @SerializedName("facility_code")
    var facility_code: String? = null,
    @SerializedName("department_uuid")
    var department_uuid: Int? = null,
    @SerializedName("department_name")
    var department_name: String? = null,
    @SerializedName("department_code")
    var department_code: String? = null,
    @SerializedName("doc_uuid")
    var doc_uuid: Int? = null,
    @SerializedName("doc_username")
    var doc_username: String? = null,
    @SerializedName("doc_firstname")
    var doc_firstname: String? = null,
    @SerializedName("doc_middlename")
    var doc_middlename: String? = null,
    @SerializedName("doc_lastname")
    var doc_lastname: String? = null,
    @SerializedName("lab_uuid")
    var lab_uuid: Int? = null,
    @SerializedName("lab_username")
    var lab_username: String? = null,
    @SerializedName("lab_firstname")
    var lab_firstname: String? = null,
    @SerializedName("lab_middlename")
    var lab_middlename: String? = null,
    @SerializedName("lab_lastname")
    var lab_lastname: String? = null,
    @SerializedName("rad_uuid")
    var rad_uuid: Int? = null,
    @SerializedName("rad_username")
    var rad_username: String? = null,
    @SerializedName("rad_firstname")
    var rad_firstname: String? = null,
    @SerializedName("rad_middlename")
    var rad_middlename: String? = null,
    @SerializedName("rad_lastname")
    var rad_lastname: String? = null,
    @SerializedName("session_name")
    var session_name: String? = null,
    @SerializedName("start_date")
    var start_date: String? = null,
    @SerializedName("end_date")
    var end_date: String? = null,
    @SerializedName("holiday_from")
    var holiday_from: String? = null,
    @SerializedName("holiday_to")
    var holiday_to: String? = null,
    @SerializedName("is_monday")
    var is_monday: Boolean? = null,
    @SerializedName("is_tuesday")
    var is_tuesday: Boolean? = null,
    @SerializedName("is_wednesday")
    var is_wednesday: Boolean? = null,
    @SerializedName("is_thursday")
    var is_thursday: Boolean? = null,
    @SerializedName("is_friday")
    var is_friday: Boolean? = null,
    @SerializedName("is_saturday")
    var is_saturday: Boolean? = null,
    @SerializedName("is_sunday")
    var is_sunday: Boolean? = null,
    @SerializedName("is_all")
    var is_all: Boolean? = null,
    @SerializedName("is_e_consultation")
    var is_e_consultation: Boolean? = null,
    @SerializedName("is_regular_consultation")
    var is_regular_consultation: Boolean? = null,
    @SerializedName("slot_duration")
    var slot_duration: Int? = null,
    @SerializedName("break_from")
    var break_from: String? = null,
    @SerializedName("break_to")
    var break_to: String? = null,
    @SerializedName("max_slot_per_day")
    var max_slot_per_day: Int? = null,
    @SerializedName("no_of_schedule_appointments")
    var no_of_schedule_appointments: Int? = null,
    @SerializedName("no_of_walk_in_patients")
    var no_of_walk_in_patients: Int? = null,
    @SerializedName("is_active")
    var is_active: Boolean? = null,
    @SerializedName("status")
    var status: Boolean? = null,
    @SerializedName("as_created_by")
    var as_created_by: Int? = null,
    @SerializedName("as_modified_by")
    var as_modified_by: Int? = null,
    @SerializedName("as_created_date")
    var as_created_date: String? = null,
    @SerializedName("modified_date")
    var modified_date: String? = null,
    @SerializedName("created_username")
    var created_username: String? = null,
    @SerializedName("created_firstname")
    var created_firstname: String? = null,
    @SerializedName("created_middlename")
    var created_middlename: String? = null,
    @SerializedName("created_lastname")
    var created_lastname: String? = null,
    @SerializedName("modified_username")
    var modified_username: String? = null,
    @SerializedName("modified_firstname")
    var modified_firstname: String? = null,
    @SerializedName("modified_middlename")
    var modified_middlename: String? = null,
    @SerializedName("modified_lastname")
    var modified_lastname: String? = null,
    @SerializedName("as_start_time")
    var start_time: String? = null,
    @SerializedName("as_end_time")
    var end_time: String? = null,
    @SerializedName("as_image_url")
    var as_image_url: String? = null
) : Parcelable