package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.InfoDialogFragment
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.PrescriptionDurationAdapter
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.PrescriptionSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentPrescRouteSpinnerresponseContent
import com.hmis_tn.doctor.utils.Utils

class TreatmentKitPrescriptionAdapter(
    private val context: Activity,
    private var favouritesArrayList: ArrayList<FavouritesModel?>
) : RecyclerView.Adapter<TreatmentKitPrescriptionAdapter.MyViewHolder>(), ITreatmentKitAdapter {
    private var routeNamesMap = mutableMapOf<Int, String>()
    private var frequencyMap = mutableMapOf<Int, String>()

    private var frequnecyDataList: List<PrescriptionFrequencyresponseContent?>? = null

    private var investigationList = mutableMapOf<Int, String>()
    private var injectionStoreList = mutableMapOf<Int, String>()
    var hashmapDuration: HashMap<Int, Int> = HashMap()
    private val hashMapFrequency: HashMap<Int, Int> = HashMap()
    private val hashMapRoute: HashMap<Int, Int> = HashMap()
    private val hashMapInstruction: HashMap<Int, Int> = HashMap()

    private val hashFrequencySpinnerList: HashMap<Int, Int> = HashMap()
    private val hashRouteSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashInstructionSpinnerList: HashMap<Int, Int> = HashMap()

    private val hashInjectionStoreSpinnerList: HashMap<Int, Int> = HashMap()

    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null

    private var onCommandClickListener: OnPrescriptionCommandClickListener? = null

    private var durationArrayList: List<PrescriptionDurationResponseContent?>? = ArrayList()
    lateinit var selectedResponseContent: FavouritesModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_treatment_kit_prescription, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        selectedResponseContent = favouritesArrayList[position]!!
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.autoCompleteTextView.setText(selectedResponseContent.itemAppendString, false)
        holder.durationTextView.text = selectedResponseContent.duration

        holder.textViewQty.text = favouritesArrayList.get(position)?.drug_quantity
        //holder.textViewQty.text = "90"

        holder.strengthTextView.text = favouritesArrayList.get(position)?.Presstrength
        //holder.strengthTextView.text = "100 mg"

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

        holder.infoImageView.setOnClickListener {
            selectedResponseContent = favouritesArrayList[position]!!
            val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            val dialog = InfoDialogFragment()
            bundle.putString("DrugID", selectedResponseContent.test_master_id.toString())
            dialog.arguments = bundle
            dialog.show(ft, "Tag")
        }

        holder.minusImageView.setOnClickListener {
            holder.durationTextView.text =
                (holder.durationTextView.text.toString().toInt() - 1).toString()
            selectedResponseContent.duration = holder.durationTextView.text.toString()
            var prescribed_quantity_Data: Int = 0
            val period = selectedResponseContent.PrescriptiondurationPeriodId
            var prescribed_quantity = selectedResponseContent.perdayquantityPrescription
            if (prescribed_quantity.contains("-")) {
                prescribed_quantity = Utils.stringToInt(prescribed_quantity).toString()
                val periodCode = selectedResponseContent.drug_code
                when (periodCode) {
                    "Days" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                    }
                    "Weeks" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 7
                    }
                    "Months" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 30
                    }
                    "Years" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 365
                    }
                    else -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                    }
                }
            }

            selectedResponseContent.drug_quantity = prescribed_quantity_Data.toString()
            holder.textViewQty.text = prescribed_quantity_Data.toString()

            if (holder.durationTextView.text.toString().toInt() == 0) {
                holder.minusImageView.alpha = 0.5f
                holder.minusImageView.isEnabled = false
            }
        }

        holder.plusImageView.setOnClickListener {
            holder.minusImageView.alpha = 1f
            holder.minusImageView.isEnabled = true
            holder.durationTextView.text =
                (holder.durationTextView.text.toString().toInt() + 1).toString()
            selectedResponseContent.duration = holder.durationTextView.text.toString()
            var prescribed_quantity_Data: Int = 0
            val period = selectedResponseContent.PrescriptiondurationPeriodId
            var prescribed_quantity = selectedResponseContent.perdayquantityPrescription
            if (prescribed_quantity.contains("-")) {
                prescribed_quantity = Utils.stringToInt(prescribed_quantity).toString()
                val periodCode = selectedResponseContent.drug_code
                when (periodCode) {
                    "Days" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                    }
                    "Weeks" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 7
                    }
                    "Months" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 30
                    }
                    "Years" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 365
                    }
                    else -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                    }
                }
            }

            selectedResponseContent.drug_quantity = prescribed_quantity_Data.toString()
            holder.textViewQty.text = prescribed_quantity_Data.toString()
        }

        val gridLayoutManager = GridLayoutManager(
            context,
            1,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.durationRecyclerView.layoutManager = gridLayoutManager
        if (selectedResponseContent.PrescriptiondurationPeriodId == 0) {
            if (durationArrayList?.isNotEmpty()!!) {
                selectedResponseContent.PrescriptiondurationPeriodId =
                    durationArrayList?.get(0)?.uuid!!
            }
        }
        val durationAdapter = PrescriptionDurationAdapter(context, durationArrayList)
        holder.durationRecyclerView.adapter = durationAdapter
        try {
            if (hashmapDuration.containsKey(selectedResponseContent.drug_id!!)) {
                durationAdapter.setFocus(hashmapDuration.get(selectedResponseContent.drug_id!!))
            }
        } catch (e: Exception) {

        }
        if (hashmapDuration.get(selectedResponseContent.drug_id!!) != null) {
            val pos = hashmapDuration.get(selectedResponseContent.drug_id!!)
            durationAdapter.updateSelectStatus(pos!!)
        } else {
            hashmapDuration.put(selectedResponseContent.drug_id!!, 1)
        }
        durationAdapter.setOnItemClickListener(object :
            PrescriptionDurationAdapter.OnItemClickListener {

            override fun onItemClick(durationID: Int, poss: Int) {

                hashmapDuration.put(selectedResponseContent.drug_id!!, durationID)

                favouritesArrayList[position]!!.PrescriptiondurationPeriodId = durationID

                durationAdapter.updateSelectStatus((durationID))


                var prescribed_quantity_Data: Int = 0

                val period = durationID


                var prescribed_quantity = selectedResponseContent.perdayquantityPrescription

                if (prescribed_quantity.contains("-")) {


                    prescribed_quantity = Utils.stringToInt(prescribed_quantity).toString()


                    var periodCode = durationArrayList?.get(poss)?.code


                    when (periodCode) {
                        "Days" -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                        }
                        "Weeks" -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 7
                        }
                        "Months" -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 30
                        }
                        "Years" -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 365
                        }
                        else -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                        }
                    }

                }

                selectedResponseContent.drug_quantity = prescribed_quantity_Data.toString()
                holder.textViewQty.text = prescribed_quantity_Data.toString()


            }
        })
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(selectedResponseContent, position)
        }


        val adapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                routeNamesMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.route_spinner.adapter = adapter
        try {
            if (hashMapRoute.containsKey(selectedResponseContent.drug_route_id)) {
                holder.route_spinner.setSelection(hashMapRoute.get(selectedResponseContent.drug_route_id)!!)
            }
        } catch (e: Exception) {

        }

        try {
            holder.route_spinner.setSelection(selectedResponseContent.drug_route_id!!)
        } catch (e: Exception) {
            e.printStackTrace()
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
                selectedResponseContent = favouritesArrayList[position]!!
                favouritesArrayList[position]!!.selectRouteID =
                    routeNamesMap.filterValues { it == itemValue }.keys.toList()[0]
                hashMapRoute.put(selectedResponseContent.test_master_id!!, pos)
            }
        }

        val frequencyAdapter = ArrayAdapter<String>(
            context,
            R.layout.spinner_item,
            frequencyMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.frequency_spinner.adapter = frequencyAdapter
        try {
            if (hashMapFrequency.containsKey(selectedResponseContent.test_master_id)) {
                holder.frequency_spinner.setSelection(hashMapFrequency.get(selectedResponseContent.test_master_id)!!)
            }
        } catch (e: Exception) {
            Log.i("SpinnerMapErr", e.toString())
        }

        if (selectedResponseContent.drug_frequency_id != null && selectedResponseContent.drug_frequency_id != "") {
            val check =
                hashFrequencySpinnerList.any { it.key == selectedResponseContent.drug_frequency_id!!.toInt() }

            if (check) {
                holder.frequency_spinner.setSelection(
                    hashFrequencySpinnerList.get(
                        selectedResponseContent.drug_frequency_id!!.toInt()
                    )!!
                )
            }
        }

        if (selectedResponseContent.drug_route_id != 0 && selectedResponseContent.drug_route_id != null) {
            val check =
                hashRouteSpinnerList.any { it.key == selectedResponseContent.drug_route_id!!.toInt() }
            if (check) {
                holder.route_spinner.setSelection(hashRouteSpinnerList.get(selectedResponseContent.drug_route_id!!.toInt())!!)
            }
        }

        if (selectedResponseContent.drug_instruction_id!! != 0 && selectedResponseContent.drug_instruction_id != null) {
            holder.instruction_spinner.setSelection(
                hashInstructionSpinnerList.get(
                    selectedResponseContent.drug_instruction_id!!.toInt()
                )!!
            )
        }

        if (selectedResponseContent.drug_duration != 0 && selectedResponseContent.drug_duration != null) {
            holder.durationTextView.text = selectedResponseContent.drug_duration!!.toString()
        }

        if (selectedResponseContent.durationPeriodId != 0 && selectedResponseContent.durationPeriodId != null) {

            if (selectedResponseContent.durationPeriodId.toString().contains(".")) {
                selectedResponseContent.durationPeriodId =
                    selectedResponseContent.durationPeriodId.toString().substring(
                        0,
                        selectedResponseContent.durationPeriodId.toString().indexOf('.')
                    ).toInt()
            }
            durationAdapter.selectDurationPosition(selectedResponseContent.durationPeriodId!!)
        }

        if (selectedResponseContent.perdayquantityPrescription.isNotEmpty()) {
            var prescribed_quantity_Data: Int = 0
            val period = selectedResponseContent.PrescriptiondurationPeriodId
            var prescribed_quantity = selectedResponseContent.perdayquantityPrescription
            if (prescribed_quantity.contains("-")) {
                prescribed_quantity = Utils.stringToInt(prescribed_quantity).toString()
                val periodCode = selectedResponseContent.drug_code
                when (periodCode) {
                    "Days" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                    }
                    "Weeks" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 7
                    }
                    "Months" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 30
                    }
                    "Years" -> {
                        prescribed_quantity_Data =
                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 365
                    }
                    else -> {
//                        prescribed_quantity_Data =
//                            prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                    }
                }
            }
            selectedResponseContent.drug_quantity = prescribed_quantity_Data.toString()
            holder.textViewQty.text = prescribed_quantity_Data.toString()
        }

        if (selectedResponseContent.Presstrength != null) {
            holder.strengthTextView.text = selectedResponseContent.Presstrength
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
                    Log.i("", "" + itemValue)
                    Log.i("", "" + itemValue)
                    Log.i("", "" + itemValue)
                    selectedResponseContent = favouritesArrayList[position]!!
                    favouritesArrayList[position]!!.perdayquantityPrescription =
                        frequnecyDataList?.get(pos)!!.name.toString()
                    favouritesArrayList[position]!!.selecteFrequencyID =
                        frequencyMap.filterValues { it == itemValue }.keys.toList()[0]
                    hashMapFrequency.put(selectedResponseContent.test_master_id!!, pos)

                    var prescribed_quantity = selectedResponseContent.perdayquantityPrescription

                    var prescribed_quantity_Data = 0

                    if (prescribed_quantity.contains("-")) {


                        prescribed_quantity = Utils.stringToInt(prescribed_quantity).toString()


                        val periodCode = selectedResponseContent.drug_code

                        when (periodCode) {
                            "Days" -> {
                                prescribed_quantity_Data =
                                    prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                            }
                            "Weeks" -> {
                                prescribed_quantity_Data =
                                    prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 7
                            }
                            "Months" -> {
                                prescribed_quantity_Data =
                                    prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 30
                            }
                            "Years" -> {
                                prescribed_quantity_Data =
                                    prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt() * 365
                            }
                            else -> {

//                                prescribed_quantity_Data =
//                                    prescribed_quantity.toInt() * selectedResponseContent.duration!!.toInt()
                            }
                        }


                    }

                    selectedResponseContent.drug_quantity = prescribed_quantity_Data.toString()

                    holder.textViewQty.text = prescribed_quantity_Data.toString()
                }
            }

        /*val instructionAdapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                investigationList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.instruction_spinner.adapter = instructionAdapter
        try {
            if (hashMapInstruction.containsKey(selectedResponseContent.test_master_id)) {
                holder.instruction_spinner.setSelection(hashMapInstruction.get(
                    selectedResponseContent.test_master_id
                )!!);
            }
        }catch (e:Exception){
            Log.i("SpinnerMapErr",e.toString())
        }*/


        if (favouritesArrayList[position]!!.drug_active!!) {
            val instructionAdapter =
                ArrayAdapter<String>(
                    context,
                    R.layout.spinner_item,
                    injectionStoreList.values.toMutableList()
                )
            adapter.setDropDownViewResource(R.layout.spinner_item)
            holder.instruction_spinner.adapter = instructionAdapter

            try {
                if (hashInjectionStoreSpinnerList.containsKey(selectedResponseContent.test_master_id)) {
                    holder.instruction_spinner.setSelection(
                        hashInjectionStoreSpinnerList.get(
                            selectedResponseContent.test_master_id
                        )!!
                    )
                }
            } catch (e: Exception) {
                Log.i("SpinnerMapErr", e.toString())
            }

        } else {

            val instructionAdapter =
                ArrayAdapter<String>(
                    context,
                    R.layout.spinner_item,
                    investigationList.values.toMutableList()
                )
            adapter.setDropDownViewResource(R.layout.spinner_item)
            holder.instruction_spinner.adapter = instructionAdapter


            try {
                if (hashMapInstruction.containsKey(selectedResponseContent.test_master_id)) {
                    holder.instruction_spinner.setSelection(
                        hashMapInstruction.get(
                            selectedResponseContent.test_master_id
                        )!!
                    )
                }
            } catch (e: Exception) {
                Log.i("SpinnerMapErr", e.toString())
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
                    selectedResponseContent = favouritesArrayList[position]!!
                    if (favouritesArrayList[position]!!.drug_active!!) {
                        favouritesArrayList[position]!!.selectInvestID =
                            injectionStoreList.filterValues { it == itemValue }.keys.toList()[0]
                    } else {

                        favouritesArrayList[position]!!.selectInvestID =
                            investigationList.filterValues { it == itemValue }.keys.toList()[0]

                    }
                    //hashMapInstruction.put(selectedResponseContent.test_master_id!!,pos)
                }
            }

        holder.commentsImageView.setOnClickListener {
            onCommandClickListener!!.onCommandClick(
                position,
                favouritesArrayList.get(position)!!.commands
            )
        }

        holder.deleteImageView.setOnClickListener {
            selectedResponseContent.drug_code = favouritesArrayList[position]?.drug_code
            selectedResponseContent = favouritesArrayList[position]!!
            if (!holder.autoCompleteTextView.text.trim().isEmpty()) {
                onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
                hashMapFrequency.remove(selectedResponseContent.test_master_id)
                hashmapDuration.remove(selectedResponseContent.drug_id!!)
                hashMapFrequency.remove(selectedResponseContent.test_master_id)
                hashMapInstruction.remove(selectedResponseContent.test_master_id)
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


    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.PresSerialNumberTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val commentsImageView: ImageView = itemView.findViewById(R.id.commentsImageView)
        internal val autoCompleteTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.PresAutoCompleteTextView)
        internal val mainLinearLayout: LinearLayoutCompat =
            itemView.findViewById(R.id.mainLinearLayout)
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

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: FavouritesModel?,
            position: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnSearchInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int
        )
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


    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }

    fun clearall() {
        favouritesArrayList.clear()
        notifyDataSetChanged()

    }

    fun deleteRow(

        position1: Int
    ): Boolean {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)

        val data = favouritesArrayList[position1]

        this.favouritesArrayList.removeAt(position1)

        var ischeck: Boolean = true


        for (i in this.favouritesArrayList.indices) {

            if (favouritesArrayList[i]!!.template_id == data!!.template_id) {

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
    fun deleteRowFromFavourites(
        drugId: Int
    ) {

        if (favouritesArrayList.size > 0) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
        }

        for (i in favouritesArrayList.indices) {
            if (favouritesArrayList.get(i)?.favourite_id?.equals(drugId)!!) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()
        addRow(FavouritesModel())
    }


    fun addFavouritesInRow(responseContent: FavouritesModel?) {

        val check = favouritesArrayList.any { it!!.drug_id == responseContent?.drug_id }

        if (!check) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
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
        notifyDataSetChanged()
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
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as PrescriptionSearchResponseContent?
            val check = favouritesArrayList.any { it!!.drug_id == selectedPoi?.uuid }

            if (!check) {
                dropdownReferenceView.setText(selectedPoi?.name)
                val st = FavouritesModel()
                val selectedSearchPrescriptionBean = favouritesArrayList[selectedSearchPosition]
                selectedSearchPrescriptionBean?.drug_name = selectedPoi?.name
                selectedSearchPrescriptionBean?.itemAppendString = selectedPoi?.name
                selectedSearchPrescriptionBean?.drug_id = selectedPoi?.uuid
                selectedSearchPrescriptionBean?.Presstrength = selectedPoi?.strength
                selectedSearchPrescriptionBean?.drug_period_code = selectedPoi?.code.toString()

                selectedSearchPrescriptionBean?.drug_active = selectedPoi?.is_emar

                if (!favouritesArrayList.contains(st)) {
                    addRow(FavouritesModel())
                }
                notifyDataSetChanged()
            } else {
                notifyDataSetChanged()

                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()
            }
        }
    }

    fun setadapterTypeValue(responseContents: List<TreatmentPrescRouteSpinnerresponseContent?>?) {

        routeNamesMap = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()

        hashRouteSpinnerList.clear()

        for (i in responseContents.indices) {
            hashRouteSpinnerList[responseContents[i]!!.uuid!!] = i
        }
        notifyDataSetChanged()

    }

    fun setFrequencyList(responseContents: List<PrescriptionFrequencyresponseContent?>?) {
        frequnecyDataList = responseContents
        frequencyMap = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()
        hashFrequencySpinnerList.clear()
        for (i in responseContents.indices) {

            hashFrequencySpinnerList[responseContents[i]!!.uuid!!] = i
        }

        notifyDataSetChanged()

    }

    fun setInstructionList(responseContents: List<PresInstructionResponseContent?>?) {
        investigationList = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()

        hashInstructionSpinnerList.clear()
        for (i in responseContents.indices) {
            hashInstructionSpinnerList[responseContents[i]!!.uuid] = i
        }
        notifyDataSetChanged()

    }

    fun setInjectionList(responseContents: List<InjectionDepartment?>?) {

        injectionStoreList =
            responseContents?.map { it?.uuid!! to it.store_name }!!.toMap().toMutableMap()

        hashInjectionStoreSpinnerList.clear()
        for (i in responseContents.indices) {
            hashInjectionStoreSpinnerList[responseContents[i]!!.uuid] = i
        }
        notifyDataSetChanged()

    }

    fun setDuration(durationArrayList_: List<PrescriptionDurationResponseContent?>) {
        this.durationArrayList = durationArrayList_
        notifyDataSetChanged()
    }


    fun getItems(): ArrayList<FavouritesModel?> {

        return favouritesArrayList

    }

    fun addCommands(position: Int, command: String) {

        favouritesArrayList[position]!!.commands = command

        notifyDataSetChanged()

    }


    interface OnPrescriptionCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String
        )
    }

    fun setOnPrescriptionCommandClickListener(onCommandClickListener: OnPrescriptionCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }


}
