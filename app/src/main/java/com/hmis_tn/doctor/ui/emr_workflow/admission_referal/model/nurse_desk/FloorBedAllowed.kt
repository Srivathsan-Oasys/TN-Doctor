package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.nurse_desk

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FloorBedAllowed(
    var data: List<Int> = listOf(),
    var type: String = ""
) : Parcelable