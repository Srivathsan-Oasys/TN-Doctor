package com.hmis_tn.doctor.api_service

import com.hmis_tn.doctor.BuildConfig.BASE_DOMAIN

object ApiUrl {

    const val Login =
        BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/loginNew"
    const val setPassword =
        BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/newUser_changePassword"
    const val Register =
        BASE_DOMAIN + "registration/v1/api/patient/search"
    const val Mypatient =
        BASE_DOMAIN + "HMIS-EMR/v1/api/my-patient-list/getMyPatientList"
    const val SerachOldPin =
        BASE_DOMAIN + "registration/v1/api/patient/old-patient-search"
    const val EMRWORKFLOW =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-workflow-settings/getEMRWorkflowByUserId"
    const val DashBoardDetail =
        BASE_DOMAIN + "HMIS-EMR/v1/api/dashboard/getDashBoarddata"
    const val GetConfigList =
        BASE_DOMAIN + "Appmaster/v1/api/contextActivityMap/getMappingByContextId"
    const val Getnstitutions =
        BASE_DOMAIN + "Appmaster/v1/api/userFacility/getUserFacilityByUId"

    const val configCreate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-workflow-settings/create"

    const val GetconfigUpdate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-workflow-settings/update"

    const val GetNurseconfigUpdate =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/work-flow/update"


    const val GetNurseconfigCreate =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/work-flow/create"
    const val GetHistoryconfigUpdate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-history-settings/update"
    const val GetHistoryconfigCreate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-history-settings/create"
    const val GetFavorites =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getFavourite"
    const val GetDietFavorites =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getDietFavourite"
    const val GetTemplete =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettemplateByID"


    const val GetInvestigationTemplete =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettemplateByID"

    const val GetPrescriptionTemplete =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettemplateByID"
    const val GetOfficeList =
        BASE_DOMAIN + "Appmaster/v1/api/facility/getUserOfficeByUserId"
    const val GetDuration =
        BASE_DOMAIN + "HMIS-EMR/v1/api/chief-complaints-duration/get"
    const val GetChiefComplaintsSearchResult =
        BASE_DOMAIN + "HMIS-EMR/v1/api/chief-complaints-master/getByFilters"
    const val GetLabSearchResult =
        BASE_DOMAIN + "HMIS-LIS/v1/api/testmaster/gettestandprofileinfo"
    const val getRadiologyCodeSearchUrl =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/testmaster/gettestandprofileinfo"
    const val getInvestigationCodeSearchUrl =
        BASE_DOMAIN + "HMIS-INV/v1/api/testmaster/gettestandprofileinfo"
    const val GetRadioSearchResult =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/testmaster/gettestandprofileinfo"
    const val GetInstitutionList =
        BASE_DOMAIN + "Appmaster/v1/api/facility/getFacilityByHealthOfficeId"
    const val GetDepartmentList =
        BASE_DOMAIN + "Appmaster/v1/api/manageInstitution/getManageInstitutionByUFId"
    const val GetEncounters =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
    const val CreateEncounter =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/create"
    const val GetLabType =
        BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getReference"
    const val GetRmisLabType =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/commonReference/getReference"
    const val GetInvestigationType =
        BASE_DOMAIN + "HMIS-INV/v1/api/commonReference/getReference"
    const val GetRadiologyType =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/commonReference/getReference"

    const val GetToLocation =
        BASE_DOMAIN + "HMIS-LIS/v1/api/tolocationmaster/gettolocationmaster"
    const val GetRmisToLocation =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/tolocationmaster/gettolocationmaster"
    const val GetTestMethod =
        BASE_DOMAIN + "HMIS-LIS/v1/api/tolocationmaster/gettolocationmaster"
    const val GetInvestigationToLocation =
        BASE_DOMAIN + "HMIS-INV/v1/api/tolocationmaster/gettolocationmaster"
    const val updateInv =
        BASE_DOMAIN + "HMIS-INV/v1/api/patientorders/updatePatientOrder"

    const val GetInvestigationToLocationMapId =
        BASE_DOMAIN + "HMIS-INV/v1/api/departmentwisemapping/gettolocationmapid"

    const val GetPrevLab =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/getLatestRecords"
    const val GetFavDepartmentList =
        BASE_DOMAIN + "Appmaster/v1/api/department/getDepartmentOnlyById"
    const val GetFavaddDepartmentList =
        BASE_DOMAIN + "Appmaster/v1/api/department/getAllDepartment"
    const val EmrPost =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/postpatientorder"
    const val LmisLabUpdate =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/updatePatientOrder"
    const val RadiologyUpdate =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorders/updatePatientOrder"
    const val EmrDietPost =
        BASE_DOMAIN + "HMIS-DIET_KITCHEN/v1/api/dietorder/addDietOrder"
    const val GetVitalsTemplatet =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettemplateByID"
    const val InsertChiefComplaint =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-chief-complaints/create"
    const val GetHistoryAll =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-history-settings/getById"
    const val GetEncounterAllergyType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter-type/getEncounterType"
    const val GetPrevRadiology =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorders/getLatestRecords"
    const val GetAllergyName =
        BASE_DOMAIN + "HMIS-EMR/v1/api/allergyMaster/getAllergyMaster"
    const val GetAllergySource =
        BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"
    const val GetAllergySeverity =
        BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"
    const val GetPrevPrescription =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/getPreviousPrescription"
    const val GetFrequency =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/commonReference/getReference"
    const val GetPrescriptionDuration =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/commonReference/getReference"
    const val InsertDiagnosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-diagnosis/create"
    const val SearchDiagnosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/diagnosis/getDFilter"
    const val GetToLocationRadiology =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/tolocationmaster/gettolocationmaster"
    const val EmrRadiologypost =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorders/postpatientorder"
    const val GetPrescriptionsSearchResult =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/itemMaster/drugNameSearch"
    const val GetUomVitalList =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/uomMaster/getUomMaster"
    const val GetInstruction =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/commonReference/getReference"
    const val EmrPrescriptionPost =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/addAllPrescriptionDetails"
    const val GetRouteDetails =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/commonReference/getReference"
    const val GetFavAddDiagonosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=diagnosis"
    const val GetAllergyAll =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-allergy/getPatientAllergies"
    const val DeleteRows =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/delete"
    const val DeleteTemplate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/deleteTemplateDetails"
    const val GetFavddAll =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=lab"
    const val GetFavddInvestigationAll =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=investigations"
    const val GetRadiologyFavourite =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=radiology"
    const val GetFavddAllList =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getFavouriteById"
    const val GetMyProfile =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/getUserProfileById"
    const val GetOtpForPasswordChange =
        BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/sendOtp"
    const val GetPasswordChanged =
        BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/changePassword"
    const val GetHistoryPrescription =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/getLatestPrescription"
    const val GetFamilyAllType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/family-history/getFamilyHistory"
    const val CreateAllergy =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-allergy/create"
    const val getHistoryVitals =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-patient-vitals/getHistoryPatientVitals"
    const val AddFavChiefComplaint =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create"
    const val getVitalsList =
        BASE_DOMAIN + "HMIS-EMR/v1/api/vitalMaster/getVitals"
    const val getZeroStock =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/stockItems/getZeroStockItems"
    const val GetFamilyType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"
    const val CreateFamilyHistory =
        BASE_DOMAIN + "HMIS-EMR/v1/api/family-history/create"
    const val GetSurgeryInstitutions =
        BASE_DOMAIN + "Appmaster/v1/api/userFacility/getUserFacilityByUId"
    const val GetSurgeryDetails =
        BASE_DOMAIN + "HMIS-EMR/v1/api/surgery-history/getSurgeryHistory"
    const val GetSurgeryName =
        BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"
    const val CreateSugery =
        BASE_DOMAIN + "HMIS-EMR/v1/api/surgery-history/create"
    const val GetImmunizationName =
        BASE_DOMAIN + "HMIS-EMR/v1/api/immunizations/getimmunization"
    const val GetImmunizationList =
        BASE_DOMAIN + "HMIS-EMR/v1/api/immunization/getAll"
    const val CreateImmunization =
        BASE_DOMAIN + "HMIS-EMR/v1/api/immunization/create"
    const val GetImmunizationInstitution =
        BASE_DOMAIN + "Appmaster/v1/api/facility/faciltiySearchDropdown"
    const val PrescriptionInfo =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/itemMaster/getItemMasterById"
    const val PrescriptionDrugInfo =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/StockRequests/getStockDetailsIndent"
    const val getDiagnosisHistory =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-diagnosis/getByFilters"
    const val FavouriteUpdate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/updateFavouriteById"
    const val LabTemplateCreate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/create"
    const val LabGetTemplate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettempdetails"
    const val GetVitalSearchName =
        BASE_DOMAIN + "HMIS-EMR/v1/api/vitalMaster/getAllVitals"
    const val VitualGetTemplate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettempdetails"
    const val PrescriptionFavAdd =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=drug"
    const val LabUpdateTemplate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/template/updatetemplateById"
    const val DiagonosisSearcbValue =
        BASE_DOMAIN + "HMIS-EMR/v1/api/diagnosis/getDFilter?"
    const val PrescriptionSearch =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/itemMaster/drugNameSearch"
    const val UpdateAllergy =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-allergy/updatePatientAllergy"
    const val UpdateFamilyHistory =
        BASE_DOMAIN + "HMIS-EMR/v1/api/family-history/updateFamilyHistory"
    const val UpdateSurgery =
        BASE_DOMAIN + "HMIS-EMR/v1/api/surgery-history/updateSurgery"
    const val VitalSave =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-patient-vitals/create"
    const val VitalSearch =
        BASE_DOMAIN + "HMIS-EMR/v1/api/vitalMaster/getAllVitals"
    const val labResult =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/getlatestlabresultsemr"
    const val labResultCompere =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/getlabresultsdatecompareemr"
    const val radiologyTopResult =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorders/getlatestlabresultsemr"
    const val invTopResult =
        BASE_DOMAIN + "HMIS-INV/v1/api/patientorders/getlatestlabresultsemr"


