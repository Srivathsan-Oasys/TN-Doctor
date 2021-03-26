package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.drug_info

data class GetDrugInfoResp(
    var message: String? = "",
    var req: Req? = Req(),
    var responseContents: List<ResponseContent>? = listOf(),
    var status: Int? = 0,
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)