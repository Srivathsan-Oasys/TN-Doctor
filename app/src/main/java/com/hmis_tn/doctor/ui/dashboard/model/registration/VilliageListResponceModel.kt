package com.hmis_tn.doctor.ui.dashboard.model.registration

data class VilliageListResponceModel(
    var req: String = "",
    var statusCode: Int = 0,
    var responseContents: ArrayList<Villiage> = ArrayList()
)