package com.oasys.digihealth.doctor.ui.helpdesk.model


data class AssetDetailResponseContent(
    val uuid: Int? = 0,
    val model_no: String? = "",
    val model_name: String? = "",
    val serial_no: String? = "",
    val manufacturer_uuid: Int? = 0,
    val vendor_uuid: String? = "",
    val po_number: String? = "",
    val purchase_date: String? = "",
    val grn_number: String? = "",
    val grn_date: String? = "",
    val purchase_value: String? = "",
    val code_name: String? = ""
)