package com.hmis_tn.doctor.ui.quickregistration.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.quick_reg.model.activitysession.ResponseContentsactivitysession

class SessionAdapter(
    context: Context,
    private var sessionList: ArrayList<ResponseContentsactivitysession?>?
) :
    RecyclerView.Adapter<SessionAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private val mContext: Context

    private var onPrintClickListener: OnPrintClickListener? = null


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var btn: Button

        init {
            btn = view.findViewById<View>(R.id.buttonstatus) as Button

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.session_button, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //orderNumString = labTestList[position].toString()


        val sessiondata = this.sessionList!![position]


        holder.btn.setText(sessiondata?.name)

        if (sessiondata?.status!!) {

            holder.btn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.session))

            holder.btn.setTextColor(Color.WHITE)

//            holder.btn.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.session))
        } else {

            holder.btn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.nonsession))

            holder.btn.setTextColor(Color.BLACK)

        }


        holder.btn.setOnClickListener {

            onPrintClickListener?.onPrintClick(this.sessionList!![position]!!)

            setActive(position)

        }

        /*      holder.print.setOnClickListener {

                  onPrintClickListener?.onPrintClick(this.labTestList!![position]!!.uuid!!)

             //     this.labTestList!![position]!!.uuid

              }


              */


    }

    private fun setActive(position: Int) {

        for (i in sessionList?.indices!!) {

            sessionList!![i]?.status = i == position

        }

        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return sessionList!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    fun addAll(responseContent: List<ResponseContentsactivitysession?>?) {
        this.sessionList!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    interface OnPrintClickListener {
        fun onPrintClick(
            uuid: ResponseContentsactivitysession
        )

    }

    fun setOnPrintClickListener(onprintClickListener: OnPrintClickListener) {
        this.onPrintClickListener = onprintClickListener
    }

    fun setActiveSeation(code: String) {

        for (i in sessionList?.indices!!) {

            sessionList!![i]?.status = sessionList!![i]?.code == code
        }

        notifyDataSetChanged()


    }

}