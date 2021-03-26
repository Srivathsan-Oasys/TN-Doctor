package com.hmis_tn.doctor.ui.telemedicine.session

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.toast
import com.hmis_tn.doctor.data.networking.api.res.ResAppointmentSessionView
import com.hmis_tn.doctor.ui.telemedicine.session.adapter.AdapterCalendarDays
import com.hmis_tn.doctor.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_appointment_session_create_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentSessionCreateDialogFragment : DialogFragment(), View.OnClickListener {

    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null
    private var TYPE = ""
    private var isMonday = false
    private var isTuesday = false
    private var isWednesday = false
    private var isThursday = false
    private var isFriday = false
    private var isSaturday = false
    private var isSunday = false

    private var calendarDaysMap = HashMap<String, String>()

    var appointmentSessionView: ResAppointmentSessionView? = null

    var adapterCalendarDays: AdapterCalendarDays? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appointmentSessionView = it.getParcelable(APPOINTMENT_SESSION_DIALOG)
        }
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.also {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (it.window != null)
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_appointment_session_create_dialog,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        switchListener()
        clickListener()
        initCalendarDays()

        if (appointmentSessionView != null) {
            btnSaveAppointmentCreate.visibility = View.GONE
            appointmentSessionView?.responseContents?.also {
                tvAppointmentDialogTitle.text = "Appointment Session View"
                etSessionName.setText(it.session_name ?: "")
                etSessionStartTime.setText(it.as_start_time ?: "")
                etSessionEndTime.setText(it.as_end_time ?: "")
                etSessionSlotDuration.setText(it.slot_duration.toString() ?: "")
                etSessionCharges.setText(it.econsult_charges.toString() ?: "")

                cbSessionEConsult.isChecked = it.is_e_consultation ?: true
                cbSessionRegularConsult.isChecked = it.is_regular_consultation ?: false

                disableAllViewField()

                val stringDayArray = ArrayList<String>()
                when {
                    it.is_monday!! -> stringDayArray.add("Mon")
                    it.is_tuesday!! -> stringDayArray.add("Tue")
                    it.is_wednesday!! -> stringDayArray.add("Wed")
                    it.is_thursday!! -> stringDayArray.add("Thu")
                    it.is_friday!! -> stringDayArray.add("Fri")
                    it.is_saturday!! -> stringDayArray.add("Sat")
                    it.is_sunday!! -> stringDayArray.add("Sun")
                }

                stringDayArray.forEach { days ->
                    if (!calendarDaysMap.containsKey(days)) {
                        calendarDaysMap[days] = days
                    } else
                        calendarDaysMap.remove(days)
                    adapterCalendarDays?.notifyDaysSelected(calendarDaysMap, true)
                }
            }
        }
    }

    private fun disableAllViewField() {
        etSessionName.isEnabled = false
        etSessionStartTime.isEnabled = false
        etSessionEndTime.isEnabled = false
        etSessionSlotDuration.isEnabled = false
        etSessionCharges.isEnabled = false
        cbSessionEConsult.isEnabled = false
        cbSessionRegularConsult.isEnabled = false
    }

    private fun clickListener() {
        etSessionStartTime.setOnClickListener(this)
        etSessionEndTime.setOnClickListener(this)
        btnSaveAppointmentCreate.setOnClickListener(this)
        btnCancelAppointmentCreate.setOnClickListener(this)
        ivCloseDialog.setOnClickListener(this)
    }

    private fun switchListener() {
        cbSessionEConsult.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.id == R.id.cbSessionEConsult) {
                cbSessionRegularConsult.isEnabled = !isChecked
                tlSessionCharges.isVisible = isChecked
            }
        }
        cbSessionRegularConsult.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.id == R.id.cbSessionRegularConsult) {
                cbSessionEConsult.isEnabled = !isChecked
            }
        }
    }

    private fun initCalendarDays() {
        val daysArray =
            requireContext().resources.getStringArray(R.array.days_arrays).toMutableList()
        val calendarArrayList = ArrayList<String>()
        calendarArrayList.addAll(daysArray)
        rvCalendarDays.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapterCalendarDays = AdapterCalendarDays(calendarArrayList) {
                if (!calendarDaysMap.containsKey(it)) {
                    calendarDaysMap[it] = it
                } else
                    calendarDaysMap.remove(it)
                (adapter as AdapterCalendarDays).notifyDaysSelected(calendarDaysMap, false)
                LogUtils.logE("selected", "days____$calendarDaysMap")
            }
            adapter = adapterCalendarDays
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.etSessionStartTime -> openTimePicker(type = "START")
            R.id.etSessionEndTime -> openTimePicker(type = "END")
            R.id.btnSaveAppointmentCreate -> {
                if (isValid()) {
                    validateData()
                }
            }
            R.id.btnCancelAppointmentCreate -> dismiss()
            R.id.ivCloseDialog -> dismiss()
        }
    }

    private fun openTimePicker(type: String) {
        TYPE = type
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val calendarDisplay: Calendar = Calendar.getInstance()
        val calendar: Calendar = Calendar.getInstance()

        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        if (TYPE == "START") {
            mHour = calendar.get(Calendar.HOUR_OF_DAY)
            mMinute = calendar.get(Calendar.MINUTE)
            mSecond = calendar.get(Calendar.SECOND)
        } else {
            mHour = calendarDisplay.get(Calendar.HOUR_OF_DAY)
            mMinute = calendarDisplay.get(Calendar.MINUTE)
            mSecond = calendarDisplay.get(Calendar.SECOND)
        }

        val timePickerDialog = TimePickerDialog(
            this.activity,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                if (TYPE == "START") {
                    calendarDisplay.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendarDisplay.set(Calendar.MINUTE, minute)
                } else {
                    calendarDisplay.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendarDisplay.set(Calendar.MINUTE, minute)
                }
                if (TYPE == "START") {
                    val displayStartTime = sdf.format(calendarDisplay.time)
                    etSessionStartTime.setText(displayStartTime)
                } else {
                    val displayEndTime = sdf.format(calendarDisplay.time)
                    etSessionEndTime.setText(displayEndTime)
                }
            },
            mHour!!,
            mMinute!!,
            false
        )
        timePickerDialog.updateTime(
            calendarDisplay.get(Calendar.HOUR_OF_DAY),
            calendarDisplay.get(Calendar.MINUTE)
        )
        timePickerDialog.show()
    }

    private fun validateData() {
        calendarDaysMap.forEach { (_, value) ->
            when (value) {
                "Mon" -> isMonday = true
                "Tue" -> isTuesday = true
                "Wed" -> isWednesday = true
                "Thu" -> isThursday = true
                "Fri" -> isFriday = true
                "Sat" -> isSaturday = true
                "Sun" -> isSunday = true
            }
        }
        dismiss()
    }

    private fun isValid(): Boolean {
        return when {
            etSessionName.text.toString().isEmpty() -> {
                requireContext().toast("Choose Session name to process !")
                false
            }
            etSessionStartTime.text.toString().isEmpty() -> {
                requireContext().toast("Choose Start Time to process !")
                false
            }
            etSessionEndTime.text.toString().isEmpty() -> {
                requireContext().toast("Choose End Time to process !")
                false
            }
            etSessionSlotDuration.text.toString().isEmpty() -> {
                requireContext().toast("Choose Slot duration to process !")
                false
            }
            !cbSessionEConsult.isChecked && !cbSessionRegularConsult.isChecked -> {
                requireContext().toast("Select any one consultation tick box to process !")
                false
            }
            etSessionCharges.text.toString().isEmpty() -> {
                if (cbSessionEConsult.isChecked) {
                    requireContext().toast("Enter consultation charge to process !")
                    false
                } else
                    true
            }
            else -> {
                true
            }
        }
    }

    companion object {

        const val APPOINTMENT_SESSION_DIALOG = "APPOINTMENT_SESSION_"

        @JvmStatic
        fun newInstance(appointmentSessionView: ResAppointmentSessionView) =
            AppointmentSessionCreateDialogFragment().apply {
                arguments = bundleOf(
                    APPOINTMENT_SESSION_DIALOG to appointmentSessionView
                )
            }
    }
}