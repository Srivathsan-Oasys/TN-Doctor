package com.hmis_tn.doctor.ui.emr_workflow.lab.ui

import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel

interface OnItemFavListener {
    fun onItemFavClick(
        favmodel: FavouritesModel?,
        position: Int,
        selected: Boolean
    )

}