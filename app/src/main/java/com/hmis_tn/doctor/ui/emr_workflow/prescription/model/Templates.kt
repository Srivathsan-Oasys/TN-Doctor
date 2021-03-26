package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

import android.os.Parcel
import android.os.Parcelable


data class Templates(
    var viewstatus: Int = 0,
    var isFavposition: Int = 0,
    var isTemposition: Int = 0,
    var drug_details: ArrayList<DrugDetail> = ArrayList(),
    val temp_details: PrescriptionTempDetails = PrescriptionTempDetails(),
    var collapse: Boolean = true
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        TODO("drug_details"),
        TODO("temp_details")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(viewstatus)
        parcel.writeInt(isFavposition)
        parcel.writeInt(isTemposition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Templates> {
        override fun createFromParcel(parcel: Parcel): Templates {
            return Templates(parcel)
        }

        override fun newArray(size: Int): Array<Templates?> {
            return arrayOfNulls(size)
        }
    }
}