package com.oasys.digihealth.doctor.ui.quick_reg.model.request

class SearchPatientList {
    var SerialNo: String? = ""
    var PinNumber: String? = ""
    var Name: String? = ""
    var Gender: String? = ""
    var lastvisitdate : String ?=""


    constructor() {}
    constructor(SerialNo: String?, PinNumber: String?, Name: String?,Gender:String?,lastvisitdate : String?) {
        this.SerialNo = SerialNo
        this.PinNumber = PinNumber
        this.Name = Name
        this.Gender = Gender
        this.lastvisitdate = lastvisitdate


    }
}