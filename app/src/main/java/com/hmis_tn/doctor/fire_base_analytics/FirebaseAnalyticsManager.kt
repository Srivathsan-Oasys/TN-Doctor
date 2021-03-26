package com.hmis_tn.doctor.fire_base_analytics

import android.content.Context
import android.os.Bundle
import com.hmis_tn.doctor.config.AppConstants

class FirebaseAnalyticsManager(context: Context) {
    private var fireBaseManager: FireBaseManger? = null

    init {
        if (fireBaseManager == null) {
            fireBaseManager =
                FireBaseManger(context)
        }
    }


    fun tarckLoginVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HMIS_MOBILE_LOGIN_VISIT, bundle)
    }

    fun tarckLoginComplete() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(
            AnalyticsConstant.HMIS_LIMS_MOBILE_LOGIN_COMPLETE,
            bundle
        )
    }

    fun tarckLoginFaliure(message: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HMIS_MOBILE_LOGIN_FAILURE, bundle)
    }

    fun trackLMISNewOrderVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(
            AnalyticsConstant.HMIS_LIMS_MOBILE_NEWORDER_VISIT,
            bundle
        )
    }

    fun trackLMISNewOrderSuccess(name: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("Parent name", name)
        fireBaseManager?.trackFireBaseEvent(
            AnalyticsConstant.HMIS_LIMS_MOBILE_NEWORDER_SUCCESS,
            bundle
        )
    }

