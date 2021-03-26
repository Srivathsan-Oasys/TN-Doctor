package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResDischargeSummaryPrevData(
    var statusCode: Int? = 0,
    @SerializedName("responseContent")
    var DSResDataList: DSResData? = null
)

data class DSReqData(
    var user_type_uuid: Int? = 0,
    var is_anaesthetist: Int? = 0
)

data class DSResData(
    @SerializedName("ds_list")
    var ds_list: List<DSList>? = null
)

data class DSList(
    @SerializedName("ds_details")
    var ds_details: DsDetails? = null,
    @SerializedName("dsd_details")
    var dsd_details: List<DSDList>? = null
)

data class DsDetails(
    var discharge_id: Int? = 0,
    var facility_id: Int? = 0,
    var department_uuid: Int? = 0,
    var patient_id: Int? = 0,
    var encounter_id: Int? = 0,
    var admission_id: Int? = 0,
    var doctor_id: Int? = 0,
    var dctor_name: String? = "",
    var generated_by_id: Int? = 0,
    var nurse_name: String? = "",
    var generated_date: String? = "",
    var note_template_id: Int? = 0,
    var data_template: Int? = 0
)

data class DSDList(
    var discharge_details_id: Int? = 0,
    var discharge_summary_id: Int? = 0,
    var context_id: Int? = 0,
    var activity_id: Int? = 0,
    var context_activity_map_id: Int? = 0,
    var transaction_id: Int? = 0,
    var display_order: Int? = 0
)