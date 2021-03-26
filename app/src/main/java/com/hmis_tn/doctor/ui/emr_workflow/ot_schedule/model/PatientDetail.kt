package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model


data class PatientDetail(
    var uhid: String?,
    var title_uuid: String?,
    var uuid: Int?,
    var first_name: String?,
    var middle_name: String?,
    var last_name: String?,
    var age: Int?,
    var gender_uuid: Int?,
    var para_2:  String?,
    var modified_date:  String?,
    var gender_details: GenderDetails?
){
    var isExpanded = false
}

data class GenderDetails(
    var uuid :Int?,
    var name :String
)
