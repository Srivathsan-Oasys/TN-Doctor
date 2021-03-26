package com.hmis_tn.doctor.ui.out_patient.model.responseaddpatient

data class ResponseAddPatient(
    var responseContent: ResponseContentaddPatient? = ResponseContentaddPatient(),
    var statusCode: Int? = 0
)