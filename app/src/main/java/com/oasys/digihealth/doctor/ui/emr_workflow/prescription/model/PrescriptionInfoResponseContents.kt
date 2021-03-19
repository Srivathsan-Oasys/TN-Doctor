package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class PrescriptionInfoResponseContents(
    val base_uom_conversion_qty: Int = 0,
    val base_uom_id: Int = 0,
    val can_calculate_frequency_qty: Boolean = false,
    val can_return: Boolean = false,
    val cgst_uuid: Int = 0,
    val code: String = "",
    val cost_price: String = "",
    val createdUser_name: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val description: Any = Any(),
    val discount_mode_uuid: Int = 0,
    val discount_value: String = "",
    val facility_uuid: Int = 0,
    val generic_master: Any = Any(),
    val generic_uuid: Int = 0,
    val gst_uuid: Int = 0,
    val hsn_code: Any = Any(),
    val hsn_id: Int = 0,
    val hsn_name: Any = Any(),
    val image_path: Any = Any(),
    val is_active: Boolean = false,
    val is_batch_mandatory: IsBatchMandatory = IsBatchMandatory(),
    val is_emar: Boolean = false,
    val is_essential: Boolean = false,
    val is_expiry_mandatory: IsExpiryMandatory = IsExpiryMandatory(),
    val is_narcotic: Boolean = false,
    val is_poison: Boolean = false,
    val is_public: Boolean = false,
    val is_reusable: Boolean = false,
    val item_category: Any = Any(),
    val item_category_uuid: Int = 0,
    val item_sub_category: ItemSubCategory = ItemSubCategory(),
    val item_sub_category_uuid: Int = 0,
    val manufacturer_master: Any = Any(),
    val manufacturer_uuid: Int = 0,
    val max_usage: Int = 0,
    val min_usage: Int = 0,
    val modifiedUser_name: String = "",
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "",
    val non_moving_days: Int = 0,
    val product_sub_type_uuid: Int = 0,
    val product_type: Any = Any(),
    val product_type_uuid: Int = 0,
    val purchase_uom_conversion_qty: Int = 0,
    val purchase_uom_id: Int = 0,
    val regional_name: Any = Any(),
    val return_limit: Int = 0,
    val return_period_uuid: Int = 0,
    val revision: Int = 0,
    val sale_price: Any = Any(),
    val sale_uom_conversion_qty: Int = 0,
    val sale_uom_id: Int = 0,
    val schedule_type_uuid: Int = 0,
    val sgst_uuid: Int = 0,
    val status: Boolean = false,
    val stock_item: StockItem = StockItem(),
    val storage_condition_uuid: Int = 0,
    val strength: Any = Any(),
    val uuid: Int = 0,
    val volume: Any = Any(),
    var itemAppendString: String? = "",
    val wastage_uom_conversion_qty: Int = 0,
    val wastage_uom_uuid: Int = 0
)