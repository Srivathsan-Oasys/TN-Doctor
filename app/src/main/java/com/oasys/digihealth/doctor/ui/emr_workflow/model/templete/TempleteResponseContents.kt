package com.oasys.digihealth.doctor.ui.emr_workflow.model.templete

data class TempleteResponseContents(
    var templates_lab_list: List<TemplatesLab?>? = listOf(),
    var templates_radiology_list: List<TemplatesLab?>? = listOf(),
    var templates_list: List<TemplatesLab?>? = listOf()
)