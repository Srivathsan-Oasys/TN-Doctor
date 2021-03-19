package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

interface ClearFavParticularPositionListener {
//    fun sendRefresh()

    fun ClearFavParticularPosition(position: Int)
    fun ClearAllData()

    fun checkanduncheck(position: Int, isSelect: Boolean)

    fun checkanduncheck(position: ArrayList<Int>, isSelect: Boolean)

    fun drugIdPresentCheck(drug_id: Int): Boolean
    fun clearDataUsingDrugid(drug_id: Int)

    fun favouriteID(favouriteID: Int)

    fun updateSelectStatus(favortem: Int, position: Int, selected: Boolean)

}

