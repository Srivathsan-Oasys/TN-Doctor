package com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult

import android.os.Parcel
import android.os.Parcelable
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest.AnalyteUom
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest.WorkOrderAttachment
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.Testrefmaster

data class Row(
    val acknowledge_by: Int = 0,
    val acknowledge_date: String = "",
    var analyte_master: TestMaster = TestMaster(),
    var analyte_master_uuid: Int = 0,
    val analyte_ref_master: Any = Any(),
    val analyte_ref_master_uuid: Any = Any(),
    val analyte_uom_uuid: Int = 0,
    val analyzer_analyte_ref_master_uuid: Any = Any(),
    val approved_by: Int = 0,
    val approved_date: String = "",
    val auth_status_uuid: Any = Any(),
    val cancel_reason: Any = Any(),
    val canceled_by: Any = Any(),
    val canceled_datetime: Any = Any(),
    val comments: String = "",
    val confidential_uuid: Any = Any(),
    val created_by: Any = Any(),
    val created_date: String = "",
    val doctor_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val facility_uuid: Int = 0,
    val from_department_uuid: Int = 0,
    val from_facility_uuid: Int = 0,
    val from_to_location_uuid: Int = 0,
    val group_uuid: Int = 0,
    val is_acknowledged: Boolean = false,
    val is_active: Boolean = false,
    val is_approval_requried: Any = Any(),
    val is_confidential: Boolean = false,
    val is_other_facility: Any = Any(),
    val is_profile: Boolean = false,
    val is_visible_from_facility: Boolean = false,
    val is_visible_to_facility: Boolean = false,
    val lab_master_type_uuid: Int = 0,
    val lis_result_value: Any = Any(),
    val modified_by: Int = 0,
    val modified_date: String = "",
    val order_priority_uuid: Int = 0,
    val order_process_date: String = "",
    val patient_order_detail_uuid: Int = 0,
    val patient_order_test_detail_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val patient_work_order_uuid: Int = 0,
    val profile_master: Any = Any(),
    val profile_master_uuid: Any = Any(),
    var qualifier_uuid: Int = 0,
    val ref_value: Any = Any(),
    val released_by: Any = Any(),
    val released_date: Any = Any(),
    var result_value: String = "",
    val revision: Any = Any(),
    val sample_collection_by: Int = 0,
    val sample_collection_date: String = "",
    val sample_identifier: String = "",
    val status: Boolean = false,
    var tat_session_end: String = "",
    var tat_session_start: String = "",
    val tech_validation_by: Int = 0,
    val tech_validation_date: String = "",
    val test_diseases_uuid: Any = Any(),
    val test_master: TestMaster = TestMaster(),
    val test_master_uuid: Int = 0,
    val test_ref_master: Testrefmaster = Testrefmaster(),
    val test_ref_master_uuid: Any = Any(),
    val test_to_location_uuid: Int = 0,
    val to_department_uuid: Int = 0,
    val to_facility_uuid: Any = Any(),
    val to_sub_department_uuid: Int = 0,
    val uuid: Int = 0,
    val vendor_sample_uuid: Int = 0,
    val vendor_test_uuid: Int = 0,
    val work_order_attachment_uuid: Any = Any(),
    val work_order_attachment:WorkOrderAttachment = WorkOrderAttachment(),
    val work_order_status_uuid: Int = 0,
    var analyte_uom: AnalyteUom = AnalyteUom(),
    var qualifierid:Int=0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        TODO("analyte_master"),
        TODO("analyte_master_uuid"),
        TODO("analyte_ref_master"),
        TODO("analyte_ref_master_uuid"),
        parcel.readInt(),
        TODO("analyzer_analyte_ref_master_uuid"),
        parcel.readInt(),
        parcel.readString()!!,
        TODO("auth_status_uuid"),
        TODO("cancel_reason"),
        TODO("canceled_by"),
        TODO("canceled_datetime"),
        parcel.readString()!!,
        TODO("confidential_uuid"),
        TODO("created_by"),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        TODO("is_approval_requried"),
        parcel.readByte() != 0.toByte(),
        TODO("is_other_facility"),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        TODO("lis_result_value"),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        TODO("profile_master"),
        TODO("profile_master_uuid"),
        parcel.readInt(),
        TODO("ref_value"),
        TODO("released_by"),
        TODO("released_date"),
        parcel.readString()!!,
        TODO("revision"),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        TODO("test_diseases_uuid"),
        TODO("test_master"),
        parcel.readInt(),
        TODO("test_ref_master"),
        TODO("test_ref_master_uuid"),
        parcel.readInt(),
        parcel.readInt(),
        TODO("to_facility_uuid"),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        TODO("work_order_attachment"),
        TODO("work_order_attachment_uuid"),
        parcel.readInt(),
        TODO("analyte_uom"),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(acknowledge_by)
        parcel.writeString(acknowledge_date)
        parcel.writeInt(analyte_uom_uuid)
        parcel.writeInt(approved_by)
        parcel.writeString(approved_date)
        parcel.writeString(comments)
        parcel.writeString(created_date)
        parcel.writeInt(doctor_uuid)
        parcel.writeInt(encounter_uuid)
        parcel.writeInt(facility_uuid)
        parcel.writeInt(from_department_uuid)
        parcel.writeInt(from_facility_uuid)
        parcel.writeInt(from_to_location_uuid)
        parcel.writeInt(group_uuid)
        parcel.writeByte(if (is_acknowledged) 1 else 0)
        parcel.writeByte(if (is_active) 1 else 0)
        parcel.writeByte(if (is_confidential) 1 else 0)
        parcel.writeByte(if (is_profile) 1 else 0)
        parcel.writeByte(if (is_visible_from_facility) 1 else 0)
        parcel.writeByte(if (is_visible_to_facility) 1 else 0)
        parcel.writeInt(lab_master_type_uuid)
        parcel.writeInt(modified_by)
        parcel.writeString(modified_date)
        parcel.writeInt(order_priority_uuid)
        parcel.writeString(order_process_date)
        parcel.writeInt(patient_order_detail_uuid)
        parcel.writeInt(patient_order_test_detail_uuid)
        parcel.writeInt(patient_order_uuid)
        parcel.writeInt(patient_uuid)
        parcel.writeInt(patient_work_order_uuid)
        parcel.writeInt(qualifier_uuid)
        parcel.writeString(result_value)
        parcel.writeInt(sample_collection_by)
        parcel.writeString(sample_collection_date)
        parcel.writeString(sample_identifier)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(tat_session_end)
        parcel.writeString(tat_session_start)
        parcel.writeInt(tech_validation_by)
        parcel.writeString(tech_validation_date)
        parcel.writeInt(test_master_uuid)
        parcel.writeInt(test_to_location_uuid)
        parcel.writeInt(to_department_uuid)
        parcel.writeInt(to_sub_department_uuid)
        parcel.writeInt(uuid)
        parcel.writeInt(vendor_sample_uuid)
        parcel.writeInt(vendor_test_uuid)
        parcel.writeInt(work_order_status_uuid)
        parcel.writeInt(qualifierid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Row> {
        override fun createFromParcel(parcel: Parcel): Row {
            return Row(parcel)
        }

        override fun newArray(size: Int): Array<Row?> {
            return arrayOfNulls(size)
        }
    }
}