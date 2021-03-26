package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.ui

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.item_critical_care_chart_table.view.*

class CriticalCareChartCompareListAdapter(
    private val map: LinkedHashMap<String, LinkedHashMap<String, Float>>?
) : RecyclerView.Adapter<CriticalCareChartCompareListAdapter.MyViewHolder>() {

    private var utils: Utils? = null
    private var date: String = ""

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_critical_care_chart_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        val rowHead = try {
            map?.keys?.toTypedArray()?.get(0)
        } catch (e: Exception) {
            ""
        }
        val a = map?.get(rowHead)
        return a?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.llRow.removeAllViews()
        val date = try {
            map?.keys?.toTypedArray()?.get(position)
        } catch (e: Exception) {
            ""
        }
        val a = map?.get(date)
        val rowHead = a?.keys?.toTypedArray()?.get(0)

        a?.forEach { entry ->
            val b = a[entry.key]
        }

        utils = Utils(holder.itemView.context)
        var layoutPopulated: Boolean
        val typefacePoppins = ResourcesCompat.getFont(holder.itemView.context, R.font.poppins)

        holder.itemView.llRow.addView(
            drawSeparator(
                holder.itemView.context,
                (utils?.convertDpToPixel(2f, holder.itemView.context))?.toInt() ?: 2,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        holder.itemView.llRow.addView(
            drawTextView(
                holder.itemView.context,
                (utils?.convertDpToPixel(100f, holder.itemView.context))?.toInt() ?: 100,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                rowHead ?: "",
                typefacePoppins!!
            )
        )
        holder.itemView.llRow.addView(
            drawSeparator(
                holder.itemView.context,
                (utils?.convertDpToPixel(2f, holder.itemView.context))?.toInt() ?: 2,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        val listSize = map?.get(date)?.size ?: 0
        map?.forEach { entry ->
            layoutPopulated = false
            for (i in 0 until listSize) {
                val x = map[date]
                val y = x?.get(x.keys.toTypedArray().get(i))
//                if(rowHead == x?.keys?.toTypedArray()?.get(i)) {
                if (entry.key == date) {
                    holder.itemView.llRow.addView(
                        drawTextView(
                            holder.itemView.context,
                            (utils?.convertDpToPixel(100f, holder.itemView.context))?.toInt()
                                ?: 100,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            map[date]?.get(map[date]?.keys?.toTypedArray()?.get(i))?.toString()
                                ?: "",
                            typefacePoppins
                        )
                    )
                    layoutPopulated = true
                }
            }
        }
    }

    private fun drawSeparator(
        context: Context, width: Int, height: Int
    ): View {
        val viewDivider = View(context)
        viewDivider.layoutParams =
            ViewGroup.LayoutParams(
                width,
                height
            )
        viewDivider.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.view
            )
        )
        return viewDivider
    }

    private fun drawTextView(
        context: Context, width: Int, height: Int, text: String, typeface: Typeface
    ): TextView {
        val tv = TextView(context)
        tv.layoutParams = LinearLayout.LayoutParams(
            width,
            height
        )
        tv.gravity = Gravity.CENTER
        tv.text = text
        tv.typeface = typeface
        return tv
    }

}
