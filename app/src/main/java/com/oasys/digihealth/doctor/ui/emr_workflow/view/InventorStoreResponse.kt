package com.oasys.digihealth.doctor.ui.emr_workflow.view

data class InventorStoreResponse(
    val message: String? = "",
    val status: Int? = 0,
    val responseContents: List<Storere?>? = listOf(),
    val totalRecords: Int? = 0
)