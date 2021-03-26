package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqEConsult(
    @SerializedName("Id")
    var Id: String? = null,
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
    @SerializedName("tele_url_link")
    var tele_url_link: String? = null,
    @SerializedName("room_count")
    var room_count: Int? = null
)