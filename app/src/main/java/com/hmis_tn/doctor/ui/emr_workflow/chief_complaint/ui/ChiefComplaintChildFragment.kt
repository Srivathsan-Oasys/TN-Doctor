package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.callbacks.FragmentBackClick
import com.hmis_tn.doctor.component.extention.*
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentChiefComplaintsChildBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.request.ChiefComplaintRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.response.ChiefComplaintResponse
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.ChiefComplaintViewModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.ChiefComplaintViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChiefComplaintChildFragment : Fragment(), ChiefComplaintFavouriteFragment.FavClickedListener,
    PrevChiefComplaintFragment.CancelListener {

    private var sdf: SimpleDateFormat? = null
    private var customdialog: Dialog? = null
    private var binding: FragmentChiefComplaintsChildBinding? = null
    private var viewModel: ChiefComplaintViewModel? = null
    private var utils: Utils? = null
    var chiefComplaintFavouritesAdapter: Chief_Complaint_FavouritesAdapter? = null
    private var responseContents: String? = ""
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    lateinit var chiefComplaintsAdapter: ChiefComplaintsAdapter
    private var fragmentBackClick: FragmentBackClick? = null
    private var selectedSearchPosition = -1
    lateinit var drugNmae: TextView
    lateinit var searchAutoCompleteTextView: AppCompatAutoCompleteTextView
    private val chiefcomplaintRequestArray = ArrayList<ChiefComplaintRequestModel>()
    private var appPreferences: AppPreferences? = null
    private var facility_id: Int? = 0
    private var departmentID: Int? = 0
    var mCallbackChiefFavFragment: ClearFavParticularPositionListener? = null
    var startTime: String? = null
    private var modifiyPosition: Int = 0
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var selectedDurationId: Int? = 0
    private var selectedDurationName: String? = null


    lateinit var chiefComplientMobileAdapter: ChiefComplientMobileAdapter

    var department_UUID: Int = 0
    private var encounterType: Int = 0
    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0
    private var labMasterTypeUUID: Int = 0


    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()
    private var durationMutablemap = mutableMapOf<Int, String>()
    private val durationUuidHashMap: HashMap<Int, Int> = HashMap()
    private val durationNameHashMap: HashMap<Int, String> = HashMap()

    private var chiefComplientArrayList: ArrayList<ResponseContent>? = ArrayList()
    private var chiefComplientMutablemap = mutableMapOf<Int, String>()
    private val chiefCompilentUuidHashMap: HashMap<Int, Int> = HashMap()

    private val chiefComplientData: ArrayList<FavouritesModel?>? = ArrayList()
    private var dataToBeAdded: FavouritesModel = FavouritesModel()
    private var mode: Int = 0
    private var durationaAdapter: DurationAdapter? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_chief_complaints_child,
                container,
                false
            )
        if (activity !is FragmentBackClick) {
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        initView()
        initPrefences()
        initDate()
        initViewModel()
        initDrawerLayout()
        initClickLisitner()
        initViewPager()
        //setAdapterTabAdapter()
        if (isTablet(requireContext())) {
            setAdapterTabAdapter()
        } else {
            setMobileAdapter()
        }
        initComplientSearch()
        addNewItemListner()
        clearItemLisitner()
        viewModel?.getDuration(getDurationRetrofitCallBack)

        return binding!!.root
    }

    private fun initViewModel() {

        viewModel = ChiefComplaintViewModelFactory(
            requireActivity().application
        ).create(ChiefComplaintViewModel::class.java)

        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
    }

    private fun initPrefences() {
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        departmentID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!
        utils = Utils(requireContext())
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }


    private fun initViewPager() {
        setupViewPager(binding?.viewpager!!)
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)
    }

    private fun initDate() {
        sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        startTime = sdf!!.format(Date())
    }

    private fun initDrawerLayout() {
        binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.favouriteTabDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
    }

    private fun initClickLisitner() {
        binding?.saveCardView?.setOnClickListener {
            if (isTablet(requireContext())) {
                val arrayItemData = chiefComplaintsAdapter.getall()
                val datasize: Int = arrayItemData.size
                if (arrayItemData.size > 1) {
                    viewModel?.getEncounter(
                        facility_id!!,
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
            } else {
                if (chiefComplientData?.size!! > 0) {
                    if (checkForError()) {
                        viewModel?.getEncounter(
                            facility_id!!,
                            patientUuid,
                            encounterType,
                            fetchEncounterRetrofitCallBack
                        )
                    } else {
                        utils?.showToast(
                            R.color.positiveToast,
                            binding?.mainLayout!!,
                            "Please check your item"
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
        }

        binding?.clearCardView?.setOnClickListener {
            if (isTablet(requireContext())) {
                chiefComplaintsAdapter.clearall()
                chiefComplaintsAdapter.addRow(FavouritesModel())
                mCallbackChiefFavFragment?.ClearAllData()
            } else {
                chiefComplientMobileAdapter.clearall()
                mCallbackChiefFavFragment?.ClearAllData()
                clearAddAll()
            }
        }
    }

    fun initComplientSearch() {
        binding?.actComplientName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (binding?.actComplientName?.hasFocus()!!) {
                    if (s.length > 2) {
                        viewModel?.getComplaintSearchResult(
                            s.toString(),
                            getComplaintSearchRetrofitCallBack
                        )
                    }

                }
            }
        })

    }

    private fun setupViewPager(viewPager: ViewPager) {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter?.addFragment(ChiefComplaintFavouriteFragment(), "Favourite")
        viewPagerAdapter?.addFragment(PrevChiefComplaintFragment(), "Prev.chiefComplaint")
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

    private fun setMobileAdapter() {
        chiefComplientMobileAdapter = ChiefComplientMobileAdapter(requireActivity(), ArrayList())
        binding?.saveChiefMobileRecyclerView?.adapter = chiefComplientMobileAdapter

        chiefComplientMobileAdapter.onItemClick(object :
            ChiefComplientMobileAdapter.OnSelectItemCommunication {
            override fun onClick(
                position: Int,
                selectedItem: Boolean,
                favouritesModel: FavouritesModel?
            ) {
                chiefComplientMobileAdapter.updateSelectStatus(position, selectedItem)
                if (!selectedItem) {
                    editChiefComplientsItem(position, favouritesModel)
                } else {
                    clearAddAll()
                }

            }

        })
        chiefComplientMobileAdapter.setOnDeleteClickListener(object :
            ChiefComplientMobileAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "${responseContent?.chief_complaint_name} Deleted successfully"

                )
                chiefComplientMobileAdapter.delete(position)

                if (responseContent?.viewChiefcomplaintstatus == 1) {

                    mCallbackChiefFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)
                }
            }
        })

    }


    private fun setAdapterTabAdapter() {
        chiefComplaintsAdapter =
            ChiefComplaintsAdapter(
                requireActivity(),
                ArrayList()
            )
        binding!!.saveChiefComplaintsRecyclerView.adapter = chiefComplaintsAdapter
        chiefComplaintsAdapter.addRow(FavouritesModel())
        chiefComplaintsAdapter.setOnDeleteClickListener(object :
            ChiefComplaintsAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesModel: FavouritesModel?, position: Int) {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "${favouritesModel?.chief_complaint_name} Deleted successfully"

                )
                chiefComplaintsAdapter.deleteRow(favouritesModel, position)

                if (favouritesModel?.viewChiefcomplaintstatus == 1) {

                    mCallbackChiefFavFragment?.ClearFavParticularPosition(favouritesModel.isFavposition)
                }

                /*
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
                       "${drugNmae.text} '" + favouritesModel?.chief_complaint_name + "(" + favouritesModel?.chief_complaint_code + ")" + "'Record ?"
                   val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                   val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                   yesBtn.setOnClickListener {


                       customdialog!!.dismiss()


                   }
                   noBtn.setOnClickListener {
                       customdialog!!.dismiss()
                   }
                   customdialog!!.show()*/

            }
        })
        chiefComplaintsAdapter.setOnSearchInitiatedListener(object :
            ChiefComplaintsAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int
            ) {

                selectedSearchPosition = position
                dropdownReferenceView = view
                viewModel?.getComplaintSearchResult(query, getComplaintSearchRetrofitCallBack)

            }
        })
    }


    fun setSearchAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<ResponseContent>
    ) {

        val responseContentAdapter = ChiefComplaintSearchResultAdapter(
            this.requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->

            val selectedPoi = parent.adapter.getItem(position) as ResponseContent?

            dropdownReferenceView.setText(selectedPoi?.name)


        }
    }

    val insertChiefComplaintsRetrofitCallback = object : RetrofitCallback<ChiefComplaintResponse> {
        override fun onSuccessfulResponse(responseBody: Response<ChiefComplaintResponse>?) {
            trackChiefComplaintsSaveComplete(utils?.getEncounterType(), "success", "")
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Chief  Complaints saved successfully"
            )
            if (isTablet(requireContext())) {
                chiefComplaintsAdapter.clearall()
                chiefComplaintsAdapter.addRow(FavouritesModel())
            } else {
                clearAddAll()
                chiefComplientMobileAdapter.clearall()
                (viewPagerAdapter?.getCurrentFragment(1) as PrevChiefComplaintFragment).refreshList(
                    patientUuid,
                    facility_id
                )
            }
            mCallbackChiefFavFragment?.ClearAllData()

//            chiefComplaintsAdapter?.addTempleteRow(TempDetails())

            Log.i(":", "" + responseBody!!.body().toString())

        }

        override fun onBadRequest(response: Response<ChiefComplaintResponse>) {
            trackChiefComplaintsSaveComplete(
                utils!!.getEncounterType(),
                "failure",
                response.message()
            )
            val gson = GsonBuilder().create()
            val responseModel: FavouritesResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    FavouritesResponseModel::class.java
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
            trackChiefComplaintsSaveComplete(
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
            trackChiefComplaintsSaveComplete(
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
            trackChiefComplaintsSaveComplete(
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
            trackChiefComplaintsSaveComplete(utils!!.getEncounterType(), "failure", failure)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<ComplaintSearchResponseModel> {
            override fun onSuccessfulResponse(response: Response<ComplaintSearchResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        chiefComplaintsAdapter.setAdapter(
                            dropdownReferenceView,
                            response.body()?.responseContents!!,
                            selectedSearchPosition
                        )
                    } else {
                        chiefComplientArrayList = response.body()?.responseContents
                        setSearchMobileAdapter(chiefComplientArrayList)
                    }

                }
            }

            override fun onBadRequest(response: Response<ComplaintSearchResponseModel>) {
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

    val getDurationRetrofitCallBack =
        object : RetrofitCallback<DurationResponseModel> {
            override fun onSuccessfulResponse(response: Response<DurationResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        chiefComplaintsAdapter.setDuration(response.body()?.responseContents!!)
                    } else {
                        durationArrayList = response.body()?.responseContents
                        durationMutablemap =
                            durationArrayList?.map { it?.duration_period_id!! to it.duration_period_name!! }!!
                                .toMap()
                                .toMutableMap()
                        for (i in durationArrayList?.indices!!) {
                            durationUuidHashMap[durationArrayList!![i]?.duration_period_id!!] = i

                        }
                        setDurationAdapter()
                    }
                }
            }

            override fun onBadRequest(response: Response<DurationResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: DurationResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        DurationResponseModel::class.java
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
        if (childFragment is ChiefComplaintFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackChiefFavFragment = childFragment
        }
        if (childFragment is PrevChiefComplaintFragment) {
            childFragment.setOnTextClickedListener(this)
        }
    }


    override fun sendFavAddInChiefFav(
        favmodel: FavouritesModel?,
        position: Int,
        selected: Boolean
    ) {
        favmodel?.viewChiefcomplaintstatus = 1
        favmodel?.isFavposition = position
        if (isTablet(requireContext())) {
            if (!selected) {
                chiefComplaintsAdapter.addFavouritesInRow(favmodel)
            } else {
                chiefComplaintsAdapter.deleteRowFromFav(favmodel?.chief_complaint_id!!, position)
            }
        } else {
            if (!selected) {
                if (!checkAlreadyItemPresent(favmodel)!!) {
                    favmodel?.durationPeriodId = 0
                    favmodel?.durationName = "Select peroid"
                    favmodel?.duration = ""
                    favmodel?.itemAppendString = favmodel?.chief_complaint_name
                    chiefComplientData?.add(favmodel)
                    chiefComplientMobileAdapter.updateAdapter(chiefComplientData)

                } else {
                    itemAlreadyPresent()
                }
            } else {
                for (i in chiefComplientData?.indices!!) {
                    if (chiefComplientData.get(i)?.chief_complaint_id?.equals(favmodel?.chief_complaint_id!!)!!) {
                        this.chiefComplientData.removeAt(i)
                        chiefComplientMobileAdapter.updateAdapter(chiefComplientData)
                    }

                }
            }
        }
    }

    //track ChiefComplaints visit
    fun trackChiefAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackChiefComplaintsVisit(type)
    }

    //track ChiefComplaints save
    fun trackChiefComplaintsSaveStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackChiefComplaintsSaveStart(type)
    }

    fun trackChiefComplaintsSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager()
            .trackChiefComplaintsSaveComplete(type, status, message)
    }

    override fun cancelfunction() {
        binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
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
                    if (isTablet(requireContext())) {
                        saveChiefcomplaient()
                    } else {
                        saveChiefComplientForMobile()

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
            patientUuid =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()
            if (isTablet(requireContext())) {
                saveChiefcomplaient()
            } else {
                saveChiefComplientForMobile()
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

    private fun saveChiefcomplaient() {
        val endTime = sdf?.format(Date())
        val arrayItemData = chiefComplaintsAdapter.getall()
        val datasize: Int = arrayItemData.size
        if (datasize > 1) {
            for (i in 0..(datasize - 2)) {
                val chiefcomplaintRequestModel = ChiefComplaintRequestModel()
                chiefcomplaintRequestModel.chief_complaint_duration =
                    arrayItemData.get(i)!!.duration.toString()
                chiefcomplaintRequestModel.chief_complaint_duration_period_uuid =
                    arrayItemData.get(i)!!.durationPeriodId.toString()
                chiefcomplaintRequestModel.chief_complaint_uuid =
                    arrayItemData.get(i)!!.chief_complaint_id!!
                chiefcomplaintRequestModel.consultation_uuid = 1
                chiefcomplaintRequestModel.encounter_type_uuid = encounterType
                chiefcomplaintRequestModel.encounter_uuid = encounter_uuid
                chiefcomplaintRequestModel.department_uuid = department_UUID.toString()
                chiefcomplaintRequestModel.encounter_doctor_uuid = doctor_en_uuid.toInt()
                chiefcomplaintRequestModel.patient_uuid = patientUuid.toString()
                chiefcomplaintRequestModel.revision = 1
                chiefcomplaintRequestModel.tat_end_time = endTime!!
                chiefcomplaintRequestModel.tat_start_time = startTime as String

                chiefcomplaintRequestArray.add(chiefcomplaintRequestModel)
            }

            viewModel!!.InsertCheifComplant(
                facility_id,
                chiefcomplaintRequestArray,
                insertChiefComplaintsRetrofitCallback
            )
        } else {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select any one item"
            )
        }
    }


    private fun saveChiefComplientForMobile() {
        val endTime = sdf?.format(Date())
        val arrayItemData = chiefComplientData
        val datasize: Int = chiefComplientData?.size!!
        if (datasize > 0) {
            for (i in 0 until datasize) {
                val chiefcomplaintRequestModel = ChiefComplaintRequestModel()
                chiefcomplaintRequestModel.chief_complaint_duration =
                    arrayItemData?.get(i)!!.duration.toString()
                chiefcomplaintRequestModel.chief_complaint_duration_period_uuid =
                    arrayItemData.get(i)!!.durationPeriodId.toString()
                chiefcomplaintRequestModel.chief_complaint_uuid =
                    arrayItemData.get(i)!!.chief_complaint_id!!
                chiefcomplaintRequestModel.consultation_uuid = 1
                chiefcomplaintRequestModel.encounter_type_uuid = encounterType
                chiefcomplaintRequestModel.encounter_uuid = encounter_uuid
                chiefcomplaintRequestModel.department_uuid = department_UUID.toString()
                chiefcomplaintRequestModel.encounter_doctor_uuid = doctor_en_uuid.toInt()
                chiefcomplaintRequestModel.patient_uuid = patientUuid.toString()
                chiefcomplaintRequestModel.revision = 1
                chiefcomplaintRequestModel.tat_end_time = endTime!!
                chiefcomplaintRequestModel.tat_start_time = startTime as String

                chiefcomplaintRequestArray.add(chiefcomplaintRequestModel)
            }

            viewModel!!.InsertCheifComplant(
                facility_id,
                chiefcomplaintRequestArray,
                insertChiefComplaintsRetrofitCallback
            )
        } else {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select any one item"
            )
        }
    }


    private fun setSearchMobileAdapter(chiefComplientArrayList: ArrayList<ResponseContent>?) {
        val responseContentAdapter = ChiefComplaintSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            chiefComplientArrayList!!
        )
        binding?.actComplientName?.threshold = 1
        binding?.actComplientName?.setAdapter(responseContentAdapter)
        binding?.actComplientName?.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ResponseContent?
            binding?.actComplientName?.setText(selectedPoi?.name)
            dataToBeAdded.chief_complaint_name = selectedPoi?.name
            dataToBeAdded.itemAppendString = selectedPoi?.name
            dataToBeAdded.test_master_name = selectedPoi?.name
            dataToBeAdded.chief_complaint_id = selectedPoi?.uuid
            dataToBeAdded.chief_complaint_code = selectedPoi?.code.toString()
        }
    }


    private fun setDurationAdapter() {
        val gridLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding?.durationRecyclerView?.layoutManager = gridLayoutManager
        durationaAdapter = DurationAdapter(requireActivity(), durationArrayList)
        binding?.durationRecyclerView?.adapter = durationaAdapter
        durationaAdapter!!.setOnItemClickListener(object : DurationAdapter.OnItemClickListener {
            override fun onItemClick(durationId: Int, selectedPostion: Int) {
                selectedDurationId = durationId
                selectedDurationName = durationArrayList?.get(selectedPostion)?.duration_period_name
                durationaAdapter!!.updateSelectStatus((durationId))
            }

        })
    }


    private fun addNewItemListner() {
        binding?.add?.setOnClickListener {
            if (dataToBeAdded.chief_complaint_id == 0
                || binding?.actComplientName?.text?.isEmpty()!!
            ) {
                Toast.makeText(context, "Please select any complient", Toast.LENGTH_LONG)?.show()

            } else if (binding?.duration?.text!!.trim().isEmpty()) {
                Toast.makeText(context, "Please enter duration", Toast.LENGTH_LONG)?.show()

            } else if (selectedDurationId == 0) {
                Toast.makeText(context, "Please Select period", Toast.LENGTH_LONG)?.show()

            } else {
                if (dataToBeAdded.isEditableMode!!) {
                    dataToBeAdded.isEditableMode = false
                    dataToBeAdded.labDataSelected = false
                    dataToBeAdded.duration = binding?.duration?.text.toString()
                    dataToBeAdded.durationPeriodId = selectedDurationId
                    dataToBeAdded.durationName = selectedDurationName
                    chiefComplientData?.set(modifiyPosition, dataToBeAdded)
                    chiefComplientMobileAdapter.updateAdapter(chiefComplientData)
                    clearAddAll()
                } else {
                    if (!checkAlreadyItemPresent(dataToBeAdded)!!) {
                        dataToBeAdded.duration = binding?.duration?.text.toString()
                        dataToBeAdded.durationPeriodId = selectedDurationId
                        dataToBeAdded.durationName = selectedDurationName
                        chiefComplientData?.add(dataToBeAdded)
                        chiefComplientMobileAdapter.updateAdapter(chiefComplientData)
                        clearAddAll()
                    } else {
                        itemAlreadyPresent()
                    }
                }
            }
        }
    }

    private fun editChiefComplientsItem(position: Int, favouritesModel: FavouritesModel?) {
        mode = 2
        modifiyPosition = position
        dataToBeAdded = FavouritesModel()
        dataToBeAdded = favouritesModel!!
        dataToBeAdded.isEditableMode = true
        binding?.actComplientName?.setText(dataToBeAdded.chief_complaint_name)
        binding?.duration?.setText(dataToBeAdded.duration)
        durationaAdapter!!.updateSelectStatus((dataToBeAdded.durationPeriodId!!))
        showDropDown()
    }

    private fun clearItemLisitner() {
        binding?.clear?.setOnClickListener {
            clearAddAll()
        }
    }

    fun clearAddAll() {
        binding?.actComplientName?.setText("")
        binding?.duration?.setText("")
        selectedDurationId = 0
        selectedDurationName = ""
        durationaAdapter?.updateSelectStatus(0)
        dataToBeAdded = FavouritesModel()
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

    fun checkAlreadyItemPresent(favouritesModel: FavouritesModel?): Boolean? {
        return chiefComplientData?.any { it!!.chief_complaint_id == favouritesModel?.chief_complaint_id }
    }

    private fun itemAlreadyPresent() {
        Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
    }

    private fun initView() {
        binding?.llDropDownView?.hide()
        binding?.rlHeader?.setOnClickListener {
            if (binding?.llDropDownView?.isvisible()!!) {
                hideDropDown()
            } else {
                showDropDown()
            }
        }
    }

    private fun checkForError(): Boolean {
        var result = true
        for (i in chiefComplientData?.indices!!) {
            val check = chiefComplientData.get(i)?.isReadyForSave!!
            if (!check) {
                result = check
            }
        }
        return result
    }
}
