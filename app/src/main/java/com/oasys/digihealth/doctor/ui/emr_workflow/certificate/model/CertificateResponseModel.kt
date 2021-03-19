package com.oasys.digihealth.doctor.ui.emr_workflow.certificate.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CertificateResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: CertificateResponseContents = CertificateResponseContents()
) : Parcelable