package com.oasys.digihealth.doctor.ui.emr_workflow.diet.model

data class Templates(
    val diet_details: List<TemplateDietDetail>,
    val temp_details: TempDetails?
)