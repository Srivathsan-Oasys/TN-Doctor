package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedData
import kotlinx.android.synthetic.main.item_lab_details.view.*


class SnomedMainMobAdapter(
    private val context: Activity,
    private var arrayListLabFavList: ArrayList<SnomedData>
) : RecyclerView.Adapter<SnomedMainMobAdapter.MyViewHolder>() {
    private var onViewClickListener: OnViewClickListener? = null

    private var selectposition: Int? = null

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.row_snomed_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.itemView.tv_no.text = (position + 1).toString()


        holder.itemView.tv_title.text = ("Snomed" + " - " + arrayListLabFavList[position].ConceptId)

        holder.itemView.tv_sub_menu.text = arrayListLabFavList[position].ConceptName



        if (position == selectposition) {


            setSelectedBackground(holder)


        } else {

            setUnSelectedBackground(holder)
        }





        holder.itemView.rl_view.setOnClickListener {
            val popup = PopupMenu(context, holder.itemView.rl_view.textViewOptions)
            popup.inflate(R.menu.action_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.view -> {
                        onViewClickListener!!.onViewClick(arrayListLabFavList[position])

                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }





        holder.itemView.ll_edit_view.setOnClickListener {

            selectposition = position

            notifyDataSetChanged()

        }


    }

    fun setData(snomedData: ArrayList<SnomedData>) {

        this.arrayListLabFavList = snomedData

        selectposition = null

        notifyDataSetChanged()

    }

    interface OnViewClickListener {
        fun onViewClick(
            favouritesModel: SnomedData?
        )
    }

    fun setOnViewClickListener(onViewClickListener1: OnViewClickListener) {
        this.onViewClickListener = onViewClickListener1
    }

    fun getFirstData(): SnomedData {

        return this.arrayListLabFavList[selectposition!!]

    }

    fun getitemsize(): Int {

        return arrayListLabFavList.size

    }


    fun setUnSelectedBackground(holder: MyViewHolder) {
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
        holder.itemView.textViewOptions.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
    }

    fun setSelectedBackground(holder: MyViewHolder) {
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
        holder.itemView.textViewOptions.setTextColor(
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

    fun checkSelect(): Boolean {
        return selectposition != null
    }
}


