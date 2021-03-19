package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentLabChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.LabViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabFavModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabToLocationResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabTypeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PodArrResult
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavSearchResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.saveTemplate.ManageLabSaveTemplateFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model.LabViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.LabModifiyRequest
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.LabModifiyResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Request.EmrRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Request.Header
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.LabDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.CommentDialogFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.GetToLocationTestResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.ui.PrevLabFragment
import com.oasys.digihealth.doctor.ui.quick_reg.model.ResponseTestMethod
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LabChildFragment : Fragment(), LabFavouriteFragment.FavClickedListener,
    LabTempleteFragment.TempleteClickedListener, PrevLabFragment.LabPrevClickedListener,
    ManageLabSaveTemplateFragment.LabChiefComplaintListener,
    CommentDialogFragment.CommandClickedListener {
    private var arrayItemData: ArrayList<FavouritesModel?>? = null
    private var binding: FragmentLabChildBinding? = null
    lateinit var drugNmae: TextView
    private var viewModel: LabViewModel? = null
    private var utils: Utils? = null
    private var customdialog: Dialog? = null
    private var responseContents: String? = ""
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    lateinit var SpinnerToLocation: Spinner
    var labAdapter: LabAdapter? = null
    private var fragmentBackClick: FragmentBackClick? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    val detailsList = ArrayList<Detail>()
    var facility_id: Int = 0
    var department_UUID: Int = 0

    var startTime: String = ""

    private var encounterType: Int = 0
    private var warduuid: Int = 0
    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0
    private var labMasterTypeUUID: Int = 0

    var searchposition: Int = 0
    val header: Header? = Header()
    val emrRequestModel: EmrRequestModel? = EmrRequestModel()
    var mCallbackLabFavFragment: ClearFavParticularPositionListener? = null
    var mCallbackLabTemplateStatus: ClearTemplateParticularPositionListener? = null
    var mCallbackLabTemplateFragment: ClearTemplateParticularPositionListener? = null
    var prevOrder: Boolean = false
    var isModifiy: Boolean = false
    val labModifiyRequest: LabModifiyRequest = LabModifiyRequest()
    var removedListFromOriginal: ArrayList<FavouritesModel?>? = ArrayList()

    val exsistingDeatil: LabModifiyRequest.ExistingDetail =
        LabModifiyRequest.ExistingDetail()
    val newDetail: LabModifiyRequest.NewDetail =
        LabModifiyRequest.NewDetail()
    val removeData: LabModifiyRequest.RemovedDetail = LabModifiyRequest.RemovedDetail()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_lab_child,
                container,
                false
            )
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        appPreferences?.saveInt(AppConstants.LAB_MASTER_TYPE_ID, 1)
        labMasterTypeUUID = appPreferences?.getInt(AppConstants.LAB_MASTER_TYPE_ID)!!
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        warduuid = appPreferences?.getInt(AppConstants.WARDUUID)!!


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        startTime = sdf.format(Date())
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!

        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        if (activity !is FragmentBackClick) {
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        viewModel = LabViewModelFactory(
            requireActivity().application
        ).create(LabViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this

        utils = Utils(requireContext())

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        binding?.saveCardView?.isEnabled = true
        setAdapter()
        setupViewPager(binding?.viewpager!!)
        adapterListiners()
        saveListiners()
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)
        viewModel?.getLabType(getLabTypeRetrofitCallback, facility_id)
        return binding!!.root
    }

    private fun adapterListiners() {
        labAdapter?.setOnitemChangeListener(object : LabAdapter.OnitemChangeListener {
            override fun onitemChangeClick(uuid: ArrayList<Int>) {

                mCallbackLabFavFragment?.checkanduncheck(uuid, true)

                mCallbackLabTemplateStatus?.checkanduncheck(uuid)

            }
        })
        labAdapter?.setOnCommandClickListener(object : LabAdapter.OnCommandClickListener {
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

    private fun saveListiners() {
        binding?.saveCardView!!.setOnClickListener {
            arrayItemData = labAdapter?.getItems()
            if (arrayItemData?.size!! > 1) {
                viewModel?.getEncounter(
                    facility_id,
                    patientUuid,
                    encounterType,
                    fetchEncounterRetrofitCallBack
                )
            } else {

                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Please select any one item"
                )
            }

        }
        binding?.clearCardView?.setOnClickListener {
            //    mCallbackLabFavFragment?.ClearAllData()
            labAdapter?.clearall()
            labAdapter?.addRow(FavouritesModel())
            isModifiy = false
            prevOrder = false
            labAdapter?.getIdList()
            //  labAdapter?.addTempleteRow(TempDetails())
        }

        binding?.saveTemplateCardView!!.setOnClickListener {
            val arrayItemData = labAdapter!!.getAll()
            if (arrayItemData.size > 1) {
                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = ManageLabSaveTemplateFragment()
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
    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(LabFavouriteFragment(), "Favourite")
        adapter.addFragment(LabTempleteFragment(), "Templete")
        adapter.addFragment(PrevLabFragment(), "Prev.Lab")

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
        labAdapter =
            LabAdapter(
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
        binding?.savelabRecyclerView?.adapter = labAdapter
        labAdapter?.addRow(FavouritesModel())
        labAdapter?.addTempleteRow(TempDetails())

        labAdapter?.setOnDeleteClickListener(object :
            LabAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
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
                        val check = labAdapter?.deleteRow(position)
                        if (responseContent?.viewLabstatus == 1) {
                            labAdapter?.getIdList()
                            //  mCallbackLabFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)
                        } else if (responseContent?.viewLabstatus == 2) {
                            labAdapter?.getIdList()
                            /* if (check!!) {
                                 mCallbackLabTemplateFragment?.ClearTemplateParticularPosition(
                                     responseContent.isTemposition
                                 )
                             }
 */
                        }
                        customdialog!!.dismiss()
                        customdialog = null

                        utils?.showToast(
                            R.color.positiveToast,
                            binding?.mainLayout!!,
                            "Test name Deleted successfully"

                        )
                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()

                        customdialog = null
                    }

                    if (customdialog != null) {

                        customdialog!!.show()

                    }
                }
            }
        })
        labAdapter?.setOnSearchInitiatedListener(object :
            LabAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int,
                spinnerToLocation: Spinner
            ) {
                dropdownReferenceView = view
                searchposition = position
                SpinnerToLocation = spinnerToLocation
                viewModel?.getComplaintSearchResult(
                    facility_id,
                    query,
                    getComplaintSearchRetrofitCallBack
                )
            }
        })

        labAdapter?.setOnListItemClickListener(object : LabAdapter.OnListItemClickListener {
            override fun onListItemClick(
                responseContent: FavouritesModel?,
                position: Int,
                spinnerToLocation: Spinner
            ) {
                SpinnerToLocation = spinnerToLocation
                searchposition = position
                viewModel?.getToLocationTest(
                    getLabToLoctionTestRetrofitCallback,
                    facility_id,
                    department_UUID,
                    responseContent!!.test_master_id
                )

            }
        })


    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is LabFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is LabTempleteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackLabFavFragment = childFragment
        }
        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackLabTemplateFragment = childFragment
        }

        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackLabTemplateStatus = childFragment
        }
        if (childFragment is CommentDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ManageLabSaveTemplateFragment) {
            childFragment.setOnClickedListener(this)
        }
        if (childFragment is PrevLabFragment) {
            childFragment.setOnTextClickedListener(this)
        }
    }

    override fun sendFavAddInLab(labFav: LabFavModel?, position: Int, selected: Boolean) {
        if (!selected) {
            var check = labAdapter?.checkAlreadyPresent(labFav?.test_master_id)
            if (check!!) {
                val favmodel: FavouritesModel? = FavouritesModel()
                favmodel?.viewLabstatus = 1
                favmodel?.isFavposition = position
                favmodel?.selectedLocationName = "Select Location"
                favmodel?.selectToTestMethodName = "Select Test Method"
                favmodel?.selectTypeName = "Select Type"
                favmodel?.selectTypeUUID = 2
                // favmodel?.selectTypeName = priorityNameHashMap[2]
                favmodel?.test_master_name = labFav!!.test_master_name
                favmodel?.test_master_id = labFav.test_master_id
                favmodel?.test_master_code = labFav.test_master_code
                //  showprogressDailog()
                labAdapter!!.addFavouritesInRowModule(1, favmodel, position, selected)
                /*  viewModel?.getComplaintSearchResult(
                    facility_id,
                    favmodel?.test_master_name!!,
                    getFavSearchRetrofitCallBack
                )
                showprogressDailog()
                viewModel?.getToLocationTest(
                    getFavToLoctionTestRetrofitCallback,
                    facility_id,
                    department_UUID,
                    favmodel?.test_master_id
                )*/
            } else {
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                    ?.show()
            }
        } else {
            labAdapter!!.deleteRowFromTemplate(labFav?.test_master_id, 1)
        }
    }

    override fun sendTemplete(
        templeteDetails: List<LabDetail?>?,
        position: Int,
        selected: Boolean,
        id: Int
    ) {
        if (!selected) {
            for (i in templeteDetails!!.indices) {
                val favmodel: FavouritesModel? = FavouritesModel()
                favmodel?.viewLabstatus = 2
                favmodel?.isTemposition = position
                favmodel?.test_master_name = templeteDetails[i]!!.lab_name
                favmodel?.test_master_id = templeteDetails[i]!!.lab_test_uuid
                favmodel?.test_master_code = templeteDetails[i]!!.lab_code
                favmodel?.template_id = id
                labAdapter!!.addFavouritesInRowModule(2, favmodel, position, selected)

            }
        } else {
            for (i in templeteDetails!!.indices) {
                labAdapter!!.deleteRowFromTemplate(templeteDetails[i]!!.lab_test_uuid, 2)
            }

        }
    }

    override fun sendDataChiefComplaint() {
        mCallbackLabTemplateFragment?.GetTemplateDetails()
    }

    override fun sendCommandPosData(position: Int, command: String) {
        labAdapter!!.addCommands(position, command)
    }

    override fun sendPrevtoChild(responseContent: List<PodArrResult>?, isModifiy: Boolean) {

        prevOrder = true
        this.isModifiy = isModifiy
        for (i in responseContent!!.indices) {
            val favmodel: FavouritesModel? = FavouritesModel()
            favmodel!!.chief_complaint_name = responseContent[i].name
            favmodel.itemAppendString = responseContent[i].name
            favmodel.test_master_name = responseContent[i].name
            favmodel.test_master_id = responseContent[i].test_master_uuid
            favmodel.test_master_code = responseContent[i].code
            favmodel.selectTypeUUID = responseContent[i].order_priority_uuid
            favmodel.selectToLocationUUID = responseContent[i].order_to_location_uuid
            favmodel.TestMethodId = responseContent[i].test_method_uuid

            if (isModifiy) {
                favmodel.patient_order_details_uuid =
                    responseContent[i].patient_order_details_uuid
                favmodel.patient_order_uuid = responseContent[i].patient_order_uuid
                favmodel.isModifiy = true
            } else {
                favmodel.isModifiy = false
            }
            labAdapter!!.addFavouritesInRow(favmodel)
        }
    }

    fun updateSaveOrder() {
        labModifiyRequest.removed_details?.clear()
        labModifiyRequest.existing_details?.clear()
        labModifiyRequest.new_details?.clear()
        arrayItemData = labAdapter?.getItems()
        removedListFromOriginal = labAdapter?.getRemovedItems()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val datasize: Int = arrayItemData?.size!!
        for (i in 0..(datasize - 2)) {
            if (arrayItemData!![i]!!.isModifiy!!) {

                //profileuuid
                exsistingDeatil.test_master_uuid = arrayItemData?.get(i)?.test_master_id
                exsistingDeatil.profile_uuid = null

                exsistingDeatil.application_type_uuid = 3
                exsistingDeatil.details_comments = ""
                exsistingDeatil.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID
                exsistingDeatil.patient_order_uuid =
                    arrayItemData?.get(i)?.patient_order_uuid
                exsistingDeatil.to_location_uuid =
                    arrayItemData?.get(i)?.selectToLocationUUID
                exsistingDeatil.type_of_method_uuid =
                    arrayItemData?.get(i)?.selectToTestMethodUUID
                exsistingDeatil.uuid =
                    arrayItemData?.get(i)?.patient_order_details_uuid
                labModifiyRequest.existing_details?.add(exsistingDeatil)
            } else {
                newDetail.test_master_uuid = arrayItemData?.get(i)?.test_master_id
                newDetail.application_type_uuid = 3
                newDetail.doctor_uuid = userDataStoreBean?.uuid.toString()
                newDetail.encounter_type_uuid = encounterType
                newDetail.encounter_uuid = encounter_uuid
                newDetail.from_department_uuid = "0"
                newDetail.group_uuid = 0
                newDetail.is_ordered = true
                newDetail.lab_master_type_uuid = AppConstants.LAB_TESTMASTER_UUID
                newDetail.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID
                //newDetail.order_request_date =
                newDetail.order_status_uuid = 1
                newDetail.patient_order_uuid = arrayItemData?.get(i)?.patient_order_uuid
                newDetail.patient_uuid = patientUuid.toString()
                newDetail.patient_work_order_by = 1
                newDetail.to_department_uuid =
                    department_UUID.toInt()
                newDetail.to_location_uuid =
                    arrayItemData?.get(i)?.selectToLocationUUID
                labModifiyRequest.new_details?.add(newDetail)
            }
        }
        val removeDatasize: Int = removedListFromOriginal?.size!!
        if (removeDatasize > 0) {
            for (i in 0..(removeDatasize - 1)) {
                removeData.patient_orders_uuid = removedListFromOriginal?.get(i)?.patient_order_uuid
                removeData.profile_uuid = null
                removeData.test_master_uuid = removedListFromOriginal?.get(i)?.test_master_id
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
    }


    private fun saveLab() {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val dateInString = sdf.format(Date())
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        arrayItemData = labAdapter?.getItems()

        if (prevOrder) {
            detailsList.clear()
            for (i in arrayItemData?.indices!!) {
                val data = arrayItemData!!.size
                if (i != data - 1) {
                    val details: Detail = Detail()
                    details.encounter_type_uuid = encounterType.toInt()
                    details.encounter_type_uuid = encounterType
                    details.group_uuid = 0
                    details.is_profile = false
                    details.lab_master_type_uuid = AppConstants.LAB_MASTER_ID
                    details.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID!!
                    details.profile_uuid = ""
                    details.test_master_uuid =
                        arrayItemData?.get(i)?.test_master_id!!.toString()
                    details.to_department_uuid = department_UUID.toInt()
                    details.details_comments = arrayItemData?.get(i)?.commands!!
                    details.to_location_uuid =
                        arrayItemData?.get(i)?.selectToLocationUUID.toString()
                    details.to_sub_department_uuid = 0
                    details.type_of_method_uuid =
                        arrayItemData?.get(i)?.selectToTestMethodUUID!!
                    details.application_type_uuid = 10


                    //wardidbind for ip

                    if (encounterType == AppConstants.TYPE_IN_PATIENT) {


                        details.ward_uuid = warduuid
                    }

                    detailsList.add(details)
                }
            }
            header?.consultation_uuid = 0
            header?.department_uuid = department_UUID.toString()
            header?.doctor_uuid = userDataStoreBean?.uuid.toString()
            header?.encounter_type_uuid = encounterType.toInt()
            header?.encounter_uuid = encounter_uuid.toInt()
            header?.encounter_doctor_uuid = doctor_en_uuid.toInt()
            header?.facility_uuid = facility_id.toString()
            header?.lab_master_type_uuid = labMasterTypeUUID.toInt()
            header?.order_status_uuid = 0
            header?.order_to_location_uuid = 1
            header?.patient_treatment_uuid = 0
            header?.patient_uuid = patientUuid.toString()
            header?.sub_department_uuid = 0
            header?.treatment_plan_uuid = 0
            header?.application_type_uuid = 10

            header?.tat_session_start = startTime

            header?.tat_session_end = sdf.format(Date())

            //wardidbind for ip

            if (encounterType == AppConstants.TYPE_IN_PATIENT) {


                header?.ward_uuid = warduuid
            }
            emrRequestModel?.header = this.header!!
            emrRequestModel?.details = this.detailsList
            val request = Gson().toJson(emrRequestModel)
            Log.i("", "" + request)
            viewModel?.labInsert(facility_id, emrRequestModel!!, emrpostRetrofitCallback)

        } else {
            /*   if (arrayItemData?.size!! > 0) {
                   arrayItemData!!.removeAt(arrayItemData!!.size - 1);
               }*/
            detailsList.clear()

            if (arrayItemData?.size!! > 1) {

                val sizeData = arrayItemData!!.size

                for (i in 0..sizeData - 2) {
                    val details: Detail = Detail()
                    details.encounter_type_uuid = encounterType.toInt()
                    details.encounter_type_uuid = encounterType
                    details.group_uuid = 0
                    details.is_profile = false
                    details.lab_master_type_uuid = labMasterTypeUUID.toInt()
                    details.order_priority_uuid =
                        arrayItemData?.get(i)?.selectTypeUUID!!
                    details.details_comments = arrayItemData?.get(i)?.commands!!
                    details.profile_uuid = ""
                    details.test_master_uuid =
                        arrayItemData?.get(i)?.test_master_id!!.toString()
                    details.to_department_uuid = department_UUID.toInt()
                    details.to_location_uuid =
                        arrayItemData?.get(i)?.selectToLocationUUID.toString()
                    details.to_sub_department_uuid = 0
                    details.application_type_uuid = 10


                    //wardidbind for ip

                    if (encounterType == AppConstants.TYPE_IN_PATIENT) {


                        details.ward_uuid = warduuid
                    }
                    details.type_of_method_uuid =
                        arrayItemData?.get(i)?.selectToTestMethodUUID!!

                    detailsList.add(details)
                }

                header?.consultation_uuid = 0
                header?.department_uuid = department_UUID.toString()
                header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                header?.encounter_type_uuid = encounterType.toInt()
                header?.encounter_uuid = encounter_uuid.toInt()
                header?.facility_uuid = facility_id.toString()
                header?.lab_master_type_uuid = labMasterTypeUUID.toInt()
                header?.order_status_uuid = 0
                header?.order_to_location_uuid = 1
                header?.patient_treatment_uuid = 0
                header?.patient_uuid = patientUuid.toString()
                header?.encounter_doctor_uuid = doctor_en_uuid.toInt()
                header?.application_type_uuid = 10

                header?.tat_session_start = startTime

                header?.tat_session_end = sdf.format(Date())


                //wardidbind for ip

                if (encounterType == AppConstants.TYPE_IN_PATIENT) {


                    header?.ward_uuid = warduuid
                }
                header?.sub_department_uuid = 0
                header?.treatment_plan_uuid = 0
                emrRequestModel?.header = this.header!!
                emrRequestModel?.details = this.detailsList

                viewModel?.labInsert(facility_id, emrRequestModel!!, emrpostRetrofitCallback)

            } else {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Please select any one item"
                )
            }
        }
    }

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    val encounterResponseContent = response.body()?.responseContents!!
                    doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_uuid = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)
                    if (isModifiy) {
                        updateSaveOrder()
                    } else {
                        saveLab()
                    }

                } else {
                    viewModel?.createEncounter(
                        patientUuid,
                        encounterType,
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
            patientUuid = response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveLab()
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
    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<FavSearchResponce> {
            override fun onSuccessfulResponse(response: Response<FavSearchResponce>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    labAdapter?.setAdapter(
                        dropdownReferenceView,
                        response.body()?.responseContents!!, searchposition, SpinnerToLocation
                    )

                }
            }

            override fun onBadRequest(response: Response<FavSearchResponce>) {
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
    val getLabTypeRetrofitCallback = object : RetrofitCallback<LabTypeResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabTypeResponseModel>?) {
            labAdapter?.setadapterTypeValue(responseBody?.body()?.responseContents)
            viewModel?.getMasterLocation(getLabToLoctionRetrofitCallback, facility_id)
        }

        override fun onBadRequest(errorBody: Response<LabTypeResponseModel>?) {

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
    val getTestethdCallBack = object : RetrofitCallback<ResponseTestMethod> {
        override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {

            labAdapter!!.setadapterTestMethodValue(response.body()!!.responseContents)

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
    val getLabToLoctionTestRetrofitCallback = object : RetrofitCallback<GetToLocationTestResponse> {
        override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {


            labAdapter?.setToLocation(
                responseBody?.body()?.responseContents,
                SpinnerToLocation,
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
    val emrpostRetrofitCallback = object : RetrofitCallback<EmrResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<EmrResponceModel>?) {
            mCallbackLabFavFragment?.ClearAllData()
            mCallbackLabTemplateFragment?.ClearAllData()
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                //    responseBody?.body()?.message!!
                "Lab Order saved successfully"

            )


            labAdapter?.clearall()
            labAdapter?.addRow(FavouritesModel())
            labAdapter?.addTempleteRow(TempDetails())

            prevOrder = false

            //setAdapter()
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
            isModifiy = false
            labAdapter?.clearall()
            labAdapter?.addRow(FavouritesModel())
            labAdapter?.addTempleteRow(TempDetails())

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

    val getLabToLoctionRetrofitCallback = object : RetrofitCallback<LabToLocationResponse> {

        override fun onSuccessfulResponse(responseBody: Response<LabToLocationResponse>?) {

            labAdapter?.setToLocationList(responseBody?.body()?.responseContents)

            viewModel?.getMethod("type_of_method", getTestethdCallBack)

        }

        override fun onBadRequest(errorBody: Response<LabToLocationResponse>?) {
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


}