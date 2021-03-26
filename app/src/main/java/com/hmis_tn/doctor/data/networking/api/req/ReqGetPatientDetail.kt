package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqGetPatientDetail(
    @SerializedName("patientId")
    var patientId: Int? = null
)