    const val radiologyResult =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorderdetails/getAuthorizedRadiologyResults"
    const val GetStoreMaster =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/storeMaster/getStoreDepartmentById"
    const val GetLabResultByDate =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/getlabresultsdatecompareemr"
    const val GetPrevTreatmentKit =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-treatment/prevKitOrdersById"
    const val GetSpecimen_Type =
        BASE_DOMAIN + "registration/v1/api/masters/getActiveLists"
    const val GetRoleBased =
        BASE_DOMAIN + "Appmaster/v1/api/roleControlActivity/getRoleControlActivityByRoleandActivityCode"
    const val getUserDepartment =
        BASE_DOMAIN + "Appmaster/v1/api/userDepartment/getUserDepartment"
    const val GetSnommed =
        BASE_DOMAIN + "HMIS-EMR/v1/api/snomed/getsnomeddetails"
    const val GetParentSnommed =
        BASE_DOMAIN + "HMIS-EMR/v1/api/snomed/getsnomedparent"
    const val GetChildSnommed =
        BASE_DOMAIN + "HMIS-EMR/v1/api/snomed/getsnomedchildren"
    const val sendApprovel =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/sendApprovalTestWise"

    const val treatmentKitAutoSearch =
        BASE_DOMAIN + "HMIS-EMR/v1/api/treatment-kit/autoSearch"
    const val treatmentKitFavouriteById =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getTreatmentKitFavouriteById"

    const val RmissendApprovel =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientworkorder/sendApprovalTestWise"
    const val CreateHistoryDiagnosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-diagnosis/create"
    const val getOpNotes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getAll"
    const val orderProcessApprovel =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/orderProcessApproval"
    const val getRejectReference =
        BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getReference"
    const val getRmisRejectReference =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/commonReference/getReference"

    const val getOPNotesAll =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getById"
    const val CreateTreatmentKit =
        BASE_DOMAIN + "HMIS-EMR/v1/api/treatment-kit/create"
    const val CreateTreatmentKitOrder =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-treatment/create"
    const val getTreatmentkitInvestigationSearch =
        BASE_DOMAIN + "HMIS-INV/v1/api/testmaster/gettestandprofileinfo"

    //Prescription Update

    const val updatePrescription =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/updatePrescription"

    //Radilogy to to location


    const val getToLocationRadilogyTest =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/departmentwisemapping/gettolocationmapid"

    //Covid Registration Section A1 APIs
    const val GetSalutationTitiles =
        BASE_DOMAIN + "Appmaster/v1/api/title/getTitle"
    const val GetGender =
        BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"
    const val GetPeriod =
        BASE_DOMAIN + "registration/v1/api/period/getPeriod"
    const val GetNationalityAndMobileAndPatientCateType =
        BASE_DOMAIN + "registration/v1/api/masters/getActiveLists"

    const val GetNationalityAllActive =
        BASE_DOMAIN + "Appmaster/v1/api/country/getAllActive"
    const val getState =
        BASE_DOMAIN + "Appmaster/v1/api/state/getByCountryId"
    const val getDistict =
        BASE_DOMAIN + "Appmaster/v1/api/districtMaster/getByStateId"
    const val getTaluk =
        BASE_DOMAIN + "Appmaster/v1/api/taluk/getByDistrictId"
    const val getVillage =
        BASE_DOMAIN + "Appmaster/v1/api/healthvillage/getbytalukid"
    const val addPatientDetails =
        BASE_DOMAIN + "registration/v1/api/covidRegistration/addPatientDetails"
    const val getCovidPatientSearch =
        BASE_DOMAIN + "registration/v1/api/patient/search"
    const val GetQuarantineType =
        BASE_DOMAIN + "registration/v1/api/masters/getActiveLists?table_name=quarentine_status_type"
    const val CovidUpdate =
        BASE_DOMAIN + "registration/v1/api/covidRegistration/updatePatientDetails"
    const val PrevChiefComplaint =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-chief-complaints/get-prev-pat-cc-by-patientId"
    const val GetINvestigationsSearchResult =
        BASE_DOMAIN + "HMIS-INV/v1/api/testmaster/gettestandprofileinfo"
    const val GetPrevInvestigation =
        BASE_DOMAIN + "HMIS-INV/v1/api/patientorders/getLatestRecords"
    const val GetOpExpandableList =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getById"
    const val PrevDiagnosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-diagnosis/getPreviousDiagnosisByPatiendId"
    const val GetRepeatedResult =
        BASE_DOMAIN + "registration/v1/api/masters/getActiveLists?table_name=test_result"
    const val GetIntervals =
        BASE_DOMAIN + "registration/v1/api/masters/getActiveLists?table_name=repeat_test_type"
    const val GetPrescFrequency =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/drugFrequency/getDrugFrequency"
    const val GetDetailsbyTablename =
        BASE_DOMAIN + "registration/v1/api/patient/getDetailsByTableName"
    const val orderSendtonext =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/assigntootherinstitute"

    const val RmisorderSendtonext =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorderdetails/assigntootherreg"
    const val CovidQuickRegistrationSave =
        BASE_DOMAIN + "registration/v1/api/quickRegistration/addPatientDetails"
    const val facilityLocation =
        BASE_DOMAIN + "Appmaster/v1/api/facilityLocation/getFacilityLocationByfacilityId"
    const val orderDetailsGet =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/getOrderProcessDetails"
    const val getNoteTemplate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/notetemplate/getNoteTemplateById"

    const val RmisorderDetailsGet =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorderdetails/getOrderProcessDetails"
    const val orderDetailsGetLabApproval =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/getOrderProcessDetails"
    const val getBlockZone =
        BASE_DOMAIN + "Appmaster/v1/api/healthblock/getBlockOnlyById"
    const val getTestMaster =
        BASE_DOMAIN + "HMIS-LIS/v1/api/testmaster/gettestmaster"
    const val getSavedPDF =
        BASE_DOMAIN + "registration/v1/api/patient/printPatient"

    //Admission
    const val getAdmissionDepartment =
        BASE_DOMAIN + "Appmaster/v1/api/department/getAllDepartment"
    const val getAdmissionWardById =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/room/getRoomByWardId"

    const val getAdmissionGender =
        BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"
    const val getAdmissionDistrict =
        BASE_DOMAIN + "Appmaster/v1/api/districtMaster/getAll"
    const val getAdmissionDistrictById =
        BASE_DOMAIN + "Appmaster/v1/api/taluk/getByDistrictId"
    const val getAdmissionTalukById =
        BASE_DOMAIN + "Appmaster/v1/api/healthvillage/getbytalukid"
    const val getAdmissionReference =
        BASE_DOMAIN + "Appmaster/v1/api/commonReference/getReference"
    const val getAdmissionWard =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getActiveWardByIsCasuality"
    const val getAdmissionDoctor =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/getusersbyusertypeids"
    const val addAdmission =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/create"
    const val updateAdmission =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/update"
    const val getAdmissionByid =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/getadmissionById"
    const val getAdmissionCommonReference =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/commonreferencegroup/getReference"
    const val searchPinOrMobile =
        BASE_DOMAIN + "registration/v1/api/patient/search"
    const val getAdmissionDiagnosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/diagnosis/getDiagnosisAutoSearch"
    const val getAttenderPass =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/printAdmissionDetails"
    const val getFacilityLocationByFacilityId =
        BASE_DOMAIN + "Appmaster/v1/api/facilityLocation/getFacilityLocationByfacilityId"

