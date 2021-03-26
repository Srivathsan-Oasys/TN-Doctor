package com.hmis_tn.doctor.ui.login.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityLoginBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.institution.common_departmant.model.DepartmentResponseModel
import com.hmis_tn.doctor.ui.institution.common_departmant.view.fragment.DepartmentInstituteDialogFragment
import com.hmis_tn.doctor.ui.institution.lmis.model.LocationMasterResponseModel
import com.hmis_tn.doctor.ui.institution.lmis.view.fragment.LabInstituteDialogFragment
import com.hmis_tn.doctor.ui.institution.rmis.view.fragment.RadiologyInstituteDialogFragment
import com.hmis_tn.doctor.ui.login.model.*
import com.hmis_tn.doctor.ui.login.model.institution_response.InstitutionResponseModel
import com.hmis_tn.doctor.ui.login.model.login_response_model.LoginResponseContents
import com.hmis_tn.doctor.ui.login.model.login_response_model.LoginResponseModel
import com.hmis_tn.doctor.ui.login.model.login_response_model.ModuleDetail
import com.hmis_tn.doctor.ui.login.model.office_response.OfficeResponseModel
import com.hmis_tn.doctor.ui.login.setpassword.view.SetPasswordFragment
import com.hmis_tn.doctor.ui.login.view_model.LoginViewModel
import com.hmis_tn.doctor.utils.Utils
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList

class LoginActivity : AppCompatActivity() {
    private var facilityUserID: Int? = 0
    private var departmentID: Int? = 0
    private var facilitylevelID: Int? = 0
    private var module_details: List<ModuleDetail?>? = ArrayList()
    private var binding: ActivityLoginBinding? = null
    private var viewModel: LoginViewModel? = null
    private var utils: Utils? = null
    var action: Boolean? = null
    var enableeye: Boolean? = true
    var version: String? = null
    var type: String = ""
    var resourse: Resources? = null
//    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var customProgressDialog: CustomProgressDialog? = null
    var appPreferences: AppPreferences? = null

    //    private val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
//    private var firebaseDefaultMap: HashMap<String, Int>? = null
    val VERSION_CODE_KEY = "latest_app_version"
    private val TAG = "MainActivity"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = if (Utils(this).isTablet(this)) {
            this.setTheme(R.style.Hmis_LoginTablet)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            this.setTheme(R.style.Hmis_Login)
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        initView()
        languageView()
        viewModelObserve()
        btnView()
    }

    private fun languageView() {
        val language = appPreferences?.getString(AppConstants.LANGUAGE)
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        version = pInfo.versionName

        if (language?.equals("")!!) {
            appPreferences?.saveString(AppConstants.LANGUAGE, "English")
        }
    }

    private fun initView() {
        utils = Utils(this)
        customProgressDialog = CustomProgressDialog(this)
        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)
        appPreferences?.saveInt(AppConstants.WARDUUID, 0)
        appPreferences?.saveInt(AppConstants.FACILITY_UUID, 0)
        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding?.tvVersion?.text = "Version" + " " + BuildConfig.VERSION_NAME

