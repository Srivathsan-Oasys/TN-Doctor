package com.oasys.digihealth.doctor.ui.telemedicine.videocall

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.component.extention.toast
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.data.event.NetworkEvent
import com.oasys.digihealth.doctor.data.networking.api.req.ReqEConsult
import com.oasys.digihealth.doctor.data.networking.api.req.ReqGetPatientById
import com.oasys.digihealth.doctor.data.networking.api.req.ReqGetPatientDetail
import com.oasys.digihealth.doctor.data.networking.api.req.ReqUpdateRoomBody
import com.oasys.digihealth.doctor.data.networking.api.res.ResGetPatientById
import com.oasys.digihealth.doctor.data.networking.api.res.ResPostEConsult
import com.oasys.digihealth.doctor.data.utility.Constants.CAMERA_MIC_PERMISSION_REQUEST_CODE
import com.oasys.digihealth.doctor.databinding.ActivityVideoCallBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui.AdmissionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.ui.BloodRequestFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.ChiefComplaintsFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.DocumentFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.LabFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.DiagnosisFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui.DietFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.ui.HistoryFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui.InvestigationFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FetchEncounterResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui.MRDFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.ui.OpNotesFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.ui.OtNotesFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.PrescriptionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.view_model.EmrViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.view_model.EmrViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.ui.VitalsFragment
import com.oasys.digihealth.doctor.ui.telemedicine.pageradapter.EMRPagerAdapter
import com.oasys.digihealth.doctor.ui.viewmodel.VideoCallViewModel
import com.oasys.digihealth.doctor.ui.viewmodel.VideoChatViewModel
import com.oasys.digihealth.doctor.utils.Utils
import com.opentok.android.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class VideoCallActivity : AppCompatActivity(), Session.SessionListener,
    PublisherKit.PublisherListener, NotificationDialogFragment.OnClickIncomingNotification {

    private lateinit var binding: ActivityVideoCallBinding

    private val videoChatViewModel by viewModel<VideoChatViewModel>()
    private val videoCallViewModel by viewModel<VideoCallViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var mSession: Session? = null
    private var mPublisher: Publisher? = null
    private var mSubscriber: Subscriber? = null
    private var doctorID = 0
    private var appointment_uuid = 0
    private var userID = 0
    private var patientID = 0

    private var tokenBearer = ""
    private var userUUID: Int = -1

    private var token = ""
    private var apiKey = ""
    private var sessionID = ""

    private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var currentTime = ""
    private var currentDate = ""

    private var startDateAndTime = ""
    private var endDateAndTime = ""

    private var stream: Stream? = null
    var randomUUID = ""
    var resPostEConsult: ResPostEConsult? = null

    var enconsult_uuid = 0


    /*
    * EMR VIDEO CALL RELATED
    * */
    private var encounter_uuid: Int? = 0
    private var encounter_doctor_uuid: Int? = 0
    private var viewpageradapter: EMRPagerAdapter? = null
    private var tabsArrayList: List<ResponseContent?>? = null
    private var viewModel: EmrViewModel? = null
    private var utils: Utils? = null
    private var selectedFragment: Fragment? = null
    lateinit var encounterResponseContent: List<FetchEncounterResponseContent?>
    private lateinit var getStoreMasterId: List<StoreMasterresponseContent?>
    private var encounterType: Int = 0
    private var facility_id: Int = 0
    private var department_uuid: Int = 0
    private var store_master_uuid: Int? = null
    var appPreferences: AppPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_call)

        viewModel = EmrViewModelFactory(application).create(EmrViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)
        //encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        encounterType = 1

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!

        viewModel!!.errorText.observe(this, Observer { toastMessage ->
            utils!!.showToast(R.color.negativeToast, binding.mainLayout, toastMessage)
        })
        currentDate = dateFormatter.format(Date().time)
        currentTime = timeFormatter.format(Date().time)

        binding.contentLinearLayout.visibility = View.INVISIBLE
        binding.noDataFoundTextView.visibility = View.INVISIBLE

        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean.access_token
        userUUID = userDataStoreBean.uuid!!
        doctorID = userDataStoreBean.uuid

