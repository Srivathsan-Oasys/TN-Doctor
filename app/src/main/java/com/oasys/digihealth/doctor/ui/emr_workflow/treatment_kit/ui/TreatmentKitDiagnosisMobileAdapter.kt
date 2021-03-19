package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui

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
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.DiagnosisListData
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedData
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import kotlinx.android.synthetic.main.item_lab_details.view.*
import kotlinx.android.synthetic.main.row_mobile_treatmentkit_diagnosis_list.view.*

class TreatmentKitDiagnosisMobileAdapter(
    private val context: Activity,
    private val favouritesArrayList: ArrayList<DiagnosisListData?>
) :
    RecyclerView.Adapter<TreatmentKitDiagnosisMobileAdapter.MyViewHolder>(), ITreatmentKitAdapter {

    private var onSearchDignisisInitiatedListener: OnSearchDignisisInitiatedListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    lateinit var selectedResponseContent: FavouritesModel
    private var onCommandClickListener: OnCommandClickListener? = null


    private var onItemClickListener: OnItemClickListener? = null

    private var onSelectItemCommunication: OnSelectItemCommunication? = null

    /*

    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_diagnosis_list, parent, false)
        return MyViewHolder(view)
    }


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val chiefComplaintsModel = favouritesArrayList[position]!!



        if (chiefComplaintsModel.is_snomed == 0) {

//            holder.typeTextView.text = "ICD 10"
        } else {
//            holder.typeTextView.text = "Snomed"

        }


        val selectedItem = favouritesArrayList.get(position)?.drug_active
        val selectedData = favouritesArrayList.get(position)
        if (selectedItem!!) {
            setSelectedBackground(holder)
        } else {
            setUnSelectedBackground(holder)
        }
        holder.itemView.ll_edit_view.setOnClickListener {

            onSelectItemCommunication?.onClick(
                position,
                selectedItem,
                favouritesArrayList.get(position)
            )
        }


        holder.itemView.rl_view.setOnClickListener {
            val popup = PopupMenu(context, holder.itemView.rl_view.textViewOptions)
            popup.inflate(R.menu.delete_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    /*    R.id.edit -> {
                            onCommandClickListener!!.onCommandClick(
                                position,
                                favouritesArrayList!!.get(position)!!.commands
                            )
                        }*/
                    R.id.delete -> {
                        onDeleteClickListener?.onDeleteClick(
                            favouritesArrayList[position],
                            position
                        )
                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }
        holder.itemView.tv_no.text = (position + 1).toString()


        if (chiefComplaintsModel.is_snomed == 0) {


            holder.itemView.tv_title.text = ("ICD 10" + " - " + selectedData?.diagnosis_code)
        } else {

            holder.itemView.tv_title.text = ("Snomed" + " - " + selectedData?.diagnosis_code)


        }

        holder.itemView.tv_sub_menu.text = selectedData?.diagnosis_name


    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun setUnSelectedBackground(holder: MyViewHolder) {
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

    fun setSelectedBackground(holder: MyViewHolder) {
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

    interface OnItemClickListener {
        fun onItemClick(
            favouritesModel: DiagnosisListData?,
            position: Int
        )
    }

    fun setOnItemClickListener(onitemClickListener: OnItemClickListener) {
        this.onItemClickListener = onitemClickListener
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesModel: DiagnosisListData?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }


    fun setOnCommandClickListener(onCommandClickListener: OnCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }

    interface OnCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String
        )
    }

    interface OnSearchDignisisInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int
        )
    }

    fun setOnSearchInitiatedListener(OnSearchDignisisInitiatedListener: OnSearchDignisisInitiatedListener) {
        this.onSearchDignisisInitiatedListener = OnSearchDignisisInitiatedListener
    }


    fun deleteRow(
        favouritesModel: DiagnosisListData,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.removeAt(position1)
        notifyDataSetChanged()
    }

    fun deleteRow(
        diagnosCode: String?
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.forEach {
            if (it?.diagnosis_code == diagnosCode) {
                favouritesArrayList.remove(it)
            }
        }
        notifyDataSetChanged()
    }

    fun deleteRowFromFavourites(
        favouritesModel: DiagnosisListData?,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.remove(favouritesModel)
        notifyDataSetChanged()

    }


    fun addFavouritesInRow(
        favouritesModel: DiagnosisListData?
    ) {

        val check = favouritesArrayList.any { it!!.diagnosis_id == favouritesModel?.diagnosis_id }

        if (!check) {

            favouritesArrayList.add(favouritesModel)
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()

        }

    }

    fun addRow(
        favouritesModel: DiagnosisListData?
    ) {

        val check = favouritesArrayList.any { it!!.diagnosis_id == favouritesModel?.diagnosis_id }

        if (!check) {

            favouritesArrayList.add(favouritesModel)
            notifyDataSetChanged()
        } else {

            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()

            notifyDataSetChanged()

        }
    }


    fun UpdateRow(position: Int, data: DiagnosisListData) {

        data.drug_active = false

        favouritesArrayList[position] = data

        notifyDataSetChanged()


    }

    fun UpdateRow(data: DiagnosisListData) {


        for (i in favouritesArrayList.indices) {

            if (favouritesArrayList[i]?.diagnosis_id == data.diagnosis_id) {

                favouritesArrayList[i] = data

                notifyItemChanged(i)

                break


            }


        }


    }


    fun clearall() {
        favouritesArrayList.clear()
        notifyDataSetChanged()
    }

    fun getall(): ArrayList<DiagnosisListData?> {
        return favouritesArrayList
    }


    fun addSnomed(data: SnomedData) {

        val favouritesModel: FavouritesModel = FavouritesModel()

        favouritesModel.itemAppendString = data.ConceptName

        favouritesModel.diagnosis_id = data.ConceptId.toString()

    }


    fun onItemClick(onSelectItemCommunication: OnSelectItemCommunication) {
        this.onSelectItemCommunication = onSelectItemCommunication
    }

    interface OnSelectItemCommunication {
        fun onClick(
            position: Int,
            selectedItem: Boolean,
            favouritesModel: DiagnosisListData?
        )
    }


    fun updateSelectStatus(position: Int, selected: Boolean) {
        this.favouritesArrayList.get(position)?.drug_active = !selected
        unSelectOtherPositions(position)
        notifyDataSetChanged()
    }


    fun unSelectOtherPositions(position: Int) {
        for (i in favouritesArrayList.indices) {
            if (i != position) {
                favouritesArrayList[i]?.drug_active = false
            }
        }
    }

    fun addCommands(position: Int, command: String) {
        favouritesArrayList[position]!!.commands = command
        notifyDataSetChanged()
    }


}


