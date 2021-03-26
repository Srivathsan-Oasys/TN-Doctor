package com.hmis_tn.doctor.ui.emr_workflow.lab_result.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.LabResultCommentDialogFragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.LabResultViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab_result.view_model.LabResultViewModel
import com.hmis_tn.doctor.utils.Utils


class LabResultCommentDialogFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: LabResultViewModel? = null


    var binding: LabResultCommentDialogFragmentBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var facility_id: Int? = 0
    private var testMethodCode: String = ""

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

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
                R.layout.lab_result_comment_dialog_fragment,
                container,
                false
            )
        viewModel = LabResultViewModelFactory(
            requireActivity().application
        )
            .create(LabResultViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
//
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        val args = arguments

        if (args != null) {
            testMethodCode = args.getString("RESULT")!!

            binding?.supplierCodeTxt?.setText(args.getString("RESULT"))

            binding?.saveCardView?.visibility = View.GONE
            binding?.clearCardView?.visibility = View.GONE

            binding?.supplierCodeTxt?.isFocusable = false
        }



        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }


        return binding!!.root
    }


}