package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentCriticalCareChartCompareGraphicalBinding

class CriticalCareChartCompareGraphicalFragment() : Fragment() {

    private var compareData: LinkedHashMap<String, LinkedHashMap<String, Float>>? = null

    constructor(compareData: LinkedHashMap<String, LinkedHashMap<String, Float>>?) : this() {
        this.compareData = compareData
    }

    private var binding: FragmentCriticalCareChartCompareGraphicalBinding? = null

    private val colorList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_critical_care_chart_compare_graphical,
            container,
            false
        )

        initViews()
        listeners()

        return binding?.root!!
    }

    private fun initViews() {
        initColors()
        populateGraph(formMapsForGraph())
    }

    private fun listeners() {

    }

    private fun initColors() {
        colorList.add("#F2D7D5")
        colorList.add("#E8DAEF")
        colorList.add("#A2D9CE")
        colorList.add("#F8C471")
        colorList.add("#85C1E9")
    }

    private fun formMapsForGraph(): LinkedHashMap<String, LinkedHashMap<String, Int>> {
        var chartMap: LinkedHashMap<String, Int>?
        val totalChartMap = LinkedHashMap<String, LinkedHashMap<String, Int>>()
        compareData?.forEach { entry ->
            val dateMap = entry
            entry.value.forEach { entry1 ->
                val observedValuesMap = entry1
                chartMap = totalChartMap[entry1.key]
                if (chartMap == null)
                    chartMap = LinkedHashMap()
                val i = entry1.value.toInt()
                chartMap?.put(entry.key, i)
                totalChartMap.put(entry1.key, chartMap!!)
            }
        }
        return totalChartMap
    }

    private fun populateGraph(map: LinkedHashMap<String, LinkedHashMap<String, Int>>) {
        val lines = ArrayList<ILineDataSet>()
        map.forEach { entry ->
            val consMap = entry.value

            val graphValue1: MutableCollection<Int> = consMap.values
            val listGraphValues1: ArrayList<Int> = java.util.ArrayList<Int>(graphValue1)
            val graphKey1: MutableCollection<String> = consMap.keys
            val listKey1: ArrayList<String> = java.util.ArrayList<String>(graphKey1)

            val xAxisLabel1 = ArrayList<String>()
            for (i in listKey1.indices) {
                xAxisLabel1.add(listKey1[i])
            }

//            binding?.graphview?.getLayoutParams()?.height = 1000
//            binding?.graphview?.invalidate()

            val xAxis: XAxis = binding?.graphview?.xAxis!!
            xAxis.valueFormatter = IndexAxisValueFormatter(getDate(xAxisLabel1))
            xAxis.textSize = 12f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = ColorTemplate.getHoloBlue()
            xAxis.isEnabled = true
            xAxis.enableGridDashedLine(10f, 10f, 0f)

            val leftAxis: YAxis = binding?.graphview?.axisLeft!!
            leftAxis.removeAllLimitLines()
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            leftAxis.textColor = ColorTemplate.getHoloBlue()
            leftAxis.axisMinimum = 0f
            leftAxis.enableGridDashedLine(10f, 10f, 0f)
            leftAxis.setDrawLimitLinesBehindData(true)
            leftAxis.setDrawGridLines(true)

            binding?.graphview?.axisRight?.isEnabled = false

            val entries1 = ArrayList<Entry>()

            for (i in listKey1.indices) {
                entries1.add(Entry(i.toFloat(), listGraphValues1[i].toFloat()))
            }

            val set1 = LineDataSet(entries1, entry.key)
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.GREEN)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = Color.parseColor(colorList[lines.size % 5])
            set1.fillColor = Color.parseColor(colorList[lines.size % 5])
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider ->
                    binding?.graphview?.axisLeft?.axisMinimum!!
                }

            lines.add(set1)
        }

        val data = LineData(lines)
        data.setValueTextSize(9f)
        data.setDrawValues(false)

        binding?.graphview?.data = data
    }

    fun getDate(xAxisLabel1: ArrayList<String>): ArrayList<String>? {
        val label = java.util.ArrayList<String>()
        for (i in xAxisLabel1.indices) label.add(xAxisLabel1.get(i))
        return label
    }
}
