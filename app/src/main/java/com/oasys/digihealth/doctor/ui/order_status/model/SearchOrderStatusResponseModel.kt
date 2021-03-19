package com.oasys.digihealth.doctor.ui.order_status.model

import android.os.Parcel
import android.os.Parcelable

data class SearchOrderStatusResponseModel(
    val completedCount: Int = 0,
    val message: String = "",
    val pendingCount: Int = 0,
    val responseContents: List<SEarchOrderStatusResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(completedCount)
        parcel.writeString(message)
        parcel.writeInt(pendingCount)
        parcel.writeTypedList(responseContents)
        parcel.writeInt(statusCode)
        parcel.writeInt(totalRecords)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchOrderStatusResponseModel> {
        override fun createFromParcel(parcel: Parcel): SearchOrderStatusResponseModel {
            return SearchOrderStatusResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<SearchOrderStatusResponseModel?> {
            return arrayOfNulls(size)
        }
    }

}