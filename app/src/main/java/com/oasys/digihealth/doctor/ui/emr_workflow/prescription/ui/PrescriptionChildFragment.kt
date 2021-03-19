package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.hide
import com.oasys.digihealth.doctor.component.extention.isvisible
import com.oasys.digihealth.doctor.component.extention.show
import com.oasys.digihealth.doctor.component.extention.slideDown
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentPrescriptionChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.SaveTemplateDialogFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.ManagePreTempAddEdit
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.PrescriptionViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.PrescriptionViewModelFactory
import com.oasys.digihealth.doctor.ui.nursedesk.nurse_desk_discharge_summary.ui.NurseDeskDischargeSummaryChildFragment
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*

class PrescriptionChildFragment : Fragment(), PrescriptionFavouriteFragment.FavClickedListener,
    PrescriptionTempleteFragment.TempleteClickedListener,
    PrevPrescriptionFragment.PrevClickedListener, CommentDialogFragment.CommandClickedListener,
    SaveTemplateDialogFragment.LabChiefComplaintListener,
    ManagePreTempAddEdit.OnTempRefreshListener {
    private var arrayItemData: ArrayList<FavouritesModel?>? = null

    private var saveData: ArrayList<PrescriptionListData?>? = null
    private var facility_id: Int? = 0
    private var prescriptionId: Int? = 0

    private var injection_room_uuid: Int? = 0

    lateinit var drugNmae: TextView
    private var binding: FragmentPrescriptionChildBinding? = null
    private var customdialog: Dialog? = null
    private var viewModel: PrescriptionViewModel? = null
    var prescriptionAdapter: PrescriptionAdapter? = null
    var prescriptionTabAdapter: PrescriptionTabAdapter? = null
    var prescriptionMobileAdapter: PrescriptionMobileAdapter? = null
    private var utils: Utils? = null

    private var isPrint: Boolean = false
    private var IsTablet: Boolean = false
    private var printUUid: Int? = null

    var downloadPDFTask: DownloadPDFTask? = null

    private var destinationFile: File? = null
    private var destinationPDFFile: File? = null

    // create boolean for fetching data
    private var isViewShown = false
    val header: Header? = Header()
    val emrPrescriptionRequestModel: emr_prescription_postalldata_requestmodel? =
        emr_prescription_postalldata_requestmodel()
    private var responseContents: String? = ""
    var mCallbackPresFavFragment: ClearFavParticularPositionListener? = null
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    var mCallbackPrevTemplateFragment: ClearTemplateParticularPositionListener? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var customProgressDialog: CustomProgressDialog? = null
    val detailsList = ArrayList<Detail>()
    private var selectedSearchPosition = -1

    private var PageType: Int? = 0

    private var patient_UUID: Int? = 0
    private var department_UUID: Int? = 0
    private var encounter_Type: Int? = 0
    private var encounter_uuid: Int? = 0
    private var StoreMasterID: Int? = 0
    private var doctor_en_uuid: Int? = 0

    var mCallbackLabTemplateFragment: ClearTemplateParticularPositionListener? = null

    var durationAdapter: PrescriptionDurationAdapter? = null


    var InjectionList: ArrayList<InjectionDepartment> = ArrayList()
    var InstructionList: ArrayList<PresInstructionResponseContent> = ArrayList()
    var RouteList: ArrayList<PrescriptionRouteResponseContent> = ArrayList()
    var freqencyList: ArrayList<PrescriptionFrequencyresponseContent> = ArrayList()

    var durationArrayList: ArrayList<PrescriptionDurationResponseContent> = ArrayList()

    private var instructionMap = mutableMapOf<Int, String>()
    private var routeNamesMap = mutableMapOf<Int, String>()
    private var frequencyMap = mutableMapOf<Int, String>()

    var AddNew = PrescriptionNewData()

    var isModify: Boolean = false
    var IsUpdateList: Boolean = false
    var UpdatePosition: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_prescription_child,
                container,
                false
            )
        viewModel = PrescriptionViewModelFactory(
            requireActivity().application
        ).create(PrescriptionViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this

        utils = Utils(requireContext())

        val args = arguments
        if (args == null) {

        } else {

            PageType = args.getInt(AppConstants.RESPONSETYPE)

            Log.i("TypeFRagment", "" + PageType)

            fetchData()

        }
        return binding!!.root
    }

    private fun fetchData() {
        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val labMasterTypeUUID = appPreferences?.getInt(AppConstants.LAB_MASTER_TYPE_ID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)

        val storename = appPreferences?.getString(AppConstants.STOREMASTER_NAME)

        StoreMasterID = appPreferences?.getInt(AppConstants.STOREMASTER_UUID)

        trackPrescriptionAnalyticsVisit(utils!!.getEncounterType())

        appPreferences?.saveInt(AppConstants.PRESCRIPTIONTYPE, this.PageType!!)

        //val encounter_doctoruuid = appPreferences?.getInt(AppConstants?.ENCOUNTER_DOCTOR_UUID)
        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }


        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        binding?.drawerLayout!!.openDrawer(GravityCompat.END)

        IsTablet = resources.getBoolean(R.bool.isTablet)

        if (StoreMasterID == null || StoreMasterID == 0) {

            Toast.makeText(
                context,
                "Store Name Not Allocated for Login Department",
                Toast.LENGTH_SHORT
            ).show()

        }

        setupViewPager(binding?.viewpager!!)
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)

        if (IsTablet) {

            viewModel?.getInstructionDetails(getInstructionRetrofitCallback, facility_id)

            viewModel?.getInjectionDetails(getInjectionRetrofitCallback, facility_id)

            if (storename != null && storename != "") {

                binding!!.storeName!!.text = "Store name: $storename"

            } else {

                binding!!.storeName!!.text = "Store name:"
            }
//


            setTabletAdapter()

            setADapter()
            //   val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


            viewModel?.getPrescriptionDuration(getPrescriptionDurationRetrofitCallBack, facility_id)

            viewModel?.getFrequencyDetails(getFrequencyRetrofitCallback, facility_id)

            viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)

            binding!!.doctorName!!.setText(userDataStoreBean?.title!!.name + userDataStoreBean.first_name)
            binding?.clearCardView?.setOnClickListener {
                mCallbackPresFavFragment?.ClearAllData()
                mCallbackPrevTemplateFragment?.ClearAllData()

                prescriptionTabAdapter?.clearall()
                prescriptionTabAdapter?.addRow(FavouritesModel())

            }


            val next = "<font color='#EE0000'>*</font>"

            binding?.tvAuthorizedSignature?.text = Html.fromHtml("Authorized Signature$next")

            binding?.saveCardView!!.setOnClickListener {

                trackPrescriptionSaveStart(utils!!.getEncounterType())

                arrayItemData = prescriptionTabAdapter?.getItems()

                detailsList.clear()

                if (arrayItemData?.size!! > 1) {

                    viewModel?.getEncounter(
                        facility_id!!,
                        patient_UUID!!,
                        encounter_Type!!,
                        fetchEncounterRetrofitCallBack
                    )

                } else {

                    Toast.makeText(this.context, "Please add any drug", Toast.LENGTH_SHORT).show()
                }
            }


            binding?.print?.setOnClickListener {


                isPrint = true

                trackPrescriptionSaveStart(utils!!.getEncounterType())

                arrayItemData = prescriptionTabAdapter?.getItems()

                detailsList.clear()

                if (arrayItemData?.size!! > 1) {

                    viewModel?.getEncounter(
                        facility_id!!,
                        patient_UUID!!,
                        encounter_Type!!,
                        fetchEncounterRetrofitCallBack
                    )

                } else {

                    Toast.makeText(this.context, "Please add any drug", Toast.LENGTH_SHORT).show()
                }


            }

            binding?.zeroStockCardView?.setOnClickListener {
                val ft = requireActivity().supportFragmentManager.beginTransaction()
                val dialog = ZeroStockDialogFragment()
                dialog.show(ft, "Tag")
            }

            binding?.saveTemplateCardView?.setOnClickListener {


                var getData = prescriptionTabAdapter?.getItems()?.size!!

                if (getData > 1) {

                    //    val ft = requireActivity().supportFragmentManager.beginTransaction()
                    val dialog = SaveTemplateDialogFragment()
                    //    dialog.show(ft, "Tag")
                    val ft = childFragmentManager.beginTransaction()
                    //val dialog = ManageLabFavouriteFragment()
                    val bundle = Bundle()
                    val saveArray = prescriptionTabAdapter!!.getItems()
                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArray)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")

                } else {

                    Toast.makeText(this.context, "Please add any drug", Toast.LENGTH_SHORT).show()
                }


            }

        } else {

            viewModel?.getPrescriptionDuration(getPrescriptionDurationRetrofitCallBack, facility_id)

            viewModel?.getFrequencyDetails(getFrequencyRetrofitCallback, facility_id)

            viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)

            viewModel?.getInstructionDetails(getInstructionRetrofitCallback, facility_id)

            viewModel?.getInjectionDetails(getInjectionRetrofitCallback, facility_id)

            binding?.addMedicineLayout?.setOnClickListener {
                if (binding?.addMedicineDetailLayout?.isvisible()!!) {
                    hideDropDown()
                } else {
                    showDropDown()
                }
            }

            prescriptionMobileAdapter =
                PrescriptionMobileAdapter(
                    requireActivity(), ArrayList()
                )
            binding?.prescriptionListRecyclerView?.adapter = prescriptionMobileAdapter



            binding?.saveCardView!!.setOnClickListener {

                trackPrescriptionSaveStart(utils!!.getEncounterType())

                saveData = prescriptionMobileAdapter?.getItems()


                for (i in saveData!!.indices) {


                    if (saveData!![i]!!.drug_active!!) {

                        var ck = InjectionList.any { it.uuid == saveData!![i]!!.selectInvestID }

                        if (!ck) {

                            toast("Please check all Items")

                        }

                    }

                }



                detailsList.clear()

                if (saveData?.size!! > 0) {

                    viewModel?.getEncounter(
                        facility_id!!,
                        patient_UUID!!,
                        encounter_Type!!,
                        fetchEncounterRetrofitCallBack
                    )

                } else {

                    Toast.makeText(this.context, "Please add any drug", Toast.LENGTH_SHORT).show()
                }
            }

            binding?.saveTemplateCardView?.setOnClickListener {


                var listData = prescriptionMobileAdapter!!.getItems()


                if (listData.size > 0) {

                    val ft = childFragmentManager.beginTransaction()

                    val dialog = ManagePreTempAddEdit()

                    val bundle = Bundle()

                    bundle.putString("from", "Add")

                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, listData)

                    dialog.arguments = bundle

                    dialog.show(ft, "Tag")

                } else {

                    Toast.makeText(this.context, "Please add any drug", Toast.LENGTH_SHORT).show()
                }

            }


            prescriptionMobileAdapter?.setOnRefreshClickListener(object :
                PrescriptionMobileAdapter.OnRefreshClickListener {
                override fun onRefreshClick() {

                    IsUpdateList = false

                    clearList()

                    hideDropDown()

                }
            })

            prescriptionMobileAdapter?.setOnItemClickListener(object :
                PrescriptionMobileAdapter.OnItemClickListener {
                override fun onItemClick(responseContent: PrescriptionListData?, position: Int) {

                    clearList()

                    showDropDown()

                    IsUpdateList = true

                    AddNew = setData(responseContent)

                    UpdatePosition = position
                    binding?.duration?.setText(AddNew.duration)
                    binding?.PresAutoCompleteTextView?.setText(AddNew.itemAppendString)
                    binding?.Strength?.setText(AddNew.Presstrength)
                    durationAdapter?.updateSelectStatus(AddNew.PrescriptiondurationPeriodId)

                    setFreqency(freqencyList)

                    for (i in freqencyList.indices) {
                        if (freqencyList[i].uuid == AddNew.selecteFrequencyID) {

                            binding?.frequencySpinner?.setSelection(i + 1)
                            break

                        }
                    }

                    setRoute(RouteList)
                    for (i in RouteList.indices) {
                        if (RouteList[i].uuid == AddNew.selectRouteID) {
                            binding?.routeSpinner?.setSelection(i + 1)
                            break
                        }
                    }

                    if (AddNew.drug_active!!) {

                        setInjection(InjectionList)
                        for (i in InjectionList.indices) {
                            if (InjectionList[i].uuid == AddNew.selectInvestID) {
                                binding?.instructionSpinner?.setSelection(i + 1)
                                break
                            }
                        }


                    } else {
                        setInstruction(InstructionList)
                        for (i in InstructionList.indices) {
                            if (InstructionList[i].uuid == AddNew.selectInvestID) {
                                binding?.instructionSpinner?.setSelection(i + 1)
                                break
                            }
                        }

                    }

                }
            })

            prescriptionMobileAdapter?.setOnDeleteClickListener(object :
                PrescriptionMobileAdapter.OnDeleteClickListener {
                override fun onDeleteClick(responseContent: PrescriptionListData?, position: Int) {

                    toast("${responseContent!!.drug_name} Deleted Successfully")

                    val check = prescriptionMobileAdapter?.deleteRow(position)

                    if (responseContent.viewPrescriptionstatus == 1) {

                        mCallbackPresFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)

                    } else if (responseContent.viewPrescriptionstatus == 2) {
                        val ischeckIdPresent =
                            mCallbackPresFavFragment!!.drugIdPresentCheck(responseContent.test_master_id!!)
                        if (ischeckIdPresent) {
                            mCallbackPresFavFragment?.clearDataUsingDrugid(responseContent.drug_id!!)
                        }
                        if (check!!) {
                            mCallbackPrevTemplateFragment?.ClearTemplateParticularPosition(
                                responseContent.isTemposition
                            )

                        }
                    }

                }
            })



            prescriptionMobileAdapter?.setOnCommandClickListener(object :
                PrescriptionMobileAdapter.OnCommandClickListener {
                override fun onCommandClick(position: Int, Command: String) {

                    //     val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    val dialog = CommentDialogFragment()

                    val ft = childFragmentManager.beginTransaction()

                    val bundle = Bundle()
                    bundle.putInt("position", position)
                    bundle.putString("commands", Command)

                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")


                }
            })

            binding?.PresAutoCompleteTextView?.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.length > 2 && s.length < 5) {

                        viewModel?.getComplaintSearchResult(
                            s.toString(),
                            getComplaintSearchRetrofitCallBack,
                            facility_id
                        )

                    }
                }
            })

            routeNamesMap.put(0, "")
            instructionMap.put(0, "")
            frequencyMap.put(0, "")


            setDummyInSpinner(binding?.routeSpinner, routeNamesMap)
            setDummyInSpinner(binding?.frequencySpinner, instructionMap)
            setDummyInSpinner(binding?.instructionSpinner, frequencyMap)

            binding?.routeSpinner?.onItemSelectedListener =
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
                        AddNew.selectRouteID =
                            routeNamesMap.filterValues { it == itemValue }.keys.toList()[0]


                        AddNew.drug_route_name = itemValue

                        Log.i(
                            "LabType",
                            "name = " + itemValue + "uuid=" + routeNamesMap.filterValues { it == itemValue }.keys.toList()[0]
                        )
                    }
                }

            binding?.instructionSpinner?.onItemSelectedListener =
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


                        AddNew.selectInvestID =
                            instructionMap.filterValues { it == itemValue }.keys.toList()[0]

                        AddNew.drug_instruction_name = itemValue

                        Log.i(
                            "LabType",
                            "name = " + itemValue + "uuid=" + instructionMap.filterValues { it == itemValue }.keys.toList()[0]
                        )
                    }
                }

            binding?.frequencySpinner?.onItemSelectedListener =
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
                        AddNew.selecteFrequencyID =
                            frequencyMap.filterValues { it == itemValue }.keys.toList()[0]


                        if (freqencyList.isNotEmpty()) {

                            if (pos != 0) {

                                AddNew.perdayquantityPrescription =
                                    freqencyList[pos - 1].perdayquantity.toString()

                            }
                        }
                        AddNew.drug_frequency_name = itemValue

                        Log.i(
                            "LabType",
                            "name = " + itemValue + "uuid=" + frequencyMap.filterValues { it == itemValue }.keys.toList()[0]
                        )
                    }
                }

            binding?.addCardView?.setOnClickListener {

                if (AddNew.test_master_id == 0) {

                    Toast.makeText(
                        requireContext(),
                        "Please enter valid Drug name",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                if (binding?.duration?.text.isNullOrEmpty()) {

                    Toast.makeText(requireContext(), "Please enter Duration", Toast.LENGTH_SHORT)
                        .show()

                    return@setOnClickListener
                }

                if (AddNew.PrescriptiondurationPeriodId == 0) {

                    Toast.makeText(requireContext(), "Please Select Duration", Toast.LENGTH_SHORT)
                        .show()

                    return@setOnClickListener

                }

                if (AddNew.selectRouteID == 0) {

                    Toast.makeText(requireContext(), "Please Select Route", Toast.LENGTH_SHORT)
                        .show()

                    return@setOnClickListener

                }

                if (AddNew.selecteFrequencyID == 0) {

                    Toast.makeText(requireContext(), "Please Select Frequency", Toast.LENGTH_SHORT)
                        .show()

                    return@setOnClickListener

                }

                if (AddNew.selectInvestID == 0) {

                    Toast.makeText(
                        requireContext(),
                        "Please Select Instruction",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener

                }

                if (AddNew.drug_period_name == "") {

                    Toast.makeText(
                        requireContext(),
                        "Please Select Duration Period",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener

                }

                AddNew.duration = binding?.duration?.text.toString()

                when (AddNew.drug_period_name) {

                    "Days" -> {
                        AddNew.drug_quantity =
                            (AddNew.perdayquantityPrescription!!.toDouble() * AddNew.duration!!.toInt()).toString()
                    }
                    "Weeks" -> {


                        AddNew.drug_quantity =
                            (AddNew.perdayquantityPrescription!!.toDouble() * AddNew.duration!!.toInt() * 7).toString()

                    }
                    "Months" -> {


                        AddNew.drug_quantity =
                            (AddNew.perdayquantityPrescription!!.toDouble() * AddNew.duration!!.toInt() * 30).toString()

                    }
                    "Years" -> {

                        AddNew.drug_quantity =
                            (AddNew.perdayquantityPrescription!!.toDouble() * AddNew.duration!!.toInt() * 365).toString()

                    }
                    else -> {

                        AddNew.drug_quantity =
                            (AddNew.perdayquantityPrescription!!.toDouble() * AddNew.duration!!.toInt()).toString()
                    }
                }


                var checkdata = prescriptionMobileAdapter!!.getItems()

                var injId: Int = AddNew.selectInvestID

                for (k in checkdata.indices) {


                    if (AddNew.drug_active!!) {

                        if (checkdata[k]!!.drug_active!!) {

                            var ck = InjectionList.any { it.uuid == checkdata[k]!!.selectInvestID }

                            if (checkdata[k]!!.selectInvestID != 0 && ck) {

                                if (injId != checkdata[k]!!.selectInvestID) {

                                    toast("Injection Room must be same")

                                    return@setOnClickListener
                                }


                            }

                        }

                    }
                }

                if (IsUpdateList) {

                    val sendData = setListData(AddNew)

                    prescriptionMobileAdapter!!.Update(sendData, UpdatePosition!!)


                    IsUpdateList = false

                } else {


                    val sendData = setListData(AddNew)

                    prescriptionMobileAdapter!!.addDummy(sendData)

                }
                clearList()

                binding?.addMedicineDetailLayout?.visibility = View.GONE

            }

            binding?.clearCardView?.setOnClickListener {


                clearList()

            }

            binding?.clearListCardView?.setOnClickListener {


                isModify = false

                mCallbackPresFavFragment?.ClearAllData()
                mCallbackPrevTemplateFragment?.ClearAllData()
                prescriptionMobileAdapter!!.clearall()

                clearList()
            }


        }
    }

    private fun setData(responseContent: PrescriptionListData?): PrescriptionNewData {


        return PrescriptionNewData(

            drug_active = responseContent!!.drug_active,
            drug_duration = responseContent.drug_duration!!,
            drug_code = responseContent.drug_code,
            drug_quantity = responseContent.drug_quantity,
            Presstrength = responseContent.Presstrength,
            drug_frequency_id = responseContent.drug_frequency_id,


            drug_frequency_name = responseContent.drug_frequency_name,
            drug_instruction_code = responseContent.drug_instruction_code,
            drug_instruction_id = responseContent.drug_instruction_id,
            drug_instruction_name = responseContent.drug_instruction_name,

            drug_id = responseContent.drug_id,
            store_master_uuid = responseContent.store_master_uuid,
            drug_name = responseContent.drug_name,
            drug_period_code = responseContent.drug_period_code,
            PrescriptiondurationPeriodId = responseContent.PrescriptiondurationPeriodId,
            drug_period_id = responseContent.drug_period_id,

            drug_period_name = responseContent.drug_period_name,
            drug_route_id = responseContent.drug_route_id,
            drug_route_name = responseContent.drug_route_name!!,


            favourite_details_id = responseContent.favourite_details_id,

            favourite_display_order = responseContent.favourite_display_order,
            favourite_id = responseContent.favourite_id,
            speciality_sketch_id = responseContent.speciality_sketch_id,
            favourite_name = responseContent.favourite_name,
            test_master_code = responseContent.test_master_code,
            test_master_description = responseContent.test_master_description,
            test_master_id = responseContent.test_master_id,
            test_master_name = responseContent.test_master_name,

            TestMethodId = responseContent.TestMethodId,
            isSelected = responseContent.isSelected,
            itemAppendString = responseContent.itemAppendString,
            position = responseContent.position,
            duration = responseContent.duration,
            durationPeriodId = responseContent.durationPeriodId,
            selectTypeUUID = responseContent.selectTypeUUID,
            selectRouteID = responseContent.selectRouteID,
            selecteFrequencyID = responseContent.selecteFrequencyID,
            selectInvestID = responseContent.selectInvestID,
            selectToLocationUUID = responseContent.selectToLocationUUID,
            selectToTestMethodUUID = responseContent.selectToTestMethodUUID,
            profile_master_uuid = responseContent.profile_master_uuid,

            perdayquantityPrescription = responseContent.perdayquantityPrescription,
            isFavposition = responseContent.isFavposition,
            viewLabstatus = responseContent.viewLabstatus,
            isTemposition = responseContent.isTemposition,
            code = responseContent.code,
            template_id = responseContent.template_id,
            commands = responseContent.commands,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            treatment_kit_id = responseContent.treatment_kit_id,
            treatment_kit_type_id = responseContent.treatment_kit_type_id,

            department_id = responseContent.department_id,
            template_details_uuid = responseContent.template_details_uuid,
            patient_order_uuid = responseContent.patient_order_uuid,
            patient_order_details_uuid = responseContent.patient_order_details_uuid,
            collapse = responseContent.collapse,
            Update = responseContent.Update,
            drug_is_emar = responseContent.drug_is_emar,
            viewPrescriptionstatus = responseContent.viewPrescriptionstatus
        )

    }

    private fun setListData(responseContent: PrescriptionNewData?): PrescriptionListData {


        return PrescriptionListData(

            drug_active = responseContent!!.drug_active,
            drug_duration = responseContent.drug_duration!!,
            drug_code = responseContent.drug_code,
            drug_quantity = responseContent.drug_quantity,
            Presstrength = responseContent.Presstrength,
            drug_frequency_id = responseContent.drug_frequency_id,


            drug_frequency_name = responseContent.drug_frequency_name,
            drug_instruction_code = responseContent.drug_instruction_code,
            drug_instruction_id = responseContent.drug_instruction_id,
            drug_instruction_name = responseContent.drug_instruction_name,

            drug_id = responseContent.drug_id,
            store_master_uuid = responseContent.store_master_uuid,
            drug_name = responseContent.drug_name,
            drug_period_code = responseContent.drug_period_code,
            PrescriptiondurationPeriodId = responseContent.PrescriptiondurationPeriodId,
            drug_period_id = responseContent.drug_period_id,

            drug_period_name = responseContent.drug_period_name,
            drug_route_id = responseContent.drug_route_id,
            drug_route_name = responseContent.drug_route_name!!,


            favourite_details_id = responseContent.favourite_details_id,

            favourite_display_order = responseContent.favourite_display_order,
            favourite_id = responseContent.favourite_id,
            speciality_sketch_id = responseContent.speciality_sketch_id,
            favourite_name = responseContent.favourite_name,
            test_master_code = responseContent.test_master_code,
            test_master_description = responseContent.test_master_description,
            test_master_id = responseContent.test_master_id,
            test_master_name = responseContent.test_master_name,

            TestMethodId = responseContent.TestMethodId,
            isSelected = responseContent.isSelected,
            itemAppendString = responseContent.itemAppendString,
            position = responseContent.position,
            duration = responseContent.duration,
            durationPeriodId = responseContent.durationPeriodId,
            selectTypeUUID = responseContent.selectTypeUUID,
            selectRouteID = responseContent.selectRouteID,
            selecteFrequencyID = responseContent.selecteFrequencyID,
            selectInvestID = responseContent.selectInvestID,
            selectToLocationUUID = responseContent.selectToLocationUUID,
            selectToTestMethodUUID = responseContent.selectToTestMethodUUID,
            profile_master_uuid = responseContent.profile_master_uuid,

            perdayquantityPrescription = responseContent.perdayquantityPrescription,
            isFavposition = responseContent.isFavposition,
            viewLabstatus = responseContent.viewLabstatus,
            isTemposition = responseContent.isTemposition,
            code = responseContent.code,
            template_id = responseContent.template_id,
            commands = responseContent.commands,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            treatment_kit_id = responseContent.treatment_kit_id,
            treatment_kit_type_id = responseContent.treatment_kit_type_id,

            department_id = responseContent.department_id,
            template_details_uuid = responseContent.template_details_uuid,
            patient_order_uuid = responseContent.patient_order_uuid,
            patient_order_details_uuid = responseContent.patient_order_details_uuid,
            collapse = responseContent.collapse,
            Update = responseContent.Update,
            drug_is_emar = responseContent.drug_is_emar,
            viewPrescriptionstatus = responseContent.viewPrescriptionstatus
        )

    }

    private fun setFavListData(responseContent: FavouritesModel?): PrescriptionListData {


        return PrescriptionListData(

            drug_active = responseContent!!.drug_active,
            drug_duration = responseContent.drug_duration!!,
            drug_code = responseContent.drug_code,
            drug_quantity = responseContent.drug_quantity,
            Presstrength = responseContent.Presstrength,
            drug_frequency_id = responseContent.drug_frequency_id,


            drug_frequency_name = responseContent.drug_frequency_name,
            drug_instruction_code = responseContent.drug_instruction_code,
            drug_instruction_id = responseContent.drug_instruction_id,
            drug_instruction_name = responseContent.drug_instruction_name,

            drug_id = responseContent.drug_id,
            store_master_uuid = responseContent.store_master_uuid,
            drug_name = responseContent.drug_name,
            drug_period_code = responseContent.drug_period_code,
            PrescriptiondurationPeriodId = responseContent.PrescriptiondurationPeriodId,
            drug_period_id = responseContent.drug_period_id,

            drug_period_name = responseContent.drug_period_name,
            drug_route_id = responseContent.drug_route_id,
            drug_route_name = responseContent.drug_route_name!!,


            favourite_details_id = responseContent.favourite_details_id,

            favourite_display_order = responseContent.favourite_display_order,
            favourite_id = responseContent.favourite_id,
            speciality_sketch_id = responseContent.speciality_sketch_id,
            favourite_name = responseContent.favourite_name,
            test_master_code = responseContent.test_master_code,
            test_master_description = responseContent.test_master_description,
            test_master_id = responseContent.test_master_id,
            test_master_name = responseContent.test_master_name,

            TestMethodId = responseContent.TestMethodId,
            isSelected = responseContent.isSelected,
            itemAppendString = responseContent.itemAppendString,
            position = responseContent.position,
            duration = responseContent.duration,
            durationPeriodId = responseContent.durationPeriodId,
            selectTypeUUID = responseContent.selectTypeUUID,
            selectRouteID = responseContent.selectRouteID,
            selecteFrequencyID = responseContent.selecteFrequencyID,
            selectInvestID = responseContent.selectInvestID,
            selectToLocationUUID = responseContent.selectToLocationUUID,
            selectToTestMethodUUID = responseContent.selectToTestMethodUUID,
            profile_master_uuid = responseContent.profile_master_uuid,

            perdayquantityPrescription = responseContent.perdayquantityPrescription,
            isFavposition = responseContent.isFavposition,
            viewLabstatus = responseContent.viewLabstatus,
            isTemposition = responseContent.isTemposition,
            code = responseContent.code,
            template_id = responseContent.template_id,
            commands = responseContent.commands,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            treatment_kit_id = responseContent.treatment_kit_id,
            treatment_kit_type_id = responseContent.treatment_kit_type_id,

            department_id = responseContent.department_id,
            template_details_uuid = responseContent.template_details_uuid,
            patient_order_uuid = responseContent.patient_order_uuid,
            patient_order_details_uuid = responseContent.patient_order_details_uuid,
            collapse = responseContent.collapse,
            Update = responseContent.Update,
            drug_is_emar = responseContent.drug_is_emar,
            viewPrescriptionstatus = responseContent.viewPrescriptionstatus
        )

    }

    private fun setDatatoFav(responseContent: PrescriptionListData?): FavouritesModel {


        return FavouritesModel(

            drug_active = responseContent!!.drug_active,
            drug_duration = responseContent.drug_duration!!,
            drug_code = responseContent.drug_code,
            drug_quantity = responseContent.drug_quantity,
            Presstrength = responseContent.Presstrength,
            drug_frequency_id = responseContent.drug_frequency_id,


            drug_frequency_name = responseContent.drug_frequency_name,
            drug_instruction_code = responseContent.drug_instruction_code,
            drug_instruction_id = responseContent.drug_instruction_id,
            drug_instruction_name = responseContent.drug_instruction_name,

            drug_id = responseContent.drug_id,
            store_master_uuid = responseContent.store_master_uuid,
            drug_name = responseContent.drug_name,
            drug_period_code = responseContent.drug_period_code,
            PrescriptiondurationPeriodId = responseContent.PrescriptiondurationPeriodId,
            drug_period_id = responseContent.drug_period_id,

            drug_period_name = responseContent.drug_period_name,
            drug_route_id = responseContent.drug_route_id,
            drug_route_name = responseContent.drug_route_name!!,


            favourite_details_id = responseContent.favourite_details_id,

            favourite_display_order = responseContent.favourite_display_order,
            favourite_id = responseContent.favourite_id,
            speciality_sketch_id = responseContent.speciality_sketch_id,
            favourite_name = responseContent.favourite_name,
            test_master_code = responseContent.test_master_code,
            test_master_description = responseContent.test_master_description,
            test_master_id = responseContent.test_master_id,
            test_master_name = responseContent.test_master_name,

            TestMethodId = responseContent.TestMethodId,
            isSelected = responseContent.isSelected,
            itemAppendString = responseContent.itemAppendString,
            position = responseContent.position,
            duration = responseContent.duration,
            durationPeriodId = responseContent.durationPeriodId,
            selectTypeUUID = responseContent.selectTypeUUID,
            selectRouteID = responseContent.selectRouteID,
            selecteFrequencyID = responseContent.selecteFrequencyID,
            selectInvestID = responseContent.selectInvestID,
            selectToLocationUUID = responseContent.selectToLocationUUID,
            selectToTestMethodUUID = responseContent.selectToTestMethodUUID,
            profile_master_uuid = responseContent.profile_master_uuid,

            perdayquantityPrescription = responseContent.perdayquantityPrescription!!,
            isFavposition = responseContent.isFavposition,
            viewLabstatus = responseContent.viewLabstatus,
            isTemposition = responseContent.isTemposition,
            code = responseContent.code!!,
            template_id = responseContent.template_id,
            commands = responseContent.commands!!,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            treatment_kit_id = responseContent.treatment_kit_id,
            treatment_kit_type_id = responseContent.treatment_kit_type_id,

            department_id = responseContent.department_id,
            template_details_uuid = responseContent.template_details_uuid,
            patient_order_uuid = responseContent.patient_order_uuid,
            patient_order_details_uuid = responseContent.patient_order_details_uuid,
            collapse = responseContent.collapse,
            Update = responseContent.Update,
            drug_is_emar = responseContent.drug_is_emar,
            viewPrescriptionstatus = responseContent.viewPrescriptionstatus!!
        )

    }


    private fun clearList() {

        AddNew = PrescriptionNewData()
        binding?.PresAutoCompleteTextView?.setText("")
        binding?.Strength?.setText("")
        binding?.duration?.setText("")
        routeNamesMap.clear()
        instructionMap.clear()
        frequencyMap.clear()
        routeNamesMap.put(0, "")
        instructionMap.put(0, "")
        frequencyMap.put(0, "")

        durationAdapter?.updateSelectStatus(0)

        setDummyInSpinner(binding?.routeSpinner, routeNamesMap)
        setDummyInSpinner(binding?.frequencySpinner, instructionMap)
        setDummyInSpinner(binding?.instructionSpinner, frequencyMap)


    }

    private fun setDummyInSpinner(spinner: AppCompatSpinner?, Mapdata: MutableMap<Int, String>) {

        val Adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                Mapdata.values.toMutableList()
            )
        Adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner?.adapter = Adapter


    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(PrescriptionFavouriteFragment(), "Favourite")
        adapter.addFragment(PrescriptionTempleteFragment(), "Templete")
        adapter.addFragment(PrevPrescriptionFragment(), "Prev.Prescription")

        viewPager.adapter = adapter

    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = java.util.ArrayList<Fragment>()
        private val mFragmentTitleList = java.util.ArrayList<String>()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }


    private fun setTabletAdapter() {

        prescriptionTabAdapter =
            PrescriptionTabAdapter(requireActivity(), ArrayList(), ArrayList())
        binding?.savePrescriptionRecyclerView?.adapter = prescriptionTabAdapter
        prescriptionTabAdapter?.addRow(FavouritesModel())


        prescriptionTabAdapter?.setOnSearchInitiatedListener(object :
            PrescriptionTabAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int
            ) {
                dropdownReferenceView = view
                selectedSearchPosition = position
                viewModel?.getComplaintSearchResult(
                    query,
                    getComplaintSearchRetrofitCallBack,
                    facility_id
                )
            }
        })


        prescriptionTabAdapter?.setOnDeleteClickListener(object :
            PrescriptionTabAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                ////////////////////////
                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                    customdialog = null
                }
                drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text} '" + responseContent?.itemAppendString + "' Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {


                    prescriptionTabAdapter?.deleteRow(position)


                    customdialog!!.dismiss()

                    customdialog = null
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                    customdialog = null
                }

                if (customdialog != null) {

                    customdialog!!.show()

                }
            }
        })
        prescriptionTabAdapter?.setOnCommandClickListener(object :
            PrescriptionTabAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String) {

                //     val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = CommentDialogFragment()

                val ft = childFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putString("commands", Command)

                dialog.arguments = bundle
                dialog.show(ft, "Tag")


            }
        })


        prescriptionTabAdapter?.setOnitemChangeListener(object :
            PrescriptionTabAdapter.OnitemChangeListener {
            override fun onitemChangeClick(uuid: ArrayList<Int>) {


                mCallbackPresFavFragment?.checkanduncheck(uuid, true)

                mCallbackPrevTemplateFragment?.checkanduncheck(uuid)

            }
        })


    }


    private fun setADapter() {
        prescriptionAdapter =
            PrescriptionAdapter(requireActivity(), ArrayList(), ArrayList()) { position, selected ->
                mCallbackPresFavFragment?.updateSelectStatus(1, position, selected)
            }
//        binding?.savePrescriptionRecyclerView?.adapter = prescriptionAdapter
        prescriptionAdapter?.addRow(FavouritesModel())
        prescriptionAdapter?.addTempleteRow(TempDetails())

        prescriptionAdapter?.setOnSearchInitiatedListener(object :
            PrescriptionAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int
            ) {
                dropdownReferenceView = view
                selectedSearchPosition = position
                viewModel?.getComplaintSearchResult(
                    query,
                    getComplaintSearchRetrofitCallBack,
                    facility_id
                )
            }
        })


        prescriptionAdapter?.setOnDeleteClickListener(object :
            PrescriptionAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                ////////////////////////
                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                    customdialog = null
                }
                drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text} '" + responseContent?.drug_name + "' Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    val check = prescriptionAdapter?.deleteRow(position)
