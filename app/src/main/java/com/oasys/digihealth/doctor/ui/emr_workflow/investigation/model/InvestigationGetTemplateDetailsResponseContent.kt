package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

import android.os.Parcel
import android.os.Parcelable

data class InvestigationGetTemplateDetailsResponseContent(
    val Invest_details: List<InvestDetailX> = listOf(),
    val temp_details: TempDetailsX = TempDetailsX()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("Invest_details"),
        TODO("temp_details")
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InvestigationGetTemplateDetailsResponseContent> {
        override fun createFromParcel(parcel: Parcel): InvestigationGetTemplateDetailsResponseContent {
            return InvestigationGetTemplateDetailsResponseContent(parcel)
        }

        override fun newArray(size: Int): Array<InvestigationGetTemplateDetailsResponseContent?> {
            return arrayOfNulls(size)
        }
    }

}

