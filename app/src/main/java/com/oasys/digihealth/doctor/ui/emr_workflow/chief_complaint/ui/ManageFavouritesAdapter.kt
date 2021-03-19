package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel

class ManageFavouritesAdapter(
    private val context: Context,
    private val favouritesList: ArrayList<FavouritesModel?>
) :
    RecyclerView.Adapter<ManageFavouritesAdapter.MyViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_manage_favourites, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = favouritesList[position]
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.complaintNameTextView.text = responseContent?.chief_complaint_name
        holder.deleteImageView.setOnClickListener {
            onItemClickListener?.onItemClick(responseContent, position)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(responseContent, position)
        }
    }

    override fun getItemCount(): Int {
        return favouritesList.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val complaintNameTextView: TextView =
            itemView.findViewById(R.id.complaintNameTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
    }

    interface OnItemClickListener {
        fun onItemClick(
            favouritesModel: FavouritesModel?,
            position: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun addFav(selectedSearchChiefComplaintBean: FavouritesModel) {

        favouritesList.add(selectedSearchChiefComplaintBean)

    }

}


