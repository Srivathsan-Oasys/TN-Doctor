package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogTemplateEditPrescriptionBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.ChiefComplaintFavAddresponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.request.Headers
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.PrescriptionSearchAdapter
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.SaveTemplateRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.SearchPrescription
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.SearchPrescriptionResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.response.SaveTemplateResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request.ExistingDetail
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request.NewDetail
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request.RemovedDetail
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request.UpdateRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.response.UpdateResponseModel
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException

class ManagePreTempAddEdit : DialogFragment() {
    private var ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
    private var favouriteData: Templates? = null
    private var is_active: Boolean = true
    private var Str_auto_id: Int? = 0
    private var content: String? = null
    private var viewModel: ManagePreTempAddEditviewModel? = null
    var binding: DialogTemplateEditPrescriptionBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    lateinit var drugNmae: TextView
    private var deparment_UUID: Int? = 0
    private var facility_id: Int? = 0
    private var user_uuid: Int? = 0
    private var customdialog: Dialog? = null
    var status: Boolean? = true
    private var customProgressDialog: CustomProgressDialog? = null

    var fromStats: Boolean? = false

    var exitingStatus: Boolean? = false

    var callbackfavourite: OnTempRefreshListener? = null
    private var test_uuid: Int? = 0

    private var routeSpinnerList = mutableMapOf<Int, String>()
    private var frequencySpinnerList = mutableMapOf<Int, String>()
    private var instructionSpinnerList = mutableMapOf<Int, String>()
    private var durationSpinnerList = mutableMapOf<Int, String>()


    private val hashfrequencySpinnerList: HashMap<Int, Int> = HashMap()
    private val hashrouteSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashinstructionSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashdurationSpinnerList: HashMap<Int, Int> = HashMap()

    private var selectRouteUuid: Int = 0
    private var selectFrequencyUuid: Int = 0
    private var selectInstructionUuid: Int = 0
    private var selectDurationUuid: Int = 0

    private var exitiingPisition: Int = 0


    private var IsTablet: Boolean = false


    private var frequencyList: ArrayList<PrescriptionFrequencyresponseContent?>? = ArrayList()
    private var instructionList: ArrayList<PresInstructionResponseContent?>? = ArrayList()
    private var routeList: ArrayList<PrescriptionRouteResponseContent?>? = ArrayList()
    private var periodList: ArrayList<PrescriptionDurationResponseContent?>? = ArrayList()

    var saveTemplateAdapter: ManagePreTempAdapter? = null
    var saveTemplateMobAdapter: ManagePreTempMobAdapter? = null

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    val header: Headers? = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()

    var bundle: Bundle = Bundle()

    var addnewstatus: Boolean? = false

    var removed_details: ArrayList<RemovedDetail> = ArrayList()

    var new_details: ArrayList<NewDetail> = ArrayList()

