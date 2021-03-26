package com.hmis_tn.doctor.fire_base_analytics

import android.content.Context
import com.hmis_tn.doctor.application.HmisApplication

class AnalyticsManager {

    fun tarckLoginVisit(context: Context) {
        HmisApplication.instance.fireBaseManager.tarckLoginVisit()
    }

    fun trackLoginSuccess(context: Context) {
        HmisApplication.instance.fireBaseManager.tarckLoginComplete()
    }

    fun trackLoginFailed(context: Context, message: String) {
        HmisApplication.instance.fireBaseManager.tarckLoginFaliure(message)
    }

    fun trackLMISNewOrderVisit(context: Context) {
        HmisApplication.instance.fireBaseManager.trackLMISNewOrderVisit()
    }

    fun trackLMISNewOrderSuccess(context: Context, message: String) {
        HmisApplication.instance.fireBaseManager.trackLMISNewOrderSuccess(message)
    }

    /*   fun trackLMISLabApprovalVisit(context: Context){
           HmisApplication.instance.fireBaseManager.trackLMISLabApprovalVisit()
           HmisApplication.instance.faceBookManager.trackLMISLabApprovalVisit(context)
       }*/

    fun trackLMISLabSave(context: Context, message: String) {
        HmisApplication.instance.fireBaseManager.trackLMISLabSave(message)
    }

    fun trackLMISLabTestOrderApproval(context: Context, message: String) {
        HmisApplication.instance.fireBaseManager.trackLMISLabTestOrderApprovel(message)
    }

    fun trackLMISLabProcessOrderApproval(context: Context, name: String) {
        HmisApplication.instance.fireBaseManager.trackLMISLabProcessOrderApproval(name)
    }

    fun trackLMISLabApprovalOrderApproval(context: Context, name: String) {
        HmisApplication.instance.fireBaseManager.trackLMISLabApprovalOrderApproval(name)
    }


    /*
    DASHBOARD
     */

    fun trackDashBoardVisit() {
        HmisApplication.instance.fireBaseManager.trackDashBoardVisit()
    }

    fun trackDashBoardOPSearchVisit() {
        HmisApplication.instance.fireBaseManager.trackDashBoardOPSearchVisit()
    }

    fun trackDashBoardOPMyPatientVisit() {
        HmisApplication.instance.fireBaseManager.trackDashBoardOPMyPatientVisit()
    }

    fun trackDashBoardIPVisit() {
        HmisApplication.instance.fireBaseManager.trackDashBoardIPVisit()
    }

    fun trackDashboardIPDischargeVisit() {
        HmisApplication.instance.fireBaseManager.trackDashboardIPDischargeVisit()
    }

    /*CONFIGURATION*/

    fun trackConfigurationVisit() {
        HmisApplication.instance.fireBaseManager.trackConfigurationVisit()
    }

    fun trackOPConfigSave(type: String) {
        HmisApplication.instance.fireBaseManager.trackOPConfigSave(type)
    }

    /*History*/
    fun trackHistory(type: String) {
        HmisApplication.instance.fireBaseManager.trackHistory(type)
    }

    /*Lab*/

