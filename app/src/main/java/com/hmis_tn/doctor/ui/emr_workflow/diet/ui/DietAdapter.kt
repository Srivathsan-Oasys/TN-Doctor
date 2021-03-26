package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.DietResponseResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempDetails
import kotlin.collections.toMutableMap as toMutableMap1

class DietAdapter(
    private val context: Activity,
    private val favouritesArrayList: ArrayList<FavouritesModel?>,
    private val templeteArrayList: ArrayList<TempDetails?>
) : RecyclerView.Adapter<DietAdapter.MyViewHolder>() {

    private val TAG = DietAdapter::class.java.simpleName
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    lateinit var selectedResponseContent: FavouritesModel

    private var dietCategoryMap = mutableMapOf<Int, String>()
    private var dietfrequencymap = mutableMapOf<Int, String>()

    private val dietCategoryMapID: HashMap<Int, Int> = HashMap()
    private val dieFrequencyMapID: HashMap<Int, Int> = HashMap()

    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_diet, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        selectedResponseContent = favouritesArrayList[position]!!
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.autoCompleteTextView.setText(selectedResponseContent.itemAppendString, false)
        holder.autoCompleteTextView.requestFocus()
        holder.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    try {
                        onSearchInitiatedListener?.onSearchInitiated(
                            s.toString(),
                            holder.autoCompleteTextView,
                            position
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })

        holder.etqty.setText(selectedResponseContent.diet_quantity.toString())

        holder.etqty.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 0) {
                    Log.e("quantity", "" + s.toString().toInt())
                    selectedResponseContent.diet_quantity = s.toString().toInt()
                    favouritesArrayList[position]!!.diet_quantity = s.toString().toInt()
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

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(selectedResponseContent, position)
        }

        holder.deleteImageView.setOnClickListener {
            selectedResponseContent = favouritesArrayList[position]!!
            if (!holder.autoCompleteTextView.text.trim().isEmpty()) {
                onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
                dietCategoryMapID.remove(selectedResponseContent.diet_master_id)
            }
        }

        if (position == favouritesArrayList.size - 1) {
            holder.deleteImageView.alpha = 0.2f
            holder.deleteImageView.isEnabled = false
        } else {
            holder.deleteImageView.alpha = 1f
            holder.deleteImageView.isEnabled = true
        }

        val adapterCatagory =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                dietCategoryMap.values.toMutableList()
            )
        adapterCatagory.setDropDownViewResource(R.layout.spinner_item)
        holder.catagorySpinner.adapter = adapterCatagory
        try {
            if (dietCategoryMap.containsKey(selectedResponseContent.diet_category_id)) {
                holder.catagorySpinner.setSelection(selectedResponseContent.diet_category_id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.catagorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    try {
                        val itemValue = parent?.getItemAtPosition(pos).toString()
                        selectedResponseContent = favouritesArrayList[position]!!
                        selectedResponseContent.selectTypeUUID =
                            dietCategoryMap.filterValues { it == itemValue }.keys.toList()[0]
                        dietCategoryMapID[selectedResponseContent.diet_master_id] = pos
                        selectedResponseContent.diet_category_id =
                            selectedResponseContent.selectTypeUUID
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        val adapterfrequency =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                dietfrequencymap.values.toMutableList()
            )
        adapterfrequency.setDropDownViewResource(R.layout.spinner_item)
        holder.frequencySpinner.adapter = adapterfrequency
        try {
            if (dietfrequencymap.containsKey(selectedResponseContent.diet_frequency_id)) {
                holder.frequencySpinner.setSelection(selectedResponseContent.diet_frequency_id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.frequencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    try {
                        val itemValue = parent?.getItemAtPosition(pos).toString()
                        selectedResponseContent = favouritesArrayList[position]!!

                        selectedResponseContent.selectTypeUUID = dietfrequencymap
                            .filterValues { it == itemValue }.keys.toList()[0]
                        dieFrequencyMapID[selectedResponseContent.diet_master_id] = pos
                        selectedResponseContent.diet_frequency_id =
                            selectedResponseContent.selectTypeUUID
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val autoCompleteTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.autoCompleteTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
        internal val etqty: EditText = itemView.findViewById(R.id.et_qty)
        internal val catagorySpinner: Spinner = itemView.findViewById(R.id.type_category)
        internal val frequencySpinner: Spinner = itemView.findViewById(R.id.type_frequency)
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
            position: Int
        )
    }

    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }

    fun clearAll() {
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
        var ischeck = true
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
            if (favouritesArrayList[i]?.diet_master_id?.equals(tempId!!)!! &&
                favouritesArrayList[i]?.viewLabstatus?.equals(position1)!!
            ) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
        notifyDataSetChanged()
        addRow(FavouritesModel())
    }

    fun addFavouritesInRow(
        responseContent: FavouritesModel?,
        viewLabstatus: Int
    ) {
        val check =
            favouritesArrayList.any { it!!.diet_master_id == responseContent?.diet_master_id }
        if (!check) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
            responseContent?.itemAppendString = responseContent?.diet_master_name
            responseContent?.diet_master_id = responseContent?.diet_master_id!!
            responseContent.viewLabstatus = viewLabstatus
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
            favouritesArrayList.any { it!!.diet_master_id == responseContent?.diet_master_id }
        if (!check) {
            favouritesArrayList.add(responseContent)
//            notifyItemInserted(favouritesArrayList.size - 1)
            notifyDataSetChanged()
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

    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<DietResponseResponseContent?>?,
        searchposition: Int
    ) {
        val responseContentAdapter = DietSearchResultAdapter(
            context,
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.showDropDown()

        dropdownReferenceView.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as DietResponseResponseContent?
            val check = favouritesArrayList.any { it!!.diet_master_id == selectedPoi?.uuid }
            if (!check) {
                try {
                    dropdownReferenceView.setText(selectedPoi?.name)
                    favouritesArrayList[searchposition]!!.chief_complaint_name = selectedPoi?.name
                    favouritesArrayList[searchposition]!!.itemAppendString = selectedPoi?.name
                    favouritesArrayList[searchposition]!!.diet_master_id = selectedPoi?.uuid!!
                    favouritesArrayList[searchposition]!!.diet_master_code =
                        selectedPoi.code.toString()
                    favouritesArrayList[searchposition]!!.diet_master_name =
                        selectedPoi.name.toString()
                    favouritesArrayList[searchposition]!!.diet_frequency_id =
                        selectedPoi.diet_frequency_uuid!!
                    favouritesArrayList[searchposition]!!.diet_category_id =
                        selectedPoi.diet_category_uuid!!
                    notifyDataSetChanged()
                    addRow(FavouritesModel())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()
            }
        }
    }

    fun getItems(): ArrayList<FavouritesModel?> {
        return favouritesArrayList
    }

    fun getAll(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList
    }

    fun addPrevInRow(
        responseContent: FavouritesModel?
    ) {
        val check =
            favouritesArrayList.any { it!!.diet_master_id == responseContent?.diet_master_id }
        if (!check) {
//            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
            responseContent?.itemAppendString = responseContent?.test_master_name
            responseContent?.diet_master_id = responseContent?.diet_master_id!!
            favouritesArrayList.add(responseContent)
            favouritesArrayList.add(FavouritesModel())
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    fun setadapterCategoryValue(responseContents: List<FavAddAllDepatResponseContent>?) {
        dietCategoryMap =
            responseContents?.map { it.uuid to it.name }!!.toMap().toMutableMap1()
        //   notifyDataSetChanged()
    }

    fun setadapterFrequencyValue(responseContents: List<FavAddAllDepatResponseContent>?) {
        dietfrequencymap =
            responseContents?.map { it.uuid to it.name }!!.toMap().toMutableMap1()
        //notifyDataSetChanged()
    }
}