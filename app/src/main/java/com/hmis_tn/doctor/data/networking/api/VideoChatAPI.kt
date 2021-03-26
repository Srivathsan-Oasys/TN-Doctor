package com.hmis_tn.doctor.data.networking.api

import com.hmis_tn.doctor.BuildConfig.BASE_URL_OPENTOK_SESSION
import com.hmis_tn.doctor.data.networking.api.res.ResVideoChat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoChatAPI {

    @GET(BASE_URL_OPENTOK_SESSION + "{uuid}")
    suspend fun videoCallAPI(@Path("uuid") randomUUID: String): Response<ResVideoChat>


}