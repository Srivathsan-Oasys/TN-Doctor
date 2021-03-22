package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse

data class RecertData(
    var adminssion: List<Int> = listOf(),
    var encounter: Encounter = Encounter(),
    var revert: Revert = Revert()
)