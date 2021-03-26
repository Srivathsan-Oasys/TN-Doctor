package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.nurse_desk

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WardBedInfo(
    var bed_number: Int = 0,
    var is_active: Boolean = false,
    var is_floor_bed: Boolean = false,
    var is_occupied: Boolean = false,
    var room_uuid: Int = 0,
    var patient_uuid: Int? = null,
    var admission_uuid: Int? = null,
    var status: Boolean = false,
    var uuid: Int = 0,
    var is_icu: Boolean = false,
    var is_oxygen: Boolean = false,
    var is_ventilator: Boolean = false,
    var ward_uuid: Int = 0,
    var active: Boolean = false
) : Parcelable