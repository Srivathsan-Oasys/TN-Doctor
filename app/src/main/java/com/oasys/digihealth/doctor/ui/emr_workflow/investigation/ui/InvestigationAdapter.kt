package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationLocationResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationTypeResponseContent

import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.ToLocation
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyAdapter


typealias OnStatusChanged = (favortemstatus: Int, position: Int, selected: Boolean) -> Unit

class InvestigationAdapter(
    private val context: Activity,
    private val favouritesArrayList: ArrayList<FavouritesModel?>,
    private val templeteArrayList: ArrayList<TempDetails?>,
    val onStatusChanged: OnStatusChanged

) :
    RecyclerView.Adapter<InvestigationAdapter.MyViewHolder>() {

    private var typeNamesList = mutableMapOf<Int, String>()
    private var toLocationMap = mutableMapOf<Int, String>()
    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()
    lateinit var selectedResponseContent: FavouritesModel
    private var onCommandClickListener: OnCommandClickListener? = null

    private val hashMapOrderToLocation: HashMap<Int, Int> = HashMap()
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
        selectedResponseContent = favouritesArrayList[position]!!
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.autoCompleteTextView.setText(selectedResponseContent.itemAppendString, false)
        if (selectedResponseContent.test_master_code == null) {

            holder.codeTextView.text = " "
        } else {
            holder.codeTextView.text = selectedResponseContent.test_master_code.toString()

        }

/*
        holder.codeTextView.setText(selectedResponseContent.test_master_code.toString())
*/

        holder.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.autoCompleteTextView, position,
                        holder.spinnerToLocation

                    )
                }
            }
        })
        holder.commentsImageView.setOnClickListener {
            onCommandClickListener!!.onCommandClick(
                position,
                favouritesArrayList.get(position)!!.commands
            )
        }



        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }



        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(selectedResponseContent, position)
        }
        holder.deleteImageView.setOnClickListener {

            selectedResponseContent = favouritesArrayList[position]!!

            if (!holder.autoCompleteTextView.text.trim().isEmpty()) {

                onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
                toLocationMap.remove(selectedResponseContent.selectToLocationUUID)
                typeNamesList.remove(selectedResponseContent.selectTypeUUID)

            }

        }
        if (position == favouritesArrayList.size - 1) {
            holder.deleteImageView.alpha = 0.2f
            holder.deleteImageView.isEnabled = false
            holder.autoCompleteTextView.isFocusable = true
            holder.autoCompleteTextView.isFocusableInTouchMode = true

            holder.autoCompleteTextView.requestFocus()
        } else {
            holder.deleteImageView.alpha = 1f
            holder.deleteImageView.isEnabled = true

            holder.autoCompleteTextView.isFocusable = false
            holder.autoCompleteTextView.isFocusableInTouchMode = false
        }


        val adapter =
            ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                typeNamesList.values.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner_type.adapter = adapter
        holder.spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()
                selectedResponseContent = favouritesArrayList[position]!!
                selectedResponseContent.selectTypeUUID =
                    typeNamesList.filterValues { it == itemValue }.keys.toList()[0]
                Log.i(
                    "InvestigationType",
                    "name = " + itemValue + "uuid=" + typeNamesList.filterValues { it == itemValue }.keys.toList()[0]
                )
            }

        }


        val locationAdapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                toLocationMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.spinnerToLocation.adapter = locationAdapter
        try {
            if (hashMapOrderToLocation.containsKey(selectedResponseContent.selectToLocationUUID)) {
                holder.spinnerToLocation.setSelection(
                    hashMapOrderToLocation.get(
                        selectedResponseContent.selectToLocationUUID
                    )!!
                )
            }
        } catch (e: Exception) {
            Log.i("SpinnerMapErr", e.toString())
        }

        holder.spinnerToLocation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {


                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedResponseContent = favouritesArrayList[position]!!
                    selectedResponseContent.selectToLocationUUID =
                        toLocationMap.filterValues { it == itemValue }.keys.toList()[0]
                }

            }


    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val autoCompleteTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.autoCompleteTextView)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
        internal val spinner_type: Spinner = itemView.findViewById(R.id.type_spinner)
        internal val spinnerToLocation: Spinner = itemView.findViewById(R.id.tolocation)
        internal val codeTextView: TextView = itemView.findViewById(R.id.codeTextView)
        internal val commentsImageView: ImageView = itemView.findViewById(R.id.commentsImageView)

    }

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

    fun deleteRow(
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.removeAt(position1)
        notifyDataSetChanged()

    }


    fun addFavouritesInRows(
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
        favouritesArrayList.add(responseContent)
        notifyItemInserted(favouritesArrayList.size - 1)
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

    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<InvestigationSearchResponseContent>,
        searchposition: Int,
        spinnerToLocation: Spinner
    ) {

        val responseContentAdapter = InvestigationSearchResultAdapter(
            context,
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi =
                parent.adapter.getItem(position) as InvestigationSearchResponseContent?


            val check = favouritesArrayList.any { it!!.investigation_id == selectedPoi?.uuid }
            if (!check) {

                dropdownReferenceView.setText(selectedPoi?.name)
                favouritesArrayList[searchposition]?.investigation_name = selectedPoi?.name
                favouritesArrayList[searchposition]?.itemAppendString = selectedPoi?.name
                favouritesArrayList[searchposition]?.test_master_name = selectedPoi?.name
                favouritesArrayList[searchposition]?.investigation_id = selectedPoi?.uuid
                favouritesArrayList[searchposition]?.test_master_code = selectedPoi?.code.toString()

                onListItemClickListener!!.onListItemClick(
                    favouritesArrayList[searchposition],
                    searchposition,
                    spinnerToLocation
                )


                addRow(FavouritesModel())

                notifyDataSetChanged()
            } else {

                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()

            }
        }

//        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
//            val selectedPoi = parent.adapter.getItem(position) as InvestigationSearchResponseContent?
//            dropdownReferenceView.setText(selectedPoi?.name)
//            val st = FavouritesModel()
//            st.chief_complaint_name = "dummy"
//            selectedResponseContent.chief_complaint_name = selectedPoi?.name
//            selectedResponseContent.itemAppendString = selectedPoi?.name
//            if (!favouritesArrayList.contains(st)) {
//                addRow(FavouritesModel())
//            }
//        }
    }

    fun setadapterTypeValue(responseContents: List<InvestigationTypeResponseContent?>?) {

        typeNamesList = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()
        notifyDataSetChanged()

    }

