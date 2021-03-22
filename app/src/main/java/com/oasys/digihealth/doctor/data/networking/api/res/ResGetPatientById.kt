package com.oasys.digihealth.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResGetPatientById(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("responseContents")
    var responseContents: PatientById? = null
):Parcelable

@Parcelize
data class PatientById(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("doc_uuid")
    var doc_uuid: Int? = null,
    @SerializedName("pat_uuid")
    var pat_uuid: Int? = null,
    @SerializedName("appointment_uuid")
    var appointment_uuid: Int? = null,
    @SerializedName("pat_start_time")
    var pat_start_time: String? = null
):Parcelable

