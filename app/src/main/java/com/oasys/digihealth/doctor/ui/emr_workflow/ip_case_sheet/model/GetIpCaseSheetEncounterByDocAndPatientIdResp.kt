package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model

data class GetIpCaseSheetEncounterByDocAndPatientIdResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContentXX>?
)