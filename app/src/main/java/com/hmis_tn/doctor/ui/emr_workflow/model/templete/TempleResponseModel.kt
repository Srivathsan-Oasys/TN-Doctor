package com.hmis_tn.doctor.ui.emr_workflow.model.templete

data class TempleResponseModel(
    var req: String? = "",
    var responseContents: TempleteResponseContents? = TempleteResponseContents(),
    var statusCode: Int? = 0
)