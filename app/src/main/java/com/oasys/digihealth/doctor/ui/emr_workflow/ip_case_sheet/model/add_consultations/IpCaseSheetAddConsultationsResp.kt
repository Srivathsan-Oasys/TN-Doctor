package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.add_consultations

data class IpCaseSheetAddConsultationsResp(
    var code: Int? = 0,
    var message: String? = "",
    var reqContents: ReqContents? = ReqContents(),
    var responseContents: ResponseContents? = ResponseContents()
)