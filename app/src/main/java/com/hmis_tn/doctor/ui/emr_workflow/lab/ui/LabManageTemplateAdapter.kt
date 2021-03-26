package com.hmis_tn.doctor.ui.emr_workflow.lab.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import kotlinx.android.synthetic.main.row_lab.view.rl_view
import kotlinx.android.synthetic.main.row_lab.view.serialNumberTextView
import kotlinx.android.synthetic.main.row_manage_template_lab.view.*

class LabManageTemplateAdapter(
    context: Context,
    private var arrayListLabFavList: ArrayList<ResponseContentsfav?>
) : RecyclerView.Adapter<LabManageTemplateAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mContext: Context
    var orderNumString: String? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /* var textName: TextView
         var textcode: TextView
         var serialNumber: TextView

         var deleteImage : ImageView
         val mainLinearLayout: ConstraintLayout

         init {
             serialNumber = view.findViewById<View>(R.id.serialNumberTextView) as TextView
             textName = view.findViewById<View>(R.id.textNames) as TextView
             textcode = view.findViewById<View>(R.id.codesTextView) as TextView
             deleteImage = view.findViewById<View>(R.id.deleteImageView) as ImageView
              mainLinearLayout = itemView.findViewById(R.id.mainLinearLayout)


         }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_manage_template_lab, parent, false) as ConstraintLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayListLabFavList[position].toString()
        val list = arrayListLabFavList[position]

        if (isTablet(mContext)) {
            holder.itemView.serialNumberTextView.text = (position + 1).toString()
            holder.itemView.codesTextView.text = list!!.test_master_code
            holder.itemView.textNames.text = list.test_master_name
            holder.itemView.im_delete.setOnClickListener {
                onDeleteClickListener?.onDeleteClick(list, position)

            }
            /*   if (position % 2 == 0) {
                holder.itemView.mainLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.alternateRow
                    )
                )
            } else {
                holder.itemView.mainLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.white
                    )
                )
            }*/
        } else {
            holder.itemView.serialNumberTextView.text = (position + 1).toString()
            holder.itemView.textNames.text = list!!.test_master_code
            holder.itemView.codesTextView.text = list.test_master_name
            holder.itemView.rl_view.setOnClickListener {
                onDeleteClickListener?.onDeleteClick(list, position)

            }
        }


    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }

    fun setFavAddItem(responseContantSave: ArrayList<ResponseContentsfav?>) {
        arrayListLabFavList = responseContantSave
        notifyDataSetChanged()
    }


    fun getItems(): ArrayList<ResponseContentsfav?> {

        return arrayListLabFavList

    }


    /*
Delete
*/
    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseData: ResponseContentsfav?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun cleardata() {
        arrayListLabFavList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        arrayListLabFavList.removeAt(position)
        notifyDataSetChanged()
    }


    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }
}


