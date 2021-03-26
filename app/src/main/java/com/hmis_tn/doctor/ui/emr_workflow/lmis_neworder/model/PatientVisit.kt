package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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