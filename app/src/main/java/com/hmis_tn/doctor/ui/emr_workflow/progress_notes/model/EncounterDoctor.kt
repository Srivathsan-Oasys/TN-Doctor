package com.hmis_tn.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class EncounterDoctor(
    @SerializedName("doctor_uuid")
    var doctorUuid: Int?,
    @SerializedName("uuid")
    var uuid: Int?
)