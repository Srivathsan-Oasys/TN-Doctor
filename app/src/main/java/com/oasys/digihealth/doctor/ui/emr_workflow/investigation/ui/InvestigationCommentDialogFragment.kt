package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.InvestigationCommentDialogFragmentBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils


class InvestigationCommentDialogFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: InvestigationViewModel? = null


    var binding: InvestigationCommentDialogFragmentBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var facility_id: Int? = 0
    private var testMethodCode: String = ""
    private var mainCallback: CommandClickedListener? = null


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
                R.layout.investigation_comment_dialog_fragment,
                container,
                false
            )
        viewModel = InvestigationViewModelFactory(
            requireActivity().application
        )
            .create(InvestigationViewModel::class.java)

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
            val getPosition = args.getInt("position")
            val commands = args.getString("commands")

            binding?.supplierCodeTxt?.setText(commands)
        }

        binding?.saveCardView?.setOnClickListener {
            val args = arguments
            if (args != null) {
                val getPosition = args.getInt("position")

                val commands = args.getString("commands")

                Log.i("get", "" + getPosition)
                val str_commmends = binding?.supplierCodeTxt?.text?.toString()

                mainCallback!!.sendCommandPosData(
                    getPosition,
                    str_commmends!!
                )

                dialog?.dismiss()
            }

        }




        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding!!.clearCardView.setOnClickListener {

            binding?.supplierCodeTxt?.setText("")
        }


        return binding!!.root
    }

    interface CommandClickedListener {
        fun sendCommandPosData(
            position: Int,
            command: String
        )
    }

    fun setOnTextClickedListener(callback: CommandClickedListener) {
        this.mainCallback = callback
    }


}