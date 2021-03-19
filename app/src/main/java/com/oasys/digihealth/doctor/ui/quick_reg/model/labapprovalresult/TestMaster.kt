package com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult

data class TestMaster(
    val analyte_uom: AnalyteUom = AnalyteUom(),
    val code: String = "",
    val formula: String = "",
    val list_of_value: String = "",
    val name: String = "",
    val note_template_uuid: Any = Any(),
    val short_code: Any = Any(),
    val uuid: Int = 0,
    val value_type_master: ValueTypeMaster = ValueTypeMaster(),
    val value_type_uuid: Int = 0
)