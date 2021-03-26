package com.hmis_tn.doctor.ui.quickregistration.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetAllResponse(
    var Is_default: Boolean? = null,
    var code: String = "",
    var color: String? = null,
    var created_by: Int = 0,
    var created_date: String = "",
    var display_order: Int? = null,
    var is_active: Boolean = false,
    var language_uuid: Int? = null,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var revision: String? = null,
    var uuid: Int = 0
) : Parcelable