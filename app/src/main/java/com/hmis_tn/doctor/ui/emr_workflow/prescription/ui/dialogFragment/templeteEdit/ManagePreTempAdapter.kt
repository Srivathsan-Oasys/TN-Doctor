package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.DrugDetail

class ManagePreTempAdapter(
    private var favouritesModel: ArrayList<DrugDetail>,
    private val context: Context
) :
    RecyclerView.Adapter<ManagePreTempAdapter.MyViewHolder>() {

    private var frequencySpinnerList = mutableMapOf<Int, String>()
    var routeSpinnerList = mutableMapOf<Int, String>()
    var instructionSpinnerList = mutableMapOf<Int, String>()
    var durationSpinnerList = mutableMapOf<Int, String>()


    private var onDeleteClickListener: OnDeleteClickListener? = null

    private var onViewClickListener: OnViewClickListener? = null

    //private var onDeleteClickListener: OnDeleteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_prescription_add_template, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {

        return favouritesModel.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val selectedData = favouritesModel[position]

        holder.serialNumberTextView.text = (position + 1).toString()
        holder.drugNameTextView.text = selectedData.drug_name
        holder.periodTxt.text = selectedData.drug_period_name

        holder.routeTextView.text = routeSpinnerList[selectedData.drug_route_id]
        holder.frequencyTextView.text = frequencySpinnerList[selectedData.drug_frequency_id]
        holder.durationTextView.text = selectedData.drug_duration.toString()

        holder.periodTxt.text = durationSpinnerList[selectedData.drug_period_id]
        holder.instructionTextView.text = instructionSpinnerList[selectedData.drug_instruction_id]

        holder.deleteImageView.setOnClickListener {

            onDeleteClickListener?.onDeleteClick(selectedData, position)


        }

        holder.mainLinearLayout.setOnClickListener {

            onViewClickListener?.onViewClick(selectedData, position)
        }
        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }


    }

    fun deleteRow(position: Int) {
        this.favouritesModel.removeAt(position)

        notifyDataSetChanged()

    }

    fun getAll(): ArrayList<DrugDetail> {

        return this.favouritesModel

    }

    fun addRow(sendData: DrugDetail) {

        val check = favouritesModel.any { it.drug_id == sendData.drug_id }

        if (!check) {

            this.favouritesModel.add(sendData)

        } else {

            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                ?.show()
        }

        notifyDataSetChanged()
    }

    fun clearAll() {

        this.favouritesModel.clear()

        notifyDataSetChanged()

    }

    fun setRote(routeSpinnerList: MutableMap<Int, String>) {

        this.routeSpinnerList = routeSpinnerList

        notifyDataSetChanged()

    }

    fun setfrequencySpinnerList(frequencySpinnerList: MutableMap<Int, String>) {

        this.frequencySpinnerList = frequencySpinnerList

        notifyDataSetChanged()

    }

    fun setinstructionSpinnerList(instructionSpinnerList: MutableMap<Int, String>) {

        this.instructionSpinnerList = instructionSpinnerList

        notifyDataSetChanged()

    }

    fun setdurationSpinnerList(durationSpinnerList: MutableMap<Int, String>) {

        this.durationSpinnerList = durationSpinnerList

        notifyDataSetChanged()

    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)

        internal val drugNameTextView: TextView = itemView.findViewById(R.id.drugNameTextView)

        internal val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)

        internal val frequencyTextView: TextView = itemView.findViewById(R.id.frequencyTextView)

        internal val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)

        internal val instructionTextView: TextView = itemView.findViewById(R.id.instructionTextView)

        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)

        internal val periodTxt: TextView = itemView.findViewById(R.id.periodTxt)


        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)


    }


    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseContent: DrugDetail?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    interface OnViewClickListener {
        fun onViewClick(
            responseContent: DrugDetail?,
            position: Int
        )
    }

    fun setOnViewClickListener(onViewClickListener: OnViewClickListener) {
        this.onViewClickListener = onViewClickListener
    }

    fun updateRow(sendData: DrugDetail, exitiingPisition: Int) {

        this.favouritesModel[exitiingPisition] = sendData

        notifyDataSetChanged()
    }


}