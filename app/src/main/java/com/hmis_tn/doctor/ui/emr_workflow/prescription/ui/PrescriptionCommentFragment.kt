package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.databinding.DialogCommentBinding
import com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model.PrescriptionCommentViewModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model.PrescriptionCommentViewModelFactory

class PrescriptionCommentFragment : DialogFragment() {

    private var content: String? = null
    var binding: DialogCommentBinding? = null
    private var viewModel: PrescriptionCommentViewModel? = null

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
            DataBindingUtil.inflate(inflater, R.layout.dialog_comment, container, false)
        viewModel = PrescriptionCommentViewModelFactory(
            requireActivity().application
        )
            .create(PrescriptionCommentViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        viewModel!!.errorText.observe(
            this.activity!!,
            Observer { toastMessage ->
                //utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
                Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show()
            })



        return binding?.root
    }


}