package com.hmis_tn.doctor.ui.login.setpassword.view

import android.app.Application
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
import androidx.lifecycle.ViewModelProvider
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogFirsttimeChangepasswordBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.login.model.SimpleResponseModel
import com.hmis_tn.doctor.ui.login.setpassword.view_model.SetPasswordViewModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class SetPasswordFragment : DialogFragment() {
    private var content: String? = null
    var binding: DialogFirsttimeChangepasswordBinding? = null
    private var viewModel: SetPasswordViewModel? = null
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
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_firsttime_changepassword,
                container,
                false
            )
/*
            viewModel = SetPasswordViewModelFactory(
                requireActivity().application)
                .create(SetPasswordViewModel::class.java)
*/
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(Application()).create(
            SetPasswordViewModel::class.java
        )
        binding?.viewModel = this.viewModel
        binding?.lifecycleOwner = this


        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application!!)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        binding?.userNameEditText?.setOnKeyListener(null)
        binding?.userNameEditText?.setText(userDataStoreBean?.user_name)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        viewModel!!.errorText.observe(
            this,
            Observer { toastMessage ->
                //utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
                Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show()
            })

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
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
                    binding!!.newPassword.error = "Please enter minimum 6 chracters"
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
                    binding!!.confirmPassword.error = "Please enter minimum 6 chracters"
                }
            }
        })

        binding?.changePasswordButton?.setOnClickListener {


            viewModel?.onChangePassword(
                userDataStoreBean?.user_name.toString(),
                Utils.encrypt(viewModel?.enterNewPasswordEditText?.value.toString()).toString(),
                changePasswordRetrofitCallBack
            )


        }
        return binding?.root
    }


    val changePasswordRetrofitCallBack = object : RetrofitCallback<SimpleResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel?>) {

            toast(getString(R.string.password_changed))
            dialog!!.dismiss()

        }

        override fun onBadRequest(response: Response<SimpleResponseModel?>) {

            toast(getString(R.string.something_went_wrong))

        }

        override fun onServerError(response: Response<*>?) {

            toast(getString(R.string.something_went_wrong))
        }

        override fun onUnAuthorized() {

            toast(getString(R.string.unauthorized))

        }

        override fun onForbidden() {

            toast(getString(R.string.something_went_wrong))

        }

        override fun onFailure(s: String?) {
            if (s != null) {
                toast(s)
            }
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }

    private fun toast(s: String) {

        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()

    }


}