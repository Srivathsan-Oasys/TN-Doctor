package com.oasys.digihealth.doctor.ui.quick_reg.view

data class orderList (
    var title: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var Status:Int?=null,
    var umo:String="",
    var value:String="",
    var type:String="",
    var testName:String="",
    var formula:String="",
    var PostionToData:MutableMap<Int,Int> = mutableMapOf(),
    var DataTOPostion:MutableMap<Int,Int> = mutableMapOf(),
    var spinnerdata:ArrayList<String> = ArrayList(),
    var formulapostion:ArrayList<Int> = ArrayList(),

    var spinnerData:MutableMap<Int,String> = mutableMapOf(),
    var spinner:MutableMap<String,Int> = mutableMapOf(),
    var code:String="",
    var note_template_uuid:Int = 0

)