        if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {
            binding?.tvBottomView?.text = getString(R.string.copyright_msg_pune)
        } else {
            binding?.tvBottomView?.text = getString(R.string.copyright_msg)
            binding?.tvBottomView?.text = ""
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnView() {
        binding?.click?.setOnClickListener {
            val encryptvalue = Utils.encrypt(viewModel?.password?.value)
            Log.e("username", "" + viewModel?.username?.value)
            Log.e("pwd", "" + encryptvalue)
            viewModel?.onLoginClicked(encryptvalue)
        }

        //terms And Cndition
        binding?.termsAndCndition?.setOnClickListener {
            loadTermsAndCondtions()
        }

        binding?.termsAndCndition1?.setOnClickListener {
            loadTermsAndCondtions()
        }

        binding?.termsAndCndition2?.setOnClickListener {
            loadTermsAndCondtions()
        }

        binding?.tvPrivacyPolicy?.setOnClickListener {
            loadPrivacyPolicy()
        }

        binding?.tvPrivacyPolicy1?.setOnClickListener {
            loadPrivacyPolicy()
        }

        binding?.tvPrivacyPolicy2?.setOnClickListener {
            loadPrivacyPolicy()
        }

        //validations
        binding!!.userName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize >= 3) {
                    binding!!.userName.error = null
                } else {
                    binding!!.userName.error = "Please enter minimum 3 characters"
                }
            }
        })

        binding!!.passwordEdittext.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize >= 6) {
                    binding!!.passwordEdittext.error = null
                } else {
                    binding!!.passwordEdittext.error = "Please enter minimum 6 chracters"
                }
            }
        })

        binding!!.newPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize >= 6) {
                    binding!!.newPasswordEditText.error = null
                } else {
                    binding!!.newPasswordEditText.error = "Please enter minimum 6 chracters"
                }
            }
        })

        binding!!.confirmNewPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize >= 6) {
                    binding!!.confirmNewPasswordEditText.error = null
                } else {
                    binding!!.confirmNewPasswordEditText.error = "Please enter minimum 6 chracters"
                }
            }
        })

        // password view
        binding?.passwordEdittext?.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            binding?.passwordEdittext?.clearFocus()
            binding?.passwordEdittext?.requestFocus()
            val m: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding?.passwordEdittext?.right!! - binding?.passwordEdittext?.compoundDrawables
                        ?.get(DRAWABLE_RIGHT)?.bounds?.width()!!
                ) {
                    if (this.enableeye!!) {
                        binding?.passwordEdittext?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye__open,
                            0
                        )
                        enableeye = false
                        binding?.passwordEdittext?.inputType =
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    } else {
                        binding?.passwordEdittext?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye_close,
                            0
                        )
                        enableeye = true
                        binding?.passwordEdittext?.inputType = InputType.TYPE_CLASS_TEXT or
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                    return@OnTouchListener true
                }
            }
            false
        })

        binding?.newPasswordEditText?.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            binding?.newPasswordEditText?.clearFocus()
            binding?.newPasswordEditText?.requestFocus()
            val m: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding?.newPasswordEditText?.right!! - binding?.newPasswordEditText?.compoundDrawables
                        ?.get(DRAWABLE_RIGHT)?.bounds?.width()!!
                ) {
                    if (this.enableeye!!) {
                        binding?.newPasswordEditText?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye__open,
                            0
                        )
                        enableeye = false
                        binding?.newPasswordEditText?.inputType =
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    } else {
                        binding?.newPasswordEditText?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye_close,
                            0
                        )
                        enableeye = true
                        binding?.newPasswordEditText?.inputType = InputType.TYPE_CLASS_TEXT or
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                    return@OnTouchListener true
                }
            }
            false
        })

        binding?.rlLogin?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff: Int =
                binding?.rlLogin?.rootView?.height!! - binding?.rlLogin?.height!!
            if (heightDiff > dpToPx(this@LoginActivity, 250F)) {
                binding?.rlBottomLayout?.visibility = View.GONE
                binding?.rlBottomLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.transparent
                    )
                )
            } else {
                binding?.rlBottomLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                binding?.rlBottomLayout?.visibility = View.VISIBLE
            }
        }
    }

    private fun viewModelObserve() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
            LoginViewModel::class.java
        )
        viewModel!!.loginRetrofitCallBack = loginRetrofitCallBack
        viewModel!!.otpRetrofitCallBack = otpRetrofitCallBack
        viewModel!!.changePasswordRetrofitCallBack = changePasswordRetrofitCallBack
        binding!!.lifecycleOwner = this
        binding?.viewModel = viewModel
        viewModel!!.errorText.observe(this,
            androidx.lifecycle.Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        viewModel!!.progress.observe(this, androidx.lifecycle.Observer {
            if (it == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (it == View.GONE) {
                customProgressDialog!!.dismiss()
            }
        })
    }

    private fun dpToPx(context: Context, valueInDp: Float): Float {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
    }

    private fun navigateToPlayStore() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                        or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
        }
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    private fun getCurrentVersionCode(): Int {
        try {
            return packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    val loginRetrofitCallBack = object : RetrofitCallback<LoginResponseModel?> {

        override fun onSuccessfulResponse(response: Response<LoginResponseModel?>) {
            if (response.body()!!.statusCode == 200) {
                loginSeassion(response.body()!!.responseContents!!)
                if (response.body()!!.responseContents!!.userDetails!!.first_time_login != null &&
                    response.body()!!.responseContents!!.userDetails!!.first_time_login != false
                ) {
                    //user Detials saved localy
                    val userDetailsRoomRepository = UserDetailsRoomRepository(application)
                    userDetailsRoomRepository.insertData(response.body()!!.responseContents?.userDetails!!)
                    appPreferences?.saveInt(AppConstants.LAB_UUID, 0)
                    appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
                    appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID, "")
                    module_details = response.body()?.responseContents?.moduleDetails

                    // Land Screen control
                    val landString =
                        response.body()?.responseContents?.activityDetails?.get(0)!!.activity!!.code
                    appPreferences?.saveString(AppConstants.LANDSCREEN, landString)

                    // check emr role
                    val checkEMR =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_EMR }
                    appPreferences?.saveBoolean(AppConstants.EMRCHECK, checkEMR)

                    // check registration
                    val checkREGISTER =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_Registration }
                    appPreferences?.saveBoolean(AppConstants.REGISTRATIONCHECK, checkREGISTER)

                    //Lmis
                    val checkLIMS =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_LMIS }
                    appPreferences?.saveBoolean(AppConstants.LMISCHECK, checkLIMS)

                    //RMIS
                    val CheckRMIS =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_RMIS }
                    appPreferences?.saveBoolean(AppConstants.CHECK_RMIS, CheckRMIS)

                    // Modules List
                    for (i in module_details!!.indices) {
                        //Lmis
                        if (module_details!![i]?.code == AppConstants.ROLE_LMIS) {
                            val details = module_details!![i]!!.role_activities
                            val checkLabTest =
                                details!!.any { it!!.activity.code == AppConstants.LABTESTCODE }
                            appPreferences?.saveBoolean(AppConstants.LABTEST, checkLabTest)
                            val checkLabApprovel =
                                details.any { it!!.activity.code == AppConstants.LABAPPROVELCODE }
                            appPreferences?.saveBoolean(
                                AppConstants.LABAPPROVEL,
                                checkLabApprovel
                            )
                            val checkLabProcess =
                                details.any { it!!.activity.code == AppConstants.LABTESTCODE }
                            appPreferences?.saveBoolean(
                                AppConstants.LABPROCESS,
                                checkLabProcess
                            )
                            val checkLabneworder =
                                details.any { it!!.activity.code == AppConstants.LABNEWORDERCODE }
                            appPreferences?.saveBoolean(
                                AppConstants.LABNEWORDER,
                                checkLabneworder
                            )
                            val checkLabsampledispatch =
                                details.any { it!!.activity.code == AppConstants.LABSAMPLEDISPATCHCODE }
                            appPreferences?.saveBoolean(
                                AppConstants.LABSAMPLEDISPATCH,
                                checkLabsampledispatch
                            )
                            val checkLabresultdispatch =
                                details.any { it!!.activity.code == AppConstants.LABRESULTDISPATCHCODE }
                            appPreferences?.saveBoolean(
                                AppConstants.LABRESULTDISPATCH,
                                checkLabresultdispatch
                            )
                            val checkOrderStatus =
                                details.any { it!!.activity.code == AppConstants.LABORDERSTATUSCODE }
                            appPreferences?.saveBoolean(
                                AppConstants.LABORDERSTATUS,
                                checkOrderStatus
                            )
                        }

                        //RMIS
                        if (module_details!![i]?.code == AppConstants.ROLE_RMIS) {
                            val details = module_details!![i]!!.role_activities
                            val checkLabTest =
                                details!!.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TEST }
                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TEST,
                                checkLabTest
                            )
                            val checkLabApprovel =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TESTAPPROVAL }
                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TESTAPPROVAL,
                                checkLabApprovel
                            )
                            val checkLabProcess =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TESTPROCESS }
                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TESTPROCESS,
                                checkLabProcess
                            )
                            val checkLabneworder =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TECH }
                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TECH,
                                checkLabneworder
                            )
                            val checkLabsampledispatch =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_DISPATCH }
                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_DISPATCH,
                                checkLabsampledispatch
                            )
                            val checkOrderStatus =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_ORDER }
                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_ORDER,
                                checkOrderStatus
                            )
                        }
                    }

                    // Check valid Login If only lMIS or RMIS is there
                    if ((checkLIMS || CheckRMIS) || (checkEMR || checkREGISTER)) {
                        if (module_details?.size!! > 0) {
                            val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
                            Log.i("", "" + userDataStoreBean?.user_type?.revision)
                            if (response.body()!!.responseContents?.userDetails!!.user_type!!.code != null
                                && response.body()!!.responseContents?.userDetails!!.user_type!!.code != ""
                            ) {
                                type =
                                    response.body()!!.responseContents?.userDetails!!.user_type!!.code!!
                                appPreferences?.saveString(AppConstants.LOGINTYPE, type)
                            } else {
                                type =
                                    response.body()!!.responseContents?.userDetails!!.user_type!!.name!!
                                appPreferences?.saveString(AppConstants.LOGINTYPE, type)
                            }
//                            type= "Lab Incharge"
                            if (response.body()!!.responseContents?.userDetails?.office_user == null ||
                                response.body()!!.responseContents?.userDetails?.office_user == false
                            ) {
                                viewModel!!.getfacilityCallback(
                                    response.body()!!.responseContents?.userDetails?.uuid,
                                    facilitycallbackRetrofitCallback
                                )
                            } else {
                                viewModel!!.getOfficeList(officeRetrofitCallBack)
                            }
                        }
                    } else {
                        toast("Invalid User Please contect Admin to check your Controller")
                    }
                    //   Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel!!.username.value = ""
                    viewModel!!.password.value = ""
                    val userDetailsRoomRepository = UserDetailsRoomRepository(application)
                    userDetailsRoomRepository.insertData(response.body()!!.responseContents?.userDetails!!)

                    val ft = supportFragmentManager.beginTransaction()
                    val dialog = SetPasswordFragment()
                    dialog.show(ft, "Tag")
                }
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    response.body()!!.msg.toString()
                )
            }
        }

        override fun onBadRequest(response: Response<LoginResponseModel?>) {
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*/
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*/
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.unauthorized))
*/
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*/
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(this@LoginActivity, s)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun loginSeassion(responseContents: LoginResponseContents) {
        val sessionRequest = LoginSessionRequest()
        sessionRequest.ApplicationId = 10
        sessionRequest.Password = responseContents.userDetails?.SessionId
        sessionRequest.LoginId = responseContents.userDetails?.uuid.toString()
        sessionRequest.Logininfo = responseContents.userDetails?.first_name
        sessionRequest.RoleInfo = responseContents.userDetails?.role?.role_name
        sessionRequest.UserName = responseContents.userDetails?.first_name
        sessionRequest.UserTypeId = responseContents.userDetails?.user_type_uuid.toString()
        if (Utils.isNetworkConnected(application)) {
            // Internet available
            if (Utils.isConnectedWifi(application)) {
                // WIFI
                val wm =
                    applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val ipAddress: String =
                    BigInteger.valueOf(wm.dhcpInfo.netmask.toLong()).toString()
                sessionRequest.IPAddress = ipAddress
            }
            // Internet available
            if (Utils.isConnectedInternet(application)) {
                // MOBILE DATA
                sessionRequest.IPAddress = utils?.getIPAddress(true)
            }
        }
        viewModel?.Loginseassion(responseContents, sessionRequest, LoginSeasionRetrofitCallBack)
    }


    val LoginSeasionRetrofitCallBack = object : RetrofitCallback<SimpleResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel?>) {

        }

        override fun onBadRequest(response: Response<SimpleResponseModel?>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    val officeRetrofitCallBack =
        object : RetrofitCallback<OfficeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<OfficeResponseModel?>) {
                Log.i("", "" + responseBody.body())
                if (responseBody.body()?.responseContents!!.isNotEmpty()) {

                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, 0)
                    appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
                    //startActivity(Intent(this@LoginActivity, MainLandScreenActivity::class.java))
                    // finish()


                }

            }

            override fun onBadRequest(errorBody: Response<OfficeResponseModel?>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, s!!)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    val otpRetrofitCallBack = object : RetrofitCallback<ChangePasswordOTPResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<ChangePasswordOTPResponseModel?>) {
            if (responseBody.body()?.status == "success") {
                Log.e("SendOTP", responseBody.body()?.toString()!!)
                viewModel!!.forgetUsernemeLayout.value = 8
                viewModel!!.changePasswordLayout.value = 0
                viewModel!!.otp.value = responseBody.body()?.responseContents?.otp
                viewModel!!.otp.value = responseBody.body()?.responseContents?.otp
                Toast.makeText(this@LoginActivity, responseBody.body()?.msg, Toast.LENGTH_LONG)
                    .show()
                //binding?.otpLayout!!.visibility = View.GONE
            }

        }

        override fun onBadRequest(response: Response<ChangePasswordOTPResponseModel?>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val changePasswordRetrofitCallBack = object : RetrofitCallback<PasswordChangeResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<PasswordChangeResponseModel?>) {
            viewModel!!.visisbleLogin()
            Toast.makeText(this@LoginActivity, responseBody.body()?.msg!!, Toast.LENGTH_LONG)
                .show()
        }

        override fun onBadRequest(response: Response<PasswordChangeResponseModel?>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    val facilitycallbackRetrofitCallback =
        object : RetrofitCallback<InstitutionResponseModel> {

            override fun onSuccessfulResponse(responseBody: Response<InstitutionResponseModel?>) {
                Log.i("", "" + responseBody.body()?.responseContents)
                if (responseBody.body()?.responseContents?.size!! > 1) {
                    // CHeck  RMIS INstutuion Dialog
                    when (type) {
                        AppConstants.LABINCHARGE -> {
                            val ft = supportFragmentManager.beginTransaction()
                            val dialog = LabInstituteDialogFragment()
                            dialog.show(ft, "Tag")
                        }
                        //Check  RMIS INstutuion Dialog
                        AppConstants.RADINCHARGE -> {
                            val ft = supportFragmentManager.beginTransaction()
                            val dialog = RadiologyInstituteDialogFragment()
                            dialog.show(ft, "Tag")
                        }
                        // common INstutuion Dialog
                        else -> {
                            val ft = supportFragmentManager.beginTransaction()
                            val dialog = DepartmentInstituteDialogFragment()
                            dialog.show(ft, "Tag")
                        }
                    }
                } else if (responseBody.body()?.responseContents?.size!! == 1) {
                    facilitylevelID = responseBody.body()?.responseContents?.get(0)?.facility_uuid
                    facilityUserID = responseBody.body()?.responseContents?.get(0)!!.user_uuid
                    appPreferences?.saveString(
                        AppConstants.INSTITUTION_NAME,
                        responseBody.body()?.responseContents?.get(0)!!.facility?.name
                    )
                    viewModel?.getDepartmentList(facilitylevelID, facilityUserID, depatmentCallback)
                } else {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.Instutionmaperror)
                    )
                }
            }

            override fun onBadRequest(errorBody: Response<InstitutionResponseModel?>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    /*
    Department
     */
    val depatmentCallback = object :
        RetrofitCallback<DepartmentResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<DepartmentResponseModel?>) {
            Log.i("", "" + responseBody.body()?.responseContents)
            val jsonresponse = Gson().toJson(responseBody.body()?.responseContents)
            Log.i("", "" + jsonresponse)
            if (responseBody.body()?.responseContents?.size!! > 1) {
                when (type) {
                    AppConstants.LABINCHARGE -> {
                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = LabInstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    }
                    AppConstants.RADINCHARGE -> {
                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = RadiologyInstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    }
                    else -> {
                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = DepartmentInstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    }
                }
            } else if (responseBody.body()?.responseContents?.size!! == 1) {
                facilitylevelID = responseBody.body()?.responseContents?.get(0)?.facility_uuid
                departmentID = responseBody.body()?.responseContents?.get(0)?.department_uuid
                val departmentName = responseBody.body()?.responseContents?.get(0)?.department?.name
                if (type == AppConstants.LABINCHARGE) {
                    viewModel!!.getLocationMaster(
                        facilitylevelID!!, LocationMasterResponseCallback
                    )
                } else {
                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                    appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, departmentID!!)
                    appPreferences?.saveString(AppConstants.DEPARTMENT_NAME, departmentName!!)
                    //    startActivity(Intent(this@LoginActivity, MainLandScreenActivity::class.java))
                    //   finish()
                }
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.Departmantmaperror)
                )
            }
        }

        override fun onBadRequest(errorBody: Response<DepartmentResponseModel?>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val LocationMasterResponseCallback = object : RetrofitCallback<LocationMasterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModel?>) {
            val data = responseBody.body()!!.responseContents
            if (data.isNotEmpty()) {
                if (responseBody.body()?.responseContents?.size!! > 1) {
                    val ft = supportFragmentManager.beginTransaction()
                    val dialog = LabInstituteDialogFragment()
                    dialog.show(ft, "Tag")
                } else {
                    val labUUID: Int = responseBody.body()!!.responseContents[0].uuid
                    val LabdepartmentId =
                        responseBody.body()!!.responseContents[0].department_uuid
                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                    appPreferences?.saveInt(AppConstants.LAB_UUID, labUUID)

                    if (LabdepartmentId != null) {
                        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, LabdepartmentId)
                    } else {
                        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
                    }
                    val tolocationMap =
                        responseBody.body()!!.responseContents[0].to_location_department_maps
                    if (tolocationMap.isNotEmpty()) {
                        val otherdepaertment: ArrayList<Int> = ArrayList()
                        for (i in tolocationMap.indices) {
                            otherdepaertment.add(tolocationMap[i].department_uuid)
                        }
                        val res = otherdepaertment.toString()
                        Log.i("department", "" + res)
                        appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID, res)
                    }

                    //  startActivity(Intent(this@LoginActivity, MainLandScreenActivity::class.java))
                    //finish()
                }
            }
        }

        override fun onBadRequest(errorBody: Response<LocationMasterResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody.errorBody()!!.string(),
                    LocationMasterResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun loadTermsAndCondtions() {
        val intent = Intent(this@LoginActivity, TermsAndConditionActivity::class.java)
        intent.putExtra("isTermsCondition", true)
        startActivity(intent)
    }

    private fun loadPrivacyPolicy() {
        val intent = Intent(this@LoginActivity, TermsAndConditionActivity::class.java)
        intent.putExtra("isTermsCondition", false)
        startActivity(intent)
    }

}