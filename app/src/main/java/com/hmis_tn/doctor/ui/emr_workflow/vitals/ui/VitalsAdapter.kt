package com.hmis_tn.doctor.ui.emr_workflow.vitals.ui

import android.app.Activity
import android.app.Dialog
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.TemplateDetail
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.response.GetVital
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.responseuommodule.ResponseContentsUOM


class VitalsAdapter(
    private val context: Activity,
    private var vitalsList: ArrayList<TemplateDetail>


) : RecyclerView.Adapter<VitalsAdapter.VitalHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    private var typeList = mutableMapOf<Int?, String?>()
    private val hashMapType: HashMap<Int, Int> = HashMap()
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var parentID = 0
    //var hashMapVitalsList : HashMap<Int,Int> = HashMap()

    private val hashtypeSpinnerList: HashMap<Int?, Int?> = HashMap()
    private var onSearchClickListener: OnSearchClickListener? = null
    private var textChanged = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VitalHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.row_vitals, parent, false)
        return VitalHolder(view)

    }

    override fun getItemCount(): Int {
        return vitalsList.size

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: VitalHolder, position: Int) {
        val vitalsTemplateMasterDetail = vitalsList[position]
        holder.nameTextView.setText(vitalsTemplateMasterDetail.name)

        val pos = position + 1
        holder.serialNumberTextView.text = pos.toString()
        holder.deleteImageView.setOnClickListener {
            //onDeleteClickListener?.onDeleteClick(vitalsTemplateMasterDetail, position)

            deleteItem(position, vitalsTemplateMasterDetail.name)
        }

        holder.spinner_type.setSelection(vitalsTemplateMasterDetail.uom_master_uuid)

        holder.vitualEditText.setText(vitalsTemplateMasterDetail.vital_value)
        if (position == vitalsList.size - 1) {
            holder.deleteImageView.visibility = View.INVISIBLE
            holder.spinner_type.visibility = View.INVISIBLE
            holder.nameTextView.isFocusable = true
            holder.nameTextView.isFocusableInTouchMode = true
            holder.nameTextView.requestFocus()


        } else {
            holder.deleteImageView.visibility = View.VISIBLE
            holder.spinner_type.visibility = View.VISIBLE
            holder.nameTextView.isFocusable = false
            holder.nameTextView.isFocusableInTouchMode = false
        }

        holder.vitualEditText.inputType =
            if (vitalsTemplateMasterDetail.vital_value_type_uuid == 12) {
                InputType.TYPE_CLASS_NUMBER
            } else {
                InputType.TYPE_CLASS_TEXT
            }

        //Fill EditText with the value you have in data source
        holder.vitualEditText.setText(vitalsList.get(position).vital_value)
        holder.vitualEditText.id = position

        //we need to update adapter once we finish with editing
        var bmiWeight: Int = 0
        var bmiHeight: Int = 0
        holder.vitualEditText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    if (s.isNotEmpty()) {
                        vitalsList[position].vital_value = s.toString()
                        if (s.length == 1) {
                            if (s.toString() == " ") {
                                holder.vitualEditText.setText("")
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (vitalsList.size > 3) {
                    var bmiCalcFinal: Double = 0.0

                    vitalsList.forEach {
                        when (it.name) {
                            "HEIGHT", "Height", "height" -> bmiHeight =
                                if (it.vital_value.isNotEmpty()) it.vital_value.toInt() else 0
                            "WEIGHT", "Weight", "weight" -> bmiWeight =
                                if (it.vital_value.isNotEmpty()) it.vital_value.toInt() else 0
                            else -> Unit
                        }
                    }

                    if (bmiHeight > 0 && bmiWeight > 0) {
                        bmiCalcFinal =
                            bmiWeight.toDouble() / bmiHeight.toDouble() / bmiHeight.toDouble() * 10000

                        Log.e("value", "bmiCalc______" + bmiCalcFinal)
                    }

                    /*if (vitalsList[position].name == "BMI") {
                        if (bmiHeight>0 && bmiWeight>0){
                            bmiCalcFinal = bmiWeight.toDouble() / bmiHeight.toDouble() / bmiHeight.toDouble() * 10000

                            Log.e("value", "bmiCalc______" + bmiCalcFinal)
                        }
                        *//*if (vitalsList[0].name == "HEIGHT" && !vitalsList[0].vital_value.isNullOrEmpty() &&
                            vitalsList[1].name == "WEIGHT" && !vitalsList[1].vital_value.isNullOrEmpty()
                        ) {
                            val bmiWeight = (vitalsList[1].vital_value).toDouble()
                            val bmiHeight = (vitalsList[0].vital_value).toDouble()
                            bmiCalcFinal = bmiWeight / bmiHeight / bmiHeight * 10000

                            Log.e("value", "bmiCalc______" + bmiCalcFinal)
                        }*//*
                    }*/
                    if (textChanged) {
                        textChanged = false
                        if (vitalsList[position].name == "BMI" || vitalsList[position].name == "bmi" || vitalsList[position].name == "Bmi")
                            holder.vitualEditText.setText(bmiCalcFinal.toString())
                    }
                }
            }
        })

        holder.nameTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 2) {

                    onSearchClickListener?.onSearchClick(
                        holder.nameTextView, position
                    )

                }
                textChanged = true
            }
        })


        val adapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                typeList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        holder.spinner_type.adapter = adapter

        holder.spinner_type.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                val itemValue = parent?.getItemAtPosition(pos).toString()
                vitalsTemplateMasterDetail.uom_master_uuid =
                    typeList.filterValues { it == itemValue }.keys.toList()[0]!!

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

                val itemValue = parent?.getItemAtPosition(pos).toString()
                vitalsTemplateMasterDetail.uom_master_uuid =
                    typeList.filterValues { it == itemValue }.keys.toList()[0]!!

                //Log.e("SpinnerSelec",vitalsList[position].uom_master_uuid.toString())
                //hashMapType[vitalsTemplateMasterDetail.uom_master_uuid] = pos

            }
        }

        try {
            holder.spinner_type.setSelection(hashtypeSpinnerList.get(vitalsTemplateMasterDetail.uom_master_uuid)!!)

        } catch (e: Exception) {
            Log.i("SpinnerMapErr", e.toString())
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


    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            templateDetail: TemplateDetail,
            position: Int
        )
    }


    interface OnItemClickListener {
        fun onItemClick(
            templateDetail: TemplateDetail?,
            position: Int,
            selected: Boolean
        )
    }


    fun setOnSearchClickListener(onSearchClickListener: OnSearchClickListener) {
        this.onSearchClickListener = onSearchClickListener
    }

    interface OnSearchClickListener {
        fun onSearchClick(
            autoCompleteTextView: AppCompatAutoCompleteTextView,
            position: Int
        )
    }


    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<GetVital>,
        searchPosition: Int?
    ) {
        val responseContentAdapter = VitalSearchAdapter(
            context,
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as GetVital?

            val check = vitalsList.any { it.uuid == selectedPoi?.uuid }

            if (!check) {

                dropdownReferenceView.setText(selectedPoi?.name)
                vitalsList[searchPosition!!].name = selectedPoi!!.name
                vitalsList[searchPosition].uuid = selectedPoi.uuid
                vitalsList.add(TemplateDetail())
                notifyDataSetChanged()

            } else {
                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()
            }
        }
    }

    inner class VitalHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val nameTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.VitalName)
        internal val spinner_type: Spinner = itemView.findViewById(R.id.uomSpinner)
        internal var vitualEditText: EditText = itemView.findViewById(R.id.vitalEdittext)
        internal var mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)


    }

    fun addFavouritesInRow(templateMasterDetails: ArrayList<TemplateDetail>) {

        for (i in templateMasterDetails.indices) {

            val check = vitalsList.any { it.uuid == templateMasterDetails[i].uuid }

            if (!check) {

                if (templateMasterDetails.get(i).is_default) {
                    vitalsList.add(templateMasterDetails.get(i))
                }

                notifyDataSetChanged()
            } else {

                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()

            }
        }
        vitalsList.add(TemplateDetail())
        notifyDataSetChanged()
    }

    fun addRow(
        responseContent: TemplateDetail?

    ) {
        val check = vitalsList.any { it.uuid == responseContent?.uuid }

        if (!check) {
            vitalsList.removeAt(vitalsList.size - 1)
            vitalsList.add(responseContent!!)
            vitalsList.add(TemplateDetail())
            notifyDataSetChanged()
            //notifyItemInserted(vitalsList.size - 1)
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    //Navigation template to list delete
    fun deleteRowItem(id: Int) {
        for (i in vitalsList.indices) {

            if (vitalsList.get(i).uuid.equals(id)) {
                this.vitalsList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int, name: String) {

        val customdialog = Dialog(context)
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customdialog.setCancelable(false)
        customdialog.setContentView(R.layout.delete_cutsom_layout)
        val closeImageView = customdialog.findViewById(R.id.closeImageView) as ImageView
        closeImageView.setOnClickListener {
            customdialog.dismiss()
        }
        val drugNmae = customdialog.findViewById(R.id.addDeleteName) as TextView
        drugNmae.text = "${drugNmae.text} '" + name + "' Record ?"

        val yesBtn = customdialog.findViewById(R.id.yes) as CardView
        val noBtn = customdialog.findViewById(R.id.no) as CardView
        yesBtn.setOnClickListener {
            try {
                if (!vitalsList.isNullOrEmpty() && vitalsList.size > 0) {
                    vitalsList.removeAt(position)
                    notifyDataSetChanged()
                }
                customdialog.dismiss()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        noBtn.setOnClickListener {
            customdialog.dismiss()
        }
        customdialog.show()

    }

    fun setTypeValue(responseContents: List<ResponseContentsUOM?>?) {

        typeList = responseContents!!.map { it!!.uuid to it.name }.toMap().toMutableMap()

        hashtypeSpinnerList.clear()

        for (i in responseContents.indices) {

            hashtypeSpinnerList[responseContents[i]!!.uuid] = i
        }

        notifyDataSetChanged()

    }

    fun getall(): ArrayList<TemplateDetail> {

        return this.vitalsList

    }

    fun clearAll() {

        vitalsList.clear()
        vitalsList = ArrayList<TemplateDetail>()

        notifyDataSetChanged()
    }


}









