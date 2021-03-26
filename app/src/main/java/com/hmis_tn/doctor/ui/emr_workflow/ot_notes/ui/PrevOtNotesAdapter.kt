package com.hmis_tn.doctor.ui.emr_workflow.ot_notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.ot_notes.model.prev_records.ResponseContent
import kotlinx.android.synthetic.main.item_prev_op_notes.view.*

class PrevOtNotesAdapter(
    private val list: ArrayList<ResponseContent>,
    private val view: (ResponseContent) -> Unit
) :
    RecyclerView.Adapter<PrevOtNotesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_ot_notes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val record = list[position]
        holder.itemView.tvPrevOpNotesSNo.text = "${position + 1}"
        holder.itemView.tvPrevOpNotesDate.text = record.created_date
        holder.itemView.tvPrevOpNotesName.text = record.profile?.profile_name ?: ""

        holder.itemView.imgPrevOpNotesAction.setOnClickListener {
            view(record)
        }
    }
}