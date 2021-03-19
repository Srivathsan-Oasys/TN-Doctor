package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model

import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response.GetVital

data class VitalsSearchResponseContents(
    val getVitals: List<GetVital> = listOf()
)