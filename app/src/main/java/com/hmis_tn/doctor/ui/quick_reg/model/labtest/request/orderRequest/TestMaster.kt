package com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest

data class TestMaster(
    var analyte_uom: AnalyteUom = AnalyteUom(),
    var code: String = "",
    var formula: String = "",
    var list_of_value: String = "",
    var name: String = "",
    var note_template_uuid: Int? = 0,
    var short_code: Any = Any(),
    var uuid: Int = 0,
    var value_type_master: ValueTypeMaster = ValueTypeMaster(),
    var value_type_uuid: Int = 0
)