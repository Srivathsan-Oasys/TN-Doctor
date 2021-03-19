package com.oasys.digihealth.doctor.ui.quick_reg.model

data class GetTestMasterList(
    var analyte_uom_uuid: Int = 0,
    var code: String = "",
    var confidential_uuid: Int = 0,
    var container_type: ContainerType = ContainerType(),
    var container_type_uuid: Int = 0,
    var created_by: Int = 0,
    var created_date: String = "",
    var department_name: String = "",
    var department_uuid: Int = 0,
    var description: String = "",
    var display_order: Any = Any(),
    var estimate_process_time: Any = Any(),
    var formula: String = "",
    var is_active: Boolean = false,
    var is_approval_requried: Boolean = false,
    var is_confidential: Boolean = false,
    var is_notify_lab: Any = Any(),
    var is_separate_sample_uuid: Boolean = false,
    var is_separate_work_order: Any = Any(),
    var lab_master_type: LabMasterType = LabMasterType(),
    var lab_master_type_uuid: Int = 0,
    var list_of_value: String = "",
    var loinc_code_master: Any = Any(),
    var methodology: String = "",
    var min_sample_volume: Any = Any(),
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var note_template_uuid: Any = Any(),
    var print_order: Any = Any(),
    var ref_link: Any = Any(),
    var revision: Any = Any(),
    var sample_display: Any = Any(),
    var sample_master: Any = Any(),
    var sample_master_uuid: Int = 0,
    var sample_type: SampleType = SampleType(),
    var sample_type_uuid: Int = 0,
    var sample_volume: String = "",
    var short_code: Any = Any(),
    var status: Boolean = false,
    var sub_department_uuid: Int = 0,
    var tat_in_hours: Any = Any(),
    var test_diseases_uuid: Int = 0,
    var test_master_analyte_maps: List<Any> = listOf(),
    var test_ref_masters: List<Any> = listOf(),
    var type_of_method_uuid: Int = 0,
    var uuid: Int = 0,
    var value_type_uuid: Int = 0
)