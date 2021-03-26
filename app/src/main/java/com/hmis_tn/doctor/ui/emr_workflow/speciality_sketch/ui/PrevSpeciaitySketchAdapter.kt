package com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchPrevContent
import kotlinx.android.synthetic.main.item_lab_details.view.*
import kotlinx.android.synthetic.main.item_prev_diet.view.*
import kotlinx.android.synthetic.main.row_speciality_sketch_list.view.*
import kotlinx.android.synthetic.main.row_speciality_sketch_list.view.rl_view
import kotlinx.android.synthetic.main.row_speciality_sketch_list.view.tv_no
import kotlinx.android.synthetic.main.row_speciality_sketch_list.view.tv_sub_menu
import kotlinx.android.synthetic.main.row_speciality_sketch_list.view.tv_title

class PrevSpeciaitySketchAdapter(
    private val mContent: Context,
    private val list: ArrayList<SpecialitySketchPrevContent>
) : RecyclerView.Adapter<PrevSpeciaitySketchAdapter.MyViewHolder>() {


    private var onViewClickListener: OnViewClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_speciality_sketch_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val prevSpecialitySketch = list[position]

        holder.itemView.tv_no.text = (position + 1).toString()
        holder.itemView.tv_title.text = prevSpecialitySketch.sketch_name ?: ""
        holder.itemView.tv_sub_menu.text = prevSpecialitySketch.created_date ?: ""

        holder.itemView.rl_view.setOnClickListener {

            val popup =
                android.widget.PopupMenu(mContent, holder.itemView.rl_view)
            //inflating menu from xml resource
            popup.inflate(R.menu.view_manu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.view ->  //handle menu2 click
                    {

                        onViewClickListener!!.onViewClick(list[position])

                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
            //displaying the popup
            popup.show()

        }

    }

    fun setData(responseContents: List<SpecialitySketchPrevContent>) {
        list.addAll(responseContents)

    }


    interface OnViewClickListener {
        fun onViewClick(
            responseContent: SpecialitySketchPrevContent
        )
    }

    fun setOnViewClickListener(onDeleteClickListener: OnViewClickListener) {
        this.onViewClickListener = onDeleteClickListener
    }


}