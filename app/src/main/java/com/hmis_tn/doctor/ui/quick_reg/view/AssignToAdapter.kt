package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.quick_reg.model.LocationMaster
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.Assigntoother
import com.hmis_tn.doctor.ui.quick_reg.model.request.LabName

class AssignToAdapter(context: Context, private var labTestList: ArrayList<Assigntoother>) :
    RecyclerView.Adapter<AssignToAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null

    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null

    private var onSearch:OnSearch?=null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumberTextView: TextView
        var textProcess: TextView
        var result_text: AppCompatAutoCompleteTextView
        var lab_spinner: AppCompatAutoCompleteTextView

        var mainLinearLayout: LinearLayout


        init {
            serialNumberTextView = view.findViewById<View>(R.id.serialNumberTextView) as TextView

            textProcess = view.findViewById<View>(R.id.textProcess) as TextView
            result_text = view.findViewById<View>(R.id.institution_spinner) as AppCompatAutoCompleteTextView
            lab_spinner = view.findViewById<View>(R.id.lab_spinner) as AppCompatAutoCompleteTextView

            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_assign_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = labTestList[position].toString()
        holder.serialNumberTextView.text = (position + 1).toString()
        val movie = labTestList[position]
        holder.textProcess.text = movie.testname
/*
        status = holder.status.text as String?
*/

        holder.result_text.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length<6) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.result_text,position,holder.lab_spinner
                    )
                }
                else if(s.isEmpty()){

                    labTestList[position].to_facility=0
                }

            }
        })


        holder.lab_spinner.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable) {
               if(s.isEmpty()){
                    labTestList[position].to_location_uuid=""
                }

            }
        })

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


    interface OnSearchInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int,
            lab: AppCompatAutoCompleteTextView
        )
    }

    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }
    override fun getItemCount(): Int {
        return labTestList.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }




    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<LabName>,
        searchposition: Int,
        lab: AppCompatAutoCompleteTextView
    ) {

        val responseContentAdapter = LabNameAdapter(
            this.mContext,
            R.layout.assign_to_other_spinner_text,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()

        dropdownReferenceView.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as LabName?

            dropdownReferenceView.setText(selectedPoi!!.name)

            lab.isEnabled=true

            labTestList[searchposition].to_facility=selectedPoi.uuid
            onSearch!!.onSearchFunction(selectedPoi.uuid,lab,searchposition)


        }}

    interface OnSearch {
        fun onSearchFunction(
            data: Int,
            dropdownReferenceView: AppCompatAutoCompleteTextView,
            searchposition:Int
        )
    }

    fun setOnSearch(onSearchList: OnSearch) {
        this.onSearch = onSearchList
    }



    fun setlabAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<LocationMaster>,
        current:Int
    ) {

        val responseContentAdapter = LocationMasterAdapter(
            this.mContext,
            R.layout.assign_to_other_spinner_text,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()

        dropdownReferenceView.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as LocationMaster?

            labTestList[current].to_location_uuid= selectedPoi!!.uuid.toString()

            dropdownReferenceView.setText(selectedPoi!!.location_name)


        }}

    fun setData(datas:ArrayList<Assigntoother>){

        this!!.labTestList=datas

        notifyDataSetChanged()

    }

    fun getAll():ArrayList<Assigntoother> {

        return labTestList
    }


}
