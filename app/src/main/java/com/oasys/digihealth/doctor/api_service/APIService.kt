package com.oasys.digihealth.doctor.api_service

import android.content.Context
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.BuildConfig.BASE_DOMAIN
import com.oasys.digihealth.doctor.BuildConfig.BASE_DOMAIN
import com.oasys.digihealth.doctor.BuildConfig.BASE_URL
import com.oasys.digihealth.doctor.api_service.ApiUrl.Login
import com.oasys.digihealth.doctor.api_service.ApiUrl.Register
import com.oasys.digihealth.doctor.api_service.ApiUrl.departmentAutocomplete
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigResponseModel
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigUpdateRequestModel
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigUpdateResponseModel
import com.oasys.digihealth.doctor.ui.configuration.model.HistoryConfigUpdateRequestModel
import com.oasys.digihealth.doctor.ui.covid.addpatientrequest.*
import com.oasys.digihealth.doctor.ui.covid.addpatientresponse.AddPatientResponse
import com.oasys.digihealth.doctor.ui.dashboard.model.*
import com.oasys.digihealth.doctor.ui.dashboard.model.registration.*
import com.oasys.digihealth.doctor.ui.detailedRegistration.model.GetAllDepartmentRequestModel
import com.oasys.digihealth.doctor.ui.detailedRegistration.model.GetPatientAllReferralsResponse
import com.oasys.digihealth.doctor.ui.detailedRegistration.model.GetPatientAllVisitsRequest
import com.oasys.digihealth.doctor.ui.detailedRegistration.model.GetPatientAllVisitsResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.AdmissionPatientUpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.AdmissionSaveRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.AdmissionUpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.TrasfferedRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response.*
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.add_consultations.AnaesthesiaNotesAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.add_consultations.AnaesthesiaNotesAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.get_default.GetAnesthesiaNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.prev_records.GetAnesthesiaNotesPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.prev_records.observed_values.GetAnesthesiaNotesObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.set_default.SetAnesthesiaNotesDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.set_default.SetAnesthesiaNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.certificate.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.PrevChiefComplainResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.request.ChiefComplaintRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.response.ChiefComplaintResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.ChiefComplaintFavAddresponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.ChiefCompliantAddRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.compare_data.GetCriticalCareChartCompareDataResp
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartFilterHeadingsResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.SaveCriticalCareChartConfig
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.DiagnosisRequest
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.DiagnosisResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.PreDiagnosisResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.DiagonosisSearchResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedChildDataResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedDataResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedParentDataResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.DietFavMangeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.GetPreviousDietOrderResp
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.ResponseDietGetTemplateDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.SearchDietResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.DietOrderrequest
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.GetPreviousDietOrderReq
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.RequestDietFavModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.template_department.GetAllDepartmentReq
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.template_department.GetAllDepartmentResp
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.DischargeSummaryListResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.previous_model.DischargeSummaryPreviousResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.model.AddDocumentDetailsResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.model.DeleteDocumentResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.model.DocumentTypeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.model.FileUploadResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.model.AdmissionReferalResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.*
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.edit.EditAllergyBindResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.period.PeriodsResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.DiagnosisSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.HistoryDiagnosisCreateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.HistoryDiagnosisResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.HistoryDiagnosisUpdateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.model.CreateFamilyHistoryResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.model.FamilyHistoryResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.model.FamilyTypeSpinnerResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.model.FamilyUpdateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.model.edit.EditImmunizationResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.model.response.HistoryResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.model.PrescriptionHistoryResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response.*
import com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response.edit.EditSurgeryResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.vitals.model.response.HistoryVitalsResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.requset_model.InvestigationRequset
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.models.InvUpdateRequest
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.model.InvestigationResultResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.add_consultations.IpCaseSheetAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.add_consultations.IpCaseSheetAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.get_default.GetIpCaseSheetDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records.GetIpCaseSheetPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records.observed_values.GetIpCaseSheetObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.set_default.SetIpCaseSheetDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.set_default.SetIpCaseSheetDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavSearchResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.labviewresponse.LabViewResponseModule
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.RequestLabFavModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseLabGetTemplateDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.LabResultGetByDataResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.LabResultResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare.LabCompareResp
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request.LabTechSearch
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request.RequestLmisNewOrder
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.response.GetWardIdResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.response.ResponseLmisListview
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Request.EmrRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.GetStoreMasterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.PatientDetailResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.PatientLatestRecordResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.CaseSheetRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.CaseSheetResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.MRDResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.add_consultations.OpNotesAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.add_consultations.OpNotesAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.get_default.GetOpNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.GetOpNotesPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.observed_values.GetOpNotesObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.set_default.SetOpNotesDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.set_default.SetOpNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.add_consultations.OtNotesAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.add_consultations.OtNotesAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.get_default.GetOtNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.GetOtNotesPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.observed_values.GetOtNotesObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.set_default.SetOtNotesDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.set_default.SetOtNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.delete.DeleteOtScheduleReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.delete.DeleteOtScheduleResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.modify.ModifyOtScheduleReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.modify.ModifyOtScheduleResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtNameSpinnerResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtSchedulToCalendarResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtSurgeryNameResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtTypeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.*
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.save.AddSurgeryResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners.*
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.view.ViewOtScheduleReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.view.ViewOtScheduleResp
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.requestparamter.RequestPrecEditModule
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.updaterequest.UpdatePrescriptionRequest
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.updateresponse.ResponsePreFavEdit
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.SaveTemplateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.SearchPrescriptionResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.response.SaveTemplateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request.UpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.response.UpdateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.model.RadiologyTopResponseMOdel
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model.SpecalityListResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchFavMangeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchPrevResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.OrderSaveTreatmentKitResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.TreatAddFavRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.TreatmentKitPrevResponsModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.TreatmentkitSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.drug_info.GetDrugInfoReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.drug_info.GetDrugInfoResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search.GetInvestigationCodeSearchReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search.GetInvestigationCodeSearchResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify.UpdateTreatmentKitPreviousOrderReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify.UpdateTreatmentKitPreviousOrderResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.radiology_code_search.GetRadiologyCodeSearchReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.radiology_code_search.GetRadiologyCodeSearchResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request.CreateTreatmentkitRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request.TKOrderRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response.*
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search.AutoSearchReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search.AutoSearchResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search.TreatmentKitFavouriteResp
import com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.model.PrevLabResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalSaveRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalsListResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalsSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalsTemplateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.previous_vitals.GetPrevPatientVitalResp
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.request.VitalFavUpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response.VitalSaveResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response.VitalSearchListResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response.VitalSearchNameResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.responseedittemplatevitual.ResponseEditTemplate
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.responseuommodule.ResponseUOMModules
import com.oasys.digihealth.doctor.ui.helpdesk.model.*
import com.oasys.digihealth.doctor.ui.institute.model.DepartmentResponseModel
import com.oasys.digihealth.doctor.ui.institute.model.GetLocationRequest
import com.oasys.digihealth.doctor.ui.institute.model.InstitutionResponseModel
import com.oasys.digihealth.doctor.ui.institute.model.OfficeResponseModel
import com.oasys.digihealth.doctor.ui.institute.model.Phermisiun.StoreListResponseModel
import com.oasys.digihealth.doctor.ui.institute.model.wardList.WardListResponseModel
import com.oasys.digihealth.doctor.ui.login.model.LoginSeesionRequest
import com.oasys.digihealth.doctor.ui.login.model.SimpleResponseModel
import com.oasys.digihealth.doctor.ui.login.model.login_response_model.LoginResponseModel
import com.oasys.digihealth.doctor.ui.myprofile.model.MyProfileResponseModel
import com.oasys.digihealth.doctor.ui.order_status.model.OrderStatusRequestModel
import com.oasys.digihealth.doctor.ui.order_status.model.OrderStatusResponseModel
import com.oasys.digihealth.doctor.ui.order_status.model.OrderStatusSpinnerResponseModel
import com.oasys.digihealth.doctor.ui.order_status.model.TestNameResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.model.requestaddpatient.RequestAddPatient
import com.oasys.digihealth.doctor.ui.out_patient.model.responseaddpatient.ResponseAddPatient
import com.oasys.digihealth.doctor.ui.out_patient.model.search_request_model.SearchPatientRequestModel
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.InPatientResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.MyPatientsResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.OldPatientResponseModule
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.SearchResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.*
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.SampleAcceptedRequest
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.LabAssignedToResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.LabTestSpinnerResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.RejectReferenceResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.UserProfileResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response.LabFilterResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.LabNameSearchResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.QuickRegistrationRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.QuickRegistrationUpdateRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.SaveOrderRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.session.ResponseSesionModule
import com.oasys.digihealth.doctor.ui.quick_reg.model.template.GetNoteTemplateReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.template.GetNoteTemplateResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.testprocess.sampleTransportRequestModel
import com.oasys.digihealth.doctor.ui.quickregistration.model.*
import com.oasys.digihealth.doctor.ui.quickregistration.model.response.PDSResponseModule
import com.oasys.digihealth.doctor.ui.quickregistration.model.response.QuickelementRoleResponseModel
import com.oasys.digihealth.doctor.ui.resultdispatch.model.ResponseResultDispatch
import com.oasys.digihealth.doctor.ui.resultdispatch.request.RequestDispatchSearch
import com.oasys.digihealth.doctor.ui.resultdispatch.request.Requestpdf
import com.oasys.digihealth.doctor.ui.sample_dispatch.model.DispatchReq
import com.oasys.digihealth.doctor.ui.sample_dispatch.model.OrderByResponseModel
import com.oasys.digihealth.doctor.ui.sample_dispatch.model.SampleDispatchRequest
import com.oasys.digihealth.doctor.ui.sample_dispatch.model.SampleDispatchResponseModel
import com.oasys.digihealth.doctor.ui.tutorial.model.RoleControlActivityResponseModel
import com.oasys.digihealth.doctor.ui.tutorial.model.UserManualDeleteResponseModel
import com.oasys.digihealth.doctor.ui.tutorial.model.UserManualResponseModel
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import retrofit2.http.Header
import java.util.concurrent.TimeUnit

interface APIService {
    object Factory {
        fun create(context: Context?): APIService {
            val b = OkHttpClient.Builder()
            b.readTimeout(
                AppConstants.TIMEOUT_VALUE.toLong(),
                TimeUnit.MILLISECONDS
            )
            b.writeTimeout(
                AppConstants.TIMEOUT_VALUE.toLong(),
                TimeUnit.MILLISECONDS
            )
            val gson = GsonBuilder()
                .serializeNulls()
                .setLenient()
                .create()
//            if
//            {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
//                val client = b.addNetworkInterceptor(StethoInterceptor()).build()
            val client = b.addInterceptor(ChuckInterceptor(context))
                .addInterceptor(logging)
                .build()
//            }
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return retrofit.create(APIService::class.java)
        }
    }

    //Login
    @FormUrlEncoded
    @POST(ApiUrl.Login)
    fun getLoginDetails(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<LoginResponseModel?>?

    //   @FormUrlEncoded
    /*
    @POST(Login)
    Call<LoginResponseModel> getLoginDetails(@Body RequestBody req);
*/

    @POST(ApiUrl.setPassword)
    fun setPassword(
        @Body requestbody: RequestBody?
    ): Call<SimpleResponseModel?>?

    //Search
    @POST(ApiUrl.Register)
    fun searchOutPatient(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body searchPatientRequestModel: SearchPatientRequestModel?
    ): Call<SearchResponseModel?>?

    @POST(ApiUrl.Mypatient)
    fun searchMypatient(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<MyPatientsResponseModel?>?

    //DashBoard
    @GET(ApiUrl.DashBoardDetail)
    fun getDashBoardResponse(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Query("depertment_Id") depertment_id: Int,
        @Query("from_date") fromData: String?,
        @Query("to_date") toDate: String?,
        @Query("gender") gender: String?,
        @Query("session") session: String?,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<DashBoardResponse?>?

    //Blue Print
    @GET(ApiUrl.getBluePrintData)
    fun getBluePrintData(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("facility_uuid") facilityuuid: String?,
        @Query("department_uuid") department_uuid: String?,
        @Query("ward_uuid") ward_uuid: String?
    ): Call<BluePrintResponseModel?>?


    //GetEMRWorkFlow
    @GET(ApiUrl.EMRWORKFLOW)
    fun getEmrWorkflow(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int
    ): Call<EmrWorkFlowResponseModel?>?

    //GetEMRWorkFlowForOpAndIp
    @GET(ApiUrl.EMRWORKFLOW)
    fun getEmrWorkflowForIpAndOp(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Query("context_uuid") contextId: Int
    ): Call<EmrWorkFlowResponseModel?>?

    //Configuation
    @POST(ApiUrl.GetConfigList)
    fun getConfigList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body body: RequestBody?
    ): Call<ConfigResponseModel?>?

    //Config Create
    @POST(ApiUrl.configCreate)
    fun getConfigCreate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<ConfigUpdateRequestModel?>?
    ): Call<ConfigUpdateResponseModel?>?


    //Config Update
    @PUT(ApiUrl.GetconfigUpdate)
    fun getConfigUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<ConfigUpdateRequestModel?>?
    ): Call<ConfigUpdateResponseModel?>?

