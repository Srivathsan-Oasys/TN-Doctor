package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.component.extention.hide
import com.oasys.digihealth.doctor.component.extention.isvisible
import com.oasys.digihealth.doctor.component.extention.show
import com.oasys.digihealth.doctor.component.extention.slideDown
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentLabChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.LabViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavSearch
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
import com.oasys.digihealth.doctor.ui.quick_reg.model.ResponseTestMethodContent
import com.oasys.digihealth.doctor.utils.Utils
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import kotlinx.android.synthetic.main.fragment_lab_child_new.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LabChildFragmentNew : Fragment(), LabFavouriteFragment.FavClickedListener,
    LabTempleteFragment.TempleteClickedListener, PrevLabFragment.LabPrevClickedListener,
    ManageLabSaveTemplateFragment.LabChiefComplaintListener,
    CommentDialogFragment.CommandClickedListener {
    private var arrayItemData: ArrayList<FavouritesModel?>? = ArrayList()
    private var binding: FragmentLabChildBinding? = null
    lateinit var drugNmae: TextView
    private var viewModel: LabViewModel? = null
    private var utils: Utils? = null
    private var customdialog: Dialog? = null
    private var responseContents: String? = ""
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    lateinit var SpinnerToLocation: Spinner
    var labAdapter: LabListingAdapter? = null
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

    var selectedResponseContent: FavouritesModel = FavouritesModel()
    private var viewPagerAdapter: ViewPagerAdapter? = null

    private var priorityListFilter: ArrayList<LabTypeResponseContent?>? =
        ArrayList()
    private var priorityList = mutableMapOf<Int, String>()
    private val priorityNameHashMapUUid: HashMap<Int, Int> = HashMap()
    private val priorityNameHashMap: HashMap<Int, String> = HashMap()

    private var testMethodListFilter: ArrayList<ResponseTestMethodContent?>? =
        ArrayList()
    private var testMethodList = mutableMapOf<Int, String>()
    private val testMethodHashMap: HashMap<Int, Int> = HashMap()
    private val testMethodNameHashMap: HashMap<Int, String> = HashMap()

    private var orderLocationListFilter: ArrayList<LabToLocationResponse.LabToLocationContent?>? =
        ArrayList()
    private var orderToLocationList = mutableMapOf<Int, String>()
    private val orderLocationHashMap: HashMap<Int, Int> = HashMap()


    private var priorityListDummy = mutableMapOf<Int, String>()
    private var testMethodListDummy = mutableMapOf<Int, String>()
    private var orderToLocationListDummy = mutableMapOf<Int, String>()


    private var mode: Int = 0
    private var modifiyPosition: Int = 0

    val labModifiyRequest: LabModifiyRequest = LabModifiyRequest()
    var removedListFromOriginal: ArrayList<FavouritesModel?>? = ArrayList()

    val exsistingDeatil: LabModifiyRequest.ExistingDetail =
        LabModifiyRequest.ExistingDetail()
    val newDetail: LabModifiyRequest.NewDetail =
        LabModifiyRequest.NewDetail()
    val removeData: LabModifiyRequest.RemovedDetail = LabModifiyRequest.RemovedDetail()
    var fromFavData: FavouritesModel? = null
    private var customProgressDialog: CustomProgressDialog? = null


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

        initView()
        initPerefences()
        initViewModel()
        initCompleteCompleteTextView()
        initAddView()
        setSpinnerClickListiner()
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        binding?.drawerLayout!!.openDrawer(GravityCompat.END)
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
        binding?.saveCardView?.isEnabled = true


        setAdapter()
        setupViewPager(binding?.viewpager!!)
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)

        viewModel?.getLabType(getLabTypeRetrofitCallback, facility_id)

        labAdapter?.setOnCommandClickListener(object : LabListingAdapter.OnCommandClickListener {
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
        binding?.saveCardView!!.setOnClickListener {
            tracklabSaveStart(utils?.getEncounterType())
            arrayItemData = labAdapter?.getItems()
            if (arrayItemData?.size!! > 0) {
                if (checkForError()) {
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
                        "Please Check your items"
                    )
                }
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
            //     mCallbackLabTemplateFragment?.ClearAllData()
            labAdapter?.clearall()
            isModifiy = false
            prevOrder = false
            labAdapter?.getIdList()
            clearAddAll()
        }


        labAdapter?.onItemClick(object : LabListingAdapter.OnSelectItemCommunication {
            override fun onClick(
                position: Int,
                selectedItem: Boolean,
                favouritesModel: FavouritesModel?
            ) {
                labAdapter?.updateSelectStatus(position, selectedItem)
                editLabItem(position, favouritesModel)

            }

        })

        binding?.saveTemplateCardView!!.setOnClickListener {
            val arrayItemData = labAdapter!!.getItems()
            if (arrayItemData.size > 0) {
                if (checkForError()) {
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
                        "Please Check your items"
                    )
                }
            } else {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Please select any one item"
                )
            }
        }
        return binding!!.root
    }


    fun initPerefences() {
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())
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
        trackLabAnalyticsVisit(utils!!.getEncounterType())
    }

    fun initViewModel() {
        viewModel = LabViewModelFactory(
            requireActivity().application
        ).create(LabViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this

    }

    private fun setupViewPager(viewPager: ViewPager) {

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter?.addFragment(LabFavouriteFragment(), "Favourite")
        viewPagerAdapter?.addFragment(LabTempleteFragment(), "Template")
        viewPagerAdapter?.addFragment(PrevLabFragment(), "Prev.Lab")

        viewPager.adapter = viewPagerAdapter

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

    private fun setAdapter() {
        labAdapter =
            LabListingAdapter(
                requireActivity(),
                arrayItemData, ArrayList()
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
        // labAdapter?.addRow(FavouritesModel())
        labAdapter?.addTempleteRow(TempDetails())

        labAdapter?.setOnitemChangeListener(object : LabListingAdapter.OnitemChangeListener {
            override fun onitemChangeClick(uuid: ArrayList<Int>) {

                mCallbackLabFavFragment?.checkanduncheck(uuid, true)

                mCallbackLabTemplateStatus?.checkanduncheck(uuid)

            }
        })

        labAdapter?.setOnDeleteClickListener(object :
            LabListingAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                val check = labAdapter?.deleteRow(position)
                if (responseContent?.viewLabstatus == 1) {
                    labAdapter?.getIdList()
                    // mCallbackLabFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)
                } else if (responseContent?.viewLabstatus == 2) {
                    if (check!!) {
                        labAdapter?.getIdList()
                    }

                }
                if (position == modifiyPosition) {
                    clearAddAll()
                }

                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Test name Deleted successfully"

                )

            }
        })


    }

    val getLabToLoctionTestRetrofitCallback = object : RetrofitCallback<GetToLocationTestResponse> {
        override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {
            responseBody?.body()?.responseContents?.to_location_uuid?.let {
                setOrderTolocationTestMethod(
                    it
                )
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


    val getFavToLoctionTestRetrofitCallback = object : RetrofitCallback<GetToLocationTestResponse> {
        override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {
            hideProgressDailog()
            labAdapter?.setToLocation(
                responseBody?.body()?.responseContents
            )
            hideProgressDailog()
        }

        override fun onBadRequest(errorBody: Response<GetToLocationTestResponse>?) {
            hideProgressDailog()
        }

        override fun onServerError(response: Response<*>?) {
            hideProgressDailog()
        }

        override fun onUnAuthorized() {
            hideProgressDailog()
        }

        override fun onForbidden() {
            hideProgressDailog()
        }

        override fun onFailure(s: String?) {
            hideProgressDailog()
        }

        override fun onEverytime() {
            hideProgressDailog()
        }

    }

    val emrpostRetrofitCallback = object : RetrofitCallback<EmrResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<EmrResponceModel>?) {
            trackLabSaveComplete(utils?.getEncounterType(), "success", "")

            mCallbackLabFavFragment?.ClearAllData()
            mCallbackLabTemplateFragment?.ClearAllData()
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                //    responseBody?.body()?.message!!
                "Lab Order saved successfully"

            )
            labAdapter?.clearall()
            clearAddAll()
            prevOrder = false
            (viewPagerAdapter?.getCurrentFragment(2) as PrevLabFragment).refreshList(
                patientUuid,
                facility_id
            )

        }

        override fun onBadRequest(response: Response<EmrResponceModel>?) {
            val gson = GsonBuilder().create()
            trackLabSaveComplete(utils!!.getEncounterType(), "failure", response?.message())
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
            trackLabSaveComplete(
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
            trackLabSaveComplete(
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
            trackLabSaveComplete(
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
            trackLabSaveComplete(utils!!.getEncounterType(), "failure", failure)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
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
                    setTestNameAdpater(response.body()?.responseContents!!)
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


    val getFavSearchRetrofitCallBack =
        object : RetrofitCallback<FavSearchResponce> {
            override fun onSuccessfulResponse(response: Response<FavSearchResponce>) {
                val check =
                    testMethodHashMap.any { it.key == response.body()?.responseContents?.get(0)?.type_of_method_uuid }
                if (check) {
                    labAdapter?.setTestMethod(
                        response.body()?.responseContents?.get(0)?.type_of_method_uuid,
                        response.body()?.responseContents?.get(0)?.uuid,
                        testMethodNameHashMap[response.body()?.responseContents?.get(0)?.type_of_method_uuid!!]
                    )
                }
                hideProgressDailog()
            }

            override fun onBadRequest(response: Response<FavSearchResponce>) {
                hideProgressDailog()
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
                hideProgressDailog()
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                hideProgressDailog()
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                hideProgressDailog()
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                hideProgressDailog()
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                hideProgressDailog()
                viewModel!!.progress.value = 8
            }
        }

    val getLabTypeRetrofitCallback = object : RetrofitCallback<LabTypeResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabTypeResponseModel>?) {
            var data = LabTypeResponseContent()
            data.uuid = 0
            data.name = "select Type"
            priorityListFilter?.add(data)
            responseBody?.body()?.responseContents?.let { priorityListFilter?.addAll(it) }
            priorityList =
                priorityListFilter?.map { it?.uuid!! to it.name!! }!!.toMap()
                    .toMutableMap()
            priorityNameHashMap.clear()
            priorityNameHashMapUUid.clear()
            for (i in priorityListFilter?.indices!!) {
                priorityNameHashMapUUid[i] =
                    priorityListFilter?.get(i)?.uuid!!
                priorityNameHashMap[priorityListFilter?.get(i)!!.uuid!!] =
                    priorityListFilter?.get(i)?.name!!
            }
            labAdapter?.setadapterTypeValue(priorityListFilter)
            viewModel?.getMethod("type_of_method", getTestethdCallBack)

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
    val getMasterLoctionRetrofitCallback = object : RetrofitCallback<LabToLocationResponse> {

        override fun onSuccessfulResponse(responseBody: Response<LabToLocationResponse>?) {
            var data = LabToLocationResponse.LabToLocationContent()
            data.uuid = 0
            data.location_name = "selectLocation"
            orderLocationListFilter?.add(data)
            orderLocationListFilter?.addAll(responseBody?.body()?.responseContents!!)
            orderToLocationList =
                orderLocationListFilter?.map { it?.uuid!! to it.location_name!! }!!
                    .toMap().toMutableMap()
            orderLocationHashMap.clear()
            for (i in orderLocationListFilter?.indices!!) {
                orderLocationHashMap[orderLocationListFilter?.get(i)!!.uuid!!] = i
            }
            labAdapter?.setToLocationList(orderLocationListFilter)
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
    val getTestethdCallBack = object : RetrofitCallback<ResponseTestMethod> {
        override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {
            val data = ResponseTestMethodContent()
            data.name = "SelectMethod"
            data.uuid = 0
            testMethodListFilter?.add(data)
            response.body()?.responseContents?.let { testMethodListFilter?.addAll(it) }
            testMethodList =
                testMethodListFilter?.map { it?.uuid!! to it.name!! }!!.toMap()
                    .toMutableMap()
            testMethodHashMap.clear()
            testMethodNameHashMap.clear()
            for (i in testMethodListFilter?.indices!!) {
                testMethodHashMap[testMethodListFilter?.get(i)!!.uuid!!] = i
                testMethodNameHashMap[testMethodListFilter?.get(i)!!.uuid!!] =
                    testMethodListFilter?.get(i)?.name!!
            }
            labAdapter!!.setadapterTestMethodValue(testMethodListFilter)
            viewModel?.getMasterLocation(getMasterLoctionRetrofitCallback, facility_id)

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
                favmodel?.selectTypeName = priorityNameHashMap[2]
                favmodel?.test_master_name = labFav!!.test_master_name
                favmodel?.test_master_id = labFav.test_master_id
                favmodel?.test_master_code = labFav.test_master_code
                showprogressDailog()
                labAdapter!!.addFavouritesInRowModule(1, favmodel, position, selected)
                viewModel?.getComplaintSearchResult(
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
                )
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
                favmodel?.selectedLocationName = "Select Location"
                favmodel?.selectToTestMethodName = "Select Test Method"
                favmodel?.selectTypeUUID = 2
                favmodel?.selectTypeName = priorityNameHashMap[2]
                showprogressDailog()
                labAdapter!!.addFavouritesInRowModule(2, favmodel, position, selected)
                viewModel?.getComplaintSearchResult(
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
                )
            }
        } else {
            for (i in templeteDetails!!.indices) {
                labAdapter!!.deleteRowFromTemplate(templeteDetails[i]!!.lab_test_uuid, 2)
            }

        }
    }

    override fun sendPrevtoChild(
        responseContent: List<PodArrResult>?,
        modifiy: Boolean
    ) {
        labAdapter?.clearall()
        prevOrder = true
        this.isModifiy = modifiy
        for (i in responseContent!!.indices) {
            val favmodel: FavouritesModel? = FavouritesModel()
            favmodel!!.chief_complaint_name = responseContent[i].name
            favmodel.itemAppendString = responseContent[i].name
            favmodel.test_master_name = responseContent[i].name
            favmodel.test_master_id = responseContent[i].test_master_uuid
            favmodel.test_master_code = responseContent[i].code
            favmodel.selectTypeUUID = responseContent[i].order_priority_uuid
            favmodel.selectTypeName = responseContent[i].order_priority_name
            favmodel.selectedLocationName = responseContent[i].order_to_location
            favmodel.selectToLocationUUID = responseContent[i].order_to_location_uuid
            favmodel.selectToTestMethodUUID = responseContent[i].test_method_uuid
            favmodel.selectToTestMethodName = testMethodNameHashMap.get(
                responseContent[i].test_method_uuid
            )
            if (modifiy) {
                favmodel.patient_order_details_uuid =
                    responseContent[i].patient_order_details_uuid
                favmodel.patient_order_uuid = responseContent[i].patient_order_uuid
                favmodel.isModifiy = true
            } else {
                favmodel.isModifiy = false
            }
            labAdapter!!.addPrevModifyInRow(favmodel)

        }


    }

    override fun sendDataChiefComplaint() {
        mCallbackLabTemplateFragment?.GetTemplateDetails()
    }

    //track lab visit
    fun trackLabAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackLabVisit(type)
    }


    fun tracklabSaveStart(type: String?) {
        AnalyticsManager.getAnalyticsManager().tracklabSaveStart(type)
    }


    fun trackLabSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackLabSaveComplete(type, status, message)
    }

    override fun sendCommandPosData(position: Int, command: String) {

        labAdapter!!.addCommands(position, command)
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

    fun checkForError(): Boolean {
        var result: Boolean = true
        for (i in arrayItemData?.indices!!) {
            val check = arrayItemData!!.get(i)?.isReadyForSave!!
            if (!check) {
                result = check
            }
        }
        return result
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

            if (arrayItemData?.size!! > 0) {

                val sizeData = arrayItemData!!.size

                for (i in 0..sizeData - 1) {
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


    val getCodeSearchRetrofitCallBack =
        object : RetrofitCallback<FavSearchResponce> {
            override fun onSuccessfulResponse(response: Response<FavSearchResponce>) {
                //responseContents = response.body()?.responseContents!!
                setTestCodeAdapter(response.body()?.responseContents!!)
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
                        mainLayout!!,
                        responseModel.message!!
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast, mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


            doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patientUuid =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()
            if (isModifiy) {
                updateSaveOrder()
            } else {
                saveLab()
            }
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


    fun initView() {
        binding?.llDropDownView?.hide()
        binding?.rlHeader?.setOnClickListener {
            if (binding?.llDropDownView?.isvisible()!!) {
                hideDropDown()
            } else {
                showDropDown()
            }
        }
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

    fun setTestNameAdpater(responseContents: ArrayList<FavSearch>) {
        val responseContentAdapter = LabSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents,
            true
        )
        binding?.actCodeTestName?.threshold = 1
        binding?.actCodeTestName?.setAdapter(responseContentAdapter)
        binding?.actCodeTestName?.showDropDown()
        binding?.actCodeTestName?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actCodeTestName?.clearFocus()
            binding?.actCodeTestName?.setText(selectedPoi?.name)
            binding?.actCode?.setText(selectedPoi?.code)
            setOtherData(selectedPoi)
            selectedResponseContent.test_master_code = selectedPoi?.code
            selectedResponseContent.test_master_name = selectedPoi?.name
            selectedResponseContent.test_master_id = selectedPoi?.uuid
        }

    }


    fun setTestCodeAdapter(responseContents: ArrayList<FavSearch>) {
        val responseContentAdapter = LabSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents,
            false
        )
        binding?.actCode?.threshold = 1
        binding?.actCode?.setAdapter(responseContentAdapter)
        binding?.actCode?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actCode?.setText(selectedPoi?.code)
            binding?.actCodeTestName?.setText(selectedPoi?.name)
            setOtherData(selectedPoi)
            selectedResponseContent.test_master_code = selectedPoi?.code
            selectedResponseContent.test_master_name = selectedPoi?.name
            selectedResponseContent.test_master_id = selectedPoi?.uuid
        }


    }

    fun setOtherData(data: FavSearch?) {
        setTypeSpinner()
        setTestMethodSpinner(data?.type_of_method_uuid)
        setOrderToLocationSpinner()
        if (mode != 2) {
            viewModel?.getToLocationTest(
                getLabToLoctionTestRetrofitCallback,
                facility_id,
                department_UUID,
                data?.uuid
            )
        }
    }

    fun setTypeSpinner() {
        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                priorityList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerPriority?.adapter = adapter
        if (mode != 2) {
            binding?.spinnerPriority?.setSelection(1)
            selectedResponseContent.selectTypeUUID = priorityNameHashMapUUid.get(2)!!
            selectedResponseContent.selectTypeName =
                priorityNameHashMap.get(priorityNameHashMapUUid.get(selectedResponseContent.selectTypeUUID)!!)
        } else {
            priorityNameHashMapUUid.get(selectedResponseContent.selectTypeUUID)?.let {
                binding?.spinnerPriority?.setSelection(
                    it
                )
            }
        }
    }

    fun setTestMethodSpinner(typeOfMethodUuid: Int?) {
        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                testMethodList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerTestMethod?.adapter = adapter
        if (mode != 2) {
            if (typeOfMethodUuid != null && typeOfMethodUuid != 0) {
                try {
                    if (testMethodHashMap.containsKey(typeOfMethodUuid)) {
                        binding?.spinnerTestMethod?.setSelection(
                            testMethodHashMap.get(
                                typeOfMethodUuid
                            )!!
                        )
                        selectedResponseContent.selectToTestMethodUUID = typeOfMethodUuid
                        selectedResponseContent.selectToTestMethodName = testMethodNameHashMap.get(
                            typeOfMethodUuid
                        )

                    }
                } catch (e: Exception) {

                }

            } else {
                binding?.spinnerTestMethod?.setSelection(0)
            }
        } else {
            testMethodHashMap.get(
                selectedResponseContent.selectToTestMethodUUID
            )?.let {
                binding?.spinnerTestMethod?.setSelection(
                    it
                )
            }
        }
    }

    fun setOrderToLocationSpinner() {
        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                orderToLocationList.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerOrderLocation?.adapter = adapter
        if (mode != 2) {
            binding?.spinnerOrderLocation?.setSelection(0)
        } else {
            orderLocationHashMap.get(selectedResponseContent.selectToLocationUUID)?.let {
                binding?.spinnerOrderLocation?.setSelection(
                    it
                )
            }
            mode = 0
        }
    }


    fun initCompleteCompleteTextView() {
        binding?.actCodeTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (binding?.actCodeTestName?.hasFocus()!!) {
                    if (s.length > 2) {
                        if (mode != 2) {
                            viewModel?.getComplaintSearchResult(
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
                if (s.length > 2) {
                    if (mode != 2) {
                        viewModel?.getCodeSearchResult(
                            facility_id,
                            s.toString(),
                            getCodeSearchRetrofitCallBack
                        )
                    }
                }
            }
        })
    }


    fun initAddView() {
        binding?.add?.setOnClickListener {
            if (act_code.text.isEmpty() || act_code_test_name.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please Select  TestName ", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedResponseContent.selectTypeUUID == 0) {
                Toast.makeText(requireContext(), "Please Select Priority", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedResponseContent.selectToTestMethodUUID == 0) {
                Toast.makeText(requireContext(), "Please Select Test Method", Toast.LENGTH_SHORT)
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
                    arrayItemData?.set(modifiyPosition, selectedResponseContent)
                    labAdapter?.updateAdapter(arrayItemData)
                    clearAddAll()

                } else {
                    val check =
                        arrayItemData?.any { it!!.test_master_id == selectedResponseContent.test_master_id }
                    if (!check!!) {
                        arrayItemData?.add(selectedResponseContent)
                        labAdapter?.updateAdapter(arrayItemData)
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

        binding?.clear?.setOnClickListener {
            clearAddAll()
        }

    }

    fun checkForDuplicate(
        responseContent: FavouritesModel?
    ) {
        val check =
            arrayItemData?.any { it!!.test_master_id == responseContent?.test_master_id }
        if (check!!) {
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    fun clearAddAll() {
        binding?.actCodeTestName?.setText("")
        binding?.actCode?.setText("")
        setDummyPrioritySpinner()
        setDummyPirorityList()
        setDummyOrderToLocationSpinner()
        selectedResponseContent = FavouritesModel()
    }

    fun setDummyPrioritySpinner() {
        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                priorityListDummy.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerPriority?.adapter = adapter
    }

    fun setDummyPirorityList() {
        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                testMethodListDummy.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerTestMethod?.adapter = adapter
    }

    fun setDummyOrderToLocationSpinner() {
        val adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                orderToLocationListDummy.values.toMutableList()
            )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner_order_location.adapter = adapter
        binding?.spinnerOrderLocation?.setSelection(0)
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
                    selectedResponseContent.selectedLocationName =
                        orderToLocationList.get(selectedResponseContent.selectToLocationUUID)!!
                }

            }

        binding?.spinnerTestMethod?.onItemSelectedListener =
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
                    selectedResponseContent.selectToTestMethodUUID =
                        testMethodList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedResponseContent.selectToTestMethodName =
                        testMethodNameHashMap.get(selectedResponseContent.selectToTestMethodUUID)
                            .toString()

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
                    selectedResponseContent.selectTypeName =
                        priorityNameHashMap.get(selectedResponseContent.selectTypeUUID)

                }

            }
    }


    fun setOrderTolocationTestMethod(toLocationUuid: Int) {
        val check =
            this.orderLocationHashMap.any { it.key == toLocationUuid }
        if (check) {
            spinner_order_location.setSelection(orderLocationHashMap[toLocationUuid]!!)
            selectedResponseContent.selectToLocationUUID = toLocationUuid
        }
    }

    fun editLabItem(
        position: Int,
        favouritesModel: FavouritesModel?
    ) {
        mode = 2
        modifiyPosition = position
        selectedResponseContent = FavouritesModel()
        selectedResponseContent = favouritesModel!!
        selectedResponseContent.isEditableMode = true
        binding?.actCodeTestName?.setText(selectedResponseContent.test_master_name)
        binding?.actCode?.setText(selectedResponseContent.test_master_code)
        setOtherData(null)
        showDropDown()

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
            clearAddAll()
            labAdapter?.clearall()
            (viewPagerAdapter?.getCurrentFragment(2) as PrevLabFragment).refreshList(
                patientUuid,
                facility_id
            )

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


    fun updateSaveOrder() {
        labModifiyRequest.removed_details?.clear()
        labModifiyRequest.existing_details?.clear()
        labModifiyRequest.new_details?.clear()
        arrayItemData = labAdapter?.getItems()
        removedListFromOriginal = labAdapter?.getRemovedItems()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val datasize: Int = arrayItemData?.size!!
        for (i in 0..(datasize - 1)) {
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

    fun showprogressDailog() {
        if (customProgressDialog != null) {
            customProgressDialog?.show()
        }
    }

    fun hideProgressDailog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
        }
    }


}



