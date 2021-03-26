package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request

data class SearchPrescriptionResponseModel(
    var message: String = "",
    var req: Req = Req(),
    var responseContents: ArrayList<SearchPrescription> = ArrayList(),
    var status: Int = 0,
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)