package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.hmis_tn.doctor.R

class CommentDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_comment)
    }
}