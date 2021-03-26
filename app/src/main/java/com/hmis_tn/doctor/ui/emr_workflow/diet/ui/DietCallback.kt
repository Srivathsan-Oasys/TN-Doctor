package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

import com.hmis_tn.doctor.ui.emr_workflow.diet.model.PatientDietOrder

interface DietCallback {

    fun OnviewClick(model: PatientDietOrder?)
}