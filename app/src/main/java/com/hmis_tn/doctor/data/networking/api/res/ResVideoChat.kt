package com.hmis_tn.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResVideoChat(
    @SerializedName("apiKey")
    var apiKey: String? = "",
    @SerializedName("sessionId")
    var sessionId: String? = "",
    @SerializedName("token")
    var token: String? = ""
):Parcelable