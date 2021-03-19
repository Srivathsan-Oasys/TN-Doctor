package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabToLocationResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabTypeResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.ToLocation
import kotlinx.android.synthetic.main.item_lab_details.view.*


typealias OnMobileStatusChanged = (favortemstatus: Int, position: Int, selected: Boolean) -> Unit

class RadiologyMobileAdapter(
    private val context: Activity,
    private var favouritesArrayList: ArrayList<FavouritesModel?>?,
    val onStatusChanged: OnMobileStatusChanged
) : RecyclerView.Adapter<RadiologyMobileAdapter.MyViewHolder>() {

    private var totalitem: Boolean? = false

    lateinit var selectedResponseContent: FavouritesModel
    var removedListFromOriginal: ArrayList<FavouritesModel?>? = ArrayList()


    private val hashMapType: HashMap<Int, Int> = HashMap()
    private var typeNamesList = mutableMapOf<Int, String>()
    private var testTypeArrayList: List<LabTypeResponseContent?>? = listOf()

    private val hashMapOrderToLocation: HashMap<Int, Int> = HashMap()
    private var toLocationMap = mutableMapOf<Int, String>()
    private var locationArrayList: List<LabToLocationResponse.LabToLocationContent?>? = listOf()


    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onCommandClickListener: OnCommandClickListener? = null
    private var onSelectItemCommunication: OnSelectItemCommunication? = null

    private var favouritesDataTestHashMap: HashMap<Int, Int> = HashMap()
    private var onitemChangeListener: RadiologyMobileAdapter.OnitemChangeListener? = null


    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_radiology, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedItem = favouritesArrayList?.get(position)?.labDataSelected
        val selectedData = favouritesArrayList?.get(position)
        if (selectedItem!!) {
            setSelectedBackground(holder)
        } else {
            setUnSelectedBackground(holder)
        }
        holder.itemView.ll_edit_view.setOnClickListener {
            onSelectItemCommunication?.onClick(
                position,
                selectedItem,
                favouritesArrayList?.get(position)
            )
        }
        holder.itemView.rl_view.setOnClickListener {
            showPopUp(position, holder)
        }
        holder.itemView.tv_no.text = (position + 1).toString()
        holder.itemView.tv_title.text =
            (selectedData?.test_master_name + "-" + selectedData?.selectTypeName)
        holder.itemView.tv_sub_menu.text = selectedData?.selectedLocationName
        if (selectedData?.selectToLocationUUID == 0 || selectedData?.selectedLocationName.isNullOrEmpty() ||
            selectedData?.selectTypeUUID == 0 || selectedData?.selectTypeName.isNullOrEmpty()
        ) {
            favouritesArrayList!![position]?.isReadyForSave = false
            if (favouritesArrayList!![position]?.labDataSelected!!) {
                setSelectedAndErrorBackground(holder)
            } else {
                seterrorBackground(holder)
            }
        } else {
            favouritesArrayList!![position]?.isReadyForSave = true
        }

    }


    fun showPopUp(position: Int, holder: MyViewHolder) {
        val popup = PopupMenu(context, holder.itemView.rl_view.textViewOptions)
        popup.inflate(R.menu.lab_options_menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    onCommandClickListener!!.onCommandClick(
                        position,
                        favouritesArrayList!!.get(position)!!.commands
                    )
                }
                R.id.delete -> {
                    selectedResponseContent = favouritesArrayList!![position]!!
                    onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
                    hashMapType.remove(selectedResponseContent.test_master_id)
                }

            }
            return@setOnMenuItemClickListener false
        }
        popup.show()
    }


    fun setUnSelectedBackground(holder: RadiologyMobileAdapter.MyViewHolder) {
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

    fun setSelectedBackground(holder: RadiologyMobileAdapter.MyViewHolder) {
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

    fun seterrorBackground(holder: MyViewHolder) {
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

    fun setSelectedAndErrorBackground(holder: MyViewHolder) {
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
        return favouritesArrayList!!.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun clearall() {
        favouritesArrayList?.clear()
        removedListFromOriginal?.clear()
        getIdList()
        notifyDataSetChanged()


    }

    fun deleteRow(
        position1: Int
    ): Boolean {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        if (favouritesArrayList!![position1]?.isModifiy!!) {
            removedListFromOriginal?.add(favouritesArrayList!![position1])
        }
        val data = favouritesArrayList?.get(position1)
        this.favouritesArrayList?.removeAt(position1)
        var ischeck: Boolean = true
        for (i in this.favouritesArrayList?.indices!!) {
            if (favouritesArrayList?.get(i)!!.template_id == data!!.template_id) {
                ischeck = false
                break
            }
        }
        notifyItemRemoved(position1)
        notifyDataSetChanged()
        return ischeck
    }

    /*
    Delete row from template
    */
    fun deleteRowFromTemplate(
        tempId: Int?,
        position1: Int
    ) {
        for (i in favouritesArrayList?.indices!!) {
            if (favouritesArrayList!!.get(i)?.test_master_id?.equals(tempId!!)!!) {
                this.favouritesArrayList!!.removeAt(i)
                notifyItemRemoved(i)
                getIdList()
                break
            }

        }

        notifyDataSetChanged()
    }


    fun addFavouritesInRowModule(
        favortemstatus: Int,
        responseContent: FavouritesModel?,
        position: Int,
        selected: Boolean
    ) {
        if (favortemstatus == 1) {
            val check =
                favouritesArrayList?.any { it!!.test_master_id == responseContent?.test_master_id }
            if (!check!!) {
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                favouritesArrayList?.add(responseContent)
                getIdList()
                notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                    ?.show()
                onStatusChanged.invoke(favortemstatus, position, true)
                notifyDataSetChanged()

            }
        } else {
            val check =
                favouritesArrayList?.any { it!!.test_master_id == responseContent?.test_master_id }
            if (!check!!) {
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                favouritesArrayList?.add(responseContent)
                totalitem = true
                getIdList()
                notifyDataSetChanged()
            } else {
                if (totalitem!!) {
                    Toast.makeText(
                        context,
                        "Already Item available in the list",
                        Toast.LENGTH_SHORT
                    )
                        ?.show()
                    onStatusChanged.invoke(favortemstatus, position, false)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        context,
                        "Already Item available in the list",
                        Toast.LENGTH_SHORT
                    )
                        ?.show()
                    onStatusChanged.invoke(favortemstatus, position, true)
                    notifyDataSetChanged()
                }


            }
        }


    }

    fun updateSelectStatus(
        position: Int,
        selected: Boolean
    ) {
        this.favouritesArrayList?.get(position)?.labDataSelected = !selected
        unSelectOtherPositions(position)
        notifyDataSetChanged()
    }


    fun unSelectOtherPositions(position: Int) {
        for (i in favouritesArrayList?.indices!!) {
            if (i != position) {
                favouritesArrayList!![i]?.labDataSelected = false
            }
        }
    }

    fun setadapterTypeValue(responseContents: List<LabTypeResponseContent?>?) {
        typeNamesList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        testTypeArrayList = responseContents
        notifyDataSetChanged()
    }

    fun setToLocationList(responseContents: List<LabToLocationResponse.LabToLocationContent?>?) {
        locationArrayList = responseContents
        toLocationMap =
            responseContents?.map { it?.uuid!! to it.location_name!! }!!.toMap().toMutableMap()
        hashMapOrderToLocation.clear()
        for (i in responseContents.indices) {
            hashMapOrderToLocation[responseContents[i]!!.uuid!!] = i
        }
        notifyDataSetChanged()

    }

    fun addPrevModifyInRow(
        responseContent: FavouritesModel?
    ) {
        val check =
            favouritesArrayList?.any { it!!.test_master_id == responseContent?.test_master_id }
        if (!check!!) {
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            favouritesArrayList?.add(responseContent)
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }


    fun setToLocation(
        responseContents: ToLocation?
    ) {
        for (i in favouritesArrayList?.indices!!) {
            favouritesDataTestHashMap[favouritesArrayList!![i]?.test_master_id!!] = i
        }
        var updatePostion = favouritesDataTestHashMap[responseContents?.test_master_uuid]

        val check =
            this.hashMapOrderToLocation.any { it.key == responseContents?.to_location_uuid }
        if (check) {
            this.favouritesArrayList?.get(updatePostion!!)!!.selectToLocationUUID =
                responseContents?.to_location_uuid!!
            this.favouritesArrayList?.get(updatePostion!!)!!.selectedLocationName =
                toLocationMap[responseContents.to_location_uuid].toString()
            notifyDataSetChanged()
        }
    }


    fun addCommands(position: Int, command: String) {
        favouritesArrayList?.get(position)!!.commands = command
        notifyDataSetChanged()
    }

    fun clearallAddone() {
        favouritesArrayList?.clear()
        favouritesArrayList?.add(FavouritesModel())
        notifyDataSetChanged()
    }

    fun getRemovedItems(): ArrayList<FavouritesModel?>? {
        return removedListFromOriginal
    }

    fun checkAlreadyPresent(testMasterUuid: Int?): Boolean {
        val check =
            favouritesArrayList?.any { it!!.test_master_id == testMasterUuid }
        return check!!
    }

    fun updateAdapter(labData: ArrayList<FavouritesModel?>?) {
        this.favouritesArrayList = labData
        getIdList()
        notifyDataSetChanged()

    }

    fun getIdList() {

        var sendListId: ArrayList<Int> = ArrayList()
        for (i in favouritesArrayList?.indices!!) {

            sendListId.add(favouritesArrayList!![i]?.test_master_id!!)
        }

        onitemChangeListener?.onitemChangeClick(sendListId)

    }


    fun getItems(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList!!
    }

    fun getAll(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList!!

    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun setOnCommandClickListener(onCommandClickListener: OnCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }


    fun onItemClick(onSelectItemCommunication: OnSelectItemCommunication) {
        this.onSelectItemCommunication = onSelectItemCommunication
    }

    interface OnSelectItemCommunication {
        fun onClick(
            position: Int,
            selectedItem: Boolean,
            favouritesModel: FavouritesModel?
        )
    }

    interface OnCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String
        )
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseContent: FavouritesModel?,
            position: Int
        )
    }

    interface OnitemChangeListener {
        fun onitemChangeClick(
            uuid: ArrayList<Int>
        )
    }

    fun setOnitemChangeListener(onCommandClickListener: RadiologyMobileAdapter.OnitemChangeListener) {
        this.onitemChangeListener = onCommandClickListener
    }


}