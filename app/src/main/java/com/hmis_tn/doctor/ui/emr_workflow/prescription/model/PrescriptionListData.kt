package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

import android.os.Parcel
import android.os.Parcelable

data class PrescriptionListData(

    var drug_active: Boolean? = false,
    var drug_duration: Int? = 0,
    var drug_code: String? = "",
    var drug_quantity: String? = "1",
    var Presstrength: String? = "",
    var drug_frequency_id: String? = "",
    var drug_frequency_name: String? = "",

    var drug_instruction_code: String? = null,
    var drug_instruction_id: Int? = 0,
    var drug_instruction_name: String? = "",

    var drug_id: Int? = 0,
    var store_master_uuid: Int? = 0,
    var drug_name: String? = null,
    var drug_period_code: String? = "",
    var PrescriptiondurationPeriodId: Int = 1,
    var drug_period_id: Int? = null,
    var drug_period_name: String? = "",
    var drug_route_id: Int? = 0,
    var drug_route_name: String? = "",
    var favourite_details_id: Int? = null,
    var favourite_display_order: Int? = null,
    var favourite_id: Int? = null,
    var speciality_sketch_id: Int? = null,
    var favourite_name: String? = null,
    var test_master_code: String? = null,
    var test_master_description: String? = null,
    var test_master_id: Int? = 0,
    var test_master_name: String? = "",
    var TestMethodId: Int = 0,
    var isSelected: Boolean? = false,
    var itemAppendString: String? = "",
    var position: Int? = 0,
    var duration: String? = "1",
    var durationPeriodId: Int? = 0,
    var selectTypeUUID: Int = 0,
    var selectRouteID: Int = 0,
    var selecteFrequencyID: Int = 0,
    var selectInvestID: Int = 0,
    var selectToLocationUUID: Int = 0,
    var selectToTestMethodUUID: Int = 0,
    var profile_master_uuid: Int = 0,

    var perdayquantityPrescription: String? = "",
    var isFavposition: Int = 0,
    var viewLabstatus: Int = 0,
    var isTemposition: Int = 0,
    var code: String? = "",
    var template_id: Int = 1,
    var commands: String? = "",
    var diagnosisUUiD: Int = 0,
    var favourite_active: Boolean? = false,
    val favourite_code: String? = "",
    val favourite_type_id: Int? = 0,
    val treatment_kit_id: Int? = 0,
    var treatment_kit_type_id: Int? = 0,

    var department_id: Int = 0,
    var template_details_uuid: Int = 0,
    var patient_order_uuid: Int = 0,
    var patient_order_details_uuid: Int = 0,
    var collapse: Boolean = true,
    var Update: Boolean = false,
    var drug_is_emar: Boolean? = false,
    var viewPrescriptionstatus: Int? = 0

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(drug_active)
        parcel.writeValue(drug_duration)
        parcel.writeString(drug_code)
        parcel.writeString(drug_quantity)
        parcel.writeString(Presstrength)
        parcel.writeString(drug_frequency_id)
        parcel.writeString(drug_frequency_name)
        parcel.writeString(drug_instruction_code)
        parcel.writeValue(drug_instruction_id)
        parcel.writeString(drug_instruction_name)
        parcel.writeValue(drug_id)
        parcel.writeValue(store_master_uuid)
        parcel.writeString(drug_name)
        parcel.writeString(drug_period_code)
        parcel.writeInt(PrescriptiondurationPeriodId)
        parcel.writeValue(drug_period_id)
        parcel.writeString(drug_period_name)
        parcel.writeValue(drug_route_id)
        parcel.writeString(drug_route_name)
        parcel.writeValue(favourite_details_id)
        parcel.writeValue(favourite_display_order)
        parcel.writeValue(favourite_id)
        parcel.writeValue(speciality_sketch_id)
        parcel.writeString(favourite_name)
        parcel.writeString(test_master_code)
        parcel.writeString(test_master_description)
        parcel.writeValue(test_master_id)
        parcel.writeString(test_master_name)
        parcel.writeInt(TestMethodId)
        parcel.writeValue(isSelected)
        parcel.writeString(itemAppendString)
        parcel.writeValue(position)
        parcel.writeString(duration)
        parcel.writeValue(durationPeriodId)
        parcel.writeInt(selectTypeUUID)
        parcel.writeInt(selectRouteID)
        parcel.writeInt(selecteFrequencyID)
        parcel.writeInt(selectInvestID)
        parcel.writeInt(selectToLocationUUID)
        parcel.writeInt(selectToTestMethodUUID)
        parcel.writeInt(profile_master_uuid)
        parcel.writeString(perdayquantityPrescription)
        parcel.writeInt(isFavposition)
        parcel.writeInt(viewLabstatus)
        parcel.writeInt(isTemposition)
        parcel.writeString(code)
        parcel.writeInt(template_id)
        parcel.writeString(commands)
        parcel.writeInt(diagnosisUUiD)
        parcel.writeValue(favourite_active)
        parcel.writeString(favourite_code)
        parcel.writeValue(favourite_type_id)
        parcel.writeValue(treatment_kit_id)
        parcel.writeValue(treatment_kit_type_id)
        parcel.writeInt(department_id)
        parcel.writeInt(template_details_uuid)
        parcel.writeInt(patient_order_uuid)
        parcel.writeInt(patient_order_details_uuid)
        parcel.writeByte(if (collapse) 1 else 0)
        parcel.writeByte(if (Update) 1 else 0)
        parcel.writeValue(drug_is_emar)
        parcel.writeValue(viewPrescriptionstatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrescriptionListData> {
        override fun createFromParcel(parcel: Parcel): PrescriptionListData {
            return PrescriptionListData(parcel)
        }

        override fun newArray(size: Int): Array<PrescriptionListData?> {
            return arrayOfNulls(size)
        }
    }
}