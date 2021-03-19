package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

import android.os.Parcel
import android.os.Parcelable

data class PrescriptionFavResponseContent(
    val chief_complaint_code: Any = Any(),
    val chief_complaint_id: Any = Any(),
    val chief_complaint_name: Any = Any(),
    val diagnosis_code: Any = Any(),
    val diagnosis_description: Any = Any(),
    val diagnosis_id: Int = 0,
    val diagnosis_name: Any = Any(),
    val drug_active: Boolean = false,
    val drug_duration: Int = 0,
    val drug_frequency_id: Int = 0,
    val drug_frequency_name: String = "",
    val drug_id: Int = 0,
    val drug_instruction_code: String = "",
    val drug_instruction_id: Int = 0,
    val drug_instruction_name: String = "",
    val drug_is_emar: Boolean = false,
    val drug_name: String = "",
    val drug_period_code: String = "",
    val drug_period_id: Int = 0,
    val drug_period_name: String = "",
    val drug_route_id: Int = 0,
    val drug_route_name: String = "",
    val drug_strength: String = "",
    val favourite_details_id: Int = 0,
    val favourite_display_order: Int = 0,
    val favourite_id: Int = 0,
    val favourite_name: Any = Any(),
    val test_master_code: Any = Any(),
    val test_master_description: Any = Any(),
    val test_master_id: Int = 0,
    val test_master_name: Any = Any(),
    val vital_master_name: Any = Any(),
    val vital_master_uom: Any = Any(),
    var isSelected: Boolean? = false,
    var viewPrescriptionstatus: Int = 0
) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        return 0
    }
}