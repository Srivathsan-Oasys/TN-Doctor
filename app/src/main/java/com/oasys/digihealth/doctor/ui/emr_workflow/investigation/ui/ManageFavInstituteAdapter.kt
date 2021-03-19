package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.ManageFavAddResponseContents


class ManageFavInstituteAdapter(
    context: Context,
    private var arrayListINvFavList: ArrayList<ManageFavAddResponseContents?>
) : RecyclerView.Adapter<ManageFavInstituteAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListeners? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textName: TextView
        var textdisplayORder: TextView
        var serialNumber: TextView
        var deleteImage: ImageView
        var mainLinearLayout: ConstraintLayout


        init {
            serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            textName = view.findViewById<View>(R.id.textName) as TextView
            textdisplayORder = view.findViewById<View>(R.id.textdisplayORder) as TextView
            deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as ConstraintLayout


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_manage_fav_institute, parent, false) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListINvFavList[position].toString()
        val list = arrayListINvFavList[position]
        holder.serialNumber.text = (position + 1).toString()
        if (list!!.test_master_name !== null) {
            holder.textName.text = list!!.test_master_name.toString()
        }

        holder.textdisplayORder.text = list!!.favourite_display_order.toString()
        holder.deleteImage.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(list.favourite_id, list.test_master_name)
        }
        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }


    }

    fun setFavAddItem(responseContents: ManageFavAddResponseContents?) {
        arrayListINvFavList.add(responseContents)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayListINvFavList.size
    }


    /*
Delete
*/

    interface OnDeleteClickListeners {
        fun onDeleteClick(
            favouritesID: Int?,
            testMasterName: String?
        )
    }

    fun clearadapter() {
        arrayListINvFavList.clear()
        notifyDataSetChanged()
    }


    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListeners) {
        this.onDeleteClickListener = onDeleteClickListener
    }


    fun adapterrefresh(deletefavouriteID: Int?) {

        for (i in arrayListINvFavList.indices) {
            if (arrayListINvFavList.get(i)?.favourite_id?.equals(deletefavouriteID!!)!!) {
                this.arrayListINvFavList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()

    }


    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}