    const val printadmission =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/printAdmissionDetails"


    const val getWardId =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/getWardByPatientID"

    const val getWardSpinner =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getActiveWard"

    //dipstch pdf
    const val dispatchPDF =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/printsampletransport"
    const val labPDF =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorderdetails/printauthorizedlabresults"

    const val rmisPDF =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientworkorderdetails/printauthorizedlabresults"
    const val saveOrder =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/postPatientOrderReg"
    const val getLocationMaster =
        BASE_DOMAIN + "HMIS-LIS/v1/api/tolocationmaster/gettolocationmasterbyfacilityid"

    const val getRmisLocationMaster =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/tolocationmaster/gettolocationmasterbyfacilityid"
    const val getLabName =
        BASE_DOMAIN + "Appmaster/v1/api/facility/otherFaciltiySearchDropdown"
    const val GetRefrenceTestMethod =
        BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getAllReference"
    const val updateQuickRegistration =
        BASE_DOMAIN + "registration/v1/api/quickRegistration/updatePatientDetails"

    const val updateOPCorrecttion =
        BASE_DOMAIN + "registration/v1/api/correction/updatePatientDetails"

    const val rejectData =
        BASE_DOMAIN + "HMIS-LIS/v1/api/sampletransportdetails/sampleRejectForAll"

    const val RmisrejectData =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/sampletransportdetails/sampleRejectForAll"
    const val sampleRecived =
        BASE_DOMAIN + "HMIS-LIS/v1/api/sampletransportdetails/sampletransportreceived"

    const val RmissampleRecived =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/sampletransportdetails/sampletransportreceived"
    const val getLAbNameinList =
        BASE_DOMAIN + "Appmaster/v1/api/facility/getFacilityExclusiveById"
    const val getApplicationRules =
        BASE_DOMAIN + "Appmaster/v1/api/applicationRuleSettings/getApplicationRuleSettings"
    const val getLabTestList =
        BASE_DOMAIN + "HMIS-LIS/v1/api/viewlabtest/getviewlabtest"

    const val getRmisTestList =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/viewlabtest/getviewlabtest"

    const val gettolocationmapid =
        BASE_DOMAIN + "HMIS-LIS/v1/api/departmentwisemapping/gettolocationmapid"
    const val getAssignedSpinnerList =
        BASE_DOMAIN + "Appmaster/v1/api/facility/getAllFacility"

    //OP Consolidated Report
    const val getLabConsolidatedOPReportTable =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getConsolidatedReportTable"
    const val getLabConsolidatedOPReportLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getConsolidatedReportLabel"
    const val getDistrictOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getDistrictDropdownFilter"
    const val getHUDOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getHudDropdown"
    const val getBlockOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getBlockDropdown"
    const val getOfficeOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getHealthOfficeDropdown"
    const val getDepartmentOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getConsolidatedReportTotalTestLabel"
    const val getInstitutionOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getInstitutionDropdown"
    const val getInstitutionTypeOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getInstitutionTypeDropdown"
    const val getTestNameOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getTestNameDropdown"
    const val getLabNameOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getLabNameDropdown"
    const val getGenderOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getGenderDropdown"
    const val getStatusOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getOrderStatusDropdown"

    //IP Consolidated Report
    const val getLabConsolidatedIPReportTable =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getConsolidatedReportTable"
    const val getLabConsolidatedIPReportLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getConsolidatedReportLabel"
    const val getDistrictIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getDistrictDropdownFilter"
    const val getHUDIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getHudDropdown"
    const val getBlockIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getBlockDropdown"
    const val getOfficeIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getHealthOfficeDropdown"
    const val getDepartmentIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getConsolidatedReportTotalTestLabel"
    const val getInstitutionIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getInstitutionDropdown"
    const val getInstitutionTypeIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getInstitutionTypeDropdown"
    const val getTestNameIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getTestNameDropdown"
    const val getLabNameIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getLabNameDropdown"
    const val getGenderIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getGenderDropdown"
    const val getStatusIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipconsolidatedReport/getOrderStatusDropdown"

    //OP Lab Wise Report
    const val getLabWiseReportOPTable =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getLabwiseReportTable"
    const val getLabWiseReportOPLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getLabwiseReportLabel"
    const val getLabWiseDistrictOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getDistrictDropdownFilter"
    const val getLabWiseHUDOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getHudDropdown"
    const val getLabWiseBlockOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getBlockDropdown"
    const val getLabWiseOfficeOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getHealthOfficeDropdown"
    const val getLabWiseInstitutionOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getInstitutionDropdown"
    const val getLabWiseInstitutionTypeOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getInstitutionTypeDropdown"
    const val getLabWiseTestNameOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getTestNameDropdown"
    const val getLabWiseLabNameOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getLabNameDropdown"
    const val getLabWiseGenderOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getGenderDropdown"
    const val getLabWiseStatusOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseReport/getOrderStatusDropdown"

    //IP Lab Wise Report
    const val getLabWiseReportIPTable =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getLabwiseReportTable"
    const val getLabWiseReportIPLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getLabwiseReportLabel"
    const val getLabWiseDistrictIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getDistrictDropdownFilter"
    const val getLabWiseHUDIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getHudDropdown"
    const val getLabWiseBlockIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getBlockDropdown"
    const val getLabWiseOfficeIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getHealthOfficeDropdown"
    const val getLabWiseInstitutionIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getInstitutionDropdown"
    const val getLabWiseInstitutionTypeIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getInstitutionTypeDropdown"
    const val getLabWiseTestNamIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getTestNameDropdown"
    const val getLabWiseLabNameIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getLabNameDropdown"
    const val getLabWiseGenderIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getGenderDropdown"
    const val getLabWiseStatusIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseReport/getOrderStatusDropdown"


    //OP Lab Test Wise Report
    const val getLabTestWiseOPReportTable =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getLabwiseTestReportTable"
    const val getLabTestWiseOPReportLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getLabwiseTestReportLabel"
    const val getLabTestWiseDistrictOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getDistrictDropdownFilter"
    const val getLabTestWiseHUDOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getHudDropdown"
    const val getLabTestWiseBlockOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getBlockDropdown"
    const val getLabTestWiseOfficeOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getHealthOfficeDropdown"
    const val getLabTestWiseInstitutionOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getInstitutionDropdown"
    const val getLabTestWiseInstitutionTypeOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getInstitutionTypeDropdown"
    const val getLabTestWiseTestNameOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getTestNameDropdown"
    const val getLabTestWiseLabNameOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getLabNameDropdown"
    const val getLabTestWiseGenderOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getGenderDropdown"
    const val getLabTestWiseStatusOPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabwiseTestReport/getOrderStatusDropdown"


    //IP Lab Test Wise Report
    const val getLabTestWiseIPReportTable =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getLabwiseTestReportTable"
    const val getLabTestWiseIPReportLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getLabwiseTestReportLabel"
    const val getLabTestWiseDistrictIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getDistrictDropdownFilter"
    const val getLabTestWiseHUDIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getHudDropdown"
    const val getLabTestWiseBlockIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getBlockDropdown"
    const val getLabTestWiseOfficeIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getHealthOfficeDropdown"
    const val getLabTestWiseInstitutionIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getInstitutionDropdown"
    const val getLabTestWiseInstitutionTypeIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getInstitutionTypeDropdown"
    const val getLabTestWiseTestNameIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getTestNameDropdown"
    const val getLabTestWiseLabNameIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getLabNameDropdown"
    const val getLabTestWiseGenderIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getGenderDropdown"
    const val getLabTestWiseStatusIPSpinnerList =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/iplabwiseTestReport/getOrderStatusDropdown"


    //Session Wise Report
    const val getSessionDistrictSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getDistrictDropdownFilter"
    const val getSessionHUDSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getHudDropdown"
    const val getSessionBlockSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getBlockDropdown"
    const val getSessionOfficeSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getHealthOfficeDropdown"
    const val getSessionInsitutionSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getInstitutionDropdown"
    const val getSessionInsitutionTypeSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getInstitutionTypeDropdown"
    const val getSessionGenderSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getGenderDropdown"
    const val getSessionSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getSessionDropdown"
    const val getSessionReportLable =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getOpCensusChartLabel"
    const val getSessionReportChart =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getOpCensusChart"
    const val getSessionReportChartWithTime =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getOpCensusChartWithTime"
    const val getSessionReportSummary =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getOpCensusSession"
    const val getSessionReportDetail =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusSession/getOpCensusSessionDetails"

