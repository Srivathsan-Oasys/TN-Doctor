package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response

import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.IsActive
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.Revision
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.Status

data class Type(
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val display_order: Int? = 0,
    val is_active: IsActive? = IsActive(),
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val revision: Revision? = Revision(),
    val status: Status? = Status(),
    val uuid: Int? = 0
)