package com.hmis_tn.doctor.ui.quick_reg.model.labtest.request

import android.os.Parcel
import android.os.Parcelable

data class SendIdList (
    var Id:Int=0,
    var name:String=""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SendIdList> {
        override fun createFromParcel(parcel: Parcel): SendIdList {
            return SendIdList(parcel)
        }

        override fun newArray(size: Int): Array<SendIdList?> {
            return arrayOfNulls(size)
        }
    }
}