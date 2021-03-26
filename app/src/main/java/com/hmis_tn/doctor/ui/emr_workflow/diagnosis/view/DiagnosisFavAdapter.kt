package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.addFavlistDiagonosisDataModel
import kotlinx.android.synthetic.main.row_diagnosis_addfav_list.view.*


class DiagnosisFavAdapter(
    private val context: Activity,
    var arrayListLabFavList: ArrayList<addFavlistDiagonosisDataModel?>
) : RecyclerView.Adapter<DiagnosisFavAdapter.MyViewHolder>() {
    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiagnosisFavAdapter.MyViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.row_diagnosis_addfav_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiagnosisFavAdapter.MyViewHolder, position: Int) {

        if (arrayListLabFavList.size != 0) {
            val response = arrayListLabFavList[position]
            /*holder.serialNumberTextView.text = (position + 1).toString()
        holder?.testNameTextView?.text = response?.test_master_name
        holder?.displayORder?.text = response?.favourite_display_order?.toString()*/


            if (isTablet(holder.itemView.context)) {

                if (position % 2 == 0) {
                    holder.itemView.mainLinearLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.alternateRow
                        )
                    )
                } else {
                    holder.itemView.mainLinearLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                }

                holder.itemView.sNoTextView.text = (position + 1).toString()

                holder.itemView.textName?.text = response!!.diagonosisname

                holder.itemView.displayOrderTextView?.text = response.display_order.toString()
                //   holder.displaytype?.text ="ICD10"

                holder.itemView.deleteImageView.setOnClickListener {
                    onDeleteClickListener?.onDeleteClick(
                        response.diagonosis_id.toInt(),
                        response.diagonosisname
                    )
                }


            } else {


                holder.itemView.tv_no.text = (position + 1).toString()
                holder.itemView.tv_title?.text = response!!.diagonosisname
                holder.itemView.tv_sub_menu?.text =
                    "Display Order " + response.display_order.toString()

                holder.itemView.deleteImageView.setOnClickListener {
                    onDeleteClickListener?.onDeleteClick(
                        response.diagonosis_id.toInt(),
                        response.diagonosisname
                    )
                }

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