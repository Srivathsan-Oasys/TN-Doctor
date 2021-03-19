package com.oasys.digihealth.doctor.component.listener


import com.oasys.digihealth.doctor.data.networking.api.res.*
import java.io.File
import java.util.*

typealias OnDayOfWeekClickListener = (date: Date) -> Unit

typealias OnClickBookAgain = (resMyBookingData: ResMyBookingData) -> Unit

typealias OnClickPatient = (resPatientListSearch: ResPatientListSearch) -> Unit

typealias OnBookAppointmentListener = (doctorList: DoctorList) -> Unit

typealias OnSlotAppointment = (slotData: DoctorSlotList) -> Unit

typealias OnPatientSelected = (patient: ResPatientListSearch) -> Unit

typealias OnCalendarDaysSelected = (day: String) -> Unit

typealias OnDownloadPDFSuccess = (file: File?) -> Unit

typealias OnClickDeleteSession = (appointmentObj: ResAppointmentSessionList) -> Unit
typealias OnClickViewSession = (appointmentObj: ResAppointmentSessionList) -> Unit
typealias OnClickEditSession = (appointmentObj: ResAppointmentSessionList) -> Unit

typealias OnClickDeleteBookAppointment = (resAppointmentBookedList:ResAppointmentBookedList) -> Unit
typealias OnClickEditBookAppointment = (resAppointmentBookedList:ResAppointmentBookedList) -> Unit
typealias OnClickCheckInBookAppointment = (resAppointmentBookedList:ResAppointmentBookedList) -> Unit

typealias OnClickedYes<R> = (R) -> Unit

typealias OnClickedNo<R> = (R) -> Unit
