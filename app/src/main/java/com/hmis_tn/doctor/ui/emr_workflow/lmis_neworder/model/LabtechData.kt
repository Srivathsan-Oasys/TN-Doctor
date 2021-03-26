package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model

import android.os.Parcel
import android.os.Parcelable

data class LabtechData(
    var id: Int = 0,
    var labId: Int = 0,
    var labName: String = "",
    var type: Int = 0,
    var typeofmaster: String = "",
    var SpecimenId: Int = 0,
    var TestMethodId: Int = 0,
    var OrderToLocationId: Int = 0,
    var isfrom: Int = 0,
    var isFavpos: Int = 0,
    var Labstatus: Int = 0,
    var to_department_id: Int = 0,
    var status: Boolean = false,
    var isTemposition: Int? = 0,
    var code: String = "",
    var commands: String = "",
    var mode: Int = 2,
    var patient_order_details_uuid: Int = 0,
    var patient_order_uuid: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(labId)
        parcel.writeString(labName)
        parcel.writeInt(type)
        parcel.writeString(typeofmaster)
        parcel.writeInt(SpecimenId)
        parcel.writeInt(TestMethodId)
        parcel.writeInt(OrderToLocationId)
        parcel.writeInt(isfrom)
        parcel.writeInt(isFavpos)
        parcel.writeInt(Labstatus)
        parcel.writeInt(to_department_id)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabtechData> {
        override fun createFromParcel(parcel: Parcel): LabtechData {
            return LabtechData(parcel)
        }

        override fun newArray(size: Int): Array<LabtechData?> {
            return arrayOfNulls(size)
        }
    }
}