    //Day Wise Patients List
    const val getDayWisePatientsListReportLable =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opDaywisePatientNameList/getOpDaywisePatientNameListChartLabel"
    const val getDayWisePatientsListReportChart =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opDaywisePatientNameList/getOpDaywisePatientNameListChart"
    const val getDayWisePatientsListReportChartWithTime =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opDaywisePatientNameList/getOpDaywisePatientNameListChartWithTime"
    const val getDayWisePatientsListReportSummary =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opDaywisePatientNameList/getOpDaywisePatientNameListTable"
    const val getDayWisePatientsListReportDetail =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opDaywisePatientNameList/getOpDaywisePatientNameListDetails"


    //Date Wise Report
    const val getOpCensusDate =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getOpCensusDate"
    const val getOpCensusDateWiseDetails =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getOpCensusDateWiseDetails"
    const val getDateDistrictSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getDistrictDropdownFilter"
    const val getDateHUDSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getHudDropdown"
    const val getDateBlockSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getBlockDropdown"
    const val getDateOfficeSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getHealthOfficeDropdown"
    const val getDateInsitutionSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getInstitutionDropdown"
    const val getDateInsitutionTypeSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getInstitutionTypeDropdown"
    const val getDateGenderSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getGenderDropdown"
    const val getDateSpinnerList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getSessionDropdown"
    const val getDatenReportLable =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getOpCensusChartLabel"
    const val getDateReportChart =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getOpCensusChart"
    const val getDateReportChartWithTime =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDate/getOpCensusChartWithTime"


    //DischargeSummary Count Wise Report
    const val getDischargeSummaryTable =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getDischargeSummaryTable"
    const val getDischargeSummaryDistrictSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getDistrictDropdownFilter"
    const val getDischargeSummaryHUDSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getHudDropdown"
    const val getDischargeSummaryBlockSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getBlockDropdown"
    const val getDischargeSummaryOfficeSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getHealthOfficeDropdown"
    const val getDischargeSummaryInsitutionSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getInstitutionDropdown"
    const val getDischargeSummaryInsitutionTypeSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getInstitutionTypeDropdown"
    const val getDischargeSummaryGenderSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getGenderDropdown"
    const val getDischargeSummarySpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getSessionDropdown"


    //Admission Day Wise Report
    const val getIPDayWisePatients =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getIPDayWisePatients"
    const val getIPDayWisePatientsDetail =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getIPDayWisePatientsCensusDetail"
    const val getAdmissionDayDistrictSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getDistrictDropdownFilter"
    const val getAdmissionDayHUDSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getHudDropdown"
    const val getAdmissionDayBlockSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getBlockDropdown"
    const val getAdmissionDayOfficeSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getHealthOfficeDropdown"
    const val getAdmissionDayInsitutionSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getInstitutionDropdown"
    const val getAdmissionDayInsitutionTypeSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getInstitutionTypeDropdown"
    const val getAdmissionDayGenderSpinnerList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getGenderDropdown"
    const val getAdmissionDaynReportLable =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getIPDayWisePatientsChartLabel"
    const val getAdmissionDayReportChartWithTime =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getIPDayWisePatientsWiseChartWithTime"
    const val getAdmissionDayReportChart =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipdayWisePatients/getIPDayWisePatientsWiseChart"

    //DistrictWise Patient OP

    const val getDistPatientCountReportLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getDistrictWisePatientCountLabel"
    const val getDistPatientCountReportTabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getDistrictWisePatientCountTable"
    const val getDistrictHudSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getHudDropdown"
    const val getDistrictBlockSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getBlockDropdown"
    const val getDistrictOfficeSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getHealthOfficeDropdown"
    const val getDistrictInstituteTypeSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getInstitutionTypeDropdown"
    const val getDistrictInstituteSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getInstitutionDropdown"
    const val getDistrictGenderSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getGenderDropdown"
    const val getDistrictDropDownFilterSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getDistrictDropdownFilter"
    const val getDistrictLabNameSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getLabNameDropdown"
    const val getDistrictLTestNameSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getTestNameDropdown"
    const val getDistrictStatusSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWisePatient/getOrderStatusDropdown"


    //DistrictWise Patient Ip

    const val getDistPatientCountReportLabelIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getDistrictWisePatientCountLabel"
    const val getDistPatientCountReportTabelIP =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getDistrictWisePatientCountTable"
    const val getDistrictHudSpinnerIP =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getHudDropdown"
    const val getDistrictBlockSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getBlockDropdown"
    const val getDistrictOfficeSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getHealthOfficeDropdown"
    const val getDistrictInstituteTypeSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getInstitutionTypeDropdown"
    const val getDistrictInstituteSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getInstitutionDropdown"
    const val getDistrictGenderSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getGenderDropdown"
    const val getDistrictDropDownFilterSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getDistrictDropdownFilter"
    const val getDistrictLabNameSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getLabNameDropdown"
    const val getDistrictLTestNameSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWisePatient/getTestNameDropdown"
    const val getDistrictStatusSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getOrderStatusDropdown"


    //districtTest Op

    const val getDistTestHudSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getHudDropdown"
    const val getDistTestBlockSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getBlockDropdown"
    const val getDistTestOfficeSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getHealthOfficeDropdown"
    const val getDistTestInstituteSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getInstitutionDropdown"
    const val getDistTestInstituteTypeSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getInstitutionTypeDropdown"
    const val getDistTestGenderSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getGenderDropdown"
    const val getDistTestDropdownSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getDistrictDropdownFilter"
    const val getDistTestLabNameSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getLabNameDropdown"
    const val getDistrictTestNameSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getTestNameDropdown"
    const val getDistrictTestOrderStatusSpinner =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getOrderStatusDropdown"
    const val getDistrictTestCountLabel =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getDistrictWiseTestCountLabel"
    const val getDistrictTestCountTable =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opdistrictWiseTestCount/getDistrictWiseTestCountTable"


    //districtTest Ip

    const val getDistTestHudSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getHudDropdown"
    const val getDistTestBlockSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getBlockDropdown"
    const val getDistTestOfficeSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getHealthOfficeDropdown"
    const val getDistTestInstituteSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getInstitutionDropdown"
    const val getDistTestInstituteTypeSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getInstitutionTypeDropdown"
    const val getDistTestGenderSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getGenderDropdown"
    const val getDistTestDropdownSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getDistrictDropdownFilter"
    const val getDistTestLabNameSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getLabNameDropdown"
    const val getDistrictTestNameSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getTestNameDropdown"
    const val getDistrictTestOrderStatusSpinnerIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getOrderStatusDropdown"
    const val getDistrictTestCountLabelIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getDistrictWiseTestCountLabel"
    const val getDistrictTestCountTableIp =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/ipdistrictWiseTestCount/getDistrictWiseTestCountTable"


    const val orderProcess =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/orderProcessSave"
    const val getUserProfile =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/getUserProfile"
    const val getSampleAcceptance =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/ordersampleacceptancetestwise"
    const val getRmisSampleAcceptance =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorderdetails/ordersampleacceptancetestwise"
    const val getLabTestApproval =
        BASE_DOMAIN + "HMIS-LIS/v1/api/viewlabtest/getviewlabtest"


    const val getNewTickets =
        BASE_DOMAIN + "Helpdesk/v1/api/helpDeskTicket/getNewTicket"
    const val getTicketById =
        BASE_DOMAIN + "Helpdesk/v1/api/helpDeskTicket/getNewTicketById"
    const val getTicketCount =
        BASE_DOMAIN + "Helpdesk/v1/api/helpDeskTicket/TicketStatusCount"
    const val addNewTicket =
        BASE_DOMAIN + "Helpdesk/v1/api/helpDeskTicket/addAppUserTicket"
    const val getFacilityByUUID =
        BASE_DOMAIN + "Appmaster/v1/api/facility/getFacilityByuuid"
    const val getAsset =
        BASE_DOMAIN + "assetmanagement/v1/api/asset/getAllAssets"
    const val getDepartment =
        BASE_DOMAIN + "Appmaster/v1/api/facilityDepartment/getDepartmentByFacilityId"

