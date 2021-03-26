package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.hmis_tn.doctor.R

class DrugInfoDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_drug_info)
    }
}