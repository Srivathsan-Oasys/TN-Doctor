package com.hmis_tn.doctor.ui.emr_workflow.investigation.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.models.InvestigationListData
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel

import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.ToLocation
import com.hmis_tn.doctor.ui.emr_workflow.radiology.ui.RadiologyAdapter
import kotlinx.android.synthetic.main.item_lab_details.view.*


class InvestigationMobileAdapter(
    private val context: Activity,
    private val favouritesArrayList: ArrayList<InvestigationListData>
) :
    RecyclerView.Adapter<InvestigationMobileAdapter.MyViewHolder>() {

    private var typeNamesList = mutableMapOf<Int, String>()
    private var toLocationMap = mutableMapOf<Int, String>()
    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()
    private var onCommandClickListener: OnCommandClickListener? = null
    private var onitemChangeListener: OnitemChangeListener? = null

    private val hashMapOrderToLocation: HashMap<Int, Int> = HashMap()
    private val hashMapList: HashMap<Int, Int> = HashMap()
    private var onListItemClickListener: RadiologyAdapter.OnListItemClickListener? = null

    private var totalitem: Boolean? = false


    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_investigation, parent, false)

        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val selectedItem = favouritesArrayList.get(position).labDataSelected
        val selectedData = favouritesArrayList.get(position)
        if (selectedItem!!) {
            setSelectedBackground(holder)
        } else {
            setUnSelectedBackground(holder)
        }
        holder.itemView.ll_edit_view.setOnClickListener {
            onItemClickListener?.onItemClick(
                favouritesArrayList.get(position),
                position,
                selectedItem
            )

        }

        holder.itemView.rl_view.setOnClickListener {
            val popup = PopupMenu(context, holder.itemView.rl_view.textViewOptions)
            popup.inflate(R.menu.lab_options_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        onCommandClickListener!!.onCommandClick(
                            position,
                            favouritesArrayList.get(position).commands ?: ""
                        )
                    }
                    R.id.delete -> {

                        onDeleteClickListener?.onDeleteClick(
                            favouritesArrayList.get(position),
                            position
                        )
                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }
        holder.itemView.tv_no.text = (position + 1).toString()
        holder.itemView.tv_title.text =
            (selectedData.investigation_name + "-" + selectedData.selectTypeName)
        holder.itemView.tv_sub_menu.text = selectedData.selectedLocationName
        if (selectedData.selectToLocationUUID == 0 || selectedData.selectedLocationName.isNullOrEmpty() ||
            selectedData.selectTypeUUID == 0 || selectedData.selectTypeName.isNullOrEmpty()
        ) {
            favouritesArrayList[position].isReadyForSave = false
            if (favouritesArrayList[position].labDataSelected!!) {
                setSelectedAndErrorBackground(holder)
            } else {
                seterrorBackground(holder)
            }
        } else {
            favouritesArrayList[position].isReadyForSave = true
        }

    }

    fun updateSelectStatus(position: Int, selected: Boolean) {
        this.favouritesArrayList.get(position).labDataSelected = !selected
        unSelectOtherPositions(position)
        notifyDataSetChanged()
    }


    fun unSelectOtherPositions(position: Int) {
        for (i in favouritesArrayList.indices) {
            if (i != position) {
                favouritesArrayList[i].labDataSelected = false
            }
        }
    }

    fun setUnSelectedBackground(holder: InvestigationMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circel
        )
        holder.itemView.textViewOptions.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
    }

    fun setSelectedBackground(holder: InvestigationMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.colorPrimary
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.textViewOptions.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circle_grey
        )
    }

    fun seterrorBackground(holder: InvestigationMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.textViewOptions.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circle_red
        )
    }

    fun setSelectedAndErrorBackground(holder: InvestigationMobileAdapter.MyViewHolder) {
        holder.itemView.cv_outer_view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        holder.itemView.tv_title.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_sub_menu.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.textViewOptions.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tv_no.background = ContextCompat.getDrawable(
            context,
            R.drawable.circle_grey
        )
    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: InvestigationListData?,
            position: Int,
            selected: Boolean
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseContent: InvestigationListData?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    interface OnSearchInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int,
            spinnerToLocation: Spinner

        )
    }

    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }

    fun deleteRow(
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.removeAt(position1)

        getIdList()
        notifyDataSetChanged()

    }

    fun removeFromFav(
        uuid: Int
    ) {

        for (i in favouritesArrayList.indices) {

            if (favouritesArrayList[i].investigation_id == uuid) {

                this.favouritesArrayList.removeAt(i)

                break
            }

        }

        getIdList()

        notifyDataSetChanged()
    }

    fun addFavouritesInRows(
        responseContent: FavouritesModel?
    ) {
        val check =
            favouritesArrayList.any { it.investigation_id == responseContent?.test_master_id }
        if (!check) {


            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            //  favouritesArrayList.add(responseContent)


            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }

    }

    fun getItems(): ArrayList<InvestigationListData> {
        return favouritesArrayList
    }

    fun clearallAddone() {
        favouritesArrayList.clear()
        favouritesArrayList.add(InvestigationListData())
        notifyDataSetChanged()
    }


    fun clearall() {

        favouritesArrayList.clear()
        notifyDataSetChanged()

        getIdList()

    }

    interface OnCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String
        )
    }

    fun setOnCommandClickListener(onCommandClickListener: OnCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }

    fun addCommands(position: Int, command: String) {

        favouritesArrayList[position].commands = command

        notifyDataSetChanged()

    }

    fun setOnListItemClickListener(onListItemClickListener: RadiologyAdapter.OnListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener
    }

    fun Add(new: InvestigationListData) {

        favouritesArrayList.add(new)

        getIdList()

        notifyDataSetChanged()

    }

    fun updateData(new: InvestigationListData) {

        for (i in favouritesArrayList.indices) {

            if (favouritesArrayList[i].labDataSelected!!) {

                favouritesArrayList[i] = new

                break
            }

        }

        getIdList()

        notifyDataSetChanged()
    }

    fun getIdList() {

        var sendListId: ArrayList<Int> = ArrayList()

        for (i in favouritesArrayList.indices) {

            sendListId.add(favouritesArrayList[i].investigation_id!!)
        }

        onitemChangeListener?.onitemChangeClick(sendListId)

    }

    fun checkData(investigation_id: Int): Boolean {

        return favouritesArrayList.any { it.investigation_id == investigation_id }
    }

    fun UpdateTolocation(responseContents: ToLocation?, selectedLocationName: String) {

        var list = listhashmap()

        var posi = list[responseContents?.test_master_uuid]

        if (posi != null) {
            favouritesArrayList[posi].selectToLocationUUID = responseContents!!.to_location_uuid
            favouritesArrayList[posi].selectedLocationName = selectedLocationName

            notifyItemChanged(posi)
        }


    }


    fun listhashmap(): HashMap<Int, Int> {
        hashMapList.clear()


        for (i in favouritesArrayList.indices) {

            hashMapList[favouritesArrayList[i].investigation_id!!] = i

        }

        return hashMapList
    }


    interface OnitemChangeListener {
        fun onitemChangeClick(
            uuid: ArrayList<Int>
        )
    }

    fun setOnitemChangeListener(onCommandClickListener: OnitemChangeListener) {
        this.onitemChangeListener = onCommandClickListener
    }


}



