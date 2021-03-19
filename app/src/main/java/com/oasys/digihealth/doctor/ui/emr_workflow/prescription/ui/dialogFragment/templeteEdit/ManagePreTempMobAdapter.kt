package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.DrugDetail

class ManagePreTempMobAdapter(
    private var favouritesModel: ArrayList<DrugDetail>,
    private val context: Context
) :
    RecyclerView.Adapter<ManagePreTempMobAdapter.MyViewHolder>() {

    private var frequencySpinnerList = mutableMapOf<Int, String>()
    var routeSpinnerList = mutableMapOf<Int, String>()
    var instructionSpinnerList = mutableMapOf<Int, String>()
    var durationSpinnerList = mutableMapOf<Int, String>()


    private var onDeleteClickListener: OnDeleteClickListener? = null

    private var onViewClickListener: OnViewClickListener? = null

    private var onRefreshClickListener: OnRefreshClickListener? = null

    //private var onDeleteClickListener: OnDeleteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.row_manage_pre_fav_temp, parent, false)

        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {

        return favouritesModel.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val selectedData = favouritesModel[position]


        selectedData.drug_route_name = routeSpinnerList[selectedData.drug_route_id] ?: ""

        selectedData.drug_frequency_name =
            frequencySpinnerList[selectedData.drug_frequency_id] ?: ""

        selectedData.drug_period_name = durationSpinnerList[selectedData.drug_period_id] ?: ""
        selectedData.drug_instruction_name =
            instructionSpinnerList[selectedData.drug_instruction_id] ?: ""


        holder.nameTextView.text = selectedData.drug_name + "- " + selectedData.drug_route_name

        holder.detailsTextView.text =
            selectedData.drug_frequency_name + "- " + "${selectedData.drug_duration} ${selectedData.drug_period_name} - ${selectedData.drug_instruction_name}"

        holder.moreImageView.setOnClickListener {

            val popup =
                PopupMenu(context, holder.moreImageView)
            //inflating menu from xml resource
            popup.inflate(R.menu.delete_menu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.delete ->  //handle menu2 click
                    {
                        onDeleteClickListener?.onDeleteClick(selectedData, position)

                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
            //displaying the popup
            popup.show()

        }


        if (!favouritesModel[position].is_active) {


            holder.mainLinearLayout.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.detailsTextView.setTextColor(ContextCompat.getColor(context, R.color.white))


        } else {
            holder.mainLinearLayout.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.navColor))
            holder.detailsTextView.setTextColor(ContextCompat.getColor(context, R.color.navColor))

        }

        holder.mainLinearLayout.setOnClickListener {

            if (!favouritesModel[position].is_active) {

                onRefreshClickListener!!.onRefreshClick()

                setCollageClear()


            } else {

                setCollage(position)

                onViewClickListener?.onViewClick(favouritesModel[position], position)

            }

        }

        /*  holder.mainLinearLayout.setOnClickListener{

              onViewClickListener?.onViewClick(selectedData,position)
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

  */

    }

    fun deleteRow(position: Int) {
        this.favouritesModel.removeAt(position)

        notifyDataSetChanged()

    }

    fun setCollageClear() {

        for (i in favouritesModel.indices) {

            favouritesModel[i].is_active = true
        }

        notifyDataSetChanged()

    }

    private fun setCollage(position: Int) {

        for (i in favouritesModel.indices) {

            favouritesModel[i].is_active =
                position != i
        }

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

    fun setData(data: ArrayList<DrugDetail>) {

        this.favouritesModel = data

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

    interface OnRefreshClickListener {
        fun onRefreshClick(
        )
    }

    fun setOnRefreshClickListener(onCommandClickListener: OnRefreshClickListener) {
        this.onRefreshClickListener = onCommandClickListener
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
/*
        internal val serialNumberTextView: TextView = itemView.findViewById(R.id.serialNumberTextView)

        internal val drugNameTextView: TextView = itemView.findViewById(R.id.drugNameTextView)

        internal val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)

        internal val frequencyTextView: TextView = itemView.findViewById(R.id.frequencyTextView)

        internal val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)

        internal val instructionTextView: TextView = itemView.findViewById(R.id.instructionTextView)

        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)

        internal val periodTxt: TextView = itemView.findViewById(R.id.periodTxt)


        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)*/

        internal val mainLinearLayout: CardView = itemView.findViewById(R.id.cardView)
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val detailsTextView: TextView = itemView.findViewById(R.id.DetailsTextView)
        internal val moreImageView: ImageView = itemView.findViewById(R.id.moreImageView)


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