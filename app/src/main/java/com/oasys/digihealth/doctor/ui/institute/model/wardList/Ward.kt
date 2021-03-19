package com.oasys.digihealth.doctor.ui.institute.model.wardList

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ward(
    val department_name: String? = null,
    val department_uuid: Int? = null,
    val facility_name: String? = null,
    val facility_uuid: Int? = null,
    val ward_name: String? = null,
    val ward_uuid: Int? = null
) : Parcelable