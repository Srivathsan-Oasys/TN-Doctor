package com.oasys.digihealth.doctor.ui.dashboard.model.registration

data class DistrictMaster(
    var code: String = "",
    var name: String = "",
    var state_master: StateMaster = StateMaster(),
    var uuid: Int = 0
)