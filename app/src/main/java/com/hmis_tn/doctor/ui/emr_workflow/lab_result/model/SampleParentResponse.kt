package com.hmis_tn.doctor.ui.emr_workflow.lab_result.model

class SampleParentResponse {
    var title: String? = null
    var genre: String? = null
    var year: String? = null

    constructor()
    constructor(title: String?, genre: String?, year: String?) {
        this.title = title
        this.genre = genre
        this.year = year
    }

}