package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationLocationResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationTypeResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempDetails
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.TreatmentkitSearchresponseContent


class TreatmentKitInvestigationAdapter(
    private val context: Activity,
    private var favouritesArrayList: ArrayList<FavouritesModel?>,
    private val templeteArrayList: ArrayList<TempDetails?>
) :
    RecyclerView.Adapter<TreatmentKitInvestigationAdapter.MyViewHolder>(), ITreatmentKitAdapter {

    private var typeNamesList = mutableMapOf<Int, String>()
    private var toLocationMap = mutableMapOf<Int, String>()
    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()
    lateinit var selectedResponseContent: FavouritesModel

    private var onCommandClickListener: OnInvestigationCommandClickListener? = null

    private var code: Int? = 1
    private var name: Int? = 2

    @SuppressLint("NewApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.row_investigation_treatment, parent, false)

        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        selectedResponseContent = favouritesArrayList[position]!!
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.autoCompleteTextView.setText(selectedResponseContent.itemAppendString, false)
        if (selectedResponseContent.test_master_code.toString().equals("null")) {
            holder.codeAutoCompleteTextView.text = ""
        } else {
            holder.codeAutoCompleteTextView.text = selectedResponseContent.test_master_code.toString()
        }

        holder.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.autoCompleteTextView,
                        position, name!!
                    )
                }
            }
        })

        /*holder.codeAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.codeAutoCompleteTextView,
                        position,code!!
                    )
                }
            }
        })*/

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

        if (position == favouritesArrayList.size - 1) {
            holder.deleteImageView.alpha = 0.2f
            holder.deleteImageView.isEnabled = false

            holder.autoCompleteTextView.isFocusable = true
            holder.autoCompleteTextView.isFocusableInTouchMode = true

            //holder.codeAutoCompleteTextView.setFocusable(true);
            //holder.codeAutoCompleteTextView.setFocusableInTouchMode(true);

            holder.autoCompleteTextView.requestFocus()
        } else {
            holder.deleteImageView.alpha = 1f
            holder.deleteImageView.isEnabled = true
            holder.autoCompleteTextView.isFocusable = false
            holder.autoCompleteTextView.isFocusableInTouchMode = false

            //holder.codeAutoCompleteTextView.setFocusable(false);
            //holder.codeAutoCompleteTextView.setFocusableInTouchMode(false);
        }



        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(selectedResponseContent, position)
        }
        holder.deleteImageView.setOnClickListener {
            selectedResponseContent.chief_complaint_name =
                holder.autoCompleteTextView.text.toString()
            selectedResponseContent.chief_complaint_code =
                holder.codeAutoCompleteTextView.text.toString()
            onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
        }

        val gridLayoutManager = GridLayoutManager(
            context, 1,
            LinearLayoutManager.HORIZONTAL, false
        )


        val adapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                typeNamesList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
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
        /*holder.infoImageView.setOnClickListener{
            selectedResponseContent = favouritesArrayList[position]!!
            val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            val dialog = InfoDialogFragment()
            bundle.putString("DrugID", selectedResponseContent.test_master_id.toString())
            dialog.setArguments(bundle)

            dialog.show(ft, "Tag")

        }*/

        holder.commentsImageView.setOnClickListener {
            onCommandClickListener!!.onCommandClick(
                position,
                favouritesArrayList.get(position)!!.commands
            )
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
        internal val mainLinearLayout: LinearLayoutCompat =
            itemView.findViewById(R.id.mainLinearLayout)
        internal val spinner_type: Spinner = itemView.findViewById(R.id.type_spinner)
        internal val spinnerToLocation: Spinner = itemView.findViewById(R.id.tolocation)
        internal val codeAutoCompleteTextView: TextView =
            itemView.findViewById(R.id.codeAutoCompleteTextView)
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
            search: Int
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

    fun deleteRowFromFavourites(invId: Int) {
        if (favouritesArrayList.size > 0) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
        }

        for (i in favouritesArrayList.indices) {
            if (favouritesArrayList.get(i)?.favourite_id?.equals(invId)!!) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()
        addRow(FavouritesModel())
    }

    fun addFavouritesInRow(
        responseContent: FavouritesModel?
    ) {
        val check =
            favouritesArrayList.any { it!!.test_master_id == responseContent!!.test_master_id }

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
        responseContents: ArrayList<TreatmentkitSearchresponseContent>,
        searchPosition: Int?,
        search: Int?
    ) {

        if (search == 2) {
            val responseContentAdapter = TreatmentInvestigationSearchResultAdapter(
                context,
                R.layout.row_chief_complaint_search_result,
                responseContents
            )
            dropdownReferenceView.setAdapter(responseContentAdapter)
        } else if (search == 1) {
            val responseContentAdapter = TreatmentInvestigationCodeSearchResultAdapter(
                context,
                R.layout.row_chief_complaint_search_result,
                responseContents
            )
            dropdownReferenceView.setAdapter(responseContentAdapter)
        }
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as TreatmentkitSearchresponseContent
            val check = favouritesArrayList.any { it!!.test_master_id == selectedPoi.uuid }

            if (!check) {
                dropdownReferenceView.setText(selectedPoi.name)
                val st = FavouritesModel()
                st.chief_complaint_name = "dummy"

                favouritesArrayList[searchPosition!!]!!.chief_complaint_name = selectedPoi.name
                favouritesArrayList[searchPosition]!!.test_master_code = selectedPoi.code
                favouritesArrayList[searchPosition]!!.itemAppendString = selectedPoi.name

                if (selectedPoi.type.equals("test_master")) {
                    favouritesArrayList[searchPosition]!!.test_master_id = selectedPoi.uuid
                } else if (selectedPoi.type.equals("profile_master")) {
                    favouritesArrayList[searchPosition]!!.profile_master_uuid = selectedPoi.uuid!!
                } else {
                    favouritesArrayList[searchPosition]!!.test_master_id = selectedPoi.uuid
                }


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

    fun setadapterTypeValue(responseContents: List<InvestigationTypeResponseContent?>?) {

        typeNamesList = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()
        notifyDataSetChanged()

    }

    fun setToLocationList(responseContents: List<InvestigationLocationResponseContent?>?) {
        toLocationMap =
            responseContents?.map { it?.uuid!! to it.location_name }!!.toMap().toMutableMap()
        notifyDataSetChanged()

    }

    fun getItems(): ArrayList<FavouritesModel?> {
        return favouritesArrayList
    }


    fun clearall() {
        favouritesArrayList.clear()
        notifyDataSetChanged()
    }

    fun addCommands(position: Int, command: String) {

        favouritesArrayList[position]!!.commands = command

        notifyDataSetChanged()

    }


    interface OnInvestigationCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String
        )
    }

    fun setOnInvestigationCommandClickListener(onCommandClickListener: OnInvestigationCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }

}



