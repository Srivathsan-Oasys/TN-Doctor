package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request

data class UpdateRequestModel(
    var existing_details: ArrayList<ExistingDetail> = ArrayList(),
    var headers: Headers = Headers(),
    var new_details: ArrayList<NewDetail> = ArrayList(),
    var removed_details: ArrayList<RemovedDetail> = ArrayList()
)