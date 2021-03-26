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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.ResponseContentsdiagonosissearch
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.DianosisCodeSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.DianosisSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel

class TreatmentKitDiagnosisAdapter(
    private val context: Activity,
    private var favouritesArrayList: ArrayList<FavouritesModel?>
) :
    RecyclerView.Adapter<TreatmentKitDiagnosisAdapter.MyViewHolder>(), ITreatmentKitAdapter {

    private var onSearchDignisisInitiatedListener: OnSearchDignisisInitiatedListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    lateinit var selectedResponseContent: FavouritesModel
    private var code: Int? = 1
    private var name: Int? = 2

    /* private var onItemClickListener: OnItemClickListener? = null

     private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_treatmentkit_diagnosis_list, parent, false)
        return MyViewHolder(view)
    }


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val chiefComplaintsModel = favouritesArrayList[position]!!
        holder.serialNumberTextView.text = (position + 1).toString()

        holder.diagnosisCodeAutoCompleteTextView.text = chiefComplaintsModel.diagnosis_code

        holder.autoCompleteTextView.setText(chiefComplaintsModel.itemAppendString)



        holder.autoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    onSearchDignisisInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.autoCompleteTextView,
                        position, name!!
                    )
                }
            }
        })


        /*holder.diagnosisCodeAutoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length<5) {
                    onSearchDignisisInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.diagnosisCodeAutoCompleteTextView,
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


        holder.deleteImageView.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(chiefComplaintsModel, position)
        }

        if (position == favouritesArrayList.size - 1) {
            holder.deleteImageView.alpha = 0.2f
            holder.deleteImageView.isEnabled = false

            holder.autoCompleteTextView.isFocusable = true
            holder.autoCompleteTextView.isFocusableInTouchMode = true

            //holder.diagnosisCodeAutoCompleteTextView.setFocusable(true);
            //holder.diagnosisCodeAutoCompleteTextView.setFocusableInTouchMode(true);

            holder.autoCompleteTextView.requestFocus()
        } else {
            holder.deleteImageView.alpha = 1f
            holder.deleteImageView.isEnabled = true
            holder.autoCompleteTextView.isFocusable = false
            holder.autoCompleteTextView.isFocusableInTouchMode = false
            //holder.diagnosisCodeAutoCompleteTextView.setFocusable(false);
            //holder.diagnosisCodeAutoCompleteTextView.setFocusableInTouchMode(false);
        }
    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.diagnosisSerialNumberTextView)
        internal val deleteImageView: ImageView =
            itemView.findViewById(R.id.diagnosisDeleteImageView)
        internal val autoCompleteTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.diagnosisautoCompleteTextView)
        internal val mainLinearLayout: LinearLayoutCompat =
            itemView.findViewById(R.id.diagnosisMainLinearLayout)
        internal val diagnosisCodeAutoCompleteTextView: TextView =
            itemView.findViewById(R.id.diagnosisCodeAutoCompleteTextView)
    }

    interface OnItemClickListener {
        fun onItemClick(
            favouritesModel: FavouritesModel?,
            position: Int
        )
    }

    /*fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }*/

    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesModel: FavouritesModel?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    interface OnSearchDignisisInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int,
            search: Int
        )
    }

    fun setOnSearchInitiatedListener(OnSearchDignisisInitiatedListener: OnSearchDignisisInitiatedListener) {
        this.onSearchDignisisInitiatedListener = OnSearchDignisisInitiatedListener
    }

    fun deleteRow(
        favouritesModel: FavouritesModel?,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.removeAt(position1)
        notifyDataSetChanged()

    }

    fun deleteRowFromFavourites(diagId: Int?) {
        if (favouritesArrayList.size > 0) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
        }

        for (i in favouritesArrayList.indices) {
            if (favouritesArrayList.get(i)?.favourite_id?.equals(diagId)!!) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        notifyDataSetChanged()
        addRow(FavouritesModel())
    }


    fun addFavouritesInRow(responseContent: FavouritesModel) {

        val check = favouritesArrayList.any { it!!.diagnosis_id == responseContent.diagnosis_id }

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
        favouritesModel: FavouritesModel?
    ) {
        favouritesArrayList.add(favouritesModel)
        notifyDataSetChanged()
    }

    fun addTempleteRow(
        responseContent: FavouritesModel?
    ) {

        val check = favouritesArrayList.any { it!!.diagnosis_id == responseContent?.diagnosis_id }

        if (!check) {

            favouritesArrayList.add(responseContent)
            notifyItemInserted(favouritesArrayList.size - 1)
        } else {

            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()

        }
    }

    fun clearall() {
        favouritesArrayList.clear()
        notifyDataSetChanged()
    }

    fun getall(): ArrayList<FavouritesModel?> {
        return favouritesArrayList
    }

    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: List<ResponseContentsdiagonosissearch?>,
        selectedSearchPosition: Int,
        search: Int
    ) {

        if (search == 2) {
            val responseContentAdapter = DianosisSearchResultAdapter(
                context,
                R.layout.row_chief_complaint_search_result,
                responseContents
            )
            dropdownReferenceView.setAdapter(responseContentAdapter)
        } else if (search == 1) {
            val responseContentAdapter = DianosisCodeSearchResultAdapter(
                context,
                R.layout.row_chief_complaint_search_result,
                responseContents
            )
            dropdownReferenceView.setAdapter(responseContentAdapter)
        }
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.showDropDown()
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ResponseContentsdiagonosissearch?

            val check =
                favouritesArrayList.any { it!!.diagnosis_id == selectedPoi?.uuid.toString() }

            if (!check) {

                dropdownReferenceView.setText(selectedPoi?.name)
                favouritesArrayList[selectedSearchPosition]?.diagnosis_name = selectedPoi?.name
                favouritesArrayList[selectedSearchPosition]?.itemAppendString = selectedPoi?.name
                favouritesArrayList[selectedSearchPosition]?.diagnosis_id =
                    selectedPoi?.uuid.toString()
                favouritesArrayList[selectedSearchPosition]?.diagnosis_code =
                    selectedPoi?.code.toString()
                favouritesArrayList[selectedSearchPosition]?.diagnosisUUiD =
                    selectedPoi?.uuid!!.toInt()
                notifyDataSetChanged()
                addRow(FavouritesModel())

                dropdownReferenceView.threshold = 0
            } else {

                notifyDataSetChanged()

                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()

            }
        }
    }
}