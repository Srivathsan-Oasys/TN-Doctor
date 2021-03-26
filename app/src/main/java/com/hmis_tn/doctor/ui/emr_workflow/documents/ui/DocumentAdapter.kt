package com.hmis_tn.doctor.ui.emr_workflow.documents.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.documents.model.Attachment
import com.hmis_tn.doctor.utils.Utils
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DocumentAdapter(
    context: Context,
    private var documentArrayList: ArrayList<Attachment>
) : RecyclerView.Adapter<DocumentAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    private val hashMapType: HashMap<Int, Int> = HashMap()
    private val hashMapDocument: HashMap<Int, Int> = HashMap()
    var status: String? = null
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemdownloadClickListener: OnItemdownloadClickListener? = null
    private var filter: List<Attachment?> = ArrayList()
    private var startDate: String = ""
    private var endDate: String = ""

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumberTextView: TextView =
            view.findViewById<View>(R.id.serialNumberTextView) as TextView
        var typeTextView: TextView = view.findViewById<View>(R.id.typeTextView) as TextView
        var visitDate: TextView = view.findViewById<View>(R.id.visitDate) as TextView
        var fileName: TextView = view.findViewById<View>(R.id.fileName) as TextView
        var commentsText: TextView = view.findViewById<View>(R.id.commentsText) as TextView
        var mainLinearLayout: LinearLayoutCompat = view.findViewById(R.id.mainLinearLayout)
        var deleteImageView: ImageView = view.findViewById(R.id.deleteImageView)
        var downloadImageview: ImageView = view.findViewById(R.id.downloadImageview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_document_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.serialNumberTextView.text = (position + 1).toString()
        val documentLIst = filter[position]
        holder.visitDate.text = Utils(holder.itemView.context).displayDate(
            documentLIst!!.attached_date,
            "yyyy-MM-dd HH:mm:ss"
        )
        holder.typeTextView.text = documentLIst.attachment_type.name
        holder.commentsText.text = documentLIst.comments
        holder.fileName.text = documentLIst.attachment_name
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

        holder.downloadImageview.setOnClickListener {
            onItemdownloadClickListener?.onItemParamClick(documentLIst)
        }

        holder.deleteImageView.setOnClickListener {
            onItemClickListener?.onItemParamClick(documentLIst)
        }
    }

    fun setOnDeleteClickListener(OnitemClickListener: OnItemClickListener) {
        onItemClickListener = OnitemClickListener
    }

    fun setOnItemdowloadClickListener(onItemDownloadListener: OnItemdownloadClickListener) {
        onItemdownloadClickListener = onItemDownloadListener
    }

    fun refreshList(documentArrayList: ArrayList<Attachment>?) {
        this.documentArrayList = documentArrayList!!
        filter = documentArrayList
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filter.size
    }

    interface OnItemClickListener {
        fun onItemParamClick(
            documentId: Attachment
        )
    }

    interface OnItemdownloadClickListener {
        fun onItemParamClick(
            documentId: Attachment
        )
    }

    fun clearadapter() {
        this.documentArrayList.clear()
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale", "SimpleDateFormat")
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filter = documentArrayList
                    val filteredList = java.util.ArrayList<Attachment>()
                    filteredList.clear()

                    for (messageList in documentArrayList) {
                        if ((messageList.attached_date != null && messageList.attached_date != "") &&
                            (startDate != null && startDate != "") && (endDate != null && endDate != "")
                        ) {

//                            val formator=SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            val d = SimpleDateFormat("MMM dd, yyyy")
                            try {
                                val userDate: Date = inputFormat.parse(messageList.attached_date)
                                val datetime = d.format(userDate)
                                val formater2 = SimpleDateFormat("MMM dd, yyyy")
                                val start = formater2.parse(startDate)
                                var comingdate = formater2.parse(datetime)
                                val end = formater2.parse(endDate)
                                Log.i("comingdate", "" + start)
                                Log.i("comingdate", "" + end)
                                if (start <= comingdate) {
                                    if (comingdate <= end) {
                                        filteredList.add(messageList)
                                    }
                                }
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    if (filteredList.isNotEmpty()) {
                        filter = filteredList
                    }
                } else {
                    val filteredList = java.util.ArrayList<Attachment>()
                    for (messageList in documentArrayList) {
                        if (messageList.attachment_type.name != null) {
                            if (messageList.attachment_type.name.toLowerCase().contains(
                                    charString.toLowerCase()
                                )
                            ) {
                                filteredList.add(messageList)
                            }
                        }
                        if (messageList.attachment_name != null) {
                            if (messageList.attachment_name.toLowerCase().contains(
                                    charString.toLowerCase()
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
                filter = filterResults.values as java.util.ArrayList<Attachment>
                notifyDataSetChanged()
            }
        }
    }

    fun setDate(startDate: String?, endDate: String?) {
        this.startDate = startDate!!
        this.endDate = endDate!!
    }
}