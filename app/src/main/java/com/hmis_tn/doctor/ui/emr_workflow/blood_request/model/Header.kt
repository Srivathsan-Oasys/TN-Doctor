package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("blood_group_uuid")
    var bloodGroupUuid: String?,
    @SerializedName("blood_hb")
    var bloodHb: String?,
    @SerializedName("blood_request_purpose_uuid")
    var bloodRequestPurposeUuid: String?,
    @SerializedName("doctor_uuid")
    var doctorUuid: String?,
    @SerializedName("encounter_type_uuid")
    var encounterTypeUuid: Int?,
    @SerializedName("encounter_uuid")
    var encounterUuid: Int?,
    @SerializedName("is_pregnant")
    var isPregnant: Boolean?,
    @SerializedName("is_previous_transfusion")
    var isPreviousTransfusion: Boolean?,
    @SerializedName("patient_uuid")
    var patientUuid: String?,
    @SerializedName("transfusion_reaction")
    var transfusionReaction: Boolean?
)