/*    fun trackLMISLabApprovalVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_LABTEST_APPROVEL_VISIT, bundle)
    }*/

    fun trackLMISLabSave(name: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("Parent name", name)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_LABTEST_SAVE, bundle)
    }

    fun trackLMISLabTestOrderApprovel(name: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("Parent name", name)
        fireBaseManager?.trackFireBaseEvent(
            AnalyticsConstant.HMIS_LIMS_MOBILE_LABTEST_ORDERPROCESS_APPROVAL,
            bundle
        )
    }

    fun trackLMISLabProcessOrderApproval(name: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("Parent name", name)
        fireBaseManager?.trackFireBaseEvent(
            AnalyticsConstant.HMIS_LIMS_MOBILE_TESTPROCESS_ORDERPROCESS_APPROVAL,
            bundle
        )


    }

    fun trackLMISLabApprovalOrderApproval(name: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("Parent name", name)
        fireBaseManager?.trackFireBaseEvent(
            AnalyticsConstant.HMIS_LIMS_MOBILE_TESTAPPROVAL_LABRESULTAPPROVAL_APPROVAL,
            bundle
        )

    }


    /*Dashboard*/

    fun trackDashBoardVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DASHBOARD_VISIT, bundle)
    }

    fun trackDashBoardOPSearchVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OP_SEARCH_VISIT, bundle)
    }

    fun trackDashBoardOPMyPatientVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OP_MY_PATIENT_VISIT, bundle)
    }

    fun trackDashBoardIPVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_IP_IP_VISIT, bundle)
    }

    fun trackDashboardIPDischargeVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_IP_DISCHARGE_VISIT, bundle)
    }

    /*Configuration*/
    fun trackConfigurationVisit() {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CONFIG_VISIT, bundle)
    }

    fun trackOPConfigSave(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("type", type)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CONFIG_SAVE, bundle)
    }

    /*History*/

    fun trackHistory(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_HISTORY_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_HISTORY_VISIT_IP, bundle)
        }
    }

    /*Lab*/

    fun trackLabVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_LAB_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_LAB_VISIT_IP, bundle)
        }
    }

    fun trackLabSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_LAB_SAVE_START_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_LAB_SAVE_START_IP, bundle)
        }
    }

    fun trackLabSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_LAB_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_LAB_SAVE_COMPLETE_IP,
                bundle
            )
        }
    }

    fun trackPrevLabVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_LAB_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_LAB_VISIT_IP,
                bundle
            )
        }

    }

    /*Radiology*/

    fun trackRadiologyVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_RADIOLOGY_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_RADIOLOGY_VISIT_IP, bundle)
        }
    }

    fun trackRadiologySaveStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_RADIOLOGY_SAVE_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_RADIOLOGY_SAVE_START_IP,
                bundle
            )
        }
    }

    fun trackRadiologySaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_RADIOLOGY_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_RADIOLOGY_SAVE_COMPLETE_IP,
                bundle
            )
        }
    }

    fun trackPrevRadiologyVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_RADIOLOGY_VISIT_Op,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_RADIOLOGY_VISIT_IP,
                bundle
            )
        }
    }

    /*Prescription*/

    fun trackPrescriptionVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PRESCREPTION_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PRESCREPTION_VISIT_IP,
                bundle
            )
        }
    }

    fun trackPrescriptionSaveStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PRESCREPTION_SAVE_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PRESCREPTION_SAVE_START_IP,
                bundle
            )
        }
    }

    fun trackPrescriptionSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PRESCREPTION_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PRESCREPTION_SAVE_COMPLETE_IP,
                bundle
            )
        }
    }

    fun trackPrevPrescriptionVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_PRESCREPTION_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_PRESCREPTION_VISIT_IP,
                bundle
            )
        }
    }

    /*Chief Complaints*/

    fun trackChiefComplaintsVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CHIEFCOMPLIENTS_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CHIEFCOMPLIENTS_VISIT_IP,
                bundle
            )
        }
    }

    fun trackChiefComplaintsSaveStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CHIEFCOMPLIENTS_SAVE_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CHIEFCOMPLIENTS_SAVE_START_IP,
                bundle
            )
        }
    }

    fun trackChiefComplaintsSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CHIEFCOMPLIENTS_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CHIEFCOMPLIENTS_SAVE_COMPLETE_IP,
                bundle
            )
        }
    }

    fun trackPrevChiefComplaintsVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_CHIEFCOMPLIENTS_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOS_CHIEFCOMPLIENTS_VISIT_IP,
                bundle
            )
        }
    }


    /*Treatment Kit*/
    fun trackTreatmentVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_TK_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_TK_VISIT_IP, bundle)
        }
    }

    fun trackTreatmentOrderStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_TK_ORDER_START_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_TK_ORDER_START_IP, bundle)
        }
    }

    fun trackTreatmentOrderComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_TK_ORDER_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_TK_ORDER_COMPLETE_IP,
                bundle
            )
        }
    }

    fun trackTreatmentSaveAndOrderStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_TK_SAVE_ORDER_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_TK_SAVE_ORDER_START_IP,
                bundle
            )
        }
    }

    fun trackTreatmentSaveAndOrderComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_TK_SAVE_ORDER_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_TK_SAVE_ORDER_COMPLETE_IP,
                bundle
            )
        }

    }

    fun trackPrevTreatmentVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOUS_TK_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOUS_TK_VISIT_IP,
                bundle
            )
        }
    }

    /*Vitals*/
    fun trackVitalsVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_VITALS_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_VITALS_VISIT_IP, bundle)
        }

    }

    fun trackVitalsSaveStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_VITALS_SAVE_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_VITALS_SAVE_START_IP,
                bundle
            )
        }

    }

    fun trackVitalsSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_VITALS_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_VITALS_SAVE_COMPLETE_IP,
                bundle
            )
        }

    }

    /*Investigation*/
    fun trackInvestigationVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_INVESTIGATION_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_INVESTIGATION_VISIT_IP,
                bundle
            )
        }

    }

    fun trackInvestigationSaveStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_INVESTIGATION_SAVE_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_INVESTIGATION_SAVE_START_IP,
                bundle
            )
        }

    }

    fun trackInvestigationSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_INVESTIGATION_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_INVESTIGATION_SAVE_COMPLETE_IP,
                bundle
            )
        }

    }

    fun trackPrevInvestigationVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOUS_INVESTIGATION_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOUS_INVESTIGATION_VISIT_IP,
                bundle
            )
        }

    }


    /*Diagnosis*/
    fun trackDiagnosisVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DIGANOSIS_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DIGANOSIS_VISIT_IP, bundle)
        }

    }

    fun trackDiagnosisSaveStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DIGANOSIS_SAVE_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DIGANOSIS_SAVE_START_IP,
                bundle
            )
        }

    }

    fun trackDiagnosisSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DIGANOSIS_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DIGANOSIS_SAVE_COMPLETE_IP,
                bundle
            )
        }

    }

    fun trackPrevDiagnosisVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOUS_DIGANOSIS_VISIT_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_PREVIOUS_DIGANOSIS_VISIT_IP,
                bundle
            )
        }

    }

    /*ADMISSION AND REFERAL*/
    fun trackAdmissionRefferalVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_AAR_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_AAR_VISIT_IP, bundle)
        }

    }

    /*Lab Result*/
    fun trackLabResultVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_LR_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_LR_VISIT_IP, bundle)
        }
    }

    /*Radiology Result*/
    fun trackRadiologyResultVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_RR_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_RR_VISIT_IP, bundle)
        }

    }

    /*Investigation Result*/
    fun trackInvestigationResultVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_IR_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_IR_VISIT_IP, bundle)
        }
    }

    /*Document*/
    fun trackDocumentVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DOCUMENT_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DOCUMENT_VISIT_IP, bundle)
        }
    }

    fun trackDocumentAddStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_ADD_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_ADD_START_IP,
                bundle
            )
        }

    }

    fun trackDocumentAddComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_ADD_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_ADD_COMPLETE_IP,
                bundle
            )
        }

    }

    fun trackDocumentDownloadStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_DOWNLOAD_START_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_DOWNLOAD_START_IP,
                bundle
            )
        }

    }

    fun trackDocumentDownloadComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_DOWNLOAD_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_DOCUMENT_DOWNLOAD_COMPLETE_IP,
                bundle
            )
        }

    }

    /*Certificate*/
    fun trackCertificateVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CER_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CER_VISIT_IP, bundle)
        }

    }

    fun trackCertificateSaveStart(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CER_SAVE_START_OP, bundle)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CER_SAVE_START_IP, bundle)

    }

    fun trackCertificateSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CER_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_CER_SAVE_COMPLETE_IP,
                bundle
            )
        }

    }

    fun trackPrevCertificateVisit(type: String) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PRE_CRE_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PRE_CRE_VISIT_IP, bundle)
        }

    }

    //OP Notes
    fun trackOpNotesVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OPN_VISIT, bundle)
    }

    fun trackOpNotesSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OPN_SAVE_START, bundle)
    }

    fun trackOpNotesSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OPN_SAVE_COMPLETE, bundle)
    }

    fun trackOpNotesPreviousOpNotes(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PREVIOUS_OPN_VISIT, bundle)
    }

    //CCC
    fun trackCriticalCareChartVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CCC_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_CCC_VISIT_IP, bundle)
        }
    }

    /*Blood Request*/
    fun trackBloodRequestVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_BR_VISIT, bundle)
    }

    fun trackBloodRequestSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_BR_SAVE_START, bundle)
    }

    fun trackBloodRequestSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_BR_SAVE_COMPLETE, bundle)
    }

    fun trackBloodRequestPreviousBloodRequest(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PRE_BR, bundle)
    }

    /*Speciality Sketch*/
    fun trackSpecialitySketchVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_SS_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_SS_VISIT_IP, bundle)
        }
    }

    fun trackSpecialitySketchSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_SS_SAVE_START_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_SS_SAVE_START_IP, bundle)
        }
    }

    fun trackSpecialitySketchSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_SS_SAVE_COMPLETE_OP,
                bundle
            )
        } else {
            fireBaseManager?.trackFireBaseEvent(
                AnalyticsConstant.HEMR_M_SS_SAVE_COMPLETE_IP,
                bundle
            )
        }
    }

    fun trackSpecialitySketchPreviousSpecialitySketch(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PRE_SS_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PRE_SS_IP, bundle)
        }
    }

    /*MRD*/
    fun trackMrdVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_MRD_VISIT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_MRD_VISIT_IP, bundle)
        }
    }

    fun trackMrdPrint(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        if (type.equals(AppConstants.OUT_PATIENT)) {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_MRD_PRINT_OP, bundle)
        } else {
            fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_MRD_PRINT_IP, bundle)
        }
    }

    /*IP Case Sheet*/
    fun trackIpCaseSheetVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_IPCC_VISIT, bundle)
    }

    fun trackIpCaseSheetSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_IPCC_SAVE_START, bundle)
    }

    fun trackIpCaseSheetSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_IPCC_SAVE_COMPLETE, bundle)
    }

    /*OT Schedule*/
    fun trackOtScheduleVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OTS_VISIT, bundle)
    }

    fun trackOtScheduleAdd(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OTS_ADD, bundle)
    }

    fun trackOtScheduleDelete(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OTS_DELETE, bundle)
    }

    fun trackOtScheduleEdit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OTS_EDIT, bundle)
    }

    /*OT Notes*/
    fun trackOtNotesVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OTN_VISIT, bundle)
    }

    fun trackOtNotesSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OTN_SAVE_START, bundle)
    }

    fun trackOtNotesSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_OTN_SAVE_COMPLETE, bundle)
    }

    /*Anesthesia Notes*/
    fun trackAnesthesiaNotesVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_AN_VISIT, bundle)
    }

    fun trackAnesthesiaNotesSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_AN_SAVE_START, bundle)
    }

    fun trackAnesthesiaNotesSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_AN_SAVE_COMPLETE, bundle)
    }

    /*Progress Notes*/
    fun trackProgressNotesVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PN_VISIT, bundle)
    }

    fun trackProgressNotesAdd(type: String?, pntype: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("pntype", pntype)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PN_ADD, bundle)
    }

    /*Discharge Summary*/
    fun trackDischargeSummaryVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DS_VISIT, bundle)
    }

    fun trackDischargeSummarySaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DS_SAVE_START, bundle)
    }

    fun trackDischargeSummarySaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DS_SAVE_COMPLETE, bundle)
    }

    fun trackDischargeSummaryPreviousDischargeSummary(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PREV_DS, bundle)
    }

    /*Diet*/
    fun trackDietVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DIET_VISIT, bundle)
    }

    fun trackDietSaveStart(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DIET_SAVE_START, bundle)
    }

    fun trackDietSaveComplete(type: String?, status: String?, message: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        bundle.putString("status", status)
        bundle.putString("message", message)
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DIET_SAVE_COMPLETE, bundle)
    }

    fun trackDietPreviousDiet(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_PREV_DIET, bundle)
    }

    /*Discharge Medication*/
    fun trackDischargeMedicationVisit(type: String?) {
        val bundle = Bundle()
        bundle.putString("os", "android")
        fireBaseManager?.trackFireBaseEvent(AnalyticsConstant.HEMR_M_DM_VISIT, bundle)
    }

}