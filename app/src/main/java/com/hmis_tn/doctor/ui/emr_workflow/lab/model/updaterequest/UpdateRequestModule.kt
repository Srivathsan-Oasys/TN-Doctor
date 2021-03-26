package com.hmis_tn.doctor.ui.emr_workflow.lab.model.updaterequest

import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.request.Headers

data class UpdateRequestModule(
    var existing_details: List<Any?>? = listOf(),
    var headers: Headers? = Headers(),
    var new_details: List<NewDetail?>? = listOf(),
    var removed_details: List<RemovedDetail?>? = listOf()
)