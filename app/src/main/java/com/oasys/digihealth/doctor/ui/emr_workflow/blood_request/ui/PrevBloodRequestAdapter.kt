package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model.BloodRequestDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model.ResponseContentXX
import com.oasys.digihealth.doctor.utils.Utils

class PrevBloodRequestAdapter(
    private val list: ArrayList<ResponseContentXX>,
    private val compressAllOthers: (previousBloodRequest: ResponseContentXX) -> Unit
) :
    RecyclerView.Adapter<PrevBloodRequestAdapter.MyViewHolder>() {

    private val bloodRequestDetailsList = ArrayList<BloodRequestDetail>()
    private var prevBloodRequestChildAdapter: PreviousBloodRequestChildAdapter? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvRequestedBy: TextView = view.findViewById(R.id.tvRequestedBy)
        var tvStatus: TextView = view.findViewById(R.id.tvStatus)
        var tvBloodRequestTime: TextView = view.findViewById(R.id.tvBloodRequestTime)
        var imgDown: ImageView = view.findViewById(R.id.imgDown)
        var recyclerViewPrevBloodRequestChild: RecyclerView =
            view.findViewById(R.id.recyclerViewPrevBloodRequestChild)
        var llResult: LinearLayout = view.findViewById(R.id.llResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_blood_request, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val previousBloodRequest = list[position]

        val utils = Utils(holder.itemView.context)

        val date =
            utils.displayDate(previousBloodRequest.createdDate ?: "", "yyyy-MM-dd HH:mm:ss")
        val purpose = previousBloodRequest.bloodRequestPurpose?.name ?: ""
        val appPreferences =
            AppPreferences.getInstance(holder.itemView.context, AppConstants.SHARE_PREFERENCE_NAME)
        val encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        val dept = if (encounterType == AppConstants.TYPE_IN_PATIENT) "IP" else "OP"
        holder.tvBloodRequestTime.text = "$date - $purpose - $dept"
        holder.tvRequestedBy.text = previousBloodRequest.bloodRequestedByName ?: ""
        holder.tvStatus.text = previousBloodRequest.bloodRequestStatus?.name ?: ""


        if (previousBloodRequest.isExpanded)
            holder.llResult.visibility = View.VISIBLE
        else
            holder.llResult.visibility = View.GONE

        bloodRequestDetailsList.clear()
        previousBloodRequest.bloodRequestDetails?.forEach { bloodRequestDetail ->
            bloodRequestDetailsList.add(bloodRequestDetail)
        }

        holder.recyclerViewPrevBloodRequestChild.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        prevBloodRequestChildAdapter = PreviousBloodRequestChildAdapter(bloodRequestDetailsList)
        holder.recyclerViewPrevBloodRequestChild.adapter = prevBloodRequestChildAdapter

        holder.imgDown.setOnClickListener {
            compressAllOthers(previousBloodRequest)
            previousBloodRequest.isExpanded = true
            notifyDataSetChanged()
        }
    }
}