//                val check=labAdapter?.checkData(responseContent!!)

                    if (responseContent?.viewPrescriptionstatus == 1) {

                        mCallbackPresFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)

                    } else if (responseContent?.viewPrescriptionstatus == 2) {
                        val ischeckIdPresent =
                            mCallbackPresFavFragment!!.drugIdPresentCheck(responseContent.test_master_id!!)
                        if (ischeckIdPresent) {
                            mCallbackPresFavFragment?.clearDataUsingDrugid(responseContent.drug_id!!)
                        }
                        if (check!!) {
                            mCallbackPrevTemplateFragment?.ClearTemplateParticularPosition(
                                responseContent.isTemposition
                            )

                        }
                        //template_id
                    }


                    customdialog!!.dismiss()

                    customdialog = null
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                    customdialog = null
                }

                if (customdialog != null) {

                    customdialog!!.show()

                }
            }
        })
        prescriptionAdapter?.setOnCommandClickListener(object :
            PrescriptionAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String) {

                //     val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = CommentDialogFragment()

                val ft = childFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putString("commands", Command)

                dialog.arguments = bundle
                dialog.show(ft, "Tag")


            }
        })


    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is PrescriptionFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is PrescriptionTempleteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackPresFavFragment = childFragment
        }
        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackPrevTemplateFragment = childFragment
        }

        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackLabTemplateFragment = childFragment
        }
        if (childFragment is PrevPrescriptionFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ManagePreTempAddEdit) {
            childFragment.setOnTempRefreshListener(this)
        }

        if (childFragment is CommentDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }

        if (childFragment is SaveTemplateDialogFragment) {
            childFragment.setOnClickedListener(this)
        }


    }


    private fun pdfGeneration() {

        downloadPDFTask = null

        viewModel?.GetPrescriptionPDF(printUUid!!, pdfGenerateRetrofitCallback)
    }


    private val pdfGenerateRetrofitCallback = object : RetrofitCallback<ResponseBody> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseBody>?) {
            if (downloadPDFTask == null) {
                downloadPDFTask = DownloadPDFTask()
                downloadPDFTask?.execute(responseBody?.body())

                isPrint = false
            }
        }

        override fun onBadRequest(response: Response<ResponseBody>?) {
            //  toast(response?.message()!!.toString())
        }

        override fun onServerError(response: Response<*>) {
            //toast(response.message())
        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(failure: String) {
            //   toast(failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class DownloadPDFTask : AsyncTask<ResponseBody?, Pair<Int?, Long?>?, String?>() {

        fun doProgress(progressDetails: Pair<Int?, Long?>?) {
            publishProgress(progressDetails)
        }

        override fun onPostExecute(result: String?) {
//            showLoading(false)
//            if (isPDFButtonClicked)
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri: Uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName.toString() + ".provider",
                destinationPDFFile!!
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/pdf")
            startActivity(intent)

//            toast("Storage path: $destinationPDFFile")
        }

        override fun doInBackground(vararg params: ResponseBody?): String? {
            saveToDiskPrint(
                params[0]!!,
                "${"Prescription"} ${printUUid}.pdf"
            )
            return null
        }
    }


    private fun saveToDiskPrint(body: ResponseBody, filename: String) {
        try {
            destinationPDFFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
            if (!destinationPDFFile!!.exists()) {
                if (!destinationPDFFile!!.createNewFile()) {
                    throw IOException("Cant able to create file")
                }
            }

            val inputstream: InputStream? = body.byteStream()
            val os: OutputStream = FileOutputStream(destinationPDFFile)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            //read from is to buffer
            while (inputstream!!.read(buffer).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            inputstream.close()
            //flush OutputStream to write any buffered data to file
            os.flush()
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun checkPermissionForReadAndWriteStorage(): Boolean {
        val writeStorage =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val readStorage =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

        return writeStorage == PackageManager.PERMISSION_GRANTED &&
                readStorage == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermissionForReadAndWriteStorage(

    ) {
        if (!checkPermissionForReadAndWriteStorage()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                NurseDeskDischargeSummaryChildFragment.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            pdfGeneration()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == NurseDeskDischargeSummaryChildFragment.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            var readAndSWriteStoragePermissionGranted = true

            for (grantResult in grantResults) {
                readAndSWriteStoragePermissionGranted = readAndSWriteStoragePermissionGranted and
                        (grantResult == PackageManager.PERMISSION_GRANTED)
            }

            if (readAndSWriteStoragePermissionGranted) {

                requestPermissionForReadAndWriteStorage()

            } else {
//                getCustomDialog()
            }
        }
    }


    val getFrequencyRetrofitCallback =
        object : RetrofitCallback<PrescriptionFrequencyResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<PrescriptionFrequencyResponseModel>?) {


                if (IsTablet) {

                    prescriptionTabAdapter?.setFrequencyList(responseBody?.body()?.responseContents)

                } else {

                    freqencyList =
                        responseBody?.body()?.responseContents as ArrayList<PrescriptionFrequencyresponseContent>

                    Log.e("frew", freqencyList.toString())


                }
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

    private fun setFreqency(data: ArrayList<PrescriptionFrequencyresponseContent>) {

        frequencyMap.putAll(data.map { it.uuid!! to it.name }.toMap().toMutableMap())

        val frequencyAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            frequencyMap.values.toMutableList()
        )

        frequencyAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.frequencySpinner?.adapter = frequencyAdapter

    }


    val getRouteRetrofitCallback = object : RetrofitCallback<PrescriptionRoutResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<PrescriptionRoutResponseModel>?) {


            if (IsTablet) {
                prescriptionTabAdapter?.setRouteValue(responseBody?.body()?.responseContents)
            } else {


                RouteList =
                    responseBody?.body()?.responseContents!! as ArrayList<PrescriptionRouteResponseContent>
            }


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

    private fun setRoute(data: ArrayList<PrescriptionRouteResponseContent>) {

        routeNamesMap.putAll(data.map { it.uuid to it.name }.toMap().toMutableMap())

        val routeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            routeNamesMap.values.toMutableList()
        )
        routeAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.routeSpinner?.adapter = routeAdapter

    }

    val getInjectionRetrofitCallback = object : RetrofitCallback<InjectionDepartMentResponce> {
        override fun onSuccessfulResponse(responseBody: Response<InjectionDepartMentResponce>?) {


            if (IsTablet) {

                prescriptionTabAdapter?.setInjectionList(responseBody?.body()?.responseContents)
            } else {

                prescriptionMobileAdapter!!.setInjection(responseBody?.body()?.responseContents as ArrayList<InjectionDepartment>)

                InjectionList =
                    responseBody.body()?.responseContents!! as ArrayList<InjectionDepartment>

            }

        }

        override fun onBadRequest(errorBody: Response<InjectionDepartMentResponce>?) {
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


    private fun setInjection(data: ArrayList<InjectionDepartment>) {

        instructionMap.clear()

        instructionMap.put(0, "")

        instructionMap.putAll(data.map { it.uuid to it.store_name }.toMap().toMutableMap())

        val routeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            instructionMap.values.toMutableList()
        )
        routeAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.instructionSpinner?.adapter = routeAdapter

    }


    val getInstructionRetrofitCallback = object : RetrofitCallback<PresInstructionResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<PresInstructionResponseModel>?) {

            if (IsTablet) {
                prescriptionTabAdapter?.setInstructionList(responseBody?.body()?.responseContents)
            } else {

                prescriptionMobileAdapter?.setInstruction(responseBody?.body()?.responseContents as ArrayList<PresInstructionResponseContent>)

                InstructionList =
                    responseBody?.body()?.responseContents!! as ArrayList<PresInstructionResponseContent>

            }
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


    private fun setInstruction(data: ArrayList<PresInstructionResponseContent>) {


        instructionMap.clear()

        instructionMap.put(0, "")

        instructionMap.putAll(data.map { it.uuid to it.name }.toMap().toMutableMap())

        val routeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            instructionMap.values.toMutableList()
        )
        routeAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.instructionSpinner?.adapter = routeAdapter

    }


    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<PrescriptionSearchResponseModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionSearchResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {


                    if (IsTablet) {
                        prescriptionTabAdapter?.setPrescriptionAdapter(
                            dropdownReferenceView,
                            response.body()?.responseContents!!, selectedSearchPosition
                        )

                    } else {

                        var result = response.body()?.responseContents

                        val responseContentAdapter = PrescriptionSearchResultAdapter(
                            requireContext(),
                            R.layout.row_chief_complaint_search_result,
                            result
                        )
                        binding?.PresAutoCompleteTextView?.threshold = 1
                        binding?.PresAutoCompleteTextView?.setAdapter(responseContentAdapter)
                        binding?.PresAutoCompleteTextView?.showDropDown()
                        binding?.PresAutoCompleteTextView?.setOnItemClickListener { parent, _, position, id ->

                            val inputMethodManager =
                                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
                            val selectedPoi =
                                parent.adapter.getItem(position) as PrescriptionSearchResponseContent?

                            val st = FavouritesModel()

                            var listData = prescriptionMobileAdapter?.getItems()

                            val check = listData?.any { it!!.test_master_id == selectedPoi?.uuid }


                            if (!check!!) {

                                binding?.PresAutoCompleteTextView?.setText(selectedPoi?.name)
                                AddNew.drug_name = selectedPoi?.name
                                AddNew.itemAppendString = selectedPoi?.name
                                AddNew.test_master_id = selectedPoi?.uuid
                                AddNew.store_master_uuid =
                                    selectedPoi?.stock_item?.store_master_uuid
                                AddNew.drug_id = selectedPoi?.stock_item?.item_master_uuid
                                AddNew.drug_code =
                                    selectedPoi?.stock_item?.store_master?.store_code ?: ""
                                AddNew.drug_name =
                                    selectedPoi?.stock_item?.store_master?.store_name ?: ""
                                AddNew.Presstrength = selectedPoi?.strength
                                AddNew.drug_period_code = selectedPoi?.code.toString()
                                AddNew.drug_active = selectedPoi?.is_emar

                                binding?.Strength?.setText(selectedPoi?.strength ?: "")

                                setFreqency(freqencyList)

                                setRoute(RouteList)

                                if (selectedPoi?.is_emar!!) {

                                    setInjection(InjectionList)
                                } else {
                                    setInstruction(InstructionList)
                                }

                                if (AddNew.isFavposition != 0) {


                                }


                            } else {

                                binding?.PresAutoCompleteTextView?.setText("")

                                Toast.makeText(
                                    context,
                                    "Already Item available in the list",
                                    Toast.LENGTH_SHORT
                                )
                                    ?.show()

                            }

                        }


                    }

                }
            }

            override fun onBadRequest(response: Response<PrescriptionSearchResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionSearchResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionSearchResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message!!
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
    private val getPrescriptionDurationRetrofitCallBack =
        object : RetrofitCallback<PrescriptionDurationResponseModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionDurationResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    var result =
                        response.body()?.responseContents!! as ArrayList<PrescriptionDurationResponseContent>

                    for (i in result.indices) {


                        if (result[i].code == "Years") {

                            result.removeAt(i)
                            break
                        }
                    }

                    if (IsTablet) {
                        prescriptionTabAdapter?.setDuration(response.body()?.responseContents!!)
                    } else {


                        durationArrayList =
                            response.body()?.responseContents as ArrayList<PrescriptionDurationResponseContent>

                        val gridLayoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL, true
                        )
                        binding?.durationRecyclerView?.layoutManager = gridLayoutManager

                        durationAdapter = PrescriptionDurationAdapter(
                            requireContext(),
                            response.body()?.responseContents
                        )

                        binding?.durationRecyclerView?.adapter = durationAdapter


                        durationAdapter?.setOnItemClickListener(object :
                            PrescriptionDurationAdapter.OnItemClickListener {

                            override fun onItemClick(durationID: Int, poss: Int) {

                                durationAdapter?.updateSelectStatus((durationID))

/*
                                for(i in durationArrayList.indices){

                                    if(durationArrayList[i].uuid==durationID){

                                        AddNew.drug_period_name=durationArrayList[i].code
                                    }
                                }*/

                                AddNew.drug_period_name = durationArrayList[poss].code
                                AddNew.PrescriptiondurationPeriodId = durationID

                            }
                        })

                    }

                }
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

    override fun sendFavAddInLab(favmodel: FavouritesModel?, position: Int, selected: Boolean) {
        favmodel?.viewPrescriptionstatus = 1
        favmodel?.isFavposition = position

        Log.e("slsected", "" + selected)
        if (!selected) {

            favmodel?.test_master_id = favmodel?.drug_id
            favmodel?.itemAppendString = favmodel?.drug_name

            favmodel?.drug_active = favmodel?.drug_is_emar

            favmodel?.selectRouteID = favmodel?.drug_route_id ?: 0
            favmodel?.durationPeriodId = favmodel?.drug_period_id ?: 0
            favmodel?.selectInvestID = favmodel?.drug_instruction_id ?: 0

            if (favmodel?.drug_frequency_id != null)
                favmodel.selecteFrequencyID = favmodel.drug_frequency_id!!.toInt()

            if (favmodel?.drug_duration != null) {

                favmodel.duration = favmodel.drug_duration.toString()

            }

            if (IsTablet) {

                prescriptionTabAdapter!!.removeLastPoistion()
                prescriptionTabAdapter!!.addRow(favmodel!!)
                prescriptionTabAdapter!!.addRow(FavouritesModel())
            } else {

                var frquentityMap =
                    freqencyList.map { it.uuid!! to it.perdayquantity }.toMap().toMutableMap()

                val check = freqencyList.any { it.uuid == favmodel!!.drug_frequency_id!!.toInt() }

                if (check) {

                    favmodel!!.perdayquantityPrescription =
                        frquentityMap[favmodel.drug_frequency_id!!.toInt()].toString()


                    if (favmodel.perdayquantityPrescription == null) {

                        favmodel.perdayquantityPrescription = 1.toString()
                    }

                    if (favmodel.duration == null) {

                        favmodel.duration = 1.toString()
                    }



                    when (favmodel.drug_period_code) {

                        "Days" -> {
                            favmodel.drug_quantity =
                                (favmodel.perdayquantityPrescription.toDouble() * favmodel.duration!!.toInt()).toString()
                        }
                        "Weeks" -> {


                            favmodel.drug_quantity =
                                (favmodel.perdayquantityPrescription.toDouble() * favmodel.duration!!.toInt() * 7).toString()

                        }
                        "Months" -> {


                            favmodel.drug_quantity =
                                (favmodel.perdayquantityPrescription.toDouble() * favmodel.duration!!.toInt() * 30).toString()

                        }
                        "Years" -> {

                            favmodel.drug_quantity =
                                (favmodel.perdayquantityPrescription.toDouble() * favmodel.duration!!.toInt() * 365).toString()

                        }
                        else -> {

                            favmodel.drug_quantity =
                                (favmodel.perdayquantityPrescription.toDouble() * favmodel.duration!!.toInt()).toString()
                        }
                    }


                }


                val setData = setFavListData(favmodel)

                prescriptionMobileAdapter!!.addFavouritesInRowModule(setData, position)

            }

        } else {

            if (IsTablet) {

                prescriptionTabAdapter!!.deleteItem(favmodel?.drug_id!!)

            } else {


                prescriptionMobileAdapter!!.deleteRowFromFav(favmodel?.drug_id!!, position)
            }


        }
    }

    override fun sendTemplete(
        templeteDrugDetails: List<DrugDetail>?,
        position: Int,
        selected: Boolean,
        templateID: Int
    ) {
        if (!selected) {
            if (IsTablet) {

                prescriptionTabAdapter!!.removeLastPoistion()

            }

            for (i in templeteDrugDetails!!.indices) {

                if (IsTablet) {


                    val favmodel: FavouritesModel? = FavouritesModel()
                    favmodel?.viewPrescriptionstatus = 2
                    favmodel?.isTemposition = position
                    favmodel?.test_master_name = templeteDrugDetails[i].drug_name
                    favmodel?.test_master_id = templeteDrugDetails[i].drug_id
                    favmodel?.test_master_code = templeteDrugDetails[i].drug_code
                    favmodel?.template_id = templateID
                    favmodel?.itemAppendString = templeteDrugDetails[i].drug_name
                    favmodel?.drug_active = templeteDrugDetails[i].drug_is_emar
                    favmodel?.selectInvestID = templeteDrugDetails[i].drug_instruction_id
                    favmodel?.selectRouteID = templeteDrugDetails[i].drug_route_id
                    favmodel?.selecteFrequencyID = templeteDrugDetails[i].drug_frequency_id
                    favmodel?.duration = templeteDrugDetails[i].drug_duration.toString()
                    favmodel?.PrescriptiondurationPeriodId = templeteDrugDetails[i].drug_period_id
                    favmodel?.drug_instruction_name = templeteDrugDetails[i].drug_instruction_name

                    prescriptionTabAdapter!!.addRow(favmodel!!)

                    /*
                    val checkIsPresent =
                        prescriptionAdapter!!.isCheckAlreadyExist(templeteDrugDetails[i].drug_id)

                    if (checkIsPresent) {

                        prescriptionAdapter!!.addTemplatesInRow(favmodel)

                    }*/


                } else {

                    /*    var duarMap=durationArrayList.map { it?.uuid!! to it.code }!!.toMap().toMutableMap()
                        favmodel!!.drug_period_name= duarMap[templeteDrugDetails[i].drug_period_id]?:""
                        */


                    val favmodel: PrescriptionListData? = PrescriptionListData()
                    favmodel?.viewPrescriptionstatus = 2
                    favmodel?.isTemposition = position
                    favmodel?.test_master_name = templeteDrugDetails[i].drug_name
                    favmodel?.test_master_id = templeteDrugDetails[i].drug_id
                    favmodel?.test_master_code = templeteDrugDetails[i].drug_code
                    favmodel?.template_id = templateID
                    favmodel?.itemAppendString = templeteDrugDetails[i].drug_name
                    favmodel?.drug_active = templeteDrugDetails[i].drug_is_emar
                    favmodel?.selectInvestID = templeteDrugDetails[i].drug_instruction_id
                    favmodel?.selectRouteID = templeteDrugDetails[i].drug_route_id
                    favmodel?.selecteFrequencyID = templeteDrugDetails[i].drug_frequency_id
                    favmodel?.duration = templeteDrugDetails[i].drug_duration.toString()
                    favmodel?.PrescriptiondurationPeriodId = templeteDrugDetails[i].drug_period_id
                    favmodel?.drug_instruction_name = templeteDrugDetails[i].drug_instruction_name

                    favmodel!!.drug_period_name = templeteDrugDetails[i].drug_period_name

                    var frqMap = freqencyList.map { it.uuid!! to it.name }.toMap().toMutableMap()
                    favmodel.drug_frequency_name =
                        frqMap[templeteDrugDetails[i].drug_frequency_id] ?: ""

                    var frquentityMap =
                        freqencyList.map { it.uuid!! to it.perdayquantity }.toMap().toMutableMap()

                    favmodel.perdayquantityPrescription =
                        frquentityMap[templeteDrugDetails[i].drug_frequency_id].toString()

                    var routMap = RouteList.map { it.uuid to it.name }.toMap().toMutableMap()

                    favmodel.drug_route_name = routMap[templeteDrugDetails[i].drug_route_id] ?: ""

                    if (favmodel.perdayquantityPrescription == "null" || favmodel.perdayquantityPrescription == null) {

                        favmodel.perdayquantityPrescription = 1.toString()
                    }

                    if (favmodel.duration == null || favmodel.duration == "null") {

                        favmodel.duration = 1.toString()
                    }


                    if (templeteDrugDetails[i].drug_period_code != null) {
                        when (templeteDrugDetails[i].drug_period_code) {

                            "Days" -> {
                                favmodel.drug_quantity =
                                    (favmodel.perdayquantityPrescription!!.toDouble() * favmodel.duration!!.toInt()).toString()
                            }
                            "Weeks" -> {


                                favmodel.drug_quantity =
                                    (favmodel.perdayquantityPrescription!!.toDouble() * favmodel.duration!!.toInt() * 7).toString()

                            }
                            "Months" -> {


                                favmodel.drug_quantity =
                                    (favmodel.perdayquantityPrescription!!.toDouble() * favmodel.duration!!.toInt() * 30).toString()

                            }
                            "Years" -> {

                                favmodel.drug_quantity =
                                    (favmodel.perdayquantityPrescription!!.toDouble() * favmodel.duration!!.toInt() * 365).toString()

                            }
                            else -> {

                                favmodel.drug_quantity =
                                    (favmodel.perdayquantityPrescription!!.toDouble() * favmodel.duration!!.toInt()).toString()
                            }
                        }

                    }

                    val checkIsPresent =
                        prescriptionMobileAdapter!!.isCheckAlreadyExist(templeteDrugDetails[i].drug_id)

                    if (checkIsPresent) {

                        prescriptionMobileAdapter!!.addTemplatesInRow(favmodel)

                    }

                }

                mCallbackPresFavFragment!!.checkanduncheck(templeteDrugDetails[i].drug_id, true)

            }



            if (IsTablet) {

                prescriptionTabAdapter!!.addRow(FavouritesModel())

            }
        } else {


            if (IsTablet) {
                for (i in templeteDrugDetails!!.indices) {

                    prescriptionTabAdapter!!.deleteItem(
                        templeteDrugDetails[i].drug_id
                    )

                    /*  mCallbackPresFavFragment!!.checkanduncheck(
                          templeteDrugDetails[i].drug_id,
                          false
                      )*/

                }


            } else {


                for (i in templeteDrugDetails!!.indices) {

                    prescriptionMobileAdapter!!.deleteRowFromTemplate(
                        templeteDrugDetails[i].drug_id,
                        position
                    )

                    mCallbackPresFavFragment!!.checkanduncheck(
                        templeteDrugDetails[i].drug_id,
                        false
                    )

                }

            }
        }


    }


    val emrprescriptionpostRetrofitCallback =
        object : RetrofitCallback<PrescriptionPostAllDataResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<PrescriptionPostAllDataResponseModel>?) {
                trackPrescriptionSaveComplete(utils?.getEncounterType(), "success", "")

                if (IsTablet) {
                    mCallbackPresFavFragment?.ClearAllData()
                    mCallbackPrevTemplateFragment?.ClearAllData()


                    prescriptionTabAdapter?.clearall()
                    prescriptionTabAdapter?.addRow(FavouritesModel())
                    //  prescriptionAdapter?.addTempleteRow(TempDetails())

                } else {


                    mCallbackPresFavFragment?.ClearAllData()
                    mCallbackPrevTemplateFragment?.ClearAllData()

                    prescriptionMobileAdapter?.clearall()

                    hideDropDown()

                }


                if (PageType != 0) {


                    if (isModify)
                        toast("Discharge Medication Updated Successfully")
                    else
                        toast("Discharge Medication Created Successfully")

                } else {

                    if (isModify)
                        toast("Prescription Updated Successfully")
                    else
                        toast("Prescription Created Successfully")

                }

                isModify = false


                if (isPrint) {


                    if (checkPermissionForReadAndWriteStorage()) {

                        printUUid =
                            responseBody?.body()?.responseContents?.prescription_result?.uuid

                        pdfGeneration()

                    } else {

                        requestPermissionForReadAndWriteStorage()
                    }

                }


            }

            override fun onBadRequest(response: Response<PrescriptionPostAllDataResponseModel>) {

                trackPrescriptionSaveComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    response.message()
                )
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionPostAllDataResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionPostAllDataResponseModel::class.java
                    )

                    Log.e("BadRequest", response.toString())


                    toast(getString(R.string.something_went_wrong))

                } catch (e: Exception) {

                    toast(getString(R.string.something_went_wrong))


                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                trackPrescriptionSaveComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                toast(getString(R.string.something_went_wrong))
            }

            override fun onUnAuthorized() {
                trackPrescriptionSaveComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.unauthorized)
                )

                toast(getString(R.string.unauthorized))

            }

            override fun onForbidden() {
                trackPrescriptionSaveComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                trackPrescriptionSaveComplete(utils!!.getEncounterType(), "failure", failure)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }


            override fun onEverytime() {

                viewModel!!.progress.value = 8
            }

        }


    override fun sendPrevInPres(
        responseContent: List<PrescriptionDetail?>?,
        IsModify: Boolean,
        room_uuid: PrevRow?
    ) {

        Log.i("datas", "" + responseContent)

        Log.i("datas", "" + responseContent)
        Log.i("datas", "" + responseContent)


        isModify = IsModify

        prescriptionId = responseContent?.get(0)?.prescription_uuid ?: 0

        injection_room_uuid = room_uuid!!.injection_room_uuid

        if (IsTablet) {

            prescriptionTabAdapter!!.clearall()

            for (i in responseContent!!.indices) {

                var favouritesModel = FavouritesModel()

                favouritesModel.drug_name = responseContent.get(i)!!.item_master!!.name
                favouritesModel.itemAppendString = responseContent.get(i)!!.item_master!!.name
                favouritesModel.test_master_id = responseContent.get(i)!!.uuid
                favouritesModel.drug_period_code = responseContent.get(i)!!.item_master!!.code

                favouritesModel.selecteFrequencyID =
                    responseContent.get(i)!!.drug_frequency_uuid!!

                favouritesModel.favourite_active = IsModify

                prescriptionTabAdapter!!.addRow(favouritesModel)

            }

            prescriptionTabAdapter!!.addRow(FavouritesModel())

        } else {

            prescriptionMobileAdapter!!.clearall()

            for (i in responseContent!!.indices) {

                var favouritesModel = PrescriptionListData()

                favouritesModel.diagnosisUUiD = responseContent.get(i)!!.uuid ?: 0

                favouritesModel.drug_name = responseContent.get(i)!!.item_master!!.name
                favouritesModel.itemAppendString = responseContent.get(i)!!.item_master!!.name
                favouritesModel.test_master_id = responseContent.get(i)!!.item_master!!.uuid!!
                favouritesModel.perdayquantityPrescription =
                    responseContent.get(i)!!.item_master!!.code!!
                favouritesModel.selecteFrequencyID = responseContent.get(i)!!.drug_frequency_uuid!!

                favouritesModel.selectInvestID = responseContent.get(i)!!.drug_instruction_uuid!!
                favouritesModel.drug_instruction_name =
                    responseContent.get(i)!!.drug_instruction!!.name!!

                favouritesModel.selectRouteID = responseContent.get(i)!!.drug_route_uuid!!
                favouritesModel.drug_route_name =
                    responseContent.get(i)!!.drug_route!!.name.toString()

                favouritesModel.Presstrength = responseContent.get(i)!!.item_master!!.strength ?: ""

                favouritesModel.drug_period_name = responseContent.get(i)!!.drug_frequency!!.name!!

                favouritesModel.duration = responseContent.get(i)!!.duration.toString()


                favouritesModel.favourite_active = IsModify

                val check =
                    InjectionList.any { it.uuid == responseContent.get(i)!!.drug_instruction_uuid!! }

                favouritesModel.drug_active = responseContent.get(i)!!.is_emar ?: false


                if (favouritesModel.drug_active!!) {


                    room_uuid.injection_room_uuid
                    favouritesModel.selectInvestID = room_uuid.injection_room.uuid
                    favouritesModel.drug_instruction_name = room_uuid.injection_room.store_name


                } else {

                    favouritesModel.selectInvestID =
                        responseContent.get(i)!!.drug_instruction_uuid!!
                    favouritesModel.drug_instruction_name =
                        responseContent.get(i)!!.drug_instruction!!.name!!

                }

                favouritesModel.drug_period_name = responseContent.get(i)!!.duration_period!!.code

                var frquentityMap =
                    freqencyList.map { it.uuid!! to it.perdayquantity }.toMap().toMutableMap()

                favouritesModel.perdayquantityPrescription =
                    frquentityMap[responseContent.get(i)!!.drug_frequency_uuid!!].toString()

                if (favouritesModel.perdayquantityPrescription == null) {

                    favouritesModel.perdayquantityPrescription = 1.toString()
                }

                if (favouritesModel.duration == null) {

                    favouritesModel.duration = 1.toString()
                }

                favouritesModel.drug_frequency_name =
                    responseContent.get(i)!!.drug_frequency!!.name ?: ""

                when (responseContent.get(i)!!.duration_period!!.code) {

                    "Days" -> {
                        favouritesModel.drug_quantity =
                            (favouritesModel.perdayquantityPrescription!!.toDouble() * favouritesModel.duration!!.toInt()).toString()
                    }
                    "Weeks" -> {


                        favouritesModel.drug_quantity =
                            (favouritesModel.perdayquantityPrescription!!.toDouble() * favouritesModel.duration!!.toInt() * 7).toString()

                    }
                    "Months" -> {


                        favouritesModel.drug_quantity =
                            (favouritesModel.perdayquantityPrescription!!.toDouble() * favouritesModel.duration!!.toInt() * 30).toString()

                    }
                    "Years" -> {

                        favouritesModel.drug_quantity =
                            (favouritesModel.perdayquantityPrescription!!.toDouble() * favouritesModel.duration!!.toInt() * 365).toString()

                    }
                    else -> {

                        favouritesModel.drug_quantity =
                            (favouritesModel.perdayquantityPrescription!!.toDouble() * favouritesModel.duration!!.toInt()).toString()
                    }

                }

                prescriptionMobileAdapter!!.addRow(favouritesModel)

            }

        }
    }

    override fun sendCommandPosData(position: Int, command: String) {

        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        Log.i("comanda", "" + position + "" + command)

        if (IsTablet)
            prescriptionTabAdapter!!.addCommands(position, command)
        else
            prescriptionMobileAdapter!!.addCommands(position, command)

    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            // post your code
        }
    }

    override fun onStart() {
        super.onStart()
        userVisibleHint = true
    }

    override fun sendDataChiefComplaint() {

        mCallbackLabTemplateFragment?.GetTemplateDetails()
    }

    //track prescription visit
    fun trackPrescriptionAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackPrescriptionVisit(type)
    }

    //track prescription save
    fun trackPrescriptionSaveStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackPrescriptionSaveStart(type)
    }

    fun trackPrescriptionSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackPrescriptionSaveComplete(type, status, message)
    }


    fun savePrescription() {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        var isInj: Boolean = false

        var isInjectionId: String = ""

        var short = if (IsTablet) {

            2
        } else {

            1
        }


        if (IsTablet) {

            for (i in 0..arrayItemData!!.size - short) {
                val details: Detail = Detail()

                var prescribed_quantity_Data: Int = 0

                val period = arrayItemData?.get(i)?.PrescriptiondurationPeriodId

                var prescribed_quantity = arrayItemData?.get(i)?.perdayquantityPrescription!!

                if (arrayItemData?.get(i)?.drug_active!!) {

                    details.injection_room_uuid =
                        arrayItemData?.get(i)?.selectInvestID.toString()

                    details.administer_detail_status_uuid = 1

                    if (isInjectionId == "") {
                        isInjectionId = arrayItemData?.get(i)?.selectInvestID.toString()
                    } else {
                        if (isInjectionId == arrayItemData?.get(i)?.selectInvestID.toString()) {
                            isInjectionId = arrayItemData?.get(i)?.selectInvestID.toString()

                        } else {

                            Toast.makeText(context, "Injection must be same", Toast.LENGTH_LONG)
                                .show()
                            return
                        }
                    }

                    isInj = true

                } else {

                    details.drug_instruction_uuid =
                        arrayItemData?.get(i)?.selectInvestID.toString()

                    details.administer_detail_status_uuid = 0

                }



                details.drug_route_uuid = arrayItemData?.get(i)?.selectRouteID.toString()
                details.drug_frequency_uuid =
                    arrayItemData?.get(i)?.selecteFrequencyID.toString()
                details.duration_period_uuid =
                    arrayItemData?.get(i)?.PrescriptiondurationPeriodId.toString()
                //  details.drug_instruction_uuid = arrayItemData?.get(i)?.selectInvestID.toString()
                details.duration = arrayItemData?.get(i)?.duration
                details.prescribed_quantity = arrayItemData?.get(i)?.drug_quantity!!
                details.item_master_uuid = arrayItemData?.get(i)?.test_master_id
                details.comments = arrayItemData?.get(i)?.commands
                details.is_emar = arrayItemData?.get(i)?.drug_active
                detailsList.add(details)
            }
            header?.department_uuid = department_UUID.toString()
            header?.doctor_uuid = userDataStoreBean?.uuid.toString()
            header?.encounter_type_uuid = encounter_Type!!.toInt()
            header?.encounter_uuid = encounter_uuid!!.toInt()
            header?.dispense_status_uuid = 1
            header?.patient_uuid = patient_UUID.toString()
            header?.prescription_status_uuid = 2
            header?.store_master_uuid = StoreMasterID.toString()
            header?.encounter_doctor_uuid = doctor_en_uuid.toString()

            if (IsTablet) {
                header?.notes = binding!!.notes!!.text.toString()
            }
            header?.is_digitally_signed = true

            header?.is_discharge_medication = PageType != 0

            if (isInj) {

                header?.injection_room_uuid = isInjectionId
            }

            emrPrescriptionRequestModel?.header = this.header!!
            emrPrescriptionRequestModel?.details = this.detailsList
            viewModel?.prescriptiondataInsert(
                facility_id,
                emrPrescriptionRequestModel!!,
                emrprescriptionpostRetrofitCallback
            )


        } else {


            if (isModify) {

                var updateDetails: ArrayList<PresUpdatedetail> = ArrayList()

                var Updateheader: PresUpdateheader = PresUpdateheader(
                    uuid = prescriptionId,
                    injection_room_uuid = injection_room_uuid.toString()
                )

                for (i in saveData!!.indices) {

                    val details: PresUpdatedetail = PresUpdatedetail()

                    var prescribed_quantity_Data: Int = 0

                    val period = saveData?.get(i)?.PrescriptiondurationPeriodId

                    var prescribed_quantity = saveData?.get(i)?.perdayquantityPrescription!!

                    details.uuid = saveData?.get(i)?.diagnosisUUiD

                    details.prescription_uuid = prescriptionId

                    if (saveData?.get(i)?.drug_active!!) {

                        details.drug_instruction_uuid =
                            saveData?.get(i)?.selectInvestID!!


                        if (isInjectionId == "") {
                            isInjectionId = saveData?.get(i)?.selectInvestID.toString()
                        } else {
                            if (isInjectionId == saveData?.get(i)?.selectInvestID.toString()) {
                                isInjectionId = saveData?.get(i)?.selectInvestID.toString()

                            } else {

                                Toast.makeText(
                                    context,
                                    "Injection must be same",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                return
                            }

                        }

                        isInj = true
                    } else {

                        details.drug_instruction_uuid =
                            saveData?.get(i)?.selectInvestID

                        //       details.administer_detail_status_uuid = 0

                    }



                    details.drug_route_uuid = saveData?.get(i)?.selectRouteID ?: 0
                    details.drug_frequency_uuid =
                        saveData?.get(i)?.selecteFrequencyID ?: 0
                    details.duration_period_uuid =
                        saveData?.get(i)?.PrescriptiondurationPeriodId ?: 0
                    //  details.drug_instruction_uuid = arrayItemData?.get(i)?.selectInvestID.toString()
                    details.duration = saveData?.get(i)?.duration!!.toInt()
                    details.prescribed_quantity = saveData?.get(i)?.drug_quantity!!
                    details.item_master_uuid = saveData?.get(i)?.test_master_id
                    details.comments = saveData?.get(i)?.commands
                    details.is_emar = saveData?.get(i)?.drug_active
                    updateDetails.add(details)
                }
                header?.department_uuid = department_UUID.toString()
                header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                header?.encounter_type_uuid = encounter_Type!!.toInt()
                header?.encounter_uuid = encounter_uuid!!.toInt()
                header?.dispense_status_uuid = 1
                header?.patient_uuid = patient_UUID.toString()
                header?.prescription_status_uuid = 2
                header?.store_master_uuid = StoreMasterID.toString()
                header?.encounter_doctor_uuid = doctor_en_uuid.toString()

                if (IsTablet) {
                    header?.notes = binding!!.notes!!.text.toString()
                }
                header?.is_digitally_signed = true

                header?.is_discharge_medication = PageType != 0

                if (isInj) {

                    header?.injection_room_uuid = isInjectionId
                }


                var emrPrescriptionUpdateRequest = PrescriptionUpdateRequest(
                    header = Updateheader,
                    details = updateDetails
                )


                viewModel?.prescriptiondataUpdate(
                    facility_id,
                    emrPrescriptionUpdateRequest,
                    emrprescriptionpostRetrofitCallback
                )


            } else {

                for (i in saveData!!.indices) {

                    val details: Detail = Detail()

                    var prescribed_quantity_Data: Int = 0

                    val period = saveData?.get(i)?.PrescriptiondurationPeriodId

                    var prescribed_quantity = saveData?.get(i)?.perdayquantityPrescription!!

                    if (saveData?.get(i)?.drug_active!!) {

                        details.injection_room_uuid =
                            saveData?.get(i)?.selectInvestID.toString()

                        details.administer_detail_status_uuid = 1

                        if (isInjectionId == "") {
                            isInjectionId = saveData?.get(i)?.selectInvestID.toString()
                        } else {
                            if (isInjectionId == saveData?.get(i)?.selectInvestID.toString()) {
                                isInjectionId = saveData?.get(i)?.selectInvestID.toString()

                            } else {

                                Toast.makeText(
                                    context,
                                    "Injection must be same",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                return
                            }

                        }

                        isInj = true
                    } else {

                        details.drug_instruction_uuid =
                            saveData?.get(i)?.selectInvestID.toString()

                        details.administer_detail_status_uuid = 0

                    }



                    details.drug_route_uuid = saveData?.get(i)?.selectRouteID.toString()
                    details.drug_frequency_uuid =
                        saveData?.get(i)?.selecteFrequencyID.toString()
                    details.duration_period_uuid =
                        saveData?.get(i)?.PrescriptiondurationPeriodId.toString()
                    //  details.drug_instruction_uuid = arrayItemData?.get(i)?.selectInvestID.toString()
                    details.duration = saveData?.get(i)?.duration
                    details.prescribed_quantity = saveData?.get(i)?.drug_quantity!!
                    details.item_master_uuid = saveData?.get(i)?.test_master_id
                    details.comments = saveData?.get(i)?.commands
                    details.is_emar = saveData?.get(i)?.drug_active
                    detailsList.add(details)
                }
                header?.department_uuid = department_UUID.toString()
                header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                header?.encounter_type_uuid = encounter_Type!!.toInt()
                header?.encounter_uuid = encounter_uuid!!.toInt()
                header?.dispense_status_uuid = 1
                header?.patient_uuid = patient_UUID.toString()
                header?.prescription_status_uuid = 2
                header?.store_master_uuid = StoreMasterID.toString()
                header?.encounter_doctor_uuid = doctor_en_uuid.toString()

                if (IsTablet) {
                    header?.notes = binding!!.notes!!.text.toString()
                }
                header?.is_digitally_signed = true

                header?.is_discharge_medication = PageType != 0

                if (isInj) {

                    header?.injection_room_uuid = isInjectionId
                }

                emrPrescriptionRequestModel?.header = this.header!!
                emrPrescriptionRequestModel?.details = this.detailsList
                viewModel?.prescriptiondataInsert(
                    facility_id,
                    emrPrescriptionRequestModel!!,
                    emrprescriptionpostRetrofitCallback
                )

            }


        }


        /*val responseobject = Gson().toJson(emrPrescriptionRequestModel)
        Log.e("PrescReq",responseobject.toString())*/
    }

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    var encounterResponseContent = response.body()?.responseContents!!
                    doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_uuid = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid!!)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
                    savePrescription()

                } else {
                    viewModel?.createEncounter(
                        patient_UUID!!,
                        encounter_Type!!,
                        createEncounterRetrofitCallback
                    )
                }
            }

            override fun onBadRequest(response: Response<FectchEncounterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FectchEncounterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FectchEncounterResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message!!
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


    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


            doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patient_UUID =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            savePrescription()
        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CreateEncounterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CreateEncounterResponseModel::class.java
                )
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       responseModel.message!!
                   )*/
            } catch (e: Exception) {
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       getString(R.string.something_went_wrong)
                   )*/
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            /* utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 getString(R.string.something_went_wrong)
             )*/
        }

        override fun onUnAuthorized() {
            /*    utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )*/
        }

        override fun onForbidden() {
            /*   utils?.showToast(
                   R.color.negativeToast,
                   binding?.mainLayout!!,
                   getString(R.string.something_went_wrong)
               )*/
        }

        override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    fun showDropDown() {
        slideDown(requireContext(), binding?.addMedicineDetailLayout!!)
        binding?.addMedicineDetailLayout?.show()
        binding?.imgDownPatient?.rotation = 270F
    }

    fun hideDropDown() {
        slideDown(requireContext(), binding?.addMedicineDetailLayout!!)
        binding?.imgDownPatient?.rotation = 90F
        binding?.addMedicineDetailLayout?.hide()
    }

    override fun onTempID(position: Int) {

    }

    override fun onRefresh() {

        prescriptionMobileAdapter?.clearall()

        mCallbackLabTemplateFragment?.GetTemplateDetails()

        mCallbackPresFavFragment?.ClearAllData()
        mCallbackPrevTemplateFragment?.ClearAllData()


    }


    private fun toast(s: String) {

        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()

    }
}