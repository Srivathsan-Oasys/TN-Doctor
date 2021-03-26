package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel

class SaveTemplateAdapter(
    private var favouritesModel: ArrayList<FavouritesModel>,
    private val context: Context,
    private val frequencySpinnerList: MutableMap<Int, String>,
    val routeSpinnerList: MutableMap<Int, String>,
    val instructionSpinnerList: MutableMap<Int, String>,
    val durationSpinnerList: MutableMap<Int, String>
) :
    RecyclerView.Adapter<SaveTemplateAdapter.MyViewHolder>() {

    //private var onDeleteClickListener: OnDeleteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_prescription_save_template, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {

        return favouritesModel.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val selectedData = favouritesModel[position]

        holder.serialNumberTextView.text = (position + 1).toString()
        holder.drugNameTextView.text = selectedData.itemAppendString
        holder.routeTextView.text = routeSpinnerList[selectedData.selectRouteID]
        holder.frequencyTextView.text = frequencySpinnerList[selectedData.selecteFrequencyID]
        holder.durationTextView.text =
            selectedData.duration + " " + durationSpinnerList[selectedData.PrescriptiondurationPeriodId]
        holder.instructionTextView.text = instructionSpinnerList[selectedData.selectInvestID]

        holder.deleteImageView.setOnClickListener {


            this.favouritesModel.removeAt(position)

            notifyDataSetChanged()
//            onDeleteClickListener!!.onDeleteClick(position)
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


        //   notifyDataSetChanged()

    }

    fun getAll(): ArrayList<FavouritesModel> {

        return this.favouritesModel

    }

    fun addRow(sendData: FavouritesModel) {

        this.favouritesModel.add(sendData)

        notifyDataSetChanged()
    }

    fun clearAll() {

        this.favouritesModel.clear()

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

        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)


    }

    /*  interface OnDeleteClickListener {
          fun onDeleteClick(
                  responseContent: FavouritesModel?,
                  position: Int
          )
      }

      fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
          this.onDeleteClickListener = onDeleteClickListener
      }*/


}