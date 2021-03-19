package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui


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
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.component.extention.*
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentRadiologyChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabSearchResultAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabToLocationResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PodArrResult
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavSearch
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavSearchResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Request.EmrRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Request.Header
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.LabDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.saveTemplate.ManageRadiologySaveTemplateFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.RadiologyViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.RadiologyViewModelFactory
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class RadiologyChildFragment : Fragment(), RadiologyFavouriteFragment.RadiologyFavClickedListener,
    RadiologyTempleteFragment.RadiologyTempleteClickedListener,
    PrevRadiologyFragment.RadiologyPrevClickedListener,
    ManageRadiologySaveTemplateFragment.LabChiefComplaintListener,
    RadiologyCommentDialogFragment.CommandClickedListener,
    ManageRadiologySaveTemplateFragment.RadiologyTempReferesh {

    private var customProgressDialog: CustomProgressDialog? = null

    private var customdialog: Dialog? = null
    lateinit var drugNmae: TextView

    private var facility_id: Int? = 0
    private var department_UUID: Int? = 0
    private var Str_auto_id: Int? = 0
    private var encounterType: Int = 0
    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0
    private var doctor_uuid: Int? = 0


    private var deleteList: ArrayList<Int> = ArrayList()
    private var modifiedData: ArrayList<PodArrResult>? = ArrayList()
    private var arrayItemData: ArrayList<FavouritesModel?>? = ArrayList()
    val detailsList = ArrayList<Detail>()
    var arrayList: ArrayList<FavouritesModel>? = null


    private var searchPosition: Int? = 0
    private var binding: FragmentRadiologyChildBinding? = null
    private var viewModel: RadiologyViewModel? = null

    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null

    private var responseContents: String? = ""
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    lateinit var SpinnerToLocation: Spinner


    var radiologyAdapter: RadiologyAdapter? = null

    val header: Header? = Header()
    val emrRequestModel: EmrRequestModel? = EmrRequestModel()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var mCallbackLabFavFragment: ClearFavParticularPositionListener? = null
    var mCallbackLabTemplateFragment: ClearTemplateParticularPositionListener? = null
    var mCallbackRadiologyFavFragment: ClearFavParticularPositionListener? = null
    var mCallbackRadiologyTemplateFragment: ClearTemplateParticularPositionListener? = null
    private var fragmentBackClick: FragmentBackClick? = null


    var prevOrder: Boolean = false
    var isModifiy: Boolean = false
    var isCodeSearch: Boolean? = null

    var radiologyMobileAdapter: RadiologyMobileAdapter? = null


    private var typeListMutableMap = mutableMapOf<Int, String>()
    private var typeUuidList = HashMap<Int, Int>()
    private var typeList: ArrayList<RadiologyTypeResponseContent?>? =
        ArrayList()

    private var loationListMutableMap = mutableMapOf<Int, String>()
    private var locationUuisList = HashMap<Int, Int>()
    private var locationList: ArrayList<LabToLocationResponse.LabToLocationContent?>? =
        ArrayList()


    var mobileTypeSpinnerAdapter: ArrayAdapter<String>? = null
    var mobileLocationSpinnerAdapter: ArrayAdapter<String>? = null

    private var dataToBeAdded: FavouritesModel? = FavouritesModel()
    private var modifiyPosition: Int = 0
    private var viewPagerAdapter: ViewPagerAdapter? = null

    private var selectedTypeUuid: Int? = 0
    private var selectedTypeName: String? = ""

    private var selectedLocationUuid: Int? = 0
    private var selectedLocationName: String? = ""

    private var selectedRadiologyCode: String? = ""
    private var selectedRadiologyName: String? = ""
    private var selectedRadiologyUUID: Int? = 0
    private var patientOrderUUid: Int? = 0


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_radiology_child,
                container,
                false
            )

        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        deleteList.clear()
        binding?.saveCardView?.isEnabled = true
        initPref()
        initViewmodel()
        initDrawerLayout()
        initSaveView()
        setupViewPager(binding?.viewpager!!)
        if (isTablet(requireContext())) {
            setTabAdapter()
        } else {
            binding?.llAddRadiologyDetail?.hide()
            binding?.llAddRadiology?.setOnClickListener {
                if (binding?.llAddRadiologyDetail?.isvisible()!!) {
                    hideDropDown()
                } else {
                    showDropDown()
                }
            }
            setMobileAdapter()
        }
        return binding!!.root
    }

    private fun initPref() {
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences?.saveInt(AppConstants.LAB_MASTER_TYPE_ID, 2)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        doctor_uuid = appPreferences?.getInt(AppConstants.DoctorUUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
    }

    private fun initViewmodel() {
        viewModel = RadiologyViewModelFactory(
            requireActivity().application
        ).create(RadiologyViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        if (isTablet(requireContext())) {
            viewModel?.getRadiologyType(getRadiologyTypeRetrofitCallback, facility_id)
            viewModel?.getToLocationList(
                getRadiologyToLocationListRetrofitCallback,
                facility_id,
                department_UUID,
                Str_auto_id
            )
        }
    }

    private fun initDrawerLayout() {
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
    }

    private fun initSaveView() {

        binding!!.saveTemplate?.setOnClickListener {
            var arrayItemData = ArrayList<FavouritesModel?>()
            if (isTablet(requireContext())) {
                arrayItemData = radiologyAdapter!!.getAll()
                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = ManageRadiologySaveTemplateFragment()
                val bundle = Bundle()
                bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, arrayItemData)

                labtemplatedialog.arguments = bundle

                labtemplatedialog.show(ft, "Tag")
            } else {
                arrayItemData = radiologyMobileAdapter!!.getAll()
                if (arrayItemData.size > 0) {
                    val ft = childFragmentManager.beginTransaction()
                    val labtemplatedialog = ManageRadiologySaveTemplateFragment()
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, arrayItemData)

                    labtemplatedialog.arguments = bundle

                    labtemplatedialog.show(ft, "Tag")
                } else {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.frameLayout!!,
                        getString(R.string.Please_add_one_item)
                    )
                }
            }


        }

        binding?.saveCardView!!.setOnClickListener {
            if (isTablet(requireContext())) {
                arrayItemData = radiologyAdapter?.getItems()
                val datasize: Int = arrayItemData?.size!!
                createTabEncounter(datasize)
            } else {
                arrayItemData = radiologyMobileAdapter?.getItems()
                val datasize: Int = arrayItemData?.size!!
                createMobileEncounter(datasize)

            }
            Log.e("arrayItemData", "___" + arrayItemData.toString())
            detailsList.clear()


        }

        binding?.clearCardView?.setOnClickListener {
            radiologyMobileAdapter?.clearall()
            mCallbackRadiologyFavFragment?.ClearAllData()
            mCallbackRadiologyTemplateFragment?.ClearAllData()
            deleteList.clear()
            radiologyAdapter?.clearall()
            radiologyAdapter?.addRow(FavouritesModel())
            radiologyAdapter?.addTempleteRow(TempDetails())
            isModifiy = false
            prevOrder = false
        }
    }

    fun createMobileEncounter(datasize: Int) {
        if (checkForError()) {
            if (datasize > 0) {
                viewModel?.getEncounter(
                    facility_id!!,
                    patientUuid,
                    encounterType,
                    fetchEncounterRetrofitCallBack
                )

            } else {
                showToast("Please select any one item")
            }
        } else {
            showToast("Please check your items")
        }
    }

    fun createTabEncounter(datasize: Int) {
        if (datasize > 1) {
            viewModel?.getEncounter(
                facility_id!!,
                patientUuid,
                encounterType,
                fetchEncounterRetrofitCallBack
            )

        } else {
            showToast("Please select any one item")
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

    private fun setupViewPager(viewPager: ViewPager) {
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter?.addFragment(RadiologyFavouriteFragment(), "  Favourite")
        viewPagerAdapter?.addFragment(RadiologyTempleteFragment(), "  Template")
        viewPagerAdapter?.addFragment(PrevRadiologyFragment(), "Prev.Radiology")
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

    private fun setTabAdapter() {
        radiologyAdapter =
            RadiologyAdapter(
                requireActivity(),
                ArrayList(),
                ArrayList()
            ) { favortem, position, selected ->
                if (favortem == 1) {
                    mCallbackRadiologyFavFragment?.updateSelectStatus(favortem, position, selected)
                } else {
                    mCallbackRadiologyTemplateFragment?.updatestaus(favortem, position, selected)
                }
            }
        binding?.savelabRecyclerView?.adapter = radiologyAdapter
        radiologyAdapter?.addRow(FavouritesModel())
        radiologyAdapter?.addTempleteRow(TempDetails())

        radiologyAdapter?.setOnDeleteClickListener(object :
            RadiologyAdapter.OnDeleteClickListener {
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


                        if (responseContent!!.treatment_kit_type_id == 1) {

                            deleteList.add(responseContent.test_master_id!!)

                        }
                        val check = radiologyAdapter?.deleteRow(position)

                        if (responseContent.viewRadiologystatus == 1) {
                            radiologyMobileAdapter?.getIdList()

                            /*  mCallbackRadiologyFavFragment?.ClearFavParticularPosition(
                                  responseContent.isFavposition
                              )*/

                        } else if (responseContent.viewRadiologystatus == 2) {

                            if (check!!) {
                                radiologyMobileAdapter?.getIdList()


                                /* mCallbackRadiologyTemplateFragment?.ClearTemplateParticularPosition(
                                     responseContent.isTemposition
                                 )*/

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
            }
        })


        radiologyAdapter?.setOnListItemClickListener(object :
            RadiologyAdapter.OnListItemClickListener {
            override fun onListItemClick(
                responseContent: FavouritesModel?,
                position: Int,
                spinnerToLocation: Spinner
            ) {
                SpinnerToLocation = spinnerToLocation
                searchPosition = position

                viewModel?.getToLocation(
                    getRadiologyToLocationRetrofitCallback,
                    facility_id,
                    department_UUID,
                    responseContent!!.test_master_id
                )

            }
        })

        radiologyAdapter?.setOnSearchInitiatedListener(object :
            RadiologyAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int,
                spinnerToLocation: Spinner
            ) {
                dropdownReferenceView = view
                SpinnerToLocation = spinnerToLocation
                searchPosition = position
                viewModel?.getRadioSearchResult(
                    facility_id,
                    query,
                    getRadiologySearchRetrofitCallBack
                )
            }
        })

        radiologyAdapter?.setOnCommandClickListener(object :
            RadiologyAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String) {

                //     val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = RadiologyCommentDialogFragment()

                val ft = childFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putString("commands", Command)

                dialog.arguments = bundle
                dialog.show(ft, "Tag")


            }
        })
    }

    private fun setMobileAdapter() {
        initMobileSpinnerAdapter()
        initMobileClickListiner()
        initAddView()
        radiologyMobileAdapter =
            RadiologyMobileAdapter(
                requireActivity(),
                arrayItemData
            ) { favortem, position, selected ->

            }
        binding?.rvItemView?.adapter = radiologyMobileAdapter

        radiologyMobileAdapter?.onItemClick(object :
            RadiologyMobileAdapter.OnSelectItemCommunication {
            override fun onClick(
                position: Int,
                selectedItem: Boolean,
                favouritesModel: FavouritesModel?
            ) {
                radiologyMobileAdapter?.updateSelectStatus(position, selectedItem)
                if (!selectedItem) {
                    modifiyPosition = position
                    dataToBeAdded = FavouritesModel()
                    dataToBeAdded = favouritesModel!!
                    dataToBeAdded?.isEditableMode = true
                    selectedRadiologyName = dataToBeAdded?.test_master_name
                    selectedRadiologyCode = dataToBeAdded?.test_master_code
                    selectedRadiologyUUID = dataToBeAdded?.test_master_id
                    selectedTypeUuid = dataToBeAdded?.selectTypeUUID
                    selectedTypeName = dataToBeAdded?.selectTypeName
                    selectedLocationUuid = dataToBeAdded?.selectToLocationUUID
                    selectedLocationName = dataToBeAdded?.selectedLocationName
                    binding?.actvRadiologyTestName?.setText(dataToBeAdded?.test_master_name)
                    binding?.actvRadiologyCode?.setText(dataToBeAdded?.test_master_code)
                    dataToBeAdded?.selectToLocationUUID?.let { setMobileOrderToLocation(it) }
                    dataToBeAdded?.selectTypeUUID?.let { setMobileOrderType(it) }
                    showDropDown()
                } else {
                    clearAddAll()
                }
            }

        })

        radiologyMobileAdapter?.setOnCommandClickListener(object :
            RadiologyMobileAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String) {
                val dialog = RadiologyCommentDialogFragment()

                val ft = childFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putString("commands", Command)

                dialog.arguments = bundle
                dialog.show(ft, "Tag")

            }
        })

        radiologyMobileAdapter?.setOnDeleteClickListener(object :
            RadiologyMobileAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {

                if (responseContent!!.treatment_kit_type_id == 1) {

                    deleteList.add(responseContent.test_master_id!!)

                }
                val check = radiologyMobileAdapter?.deleteRow(position)

                if (responseContent.viewRadiologystatus == 1) {
                    mCallbackRadiologyFavFragment?.ClearFavParticularPosition(
                        responseContent.isFavposition
                    )

                } else if (responseContent.viewRadiologystatus == 2) {

                    if (check!!) {

                        mCallbackRadiologyTemplateFragment?.ClearTemplateParticularPosition(
                            responseContent.isTemposition
                        )

                    }
                    //template_id
                }
            }
        })
        radiologyMobileAdapter?.setOnitemChangeListener(object :
            RadiologyMobileAdapter.OnitemChangeListener {
            override fun onitemChangeClick(uuid: ArrayList<Int>) {

                mCallbackRadiologyFavFragment?.checkanduncheck(uuid, true)
                mCallbackRadiologyTemplateFragment?.checkanduncheck(uuid)

            }
        })
    }

    private fun initMobileClickListiner() {
        binding?.actvRadiologyTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (binding?.actvRadiologyTestName?.hasFocus()!!) {
                    if (s.length > 2) {
                        isCodeSearch = false
                        viewModel?.getRadioSearchResult(
                            facility_id,
                            s.toString(),
                            getRadiologySearchRetrofitCallBack
                        )
                    }

                }
            }
        })
        binding?.actvRadiologyCode?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    isCodeSearch = true
                    viewModel?.getRadioSearchResult(
                        facility_id,
                        s.toString(),
                        getRadiologySearchRetrofitCallBack
                    )
                }
            }
        })
        binding?.spinnerRadiologyOrderLocation?.onItemSelectedListener =
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
                    selectedLocationUuid =
                        loationListMutableMap.filterValues { it == itemValue }.keys.toList()[0]
                    selectedLocationName = itemValue
                }

            }
        binding?.spinnerRadiologyPriority?.onItemSelectedListener =
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
                    selectedTypeUuid =
                        typeListMutableMap.filterValues { it == itemValue }.keys.toList()[0]
                    selectedTypeName = itemValue

                }

            }

    }

    private fun initMobileSpinnerAdapter() {
        mobileTypeSpinnerAdapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                typeListMutableMap.values.toMutableList()
            )
        mobileTypeSpinnerAdapter?.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerRadiologyPriority?.adapter = mobileTypeSpinnerAdapter

        mobileLocationSpinnerAdapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                loationListMutableMap.values.toMutableList()
            )
        mobileLocationSpinnerAdapter?.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerRadiologyOrderLocation?.adapter = mobileLocationSpinnerAdapter

        viewModel?.getRadiologyType(getRadiologyTypeRetrofitCallback, facility_id)
        viewModel?.getToLocationList(
            getRadiologyToLocationListRetrofitCallback,
            facility_id,
            department_UUID,
            Str_auto_id
        )
    }

    fun updateTypeSpinnerAdapter() {
        mobileTypeSpinnerAdapter?.clear()
        mobileTypeSpinnerAdapter?.addAll(typeListMutableMap.values.toMutableList())
        mobileTypeSpinnerAdapter?.notifyDataSetChanged()
        binding?.spinnerRadiologyPriority?.setSelection(0)
    }

    fun updateLocationSpinnerAdapter() {
        mobileLocationSpinnerAdapter?.clear()
        mobileLocationSpinnerAdapter?.addAll(loationListMutableMap.values.toMutableList())
        mobileLocationSpinnerAdapter?.notifyDataSetChanged()
        binding?.spinnerRadiologyOrderLocation?.setSelection(0)
    }

    fun setMobileOrderType(uuid: Int) {
        val check = this.typeListMutableMap.any { it.key == uuid }
        if (check) {
            typeUuidList[uuid]?.let { binding?.spinnerRadiologyPriority?.setSelection(it) }
        } else {
            binding?.spinnerRadiologyPriority?.setSelection(0)
        }
    }


    fun setMobileOrderToLocation(uuid: Int) {
        val check =
            this.loationListMutableMap.any { it.key == uuid }
        if (check) {
            locationUuisList[uuid]?.let { binding?.spinnerRadiologyOrderLocation?.setSelection(it) }
        } else {
            binding?.spinnerRadiologyOrderLocation?.setSelection(0)
        }
    }

    fun initAddView() {
        binding?.cvRadiologyAdd?.setOnClickListener {
            if (binding?.actvRadiologyCode?.text?.isEmpty()!! || binding?.actvRadiologyTestName?.text?.isEmpty()!!) {
                showToast(getString(R.string.error_test_name))
            } else if (selectedTypeUuid == 0) {
                showToast(getString(R.string.error_priority))
            } else if (selectedLocationUuid == 0) {
                showToast(getString(R.string.error_location))
            } else {
                dataToBeAdded?.selectTypeName = selectedTypeName
                dataToBeAdded?.selectTypeUUID = selectedTypeUuid!!
                dataToBeAdded?.selectToLocationUUID = selectedLocationUuid!!
                dataToBeAdded?.selectedLocationName = selectedLocationName!!
                dataToBeAdded?.test_master_code = selectedRadiologyCode
                dataToBeAdded?.test_master_name = selectedRadiologyName
                dataToBeAdded?.test_master_id = selectedRadiologyUUID
                dataToBeAdded?.itemAppendString = selectedRadiologyName
                if (dataToBeAdded?.isEditableMode!!) {
                    dataToBeAdded?.isEditableMode = false
                    dataToBeAdded?.labDataSelected = false
                    arrayItemData?.set(modifiyPosition, dataToBeAdded)
                    radiologyMobileAdapter?.updateAdapter(arrayItemData)
                    clearAddAll()
                    hideDropDown()
                } else {
                    val check =
                        arrayItemData?.any { it!!.test_master_id == dataToBeAdded?.test_master_id }
                    if (!check!!) {
                        arrayItemData?.add(dataToBeAdded)
                        radiologyMobileAdapter?.updateAdapter(arrayItemData)
                        clearAddAll()
                        hideDropDown()
                    } else {
                        showToast(getString(R.string.error_item_exsist))
                    }
                }

            }

        }
        binding?.cvRadiologyClear?.setOnClickListener {
            clearAddAll()
        }
    }

    fun clearAddAll() {
        selectedLocationName = ""
        selectedTypeName = ""
        selectedTypeUuid = 0
        selectedLocationUuid = 0
        selectedRadiologyUUID = 0
        selectedRadiologyName = ""
        selectedRadiologyUUID = 0
        binding?.actvRadiologyTestName?.setText("")
        binding?.actvRadiologyCode?.setText("")
        binding?.spinnerRadiologyOrderLocation?.setSelection(0)
        binding?.spinnerRadiologyPriority?.setSelection(0)
        dataToBeAdded = FavouritesModel()
    }

    fun initSearchSpinner(
        isfromCode: Boolean,
        responseContents: ArrayList<FavSearch>
    ) {
        if (isfromCode) {
            val responseContentAdapter = LabSearchResultAdapter(
                requireContext(),
                R.layout.row_chief_complaint_search_result,
                responseContents,
                false
            )
            binding?.actvRadiologyCode?.threshold = 1
            binding?.actvRadiologyCode?.setAdapter(responseContentAdapter)
            binding?.actvRadiologyCode?.setOnItemClickListener { parent, _, pos, id ->
                val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
                binding?.actvRadiologyCode?.setText(selectedPoi?.code)
                binding?.actvRadiologyTestName?.setText(selectedPoi?.name)
                selectedRadiologyCode = selectedPoi?.code
                selectedRadiologyName = selectedPoi?.name
                selectedRadiologyUUID = selectedPoi?.uuid
                setMobileOrderType(1)
                viewModel?.getToLocation(
                    getRadiologyToLocationRetrofitCallback,
                    facility_id,
                    department_UUID,
                    selectedPoi?.uuid
                )
            }

        } else {
            val responseContentAdapter = LabSearchResultAdapter(
                requireContext(),
                R.layout.row_chief_complaint_search_result,
                responseContents,
                true
            )
            binding?.actvRadiologyTestName?.threshold = 1
            binding?.actvRadiologyTestName?.setAdapter(responseContentAdapter)
            binding?.actvRadiologyTestName?.showDropDown()
            binding?.actvRadiologyTestName?.setOnItemClickListener { parent, _, pos, id ->
                val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
                binding?.actvRadiologyTestName?.clearFocus()
                binding?.actvRadiologyTestName?.setText(selectedPoi?.name)
                binding?.actvRadiologyCode?.setText(selectedPoi?.code)
                selectedRadiologyCode = selectedPoi?.code
                selectedRadiologyName = selectedPoi?.name
                selectedRadiologyUUID = selectedPoi?.uuid
                setMobileOrderType(1)
                viewModel?.getToLocation(
                    getRadiologyToLocationRetrofitCallback,
                    facility_id,
                    department_UUID,
                    selectedPoi?.uuid
                )
            }

        }

    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is RadiologyFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)

        }
        if (childFragment is RadiologyTempleteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is PrevRadiologyFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ManageRadiologySaveTemplateFragment) {
            childFragment.setOnClickedListener(this)
            childFragment.setTempRefresh(this)
        }
        if (childFragment is RadiologyCommentDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }

        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackRadiologyFavFragment = childFragment
        }
        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackRadiologyTemplateFragment =
                childFragment
        }
    }

    override fun sendFavAddInLab(favRadData: RadiologyFavData?, position: Int, selected: Boolean) {
        favRadData?.viewRadiologystatus = 1
        favRadData?.isFavposition = position
        if (!selected) {
            if (isTablet(requireContext())) {
                //  radiologyAdapter!!.addFavouritesInRowModule(1, favmodel, position, selected)
            } else {
                if (radiologyMobileAdapter?.checkAlreadyPresent(favRadData?.test_master_id)!!) {
                    showToast(getString(R.string.error_item_exsist))
                } else {
                    var data = FavouritesModel()
                    data.viewRadiologystatus = favRadData?.viewRadiologystatus!!
                    data.isFavposition = favRadData.isFavposition
                    data.selectTypeUUID = 1
                    data.selectTypeName = typeListMutableMap.get(1)
                    data.selectedLocationName = "Select Location"
                    data.selectToLocationUUID = 0
                    data.test_master_name = favRadData.test_master_name
                    data.test_master_id = favRadData.test_master_id
                    data.test_master_code = favRadData.test_master_code
                    viewModel?.getToLocation(
                        getLocationFavRetrofitCallback,
                        facility_id, department_UUID, favRadData.test_master_id
                    )
                    radiologyMobileAdapter?.addFavouritesInRowModule(1, data, position, selected)

                }

            }
        } else {
            if (isTablet(requireContext())) {
                radiologyAdapter!!.deleteRowFromTemplate(favRadData?.test_master_id, position)
            } else {
                radiologyMobileAdapter!!.deleteRowFromTemplate(favRadData?.test_master_id, position)
            }
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
                favmodel?.viewRadiologystatus = 2
                favmodel?.isTemposition = position
                favmodel?.test_master_name = templeteDetails[i]!!.lab_name
                favmodel?.test_master_id = templeteDetails[i]!!.lab_test_uuid!!
                favmodel?.test_master_code = templeteDetails[i]!!.lab_code
                favmodel?.template_id = id
                if (isTablet(requireContext())) {
                    radiologyAdapter!!.addFavouritesInRowModule(2, favmodel, position, selected)
                } else {
                    favmodel?.selectTypeUUID = 1
                    favmodel?.selectTypeName = typeListMutableMap.get(1)
                    favmodel?.selectedLocationName = "Select Location"
                    favmodel?.selectToLocationUUID = 0
                    viewModel?.getToLocation(
                        getLocationFavRetrofitCallback,
                        facility_id, department_UUID, favmodel?.test_master_id
                    )
                    radiologyMobileAdapter?.addFavouritesInRowModule(
                        2,
                        favmodel,
                        position,
                        selected
                    )
                }

            }
        } else {
            for (i in templeteDetails!!.indices) {

                if (isTablet(requireContext())) {
                    radiologyAdapter!!.deleteRowFromTemplate(
                        templeteDetails[i]!!.lab_test_uuid,
                        2
                    )
                } else {
                    radiologyMobileAdapter!!.deleteRowFromTemplate(
                        templeteDetails[i]!!.lab_test_uuid,
                        2
                    )
                }

            }


        }
    }

    override fun sendPrevtoChild(responseContent: List<PodArrResult>?, isModifiy: Boolean) {
        arrayItemData?.clear()
        prevOrder = true
        this.isModifiy = isModifiy
        modifiedData = responseContent as ArrayList<PodArrResult>
        for (i in responseContent.indices) {
            val favmodel: FavouritesModel? = FavouritesModel()
            favmodel!!.test_master_name = responseContent[i].name
            favmodel.itemAppendString = responseContent[i].name
            favmodel.test_master_id = responseContent[i].test_master_uuid
            favmodel.test_master_code = responseContent[i].code
            favmodel.selectTypeUUID = responseContent[i].order_priority_uuid
            favmodel.selectToLocationUUID = responseContent[i].order_to_location_uuid
            favmodel.selectedLocationName = responseContent[i].order_to_location
            favmodel.selectTypeName = responseContent[i].order_priority_name
            favmodel.treatment_kit_type_id = 1
            if (isModifiy) {
                favmodel.patient_order_details_uuid =
                    responseContent[i].patient_order_details_uuid
                favmodel.patient_order_uuid = responseContent[i].patient_order_uuid
                favmodel.isModifiy = true
                patientOrderUUid = responseContent[i].patient_order_uuid
            } else {
                favmodel.isModifiy = false
            }
            if (isTablet(requireContext())) {
                radiologyAdapter!!.addFavouritesInRow(favmodel)
            } else {
                radiologyMobileAdapter?.addPrevModifyInRow(favmodel)
            }
        }

    }

    override fun sendDataChiefComplaint() {
        mCallbackLabTemplateFragment?.GetTemplateDetails()
    }

    override fun sendCommandPosData(position: Int, command: String) {
        if (isTablet(requireContext())) {
            radiologyAdapter!!.addCommands(position, command)
        } else {
            radiologyMobileAdapter!!.addCommands(position, command)

        }
    }

    private fun saveRadiologyForTab() {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val datasize: Int = arrayItemData?.size!!
        if (datasize > 1) {
            if (!isModifiy) {
                for (i in 0..(datasize - 2)) {
                    val details: Detail = Detail()
                    details.encounter_type_uuid = encounterType.toInt()
                    details.group_uuid = 0
                    details.is_profile = false
                    details.lab_master_type_uuid = AppConstants.RADIOLOGY_MASTER_ID
                    details.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID!!
                    details.profile_uuid = ""
                    details.test_master_uuid = arrayItemData?.get(i)?.test_master_id!!.toString()

                    details.to_department_uuid = department_UUID!!.toInt()
                    details.to_location_uuid =
                        arrayItemData?.get(i)?.selectToLocationUUID.toString()
                    details.to_sub_department_uuid = 0
                    details.diet_frequency_uuid = 0
                    details.diet_master_uuid = 0
                    details.diet_order_category_uuid = 0
                    details.quanty = 0
                    detailsList.add(details)
                }

                header?.consultation_uuid = 0
                header?.department_uuid = department_UUID!!.toString()
                header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                header?.encounter_type_uuid = encounterType.toInt()
                header?.encounter_uuid = encounter_uuid.toInt()
                header?.encounter_doctor_uuid = doctor_en_uuid.toInt()
                header?.facility_uuid = facility_id.toString()
                header?.lab_master_type_uuid = AppConstants.RADIOLOGY_MASTER_ID
                header?.order_status_uuid = 0
                header?.order_to_location_uuid = 1
                header?.patient_treatment_uuid = 0
                header?.patient_uuid = patientUuid.toString()
                header?.sub_department_uuid = 0
                header?.treatment_plan_uuid = 0
                header?.encounter_doctor_uuid = 0
                header?.is_active = true
                header?.order_date = ""
                header?.status = true

                emrRequestModel?.header = this.header!!
                emrRequestModel?.details = this.detailsList

                val requestmodel = Gson().toJson(emrRequestModel)


                Log.i("", "" + requestmodel)

                viewModel?.RadiologyInsert(
                    facility_id,
                    emrRequestModel!!,
                    emrPostRadiologyRetrofitCallback
                )
            } else {


                var existing_details: ArrayList<ExistingDetail> = ArrayList()
                var new_details: ArrayList<NewDetail> = ArrayList()
                val removed_details: ArrayList<RemovedDetail> = ArrayList()

                for (i in 0..(datasize - 2)) {

                    if (arrayItemData!![i]?.isModifiy!!) {


                        val existingDetail: ExistingDetail = ExistingDetail()

                        existingDetail.uuid =
                            arrayItemData!![i]!!.patient_order_details_uuid.toString()
                        existingDetail.patient_order_uuid = arrayItemData!![i]!!.patient_order_uuid
                        existingDetail.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID!!
                        existingDetail.to_location_uuid =
                            arrayItemData?.get(i)?.selectToLocationUUID!!
                        existingDetail.details_comments = arrayItemData?.get(i)?.commands.toString()
                        existingDetail.test_master_uuid = arrayItemData?.get(i)?.test_master_id!!

//                           existingDetail.profile_uuid=""

                        existing_details.add(existingDetail)
                    } else {

                        val newDetail: NewDetail = NewDetail()
                        newDetail.encounter_type_uuid = encounterType.toString()
                        newDetail.group_uuid = 0
                        newDetail.is_profile = false
                        newDetail.lab_master_type_uuid = AppConstants.RADIOLOGY_MASTER_ID
                        newDetail.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID!!
                        newDetail.profile_uuid = ""
                        newDetail.test_master_uuid = arrayItemData?.get(i)?.test_master_id!!

                        newDetail.to_department_uuid = department_UUID!!.toInt()
                        newDetail.to_location_uuid =
                            arrayItemData?.get(i)?.selectToLocationUUID.toString()
                        newDetail.to_sub_department_uuid = 0

                        new_details.add(newDetail)

                    }


                }



                for (i in deleteList.indices) {


                    val removedDetail: RemovedDetail = RemovedDetail()

                    removedDetail.uuid = deleteList[i]

                    removed_details.add(removedDetail)

                }
                val updateRequest: RadiologyRequestModel = RadiologyRequestModel()



                updateRequest.existing_details = existing_details

                updateRequest.new_details = new_details
                updateRequest.removed_details = removed_details


                viewModel!!.rmisUpdate(facility_id, updateRequest, updateRadiologyRetrofitCallback)


            }

        } else {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select any one item"
            )
        }
    }

    private fun saveRadiologyForMobile() {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val datasize: Int = arrayItemData?.size!!
        if (datasize > 0) {
            if (!isModifiy) {
                for (i in 0..(datasize - 1)) {
                    val details: Detail = Detail()
                    details.encounter_type_uuid = encounterType.toInt()
                    details.group_uuid = 0
                    details.is_profile = false
                    details.lab_master_type_uuid = AppConstants.RADIOLOGY_MASTER_ID
                    details.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID!!
                    details.profile_uuid = ""
                    details.test_master_uuid = arrayItemData?.get(i)?.test_master_id!!.toString()

                    details.to_department_uuid = department_UUID!!.toInt()
                    details.to_location_uuid =
                        arrayItemData?.get(i)?.selectToLocationUUID.toString()
                    details.to_sub_department_uuid = 0
                    details.diet_frequency_uuid = 0
                    details.diet_master_uuid = 0
                    details.diet_order_category_uuid = 0
                    details.quanty = 0
                    detailsList.add(details)
                }

                header?.consultation_uuid = 0
                header?.department_uuid = department_UUID!!.toString()
                header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                header?.encounter_type_uuid = encounterType.toInt()
                header?.encounter_uuid = encounter_uuid.toInt()
                header?.encounter_doctor_uuid = doctor_en_uuid.toInt()
                header?.facility_uuid = facility_id.toString()
                header?.lab_master_type_uuid = AppConstants.RADIOLOGY_MASTER_ID
                header?.order_status_uuid = 0
                header?.order_to_location_uuid = 1
                header?.patient_treatment_uuid = 0
                header?.patient_uuid = patientUuid.toString()
                header?.sub_department_uuid = 0
                header?.treatment_plan_uuid = 0
                header?.encounter_doctor_uuid = 0
                header?.is_active = true
                header?.order_date = ""
                header?.status = true

                emrRequestModel?.header = this.header!!
                emrRequestModel?.details = this.detailsList

                val requestmodel = Gson().toJson(emrRequestModel)


                Log.i("", "" + requestmodel)

                viewModel?.RadiologyInsert(
                    facility_id,
                    emrRequestModel!!,
                    emrPostRadiologyRetrofitCallback
                )
            } else {


                var existing_details: ArrayList<ExistingDetail> = ArrayList()
                var new_details: ArrayList<NewDetail> = ArrayList()
                val removed_details: ArrayList<RemovedDetail> = ArrayList()

                for (i in 0..(datasize - 1)) {

                    if (arrayItemData!![i]?.isModifiy!!) {


                        val existingDetail: ExistingDetail = ExistingDetail()

                        existingDetail.uuid =
                            arrayItemData!![i]!!.patient_order_details_uuid.toString()
                        existingDetail.patient_order_uuid = arrayItemData!![i]!!.patient_order_uuid
                        existingDetail.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID!!
                        existingDetail.to_location_uuid =
                            arrayItemData?.get(i)?.selectToLocationUUID!!
                        existingDetail.details_comments = arrayItemData?.get(i)?.commands.toString()
                        existingDetail.test_master_uuid = arrayItemData?.get(i)?.test_master_id!!

//                           existingDetail.profile_uuid=""

                        existing_details.add(existingDetail)
                    } else {

                        val newDetail: NewDetail = NewDetail()
                        newDetail.details_comments = ""
                        newDetail.doctor_uuid = doctor_uuid.toString()
                        newDetail.encounter_uuid = encounter_uuid.toString()
                        newDetail.encounter_type_uuid = encounterType.toString()
                        newDetail.group_uuid = 0
                        newDetail.is_ordered = true
                        newDetail.is_profile = false
                        newDetail.lab_master_type_uuid = AppConstants.RADIOLOGY_MASTER_ID
                        newDetail.order_priority_uuid = arrayItemData?.get(i)?.selectTypeUUID!!
                        newDetail.order_request_date = ""
                        newDetail.order_status_uuid = 1
                        newDetail.patient_order_uuid = this.patientOrderUUid!!
                        newDetail.patient_uuid = patientUuid.toString()
                        newDetail.patient_work_order_by = 1
                        newDetail.test_master_uuid = arrayItemData?.get(i)?.test_master_id!!

                        newDetail.to_department_uuid = department_UUID!!.toInt()
                        newDetail.to_location_uuid =
                            arrayItemData?.get(i)?.selectToLocationUUID.toString()
                        newDetail.to_sub_department_uuid = 0

                        new_details.add(newDetail)

                    }


                }

                var removedListFromOriginal: ArrayList<FavouritesModel?>? =
                    radiologyMobileAdapter?.removedListFromOriginal


                for (i in removedListFromOriginal?.indices!!) {

                    val removedDetail: RemovedDetail = RemovedDetail()

                    removedDetail.uuid = removedListFromOriginal.get(i)?.patient_order_details_uuid
                    removedDetail.patient_orders_uuid =
                        removedListFromOriginal.get(i)?.patient_order_uuid
                    removedDetail.test_master_uuid = removedListFromOriginal.get(i)?.test_master_id

                    removed_details.add(removedDetail)

                }
                val updateRequest: RadiologyRequestModel = RadiologyRequestModel()



                updateRequest.existing_details = existing_details

                updateRequest.new_details = new_details
                updateRequest.removed_details = removed_details


                viewModel!!.rmisUpdate(facility_id, updateRequest, updateRadiologyRetrofitCallback)


            }

        } else {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select any one item"
            )
        }
    }

    private val getRadiologySearchRetrofitCallBack = object : RetrofitCallback<FavSearchResponce> {
        override fun onSuccessfulResponse(response: Response<FavSearchResponce>) {
            responseContents = Gson().toJson(response.body()?.responseContents)
            if (response.body()?.responseContents?.isNotEmpty()!!) {
                if (isTablet(requireContext())) {
                    radiologyAdapter?.setAdapter(
                        dropdownReferenceView,
                        response.body()?.responseContents!!, searchPosition, SpinnerToLocation
                    )
                } else {
                    isCodeSearch?.let {
                        initSearchSpinner(
                            it,
                            response.body()?.responseContents!!
                        )
                    }
                }
            }


        }

        override fun onBadRequest(response: Response<FavSearchResponce>) {

        }

        override fun onServerError(response: Response<*>) {
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.frameLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
    private val emrPostRadiologyRetrofitCallback = object : RetrofitCallback<EmrResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<EmrResponceModel>?) {
            Log.i("radioPost", "" + responseBody?.body()?.message)
            mCallbackRadiologyFavFragment?.ClearAllData()
            utils?.showToast(
                R.color.positiveToast,
                binding?.frameLayout!!,
                "Radiology saved successfully"
            )
            deleteList.clear()
            if (isTablet(requireContext())) {
                radiologyAdapter?.clearall()
                radiologyAdapter?.addRow(FavouritesModel())
                radiologyAdapter?.addTempleteRow(TempDetails())
            } else {
                radiologyMobileAdapter?.clearall()
                clearAddAll()
            }
            mCallbackRadiologyTemplateFragment?.ClearAllData()
            mCallbackRadiologyFavFragment?.ClearAllData()
            (viewPagerAdapter?.getCurrentFragment(2) as PrevRadiologyFragment).refreshList(
                patientUuid,
                facility_id
            )


            //setAdapter()
        }

        override fun onBadRequest(response: Response<EmrResponceModel>?) {

        }

        override fun onServerError(response: Response<*>) {

            Log.i("ServerErr", response.message())

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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.frameLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }
    private val updateRadiologyRetrofitCallback =
        object : RetrofitCallback<RadiologyUpdateResponse> {
            override fun onSuccessfulResponse(responseBody: Response<RadiologyUpdateResponse>?) {
                Log.i("radioPost", "" + responseBody?.body()?.message)
                mCallbackRadiologyFavFragment?.ClearAllData()
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.frameLayout!!,
                    responseBody?.body()?.message!!
                )
                deleteList.clear()
                if (isTablet(requireContext())) {
                    radiologyAdapter?.clearall()
                    radiologyAdapter?.addRow(FavouritesModel())
                    radiologyAdapter?.addTempleteRow(TempDetails())
                } else {
                    radiologyMobileAdapter?.clearall()
                    clearAddAll()
                }
                mCallbackRadiologyTemplateFragment?.ClearAllData()
                mCallbackRadiologyFavFragment?.ClearAllData()
                (viewPagerAdapter?.getCurrentFragment(2) as PrevRadiologyFragment).refreshList(
                    patientUuid,
                    facility_id
                )

                //setAdapter()
            }

            override fun onBadRequest(response: Response<RadiologyUpdateResponse>?) {

            }

            override fun onServerError(response: Response<*>) {
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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.frameLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }


        }
    private val createEncounterRetrofitCallback =
        object : RetrofitCallback<CreateEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {
                doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
                encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
                patientUuid =
                    response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()
                if (isTablet(requireContext())) {
                    saveRadiologyForTab()
                } else {
                    saveRadiologyForMobile()
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
                        saveRadiologyForTab()
                    } else {
                        saveRadiologyForMobile()
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
    private val getRadiologyTypeRetrofitCallback =
        object : RetrofitCallback<RadiologyTypeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<RadiologyTypeResponseModel>?) {
                if (!responseBody?.body()?.responseContents?.isNullOrEmpty()!!) {
                    if (isTablet(requireContext())) {
                        radiologyAdapter?.setadapterTypeValue(responseBody.body()?.responseContents!!)
                    } else {
                        val data = RadiologyTypeResponseContent()
                        data.uuid = 0
                        data.name = getString(R.string.select_Type)
                        typeList?.add(data)
                        typeList?.addAll(responseBody.body()?.responseContents!!)
                        typeListMutableMap =
                            typeList?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()
                        for (i in typeList?.indices!!) {
                            typeList?.get(i)?.uuid?.let { typeUuidList.put(it, i) }
                        }
                        updateTypeSpinnerAdapter()
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<RadiologyTypeResponseModel>?) {
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
    private val getRadiologyToLocationListRetrofitCallback =
        object : RetrofitCallback<LabToLocationResponse> {
            override fun onSuccessfulResponse(responseBody: Response<LabToLocationResponse>?) {
                if (!responseBody?.body()?.responseContents?.isNullOrEmpty()!!) {
                    if (isTablet(requireContext())) {
                        radiologyAdapter?.setToLocationList(responseBody.body()?.responseContents)
                    } else {
                        val data = LabToLocationResponse.LabToLocationContent()
                        data.uuid = 0
                        data.location_name = getString(R.string.select_location)
                        locationList?.add(data)
                        locationList?.addAll(responseBody.body()?.responseContents!!)
                        loationListMutableMap =
                            locationList?.map { it?.uuid!! to it.location_name!! }!!.toMap()
                                .toMutableMap()
                        for (i in locationList?.indices!!) {
                            locationList?.get(i)?.uuid?.let { locationUuisList.put(it, i) }
                        }
                        updateLocationSpinnerAdapter()
                    }
                }
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
            }

        }
    private val getRadiologyToLocationRetrofitCallback =
        object : RetrofitCallback<GetToLocationTestResponse> {
            override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {
                if (isTablet(requireContext())) {
                    radiologyAdapter?.setToLocation(
                        responseBody?.body()?.responseContents,
                        SpinnerToLocation,
                        searchPosition
                    )
                } else {
                    responseBody?.body()?.responseContents?.to_location_uuid?.let {
                        setMobileOrderToLocation(it)
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
    private val getLocationFavRetrofitCallback =
        object : RetrofitCallback<GetToLocationTestResponse> {
            override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {
                radiologyMobileAdapter?.setToLocation(responseBody?.body()?.responseContents)
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

    fun showDropDown() {
        slideDown(requireContext(), binding?.llAddRadiologyDetail!!)
        binding?.llAddRadiologyDetail?.show()
        binding?.imgDownRadiology?.rotation = 270F
    }

    fun hideDropDown() {
        slideDown(requireContext(), binding?.llAddRadiologyDetail!!)
        binding?.imgDownRadiology?.rotation = 90F
        binding?.llAddRadiologyDetail?.hide()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    override fun onRefresh() {
        (viewPagerAdapter?.getCurrentFragment(1) as RadiologyTempleteFragment).refresh()
    }

}


