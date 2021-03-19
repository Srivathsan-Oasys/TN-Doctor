package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

import android.os.Parcel
import android.os.Parcelable

data class LabHeaders(
    val activity_uuid: Int = 0,
    val context_activity_map_uuid: Int = 0,
    val context_uuid: Int = 0,
    val display_order: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(activity_uuid)
        parcel.writeInt(context_activity_map_uuid)
        parcel.writeInt(context_uuid)
        parcel.writeInt(display_order)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabHeaders> {
        override fun createFromParcel(parcel: Parcel): LabHeaders {
            return LabHeaders(parcel)
        }

        override fun newArray(size: Int): Array<LabHeaders?> {
            return arrayOfNulls(size)
        }
    }
}