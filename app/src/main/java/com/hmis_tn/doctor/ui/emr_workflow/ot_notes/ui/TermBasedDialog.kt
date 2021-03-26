package com.hmis_tn.doctor.ui.emr_workflow.ot_notes.ui

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.hmis_tn.doctor.R

class TermBasedDialog(context: Context) : Dialog(context) {

    var etTermBased: EditText? = null
    var btnOk: Button? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_term_based)
        initViews()
    }

    private fun initViews() {
        etTermBased = findViewById(R.id.etTermBased)
        btnOk = findViewById(R.id.btnOk)
    }
}