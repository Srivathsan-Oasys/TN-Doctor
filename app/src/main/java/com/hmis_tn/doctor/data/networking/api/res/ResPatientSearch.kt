package com.hmis_tn.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.PatientVisit
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResPatientSearch(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("responseContents")
    var responseContents: List<ResPatientListSearch>? = null,
    @SerializedName("totalRecords")
    var totalRecords: Int? = null
) : Parcelable

@Parcelize
data class ResPatientListSearch(
    @SerializedName("uhid")
    var uhid: String? = null,
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("title_uuid")
    var title_uuid: Int? = null,
    @SerializedName("package_uuid")
    var package_uuid: Int? = null,
    @SerializedName("professional_title_uuid")
    var professional_title_uuid: Int? = null,
    @SerializedName("suffix_uuid")
    var suffix_uuid: Int? = null,
    @SerializedName("gender_uuid")
    var gender_uuid: Int? = null,
    @SerializedName("first_name")
    var first_name: String? = null,
    @SerializedName("middle_name")
    var middle_name: String? = null,
    @SerializedName("last_name")
    var last_name: String? = null,
    @SerializedName("age")
    var age: Int? = null,
    @SerializedName("is_adult")
    var is_adult: Boolean? = null,
    @SerializedName("period_uuid")
    var period_uuid: Int? = null,
    @SerializedName("dob")
    var dob: String? = null,
    @SerializedName("old_pin")
    var old_pin: Int? = null,
    @SerializedName("registered_date")
    var registered_date: String? = null,
    @SerializedName("tat_start_time")
    var tat_start_time: String? = null,
    @SerializedName("tat_end_time")
    var tat_end_time: String? = null,
    @SerializedName("registred_facility_uuid")
    var patient_type_uuid: Int? = null,
    @SerializedName("application_type_uuid")
    var application_type_uuid: Int? = null,
    @SerializedName("patient_type_uuid")
    var registred_facility_uuid: Int? = null,
    @SerializedName("created_by")
    var created_by: Int? = null,
    @SerializedName("modified_by")
    var modified_by: Int? = null,
    @SerializedName("is_active")
    var is_active: Boolean? = null,
    @SerializedName("para_1")
    var para_1: String? = null,
    @SerializedName("para_2")
    var para_2: String? = null,
    @SerializedName("para_3")
    var para_3: String? = null,
    @SerializedName("para_4")
    var para_4: String? = null,
    @SerializedName("para_5")
    var para_5: String? = null,
    @SerializedName("para_6")
    var para_6: String? = null,
    @SerializedName("patient_detail")
    var patient_detail: PatientDetail? = null,
    @SerializedName("patient_visits")
    var patient_visits: List<PatientVisit?>? = listOf(),
    var isSelected: Boolean? = null
) : Parcelable

@Parcelize
data class PatientDetail(
    val aadhaar_number: String = "",
    val address_line1: String = "",
    val address_line2: String = "",
    val address_line3: String? = null,
    val address_line4: String? = null,
    val address_line5: String = "",
    val airport_name: String? = null,
    val alternate_number: String = "",
    val attender_contact_number: String? = null,
    val attender_name: String? = null,
    val bill_class: String? = null,
    val city_uuid: Int = 0,
    val community_uuid: Int = 0,
    val complication_uuid: Int = 0,
    val contact_history: Boolean = false,
    val contact_history_details: String? = null,
    val country_uuid: Int = 0,
    val created_by: Int = 0,
    val created_date: String = "",
    val date_of_onset: String? = null,
    val death_approved_by: Int = 0,
    val death_coments: String? = null,
    val death_confirmed_by: Int = 0,
    val death_place: String = "",
    val death_updated_by: Int = 0,
    val death_updated_date: String? = null,
    val district_uuid: Int = 0,
    val email: String = "",
    val height: String = "",
    val income_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_death_confirmed: Boolean? = null,
    val is_email_communication_preference: Boolean? = null,
    val is_sms_communication_preference: Boolean? = null,
    val marital_uuid: Int = 0,
    val mobile: String = "",
    val modified_by: Int = 0,
    val modified_date: String = "",
    val nationality_type_uuid: Int = 0,
    val occupation_uuid: Int = 0,
    val other_proof_uuid: Int = 0,
    val out_come_type_uuid: Int = 0,
    val para_1: String = "",
    val patient_uuid: Int = 0,
    val photo_path: String = "",
    val pincode: String = "",
    val place_uuid: Int = 0,
    val quarantine_status_date: String = "",
    val quarentine_status_type_uuid: Int = 0,
    val relation_type_uuid: Int = 0,
    val religion_uuid: Int = 0,
    val repeat_test_type_uuid: Int = 0,
    val revision: Boolean = false,
    val smart_ration_number: String = "",
    val state_uuid: Int = 0,
    val symptoms: Boolean = false,
    val taluk_uuid: Int = 0,
    val test_result: Boolean = false,
    val treatment_category_uuid: Int = 0,
    val treatment_plan_uuid: Int = 0,
    val underline_medicine_condition_uuid: Int = 0,
    val uuid: Int = 0,
    val village_uuid: Int = 0,
    val weight: String = ""
) : Parcelable

@Parcelize
data class PatientVisit(
    var created_by: Int? = 0,
    var created_date: String? = "",
    var department_uuid: Int? = 0,
    var facility_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_last_visit: Boolean? = false,
    var is_mlc: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var patient_type_uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var registered_date: String? = "",
    var revision: Boolean? = false,
    var session_uuid: Int? = 0,
    var speciality_department_uuid: Int? = 0,
    var unit_uuid: Int? = 0,
    var uuid: Int? = 0,
    var visit_number: String? = "",
    var visit_type_uuid: Int? = 0
) : Parcelable