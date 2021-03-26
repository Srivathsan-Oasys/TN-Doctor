package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import java.util.*
import kotlin.collections.ArrayList

class TreatmentKitMainFavouritesAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<TreatmentKitMainFavouritesAdapter.MyViewHolder>() {

    private var favouritesModel: ArrayList<FavouritesModel?>? = ArrayList()
    private var filter: List<FavouritesModel?>? = ArrayList()
    private var selectedArrayList: ArrayList<FavouritesModel?>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var onNextButtonEnableListener: OnNextButtonEnableListener? = null
    private var onItemDeleteClickListner: OnItemDeleteClickListner? = null
    private var onItemViewClickListner: OnItemViewClickListner? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_favourites, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = filter!![position]
        val responseoriginal = favouritesModel!![position]
        responseoriginal?.position = position

        holder.nameTextView.text = response?.favourite_name.toString()

        if (response?.isSelected!!) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
            response.isSelected = true
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.navColor))
            response.isSelected = false
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(
                response,
                position,
                response.isSelected!!,
                response.favourite_id!!
            )
        }
        holder.moreImageView.setOnClickListener {
//            //creating a popup menu
//            val popup =
//                PopupMenu(context, holder.moreImageView)
//            //inflating menu from xml resource
//            popup.inflate(R.menu.options_menu)
//            //adding click listener
//            popup.setOnMenuItemClickListener { item ->
//
//                when (item.itemId) {
//                    R.id.edit ->  //handle menu1 click
//                    {
//                        viewItem(response)
//                        return@setOnMenuItemClickListener true
//                    }
//
//                    R.id.delete ->  //handle menu2 click
//                    {
//                        deleteItem(response)
//                        return@setOnMenuItemClickListener true
//                    }
//
//                    else -> return@setOnMenuItemClickListener false
//                }
//
//            }
//            false
//
//
//            //displaying the popup
//            popup.show()

            if (!holder.editImageView.isVisible && !holder.deleteImageView.isVisible) {

                for (i in filter!!.indices) {
                    filter!![i]!!.collapse = true
                }
                filter!![position]!!.collapse = false
                holder.editImageView.visibility = View.VISIBLE
                holder.deleteImageView.visibility = View.VISIBLE
                holder.backCenterLayout.setBackgroundColor(Color.WHITE)
                holder.editImageView.setOnClickListener {
                    viewItem(response)
                }

                holder.deleteImageView.setOnClickListener {
                    deleteItem(response)
                }
            } else {
                filter!![position]!!.collapse = true
                holder.editImageView.visibility = View.INVISIBLE
                holder.deleteImageView.visibility = View.INVISIBLE
            }
            notifyDataSetChanged()
        }

        if (filter!![position]!!.collapse) {
            holder.editImageView.visibility = View.INVISIBLE
            holder.deleteImageView.visibility = View.INVISIBLE
            holder.backCenterLayout.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.editImageView.visibility = View.VISIBLE
            holder.deleteImageView.visibility = View.VISIBLE
            holder.backCenterLayout.setBackgroundColor(Color.WHITE)
        }
    }

    interface OnItemViewClickListner {
        fun onItemClick(responseContent: FavouritesModel?)
    }

    fun setOnItemViewClickListener(onItemViewClickListner: OnItemViewClickListner) {
        this.onItemViewClickListner = onItemViewClickListner
    }

    private fun viewItem(response: FavouritesModel) {
        onItemViewClickListner?.onItemClick(response)
    }

    private fun deleteItem(response: FavouritesModel) {
        onItemDeleteClickListner?.onItemClick(response)
    }

    override fun getItemCount(): Int {
        return filter!!.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val cardView: CardView = itemView.findViewById(R.id.cardView)
        internal val moreImageView: ImageView = itemView.findViewById(R.id.moreImageView)
        internal val editImageView: ImageView = itemView.findViewById(R.id.edit)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.delete)
        internal val backCenterLayout: LinearLayout = itemView.findViewById(R.id.backCenterLayout)
        //internal val delete: View = itemView.findViewById(R.id.delete)
    }

    fun refreshList(favouritesModel: ArrayList<FavouritesModel?>?) {
        this.favouritesModel = favouritesModel
        this.filter = favouritesModel
        notifyDataSetChanged()
    }

    fun getFavouritesList(): List<FavouritesModel?> {
        return this.favouritesModel!!
    }

    fun getSelectedArrayList(): List<FavouritesModel?> {
        return this.selectedArrayList!!
    }

    fun deleteItem() {
        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filter = favouritesModel
                } else {
                    val filteredList = java.util.ArrayList<FavouritesModel>()
                    for (messageList in favouritesModel!!) {
                        if (messageList?.favourite_name != null) {
                            if (messageList.favourite_name!!.toLowerCase(Locale.ROOT).contains(
                                    charString
                                )
                            ) {
                                filteredList.add(messageList)
                            }
                        }
                    }
                    filter = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filter
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                filter = filterResults.values as java.util.ArrayList<FavouritesModel>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            favouritesModel: FavouritesModel?,
            position: Int,
            selected: Boolean,
            favouriteId: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemDeleteClickListner {
        fun onItemClick(responseContent: FavouritesModel?)
    }

    fun setOnItemDeleteClickListener(onItemDeleteClickListner: OnItemDeleteClickListner) {
        this.onItemDeleteClickListner = onItemDeleteClickListner
    }

    interface OnNextButtonEnableListener {
        fun onButtonsEnable(
            isEnable: Boolean
        )
    }

    fun setOnNextButtonEnableListener(onNextButtonEnableListener: OnNextButtonEnableListener) {
        this.onNextButtonEnableListener = onNextButtonEnableListener
    }

    fun updateSelectStatus(favOrTem: Int, position: Int, selected: Boolean) {
        if (selected) {
            this.filter?.get(position)?.isSelected = false
            selectedArrayList?.remove(filter?.get(position))
        } else {
            this.filter?.get(position)?.isSelected = true
            selectedArrayList?.add(filter?.get(position))
        }
        if (selectedArrayList?.isNotEmpty()!!) {
            onNextButtonEnableListener?.onButtonsEnable(true)
        } else {
            onNextButtonEnableListener?.onButtonsEnable(false)
        }
        notifyDataSetChanged()
    }

    fun unselect(favouritesModel: FavouritesModel?) {
        this.favouritesModel?.set(favouritesModel?.position!!, favouritesModel)
        notifyDataSetChanged()
    }

    fun clearFacusData() {
        for (i in selectedArrayList!!.indices) {
            selectedArrayList!!.get(i)!!.isSelected = false
        }
        notifyDataSetChanged()
    }

    fun refreshAllData() {
        for (i in selectedArrayList!!.indices) {
            selectedArrayList!!.get(i)!!.isSelected = false
        }
        notifyDataSetChanged()
    }

    fun refreshFavParticularData(position: Int) {
        filter!!.get(position)!!.isSelected = false
        //selectedArrayList!!.get(position)!!.isSelected=false
        notifyDataSetChanged()
    }
}
