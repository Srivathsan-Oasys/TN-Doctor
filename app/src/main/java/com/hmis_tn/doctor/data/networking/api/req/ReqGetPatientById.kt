package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqGetPatientById(
    @SerializedName("Doc_id")
    var Doc_id :String?=null,
    @SerializedName("currentdateandtime")
    var currentdateandtime :String?=null
)