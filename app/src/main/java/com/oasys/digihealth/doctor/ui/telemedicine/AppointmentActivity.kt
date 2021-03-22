package com.oasys.digihealth.doctor.ui.telemedicine

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.plusAssign
import com.oasys.digihealth.doctor.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var isAppointmentSession: Boolean? = null
    internal val viewModelAppointment by viewModel<AppointmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)
        if (intent.hasExtra(ISAPPOINTMENTSESSION))
            isAppointmentSession = intent.getBooleanExtra(ISAPPOINTMENTSESSION, false)
        navController = findNavController(R.id.nav_appointment)
        findNavController(R.id.nav_appointment).navigatorProvider.apply {
            this += ActivityNavigator(
                this@AppointmentActivity
            )
            this += DialogFragmentNavigator(
                this@AppointmentActivity,
                supportFragmentManager
            )
        }

        isAppointmentSession?.let {
            if (it) {
                navController.navigate(R.id.nav_appointment_session_list)
            } else {
                navController.navigate(R.id.nav_appointment_patient_list)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigateUp(): Boolean = findNavController(R.id.nav_appointment).navigateUp()

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.nav_appointment_search, R.id.nav_appointment_main_search, R.id.nav_appointment_doctors -> super.onBackPressed()
            R.id.nav_appointment_booking -> finish()
            R.id.nav_appointment_patient_list -> finish()
            R.id.nav_appointment_session_list -> finish()
            else -> super.onBackPressed()
        }

    }

    companion object {
        const val ISAPPOINTMENTSESSION = "ISAPPOINTMENTSESSION"
    }
}
