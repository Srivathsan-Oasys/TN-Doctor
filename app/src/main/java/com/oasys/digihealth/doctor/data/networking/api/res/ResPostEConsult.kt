package com.oasys.digihealth.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResPostEConsult(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("responseContents")
    var responseContents: ResPostEConsultData? = null
)

data class ResPostEConsultData(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("doc_token")
    var doc_token: String? = null,
    @SerializedName("doc_login")
    var doc_login: String? = null,
    @SerializedName("doc_logout")
    var doc_logout: String? = null,
    @SerializedName("doc_uuid")
    var doc_uuid: String? = null,
    @SerializedName("doc_session_uuid")
    var doc_session_uuid: String? = null,
    @SerializedName("room_count")
    var room_count: Int? = null,
    @SerializedName("tele_url_link")
    var tele_url_link: String? = null,
    @SerializedName("is_active")
    var is_active: Boolean? = null,
    @SerializedName("is_live")
    var is_live: Boolean? = null,
    @SerializedName("status")
    var status: Boolean? = null,
    @SerializedName("patient_is_connect")
    var patient_is_connect: Boolean? = null
)

