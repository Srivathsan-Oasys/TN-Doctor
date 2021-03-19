package com.oasys.digihealth.doctor.ui.tutorial.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.tutorial.model.UserManualResponseContent
import com.oasys.digihealth.doctor.ui.tutorial.model.UserModuleResponseContent
import com.oasys.digihealth.doctor.ui.tutorial.viewmodel.VideoTutorialViewModel
import com.oasys.digihealth.doctor.utils.Utils

class VideoManualMainAdapter(
    context: Context,
    private var userManualTutotrial: ArrayList<UserManualResponseContent?>?,
    private var clickListener: TutorialCallback,
    private var viewModel: VideoTutorialViewModel
) :
    RecyclerView.Adapter<VideoManualMainAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    private var customdialog: Dialog? = null

    var utils: Utils? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var nameTextView: TextView

        var recyclerView: RecyclerView

        init {

            nameTextView = view.findViewById<View>(R.id.tvTitle) as TextView

            recyclerView = view.findViewById<View>(R.id.ListRecyclerView) as RecyclerView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_main_user_manal, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(
            itemLayout
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reportData = userManualTutotrial!![position]
        holder.nameTextView.text = getFileName(reportData?.module_arr?.get(0)?.activity?.name!!)

        utils = Utils(this.mContext)


        if (utils?.isTablet(mContext)!!) {
            holder.recyclerView.layoutManager =
                GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
        } else {
            holder.recyclerView.layoutManager =
                GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false)
        }



        holder.recyclerView.adapter = VideoTutorialAdapter(
            mContext, reportData.module_arr as ArrayList<UserModuleResponseContent?>,
            this.clickListener, viewModel
        )


    }

    override fun getItemCount(): Int {
        return userManualTutotrial!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<UserManualResponseContent?>?) {

        this.userManualTutotrial!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.userManualTutotrial?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(UserManualResponseContent())
    }

    fun add(r: UserManualResponseContent) {
        userManualTutotrial!!.add(r)
        notifyItemInserted(userManualTutotrial!!.size - 1)
    }

    fun getItem(position: Int): UserManualResponseContent? {
        return userManualTutotrial!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = userManualTutotrial!!.size - 1
        val result = getItem(position)
        if (result != null) {
            userManualTutotrial!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getFileName(wholePath: String): String {
        var name: String? = null
        val start: Int
        val end: Int
        start = wholePath.lastIndexOf('/')
        end = wholePath.length
        name = wholePath.substring((start + 1), end)
        return name
    }

}