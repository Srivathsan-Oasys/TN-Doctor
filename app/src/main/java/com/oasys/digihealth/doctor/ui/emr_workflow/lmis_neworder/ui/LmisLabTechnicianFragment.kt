package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.ui


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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentLmisLabTechnicianBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request.Header
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request.RequestLmisNewOrder
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.response.GetWardIdResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.response.ResponseLmisListview
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.view_model.LmisNewOrderViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.view_model.LmisNewOrderViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FetchEncounterResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.LabDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.CommentDialogFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.GetToLocationTestResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.ui.PrevLmisLabTechnicianFragment
import com.oasys.digihealth.doctor.ui.landingscreen.MainLandScreenActivity
import com.oasys.digihealth.doctor.ui.quick_reg.model.GetReferenceResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.LocationMasterResponseModelX
import com.oasys.digihealth.doctor.ui.quick_reg.model.ResponseTestMethod
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.LabTestSpinnerResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.LabTestSpinnerresponseContent
import com.oasys.digihealth.doctor.ui.quick_reg.view.LabTestActivity
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class LmisLabTechnicianFragment : Fragment(), LmisLabTechnicianFavouriteFragment.FavClickedListener,
    LmisLabTechnicianTemplateFragment.TempleteClickedListener,
    PrevLmisLabTechnicianFragment.LabPrevClickedListener,
    SaveTemplateManageLmisLabTemplateFragment.OnsaveTemplateRefreshListener,
    CommentDialogFragment.CommandClickedListener {

    private var salutaionvalue: String? = ""
    private var GettingFromTemplate: Int? = 0
    private var doctor_uuid: Int? = 0
    private var savestatus: Boolean? = false
    private var doctorID: Int? = 0
    private var logindoctorID: Int? = 0
    private var statusselect: Boolean? = false
    private var positionstatus: Int? = 0
    private var responsePatientData: ResponseContentslmisorder = ResponseContentslmisorder()
    private var arrayItemData: ArrayList<LabtechData?>? = null
    private var binding: FragmentLmisLabTechnicianBinding? = null
    lateinit var encounterResponseContent: List<FetchEncounterResponseContent?>
    var appPreferences: AppPreferences? = null
    private var viewModel: LmisNewOrderViewModel? = null
    private var utils: Utils? = null
    lateinit var drugNmae: TextView
    private var encounter_uuid: Int? = 0
    private var encounter_doctor_uuid: Int? = 0
    private var listDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMapDepart = mutableMapOf<Int, String>()
    private lateinit var listDepartmentID: List<Int?>
    private var patientId: Int? = null
    private var encounterType: Int? = null
    private var encounterDoctorUuid: Int? = null
    private var encounterUuid: Int? = null
    private var listDoctorNamesItems: ArrayList<DoctorNameResponseContent?> = ArrayList()
    private var DoctorNames = mutableMapOf<Int, String>()
    private var Str_auto_code: String? = ""
    private var Str_auto_id: Int? = 0
    private var Str_auto_name: String? = ""
    val detailsList = ArrayList<Detail>()
    private var mAdapter: LmisLabTechAdapter? = null

    val header: Header? = Header()
    val exsistingDeatil: LabModifiyRequest.ExistingDetail =
        LabModifiyRequest.ExistingDetail()
    val newDetail: LabModifiyRequest.NewDetail =
        LabModifiyRequest.NewDetail()
    val removeData: LabModifiyRequest.RemovedDetail = LabModifiyRequest.RemovedDetail()

    val labModifiyRequest: LabModifiyRequest = LabModifiyRequest()

    var wardUUid: Int = 0
    val emrRequestModel: RequestLmisNewOrder? = RequestLmisNewOrder()
    private var customdialog: Dialog? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var responseContentsItemNavToListvew: ArrayList<LabtechData> = ArrayList()
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    var searchposition: Int = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var mCallbackLabFavFragment: ClearLmisFavParticularPositionListener? = null
    var facility_id: Int = 0
    var mCallbackLmisLabTemplateFragment: ClearTemplateParticularPositionListener? = null
    var mCallbackLabTemplateFragment: ClearTemplateParticularPositionListener? = null

    var removedListFromOriginal: ArrayList<LabtechData?>? = ArrayList()

    var isModifiy: Boolean = false
    var patientOrderUuid: Int? = 0
    private var adapter: ViewPagerAdapter? = null


    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_lmis_lab_technician,
                container,
                false
            )
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val bundle = this.arguments
        if (bundle != null) {
            responsePatientData = bundle.getParcelable<ResponseContentslmisorder>("patiendata")!!
            val title_uuid = responsePatientData.title_uuid
            if (title_uuid != 0) {

                salutaionvalue = when (title_uuid) {
                    0 -> ""
                    1 -> "Mr."
                    2 -> "Mrs."
                    3 -> "Ms."
                    4 -> "Baby Of."
                    5 -> "Master."
                    6 -> "Selvi."
                    7 -> "Dr."
                    8 -> "Baby."
                    9 -> "Shri."

                    else -> ""
                }
            } else {
                val salution = bundle.getInt("salution")

                salutaionvalue = when (salution) {
                    0 -> ""
                    1 -> "Mr."
                    2 -> "Mrs."
                    3 -> "Ms."
                    4 -> "Baby Of."
                    5 -> "Master."
                    6 -> "Selvi."
                    7 -> "Dr."
                    8 -> "Baby."
                    9 -> "Shri."

                    else -> ""
                }
            }

            var gender: String? = ""
            if (responsePatientData.gender_uuid == 1) {
                gender = "Male"
            } else if (responsePatientData.gender_uuid == 2) {
                gender = "Female"
            } else if (responsePatientData.gender_uuid == 3) {
                gender = "Transgender"
            } else {
                gender = "Male"
            }
            binding?.dateTextView!!.text =
                salutaionvalue?.toString() + "" + responsePatientData.first_name + " / " + responsePatientData.age + " Year" + " / " + gender + " / " + responsePatientData.uhid + " / " +
                        responsePatientData.patient_detail!!.mobile
        }
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        logindoctorID = userDataStoreBean?.uuid
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!


        AnalyticsManager.getAnalyticsManager().trackLMISNewOrderVisit(requireContext())

        viewModel = LmisNewOrderViewModelFactory(
            requireActivity().application
        )
            .create(LmisNewOrderViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())