//        setContentView(R.layout.activity_video_call)

        if (HmisApplication.isConnected()) {
            subscribeToNetworkEvents()
            subscribeToNetworkEventsCall()
        } else
            toast("No Network")

        initUI()
    }

    private fun initUI() {
        binding.cvEndCall.setOnClickListener {
            lifecycleScope.launch {
                showLoading(true)
                currentDate = dateFormatter.format(Date().time)
                currentTime = timeFormatter.format(Date().time)
                endDateAndTime = "$currentDate $currentTime"
                subscribeToUpdateEConsultAPI()
            }
        }
        binding.cvVideoOnOff.setOnClickListener {
            lifecycleScope.launch {
                if (stream != null) {
                    if (mPublisher.publishVideo == false) {
                        binding.cvVideoOnOff.setImageResource(R.drawable.ic_video_on)
//                        mSubscriber?.subscribeToVideo = true
                        mPublisher.publishVideo = true
                        binding.publisherContainer.visibility = View.VISIBLE
                    } else {
                        binding.cvVideoOnOff.setImageResource(R.drawable.ic_video_off)
//                        mSubscriber?.subscribeToVideo = false
                        mPublisher.publishVideo = false
                        binding.publisherContainer.visibility = View.GONE
                    }
                }
            }
        }

        binding.cvMicOnOff.setOnClickListener {
            lifecycleScope.launch {
                if (stream != null) {
                    if (mPublisher.publishAudio == false) {
                        binding.cvMicOnOff.setImageResource(R.drawable.ic_mic_on)
                        mPublisher.publishAudio = true
                    } else {
                        binding.cvMicOnOff.setImageResource(R.drawable.ic_mic_off)
                        mPublisher.publishAudio = false
                    }
                }
            }
        }

        binding.tvVideoCallSwitchAction.setOnClickListener {
            if (binding.rlMainVideoCallView.visibility == View.VISIBLE) {
                binding.rlMainVideoCallView.visibility = View.GONE
                binding.rlVideoCallEMR.visibility = View.VISIBLE
                binding.tvVideoCallSwitchAction.text = "return back to video call"
            } else {
                binding.rlMainVideoCallView.visibility = View.VISIBLE
                binding.rlVideoCallEMR.visibility = View.GONE
                binding.tvVideoCallSwitchAction.text = "return back to Telemedicine EMR"
            }
        }

        binding.switchVideoCallCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.id == R.id.switchVideoCallCheck) {
                if (isChecked) {
                    videoCallViewModel.checkMultiDoctorLogin(
                        tokenBearer, userUUID, doctorID.toString()
                    ) {
                        if (it != null) {
                            if (it.statusCode == 200 || it.statusCode == 201) {
                                binding.rlVideoCallChecking.visibility = View.GONE
                                requestPermissionForCameraAndMicrophone()
                            } else {
                                alertDialogDoctorBusy(
                                    it.msg
                                        ?: "Doctor Already logged in with some other system , try to log out and try again here!"
                                )

                            }
                        } else {
                            alertDialogDoctorBusy("Doctor Already logged in with some other system , try to log out and try again here!")
                        }
                    }
                } else {
                    subscribeToUpdateConsultLogout()
                }
            }
        }
    }

    private fun subscribeToUpdateConsultLogout() {

    }

    private fun subscribeToVideoCallAPI() {
        randomUUID = UUID.randomUUID().toString()
        videoChatViewModel.videoChatAPI(randomUUID).observe(this) {
            token = it.token ?: ""
            apiKey = it.apiKey ?: ""
            sessionID = it.sessionId ?: ""
            mSession = Session.Builder(this, apiKey, sessionID).build()
            mSession.setSessionListener(this)
            subscribeToPostEConsultAPI()

            // TODO Once patient connected with doctor enable this line
            mSession.connect(token)
        }
    }

    private fun subscribeToPostEConsultAPI() {
        videoCallViewModel.postEConsult(
            tokenBearer, userUUID, ReqEConsult(
                doc_login = "$currentDate $currentTime",
                doc_logout = "",
                doc_session_uuid = sessionID,
                doc_token = token,
                doc_uuid = userUUID.toString(),
                room_count = 1,
                tele_url_link = randomUUID
            )
        ) {
            if (it != null) {
                if (it.statusCode == 200 || it.statusCode == 201) {
                    resPostEConsult = it
                    toast(it.msg ?: "")
                    enconsult_uuid = it.responseContents.uuid!!

//                    val params =
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                            PictureInPictureParams.Builder()
//                                .setAspectRatio(Rational(9, 16))
//                                .build()
//                        } else {
//                            TODO("VERSION.SDK_INT < O")
//                        }
//                    enterPictureInPictureMode(params)
                }
            } else {
                toast(it.msg ?: "Something Went wrong post e consult ")
            }
        }
    }

    private fun subscribeToUpdateEConsultAPI() {
        if (resPostEConsult != null) {
            // CURRENT DATE AND TIME WILL PASS FOR LOGOUT DOCTOR
            currentDate = dateFormatter.format(Date().time)
            currentTime = timeFormatter.format(Date().time)
            videoCallViewModel.updateEConsult(
                tokenBearer, userUUID, ReqEConsult(
                    Id = resPostEConsult.responseContents.uuid.toString(),
                    doc_logout = "$currentDate $currentTime",
                    doc_uuid = userUUID.toString()
                )
            ) {
                if (it != null) {
                    if (it.statusCode == 200 || it.statusCode == 201) {
                        toast(it.msg ?: "")
                        endCallValidation()
                        finish()
                        //TODO CLOSE VIDEO CALL AND REMOVE SESSION
                    }
                } else {
                    endCallValidation()
                    finish()
                    toast(it.msg ?: "Something Went wrong post e consult ")
                }
            }
        }
    }

    private fun subscribeToPatientGetByIdAPI() {
        videoCallViewModel.getPatientById(
            tokenBearer,
            userUUID,
            ReqGetPatientById(userUUID.toString(), "$currentDate $currentTime")
        ) {
            if (it != null && it.statusCode == 200) {
                patientID = it.responseContents.pat_uuid ?: 0
                subscribeToPatientDetailAPI(it)
            }
        }
    }

    private fun subscribeToPatientDetailAPI(patientDetail: ResGetPatientById) {
        videoCallViewModel.getPatientDetail(
            tokenBearer,
            userUUID,
            ReqGetPatientDetail(if (patientID > 0) patientID else patientDetail.responseContents.pat_uuid)
        ) {
            if (it != null && it.statusCode == 200) {
                incomingAlertDialog()
            }
        }
    }

    private fun subscribeToCallRoomCountAPI() {
        videoCallViewModel.updateEConsultRoomCount(
            tokenBearer,
            userUUID,
            ReqUpdateRoomBody(
                Id = enconsult_uuid.toString(),
                room_count = 3,
                isWeb = true
            )
        ).observe(this) {
            if (it.statusCode == 200) {

            }
        }
    }


    private fun subscribeToCallUpdatePatientIsConnectAPIAPI() {
        videoCallViewModel.updateEConsultPatientIsConnectAPIAPI(
            tokenBearer,
            userUUID,
            ReqUpdateRoomBody(
                Id = enconsult_uuid.toString(),
                patient_is_connect = 1
            )
        ).observe(this) {
            if (it.statusCode == 200) {
            }

        }
    }

    private fun incomingAlertDialog() {
        val notificationDialogFragment = NotificationDialogFragment.newInstance()
        notificationDialogFragment.setCallBackActionMethod(this)
        notificationDialogFragment.show(supportFragmentManager, "")
    }

    private fun endCallValidation() {
        if (mSubscriber != null && mSession != null) {
            mSession.onPause()
            mSubscriber.subscribeToAudio = false
            mSubscriber.subscribeToVideo = false
            mPublisher.publishVideo = false
            mPublisher.publishAudio = false
            mSession.disconnect()
            mSubscriber.destroy()
            mSubscriber = null
            binding.subscriberContainer.removeAllViews()
        } else {
            mSubscriber.subscribeToAudio = false
            mSubscriber.subscribeToVideo = false
            mSubscriber.destroy()
            mSubscriber = null
            binding.subscriberContainer.removeAllViews()
        }
    }

    private fun subscribeToNetworkEvents() {
        videoChatViewModel.networkEvent.observe(this) {
            when (it) {
                NetworkEvent.Loading -> showLoading(true)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.ApiMessage -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        toast(it.msg)
                    }
                }
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        toast(it.res)
                    }

                }
            }
        }
    }

    private fun subscribeToNetworkEventsCall() {
        videoCallViewModel.networkEvent.observe(this) {
            when (it) {
                NetworkEvent.Loading -> showLoading(true)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.ApiMessage -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        toast(it.msg)
                    }
                }
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled().run {
                        toast(it.res)
                    }

                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.pbVideoCall.isVisible = isLoading
    }


    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
