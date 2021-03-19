package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.ui

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
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.LabtechData
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.ToLocation
import com.oasys.digihealth.doctor.ui.quick_reg.model.GetReference
import com.oasys.digihealth.doctor.ui.quick_reg.model.LocationMasterX
import com.oasys.digihealth.doctor.ui.quick_reg.model.ResponseTestMethodContent
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.LabTestSpinnerresponseContent


class LmisLabTechAdapter(private val context: Activity) :
    RecyclerView.Adapter<LmisLabTechAdapter.MyViewHolder>() {
    private var selectedResponseContent: LabtechData? = null

    private val mLayoutInflater: LayoutInflater
    var orderNumString: String? = null
    private var isLoadingAdded = false
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onListItemClickListener: OnListItemClickListener? = null

    // private var onItemClickListener: OnItemClickListener? = null
    private var responseContent: ArrayList<LabtechData?>? = ArrayList()

    var removedListFromOriginal: ArrayList<LabtechData?>? = ArrayList()


    private var listfilterspecimanitem: ArrayList<ResponseTestMethodContent?>? = ArrayList()
    private var listfilterTestMethoditem: ArrayList<ResponseTestMethodContent?>? = ArrayList()

    private var typeNamesList = mutableMapOf<Int, String>()
    private var toLocationMap = mutableMapOf<Int, String>()
    private var spectiumMap = mutableMapOf<Int, String>()
    private var testMethodMap = mutableMapOf<Int, String>()
    private var onCommandClickListener: OnCommandClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null

    private val hashMapType: HashMap<Int, Int> = HashMap()

    private val hashMapOrderToLocation: HashMap<Int, Int> = HashMap()

    private val hashMapSpeciumMap: HashMap<Int, Int> = HashMap()

    private val hashMaptestMethodMap: HashMap<Int, Int> = HashMap()

//    private var testMethodArrayList: List<ResponseTestMethodContent?>?= listOf()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumberTextView: TextView
        var name: AppCompatAutoCompleteTextView
        var type: Spinner
        var Specimen: Spinner
        var TestMethod: Spinner
        var OrderToLocationId: Spinner
        var delete: ImageView
        var mainLinearLayout: LinearLayout
        var commentsImageView: ImageView

        init {
            serialNumberTextView = view.findViewById<View>(R.id.serialNoTextView) as TextView
            name = view.findViewById<View>(R.id.labname) as AppCompatAutoCompleteTextView
            type = view.findViewById<View>(R.id.type_spinner) as Spinner
            Specimen = view.findViewById<View>(R.id.Specimen) as Spinner
            TestMethod = view.findViewById<View>(R.id.Testmethod) as Spinner
            OrderToLocationId = view.findViewById<View>(R.id.OrderToLocation) as Spinner
            delete = view.findViewById<View>(R.id.deleteImageView) as ImageView
            mainLinearLayout = view.findViewById<View>(R.id.mainLinearLayout) as LinearLayout
            commentsImageView = view.findViewById(R.id.commentsImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(context)
            .inflate(R.layout.row_lab_tech, parent, false) as LinearLayout
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = responseContent!![position]
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.name.setText(response!!.labName)
        holder.name.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.name,
                        position
                    )
                }
            }
        })
        val adapter =
            ArrayAdapter<String>(
                this.context,
                R.layout.spinner_item,
                typeNamesList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.type.adapter = adapter

        if (responseContent!![position]!!.type != null) {
            val check = typeNamesList.any { it.key == responseContent!![position]!!.type }

            if (check) {

                holder.type.setSelection(hashMapType[responseContent!![position]!!.type]!!)
            } else {
                holder.type.setSelection(0)
            }

        }

        holder.type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()
                responseContent!![position]!!.type =
                    typeNamesList.filterValues { it == itemValue }.keys.toList()[0]

                Log.i(
                    "LabType",
                    "name = " + itemValue + "uuid=" + typeNamesList.filterValues { it == itemValue }.keys.toList()[0]
                )
            }

        }
        val adapter2 =
            ArrayAdapter<String>(
                this.context,
                R.layout.spinner_item,
                spectiumMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.Specimen.adapter = adapter2

        if (responseContent!![position]!!.SpecimenId != null) {
            val check2 = spectiumMap.any { it.key == responseContent!![position]!!.SpecimenId }

            if (check2) {

                holder.Specimen.setSelection(hashMapSpeciumMap[responseContent!![position]!!.SpecimenId]!!)
            } else {
                holder.Specimen.setSelection(0)
            }
        }

        holder.Specimen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()

                responseContent!![position]!!.SpecimenId =
                    spectiumMap.filterValues { it == itemValue }.keys.toList()[0]

            }
        }

        holder.delete.setOnClickListener {
            selectedResponseContent = responseContent!![position]!!
            if (!holder.name.text.trim().isEmpty()) {

                onDeleteClickListener!!.onDeleteClick(selectedResponseContent!!, position)


            }

        }

        if (position == responseContent!!.size - 1) {
            holder.delete.alpha = 0.2f
            holder.delete.isEnabled = false
            holder.commentsImageView.alpha = 0.2f
            holder.commentsImageView.isEnabled = false
            holder.commentsImageView.isFocusable = true
            holder.commentsImageView.isFocusableInTouchMode = true
            holder.name.isFocusable = true
            holder.name.isFocusableInTouchMode = true

            holder.name.requestFocus()
        } else {
            holder.delete.alpha = 1f
            holder.delete.isEnabled = true
            holder.commentsImageView.alpha = 1f
            holder.commentsImageView.isEnabled = true
            holder.commentsImageView.isFocusable = false
            holder.commentsImageView.isFocusableInTouchMode = false
            holder.name.isFocusable = false
            holder.name.isFocusableInTouchMode = false
        }
        val adapter3 =
            ArrayAdapter<String>(
                this.context,
                R.layout.spinner_item,
                testMethodMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.TestMethod.adapter = adapter3

        if (responseContent!![position]!!.TestMethodId != null) {
            val check3 =
                testMethodMap.any { it.key == responseContent!![position]!!.TestMethodId }

            if (check3) {

                holder.TestMethod.setSelection(hashMaptestMethodMap[responseContent!![position]!!.TestMethodId]!!)
            } else {
                holder.TestMethod.setSelection(0)
            }
        }

        holder.TestMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()
                if (responseContent!![position]!!.status) {
                    val testcode = listfilterTestMethoditem?.get(pos).code!!
                    if (testcode == "Rapid Di") {
                        if (responseContent!![position]!!.code == "COVID") {
                            responseContent!![position]!!.TestMethodId =
                                testMethodMap.filterValues { it == itemValue }.keys.toList()[0]
                        } else {
                            holder.TestMethod.setSelection(hashMaptestMethodMap[responseContent!![position]!!.TestMethodId]!!)
                            Toast.makeText(context, "RAPID Test only for COVID", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        responseContent!![position]!!.TestMethodId =
                            testMethodMap.filterValues { it == itemValue }.keys.toList()[0]
                    }
                } else {
                    responseContent!![position]!!.TestMethodId =
                        testMethodMap.filterValues { it == itemValue }.keys.toList()[0]
                }
            }

        }

        val adapter4 =
            ArrayAdapter<String>(
                this.context,
                R.layout.spinner_item,
                toLocationMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.OrderToLocationId.adapter = adapter4

        if (responseContent!![position]!!.OrderToLocationId != null) {
            val check4 =
                toLocationMap.any { it.key == responseContent!![position]!!.OrderToLocationId }

            if (check4) {

                holder.OrderToLocationId.setSelection(hashMapOrderToLocation[responseContent!![position]!!.OrderToLocationId]!!)

            } else {
                holder.OrderToLocationId.setSelection(0)
            }
        }
        holder.OrderToLocationId.onItemSelectedListener =
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
                    responseContent!![position]!!.OrderToLocationId =
                        toLocationMap.filterValues { it == itemValue }.keys.toList()[0]
                }
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
        holder.commentsImageView.setOnClickListener {
            onCommandClickListener!!.onCommandClick(
                position,
                responseContent!!.get(position)!!.commands
            )
        }
    }

    override fun getItemCount(): Int {
        return responseContent!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
    }

    fun addAll(responseContent: List<LabtechData?>?) {
        this.responseContent?.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.responseContent?.clear()
        this.responseContent?.add(LabtechData())
        notifyDataSetChanged()
    }

    fun add(r: LabtechData) {
        responseContent!!.add(r)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): LabtechData? {
        return responseContent!![position]
    }

    fun setadapterTypeValue(responseContents: List<GetReference?>?) {
        typeNamesList = responseContents?.map { it.uuid!! to it.name!! }!!.toMap().toMutableMap()
        for (i in responseContents.indices) {
            hashMapType[responseContents[i]!!.uuid] = i
        }
        notifyDataSetChanged()
    }

    fun setadapterLocationValue(responseContents: List<LocationMasterX?>?) {
        toLocationMap =
            responseContents?.map { it.uuid!! to it.location_name }!!.toMap().toMutableMap()
        for (i in responseContents.indices) {
            hashMapOrderToLocation[responseContents[i]!!.uuid] = i
        }
        notifyDataSetChanged()
    }

    fun setadapterSpectiumValue(responseContents: List<ResponseTestMethodContent?>?) {
        Log.i("", "" + responseContents)
        listfilterspecimanitem?.add(ResponseTestMethodContent())
        listfilterspecimanitem?.addAll((responseContents)!!)
        listfilterspecimanitem?.get(0).name = "Select Specimen"
        spectiumMap =
            listfilterspecimanitem!!.map { it.uuid!! to it.name!! }.toMap().toMutableMap()
        for (i in listfilterspecimanitem!!.indices) {
            hashMapSpeciumMap[listfilterspecimanitem!![i].uuid!!] = i
        }
        notifyDataSetChanged()
    }

    fun setadapterTestMethodValue(responseContents: List<ResponseTestMethodContent?>?) {
        listfilterTestMethoditem?.add(ResponseTestMethodContent())
        listfilterTestMethoditem?.addAll((responseContents)!!)
        listfilterTestMethoditem?.get(0).name = "Select TestMethod"
        testMethodMap =
            listfilterTestMethoditem!!.map { it.uuid!! to it.name!! }.toMap().toMutableMap()

        for (i in listfilterTestMethoditem!!.indices) {
            hashMaptestMethodMap[listfilterTestMethoditem!![i].uuid!!] = i
        }
        notifyDataSetChanged()
    }

    interface OnSearchInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int
        )
    }

    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }

    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<LabTestSpinnerresponseContent>,
        searchposition: Int
    ) {
        val responseContentAdapter = LmisLabNameSearch(
            this.context,
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()
        dropdownReferenceView.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as LabTestSpinnerresponseContent?
            val check = responseContent!!.any { it!!.id == selectedPoi.uuid }
            if (!check) {
                dropdownReferenceView.setText(selectedPoi.name)
                responseContent!![searchposition]!!.id = selectedPoi.uuid!!
                responseContent!![searchposition]!!.labId = selectedPoi.uuid
                responseContent!![searchposition]!!.to_department_id = selectedPoi.department_uuid!!
                responseContent!![searchposition]!!.labName = selectedPoi.name!!
                responseContent!![searchposition]!!.code = selectedPoi.code!!
                responseContent!![searchposition]!!.typeofmaster = selectedPoi.type!!

                if (selectedPoi.sample_type_uuid != null) {
                    responseContent!![searchposition]!!.SpecimenId = selectedPoi.sample_type_uuid
                }
                if (selectedPoi.type_of_method_uuid != null) {
                    responseContent!![searchposition]!!.TestMethodId =
                        selectedPoi.type_of_method_uuid
                }
                responseContent!![searchposition]!!.status = true


                onListItemClickListener!!.onListItemClick(
                    responseContent!![searchposition],
                    searchposition
                )

                notifyDataSetChanged()
                addRow(LabtechData())
            } else {
                notifyDataSetChanged()
                Toast.makeText(
                    this.context,
                    "Already Item available in the list",
                    Toast.LENGTH_LONG
                )?.show()
            }
        }
    }

    fun setError(dropdownReferenceView: AppCompatAutoCompleteTextView, position: Int) {
        dropdownReferenceView.error = "Test Name is Required"
    }

    private fun addRow(labtechData: LabtechData) {
        val check = responseContent!!.any { it!!.labId == labtechData.labId }
        if (!check) {
            responseContent!!.add(labtechData)
            notifyItemInserted(responseContent!!.size - 1)
        } else {
            notifyDataSetChanged()
            Toast.makeText(this.context, "Already Item available in the list", Toast.LENGTH_LONG)
                ?.show()
        }
    }

    fun getItems(): ArrayList<LabtechData?>? {
        return responseContent
    }

    fun getRemovedItems(): ArrayList<LabtechData?>? {
        return removedListFromOriginal
    }

    fun deleteRow(
        position1: Int
    ): Boolean {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        if (responseContent!![position1]?.mode == 1) {
            removedListFromOriginal?.add(responseContent!![position1])
        }
        val data = responseContent!![position1]
        this.responseContent!!.removeAt(position1)


        var ischeck: Boolean = true


        for (i in this.responseContent!!.indices) {
            if (responseContent!![i]!!.labId == data!!.labId) {
                ischeck = false
                break
            }
        }
        notifyItemRemoved(position1)
        notifyDataSetChanged()
        return ischeck
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            responseContent: LabtechData?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
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

    fun addFavouritesInRow(
        responseLabTechData: LabtechData?
    ) {
        val check = responseContent!!.any { it!!.labId == responseLabTechData?.labId }
        if (!check) {
            responseContent!!.removeAt(responseContent!!.size - 1)
            responseLabTechData!!.labName = responseLabTechData.labName
            responseLabTechData.labId = responseLabTechData.labId
            responseContent!!.add(responseLabTechData)
            responseContent!!.add(LabtechData())
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }

    }

    fun clearall() {
        responseContent!!.clear()
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
        if (responseContent!!.size > 0) {
            responseContent!!.removeAt(responseContent!!.size - 1)
        }

        for (i in responseContent!!.indices) {
            if (responseContent!!.get(i)?.labId?.equals(tempId!!)!! && responseContent!!.get(i)?.Labstatus?.equals(
                    position1
                )!!
            ) {
                this.responseContent!!.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()
        addRow(LabtechData())
    }


    fun getAll(): ArrayList<LabtechData?> {

        return this.responseContent!!

    }

    fun addCommands(position: Int, command: String) {
        responseContent!![position]!!.commands = command
        notifyDataSetChanged()
    }


    interface OnListItemClickListener {
        fun onListItemClick(
            responseContent: LabtechData?,
            position: Int
        )
    }

    fun setOnListItemClickListener(onListItemClickListener: OnListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener
    }

    fun setToLocation(responseContentsss: ToLocation?, searchposition: Int) {


        responseContent!![searchposition]!!.OrderToLocationId =
            responseContentsss?.to_location_uuid!!

        notifyDataSetChanged()

    }


}