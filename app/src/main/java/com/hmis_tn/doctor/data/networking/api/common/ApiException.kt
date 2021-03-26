package com.hmis_tn.doctor.data.networking.api.common


class ApiException(
    val httpCode: Int
) : Exception("Response is not successful") {
    @Suppress("unused")
    val apiMessage: Int = when (httpCode) {
        401, 403 -> com.hmis_tn.doctor.R.string.error_invalid_credential
        404 -> com.hmis_tn.doctor.R.string.error_no_results
        422 -> com.hmis_tn.doctor.R.string.error_unprocessable_entity
        500 -> com.hmis_tn.doctor.R.string.error_generic_message
        else -> com.hmis_tn.doctor.R.string.error_generic_message
    }
}