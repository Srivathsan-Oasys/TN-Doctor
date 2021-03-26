package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetBloodComponentsReq(
//    @SerializedName("pageNo")
//    var pageNo: Int?,
//    @SerializedName("paginationSize")
//    var paginationSize: Int?,
//    @SerializedName("sortField")
//    var sortField: String?,
//    @SerializedName("sortOrder")
//    var sortOrder: String?,
    @SerializedName("table_name")
    var tableName: String?
)