    fun trackLabVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackLabVisit(type)
    }

    fun tracklabSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackLabSaveStart(type)
    }

    fun trackLabSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackLabSaveComplete(type, status, message)
    }

    fun trackPrevLabVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevLabVisit(type)
    }

    /*Radiology*/

    fun trackRadiologyVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackRadiologyVisit(type)
    }

    fun trackRadiologySaveStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackRadiologySaveStart(type)
    }

    fun trackRadiologySaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackRadiologySaveComplete(type, status, message)
    }

    fun trackPrevRadiologyVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevRadiologyVisit(type)
    }

    /*Prescription*/

    fun trackPrescriptionVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrescriptionVisit(type)
    }

    fun trackPrescriptionSaveStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrescriptionSaveStart(type)
    }

    fun trackPrescriptionSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackPrescriptionSaveComplete(
            type,
            status,
            message
        )
    }

    fun trackPrevPrescriptionVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevPrescriptionVisit(type)
    }

    /*ChiefComplaints*/

    fun trackChiefComplaintsVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackChiefComplaintsVisit(type)
    }

    fun trackChiefComplaintsSaveStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackChiefComplaintsSaveStart(type)
    }

    fun trackChiefComplaintsSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackChiefComplaintsSaveComplete(
            type,
            status,
            message
        )
    }

    fun trackPrevChiefComplaintsVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevChiefComplaintsVisit(type)
    }

    /*Treatment Kit*/

    fun trackTreatmentVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackTreatmentVisit(type)
    }

    fun trackTreatmentOrderStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackTreatmentOrderStart(type)
    }

    fun trackTreatmentOrderComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackTreatmentOrderComplete(type, status, message)
    }

    fun trackTreatmentSaveOrderStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackTreatmentSaveAndOrderStart(type)
    }

    fun trackTreatmentSaveOrderComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackTreatmentSaveAndOrderComplete(
            type,
            status,
            message
        )
    }

    fun trackPrevTreatmentVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevTreatmentVisit(type)
    }

    /*OP Notes*/
    fun trackOpNotesVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOpNotesVisit(type)
    }

    fun trackOpNotesSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOpNotesSaveStart(type)
    }

    fun trackOpNotesSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackOpNotesSaveComplete(type, status, message)
    }

    fun trackOpNotesPreviousOpNotes(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOpNotesPreviousOpNotes(type)
    }

    /*CCC*/
    fun trackCriticalCareChartVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackCriticalCareChartVisit(type)
    }

    /*Blood Request*/
    fun trackBloodRequestVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackBloodRequestVisit(type)
    }

    fun trackBloodRequestSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackBloodRequestSaveStart(type)
    }

    fun trackBloodRequestSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackBloodRequestSaveComplete(
            type,
            status,
            message
        )
    }

    fun trackBloodRequestPreviousBloodRequest(type: String?) {
        HmisApplication.instance.fireBaseManager.trackBloodRequestPreviousBloodRequest(type)
    }

    /*Speciality Sketch*/
    fun trackSpecialitySketchVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackSpecialitySketchVisit(type)
    }

    fun trackSpecialitySketchSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackSpecialitySketchSaveStart(type)
    }

    fun trackSpecialitySketchSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackSpecialitySketchSaveComplete(
            type,
            status,
            message
        )
    }

    fun trackSpecialitySketchPreviousSpecialitySketch(type: String?) {
        HmisApplication.instance.fireBaseManager.trackSpecialitySketchPreviousSpecialitySketch(type)
    }

    /*MRD*/
    fun trackMrdVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackMrdVisit(type)
    }

    fun trackMrdPrint(type: String?) {
        HmisApplication.instance.fireBaseManager.trackMrdPrint(type)
    }

    /*IP Case Sheet*/
    fun trackIpCaseSheetVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackIpCaseSheetVisit(type)
    }

    fun trackIpCaseSheetSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackIpCaseSheetSaveStart(type)
    }

    fun trackIpCaseSheetSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackIpCaseSheetSaveComplete(type, status, message)
    }

    /*OT Schedule*/
    fun trackOtScheduleVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOtScheduleVisit(type)
    }

    fun trackOtScheduleAdd(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOtScheduleAdd(type)
    }

    fun trackOtScheduleDelete(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOtScheduleDelete(type)
    }

    fun trackOtScheduleEdit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOtScheduleEdit(type)
    }

    /*OT Notes*/
    fun trackOtNotesVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOtNotesVisit(type)
    }

    fun trackOtNotesSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackOtNotesSaveStart(type)
    }

    fun trackOtNotesSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackOtNotesSaveComplete(type, status, message)
    }

    /*Anesthesia Notes*/
    fun trackAnesthesiaNotesVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackAnesthesiaNotesVisit(type)
    }

    fun trackAnesthesiaNotesSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackAnesthesiaNotesSaveStart(type)
    }

    fun trackAnesthesiaNotesSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackAnesthesiaNotesSaveComplete(
            type,
            status,
            message
        )
    }

    /*Progress Notes*/
    fun trackProgressNotesVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackProgressNotesVisit(type)
    }

    fun trackProgressNotesAdd(type: String?, pntype: String?) {
        HmisApplication.instance.fireBaseManager.trackProgressNotesAdd(type, pntype)
    }

    /*Discharge Summary*/
    fun trackDischargeSummaryVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackDischargeSummaryVisit(type)
    }

    fun trackDischargeSummarySaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackDischargeSummarySaveStart(type)
    }

    fun trackDischargeSummarySaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackDischargeSummarySaveComplete(
            type,
            status,
            message
        )
    }

    fun trackDischargeSummaryPreviousDischargeSummary(type: String?) {
        HmisApplication.instance.fireBaseManager.trackDischargeSummaryPreviousDischargeSummary(type)
    }

    /*Diet*/
    fun trackDietVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackDietVisit(type)
    }

    fun trackDietSaveStart(type: String?) {
        HmisApplication.instance.fireBaseManager.trackDietSaveStart(type)
    }

    fun trackDietSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackDietSaveComplete(type, status, message)
    }

    fun trackDietPreviousDiet(type: String?) {
        HmisApplication.instance.fireBaseManager.trackDietPreviousDiet(type)
    }

    /*Discharge Medication*/
    fun trackDischargeMedicationVisit(type: String?) {
        HmisApplication.instance.fireBaseManager.trackDischargeMedicationVisit(type)
    }

    /*Vitals*/
    fun trackVitalVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackVitalsVisit(type)
    }

    fun trackVitalSaveStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackVitalsSaveStart(type)
    }

    fun trackVitalSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackVitalsSaveComplete(type, status, message)
    }

    /*Investigation*/

    fun trackInvestigationVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackInvestigationVisit(type)
    }

    fun trackInvestigationSaveStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackInvestigationSaveStart(type)
    }

    fun trackInvestigationSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackInvestigationSaveComplete(
            type,
            status,
            message
        )
    }

    fun trackPrevInvestigationVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevInvestigationVisit(type)
    }


    /*Diagnosis*/
    fun trackDiagnosisVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackDiagnosisVisit(type)
    }

    fun trackDiagnosisSaveStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackDiagnosisSaveStart(type)
    }

    fun trackDiagnosisSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackDiagnosisSaveComplete(type, status, message)
    }

    fun trackPrevDiagnosisVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevDiagnosisVisit(type)
    }

    /*Admission And Refferal*/
    fun trackAdmissionRefferalVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackAdmissionRefferalVisit(type)
    }

    /*Lab Result*/
    fun trackLabResultVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackLabResultVisit(type)
    }

    /*Radiology Result*/
    fun trackRadiologyResultVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackRadiologyResultVisit(type)
    }

    /*Investigation Result*/
    fun trackInvestigationResultVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackInvestigationResultVisit(type)
    }

    /*Document*/
    fun trackDocumentVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackDocumentVisit(type)
    }

    fun trackDocumentAddStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackDocumentAddStart(type)
    }

    fun trackDocumentAddComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackDocumentAddComplete(type, status, message)
    }

    fun trackDocumentDownloadstart(type: String) {
        HmisApplication.instance.fireBaseManager.trackDocumentDownloadStart(type)
    }

    fun trackDocumentDownloadComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackDocumentDownloadComplete(
            type,
            status,
            message
        )
    }

    /*Certificate*/
    fun trackCertificateVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackCertificateVisit(type)
    }

    fun trackCertificateSaveStart(type: String) {
        HmisApplication.instance.fireBaseManager.trackCertificateSaveStart(type)
    }

    fun trackCertificateSaveComplete(type: String?, status: String?, message: String?) {
        HmisApplication.instance.fireBaseManager.trackCertificateSaveComplete(type, status, message)
    }

    fun trackPrevCertificateVisit(type: String) {
        HmisApplication.instance.fireBaseManager.trackPrevCertificateVisit(type)
    }


    companion object {

        internal var analyticsManager: AnalyticsManager? = null

        fun getAnalyticsManager(): AnalyticsManager {
            if (analyticsManager == null)
                analyticsManager =
                    AnalyticsManager()
            return analyticsManager as AnalyticsManager
        }
    }

}