package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model

import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.IsNotifibale

data class DignosisSearch(
    val body_site_uuid: Int = 0,
    val code: String = "",
    val comments: String = "",
    val created_by: Int = 0,
    val created_date: Any = Any(),
    val department_uuid: Int = 0,
    val description: String = "",
    val diagnosis_category_uuid: Int = 0,
    val diagnosis_grade_uuid: Int = 0,
    val diagnosis_region_uuid: Int = 0,
    val diagnosis_scheme_uuid: Int = 0,
    val diagnosis_type_uuid: Int = 0,
    val diagnosis_version_uuid: Int = 0,
    val facility_uuid: Int = 0,
    val in_house: Any = Any(),
    val is_active: Boolean = false,
    val is_billable: Int = 0,
    var is_notifibale: IsNotifibale? = IsNotifibale(),
    val is_sensitive: Int = 0,
    val length_Of_stay: String = "",
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "",
    val position_id: Int = 0,
    val referrence_link: String = "",
    val revision: Int = 0,
    val side_uuid: Int = 0,
    val speciality: String = "",
    val status: Boolean = false,
    val synonym: String = "",
    val uuid: Int = 0
)