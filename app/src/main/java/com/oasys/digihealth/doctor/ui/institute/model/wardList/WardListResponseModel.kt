package com.oasys.digihealth.doctor.ui.institute.model.wardList

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WardListResponseModel(
    val responseContents: List<Ward>? = listOf(),
    val msg: String? = null,
    val status: String? = null,
    val statusCode: Int? = null
) : Parcelable