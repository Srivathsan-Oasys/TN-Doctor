package com.hmis_tn.doctor.ui.emr_workflow.radiology_result.ui

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.radiology_result.model.RadiologyTopResponseContent
import kotlinx.android.synthetic.main.radiology_result_parent_layout.view.*


class RadiologyResultParentAdapter(
    private val mContext: Context,
    private var radiologyResultList: ArrayList<RadiologyTopResponseContent>


) : RecyclerView.Adapter<RadiologyResultParentAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String

    private var bytestream: Bitmap? = null

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val dateTextView: TextView
        val DescriptionText: TextView
        val DescriptionText2: TextView


        val resultLinearLayout: CardView
        val previewLinearLayout: LinearLayout
        val webview: WebView


        internal var recyclerView: RecyclerView

        init {

            dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView
            DescriptionText = view.findViewById<View>(R.id.DescriptionText) as TextView
            DescriptionText2 = view.findViewById<View>(R.id.DescriptionText2) as TextView
            webview = view.findViewById<View>(R.id.detaliswebview) as WebView

            resultLinearLayout = view.findViewById(R.id.resultLinearLayout)
            previewLinearLayout = view.findViewById<View>(R.id.previewLinearLayout) as LinearLayout
            recyclerView = view.findViewById(R.id.child_recycler)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.radiology_result_parent_layout,
            parent,
            false
        ) as CardView
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = radiologyResultList.get(position).toString()
        val resultListList = radiologyResultList.get(position)
        try {

            holder.dateTextView.text =
                resultListList.order_request_date + "-" + resultListList.test_master + "-" + "-" + resultListList.department + "-" + resultListList.encounter_type
            holder.DescriptionText.text = resultListList.test_master
            holder.DescriptionText2.text = resultListList.repsonse.get(position).result_value


        } catch (e: Exception) {

        }


        holder.webview.settings.javaScriptEnabled = true
        holder.webview.loadData(
            radiologyResultList.get(position).repsonse.get(0).result_value,
            "text/html; charset=utf-8",
            "UTF-8"
        )
        holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
        val myOrderChildAdapter = RadiologyResultChildAdapter(mContext)
        val gridLayoutManagerHospitals = GridLayoutManager(
            mContext, 3,
            LinearLayoutManager.HORIZONTAL, false
        )
        holder.recyclerView.layoutManager = gridLayoutManagerHospitals
        holder.recyclerView.adapter = myOrderChildAdapter
        val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        holder.recyclerView.addItemDecoration(itemDecor)
        holder.recyclerView.adapter = myOrderChildAdapter

        if (position != 0) {

            holder.resultLinearLayout.visibility = View.GONE

        } else {
            holder.resultLinearLayout.visibility = View.VISIBLE


        }

        holder.previewLinearLayout.setOnClickListener {

            if (holder.resultLinearLayout.visibility == View.VISIBLE) {

                holder.resultLinearLayout.visibility = View.GONE

            } else {
                holder.resultLinearLayout.visibility = View.VISIBLE


            }
        }

        if (resultListList.repsonse[0].bytestream != null) {
            holder.itemView.imageView.visibility = View.VISIBLE
            holder.itemView.imageView.setImageBitmap(resultListList.repsonse[0].bytestream)
        } else {

            holder.itemView.imageView.visibility = View.GONE
        }
    }

    fun refreshList(preLabArrayList: ArrayList<RadiologyTopResponseContent>?) {
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return radiologyResultList.size
    }

    fun setData(labResultLIst: ArrayList<RadiologyTopResponseContent>) {

        this.radiologyResultList = labResultLIst

        notifyDataSetChanged()

    }

    fun setImage(byteStream: Bitmap, imgposition: Int) {

        radiologyResultList[imgposition].repsonse[0].bytestream = byteStream

        notifyItemChanged(imgposition)

    }


}