//        if(encounterType==AppConstants.TYPE_IN_PATIENT){

        viewModel?.getWardId(patientId!!, getWardIdRetrofitCallback)

        //      }
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
        setupViewPager(binding?.viewpager!!)
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)

        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.lmisLabTechnicianRecyclerView!!.layoutManager = linearLayoutManager
        mAdapter = LmisLabTechAdapter(requireActivity())
        binding?.lmisLabTechnicianRecyclerView!!.adapter = mAdapter
        mAdapter!!.add(LabtechData())
        //  binding?.viewModel?.getAllDepartment(facility_id, favAllDepartmentCallBack)
        mAdapter!!.setOnSearchInitiatedListener(object :
            LmisLabTechAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int
            ) {
                dropdownReferenceView = view
                searchposition = position
                viewModel?.getTextMethod1(query, getTestMethdCallBack1)
            }
        })
        binding!!.department.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 0) {

                    Str_auto_id = 0

                    binding!!.department.error = "Department Name is required"

                } else {

                    if (Str_auto_id == 0) {

                        binding!!.department.error = "Department Name is required"
                    } else {
                        binding!!.department.error = null

                    }

                }

            }
        })

        mAdapter?.setOnCommandClickListener(object : LmisLabTechAdapter.OnCommandClickListener {
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

        binding?.doctorSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    doctorID =
                        favAddResponseMapDepart.filterValues { it == itemValue }.keys.toList()[0]
                    /*     selectStateUuid =
                             CovidStateList.filterValues { it == itemValue }.keys.toList()[0]*/
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    doctorID =
                        favAddResponseMapDepart.filterValues { it == itemValue }.keys.toList()[0]
                    if (doctorID != 0) {
                        binding?.viewModel?.getuserDepartment(doctorID, favAllDepartmentCallBack)
                    }
                }

            }
        mAdapter?.setOnDeleteClickListener(object :
            LmisLabTechAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: LabtechData?, position: Int) {
                Log.i("", "" + responseContent)
                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text} '" + responseContent?.labName + "(" + responseContent?.code + ")" + "'Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    val check = mAdapter?.deleteRow(position)
                    if (responseContent?.Labstatus == 1) {
                        mCallbackLabFavFragment?.ClearFavParticularPosition(responseContent.isFavpos)
                    } else if (responseContent?.Labstatus == 2)
                        if (check!!) {

                            mCallbackLabTemplateFragment?.ClearTemplateParticularPosition(
                                responseContent.isTemposition!!
                            )
                        }
                    //template_id
                    customdialog!!.dismiss()
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })



        binding?.clearCardView?.setOnClickListener({

            mAdapter?.clearAll()
            mCallbackLabFavFragment?.ClearAllData()
            clearAllVlaue()

        })

        viewModel?.getDoctorName(
            facility_id,
            getDoctorNameCallback
        )
        binding?.saveTemplateCardView?.setOnClickListener {

            val arrayItemData = mAdapter!!.getAll()
            val datasize: Int = arrayItemData.size
            if (datasize > 1) {
                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = SaveTemplateManageLmisLabTemplateFragment()
                val bundle = Bundle()
                bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, arrayItemData)
                labtemplatedialog.arguments = bundle
                labtemplatedialog.show(ft, "Tag")
            } else {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Please select any one item"
                )
            }


        }
        viewModel!!.getReference(typeResponseCallback)
        binding?.saveandnextCardView?.setOnClickListener {
            savestatus = true
            saveOrder()
        }


        binding?.saveCardView?.setOnClickListener {
            savestatus = false
            saveOrder()
        }




        mAdapter?.setOnListItemClickListener(object : LmisLabTechAdapter.OnListItemClickListener {
            override fun onListItemClick(
                responseContent: LabtechData?,
                position: Int
            ) {
                searchposition = position
                viewModel?.getToLocationTest(
                    getLabToLoctionTestRetrofitCallback,
                    facility_id,
                    Str_auto_id,
                    responseContent!!.id, wardUUid
                )

            }
        })
        return binding!!.root
    }

    fun createEncounter() {
        viewModel?.createEncounter(
            patientId!!,
            encounterType!!,
            doctorID!!,
            Str_auto_id,
            createEncounterRetrofitCallback
        )
    }


    fun saveOrder() {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val dateInString = sdf.format(Date())
        labModifiyRequest.removed_details?.clear()
        labModifiyRequest.existing_details?.clear()
        labModifiyRequest.new_details?.clear()
        arrayItemData = mAdapter?.getItems()
        removedListFromOriginal = mAdapter?.getRemovedItems()
        detailsList.clear()

        when {
            doctorID == 0 -> utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select Dr name"
            )

            Str_auto_id == 0 -> utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select valid Department Name"
            )
            else -> {
                val datasize: Int = arrayItemData?.size!!
                if (datasize > 1) {
                    if (encounter_uuid != 0) {
                        for (i in 0..(datasize - 2)) {
                            val details: Detail = Detail()
                            if (arrayItemData?.get(i)?.typeofmaster?.equals("test_master")!!) {
                                //testmethod
                                details.test_master_uuid = arrayItemData?.get(i)?.labId!!
                                details.is_profile = false
                                details.profile_uuid = 0
                            } else {
                                //profileuuid
                                details.test_master_uuid = null
                                details.profile_uuid = arrayItemData?.get(i)?.labId!!
                                details.is_profile = true

                            }
                            details.lab_master_type_uuid = AppConstants.LAB_TESTMASTER_UUID
                            details.to_department_uuid = arrayItemData?.get(i)?.to_department_id
                            details.order_priority_uuid = arrayItemData?.get(i)?.type
                            details.to_location_uuid =
                                arrayItemData?.get(i)?.OrderToLocationId.toString()
                            details.sample_type_uuid = arrayItemData?.get(i)!!.SpecimenId.toString()
                            details.type_of_method_uuid =
                                arrayItemData?.get(i)?.TestMethodId?.toString()
                            details.group_uuid = 0
                            details.to_sub_department_uuid = 0
                            details.tat_session_start = dateInString
                            details.tat_session_end = dateInString
                            details.is_active = true
                            details.test_diseases_uuid = ""
                            details.is_approval_requried = false
                            details.confidential_uuid = ""
                            details.is_confidential = false
                            details.application_type_uuid = 4
                            //wardidbind for ip
                            if (encounterType == AppConstants.TYPE_IN_PATIENT) {
                                details.ward_uuid = wardUUid
                            }
                            detailsList.add(details)
                        }
                        header?.patient_uuid = patientId.toString()
                        header?.encounter_uuid = encounter_uuid
                        header?.encounter_type_uuid = encounterType!!.toInt()
                        header?.lab_master_type_uuid = AppConstants.LAB_TESTMASTER_UUID
                        header?.encounter_doctor_uuid = encounter_doctor_uuid
                        header?.doctor_uuid = doctorID.toString()
                        header?.facility_uuid = facility_id.toString()
                        header?.department_uuid = Str_auto_id?.toString()
                        header?.sub_department_uuid = 0
                        header?.order_to_location_uuid = 1
                        header?.consultation_uuid = 0
                        header?.patient_treatment_uuid = 0
                        header?.order_status_uuid = 0
                        header?.treatment_plan_uuid = 0
                        header?.from_facility_name = ""
                        header?.application_type_uuid = 4

                        //wardidbind for ip
                        if (encounterType == AppConstants.TYPE_IN_PATIENT) {
                            header?.ward_uuid = wardUUid
                        }
                        header?.tat_session_start = dateInString
                        header?.tat_session_end = dateInString
                        emrRequestModel?.header = this.header!!
                        emrRequestModel?.details = this.detailsList
                        val response = Gson().toJson(emrRequestModel)
                        Log.i("", "" + response)
                        if (!isModifiy) {
                            viewModel?.labInsert(
                                facility_id,
                                emrRequestModel!!,
                                emrpostRetrofitCallback
                            )
                        }

                        for (i in 0..(datasize - 2)) {
                            if (arrayItemData!![i]!!.mode == 1) {
                                if (arrayItemData?.get(i)?.typeofmaster?.equals("test_master")!!) {
                                    //testmethod
                                    exsistingDeatil.test_master_uuid =
                                        arrayItemData?.get(i)?.labId!!
                                    exsistingDeatil.profile_uuid = null
                                } else {
                                    //profileuuid
                                    exsistingDeatil.test_master_uuid = null
                                    exsistingDeatil.profile_uuid = arrayItemData?.get(i)?.labId!!

                                }
                                exsistingDeatil.application_type_uuid = 3
                                exsistingDeatil.details_comments = ""
                                exsistingDeatil.order_priority_uuid = arrayItemData?.get(i)?.type
                                exsistingDeatil.patient_order_uuid =
                                    arrayItemData?.get(i)?.patient_order_uuid
                                exsistingDeatil.to_location_uuid =
                                    arrayItemData?.get(i)?.OrderToLocationId
                                exsistingDeatil.type_of_method_uuid =
                                    arrayItemData?.get(i)?.TestMethodId
                                exsistingDeatil.uuid =
                                    arrayItemData?.get(i)?.patient_order_details_uuid
                                exsistingDeatil.ward_uuid = "1"
                                labModifiyRequest.existing_details?.add(exsistingDeatil)
                            } else {

                                if (arrayItemData?.get(i)?.typeofmaster?.equals("test_master")!!) {
                                    //testmethod
                                    newDetail.test_master_uuid = arrayItemData?.get(i)?.labId!!
                                    newDetail.is_profile = false
                                    newDetail.profile_uuid = null
                                } else {
                                    //profileuuid
                                    newDetail.test_master_uuid = null
                                    newDetail.profile_uuid = arrayItemData?.get(i)?.labId!!
                                    newDetail.is_profile = true

                                }

                                newDetail.application_type_uuid = 3
                                newDetail.doctor_uuid = doctorID.toString()
                                newDetail.encounter_type_uuid = 1
                                newDetail.encounter_uuid = 1
                                newDetail.from_department_uuid = "0"
                                newDetail.group_uuid = 0
                                newDetail.is_ordered = true
                                newDetail.lab_master_type_uuid = AppConstants.LAB_TESTMASTER_UUID
                                newDetail.order_priority_uuid = arrayItemData?.get(i)?.type
                                newDetail.order_request_date = dateInString
                                newDetail.order_status_uuid = 1
                                newDetail.patient_order_uuid = patientOrderUuid
                                newDetail.patient_uuid = patientId.toString()
                                newDetail.patient_work_order_by = 1
                                newDetail.to_department_uuid =
                                    arrayItemData?.get(i)?.to_department_id
                                newDetail.to_location_uuid =
                                    arrayItemData?.get(i)?.OrderToLocationId
                                newDetail.to_sub_department_uuid = 0
                                newDetail.ward_uuid = "1"
                                labModifiyRequest.new_details?.add(newDetail)
                            }
                        }
                        val removeDatasize: Int = removedListFromOriginal?.size!!
                        if (removeDatasize > 0) {
                            for (i in 0..(removeDatasize - 1)) {
                                removeData.patient_orders_uuid = patientOrderUuid
                                removeData.profile_uuid = null
                                removeData.test_master_uuid = removedListFromOriginal?.get(i)?.labId
                                removeData.uuid =
                                    removedListFromOriginal?.get(i)?.patient_order_details_uuid
                                labModifiyRequest.removed_details?.add(removeData)
                            }
                        }
                        if (isModifiy) {
                            viewModel?.updateLab(
                                facility_id,
                                labModifiyRequest, emrupdateRetrofitCallback
                            )
                        }
                    } else {
                        createEncounter()
                    }

                } else {
                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        "Please select any one item"
                    )
                }
            }
        }
    }

    val getLabToLoctionTestRetrofitCallback = object : RetrofitCallback<GetToLocationTestResponse> {
        override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {


            mAdapter?.setToLocation(
                responseBody?.body()?.responseContents,
                searchposition
            )
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


    private fun setupViewPager(viewPager: ViewPager) {

        adapter = ViewPagerAdapter(childFragmentManager)
        adapter?.addFragment(LmisLabTechnicianFavouriteFragment(), "Favourite")
        adapter?.addFragment(LmisLabTechnicianTemplateFragment(), "Templete")
        adapter?.addFragment(PrevLmisLabTechnicianFragment(), "Prev.Lab")
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

        fun getCurrentFragment(position: Int): Fragment? {
            return mFragmentList[position]
        }
    }

    val getWardIdRetrofitCallback = object : RetrofitCallback<GetWardIdResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetWardIdResponseModel>?) {

            if (responseBody?.body()?.ward_uuid != null) {

                wardUUid = responseBody.body()?.ward_uuid!!

            }
        }

        override fun onBadRequest(errorBody: Response<GetWardIdResponseModel>?) {
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


    val typeResponseCallback = object : RetrofitCallback<GetReferenceResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetReferenceResponseModel>?) {
            //viewModel!!.getTotest(covidtestResponseCallback)
            viewModel?.getMethod("sample_type", getSpeciumMethdCallBack)
            //  val data=responseBody!!.body()!!.responseContents
            mAdapter!!.setadapterTypeValue(responseBody!!.body()!!.responseContents)
        }

        override fun onBadRequest(errorBody: Response<GetReferenceResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: GetReferenceResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    GetReferenceResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.statusCode.toString()
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

        override fun onServerError(response: Response<*>?) {
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

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
    val getSpeciumMethdCallBack = object : RetrofitCallback<ResponseTestMethod> {
        override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {
            viewModel?.getMethod("type_of_method", getTestethdCallBack)
            mAdapter!!.setadapterSpectiumValue(response.body()!!.responseContents)
        }

        override fun onBadRequest(response: Response<ResponseTestMethod>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseTestMethod
            try {

                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    ResponseTestMethod::class.java
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


    val getTestethdCallBack = object : RetrofitCallback<ResponseTestMethod> {
        override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {

            viewModel!!.getLocationAPI(orderLocationResponseCallback)

            mAdapter!!.setadapterTestMethodValue(response.body()!!.responseContents)

        }

        override fun onBadRequest(response: Response<ResponseTestMethod>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseTestMethod
            try {

                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    ResponseTestMethod::class.java
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


    val orderLocationResponseCallback = object : RetrofitCallback<LocationMasterResponseModelX> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModelX>?) {

            mAdapter!!.setadapterLocationValue(responseBody!!.body()!!.responseContents)

        }

        override fun onBadRequest(errorBody: Response<LocationMasterResponseModelX>?) {
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModelX
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LocationMasterResponseModelX::class.java
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

        override fun onServerError(response: Response<*>?) {
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

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
    val getTestMethdCallBack1 =
        object : RetrofitCallback<LabTestSpinnerResponseModel> {
            @SuppressLint("SetTextI18n")
            override fun onSuccessfulResponse(response: Response<LabTestSpinnerResponseModel>) {
                Log.i("search", "" + response.body().responseContents)
                if (response.body().responseContents.isNotEmpty()!!) {
                    mAdapter?.setAdapter(
                        dropdownReferenceView,
                        response.body().responseContents!! as ArrayList<LabTestSpinnerresponseContent>,
                        searchposition
                    )
                } else {
                    mAdapter?.setError(dropdownReferenceView, searchposition)
                }
            }

            override fun onBadRequest(response: Response<LabTestSpinnerResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: LabTestSpinnerResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        LabTestSpinnerResponseModel::class.java
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
    val favAllDepartmentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {

                setAdapter(
                    responseBody.body()?.responseContents!! as ArrayList<FavAddAllDepatResponseContent>,
                    0
                )


            }

        }

        override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddAllDepatResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddAllDepatResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

        override fun onServerError(response: Response<*>?) {
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


    fun setAdapter(
        responseContents: ArrayList<FavAddAllDepatResponseContent>,
        selectedSearchPosition: Int
    ) {
        val responseContentAdapter = LimsDepatmentSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.department.threshold = 1
        binding!!.department.setAdapter(responseContentAdapter)
        binding!!.department.setText(responseContents[0].name)
        Str_auto_id = responseContents[0].uuid

        binding!!.department.error = null

        viewModel?.getEncounter(
            facility_id,
            patientId!!,
            doctorID!!,
            encounterType!!,
            Str_auto_id,
            fetchEncounterRetrofitCallBack
        )

        binding!!.department.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as FavAddAllDepatResponseContent?

            binding!!.department.setText(selectedPoi?.name)

            binding!!.department.error = null

            Str_auto_id = selectedPoi?.uuid

            viewModel?.getEncounter(
                facility_id,
                patientId!!,
                doctorID!!,
                encounterType!!,
                Str_auto_id,
                fetchEncounterRetrofitCallBack
            )
        }
    }

    private val getDoctorNameCallback =
        object : RetrofitCallback<DoctorNameResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<DoctorNameResponseModel>?) {
                listDoctorNamesItems.add(DoctorNameResponseContent())
                listDoctorNamesItems.addAll(responseBody?.body()?.responseContents!!)
                favAddResponseMapDepart =
                    listDoctorNamesItems.map { it?.uuid!! to it.title_name + "" + it.first_name }
                        .toMap().toMutableMap()
                favAddResponseMapDepart.put(0, "Doctor Name")
                listDepartmentID = ArrayList<Int?>(favAddResponseMapDepart.keys) // <== Set to List
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        favAddResponseMapDepart.values.toMutableList()
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.doctorSpinner!!.adapter = adapter

                try {

                    doctorID = responseBody.body()?.responseContents?.get(0)!!.uuid

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onBadRequest(errorBody: Response<DoctorNameResponseModel>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    fun clearAllVlaue() {
        binding?.doctorSpinner?.setSelection(0)
        binding!!.department.setText("")
        Str_auto_id = 0
        doctorID = 0
        isModifiy = false
        val error: TextView = binding?.doctorSpinner?.selectedView as TextView
        error.error = "Doctor name requried"
    }

    val emrupdateRetrofitCallback = object : RetrofitCallback<LabModifiyResponse> {
        override fun onSuccessfulResponse(responseBody: Response<LabModifiyResponse>?) {
            Log.i("res", "" + responseBody?.body()?.message)

            AnalyticsManager.getAnalyticsManager().trackLMISNewOrderSuccess(context!!, "")
            mCallbackLabFavFragment?.ClearAllData()
            mCallbackLabTemplateFragment?.ClearAllData()
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Order Updated Successfully"
            )

            mAdapter?.clearall()
            mAdapter?.add(LabtechData())
            clearAllVlaue()
            (adapter?.getCurrentFragment(2) as PrevLmisLabTechnicianFragment).refreshList(
                patientId,
                facility_id
            )
            if (savestatus!!) {
                val labtemplatedialog = LabTestActivity()

                (activity as MainLandScreenActivity).replaceFragmentNoBack(labtemplatedialog)
            }
        }

        override fun onBadRequest(response: Response<LabModifiyResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: ComplaintSearchResponseModel
            try {

                responseModel = gson.fromJson(
                    response!!.errorBody()!!.string(),
                    ComplaintSearchResponseModel::class.java
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
    val emrpostRetrofitCallback = object : RetrofitCallback<EmrResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<EmrResponceModel>?) {
            Log.i("res", "" + responseBody?.body()?.message)

            AnalyticsManager.getAnalyticsManager().trackLMISNewOrderSuccess(context!!, "")
            mCallbackLabFavFragment?.ClearAllData()
            mCallbackLabTemplateFragment?.ClearAllData()
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Order Created Successfully"
            )

            mAdapter?.clearall()
            mAdapter?.add(LabtechData())
            clearAllVlaue()
            if (savestatus!!) {
                val labtemplatedialog = LabTestActivity()

                (activity as MainLandScreenActivity).replaceFragmentNoBack(labtemplatedialog)
            }
        }

        override fun onBadRequest(response: Response<EmrResponceModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: ComplaintSearchResponseModel
            try {

                responseModel = gson.fromJson(
                    response!!.errorBody()!!.string(),
                    ComplaintSearchResponseModel::class.java
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

    override fun sendFavAddInLab(favmodel: FavouritesModel?, position: Int, selected: Boolean) {

        statusselect = selected
        positionstatus = position
        if (!statusselect!!) {
            GettingFromTemplate = 1
            viewModel?.getComplaintSearchResult(
                facility_id,
                favmodel?.test_master_name,
                getComplaintSearchRetrofitCallBack
            )
            binding!!.drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            mAdapter?.deleteRowFromTemplate(favmodel?.test_master_id, 1)
            binding!!.drawerLayout.closeDrawer(GravityCompat.END)

        }

    }

    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<ResponseLmisListview> {
            override fun onSuccessfulResponse(response: Response<ResponseLmisListview>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)
                if (GettingFromTemplate == 1) {
                    val labtechData: LabtechData = LabtechData()
                    labtechData.Labstatus = 1
                    labtechData.isFavpos = positionstatus!!
                    labtechData.labId = response.body()?.responseContents?.get(0)?.uuid!!
                    labtechData.labName = response.body()?.responseContents?.get(0)?.name!!
                    labtechData.typeofmaster = response.body()?.responseContents?.get(0)?.type!!
                    if (response.body()?.responseContents?.get(0)?.sample_type_uuid != null) {
                        labtechData.SpecimenId =
                            response.body()?.responseContents?.get(0)?.sample_type_uuid!!
                    }
                    if (response.body()?.responseContents?.get(0)?.type_of_method_uuid != null) {
                        labtechData.TestMethodId =
                            response.body()?.responseContents?.get(0)?.type_of_method_uuid!!
                    }
                    if (!statusselect!!) {
                        mAdapter?.addFavouritesInRow(labtechData)
                    } else {
//                    mAdapter?.deleteRowFromTemplate(labtechData?.labId,1)
                    }
                } else if (GettingFromTemplate == 2) {
                    val labtechData: LabtechData = LabtechData()
                    labtechData.Labstatus = 2
                    labtechData.isFavpos = positionstatus!!
                    labtechData.labId = response.body()?.responseContents?.get(0)?.uuid!!
                    labtechData.labName = response.body()?.responseContents?.get(0)?.name!!
                    labtechData.typeofmaster = response.body()?.responseContents?.get(0)?.type!!
                    if (response.body()?.responseContents?.get(0)?.sample_type_uuid != null) {
                        labtechData.SpecimenId =
                            response.body()?.responseContents?.get(0)?.sample_type_uuid!!
                    }
                    if (response.body()?.responseContents?.get(0)?.type_of_method_uuid != null) {
                        labtechData.TestMethodId =
                            response.body()?.responseContents?.get(0)?.type_of_method_uuid!!
                    }
                    if (!statusselect!!) {
                        mAdapter?.addFavouritesInRow(labtechData)
                    } else {
//                    mAdapter?.deleteRowFromTemplate(labtechData?.labId,1)
                    }
                }

            }

            override fun onBadRequest(response: Response<ResponseLmisListview>) {
                val gson = GsonBuilder().create()
                val responseModel: ComplaintSearchResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        ComplaintSearchResponseModel::class.java
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

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is LmisLabTechnicianFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearLmisFavParticularPositionListener) {
            mCallbackLabFavFragment = childFragment
        }

        if (childFragment is LmisLabTechnicianTemplateFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackLabTemplateFragment = childFragment
        }

        if (childFragment is PrevLmisLabTechnicianFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is SaveTemplateManageLmisLabTemplateFragment) {
            childFragment.setOnSaveTemplateRefreshListener(this)
        }
        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackLmisLabTemplateFragment = childFragment
        }
        if (childFragment is CommentDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }

    }

    override fun sendTemplete(
        templeteDetails: List<LabDetail?>?,
        position: Int,
        selected: Boolean,
        id: Int
    ) {
        for (i in templeteDetails!!.indices) {

            statusselect = selected
            positionstatus = position
            if (!statusselect!!) {
                GettingFromTemplate = 2
                viewModel?.getComplaintSearchResult(
                    facility_id,
                    templeteDetails.get(i)?.lab_name, getComplaintSearchRetrofitCallBack
                )
//            mAdapter?.addFavouritesInRow(labtechData)
                binding!!.drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                mAdapter?.deleteRowFromTemplate(templeteDetails.get(i)?.lab_test_uuid!!, 2)
                binding!!.drawerLayout.closeDrawer(GravityCompat.END)

            }
        }


        /*
              if (!selected) {
                  for (i in templeteDetails!!.indices) {
                      val favmodel: FavouritesModel? = FavouritesModel()
                      favmodel?.viewLabstatus = 2
                      favmodel?.isTemposition = position
                      favmodel?.test_master_name = templeteDetails[i]!!.lab_name
                      favmodel?.test_master_id=templeteDetails[i]!!.lab_test_uuid
                      favmodel?.test_master_code=templeteDetails[i]!!.lab_code
                      favmodel?.template_id=id
                      labAdapter!!.addFavouritesInRow(favmodel)

                  }
              } else {
                  for (i in templeteDetails!!.indices) {
                      labAdapter!!.deleteRowFromTemplate(templeteDetails[i]!!.lab_test_uuid, 2)
                  }

              }*/
    }

    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {

            doctorID = response.body()?.responseContents?.encounterDoctor?.doctor_uuid!!.toInt()

            patientId = response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            encounter_uuid = response.body()?.responseContents?.encounter!!.uuid!!.toInt()
            encounter_doctor_uuid =
                response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            saveOrder()
        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CreateEncounterResponseModel

            if (response.code() == 400) {
                val gson = GsonBuilder().create()
                var mError = BadRequestResponse()
                try {
                    mError =
                        gson.fromJson(
                            response.errorBody()!!.string(),
                            BadRequestResponse::class.java
                        )
                    encounter_doctor_uuid = mError?.existingDetails?.encounter_doctor_id!!
                    encounter_uuid = mError.existingDetails!!.encounter_id!!
                    Toast.makeText(
                        context,
                        mError.message,
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } catch (e: IOException) { // handle failure to read error
                }
            }

            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CreateEncounterResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message!!
                )
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
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    encounterResponseContent = response.body()?.responseContents!!
                    encounter_doctor_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid
                    encounter_uuid = encounterResponseContent.get(0)?.uuid


                }/* else {
                    viewModel?.createEncounter(
                        patientId!!,
                        encounterType!!,
                        doctorID!!,
                        Str_auto_id,
                        createEncounterRetrofitCallback
                    )
                }*/
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


    private val lmisLabDepartmentRetrofitCallback = object : RetrofitCallback<FavAddResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddResponseModel>?) {

            binding?.department!!.setText(responseBody?.body()?.responseContent?.name)
            Str_auto_id = responseBody?.body()?.responseContent?.uuid
        }

        override fun onBadRequest(response: Response<FavAddResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    FavAddResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

    override fun sendPrevtoChild(
        responseContent: List<PodArrResult>?,
        departmentID: Int?, isModifyValue: Boolean
    ) {
        mAdapter!!.clearAll()
        removedListFromOriginal?.clear()
        this.isModifiy = isModifyValue
        for (i in responseContent!!.indices) {
            val labtechData: LabtechData = LabtechData()
            if (responseContent.get(i).test_master_uuid != null) {
                labtechData.labName = responseContent.get(i).name
                labtechData.labId =
                    ((responseContent.get(i).test_master_uuid as Double).roundToInt())

            } else {
                labtechData.labName = responseContent.get(i).profile_name
                labtechData.labId = responseContent.get(i).profile_master_uuid
            }
            labtechData.to_department_id = departmentID!!
            if (responseContent.get(i).test_master_uuid != null) {
                labtechData.typeofmaster = "test_master"
            } else {
                labtechData.typeofmaster = "Profile_master"
            }

            if (responseContent.get(i).order_to_location_uuid != null) {
                labtechData.OrderToLocationId = responseContent.get(i).order_to_location_uuid
            }
            if (responseContent.get(i).type_uuid != null) {
                labtechData.type = responseContent.get(i).type_uuid
            }
            labtechData.mode = 1
            labtechData.patient_order_details_uuid =
                responseContent.get(i).patient_order_details_uuid
            labtechData.patient_order_uuid = responseContent.get(i).patient_order_uuid
            patientOrderUuid = responseContent.get(i).patient_order_uuid
            mAdapter?.addFavouritesInRow(labtechData)
            binding!!.drawerLayout.closeDrawer(GravityCompat.END)
        }

    }

    override fun onRefreshList() {
        mCallbackLmisLabTemplateFragment?.GetTemplateDetails()
    }

    override fun sendCommandPosData(position: Int, command: String) {

        mAdapter!!.addCommands(position, command)
    }

}

