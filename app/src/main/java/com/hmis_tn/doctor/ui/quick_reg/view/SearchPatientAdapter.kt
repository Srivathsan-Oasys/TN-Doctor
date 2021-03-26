package com.hmis_tn.doctor.ui.quick_reg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.quick_reg.model.QuickSearchresponseContent
import com.hmis_tn.doctor.utils.Utils

class SearchPatientAdapter(
    context: Context,
    private var searchPatienList: ArrayList<QuickSearchresponseContent?>?
) : RecyclerView.Adapter<SearchPatientAdapter.MyViewHolder>() {

    private var itemLayout: LinearLayout? = null
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    var orderNumString: String? = null
    private var onItemClickListener: OnItemClickListener? = null
    var PDSStatus: Boolean = false

    private var SalutaionList = mutableMapOf<Int, String>()
    var utils: Utils? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNUmber: TextView
        var dateTextView: TextView
        var nameTextView: TextView
        var pinTextView: TextView
        var genderTextView: TextView
        var fathernameTextView: TextView


        init {
            serialNUmber = view.findViewById<View>(R.id.serialNoTextView) as TextView
            pinTextView = view.findViewById<View>(R.id.pinTextView) as TextView
            nameTextView = view.findViewById<View>(R.id.nameTextView) as TextView
            genderTextView = view.findViewById<View>(R.id.genderTextView) as TextView
            dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView

            fathernameTextView = view.findViewById<View>(R.id.fathernameTextView) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (!PDSStatus) {
            itemLayout = LayoutInflater.from(mContext)
                .inflate(R.layout.serach_row_list, parent, false) as LinearLayout
        } else {
            itemLayout = LayoutInflater.from(mContext)
                .inflate(R.layout.serach_row_pds_list, parent, false) as LinearLayout
        }

        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        utils = Utils(this.mContext)
        orderNumString = searchPatienList!![position]?.toString()
        val searchList = searchPatienList!![position]!!
        holder.serialNUmber.text = (position + 1).toString()
        holder.pinTextView.text = searchList.uhid
        if (searchList.title_uuid != null) {

            holder.nameTextView.text =
                SalutaionList[searchList.title_uuid] + searchList.first_name

        }
        holder.fathernameTextView.text = searchList.patient_detail?.father_name ?: ""


        holder.dateTextView.text = utils?.convertDateFormat(
            searchList.registered_date!!,
            "yyyy-MM-dd HH:mm:ss",
            "dd-MMM-yyyy"
        )



        if (!PDSStatus) {
            when (searchList.gender_uuid) {
                1 -> {
                    holder.genderTextView.text = "Male"
                }
                2 -> {
                    holder.genderTextView.text = "Female"
                }
                3 -> {
                    holder.genderTextView.text = "Transgender"
                }
            }
        } else {
            when (searchList.gender) {
                "M" -> {
                    holder.genderTextView.text = "Male"
                }
                "F" -> {
                    holder.genderTextView.text = "Female"
                }

            }
        }


        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(searchList)
        }


    }

    override fun getItemCount(): Int {
        return searchPatienList!!.size
    }

    /*
sent data
*/
    interface OnItemClickListener {
        fun onItemClick(responseContent: QuickSearchresponseContent)
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    fun addAll(responseContent: List<QuickSearchresponseContent?>?) {
        searchPatienList!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.searchPatienList?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        // add(LabTestresponseContent())
    }

    fun add(r: QuickSearchresponseContent) {
        searchPatienList!!.add(r)
        notifyItemInserted(searchPatienList!!.size - 1)
    }

    fun getItem(position: Int): QuickSearchresponseContent? {
        return searchPatienList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        /*   val position = labTestList!!.size - 1
           val result = getItem(position)
           if (result != null) {
               labTestList!!.removeAt(position)
               notifyItemRemoved(position)
           }*/
    }


    fun setDataListAdd(
        b: Boolean,
        arrayListPatientList: ArrayList<QuickSearchresponseContent?>?
    ) {
        PDSStatus = b
        searchPatienList = arrayListPatientList

        notifyDataSetChanged()
    }

    fun setTitle(
        tile: MutableMap<Int, String>
    ) {
        SalutaionList = tile
        notifyDataSetChanged()
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}