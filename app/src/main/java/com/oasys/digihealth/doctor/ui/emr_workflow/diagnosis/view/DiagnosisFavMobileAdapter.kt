package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.addFavlistDiagonosisDataModel
import kotlinx.android.synthetic.main.item_lab_details.view.*


class DiagnosisFavMobileAdapter(
    private val context: Activity,
    var arrayListLabFavList: ArrayList<addFavlistDiagonosisDataModel?>
) : RecyclerView.Adapter<DiagnosisFavMobileAdapter.MyViewHolder>() {
    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiagnosisFavMobileAdapter.MyViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_diagnosis_addfav_list, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DiagnosisFavMobileAdapter.MyViewHolder, position: Int) {

        if (arrayListLabFavList.size != 0) {
            val response = arrayListLabFavList[position]


            val selectedData = arrayListLabFavList.get(position)

            holder.itemView.tv_no.text = (position + 1).toString()


            holder.itemView.tv_title.text = ("ICD 10" + " - " + selectedData?.diagonosiscode)

            holder.itemView.tv_sub_menu.text =
                selectedData?.diagonosisname + " - Display Order " + response!!.display_order.toString()

            holder.itemView.rl_view.setOnClickListener {
                val popup = androidx.appcompat.widget.PopupMenu(
                    context,
                    holder.itemView.rl_view.textViewOptions
                )
                //inflating menu from xml resource
                popup.inflate(R.menu.delete_menu)
                //adding click listener
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {

                        R.id.delete ->  //handle menu2 click
                        {
                            onDeleteClickListener?.onDeleteClick(
                                response.diagonosis_id.toInt(),
                                response.diagonosisname
                            )
                            return@setOnMenuItemClickListener true
                        }

                        else -> return@setOnMenuItemClickListener false
                    }
                }
                //displaying the popup
                popup.show()

            }


        } else {

        }

    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }

    /*
Delete
*/
    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesID: Int?,
            diagonosisname: String
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun setFavAddItem(responseContents: addFavlistDiagonosisDataModel?) {

        arrayListLabFavList.add(responseContents)
        notifyDataSetChanged()
    }

    fun refreshList(favouritesModel: ArrayList<addFavlistDiagonosisDataModel?>?) {

        arrayListLabFavList = favouritesModel!!
        // this.filter = favouritesModel

        notifyDataSetChanged()

    }

    fun clearadapter() {
        arrayListLabFavList.clear()
        notifyDataSetChanged()
    }


}