package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

interface ClearTemplateParticularPositionListener {

    fun ClearTemplateParticularPosition(position: Int)

    fun ClearAllData()

    fun GetTemplateDetails()

    fun updatestaus(favitem: Int, position: Int, select: Boolean)

    fun checkanduncheck(dataList: ArrayList<Int>)
}

