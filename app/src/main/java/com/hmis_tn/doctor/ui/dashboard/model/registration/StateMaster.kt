package com.hmis_tn.doctor.ui.dashboard.model.registration

data class StateMaster(
    var code: String = "",
    var country_master: CountryMaster = CountryMaster(),
    var name: String = "",
    var uuid: Int = 0
)