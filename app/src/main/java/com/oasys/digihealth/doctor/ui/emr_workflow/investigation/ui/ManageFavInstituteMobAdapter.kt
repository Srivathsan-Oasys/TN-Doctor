package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.ManageFavAddResponseContents
import kotlinx.android.synthetic.main.item_lab_details.view.*


class ManageFavInstituteMobAdapter(
    context: Context,
    private var arrayListINvFavList: ArrayList<ManageFavAddResponseContents?>
) : RecyclerView.Adapter<ManageFavInstituteMobAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListeners? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
/*
        var textName: TextView
        var textdisplayORder: TextView
        var serialNumber: TextView
        var deleteImage : ImageView
        var mainLinearLayout : ConstraintLayout
*/


        init {
/*
            serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
            textName = view.findViewById<View>(R.id.textName) as TextView
            textdisplayORder = view.findViewById<View>(R.id.textdisplayORder) as TextView
            deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as ConstraintLayout

*/

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout =
            LayoutInflater.from(mContext).inflate(R.layout.row_manage_fav_institute, parent, false)
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListINvFavList[position].toString()
        val list = arrayListINvFavList[position]

        holder.itemView.tv_no.text = (position + 1).toString()
        holder.itemView.tv_title.text = (list!!.test_master_name)
        holder.itemView.tv_sub_menu.text =
            "Display Order - ${list.favourite_display_order}"


        holder.itemView.rl_view.setOnClickListener {
            val popup = PopupMenu(mContext, holder.itemView.rl_view.textViewOptions)
            popup.inflate(R.menu.delete_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.delete -> {

                        onDeleteClickListener?.onDeleteClick(
                            list.favourite_id,
                            list.test_master_name
                        )
                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
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


