package com.oasys.digihealth.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResAppointmentCheckIn(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("status")
    var status: String? = null
):Parcelable