//            findViewById<View>(R.id.button).visibility = View.GONE
            binding.publisherContainer.visibility = View.GONE
            if (mPublisher != null) {
                mPublisher.view.visibility = View.GONE
            }
//            actionBar!!.hide()
        } else {
//            findViewById<View>(R.id.button).visibility = View.VISIBLE
            binding.publisherContainer.visibility = View.VISIBLE
            mPublisher.view.visibility = View.VISIBLE
            if (mPublisher != null) {
                if (mPublisher.view!! is GLSurfaceView) {
                    (mPublisher.view!! as GLSurfaceView).setZOrderOnTop(true)
                }
            }
//            actionBar!!.show()
        }
    }

    private fun checkPermissionForCameraAndMicrophone(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForCameraAndMicrophone() {
        if (!checkPermissionForCameraAndMicrophone()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                CAMERA_MIC_PERMISSION_REQUEST_CODE
            )
        } else {
            binding.tvWaiting.visibility = View.VISIBLE
            subscribeToVideoCallAPI()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            var cameraAndMicPermissionGranted = true

            for (grantResult in grantResults) {
                cameraAndMicPermissionGranted = cameraAndMicPermissionGranted and
                        (grantResult == PackageManager.PERMISSION_GRANTED)
            }
            if (cameraAndMicPermissionGranted) {
                requestPermissionForCameraAndMicrophone()
            } else {
                toast(R.string.permissions_needed)
            }
        }
    }

    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")
        showLoading(false)
        mPublisher = Publisher.Builder(this).build()
        mPublisher.setPublisherListener(this)
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
        toast("Disconnected !")
        binding.cvEndCall.performClick()
    }

    override fun onStreamReceived(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Session Received")
        if (mSubscriber == null) {
            this.stream = stream
            mSubscriber = Subscriber.Builder(this, stream).build()
            subscribeToPatientGetByIdAPI()
        }
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Session Dropped")
        this.stream = stream
        binding.tvWaiting.visibility = View.GONE
        binding.cvEndCall.performClick()
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        try {
            Handler().postDelayed({
                Toast.makeText(this, opentokError.message ?: "", Toast.LENGTH_SHORT).show()
                endCallValidation()
                finish()
            }, 200)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onStreamCreated(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated")
        this.stream = stream
        showLoading(false)
        binding.tvWaiting.visibility = View.GONE
    }

    override fun onStreamDestroyed(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed")
    }

    override fun onError(publisherkit: PublisherKit, opentokError: OpentokError) {
        Log.i(LOG_TAG, "Publisher Error:" + opentokError.message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mSession != null && mSubscriber != null) {
            mSubscriber.destroy()
            mSubscriber.subscribeToAudio = false
            mSubscriber.subscribeToVideo = false
            mSession.onPause()
            mSession.disconnect()
            binding.cvEndCall.performClick()
        } else {
            binding.cvEndCall.performClick()
        }
    }

    override fun onResume() {
        super.onResume()
        if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                !isInPictureInPictureMode
            } else {
                TODO("VERSION.SDK_INT < N")
            }
        ) {
            if (mSession != null) {
                mSession.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (AudioDeviceManager.getAudioDevice() != null) {
            AudioDeviceManager.getAudioDevice().onPause()
        }
        if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                !isInPictureInPictureMode
            } else {
                TODO("VERSION.SDK_INT < N")
            }
        ) {
            if (mSession != null) {
                mSession.onPause()
            }
        }
    }

    override fun onClickAcceptCall() {
        binding.subscriberContainer.addView(mSubscriber.view)
        binding.publisherContainer.addView(mPublisher.view)
        if (mPublisher.view is GLSurfaceView) {
            (mPublisher.view as GLSurfaceView).setZOrderOnTop(true)
        }

        mSession!!.subscribe(mSubscriber)
        mSession!!.publish(mPublisher)

        showLoading(false)
        binding.cvEndCall.show()
        binding.tvWaiting.visibility = View.GONE
        binding.cvVideoOnOff.visibility = View.VISIBLE
        binding.cvMicOnOff.visibility = View.VISIBLE

        binding.tvVideoCallSwitchAction.visibility = View.VISIBLE
        binding.rlMainVideoCallView.visibility = View.VISIBLE

        subscribeToCallRoomCountAPI()
        subscribeToCallUpdatePatientIsConnectAPIAPI()




        viewModel.getEncounter(
            facility_id,
            patientID,
            encounterType,
            fetchEncounterRetrofitCallBack
        )

        viewModel.getStoreMaster(facility_id, department_uuid, getStoreMasterRetrofitCallback)
        viewModel.getPatientLatestRecord(
            facility_id,
            patientID,
            encounterType,
            getPatientLatestEncCallback
        )
        viewModel.getPatientById(facility_id, patientID, encounterType, getPatientByIdCallback)
    }

    override fun onClickRejectCall() {
        binding.cvEndCall.performClick()
    }

    private fun alertDialogDoctorBusy(msg: String) {
        try {
            val builder = AlertDialog.Builder(
                this,
                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
            )
            builder.setTitle("Warning")
            builder.setMessage(msg)
            builder.setPositiveButton(getString(R.string.dialog_action_ok)) { d, _ ->
                d?.dismiss()
                binding.switchVideoCallCheck.isChecked = false
            }
            val alert: AlertDialog = builder.create()
            alert.setOnShowListener {
                val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
                btnPositive.textSize = 16F
                btnPositive.typeface = ResourcesCompat.getFont(this, R.font.poppins)
            }
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /*
    * EMR FLOW CODE STARTED HERE
    * */

    private fun setupViewPager(tabsArrayList: List<ResponseContent?>) {
        viewpageradapter = EMRPagerAdapter(supportFragmentManager)
        for (i in tabsArrayList.indices) {
            if (tabsArrayList[i].activity_code == AppConstants.ACTIVITY_CODE_CHIEF_COMPLAINTS) {
                viewpageradapter!!.addFragment(ChiefComplaintsFragment(), "Chief Complaints")
            } else if (tabsArrayList[i].activity_code == AppConstants.ACTIVITY_CODE_LAB) {
                viewpageradapter!!.addFragment(LabFragment(), "Lab")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_RADIOLOGY) {
                viewpageradapter!!.addFragment(RadiologyFragment(), "Radiology")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_INVESTIGATION) {
                viewpageradapter!!.addFragment(InvestigationFragment(), "Investigation")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_DIAGNOSIS) {
                viewpageradapter!!.addFragment(DiagnosisFragment(), "Diagnosis")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_HISTORY) {
                viewpageradapter!!.addFragment(HistoryFragment(), "History")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_PRESCRIPTION) {

                val prescription = PrescriptionFragment()
                val bundle = Bundle()
                bundle.putInt(AppConstants.RESPONSETYPE, 0)
                prescription.arguments = bundle


                viewpageradapter!!.addFragment(prescription, "Prescription")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_VITALS) {
                viewpageradapter!!.addFragment(VitalsFragment(), "Vitals")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_LAB_RESULT) {
//                viewpageradapter!!.addFragment(LabResultFragment(), "Lab Result")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_RADIOLOGY_RESULT) {
//                viewpageradapter!!.addFragment(RadiologyResultFragment(), "Radiology Result")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_TREATMENT_KIT) {
//                viewpageradapter!!.addFragment(TreatmentKitFragment(), "Treatment Kit")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_OP_NOTES) {
                viewpageradapter!!.addFragment(OpNotesFragment(), "Op Notes")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_DOCUMENT) {
                viewpageradapter!!.addFragment(DocumentFragment(), "Documents")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_INVESTIGATION_RESULT) {
//                viewpageradapter!!.addFragment(InvestigationResultFragment(), "Investigation Result")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_ADMISSION) {
                val bundle = Bundle()
//                bundle.putString("flow",flow)
                bundle.putString("flow", AppConstants.OUT_PATIENT)
                val admissionFragment = AdmissionFragment()
                admissionFragment.arguments = bundle
                viewpageradapter!!.addFragment(admissionFragment, "Admission/Referral")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("bloodrequest")) {                                    //Sri activity code is null
                viewpageradapter!!.addFragment(BloodRequestFragment(), "Blood Request")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("diet")) {
                viewpageradapter!!.addFragment(DietFragment(), "Diet")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("mrd")) {
                viewpageradapter!!.addFragment(MRDFragment(), "MRD")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("progress")) {
//                viewpageradapter!!.addFragment(ProgressNotesFragment(),"Progress Notes")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("discharge")) {
//                viewpageradapter!!.addFragment(DischargeSummaryFragment(),"Discharge Summary")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("casesheet")) {                                  //Sri activity code is null
//                viewpageradapter!!.addFragment(IpCaseSheetFragment(),"IP Case Sheet")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_OP_NOTES) {
                viewpageradapter!!.addFragment(OpNotesFragment(), "Op Notes")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_OT_NOTES) {
                viewpageradapter!!.addFragment(OtNotesFragment(), "Ot Notes")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_ANESTHESIA_NOTES) {
//                viewpageradapter!!.addFragment(AnesthesiaNotesFragment(),"Anesthesia Notes")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("criticalcare")) {
//                viewpageradapter!!.addFragment(CriticalCareChartFragment(),"Critical Care Chart")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("medication")) {

                val DischargeMedication = PrescriptionFragment()

                val bundle = Bundle()
                bundle.putInt(AppConstants.RESPONSETYPE, 1)
                DischargeMedication.arguments = bundle

//                viewpageradapter!!.addFragment(DischargeMedication,"Discharge medication")


            } else if (tabsArrayList[i]!!.activity_route_url.equals("sketch")) {
//                viewpageradapter!!.addFragment(SpecialitySketchChildFragment(),"Specality Sketch")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("schedule")) {
//                viewpageradapter!!.addFragment(OtSechduleFragment(),"Ot Sechdule")
            } else {
//                viewpageradapter!!.addFragment(CertificateFragment(), "Certificate")
            }
        }
        binding.viewPager.adapter = viewpageradapter
        viewpageradapter?.notifyDataSetChanged()
    }

    private val emrWorkFlowRetrofitCallBack = object {
        override fun onSuccessfulResponse(response: Response<EmrWorkFlowResponseModel>) {
            if (response.body().responseContents.isNotEmpty()!!) {
                binding.contentLinearLayout.visibility = View.VISIBLE
                binding.noDataFoundTextView.visibility = View.INVISIBLE
                tabsArrayList = response.body().responseContents!!
                setupViewPager(tabsArrayList!!)
                binding.viewPager.offscreenPageLimit = 2
                binding.tabLayout.setupWithViewPager(binding.viewPager)
                Log.e("tabsArrayList", "_______" + tabsArrayList?.size)
                for (i in tabsArrayList!!.indices) {
                    val layoutInflater: View? =
                        LayoutInflater.from(this@VideoCallActivity)
                            .inflate(R.layout.treatment_custom_tab_row, null, false)
                    val tabImageView = layoutInflater?.findViewById(R.id.tabImageView) as ImageView
                    val tabTextView = layoutInflater.findViewById(R.id.tabTextView) as TextView
                    tabTextView.text = tabsArrayList!![i].activity_name
                    binding.tabLayout.getTabAt(i).customView = layoutInflater
                }
            } else {
                binding.contentLinearLayout.visibility = View.INVISIBLE
                binding.noDataFoundTextView.visibility = View.VISIBLE
            }
        }

        override fun onBadRequest(response: Response<EmrWorkFlowResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding.mainLayout, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }
    private val fetchEncounterRetrofitCallBack =
        object {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body().responseContents.isNotEmpty()!!) {
                    encounterResponseContent = response.body().responseContents!!
                    encounter_doctor_uuid =
                        encounterResponseContent[0].encounter_doctors.get(0).uuid
                    encounter_uuid = encounterResponseContent[0].uuid
//                    appPreferences?.saveInt(
//                        AppConstants.ENCOUNTER_DOCTOR_UUID,
//                        encounter_doctor_uuid!!
//                    )
//                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
                    callEmrWorkFlow()

                } else {
                    viewModel.createEncounter(
                        patientID,
                        encounterType,
                        createEncounterRetrofitCallback
                    )
                }
            }

            override fun onBadRequest(response: Response<FectchEncounterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FectchEncounterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FectchEncounterResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout,
                        responseModel.message!!
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout, failure)
            }

            override fun onEverytime() {
                viewModel!!.progressBar.value = 8
            }
        }

    val createEncounterRetrofitCallback = object {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {

            encounter_doctor_uuid =
                response.body().responseContents.encounterDoctor.uuid!!.toInt()
            encounter_uuid = response.body().responseContents.encounter.uuid!!.toInt()
            patientID =
                response.body().responseContents.encounterDoctor.patient_uuid!!.toInt()
            /*appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, encounter_doctor_uuid!!)
            appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
            appPreferences?.saveInt(AppConstants.PATIENT_UUID, patientID)*/
            callEmrWorkFlow()
        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CreateEncounterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CreateEncounterResponseModel::class.java
                )
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       responseModel.message!!
                   )*/
            } catch (e: Exception) {
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       getString(R.string.something_went_wrong)
                   )*/
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            /* utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 getString(R.string.something_went_wrong)
             )*/
        }

        override fun onUnAuthorized() {
            /*    utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )*/
        }

        override fun onForbidden() {
            /*   utils?.showToast(
                   R.color.negativeToast,
                   binding?.mainLayout!!,
                   getString(R.string.something_went_wrong)
               )*/
        }

        override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }

    private val getPatientByIdCallback = object {
        override fun onSuccessfulResponse(response: Response<PatientDetailResponse>) {
            val data = response.body().responseContent
            if (data != null) {
                /*if (data.salutation_details?.name != null) {
                    binding?.tvPatientName?.text =
                        " " + data.salutation_details?.name + data.first_name

                } else {
                    binding?.tvPatientName?.text = " " + data.first_name
                }*/

                binding.tvPatientName.text = " " + data.first_name
                binding.tvAgeGender.text =
                    " / " + data.age.toString() + " Year(s)" + " / " + data.gender_details.name
            } else {
                binding.tvPatientName.text = "-"
                binding.tvAgeGender.text = ""
            }


        }

        override fun onBadRequest(response: Response<PatientDetailResponse>) {
            val gson = GsonBuilder().create()
            val responseModel: GetStoreMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    GetStoreMasterResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding.mainLayout, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }

    }

    private val getPatientLatestEncCallback =
        object {
            override fun onSuccessfulResponse(response: Response<PatientLatestRecordResponse>) {
                val data = response.body().responseContents
//                if (data != null)
                /*binding?.tvConsultentView?.text =
                    data?.doctorFirstName + " / " + data?.departmentName + " / " + data?.createdDate*/
            }

            override fun onBadRequest(response: Response<PatientLatestRecordResponse>) {
                val gson = GsonBuilder().create()
                val responseModel: GetStoreMasterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        GetStoreMasterResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout,
                        responseModel.message!!
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout, failure)
            }

            override fun onEverytime() {
                viewModel!!.progressBar.value = 8
            }

        }

    private val getStoreMasterRetrofitCallback =
        object {
            override fun onSuccessfulResponse(response: Response<GetStoreMasterResponseModel>) {

                if (response.body().responseContents.isNotEmpty()!!) {
                    val listData = response.body().responseContents
                    getStoreMasterId = response.body().responseContents!!


                    for (i in getStoreMasterId.size - 1 downTo 0) {

                        if (getStoreMasterId[i].store_type_uuid == 2) {

                            store_master_uuid = getStoreMasterId[i].store_master_uuid!!.toInt()
                            /*appPreferences?.saveInt(
                                AppConstants.STOREMASTER_UUID,
                                store_master_uuid!!
                            )

                            var store_name = getStoreMasterId[i]?.store_master!!.store_name

                            appPreferences?.saveString(AppConstants.STOREMASTER_NAME, store_name)*/


                            break
                        }

                    }

                    /*  store_master_uuid = getStoreMasterId[getStoreMasterId.size - 1]?.store_master_uuid!!.toInt()
                      appPreferences?.saveInt(AppConstants.STOREMASTER_UUID,store_master_uuid!!)

                      var store_name = getStoreMasterId[getStoreMasterId.size - 1]?.store_master!!.store_name


                      appPreferences?.saveString(AppConstants.STOREMASTER_NAME,store_name)*/
                } else {


                    /*appPreferences?.saveInt(AppConstants.STOREMASTER_UUID, 0)

                    appPreferences?.saveString(AppConstants.STOREMASTER_NAME, "")*/
                }
            }

            override fun onBadRequest(response: Response<GetStoreMasterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: GetStoreMasterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        GetStoreMasterResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout,
                        responseModel.message!!
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout, failure)
            }

            override fun onEverytime() {
                viewModel!!.progressBar.value = 8
            }
        }

    fun callEmrWorkFlow() {
        viewModel.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 2)
        /*if (flow.equals(AppConstants.OUT_PATIENT)) {
            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 2)
        } else {
            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 3)
        }*/
    }

    companion object {
        private val LOG_TAG = VideoCallActivity::class.java.simpleName
    }

}