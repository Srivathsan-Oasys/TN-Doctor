package com.hmis_tn.doctor.ui.emr_workflow.certificate.ui

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.certificate.model.GetCertificateResponseContent
import com.hmis_tn.doctor.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

typealias OnClickPDFDownload = () -> Unit

class PrevCertificateDialogAdapter(
    private val mContext: Activity,
    val onClickPDFDownload: OnClickPDFDownload
) : RecyclerView.Adapter<PrevCertificateDialogAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    internal lateinit var orderNumString: String
    private var arrayCertificateList: List<GetCertificateResponseContent>? = ArrayList()
    var bitmap: Bitmap? = null
    var utils: Utils? = null
    private var onItemdownloadClickListener: OnItemdownloadClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val serialNumberTextView: TextView = itemView.findViewById(R.id.sNoTextView)
        internal val downloadImageView: ImageView = itemView.findViewById(R.id.downloadImageView)
        internal val certificateName: TextView = itemView.findViewById(R.id.certificateName)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
        internal val issuedByText: TextView = itemView.findViewById(R.id.issuedByText)
        internal val dateText: TextView = itemView.findViewById(R.id.dateText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(
            R.layout.row_prev_certificate,
            parent,
            false
        ) as LinearLayout
        val recyclerView: RecyclerView
        utils = Utils(mContext)
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = arrayCertificateList?.get(position).toString()
        val response = arrayCertificateList?.get(position)
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.certificateName.text = response!!.nt_name
        holder.issuedByText.text = response.u_first_name.toString()

        holder.dateText.text = utils?.displayDate(
            response.pc_approved_on, "yyyy-MM-dd HH:mm:ss"
        )
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

        holder.downloadImageView.setOnClickListener {
            //onClickPDFDownload.invoke()
            onItemdownloadClickListener?.onItemParamClick(arrayCertificateList!![position].pc_uuid)
        }
    }

    fun setOnItemdowloadClickListener(onItemDownloadListener: OnItemdownloadClickListener) {
        onItemdownloadClickListener = onItemDownloadListener
    }

    interface OnItemdownloadClickListener {
        fun onItemParamClick(certificate_id: Int)
    }

    fun refreshList(preLabArrayList: List<GetCertificateResponseContent>?) {
        Collections.reverse(preLabArrayList!!)
        this.arrayCertificateList = preLabArrayList
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayCertificateList?.size!!
    }
}
