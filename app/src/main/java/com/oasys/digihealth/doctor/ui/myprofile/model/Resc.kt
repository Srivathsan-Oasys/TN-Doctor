package com.oasys.digihealth.doctor.ui.myprofile.model

data class Resc(
    val aadhaar_number: Any? = Any(),
    val address_line_1: String? = "",
    val address_line_2: Any? = Any(),
    val age: Any? = Any(),
    val application_login_permission: Boolean? = false,
    val country_master: Any? = Any(),
    val created_by: Int? = 0,
    val created_by_details: CreatedByDetails? = CreatedByDetails(),
    val created_date: String? = "",
    val department: Any? = Any(),
    val department_uuid: Int? = 0,
    val designation: Designation? = Designation(),
    val designation_uuid: Int? = 0,
    val district_master: DistrictMaster? = DistrictMaster(),
    val district_uuid: Int? = 0,
    val dob: String? = "",
    val email: String? = "",
    val employee_code: Any? = Any(),
    val facility: Facility? = Facility(),
    val facility_uuid: Int? = 0,
    val first_name: String? = "",
    val gender: Gender? = Gender(),
    val gender_uuid: Int? = 0,
    val gpf_cps_no: Any? = Any(),
    val gpf_suffix_uuid: Any? = Any(),
    val gpf_sufix: Any? = Any(),
    val health_office: HealthOffice? = HealthOffice(),
    val image_url: Any? = Any(),
    val is_active: Boolean? = false,
    val is_anaesthetist: Boolean? = false,
    val last_login_datetime: String? = "",
    val last_name: String? = "",
    val login_device_count: String? = "",
    val middle_name: String? = "",
    val mobile1: String? = "",
    val mobile2: Any? = Any(),
    val modified_by: Int? = 0,
    val modified_by_details: ModifiedByDetails? = ModifiedByDetails(),
    val modified_date: String? = "",
    val nationality: String? = "",
    val office_user: Boolean? = false,
    val office_uuid: Int? = 0,
    val password: String? = "",
    val preferred_language_uuid: Any? = Any(),
    val qualification: String? = "",
    val registration_number: String? = "",
    val reporting_to: ReportingTo? = ReportingTo(),
    val reporting_to_uuid: Int? = 0,
    val revision: Int? = 0,
    val role: Role? = Role(),
    val role_uuid: Int? = 0,
    val salutation_uuid: String? = "",
    val state_master: StateMaster? = StateMaster(),
    val state_uuid: Int? = 0,
    val status: Boolean? = false,
    val taluk_master: Any? = Any(),
    val taluk_uuid: Any? = Any(),
    val title: Title? = Title(),
    val unit: Any? = Any(),
    val unit_uuid: Any? = Any(),
    val userImgUrl: Any? = Any(),
    val user_name: String? = "",
    val user_type: UserType? = UserType(),
    val user_type_uuid: Int? = 0,
    val uuid: Int? = 0
)