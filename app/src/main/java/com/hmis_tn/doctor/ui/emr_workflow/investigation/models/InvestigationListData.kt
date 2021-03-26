package com.hmis_tn.doctor.ui.emr_workflow.investigation.models

import android.os.Parcel
import android.os.Parcelable

class InvestigationListData(

    var selectToLocationUUID: Int = 0,
    var selectedLocationName: String? = "",
    var selectTypeUUID: Int = 0,
    var selectTypeName: String? = null,
    var isReadyForSave: Boolean? = false,
    var isEditableMode: Boolean? = false,
    var labDataSelected: Boolean? = false,
    var investigation_id: Int? = 0,
    var commands: String? = null,
    var investigation_name: String? = null,
    var investigation_code: String? = null,
    var isFav: Boolean? = false,
    var IsTemp: Boolean? = false,
    var favPos: Int? = 0,
    var tempPos: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(selectToLocationUUID)
        parcel.writeString(selectedLocationName)
        parcel.writeInt(selectTypeUUID)
        parcel.writeString(selectTypeName)
        parcel.writeValue(isReadyForSave)
        parcel.writeValue(isEditableMode)
        parcel.writeValue(labDataSelected)
        parcel.writeValue(investigation_id)
        parcel.writeString(commands)
        parcel.writeString(investigation_name)
        parcel.writeString(investigation_code)
        parcel.writeValue(isFav)
        parcel.writeValue(IsTemp)
        parcel.writeValue(favPos)
        parcel.writeValue(tempPos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InvestigationListData> {
        override fun createFromParcel(parcel: Parcel): InvestigationListData {
            return InvestigationListData(parcel)
        }

        override fun newArray(size: Int): Array<InvestigationListData?> {
            return arrayOfNulls(size)
        }
    }
}
