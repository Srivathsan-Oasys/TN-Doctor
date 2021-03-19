package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import kotlinx.android.synthetic.main.child_recycler_list.view.*
import kotlinx.android.synthetic.main.child_recycler_list.view.sNoTextView
import kotlinx.android.synthetic.main.dialog_manage_chief_complaint_favourites.view.*
import kotlinx.android.synthetic.main.dialog_manage_chief_complaint_favourites.view.displayOrderTextView
import kotlinx.android.synthetic.main.dialog_manage_chief_complaint_favourites.view.textName
import kotlinx.android.synthetic.main.row_lab.view.mainLinearLayout
import kotlinx.android.synthetic.main.row_lab.view.rl_view
import kotlinx.android.synthetic.main.row_lab.view.tv_no
import kotlinx.android.synthetic.main.row_lab.view.tv_sub_menu
import kotlinx.android.synthetic.main.row_lab.view.tv_title
import kotlinx.android.synthetic.main.row_lab_fav_add.view.*

class LabManageFavAdapter(
    private val context: Activity,
    val arrayListLabFavList: ArrayList<ResponseContentsfav?>
) :
    RecyclerView.Adapter<LabManageFavAdapter.MyViewHolder>() {
    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* internal val serialNumberTextView: TextView =
             itemView.findViewById(R.id.sNoTextView)
         //internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
         internal val testNameTextView: TextView =
             itemView.findViewById(R.id.textName)
         internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
         internal val displayORder: TextView = itemView.findViewById(R.id.displayOrderTextView)
         internal val deleteImage : ImageView = itemView.findViewById(R.id.actiondelete)*/

    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabManageFavAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.row_lab_fav_add, parent, false) as LinearLayout
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }

    override fun onBindViewHolder(holder: LabManageFavAdapter.MyViewHolder, position: Int) {
        val response = arrayListLabFavList[position]
        if (isTablet(context)) {
            holder.itemView.sNoTextView.text = (position + 1).toString()
            holder.itemView.textName.text = response?.test_master_name
            holder.itemView.displayOrderTextView.text =
                response?.favourite_display_order?.toString()
            holder.itemView.actiondelete.setOnClickListener {
                onDeleteClickListener?.onDeleteClick(
                    response?.favourite_id!!,
                    response.test_master_name
                )
            }
        } else {
            holder.itemView.tv_no.text = (position + 1).toString()
            holder.itemView.tv_sub_menu.text = response?.test_master_name
            holder.itemView.tv_title.text =
                "Display Order" + "-" + response?.favourite_display_order?.toString()
            holder.itemView.rl_view.setOnClickListener {
                onDeleteClickListener?.onDeleteClick(
                    response?.favourite_id!!,
                    response.test_master_name
                )
            }
        }
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

    }

    fun setFavAddItem(responseContents: ResponseContentsfav?) {

        arrayListLabFavList.add(responseContents)
        notifyDataSetChanged()
    }

    /*
   Delete
    */
    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesID: Int?,
            testMasterName: String?
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun clearadapter() {
        arrayListLabFavList.clear()
        notifyDataSetChanged()
    }

    fun adapterrefresh(deletefavouriteID: Int?) {

        for (i in arrayListLabFavList.indices) {
            if (arrayListLabFavList.get(i)?.favourite_id?.equals(deletefavouriteID!!)!!) {
                this.arrayListLabFavList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()

    }

}