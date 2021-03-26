package com.hmis_tn.doctor.ui.quick_reg.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogOrderTestListBinding
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestProcessViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestProcessViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import java.util.*

class TestProcessOrderDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: LabTestProcessViewModel? = null
    var binding: DialogOrderTestListBinding? = null
    private val labTestList: MutableList<RecyclerDto> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null
    private var mAdapter: OrderProcessPrentAdapter? = null

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
            DataBindingUtil.inflate(inflater, R.layout.dialog_order_test_list, container, false)
        viewModel = LabTestProcessViewModelFactory(
            requireActivity().application

        )
            .create(LabTestProcessViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())

/*
        prepareLabTestData()
*/
        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.orderProcessDateRecyclerView!!.layoutManager = layoutmanager
        mAdapter = OrderProcessPrentAdapter(requireContext(), labTestList)
        binding?.orderProcessDateRecyclerView!!.adapter = mAdapter
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        return binding?.root
    }

//    private fun prepareLabTestData() {
//        var labApproval = RecyclerDto("mr.Santhosh kumar/21m", "20-04-2020", "Created")
//        labTestList.add(labApproval)
//        labApproval = RecyclerDto("mr.Santhosh/21m", "03215", "Accepted")
//        labTestList.add(labApproval)
//        labApproval = RecyclerDto("mr.Santhosh kumar/21m", "01012", "Rejected")
//        labTestList.add(labApproval)
//    }


}

