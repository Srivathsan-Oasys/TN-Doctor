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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.RadiologyDetail


class MrdRadilogyAdapter(
    private val mContext: Context

) : RecyclerView.Adapter<MrdRadilogyAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    internal lateinit var orderNumString: String
    private var PatientList: List<RadiologyDetail?>? = ArrayList()


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


        val mainLinearLayout: LinearLayout
        val resultLinearLayout: CardView
        val previewLinearLayout: LinearLayout


        init {
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            typeText = view.findViewById<View>(R.id.typeText) as TextView
            departmentText = view.findViewById<View>(R.id.department) as TextView
            givenByText = view.findViewById<View>(R.id.givenByText) as TextView
            institution = view.findViewById<View>(R.id.institution) as TextView
            recyclerView = view.findViewById(R.id.radiology_child_recycler)
            resultLinearLayout = view.findViewById(R.id.labResult)
            previewLinearLayout = view.findViewById<View>(R.id.labParent) as LinearLayout





            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.mrd_radiology_recycler_list,
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
        holder.dateText.text = prevList?.order_request_date
        holder.typeText.text = prevList?.encounter_type_name
        holder.departmentText.text = prevList?.department_name
        holder.givenByText.text = prevList?.vw_user_info?.first_name
        holder.institution.text = prevList?.facility_name

        holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
        val myOrderChildAdapter = MrdRadilogyChildAdapter(
            mContext,
            PatientList?.get(position)?.patient_order_test_details!!
        )
        val itemDecor = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        holder.recyclerView.addItemDecoration(itemDecor)
        holder.recyclerView.adapter = myOrderChildAdapter

        holder.previewLinearLayout.setOnClickListener {

            if (holder.resultLinearLayout.visibility == View.VISIBLE) {
                holder.resultLinearLayout.visibility = View.GONE

            } else {
                holder.resultLinearLayout.visibility = View.VISIBLE
            }
        }






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

    fun refreshList(preLabArrayList: List<RadiologyDetail?>) {
        this.PatientList = preLabArrayList
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return PatientList?.size!!
    }


}
