package com.hmis_tn.doctor.ui.emr_workflow.diet.model

import android.os.Parcel
import android.os.Parcelable

data class TemplateDetailsResponseContent(
    var templates_list: List<Templates> = listOf()
) :
    Parcelable {

    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TemplateDetailsResponseContent> {
        override fun createFromParcel(parcel: Parcel): TemplateDetailsResponseContent {
            return TemplateDetailsResponseContent(parcel)
        }

        override fun newArray(size: Int): Array<TemplateDetailsResponseContent?> {
            return arrayOfNulls(size)
        }
    }
}
