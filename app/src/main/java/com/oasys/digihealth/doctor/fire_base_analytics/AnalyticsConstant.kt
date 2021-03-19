package com.oasys.digihealth.doctor.fire_base_analytics

class AnalyticsConstant {

    companion object {
        const val HMIS_MOBILE_LOGIN_VISIT = "HLMIS_M_LOGIN_VISIT"
        const val HMIS_LIMS_MOBILE_LOGIN_COMPLETE = "HLMIS_M_LOGIN_COMPLETE"
        const val HMIS_MOBILE_LOGIN_FAILURE = "HMIS_MOBILE_MOBILE_LOGIN_FAILURE"
        const val HMIS_LIMS_MOBILE_NEWORDER_VISIT = "HLMIS_M_ NEWORDER_VISIT"
        const val HMIS_LIMS_MOBILE_NEWORDER_SUCCESS = "HLMIS_M_NEWORDER_SUCCESS"

        //const val HMIS_LIMS_MOBILE_LABTEST_APPROVEL_VISIT ="HMIS_LIMS_MOBILE_LABTEST_APPROVEL_VISIT"
        const val HMIS_LIMS_MOBILE_LABTEST_SAVE = "HLMIS_M_LABTEST_SAVE"
        const val HMIS_LIMS_MOBILE_LABTEST_ORDERPROCESS_APPROVAL =
            "HLMIS_M_LABTEST_ORDERPROCESS_APPROVAL"
        const val HMIS_LIMS_MOBILE_TESTAPPROVAL_LABRESULTAPPROVAL_APPROVAL =
            "HLMIS_M_TESTAPPROVAL_LABRESULTAPPROVAL_APPROVAL"
        const val HMIS_LIMS_MOBILE_TESTPROCESS_ORDERPROCESS_APPROVAL =
            "HLMIS_M_TESTPROCESS_ORDERPROCESS_APPROVAL"


        //DASHBOARD
        const val HEMR_M_DASHBOARD_VISIT = "HEMR_M_DASHBOARD_VISIT"
        const val HEMR_M_OP_SEARCH_VISIT = "HEMR_M_OP_SEARCH_VISIT"
        const val HEMR_M_OP_MY_PATIENT_VISIT = "HEMR_M_OP_MY_PATIENT_VISIT"
        const val HEMR_M_IP_IP_VISIT = "HEMR_M_IP_IP_VISIT"
        const val HEMR_M_IP_DISCHARGE_VISIT = "HEMR_M_IP_DISCHARGE_VISIT"


        //COMMON FOR OP AND IP

        //  CONFRIGURATION
        const val HEMR_M_CONFIG_SAVE = "HEMR_M_CONFIG_SAVE"
        const val HEMR_M_CONFIG_VISIT = "HEMR_M_CONFIG_VISIT"

        //HISTORY_OP
        const val HEMR_M_HISTORY_VISIT_OP = "HEMR_M_HISTORY_VISIT_OP"

        //HISTORY_IP
        const val HEMR_M_HISTORY_VISIT_IP = "HEMR_M_HISTORY_VISIT_IP"

        // LAB_OP
        const val HEMR_M_LAB_VISIT_OP = "HEMR_M_LAB_VISIT_OP"
        const val HEMR_M_LAB_SAVE_START_OP = "HEMR_M_LAB_SAVE_START_OP"
        const val HEMR_M_LAB_SAVE_COMPLETE_OP = "HEMR_M_LAB_SAVE_COMPLETE_OP"
        const val HEMR_M_PREVIOS_LAB_VISIT_OP = "HEMR_M_PREVIOS_LAB_VISIT_OP"

        // LAB_IP
        const val HEMR_M_LAB_VISIT_IP = "HEMR_M_LAB_VISIT_IP"
        const val HEMR_M_LAB_SAVE_START_IP = "HEMR_M_LAB_SAVE_START_IP"
        const val HEMR_M_LAB_SAVE_COMPLETE_IP = "HEMR_M_LAB_SAVE_COMPLETE_IP"
        const val HEMR_M_PREVIOS_LAB_VISIT_IP = "HEMR_M_PREVIOS_LAB_VISIT_IP"

        // RADIOLOGY_OP
        const val HEMR_M_RADIOLOGY_VISIT_OP = "HEMR_M_RADIOLOGY_VISIT_OP"
        const val HEMR_M_RADIOLOGY_SAVE_START_OP = "HEMR_M_RADIOLOGY_SAVE_START_OP"
        const val HEMR_M_RADIOLOGY_SAVE_COMPLETE_OP = "HEMR_M_RADIOLOGY_SAVE_COMPLETE_OP"
        const val HEMR_M_PREVIOS_RADIOLOGY_VISIT_Op = "HEMR_M_PREVIOS_RADIOLOGY_VISIT_OP"

        // RADIOLOGY_IP
        const val HEMR_M_RADIOLOGY_VISIT_IP = "HEMR_M_RADIOLOGY_VISIT_IP"
        const val HEMR_M_RADIOLOGY_SAVE_START_IP = "HEMR_M_RADIOLOGY_SAVE_START_IP"
        const val HEMR_M_RADIOLOGY_SAVE_COMPLETE_IP = "HEMR_M_RADIOLOGY_SAVE_COMPLETE_IP"
        const val HEMR_M_PREVIOS_RADIOLOGY_VISIT_IP = "HEMR_M_PREVIOS_RADIOLOGY_VISIT_IP"

        //  PRESCREPTION_OP
        const val HEMR_M_PRESCREPTION_VISIT_OP = "HEMR_M_PRESCREPTION_VISIT_OP"
        const val HEMR_M_PRESCREPTION_SAVE_START_OP = "HEMR_M_PRESCREPTION_SAVE_START_OP"
        const val HEMR_M_PRESCREPTION_SAVE_COMPLETE_OP = "HEMR_M_PRESCREPTION_SAVE_COMPLETE_OP"
        const val HEMR_M_PREVIOS_PRESCREPTION_VISIT_OP = "HEMR_M_PREVIOS_PRESCREPTION_VISIT_OP"

        //  PRESCREPTION_IP
        const val HEMR_M_PRESCREPTION_VISIT_IP = "HEMR_M_PRESCREPTION_VISIT_IP"
        const val HEMR_M_PRESCREPTION_SAVE_START_IP = "HEMR_M_PRESCREPTION_SAVE_START_IP"
        const val HEMR_M_PRESCREPTION_SAVE_COMPLETE_IP = "HEMR_M_PRESCREPTION_SAVE_COMPLETE_IP"
        const val HEMR_M_PREVIOS_PRESCREPTION_VISIT_IP = "HEMR_M_PREVIOS_PRESCREPTION_VISIT_IP"

        //  CHIEF_COMPLIENTS_OP
        const val HEMR_M_CHIEFCOMPLIENTS_VISIT_OP = "HEMR_M_CHIEFCOMPLIENTS_VISIT_OP"
        const val HEMR_M_CHIEFCOMPLIENTS_SAVE_START_OP = "HEMR_M_CHIEFCOMPLIENTS_SAVE_START_OP"
        const val HEMR_M_CHIEFCOMPLIENTS_SAVE_COMPLETE_OP =
            "HEMR_M_CHIEFCOMPLIENTS_SAVE_COMPLETE_OP"
        const val HEMR_M_PREVIOS_CHIEFCOMPLIENTS_VISIT_OP =
            "HEMR_M_PREVIOS_CHIEFCOMPLIENTS_VISIT_OP"

        //  CHIEF_COMPLIENTS_IP
        const val HEMR_M_CHIEFCOMPLIENTS_VISIT_IP = "HEMR_M_CHIEFCOMPLIENTS_VISIT_IP"
        const val HEMR_M_CHIEFCOMPLIENTS_SAVE_START_IP = "HEMR_M_CHIEFCOMPLIENTS_SAVE_START_IP"
        const val HEMR_M_CHIEFCOMPLIENTS_SAVE_COMPLETE_IP =
            "HEMR_M_CHIEFCOMPLIENTS_SAVE_COMPLETE_IP"
        const val HEMR_M_PREVIOS_CHIEFCOMPLIENTS_VISIT_IP =
            "HEMR_M_PREVIOS_CHIEFCOMPLIENTS_VISIT_IP"

        //  TREATMENT KIT_OP
        const val HEMR_M_TK_VISIT_OP = "HEMR_M_TK_VISIT_OP"
        const val HEMR_M_TK_ORDER_START_OP = "HEMR_M_TK_ORDER_START_OP"
        const val HEMR_M_TK_ORDER_COMPLETE_OP = "HEMR_M_TK_ORDER_COMPLETE_OP"
        const val HEMR_M_TK_SAVE_ORDER_START_OP = "HEMR_M_TK_SAVE_ORDER_START_OP"
        const val HEMR_M_TK_SAVE_ORDER_COMPLETE_OP = "HEMR_M_TK_SAVE_ORDER_COMPLETE_OP"
        const val HEMR_M_PREVIOUS_TK_VISIT_OP = "HEMR_M_PREVIOUS_TK_VISIT_OP"


        //  TREATMENT KIT_IP
        const val HEMR_M_TK_VISIT_IP = "HEMR_M_TK_VISIT_IP"
        const val HEMR_M_TK_ORDER_START_IP = "HEMR_M_TK_ORDER_START_IP"
        const val HEMR_M_TK_ORDER_COMPLETE_IP = "HEMR_M_TK_ORDER_COMPLETE_IP"
        const val HEMR_M_TK_SAVE_ORDER_START_IP = "HEMR_M_TK_SAVE_ORDER_START_IP"
        const val HEMR_M_TK_SAVE_ORDER_COMPLETE_IP = "HEMR_M_TK_SAVE_ORDER_COMPLETE_IP"
        const val HEMR_M_PREVIOUS_TK_VISIT_IP = "HEMR_M_PREVIOUS_TK_VISIT_IP"

        //  VITALS_OP
        const val HEMR_M_VITALS_VISIT_OP = "HEMR_M_VITALS_VISIT_OP"
        const val HEMR_M_VITALS_SAVE_START_OP = "HEMR_M_VITALS_SAVE_START_OP"
        const val HEMR_M_VITALS_SAVE_COMPLETE_OP = "HEMR_M_VITALS_SAVE_COMPLETE_OP"

        //  VITALS_IP
        const val HEMR_M_VITALS_VISIT_IP = "HEMR_M_VITALS_VISIT_IP"
        const val HEMR_M_VITALS_SAVE_START_IP = "HEMR_M_VITALS_SAVE_START_IP"
        const val HEMR_M_VITALS_SAVE_COMPLETE_IP = "HEMR_M_VITALS_SAVE_COMPLETE_IP"

        //  INVESTIGATION_OP
        const val HEMR_M_INVESTIGATION_VISIT_OP = "HEMR_M_INVESTIGATION_VISIT_OP"
        const val HEMR_M_INVESTIGATION_SAVE_START_OP = "HEMR_M_INVESTIGATION_SAVE_START_OP"
        const val HEMR_M_INVESTIGATION_SAVE_COMPLETE_OP = "HEMR_M_INVESTIGATION_SAVE_COMPLETE_OP"
        const val HEMR_M_PREVIOUS_INVESTIGATION_VISIT_OP = "HEMR_M_PREVIOUS_INVESTIGATION_VISIT_OP"


        //  INVESTIGATION_IP
        const val HEMR_M_INVESTIGATION_VISIT_IP = "HEMR_M_INVESTIGATION_VISIT_IP"
        const val HEMR_M_INVESTIGATION_SAVE_START_IP = "HEMR_M_INVESTIGATION_SAVE_START_IP"
        const val HEMR_M_INVESTIGATION_SAVE_COMPLETE_IP = "HEMR_M_INVESTIGATION_SAVE_COMPLETE_IP"
        const val HEMR_M_PREVIOUS_INVESTIGATION_VISIT_IP = "HEMR_M_PREVIOUS_INVESTIGATION_VISIT_IP"

        //   DIGANOSIS_OP
        const val HEMR_M_DIGANOSIS_VISIT_OP = "HEMR_M_DIGANOSIS_VISIT_OP"
        const val HEMR_M_DIGANOSIS_SAVE_START_OP = "HEMR_M_DIGANOSIS_SAVE_START_OP"
        const val HEMR_M_DIGANOSIS_SAVE_COMPLETE_OP = "HEMR_M_DIGANOSIS_SAVE_COMPLETE_OP"
        const val HEMR_M_PREVIOUS_DIGANOSIS_VISIT_OP = "HEMR_M_PREVIOUS_DIGANOSIS_VISIT_OP"

        //   DIGANOSIS_IP
        const val HEMR_M_DIGANOSIS_VISIT_IP = "HEMR_M_DIGANOSIS_VISIT_IP"
        const val HEMR_M_DIGANOSIS_SAVE_START_IP = "HEMR_M_DIGANOSIS_SAVE_START_IP"
        const val HEMR_M_DIGANOSIS_SAVE_COMPLETE_IP = "HEMR_M_DIGANOSIS_SAVE_COMPLETE_IP"
        const val HEMR_M_PREVIOUS_DIGANOSIS_VISIT_IP = "HEMR_M_PREVIOUS_DIGANOSIS_VISIT_IP"

        //   ADMISSION AND REFERAL_OP
        const val HEMR_M_AAR_VISIT_OP = "HEMR_M_AAR_VISIT_OP"

        //   ADMISSION AND REFERAL_IP
        const val HEMR_M_AAR_VISIT_IP = "HEMR_M_AAR_VISIT_IP"

        //  LABRESULT_OP
        const val HEMR_M_LR_VISIT_OP = "HEMR_M_LR_VISIT_IP"

        //  LABRESULT_IP
        const val HEMR_M_LR_VISIT_IP = "HEMR_M_LR_VISIT_IP"


        // RADIOLOGYRESULT_OP
        const val HEMR_M_RR_VISIT_OP = "HEMR_M_RR_VISIT_OP"

        // RADIOLOGYRESULT_IP
        const val HEMR_M_RR_VISIT_IP = "HEMR_M_RR_VISIT_IP"

        // INVESTIGATIONRESULT_OP
        const val HEMR_M_IR_VISIT_OP = "HEMR_M_IR_VISIT_OP"

        // INVESTIGATIONRESULT_IP
        const val HEMR_M_IR_VISIT_IP = "HEMR_M_IR_VISIT_IP"


        // DOCUMENT_OP
        const val HEMR_M_DOCUMENT_VISIT_OP = "HEMR_M_DOCUMENT_VISIT_OP"
        const val HEMR_M_DOCUMENT_ADD_START_OP = "HEMR_M_DOCUMENT_ADD_START_OP"
        const val HEMR_M_DOCUMENT_ADD_COMPLETE_OP = "HEMR_M_DOCUMENT_ADD_COMPLETE_OP"
        const val HEMR_M_DOCUMENT_DOWNLOAD_START_OP = "HEMR_M_DOCUMENT_DOWNLOAD_START_OP"
        const val HEMR_M_DOCUMENT_DOWNLOAD_COMPLETE_OP = "HEMR_M_DOCUMENT_DOWNLOAD_COMPLETE_OP"


        // DOCUMENT_IP
        const val HEMR_M_DOCUMENT_VISIT_IP = "HEMR_M_DOCUMENT_VISIT_IP"
        const val HEMR_M_DOCUMENT_ADD_START_IP = "HEMR_M_DOCUMENT_ADD_START_IP"
        const val HEMR_M_DOCUMENT_ADD_COMPLETE_IP = "HEMR_M_DOCUMENT_ADD_COMPLETE_IP"
        const val HEMR_M_DOCUMENT_DOWNLOAD_START_IP = "HEMR_M_DOCUMENT_DOWNLOAD_START_IP"
        const val HEMR_M_DOCUMENT_DOWNLOAD_COMPLETE_IP = "HEMR_M_DOCUMENT_DOWNLOAD_COMPLETE_IP"


        //  CERTIFICATE_OP
        const val HEMR_M_CER_VISIT_OP = "HEMR_M_CER_VISIT_OP"
        const val HEMR_M_CER_SAVE_START_OP = "HEMR_M_CER_SAVE_START_OP"
        const val HEMR_M_CER_SAVE_COMPLETE_OP = "HEMR_M_CER_SAVE_COMPLETE_OP"
        const val HEMR_M_PRE_CRE_VISIT_OP = "HEMR_M_PRE_CRE_VISIT_OP"

        //  CERTIFICATE_IP
        const val HEMR_M_CER_VISIT_IP = "HEMR_M_CER_VISIT_IP"
        const val HEMR_M_CER_SAVE_START_IP = "HEMR_M_CER_SAVE_START_IP"
        const val HEMR_M_CER_SAVE_COMPLETE_IP = "HEMR_M_CER_SAVE_COMPLETE_IP"
        const val HEMR_M_PRE_CRE_VISIT_IP = "HER_M_PRE_CRE_VISIT_IP"

        //  CCC_OP
        const val HEMR_M_CCC_VISIT_OP = "HEMR_M_CCC_VISIT_IP"

        //  CCC_IP
        const val HEMR_M_CCC_VISIT_IP = "HEMR_M_CCC_VISIT_IP"


        //SPECALITYSKETCH_OP
        const val HEMR_M_SS_VISIT_OP = "HEMR_M_SS_VISIT_OP"
        const val HEMR_M_SS_SAVE_START_OP = "HEMR_M_SS_SAVE_START_OP"
        const val HEMR_M_SS_SAVE_COMPLETE_OP = "HEMR_M_SS_SAVE_COMPLETE_OP"
        const val HEMR_M_PRE_SS_OP = "HEMR_M_PRE_SS"

        //SPECALITYSKETCH_IP
        const val HEMR_M_SS_VISIT_IP = "HEMR_M_SS_VISIT_IP"
        const val HEMR_M_SS_SAVE_START_IP = "HEMR_M_SS_SAVE_START_IP"
        const val HEMR_M_SS_SAVE_COMPLETE_IP = "HEMR_M_SS_SAVE_COMPLETE_IP"
        const val HEMR_M_PRE_SS_IP = "HEMR_M_PRE_SS_IP"

        //  MRD_OP
        const val HEMR_M_MRD_VISIT_OP = "HEMR_M_MRD_VISIT_OP"
        const val HEMR_M_MRD_PRINT_OP = "HEMR_M_MRD_PRINT_OP"

        //  MRD_IP
        const val HEMR_M_MRD_VISIT_IP = "HEMR_M_MRD_VISIT_IP"
        const val HEMR_M_MRD_PRINT_IP = "HEMR_M_MRD_PRINT_IP"


        //OP MODULE
        //  OPNOTES

        const val HEMR_M_OPN_VISIT = "HEMR_M_OPN_VISIT"
        const val HEMR_M_OPN_SAVE_START = "HEMR_M_OPN_SAVE_START"
        const val HEMR_M_OPN_SAVE_COMPLETE = "HEMR_M_OPN_SAVE_COMPLETE"
        const val HEMR_M_PREVIOUS_OPN_VISIT = "HEMR_M_PREVIOUS_OPN_VISIT"

        //IP MODULES

        // BLOODREQUEST
        const val HEMR_M_BR_VISIT = "HEMR_M_BR_VISIT"
        const val HEMR_M_BR_SAVE_START = "HEMR_M_BR_SAVE_START"
        const val HEMR_M_BR_SAVE_COMPLETE = "HEMR_M_BR_SAVE_COMPLETE"
        const val HEMR_M_PRE_BR = "HEMR_M_PRE_BR"


        //  IPCASESHEET
        const val HEMR_M_IPCC_VISIT = "HEMR_M_IPCC_VISIT"
        const val HEMR_M_IPCC_SAVE_START = "HEMR_M_IPCC_SAVE_START"
        const val HEMR_M_IPCC_SAVE_COMPLETE = "HEMR_M_IPCC_SAVE_COMPLETE"

        // OTSCHEDULE
        const val HEMR_M_OTS_VISIT = "HEMR_M_OTS_VISIT"
        const val HEMR_M_OTS_ADD = "HEMR_M_OTS_ADD"
        const val HEMR_M_OTS_DELETE = "HEMR_M_OTS_DELETE"
        const val HEMR_M_OTS_EDIT = "HEMR_M_OTS_EDIT"

        //OTNOTES
        const val HEMR_M_OTN_VISIT = "HEMR_M_OTN_VISIT"
        const val HEMR_M_OTN_SAVE_START = "HEMR_M_OTN_SAVE_START"
        const val HEMR_M_OTN_SAVE_COMPLETE = "HEMR_M_OTN_SAVE_COMPLETE"

        // ANESTHESIANOTES
        const val HEMR_M_AN_VISIT = "HEMR_M_AN_VISIT"
        const val HEMR_M_AN_SAVE_START = "HEMR_M_AN_SAVE_START"
        const val HEMR_M_AN_SAVE_COMPLETE = "HEMR_M_AN_SAVE_COMPLETE"

        //PROGRESSNOTES
        const val HEMR_M_PN_VISIT = "HEMR_M_PN_VISIT"
        const val HEMR_M_PN_ADD = "HEMR_M_PN_ADD"

        //Discharge Medication
        const val HEMR_M_DM_VISIT = "HEMR_M_DM_VISIT"

        //Diet
        const val HEMR_M_DIET_VISIT = "HEMR_M_DIET_VISIT"
        const val HEMR_M_DIET_SAVE_START = "HEMR_M_DIET_SAVE_START"
        const val HEMR_M_DIET_SAVE_COMPLETE = "HEMR_M_DIET_SAVE_COMPLETE"
        const val HEMR_M_PREV_DIET = "HEMR_M_PREV_DIET"

        //Discharge Summary
        const val HEMR_M_DS_VISIT = "HEMR_M_DS_VISIT"
        const val HEMR_M_DS_SAVE_START = "HEMR_M_DS_SAVE_START"
        const val HEMR_M_DS_SAVE_COMPLETE = "HEMR_M_DS_SAVE_COMPLETE"
        const val HEMR_M_PREV_DS = "HEMR_M_PREV_DS"

    }
}