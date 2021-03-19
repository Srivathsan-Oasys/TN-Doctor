package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.oasys.digihealth.doctor.R

class CommentDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_comment)
    }
}