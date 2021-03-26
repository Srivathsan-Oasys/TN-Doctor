package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqUpdateRoomBody(
    @SerializedName("Id")
    val Id: String? = null,
    @SerializedName("room_count")
    val room_count: Int? = null,
    @SerializedName("patient_is_connect")
    val patient_is_connect: Int? = null,
    @SerializedName("isWeb")
    val isWeb: Boolean? = null
)