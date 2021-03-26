package com.hmis_tn.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResGetPatientDetail(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("message")
    var message: String? = null
) : Parcelable

