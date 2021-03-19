package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response.DrugDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response.PrevInvestigationDetail

data class TreatmentKitResponsResponseContent(
    var department_id: Int = 0,
    val department_name: String = "",
    val diagnosis: ArrayList<Diagnosi> = ArrayList(),
    var doctor_id: Int = 0,
    val doctor_name: String = "",
    var encounter_type: String = "",
    var encounter_type_uuid: Int = 0,
    val labDetails: ArrayList<LabDetail> = ArrayList(),
    val drugDetails: ArrayList<DrugDetails> = ArrayList(),
    var order_id: Int = 0,
    val ordered_date: String = "",
    var patient_id: Int = 0,
    val radilogyDetails: ArrayList<RadilogyDetail> = ArrayList(),
    val InvestigationDetails: ArrayList<PrevInvestigationDetail> = ArrayList(),
    val treatment_kit_name: String = "",
    var treatment_kit_uuid: Int = 0
)