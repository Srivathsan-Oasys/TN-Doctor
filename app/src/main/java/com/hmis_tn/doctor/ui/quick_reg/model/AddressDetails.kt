package com.hmis_tn.doctor.ui.quick_reg.model

data class AddressDetails(
    var block: String? = "",
    var country: String? = "",
    var district: String? = "",
    var doorNum: String? = "",
    var pincode: String? = "",
    var state: String? = ""
)