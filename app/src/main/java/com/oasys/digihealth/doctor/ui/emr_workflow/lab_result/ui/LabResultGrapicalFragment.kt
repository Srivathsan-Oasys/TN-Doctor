package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.databinding.LabResultGrapicalFragmentBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare.ResponseContent

class LabResultGrapicalFragment(
    private val list: ArrayList<ResponseContent>,
    private val backClick: () -> Unit,
    private val cancelClick: () -> Unit
) : Fragment() {

    private var binding: LabResultGrapicalFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.lab_result_grapical_fragment,
                container,
                false
            )

        initViews()
        listeners()

        return binding!!.root
    }

    private fun initViews() {
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(getDataPoint())
        binding?.graphView?.addSeries(series)
    }

    private fun listeners() {
        binding?.nextCardView?.setOnClickListener {
            backClick()
        }

        binding?.cancelCardView?.setOnClickListener {
            cancelClick()
        }
    }

    private fun getDataPoint(): Array<DataPoint> {
        return arrayOf<DataPoint>(
            DataPoint(0.0, 1.0),
            DataPoint(2.0, 5.0),
            DataPoint(3.0, 1.0),
            DataPoint(5.0, 6.0),
            DataPoint(8.0, 3.0)
        )
    }
}