    var existing_details: ArrayList<ExistingDetail> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (dialog.window != null)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_template_edit_prescription,
                container,
                false
            )
        viewModel = ManagePreTempAddEditviewModelFactory(
            requireActivity().application
        )
            .create(ManagePreTempAddEditviewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        removed_details.clear()

        new_details.clear()

        existing_details.clear()

        IsTablet = resources.getBoolean(R.bool.isTablet)

        customProgressDialog = CustomProgressDialog(requireContext())

/*
        viewModel!!.progress.observe(requireActivity(),
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })
*/


        val args = arguments



        if (args == null) {

            //Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()

            IsTablet = resources.getBoolean(R.bool.isTablet)

            if (IsTablet) {

                saveTemplateAdapter = ManagePreTempAdapter(
                    ArrayList<DrugDetail>(),
                    requireActivity()
                )

                binding?.zeroStockRecyclerView!!.adapter = saveTemplateAdapter

            } else {


                saveTemplateMobAdapter = ManagePreTempMobAdapter(
                    ArrayList<DrugDetail>(),
                    requireActivity()
                )

                binding?.zeroStockRecyclerView!!.adapter = saveTemplateMobAdapter


            }
            binding!!.save.visibility = View.VISIBLE

            binding!!.updateTemp.visibility = View.GONE


        } else {

            IsTablet = resources.getBoolean(R.bool.isTablet)

            status = false

            fromStats = args.getString("from") == "Add"



            if (fromStats!!) {


                if (IsTablet) {

                    saveTemplateAdapter = ManagePreTempAdapter(
                        ArrayList<DrugDetail>(),
                        requireActivity()
                    )

                    binding?.zeroStockRecyclerView!!.adapter = saveTemplateAdapter

                } else {


                    saveTemplateMobAdapter = ManagePreTempMobAdapter(
                        ArrayList<DrugDetail>(),
                        requireActivity()
                    )

                    binding?.zeroStockRecyclerView!!.adapter = saveTemplateMobAdapter


                    var getData =
                        args.getParcelableArrayList<PrescriptionListData>(AppConstants.RESPONSECONTENT)

                    var sendData: ArrayList<DrugDetail> = ArrayList()

                    for (i in getData!!.indices) {

                        sendData.add(

                            DrugDetail(

                                drug_id = getData[i].test_master_id!!,
                                drug_name = getData[i].itemAppendString!!,
                                drug_route_id = getData[i].selectRouteID,
                                drug_route_name = getData[i].drug_route_name ?: "",
                                drug_frequency_id = getData[i].selecteFrequencyID,
                                drug_frequency_name = getData[i].drug_frequency_name ?: "",
                                drug_duration = getData[i].duration!!.toInt(),
                                drug_period_id = getData[i].PrescriptiondurationPeriodId,
                                drug_period_code = getData[i].drug_period_code ?: "",
                                is_active = true,
                                drug_instruction_id = getData[i].selectInvestID


                            )

                        )


                    }


                    saveTemplateMobAdapter?.setData(sendData)

                }


                binding!!.save.visibility = View.VISIBLE

                binding!!.updateTemp.visibility = View.GONE


            } else {

                favouriteData = args.getParcelable(AppConstants.RESPONSECONTENT)

                Log.i("", "" + favouriteData)


                binding!!.edtName.setText(favouriteData!!.temp_details.template_name)

                binding!!.autoCompleteTextViewDispalyName.setText(favouriteData!!.temp_details.display_order.toString())


                if (IsTablet) {
                    saveTemplateAdapter = ManagePreTempAdapter(
                        favouriteData!!.drug_details,
                        requireActivity()
                    )

                    binding?.zeroStockRecyclerView!!.adapter = saveTemplateAdapter

                } else {
                    saveTemplateMobAdapter = ManagePreTempMobAdapter(
                        favouriteData!!.drug_details,
                        requireActivity()
                    )

                    binding?.zeroStockRecyclerView!!.adapter = saveTemplateMobAdapter

                }
                binding!!.save.visibility = View.GONE

                binding!!.updateTemp.visibility = View.VISIBLE

            }


        }



        binding?.closeImageView?.setOnClickListener {

            dialog?.dismiss()

        }

        binding!!.cancel.setOnClickListener {

            dialog?.dismiss()

        }


        viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)


        binding?.autoCompleteTextViewDrugName?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                Log.i("textsixe", "" + s.length)

                if (s.length > 2 && s.length < 5) {

                    Log.i("textsixe", "" + s.length)


                    viewModel?.getSearchDetails(
                        getSearchRetrofitCallback,
                        facility_id,
                        s.toString()
                    )


                }
            }
        })

        binding?.clear?.setOnClickListener {


            binding?.autoCompleteTextViewDrugName?.setText("")

            binding?.autoCompleteTextViewDuration?.setText("")

            binding?.edtName?.setText("")
            binding?.edtDescription?.setText("")
            binding?.autoCompleteTextViewDispalyName?.setText("")
            binding?.autoCompleteTextViewDrugName?.setText("")
            binding?.autoCompleteTextViewDuration?.setText("")

            binding?.routeSpinner?.setSelection(0)
            binding?.frequencySpinner?.setSelection(0)
            binding?.PeriodSpinner?.setSelection(0)
            binding?.instructionSpinner?.setSelection(0)

        }

        binding!!.addTemp.setOnClickListener {

            val send_data = DrugDetail()

            binding!!.autoCompleteTextViewDrugName.isEnabled = true

            if (status!!) {

                if (test_uuid != 0) {

                    if (!binding!!.autoCompleteTextViewDuration.text.toString().isNullOrEmpty()) {


                        if (!binding!!.edtName.text.toString().isNullOrEmpty()) {


                            if (!binding!!.autoCompleteTextViewDispalyName.text.toString()
                                    .isNullOrEmpty()
                            ) {

                                send_data.drug_id = test_uuid!!

                                send_data.drug_name =
                                    binding!!.autoCompleteTextViewDrugName.text.toString()

                                send_data.drug_route_id = selectRouteUuid

                                send_data.drug_frequency_id = selectFrequencyUuid

                                send_data.drug_duration =
                                    binding!!.autoCompleteTextViewDuration.text.toString().toInt()

                                send_data.drug_period_id = selectDurationUuid

                                send_data.drug_instruction_id = selectInstructionUuid


                                if (IsTablet)
                                    saveTemplateAdapter!!.addRow(send_data)
                                else
                                    saveTemplateMobAdapter!!.addRow(send_data)





                                binding?.autoCompleteTextViewDrugName?.setText("")

                                binding?.StrengthSpinner?.setText("")
                                binding?.autoCompleteTextViewDuration?.setText("")
                                binding?.routeSpinner?.setSelection(0)
                                binding?.frequencySpinner?.setSelection(0)
                                binding?.PeriodSpinner?.setSelection(0)
                                binding?.instructionSpinner?.setSelection(0)
                            } else {


                                binding!!.autoCompleteTextViewDispalyName.error =
                                    "Please Enter Display Order"

                                Toast.makeText(
                                    this.context,
                                    "Please Enter Display Order",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        } else {

                            binding!!.edtName.error = "Please Enter Template Name"

                            Toast.makeText(
                                this.context,
                                "Please Enter Template Name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {

                        binding!!.autoCompleteTextViewDuration.error = "Please Enter Duration"

                        Toast.makeText(this.context, "Please Enter Duration", Toast.LENGTH_SHORT)
                            .show()

                    }


                } else {

                    Toast.makeText(
                        this.context,
                        "Please select Valid Drug Name",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            } else {

                if (test_uuid != 0) {


                    if (exitingStatus!!) {


                        send_data.drug_id = test_uuid!!

                        send_data.drug_name = binding!!.autoCompleteTextViewDrugName.text.toString()

                        send_data.drug_route_id = selectRouteUuid

                        send_data.drug_frequency_id = selectFrequencyUuid

                        send_data.drug_duration =
                            binding!!.autoCompleteTextViewDuration.text.toString().toInt()

                        send_data.drug_period_id = selectDurationUuid

                        send_data.drug_instruction_id = selectInstructionUuid

                        if (IsTablet)
                            saveTemplateAdapter!!.updateRow(send_data, exitiingPisition)
                        else {
                            saveTemplateMobAdapter!!.updateRow(send_data, exitiingPisition)
                            saveTemplateMobAdapter?.setCollageClear()

                        }

                        val existingDetail = ExistingDetail()

                        existingDetail.template_master_uuid =
                            favouriteData!!.temp_details.template_id
                        existingDetail.drug_id = test_uuid!!
                        existingDetail.drug_route_uuid = selectRouteUuid.toString()
                        existingDetail.drug_frequency_uuid = selectFrequencyUuid
                        existingDetail.drug_duration =
                            binding!!.autoCompleteTextViewDuration.text.toString().toInt()
                        existingDetail.drug_period_uuid = selectDurationUuid
                        existingDetail.drug_instruction_uuid = selectInstructionUuid
                        existingDetail.display_order =
                            binding!!.autoCompleteTextViewDispalyName.text.toString().toInt()
                        existingDetail.quantity = 14

                        existing_details.add(existingDetail)

                        exitiingPisition = 0
                        exitingStatus = false

                        binding!!.autoCompleteTextViewDrugName.text.clear()

                        binding!!.autoCompleteTextViewDuration.text.clear()

                        test_uuid = 0

                        viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)
                    } else {
                        send_data.drug_id = test_uuid!!

                        send_data.drug_name = binding!!.autoCompleteTextViewDrugName.text.toString()

                        send_data.drug_route_id = selectRouteUuid

                        send_data.drug_frequency_id = selectFrequencyUuid

                        send_data.drug_duration =
                            binding!!.autoCompleteTextViewDuration.text.toString().toInt()

                        send_data.drug_period_id = selectDurationUuid

                        send_data.drug_instruction_id = selectInstructionUuid

                        if (IsTablet)
                            saveTemplateAdapter!!.addRow(send_data)
                        else {
                            saveTemplateMobAdapter!!.addRow(send_data)
                            saveTemplateMobAdapter?.setCollageClear()

                        }

                        // for api

                        val newDetail = NewDetail()
                        newDetail.template_master_uuid = favouriteData!!.temp_details.template_id
                        newDetail.drug_id = test_uuid!!
                        newDetail.drug_route_uuid = selectRouteUuid.toString()
                        newDetail.drug_frequency_uuid = selectFrequencyUuid.toString()
                        newDetail.drug_duration =
                            binding!!.autoCompleteTextViewDuration.text.toString()
                        newDetail.drug_period_uuid = selectDurationUuid.toString()
                        newDetail.drug_instruction_uuid = selectInstructionUuid.toString()
                        newDetail.display_order =
                            binding!!.autoCompleteTextViewDispalyName.text.toString().toInt()
                        newDetail.quantity = 14

                        new_details.add(newDetail)

                        binding!!.autoCompleteTextViewDrugName.text.clear()

                        binding!!.autoCompleteTextViewDuration.text.clear()

                        test_uuid = 0

                        viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)
                    }

                    binding!!.autoCompleteTextViewDrugName.text.clear()

                    binding!!.autoCompleteTextViewDuration.text.clear()

                    test_uuid = 0


                    viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)

                } else {


                    Toast.makeText(
                        this.context,
                        "Please select Valid Drug Name",
                        Toast.LENGTH_SHORT
                    ).show()

                }


            }

        }

        binding!!.save.setOnClickListener {


            val get_all_data =
                if (IsTablet)
                    saveTemplateAdapter!!.getAll()
                else
                    saveTemplateMobAdapter!!.getAll()

            if (get_all_data.size != null) {

                if (!binding!!.edtName.text.toString().isNullOrEmpty()) {


                    if (!binding!!.autoCompleteTextViewDispalyName.text.toString()
                            .isNullOrEmpty()
                    ) {

                        val saveTemplateRequestModel = SaveTemplateRequestModel()

                        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

                        saveTemplateRequestModel.headers.name = binding!!.edtName.text.toString()
                        saveTemplateRequestModel.headers.description =
                            binding!!.edtDescription.text.toString()
                        saveTemplateRequestModel.headers.display_order =
                            binding!!.autoCompleteTextViewDispalyName.text.toString()
                        saveTemplateRequestModel.headers.template_type_uuid = 1
                        saveTemplateRequestModel.headers.user_uuid =
                            userDataStoreBean?.uuid!!.toString()
                        saveTemplateRequestModel.headers.facility_uuid = "$facility_id"
                        saveTemplateRequestModel.headers.department_uuid = "$deparment_UUID"

                        detailsList.clear()

                        for (i in get_all_data.indices) {

                            val details: Detail = Detail()

                            details.item_master_uuid = get_all_data[i].drug_id
                            details.drug_route_uuid = get_all_data[i].drug_route_id
                            details.drug_frequency_uuid = get_all_data[i].drug_frequency_id
                            details.duration = get_all_data[i].drug_duration
                            details.duration_period_uuid = get_all_data[i].drug_period_id
                            details.drug_instruction_uuid = get_all_data[i].drug_instruction_id
                            details.quantity = 1

                            detailsList.add(details)

                        }

                        saveTemplateRequestModel.details = detailsList


                        viewModel?.saveTemplate(
                            saveTemplateRetrofitCallback,
                            saveTemplateRequestModel
                        )
                    } else {


                        binding!!.autoCompleteTextViewDispalyName.error =
                            "Please Enter Display Order"

                        Toast.makeText(
                            this.context,
                            "Please Enter Display Order",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                } else {

                    binding!!.edtName.error = "Please Enter Template Name"

                    Toast.makeText(this.context, "Please Enter Template Name", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(this.context, "Please Add any one Drug", Toast.LENGTH_SHORT).show()
            }

        }

        binding!!.updateTemp.setOnClickListener {

            val updateRequestModel = UpdateRequestModel()


            updateRequestModel.headers.template_id = favouriteData!!.temp_details.template_id


            updateRequestModel.headers.name = binding!!.edtName.text.toString()
            updateRequestModel.headers.description = binding!!.edtDescription.text.toString()
            updateRequestModel.headers.display_order =
                binding!!.autoCompleteTextViewDispalyName.text.toString().toInt()

            updateRequestModel.headers.facility_uuid = facility_id.toString()
            updateRequestModel.headers.template_type_uuid = 1

            updateRequestModel.existing_details = existing_details

            updateRequestModel.new_details = new_details

            updateRequestModel.removed_details = removed_details


            viewModel!!.updateTemplate(updateTemplateRetrofitCallback, updateRequestModel)


        }


        binding?.routeSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectRouteUuid = 0

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectRouteUuid = 0
                    } else {

                        val itemValue = parent!!.getItemAtPosition(pos).toString()

                        selectRouteUuid =
                            routeSpinnerList.filterValues { it == itemValue }.keys.toList()[0]
                    }

                    Log.e("seleceted", selectRouteUuid.toString())
                }
            }



        binding?.frequencySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectFrequencyUuid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectFrequencyUuid = 0
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectFrequencyUuid =
                            frequencySpinnerList.filterValues { it == itemValue }.keys.toList()[0]

                    }

                    Log.e("Instructionseleceted", selectFrequencyUuid.toString())
                }
            }


        binding?.instructionSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectInstructionUuid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectInstructionUuid = 0
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectInstructionUuid =
                            instructionSpinnerList.filterValues { it == itemValue }.keys.toList()[0]
                    }
                    Log.e("Instructionseleceted", selectInstructionUuid.toString())
                }
            }

        binding!!.autoCompleteTextViewDrugName.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as SearchPrescription?
            binding!!.autoCompleteTextViewDrugName.setText(selectedPoi?.name)

            addnewstatus = true

            test_uuid = selectedPoi!!.uuid


        }



        binding?.PeriodSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectDurationUuid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectDurationUuid = 0
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        selectDurationUuid =
                            durationSpinnerList.filterValues { it == itemValue }.keys.toList()[0]

                    }
                    Log.e("periodseleceted", selectDurationUuid.toString())
                }
            }


        if (IsTablet) {
            saveTemplateAdapter!!.setOnDeleteClickListener(object :
                ManagePreTempAdapter.OnDeleteClickListener {
                override fun onDeleteClick(responseContent: DrugDetail?, position: Int) {
                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                    }
                    drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "${drugNmae.text} '" + responseContent?.drug_name + "' Record ?"

                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                    yesBtn.setOnClickListener {

                        if (status!!) {

                            if (IsTablet)
                                saveTemplateAdapter!!.deleteRow(position)
                            else
                                saveTemplateMobAdapter!!.deleteRow(position)

                        } else {

                            val removedDetail = RemovedDetail()

                            removedDetail.template_uuid = favouriteData!!.temp_details.template_id

                            removedDetail.template_details_uuid =
                                responseContent!!.template_details_uuid

                            removed_details.add(removedDetail)

                            if (IsTablet)
                                saveTemplateAdapter!!.deleteRow(position)
                            else
                                saveTemplateMobAdapter!!.deleteRow(position)


                        }

                        toast("${responseContent?.drug_name} Deleted Successfully")
                        customdialog!!.dismiss()
                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()
                    }
                    customdialog!!.show()


                }
            })

        } else {
            saveTemplateMobAdapter!!.setOnDeleteClickListener(object :
                ManagePreTempMobAdapter.OnDeleteClickListener {
                override fun onDeleteClick(responseContent: DrugDetail?, position: Int) {
                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                    }
                    drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "${drugNmae.text} '" + responseContent?.drug_name + "' Record ?"

                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                    yesBtn.setOnClickListener {

                        if (status!!) {

                            saveTemplateMobAdapter!!.deleteRow(position)

                        } else {

                            val removedDetail = RemovedDetail()

                            removedDetail.template_uuid = favouriteData!!.temp_details.template_id

                            removedDetail.template_details_uuid =
                                responseContent!!.template_details_uuid

                            removed_details.add(removedDetail)

                            saveTemplateMobAdapter!!.deleteRow(position)


                        }
                        customdialog!!.dismiss()
                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()
                    }
                    customdialog!!.show()


                }
            })
        }

        if (IsTablet) {
            saveTemplateAdapter!!.setOnViewClickListener(object :
                ManagePreTempAdapter.OnViewClickListener {

                override fun onViewClick(responseContent: DrugDetail?, position: Int) {


                    if (!status!!) {

                        exitingStatus = true

                        exitiingPisition = position

                        binding!!.autoCompleteTextViewDrugName.setText(responseContent!!.drug_name)

                        binding!!.autoCompleteTextViewDrugName.isEnabled = false

                        test_uuid = responseContent.drug_id

                        binding!!.autoCompleteTextViewDuration.setText(responseContent.drug_duration.toString())

                        selectRouteUuid = responseContent.drug_route_id

                        binding!!.routeSpinner.setSelection(hashrouteSpinnerList[selectRouteUuid]!!)

                        selectFrequencyUuid = responseContent.drug_frequency_id

                        binding!!.frequencySpinner.setSelection(hashfrequencySpinnerList[selectFrequencyUuid]!!)

                        selectDurationUuid = responseContent.drug_period_id

                        binding!!.PeriodSpinner.setSelection(hashdurationSpinnerList[selectDurationUuid]!!)

                        selectInstructionUuid = responseContent.drug_instruction_id

                        binding!!.instructionSpinner.setSelection(hashinstructionSpinnerList[selectInstructionUuid]!!)


                    }


                }
            })

        } else {
            saveTemplateMobAdapter!!.setOnViewClickListener(object :
                ManagePreTempMobAdapter.OnViewClickListener {

                override fun onViewClick(responseContent: DrugDetail?, position: Int) {

                    if (!status!!) {

                        exitingStatus = true

                        exitiingPisition = position

                        binding!!.autoCompleteTextViewDrugName.setText(responseContent!!.drug_name)

                        binding!!.autoCompleteTextViewDrugName.isEnabled = false

                        test_uuid = responseContent.drug_id

                        binding!!.autoCompleteTextViewDuration.setText(responseContent.drug_duration.toString())

                        selectRouteUuid = responseContent.drug_route_id

                        binding!!.routeSpinner.setSelection(hashrouteSpinnerList[selectRouteUuid]!!)

                        selectFrequencyUuid = responseContent.drug_frequency_id

                        binding!!.frequencySpinner.setSelection(hashfrequencySpinnerList[selectFrequencyUuid]!!)

                        selectDurationUuid = responseContent.drug_period_id

                        binding!!.PeriodSpinner.setSelection(hashdurationSpinnerList[selectDurationUuid]!!)

                        selectInstructionUuid = responseContent.drug_instruction_id

                        binding!!.instructionSpinner.setSelection(hashinstructionSpinnerList[selectInstructionUuid]!!)

                    }

                }
            })

            saveTemplateMobAdapter?.setOnRefreshClickListener(object :
                ManagePreTempMobAdapter.OnRefreshClickListener {
                override fun onRefreshClick() {

                    exitingStatus = false
//                    clearList()


                    binding?.autoCompleteTextViewDrugName?.setText("")

                    binding?.autoCompleteTextViewDuration?.setText("")

                    binding?.edtDescription?.setText("")
                    binding?.autoCompleteTextViewDrugName?.setText("")
                    binding?.autoCompleteTextViewDuration?.setText("")
                    //binding?.StrengthSpinner?.setText("")

                    binding?.routeSpinner?.setSelection(0)
                    binding?.frequencySpinner?.setSelection(0)
                    binding?.PeriodSpinner?.setSelection(0)
                    binding?.instructionSpinner?.setSelection(0)


                }
            })
        }
        return binding!!.root
    }

    private fun toast(s: String) {

        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    fun setAdapter(
        responseContents: ArrayList<SearchPrescription>
    ) {
        val responseContentAdapter = PrescriptionSearchAdapter(
            this.requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.autoCompleteTextViewDrugName.threshold = 1
        binding!!.autoCompleteTextViewDrugName.setAdapter(responseContentAdapter)
        binding!!.autoCompleteTextViewDrugName.showDropDown()

    }

    fun allData() {

        val args = arguments
        if (args == null) {
            status = true


        } else {


        }


    }


    val saveTemplateRetrofitCallback = object : RetrofitCallback<SaveTemplateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SaveTemplateResponseModel>?) {


            Log.i("", "" + responseBody!!.body()!!.message)

            Log.i("", "" + responseBody.body()!!.message)

            if (IsTablet)
                saveTemplateAdapter!!.clearAll()
            else
                saveTemplateMobAdapter!!.clearAll()


            binding!!.edtName.setText("")

            binding!!.edtDescription.setText("")

            binding!!.autoCompleteTextViewDispalyName.text.clear()

            binding!!.autoCompleteTextViewDrugName.text.clear()

            binding!!.autoCompleteTextViewDuration.text.clear()

            Log.i("", "" + responseBody.body()!!.message)


            toast("Template Added Successfully")

//            if(!fromStats!!)
            callbackfavourite!!.onRefresh()

            dialog!!.dismiss()

        }

        override fun onBadRequest(errorBody: Response<SaveTemplateResponseModel>?) {

            val gson = GsonBuilder().create()
            val responseModel: ChiefComplaintFavAddresponseModel
            var mError = ErrorAPIClass()
            try {
                mError =
                    gson.fromJson(errorBody!!.errorBody()!!.string(), ErrorAPIClass::class.java)
                Toast.makeText(
                    context,
                    mError.message,
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: IOException) { // handle failure to read error
            }

        }

        override fun onServerError(response: Response<*>?) {
            Log.i("", "server")
        }

        override fun onUnAuthorized() {
        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8

        }

    }


    val updateTemplateRetrofitCallback = object : RetrofitCallback<UpdateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<UpdateResponseModel>?) {


            Log.i("", "" + responseBody!!.body()!!.message)

            if (IsTablet)
                saveTemplateAdapter!!.clearAll()
            else
                saveTemplateMobAdapter!!.clearAll()

            callbackfavourite!!.onRefresh()

            binding!!.edtName.setText("")

            binding!!.edtDescription.setText("")

            binding!!.autoCompleteTextViewDispalyName.text.clear()

            binding!!.autoCompleteTextViewDrugName.text.clear()

            binding!!.autoCompleteTextViewDuration.text.clear()

            Toast.makeText(requireContext(), responseBody.body()!!.message, Toast.LENGTH_LONG)
                .show()


            dialog!!.dismiss()
        }

        override fun onBadRequest(errorBody: Response<UpdateResponseModel>?) {

            val gson = GsonBuilder().create()
            val responseModel: UpdateResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    UpdateResponseModel::class.java
                )
                Log.i("", "BADREQ" + responseModel.message)
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            Log.i("", "server")
        }

        override fun onUnAuthorized() {
        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8

        }

    }

    val getSearchRetrofitCallback = object : RetrofitCallback<SearchPrescriptionResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SearchPrescriptionResponseModel>?) {
            Log.i("res", "" + responseBody?.body()?.message)

            Log.i("res", "" + responseBody?.body()?.responseContents)


            setAdapter(responseBody?.body()?.responseContents!!)


        }


        override fun onBadRequest(errorBody: Response<SearchPrescriptionResponseModel>?) {

        }

        override fun onServerError(response: Response<*>?) {
        }

        override fun onUnAuthorized() {
        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8

        }

    }

    val getRouteRetrofitCallback = object : RetrofitCallback<PrescriptionRoutResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<PrescriptionRoutResponseModel>?) {
            Log.i("res", "" + responseBody?.body()?.message)


            routeList?.add(PrescriptionRouteResponseContent())
            routeList?.addAll(responseBody?.body()?.responseContents!!)
            setRouteSpinnerValue(routeList)

            viewModel?.getFrequencyDetails(getFrequencyRetrofitCallback, facility_id)

        }


        override fun onBadRequest(errorBody: Response<PrescriptionRoutResponseModel>?) {

        }

        override fun onServerError(response: Response<*>?) {
        }

        override fun onUnAuthorized() {
        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8

        }

    }
    val getFrequencyRetrofitCallback =
        object : RetrofitCallback<PrescriptionFrequencyResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<PrescriptionFrequencyResponseModel>?) {
                Log.i("res", "" + responseBody?.body()?.message)


                //   viewModel?.getInstructionDetails(getInstructionRetrofitCallback, facility_id)

                frequencyList?.clear()

                frequencyList?.add(
                    PrescriptionFrequencyresponseContent(
                        uuid = 0,
                        name = "Select Frequency"
                    )
                )
                frequencyList?.addAll(responseBody?.body()?.responseContents!!)
                setFrequencySpinnerValue(frequencyList)
                viewModel?.getPrescriptionDuration(getSaveDurationRetrofitCallBack, facility_id)


            }

            override fun onBadRequest(errorBody: Response<PrescriptionFrequencyResponseModel>?) {
            }

            override fun onServerError(response: Response<*>?) {
            }

            override fun onUnAuthorized() {

            }

            override fun onForbidden() {
            }

            override fun onFailure(s: String?) {
            }

            override fun onEverytime() {

                viewModel!!.progress.value = 8
            }

        }
    val getSaveDurationRetrofitCallBack =
        object : RetrofitCallback<PrescriptionDurationResponseModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionDurationResponseModel>) {
                Log.i("res", "" + response.body()?.message)

                periodList?.add(PrescriptionDurationResponseContent())
                periodList?.addAll(response.body()!!.responseContents)
                setdurationSpinnerValue(periodList)

                viewModel?.getInstructionDetails(getInstructionRetrofitCallback, facility_id)


            }

            override fun onBadRequest(response: Response<PrescriptionDurationResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionDurationResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionDurationResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }
    val getInstructionRetrofitCallback = object : RetrofitCallback<PresInstructionResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<PresInstructionResponseModel>?) {
            Log.i("res", "" + responseBody?.body()?.message)

            instructionList?.add(PresInstructionResponseContent())
            instructionList?.addAll(responseBody?.body()?.responseContents!!)
            setInsttructionSpinnerValue(instructionList)

            allData()
        }

        override fun onBadRequest(errorBody: Response<PresInstructionResponseModel>?) {
        }

        override fun onServerError(response: Response<*>?) {
        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {
        }

        override fun onFailure(s: String?) {
        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8
        }

    }
