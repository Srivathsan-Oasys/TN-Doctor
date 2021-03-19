package com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui

import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.PatientDietOrder

interface DietCallback {

    fun OnviewClick(model: PatientDietOrder?)
}