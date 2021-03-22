package com.oasys.digihealth.doctor.ui.dashboard.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogChangePasswordBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.dashboard.model.ChangePasswordOTPResponseModel
import com.oasys.digihealth.doctor.ui.dashboard.model.PasswordChangeResponseModel
import com.oasys.digihealth.doctor.ui.dashboard.view_model.ChangePasswordViewModel
import com.oasys.digihealth.doctor.ui.dashboard.view_model.ChangePasswordViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class ChangePasswordFragemnt : DialogFragment() {

    private var content: String? = null
    var binding: DialogChangePasswordBinding? = null
    private var viewModel: ChangePasswordViewModel? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var utils: Utils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_change_password, container, false)
        viewModel = ChangePasswordViewModelFactory(
            requireActivity().application
        )
            .create(ChangePasswordViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this


        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application!!)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        binding?.userNameEditText?.setOnKeyListener(null)
        binding?.userNameEditText?.setText(userDataStoreBean?.user_name)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        viewModel!!.errorText.observe(
            this.requireActivity(),
            Observer { toastMessage ->
                //utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
                Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show()
            })

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.sendOTPButton?.setOnClickListener {
            viewModel?.getOtp(
                userDataStoreBean?.user_name.toString(),
                facility_uuid!!, otpRetrofitCallBack
            )
        }

        binding!!.newPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length


                if (datasize >= 6) {
                    binding!!.newPassword.error = null
                } else {
                    binding!!.newPassword.error =
                        "Please enter minimum 6 chracters at least 1 number and 1 Alphectics"
                }
            }
        })

        binding!!.confirmPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize >= 6) {
                    binding!!.confirmPassword.error = null
                } else {
                    binding!!.confirmPassword.error =
                        "Please enter minimum 6 chracters at least 1 number and 1 Alphectics"
                }
            }
        })

        binding?.changePasswordButton?.setOnClickListener {


            viewModel?.onChangePassword(
                userDataStoreBean?.user_name.toString(),
                viewModel?.enterOTPEditText?.value!!,
                Utils.encrypt(viewModel?.enterNewPasswordEditText?.value.toString()).toString(),
                changePasswordRetrofitCallBack
            )


        }
        return binding?.root
    }


    val otpRetrofitCallBack = object:RetrofitCallback<ChangePasswordOTPResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<ChangePasswordOTPResponseModel>?) {

            if (responseBody?.body()?.status == "success") {
                viewModel?.enterOTPEditText!!.value = responseBody.body()?.responseContents?.otp

                Toast.makeText(activity, responseBody.body()?.msg, Toast.LENGTH_LONG).show()
                //        viewModel?.enterOTPEditText!!.value = responseBody.body()?.responseContents?.otp
                binding?.otpLayout!!.visibility = View.GONE
            }

        }

        override fun onBadRequest(response: Response<ChangePasswordOTPResponseModel>?) {
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
            viewModel!!.progressBar.value = 8
        }
    }


    val changePasswordRetrofitCallBack = object:RetrofitCallback<PasswordChangeResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<PasswordChangeResponseModel>?) {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.msg!!
            )

            Toast.makeText(activity, responseBody?.body()?.msg!!, Toast.LENGTH_LONG).show()


        }

        override fun onBadRequest(response: Response<PasswordChangeResponseModel>?) {
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
            viewModel!!.progressBar.value = 8
        }
    }


}