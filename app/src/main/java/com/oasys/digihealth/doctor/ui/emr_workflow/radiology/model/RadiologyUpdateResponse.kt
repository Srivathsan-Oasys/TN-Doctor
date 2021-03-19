package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class RadiologyUpdateResponse(
    val message: String? = null,
    val responseContents: ResponseContents? = null,
    val statusCode: Int? = null
) {
    data class ResponseContents(
        val create_details: List<CreateDetail?>? = null,
        val remove_details: List<RemoveDetail?>? = null,
        val update_details: List<UpdateDetail?>? = null
    ) {
        data class CreateDetail(
            val created_by: String? = null,
            val created_date: String? = null,
            val doctor_uuid: String? = null,
            val encounter_uuid: String? = null,
            val from_department_uuid: String? = null,
            val group_uuid: Int? = null,
            val is_active: Boolean? = null,
            val is_confidential: Boolean? = null,
            val is_ordered: Boolean? = null,
            val is_profile: Boolean? = null,
            val is_visible_from_facility: Boolean? = null,
            val is_visible_to_facility: Boolean? = null,
            val lab_master_type_uuid: Int? = null,
            val modified_by: String? = null,
            val modified_date: String? = null,
            val order_priority_uuid: Int? = null,
            val order_request_date: String? = null,
            val order_status_uuid: Int? = null,
            val patient_order_uuid: Int? = null,
            val patient_uuid: String? = null,
            val profile_uuid: Any? = null,
            val revision: Boolean? = null,
            val status: Boolean? = null,
            val test_master_uuid: Int? = null,
            val to_department_uuid: Int? = null,
            val to_location_uuid: Int? = null,
            val to_sub_department_uuid: Int? = null,
            val uuid: Int? = null
        )

        data class RemoveDetail(
            val created_date: String? = null,
            val is_active: Boolean? = null,
            val is_confidential: Boolean? = null,
            val is_visible_from_facility: Boolean? = null,
            val is_visible_to_facility: Boolean? = null,
            val modified_by: String? = null,
            val modified_date: String? = null,
            val profile_uuid: Any? = null,
            val revision: Boolean? = null,
            val status: Boolean? = null,
            val test_master_uuid: Int? = null,
            val uuid: Int? = null
        )

        data class UpdateDetail(
            val created_date: String? = null,
            val details_comments: String? = null,
            val is_active: Boolean? = null,
            val is_confidential: Boolean? = null,
            val is_visible_from_facility: Boolean? = null,
            val is_visible_to_facility: Boolean? = null,
            val modified_by: String? = null,
            val modified_date: String? = null,
            val order_priority_uuid: Int? = null,
            val order_request_date: String? = null,
            val patient_order_uuid: Int? = null,
            val profile_uuid: Any? = null,
            val revision: Boolean? = null,
            val status: Boolean? = null,
            val test_master_uuid: Int? = null,
            val to_location_uuid: Int? = null,
            val type_of_method_uuid: Int? = null,
            val uuid: Int? = null
        )
    }
}