package com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.prev_records.ResponseContent
import kotlinx.android.synthetic.main.item_prev_anesthesia_notes.view.*

class PrevAnesthesiaNotesAdapter(
    private val list: ArrayList<ResponseContent>,
    private val view: (ResponseContent) -> Unit
) :
    RecyclerView.Adapter<PrevAnesthesiaNotesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prev_anesthesia_notes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val record = list[position]
        holder.itemView.tvPrevAnesthesiaNotesSNo.text = "${position + 1}"
        holder.itemView.tvPrevAnesthesiaNotesDate.text = record.created_date
        holder.itemView.tvPrevAnesthesiaNotesName.text = record.profile?.profile_name ?: ""

        holder.itemView.imgPrevAnesthesiaNotesAction.setOnClickListener {
            view(record)
        }
    }
}