    const val getCategory =
        BASE_DOMAIN + "Helpdesk/v1/api/newReference/getall"
    const val getVendor =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/vendor/getVendor"
    const val getVendorByMobile =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/vendor/getVendorByMobile"
    const val getTicketUserProfile =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/getUserProfile"
    const val getPatientVisitInfo =
        BASE_DOMAIN + "registration/v1/api/patient/getPatientVisitsInfo"
    const val getTutorial =
        BASE_DOMAIN + "Appmaster/v1/api/tutorials/getTutorial"
    const val rapidSave =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/saveandapproval"
    const val getApprovalResultSpinner =
        BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getReference"
    const val GetDocumentType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patientattachments/getattachmenttype"
    const val GetAddDocumentType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patientattachments/getAllAttachments"
    const val GetUploadFile =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patientattachments/upload"
    const val GetDownload =
        BASE_DOMAIN + "Fileserver/v1/api/file/read"
    const val DeleteAttachmentsRows =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patientattachments/deleteAttachmentDetails"
    const val GetAdmissionWardList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getWardByLoggedInFacilityWithDept"
    const val GetReason =
        BASE_DOMAIN + "HMIS-EMR/v1/api/referal-reasons/getReferralReasons"
    const val GetTransmissionReason =
        BASE_DOMAIN + "HMIS-EMR/v1/api//transfer-reasons/getTransferReasons"
    const val GetNextOrder =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-referral/createPatientReferral"
    const val SaveTransfferOrder =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-transfer/create"
    const val SaveDischargeOrder =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/dischargeadvice"
    const val AdmissionSave =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-referral/createPatientReferral"
    const val getTreatmentFavourites =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getTreatmentKitFavourite"
    const val updateHistoryDiagnosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-diagnosis/updatePatientDiagnosis"
    const val investigationResult =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientorders/getradioandinvestigationpatientorder"
    const val GetNoteTemplate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/notetemplate/getNoteTemplate"
    const val GetTemplateItem =
        BASE_DOMAIN + "HMIS-EMR/v1/api/notetemplate/getNoteTemplateById"
    const val GetSaveScertificate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/certificates/create"
    const val GetCertificateAll =
        BASE_DOMAIN + "HMIS-EMR/v1/api/certificates/GetAll?"
    const val EmrInvestigationpost =
        BASE_DOMAIN + "HMIS-INV/v1/api/patientorders/postpatientorder"
    const val getAll =
        BASE_DOMAIN + "Appmaster/v1/api/newReference/getAll"

    //Lab Consolidated Test Wise Report
    const val getConsolidatedTestWiseServerTime =
        BASE_DOMAIN + "HMIS-LIS/v1/api/viewlabtest/getServerTime"
    const val getConsolidatedTestWiseInstitutionDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getInstitutionDropdown"
    const val getConsolidatedTestWiseHealthOfficeDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getHealthOfficeDropdown"
    const val getConsolidatedTestWiseInstitutionTypeDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getInstitutionTypeDropdown"
    const val getConsolidatedTestWiseBlockDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getBlockDropdown"
    const val getConsolidatedTestWiseHudDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getHudDropdown"
    const val getConsolidatedTestWiseDistrictDropdownFilter =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getDistrictDropdownFilter"
    const val getConsolidatedTestWiseInstitutionCategory =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/opconsolidatedReport/getInstitutionCategory"
    const val getConsolidatedTestWiseDepartmentDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getDepartmentDropdown"
    const val getConsolidatedTestWiseTestNameDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getTestNameDropdown"
    const val getConsolidatedTestWiseLabNameDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getLabNameDropdown"
    const val getConsolidatedTestWiseOrderStatusDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getOrderStatusDropdown"
    const val getConsolidatedTestWiseGenderDropdown =
        BASE_DOMAIN + "HMISReports_OP_Reports/v1/api/oplabConsolidateTestwiseReport/getGenderDropdown"

    //IP Admission
    const val getAdmissionList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/getadmission"
    const val deleteAdmissionList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/delete"

    const val getAllActiveCountry =
        BASE_DOMAIN + "Appmaster/v1/api/country/getAllActive"
    const val getByCountryId =
        BASE_DOMAIN + "Appmaster/v1/api/state/getByCountryId"

    //dispatch
    const val sampledispatch =
        BASE_DOMAIN + "HMIS-LIS/v1/api/sampletransportbatch/dispatchsampletransport"

    //Sample  Accepted
    const val sampleAccepted =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/ordersampleacceptancetestwise"

    // diresct approve
    const val DirectApprovel =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/saveandapproval"
    const val getAllBlood =
        BASE_DOMAIN + "Bloodbank/v1/api/newReference/getall"
    const val getPreviousBloodRequest =
        BASE_DOMAIN + "Bloodbank/v1/api/bloodRequest/getpreviousbloodRequestbyID"
    const val bloodRequestSave =
        BASE_DOMAIN + "Bloodbank/v1/api/bloodRequest/postbloodrequest"
    const val getProgressNotes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/progress/getAll"
    const val getEncounterByDocAndPatientId =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
    const val createProgressNotes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/progress/create"
    const val editProgressNote =
        BASE_DOMAIN + "HMIS-EMR/v1/api/progress/getById"
    const val updateProgressNotes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/progress/update"
    const val deleteProgressNotes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/progress/delete"


    const val getDietAllDepartment =
        BASE_DOMAIN + "Appmaster/v1/api/department/getAllDepartment"
    const val DeleteDietRows =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/delete"
    const val GetDietMasterCategory =
        BASE_DOMAIN + "HMIS-DIET_KITCHEN/v1/api/dietmaster/getDietMasterCategory"
    const val GetDietMasterFrequency =
        BASE_DOMAIN + "HMIS-DIET_KITCHEN/v1/api/dietmaster/getDietFrequency"
    const val GetDietSearchResult =
        BASE_DOMAIN + "HMIS-DIET_KITCHEN/v1/api/dietmaster/getDietMasterBySearch"

    //SpecialitySketchgrade_name
    const val GetSpecialitySketchFavorites =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getFavourite"
    const val DeleteSpecialitySketchFavorites =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/delete"
    const val GetPreviousSpecialitySketch =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-speciality-sketch/get-prev-by-patient"
    const val GetSpecialitySketchSearchResult =
        BASE_DOMAIN + "HMIS-EMR/v1/api/speciality/getAllSpeciality"

    const val GetSpecialitySketchIdResult =
        BASE_DOMAIN + "HMIS-EMR/v1/api/speciality/getSpecialityById"

    const val AddSpecialitySketchFavrt =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create"
    const val GetSpecialitySketchFavrtList =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getFavouriteById"

    const val saveSpeciality =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-speciality-sketch/create"

    //Bed Status Hospital Wise Report
    const val getBedStatusWiseLable =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getBedStatusHospitalwiseLabel"
    const val getBedStatusOccupiedDetails =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getBedStatusHospitalwiseoccupiedDetail"
    const val getBedStatusWiseTable =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getBedStatusHospitalwiseTable"
    const val getBedStatusHospitalDistrict =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getDistrictDropdownFilter"
    const val getBedStatusHospitalInstitution =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getInstitutionDropdown"

    //RMIS Dashboard
    const val getRMISDashboard =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientordertestdetails/getdashboarddetailstotalcount"
    const val getRMISDashboardChart =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientordertestdetails/getdashboardtestdetailsgroupbyreqdate"
    const val getRMISPatients =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter-type/getEncounterType"
    const val getRMISGender =
        BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"
    const val getRMISDepartment =
        BASE_DOMAIN + "Appmaster/v1/api/department/getAllLABDepartments"
    const val getRMISDashboardList =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientordertestdetails/getdashboarddetailsgroupbyreqdate"


    //Pharmacy Dashboard
    const val getPharmacyDashboard =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDashboard/pharmacyDashboard"
    const val getPharmacyChartValues =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDashboard/dateBasedTrendChart"
    const val getTopMovedDrugs =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDashboard/topMovedDrugCount"
    const val getZeroStockDrugs =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDashboard/zeroStockCount"
    const val getNonMovedDrugs =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDashboard/nonMovingDrugCount"
    const val getLowStockDrugs =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDashboard/lowStockCount"

    //TreatmentNameSearch
    const val TreatmentSearchName =
        BASE_DOMAIN + "HMIS-EMR/v1/api/treatment-kit/autoSearch"

    //TreatmentFavAdd
    const val AddFavTreatment =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=treatment"

    //TreatmentAddedFavouriteListView
    const val AddedFavListView =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getTreatmentKitFavouriteById"

    //TreatmentFavAddToList
    const val AddFavtoList =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getTreatmentKitFavouriteById"

