package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.*
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.drug_info.GetDrugInfoReq
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.drug_info.GetDrugInfoResp
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.drug_info.ResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.treatmentKitPresModel.TKtPrescriptionListData
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.dialog_drug_info.*
import retrofit2.Response

class TreatmentKitPrescriptionMobileAdapter(
    private val context: Activity,
    private val treatmentKitChildFragment: TreatmentKitChildFragment,
    private val PrescriptionArrayList: ArrayList<TKtPrescriptionListData?>
) : RecyclerView.Adapter<TreatmentKitPrescriptionMobileAdapter.MyViewHolder>(),
    ITreatmentKitAdapter {

    private var vw: View? = null

    private var utils: Utils? = null
    private lateinit var spinnerArray: MutableList<String>
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onCommandClickListener: OnCommandClickListener? = null
    private var onRefreshClickListener: OnRefreshClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null


    private var status: Boolean = true
    var InjectionList: ArrayList<InjectionDepartment> = ArrayList()
    var InstructionList: ArrayList<PresInstructionResponseContent> = ArrayList()

    lateinit var selectedResponseContent: TKtPrescriptionListData
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_mobile_prescription, parent, false)
        vw = view
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return PrescriptionArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        selectedResponseContent = PrescriptionArrayList[position]!!
        utils = Utils(holder.itemView.context)

        holder.nameTextView.text =
            selectedResponseContent.drug_name + " - " + selectedResponseContent.drug_route_name

        holder.detailsTextView.text =
            selectedResponseContent.drug_frequency_name + " - " + "${selectedResponseContent.drug_duration} " +
                    "${selectedResponseContent.drug_period_name} - ${selectedResponseContent.drug_quantity} Pills - " +
                    "${selectedResponseContent.drug_instruction_name}"

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
            val ck =
                InjectionList.any { it.uuid == PrescriptionArrayList[position]!!.drug_instruction_id }

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
                    PrescriptionArrayList[position]!!.drug_frequency_name + " - " + "${PrescriptionArrayList[position]!!.drug_duration} " +
                            "${PrescriptionArrayList[position]!!.drug_period_name} - ${PrescriptionArrayList[position]!!.drug_quantity} Pills"
            } else {
                holder.detailsTextView.text =
                    selectedResponseContent.drug_frequency_name + " - " + "${selectedResponseContent.drug_duration} " +
                            "${selectedResponseContent.drug_period_name} - ${selectedResponseContent.drug_quantity} Pills - " +
                            "${selectedResponseContent.drug_instruction_name}"

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
                val data = PrescriptionArrayList[position]!!
                onItemClickListener!!.onItemClick(data, position)
            }
        }


        holder.moreImageView.setOnClickListener {
            val popup = PopupMenu(context, holder.moreImageView)
            popup.inflate(R.menu.action_info_comment_delete)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.info -> {
                        getDrugInfo(selectedResponseContent)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.comment -> {
                        onCommandClickListener!!.onCommandClick(
                            position,
                            PrescriptionArrayList.get(position)?.commands ?: ""
                        )
                        return@setOnMenuItemClickListener true
                    }
                    R.id.delete -> {
                        onDeleteClickListener?.onDeleteClick(selectedResponseContent, position)
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
            popup.show()
        }
    }

    //    private fun getDrugInfo(itemMasterUuid: Int, storeMasterUuid: Int, storeTypeUuid: Int) {
    private fun getDrugInfo(prescription: TKtPrescriptionListData) {
        val body = GetDrugInfoReq(
            is_expiry_batch = true,
            item_master_uuid = 8,
//            store_master_uuid = prescription.store_master_uuid ?: 0,
            store_master_uuid = 62,
            store_type_uuid = 7
        )
        treatmentKitChildFragment.viewModel?.getDrugInfo(
            facilityID = treatmentKitChildFragment.facility_id,
            getDrugInfoReq = body,
            getDrugInfoRespCallback = getDrugInfoRespCallback
        )
    }

    private fun showDrugInfoDialog(context: Context, responseContent: ArrayList<ResponseContent>) {
        val drugInfoDialog = DrugInfoDialog(context)
        drugInfoDialog.show()
        drugInfoDialogFunctions(drugInfoDialog, responseContent)
    }

    private fun drugInfoDialogFunctions(
        drugInfoDialog: DrugInfoDialog,
        responseContent: ArrayList<ResponseContent>
    ) {
        val ivClose: ImageView = drugInfoDialog.findViewById(R.id.ivClose)
        val cvCancel: CardView = drugInfoDialog.findViewById(R.id.cvCancel)
        val rvDrugInfo: RecyclerView = drugInfoDialog.findViewById(R.id.rvDrugInfo)
        val tvTotalQty: TextView = drugInfoDialog.findViewById(R.id.tvTotalQty)

        rvDrugInfo.layoutManager = LinearLayoutManager(drugInfoDialog.context)
        rvDrugInfo.adapter = DrugInfoAdapter(responseContent)

        var totalQty = 0
        responseContent.forEach { content ->
            totalQty += content.quantity ?: 0
        }
        tvTotalQty.text = "Total Quantity: $totalQty"

        ivClose.setOnClickListener {
            drugInfoDialog.dismiss()
        }

        cvCancel.setOnClickListener {
            drugInfoDialog.dismiss()
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

    interface OnItemClickListener {
        fun onItemClick(
            responseContent: TKtPrescriptionListData?,
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
            responseContent: TKtPrescriptionListData?,
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
            if (PrescriptionArrayList[i]!!.drug_id == data!!.drug_id) {
                ischeck = false
                break
            }
        }
        notifyItemRemoved(position1)
        status = true
        notifyDataSetChanged()
        return ischeck
    }

    fun deleteRow(
        drugCode: String?
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.PrescriptionArrayList.forEach {
            if (it?.drug_code == drugCode) {
                PrescriptionArrayList.remove(it)
            }
        }
        notifyDataSetChanged()
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

    fun Update(responseContent: TKtPrescriptionListData?, position: Int) {
        responseContent?.Update = false
        PrescriptionArrayList[position] = responseContent
        notifyItemChanged(position)
    }

    fun Update(responseContent: TKtPrescriptionListData?) {
        for (i in PrescriptionArrayList.indices) {
            if (PrescriptionArrayList[i]?.drug_id == responseContent?.drug_id) {
                responseContent?.Update = false
                PrescriptionArrayList[i] = responseContent
                notifyItemChanged(i)
                break
            }
        }
    }

    fun addDummy(responseContent: TKtPrescriptionListData?) {
        val check = PrescriptionArrayList.any { it!!.drug_id == responseContent?.drug_id }

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
        responseContent: TKtPrescriptionListData?
    ) {
        val check =
            PrescriptionArrayList.any { it!!.drug_id == responseContent?.drug_id }
        if (!check) {
            PrescriptionArrayList.add(responseContent)
            notifyItemInserted(PrescriptionArrayList.size - 1)
        } else {
            status = true
            notifyDataSetChanged()
        }
    }

    fun getItems(): ArrayList<TKtPrescriptionListData?> {
        return PrescriptionArrayList
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

    private val getDrugInfoRespCallback =
        object : RetrofitCallback<GetDrugInfoResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetDrugInfoResp>?) {
                if (responseBody?.code() == 200) {
                    val list: ArrayList<ResponseContent> = ArrayList()
                    responseBody.body()?.responseContents?.forEach {
                        list.add(it)
                    }
                    showDrugInfoDialog(context, list)
                }
            }

            override fun onBadRequest(errorBody: Response<GetDrugInfoResp>?) {
                utils?.showToast(
                    message = treatmentKitChildFragment.resources.getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
                utils?.showToast(
                    message = treatmentKitChildFragment.resources.getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    message = treatmentKitChildFragment.resources.getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    message = treatmentKitChildFragment.resources.getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        message = s
                    )
                }
            }

            override fun onEverytime() {
            }
        }
}