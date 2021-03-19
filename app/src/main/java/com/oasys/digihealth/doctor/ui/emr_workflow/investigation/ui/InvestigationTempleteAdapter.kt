package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.TemplatesInvest

class InvestigationTempleteAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<InvestigationTempleteAdapter.MyViewHolder>() {
    private var responseContent: List<TemplatesInvest?>? = ArrayList()
    private var filter: List<TemplatesInvest?>? = ArrayList()
    private var selectedArrayList: ArrayList<TemplatesInvest?>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var onNextButtonEnableListener: OnNextButtonEnableListener? = null
    private var onItemDeleteClickListner: OnItemDeleteClickListner? = null
    private var onItemViewClickListner: OnItemViewClickListner? = null

    private var status: ArrayList<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_favourites, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = filter!![position]?.temp_details
        val invDetails = filter!![position]?.Invest_details

        val responseoriginal = responseContent!![position]?.temp_details
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
            Log.i("", "" + responseoriginal)
            onItemClickListener?.onItemClick(invDetails, position, response.isSelected!!)
        }
/*
        holder.moreImageView.setOnClickListener(View.OnClickListener {
            //creating a popup menu
            val popup =
                PopupMenu(context, holder.moreImageView)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit ->  //handle menu1 click
                    {
                        viewItem(response)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.delete ->  //handle menu2 click
                    {
                        deleteItem(res!!)
                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
            //displaying the popup
            popup.show()
        })
*/



        if (filter!![position]!!.collapse) {
            holder.editImageView.visibility = View.INVISIBLE
            holder.deleteImageView.visibility = View.INVISIBLE
            holder.backCenterLayout.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.editImageView.visibility = View.VISIBLE
            holder.deleteImageView.visibility = View.VISIBLE
            holder.backCenterLayout.setBackgroundColor(Color.WHITE)
        }


        holder.editImageView.setOnClickListener {
            viewItem(response)
        }

        holder.deleteImageView.setOnClickListener {
            deleteItem(res!!)
        }

        holder.moreImageView.setOnClickListener {

            if (holder.editImageView.isVisible == false && holder.deleteImageView.isVisible == false) {

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
                    deleteItem(res!!)
                }

            } else {
                filter!![position]!!.collapse = true
                holder.editImageView.visibility = View.INVISIBLE
                holder.deleteImageView.visibility = View.INVISIBLE

            }
            notifyDataSetChanged()
        }


    }

    /*
      View update
       */

    interface OnItemViewClickListner {
        fun onItemClick(responseContent: com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.TempDetails?)
    }


    fun setOnItemViewClickListener(onItemViewClickListner: OnItemViewClickListner) {
        this.onItemViewClickListner = onItemViewClickListner
    }

    private fun viewItem(response: com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.TempDetails) {
        onItemViewClickListner?.onItemClick(response)
    }


    private fun deleteItem(response: TemplatesInvest) {
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

    }

    fun refreshList(responseContent: List<TemplatesInvest?>?) {
        this.responseContent = responseContent
        this.filter = responseContent
        notifyDataSetChanged()
    }

    fun getFavouritesList(): List<TemplatesInvest?> {
        return this.responseContent!!
    }

    fun getSelectedArrayList(): List<TemplatesInvest?> {
        return this.selectedArrayList!!
    }

    fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    filter = responseContent
                } else {

                    val filteredList = java.util.ArrayList<TemplatesInvest>()

                    for (messageList in responseContent!!) {

                        if (messageList?.temp_details?.template_name != null) {
                            if (messageList.temp_details.template_name.toLowerCase().contains(
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
                filter = filterResults.values as java.util.ArrayList<TemplatesInvest>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: List<InvestDetail?>?,
            position: Int,
            selected: Boolean
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemDeleteClickListner {
        fun onItemClick(responseContent: TemplatesInvest?)
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

    fun updateSelectStatus(favorTem: Int, position: Int, selected: Boolean) {
        if (selected) {
            if (favorTem == 2) {
                this.filter?.get(position)?.temp_details?.isSelected = false
                selectedArrayList?.remove(filter?.get(position))
            }
        } else {
            if (favorTem == 2) {
                this.filter?.get(position)?.temp_details?.isSelected = true
                selectedArrayList?.add(filter?.get(position))
            }
        }
        if (favorTem == 2) {
            if (selectedArrayList?.isNotEmpty()!!) {
                onNextButtonEnableListener?.onButtonsEnable(true)
            } else {
                onNextButtonEnableListener?.onButtonsEnable(false)
            }
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
            selectedArrayList!!.get(i)!!.temp_details.isSelected = false
        }
        notifyDataSetChanged()
    }

    fun updateData(dataList: ArrayList<Int>) {

        status = dataList

        checkncheckData()

    }


    fun checkncheckData() {


        for (position in filter!!.indices) {

            val invDetails = filter!![position]?.Invest_details


            var dat = filter!![position]?.Invest_details!!

            var check = dat.any { it.ischeck }


            if (filter!![position]?.temp_details!!.isSelected!!) {

                for (i in invDetails!!.indices) {

                    if (status.contains(filter!![position]?.Invest_details!![i].lab_test_uuid)) {

                        filter!![position]?.Invest_details!![i].ischeck = true

                        filter!![position]?.temp_details!!.isSelected = true

                        break
                    } else {


                        filter!![position]?.Invest_details!![i].ischeck = false

                        filter!![position]?.temp_details!!.isSelected = false

                    }


                }

            } else {

                for (i in invDetails!!.indices) {


                    if (status.contains(filter!![position]?.Invest_details!![i].lab_test_uuid)) {

                        filter!![position]?.Invest_details!![i].ischeck = true

                        filter!![position]?.temp_details!!.isSelected = true
                    } else {


                        filter!![position]?.Invest_details!![i].ischeck = false

                        filter!![position]?.temp_details!!.isSelected = false

                        break
                    }


                }


            }

        }

        notifyDataSetChanged()
    }
}