    const val upfsteTreatmentKitOnModify =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-treatment/updatePreviousOrder?patient_uuid=1410440"

    //Diet url
    const val GetDietSearch =
        BASE_DOMAIN + "HMIS-DIET_KITCHEN/v1/api/dietmaster/getDietMasterBySearch"

    // DietCreate
    const val GetDietFavddAll =
        BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=diet"
    const val getPreviousDietOrder =
        BASE_DOMAIN + "HMIS-DIET_KITCHEN/v1/api/dietorder/getDietPrevoiusOrderById"

    //Nurse desk Diet
    const val getNurseDeskDiet =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/diet/getPatientDietDetails"
    const val updateDietStatus =
        BASE_DOMAIN + "HMIS-DIET_KITCHEN/v1/api/dietorder/updateDietOrderStatusById"

    //Nurse Desk Prescription
    const val getNurseDeskPrescription =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/prescription/getIPPatientPrescriptions"
    const val saveNurseDeskPrescription =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/administeredPrescription"


    //Dispense
    const val getPatientById =
        BASE_DOMAIN + "registration/v1/api/patient/getById"
    const val getPatientSearch =
        BASE_DOMAIN + "registration/v1/api/patient/search"
    const val getPharmacyDispanceList =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/getPrescriptionIdsByPatientId"

    const val getPatientPrescriptionById =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/getPatientPendingPrescriptionById"

    const val saveDispence =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDispenses/addPharmacyDispense"

    const val getPatientPrescription =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/getPatientPendingPrescriptions"
    const val getDispensePrescription =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDispenses/getPharmacyDispenseById"
    const val getPreviousDispense =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyDispenses/getDispenseByPatientId"
    const val getPreviousReturn =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyReturn/getReturnByPatientId"
    const val getReturnPrescription =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyReturn/getPharmacyReturnById"
    const val getSaveReturn =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/pharmacyReturn/addPharmacyReturn"


    //Dashboard
    const val getSession =
        BASE_DOMAIN + "Appmaster/v1/api/session/getSession"
    const val getGender =
        BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"
    const val getDoctorName =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/getAllDoctorsByFacilityId"
    const val getOrderStatus =
        BASE_DOMAIN + "HMIS-LIS/v1/api/ordertat/getorderstatus"
    const val getRMISOrderStatus =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/ordertat/getorderstatus"

    //ip case sheet
    const val ipCaseSheetGetAllProfileTypes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/get-notes-by-type"
    const val ipCaseSheetGetById =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getById"
    const val ipCaseSheetGetEncounter =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
    const val ipCaseSheetSaveAns =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/add"
    const val ipCaseSheetGetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getProfilesDefault"
    const val ipCaseSheetSetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/setProfilesDefault"
    const val ipCaseSheetGetPrevRecords =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-prev-by-patient"
    const val ipCaseSheetGetObservedValues =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-patient-by-patientId"
    const val ipCaseSheetAddConsultations =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/addConsultations"

    //op notes
    const val opNotesGetAllProfileTypes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/get-notes-by-type"
    const val opNotesGetById =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getById"
    const val opNotesGetEncounter =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
    const val opNotesSaveAns =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/add"
    const val opNotesGetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getProfilesDefault"
    const val opNotesSetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/setProfilesDefault"
    const val opNotesGetPrevRecords =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-prev-by-patient"
    const val opNotesGetObservedValues =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-patient-by-patientId"
    const val opNotesAddConsultations =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/addConsultations"

    //ot notes
    const val otNotesGetAllProfileTypes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/get-notes-by-type"
    const val otNotesGetById =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getById"
    const val otNotesGetEncounter =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
    const val otNotesSaveAns =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/add"
    const val otNotesGetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getProfilesDefault"
    const val otNotesSetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/setProfilesDefault"
    const val otNotesGetPrevRecords =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-prev-by-patient"
    const val otNotesGetObservedValues =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-patient-by-patientId"
    const val otNotesAddConsultations =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/addConsultations"

    //anesthesia notes
    const val anesthesiaNotesGetAllProfileTypes =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/get-notes-by-type"
    const val anesthesiaNotesGetById =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getById"
    const val anesthesiaNotesGetEncounter =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
    const val anesthesiaNotesSaveAns =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/add"
    const val anesthesiaNotesGetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/getProfilesDefault"
    const val anesthesiaNotesSetDefault =
        BASE_DOMAIN + "HMIS-EMR/v1/api/profiles/setProfilesDefault"
    const val anesthesiaNotesGetPrevRecords =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-prev-by-patient"
    const val anesthesiaNotesGetObservedValues =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/get-patient-by-patientId"
    const val anaesthesiaNotesAddConsultations =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-notes/addConsultations"

    //Critical Care Chart
    const val updateCriticalCareChartConfig =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-ccc-settings/update"

    const val getCriticalCareChartFilterHeadings =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-ccc-settings/getById"
    const val getCriticalCareChartHeadings =
        BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"
    const val getCriticalCareChartMaster =
        BASE_DOMAIN + "HMIS-EMR/v1/api/ccc/getcccmasterbytype"
    const val getCriticalCareChartByPatientId =
        BASE_DOMAIN + "HMIS-EMR/v1/api/CC-charts/getCCCbypatientid"
    const val getCriticalCareChartEncounter =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
    const val postCriticalCareChartCreate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/CC-charts/create"
    const val postCriticalCareChartUpdate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/CC-charts/updateCCCbypatientid"
    const val getCriticalCareChartCompareData =
        BASE_DOMAIN + "HMIS-EMR/v1/api/CC-charts/getCCCcomparedata"

    const val GetDischargeSummaryList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_dischargeSummary/getDischargeSummary"

    const val DischargeSummaryRevert =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/dischargeSummary/revert"

    const val getResultDispatch =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorderdetails/getresultdispatchlist"

    const val getRmisResultDispatch =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientworkorderdetails/getresultdispatchlist"


    const val resultPDF =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorderdetails/printauthorizedlabresults"

    //Discharge Summary
    const val GetDischargePreviousData =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/dischargeSummary/getdsdata"
    const val GetDischargeType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/discharge-summary/getdischargetype"
    const val GetDischargeDeathType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/discharge-summary/getdeathtype"
    const val GetDischargeNoteTemplate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/notetemplate/getNoteTemplate"
    const val GetDefaultTemplate =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/dischargeSummary/getDefaultTemplate"
    const val SetDefaultTemplate =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/dischargeSummary/setDefaultTemplate"
    const val GetDischargeSummaryDoctorName =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/userSearchByUserType"

    const val GetMrdList =
        BASE_DOMAIN + "MRD/v1/api/mrd/casesheet/getcasesheetsummary"

    const val getcasesheetsummary =
        BASE_DOMAIN + "MRD/v1/api/mrd/casesheet/getcasesheet"

    const val GetMrdPrintList =
        BASE_DOMAIN + "MRD/v1/api/mrd/casesheet/printCaseSheet"

    const val Session =
        BASE_DOMAIN + "Appmaster/v1/api/facilitySettings/getFacilitySettingByFId"
    const val ActivitySession =
        BASE_DOMAIN + "Appmaster/v1/api/session/getActiveSessions"

    //IPGETPATIENT
    const val GetAdmittedPatient =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/getadmission"

    //Vitals
    const val getVitalSearchName =
        BASE_DOMAIN + "HMIS-EMR/v1/api/vitalMaster/getALLVitalsFilter"

    const val getPrevPatientVital =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-patient-vitals/getPreviousPatientVitals"

    //HistoryAdmission
    const val getHistoryAdmission =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-referral/getReferralHistory"
    const val immunicationUpdate =
        BASE_DOMAIN + "HMIS-EMR/v1/api/immunization/update"

    //Ot-Schedule
    const val getOtName =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/otm/otmaster/getotmaster"
    const val getOtSurguryName =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/otm/otsurgicalmapping/getsurgerynamebyids"
    const val getOtType =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/commonreferencegroup/getReference"
    const val getOtScheduleList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/otm/otschedules/getotschedulecalendarsearch"
    const val getCheif =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/userSearchByUserType"
    const val getDiagnosis =
        BASE_DOMAIN + "HMIS-EMR/v1/api/diagnosis/search"
    const val getDischagePDF =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_dischargeSummary/printPreviousDischargeSummary"

    const val getDischargeSave =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/dischargeSummary/savedraftdischargesummary"

    const val getDischargeApproval =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/dischargeSummary/save"

    const val getDischargeRequestApproval =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_dischargeSummary/getDischargeSummaryById"

