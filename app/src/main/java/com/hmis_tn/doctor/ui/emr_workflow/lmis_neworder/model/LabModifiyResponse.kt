package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model

data class LabModifiyResponse(
    val message: String? = null,
    val responseContents: ResponseContents? = null,
    val statusCode: Int? = null
) {
    data class ResponseContents(
        val update_details: List<UpdateDetail?>? = null
    ) {
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