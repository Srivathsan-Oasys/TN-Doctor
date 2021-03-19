package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.addFavlistDiagonosisDataModel

class TreatmentKitFavouritesAdapter(
    private val context: Activity,
    var arrayListLabFavList: ArrayList<addFavlistDiagonosisDataModel?>
) : RecyclerView.Adapter<TreatmentKitFavouritesAdapter.MyViewHolder>() {
    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView = itemView.findViewById(R.id.sNoTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val testNameTextView: TextView = itemView.findViewById(R.id.textName)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
        internal val displayORder: TextView = itemView.findViewById(R.id.displayOrderTextView)
    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TreatmentKitFavouritesAdapter.MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_treatmentkit_addfav_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }

    override fun onBindViewHolder(
        holder: TreatmentKitFavouritesAdapter.MyViewHolder,
        position: Int
    ) {

        if (arrayListLabFavList.size != 0) {
            val response = arrayListLabFavList[position]
            holder.serialNumberTextView.text = (position + 1).toString()
            holder.testNameTextView.text = response!!.diagonosisname
            holder.displayORder.text = response.diagonosiscode.toString()

            holder.deleteImageView.setOnClickListener {
                if (isTablet(context)) {
                    onDeleteClickListener?.onDeleteClick(
                        response.diagonosis_id.toInt(),
                        response.diagonosisname,
                        position
                    )
                } else {
                    onDeleteClickListener?.onDeleteClick(
                        response.diagonosis_id.toInt(),
                        response.diagonosisname,
                        position
                    )
                }
            }
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

    /*
Delete
*/
    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesID: Int?,
            diagonosisname: String,
            position: Int
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
        this.arrayListLabFavList = favouritesModel!!
        // this.filter = favouritesModel
        notifyDataSetChanged()
    }

    fun addRow(data: addFavlistDiagonosisDataModel) {
        this.arrayListLabFavList.add(data)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        this.arrayListLabFavList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        arrayListLabFavList.removeAt(position)
        notifyDataSetChanged()
    }
}