/*
    fun setToLocationList(responseContents: List<InvestigationLocationResponseContent?>?) {
        toLocationMap =
            responseContents?.map { it?.uuid!! to it.location_name!! }!!.toMap().toMutableMap()
        notifyDataSetChanged()

    }
*/

    fun getItems(): ArrayList<FavouritesModel?> {
        return favouritesArrayList
    }

    fun clearallAddone() {
        favouritesArrayList.clear()
        favouritesArrayList.add(FavouritesModel())
        notifyDataSetChanged()
    }


    fun clearall() {

        favouritesArrayList.clear()
        notifyDataSetChanged()

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

        favouritesArrayList[position]!!.commands = command

        notifyDataSetChanged()

    }

    fun setOnListItemClickListener(onListItemClickListener: RadiologyAdapter.OnListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener
    }


    fun setToLocationList(
        responseContents: List<InvestigationLocationResponseContent?>?
    ) {
        toLocationMap =
            responseContents?.map { it?.uuid!! to it.location_name }!!.toMap().toMutableMap()

        hashMapOrderToLocation.clear()

        for (i in responseContents.indices) {

            hashMapOrderToLocation[responseContents[i]!!.uuid] = i
        }

        notifyDataSetChanged()
    }

    fun setToLocation(
        responseContents: ToLocation?,
        spinnerToLocation: Spinner,
        searchPosition: Int?
    ) {

        val check =
            this.hashMapOrderToLocation.any { it.key == responseContents?.to_location_uuid }
        if (check) {

            // spinnerToLocation.setSelection(hashMapOrderToLocation[responseContents?.to_location_uuid]!!)

            this.favouritesArrayList[searchPosition!!]!!.selectToLocationUUID =
                responseContents?.to_location_uuid!!

            notifyItemChanged(searchPosition)
            /*   for(i in 0 .. toLocationMap.size-1){

                   if(toLocationMap[i]==)
                       spinnerToLocation.setSelection(0)

               }*/

        }
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
                favouritesArrayList.removeAt(favouritesArrayList.size - 1)
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                favouritesArrayList.add(responseContent)
                favouritesArrayList.add(FavouritesModel())
                notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                    ?.show()
                onStatusChanged.invoke(favortemstatus, position, true)
                notifyDataSetChanged()

            }
        } else {
            val check =
                favouritesArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
            if (!check) {
                favouritesArrayList.removeAt(favouritesArrayList.size - 1)
                responseContent?.itemAppendString = responseContent?.test_master_name
                responseContent?.test_master_id = responseContent?.test_master_id
                favouritesArrayList.add(responseContent)
                favouritesArrayList.add(FavouritesModel())
                notifyDataSetChanged()
                totalitem = true
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
        if (favouritesArrayList.size > 0) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
        }

        for (i in favouritesArrayList.indices) {
            if (favouritesArrayList.get(i)?.test_master_id?.equals(tempId!!)!! && favouritesArrayList.get(
                    i
                )?.viewLabstatus?.equals(position1)!!
            ) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()
        addRow(FavouritesModel())
    }


}



