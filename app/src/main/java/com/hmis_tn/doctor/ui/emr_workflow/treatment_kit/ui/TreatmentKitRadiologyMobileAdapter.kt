package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

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
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.LabToLocationResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.LabTypeResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempDetails
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.ToLocation
import com.hmis_tn.doctor.ui.quick_reg.model.ResponseTestMethodContent
import kotlinx.android.synthetic.main.row_mobile_treatment_kit_radiology.view.*


class TreatmentKitRadiologyMobileAdapter(
    private val context: Activity,
    private var favouritesArrayList: ArrayList<FavouritesModel?>,
    private val templeteArrayList: ArrayList<TempDetails?>
) :
    RecyclerView.Adapter<TreatmentKitRadiologyMobileAdapter.MyViewHolder>(), ITreatmentKitAdapter {

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
    var hashMapRadiologyList: HashMap<Int, Int> = HashMap()
    private var testMethodMap = mutableMapOf<Int, String>()
    private val hashMaptestMethodMap: HashMap<Int, Int> = HashMap()

    private var testMethodArrayList: List<ResponseTestMethodContent?>? = listOf()
    private var testTypeArrayList: List<LabTypeResponseContent?>? = listOf()
    private var locationArrayList: List<LabToLocationResponse.LabToLocationContent?>? = listOf()

    private var onSelectItemCommunication: OnSelectItemCommunication? = null

    var removedListFromOriginal: ArrayList<FavouritesModel?>? = ArrayList()

    private var favouritesDataTestMethodHashMap: HashMap<Int, Int> = HashMap()

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_mobile_treatment_kit_radiology, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        selectedResponseContent = favouritesArrayList[position]!!


//        holder.itemView.radiologyautoCompleteTextView.text = selectedResponseContent.itemAppendString
//        if (selectedResponseContent.test_master_code.toString().equals("null")) {
//            holder.itemView.radiologyCodeAutoCompleteTextView.text = ""
//        } else {
//            holder.itemView.radiologyCodeAutoCompleteTextView.text = selectedResponseContent.test_master_code.toString()
//        }


        val selectedItem = favouritesArrayList.get(position)?.labDataSelected ?: false
        val selectedData = favouritesArrayList.get(position)
        if (selectedItem) {
            setSelectedBackground(holder)
        } else {
            setUnSelectedBackground(holder)
        }
        holder.itemView.mainLinearLayout.setOnClickListener {
            onSelectItemCommunication?.onClick(
                position,
                selectedItem,
                favouritesArrayList.get(position)
            )
        }
        hashMapRadiologyList.put(
            favouritesArrayList[position]?.test_master_id ?: 0,
            favouritesArrayList[position]?.template_id ?: 0
        )
        holder.itemView.moreImageView.setOnClickListener {
            val popup = PopupMenu(context, holder.itemView.moreImageView)
            popup.inflate(R.menu.lab_options_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        onCommandClickListener!!.onCommandClick(
                            position,
                            favouritesArrayList.get(position)!!.commands
                        )
                    }
                    R.id.delete -> {
                        selectedResponseContent = favouritesArrayList[position]!!
                        onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
                        hashMapType.remove(selectedResponseContent.test_master_id)
                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }
//        holder.itemView.tv_no.text = (position + 1).toString()
//        holder.itemView.tvRadiologyName.text =
//            (selectedData?.test_master_name + "-" + selectedData?.selectTypeName)
//        holder.itemView.tvRadiologyDesc.text = selectedData?.selectedLocationName
        holder.itemView.tvRadiologyName.text =
            "${selectedData?.radiology_name} - ${selectedData?.selectTypeName}"
        holder.itemView.tvRadiologyDesc.text = selectedData?.selectedLocationName

        if (selectedData?.selectToLocationUUID == 0 || selectedData?.selectedLocationName.isNullOrEmpty() ||
            selectedData?.selectTypeUUID == 0 || selectedData?.selectTypeName.isNullOrEmpty()
        ) {
            favouritesArrayList[position]?.isReadyForSave = false
            if (favouritesArrayList[position]?.labDataSelected!!) {
                setSelectedAndErrorBackground(holder)
            } else {
                seterrorBackground(holder)
            }
        } else {
            favouritesArrayList[position]?.isReadyForSave = true
        }
    }

    fun setUnSelectedBackground(holder: TreatmentKitRadiologyMobileAdapter.MyViewHolder) {
        holder.itemView.mainLinearLayout.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tvRadiologyName.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
        holder.itemView.tvRadiologyDesc.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey
            )
        )
    }

    fun setSelectedBackground(holder: TreatmentKitRadiologyMobileAdapter.MyViewHolder) {
        holder.itemView.mainLinearLayout.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.colorPrimary
            )
        )
        holder.itemView.tvRadiologyName.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tvRadiologyDesc.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
    }

    fun seterrorBackground(holder: MyViewHolder) {
        holder.itemView.mainLinearLayout.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        holder.itemView.tvRadiologyName.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tvRadiologyDesc.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
    }

    fun setSelectedAndErrorBackground(holder: MyViewHolder) {
        holder.itemView.mainLinearLayout.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        holder.itemView.tvRadiologyName.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        holder.itemView.tvRadiologyDesc.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
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
        favouritesArrayList.clear()
        removedListFromOriginal?.clear()
        notifyDataSetChanged()

    }


    fun clearallAddone() {
        favouritesArrayList.clear()
        favouritesArrayList.add(FavouritesModel())
        notifyDataSetChanged()
    }

    fun getRemovedItems(): ArrayList<FavouritesModel?>? {
        return removedListFromOriginal
    }


    fun deleteRow(
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.removeAt(position1)
        notifyDataSetChanged()
    }

    fun deleteRow(
        radiologyCode: String?
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.forEach {
            if (it?.radiology_code == radiologyCode) {
                favouritesArrayList.remove(it)
            }
        }
        notifyDataSetChanged()
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

        for (i in favouritesArrayList.indices) {
            if (favouritesArrayList.get(i)?.test_master_id?.equals(tempId!!)!! &&
                favouritesArrayList.get(i)?.viewLabstatus?.equals(position1)!!
            ) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()
        //  addRow(FavouritesModel())
    }

    fun checkAlreadyPresent(testMasterUuid: Int?): Boolean {
        val check = favouritesArrayList.any { it!!.test_master_id == testMasterUuid }
        return !check
    }

    fun addFavouritesInRowModule(
        favortemstatus: Int,
        responseContent: FavouritesModel?,
        position: Int,
        selected: Boolean
    ) {
        if (favortemstatus == 1) {
            val check =
                favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
            if (!check) {
                // favouritesArrayList.removeAt(favouritesArrayList.size - 1)
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                /*responseContent?.selectTypeUUID = testTypeArrayList?.get(0)?.uuid!!
                responseContent?.selectTypeName = testTypeArrayList?.get(0)?.name
                responseContent?.selectToLocationUUID = locationArrayList?.get(0)?.uuid!!
                responseContent?.selectedLocationName =  locationArrayList?.get(0)?.location_name!!
                responseContent?.selectToTestMethodUUID = testMethodArrayList?.get(0)?.uuid!!
                responseContent?.selectToTestMethodName = testMethodArrayList?.get(0)?.name*/
                favouritesArrayList.add(responseContent)
                //    favouritesArrayList.add(FavouritesModel())
                notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                    ?.show()
                //onStatusChanged.invoke(favortemstatus,position, true)
                notifyDataSetChanged()

            }
        } else {
            val check =
                favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
            if (!check) {
                // favouritesArrayList.removeAt(favouritesArrayList.size - 1)
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                /*  responseContent?.selectTypeUUID = testTypeArrayList?.get(0)?.uuid!!
                  responseContent?.selectTypeName = testTypeArrayList?.get(0)?.name
                  responseContent?.selectToLocationUUID = locationArrayList?.get(0)?.uuid!!
                  responseContent?.selectedLocationName =  locationArrayList?.get(0)?.location_name!!
                  responseContent?.selectToTestMethodUUID = testMethodArrayList?.get(0)?.uuid!!
                  responseContent?.selectToTestMethodName = testMethodArrayList?.get(0)?.name*/
                favouritesArrayList.add(responseContent)
                // favouritesArrayList.add(FavouritesModel())
                totalitem = true
                notifyDataSetChanged()
            } else {
                if (totalitem!!) {
                    Toast.makeText(
                        context,
                        "Already Item available in the list",
                        Toast.LENGTH_SHORT
                    )
                        ?.show()
                    //onStatusChanged.invoke(favortemstatus,position, false)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        context,
                        "Already Item available in the list",
                        Toast.LENGTH_SHORT
                    )
                        ?.show()
                    // onStatusChanged.invoke(favortemstatus,position, true)
                    notifyDataSetChanged()
                }


            }
        }


    }

    fun addSaveTemplateInRow(
        responseContent: FavouritesModel?
    ) {


        val check =
            favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }

        if (!check) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            favouritesArrayList.add(responseContent)
            favouritesArrayList.add(FavouritesModel())
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
            favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }

        if (!check) {
            favouritesArrayList.add(responseContent)
            notifyItemInserted(favouritesArrayList.size - 1)
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }


    fun setDuration(durationArrayList_: ArrayList<DurationResponseContent?>) {
        this.durationArrayList = durationArrayList_
        notifyDataSetChanged()
    }


    fun getItems(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList
    }

    fun getAll(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList

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
        this.favouritesArrayList.get(position)?.labDataSelected = !selected
        unSelectOtherPositions(position)
        notifyDataSetChanged()
    }


    fun unSelectOtherPositions(position: Int) {
        for (i in favouritesArrayList.indices) {
            if (i != position) {
                favouritesArrayList[i]?.labDataSelected = false
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
        favouritesArrayList.get(position)!!.commands = command
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
        testMethodMap = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        for (i in responseContents.indices) {
            hashMaptestMethodMap[responseContents[i]?.uuid!!] = i
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
            favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
        if (!check) {
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.test_master_id = responseContent?.test_master_id
            favouritesArrayList.add(responseContent)
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }


    fun updateAdapter(radiologyData: ArrayList<FavouritesModel?>) {
        radiologyData.let { this.favouritesArrayList = it }
        notifyDataSetChanged()

    }

    fun setToLocation(
        responseContents: ToLocation?
    ) {
        for (i in favouritesArrayList.indices) {
            favouritesDataTestMethodHashMap[favouritesArrayList[i]?.test_master_id!!] = i
        }
        var updatePostion = favouritesDataTestMethodHashMap[responseContents?.test_master_uuid]

        val check =
            this.hashMapOrderToLocation.any { it.key == responseContents?.to_location_uuid }
        if (check) {
            this.favouritesArrayList.get(updatePostion!!)!!.selectToLocationUUID =
                responseContents?.to_location_uuid!!
            this.favouritesArrayList.get(updatePostion)!!.selectedLocationName =
                toLocationMap[responseContents.to_location_uuid].toString()
            notifyDataSetChanged()
        }
    }

    fun setTestMethod(typeOfMethodUuid: Int?, uuid: Int?, testMethodNAme: String?) {
        for (i in favouritesArrayList.indices) {
            favouritesDataTestMethodHashMap[favouritesArrayList[i]?.test_master_id!!] = i
        }
        var updatePosition = favouritesDataTestMethodHashMap[uuid]
        favouritesArrayList[updatePosition!!]?.selectToTestMethodUUID = typeOfMethodUuid!!
        favouritesArrayList[updatePosition]?.selectToTestMethodName = testMethodNAme
        notifyDataSetChanged()
    }


    interface OnSelectItemCommunication {
        fun onClick(
            position: Int,
            selectedItem: Boolean,
            favouritesModel: FavouritesModel?
        )
    }
}