    const val getDataTemplateInfo =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_dischargeSummary/getDataTemplateInfo"


    const val saveSurgery =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/otm/otschedules/saveotschedule"
    const val oldPatient =
        BASE_DOMAIN + "registration/v1/api/patient/old-patient-search"

    const val getImage =
        BASE_DOMAIN + "Fileserver/v1/api/file/read"
    const val deleteTutorial =
        BASE_DOMAIN + "Appmaster/v1/api/tutorials/deleteTutorial"
    const val updateDownloadCount =
        BASE_DOMAIN + "Appmaster/v1/api/tutorials/updateviewanddownloadCount"
    const val getRoleControlActivityCode =
        BASE_DOMAIN + "Appmaster/v1/api/roleControlActivity/getRoleControlActivityByRoleandActivityCode"
    const val viewOtSchedule =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/otm/otschedules/viewotschedule"
    const val modifyOtSchedule =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/otm/otschedules/updateotschedule"
    const val deleteOtSchedule =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/otm/otschedules/deleteotschedule"

    //get latest encounter
    const val getLatestRecord =
        BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/get-latest-enc-by-patient"
    const val getPatientByIdUrl =
        BASE_DOMAIN + "registration/v1/api/patient/getById"
    const val GetUOMValue =
        BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"
    const val GetADmissionDischargeType =
        BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"

    //edit history
    const val getAlleryEdit =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-allergy/getPatientAllergiesById"
    const val getImmunizationEdit = BASE_DOMAIN + "HMIS-EMR/v1/api/immunization/getById"
    const val getSurgeryEdit =
        BASE_DOMAIN + "HMIS-EMR/v1/api/surgery-history/getSurgeryById"

    /*Nurse Desk*/
    const val GetWardList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getwardbyloggedyinfacility"

    const val GetStoreList =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/storeMaster/getStoreMasterByFacilityId"

    const val GetNurseworkflow =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/work-flow/getworkflowsettingsByUserID"

    const val getBedmanagemt =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/getPatientList"

    //Bed Details
    const val getBedDetails =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/getwardRoomInfo"

    // Bed allocation
    const val bedAllocation =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/bed-allocation"

    //ward list
    const val getAllwardList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getActiveWardByIsCasuality"

    // Bed Transfer

    const val bedTransfer =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/patientBedTransfer"
    // Ward Transfer

    const val wardTransfer =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/patientWardTransfer"

    const val LabView = BASE_DOMAIN + "HMIS-EMR/v1/api/lab-result/getLabResultById"

    const val nursePatientLabDetails =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/lab/getPatientLabDetails"

    //nurseDeskInvestigation
    const val getNurseDeskInvestigation =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/investigation/getPatientInvestigationDetails?"
    const val getNurseDeskResultInvestigation =
        BASE_DOMAIN + "HMIS-INV/v1/api/patientworkorderdetails/getAuthorizedRadiologyResults"

    const val getNurseDeskResultLab =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorderdetails/getauthorizedlabresults"

    //nursePrevVitals
    const val getNursePrevVitals =
        BASE_DOMAIN + "HMIS-EMR/v1/api/emr-patient-vitals/getPreviousPatientVitals"

    //nurseDEskRadiologyREsult
    const val getNurseDeskResultRadiology =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/patientworkorderdetails/getAuthorizedRadiologyResults"

    //nurse desk critical care chart
    const val getNurseDeskCCCPatientList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/getPatientList"

    const val updateTicket =
        BASE_DOMAIN + "Helpdesk/v1/api/helpDeskTicket/updateNewTicket"

    const val getSaveVendor =
        BASE_DOMAIN + "Helpdesk/v1/api/helpDeskTicket/addNewTicket"

    const val getTKRadioType =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/commonReference/getReference"

    //Nurse Desk Notes
    const val getNurseDeskNotesPatientList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/getPatientList"


    //IP MANGEMENT DASHBOARD
    const val getIpDashBoardList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/dashboard/dashboardlist"

    const val getIpLineGraph =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/dashboard/linechart"

    const val getIpWardByFacilityID =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getWardByLoggedInFacilityWithDept"


    //nurseDeskDischargeSummary
    const val getNurseDeskDiscahrgeSummaryPatientList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_dischargeSummary/getPatientList"

    //Radiology nurse
    const val getNurseRadilogyData =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/radiology/getPatientRadiologyDetails"

    ///postipsamplecollection
    const val postipsamplecollection =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/ipsamplecollection/postipsamplecollection"


    //PostInves
    const val postInvestigationnurse =
        BASE_DOMAIN + "HMIS-INV/v1/api/ipsamplecollection/postipsamplecollection"

    //Labsamplecollection
    const val postsamplecollectionlabnurse =
        BASE_DOMAIN + "HMIS-LIS/v1/api/ipsamplecollection/postipsamplecollection"


    //InjectionWorkiList
    const val InjectionWorkiList =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/getInjectionWorkList"
    const val viewInjection =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/viewInjectionBatch"
    const val doAdministration =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/manageInjectioneMAR"

    const val InjectioType =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/storeMaster/getStoresByTypeId"

    //DateWiseSessionReport
    const val getDateWiseSessionLabel =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDateWiseSession/getOpCensusChartLabel"
    const val getCensusDateWiseSessionChart =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDateWiseSession/getOpCensusChart"
    const val getCensusDateWiseSessionChartWithTime =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDateWiseSession/getOpCensusChartWithTime"
    const val getCensusDateWiseSessionList =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/opCensusDateWiseSession/getOpCensusDateWiseSession"

    //Admission Discharge List
    const val getAdmissionDischargeList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getDischargeSummaryTable"
    const val getAdmissionDischargeDistrict =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getDistrictDropdownFilter"
    const val getAdmissionDischargeBlock =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getBlockDropdown"
    const val getAdmissionDischargeHUD =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getHudDropdown"
    const val getAdmissionDischargeOffice =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getHealthOfficeDropdown"
    const val getAdmissionDischargeInstitute =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getInstitutionDropdown"
    const val getAdmissionDischargeSession =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getSessionDropdown"
    const val getAdmissionDischargeGender =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/dischargeSummary/getGenderDropdown"


    //Admission Ward Wise Report
    const val getAdmissionWardLabel =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipwardWiseCensus/getIPWardWisePatientsChartLabel"
    const val getAdmissionWardChart =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipwardWiseCensus/getIPWardWisePatientsChart"
    const val getAdmissionWardChartWithTime =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipwardWiseCensus/getIPWardWisePatientsChartWithTime"
    const val getAdmissionWardList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipwardWiseCensus/getIPWardWisePatients"
    const val getAdmissionWardListDetail =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipwardWiseCensus/getIPAdmissionWardWiseCensusDetail"

    //Admission Doctor Wise Report
    const val getAdmissionDoctorLabel =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDoctorWise/getIPAdmissionDoctorWiseChartLabel"
    const val getAdmissionDoctorChart =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDoctorWise/getIPAdmissionDoctorWiseChart"
    const val getAdmissionDoctorChartWithTime =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDoctorWise/getIPAdmissionDoctorWiseChartWithTime"
    const val getAdmissionDoctorList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDoctorWise/getIPAdmissionDoctorWise"
    const val getAdmissionDoctorListDetail =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDoctorWise/getIPAdmissionDoctorWiseCensusDetail"

    //Admission State Level Report
    const val getAdmissionStateLabel =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionStateLevel/getIPAdmissionStateLevelChartLabel"
    const val getAdmissionStateChart =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionStateLevel/getIPAdmissionStateLevelChart"
    const val getAdmissionStateChartWithTime =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionStateLevel/getIPAdmissionStateLevelChartWithTime"
    const val getAdmissionStateList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionStateLevel/getIPAdmissionStateLevelCensus"
    const val getAdmissionStateListDetail =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionStateLevel/getIPAdmissionStateLevelCensusDetail"

    //Admission District Level Report
    const val getAdmissionDistrictLabel =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDistrictWise/getIPAdmissionDistrictWiseChartLabel"
    const val getAdmissionDistrictChart =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDistrictWise/getIPAdmissionDistrictWiseChart"
    const val getAdmissionDistrictChartWithTime =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDistrictWise/getIPAdmissionDistrictWiseChartWithTime"
    const val getAdmissionDistrictList =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDistrictWise/getIPAdmissionDistrictWise"
    const val getAdmissionDistrictListDetail =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/ipadmissionDistrictWise/getIPAdmissionDistrictWiseDetail"

    //MLC
    const val getEmergenyCasualty =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/casuality/getcasualty"

