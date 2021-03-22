package com.oasys.digihealth.doctor.ui.sample_dispatch.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogSampleRejectListBinding
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.oasys.digihealth.doctor.ui.sample_dispatch.view_model.SampleDispatchViewModel
import com.oasys.digihealth.doctor.ui.sample_dispatch.view_model.SampleDispatchViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import java.util.*


class SampleDispatchRejectDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: SampleDispatchViewModel? = null
    var binding: DialogSampleRejectListBinding? = null

    private var customProgressDialog: CustomProgressDialog? = null
    var selectData: Int = 0
    private var favouriteData: ArrayList<SendIdList> = ArrayList()

    private var ref = mutableMapOf<Int, String>()

    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
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
            DataBindingUtil.inflate(inflater, R.layout.dialog_sample_reject_list, container, false)
        viewModel = SampleDispatchViewModelFactory(
            requireActivity().application

        )
            .create(SampleDispatchViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.closeImageView?.setOnClickListener {
            //Call back
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            //Call back
            dialog?.dismiss()
        }
        return binding?.root
    }
}

