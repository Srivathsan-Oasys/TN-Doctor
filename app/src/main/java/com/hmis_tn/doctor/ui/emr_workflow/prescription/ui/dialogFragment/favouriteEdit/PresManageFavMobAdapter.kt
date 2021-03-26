package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav

class PresManageFavMobAdapter(
    private val context: Activity,
    val arrayListLabFavList: ArrayList<ResponseContentsfav?>
) :
    RecyclerView.Adapter<PresManageFavMobAdapter.MyViewHolder>() {
    var routeSpinnerList = mutableMapOf<Int, String>()
    var instructionSpinnerList = mutableMapOf<Int, String>()
    var durationSpinnerList = mutableMapOf<Int, String>()
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var frequencySpinnerList = mutableMapOf<Int, String>()

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val mainLinearLayout: CardView = itemView.findViewById(R.id.cardView)
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val detailsTextView: TextView = itemView.findViewById(R.id.DetailsTextView)
        internal val moreImageView: ImageView = itemView.findViewById(R.id.moreImageView)


    }

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PresManageFavMobAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_manage_pre_fav_temp, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListLabFavList.size
    }


    override fun onBindViewHolder(holder: PresManageFavMobAdapter.MyViewHolder, position: Int) {

        val response = arrayListLabFavList[position]

        holder.nameTextView.text = response!!.drug_name + "- " + response.drug_route_name

        holder.detailsTextView.text =
            response.drug_frequency_name + "- " + "${response.drug_duration} ${response.drug_period_name} - ${response.drug_instruction_name}- Order (${response.favourite_display_order})"


        holder.moreImageView.setOnClickListener {

            val popup =
                PopupMenu(context, holder.moreImageView)
            //inflating menu from xml resource
            popup.inflate(R.menu.delete_menu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.delete ->  //handle menu2 click
                    {
                        onDeleteClickListener?.onDeleteClick(
                            response.favourite_id!!,
                            response.drug_name
                        )

                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
            //displaying the popup
            popup.show()

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