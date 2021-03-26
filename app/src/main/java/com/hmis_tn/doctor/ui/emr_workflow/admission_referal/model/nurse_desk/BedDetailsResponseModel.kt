package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.nurse_desk

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BedDetailsResponseModel(
    var responseContents: BedDetails = BedDetails(),
    var message: String = "",
    var statusCode: Int = 0
) : Parcelable