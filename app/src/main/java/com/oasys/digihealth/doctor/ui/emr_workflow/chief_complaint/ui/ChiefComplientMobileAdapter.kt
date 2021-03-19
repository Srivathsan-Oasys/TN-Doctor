package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import kotlinx.android.synthetic.main.item_lab_details.view.*
import kotlinx.android.synthetic.main.item_lab_details.view.cv_outer_view
import kotlinx.android.synthetic.main.item_lab_details.view.ll_edit_view
import kotlinx.android.synthetic.main.item_lab_details.view.rl_view
import kotlinx.android.synthetic.main.item_lab_details.view.tv_no
import kotlinx.android.synthetic.main.item_lab_details.view.tv_sub_menu
import kotlinx.android.synthetic.main.item_lab_details.view.tv_title
import kotlinx.android.synthetic.main.item_mobile_chief_complient.view.*

class ChiefComplientMobileAdapter(
    var context: Context,
    private var favouritesArrayList: ArrayList<FavouritesModel?>
) :
    RecyclerView.Adapter<ChiefComplientMobileAdapter.MyViewHolder>() {

    private var onSelectItemCommunication: OnSelectItemCommunication? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_mobile_chief_complient, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedItem = favouritesArrayList.get(position)?.labDataSelected
        val selectedData = favouritesArrayList.get(position)
        if (selectedItem!!) {
            setSelectedBackground(holder)
        } else {
            setUnSelectedBackground(holder)
        }
        holder.itemView.tv_no.text = (position + 1).toString()
        holder.itemView.tv_title.text = selectedData?.chief_complaint_name
        if (selectedData?.durationPeriodId == 0) {
            holder.itemView.tv_title.text = "${holder.itemView.tv_title.text} - Select Duration"
        } else {
            holder.itemView.tv_title.text =
                "${holder.itemView.tv_title.text} - ${selectedData?.duration}  ${selectedData?.durationName}"
        }
        holder.itemView.ll_edit_view.setOnClickListener {
            onSelectItemCommunication?.onClick(
                position,
                selectedItem,
                favouritesArrayList.get(position)
            )
        }
        if (selectedData?.durationPeriodId == 0) {
            favouritesArrayList[position]?.isReadyForSave = false
            if (favouritesArrayList[position]?.labDataSelected!!) {
                setSelectedAndErrorBackground(holder)
            } else {
                seterrorBackground(holder)
            }
        } else {
            favouritesArrayList[position]?.isReadyForSave = true
        }

        holder.itemView.rl_view.setOnClickListener {
            val popup = PopupMenu(context, holder.itemView.rl_view.moreImageView)
            popup.inflate(R.menu.lab_options_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {

                    }
                    R.id.delete -> {
                        onDeleteClickListener?.onDeleteClick(selectedData, position)
                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }

    }


    fun setUnSelectedBackground(holder: ChiefComplientMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circel
        )
        holder.itemView.moreImageView.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
    }

    fun setSelectedBackground(holder: ChiefComplientMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.colorPrimary
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.moreImageView.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circle_grey
        )
    }

    fun seterrorBackground(holder: ChiefComplientMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.moreImageView.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circle_red
        )
    }

    fun setSelectedAndErrorBackground(holder: ChiefComplientMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.moreImageView.setColorFilter(
            R.color.white
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circle_grey
        )
    }


    fun onItemClick(onSelectItemCommunication: OnSelectItemCommunication) {
        this.onSelectItemCommunication = onSelectItemCommunication
    }


    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun updateSelectStatus(position: Int, selected: Boolean) {
        this.favouritesArrayList.get(position)?.labDataSelected = !selected
        unSelectOtherPositions(position)
        notifyDataSetChanged()
    }


    fun unSelectOtherPositions(position: Int) {
        for (i in favouritesArrayList.indices) {
            if (i != position) {
                favouritesArrayList[i]?.labDataSelected = false
            }
        }
    }


    fun updateAdapter(chiefComplientData: ArrayList<FavouritesModel?>?) {
        this.favouritesArrayList = chiefComplientData!!
        notifyDataSetChanged()

    }

    fun delete(position: Int) {
        this.favouritesArrayList.removeAt(position)
        notifyDataSetChanged()
    }

    fun clearall() {
        favouritesArrayList.clear()
        notifyDataSetChanged()

    }


    interface OnSelectItemCommunication {
        fun onClick(
            position: Int,
            selectedItem: Boolean,
            favouritesModel: FavouritesModel?
        )
    }


    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseContent: FavouritesModel?,
            position: Int
        )
    }

}