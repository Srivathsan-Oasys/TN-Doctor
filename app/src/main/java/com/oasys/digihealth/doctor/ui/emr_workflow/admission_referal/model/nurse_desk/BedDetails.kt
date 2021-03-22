package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.nurse_desk

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BedDetails(
    var age_from: Int = 0,
    var age_to: Int = 0,
    var bed_count: Int = 0,
    var bed_end_number: Int = 0,
    var bed_start_number: Int = 0,
    var code: String = "",
    var created_by: Int = 0,
    var created_date: String = "",
    var department_uuid: Int = 0,
    var description: String = "",
    var facility_uuid: Int = 0,
    var floor_bed_allowed: FloorBedAllowed = FloorBedAllowed(),
    var floor_bed_count: Int = 0,
    var gender_uuid: Int = 0,
    var is_active: Boolean = false,
    var is_casualty: Boolean = false,
    var loc_block_uuid: Int = 0,
    var loc_building_uuid: Int = 0,
    var loc_floor_uuid: Int = 0,
    var loc_room_uuid: Int = 0,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var revision: Boolean = false,
    var room_count: Int = 0,
    var status: Boolean = false,
    var type: Int = 0,
    var uuid: Int = 0,
    var ward_bed_info: List<WardBedInfo> = listOf(),
    var ward_floor_bed_info: List<WardBedInfo> = listOf(),
    var ward_room_bed_info: List<WardBedInfo> = listOf()
) : Parcelable