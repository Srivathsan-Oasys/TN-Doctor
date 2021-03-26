package com.hmis_tn.doctor.ui.detailedRegistration.model

data class GetPatientAllVisit(
    var created_by: Int? = null,
    var created_date: String? = null,
    var department_details: DepartmentDetails? = null,
    var department_uuid: Int? = null,
    var facility_uuid: Int? = null,
    var is_active: Boolean? = null,
    var is_last_visit: Boolean? = null,
    var is_mlc: Boolean? = null,
    var modified_by: Int? = null,
    var modified_date: String? = null,
    var patient_type_detail: PatientTypeDetail? = null,
    var patient_type_uuid: Int? = null,
    var patient_uuid: Int? = null,
    var registered_date: String? = null,
    var revision: Boolean? = null,
    var session_uuid: Int? = null,
    var speciality_department_details: SpecialityDepartmentDetails? = null,
    var speciality_department_uuid: Int? = null,
    var unit_uuid: Int? = null,
    var uuid: Int? = null,
    var visit_number: String? = null,
    var visit_type_detail: VisitTypeDetail? = null,
    var visit_type_uuid: Int? = null
)