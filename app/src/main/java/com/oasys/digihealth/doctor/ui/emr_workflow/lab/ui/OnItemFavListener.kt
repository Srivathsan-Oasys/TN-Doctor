package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel

interface OnItemFavListener {
    fun onItemFavClick(
        favmodel: FavouritesModel?,
        position: Int,
        selected: Boolean
    )

}