/*
    private fun setInsttructionSpinnerValue(responseContents: List<PresInstructionResponseContent>?) {

        routeNamesMap = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()


    }*/


    fun setRouteSpinnerValue(responseContents: List<PrescriptionRouteResponseContent?>?) {


        routeSpinnerList = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()


        hashrouteSpinnerList.clear()

        for (i in responseContents.indices) {

            hashrouteSpinnerList[responseContents[i]!!.uuid] = i
        }

        if (IsTablet)
            saveTemplateAdapter!!.setRote(routeSpinnerList)
        else
            saveTemplateMobAdapter!!.setRote(routeSpinnerList)

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            routeSpinnerList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.routeSpinner!!.adapter = adapter


    }

    fun setFrequencySpinnerValue(responseContents: List<PrescriptionFrequencyresponseContent?>?) {


        var ListData = responseContents


        var dummy: PrescriptionFrequencyresponseContent = PrescriptionFrequencyresponseContent(
            uuid = 0,
            name = "Select Frequency"
        )

        frequencySpinnerList.clear()

        var AddData: ArrayList<PrescriptionFrequencyresponseContent?> = ArrayList()


        AddData.add(dummy)

        AddData.addAll(responseContents!!)

        frequencySpinnerList = AddData.map { it?.uuid!! to it.name }.toMap().toMutableMap()


        hashfrequencySpinnerList.clear()

        for (i in responseContents.indices) {

            this.hashfrequencySpinnerList[responseContents[i]!!.uuid!!] = i
        }

        if (IsTablet)
            saveTemplateAdapter!!.setfrequencySpinnerList(frequencySpinnerList)
        else
            saveTemplateMobAdapter!!.setfrequencySpinnerList(frequencySpinnerList)

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            frequencySpinnerList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.frequencySpinner!!.adapter = adapter

    }

    fun setInsttructionSpinnerValue(responseContents: List<PresInstructionResponseContent?>?) {

        instructionSpinnerList =
            responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()

        hashinstructionSpinnerList.clear()
        for (i in responseContents.indices) {

            hashinstructionSpinnerList[responseContents[i]!!.uuid] = i
        }

        if (IsTablet)
            saveTemplateAdapter!!.setinstructionSpinnerList(instructionSpinnerList)
        else
            saveTemplateMobAdapter!!.setinstructionSpinnerList(instructionSpinnerList)
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            instructionSpinnerList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.instructionSpinner!!.adapter = adapter

    }

    fun setdurationSpinnerValue(responseContents: List<PrescriptionDurationResponseContent?>?) {

        durationSpinnerList =
            responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()

        hashdurationSpinnerList.clear()

        for (i in responseContents.indices) {

            hashdurationSpinnerList[responseContents[i]!!.uuid] = i
        }

        if (IsTablet)
            saveTemplateAdapter!!.setdurationSpinnerList(durationSpinnerList)
        else
            saveTemplateMobAdapter!!.setdurationSpinnerList(durationSpinnerList)

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            durationSpinnerList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.PeriodSpinner!!.adapter = adapter

    }


    fun setOnTempRefreshListener(callback: OnTempRefreshListener) {
        callbackfavourite = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnTempRefreshListener {
        fun onTempID(position: Int)
        fun onRefresh()
    }


}