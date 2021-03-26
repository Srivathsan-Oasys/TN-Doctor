package com.hmis_tn.doctor.ui.quick_reg.model

data class SampleErrorResponse(
    var statusCode: Int? = 0,
    var printValidationError: PrintValidation? = PrintValidation()

)