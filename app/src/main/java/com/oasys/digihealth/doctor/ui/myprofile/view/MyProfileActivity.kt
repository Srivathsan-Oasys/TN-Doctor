package com.oasys.digihealth.doctor.ui.myprofile.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.MyProfileLayoutBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.login.view_model.MyProfileViewModelFactory
import com.oasys.digihealth.doctor.ui.myprofile.model.MyProfileResponseModel
import com.oasys.digihealth.doctor.ui.myprofile.viewmodel.MyProfileViewModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class MyProfileActivity : AppCompatActivity() {
    private var binding: MyProfileLayoutBinding? = null
    private var viewModel: MyProfileViewModel? = null
    private var responseContents: String? = ""
    private var fragmentBackClick: FragmentBackClick? = null
    private var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Utils(this).isTablet(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        binding = DataBindingUtil.setContentView(this, R.layout.my_profile_layout)
        //
        viewModel = MyProfileViewModelFactory(
            application

        ).create(MyProfileViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(this)
        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        val facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getMyProfileFlow(
            userDetailsRoomRepository?.getUserDetails()?.uuid,
            facility_id!!,
            getMyProfileResponseCallback
        )
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding!!.toolbar.setNavigationOnClickListener { finish() }

    }

    val getMyProfileResponseCallback = object : RetrofitCallback<MyProfileResponseModel> {
        override fun onSuccessfulResponse(response: Response<MyProfileResponseModel>?) {
            binding?.name!!.text = (response?.body()?.responseContents?.user_name)
            //binding?.mobileNumText!!.text = (response?.body()?.responseContents?.mobile1)
            binding?.profileDateOfBirthTxt!!.text = (response?.body()?.responseContents?.dob)
            binding?.genderTextView!!.text = (response?.body()?.responseContents?.gender?.name)
            binding?.departmentTextView!!.text =
                (response?.body()?.responseContents?.facility?.name.toString())
            binding?.mobileTextView!!.text = (response?.body()?.responseContents?.mobile1)
            binding?.emailTextView!!.text = (response?.body()?.responseContents?.email)
            binding?.designationTextView!!.text =
                (response?.body()?.responseContents?.designation?.name)
            binding?.qualificationTextView!!.text =
                (response?.body()?.responseContents?.qualification)
            binding?.registeredNumberTextView!!.text =
                (response?.body()?.responseContents?.registration_number)
            binding?.addressTextView!!.text = (response?.body()?.responseContents?.address_line_1)
            binding?.createdOnTextView!!.text = (response?.body()?.responseContents?.created_date)
            binding?.modifiedTextView!!.text = (response?.body()?.responseContents?.modified_date)
//            Picasso.get()
//                .load(response?.body()?.responseContents?.image_url.toString())
//                .into(binding?.profile);

        }

        override fun onBadRequest(errorBody: Response<MyProfileResponseModel>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.layout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.layout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }

        }

        override fun onServerError(response: Response<*>?) {
            // Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.layout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.layout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.layout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.layout!!, failure!!)
        }

        override fun onEverytime() {
            //Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }

    }


}

