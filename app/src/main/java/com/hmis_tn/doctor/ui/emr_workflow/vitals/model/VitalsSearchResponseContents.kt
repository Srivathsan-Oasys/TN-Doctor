package com.hmis_tn.doctor.ui.emr_workflow.vitals.model

import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.response.GetVital

data class VitalsSearchResponseContents(
    val getVitals: List<GetVital> = listOf()
)