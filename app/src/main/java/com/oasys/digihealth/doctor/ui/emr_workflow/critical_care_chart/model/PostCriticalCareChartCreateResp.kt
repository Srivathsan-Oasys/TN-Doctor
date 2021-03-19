package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model

import com.google.gson.annotations.SerializedName

data class PostCriticalCareChartCreateResp(
    @SerializedName("Ventilator data")
    var data1: List<Data>?,
    @SerializedName("ABG data")
    var data2: List<Data>?,
    @SerializedName("Monitor data")
    var data3: List<Data>?,
    @SerializedName("Intake data")
    var data4: List<Data>?,
    @SerializedName("BP data")
    var data5: List<Data>?,
    @SerializedName("Diabetes data")
    var data6: List<Data>?,
    @SerializedName("Dialysis data")
    var data7: List<Data>?,
    var message: String?,
    var statusCode: Int?
)