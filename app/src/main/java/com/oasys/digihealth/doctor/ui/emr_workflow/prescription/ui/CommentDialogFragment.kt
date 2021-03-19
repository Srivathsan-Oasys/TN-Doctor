package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.CommentDialogFragmentBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionInfoResponsModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.CommentViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.CommentViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils


class CommentDialogFragment : DialogFragment() {
    private var content: String? = null
    private var routeSpinnerList = mutableMapOf<Int, String>()
    private var frequencySpinnerList = mutableMapOf<Int, String>()
    private var instructionSpinnerList = mutableMapOf<Int, String>()
    private var viewModel: CommentViewModel? = null
    private var prescriptionInfoData: PrescriptionInfoResponsModel? = null

    private var mainCallback: CommandClickedListener? = null

    var binding: CommentDialogFragmentBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var facility_id: Int? = 0
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
            DataBindingUtil.inflate(inflater, R.layout.comment_dialog_fragment, container, false)
        viewModel = CommentViewModelFactory(
            requireActivity().application
        )
            .create(CommentViewModel::class.java)

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

        binding!!.clearCardView.setOnClickListener {

            binding?.supplierCodeTxt?.setText("")
        }


        binding?.saveCardView?.setOnClickListener {


            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

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

        return binding!!.root
    }

    interface CommandClickedListener {
        fun sendCommandPosData(
            position: Int,
            command: String
        )
    }

    fun setOnTextClickedListener(callback: CommandClickedListener) {
        mainCallback = callback
    }


}