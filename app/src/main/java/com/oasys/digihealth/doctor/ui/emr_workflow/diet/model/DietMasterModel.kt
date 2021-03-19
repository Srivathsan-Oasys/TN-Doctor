package com.oasys.digihealth.doctor.ui.emr_workflow.diet.model

data class DietMasterResponse(
    val req: String? = null,
    val responseContents: List<DietMasterData?>? = null,
    val statusCode: Int? = null
) {
    data class DietMasterData(
        val code: String? = null,
        val name: String? = null,
        val uuid: Int? = null
    )
}