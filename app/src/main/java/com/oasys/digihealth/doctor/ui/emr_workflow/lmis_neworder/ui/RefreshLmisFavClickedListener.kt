package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.ui

interface ClearLmisFavParticularPositionListener {
//    fun sendRefresh()

    fun ClearFavParticularPosition(position: Int)
    fun ClearAllData()

    fun checkanduncheck(position: Int, isSelect: Boolean)

    fun drugIdPresentCheck(drug_id: Int): Boolean
    fun clearDataUsingDrugid(drug_id: Int)

    fun favouriteID(favouriteID: Int)
}

