package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.nurse_desk.WardBedInfo


class ManageBedAllocateAdapter(
    context: Context,
    private var responseContents: MutableList<WardBedInfo> = ArrayList()
) :
    RecyclerView.Adapter<ManageBedAllocateAdapter.MyViewHolder>() {
    
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    var orderNumString: String? = null
    private var isLoadingAdded = false

    private var selectedOldBed = 0

    private var isselectedold = false

    private var isselectednew = false

    private var isbedselected = false

    private var oldselected: WardBedInfo = WardBedInfo()

    private var newselected: WardBedInfo? = WardBedInfo()

    private lateinit var onBedItemClickListener: OnbedItemClickListener

    private lateinit var onBedItemLoaderListener: OnbedItemLoaderListener


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bed_number: TextView = view.findViewById(R.id.bed_number)
        var bed: ImageView = view.findViewById(R.id.bed_image)
        var bedCheck: LinearLayout = view.findViewById(R.id.checkGrid)
        var mainLinearLayout: ConstraintLayout = view.findViewById(R.id.mainLinearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(
                R.layout.room_bed_management_allocator_recycler_list,
                parent,
                false
            ) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseContent = responseContents[position]

        /*      if(position==0) {
                  onBedItemLoaderListener.onBedItemLoader(true)
              }
              else if(position==responseContents.size-1) {
                  onBedItemLoaderListener.onBedItemLoader(false)
              }*/
/*
        holder.bed_number.text = (position + 1).toString()
*/
        holder.bed_number.text = responseContent.bed_number.toString()
        if (isselectedold) {
            if (selectedOldBed == responseContents[position].bed_number) {
                if (isbedselected) {
                    holder.bedCheck.setBackgroundResource(R.drawable.select_cell)
                } else {
                    holder.bedCheck.setBackgroundResource(R.drawable.green_shall)
                }
                oldselected = responseContents[position]
            }
        }
        holder.bedCheck.apply {

            with(responseContents[position]) {
                //  tvTimeView.text = convertedTime
                if (active) {
                    if (isselectednew) {
                        holder.bedCheck.setBackgroundResource(R.drawable.select_cell)
                    } else {
                        holder.bedCheck.setBackgroundResource(R.drawable.green_shall)
                    }
                    newselected = this
                } else {

                    if (selectedOldBed != responseContents[position].bed_number) {

                        holder.bedCheck.setBackgroundResource(R.drawable.edittext_border)
                    }
                }
            }
        }

        if (responseContent.is_occupied) {

            if (responseContents[position].is_oxygen != null && responseContents[position].is_ventilator != null) {

                if (responseContents[position].is_oxygen && responseContents[position].is_ventilator) {

                    holder.bed.setImageResource(R.drawable.ic_ventilation_grey)

                } else if (responseContents[position].is_oxygen) {

                    holder.bed.setImageResource(R.drawable.ic_oxygen_grey)

                } else if (responseContents[position].is_ventilator) {

                    holder.bed.setImageResource(R.drawable.ic_ventilation_grey)

                } else {

                    holder.bed.setImageResource(R.drawable.ic_bed_management_black)
                }

            } else if (responseContents[position].is_oxygen != null) {

                if (responseContents[position].is_oxygen) {

                    holder.bed.setImageResource(R.drawable.ic_oxygen_grey)

                } else {

                    holder.bed.setImageResource(R.drawable.ic_bed_management_black)
                }

            } else if (responseContents[position].is_ventilator != null) {

                if (responseContents[position].is_ventilator) {

                    holder.bed.setImageResource(R.drawable.ic_ventilation_grey)

                } else {

                    holder.bed.setImageResource(R.drawable.ic_bed_management_black)
                }

            } else {

                holder.bed.setImageResource(R.drawable.ic_bed_management_black)
            }
        } else {

            if (responseContents[position].is_oxygen != null && responseContents[position].is_ventilator != null) {

                if (responseContents[position].is_oxygen && responseContents[position].is_ventilator) {

                    holder.bed.setImageResource(R.drawable.ic_ventilation_green)

                } else if (responseContents[position].is_oxygen) {

                    holder.bed.setImageResource(R.drawable.ic_oxygen_green)
                } else if (responseContents[position].is_ventilator) {

                    holder.bed.setImageResource(R.drawable.ic_ventilation_green)

                } else {

                    holder.bed.setImageResource(R.drawable.ic_bed_management_green)
                }

            } else if (responseContents[position].is_oxygen != null) {

                if (responseContents[position].is_oxygen) {

                    holder.bed.setImageResource(R.drawable.ic_oxygen_green)
                } else {

                    holder.bed.setImageResource(R.drawable.ic_bed_management_green)
                }

            } else if (responseContents[position].is_ventilator != null) {

                if (responseContents[position].is_ventilator) {

                    holder.bed.setImageResource(R.drawable.ic_ventilation_green)

                } else {

                    holder.bed.setImageResource(R.drawable.ic_bed_management_green)
                }

            } else {

                holder.bed.setImageResource(R.drawable.ic_bed_management_green)
            }


        }


        holder.bedCheck.setOnClickListener {

            //   bed=responseContents!![position].uuid

            //   holder.bedCheck.setBackgroundResource(R.drawable.cell_shape)

            if (!responseContents[position].is_occupied || isselectednew) {

                setSelected(position, responseContents[position])
            }

            //    notifyDataSetChanged()
        }


    }


    private fun setSelected(position: Int, returndata: WardBedInfo) {


        for (i in this.responseContents.indices) {

            this.responseContents[i].active = i == position

        }

        onBedItemClickListener.onBedItemClick(position, returndata)

        notifyDataSetChanged()


    }

    override fun getItemCount(): Int {
        return responseContents.size
    }

    /*
        fun addAll(responseContent: List<RecyclerDto?>?) {
            this.responseContent?.addAll(responseContent!!)
            notifyDataSetChanged()
        }
    */
    fun clearAll() {
        this.responseContents.clear()
        notifyDataSetChanged()
    }

    fun addAll(wardBedInfo: List<WardBedInfo>) {
        this.responseContents.addAll(wardBedInfo)
        notifyDataSetChanged()
    }

    fun setSelecetedData(wbmBedNumber: Int) {

        selectedOldBed = wbmBedNumber

        isselectedold = true

    }

    fun getSave(): WardBedInfo {

        return this.newselected!!

    }

    fun oldbed(): WardBedInfo {

        return this.oldselected

    }

    fun clearCheckAll() {

        for (i in this.responseContents.indices) {

            this.responseContents[i].active = false
        }

        this.newselected = WardBedInfo()


        notifyDataSetChanged()


    }


    interface OnbedItemClickListener {
        fun onBedItemClick(
            position: Int,
            returndata: WardBedInfo
        )
    }

    fun setOnBedItemClickListener(onListItemClickListener: OnbedItemClickListener) {
        this.onBedItemClickListener = onListItemClickListener
    }

    fun allocated(bedalocated: Boolean) {

        this.isselectednew = bedalocated

    }


    fun bedselect(bedalocated: Boolean) {

        this.isbedselected = bedalocated

    }


    interface OnbedItemLoaderListener {
        fun onBedItemLoader(
            status: Boolean
        )
    }

    fun setOnBedItemLoaderListener(onListItemLoaderListener: OnbedItemLoaderListener) {
        this.onBedItemLoaderListener = onListItemLoaderListener
    }

    fun refreshposition(refreshPosition: Int?, bedData: WardBedInfo) {


        this.responseContents[refreshPosition!!].is_ventilator = bedData.is_ventilator

        this.responseContents[refreshPosition].is_oxygen = bedData.is_oxygen

        notifyItemChanged(refreshPosition)

    }

}