    const val getEmergenyCasualtyCaseTypeList =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/casuality/getcasetypelist"

    const val getEmergencySpinnerValuesCommon =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/commonreferencegroup/getReference"

    const val getEmergencySpinnerValuesRelationType =
        BASE_DOMAIN + "Appmaster/v1/api/commonReference/getReference"

    const val getEmergencySpinnerValuesGender =
        BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"

    const val getEmergencySpinnerValuesWard =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getActiveWardByIsCasuality"

    const val getEmergencySearch =
        BASE_DOMAIN + "registration/v1/api/patient/search"

    const val printCasualtyPaySlip =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/casuality/printcasualtyslip"

    const val getDepartmentById =
        BASE_DOMAIN + "Appmaster/v1/api/department/getDepartmentOnlyById"
    const val postSaveEmergencyCasualty =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/casuality/postcasuality"
    const val postUpdateEmergencyCasualty =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/casuality/updatecasuality"

    const val getRoleControlQuick =
        BASE_DOMAIN + "Appmaster/v1/api/facilityOpIpMap/getConfigByFacilityId"
    const val getWardMasterListData =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getWardView"

    //Ward Master
    //Add
    const val getWardSequenceNo =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getwardsequenceno"
    const val getWardGender =
        BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"
    const val getWardReferenceType =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/commonreferencegroup/getReference"
    const val getWardInstitution =
        BASE_DOMAIN + "Appmaster/v1/api/userFacility/getUserFacilityByUId"
    const val getWardLocationDetails =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/location/getlocationbyfacilitywithbuildingandblock"
    const val getWardFloorRoom =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/location/getlocationbyfacilitywithparentlocationId"
    const val getUpdateBedInfo =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getUpdateBedInfo"
    const val getWardDepartment =
        BASE_DOMAIN + "Appmaster/v1/api/facilityDepartment/getDepartmentByFacilityId"
    const val getWardRoomClassification =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/commonreferencegroup/getReference"

    const val createWard =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/create"

    //Update
    const val getWardRoomInfo =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/getwardRoomInfo"

    const val updateWardDetails =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/update"

    const val getWardStoresByType =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/storeMaster/getStoresByType"

    //View
    const val getWardById =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getWardById"

    //Bed Status Report
//        const val getInstitutionDropdown =
//            BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getInstitutionDropdown"

    const val getDistrictDropdownFilter =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getDistrictDropdownFilter"

    const val getBedStatusHospitalwiseLabel =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getBedStatusHospitalwiseLabel"

    const val getBedStatusHospitalwiseTable =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getBedStatusHospitalwiseTable"

    const val getWardMasterRoomImageUpload =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/room/adddocument"

    const val getDistrictDropdown =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getDistrictDropdownFilter"

    const val getInstitutionTypeDropdown =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getInstitutionTypeDropdown"

    const val getInstitutionCategoryDropdown =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getInstitutionCategory"

    const val getInstitutionDropdown =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getInstitutionDropdown"

    const val getDepartmentDropdown =
        BASE_DOMAIN + "HMISReports_IP_Reports/v1/api/bedstatushospitalwise/getDepartmentDropdown"

    //OPDAYWISEPATIENTLIST

    const val getDayWisePatientList =
        BASE_DOMAIN + "HMISReports_General/v1/api/covid19DayWisePatientlistRegistration/getCovid19DayWisePatientlistRegistration"
    const val getDayDistrictSpinner =
        BASE_DOMAIN + "HMISReports_General/v1/api/covid19DayWisePatientlistRegistration/getDistrictDropdownFilter"
    const val getDayInstitutionSpinner =
        BASE_DOMAIN + "HMISReports_General/v1/api/covid19DayWisePatientlistRegistration/getInstitutionDropdown"


    const val getWardStoreMapping =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/store/updateBulkStore_V2"
    const val getWardMasterFetchData =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/store/getStoreByWardId"
    const val deletewardmastelistdata =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/delete"


    // lis retest

    const val lmisretest =
        BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/orderProcessRetest"

    //Wardaster room setup api
    const val roomsetup =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/room/updateBulkRoomV2"


    // health office

    const val getHealthOfficeList =
        BASE_DOMAIN + "Appmaster/v1/api/healthOffice/getHealthOffice"

    const val getNurseDeskServerTime =
        BASE_DOMAIN + "HMIS-LIS/v1/api/viewlabtest/getServerTime"


    const val searchPatient =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/searchPatient"

    const val IpCorrection =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/updateIpCorrection"


    //ward

    const val getTranferListData =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_ward_transfer/wardTransferPatients"

    const val getTransferStatus =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_ward_transfer/wardTransferStatusUpdate"

    const val ReciveTranfer =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/nd_ward_transfer/wardReceivePatients"


    const val getApplicationRuleSettings =
        BASE_DOMAIN + "Appmaster/v1/api/applicationRuleSettings/getApplicationRuleSettings"

    const val getAllActiveConfigs =
        BASE_DOMAIN + "Appmaster/v1/api/title-config/getAllActiveConfigs"

    const val Login_session =
        BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/login_session"


    const val Logout_Session =
        BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/is_current_loginuser"


    const val getPrescriptionPDF =
        BASE_DOMAIN + "HMIS-INVENTORY/v1/api/prescriptions/printPrescription"

    const val getWardTypeDropdown =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/commonreferencegroup/getReference"
    const val getWardGenderDropdown =
        BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"
    const val getDepartmentByFacilityId =
        BASE_DOMAIN + "Appmaster/v1/api/facilityDepartment/getDepartmentByFacilityId"

    //Blue Print


    const val getBluePrintData =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getwardblueprint"


    //Departmentwise Reports
    const val getInstitutionDropdownRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getInstitutionDropdown"
    const val getHealthOfficeDropdownRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getHealthOfficeDropdown"
    const val getInstitutionTypeDropdownRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getInstitutionTypeDropdown"
    const val getBlockDropdownReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getBlockDropdown"
    const val getHudDropdownRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getHudDropdown"
    const val getDistrictDropdownFilterRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getDistrictDropdownFilter"
    const val getInstitutionCategoryRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getInstitutionCategory"
    const val getDepartmentDropdownRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getDepartmentDropdown"
    const val getGenderDropdownRegDepReports =
        BASE_DOMAIN + "HMISReports_Registration/v1/api/specialityCensus/getGenderDropdown"
    const val getSpecialityCensusChartLabelRegDepReports = BASE_DOMAIN +
            "HMISReports_Registration/v1/api/specialityCensus/getSpecialityCensusChartLabel"
    const val getSpecialityCensus = BASE_DOMAIN +
            "HMISReports_Registration/v1/api/specialityCensus/getSpecialityCensus"


    const val getSchema = BASE_DOMAIN +
            "Billing/v1/api/patientschemes/patientschemesdropdown"


    const val getSchemaName = BASE_DOMAIN +
            "Billing/v1/api/patientschemes/getpatientschemesbyid"


    const val getPatientAllVisits =
        BASE_DOMAIN + "registration/v1/api/patient/getPatientAllVisits"


    const val getPatientAllReferrals =
        BASE_DOMAIN + "registration/v1/api/patient/getPatientAllReferrals"

    const val getusersbyusertypeids =
        BASE_DOMAIN + "Appmaster/v1/api/userProfile/getusersbyusertypeids"
    const val getActiveWardByIsCasuality =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getActiveWardByIsCasuality"

    const val postWorkOrderAttachments =
        BASE_DOMAIN + "HMIS-RMIS/v1/api/woAttachmentRoutes/postWorkOrderAttachments"

    const val readUploadImage =
        BASE_DOMAIN + "Fileserver/v1/api/file/read"


    const val certificateDownload =
        BASE_DOMAIN + "HMIS-EMR/v1/api/certificates/printpreviouscertificates"


    const val getCurrentDateTime =
        BASE_DOMAIN + "HMIS-Login/1.0.0/api/homePages/current-date-time"

    const val getPatientAdmission =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-referral/getPatientReferral"

    const val getAllDepartments =
        BASE_DOMAIN + "Appmaster/v1/api/department/getAllDepartments"

    const val updateEMRAdmission =
        BASE_DOMAIN + "HMIS-EMR/v1/api/patient-referral/updatePatientIsAdmitted"

    const val updateAdmissionreq =
        BASE_DOMAIN + "HMIS-IP-Management/v1/api/admissionreq/update"

    const val departmentAutocomplete =
        BASE_DOMAIN + "Appmaster/v1/api/userDepartment/getConsultDepartmentsByfacilityId"

}
