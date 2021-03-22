package com.oasys.digihealth.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResAppointmentSessionView(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("responseContents")
    var responseContents: ResAppointmentSessionList? = null
):Parcelable

