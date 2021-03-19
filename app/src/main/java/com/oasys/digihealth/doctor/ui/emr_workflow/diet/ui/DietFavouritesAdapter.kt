package com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import kotlinx.android.synthetic.main.row_favourites.view.*

class DietFavouritesAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<DietFavouritesAdapter.MyViewHolder>() {

    private var responseContent: List<FavouritesModel?>? = ArrayList()
    private var filter: List<FavouritesModel?>? = ArrayList()
    private var selectedArrayList: ArrayList<FavouritesModel?>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var onNextButtonEnableListener: OnNextButtonEnableListener? = null
    private var selectData: Int? = null
    private var onItemDeleteClickListner: OnItemDeleteClickListner? = null
    private var onItemViewClickListner: OnItemViewClickListner? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_favourites, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = filter!![position]
        holder.nameTextView.text = response?.diet_master_name

        if (response?.isSelected!!) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.navColor))
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(response, position, response.isSelected!!)
        }

//        holder.moreImageView.setOnClickListener(View.OnClickListener {
//            //creating a popup menu
//            val popup =
//                PopupMenu(context, holder.moreImageView)
//            //inflating menu from xml resource
//            popup.inflate(R.menu.options_menu)
//            //adding click listener
//            popup.setOnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.edit ->  //handle menu1 click
//                    {
//
//                        viewItem(response)
//                        popup.dismiss()
//                        return@setOnMenuItemClickListener true
//                    }
//
//                    R.id.delete ->  //handle menu2 click
//                    {
//                        deleteItem(response)
//                        popup.dismiss()
//                        return@setOnMenuItemClickListener true
//                    }
//
//                    else -> return@setOnMenuItemClickListener false
//                }
//            }
//            false
//            //displaying the popup
//            popup.show()
//        })

        if (filter!![position]!!.collapse) {
            holder.itemView.edit.visibility = View.INVISIBLE
            holder.itemView.delete.visibility = View.INVISIBLE
            holder.itemView.backCenterLayout.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.itemView.edit.visibility = View.VISIBLE
            holder.itemView.delete.visibility = View.VISIBLE
            holder.itemView.backCenterLayout.setBackgroundColor(Color.WHITE)
        }

        holder.itemView.edit.setOnClickListener {
            viewItem(response)
        }

        holder.itemView.delete.setOnClickListener {
            deleteItem(response)
        }

        holder.moreImageView.setOnClickListener {
            if (!isTablet(holder.itemView.context)) {
                if (!holder.itemView.edit.isVisible && !holder.itemView.delete.isVisible) {

                    for (i in filter!!.indices) {

                        filter!![i]!!.collapse = true

                    }
                    filter!![position]!!.collapse = false
                    holder.itemView.edit.visibility = View.VISIBLE
                    holder.itemView.delete.visibility = View.VISIBLE
                    holder.itemView.backCenterLayout.setBackgroundColor(Color.WHITE)
                    holder.itemView.edit.setOnClickListener {
                        viewItem(response)
                    }

                    holder.itemView.delete.setOnClickListener {
                        deleteItem(response)
                    }

                } else {
                    filter!![position]!!.collapse = true
                    holder.itemView.edit.visibility = View.INVISIBLE
                    holder.itemView.delete.visibility = View.INVISIBLE

                }
                notifyDataSetChanged()
            }
        }
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
    }

    fun refreshList(responseContent: List<FavouritesModel?>?) {
        this.responseContent = responseContent
        this.filter = responseContent
        notifyDataSetChanged()
    }

    fun getFavouritesList(): List<FavouritesModel?> {
        return this.responseContent!!
    }

    fun getSelectedArrayList(): List<FavouritesModel?> {
        return this.selectedArrayList!!
    }

    fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    filter = responseContent
                } else {

                    val filteredList = java.util.ArrayList<FavouritesModel>()

                    for (messageList in responseContent!!) {

                        if (messageList?.diet_master_name != null) {
                            if (messageList.diet_master_name.toLowerCase().contains(
                                    charString
                                )
                            ) {

                                filteredList.add(messageList)
                            }
                        }
                    }

                    filter = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filter
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: Filter.FilterResults
            ) {
                filter = filterResults.values as java.util.ArrayList<FavouritesModel>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: FavouritesModel?,
            position: Int,
            selected: Boolean
        )
    }

    /*
  View update
   */

    interface OnItemViewClickListner {
        fun onItemClick(responseContent: FavouritesModel?)
    }

    fun setOnItemViewClickListener(onItemViewClickListner: OnItemViewClickListner) {
        this.onItemViewClickListner = onItemViewClickListner
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    /*
 Delete
   */

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

    fun updateSelectStatus(position: Int, selected: Boolean) {
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

