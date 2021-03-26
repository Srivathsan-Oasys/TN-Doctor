package com.hmis_tn.doctor.ui.emr_workflow.mrd.ui

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.CheifComplaintsDetail


class MrdChiefComplaintsAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<MrdChiefComplaintsAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<CheifComplaintsDetail?>? = ArrayList()


    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)


    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNum: TextView
        var dateText: TextView
        var typeText: TextView
        var departmentText: TextView
        var givenByText: TextView
        var institution: TextView
        internal var recyclerView: RecyclerView
        val resultLinearLayout: CardView
        val previewLinearLayout: LinearLayout
        val mainLinearLayout: LinearLayout


        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            departmentText = view.findViewById<View>(R.id.department) as TextView
            givenByText = view.findViewById<View>(R.id.givenByText) as TextView
            institution = view.findViewById<View>(R.id.institution) as TextView
            recyclerView = view.findViewById(R.id.Chief_child_recycler)
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
            resultLinearLayout = view.findViewById(R.id.chiefComplaintResult)
            previewLinearLayout = view.findViewById<View>(R.id.ChiefComplaintParent) as LinearLayout


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_chief_complaint_recycler_list,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = PatientList?.get(position).toString()
        val prevList = PatientList?.get(position)
        holder.serialNum.text = (position + 1).toString()
        holder.dateText.text = prevList?.created_date
        holder.typeText.text = prevList?.encounter_type_name
        holder.departmentText.text = prevList?.department
        holder.givenByText.text = prevList?.performed_by_first_name
        val myOrderChildAdapter = MrdChiefComplaintChildAdapter(mContext, PatientList)
        val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        holder.recyclerView.addItemDecoration(itemDecor)
        holder.recyclerView.adapter = myOrderChildAdapter



        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }


    }

    fun refreshList(preLabArrayList: List<CheifComplaintsDetail?>) {
        this.PatientList = preLabArrayList
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }


}
