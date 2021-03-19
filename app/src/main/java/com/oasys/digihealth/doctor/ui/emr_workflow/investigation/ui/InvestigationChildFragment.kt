package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.component.extention.*
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentInvestigationChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.requset_model.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.requset_model.Header
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.requset_model.InvestigationRequset
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.models.*
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.TypeDropDownAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.GetToLocationTestResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RadiologyEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.ui.PrevInvestigationFragment
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.fragment_lab_child_new.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InvestigationChildFragment : Fragment(), InvestigationFavouriteFragment.FavClickedListener,
    PrevInvestigationFragment.InvestigationPrevClickedListener,
    InvestigationCommentDialogFragment.CommandClickedListener,
    InvestigationTempleteFragment.TempleteClickedListener,
    ManageInvestTemplateFragment.OnTemplateRefreshListener {

    private var arrayItemData: ArrayList<FavouritesModel?>? = null
    private var modifiyPosition: Int = 0
    private var labtypeDropDownAdapter: TypeDropDownAdapter? = null
    private var binding: FragmentInvestigationChildBinding? = null
    private var viewModel: InvestigationViewModel? = null
    private var utils: Utils? = null
    private var responseContents: String? = ""
    private var favmodel: FavouritesModel? = null
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    var investigationAdapter: InvestigationAdapter? = null
    var investigationMobileAdapter: InvestigationMobileAdapter? = null
    private var fragmentBackClick: FragmentBackClick? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var mCallbackLabFavFragment: ClearFavParticularPositionListener? = null
    var mCallbackLabTemplateStatus: ClearTemplateParticularPositionListener? = null
    var mCallbackLabTemplateFragment: ClearTemplateParticularPositionListener? = null
    var arrayList: ArrayList<FavouritesModel>? = null
    private var facility_id: Int? = 0
    var prevOrder: Boolean = false
    var isfav: Boolean? = null
    private var customdialog: Dialog? = null
    lateinit var drugNmae: TextView
    var searchposition: Int = 0
    var modify_patient_order_uuid: Int = 0
    var facilityID: Int = 0
    private var doctor_uuid = -1
    private var uuid: Int? = 0
    private var to_location_uuid = ""
    private var to_department_uuid = ""

    private var department_UUID: Int? = 0
    private var mode: Int = 0

    lateinit var SpinnerToLocation: Spinner
    private var searchPosition: Int? = 0
    var starttime: String = ""

    var patient_UUID: Int? = 0
    var encounter_Type: Int? = 0
    var encounter_uuid: Int? = 0
    var encounter_doctor_uuid: Int? = 0

    private var testMastertId: Int? = 0
    private var test_master_uuid: Int? = 0

    private var addFavouritesModel: FavouritesModel = FavouritesModel()


    var selectedResponseContent: InvestigationAddData = InvestigationAddData()
    var investigationTypeList: ArrayList<InvestigationTypeResponseContent> = ArrayList()

    var investigationLocationList: ArrayList<InvestigationLocationResponseContent> = ArrayList()

    private var priorityList = mutableMapOf<Int, String>()
    private val priorityNameHashMapUUid: HashMap<Int, Int> = HashMap()


    private var priorityListDummy = mutableMapOf<Int, String>()
    private var orderToLocationListDummy = mutableMapOf<Int, String>()

    private var orderToLocationList = mutableMapOf<Int, String>()
    private val orderLocationHashMap: HashMap<Int, Int> = HashMap()


    val detailsList = ArrayList<Detail>()

    var isOrderLocationValidate = false
    var itemcode: Boolean = false

    val header: Header? = Header()

    private var customProgressDialog: CustomProgressDialog? = null

    val emrRequestModel: InvestigationRequset? = InvestigationRequset()


    var isModify: Boolean = false

    var existing_details: ArrayList<InvExistingDetail>? = ArrayList()
    var new_details: ArrayList<InvNewDetail>? = ArrayList()
    var removed_details: ArrayList<InvRemovedDetail>? = ArrayList()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_investigation_child,
                container,
                false
            )

        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        viewModel = InvestigationViewModelFactory(
            requireActivity().application
        )
            .create(InvestigationViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val labMasterTypeUUID = appPreferences?.getInt(AppConstants.LAB_MASTER_TYPE_ID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        encounter_doctor_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        appPreferences?.saveInt(AppConstants.LAB_MASTER_TYPE_ID, 3)
        utils = Utils(requireContext())

        trackInvestigationVisit(utils!!.getEncounterType())

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        starttime = sdf.format(Date())

        setupViewPager(binding?.viewpager!!)

        if (isTablet(requireContext()))
            tabletView()
        else
            mobileView()

        return binding!!.root
    }

    fun mobileView() {

        viewModel?.getLabType(getLabTypeRetrofitCallback, facilityID)
        viewModel?.getToLocation(getLabToLoctionRetrofitCallback, facilityID)
        initView()
        initCompleteCompleteTextView()
        initAddView()
        setSpinnerClickListiner()
        setMobAdapter()

    }

    private fun setMobAdapter() {
        investigationMobileAdapter =
            InvestigationMobileAdapter(
                requireActivity(),
                ArrayList()
            )

        binding?.saveinvRecyclerView?.adapter = investigationMobileAdapter


        investigationMobileAdapter?.setOnItemClickListener(object :
            InvestigationMobileAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: InvestigationListData?,
                position: Int,
                selected: Boolean
            ) {

                if (isModify) {

                    var check =
                        existing_details!!.any { it.test_master_uuid == responseContent?.investigation_id }
                    if (check) {
                        binding?.actCode?.isEnabled = false
                        binding?.actCodeTestName?.isEnabled = false
                    }
                }

                investigationMobileAdapter?.updateSelectStatus(position, selected)
                if (!selected)
                    editIvtItem(position, setNewData(responseContent!!))
                else {
                    clearAddAll()
                    hideDropDown()
                }

            }
        })



        investigationMobileAdapter?.setOnCommandClickListener(object :
            InvestigationMobileAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String) {

                //     val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = InvestigationCommentDialogFragment()

                val ft = childFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putString("commands", Command)

                dialog.arguments = bundle
                dialog.show(ft, "Tag")


            }
        })

        investigationMobileAdapter?.setOnitemChangeListener(object :
            InvestigationMobileAdapter.OnitemChangeListener {
            override fun onitemChangeClick(uuid: ArrayList<Int>) {

                mCallbackLabFavFragment?.checkanduncheck(uuid, true)

                mCallbackLabTemplateStatus?.checkanduncheck(uuid)

            }
        })


        investigationMobileAdapter?.setOnDeleteClickListener(object :
            InvestigationMobileAdapter.OnDeleteClickListener {

            override fun onDeleteClick(responseContent: InvestigationListData?, position: Int) {


                if (isModify) {

                    var check =
                        existing_details!!.any { it.test_master_uuid == responseContent?.investigation_id }
                    if (check) {

                        removed_details?.add(InvRemovedDetail(uuid = responseContent?.investigation_id))

                        for (i in existing_details?.indices!!) {

                            if (existing_details!![i].uuid == responseContent?.investigation_id) {

                                existing_details!!.removeAt(i)

                                break

                            }

                        }


                    }
                }

                investigationMobileAdapter?.deleteRow(position)


                Toast.makeText(
                    requireContext(),
                    "${responseContent!!.investigation_name} Deleted Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                /*
                if()
                if(responseContent?.isFav!!)
                    mCallbackLabFavFragment?.ClearFavParticularPosition(responseContent.favPos!!)*/
            }
        })


    }

    private fun editIvtItem(position: Int, responseContent: InvestigationAddData?) {

        selectedResponseContent = responseContent!!

        selectedResponseContent.isEditableMode = true

        refreshList()

    }

    private fun refreshList() {

        showDropDown()

        setPriority()
        settoLocation()
        binding?.actCodeTestName?.setText(selectedResponseContent.investigation_name)
        binding?.actCode?.setText(selectedResponseContent.investigation_code)
        for (i in 0..investigationTypeList.size - 1) {

            if (investigationTypeList[i].uuid == selectedResponseContent.selectTypeUUID) {

                binding?.spinnerPriority?.setSelection(i + 1)
            }

        }
        for (i in 0..investigationLocationList.size - 1) {

            if (investigationLocationList[i].uuid == selectedResponseContent.selectToLocationUUID) {

                binding?.spinnerOrderLocation?.setSelection(i + 1)
            }

        }

    }

    fun tabletView() {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        starttime = sdf.format(Date())

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )


        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }

        binding?.saveCardView?.isEnabled = true
        binding?.clearCardView?.isEnabled = true
        binding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setAdapter()

        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)

        viewModel?.getLabType(getLabTypeRetrofitCallback, facilityID)
        viewModel?.getToLocation(getLabToLoctionRetrofitCallback, facilityID)

        customProgressDialog = CustomProgressDialog(this.context)

        investigationAdapter?.setOnCommandClickListener(object :
            InvestigationAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String) {

                //     val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = InvestigationCommentDialogFragment()

                val ft = childFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putString("commands", Command)

                dialog.arguments = bundle
                dialog.show(ft, "Tag")


            }
        })

        binding?.saveCardView!!.setOnClickListener {

            trackInvestigationSaveStart(utils!!.getEncounterType())
            arrayItemData = investigationAdapter?.getItems()

            detailsList.clear()


            val datasize: Int = arrayItemData?.size!!

            if (datasize > 1) {
                for (i in 0..(datasize - 2)) {
                    val details: Detail = Detail()
                    details.group_uuid = 0
                    details.is_profile = false
                    details.lab_master_type_uuid = AppConstants.INVESTIGATION_MASTER_ID
                    details.order_priority_uuid =
                        arrayItemData?.get(i)?.selectTypeUUID!!.toString()
                    details.profile_uuid = ""
                    details.test_master_uuid = arrayItemData?.get(i)?.investigation_id!!

                    details.to_department_uuid = to_department_uuid.toString()
                    details.to_location_uuid =
                        to_location_uuid
                    details.to_sub_department_uuid = 0

                    detailsList.add(details)
                }


                header?.consultation_uuid = 0
                header?.department_uuid = department_UUID.toString()
                header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                header?.encounter_type_uuid = encounter_Type!!.toInt().toString()
                header?.encounter_uuid = encounter_uuid!!.toInt().toString()
                header?.encounter_doctor_uuid = encounter_doctor_uuid!!.toInt().toString()
                header?.facility_uuid = facility_id.toString()
                header?.lab_master_type_uuid = AppConstants.INVESTIGATION_MASTER_ID
                header?.order_status_uuid = 0
                header?.order_to_location_uuid = 1
                header?.patient_treatment_uuid = 0
                header?.patient_uuid = patient_UUID.toString()
                header?.sub_department_uuid = 0
                header?.treatment_plan_uuid = 0
                header?.tat_session_end = starttime
                header?.tat_session_start = sdf.format(Date())


                emrRequestModel?.header = this.header!!
                emrRequestModel?.details = this.detailsList

                val requestmodel = Gson().toJson(emrRequestModel)

                Log.i("", "" + requestmodel)
                viewModel?.getEncounter(
                    facility_id!!,
                    patient_UUID!!,
                    department_UUID!!,
                    encounter_Type!!,
                    getEncounterByDocAndPatientIdRespCallback
                )
                // }
            } else {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Please select any one item"
                )
            }


        }


        binding?.clearCardView?.setOnClickListener {
            mCallbackLabFavFragment?.ClearAllData()
            investigationAdapter?.clearall()
            investigationAdapter?.addRow(FavouritesModel())
            investigationAdapter?.addTempleteRow(TempDetails())
        }

    }


    fun initView() {
        binding?.llDropDownView?.hide()
        binding?.rlHeader?.setOnClickListener {
            if (binding?.llDropDownView?.isvisible()!!) {
                hideDropDown()
            } else {
                showDropDown()
            }
        }


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



        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)

        binding?.drawerLayout!!.openDrawer(GravityCompat.END)
    }

    fun initCompleteCompleteTextView() {

        binding?.actCodeTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (binding?.actCodeTestName?.hasFocus()!!) {
                    if (s.length > 2 && s.length < 5) {
                        if (mode != 2) {

                            itemcode = false
                            viewModel?.getInvestigationSearchResult(
                                facility_id,
                                s.toString(),
                                getComplaintSearchRetrofitCallBack
                            )
                        }
                    }

                }
            }
        })
        binding?.actCode?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 3) {
                    if (mode != 2) {
                        itemcode = true
                        viewModel?.getInvestigationSearchResult(
                            facility_id,
                            s.toString(),
                            getComplaintSearchRetrofitCallBack
                        )
                    }
                }
            }
        })
    }

    fun setSpinnerClickListiner() {
        binding?.spinnerOrderLocation?.onItemSelectedListener =
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
                    selectedResponseContent.selectToLocationUUID =
                        orderToLocationList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedResponseContent.selectedLocationName = itemValue

                    binding?.spinnerOrderLocation?.setSelection(pos)
                }

            }


        binding?.spinnerPriority?.onItemSelectedListener =
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
                    selectedResponseContent.selectTypeUUID =
                        priorityList.filterValues { it == itemValue }.keys.toList()[0]

                    selectedResponseContent.selectTypeName = itemValue

                    binding?.spinnerPriority?.setSelection(pos)

                    Log.i(
                        "LabType",
                        "name = " + itemValue + "uuid=" + priorityList.filterValues { it == itemValue }.keys.toList()[0]
                    )
                }
            }


    }

    fun initAddView() {
        binding?.add?.setOnClickListener {
            if (act_code.text.isEmpty() || act_code_test_name.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please Select TestName ", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedResponseContent.selectTypeUUID == 0) {
                Toast.makeText(requireContext(), "Please Select Priority", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedResponseContent.selectToLocationUUID == 0) {
                Toast.makeText(
                    requireContext(),
                    "Please Select Order To Location",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (selectedResponseContent.isEditableMode!!) {
                    selectedResponseContent.isEditableMode = false
                    selectedResponseContent.labDataSelected = false

                    if (isModify) {

                        for (i in existing_details!!.indices) {

                            if (existing_details!![i].test_master_uuid == selectedResponseContent.investigation_id) {

                                var data = existing_details!![i]
                                data.order_priority_uuid = selectedResponseContent.selectTypeUUID
                                data.to_location_uuid = selectedResponseContent.selectToLocationUUID
                                existing_details!![i] = data
                                break
                            }
                        }

                    }
                    var new: InvestigationListData = setListData(selectedResponseContent)
                    investigationMobileAdapter?.updateData(new)

                    clearAddAll()

                } else {
                    val check =
                        investigationMobileAdapter!!.checkData(selectedResponseContent.investigation_id!!)

                    if (!check) {


                        if (isModify) {

                            new_details?.add(
                                InvNewDetail(
                                    test_master_uuid = selectedResponseContent.investigation_id.toString(),
                                    to_location_uuid = selectedResponseContent.selectToLocationUUID,
                                    order_priority_uuid = selectedResponseContent.selectTypeUUID,
                                    is_ordered = 1,
                                    patient_order_uuid = modify_patient_order_uuid
                                )
                            )
                        }

                        var new: InvestigationListData = setListData(selectedResponseContent)
                        investigationMobileAdapter?.Add(new)



                        clearAddAll()
                    } else {
                        Toast.makeText(
                            context,
                            "Already Item available in the list",
                            Toast.LENGTH_LONG
                        )?.show()

                    }
                }

            }

        }


        binding?.saveCardView!!.setOnClickListener {


            if (!isModify) {

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                starttime = sdf.format(Date())

                val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

                trackInvestigationSaveStart(utils!!.getEncounterType())
                var mobarrayItemData = investigationMobileAdapter?.getItems()

                detailsList.clear()


                val datasize: Int = mobarrayItemData?.size!!

                if (datasize > 0) {
                    for (i in 0..(datasize - 1)) {
                        val details: Detail = Detail()
                        details.group_uuid = 0
                        details.is_profile = false
                        details.lab_master_type_uuid = AppConstants.INVESTIGATION_MASTER_ID
                        details.order_priority_uuid =
                            mobarrayItemData.get(i).selectTypeUUID.toString()
                        details.profile_uuid = ""
                        details.test_master_uuid = mobarrayItemData.get(i).investigation_id!!

                        details.to_department_uuid = department_UUID.toString()
                        details.to_location_uuid =
                            mobarrayItemData.get(i).selectToLocationUUID.toString()
                        details.to_sub_department_uuid = 0

                        detailsList.add(details)
                    }


                    header?.consultation_uuid = 0
                    header?.department_uuid = department_UUID.toString()
                    header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                    header?.encounter_type_uuid = encounter_Type!!.toInt().toString()
                    header?.encounter_uuid = encounter_uuid!!.toInt().toString()
                    header?.encounter_doctor_uuid = encounter_doctor_uuid!!.toInt().toString()
                    header?.facility_uuid = facility_id.toString()
                    header?.lab_master_type_uuid = AppConstants.INVESTIGATION_MASTER_ID
                    header?.order_status_uuid = 0
                    header?.order_to_location_uuid = 1
                    header?.patient_treatment_uuid = 0
                    header?.patient_uuid = patient_UUID.toString()
                    header?.sub_department_uuid = 0
                    header?.treatment_plan_uuid = 0
                    header?.tat_session_end = starttime
                    header?.tat_session_start = sdf.format(Date())


                    emrRequestModel?.header = this.header!!
                    emrRequestModel?.details = this.detailsList

                    val requestmodel = Gson().toJson(emrRequestModel)

                    Log.i("", "" + requestmodel)
                    viewModel?.getEncounter(
                        facility_id!!,
                        patient_UUID!!,
                        department_UUID!!,
                        encounter_Type!!,
                        getEncounterByDocAndPatientIdRespCallback
                    )
                    // }
                } else {
                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        "Please select any one item"
                    )
                }

            } else {

                viewModel?.getEncounter(
                    facility_id!!,
                    patient_UUID!!,
                    encounter_Type!!,
                    fetchEncounterRetrofitCallBack
                )
            }
        }


        binding?.clear?.setOnClickListener {

            isModify = false
            clearAddAll()
        }

        binding?.clearCardView?.setOnClickListener {
            clearAddAll()
            investigationMobileAdapter?.clearall()

        }

        binding?.saveTemplateCardView?.setOnClickListener {


            if (investigationMobileAdapter?.getItems()?.size ?: 0 > 0) {

                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = ManageInvestTemplateFragment()
                val bundle = Bundle()
                bundle.putString("status", "Add")

                var getData = investigationMobileAdapter?.getItems()
                bundle.putParcelableArrayList("ListData", getData)
                labtemplatedialog.arguments = bundle
                labtemplatedialog.show(ft, "Tag")

            } else {

                Toast.makeText(requireContext(), "Please select any one item", Toast.LENGTH_SHORT)
                    .show()

            }

        }

    }

    private fun setListData(data: InvestigationAddData): InvestigationListData {

        return InvestigationListData(
            selectToLocationUUID = data.selectToLocationUUID,
            selectedLocationName = data.selectedLocationName,
            selectTypeUUID = data.selectTypeUUID,
            selectTypeName = data.selectTypeName ?: "",
            isReadyForSave = data.isReadyForSave ?: false,
            isEditableMode = data.isEditableMode ?: false,
            labDataSelected = data.labDataSelected ?: false,
            investigation_id = data.investigation_id ?: 0,
            commands = data.commands ?: "",
            investigation_name = data.investigation_name ?: "",
            investigation_code = data.investigation_code ?: "",
            isFav = data.isFav,
            IsTemp = data.IsTemp,
            favPos = data.favPos,
            tempPos = data.tempPos
        )

    }

    private fun setNewData(data: InvestigationListData): InvestigationAddData {

        return InvestigationAddData(
            selectToLocationUUID = data.selectToLocationUUID,
            selectedLocationName = data.selectedLocationName!!,
            selectTypeUUID = data.selectTypeUUID,
            selectTypeName = data.selectTypeName ?: "",
            isReadyForSave = data.isReadyForSave ?: false,
            isEditableMode = data.isEditableMode ?: false,
            labDataSelected = data.labDataSelected ?: false,
            investigation_id = data.investigation_id ?: 0,
            commands = data.commands ?: "",
            investigation_name = data.investigation_name ?: "",
            investigation_code = data.investigation_code ?: "",
            isFav = data.isFav,
            IsTemp = data.IsTemp,
            favPos = data.favPos,
            tempPos = data.tempPos
        )

    }

    fun clearAddAll() {
        binding?.actCodeTestName?.setText("")
        binding?.actCode?.setText("")
        setDummyPrioritySpinner()
        setDummyOrderToLocationSpinner()
        selectedResponseContent = InvestigationAddData()


        binding?.actCode?.isEnabled = true
        binding?.actCodeTestName?.isEnabled = true
    }


    fun setDummyPrioritySpinner() {

        priorityList.clear()

        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                priorityList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerPriority?.adapter = adapter
    }


    fun setDummyOrderToLocationSpinner() {

        orderToLocationList.clear()
        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                orderToLocationList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner_order_location.adapter = adapter
        binding?.spinnerOrderLocation?.setSelection(0)
    }


    private fun settoLocation() {

        orderToLocationList.clear()

        orderToLocationList[0] = ""

        orderToLocationList.putAll(investigationLocationList.map { it.uuid to it.location_name }
            .toMap().toMutableMap())


        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                orderToLocationList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerOrderLocation?.adapter = adapter

    }

    private fun setPriority() {

        priorityList.clear()

        priorityList[0] = ""

        priorityList.putAll(investigationTypeList.map { it.uuid to it.name }.toMap().toMutableMap())


        val adapterspinnner =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                priorityList.values.toMutableList()
            )
        adapterspinnner.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerPriority?.adapter = adapterspinnner
        binding?.spinnerPriority?.setSelection(2)
    }


    fun showDropDown() {
        slideDown(requireContext(), binding?.llDropDownView!!)
        binding?.llDropDownView?.show()
        binding?.ivArrow?.rotation = 270F
    }

    fun hideDropDown() {
        slideDown(requireContext(), binding?.llDropDownView!!)
        binding?.ivArrow?.rotation = 90F
        binding?.llDropDownView?.hide()
    }


    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(InvestigationFavouriteFragment(), "  Favourite")
        adapter.addFragment(InvestigationTempleteFragment(), "  Templete")
        adapter.addFragment(PrevInvestigationFragment(), "Prev.Investigation")
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

    private fun setAdapter() {
        investigationAdapter =
            InvestigationAdapter(
                requireActivity(),
                ArrayList(), ArrayList()
            ) { favortem, position, selected ->
                Log.i("position", "" + position)
                Log.i("selected", "" + selected)
                if (favortem == 1) {
                    mCallbackLabFavFragment?.updateSelectStatus(favortem, position, selected)
                } else {
                    mCallbackLabTemplateStatus?.updatestaus(favortem, position, selected)
                }
            }
        binding?.saveinvRecyclerView?.adapter = investigationAdapter

        investigationAdapter?.addRow(FavouritesModel())
        investigationAdapter?.addTempleteRow(TempDetails())


        investigationAdapter?.setOnDeleteClickListener(object :
            InvestigationAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                Log.i("", "" + responseContent)
                if (customdialog == null) {
                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                        customdialog = null

                    }
                    drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "${drugNmae.text} '" + responseContent?.test_master_name + "(" + responseContent?.test_master_code + ")" + "'Record ?"
                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                    yesBtn.setOnClickListener {

                        investigationAdapter?.deleteRow(position)

                        mCallbackLabFavFragment?.ClearFavParticularPosition(responseContent!!.isFavposition)


                        customdialog!!.dismiss()


                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()
                        customdialog = null

                    }
                    customdialog!!.show()


                }
            }
        })

        investigationAdapter?.setOnListItemClickListener(object :
            RadiologyAdapter.OnListItemClickListener {
            override fun onListItemClick(
                responseContent: FavouritesModel?,
                position: Int,
                spinnerToLocation: Spinner
            ) {
                SpinnerToLocation = spinnerToLocation
                searchPosition = position

                viewModel?.getInvestigationToLocationMapId(
                    department_UUID,
                    responseContent?.investigation_id,
                    facility_id,
                    getLabToLoctionMapIdRetrofitCallback
                )

            }
        })

        investigationAdapter?.setOnSearchInitiatedListener(object :
            InvestigationAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int,
                spinnerToLocation: Spinner

            ) {
                dropdownReferenceView = view
                SpinnerToLocation = spinnerToLocation

                searchposition = position
                viewModel?.getInvestigationSearchResult(
                    facility_id,
                    query,
                    getComplaintSearchRetrofitCallBack
                )
            }
        })


    }

    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<InvestigationSearchResponseModel> {
            override fun onSuccessfulResponse(response: Response<InvestigationSearchResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    if (isTablet(requireContext())) {
                        testMastertId = response.body()?.responseContents?.get(0)?.uuid
                        investigationAdapter?.setAdapter(
                            dropdownReferenceView,
                            (response.body()?.responseContents as ArrayList<InvestigationSearchResponseContent>?)!!,
                            searchposition, SpinnerToLocation
                        )
                    } else {


                        setSearchData(response.body()?.responseContents!!)


                    }
                }

            }

            override fun onBadRequest(response: Response<InvestigationSearchResponseModel>) {

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

    private fun setSearchData(responseContents: List<InvestigationSearchResponseContent>) {


        if (itemcode) {


            val responseContentAdapter = InvestigationSearchCodeResultAdapter(
                requireContext(),
                R.layout.row_chief_complaint_search_result,
                responseContents as ArrayList<InvestigationSearchResponseContent>
            )

            binding!!.actCode!!.threshold = 1
            binding?.actCode?.setAdapter(responseContentAdapter)
            binding!!.actCode?.showDropDown()

            binding?.actCode?.setOnItemClickListener { parent, _, position, id ->
                val selectedPoi =
                    parent.adapter.getItem(position) as InvestigationSearchResponseContent?


                val check = investigationMobileAdapter!!.checkData(selectedPoi?.uuid!!)

                //        favouritesArrayList.any { it!!.investigation_id == selectedPoi?.uuid }

                if (!check) {

                    binding?.actCode?.setText(selectedPoi.code)
                    binding?.actCodeTestName?.setText(selectedPoi.name)
                    selectedResponseContent.investigation_name = selectedPoi.name
                    selectedResponseContent.investigation_id = selectedPoi.uuid
                    selectedResponseContent.investigation_code = selectedPoi.code.toString()

                    setPriority()
                    settoLocation()


                    viewModel?.getInvestigationToLocationMapId(
                        department_UUID,
                        selectedPoi.uuid,
                        facility_id,
                        getLabToLoctionMapIdRetrofitCallback
                    )


                } else {


                    binding?.actCode?.setText("")
                    binding?.actCodeTestName?.setText("")

                    Toast.makeText(
                        context,
                        "Already Item available in the list",
                        Toast.LENGTH_LONG
                    )
                        ?.show()

                }
            }
        } else {


            val responseContentAdapter = InvestigationSearchResultAdapter(
                requireContext(),
                R.layout.row_chief_complaint_search_result,
                responseContents as ArrayList<InvestigationSearchResponseContent>
            )

            binding!!.actCodeTestName!!.threshold = 1
            binding?.actCodeTestName?.setAdapter(responseContentAdapter)
            binding!!.actCodeTestName!!.showDropDown()


            binding?.actCodeTestName?.setOnItemClickListener { parent, _, position, id ->
                val selectedPoi =
                    parent.adapter.getItem(position) as InvestigationSearchResponseContent?

                val check = investigationMobileAdapter!!.checkData(selectedPoi?.uuid!!)

                //       favouritesArrayList.any { it!!.investigation_id == selectedPoi?.uuid }

                if (!check) {


                    binding?.actCode?.setText(selectedPoi.code)
                    binding?.actCodeTestName?.setText(selectedPoi.name)
                    selectedResponseContent.investigation_name = selectedPoi.name
                    selectedResponseContent.investigation_id = selectedPoi.uuid
                    selectedResponseContent.investigation_code = selectedPoi.code.toString()

                    setPriority()
                    settoLocation()

                    viewModel?.getInvestigationToLocationMapId(
                        department_UUID,
                        selectedPoi.uuid,
                        facility_id,
                        getLabToLoctionMapIdRetrofitCallback
                    )


                } else {


                    binding?.actCode?.setText("")
                    binding?.actCodeTestName?.setText("")

                    Toast.makeText(
                        context,
                        "Already Item available in the list",
                        Toast.LENGTH_LONG
                    )
                        ?.show()

                }
            }


        }
    }


    val getLabTypeRetrofitCallback = object : RetrofitCallback<InvestigationTypeResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<InvestigationTypeResponseModel>?) {


            if (isTablet(requireContext()))
                investigationAdapter?.setadapterTypeValue(responseBody?.body()?.responseContents)
            else
                investigationTypeList =
                    (responseBody?.body()?.responseContents as ArrayList<InvestigationTypeResponseContent>?)!!


        }

        override fun onBadRequest(errorBody: Response<InvestigationTypeResponseModel>?) {

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

        }

    }


    val getLabToLoctionRetrofitCallback =
        object : RetrofitCallback<InvestigationLoationResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<InvestigationLoationResponseModel>?) {
                if (!responseBody?.body()?.responseContents.isNullOrEmpty()) {

                    if (isTablet(requireContext())) {

                        val investigationLocationLists: ArrayList<InvestigationLocationResponseContent> =
                            ArrayList()
                        val resInvestigation =
                            InvestigationLocationResponseContent(uuid = 0, location_name = "")
                        investigationLocationLists.add(0, resInvestigation)
                        responseBody?.body()?.also {
                            it.responseContents.forEach { resContent ->
                                investigationLocationLists.add(resContent)
                                to_location_uuid = resContent.uuid.toString()
                            }
                            investigationAdapter?.setToLocationList(investigationLocationLists)
                        }

                    } else {

                        investigationLocationList.clear()

                        investigationLocationList =
                            responseBody?.body()?.responseContents as ArrayList<InvestigationLocationResponseContent>

                    }
                }

            }

            override fun onBadRequest(errorBody: Response<InvestigationLoationResponseModel>?) {
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
            }

        }


    val getLabToLoctionMapIdRetrofitCallback =
        object : RetrofitCallback<GetToLocationTestResponse> {
            override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) =
                if (isTablet(requireContext())) {
                    investigationAdapter?.setToLocation(
                        responseBody?.body()?.responseContents,
                        SpinnerToLocation,
                        searchPosition
                    )

                    to_department_uuid =
                        responseBody?.body()?.responseContents?.to_department_uuid!!.toString()

                } else {

                    selectedResponseContent.selectToLocationUUID =
                        responseBody?.body()?.responseContents?.to_location_uuid!!


                    for (i in 0 until investigationLocationList.size) {


                        if (selectedResponseContent.selectToLocationUUID == investigationLocationList[i].uuid) {

                            binding?.spinnerOrderLocation?.setSelection(i + 1)
                            break
                        }
                    }


                }

            override fun onBadRequest(errorBody: Response<GetToLocationTestResponse>?) {
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
            }

        }


    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    var encounterResponseContent = response.body()?.responseContents!!
                    doctor_uuid = encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_uuid = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_uuid)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
                    saveInv()

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


            doctor_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patient_UUID =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveInv()
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


    fun saveInv() {


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        starttime = sdf.format(Date())

        for (i in new_details?.indices!!) {

            new_details!![i].encounter_uuid = encounter_uuid
            new_details!![i].doctor_uuid = doctor_uuid
            new_details!![i].encounter_type_uuid = encounter_Type.toString()
//             new_details!![i].from_department_uuid=department_UUID
            new_details!![i].to_department_uuid = department_UUID
            new_details!![i].patient_order_uuid = patient_UUID
            new_details!![i].order_request_date = this.starttime
            new_details!![i].lab_master_type_uuid = AppConstants.INVESTIGATION_MASTER_ID

        }


        var req = InvUpdateRequest(
            new_details = new_details,
            existing_details = existing_details,
            removed_details = removed_details

        )
        viewModel?.UpdateInv(req, updareInvRetrofitCallback)


    }


    private val updareInvRetrofitCallback =
        object : RetrofitCallback<SimpleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {

                if (isTablet(requireContext())) {


                } else {

                    isModify = false

                    investigationMobileAdapter?.clearall()
                }

            }

            override fun onBadRequest(errorBody: Response<SimpleResponseModel>?) {

            }

            override fun onServerError(response: Response<*>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.frameLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.frameLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.frameLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.frameLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    private val getEncounterByDocAndPatientIdRespCallback =
        object : RetrofitCallback<RadiologyEncounterResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<RadiologyEncounterResponseModel>?) {

                if (!responseBody?.body()?.responseContents.isNullOrEmpty()) {

                    uuid = responseBody?.body()?.responseContents?.get(0)?.uuid ?: -1
                    doctor_uuid =
                        responseBody?.body()?.responseContents?.get(0)?.encounter_doctors?.get(0)?.uuid
                            ?: -1
                    Log.i("uuid", "___" + uuid)
                    Log.i("doctor_uuid", "___" + doctor_uuid)

                }
                viewModel?.investigationInsert(
                    facility_id,
                    emrRequestModel!!,
                    emrpostRetrofitCallback
                )

            }

            override fun onBadRequest(errorBody: Response<RadiologyEncounterResponseModel>?) {

            }

            override fun onServerError(response: Response<*>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.frameLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.frameLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.frameLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.frameLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    val emrpostRetrofitCallback = object : RetrofitCallback<InvestigationPostResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<InvestigationPostResponseModel>?) {

            trackInvestigationSaveComplete(utils?.getEncounterType(), "success", "")
            Log.i("res", "" + responseBody?.body()?.message)

            if (isTablet(requireContext())) {
                test_master_uuid = responseBody?.body()?.responseContents?.get(0)?.uuid

                mCallbackLabFavFragment?.ClearAllData()
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    responseBody?.body()?.message!!
                )
                investigationAdapter?.clearall()
                investigationAdapter?.addRow(FavouritesModel())
                investigationAdapter?.addTempleteRow(TempDetails())

            } else {

                mCallbackLabFavFragment?.ClearAllData()
                investigationMobileAdapter?.clearall()

            }

            Toast.makeText(requireContext(), "Investigation Saved Successfully", Toast.LENGTH_SHORT)
                .show()
            //setAdapter()
        }

        override fun onBadRequest(response: Response<InvestigationPostResponseModel>?) {
        }

        override fun onServerError(response: Response<*>) {
            trackInvestigationSaveComplete(
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

        override fun onUnAuthorized() {
            trackInvestigationSaveComplete(
                utils!!.getEncounterType(),
                "failure",
                getString(R.string.unauthorized)
            )
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            trackInvestigationSaveComplete(
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
            trackInvestigationSaveComplete(utils!!.getEncounterType(), "failure", failure)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    private val investigationInsertRetrofitCallback = object : RetrofitCallback<EmrResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<EmrResponceModel>?) {

            Log.i("res", "" + responseBody?.body()?.message)
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
            investigationAdapter?.clearall()
            investigationAdapter?.addRow(FavouritesModel())
            investigationAdapter?.addTempleteRow(TempDetails())

        }

        override fun onBadRequest(response: Response<EmrResponceModel>?) {

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


    val getToLoctionMapIdRetrofitCallback =
        object : RetrofitCallback<GetToLocationTestResponse> {
            override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {

                if (isfav != null) {

                    if (isfav!!) {


                        for (i in 0 until investigationLocationList.size) {

                            if (responseBody?.body()?.responseContents?.to_location_uuid == investigationLocationList[i].uuid) {

                                addFavouritesModel.selectedLocationName =
                                    investigationLocationList[i].location_name
                                break
                            }

                        }

                        investigationMobileAdapter?.UpdateTolocation(
                            responseBody?.body()?.responseContents,
                            addFavouritesModel.selectedLocationName
                        )


                    } else {

                        for (i in 0 until investigationLocationList.size) {

                            if (responseBody?.body()?.responseContents?.to_location_uuid == investigationLocationList[i].uuid) {

                                addFavouritesModel.selectedLocationName =
                                    investigationLocationList[i].location_name
                                break
                            }

                        }

                        investigationMobileAdapter?.UpdateTolocation(
                            responseBody?.body()?.responseContents,
                            addFavouritesModel.selectedLocationName
                        )


                    }


                }




                if (isTablet(requireContext())) {

                } else {

                    selectedResponseContent.selectToLocationUUID =
                        responseBody?.body()?.responseContents?.to_location_uuid!!

                }
            }

            override fun onBadRequest(errorBody: Response<GetToLocationTestResponse>?) {
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
            }

        }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is InvestigationFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is InvestigationTempleteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackLabFavFragment = childFragment
        }

        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackLabTemplateStatus = childFragment
        }
        if (childFragment is PrevInvestigationFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is InvestigationCommentDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ManageInvestTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }


    }


    override fun sendData(favmodel: FavouritesModel?, position: Int, selected: Boolean) {

        if (isTablet(requireContext())) {
            if (!selected) {
                investigationAdapter!!.addFavouritesInRows(favmodel)
            } else {
                investigationAdapter!!.deleteRow(position)

            }

        } else {
            if (!selected) {


                var check = investigationMobileAdapter?.checkData(favmodel?.test_master_id!!)

                if (!check!!) {


                    favmodel!!.isFavposition = position


                    isfav = true

                    addFavouritesModel = favmodel


                    var addData: InvestigationListData = InvestigationListData(
                        investigation_id = addFavouritesModel.test_master_id,
                        investigation_name = addFavouritesModel.test_master_name,
                        investigation_code = addFavouritesModel.test_master_code,
                        isFav = true,
                        selectTypeUUID = investigationTypeList[1].uuid,
                        selectTypeName = investigationTypeList[1].name,
                        favPos = addFavouritesModel.isFavposition
                    )

                    investigationMobileAdapter!!.Add(addData)


                    viewModel?.getInvestigationToLocationMapId(
                        department_UUID,
                        favmodel.test_master_id!!,
                        facility_id,
                        getToLoctionMapIdRetrofitCallback
                    )


                } else {

                    Toast.makeText(requireContext(), "Item already exits", Toast.LENGTH_SHORT)
                        .show()

                }

            } else {
                investigationMobileAdapter!!.removeFromFav(favmodel?.test_master_id!!)

            }

        }
    }

    override fun sendPrevtoChild(
        responseContent: List<com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.PodArrResult>?,
        isModifyData: Boolean
    ) {
        prevOrder = true


        isModify = isModifyData

        existing_details?.clear()

        new_details?.clear()

        removed_details?.clear()


        if (isTablet(requireContext())) {

            investigationAdapter!!.clearallAddone()

            for (i in responseContent!!.indices) {
                val favmodel: FavouritesModel? = FavouritesModel()
                favmodel!!.investigation_name = responseContent[i].name
                favmodel.itemAppendString = responseContent[i].name
                favmodel.test_master_name = responseContent[i].name
                favmodel.test_master_id = responseContent[i].test_master_uuid
                favmodel.test_master_code = responseContent[i].code
                favmodel.selectTypeUUID = responseContent[i].order_priority_uuid
                favmodel.selectToLocationUUID = responseContent[i].order_to_location_uuid
                investigationAdapter!!.addFavouritesInRows(favmodel)
            }

        } else {

            investigationMobileAdapter!!.clearall()

            for (i in responseContent!!.indices) {

                val addData: InvestigationListData = InvestigationListData(
                    investigation_name = responseContent[i].name,
                    investigation_id = responseContent[i].test_master_uuid,
                    investigation_code = responseContent[i].code,
                    selectTypeUUID = responseContent[i].order_priority_uuid,
                    selectTypeName = responseContent[i].order_priority_name.toString(),
                    selectToLocationUUID = responseContent[i].order_to_location_uuid,
                    selectedLocationName = responseContent[i].order_to_location.toString(),
                    commands = responseContent[i].order_to_location.toString()

                )

                investigationMobileAdapter!!.Add(addData)



                if (isModify) {

                    var addExit = InvExistingDetail(
                        details_comments = responseContent[i].comments,
                        order_priority_uuid = responseContent[i].order_priority_uuid,
                        patient_order_uuid = responseContent[i].patient_order_uuid,
                        to_location_uuid = responseContent[i].order_to_location_uuid,
                        test_master_uuid = responseContent[i].test_master_uuid
                    )

                    modify_patient_order_uuid = responseContent[i].patient_order_uuid
                    existing_details?.add(addExit)
                }
            }


        }

    }

    fun trackInvestigationVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackInvestigationVisit(type)
    }


    fun trackInvestigationSaveStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackInvestigationSaveStart(type)
    }


    fun trackInvestigationSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackInvestigationSaveComplete(type, status, message)
    }

    override fun sendCommandPosData(position: Int, command: String) {

        if (isTablet(requireContext()))
            investigationAdapter!!.addCommands(position, command)
        else
            investigationMobileAdapter!!.addCommands(position, command)
    }

    override fun sendTemplete(
        templeteDetails: List<InvestDetail?>?,
        position: Int,
        selected: Boolean,
        id: Int
    ) {

        if (isTablet(requireContext())) {
            if (!selected) {
                for (i in templeteDetails!!.indices) {
                    val favmodel: FavouritesModel? = FavouritesModel()
                    favmodel?.viewLabstatus = 2
                    favmodel?.isTemposition = position
                    favmodel?.test_master_name = templeteDetails[i]!!.lab_name
                    favmodel?.test_master_id = templeteDetails[i]!!.lab_test_uuid
                    favmodel?.test_master_code = templeteDetails[i]!!.lab_code
                    favmodel?.template_id = id
                    investigationAdapter!!.addFavouritesInRowModule(2, favmodel, position, selected)

                }
            } else {
                for (i in templeteDetails!!.indices) {
                    investigationAdapter!!.deleteRowFromTemplate(
                        templeteDetails[i]!!.lab_test_uuid,
                        2
                    )
                }

            }
        } else {

            if (!selected) {
                for (i in templeteDetails!!.indices) {

                    var check =
                        investigationMobileAdapter?.checkData(templeteDetails[i]?.lab_test_uuid!!)

                    if (!check!!) {
                        addFavouritesModel = FavouritesModel()

                        isfav = false


                        var addData: InvestigationListData = InvestigationListData(
                            investigation_id = templeteDetails[i]!!.lab_test_uuid,
                            investigation_name = templeteDetails[i]!!.lab_name,
                            investigation_code = templeteDetails[i]!!.lab_code,
                            IsTemp = true,
                            selectTypeUUID = investigationTypeList[1].uuid,
                            selectTypeName = investigationTypeList[1].name,
                            tempPos = position
                        )

                        investigationMobileAdapter!!.Add(addData)

                        viewModel?.getInvestigationToLocationMapId(
                            department_UUID,
                            templeteDetails[i]!!.lab_test_uuid,
                            facility_id,
                            getToLoctionMapIdRetrofitCallback
                        )

                    }

                }
            } else {
                for (i in templeteDetails!!.indices) {


                    var check =
                        investigationMobileAdapter?.checkData(templeteDetails[i]?.lab_test_uuid!!)

                    if (check!!) {
                        investigationMobileAdapter!!.removeFromFav(templeteDetails[i]!!.lab_test_uuid)

                    }
                }

            }


        }
    }

    override fun onTemplateID(position: Int) {

    }

    override fun onRefreshList() {

        mCallbackLabTemplateStatus!!.GetTemplateDetails()

    }

}


