package com.hmis_tn.doctor.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.hmis_tn.doctor.api_service.APIService
import com.hmis_tn.doctor.data.networking.api.res.DoctorList
import com.hmis_tn.doctor.data.networking.api.res.DoctorSlotList
import com.hmis_tn.doctor.fire_base_analytics.FirebaseAnalyticsManager
import java.util.*

class HmisApplication : MultiDexApplication() {

    init {
        instance = this
    }

    private var mRetrofitService: APIService? = null
    private var fireBaseAnalyticsManager: FirebaseAnalyticsManager? = null

    private val id: String = "1"
    private val name: String = "HmisApplication"

    override fun onCreate() {
        super.onCreate()
        app = this
        instance = this
        FirebaseApp.initializeApp(this)

        /*       //Injection used koin module instead of dagger
               startKoin {
                   androidLogger(Level.DEBUG)
                   androidContext(this@HmisApplication)
                   loadKoinModules(REQUIRED_MODULE)
               }*/

        createNotificationChannels()

        // Obtain the FirebaseAnalytics instance.
        // need to remove in production
        // Stetho.initializeWithDefaults(this);

        // Obtain the FirebaseAnalytics instance.


        // Create a new Locale object
        // Create a new Locale object
        val locale = Locale("ta")
        Locale.setDefault(locale)
        // Create a new configuration object
        // Create a new configuration object
        val config = Configuration()
        // Set the locale of the new configuration
        // Set the locale of the new configuration
        config.locale = locale
        // Update the configuration of the Accplication context
        // Update the configuration of the Accplication context
        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )

    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_1,
                "Download File",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "Channel 1"
            notificationChannel.enableVibration(true)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    fun getRetrofitService(): APIService? {
        if (mRetrofitService == null) {
            mRetrofitService = APIService.Factory.create(applicationContext)
        }
        return mRetrofitService
    }

    val fireBaseManager: FirebaseAnalyticsManager
        get() {
            if (fireBaseAnalyticsManager == null)
                fireBaseAnalyticsManager =
                    FirebaseAnalyticsManager(this)
            return fireBaseAnalyticsManager as FirebaseAnalyticsManager
        }


    companion object {
        lateinit var instance: HmisApplication

        fun applicationContext(): Context {
            return instance.applicationContext
        }

        fun get(context: Context): HmisApplication {
            return context.applicationContext as HmisApplication
        }

        private var app: HmisApplication? = null
        fun app(): HmisApplication = app!!

        fun isConnected(): Boolean {
            val connectivityManager =
                app!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null
        }

//        val REQUIRED_MODULE = listOf(
//            NETWORKING_MODULE,
//            REPOSITORY_MODULE,
//            VIEW_MODEL_MODULE
//        )


        var selectedDoctorByPatient: DoctorList? = null
        var selectedSlotByPatient: DoctorSlotList? = null
        var paymentSuccess: Boolean = false
        const val CHANNEL_1 = "Channel 1"
    }


}