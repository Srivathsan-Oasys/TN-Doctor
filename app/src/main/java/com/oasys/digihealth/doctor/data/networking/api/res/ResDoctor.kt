package com.oasys.digihealth.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResDoctor(
    @SerializedName("statusCode")
    var statusCode: String? = "",
    @SerializedName("responseContents")
    var doctorList: List<Doctors>? = null
) : Parcelable

@Parcelize
data class Doctors(
    @SerializedName("uuid")
    var uuid: Int? = 0,
    @SerializedName("user_name")
    var userName: String? = null,
    @SerializedName("first_name")
    var firstName: String? = null,
    @SerializedName("middle_name")
    var middleName: String? = null,
    @SerializedName("last_name")
    var last_name: String? = null,
    @SerializedName("user_type")
    var userType: UserType? = null,
    @SerializedName("user_payment_details")
    var userPaymentDetails: List<UserPaymentDetails>? = null
) : Parcelable

@Parcelize
data class UserType(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("name")
    var name: String? = null
) : Parcelable

@Parcelize
data class UserPaymentDetails(
    @SerializedName("consulting_charges")
    var consulting_charges: String? = null
) : Parcelable