package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

class ManageTemplateModel {
    var name: String? = null
    var code: String? = null
    var order: String? = null

    constructor()
    constructor(name: String?, code: String?, order: String?) {
        this.name = name
        this.code = code
        this.order = order
    }

}