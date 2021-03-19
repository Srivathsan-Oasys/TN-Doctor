package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabToLocationResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabTypeResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.ToLocation
import com.oasys.digihealth.doctor.ui.quick_reg.model.ResponseTestMethodContent
import kotlinx.android.synthetic.main.item_lab_details.view.*


typealias OnStatusChangedNew = (favortemstatus: Int, position: Int, selected: Boolean) -> Unit


class LabListingAdapter(
    private val context: Activity,
    private var favouritesArrayList: ArrayList<FavouritesModel?>?,
    private val templeteArrayList: ArrayList<TempDetails?>,
    val onStatusChanged: OnStatusChangedNew
) : RecyclerView.Adapter<LabListingAdapter.MyViewHolder>() {

    private var totalitem: Boolean? = false
    private val hashMapType: HashMap<Int, Int> = HashMap()
    private val hashMapOrderToLocation: HashMap<Int, Int> = HashMap()
    private var typeNamesList = mutableMapOf<Int, String>()
    private var toLocationMap = mutableMapOf<Int, String>()
    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()
    lateinit var selectedResponseContent: FavouritesModel
    private var onCommandClickListener: OnCommandClickListener? = null
    private var onstatus: OnstatuschangeListener? = null
    private var onListItemClickListener: OnListItemClickListener? = null
    var hashMapLabList: HashMap<Int, Int> = HashMap()
    private var testMethodMap = mutableMapOf<Int, String>()
    private val hashMaptestMethodMap: HashMap<Int, Int> = HashMap()

    private var testMethodArrayList: List<ResponseTestMethodContent?>? = listOf()
    private var testTypeArrayList: List<LabTypeResponseContent?>? = listOf()
    private var locationArrayList: List<LabToLocationResponse.LabToLocationContent?>? = listOf()

    private var onSelectItemCommunication: OnSelectItemCommunication? = null


    var removedListFromOriginal: ArrayList<FavouritesModel?>? = ArrayList()
    private var onitemChangeListener: LabListingAdapter.OnitemChangeListener? = null

    private var favouritesDataTestMethodHashMap: HashMap<Int, Int> = HashMap()


    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_lab, parent, false)


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
        hashMapLabList.put(
            favouritesArrayList!![position]!!.test_master_id!!,
            favouritesArrayList!![position]!!.template_id
        )
        holder.itemView.rl_view.setOnClickListener {
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
        holder.itemView.tv_no.text = (position + 1).toString()
        holder.itemView.tv_title.text =
            (selectedData?.test_master_name + "-" + selectedData?.selectTypeName)
        holder.itemView.tv_sub_menu.text =
            selectedData?.selectToTestMethodName + "-" + selectedData?.selectedLocationName
        if (selectedData?.selectToTestMethodUUID == 0 || selectedData?.selectToTestMethodName.isNullOrEmpty()
            || selectedData?.selectToLocationUUID == 0 || selectedData?.selectedLocationName.isNullOrEmpty() ||
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

/*
        holder.itemView.setOnClickListener {
            selectedResponseContent = favouritesArrayList?.get(position)!!
            onItemClickListener?.onItemClick(selectedResponseContent, position)
        }*/

    }


    fun setUnSelectedBackground(holder: LabListingAdapter.MyViewHolder) {
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

    fun setSelectedBackground(holder: LabListingAdapter.MyViewHolder) {
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

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: FavouritesModel?,
            position: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseContent: FavouritesModel?,
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

    fun clearall() {
        favouritesArrayList?.clear()
        removedListFromOriginal?.clear()
        getIdList()
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
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        val OriginalItemCount = itemCount

        for (i in favouritesArrayList?.indices!!) {
            if (favouritesArrayList!!.get(i)?.test_master_id?.equals(tempId!!)!!
            ) {
                this.favouritesArrayList!!.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        getIdList()
        notifyDataSetChanged()
        //  addRow(FavouritesModel())
    }

    fun checkAlreadyPresent(testMasterUuid: Int?): Boolean {
        val check =
            favouritesArrayList?.any { it!!.test_master_id == testMasterUuid }
        return !check!!
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

    fun addSaveTemplateInRow(
        responseContent: FavouritesModel?
    ) {


        val check =
            favouritesArrayList?.any { it!!.test_master_id == responseContent?.test_master_id }

        if (!check!!) {
            favouritesArrayList?.removeAt(favouritesArrayList!!.size - 1)
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            favouritesArrayList?.add(responseContent)
            favouritesArrayList?.add(FavouritesModel())
            notifyDataSetChanged()
        } else {

            notifyDataSetChanged()

            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()

        }
    }

    fun addRow(
        responseContent: FavouritesModel?

    ) {
        val check =
            favouritesArrayList?.any { it!!.test_master_id == responseContent?.test_master_id }

        if (!check!!) {
            favouritesArrayList?.add(responseContent)
            notifyItemInserted(favouritesArrayList?.size!! - 1)
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    fun addTempleteRow(
        responseContent: TempDetails?
    ) {
        templeteArrayList.add(responseContent)
        notifyItemInserted(templeteArrayList.size - 1)
    }

    fun setDuration(durationArrayList_: ArrayList<DurationResponseContent?>) {
        this.durationArrayList = durationArrayList_
        notifyDataSetChanged()
    }


    fun getItems(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList!!
    }

    fun getAll(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList!!

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


    fun onItemClick(onSelectItemCommunication: OnSelectItemCommunication) {
        this.onSelectItemCommunication = onSelectItemCommunication
    }


    fun updateSelectStatus(position: Int, selected: Boolean) {
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

    /*
    onstatuslister
     */
    interface OnstatuschangeListener {
        fun onStatuschange(position: Int, selected: Boolean)
    }

    fun addCommands(position: Int, command: String) {
        favouritesArrayList?.get(position)!!.commands = command
        notifyDataSetChanged()
    }

    interface OnListItemClickListener {
        fun onListItemClick(
            responseContent: FavouritesModel?,
            position: Int,
            spinnerToLocation: Spinner
        )
    }

    fun setOnListItemClickListener(onListItemClickListener: OnListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener
    }


    fun setadapterTestMethodValue(responseContents: List<ResponseTestMethodContent?>?) {
        testMethodArrayList = responseContents
        testMethodMap = responseContents?.map { it.uuid!! to it.name!! }!!.toMap().toMutableMap()
        for (i in responseContents.indices) {
            hashMaptestMethodMap[responseContents[i].uuid!!] = i
        }
        notifyDataSetChanged()
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


    fun updateAdapter(labData: ArrayList<FavouritesModel?>?) {
        this.favouritesArrayList = labData
        getIdList()
        notifyDataSetChanged()

    }

    fun setToLocation(
        responseContents: ToLocation?
    ) {
        for (i in favouritesArrayList?.indices!!) {
            favouritesDataTestMethodHashMap[favouritesArrayList!![i]?.test_master_id!!] = i
        }
        var updatePostion = favouritesDataTestMethodHashMap[responseContents?.test_master_uuid]

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

    fun setTestMethod(typeOfMethodUuid: Int?, uuid: Int?, testMethodNAme: String?) {
        for (i in favouritesArrayList?.indices!!) {
            favouritesDataTestMethodHashMap[favouritesArrayList!![i]?.test_master_id!!] = i
        }
        var updatePosition = favouritesDataTestMethodHashMap[uuid]
        favouritesArrayList!![updatePosition!!]?.selectToTestMethodUUID = typeOfMethodUuid!!
        favouritesArrayList!![updatePosition]?.selectToTestMethodName = testMethodNAme
        notifyDataSetChanged()
    }


    fun getIdList() {

        var sendListId: ArrayList<Int> = ArrayList()
        for (i in favouritesArrayList?.indices!!) {

            sendListId.add(favouritesArrayList!![i]?.test_master_id!!)
        }

        onitemChangeListener?.onitemChangeClick(sendListId)

    }

    interface OnSelectItemCommunication {
        fun onClick(
            position: Int,
            selectedItem: Boolean,
            favouritesModel: FavouritesModel?
        )
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
