package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*

class PrescriptionMobileAdapter(
    private val context: Activity,
    private val PrescriptionArrayList: ArrayList<PrescriptionListData?>
) : RecyclerView.Adapter<PrescriptionMobileAdapter.MyViewHolder>() {


    private var vw: View? = null

    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onRefreshClickListener: OnRefreshClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    private var onCommandClickListener: OnCommandClickListener? = null

    private var status: Boolean = true
    var InjectionList: ArrayList<InjectionDepartment> = ArrayList()
    var InstructionList: ArrayList<PresInstructionResponseContent> = ArrayList()

    lateinit var selectedResponseContent: PrescriptionListData
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_mobile_prescription, parent, false)

        vw = view
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return PrescriptionArrayList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        selectedResponseContent = PrescriptionArrayList[position]!!

        holder.nameTextView.text =
            selectedResponseContent.itemAppendString + " - " + selectedResponseContent.drug_route_name

        holder.detailsTextView.text =
            selectedResponseContent.drug_frequency_name + " - " + "${selectedResponseContent.duration} ${selectedResponseContent.drug_period_name} - ${selectedResponseContent.drug_quantity} Pills - ${selectedResponseContent.drug_instruction_name}"

        if (selectedResponseContent.Update) {

            holder.mainLinearLayout.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.detailsTextView.setTextColor(ContextCompat.getColor(context, R.color.white))


        } else {
            holder.mainLinearLayout.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.navColor))
            holder.detailsTextView.setTextColor(ContextCompat.getColor(context, R.color.navColor))

        }


        if (PrescriptionArrayList[position]!!.drug_active!!) {

            var ck =
                InjectionList.any { it.uuid == PrescriptionArrayList[position]!!.selectInvestID }

            if (!ck) {

                status = false

                holder.mainLinearLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )
                holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.detailsTextView.setTextColor(ContextCompat.getColor(context, R.color.white))

                holder.detailsTextView.text =
                    PrescriptionArrayList[position]!!.drug_frequency_name + " - " + "${PrescriptionArrayList[position]!!.duration} ${PrescriptionArrayList[position]!!.drug_period_name} - ${PrescriptionArrayList[position]!!.drug_quantity} Pills"

            } else {

                holder.detailsTextView.text =
                    selectedResponseContent.drug_frequency_name + " - " + "${selectedResponseContent.duration} ${selectedResponseContent.drug_period_name} - ${selectedResponseContent.drug_quantity} Pills - ${selectedResponseContent.drug_instruction_name}"

                if (PrescriptionArrayList[position]!!.Update) {


                    holder.mainLinearLayout.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                        )
                    )
                    holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                    holder.detailsTextView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )


                } else {
                    holder.mainLinearLayout.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    holder.nameTextView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.navColor
                        )
                    )
                    holder.detailsTextView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.navColor
                        )
                    )

                }

            }


        }



        holder.mainLinearLayout.setOnClickListener {

            if (PrescriptionArrayList[position]!!.Update) {

                setCollageClear()

                onRefreshClickListener!!.onRefreshClick()


            } else {

                setCollage(position)

                var data = PrescriptionArrayList[position]!!

                onItemClickListener!!.onItemClick(data, position)

            }

        }



        holder.moreImageView.setOnClickListener {

            val popup =
                PopupMenu(context, holder.moreImageView)
            //inflating menu from xml resource
            popup.inflate(R.menu.lab_options_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        onCommandClickListener!!.onCommandClick(
                            position,
                            PrescriptionArrayList.get(position)!!.commands ?: ""
                        )
                    }
                    R.id.delete -> {

                        onDeleteClickListener?.onDeleteClick(
                            PrescriptionArrayList.get(position),
                            position
                        )
                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()

        }


    }

    private fun setCollage(position: Int) {

        for (i in PrescriptionArrayList.indices) {

            PrescriptionArrayList[i]!!.Update =
                position == i
        }

        status = true

        notifyDataSetChanged()

    }


    private fun setCollageClear() {

        for (i in PrescriptionArrayList.indices) {

            PrescriptionArrayList[i]!!.Update = false
        }

        status = true

        notifyDataSetChanged()

    }

    class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val mainLinearLayout: CardView = itemView.findViewById(R.id.cardView)
        internal val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        internal val detailsTextView: TextView = itemView.findViewById(R.id.DetailsTextView)
        internal val moreImageView: ImageView = itemView.findViewById(R.id.moreImageView)


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


    interface OnItemClickListener {
        fun onItemClick(
            responseContent: PrescriptionListData?,
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
            responseContent: PrescriptionListData?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }


    interface OnRefreshClickListener {
        fun onRefreshClick(
        )
    }

    fun setOnRefreshClickListener(onCommandClickListener: OnRefreshClickListener) {
        this.onRefreshClickListener = onCommandClickListener
    }


    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }

    fun clearall() {
        PrescriptionArrayList.clear()
        status = true
        notifyDataSetChanged()

    }

    fun deleteRow(

        position1: Int
    ): Boolean {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)

        val data = PrescriptionArrayList[position1]

        this.PrescriptionArrayList.removeAt(position1)

        var ischeck: Boolean = true

        //favouritesArrayList.any{ it!!.template_id == data?.template_id}

        for (i in this.PrescriptionArrayList.indices) {

            if (PrescriptionArrayList[i]!!.template_id == data!!.template_id) {

                ischeck = false
                break

            }
        }


        notifyItemRemoved(position1)
        status = true
        notifyDataSetChanged()

        return ischeck

    }

    /*
   Delete row from template
   */
    fun deleteRowFromFav(
        drugId: Int,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        val OriginalItemCount = itemCount

        for (i in PrescriptionArrayList.indices) {
            if (PrescriptionArrayList.get(i)?.drug_id?.equals(drugId)!!) {
                this.PrescriptionArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }
        status = true
        notifyDataSetChanged()
    }

    fun addFavouritesInRowModule(
        responseContent: PrescriptionListData?,
        position: Int
    ) {

        val check = PrescriptionArrayList.any { it!!.test_master_id == responseContent?.drug_id }

        if (!check) {

            responseContent?.test_master_id = responseContent?.drug_id
            responseContent?.itemAppendString = responseContent?.drug_name

            PrescriptionArrayList.add(responseContent)
            status = true
            notifyDataSetChanged()


        } else {
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                ?.show()

            status = true
            notifyDataSetChanged()
        }
    }

    fun addTemplatesInRow(templateDrugDetails: PrescriptionListData?) {

        val check =
            PrescriptionArrayList.any { it!!.test_master_id == templateDrugDetails?.test_master_id }
        if (!check) {

            PrescriptionArrayList.add(templateDrugDetails)
            status = true
            notifyDataSetChanged()

        } else {

            status = true
            notifyDataSetChanged()

            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                ?.show()

        }

    }


    fun Update(responseContent: PrescriptionListData?, position: Int) {

        responseContent?.Update = false

        PrescriptionArrayList[position] = responseContent

        notifyItemChanged(position)

    }

    fun addDummy(responseContent: PrescriptionListData?) {

        val check =
            PrescriptionArrayList.any { it!!.test_master_id == responseContent?.test_master_id }


        if (!check) {

            PrescriptionArrayList.add(responseContent)
            status = true
            notifyDataSetChanged()

        } else {
            status = true

            notifyDataSetChanged()

            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                ?.show()

        }

    }


    fun addRow(
        responseContent: PrescriptionListData?

    ) {


        val check =
            PrescriptionArrayList.any { it!!.test_master_id == responseContent?.test_master_id }
        if (!check) {
            PrescriptionArrayList.add(responseContent)
            notifyItemInserted(PrescriptionArrayList.size - 1)
        } else {

            status = true
            notifyDataSetChanged()
        }
    }


    fun getItems(): ArrayList<PrescriptionListData?> {

        return PrescriptionArrayList

    }

    fun deleteRowFromTemplate(drugId: Int, position: Int) {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        val OriginalItemCount = itemCount


        for (i in PrescriptionArrayList.indices) {
            if (PrescriptionArrayList.get(i)?.test_master_id?.equals(drugId)!!) {
                this.PrescriptionArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }

        }

    }

    fun isCheckAlreadyExist(drugId: Int): Boolean {
        var isCheck: Boolean = true

        for (i in PrescriptionArrayList.indices) {
            if (PrescriptionArrayList.get(i)?.drug_id?.equals(drugId)!!) {
                isCheck = false
                break
            }

        }

        return isCheck
    }

    fun setInjection(InjectionListData: ArrayList<InjectionDepartment>) {

        this.InjectionList = InjectionListData
        status = true
        notifyDataSetChanged()
    }

    fun getStatus(): Boolean {

        return status
    }

    fun setInstruction(InstructionListData: ArrayList<PresInstructionResponseContent>) {
        this.InstructionList = InstructionListData
        status = true
        notifyDataSetChanged()
    }


    fun addCommands(position: Int, command: String) {

        PrescriptionArrayList[position]!!.commands = command

        notifyDataSetChanged()

    }
}