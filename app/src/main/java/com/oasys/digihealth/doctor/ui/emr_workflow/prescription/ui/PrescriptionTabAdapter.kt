package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.*

class PrescriptionTabAdapter(
    private val context: Activity,
    private val PrescriptionArrayList: ArrayList<FavouritesModel?>,
    private val templeteArrayList: ArrayList<TempDetails?>
) : RecyclerView.Adapter<PrescriptionTabAdapter.MyViewHolder>() {

    private var vv: View? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null

    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onCommandClickListener: OnCommandClickListener? = null
    private var onitemChangeListener: OnitemChangeListener? = null


    //spinner Data

    private var RouteData: ArrayList<PrescriptionRouteResponseContent> = ArrayList()
    private var routeNamesMap = mutableMapOf<Int, String>()


    private var frequnecyDataList: ArrayList<PrescriptionFrequencyresponseContent> = ArrayList()
    private var frequencyMap = mutableMapOf<Int, String>()


    private var injectionStoreDataList: ArrayList<InjectionDepartment> = ArrayList()
    private var injectionStoreList = mutableMapOf<Int, String>()

    private var investigationDataList: ArrayList<PresInstructionResponseContent> = ArrayList()
    private var investigationList = mutableMapOf<Int, String>()


    //recyclerview

    private var durationArrayList: List<PrescriptionDurationResponseContent?>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_prescription, parent, false)

        vv = view
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return PrescriptionArrayList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var selectedResponseContent = PrescriptionArrayList[position]

        holder.serialNumberTextView.text = (position + 1).toString()

        holder.autoCompleteTextView.setText(PrescriptionArrayList[position]?.itemAppendString ?: "")

        holder.durationTextView.text = PrescriptionArrayList[position]?.duration ?: "1"

        holder.textViewQty.text = PrescriptionArrayList[position]!!.drug_quantity ?: "0"

        if (PrescriptionArrayList[position]?.duration != "") {

            holder.durationTextView.text = PrescriptionArrayList[position]?.duration ?: "1"

        } else {

            holder.durationTextView.text = "1"

            PrescriptionArrayList[position]!!.duration = 1.toString()

        }



        if (selectedResponseContent!!.Presstrength != null) {
            holder.strengthTextView.text = selectedResponseContent.Presstrength
        }

//search

        holder.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.autoCompleteTextView,
                        position
                    )
                }
            }
        })

//route
        val adapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                routeNamesMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.route_spinner.adapter = adapter

        for (i in RouteData.indices) {

            if (PrescriptionArrayList[position]?.selectRouteID == RouteData[i].uuid) {

                holder.route_spinner.setSelection(i + 1)

            }


        }


        holder.route_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()

                PrescriptionArrayList[position]!!.selectRouteID =
                    routeNamesMap.filterValues { it == itemValue }.keys.toList()[0]

            }

        }
//frequency

        val frequencyAdapter = ArrayAdapter<String>(
            context,
            R.layout.spinner_item,
            frequencyMap.values.toMutableList()
        )
        frequencyAdapter.setDropDownViewResource(R.layout.spinner_item)
        holder.frequency_spinner.adapter = frequencyAdapter

        for (i in frequnecyDataList.indices) {

            if (PrescriptionArrayList[position]?.selecteFrequencyID == frequnecyDataList[i].uuid) {

                holder.frequency_spinner.setSelection(i + 1)

                quantityCalculate(position, holder.textViewQty)

            }

        }

        holder.frequency_spinner.onItemSelectedListener =
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

                    PrescriptionArrayList[position]!!.selecteFrequencyID =
                        frequencyMap.filterValues { it == itemValue }.keys.toList()[0]

                    PrescriptionArrayList[position]!!.perdayquantityPrescription =
                        frequnecyDataList[pos + 1].perdayquantity.toString()

                    quantityCalculate(position, holder.textViewQty)

                }

            }

        //injection room

        if (PrescriptionArrayList[position]!!.drug_active!!) {
            val instructionAdapter =
                ArrayAdapter<String>(
                    context,
                    R.layout.spinner_item,
                    injectionStoreList.values.toMutableList()
                )
            instructionAdapter.setDropDownViewResource(R.layout.spinner_item)
            holder.instruction_spinner.adapter = instructionAdapter


            for (i in injectionStoreDataList.indices) {

                if (PrescriptionArrayList[position]?.selectInvestID == injectionStoreDataList[i].uuid) {

                    holder.instruction_spinner.setSelection(i + 1)

                }

            }


        } else {

            val instructionAdapter =
                ArrayAdapter<String>(
                    context,
                    R.layout.spinner_item,
                    investigationList.values.toMutableList()
                )
            instructionAdapter.setDropDownViewResource(R.layout.spinner_item)
            holder.instruction_spinner.adapter = instructionAdapter

            for (i in investigationDataList.indices) {

                if (PrescriptionArrayList[position]?.selectInvestID == investigationDataList[i].uuid) {

                    holder.instruction_spinner.setSelection(i + 1)

                }

            }


        }


        holder.instruction_spinner.onItemSelectedListener =
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

                    if (PrescriptionArrayList[position]!!.drug_active!!) {
                        PrescriptionArrayList[position]!!.selectInvestID =
                            injectionStoreList.filterValues { it == itemValue }.keys.toList()[0]
                    } else {

                        PrescriptionArrayList[position]!!.selectInvestID =
                            investigationList.filterValues { it == itemValue }.keys.toList()[0]

                    }


                }

            }

