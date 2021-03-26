package com.hmis_tn.doctor.data.utility

object
API {
    const val BASIC_AUTH = "Basic "

    const val APP_MASTER = "Appmaster/v1/api/"
    const val APP_TELEMEDICINE = "Telemedicine/v1/api/"
    const val APP_ECONSULTATION = "Econsult/v1/api/"
    const val APP_REGISTRATION = "registration/v1/api/"

    const val API_APPOINTMENT_BOOKED = "bookappointment/getbookAppointment"
    const val API_APPOINTMENT_SESSION_GET = "appointmentsession/getappointmentsession"
    const val API_APPOINTMENT_SESSION_UPDATE = "appointmentsession/updateappointmentsession"
    const val API_APPOINTMENT_BOOKED_CANCEL = "bookappointment/cancelappointment"
    const val API_APPOINTMENT_BOOKED_CHECK_IN = "appointments/appointmentcheckin"

    const val API_PATIENT_SEARCH = "patient/search"
    const val API_DOCTOR_LIST = "appointmentsession/getappointmentsessiondoctorlist"
    const val API_DELETE_APPOINTMENT = "appointmentsession/deleteappointmentsession"
    const val API_VIEW_APPOINTMENT = "appointmentsession/getappointmentsessionbyid"

    const val API_BOOK_APPOINTMENT = "bookappointment/postbookAppointment"
    const val API_AVAILABILITY_SLOT = "appointmentslots/getappointmentslots"
    const val API_UPDATE_SLOT = "appointmentslots/updateappointmentslots"
    const val API_BOOK_SLOT_APPOINTMENT = "bookappointment/updatebookappointment"


    /* APPOINTMENT SESSION CREATE RELATED*/
    const val API_APPOINTMENT_INSTITUTE_LIST = "facility/getfacilitybyuserid"
    const val API_APPOINTMENT_USER_TYPE_IDS= "userProfile/getusersbyusertypeids"
    const val API_SEARCH = "userProfile/userSearchByUserType"
    const val API_DEPARTMENT_LIST= "facilityDepartment/getDepartmentByFacilityId"

    /* APPOINTMENT VIDEO CALL RELATED*/
    const val API_POST_E_CONSULT = "econsult/posteconsult"
    const val API_UPDATE_E_CONSULT = "econsult/updateeconsult"
    const val VIDEO_CALL_CHECK_MULTI_LOGIN = "econsult/checkmultidoctorlogin"
    const val GET_PATIENT_BY_ID = "econsultpatient/getbypatientId"
    const val GET_PATIENT_DETAIL = "registration/v1/api/patient/getById"
    const val API_ECONSULT_UPDATE_ROOM_COUNT = "econsult/updateroomcount"
    const val API_ECONSULT_UPDATE_PATIENT_CONNECT = "econsult/updatepatientisconnect"

    /* telemedicine video call e consult */
    const val API_BOOK_APPOINTMENT_FILTER = "bookappointment/getbookAppointmentfillter"
}

object AppPreference {
    var ACCESS_TOKEN = "ACCESS_TOKEN"
    var APP_ACCESS_TOKEN = "b461162f-0f60-3e22-add5-8ec727fe5643"
}

object Constants {
    const val CAMERA_MIC_PERMISSION_REQUEST_CODE = 302
    const val REQUEST_CALENDAR_READ_WRITE_PERMISSION = 4000
    const val DRAWABLE_RIGHT = 2
}

object Extras {
    const val NO_RECORD = "No data found."
    const val EConsultLive = "EConsultLive"
}

object APIErrorKey {

}

