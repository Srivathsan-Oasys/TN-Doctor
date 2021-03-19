package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.databinding.FragmentCriticalCareChartCompareListBinding
import com.oasys.digihealth.doctor.utils.Utils

class CriticalCareChartCompareListFragment(
    private val compareData: LinkedHashMap<String, LinkedHashMap<String, Float>>?   //date, <name, observedValue> - colHead, <rowHead, cellValue>
) : Fragment() {

    private var binding: FragmentCriticalCareChartCompareListBinding? = null
    private var utils: Utils? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_critical_care_chart_compare_list,
            container,
            false
        )
        utils = Utils(requireActivity())

        initViews()
        listeners()

        return binding?.root!!
    }

    private fun initViews() {
        binding?.rvTable?.layoutManager = LinearLayoutManager(context)
        binding?.rvTable?.adapter = CriticalCareChartCompareListAdapter(compareData)
        populateTable()
    }

    private fun listeners() {

    }

    private fun populateTable() {
        context?.let { context ->
            val typefacePoppinsBold = ResourcesCompat.getFont(context, R.font.poppins_bold)
            binding?.llTableHeading?.addView(drawSeparator(context))
            binding?.llTableHeading?.addView(drawTextView(context, "", typefacePoppinsBold!!))
            binding?.llTableHeading?.addView(drawSeparator(context))

            compareData?.forEach { entry: Map.Entry<String, LinkedHashMap<String, Float>> ->
                binding?.llTableHeading?.addView(
                    drawTextView(
                        context,
                        entry.key,
                        typefacePoppinsBold!!
                    )
                )
            }

            binding?.rvTable?.adapter?.notifyDataSetChanged()
        }
    }

    private fun drawSeparator(context: Context): View {
        val viewDivider = View(context)
        viewDivider.layoutParams =
            ViewGroup.LayoutParams(
                (utils?.convertDpToPixel(2f, context))?.toInt() ?: 2,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        viewDivider.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.view
            )
        )
        return viewDivider
    }

    private fun drawTextView(context: Context, text: String, typeface: Typeface): TextView {
        val tv = TextView(context)
        tv.layoutParams = LinearLayout.LayoutParams(
            (utils?.convertDpToPixel(100f, context))?.toInt() ?: 100,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tv.gravity = Gravity.CENTER
        tv.text = text
        tv.typeface = typeface
        return tv
    }
}
