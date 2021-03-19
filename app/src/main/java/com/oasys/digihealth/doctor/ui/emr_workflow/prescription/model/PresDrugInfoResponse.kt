package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PresDrugInfoResponse(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("responseContents")
    var responseContents: ArrayList<PresDrugInfoData>? = null
) : Parcelable

@Parcelize
data class PresDrugInfoData(
    @SerializedName("statusCode")
    var uuid: Int? = null,
    @SerializedName("stock_item_uuid")
    var stock_item_uuid: Int? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = null,
    @SerializedName("store_master_uuid")
    var store_master_uuid: Int? = null,
    @SerializedName("item_master_uuid")
    var item_master_uuid: Int? = null,
    @SerializedName("bar_code_id")
    var bar_code_id: Int? = null,
    @SerializedName("batch_id")
    var batch_id: String? = null,
    @SerializedName("expiry_date")
    var expiry_date: String? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("base_uom_uuid")
    var base_uom_uuid: Int? = null,
    @SerializedName("purchase_uom_uuid")
    var purchase_uom_uuid: Int? = null,
    @SerializedName("sale_uom_uuid")
    var sale_uom_uuid: Int? = null,
    @SerializedName("uom_price")
    var uom_price: String? = null,
    @SerializedName("unit_price")
    var unit_price: String? = null,
    @SerializedName("gst_uuid")
    var gst_uuid: Int? = null,
    @SerializedName("cgst_uuid")
    var cgst_uuid: Int? = null,
    @SerializedName("sgst_uuid")
    var sgst_uuid: Int? = null,
    @SerializedName("discount_mode_uuid")
    var discount_mode_uuid: Int? = null,
    @SerializedName("discount")
    var discount: String? = null,
    @SerializedName("cost_price")
    var cost_price: String? = null,
    @SerializedName("sale_price")
    var sale_price: String? = null,
    @SerializedName("stock_entry_uuid")
    var stock_entry_uuid: Int? = null,
    @SerializedName("stock_entry_detail_uuid")
    var stock_entry_detail_uuid: Int? = null,
    @SerializedName("grn_uuid")
    var grn_uuid: Int? = null,
    @SerializedName("grn_detail_uuid")
    var grn_detail_uuid: Int? = null,
    @SerializedName("is_expiry_updated")
    var is_expiry_updated: Boolean? = null,
    @SerializedName("store_master")
    var store_master: PresDrugInfoStoreMaster? = null
) : Parcelable

@Parcelize
data class PresDrugInfoStoreMaster(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("store_code")
    var store_code: String? = null,
    @SerializedName("store_name")
    var store_name: String? = null,
    @SerializedName("store_description")
    var store_description: String? = null,
    @SerializedName("store_type_uuid")
    var store_type_uuid: Int? = null,
    @SerializedName("store_category_uuid")
    var store_category_uuid: Int? = null,
    @SerializedName("store_sub_category_uuid")
    var store_sub_category_uuid: Int? = null,
    @SerializedName("store_policy_uuid")
    var store_policy_uuid: Int? = null
) : Parcelable
