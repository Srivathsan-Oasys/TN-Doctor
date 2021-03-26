package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav

class PresManageFavAdapter(
    private val context: Activity,
    val arrayListLabFavList: ArrayList<ResponseContentsfav?>
) :
    RecyclerView.Adapter<PresManageFavAdapter.MyViewHolder>() {
    var routeSpinnerList = mutableMapOf<Int, String>()
    var instructionSpinnerList = mutableMapOf<Int, String>()
    var durationSpinnerList = mutableMapOf<Int, String>()
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var frequencySpinnerList = mutableMapOf<Int, String>()

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)

        //internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val drugNameTextView: TextView =
            itemView.findViewById(R.id.drugNameTextView)
        internal val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)

        internal val frequencyTextView: TextView = itemView.findViewById(R.id.frequencyTextView)

        internal val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)

        internal val periodTxt: TextView = itemView.findViewById(R.id.periodTxt)

        internal val displayOrderTxt: TextView = itemView.findViewById(R.id.displayOrderTxt)

        internal val instructionTextView: TextView = itemView.findViewById(R.id.instructionTextView)

        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)

        internal val listLayout: LinearLayout = itemView.findViewById(R.id.listLayout)

    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PresManageFavAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.row_prescription_save_favourite, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }


    override fun onBindViewHolder(holder: PresManageFavAdapter.MyViewHolder, position: Int) {
        val response = arrayListLabFavList[position]
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.drugNameTextView.text = response?.drug_name
        holder.durationTextView.text = response?.drug_duration?.toString()
        holder.frequencyTextView.text = response?.drug_frequency_name?.toString()
        holder.instructionTextView.text = response?.drug_instruction_name?.toString()
        holder.routeTextView.text = response?.drug_route_name?.toString()
        holder.periodTxt.text = response?.drug_period_name?.toString()
        holder.displayOrderTxt.text = response?.favourite_display_order?.toString()



        holder.deleteImageView.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(response?.favourite_id!!, response.drug_name)
        }
        if (position % 2 == 0) {
            holder.listLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        } else {
            holder.listLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }


    }

    fun setFavAddItem(responseContents: ResponseContentsfav?) {

        arrayListLabFavList.add(responseContents)
        notifyDataSetChanged()
    }

    /*
   Delete
    */
    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesID: Int?,
            testMasterName: String?
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun clearadapter() {
        arrayListLabFavList.clear()
        notifyDataSetChanged()
    }

    fun adapterrefresh(deletefavouriteID: Int?) {

        for (i in arrayListLabFavList.indices) {
            if (arrayListLabFavList.get(i)?.favourite_id?.equals(deletefavouriteID!!)!!) {
                this.arrayListLabFavList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()

    }

    fun setfrequencySpinnerList(frequencySpinnerList: MutableMap<Int, String>) {

        this.frequencySpinnerList = frequencySpinnerList

        notifyDataSetChanged()

    }

    fun clearAll() {

        arrayListLabFavList.clear()

        notifyDataSetChanged()

    }

    fun setRote(routeSpinnerList: MutableMap<Int, String>) {

        this.routeSpinnerList = routeSpinnerList

        notifyDataSetChanged()

    }


    fun setinstructionSpinnerList(instructionSpinnerList: MutableMap<Int, String>) {

        this.instructionSpinnerList = instructionSpinnerList

        notifyDataSetChanged()

    }

    fun setdurationSpinnerList(durationSpinnerList: MutableMap<Int, String>) {

        this.durationSpinnerList = durationSpinnerList

        notifyDataSetChanged()

    }

}