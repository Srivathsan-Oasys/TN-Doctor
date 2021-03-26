package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel

class SaveTemplateMobAdapter(
    private var favouritesModel: ArrayList<FavouritesModel>,
    private val context: Context,
    private val frequencySpinnerList: MutableMap<Int, String>,
    val routeSpinnerList: MutableMap<Int, String>,
    val instructionSpinnerList: MutableMap<Int, String>,
    val durationSpinnerList: MutableMap<Int, String>
) :
    RecyclerView.Adapter<SaveTemplateMobAdapter.MyViewHolder>() {

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

        selectedData.drug_route_name = routeSpinnerList[selectedData.selectRouteID] ?: ""
        selectedData.drug_frequency_name =
            frequencySpinnerList[selectedData.selecteFrequencyID] ?: ""
        selectedData.drug_period_name =
            durationSpinnerList[selectedData.PrescriptiondurationPeriodId] ?: ""
        selectedData.drug_instruction_name =
            instructionSpinnerList[selectedData.selectInvestID] ?: ""


        holder.nameTextView.text = selectedData.itemAppendString + "- " + selectedData.drug_route_name

        holder.detailsTextView.text =
            selectedData.drug_frequency_name + "- " + "${selectedData.duration} ${selectedData.drug_period_name} - ${selectedData.drug_instruction_name}"



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
                        this.favouritesModel.removeAt(position)

                        notifyDataSetChanged()

                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
            //displaying the popup
            popup.show()

        }
/*

        holder.deleteImageView.setOnClickListener {



//            onDeleteClickListener!!.onDeleteClick(position)
        }

*/


        //   notifyDataSetChanged()

    }

    fun getAll(): ArrayList<FavouritesModel> {

        return this.favouritesModel

    }

    fun addRow(sendData: FavouritesModel) {

        this.favouritesModel.add(sendData)

        notifyDataSetChanged()
    }

    fun addAll(sendData: ArrayList<FavouritesModel>) {

        this.favouritesModel = sendData

        notifyDataSetChanged()
    }

    fun clearAll() {

        this.favouritesModel.clear()

        notifyDataSetChanged()

    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /*  internal val serialNumberTextView: TextView = itemView.findViewById(R.id.serialNumberTextView)

          internal val drugNameTextView: TextView = itemView.findViewById(R.id.drugNameTextView)

          internal val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)

          internal val frequencyTextView: TextView = itemView.findViewById(R.id.frequencyTextView)

          internal val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)

          internal val instructionTextView: TextView = itemView.findViewById(R.id.instructionTextView)

          internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)

          internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)

  */

        internal val mainLinearLayout: CardView = itemView.findViewById(R.id.cardView)
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val detailsTextView: TextView = itemView.findViewById(R.id.DetailsTextView)
        internal val moreImageView: ImageView = itemView.findViewById(R.id.moreImageView)


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