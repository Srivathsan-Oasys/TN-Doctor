package com.hmis_tn.doctor.ui.order_status.model

import android.os.Parcel
import android.os.Parcelable

data class SEarchOrderStatusResponseContent(
    val acknowledge_by: Any = Any(),
    val acknowledge_date: Any = Any(),
    val age: String = "",
    val ageperiod: String = "",
    val approved_by: Any = Any(),
    val approved_date: Any = Any(),
    val dob: String = "",
    val facility_uuid: Int = 0,
    val modified_date: String = "",
    val nurse_collected_by: Any = Any(),
    val nurse_collected_date: Any = Any(),
    val order_created_by: String = "",
    val order_request_date: String = "",
    val order_status_name: String = "",
    val order_status_uuid: Int = 0,
    val order_tat_uuid: Int = 0,
    val patient_name: String = "",
    val patient_uuid: Int = 0,
    val profile_name: Any = Any(),
    val profile_uuid: Any = Any(),
    val received_by: Any = Any(),
    val received_date: Any = Any(),
    val rejected_by: Any = Any(),
    val rejected_date: Any = Any(),
    val rejected_reason: Any = Any(),
    val released_by: Any = Any(),
    val released_date: Any = Any(),
    val retest_by: Any = Any(),
    val retest_date: Any = Any(),
    val sample_collected_by: String = "",
    val sample_collected_date: String = "",
    val tech_validation_by: Any = Any(),
    val tech_validation_date: Any = Any(),
    val test_name: String = "",
    val test_uuid: Int = 0,
    val transferred_by: Any = Any(),
    val transferred_date: Any = Any(),
    val uhid: String = "",
    val work_order_status_name: String = "",
    val work_order_status_uuid: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        (parcel.readValue(Int::class.java.classLoader) as? Int)!!


    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SEarchOrderStatusResponseContent> {
        override fun createFromParcel(parcel: Parcel): SEarchOrderStatusResponseContent {
            return SEarchOrderStatusResponseContent(parcel)
        }

        override fun newArray(size: Int): Array<SEarchOrderStatusResponseContent?> {
            return arrayOfNulls(size)
        }
    }

}

fun describeContents(): Int {
    TODO("Not yet implemented")
}