//duration


        holder.plusImageView.setOnClickListener {
            holder.minusImageView.alpha = 1f
            holder.minusImageView.isEnabled = true

            if (holder.durationTextView.text.isEmpty())
                holder.durationTextView.text = 1.toString()
            else
                holder.durationTextView.text =
                    (holder.durationTextView.text.toString().toInt() + 1).toString()

            PrescriptionArrayList[position]!!.duration = holder.durationTextView.text.toString()


            quantityCalculate(position, holder.textViewQty)

        }


        holder.minusImageView.setOnClickListener {

            if (holder.durationTextView.text != "0") {

                if (holder.durationTextView.text.isEmpty())
                    holder.durationTextView.text = 1.toString()
                else
                    holder.durationTextView.text =
                        (holder.durationTextView.text.toString().toInt() - 1).toString()


                PrescriptionArrayList[position]!!.duration = holder.durationTextView.text.toString()

                quantityCalculate(position, holder.textViewQty)
            }

        }

        val gridLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, true
        )
        holder.durationRecyclerView.layoutManager = gridLayoutManager

        val durationAdapter = PrescriptionDurationAdapter(context, durationArrayList)
        holder.durationRecyclerView.adapter = durationAdapter


        if (PrescriptionArrayList[position]!!.PrescriptiondurationPeriodId != 0) {

            durationAdapter.setData(PrescriptionArrayList[position]!!.PrescriptiondurationPeriodId)

            quantityCalculate(position, holder.textViewQty)

        }


        durationAdapter.setOnItemClickListener(object :
            PrescriptionDurationAdapter.OnItemClickListener {

            override fun onItemClick(durationID: Int, poss: Int) {

                PrescriptionArrayList[position]!!.PrescriptiondurationPeriodId = durationID

                PrescriptionArrayList[position]!!.drug_code = durationArrayList?.get(poss)?.code

                durationAdapter.updateSelectStatus((durationID))

                quantityCalculate(position, holder.textViewQty)


            }
        })




        holder.commentsImageView.setOnClickListener {

            onCommandClickListener!!.onCommandClick(
                position,
                PrescriptionArrayList[position]!!.commands
            )


        }

        holder.deleteImageView.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(PrescriptionArrayList[position], position)
        }


        holder.infoImageView.setOnClickListener {
            val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            val dialog = PrescriptionDrugInfoDialogFragment()
            bundle.putString("drugName", PrescriptionArrayList[position]!!.drug_name.toString())
            bundle.putString("drugCode", PrescriptionArrayList[position]!!.drug_code.toString())
            bundle.putString("itemMasterUUID", PrescriptionArrayList[position]!!.drug_id.toString())
            bundle.putString(
                "storeMasterUUID",
                PrescriptionArrayList[position]!!.store_master_uuid.toString()
            )
            dialog.arguments = bundle

            dialog.show(ft, "Tag")


        }



        if (position == PrescriptionArrayList.size - 1) {
            holder.deleteImageView.alpha = 0.2f
            holder.deleteImageView.isEnabled = false
            holder.commentsImageView.alpha = 0.2f
            holder.commentsImageView.isEnabled = false
            holder.infoImageView.alpha = 0.2f
            holder.infoImageView.isEnabled = false
        } else {
            holder.deleteImageView.alpha = 1f
            holder.commentsImageView.alpha = 1f
            holder.infoImageView.alpha = 1f
            holder.deleteImageView.isEnabled = true
            holder.commentsImageView.isEnabled = true
            holder.infoImageView.isEnabled = true
        }

        holder.autoCompleteTextView.isEnabled =
            PrescriptionArrayList[position]!!.test_master_id == 0


    }

    class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.PresSerialNumberTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val commentsImageView: ImageView = itemView.findViewById(R.id.commentsImageView)
        internal val autoCompleteTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.PresAutoCompleteTextView)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
        internal val route_spinner: Spinner = itemView.findViewById(R.id.route_spinner)
        internal val frequency_spinner: Spinner = itemView.findViewById(R.id.frequency_spinner)
        internal val instruction_spinner: Spinner = itemView.findViewById(R.id.instruction_spinner)
        internal val minusImageView: ImageView = itemView.findViewById(R.id.minusImageView)
        internal val plusImageView: ImageView = itemView.findViewById(R.id.plusImageView)
        internal val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        internal val durationRecyclerView: RecyclerView =
            itemView.findViewById(R.id.durationRecyclerView)
        internal val infoImageView: ImageView = itemView.findViewById(R.id.infoImageView)
        internal val strengthTextView: TextView = itemView.findViewById(R.id.textViewStrength)
        internal val textViewQty: TextView = itemView.findViewById(R.id.textViewQty)


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


    interface OnCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String
        )
    }

    fun setOnCommandClickListener(onCommandClickListener: OnCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
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


    fun addRow(favouritesModel: FavouritesModel) {

        val check =
            PrescriptionArrayList.any { it!!.test_master_id == favouritesModel.test_master_id }
        if (!check) {
            PrescriptionArrayList.add(favouritesModel)
            getIdList()
            notifyDataSetChanged()

        }

    }

    fun setPrescriptionAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: List<PrescriptionSearchResponseContent?>?,
        selectedSearchPosition: Int
    ) {

        val responseContentAdapter = PrescriptionSearchResultAdapter(
            context,
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->


            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(vv!!.windowToken, 0)
            val selectedPoi = parent.adapter.getItem(position) as PrescriptionSearchResponseContent?

            val st = FavouritesModel()

            val check = PrescriptionArrayList.any { it!!.test_master_id == selectedPoi?.uuid }
            if (!check) {
                val selectedSearchPrescriptionBean = PrescriptionArrayList[selectedSearchPosition]
                PrescriptionArrayList[selectedSearchPosition]?.itemAppendString = selectedPoi?.name
                PrescriptionArrayList[selectedSearchPosition]?.test_master_id = selectedPoi?.uuid
                PrescriptionArrayList[selectedSearchPosition]?.store_master_uuid =
                    selectedPoi?.stock_item?.store_master_uuid
                PrescriptionArrayList[selectedSearchPosition]?.drug_id =
                    selectedPoi?.stock_item?.item_master_uuid
                PrescriptionArrayList[selectedSearchPosition]?.drug_code =
                    selectedPoi?.stock_item?.store_master?.store_code ?: ""
                PrescriptionArrayList[selectedSearchPosition]?.drug_name =
                    selectedPoi?.stock_item?.store_master?.store_name ?: ""
                PrescriptionArrayList[selectedSearchPosition]?.Presstrength = selectedPoi?.strength
                PrescriptionArrayList[selectedSearchPosition]?.drug_period_code =
                    selectedPoi?.code.toString()

                PrescriptionArrayList[selectedSearchPosition]?.drug_active = selectedPoi?.is_emar

                notifyDataSetChanged()

                addRow(FavouritesModel())

            } else {
                notifyDataSetChanged()

                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                    ?.show()

            }
        }
    }

    fun setRouteValue(responseContents: List<PrescriptionRouteResponseContent>?) {

        RouteData = responseContents as ArrayList<PrescriptionRouteResponseContent>

        routeNamesMap.clear()

        routeNamesMap[0] = "Select Route"

        routeNamesMap.putAll(responseContents.map { it.uuid to it.name }.toMap().toMutableMap())

        notifyDataSetChanged()

    }

    fun setFrequencyList(responseContents: List<PrescriptionFrequencyresponseContent?>?) {


        frequnecyDataList = responseContents as ArrayList<PrescriptionFrequencyresponseContent>

        frequencyMap.clear()
        frequencyMap.put(0, "Select Frequency")

        frequencyMap.putAll(responseContents.map { it.uuid!! to it.name }.toMap().toMutableMap())


        notifyDataSetChanged()

    }

    fun setInjectionList(responseContents: List<InjectionDepartment>?) {


        injectionStoreDataList = responseContents as ArrayList<InjectionDepartment>

        injectionStoreList.clear()
        injectionStoreList.put(0, "Select Insj")

        injectionStoreList.putAll(
            responseContents.map { it.uuid to it.store_name }.toMap().toMutableMap()
        )


        notifyDataSetChanged()


    }

    fun setInstructionList(responseContents: List<PresInstructionResponseContent>?) {


        investigationDataList = responseContents as ArrayList<PresInstructionResponseContent>

        investigationList.clear()
        investigationList.put(0, "Select Inst")

        investigationList.putAll(responseContents.map { it.uuid to it.name }.toMap().toMutableMap())


        notifyDataSetChanged()


    }

    fun setDuration(responseContents: List<PrescriptionDurationResponseContent>) {

        this.durationArrayList = responseContents
        notifyDataSetChanged()

    }


    fun quantityCalculate(position: Int, textViewQty: TextView) {


        if (PrescriptionArrayList[position]!!.perdayquantityPrescription != "" && PrescriptionArrayList[position]!!.duration != "") {

            when (PrescriptionArrayList[position]!!.drug_code) {

                "Days" -> {
                    PrescriptionArrayList[position]!!.drug_quantity =
                        (PrescriptionArrayList[position]!!.perdayquantityPrescription.toDouble() * PrescriptionArrayList[position]!!.duration!!.toInt()).toString()

                    textViewQty.text = PrescriptionArrayList[position]!!.drug_quantity
                }
                "Weeks" -> {


                    PrescriptionArrayList[position]!!.drug_quantity =
                        (PrescriptionArrayList[position]!!.perdayquantityPrescription.toDouble() * PrescriptionArrayList[position]!!.duration!!.toInt() * 7).toString()

                    textViewQty.text = PrescriptionArrayList[position]!!.drug_quantity
                }
                "Months" -> {

                    PrescriptionArrayList[position]!!.drug_quantity =
                        (PrescriptionArrayList[position]!!.perdayquantityPrescription.toDouble() * PrescriptionArrayList[position]!!.duration!!.toInt() * 30).toString()

                    textViewQty.text = PrescriptionArrayList[position]!!.drug_quantity
                }
                "Years" -> {

                    PrescriptionArrayList[position]!!.drug_quantity =
                        (PrescriptionArrayList[position]!!.perdayquantityPrescription.toDouble() * PrescriptionArrayList[position]!!.duration!!.toInt() * 365).toString()

                    textViewQty.text = PrescriptionArrayList[position]!!.drug_quantity
                }
                else -> {

                    PrescriptionArrayList[position]!!.drug_quantity =
                        (PrescriptionArrayList[position]!!.perdayquantityPrescription.toDouble() * PrescriptionArrayList[position]!!.duration!!.toInt()).toString()

                    textViewQty.text = PrescriptionArrayList[position]!!.drug_quantity
                }
            }
        }

    }


    fun addCommands(position: Int, command: String) {

        PrescriptionArrayList[position]!!.commands = command

        notifyDataSetChanged()

    }


    fun deleteRow(
        position1: Int
    ) {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)

        this.PrescriptionArrayList.removeAt(position1)



        notifyItemRemoved(position1)
        notifyDataSetChanged()

        getIdList()

    }

    fun getIdList() {

        var sendListId: ArrayList<Int> = ArrayList()

        for (i in PrescriptionArrayList.indices) {

            sendListId.add(PrescriptionArrayList[i]!!.test_master_id!!)
        }

        onitemChangeListener?.onitemChangeClick(sendListId)

    }


    interface OnitemChangeListener {
        fun onitemChangeClick(
            uuid: ArrayList<Int>
        )
    }

    fun setOnitemChangeListener(onCommandClickListener: OnitemChangeListener) {
        this.onitemChangeListener = onCommandClickListener
    }


    fun deleteItem(
        uuid: Int
    ) {

        for (i in PrescriptionArrayList.indices) {

            if (PrescriptionArrayList[i]!!.test_master_id == uuid) {

                this.PrescriptionArrayList.removeAt(i)

                break
            }

        }

        getIdList()

        notifyDataSetChanged()
    }

    fun removeLastPoistion() {

        PrescriptionArrayList.removeAt(PrescriptionArrayList.size - 1)

        notifyDataSetChanged()
    }

    fun clearall() {


        PrescriptionArrayList.clear()

        notifyDataSetChanged()

    }

    fun getItems(): ArrayList<FavouritesModel?> {

        return PrescriptionArrayList

    }


}