    //Config Update
    @PUT(ApiUrl.GetNurseconfigUpdate)
    fun getNurseConfigUpdate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<NurseUpdateRequestModule?>?
    ): Call<ConfigUpdateResponseModel?>?

    @POST(ApiUrl.GetNurseconfigCreate)
    fun getNurseConfigCreate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<NurseUpdateRequestModule?>?
    ): Call<ConfigUpdateResponseModel?>?

    @GET(ApiUrl.GetFavorites)
    fun getFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Query("fav_type_id") fav_type_id: Int
    ): Call<FavouritesResponseModel?>?


    @GET(ApiUrl.GetFavorites)
    fun getRadiologyFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Query("fav_type_id") fav_type_id: Int
    ): Call<RadiologyFavResponse?>?


    @GET(ApiUrl.GetFavorites)
    fun getLabFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Query("fav_type_id") fav_type_id: Int
    ): Call<LabFavResponseModel?>?


    @GET(ApiUrl.GetFavorites)
    fun getPrescriptionFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Query("fav_type_id") fav_type_id: Int,
        @Query("store_master_uuid") store_master_uuid: Int?
    ): Call<FavouritesResponseModel?>?

    /*
      Templete
     */
    @GET(ApiUrl.GetTemplete)
    fun getTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("dept_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int
    ): Call<TempleResponseModel?>?


    /*
    Templete
   */
    @GET(ApiUrl.GetTemplete)
    fun getLmisTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("lab_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int
    ): Call<TempleResponseModel?>?


    /*
   Templete investigation
  */
    @GET(ApiUrl.GetInvestigationTemplete)
    fun getInvestigationTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("temp_type_id") temp_type_id: Int
    ): Call<InvestigationTemplateResponse?>?

    @GET(ApiUrl.GetPrescriptionTemplete)
    fun getPrescriptionTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("dept_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int
    ): Call<PrescriptionTemplateResponseModel?>?

    @GET(ApiUrl.GetPrescriptionTemplete)
    fun getPrescriptionStoreTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("dept_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("store_master_uuid") store_master_uuid: Int?
    ): Call<PrescriptionTemplateResponseModel?>?

    @POST(ApiUrl.GetOfficeList)
    fun getOfficeList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<OfficeResponseModel?>?

    @GET(ApiUrl.GetDuration)
    fun getDuration(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int
    ): Call<DurationResponseModel?>?

    @POST(ApiUrl.GetAllergySource)
    fun getPeriod(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body body: RequestBody?
    ): Call<PeriodsResponseModel?>?

    @GET(ApiUrl.GetChiefComplaintsSearchResult)
    fun getChiefComplaintsSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Query("searchBy") searchBy: String?,
        @Query("searchValue") searchValue: String?
    ): Call<ComplaintSearchResponseModel?>?

    @POST(ApiUrl.GetLabSearchResult)
    fun getLAbSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavSearchResponce?>?

    //Treatmentkit
    @POST(ApiUrl.getRadiologyCodeSearchUrl)
    fun getRadiologyCodeSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getRadiologyCodeSearchReq: GetRadiologyCodeSearchReq
    ): Call<GetRadiologyCodeSearchResp>

    @POST(ApiUrl.getInvestigationCodeSearchUrl)
    fun getInvestigationCodeSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getInvestigationCodeSearchReq: GetInvestigationCodeSearchReq
    ): Call<GetInvestigationCodeSearchResp>

    @POST(ApiUrl.PrescriptionDrugInfo)
    fun getDrugInfo(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetDrugInfoReq
    ): Call<GetDrugInfoResp>

    //Diet
    @POST(ApiUrl.GetDietSearch)
    fun getDietSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SearchDietResponse?>?

    @POST(ApiUrl.GetRadioSearchResult)
    fun getRadioSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavSearchResponce?>?


    @POST(ApiUrl.GetRadioSearchResult)
    fun getRadioRmisSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LabTestSpinnerResponseModel?>?

    @POST(ApiUrl.GetINvestigationsSearchResult)
    fun getInvestigationSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<InvestigationSearchResponseModel?>?

    //getInstitutionlistrrrww
    @POST(ApiUrl.GetInstitutionList)
    fun getInstitutionList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<InstitutionResponseModel?>?

    /**
     * Get Department List
     *
     * @param authorization
     * @param user_uuid
     * @param body
     * @return
     */
    @POST(ApiUrl.GetDepartmentList)
    fun getDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<DepartmentResponseModel?>?

    @GET(GetEncounters)
    fun getEncounters(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<FectchEncounterResponseModel?>?

    @POST(ApiUrl.CreateEncounter)
    fun createEncounter(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body createEncounterRequestModel: CreateEncounterRequestModel?
    ): Call<CreateEncounterResponseModel?>?

    /*
    Type
     */
    @POST(ApiUrl.GetLabType)
    fun getLabType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body labtype: RequestBody?
    ): Call<LabTypeResponseModel?>?

    @POST(ApiUrl.GetInvestigationType)
    fun getInvestigationType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body labtype: RequestBody?
    ): Call<InvestigationTypeResponseModel?>?


    @POST(ApiUrl.GetRadiologyType)
    fun getRadiologyType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body labtype: RequestBody?
    ): Call<RadiologyTypeResponseModel?>?

    /*TKRadioType*/
    @POST(ApiUrl.getTKRadioType)
    fun getTKRadioType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body type: RequestBody?
    ): Call<RadiologyTypeResponseModel?>?

    /*Get */
    @POST(ApiUrl.getNurseDeskNotesPatientList)
    fun getNurseDeskNotesPatientList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getNurseDeskNotesPatientListReq: GetNurseDeskNotesPatientListReq?
    ): Call<GetNurseDeskNotesPatientListResp>

    /*GET to Location*/
    @POST(ApiUrl.GetToLocation)
    fun getToLocation(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<LabToLocationResponse?>?

    @POST(ApiUrl.GetInvestigationToLocation)
    fun getInvestigationToLocation(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<InvestigationLoationResponseModel?>?

    @POST(ApiUrl.GetInvestigationToLocationMapId)
    fun getInvestigationToLocationMapId(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody
    ): Call<GetToLocationTestResponse?>?


    @POST(ApiUrl.GetPrevLab)
    fun getPrevLab(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrevLabResponseModel?>?

    @POST(ApiUrl.GetPrevLab)
    fun getPrevLmisLab(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrevLabLmisResponseModel?>?

    @POST(ApiUrl.GetPrevInvestigation)
    fun getPrevInvestigation(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<InvestigationPrevResponseModel?>?

    @POST(ApiUrl.GetFavDepartmentList)
    fun getFavDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddResponseModel?>?

    @POST(ApiUrl.lmisretest)
    fun lmisRetest(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.GetFavaddDepartmentList)
    fun getFavddAllADepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddAllDepatResponseModel?>?

    @POST(ApiUrl.getUserDepartment)
    fun getUserDepartment(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddAllDepatResponseModel?>?

    @POST(ApiUrl.GetFavaddDepartmentList)
    fun getSearchDepartment(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetAllDepartmentRequest?
    ): Call<FavAddAllDepatResponseModel?>?


    @POST(ApiUrl.getSchema)
    fun getAllSchema(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SchemaRequest?
    ): Call<SchemaResponseModel?>?


    @POST(ApiUrl.getSchemaName)
    fun getSchemaName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SchemaNameResponce?>?


    @POST(ApiUrl.GetFavaddDepartmentList)
    fun getSearchDepartmentAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetAllDepartmentRequestModel?
    ): Call<FavAddAllDepatResponseModel?>?

    @POST(ApiUrl.EmrPost)
    fun Emrpost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body emrRequestModel: EmrRequestModel?
    ): Call<EmrResponceModel?>?

    //DEVHMIS-LIS/v1/api/testmaster/gettestandprofileinfo
    @POST(ApiUrl.GetLabSearchResult)
    fun getAutocommitText(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddTestNameResponse?>?

    @POST(ApiUrl.GetRadioSearchResult)
    fun getRadioAutocommitText(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddTestNameResponse?>?

    @GET(GetVitalsTemplatet)
    fun getVitalsTemplatet(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("user_uuid") user_uid: Int
    ): Call<VitalsTemplateResponseModel?>?

    @POST(ApiUrl.InsertChiefComplaint)
    fun insertChiefComplaint(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<ChiefComplaintRequestModel>
    ): Call<ChiefComplaintResponse?>?

    @GET(GetHistoryAll)
    fun getHistoryAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<HistoryResponce?>?

    @GET(GetEncounterAllergyType)
    fun getEncounterAllergyType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<EncounterAllergyTypeResponse?>?

    /*
  Radiology Prev-Radiology
   */
    @POST(ApiUrl.GetPrevRadiology)
    fun getPrevRadiology(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrevLabResponseModel?>?


    @POST(ApiUrl.GetPrevRadiology)
    fun getPrevRmisLab(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrevLabLmisResponseModel?>?

    @POST(ApiUrl.GetAllergyName)
    fun getAllergyName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<AllergyNameResponse?>?

    @POST(ApiUrl.GetAllergySource)
    fun getAllergySource(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body allergysource: RequestBody?
    ): Call<AllergySourceResponse?>?

    @POST(ApiUrl.GetAllergySeverity)
    fun getAllergySeverity(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body allergysource: RequestBody?
    ): Call<AllergySeverityResponse?>?

    @POST(ApiUrl.GetPrevPrescription)
    fun getPrevPrescription(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrevPrescriptionModel?>?

    @POST(ApiUrl.GetFrequency)
    fun getFrequency(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PresDrugFrequencyResponseModel?>?

    @POST(ApiUrl.GetPrescriptionDuration)
    fun getPrescriptionDuration(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrescriptionDurationResponseModel?>?

    @POST(ApiUrl.InsertDiagnosis)
    fun insertDiagnosis(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<DiagnosisRequest>
    ): Call<DiagnosisResponseModel?>?

    @GET(SearchDiagnosis)
    fun searchDiagnosis(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("searchBy") searchBy: String?,
        @Query("searchValue") searchValue: String?
    ): Call<DiagnosisSearchResponseModel?>?

    /*
    Radiology ORDER TO LOCATION
     */
    @POST(ApiUrl.GetToLocationRadiology)
    fun getToLocationRadiology(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<LabToLocationResponse?>?

    @POST(ApiUrl.GetToLocationRadiology)
    fun GetToLocationTReatmentKit(
        @Header("Accept-Language") lang: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<LabToLocationResponse?>?


    /*
    Radiology post data
     */
    @POST(ApiUrl.EmrRadiologypost)
    fun EmrRadiologypost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body emrRequestModel: EmrRequestModel?
    ): Call<EmrResponceModel?>?

    @POST(ApiUrl.EmrRadiologypost)
    fun rmisEmrpost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestLmisNewOrder: RequestLmisNewOrder?
    ): Call<EmrResponceModel?>?

    /*
    Prescription Search*/
    @POST(ApiUrl.GetPrescriptionsSearchResult)
    fun getPrescriptionsSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrescriptionSearchResponseModel?>?

    @POST(ApiUrl.GetInstruction)
    fun getInstruction(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PresInstructionResponseModel?>?

    /*
    Prescription all data post
     */
    @POST(ApiUrl.EmrPrescriptionPost)
    fun EmrPrescriptionPost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body emrPrescriptionRequestModel: emr_prescription_postalldata_requestmodel?
    ): Call<PrescriptionPostAllDataResponseModel?>?


    @POST(ApiUrl.updatePrescription)
    fun EmrPrescriptionUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body emrPrescriptionRequestModel: PrescriptionUpdateRequest?
    ): Call<PrescriptionPostAllDataResponseModel?>?

    /*
    Route
     */
    @POST(ApiUrl.GetRouteDetails)
    fun getRouteDetails(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrescriptionRoutResponseModel?>?

    @GET(GetAllergyAll)
    fun getAllergyAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<AllergyAllGetResponseModel?>?

    @PUT(DeleteRows)
    fun deleteRows(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DeleteResponseModel?>?

    @PUT(DeleteTemplate)
    fun deleteTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DeleteResponseModel?>?

    @POST(ApiUrl.InjectioType)
    fun getInjectionType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<InjectionDepartMentResponce?>?


    @POST(ApiUrl.GetFavddAll)
    fun getFavddAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestLabFavModel?
    ): Call<LabFavManageResponseModel?>?

    @POST(ApiUrl.GetFavddInvestigationAll)
    fun getFavddInvestigationAll(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: InvestigationFavRequestModel?
    ): Call<InvestigationFavManageResponseModel?>?

    @POST(ApiUrl.GetRadiologyFavourite)
    fun getRadilogyFavAddAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestRadiologyFavModel?
    ): Call<LabFavManageResponseModel?>?

    @POST(ApiUrl.GetFavAddDiagonosis)
    fun getDiagonosisFavAddAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: ChiefCompliantAddRequestModel?
    ): Call<LabFavManageResponseModel?>?

    @GET(GetFavddAllList)
    fun getFavddAllList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("favourite_id") favourite_id: Int,
        @Query("favourite_type_id") favourite_type_id: Int
    ): Call<FavAddListResponse?>?

    @GET(GetFavddAllList)
    fun getFavddAllInvestList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("favourite_id") favourite_id: Int,
        @Query("favourite_type_id") favourite_type_id: Int
    ): Call<ManageFavAddResponse?>?

    @GET(GetFavddAllList)
    fun getFavddAllRadiologyList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("favourite_id") favourite_id: Int,
        @Query("favourite_type_id") favourite_type_id: Int
    ): Call<ManageRadiologyFavAddResponseModel?>?

    @POST(ApiUrl.GetMyProfile)
    fun getMyProfile(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<MyProfileResponseModel?>?

    @POST(ApiUrl.GetOtpForPasswordChange)
    fun getOtpForPasswordChange(@Body body: RequestBody?): Call<ChangePasswordOTPResponseModel?>?

    @POST(ApiUrl.GetPasswordChanged)
    fun getPasswordChanged(@Body body: RequestBody?): Call<PasswordChangeResponseModel?>?

    @POST(ApiUrl.GetHistoryPrescription)
    fun getHistoryPrescription(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrescriptionHistoryResponseModel?>?

    @GET(GetFamilyAllType)
    fun getFamilyAllType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<FamilyHistoryResponseModel?>?

    @POST(ApiUrl.AddFavChiefComplaint)
    fun AddFavChiefComplaint(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("searchkey") searchkey: String?,
        @Body chiefCompliantAddRequestModel: ChiefCompliantAddRequestModel?
    ): Call<ChiefComplaintFavAddresponseModel?>?

    @POST(ApiUrl.CreateAllergy)
    fun createAllergy(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<AllergyCreateResponseModel?>?

    @GET(getHistoryVitals)
    fun getHistoryVitals(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("department_uuid") department_uuid: Int
    ): Call<HistoryVitalsResponseModel?>?

    @GET(VitalSearch)
    fun getVitalsName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<VitalsListResponseModel?>?

    @POST(ApiUrl.getZeroStock)
    fun getZeroStock(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ZeroStockResponseModel?>?

    //Config Update
    @PUT(GetHistoryconfigUpdate)
    fun getHistoryConfigUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<HistoryConfigUpdateRequestModel?>?
    ): Call<ConfigUpdateResponseModel?>?

    // config create

    @POST(ApiUrl.GetHistoryconfigCreate)
    fun getHistoryConfigCreate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<HistoryConfigUpdateRequestModel?>?
    ): Call<ConfigUpdateResponseModel?>?

    @POST(ApiUrl.GetFamilyType)
    fun getHistoryFamilyType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FamilyTypeSpinnerResponseModel?>?

    @POST(ApiUrl.CreateFamilyHistory)
    fun createFamilyHistory(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<CreateFamilyHistoryResponseModel?>?

    @POST(ApiUrl.GetSurgeryInstitutions)
    fun getInstitutions(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<SurgeryInstitutionResponseModel?>?

    @POST(ApiUrl.GetSurgeryInstitutions)
    fun getFaciltyCheck(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body body: RequestBody?
    ): Call<SurgeryInstitutionResponseModel?>?

    @GET(GetSurgeryDetails)
    fun getSurgery(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuidd: Int
    ): Call<HistorySurgeryResponseModel?>?

    @POST(ApiUrl.GetSurgeryName)
    fun getName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SurgeryNameResponseModel?>?

    @POST(ApiUrl.CreateSugery)
    fun createSurgery(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<CreateSurgeryResponseModel?>?

    @GET(getDiagnosisHistory)
    fun getDiagnosisList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("searchKey") searchKey: String?,
        @Query("searchValue") searchValue: Int,
        @Query("patientId") patientId: Int,
        @Query("departmentId") departmentId: Int
    ): Call<HistoryDiagnosisResponseModel?>?

    @POST(ApiUrl.PrescriptionInfo)
    fun getPrescriptionInfo(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrescriptionInfoResponsModel?>?

    @POST(ApiUrl.PrescriptionDrugInfo)
    fun getPrescriptionDrugInfo(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PresDrugInfoResponse?>?

    @PUT(FavouriteUpdate)
    fun labEditFav(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavEditResponse?>?

    @PUT(FavouriteUpdate)
    fun PresEditFav(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: UpdatePrescriptionRequest?
    ): Call<ResponsePreFavEdit?>?

    @POST(ApiUrl.LabTemplateCreate)
    fun createTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestTemplateAddDetails?
    ): Call<ReponseTemplateadd?>?

    @POST(ApiUrl.LabTemplateCreate)
    fun createRadiologyTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RadioogyTemplateRequest?
    ): Call<ManageRadiologyTemplateResponseModel?>?

    @POST(ApiUrl.GetImmunizationName)
    fun getImmunizationName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<ImmunizationNameResponseModel?>?

    @GET(GetImmunizationList)
    fun getImmunizationAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetImmunizationResponseModel?>?

    @POST(ApiUrl.CreateImmunization)
    fun createImmunization(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<CreateImmunizationResponseModel?>?

    @POST(ApiUrl.GetImmunizationInstitution)
    fun getImmunizationInstitution(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ImmunizationInstitutionResponseModel?>?

    @GET(LabGetTemplate)
    fun getLastTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("dept_id") dept_id: Int
    ): Call<ResponseLabGetTemplateDetails?>?

    @GET(LabGetTemplate)
    fun InvestigationGetTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("dept_id") dept_id: Int
    ): Call<InvestigationGetTemplateDetailsResponse?>?

    @GET(LabGetTemplate)
    fun getDietTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("dept_id") dept_id: Int
    ): Call<ResponseDietGetTemplateDetails?>?

    @GET(VitualGetTemplate)
    fun getLastVitualTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("dept_id") dept_id: Int
    ): Call<ResponseEditTemplate?>?

    /* @GET(VitualGetTemplate)
     fun getVitualTemplate(
         @Header("Authorization") authorization: String?,
         @Header("user_uuid") user_uuid: Int,
         @Header("facility_uuid") facility_uuid: Int,
         @Query("temp_id") temp_id: Int,
         @Query("temp_type_id") temp_type_id: Int,
         @Query("dept_id") dept_id: Int
     ): Call<TempEditResponseModel?>?*/

    @PUT(LabUpdateTemplate)
    fun getTemplateUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: UpdateRequestModule?
    ): Call<UpdateResponse?>?

    @POST(ApiUrl.GetAllergySource)
    fun gatUomVitalList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<UOMNewReferernceResponseModel?>?


    @GET(DiagonosisSearcbValue)
    fun getDiagonosisName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("searchBy") searchby: String?,
        @Query("searchValue") searchValue: String?
    ): Call<DiagonosisSearchResponse?>?

    @POST(ApiUrl.PrescriptionSearch)
    fun getprescriptionSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SearchPrescriptionResponseModel?>?

    @POST(ApiUrl.LabTemplateCreate)
    fun savePrescriptionTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SaveTemplateRequestModel?
    ): Call<SaveTemplateResponseModel?>?

    @PUT(LabUpdateTemplate)
    fun updatePrescriptionTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: UpdateRequestModel?
    ): Call<UpdateResponseModel?>?

    @POST(ApiUrl.labResult)
    fun getLabResult(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LabResultResponseModel?>?


    @POST(ApiUrl.labResultCompere)
    fun getLabResultCompere(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LabCompareResp?>?


    @POST(ApiUrl.radiologyTopResult)
    fun getradiologyTopResult(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<RadiologyTopResponseMOdel?>?

    @POST(ApiUrl.invTopResult)
    fun getinvTopResult(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<InvestigationResultResponseModel?>?


/*
    @POST(ApiUrl.radiologyResult)
    fun getRadiologyResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<Rad?>?
*/

    @POST(ApiUrl.investigationResult)
    fun getInvestigationResult(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<InvestigationResultResponseModel?>?

    @POST(ApiUrl.gettolocationmapid)
    fun gettolocationmapid(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<MapListResponseModel?>?

    @PUT(UpdateAllergy)
    fun updateAllergy(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("uuid") uuid: Int,
        @Body body: RequestBody?
    ): Call<AllergyUpdateResponseModel?>?

    @PUT(UpdateFamilyHistory)
    fun updateFamilyHistory(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("uuid") uuid: Int,
        @Body body: RequestBody?
    ): Call<FamilyUpdateResponseModel?>?

    @PUT(UpdateSurgery)
    fun updateSurgery(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("uuid") uuid: Int,
        @Body body: RequestBody?
    ): Call<SurgeryUpdateResponseModel?>?

    @GET(GetVitalSearchName)
    fun getAllVital(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<VitalsSearchResponseModel?>?

    @POST(ApiUrl.VitalSave)
    fun saveVitals(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<VitalSaveRequestModel>
    ): Call<VitalSaveResponseModel?>?

    @GET(VitalSearch)
    fun getVitals(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<VitalSearchListResponseModel?>?

    @POST(ApiUrl.GetStoreMaster)
    fun getStoreMaster(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetStoreMasterResponseModel?>?

    @POST(ApiUrl.PrescriptionFavAdd)
    fun getPrescriptionFavAddAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestPrecEditModule?
    ): Call<ResponsePrescriptionFav?>?

    @POST(ApiUrl.GetLabResultByDate)
    fun getLabResultByDate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LabResultGetByDataResponseModel?>?

    @POST(ApiUrl.GetPrevTreatmentKit)
    fun getPrevTreatmentKit(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") uuid: Int
    ): Call<TreatmentKitPrevResponsModel?>?

    @POST(ApiUrl.CreateHistoryDiagnosis)
    fun createDiagnosis(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<HistoryDiagnosisCreateResponseModel?>?

    @GET(GetSnommed)
    fun getSnommed(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("key") key: String?
    ): Call<SnomedDataResponseModel?>?

    @GET(GetParentSnommed)
    fun getParentSnommed(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("key") key: String?
    ): Call<SnomedParentDataResponseModel?>?

    @GET(GetChildSnommed)
    fun getChildSnommed(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("key") key: String?
    ): Call<SnomedChildDataResponseModel?>?

    @POST(ApiUrl.getState)
    fun getState(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<StateListResponseModel?>?

    @POST(ApiUrl.getDistict)
    fun getDistict(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DistrictListResponseModel?>?

    @POST(ApiUrl.getTaluk)
    fun getTaluk(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<TalukListResponseModel?>?

    @POST(ApiUrl.getVillage)
    fun getVillage(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<VilliageListResponceModel?>?

    @POST(ApiUrl.GetSalutationTitiles)
    fun getCovidNameTitle(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<CovidSalutationTitleResponseModel?>?

    @POST(ApiUrl.GetGender)
    fun getCovidGender(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<CovidGenderResponseModel?>?

    @GET(GetPeriod)
    fun getCovidPeriod(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<CovidPeriodResponseModel?>?

    @GET(GetNationalityAndMobileAndPatientCateType)
    fun getNationality(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("table_name") tableName: String?
    ): Call<CovidNationalityResponseModel?>?

    @POST(ApiUrl.GetNationalityAllActive)
    fun getAllActiveNationality(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<CovidNationalityResponseModel?>?

    @GET(GetNationalityAndMobileAndPatientCateType)
    fun getMobileBelongsTo(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("table_name") tableName: String?
    ): Call<CovidMobileBelongsToResponseModel?>?

    @GET(GetNationalityAndMobileAndPatientCateType)
    fun getPatientCategory(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("table_name") tableName: String?
    ): Call<CovidPatientCategoryResponseModel?>?

    @POST(ApiUrl.addPatientDetails)
    fun getAddPatientDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AddPatientDetailsRequest?
    ): Call<AddPatientResponse?>?

    //dispatch
    @POST(ApiUrl.sampledispatch)
    fun dispatch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: DispatchReq?
    ): Call<SampleDispatchResponseModel?>?

    @POST(ApiUrl.getAll)
    fun getAllDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetAllResponseModel?>?

    @GET(getConsolidatedTestWiseServerTime)
    fun getConsolidatedTestWiseServerTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetServerTimeResp>

    @POST(ApiUrl.getConsolidatedTestWiseInstitutionDropdown)
    fun getConsolidatedTestWiseInstitutionDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getInstitutionDropdownReq: GetInstitutionDropdownReq
    ): Call<GetInstitutionDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseHealthOfficeDropdown)
    fun getConsolidatedTestWiseHealthOfficeDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getHealthOfficeDropdownReq: GetHealthOfficeDropdownReq
    ): Call<GetHealthOfficeDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseInstitutionTypeDropdown)
    fun getConsolidatedTestWiseInstitutionTypeDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getInstitutionTypeDropdownReq: GetInstitutionTypeDropdownReq
    ): Call<GetInstitutionTypeDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseBlockDropdown)
    fun getConsolidatedTestWiseBlockDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getBlockDropdownReq: GetBlockDropdownReq
    ): Call<GetBlockDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseHudDropdown)
    fun getConsolidatedTestWiseHudDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getHudDropdownReq: GetHudDropdownReq
    ): Call<GetHudDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseDistrictDropdownFilter)
    fun getConsolidatedTestWiseDistrictDropdownFilter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getDistrictDropdownFilterReq: GetDistrictDropdownFilterReq
    ): Call<GetDistrictDropdownFilterResp>

    @POST(ApiUrl.getConsolidatedTestWiseInstitutionCategory)
    fun getConsolidatedTestWiseInstitutionCategory(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getInstitutionCategoryReq: GetInstitutionCategoryReq
    ): Call<GetInstitutionCategoryResp>

    @POST(ApiUrl.getConsolidatedTestWiseDepartmentDropdown)
    fun getConsolidatedTestWiseDepartmentDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getDepartmentDropdownReq: GetDepartmentDropdownReq
    ): Call<GetDepartmentDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseTestNameDropdown)
    fun getConsolidatedTestWiseTestNameDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getTestNameDropdownReq: GetTestNameDropdownReq
    ): Call<GetTestNameDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseLabNameDropdown)
    fun getConsolidatedTestWiseLabNameDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getLabNameDropdownReq: GetLabNameDropdownReq
    ): Call<GetLabNameDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseOrderStatusDropdown)
    fun getConsolidatedTestWiseOrderStatusDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getOrderStatusDropdownReq: GetOrderStatusDropdownReq
    ): Call<GetOrderStatusDropdownResp>

    @POST(ApiUrl.getConsolidatedTestWiseGenderDropdown)
    fun getConsolidatedTestWiseGenderDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getGenderDropdownReq: GetGenderDropdownReq
    ): Call<GetGenderDropdownResp>

    @GET(GetSpecimen_Type)
    fun getSpecimen_Type(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("Accept-Language") value: String?,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("table_name") table_name: String?
    ): Call<ResponseSpicemanType?>?

    @GET(GetQuarantineType)
    fun getQuarantineType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<CovidQuarantineTypeResponseModel?>?

    @POST(ApiUrl.getCovidPatientSearch)
    fun getCovidPatientSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("Accept-Language") value: String?,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<CovidRegistrationSearchResponseModel?>?

    @PUT(CovidUpdate)
    fun getUpdatePatientDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: UpdatePatientDetailsRequest?
    ): Call<AddPatientResponse?>?

    @POST(ApiUrl.CreateTreatmentKit)
    fun postTreatmentKit(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: CreateTreatmentkitRequestModel?
    ): Call<TreatmentKitCreateResponseModel?>?

    @GET(PrevChiefComplaint)
    fun getPrevChiefComplaint(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("encounterTypeId") encounterTypeId: Int?,
        @Query("patientId") patientId: String?,
        @Query("limit") limit: String?
    ): Call<PrevChiefComplainResponseModel?>?

    @POST(ApiUrl.getTreatmentkitInvestigationSearch)
    fun getTraetmentkitSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<TreatmentkitSearchResponseModel?>?

    @POST(ApiUrl.GetPrescFrequency)
    fun getPrescFrequency(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrescriptionFrequencyResponseModel?>?

    @GET(PrevDiagnosis)
    fun getPrevDiagnosis(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("encounterTypeId") encounterTypeId: String?,
        @Query("patientId") patientId: String?
    ): Call<PreDiagnosisResponseModel?>?

    @GET(GetRepeatedResult)
    fun getRepeatedResult(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<RepeatedResultResponseModel?>?

    @GET(GetIntervals)
    fun getIntervals(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<RepeatedIntervalReponseModel?>?

    @GET(GetDetailsbyTablename)
    fun getspecimenDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("tableName") tableName: String?,
        @Query("patientId") patientId: String?
    ): Call<specimenResponseModel?>?

    @GET(GetDetailsbyTablename)
    fun getsymptomsDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("tableName") tableName: String?,
        @Query("patientId") patientId: String?
    ): Call<symptomResponseModel?>?

    @GET(GetDetailsbyTablename)
    fun getpatientDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("tableName") tableName: String?,
        @Query("patientId") patientId: String?
    ): Call<CovidRegisterPatientResponseModel?>?

    @GET(GetDetailsbyTablename)
    fun getconditionDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("tableName") tableName: String?,
        @Query("patientId") patientId: String?
    ): Call<ConditionDetailsResponseModel?>?

    @POST(ApiUrl.CovidQuickRegistrationSave)
    fun quickRegistrationSave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: QuickRegistrationRequestModel?
    ): Call<QuickRegistrationSaveResponseModel?>?

    @POST(ApiUrl.GetRoleBased)
    fun getPrivilliageModule(
        @Header("Authorization") authorization: String?,
        @Header("Accept-Language") language: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponsePrivillageModule?>?

    @POST(ApiUrl.GetRefrenceTestMethod)
    fun getTestMethod(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseTestMethod?>?

    @POST(ApiUrl.GetRefrenceTestMethod)
    fun getSearchOrderStatus(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OrderStatusSpinnerResponseModel?>?

    @POST(ApiUrl.GetLabSearchResult)
    fun getOrderStatusTestName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<TestNameResponseModel?>?

    @Streaming
    @POST(ApiUrl.getSavedPDF)
    fun getPDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: PDFRequestModel?
    ): Call<ResponseBody?>?

    @Streaming
    @POST(ApiUrl.dispatchPDF)
    fun getDispatchPDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @Streaming
    @POST(ApiUrl.labPDF)
    fun getLabPDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: LabPrintReq?
    ): Call<ResponseBody?>?

    @Streaming
    @POST(ApiUrl.rmisPDF)
    fun getRmisPDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: LabPrintReq?
    ): Call<ResponseBody?>?

    @Streaming
    @POST(ApiUrl.getSavedPDF)
    fun getPDFQuick(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: PrintQuickRequest?
    ): Call<ResponseBody?>?

    @Streaming
    @GET(certificateDownload)
    fun getCertificatePDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("certificate_uuid") certificate_uuid: Int
    ): Call<ResponseBody?>?

    @POST(ApiUrl.facilityLocation)
    fun getFacilityLocation(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FacilityLocationResponseModel?>?

    @POST(ApiUrl.getLabName)
    fun getLabname(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LabNameSearchResponseModel?>?

    @POST(ApiUrl.getLocationMaster)
    fun getLocationMaster(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModel?>?

    @POST(ApiUrl.getRmisLocationMaster)
    fun getRmisLocationMaster(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModel?>?

    @POST(ApiUrl.getLocationMaster)
    fun getLocationMasterLogin(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetLocationRequest?
    ): Call<LocationMasterResponseModel?>?

    @POST(ApiUrl.getBlockZone)
    fun getBlockZone(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<BlockZoneResponseModel?>?

    //Search
    @POST(ApiUrl.Register)
    fun searchOutPatientcovid(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body searchPatientRequestModel: SearchPatientRequestModelCovid?
    ): Call<QuickSearchResponseModel?>?

    //Search
    @POST(ApiUrl.SerachOldPin)
    fun searchOldPincovid(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body searchPatientRequestModel: SearchPatientRequestModelCovid?
    ): Call<QuickSearchResponseModel?>?

    @POST(ApiUrl.orderProcessApprovel)
    fun orderApproved(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body approvalRequestModel: ApprovalRequestModel?
    ): Call<OrderApprovedResponseModel?>?

    @POST(ApiUrl.GetToLocation)
    fun getLocation(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModelX?>?

    @POST(ApiUrl.GetRmisToLocation)
    fun getRmisLocation(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModelX?>?


    @POST(ApiUrl.getTestMaster)
    fun getTest(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GettestResponseModel?>?

    @POST(ApiUrl.GetLabType)
    fun getReference(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body labtype: RequestBody?
    ): Call<GetReferenceResponseModel?>?


    @POST(ApiUrl.GetRmisLabType)
    fun getRmisReference(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body labtype: RequestBody?
    ): Call<GetReferenceResponseModel?>?

    @POST(ApiUrl.saveOrder)
    fun saveOrder(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body saveOrderRequestModel: SaveOrderRequestModel?
    ): Call<SaveOrderResponseModel?>?

    @PUT(updateQuickRegistration)
    fun updateQuickRegistration(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: QuickRegistrationUpdateRequestModel?
    ): Call<QuickRegistrationUpdateResponseModel?>?

    @PUT(updateQuickRegistration)
    fun updateQuickRegistrationFromQuick(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: QuickRegistrationUpdateRequest?
    ): Call<QuickRegistrationUpdateResponseModel?>?

    @PUT(updateOPCorrecttion)
    fun updateQuickRegistrationFromOpCorrection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: OpCorrectionRequest?
    ): Call<QuickRegistrationUpdateResponseModel?>?

    @PUT(updateQuickRegistration)
    fun updateQuickRegistrationFromQuick(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: QuickRegistrationUpdateRequestWitholdPin?
    ): Call<QuickRegistrationUpdateResponseModel?>?

    @POST(ApiUrl.getLAbNameinList)
    fun getLabNameinList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetLabNameListResponseModel?>?

    @GET(getApplicationRules)
    fun getApplicationRules(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetApplicationRulesResponseModel?>?

    @POST(ApiUrl.getLabTestList)
    fun getLabTestList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: LabTestRequestModel?
    ): Call<LabTestResponseModel?>?

    @POST(ApiUrl.getRmisTestList)
    fun geRmisTestList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: LabTestRequestModel?
    ): Call<LabTestResponseModel?>?


    @POST(ApiUrl.rapidSave)
    fun rapidSave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: LabrapidSaveRequestModel?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.sampleRecived)
    fun sampleRecived(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: sampleTransportRequestModel?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.RmissampleRecived)
    fun RmissampleRecived(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: sampleTransportRequestModel?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.orderSendtonext)
    fun orderSendtonext(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AssigntootherRequest?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.RmisorderSendtonext)
    fun RmisorderSendtonext(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RmisAssigntoOtherReq?
    ): Call<SimpleResponseModel?>?


    @POST(ApiUrl.getLabTestApproval)
    fun getLabTestApproval(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: LabTestApprovalRequestModel?
    ): Call<LabTestApprovalResponseModel?>?

    @POST(ApiUrl.getRmisTestList)
    fun getRmisTestApproval(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: LabTestApprovalRequestModel?
    ): Call<LabTestApprovalResponseModel?>?

    @POST(ApiUrl.getLabTestApproval)
    fun getSampleDispatchList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: SampleDispatchRequest?
    ): Call<LabTestApprovalResponseModel?>?


    @POST(ApiUrl.getNewTickets)
    fun getTicketList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<TicketListResponseModel?>?

    @POST(ApiUrl.getAdmissionList)
    fun getIPAdmissionList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: AdmissionListRequestModel?
    ): Call<AdmissionListResponse?>?

    @POST(ApiUrl.deleteAdmissionList)
    fun deleteAdmissionList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<AdmissionListResponseModel?>?

    @POST(ApiUrl.getAllActiveCountry)
    fun getAllActiveCountry(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetAllCountryReq?
    ): Call<GetAllCountryResp>?

    @POST(ApiUrl.getByCountryId)
    fun getByCountryId(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetByCountryIdReq?
    ): Call<GetByCountryIdResp>?

    @Streaming
    @POST(ApiUrl.printadmission)
    fun getAdmissionPDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @POST(ApiUrl.getTicketById)
    fun getTicketById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<TicketIdResponseModel?>?

    @POST(ApiUrl.getTicketCount)
    fun getTicketCount(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<TicketCountResponseModel?>?

    @Multipart
    @POST(ApiUrl.addNewTicket)
    fun addNewTicket(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Part file: MultipartBody.Part?,
        @Part("mobile") mobile: RequestBody?,
        @Part("subject") subject: RequestBody?,
        @Part("created_by") created_b: RequestBody?,
        @Part("application_user_uuid") application_user_uuid: RequestBody?,
        @Part("asset_code") asset_code: RequestBody?,
        @Part("assignto") assignto: RequestBody?,
        @Part("make") make: RequestBody?,
        @Part("model") model: RequestBody?,
        @Part("problem_description") problem_description: RequestBody?,
        @Part("serial") serial: RequestBody?,
        @Part("vendormail") vendormail: RequestBody?,
        @Part("facility_uuid") facility_id: RequestBody?,
        @Part("department_uuid") department_uuid: RequestBody?,
        @Part("user_type_uuid") user_type_uuid: RequestBody?,
        @Part("ticketstatus_uuid") ticketstatus_uuid: RequestBody?,
        @Part("assest_uuid") assest_uuid: RequestBody?,
        @Part("category_uuid") category_uuid: RequestBody?,
        @Part("priority_uuid") priority_uuid: RequestBody?,
        @Part("priority") priority: RequestBody?
    ): Call<TicketInstitutionResponseModel?>?

    @Multipart
    @POST(ApiUrl.saveSpeciality)
    fun saveSpecialtySketch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Part file: MultipartBody.Part?,
        @Part("speciality_sketch_uuid") speciality_sketch_uuid: RequestBody?,
        @Part("doctor_uuid") doctor_uuid: RequestBody?,
        @Part("encounter_uuid") encounter_uuid: RequestBody?,
        @Part("encounter_type_uuid") encounter_type_uuid: RequestBody?,
        @Part("patient_uuid") patient_uuid: RequestBody?,
        @Part("department_uuid") department_uuid: RequestBody?
    ): Call<SimpleResponseModel?>?

    @Multipart
    @POST(ApiUrl.updateTicket)
    fun updateTicket(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Part("mobile") mobile: RequestBody?,
        @Part("subject") subject: RequestBody?,
        @Part("created_by") created_b: RequestBody?,
        @Part("application_user_uuid") application_user_uuid: RequestBody?,
        @Part("asset_code") asset_code: RequestBody?,
        @Part("assignto") assignto: RequestBody?,
        @Part("make") make: RequestBody?,
        @Part("model") model: RequestBody?,
        @Part("problem_description") problem_description: RequestBody?,
        @Part("serial") serial: RequestBody?,
        @Part("vendormail") vendormail: RequestBody?,
        @Part("facility_uuid") facility_id: RequestBody?,
        @Part("department_uuid") department_uuid: RequestBody?,
        @Part("user_type_uuid") user_type_uuid: RequestBody?,
        @Part("ticketstatus_uuid") ticketstatus_uuid: RequestBody?,
        @Part("assest_uuid") assest_uuid: RequestBody?,
        @Part("category_uuid") category_uuid: RequestBody?,
        @Part("subcategory_uuid") subcategory_uuid: RequestBody?,
        @Part("priority_uuid") priority_uuid: RequestBody?,
        @Part("ticketmanagment_uuid") ticketmanagment_uuid: RequestBody?
    ): Call<TicketInstitutionResponseModel?>?

    @POST(ApiUrl.getFacilityByUUID)
    fun getFacilityByUUID(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<TicketInstitutionResponseModel?>?

    @POST(ApiUrl.getAsset)
    fun getAssetCode(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<AssetResponseModel?>?

    @POST(ApiUrl.getDepartment)
    fun getDepartment(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<com.oasys.digihealth.doctor.ui.helpdesk.model.DepartmentResponseModel?>?

    @POST(ApiUrl.getCategory)
    fun getCategory(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<CategoryListResponseModel?>?


    @POST(ApiUrl.getAdmissionDepartment)
    fun getAdmissionDepartment(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?
    ): Call<DropDownResponseModel?>?

    @GET(getAdmissionWardById)
    fun getAdmissionWardById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Query("ward_uuid") ward_uuid: Int,
        @Query("room_uuid") room_uuid: Int,
        @Query("admission_status") admission_status: Int
    ): Call<DropDownResponseModel?>?

    /*
      wardroom id
     */
    @GET(getAdmissionWardById)
    fun getRoomByWardIDfetch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int,
        @Query("room_uuid") room_uuid: Int

    ): Call<ResponseRoomSetupFetchData?>?

    @POST(ApiUrl.getAdmissionGender)
    fun getAdmissionGender(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?
    ): Call<DropDownResponseModel?>?


    @POST(ApiUrl.GetSalutationTitiles)
    fun getAdmissionTitle(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?
    ): Call<DropDownResponseModel?>?

    @POST(ApiUrl.getAdmissionDistrict)
    fun getAdmissionDistrict(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?
    ): Call<DropDownResponseModel?>?

    @POST(ApiUrl.getAdmissionReference)
    fun getReference(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DropDownResponseModel?>?

    @POST(ApiUrl.getAdmissionWard)
    fun getAdmissionWard(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DropDownResponseModel?>?

    @POST(ApiUrl.getAdmissionDoctor)
    fun getAdmissionDoctor(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DoctorListResponseModel?>?

    @POST(ApiUrl.searchPinOrMobile)
    fun searchPinOrMobile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<SearchPinResponseModel?>?

    @POST(ApiUrl.addAdmission)
    fun addAdmission(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: AddAdmissionRequestModel?
    ): Call<AddAdmissionResponse?>?

    @POST(ApiUrl.updateAdmission)
    fun updateAdmission(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: UpdateAdmissionRequestModel?
    ): Call<AddAdmissionResponse?>?

    @POST(ApiUrl.searchPatient)
    fun searchIpPatient(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<SearchPatientResponse?>?

    @POST(ApiUrl.IpCorrection)
    fun IpCorrectionUpdate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: IpCorrectionUpdateRequest?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.getAdmissionByid)
    fun getAdmissionById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<AdmissionByIdResponseModel?>?

    @POST(ApiUrl.getAttenderPass)
    fun getAttenderPass(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @POST(ApiUrl.getFacilityLocationByFacilityId)
    fun getFacilityLocationByFacilityId(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body facilityLocationReq: FacilityLocationReq
    ): Call<FacilityLocationResp>

    @POST(ApiUrl.getAdmissionDistrictById)
    fun getAdmissionTaluk(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DropDownResponseModel?>?

    @POST(ApiUrl.getAdmissionTalukById)
    fun getAdmissionVillage(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DropDownResponseModel?>?


    @POST(ApiUrl.getAdmissionCommonReference)
    fun getCommonReference(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DropDownResponseModel?>?

    @POST(ApiUrl.getAdmissionDiagnosis)
    fun getAdmissionDiagnosis(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DiagnosisListResponseModel?>?

    @POST(ApiUrl.getVendor)
    fun getVendor(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<VendorListResponseModel?>?


    @POST(ApiUrl.getVendorByMobile)
    fun getVendorByMobile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<VendorByMobileResponseModel?>?

    @POST(ApiUrl.getTicketUserProfile)
    fun getTicketUserProfile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<VendorListResponseModel?>?

    @POST(ApiUrl.getPatientVisitInfo)
    fun getPatientVisit(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<VendorListResponseModel?>?

    @POST(ApiUrl.getTutorial)
    fun getTutorialList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<UserManualResponseModel?>?

    @POST(ApiUrl.orderDetailsGet)
    fun orderDetailsGet(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: Req?
    ): Call<OrderProcessDetailsResponseModel?>?

    @POST(ApiUrl.getNoteTemplate)
    fun getNoteTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getNoteTemplateReq: GetNoteTemplateReq
    ): Call<GetNoteTemplateResp?>?

    @POST(ApiUrl.RmisorderDetailsGet)
    fun RmisorderDetailsGet(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: Req?
    ): Call<OrderProcessDetailsResponseModel?>?

    @POST(ApiUrl.orderDetailsGetLabApproval)
    fun orderDetailsGetLabApproval(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult.Req?
    ): Call<LabApprovalResultResponse?>?

    @POST(ApiUrl.RmisorderDetailsGet)
    fun RmisorderDetailsGetLabApproval(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult.Req?
    ): Call<LabApprovalResultResponse?>?

    @POST(ApiUrl.rejectData)
    fun rejectTestLab(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RejectRequestModel?
    ): Call<SimpleResponseModel?>?


    @POST(ApiUrl.RmisrejectData)
    fun RmisrejectTestLab(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RejectRequestModel?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.sendApprovel)
    fun sendApprovel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SendApprovalRequestModel?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.treatmentKitAutoSearch)
    fun treatmentKitAutoSearch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body autoSearchReq: AutoSearchReq
    ): Call<AutoSearchResp>

    @GET(treatmentKitFavouriteById)
    fun treatmentKitFavouriteById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("treatmentId") treatmentId: Int,
        @Query("favouriteId") favouriteId: String
    ): Call<TreatmentKitFavouriteResp>

    @POST(ApiUrl.RmissendApprovel)
    fun RmissendApprovel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SendApprovalRequestModel?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.orderProcess)
    fun orderProcess(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: OrderToProcessReqestModel?
    ): Call<OrderProcessResponseModel?>?

    @POST(ApiUrl.DirectApprovel)
    fun directApprovel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body directApprovelReq: DirectApprovelReq?
    ): Call<SimpleResponseModel?>?

    //Bed Management Report Dashboard
    @POST(ApiUrl.getBedStatusWiseLable)
    fun getBedManagementReportLable(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: BedStatusHospitalWiseLabelRequest?
    ): Call<BedStatusHospitalWiseLabelModel?>?

    @POST(ApiUrl.getBedStatusOccupiedDetails)
    fun getBedStatusOccupiedReport(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: bedStatusOccupiedDetailRequest?
    ): Call<BedStatusOccupiedDetailResponse?>?

    @POST(ApiUrl.getBedStatusWiseTable)
    fun getBedManagementReportTable(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: BedStatusHospitalWiseLabelRequest?
    ): Call<BedStatusHospitalWiseTabelResponse?>?

    @POST(ApiUrl.getBedStatusHospitalDistrict)
    fun getBedStatusHospitalDistrict(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DistrictResponse?>?

    @POST(ApiUrl.getBedStatusHospitalInstitution)
    fun getBedStatusHospitalInstitution(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<InstitutionResponse?>?


    //RMIS Dashboard
    @POST(ApiUrl.getRMISDashboard)
    fun getRMISDashboard(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<RMISDashboardResponse?>?

    @POST(ApiUrl.getRMISDashboardChart)
    fun getRMISDashboardChart(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<RMISDashboardChartResponse?>?


    @GET(getRMISPatients)
    fun getRMISPatients(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?
    ): Call<DropDownResponseModel?>?


    @POST(ApiUrl.getRMISGender)
    fun getRMISGender(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DropDownResponseModel?>?

    @POST(ApiUrl.getRMISDashboardList)
    fun getRMISDashboardList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DashboardListResponseModel?>?


    @POST(ApiUrl.getRMISDepartment)
    fun getRMISDepartment(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<DropDownResponseModel?>?


    //Pharmacy Dashboard
    @POST(ApiUrl.getPharmacyDashboard)
    fun getPharmacyDashboard(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyDashboardResponse?>?

    @POST(ApiUrl.getPharmacyChartValues)
    fun getPharmacyChartValues(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyChartResponse?>?

    @POST(ApiUrl.getTopMovedDrugs)
    fun getTopMovedDrugs(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyDrugListResponse?>?

    @POST(ApiUrl.getZeroStockDrugs)
    fun getZeroStockDrug(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyDrugListResponse?>?

    @POST(ApiUrl.getNonMovedDrugs)
    fun getNonMovedDrug(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyDrugListResponse?>?

    @POST(ApiUrl.getLowStockDrugs)
    fun getLowStockDrug(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyDrugListResponse?>?

    //TestProcessResponseModel
    @POST(ApiUrl.getLabTestApproval)
    fun getLabTestProcess(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: TestProcessRequestModel?
    ): Call<TestProcessResponseModel?>?

    @POST(ApiUrl.getRmisTestList)
    fun getRmisTestProcess(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: TestProcessRequestModel?
    ): Call<TestProcessResponseModel?>?

    @POST(ApiUrl.getRejectReference)
    fun getRejectReference(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<RejectReferenceResponseModel?>?

    @POST(ApiUrl.getRmisRejectReference)
    fun getRmisRejectReference(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<RejectReferenceResponseModel?>?

    @POST(ApiUrl.getUserProfile)
    fun getUserProfile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<UserProfileResponseModel?>?


    @POST(ApiUrl.getTicketUserProfile)
    fun getUserProfile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<VendorListResponseModel?>?

    @POST(ApiUrl.getSaveVendor)
    fun getSaveVendor(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: AddTicketRequestModel?
    ): Call<VendorByMobileResponseModel?>?

    @POST(ApiUrl.getSampleAcceptance)
    fun getSampleAccept(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body sampleAcceptedRequest: SampleAcceptedRequest?
    ): Call<SampleAcceptanceResponseModel?>?


    @POST(ApiUrl.getRmisSampleAcceptance)
    fun getRmisSampleAccept(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body sampleAcceptedRequest: SampleAcceptedRequest?
    ): Call<SampleAcceptanceResponseModel?>?


    @POST(ApiUrl.GetLabSearchResult)
    fun getLabTestSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: RequestBody?
    ): Call<LabTestSpinnerResponseModel?>?

    @POST(ApiUrl.GetLabSearchResult)
    fun getLabTestSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body labTechSearch: LabTechSearch?
    ): Call<LabTestSpinnerResponseModel?>?

    @POST(ApiUrl.getAssignedSpinnerList)
    fun getLabAssignedToSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: RequestBody?
    ): Call<LabAssignedToResponseModel?>?

    //OP Consolidated Report
    @POST(ApiUrl.getLabConsolidatedOPReportTable)
    fun getLabConsolidatedOPReportTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabConsolidatedReportRequestModel?
    ): Call<LabConsolidatedReportResponseModel?>?

    @POST(ApiUrl.getLabConsolidatedOPReportLabel)
    fun getLabConsolidatedOPReportLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabConsolidatedReportRequestModel?
    ): Call<LabConsolidatedReportLabelResponseModel?>?

    @POST(ApiUrl.getDistrictOPSpinnerList)
    fun getLabDistrictOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getHUDOPSpinnerList)
    fun getLabHUDOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getBlockOPSpinnerList)
    fun getLabBlockOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getOfficeOPSpinnerList)
    fun getLabOfficeOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDepartmentOPSpinnerList)
    fun getLabDepartmentOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getInstitutionTypeOPSpinnerList)
    fun getLabInstitutionTypeOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getInstitutionOPSpinnerList)
    fun getLabInstitutionOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getTestNameOPSpinnerList)
    fun getLabTestNameOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabNameOPSpinnerList)
    fun getLabNameOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getGenderOPSpinnerList)
    fun getLabGenderOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getStatusOPSpinnerList)
    fun getLabStatusOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    //IP Consolidated Report
    @POST(ApiUrl.getLabConsolidatedIPReportTable)
    fun getLabConsolidatedIPReportTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabConsolidatedReportRequestModel?
    ): Call<LabConsolidatedReportResponseModel?>?

    @POST(ApiUrl.getLabConsolidatedIPReportLabel)
    fun getLabConsolidatedIPReportLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabConsolidatedReportRequestModel?
    ): Call<LabConsolidatedReportLabelResponseModel?>?

    @POST(ApiUrl.getDistrictIPSpinnerList)
    fun getLabDistrictIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getHUDIPSpinnerList)
    fun getLabHUDIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getBlockIPSpinnerList)
    fun getLabBlockIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getOfficeIPSpinnerList)
    fun getLabOfficeIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getInstitutionIPSpinnerList)
    fun getLabInstitutionIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDepartmentIPSpinnerList)
    fun getLabDepartmentIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getInstitutionTypeIPSpinnerList)
    fun getLabInstitutionTypeIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getTestNameIPSpinnerList)
    fun getLabTestNameIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabNameIPSpinnerList)
    fun getLabNameIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getGenderIPSpinnerList)
    fun getLabGenderIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getStatusIPSpinnerList)
    fun getLabStatusIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    //Session Wise Report
    @POST(ApiUrl.getSessionDistrictSpinnerList)
    fun getSessionDistrictSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionHUDSpinnerList)
    fun getSessionHUDSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionBlockSpinnerList)
    fun getSessionBlockSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionOfficeSpinnerList)
    fun getSessionOfficeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionInsitutionTypeSpinnerList)
    fun getSessionInstitutionTypeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionInsitutionSpinnerList)
    fun getSessionInstitutionSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionGenderSpinnerList)
    fun getSessionGenderSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionSpinnerList)
    fun getSessionSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getSessionReportLable)
    fun getSessionReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportLableResponse?>?

    @POST(ApiUrl.getSessionReportChart)
    fun getSessionReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportChartResponse?>?

    @POST(ApiUrl.getSessionReportChartWithTime)
    fun getSessionReportChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportChartResponse?>?

    //DayWisePatientsList
    @POST(ApiUrl.getDayWisePatientsListReportLable)
    fun getDayWisePatientsListReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportLableResponse?>?

    @POST(ApiUrl.getDayWisePatientsListReportChart)
    fun getDayWisePatientsListReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportChartResponse?>?


    @POST(ApiUrl.getDayWisePatientsListReportChartWithTime)
    fun getDayWisePatientsListReportChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportChartResponse?>?


    @POST(ApiUrl.getDayWisePatientsListReportSummary)
    fun getDayWisePatientsListReportSummary(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DayWisePatientListResponseModel?>?


    @POST(ApiUrl.getDayWisePatientsListReportDetail)
    fun getDayWisePatientsListReportDetail(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DayWisePatientListResponseModel?>?

    //DateWiseSessionReport
    @POST(ApiUrl.getDateWiseSessionLabel)
    fun getDateWiseSessionReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DateWiseSessionReportLableResponse?>?

    @POST(ApiUrl.getCensusDateWiseSessionChart)
    fun getDateWiseSessionReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DateWiseSessionReportChartResponse?>?

    @POST(ApiUrl.getCensusDateWiseSessionChartWithTime)
    fun getDateWiseSessionReportChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DateWiseSessionReportChartResponse?>?

    @POST(ApiUrl.getCensusDateWiseSessionList)
    fun getDateWiseSessionReportList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DateWiseSessionListResponseModel?>?

    @POST(ApiUrl.getSessionReportSummary)
    fun getSessionReportSummaryList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionSummaryListResponse?>?

    @POST(ApiUrl.getSessionReportDetail)
    fun getSessionReportDetailList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionSummaryListResponse?>?

    //Admission Wise Report
    @POST(ApiUrl.getAdmissionWardLabel)
    fun getAdmissionWardReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<WardWiseLabelResponseModel?>?


    @POST(ApiUrl.getSessionReportLable)
    fun getDischargeReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportLableResponse?>?

    @POST(ApiUrl.getAdmissionWardChart)
    fun getAdmissionWardReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<WardWiseChartResponseModel?>?

    @POST(ApiUrl.getAdmissionWardChartWithTime)
    fun getAdmissionWardReportChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<WardWiseChartResponseModel?>?


    @POST(ApiUrl.getAdmissionWardList)
    fun getAdmissionWardList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<WardListReportResponse?>?

    @POST(ApiUrl.getAdmissionWardListDetail)
    fun getAdmissionWardListDetail(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<WardListReportResponse?>?


    //Admission DoctorWise
    @POST(ApiUrl.getAdmissionDoctorLabel)
    fun getAdmissionDoctorReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DoctorWiseLabelResponse?>?

    @POST(ApiUrl.getAdmissionDoctorChart)
    fun getAdmissionDoctorReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DoctorWiseChartResponse?>?

    @POST(ApiUrl.getAdmissionDoctorChartWithTime)
    fun getAdmissionDoctorReportChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DoctorWiseChartResponse?>?

    @POST(ApiUrl.getAdmissionDoctorList)
    fun getAdmissionDoctorReportList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DoctorWiseListResponse?>?

    @POST(ApiUrl.getAdmissionDoctorListDetail)
    fun getAdmissionDoctorReportListDetail(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DoctorWiseListResponse?>?

    //Admission State Level
    @POST(ApiUrl.getAdmissionStateLabel)
    fun getAdmissionStateReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<StateLevelLabelResponse?>?

    @POST(ApiUrl.getAdmissionStateChart)
    fun getAdmissionStateReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<StateLevelChartResponse?>?

    @POST(ApiUrl.getAdmissionStateChartWithTime)
    fun getAdmissionStateReportChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<StateLevelChartResponse?>?

    @POST(ApiUrl.getAdmissionStateList)
    fun getAdmissionStateReportList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<StateLevelListResponse?>?

    @POST(ApiUrl.getAdmissionStateListDetail)
    fun getAdmissionStateReportListDetail(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<StateLevelListResponse?>?

    //Admission District Label
    @POST(ApiUrl.getAdmissionDistrictLabel)
    fun getAdmissionDistrictLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DistrictLevelLabelResponse?>?

    @POST(ApiUrl.getAdmissionDistrictChart)
    fun getAdmissionDistrictChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DoctorWiseChartResponse?>?

    @POST(ApiUrl.getAdmissionDistrictChartWithTime)
    fun getAdmissionDistrictChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DoctorWiseChartResponse?>?

    @POST(ApiUrl.getAdmissionDistrictList)
    fun getAdmissionDistrictList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DistrictLevelListResponse?>?

    @POST(ApiUrl.getAdmissionDistrictListDetail)
    fun getAdmissionDistrictListDetail(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DistrictLevelListResponse?>?


    //Date Wise Report
    @POST(ApiUrl.getDateDistrictSpinnerList)
    fun getDateDistrictSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDateHUDSpinnerList)
    fun getDateHUDSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDateBlockSpinnerList)
    fun getDateBlockSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDateOfficeSpinnerList)
    fun getDateOfficeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDateInsitutionTypeSpinnerList)
    fun getDateInstitutionTypeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDateInsitutionSpinnerList)
    fun getDateInstitutionSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDateGenderSpinnerList)
    fun getDateGenderSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDateSpinnerList)
    fun getDateSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDatenReportLable)
    fun getDateReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportLableResponse?>?

    @POST(ApiUrl.getDateReportChart)
    fun getDateReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportChartResponse?>?

    @POST(ApiUrl.getDateReportChartWithTime)
    fun getDateReportChartWithTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportChartResponse?>?


    @POST(ApiUrl.getOpCensusDate)
    fun getOpCensusDate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DateWiseDetailResponseModel?>?

    @POST(ApiUrl.getOpCensusDateWiseDetails)
    fun getOpCensusDateWiseDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DateWiseDetailResponseModel?>?


    //Discharge Summary Count Wise Report
    @POST(ApiUrl.getDischargeSummaryDistrictSpinnerList)
    fun getDischargeSummaryDistrictSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDischargeSummaryHUDSpinnerList)
    fun getDischargeSummaryHUDSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDischargeSummaryBlockSpinnerList)
    fun getDischargeSummaryBlockSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDischargeSummaryOfficeSpinnerList)
    fun getDischargeSummaryOfficeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDischargeSummaryInsitutionTypeSpinnerList)
    fun getDischargeSummaryInstitutionTypeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDischargeSummaryInsitutionSpinnerList)
    fun getDischargeSummaryInstitutionSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDischargeSummaryGenderSpinnerList)
    fun getDischargeSummaryGenderSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDischargeSummarySpinnerList)
    fun getDischargeSummarySpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getDischargeSummaryTable)
    fun getDischargeSummaryTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DischargeSummaryReportCountWiseResponse?>?


    //DayWisePatientList
    @POST(ApiUrl.getDayWisePatientList)
    fun getDayWisePatientList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<DayWisePatientListResponseModel?>?

    @POST(ApiUrl.getDayDistrictSpinner)
    fun getDayDistrictSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDayInstitutionSpinner)
    fun getDayInstitutionSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody
    ): Call<LabFilterResponseModel?>?


    //AdmissionDayWisePatient


    //Date Wise Report
    @POST(ApiUrl.getAdmissionDayDistrictSpinnerList)
    fun getAdmissionDayDistrictSpinnerList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getAdmissionDayHUDSpinnerList)
    fun getAdmissionDayHUDSpinnerList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getAdmissionDayBlockSpinnerList)
    fun getAdmissionDayBlockSpinnerList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getAdmissionDayOfficeSpinnerList)
    fun getAdmissionDayOfficeSpinnerList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getAdmissionDayInsitutionTypeSpinnerList)
    fun getAdmissionDayInsitutionTypeSpinnerList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getAdmissionDayInsitutionSpinnerList)
    fun getAdmissionDayInsitutionSpinnerList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getAdmissionDayGenderSpinnerList)
    fun getAdmissionDayGenderSpinnerList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getAdmissionDaynReportLable)
    fun getAdmissionDaynReportLable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<AdmissionDayPatientLabelRespone?>?

    @POST(ApiUrl.getAdmissionDayReportChart)
    fun getAdmissionDayReportChart(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<AdmissionDayPatientChartResponse?>?

    @POST(ApiUrl.getAdmissionDayReportChartWithTime)
    fun getAdmissionDayReportChartTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<AdmissionDayPatientChartResponse?>?


    @POST(ApiUrl.getIPDayWisePatients)
    fun getIPDayWisePatients(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<AdmissionDayPatientResponse?>?

    @POST(ApiUrl.getIPDayWisePatientsDetail)
    fun getIPDayWisePatientsDetail(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<AdmissionDayPatientResponse?>?


    //DistrictWisePatient OP


    @POST(ApiUrl.getDistPatientCountReportLabel)
    fun getDistPatientCountLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?

    @POST(ApiUrl.getDistPatientCountReportTabel)
    fun getDistPatientCountTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportResponseModel?>?

    @POST(ApiUrl.getDistrictHudSpinner)
    fun getDistHUDSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictDropDownFilterSpinner)
    fun getDistDropDownSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictBlockSpinner)
    fun getDistBlockSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictOfficeSpinner)
    fun getDistOfficeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictInstituteTypeSpinner)
    fun getDistInstitutionTypeSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictInstituteSpinner)
    fun getDistInstitutionSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictLTestNameSpinner)
    fun getDistTestNameSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictLabNameSpinner)
    fun getDistLabNameSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictGenderSpinner)
    fun getDistGenderSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictStatusSpinner)
    fun getDistStatusSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    //DistrictWisePatient Ip


    @POST(ApiUrl.getDistPatientCountReportLabelIp)
    fun getDistPatientCountLabelIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?

    @POST(ApiUrl.getDistPatientCountReportTabelIP)
    fun getDistPatientCountTableIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportResponseModel?>?

    @POST(ApiUrl.getDistrictHudSpinnerIP)
    fun getDistHUDSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictDropDownFilterSpinnerIp)
    fun getDistDropDownSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictBlockSpinnerIp)
    fun getDistBlockSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictOfficeSpinnerIp)
    fun getDistOfficeSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictInstituteTypeSpinnerIp)
    fun getDistInstitutionTypeSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictInstituteSpinnerIp)
    fun getDistInstitutionSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictLTestNameSpinnerIp)
    fun getDistTestNameSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictLabNameSpinnerIp)
    fun getDistLabNameSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictGenderSpinnerIp)
    fun getDistGenderSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictStatusSpinnerIp)
    fun getDistStatusSpinnerIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    //DistrictwiseTestOP

    @POST(ApiUrl.getDistTestHudSpinner)
    fun getDistTestHUDList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestBlockSpinner)
    fun getDistTestBlockList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestOfficeSpinner)
    fun getDistTestOfficeList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestInstituteTypeSpinner)
    fun getDistTestInstituteTypeList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestInstituteSpinner)
    fun getDistTestInstituteList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestGenderSpinner)
    fun getDistTestGenderList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getDistTestDropdownSpinner)
    fun getDistTestDropDownList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestLabNameSpinner)
    fun getDistTestLabNameList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictTestNameSpinner)
    fun getDistrictTestNameList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictTestOrderStatusSpinner)
    fun getDistTestOrderStatusList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictTestCountLabel)
    fun getDistrictTestCountLabelList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?


    @POST(ApiUrl.getDistrictTestCountTable)
    fun getDistrictTestCountTableList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabTestWiseReportResponseModel?>?


    //DistrictwiseTestIp

    @POST(ApiUrl.getDistTestHudSpinnerIp)
    fun getDistTestHUDListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestBlockSpinnerIp)
    fun getDistTestBlockListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestOfficeSpinnerIp)
    fun getDistTestOfficeListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestInstituteTypeSpinnerIp)
    fun getDistTestInstituteTypeListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestInstituteSpinnerIp)
    fun getDistTestInstituteListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestGenderSpinnerIp)
    fun getDistTestGenderListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getDistTestDropdownSpinnerIp)
    fun getDistTestDropDownListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistTestLabNameSpinnerIp)
    fun getDistTestLabNameListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictTestNameSpinnerIp)
    fun getDistrictTestNameListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictTestOrderStatusSpinnerIp)
    fun getDistTestOrderStatusListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getDistrictTestCountLabelIp)
    fun getDistrictTestCountLabelListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?


    @POST(ApiUrl.getDistrictTestCountTableIp)
    fun getDistrictTestCountTableListIp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabTestWiseReportResponseModel?>?


    // OP Lab Wise Report
    @POST(ApiUrl.getLabWiseReportOPTable)
    fun getLabWiseReportOPTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportResponseModel?>?

    @POST(ApiUrl.getLabWiseReportOPLabel)
    fun getLabWiseReportOPLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?

    @POST(ApiUrl.getLabWiseDistrictOPSpinnerList)
    fun getLabWiseDistrictOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseHUDOPSpinnerList)
    fun getLabWiseHUDOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseBlockOPSpinnerList)
    fun getLabWiseBlockOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseOfficeOPSpinnerList)
    fun getLabWiseOfficeOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseInstitutionTypeOPSpinnerList)
    fun getLabWiseInstitutionTypeOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseInstitutionOPSpinnerList)
    fun getLabWiseInstitutionOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseTestNameOPSpinnerList)
    fun getLabWiseTestNameOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseLabNameOPSpinnerList)
    fun getLabWiseLabNameOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseGenderOPSpinnerList)
    fun getLabWiseGenderOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseStatusOPSpinnerList)
    fun getLabWiseStatusOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    // IP Lab Wise Report
    @POST(ApiUrl.getLabWiseReportIPTable)
    fun getLabWiseReportIPTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportResponseModel?>?

    @POST(ApiUrl.getLabWiseReportIPLabel)
    fun getLabWiseReportIPLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?

    @POST(ApiUrl.getLabWiseDistrictIPSpinnerList)
    fun getLabWiseDistrictIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseHUDIPSpinnerList)
    fun getLabWiseHUDIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseBlockIPSpinnerList)
    fun getLabWiseBlockIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseOfficeIPSpinnerList)
    fun getLabWiseOfficeIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseInstitutionTypeIPSpinnerList)
    fun getLabWiseInstitutionTypeIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseInstitutionIPSpinnerList)
    fun getLabWiseInstitutionIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseTestNamIPSpinnerList)
    fun getLabWiseTestNameIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseLabNameIPSpinnerList)
    fun getLabWiseLabNameIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseGenderIPSpinnerList)
    fun getLabWiseGenderIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabWiseStatusIPSpinnerList)
    fun getLabWiseStatusIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    // OP Lab Test Wise Report
    @POST(ApiUrl.getLabTestWiseOPReportTable)
    fun getLabTestWiseOPReportTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabTestWiseReportResponseModel?>?

    @POST(ApiUrl.getLabTestWiseOPReportLabel)
    fun getLabTestWiseOPReportLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?

    @POST(ApiUrl.getLabTestWiseDistrictOPSpinnerList)
    fun getLabTestWiseDistrictOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseHUDOPSpinnerList)
    fun getLabTestWiseHUDOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseBlockOPSpinnerList)
    fun getLabTestWiseBlockOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseOfficeOPSpinnerList)
    fun getLabTestWiseOfficeOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseInstitutionTypeOPSpinnerList)
    fun getLabTestWiseInstitutionTypeOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseInstitutionOPSpinnerList)
    fun getLabTestWiseInstitutionOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseTestNameOPSpinnerList)
    fun getLabTestWiseTestNameOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseLabNameOPSpinnerList)
    fun getLabTestWiseLabNameOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseGenderOPSpinnerList)
    fun getLabTestWiseGenderOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseStatusOPSpinnerList)
    fun getLabTestWiseStatusOPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    // IP Lab Test Wise Report
    @POST(ApiUrl.getLabTestWiseIPReportTable)
    fun getLabTestWiseIPReportTable(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabTestWiseReportResponseModel?>?

    @POST(ApiUrl.getLabTestWiseIPReportLabel)
    fun getLabTestWiseIPReportLabel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: LabWiseReportRequestModel?
    ): Call<LabWiseReportLabelResponseModel?>?

    @POST(ApiUrl.getLabTestWiseDistrictIPSpinnerList)
    fun getLabTestWiseDistrictIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseHUDIPSpinnerList)
    fun getLabTestWiseHUDIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseBlockIPSpinnerList)
    fun getLabTestWiseBlockIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseOfficeIPSpinnerList)
    fun getLabTestWiseOfficeIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseInstitutionTypeIPSpinnerList)
    fun getLabTestWiseInstitutionTypeIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseInstitutionIPSpinnerList)
    fun getLabTestWiseInstitutionIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseTestNameIPSpinnerList)
    fun getLabTestWiseTestNameIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseLabNameIPSpinnerList)
    fun getLabTestWiseLabNameIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseGenderIPSpinnerList)
    fun getLabTestWiseGenderIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getLabTestWiseStatusIPSpinnerList)
    fun getLabTestWiseStatusIPSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getApprovalResultSpinner)
    fun getApprovalResultSpinner(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body body: RequestBody?
    ): Call<LabApprovalSpinnerResponseModel?>?

    @GET(GetDocumentType)
    fun getDocumentType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<DocumentTypeResponseModel?>?

    @GET(GetAddDocumentType)
    fun getAddDocumentDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<AddDocumentDetailsResponseModel?>?

    @Multipart
    @POST(ApiUrl.GetUploadFile)
    fun getUploadFile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Part file: MultipartBody.Part?,
        @Part("folder_name") foldername: RequestBody?,
        @Part("attached_date") multipartattachedDate: RequestBody?,
        @Part("patient_uuid") patientuuid: RequestBody?,
        @Part("encounter_uuid") encounteruuid: RequestBody?,
        @Part("attachment_type_uuid") attachmenttypeuuid: RequestBody?,
        @Part("comments") comments: RequestBody?,
        @Part("attachment_name") attachmentname: RequestBody?
    ): Call<FileUploadResponseModel?>?

    fun getUploadFile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Part("name") filename: String?,
        @Part file: MultipartBody.Part?
    ): Call<ResponseBody?>?

    @PUT(DeleteAttachmentsRows)
    fun deleteAttachmentsRows(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("attachment_uuid") attachementid: Int
    ): Call<DeleteDocumentResponseModel?>?

    @PUT(updateHistoryDiagnosis)
    fun updateDiagnosis(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_diagnosis_id") patient_diagnosis_id: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("department_uuid") department_uuid: Int,
        @Body body: RequestBody?
    ): Call<HistoryDiagnosisUpdateResponseModel?>?

    @POST(ApiUrl.GetAdmissionWardList)
    fun getAdmissionWardList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<AdmissionWardResponseModel?>?

    @GET(GetReason)
    fun getReason(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<ReasonResponseModel?>?


    @GET(GetTransmissionReason)
    fun getTransmissionReason(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<TransmissionReasonResponseModel?>?

    @POST(ApiUrl.GetNextOrder)
    fun nextOrder(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body refferalNextRequestModel: RefferalNextRequestModel?
    ): Call<RefferaNextResponseModel?>?

    @POST(ApiUrl.SaveTransfferOrder)
    fun SaveTransfferOrder(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body refferalNextRequestModel: TrasfferedRequestModel?
    ): Call<TransferredResponseModel?>?

    @POST(ApiUrl.SaveDischargeOrder)
    fun SaveDischargeOrder(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body refferalNextRequestModel: DischargSaveeRequest?
    ): Call<DischargeSaveResponse?>?


    @POST(ApiUrl.CreateTreatmentKitOrder)
    fun orderSave(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: TKOrderRequestModel?
    ): Call<OrderSaveTreatmentKitResponseModel?>?

    @Streaming
    @POST(ApiUrl.GetDownload)
    fun getDownload(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @Streaming
    @POST(ApiUrl.GetDownload)
    fun getResultDownload(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @POST(ApiUrl.GetNoteTemplate)
    fun getNoteTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<TemplatesResponseModel?>?

    @POST(ApiUrl.GetTemplateItem)
    fun getItemTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<TemplateItemResponseModel?>?

    @POST(ApiUrl.GetSaveScertificate)
    fun saveCertificate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body certificateRequestModel: CertificateRequestModel?
    ): Call<CertificateResponseModel?>?

    @GET(GetCertificateAll)
    fun getCertificateAll(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patientId: Int
    ): Call<GetCertificateResponseModel?>?

    @POST(ApiUrl.EmrInvestigationpost)
    fun EmrInvestigationpost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body emrRequestModelr: InvestigationRequset?
    ): Call<InvestigationPostResponseModel?>?

    @GET(getTreatmentFavourites)
    fun getTKFavourite(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("departmentId") departmentId: String?
    ): Call<FavouritesResponseModel?>?

    @POST(ApiUrl.AdmissionSave)
    fun saveAdmission(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AdmissionSaveRequestModel?
    ): Call<AdmissionSaveResponseModel?>?

    @GET(GetDietFavorites)
    fun getDietFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("departmentId") dept_id: Int
    ): Call<FavouritesResponseModel?>?

    @PUT(DeleteDietRows)
    fun deletedietRows(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DeleteResponseModel?>?

    @POST(ApiUrl.getDietAllDepartment)
    fun getDietAllDepartment(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getAllDepartmentReq: GetAllDepartmentReq
    ): Call<GetAllDepartmentResp>

    @GET(GetDietMasterCategory)
    fun getDietMasterCategoryList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<FavAddAllDepatResponseModel?>?

    @GET(GetDietMasterFrequency)
    fun getDietMasterFrequency(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<FavAddAllDepatResponseModel?>?

    @GET(GetSpecialitySketchFavorites)
    fun getSpecialitySketchFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("fav_type_id") fav_type_id: Int
    ): Call<FavouritesResponseModel?>?

    @PUT(DeleteSpecialitySketchFavorites)
    fun deleteSpecialitySketchFavourite(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DeleteResponseModel?>?

    @GET(GetPreviousSpecialitySketch)
    fun getPreviousSpecialitySketch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<SpecialitySketchPrevResponseModel?>?

    @POST(ApiUrl.GetSpecialitySketchSearchResult)
    fun GetSpecialitySketchSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SpecialitySketchFavMangeResponseModel?>?


    @POST(ApiUrl.GetSpecialitySketchIdResult)
    fun GetSpecialitySketchIdResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SpecalityListResponce?>?

    @POST(ApiUrl.AddSpecialitySketchFavrt)
    fun AddSpecialitySketchFavrt(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("searchkey") searchkey: String?,
        @Body body: RequestDietFavModel?
    ): Call<DietFavMangeResponseModel?>?

    @GET(GetSpecialitySketchFavrtList)
    fun getSpecialitySketchFavrtList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("favourite_id") favourite_id: Int,
        @Query("favourite_type_id") favourite_type_id: Int
    ): Call<FavAddListResponse?>?

    @POST(ApiUrl.getAllBlood)
    fun getAllBloodGroup(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getAllBloodGroupReq: GetAllBloodGroupReq?
    ): Call<GetAllBloodGroupResp?>?

    @POST(ApiUrl.getAllBlood)
    fun getAllPurpose(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getAllPurposeReq: GetAllPurposeReq?
    ): Call<GetAllPurposeResp?>?

    @POST(ApiUrl.getAllBlood)
    fun getBloodComponents(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getBloodComponentsReq: GetBloodComponentsReq?
    ): Call<GetBloodComponentsResp?>?

    @POST(ApiUrl.getPreviousBloodRequest)
    fun getPreviousBlood(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getPreviousBloodReq: GetPreviousBloodReq?
    ): Call<GetPreviousBloodResp?>?

    @POST(ApiUrl.GetDietSearchResult)
    fun GetDietSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<InvestigationSearchResponseModel?>?

    @POST(ApiUrl.GetDietFavddAll)
    fun getDietFavddAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestDietFavModel?
    ): Call<DietFavMangeResponseModel?>?

    @POST(ApiUrl.getPreviousDietOrder)
    fun getPreviousDietOrder(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getPreviousDietOrderReq: GetPreviousDietOrderReq?
    ): Call<GetPreviousDietOrderResp?>?

    @POST(ApiUrl.bloodRequestSave)
    fun bloodRequestSave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body bloodRequestSaveReq: BloodRequestSaveReq?
    ): Call<BloodRequestSaveResp?>?

    @GET(getProgressNotes)
    fun getProgressNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetProgressNotesResp?>?

    @GET(getEncounterByDocAndPatientId)
    fun getEncounter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<GetEncounterByDocAndPatientIdResp?>?

    @GET(getEncounterByDocAndPatientId)
    fun getRadiologyEncounter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<RadiologyEncounterResponseModel?>?

    @POST(ApiUrl.createProgressNotes)
    fun createProgressNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body createProgressNotesReq: CreateProgressNotesReq?
    ): Call<CreateProgressNotesResp?>?

    @GET(editProgressNote)
    fun editProgressNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("progressNotes_uuid") pUuid: Int
    ): Call<EditProgressNotesResp?>?

    @PUT(updateProgressNotes)
    fun updateProgressNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("progressNotes_uuid") pUuid: Int,
        @Body updateProgressNotesReq: UpdateProgressNotesReq?
    ): Call<UpdateProgressNotesResp?>?

    @PUT(deleteProgressNotes)
    fun deleteProgressNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("progressNotes_uuid") pUuid: Int
    ): Call<DeleteProgressNotesResp?>?

    @POST(ApiUrl.Register)
    fun searchOutPatientLmisOrder(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body searchPatientRequestModel: SearchPatientRequestModel?
    ): Call<NewLmisOrderModule?>?

    @GET(getSession)
    fun getSession(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetSessionResp?>?

    @POST(ApiUrl.getGender)
    fun getGender(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getGenderReq: GetGenderReq?
    ): Call<GetGenderResp?>?

    @GET(GetFavddAllList)
    fun getFavddAllListDiet(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("favourite_id") favourite_id: Int,
        @Query("favourite_type_id") favourite_type_id: Int
    ): Call<FavAddListResponse?>?

    @PUT(updateInv)
    fun UpdateInv(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body invupdateData: InvUpdateRequest?
    ): Call<SimpleResponseModel?>?

    @PUT(FavouriteUpdate)
    fun dietEditFav(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavEditResponse?>?

    @POST(ApiUrl.getDoctorName)
    fun getDoctorName(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DoctorNameResponseModel?>?

    @POST(ApiUrl.EmrPost)
    fun lmisEmrpost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestLmisNewOrder: RequestLmisNewOrder?
    ): Call<EmrResponceModel?>?

    @POST(ApiUrl.GetLabSearchResult)
    fun getLmisSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseLmisListview?>?

    @GET(GetEncounters)
    fun getLmisEncounters(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<FectchEncounterResponseModel?>?

    @POST(ApiUrl.CreateEncounter)
    fun LmiscreateEncounter(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") faciltyid: Int,
        @Body createEncounterRequestModel: CreateEncounterRequestModel?
    ): Call<CreateEncounterResponseModel?>?

    @POST(ApiUrl.TreatmentSearchName)
    fun getTreatmentName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<TreatmentNameResponseModel?>?

    @POST(ApiUrl.AddFavTreatment)
    fun FavAdd(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: TreatAddFavRequestModel?
    ): Call<TreaFavAddedResponseModel?>?

    @GET(AddFavtoList)
    fun getFavList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("treatmentId") treatmentId: Int,
        @Query("favouriteId") favouriteId: Int
    ): Call<FavouriteAddToListResponseModel?>?

    @PUT(upfsteTreatmentKitOnModify)
    fun modifyTreatmentKit(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("order_id") orderId: Int,
        @Body updateTreatmentKitPreviousOrderReq: UpdateTreatmentKitPreviousOrderReq
    ): Call<UpdateTreatmentKitPreviousOrderResp?>?

    @POST(ApiUrl.EmrDietPost)
    fun emrDietpost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body emrRequestModelr: DietOrderrequest?
    ): Call<EmrResponceModel?>?

    @POST(ApiUrl.getDoctorName)
    fun getDispatchOrderBY(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OrderByResponseModel?>?

    @POST(ApiUrl.getOrderStatus)
    fun getOrderStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OrderStatusResponseModel?>?

    @POST(ApiUrl.getRMISOrderStatus)
    fun getRMISOrderStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<RmisOrderStatusModules?>?

    @POST(ApiUrl.getOrderStatus)
    fun getSearchOrderStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body orderStatusRequestModel: OrderStatusRequestModel?
    ): Call<OrderStatusResponseModel?>?

    /*
    @POST(ApiUrl.getOrderStatus)
    Call<SearchOrderStatusResponseModel> getSearchOrderStatus(@Header("Accept-Language") String acceptLanguage,
                                                              @Header("Authorization") String authorization,
                                                              @Header("user_uuid") int user_uuid,
                                                              @Header("facility_uuid") int facility_uuid,
                                                              @Body SearchPatientRequestModelCovid searchPatientRequestModelCovid);
*/
    @GET(ipCaseSheetGetAllProfileTypes)
    fun getIpCaseSheetAllProfileTypes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profileType: Int,
        @Query("department_uuid") departmentUuid: Int
    ): Call<GetIpCaseSheetAllProfileTypesResp?>?

    @GET(ipCaseSheetGetById)
    fun getCaseSheet(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_uuid") profile_uuid: Int
    ): Call<GetCaseSheetDetailResp?>?

    @GET(ipCaseSheetGetEncounter)
    fun getIpCaseSheetEncounter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<GetIpCaseSheetEncounterByDocAndPatientIdResp?>?

    @POST(ApiUrl.ipCaseSheetSaveAns)
    fun saveCaseSheetAnswers(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body saveCaseSheetDetailsReq: SaveCaseSheetDetailsReq?
    ): Call<SaveCaseSheetDetailsResp?>?

    @GET(ipCaseSheetGetDefault)
    fun getIpCaseSheetDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetIpCaseSheetDefaultResp>

    @POST(ApiUrl.ipCaseSheetSetDefault)
    fun setIpCaseSheetDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body setIpCaseSheetDefaultReq: SetIpCaseSheetDefaultReq
    ): Call<SetIpCaseSheetDefaultResp>

    @GET(ipCaseSheetGetPrevRecords)
    fun getIpCaseSheetPreviousRecords(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetIpCaseSheetPreviousRecordsResp>

    @GET(ipCaseSheetGetObservedValues)
    fun getIpCaseSheetObservedValues(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetIpCaseSheetObservedValuesResp>

    @POST(ApiUrl.ipCaseSheetAddConsultations)
    fun ipCaseSheetAddConsultations(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body ipCaseSheetAddConsultationsReq: IpCaseSheetAddConsultationsReq
    ): Call<IpCaseSheetAddConsultationsResp>

    @GET(opNotesGetAllProfileTypes)
    fun getOpNotesAllProfileTypes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profileType: Int,
        @Query("department_uuid") departmentUuid: Int
    ): Call<GetOpNotesAllProfileTypesResp?>?

    @GET(opNotesGetById)
    fun getOpNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_uuid") profile_uuid: Int
    ): Call<GetOpNotesDetailResp?>?

    @GET(opNotesGetEncounter)
    fun getOpNotesEncounter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<GetOpNotesEncounterByDocAndPatientIdResp?>?

    @POST(ApiUrl.opNotesSaveAns)
    fun saveOpNotesAnswers(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body saveOpNotesDetailsReq: SaveOpNotesDetailsReq?
    ): Call<SaveOpNotesDetailsResp?>?

    @GET(opNotesGetDefault)
    fun getOpNotesDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetOpNotesDefaultResp>

    @POST(ApiUrl.opNotesSetDefault)
    fun setOpNotesDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body setOpNotesDefaultReq: SetOpNotesDefaultReq
    ): Call<SetOpNotesDefaultResp>

    @GET(opNotesGetPrevRecords)
    fun getOpNotesPreviousRecords(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetOpNotesPreviousRecordsResp>

    @GET(opNotesGetObservedValues)
    fun getOpNotesObservedValues(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetOpNotesObservedValuesResp>

    @POST(ApiUrl.opNotesAddConsultations)
    fun opNotesAddConsultations(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body opNotesAddConsultationsReq: OpNotesAddConsultationsReq
    ): Call<OpNotesAddConsultationsResp>

    @GET(otNotesGetAllProfileTypes)
    fun getOtNotesAllProfileTypes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profileType: Int,
        @Query("department_uuid") departmentUuid: Int
    ): Call<GetOtNotesAllProfileTypesResp?>?

    @GET(otNotesGetById)
    fun getOtNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_uuid") profile_uuid: Int
    ): Call<GetOtNotesDetailResp?>?

    @GET(otNotesGetEncounter)
    fun getOtNotesEncounter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<GetOtNotesEncounterByDocAndPatientIdResp?>?

    @POST(ApiUrl.otNotesSaveAns)
    fun saveOtNotesAnswers(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body saveOtNotesDetailsReq: SaveOtNotesDetailsReq?
    ): Call<SaveOtNotesDetailsResp?>?

    @GET(otNotesGetDefault)
    fun getOtNotesDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetOtNotesDefaultResp>

    @POST(ApiUrl.otNotesSetDefault)
    fun setOtNotesDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body setOtNotesDefaultReq: SetOtNotesDefaultReq
    ): Call<SetOtNotesDefaultResp>

    @GET(otNotesGetPrevRecords)
    fun getOtNotesPreviousRecords(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetOtNotesPreviousRecordsResp>

    @GET(otNotesGetObservedValues)
    fun getOtNotesObservedValues(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetOtNotesObservedValuesResp>

    @POST(ApiUrl.otNotesAddConsultations)
    fun otNotesAddConsultations(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body otNotesAddConsultationsReq: OtNotesAddConsultationsReq
    ): Call<OtNotesAddConsultationsResp>

    @GET(anesthesiaNotesGetAllProfileTypes)
    fun getAnesthesiaNotesAllProfileTypes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profileType: Int,
        @Query("department_uuid") departmentUuid: Int
    ): Call<GetAnesthesiaNotesAllProfileTypesResp?>?

    @GET(anesthesiaNotesGetById)
    fun getAnesthesiaNotes(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_uuid") profile_uuid: Int
    ): Call<GetAnesthesiaNotesDetailResp?>?

    @GET(anesthesiaNotesGetEncounter)
    fun getAnesthesiaNotesEncounter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<GetAnesthesiaNotesEncounterByDocAndPatientIdResp?>?

    @POST(ApiUrl.anesthesiaNotesSaveAns)
    fun saveAnesthesiaNotesAnswers(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body saveAnesthesiaNotesDetailsReq: SaveAnesthesiaNotesDetailsReq?
    ): Call<SaveAnesthesiaNotesDetailsResp?>?

    @GET(anesthesiaNotesGetDefault)
    fun getAnesthesiaNotesDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetAnesthesiaNotesDefaultResp>

    @POST(ApiUrl.anesthesiaNotesSetDefault)
    fun setAnesthesiaNotesDefault(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body setAnesthesiaNotesDefaultReq: SetAnesthesiaNotesDefaultReq
    ): Call<SetAnesthesiaNotesDefaultResp>

    @GET(anesthesiaNotesGetPrevRecords)
    fun getAnesthesiaNotesPreviousRecords(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("profile_type_uuid") profile_type_uuid: Int
    ): Call<GetAnesthesiaNotesPreviousRecordsResp>

    @GET(anesthesiaNotesGetObservedValues)
    fun getAnesthesiaNotesObservedValues(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetAnesthesiaNotesObservedValuesResp>

    @POST(ApiUrl.anaesthesiaNotesAddConsultations)
    fun anaesthesiaNotesAddConsultations(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body anaesthesiaNotesAddConsultationsReq: AnaesthesiaNotesAddConsultationsReq
    ): Call<AnaesthesiaNotesAddConsultationsResp>

    @GET(getCriticalCareChartFilterHeadings)
    fun getCriticalCareChartFilterHeadings(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<CriticalCareChartFilterHeadingsResponse>

    @PUT(updateCriticalCareChartConfig)
    fun UpdateCriticalCareChartConfig(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Body body: ArrayList<SaveCriticalCareChartConfig>?
    ): Call<SimpleResponseModel>

    @POST(ApiUrl.getCriticalCareChartHeadings)
    fun getCriticalCareChartHeadings(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getCriticalCareChartHeadingsReq: GetCriticalCareChartHeadingsReq
    ): Call<GetCriticalCareChartHeadingsResp?>?

    @POST(ApiUrl.getCriticalCareChartMaster)
    fun getCriticalCareChartMaster(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getCriticaCareChartMasterReq: GetCriticalCareChartMasterReq?
    ): Call<GetCriticalCareChartMasterResp?>?

    @GET(getCriticalCareChartByPatientId)
    fun getCriticalCareChartByPatientIdResp(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("critical_care_type") critical_care_type: Int
    ): Call<GetCriticalCareChartByPatientIdResp?>?

    @GET(getCriticalCareChartEncounter)
    fun getCriticalCareChartEncounter(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<GetCriticalCareChartEncounterResp?>?

    @POST(ApiUrl.postCriticalCareChartCreate)
    fun postCriticalCareChartCreate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body postCriticalCareChartCreateReq: PostCriticalCareChartCreateReq?
    ): Call<PostCriticalCareChartCreateResp?>?

    @POST(ApiUrl.postCriticalCareChartUpdate)
    fun postCriticalCareChartUpdate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body postCriticalCareChartUpdateReq: PostCriticalCareChartUpdateReq?
    ): Call<PostCriticalCareChartUpdateResp?>?

    @GET(getCriticalCareChartCompareData)
    fun getCriticalCareChartCompareData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int?,
        @Query("critical_care_type") critical_care_type: Int?,
        @Query("from_date") from_date: String?,
        @Query("to_date") to_date: String
    ): Call<GetCriticalCareChartCompareDataResp?>?

    //Discharge Summary dashboard API
    @GET(GetDischargeSummaryList)
    fun getDischargeSummaryList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("encounter_uuid") encounter_uuid: Int
    ): Call<DischargeSummaryListResponseModel?>?

    @GET(GetDischargeSummaryList)
    fun getNurseDischargeSummaryList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("encounter_uuid") encounter_uuid: Int
    ): Call<DischargeSummaryListResponseModel?>?

    @POST(ApiUrl.DischargeSummaryRevert)
    fun revertDischargePatient(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body revertreq: RevertRequest
    ): Call<RevertResponseModel?>?

    @POST(ApiUrl.getResultDispatch)
    fun getresultdispatchlist(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body requestResultdiapatch: RequestDispatchSearch?
    ): Call<ResponseResultDispatch?>?

    @POST(ApiUrl.getRmisResultDispatch)
    fun getRmisresultdispatchlist(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body requestResultdiapatch: RequestDispatchSearch?
    ): Call<RmisResultDispatchResponseModule?>?

    @Streaming
    @POST(ApiUrl.resultPDF)
    fun getresultPDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestpdf: Requestpdf?
    ): Call<ResponseBody?>?

    //Discharge Summary Previous DATA
    @GET(GetDischargePreviousData)
    fun getDischargeSummaryPreviousData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("encounter_uuid") encounter_uuid: Int?
    ): Call<DischargeSummaryPreviousResponseModel?>?

    @GET(GetDischargePreviousData)
    fun getNurseDischargeSummaryPreviousData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<DischargeSummaryPreviousResponseModel?>?


    //Discharge Type API
    @GET(GetDischargeType)
    fun getDischargeSummaryType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int
    ): Call<ResDischargeType?>?

    @GET(GetDischargeType)
    fun getNurseDischargeSummaryType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<ResDischargeType?>?


    //Discharge Death Type
    @GET(GetDischargeDeathType)
    fun getDischargeSummaryDeathType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int
    ): Call<ResDischargeDeathType?>?

    @GET(GetDischargeDeathType)
    fun getNurseDischargeSummaryDeathType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<ResDischargeDeathType?>?

    //Discharge Default Template
    @GET(GetDefaultTemplate)
    fun getDischargeSummaryDefaultTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int
    ): Call<ResDischargeSummaryDefaultTemplate?>?

    @GET(GetDefaultTemplate)
    fun getNurseDischargeSummaryDefaultTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<ResDischargeSummaryDefaultTemplate?>?


    //Discharge Note Template
    @POST(ApiUrl.GetDischargeNoteTemplate)
    fun getDischargeNoteTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int
    ): Call<ResDischargeSummaryTemplate?>?


    @POST(ApiUrl.GetDischargeNoteTemplate)
    fun getNurseDischargeNoteTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResDischargeSummaryTemplate?>?

    //Discharge Note Template
    @POST(ApiUrl.SetDefaultTemplate)
    fun setDischargeDefaultTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body reqDefaultTemplate: ReqDefaultTemplate?
    ): Call<ResponseBody?>?

    @GET(GetEncounters)
    fun getDischargeEncounters(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<EncounterResponseModel?>?

    @GET(GetEncounters)
    fun getNurseDischargeEncounters(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<EncounterResponseModel?>?

    @GET(GetMrdList)
    fun getMrdList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("encounter_uuid") encounter_uuid: String?,
        @Query("encounter_type_uuid") encounter_type_uuid: String?,
        @Query("visit_date") visit_date: String?
    ): Call<MRDResponseModel?>?


    @POST(ApiUrl.getcasesheetsummary)
    fun getcasesheetsummary(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body caseSheetRequestModel: CaseSheetRequestModel?

    ): Call<CaseSheetResponseModel?>?


    @POST(ApiUrl.GetMrdPrintList)
    fun getMrdPrintList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?


    @POST(ApiUrl.Session)
    fun getSessionTimer(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body body: RequestBody?
    ): Call<ResponseSesionModule?>?


    @POST(ApiUrl.GetDischargeSummaryDoctorName)
    fun getDischargeSummaryDoctorName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body body: RequestBody?
    ): Call<DischargeSummaryDoctorNameResponseModel?>?

    @POST(ApiUrl.GetDischargeSummaryDoctorName)
    fun getNurseDischargeSummaryDoctorName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body body: RequestBody?
    ): Call<DischargeSummaryDoctorNameResponseModel?>?


    //Discharge Summary dashboard API
    @GET(ActivitySession)
    fun getActivitySession(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<ResponseActivitySession?>?

    //IPGETPATIENTLIST
    @POST(ApiUrl.GetAdmittedPatient)
    fun getAdmittedPAtientList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody
    ): Call<InPatientResponseModel?>?

    @GET(GetFavorites)
    fun getLmisFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("lab_id") dept_id: Int,
        @Query("fav_type_id") fav_type_id: Int
    ): Call<FavouritesResponseModel?>?

    @GET(GetFavorites)
    fun getLmisFavouritesDept(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("lab_id") lab_id: Int,
        @Query("fav_type_id") fav_type_id: Int
    ): Call<FavouritesResponseModel?>?

    //VitalNameSearch
    @POST(ApiUrl.getVitalSearchName)
    fun getVitalSearchNameNew(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<VitalSearchNameResponseModel?>?

    @GET(getPrevPatientVital)
    fun getPrevPatientVital(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patientUuid: Int,
        @Query("department_uuid") departmentUuid: Int
    ): Call<GetPrevPatientVitalResp?>?

    @PUT(LabUpdateTemplate)
    fun getVitalTemplateUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: VitalFavUpdateRequestModel?
    ): Call<UpdateResponse?>?

    @GET(LabGetTemplate)
    fun getLmisLastTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("lab_id") dept_id: Int
    ): Call<ResponseLabGetTemplateDetails?>?

    @POST(ApiUrl.GetLabSearchResult)
    fun getLmisLAbSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavSearchResponce?>?

    @GET(getHistoryAdmission)
    fun getAdmissionType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<AdmissionReferalResponseModel?>?

    @POST(ApiUrl.getOtName)
    fun getOtName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OtNameSpinnerResponseModel?>?

    @POST(ApiUrl.getOtSurguryName)
    fun getOtSurgery(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OtSurgeryNameResponseModel?>?

    @POST(ApiUrl.getOtType)
    fun getOtType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OtTypeResponseModel?>?

    //Search
    @POST(ApiUrl.Register)
    fun searchPDSPatient(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PDSResponseModule?>?

    @POST(ApiUrl.CovidQuickRegistrationSave)
    fun quickRegistrationSave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RegistrationUatSaveRequest?
    ): Call<QuickRegistrationSaveResponseModel?>?


    @POST(ApiUrl.CovidQuickRegistrationSave)
    fun quickRegistrationSave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: QuickRegWItholdpinSaveReq?
    ): Call<QuickRegistrationSaveResponseModel?>?

    @POST(ApiUrl.getOtScheduleList)
    fun getOtSchedule(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OtSchedulToCalendarResponseModel?>?

    /* @POST(ApiUrl.getCriticalCareChartMaster)
    Call<GetCriticalCareChartMasterResp> getCriticalCareChartMaster(@Header("Accept-Language") String acceptLanguage,
                                                                    @Header("Authorization") String authorization,
                                                                    @Header("user_uuid") int user_uuid,
                                                                    @Header("facility_uuid") int facility_uuid,
                                                                    @Body GetCriticalCareChartMasterReq getCriticaCareChartMasterReq);

    @GET(getCriticalCareChartByPatientId)
    Call<GetCriticalCareChartByPatientIdResp> getCriticalCareChartByPatientIdResp(@Header("Accept-Language") String acceptLanguage,
                                                                                  @Header("Authorization") String authorization,
                                                                                  @Header("user_uuid") int user_uuid,
                                                                                  @Header("facility_uuid") int facility_uuid,
                                                                                  @Query("patient_uuid") int patient_uuid,
                                                                                  @Query("critical_care_type") int critical_care_type);*/
    @Streaming
    @GET(getDischagePDF)
    fun getDischargePDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("discharge_summary_uuid") discharge_summary_uuid: Int
    ): Call<ResponseBody?>?


    @Streaming
    @POST(ApiUrl.getPrescriptionPDF)
    fun getPrescriptionPDF(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody
    ): Call<ResponseBody?>?

    @POST(ApiUrl.getDischargeSave)
    fun getDischargeSave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SaveRequestModel?
    ): Call<SaveResponseModel?>?

    @POST(ApiUrl.getDischargeApproval)
    fun getDischargeSApproval(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SaveRequestModel?
    ): Call<SaveResponseModel?>?

    @GET(getDischargeRequestApproval)
    fun getDischargeRequests(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("admission_uuid") admission_uuid: Int
    ): Call<DischargeRequestedResponse?>?

    @POST(ApiUrl.getDataTemplateInfo)
    fun getDataTemplateInfo(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResDischargeSummaryTemplateData?>?

    @Streaming
    @POST(ApiUrl.GetDownload)
    fun getDSspecialityDownload(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?


    //add Surgery PIN
    @POST(ApiUrl.Register)
    fun getSurgeryPin(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetSurgeryPinResponseModel?>?

    @POST(ApiUrl.getCheif)
    fun getCheif(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<CheifResponseModel?>?

    @POST(ApiUrl.getCheif)
    fun getSurgeon(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SurgeonResponseModel?>?

    @POST(ApiUrl.getCheif)
    fun getNurse(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<NurseAssistantResponseModel?>?

    @POST(ApiUrl.getCheif)
    fun getAnesthetist(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<AnesthetistSpinnerResponseModel?>?

    @POST(ApiUrl.getOtType)
    fun getSpinnerLession(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LessionSpinnerResponseModel?>?

    @POST(ApiUrl.GetSurgeryName)
    fun getSpinnerSide(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SideSpinnerResponseModel?>?

    @POST(ApiUrl.GetSurgeryName)
    fun getAnaeesthesia(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<AnaeesthesiaSpinnerResponseModel?>?

    @POST(ApiUrl.GetSurgeryName)
    fun getPosition(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PositionResponseModel?>?

    @POST(ApiUrl.getOtType)
    fun getPriority(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PriorityResponseModel?>?

    @POST(ApiUrl.getOtType)
    fun getGrade(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GradeResponseModel?>?

    @POST(ApiUrl.getOtSurguryName)
    fun getSurgerySearchName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SurgerySearchNameResponseModel?>?

    @POST(ApiUrl.saveSurgery)
    fun addSurgery(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<AddSurgeryResponseModel?>?

    @POST(ApiUrl.viewOtSchedule)
    fun viewOtSchedule(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body viewOtScheduleReq: ViewOtScheduleReq?
    ): Call<ViewOtScheduleResp?>?

    @POST(ApiUrl.modifyOtSchedule)
    fun modifyOtSchedule(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body modifyOtScheduleReq: ModifyOtScheduleReq?
    ): Call<ModifyOtScheduleResp?>?

    @POST(ApiUrl.oldPatient)
    fun oldPatient(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OldPatientResponseModule?>?

    @POST(ApiUrl.getDiagnosis)
    fun getDiagnosis(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OtDiagnosisSearchResponseModel?>?

    @POST(ApiUrl.getDiagnosis)
    fun getDiagnosisSearch(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DiagonosisSearchResponse?>?

    @Streaming
    @POST(ApiUrl.getImage)
    fun getImage(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @POST(ApiUrl.deleteTutorial)
    fun deleteTutorial(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<UserManualDeleteResponseModel?>?

    @POST(ApiUrl.updateDownloadCount)
    fun updateDownloadCount(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<UserManualDeleteResponseModel?>?

    @POST(ApiUrl.getRoleControlActivityCode)
    fun getRoleControlActivity(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<RoleControlActivityResponseModel?>?

    @POST(ApiUrl.GetUOMValue)
    fun getUOMValue(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseUOMModules?>?

    @POST(ApiUrl.CovidQuickRegistrationSave)
    fun addPatientsave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestAddPatient?
    ): Call<ResponseAddPatient?>?


    //history

    @PUT(immunicationUpdate)
    fun immunicationUpdate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("uuid") uuid: Int,
        @Body body: RequestBody?
    ): Call<ImmuUpdateResponse?>?

    @GET(getLatestRecord)
    fun getLatestRecordById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Query("patientId") patientId: Int,
        @Query("encounterTypeId") encounterTypeId: Int
    ): Call<PatientLatestRecordResponse?>?

    @POST(ApiUrl.getPatientByIdUrl)
    fun getPatientById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody
    ): Call<PatientDetailResponse?>?

    @POST(ApiUrl.deleteOtSchedule)
    fun deleteOtSchedule(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body deleteOtScheduleReq: DeleteOtScheduleReq?
    ): Call<DeleteOtScheduleResp?>?

    @POST(ApiUrl.GetADmissionDischargeType)
    fun getADmissionDischargeType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<AdmissionDischargeTypeResponseModel?>?


    //Radilogy Test Location

    @POST(ApiUrl.getToLocationRadilogyTest)
    fun getToLocationRadilogyTest(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetToLocationTestResponse?>?

    @POST(ApiUrl.gettolocationmapid)
    fun getToLocationLabTest(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetToLocationTestResponse?>?

    @GET(getAlleryEdit)
    fun getAlleryEdit(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("uuid") uuid: String
    ): Call<EditAllergyBindResponseModel?>?

    @GET(getImmunizationEdit)
    fun getImmunizationEdit(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("uuid") uuid: String
    ): Call<EditImmunizationResponseModel?>?

    @GET(getSurgeryEdit)
    fun getSurgeryEdit(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("uuid") uuid: String
    ): Call<EditSurgeryResponseModel?>?
/*Nurse Desk*/

    // ward details
    @POST(ApiUrl.GetWardList)
    fun getWardList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<WardListResponseModel?>?

    @POST(ApiUrl.GetStoreList)
    fun getStoreList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<StoreListResponseModel?>?

    // Nurse Workflow details
    @GET(GetNurseworkflow)
    fun getWorkFlowNurseGetList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<EmrWorkFlowResponseModel?>?

    //patient Details
    @POST(ApiUrl.getBedmanagemt)
    fun getBedmanagemt(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetPatientListRequestModel
    ): Call<BedManagementPatientListResponseMosel?>?


    //Bed Details
    @GET(getBedDetails)
    fun getBedDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int
    ): Call<BedDetailsResponseModel?>?

    //Get All Bed List

    @POST(ApiUrl.getAllwardList)
    fun getAllBedList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body allward: AllWardListreq
    ): Call<AllWardListDropDownResponse?>?

    //bad Allocation

    @POST(ApiUrl.bedAllocation)
    fun bedAllocation(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body bedallocation: BedAllocationRequest
    ): Call<SimpleWardResponse?>?

    // Bed Transfer

    @POST(ApiUrl.bedTransfer)
    fun bedTransfer(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body bedTransfer: BedTransferRequest
    ): Call<SimpleWardResponse?>?


    //ward transfer

    @POST(ApiUrl.wardTransfer)
    fun wardTransfer(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body wardTransfer: WardTransferRequest
    ): Call<SimpleWardResponse?>?

    //Labview
    @GET(LabView)
    fun getLabViewDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("user_uuid") user_uuid_details: Int,
        @Query("patient_order_uuid") patient_order_uuid: Int
    ): Call<LabViewResponseModule?>?

    //Labview
    @GET(nursePatientLabDetails)
    fun getnurseLabViewDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int
    ): Call<NurseLabResponseModule?>?


    //nurseDeSkInvestigationview
    @GET(getNurseDeskInvestigation)
    fun getNurseDeskInvestigationDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int

    ): Call<NurseDeskInvestigationResponseModel?>?

    @POST(ApiUrl.getNurseDeskResultInvestigation)
    fun getNurseDeskResultInvestigationDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseDeskInvestigationResultResponseModel?>?

    //IpManagement
    @POST(ApiUrl.getIpDashBoardList)
    fun getIpDashBoardList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int?,
        @Header("facility_uuid") facility_uuid: Int?,
        @Body body: IpManageDashBoardRequest?
    ): Call<IpDashBoardResponse?>

    @POST(ApiUrl.getIpLineGraph)
    fun getIpLineGraphData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int?,
        @Header("facility_uuid") facility_uuid: Int?,
        @Body body: IpManageLineGraphRequest?
    ): Call<IpLineGraphResponse?>


    @POST(ApiUrl.getIpWardByFacilityID)
    fun getWardByDepartmentId(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int?,
        @Header("facility_uuid") facility_uuid: Int?,
        @Body body: WardByRequest?
    ): Call<WardByDepartmentModelResponse?>

    @POST(ApiUrl.getNurseDeskResultLab)
    fun getNurseDeskLabDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: requestlabresult?

    ): Call<NurseDeskLabResultResponseModel?>?


    @POST(ApiUrl.getNurseDeskResultRadiology)
    fun getNurseDeskRadiologyResultDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseDeskRadiologyResulyResponseModel?>?

    @POST(ApiUrl.getNurseDeskCCCPatientList)
    fun getNurseDeskCCCPatientList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getNurseDeskCCCPatientListReq: GetNurseDeskCriticalCareChartPatientListReq?
    ): Call<GetNurseDeskCriticalCareChartPatientListResp>


    @GET(getNurseDeskDiet)
    fun getNurseDeskDiet(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int
    ): Call<NurseDeskDietResponse?>?

    @GET(getNurseDeskDiet)
    fun getNurseDeskDietList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int,
        @Query("search_uhid") search_uhid: String,
        @Query("search_patient_name") search_patient_name: String,
        @Query("search_diet_name") search_diet_name: String
    ): Call<NurseDeskDietResponse?>?

    @POST(ApiUrl.updateDietStatus)
    fun updateDietStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<NurseDeskDietResponse?>?

    //Prescription
    @POST(ApiUrl.getNurseDeskPrescription)
    fun getNurseDeskPrescription(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<NurseDeskPrescriptionResponse?>?

    @POST(ApiUrl.saveNurseDeskPrescription)
    fun saveNurseDeskPrescription(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SavePrescriptionRequest?
    ): Call<NurseDeskPrescriptionResponse?>?

    //Dispense
    @POST(ApiUrl.getPatientById)
    fun getPatientById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyDispenseResponse?>?

    @POST(ApiUrl.getPatientPrescriptionById)
    fun getPatientPrescription(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DrugsListResponse?>?

    @POST(ApiUrl.saveDispence)
    fun saveDispence(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body dispanceRequest: DispanceRequest?
    ): Call<DispenseResponce?>?

    @POST(ApiUrl.getPharmacyDispanceList)
    fun getPharmacyDispanceList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetpharmacyListResponse?>?

    @POST(ApiUrl.getDispensePrescription)
    fun getDispensePrescription(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PreviousDispenseListResponse?>?


    @POST(ApiUrl.getPreviousDispense)
    fun getPreviousDispense(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PreviousDispenseResponse?>?

    @POST(ApiUrl.getPatientSearch)
    fun getPatientSearch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PatientResponse?>?

    //Pharmacy management Return
    @POST(ApiUrl.getPatientById)
    fun getReturnPatientById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PharmacyReturnResponse?>?

    @POST(ApiUrl.getReturnPrescription)
    fun getReturnPrescription(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FindReturnListResponse?>?

    @POST(ApiUrl.getPatientPrescription)
    fun getReturnPatientPrescription(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ReturnDrugsListResponse?>?

    @POST(ApiUrl.getPatientSearch)
    fun getReturnPatientSearch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PatientsResponse?>?

    @POST(ApiUrl.getPreviousDispense)
    fun getPreviousReturn(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FindReturnResponse?>?

    @POST(ApiUrl.getPreviousReturn)
    fun getPreviousReturns(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PreviousReturnsResponse?>?

    @POST(ApiUrl.getSaveReturn)
    fun getSaveReturn(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SaveReturnRequest?
    ): Call<FindReturnListResponse?>?


    //nurseDesk DischargeSummary
    @POST(ApiUrl.getNurseDeskDiscahrgeSummaryPatientList)
    fun getnurseDeskDischargeSummaryPatientList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: GetDischargeSummaryListRequestModel

    ): Call<GetDischargeSummaryResponseModel?>?

    //NurseVitals
    @POST(ApiUrl.getBedmanagemt)
    fun getNurseDeskVitalsList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseDeskVitalsResponseModel?>?

    @GET(getVitalsList)
    fun getVitalsList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<MainVItalsListResponseModel?>?

    @GET(getNursePrevVitals)
    fun getPrevNurseVitalsList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("department_uuid") department_uuid: Int
    ): Call<PreviousVitalsResponseModel?>?


    @GET(getWardId)
    fun getWardIdByPatientId(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetWardIdResponseModel?>?

    @POST(ApiUrl.getWardSpinner)
    fun getWardSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardSpinnerReq: GetWardSpinnerReq
    ): Call<GetWardSpinnerResp>?

    //Radiology
    @GET(getNurseRadilogyData)
    fun getNurseRadilogyData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int

    ): Call<NurseRadiologyResponse?>?


    @GET(getNurseDeskInvestigation)
    fun getNurseInvestigationData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int

    ): Call<NurseDeskInvestigationResponseModel?>?

    @POST(ApiUrl.postInvestigationnurse)
    fun getInvestigationpostipsamplecollection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseInvestigationPostResponse?>?

    @POST(ApiUrl.postsamplecollectionlabnurse)
    fun getlabpostipsamplecollection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseLabSampleCollection?>?

    @POST(ApiUrl.getEmergenyCasualty)
    fun getEmergencyCasualty(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getEmergencyCasualtyDetailsReq: GetEmergencyCasualtyDetailsReq
    ): Call<GetEmergencyCasualtyDetailsResp>

    @POST(ApiUrl.getEmergenyCasualtyCaseTypeList)
    fun getEmergencyCasualtyCaseType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getEmergencyCaseTypeListReq: GetEmergencyCaseTypeListReq
    ): Call<GetEmergencyCaseTypeListResp>

    @POST(ApiUrl.getEmergencySpinnerValuesCommon)
    fun getEmergencySpinnerValuesCommon(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getEmergencySpinnerValuesCommonReq: GetEmergencySpinnerValuesCommonReq
    ): Call<GetEmergencySpinnerValuesCommonResp>

    @POST(ApiUrl.getEmergencySpinnerValuesRelationType)
    fun getEmergencySpinnerValuesRelationType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getEmergencySpinnerValuesRelationTypeReq: GetEmergencySpinnerValuesRelationTypeReq
    ): Call<GetEmergencySpinnerValuesRelationTypeResp>

    @POST(ApiUrl.getEmergencySpinnerValuesGender)
    fun getEmergencySpinnerValuesGender(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetEmergencySpinnerValuesGenderResp>

    @POST(ApiUrl.getEmergencySpinnerValuesWard)
    fun getEmergencySpinnerValuesWard(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getEmergencySpinnerValuesWardReq: GetEmergencySpinnerValuesWardReq
    ): Call<GetEmergencySpinnerValuesWardResp>

    @POST(ApiUrl.getEmergencySearch)
    fun getEmergencySearch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getEmergencySearch: GetEmergencySearchReq
    ): Call<GetEmergencySearchResp>

    @Streaming
    @POST(ApiUrl.printCasualtyPaySlip)
    fun printCasualtyPaySlip(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body printCasualtyPaySlipReq: PrintCasualtyPaySlipReq
    ): Call<ResponseBody>

    @POST(ApiUrl.getDepartmentById)
    fun getDepartmentById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getDepartmentByIdReq: GetDepartmentByIdReq
    ): Call<GetDepartmentByIdResp>

    @POST(ApiUrl.postSaveEmergencyCasualty)
    fun saveEmergencyCasualty(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body saveEmergencyCasualtyReq: SaveEmergencyCasualtyReq
    ): Call<SaveEmergencyCasualtyResp>

    @POST(ApiUrl.postUpdateEmergencyCasualty)
    fun updateEmergencyCasualty(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body updateEmergencyCasualtyReq: UpdateEmergencyCasualtyReq
    ): Call<UpdateEmergencyCasualtyResp>

    //Pharmacy
    @POST(ApiUrl.GetFrequency)
    fun getInjectionStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<StatusSpinnerResponseModel?>?

    @POST(ApiUrl.Register)
    fun getSearchPatientList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SearchMobileresponseModel?>?

    @POST(ApiUrl.InjectionWorkiList)
    fun getInjectionWorkiList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<InjectionListResponseModel?>?

    @POST(ApiUrl.viewInjection)
    fun getViewInjection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ViewDialogResponseModel?>?

    @POST(ApiUrl.doAdministration)
    fun postDoAdministration(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AdministrationRequestModel?
    ): Call<AdministrationPostResponseModel?>?

    //getBedmanagemt
    @POST(ApiUrl.postipsamplecollection)
    fun getpostipsamplecollection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NursedeskPostsampleResponse?>?


    @GET(getRoleControlQuick)
    fun getRoleControlQuick(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("screenTypeCode") screentypeCode: Int
    ): Call<QuickelementRoleResponseModel?>?

    //GetWardMaster
    @POST(ApiUrl.getWardMasterListData)
    fun getWardMasterList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AdmissionListRequestModel
    ): Call<ResponseWardViewModules?>?

    @POST(ApiUrl.getWardSequenceNo)
    fun getWardSequenceNo(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetSequenceNoResp?>?

    @POST(ApiUrl.getWardGender)
    fun getWardGender(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardGenderReq: GetWardGenderReq
    ): Call<GetWardGenderResp?>?

    @POST(ApiUrl.getWardReferenceType)
    fun getWardReferenceType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardReferenceReq: GetWardReferenceTypeReq
    ): Call<GetWardReferenceTypeResp?>?

    @POST(ApiUrl.getWardInstitution)
    fun getWardInstitution(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardInstitutionReq: GetWardInstitutionReq
    ): Call<GetWardInstitutionResp?>?

    @POST(ApiUrl.getWardLocationDetails)
    fun getWardLocationDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardLocationDetailsReq: GetWardLocationDetailsReq
    ): Call<GetWardLocationDetailsResp?>?

    @POST(ApiUrl.getWardFloorRoom)
    fun getWardFloorRoom(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardFloorRoomReq: GetWardFloorRoomReq
    ): Call<GetWardFloorRoomResp?>?

    @POST(ApiUrl.getWardDepartment)
    fun getWardDepartment(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardDepartmentReq: GetWardDepartmentReq
    ): Call<GetWardDepartmentResp?>?

    @POST(ApiUrl.getWardRoomClassification)
    fun getWardRoomClassification(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardRoomClassificationReq: GetWardRoomClassificationReq
    ): Call<GetWardRoomClassificationResp?>?

    @POST(ApiUrl.createWard)
    fun createWard(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body createWardReq: CreateWardReq
    ): Call<CreateWardResp?>?

    @GET(getWardRoomInfo)
    fun getWardRoomInfo(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") wardUuid: Int
    ): Call<GetWardRoomInfoResp?>?

    @POST(ApiUrl.updateWardDetails)
    fun updateWardDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body updateWardReq: UpdateWardReq
    ): Call<UpdateWardResp?>?

    @POST(ApiUrl.getWardStoresByType)
    fun getWardStoreByType(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardStoreByTypeReq: GetWardStoreByTypeReq
    ): Call<GetWardStoreByTypeResp?>?

    @POST(ApiUrl.getWardById)
    fun getWardById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardByIdReq: GetWardByIdReq
    ): Call<GetWardByIdResp?>?


    ///WardMaster update bed
    @POST(ApiUrl.getUpdateBedInfo)
    fun getUpdateBedInformation(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestBedUpdateModule: java.util.ArrayList<RequestBedUpdateModules>
    ): Call<ReponseBedUpdate?>?


    @Multipart
    @POST(ApiUrl.getWardMasterRoomImageUpload)
    fun getUploadFileWardRoom(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Part file: MultipartBody.Part?
    ): Call<UploadimageResponse?>?

    /*
    Wardstore mapping
     */
    @POST(ApiUrl.getWardStoreMapping)
    fun getWardStoreMAppingData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int?,
        @Header("facility_uuid") facility_uuid: Int?,
        @Body body: RequestWardRoomMapping?
    ): Call<ResponseWardStoreMapping?>

    /*
    Wardmaster getlist
     */

    //FetchWardmasterdetails
    @POST(ApiUrl.getWardMasterFetchData)
    fun getWardMasterFetchData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody
    ): Call<ResponseFetchDataWardMaster?>?

    /*
    WardMaster Data List
     */
    //getBedmanagemt
    @POST(ApiUrl.deletewardmastelistdata)
    fun getDeleteWardMasterData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponsemasterlistDelete?>?


    //TreatmentKit Prescription Route Spinner
    @POST(ApiUrl.GetFrequency)
    fun getTKPrescRouteSpinner(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<TreatmentPrescRouteSpinnerResponse?>?
    //wardmaster --> roomsetup

    /*
   Wardstore mapping
    */
    @POST(ApiUrl.roomsetup)
    fun getRoomsetupData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int?,
        @Header("facility_uuid") facility_uuid: Int?,
        @Body body: RequestBedRoomselected?
    ): Call<RoomSetupResponse?>


    //Health office


    @POST(ApiUrl.getHealthOfficeList)
    fun getHealthOfficeList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: GetHealthOfficeListRequest?
    ): Call<HealthOfficeListResponseModel?>?


    //IP Admission PeriodSpinner
    @GET(GetPeriod)
    fun getPeriodSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<DropDownResponseModel?>?

    @GET(getNurseDeskServerTime)
    fun getNurseDeskServerTime(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetNurseDeskServerTimeResp?>?


    @POST(ApiUrl.getTranferListData)
    fun getTranferListData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: TransferListRequestModel?
    ): Call<TranferListDataResponseModel?>?


    @POST(ApiUrl.getTransferStatus)
    fun getTransferStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: ArrayList<TranferStatusRequestModelItem>?
    ): Call<SimpleResponseModel?>?


    @POST(ApiUrl.ReciveTranfer)
    fun getReciveTranfer(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: ArrayList<TranferStatusRequestModelItem>?
    ): Call<SimpleResponseModel?>?

    @POST(ApiUrl.getDistrictDropdown)
    fun getDistrictDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body districtReq: DistrictReq
    ): Call<DistrictResp>

    @POST(ApiUrl.getInstitutionTypeDropdown)
    fun getInstitutionTypeDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body institutionTypeReq: InstitutionTypeReq
    ): Call<InstitutionTypeResp>

    @POST(ApiUrl.getInstitutionCategoryDropdown)
    fun getInstitutionCategoryDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body institutionCategoryReq: InstitutionCategoryReq
    ): Call<InstitutionCategoryResp>

    @POST(ApiUrl.getInstitutionDropdown)
    fun getInstitutionDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body institutionReq: InstitutionReq
    ): Call<InstitutionResp>

    @POST(ApiUrl.getDepartmentDropdown)
    fun getDepartmentDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body departmentReq: DepartmentReq
    ): Call<DepartmentResp>

    @POST(ApiUrl.getWardTypeDropdown)
    fun getWardTypeDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getWardTypeReq: GetWardTypeReq
    ): Call<GetWardTypeResp>

    @POST(ApiUrl.getWardGenderDropdown)
    fun getWardGenderDropdown(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<com.oasys.digihealth.doctor.ui.ip_management.wardmaster.model.advanced_search.GetGenderResp>

    @POST(ApiUrl.getDepartmentByFacilityId)
    fun getDepartmentByFacilityId(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getDepartmentReq: GetDepartmentReq
    ): Call<GetDepartmentResp>


    @GET(getApplicationRuleSettings)
    fun getApplicationRuleSettings(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetApplicationRuleSettingsResp>


    @GET(getAllActiveConfigs)
    fun getAllActiveConfigs(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetAllActiveConfigsResp>


    @POST(ApiUrl.Login_session)
    fun LoginSeasion(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: String,
        @Header("session_id") session_id: String?,
        @Body req: LoginSeesionRequest
    ): Call<SimpleResponseModel>


    //DepartmentWise reports

    //Session Wise Report
    @POST(ApiUrl.getInstitutionCategoryRegDepReports)
    fun getInstitutionCategoryApi(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<InstitutionCategoryResponse?>?


    @POST(ApiUrl.getDepartmentDropdownRegDepReports)
    fun getDepartmentDropdownRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getHudDropdownRegDepReports)
    fun getSessionHUDSpinnerRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getBlockDropdownReports)
    fun getBlockDropdownReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getHealthOfficeDropdownRegDepReports)
    fun getHealthOfficeDropdownRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getInstitutionTypeDropdownRegDepReports)
    fun getInstitutionTypeDropdownRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.getInstitutionDropdownRegDepReports)
    fun getInstitutionDropdownRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @PUT(LmisLabUpdate)
    fun lmisLabUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestLmisNewOrder: LabModifiyRequest?
    ): Call<LabModifiyResponse?>?

    @PUT(RadiologyUpdate)
    fun rmisUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestRmisupdate: RadiologyRequestModel?
    ): Call<RadiologyUpdateResponse?>?


    @POST(ApiUrl.getGenderDropdownRegDepReports)
    fun getGenderDropdownRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?


    @POST(ApiUrl.getSpecialityCensusChartLabelRegDepReports)
    fun getSpecialityCensusChartLabelRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<SessionReportLableResponse?>?

    @POST(ApiUrl.getSpecialityCensus)
    fun getSpecialityCensus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: SessionReportRequestModel?
    ): Call<getSpecialityCensusResponse?>?


    @POST(ApiUrl.getDistrictDropdownFilterRegDepReports)
    fun getDistrictDropdownFilterRegDepReports(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody?
    ): Call<LabFilterResponseModel?>?

    @POST(ApiUrl.Logout_Session)
    fun LogoutSeasion(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("session_id") session_id: String?,
        @Body req: RequestBody
    ): Call<SimpleResponseModel>


    @GET(getPatientAllReferrals)
    fun getPatientAllReferrals(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patient_uuidd: String
    ): Call<GetPatientAllReferralsResponse?>?

    @POST(ApiUrl.getPatientAllVisits)
    fun getPatientAllVisits(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body req: GetPatientAllVisitsRequest
    ): Call<GetPatientAllVisitsResponse?>?

    @POST(ApiUrl.getusersbyusertypeids)
    fun getusersbyusertypeids(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getDoctorsReq: GetDoctorsReq
    ): Call<GetDoctorsResp>

    @POST(ApiUrl.getActiveWardByIsCasuality)
    fun getActiveWardByIsCasuality(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getToWardReq: GetToWardReq
    ): Call<GetToWardResp>

    @Multipart
    @POST(ApiUrl.postWorkOrderAttachments)
    fun postWorkOrderAttachments(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Part body: MultipartBody.Part
    ): Call<PostImageUploadResp>

    @POST(ApiUrl.readUploadImage)
    fun readUploadImage(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body readUploadImageReq: ReadUploadImageReq
    ): Call<ResponseBody>

    //Admission autocomplete
    @POST(ApiUrl.GetFavaddDepartmentList)
    fun getAutoCompleteDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DepartmentAutoCompleteResponseModel?>?


    //CurrentDateTime

    @GET(getCurrentDateTime)
    fun CurrentDateTime(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<CurrentDateTimeResponseModel?>?


    @POST(ApiUrl.getAllDepartments)
    fun AllDepartment(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<AllDepartmentsResponseModel>


    @POST(ApiUrl.GetFavaddDepartmentList)
    fun getSearchDepartment(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response.DepartmentResponseModel>

    @GET(getPatientAdmission)
    fun getPatientAdmissionRef(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int,
        @Query("facility_uuid") facility_id: Int,
        @Query("referral_type_uuid") referral_type_uuid: Int
    ): Call<AdmissionPatientRefResponseModel?>?


    @POST(ApiUrl.updateEMRAdmission)
    fun updateEMRAdmission(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AdmissionPatientUpdateRequestModel?
    ): Call<AdmissionUpdatePatientResponseModel?>?

    @POST(ApiUrl.updateAdmissionreq)
    fun updateAdmissionreq(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AdmissionUpdateRequestModel?
    ): Call<AdmissionUpdateRespModel?>?


    @POST(ApiUrl.departmentAutocomplete)
    fun getAutoCompleteDepartment(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DeptsRespModel?>?
}