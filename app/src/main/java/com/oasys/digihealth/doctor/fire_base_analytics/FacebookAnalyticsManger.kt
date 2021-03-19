package com.oasys.digihealth.doctor.fire_base_analytics

class FacebookAnalyticsManger {

    /*fun facebookConfig(context: Context) {
        FacebookSdk.sdkInitialize(context);
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

    }

    fun tarckLoginVisit(context: Context) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        logger.logEvent(AnalyticsConstant.HMIS_MOBILE_LOGIN_VISIT,parameters)
    }

    fun tarckLoginSucess(context: Context) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_LOGIN_COMPLETE,parameters)
    }

    fun tarckLoginFaliure(context: Context,message: String) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        logger.logEvent(AnalyticsConstant.HMIS_MOBILE_LOGIN_FAILURE,parameters)
    }

    fun trackLMISNewOrderVisit(context: Context) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_NEWORDER_VISIT,parameters)
    }

    fun trackLMISNewOrderSuccess(context: Context, name: String) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        parameters.putString("Parent name", name)
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_NEWORDER_SUCCESS,parameters)

    }

    *//*fun trackLMISLabApprovalVisit(context: Context) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_LABTEST_APPROVEL_VISIT,parameters)
    }*//*

    fun trackLMISLabSave(context: Context, name: String) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        parameters.putString("Parent name", name)
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_LABTEST_SAVE,parameters)

    }

    fun trackLMISLabTestOrderApprovel(context: Context, name: String) {
        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        parameters.putString("Parent name", name)
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_LABTEST_ORDERPROCESS_APPROVAL,parameters)
    }

    fun trackLMISLabProcessOrderApproval(context: Context, name: String) {

        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        parameters.putString("Parent name", name)
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_TESTPROCESS_ORDERPROCESS_APPROVAL,parameters)

    }

    fun trackLMISLabApprovalOrderApproval(context: Context, name: String) {

        val logger = AppEventsLogger.newLogger(context)
        val parameters = Bundle()
        parameters.putString("os","android")
        parameters.putString("Parent name", name)
        logger.logEvent(AnalyticsConstant.HMIS_LIMS_MOBILE_TESTAPPROVAL_LABRESULTAPPROVAL_APPROVAL,parameters)

    }*/

}