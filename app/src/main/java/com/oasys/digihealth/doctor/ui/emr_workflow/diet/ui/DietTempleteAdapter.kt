package com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
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
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.DietTemplateDeatils
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TemplatesLab
import kotlinx.android.synthetic.main.row_favourites.view.*

class DietTempleteAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<DietTempleteAdapter.MyViewHolder>() {

    private var responseContent: List<TemplatesLab?>? = ArrayList()
    private var filter: List<TemplatesLab?>? = ArrayList()
    private var selectedArrayList: ArrayList<TemplatesLab?>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var onNextButtonEnableListener: OnNextButtonEnableListener? = null
    private var onItemDeleteClickListner: OnItemDeleteClickListner? = null
    private var onItemViewClickListner: OnItemViewClickListner? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_favourites, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val resp = filter!!.get(position)!!
        val response = filter!![position]?.temp_details
        val dietDetails = filter!![position]?.diet_details
        val responseOriginal = responseContent!![position]?.temp_details

        val res = filter!![position]
        holder.nameTextView.text = response?.template_name
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
            Log.i("", "" + responseOriginal)
            onItemClickListener?.onItemClick(
                dietDetails,
                position,
                response.isSelected!!,
                response.template_id!!
            )
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
//                        viewItem(response)
//                        return@setOnMenuItemClickListener true
//                    }
//
//                    R.id.delete ->  //handle menu2 click
//                    {
//                        deleteItem(res!!)
//                        return@setOnMenuItemClickListener true
//                    }
//
//                    else -> return@setOnMenuItemClickListener false
//                }
//            }
//            //displaying the popup
//            popup.show()
//        })

        if (filter!![position]!!.collapse == true) {
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
    /*
       View update
        */

    interface OnItemViewClickListner {
        fun onItemClick(responseContent: TempDetails?)
    }

    fun setOnItemViewClickListener(onItemViewClickListner: OnItemViewClickListner) {
        this.onItemViewClickListner = onItemViewClickListner
    }

    private fun viewItem(response: TempDetails) {
        onItemViewClickListner?.onItemClick(response)
    }

    private fun deleteItem(response: TempDetails) {
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

    fun refreshList(responseContent: List<TemplatesLab?>?) {
        this.responseContent = responseContent
        this.filter = responseContent
        notifyDataSetChanged()
    }

    fun getFavouritesList(): List<TemplatesLab?> {
        return this.responseContent!!
    }

    fun getSelectedArrayList(): List<TemplatesLab?> {
        return this.selectedArrayList!!
    }

    fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    filter = responseContent
                } else {

                    val filteredList = java.util.ArrayList<TemplatesLab>()

                    for (messageList in responseContent!!) {

                        if (messageList?.temp_details?.template_name != null) {
                            if (messageList.temp_details?.template_name?.toLowerCase()?.contains(
                                    charString
                                )!!
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
                filter = filterResults.values as java.util.ArrayList<TemplatesLab>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: List<DietTemplateDeatils?>?,
            position: Int,
            selected: Boolean,
            id: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemDeleteClickListner {
        fun onItemClick(responseContent: TempDetails?)
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
            this.filter?.get(position)?.temp_details?.isSelected = false
            selectedArrayList?.remove(filter?.get(position))
        } else {
            this.filter?.get(position)?.temp_details?.isSelected = true
            selectedArrayList?.add(filter?.get(position))
        }
        if (selectedArrayList?.isNotEmpty()!!) {
            onNextButtonEnableListener?.onButtonsEnable(true)
        } else {
            onNextButtonEnableListener?.onButtonsEnable(false)
        }
        notifyDataSetChanged()
    }

    fun refreshParticularData(position: Int) {

        this.filter?.get(position)?.temp_details?.isSelected = false
//        selectedArrayList!!.get(position)!!.temp_details?.isSelected=false

        notifyDataSetChanged()
    }

    fun refreshAllData() {
        for (i in selectedArrayList!!.indices) {
            selectedArrayList!!.get(i)!!.temp_details!!.isSelected = false
        }
        notifyDataSetChanged()
    }


}

