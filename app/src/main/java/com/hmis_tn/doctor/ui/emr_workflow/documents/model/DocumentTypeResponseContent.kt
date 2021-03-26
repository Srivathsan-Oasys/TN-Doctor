package com.hmis_tn.doctor.ui.emr_workflow.documents.model

data class DocumentTypeResponseContent(
    val Is_default: Boolean = false,
    val code: String = "",
    val color: String = "",
    val created_by: Int = 0,
    val created_date: Any = Any(),
    val display_order: Int = 0,
    val is_active: Boolean = false,
    val language: Int = 0,
    val modified_by: Int = 0,
    val modified_date: Any = Any(),
    val name: String = "",
    val revision: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)