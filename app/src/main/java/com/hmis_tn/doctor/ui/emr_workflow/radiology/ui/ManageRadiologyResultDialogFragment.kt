package com.hmis_tn.doctor.ui.emr_workflow.radiology.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogManageRadiologyResultBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ManageLabPrevLabAdapter
import com.hmis_tn.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyResultViewModel
import com.hmis_tn.doctor.ui.emr_workflow.radiology.view_model.ManageRadiologyResultViewModelFactory
import com.hmis_tn.doctor.utils.Utils


class ManageRadiologyResultDialogFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: ManageRadiologyResultViewModel? = null
    private var managePrevLabAdapter: ManageLabPrevLabAdapter? = null
    var binding: DialogManageRadiologyResultBinding? = null
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
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_manage_radiology_result,
                container,
                false
            )
        viewModel = ManageRadiologyResultViewModelFactory(
            requireActivity().application
        )
            .create(ManageRadiologyResultViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        return binding!!.root
    }


}