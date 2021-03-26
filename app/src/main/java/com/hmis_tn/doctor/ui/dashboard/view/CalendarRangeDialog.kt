package com.hmis_tn.doctor.ui.dashboard.view

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.hmis_tn.doctor.R

class CalendarRangeDialog(context: Context) : Dialog(context) {

    var spinnerSession: Spinner? = null
    var spinnerGender: Spinner? = null
    var etStartDate: EditText? = null
    var etEndDate: EditText? = null
    var btnApply: Button? = null
    var btnCancel: Button? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_calendar_range)
        initViews()
    }

    private fun initViews() {
        spinnerSession = findViewById(R.id.spinnerSession)
        spinnerGender = findViewById(R.id.spinnerGender)
        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        btnApply = findViewById(R.id.btnApply)
        btnCancel = findViewById(R.id.btnCancel)
    }
}