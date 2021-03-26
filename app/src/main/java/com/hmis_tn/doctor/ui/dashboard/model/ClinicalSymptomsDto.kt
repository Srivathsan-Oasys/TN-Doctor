package com.hmis_tn.doctor.ui.dashboard.model

class ClinicalSymptomsDto {
    var title: String? = null
    var name: String? = null


    constructor()

    constructor(title: String?, name: String) {
        this.title = title
        this.name = name


    }

}