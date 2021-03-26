package com.hmis_tn.doctor.ui.quick_reg.view

import androidx.fragment.app.Fragment

class QuickRegistrationActivity :
    Fragment()/*, SearchPatientDialogFragment.OnOrderProcessListener */ {

//    private var sharepreferlastPin: String? = ""
//    private var searchresponseData: ArrayList<QuickSearchresponseContent?> = ArrayList()
//
//    //    private var roleUUID: Int?  =0
//    private var array_testmethod: List<ResponseTestMethodContent?>? = ArrayList()
//    private var array_testmethod1: List<ResponseTestMethodContent?>? = ArrayList()
//    var NasopharyngealID1: Int? = 0
//    var rtpcrID: Int? = 0
//    private var currentPage = 0
//    private var pageSize = 10
//    private var customProgressDialog: CustomProgressDialog? = null
//    var rtpcrID1: Int? = 0
//    var NasopharyngealID: Int? = 0
//
//    private var uhid: String = ""
//    var binding: ActivityQuickRegistrationBinding? = null
//    var utils: Utils? = null
//    private var viewModel: QuickRegistrationViewModel? = null
//
//    private var registerDate: String = ""
//    var appPreferences: AppPreferences? = null
//    private var autocompleteNameTestResponse: List<CovidRegistrationSearchResponseContent>? = null
//
//    private var customdialog: Dialog? = null
//
//    private var TOTAL_PAGES: Int = 0
//    private var CovidGenderList = mutableMapOf<Int, String>()
//    private var CovidPeriodList = mutableMapOf<Int, String>()
//
//    private var typeNamesList = mutableMapOf<Int, String>()
//
//    private var facility_id: Int? = 0
//    private var departmentUUId: Int? = 0
//    private var patientUUId: Int? = 0
//
//    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
//    private var updateId: Int? = 0
//
//    private var selectPeriodUuid: Int? = 0
//    private var selectGenderUuid: Int? = 0
//
//    private var radioid: Int? = 0
//
//    private var locationId: Int? = 0
//
//    private val hashPeriodSpinnerList: HashMap<Int, Int> = HashMap()
//    private val hashGenderSpinnerList: HashMap<Int, Int> = HashMap()
//    private val hashNationalitySpinnerList: HashMap<Int, Int> = HashMap()
//    private val hashStateSpinnerList: HashMap<Int, Int> = HashMap()
//    private val hashDistrictSpinnerList: HashMap<Int, Int> = HashMap()
//    private val hashBlockSpinnerList: HashMap<Int, Int> = HashMap()
//
//    var selectNationalityUuid: Int = 0
//
//    var selectStateUuid: Int = 0
//
//    var selectDistictUuid: Int = 0
//
//    var selectBelongUuid: Int = 0
//
//    var locationMapId: Int = 0
//
//    var gettest: GetTestMasterList = GetTestMasterList()
//
//    var getReference: GetReference = GetReference()
//
//    var encounter_doctor_id: Int? = 0
//
//    var encounter_id: Int? = 0
//
//
//    private var sariStatus: Boolean = false
//
//    private var iliStatus: Boolean = false
//
//    private var nosymptomsStatus: Boolean = false
//
//    private var quickRegistrationSaveResponseModel = QuickRegistrationRequestModel()
//
//
//    private var CovidNationalityList = mutableMapOf<Int, String>()
//
//    private var CovidStateList = mutableMapOf<Int, String>()
//
//    private var CovidDistictList = mutableMapOf<Int, String>()
//
//    private var CovidBlockList = mutableMapOf<Int, String>()
//
//    private var ispublic: Boolean? = null
//
//    private var sampleid: Int? = null
//
//    private var selectLabNameID: Int? = 0
//
//    private var alreadyExists: Boolean = false
//
//    private var adultFromAge: Int = 14
//
//    private var childToAge: Int = 14
//
//    private var isadult: Int = 0
//    private var adultToAge: Int = 100
//
//    private var facility_Name: String = ""
//
//
//    //private var customProgressDialog: CustomProgressDialog? = null
//    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding =
//            DataBindingUtil.inflate(
//                inflater,
//                R.layout.activity_quick_registration,
//                container,
//                false
//            )
//
//        viewModel = QuickRegistrationViewModelFactory(
//            requireActivity().application
//        )
//            .create(QuickRegistrationViewModel::class.java)
//        binding?.lifecycleOwner = this
//        binding?.viewModel = viewModel
//        utils = Utils(requireContext())
//
//
//        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
//        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
//        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
//        departmentUUId = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
//        sharepreferlastPin = appPreferences?.getString(AppConstants.LASTPIN)
//        facility_Name = appPreferences?.getString(AppConstants.INSTITUTION_NAME)!!
//        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
//        val roleid = userDataStoreBean?.role_uuid
//        /* val toolbar =
//             findViewById<View>(R.id.toolbar) as Toolbar*/
//        //setSupportActionBar(toolbar)
//        if (!sharepreferlastPin.equals("")) {
//            val extras = requireActivity().intent.extras
//            if (extras != null) {
//                binding?.lastpinnumber?.visibility = View.INVISIBLE
//                binding?.lastpin?.visibility = View.INVISIBLE
//            } else {
//                binding?.lastpinnumber?.visibility = View.VISIBLE
//                binding?.lastpin?.visibility = View.VISIBLE
//                binding?.lastpinnumber?.text = sharepreferlastPin
//            }
//        } else {
//            binding?.lastpin?.visibility = View.INVISIBLE
//            binding?.lastpinnumber?.visibility = View.INVISIBLE
//        }
//
//
//        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        //toolbar.setNavigationOnClickListener { finish() }
//        customProgressDialog = CustomProgressDialog(requireContext())
//        viewModel!!.errorText.observe(viewLifecycleOwner,
//            Observer { toastMessage ->
//                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
//            })
//
//        viewModel!!.progress.observe(viewLifecycleOwner,
//            Observer { progress ->
//                if (progress == View.VISIBLE) {
//                    customProgressDialog!!.show()
//                } else if (progress == View.GONE) {
//                    customProgressDialog!!.dismiss()
//                }
//            })
//
//        viewModel?.getTextMethod(facility_id!!, getTestMethdCallBack)
//        //Privillage
//        viewModel?.getPrevilage(facility_id!!, roleid, getPrivilageRetrofitCallback)
//
//
//        // viewModel!!.getLocationAPI(covidLocationResponseCallback)
//
//        viewModel!!.getReference(covidgetReferenceResponseCallback)
//
//
//
//        binding?.searchButton?.setOnClickListener {
//            val MobileNumber = binding?.quickMobileNum?.text?.toString()
//            val quicksearch = binding?.qucikSearch?.text?.toString()
//            val pinnumber = binding?.quickPin?.text?.toString()
//            viewModel?.searchPatient(
//                quicksearch!!,
//                pinnumber!!,
//                MobileNumber!!,
//                patientSearchRetrofitCallBack
//            )
//            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
//
//        }
//
//        binding?.advanceSearchText?.setOnClickListener {
//
//            if (binding?.advanceSearchLayout?.visibility == View.VISIBLE) {
//                binding?.advanceSearchLayout?.visibility = View.GONE
//
//
//            } else {
//                binding?.advanceSearchLayout?.visibility = View.VISIBLE
//
//
//            }
//        }
//
//        binding!!.clear.setOnClickListener {
//
//            binding!!.qucikSearch.setText("")
//
//            binding!!.quickPin.setText("")
//
//            binding!!.quickMobileNum.setText("")
//
//            binding!!.quickExistPin.setText("")
//
//            binding!!.qucikPds.setText("")
//        }
//        binding!!.clearCardView.setOnClickListener {
//            val ft = requireFragmentManager().beginTransaction()
//            if (Build.VERSION.SDK_INT >= 26) {
//                ft.setReorderingAllowed(false)
//            }
//            ft.detach(this).attach(this).commit()
//        }
//
//
//        binding!!.quickAge.addTextChangedListener(object : TextWatcher {
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//                if (s.toString() != "") {
//
//                    val datasize = s.toString().toInt()
//
//                    if (selectPeriodUuid == 4) {
//
//                        if (datasize > adultToAge) {
//
//                            binding!!.quickAge.error = "Age Year must be less than $adultToAge"
//
//                        } else {
//
//                            binding!!.quickAge.error = null
//
//                        }
//                    } else if (selectPeriodUuid == 2) {
//
//                        if (datasize > 12) {
//
//                            binding!!.quickAge.error = "Age (Month period) must be less than 12"
//                        } else {
//
//                            binding!!.quickAge.error = null
//
//                        }
//                    } else if (selectPeriodUuid == 3) {
//
//                        if (datasize > 31) {
//
//                            binding!!.quickAge.error = "Age (Day period) must be less than 31"
//
//                        } else {
//
//                            binding!!.quickAge.error = null
//
//                        }
//                    }
//
//                }
//            }
//        })
//
//        binding!!.quickMobile.addTextChangedListener(object : TextWatcher {
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//                val datasize = s.trim().length
//
//                if (datasize == 10) {
//
//
//                    if (s.trim().toString().toLong() < 6000000000) {
//
//                        binding!!.quickMobile.error = "Mobile Number InValid"
//                    } else {
//
//                        binding!!.quickMobile.error = null
//                    }
//                } else {
//
//
//                    binding!!.quickMobile.error = "Mobile Number Must be 10 digit"
//                }
//
//            }
//        })
//
//
//        binding!!.quickPincode.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//                val datasize = s.trim().length
//                if (datasize == 6) {
//                    binding!!.quickPincode.error = null
//                } else {
//                    binding!!.quickPincode.error = "Pin code Must be 6 digit"
//                }
//            }
//        })
//
//        binding!!.quickName.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//                val datasize = s.trim().length
//
//                if (datasize == 100) {
//                    binding!!.quickName.error = "Name must be Maximum 100 letters"
//                } else
//                    if (datasize > 3) {
//                        binding!!.quickName.error = null
//                    } else {
//                        binding!!.quickName.error = "Name must be Minimum 3 letters"
//                    }
//            }
//        })
//
//        binding!!.qucikLabName.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//                val datasize = s.trim().length
//
//                if (datasize in 3..7) {
//
//                    viewModel!!.getLabName(s.toString(), coviLabnameResponseCallback)
//
//
//                } else if (datasize == 0) {
//
//                    binding!!.radiobutton2.isEnabled = true
//
//                    selectLabNameID = 0
//                }
//            }
//        })
//
//        binding!!.radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            if (R.id.radiobutton1 == checkedId) {
//
//                binding!!.labname.isEnabled = true
//
//                ispublic = false
//
//                radioid = rtpcrID
//                sampleid = NasopharyngealID!!
//            } else if (R.id.radiobutton2 == checkedId) {
//
//                binding!!.labname.isEnabled = false
//
//                ispublic = true
//
//                radioid = rtpcrID1
//
//                sampleid = NasopharyngealID1!!
//            }
//        }
//
//        binding?.saveCardView!!.setOnClickListener {
//
//            if (!binding!!.quickName.text.toString().isNullOrEmpty()) {
//
//                if (!binding!!.quickAge.text.toString().isNullOrEmpty()) {
//
//                    if (!binding!!.quickMobile.text.toString().isNullOrEmpty()) {
//
//                        if (!binding!!.quickAddress.text.toString().isNullOrEmpty()) {
//
//                            if (!binding!!.quickPincode.text.toString().isNullOrEmpty()) {
//
////                                if( (binding!!.Checkbox1.isChecked || binding!!.Checkbox2.isChecked) ||  binding!!.Checkbox3.isChecked) {
//
//
//                                var DOB = ""
//
//                                isadult =
//                                    if (selectPeriodUuid == 4 && binding!!.quickAge.text.toString()
//                                            .toInt() >= adultFromAge
//                                    ) {
//                                        1
//                                    } else {
//                                        0
//                                    }
//
//                                Log.i("save", "atlut" + isadult)
//
//
//                                when (selectPeriodUuid) {
//                                    4 -> {
//
//                                        DOB = utils!!.getYear(
//                                            binding!!.quickAge.text.toString().toInt()
//                                        )
//                                            .toString()
//                                    }
//                                    2 -> {
//                                        DOB = utils!!.getAgeMonth(
//                                            binding!!.quickAge.text.toString().toInt()
//                                        )
//                                            .toString()
//
//                                    }
//                                    3 -> {
//
//                                        DOB = utils!!.getDateDaysAgo(
//                                            binding!!.quickAge.text.toString().toInt()
//                                        ).toString()
//
//                                    }
//                                }
//
//                                Log.i("DOB", DOB)
//
//                                if (updateId == 0) {
//                                    quickRegistrationSaveResponseModel.first_name =
//                                        binding!!.quickName.text.toString()
//                                    quickRegistrationSaveResponseModel.mobile =
//                                        binding!!.quickMobile.text.toString()
//                                    quickRegistrationSaveResponseModel.sari = sariStatus
//                                    quickRegistrationSaveResponseModel.ili = iliStatus
//                                    quickRegistrationSaveResponseModel.age =
//                                        binding!!.quickAge.text.toString().toInt()
//                                    quickRegistrationSaveResponseModel.no_symptom =
//                                        nosymptomsStatus
//                                    quickRegistrationSaveResponseModel.is_rapid_test = ispublic
//                                    quickRegistrationSaveResponseModel.sample_type_uuid =
//                                        sampleid
//                                    quickRegistrationSaveResponseModel.gender_uuid =
//                                        selectGenderUuid
//                                    quickRegistrationSaveResponseModel.session_uuid = 1
//                                    quickRegistrationSaveResponseModel.department_uuid =
//                                        departmentUUId.toString()
//                                    quickRegistrationSaveResponseModel.period_uuid =
//                                        selectPeriodUuid
//                                    quickRegistrationSaveResponseModel.registred_facility_uuid =
//                                        facility_id.toString()
//                                    quickRegistrationSaveResponseModel.is_dob_auto_calculate = 1
//                                    quickRegistrationSaveResponseModel.isDrMobileApi = 1
//                                    quickRegistrationSaveResponseModel.country_uuid =
//                                        selectNationalityUuid
//                                    quickRegistrationSaveResponseModel.state_uuid =
//                                        selectStateUuid
//                                    quickRegistrationSaveResponseModel.pincode =
//                                        binding!!.quickPincode.text.toString()
//                                    quickRegistrationSaveResponseModel.address_line1 =
//                                        binding!!.quickAddress.text.toString()
//                                    quickRegistrationSaveResponseModel.district_uuid =
//                                        selectDistictUuid
//                                    quickRegistrationSaveResponseModel.block_uuid =
//                                        selectBelongUuid
//                                    quickRegistrationSaveResponseModel.saveExists =
//                                        alreadyExists
//                                    quickRegistrationSaveResponseModel.dob =
//                                        DOB
//                                    quickRegistrationSaveResponseModel.is_adult = isadult
//                                    if (selectLabNameID != null) {
//                                        quickRegistrationSaveResponseModel.lab_to_facility_uuid =
//                                            selectLabNameID!!
//                                    }
//
//                                    var req = Gson().toJson(quickRegistrationSaveResponseModel)
//                                    Log.e("Data", req.toString())
//                                    viewModel?.quickRegistrationSaveList(
//                                        quickRegistrationSaveResponseModel,
//                                        saveQuickRegistrationRetrofitCallback
//                                    )
//                                } else {
//
//                                    val quickUpdateRequestModel =
//                                        QuickRegistrationUpdateRequestModel()
//                                    quickUpdateRequestModel.first_name =
//                                        binding!!.quickName.text.toString()
//                                    quickUpdateRequestModel.age =
//                                        binding!!.quickAge.text.toString().toInt()
//                                    quickUpdateRequestModel.mobile =
//                                        binding!!.quickMobile.text.toString()
//                                    quickUpdateRequestModel.sari = sariStatus
//                                    quickUpdateRequestModel.ili = iliStatus
//                                    quickUpdateRequestModel.no_symptom = nosymptomsStatus
//                                    quickUpdateRequestModel.is_rapid_test = ispublic
//                                    quickUpdateRequestModel.sample_type_uuid = sampleid
//                                    quickUpdateRequestModel.gender_uuid = selectGenderUuid
//                                    quickUpdateRequestModel.session_uuid = 1
//                                    quickUpdateRequestModel.department_uuid =
//                                        departmentUUId.toString()
//                                    quickUpdateRequestModel.period_uuid = selectPeriodUuid
//                                    quickUpdateRequestModel.registred_facility_uuid =
//                                        facility_id.toString()
//                                    quickUpdateRequestModel.is_dob_auto_calculate = 1
//                                    quickUpdateRequestModel.country_uuid = selectNationalityUuid
//                                    quickUpdateRequestModel.state_uuid = selectStateUuid
//                                    quickRegistrationSaveResponseModel.isDrMobileApi = 1
//                                    quickUpdateRequestModel.pincode =
//                                        binding!!.quickPincode.text.toString()
//                                    quickUpdateRequestModel.address_line1 =
//                                        binding!!.quickAddress.text.toString()
//                                    quickUpdateRequestModel.district_uuid = selectDistictUuid
//                                    quickUpdateRequestModel.block_uuid = selectBelongUuid
//                                    quickUpdateRequestModel.saveExists = false
//                                    quickUpdateRequestModel.dob = DOB
//                                    quickUpdateRequestModel.is_adult = isadult
//                                    quickUpdateRequestModel.uuid = patientUUId
//                                    if (selectLabNameID != null) {
//                                        quickUpdateRequestModel.lab_to_facility_uuid =
//                                            selectLabNameID!!
//                                    }
//                                    viewModel?.quickRegistrationUpdate(
//                                        quickUpdateRequestModel,
//                                        updateQuickRegistrationRetrofitCallback
//                                    )
//                                }
//
//
///*                                }
//                                else{
//                                    Toast.makeText(context!!,"Please check any symptoms checkbox",Toast.LENGTH_SHORT).show()
//                                }*/
//                            } else {
//
//                                binding!!.quickPincode.error = "Pincode Can't be empty"
//
//                            }
//
//
//                        } else {
//
//                            binding!!.quickAddress.error = "Address Can't be empty"
//
//                        }
//
//
//                    } else {
//
//                        binding!!.quickMobile.error = "Mobile Can't be empty"
//
//                    }
//
//                } else {
//
//                    binding!!.quickAge.error = "Age Can't be empty"
//
//                }
//
//            } else {
//
//                binding!!.quickName.error = "Name Can't be empty"
//
//            }
//
//        }
//
//        binding?.saveOrderCardView!!.setOnClickListener {
//
//
//            /*            startActivity(
//                            Intent(this,DialogPDFViewerActivity::class.java)
//                        )*/
//
//
//            //startActivity(Intent(this,LabTestActivity::class.java))
//
//            if (!binding!!.quickName.text.toString().isNullOrEmpty()) {
//
//                if (!binding!!.quickAge.text.toString().isNullOrEmpty()) {
//
//                    if (!binding!!.quickMobile.text.toString().isNullOrEmpty()) {
//
//                        if (!binding!!.quickAddress.text.toString().isNullOrEmpty()) {
//
//                            if (!binding!!.quickPincode.text.toString().isNullOrEmpty()) {
//
//                                if ((binding!!.Checkbox1.isChecked || binding!!.Checkbox2.isChecked) || binding!!.Checkbox3.isChecked) {
//
//                                    var DOB = ""
//
//                                    isadult =
//                                        if (selectPeriodUuid == 4 && binding!!.quickAge.text.toString()
//                                                .toInt() >= adultFromAge
//                                        ) {
//                                            1
//                                        } else {
//                                            0
//                                        }
//
//                                    Log.i("save", "atlut" + isadult)
//
//
//                                    when (selectPeriodUuid) {
//                                        4 -> {
//
//                                            DOB = utils!!.getYear(
//                                                binding!!.quickAge.text.toString().toInt()
//                                            )
//                                                .toString()
//                                        }
//                                        2 -> {
//                                            DOB = utils!!.getAgeMonth(
//                                                binding!!.quickAge.text.toString().toInt()
//                                            )
//                                                .toString()
//
//                                        }
//                                        3 -> {
//
//                                            DOB = utils!!.getDateDaysAgo(
//                                                binding!!.quickAge.text.toString().toInt()
//                                            ).toString()
//
//                                        }
//                                    }
//
//                                    Log.i("DOB", DOB)
//
//
//                                    if (updateId == 0) {
//                                        quickRegistrationSaveResponseModel.first_name =
//                                            binding!!.quickName.text.toString()
//                                        quickRegistrationSaveResponseModel.mobile =
//                                            binding!!.quickMobile.text.toString()
//                                        quickRegistrationSaveResponseModel.sari = sariStatus
//                                        quickRegistrationSaveResponseModel.ili = iliStatus
//                                        quickRegistrationSaveResponseModel.age =
//                                            binding!!.quickAge.text.toString().toInt()
//                                        quickRegistrationSaveResponseModel.no_symptom =
//                                            nosymptomsStatus
//                                        quickRegistrationSaveResponseModel.is_rapid_test = ispublic
//                                        quickRegistrationSaveResponseModel.sample_type_uuid =
//                                            sampleid
//                                        quickRegistrationSaveResponseModel.gender_uuid =
//                                            selectGenderUuid
//                                        quickRegistrationSaveResponseModel.session_uuid = 1
//                                        quickRegistrationSaveResponseModel.department_uuid =
//                                            departmentUUId.toString()
//                                        quickRegistrationSaveResponseModel.isDrMobileApi = 1
//                                        quickRegistrationSaveResponseModel.period_uuid =
//                                            selectPeriodUuid
//                                        quickRegistrationSaveResponseModel.registred_facility_uuid =
//                                            facility_id.toString()
//                                        quickRegistrationSaveResponseModel.is_dob_auto_calculate = 1
//                                        quickRegistrationSaveResponseModel.country_uuid =
//                                            selectNationalityUuid
//                                        quickRegistrationSaveResponseModel.state_uuid =
//                                            selectStateUuid
//                                        quickRegistrationSaveResponseModel.pincode =
//                                            binding!!.quickPincode.text.toString()
//                                        quickRegistrationSaveResponseModel.address_line1 =
//                                            binding!!.quickAddress.text.toString()
//                                        quickRegistrationSaveResponseModel.district_uuid =
//                                            selectDistictUuid
//                                        quickRegistrationSaveResponseModel.block_uuid =
//                                            selectBelongUuid
//                                        quickRegistrationSaveResponseModel.saveExists =
//                                            alreadyExists
//                                        quickRegistrationSaveResponseModel.dob =
//                                            DOB
//                                        quickRegistrationSaveResponseModel.is_adult = isadult
//                                        if (selectLabNameID != null) {
//                                            quickRegistrationSaveResponseModel.lab_to_facility_uuid =
//                                                selectLabNameID!!
//                                        }
//
//                                        var req = Gson().toJson(quickRegistrationSaveResponseModel)
//                                        Log.e("Data", req.toString())
//                                        viewModel?.quickRegistrationSaveList(
//                                            quickRegistrationSaveResponseModel,
//                                            saveQuickOrderRegistrationRetrofitCallback
//                                        )
//                                    } else {
//
//                                        val quickUpdateRequestModel =
//                                            QuickRegistrationUpdateRequestModel()
//                                        quickUpdateRequestModel.first_name =
//                                            binding!!.quickName.text.toString()
//                                        quickUpdateRequestModel.age =
//                                            binding!!.quickAge.text.toString().toInt()
//                                        quickUpdateRequestModel.mobile =
//                                            binding!!.quickMobile.text.toString()
//                                        quickUpdateRequestModel.sari = sariStatus
//                                        quickUpdateRequestModel.ili = iliStatus
//                                        quickUpdateRequestModel.no_symptom = nosymptomsStatus
//                                        quickUpdateRequestModel.is_rapid_test = ispublic
//                                        quickUpdateRequestModel.sample_type_uuid = sampleid
//                                        quickUpdateRequestModel.gender_uuid = selectGenderUuid
//                                        quickUpdateRequestModel.session_uuid = 1
//                                        quickUpdateRequestModel.isDrMobileApi = 1
//                                        quickUpdateRequestModel.department_uuid =
//                                            departmentUUId.toString()
//                                        quickUpdateRequestModel.period_uuid = selectPeriodUuid
//                                        quickUpdateRequestModel.registred_facility_uuid =
//                                            facility_id.toString()
//                                        quickUpdateRequestModel.is_dob_auto_calculate = 1
//                                        quickUpdateRequestModel.country_uuid = selectNationalityUuid
//                                        quickUpdateRequestModel.state_uuid = selectStateUuid
//                                        quickUpdateRequestModel.pincode =
//                                            binding!!.quickPincode.text.toString()
//                                        quickUpdateRequestModel.address_line1 =
//                                            binding!!.quickAddress.text.toString()
//                                        quickUpdateRequestModel.district_uuid = selectDistictUuid
//                                        quickUpdateRequestModel.block_uuid = selectBelongUuid
//                                        quickUpdateRequestModel.saveExists = false
//                                        quickUpdateRequestModel.dob = DOB
//                                        quickUpdateRequestModel.is_adult = isadult
//                                        quickUpdateRequestModel.uuid = patientUUId
//                                        if (selectLabNameID != null) {
//                                            quickUpdateRequestModel.lab_to_facility_uuid =
//                                                selectLabNameID!!
//                                        }
//
//                                        viewModel?.quickRegistrationUpdate(
//                                            quickUpdateRequestModel,
//                                            updateQuicksaveorderRegistrationRetrofitCallback
//                                        )
//                                    }
//                                } else {
//                                    Toast.makeText(
//                                        context!!,
//                                        "Please check any symptoms checkbox",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//
//                            } else {
//
//                                binding!!.quickPincode.error = "Pincode Can't be empty"
//
//                            }
//
//
//                        } else {
//
//                            binding!!.quickAddress.error = "Address Can't be empty"
//
//                        }
//
//
//                    } else {
//
//                        binding!!.quickMobile.error = "Mobile Can't be empty"
//
//                    }
//
//                } else {
//
//                    binding!!.quickAge.error = "Age Can't be empty"
//
//                }
//
//            } else {
//
//                binding!!.quickName.error = "Name Can't be empty"
//
//            }
//        }
//
//        binding?.searchDrawerCardView?.setOnClickListener {
//            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
//        }
//        binding?.drawerLayout?.drawerElevation = 0f
//        binding?.drawerLayout?.setScrimColor(
//            ContextCompat.getColor(
//                requireContext(),
//                android.R.color.transparent
//            )
//        )
//
//        viewModel?.getCovidPeriodList(facility_id!!, covidPeriodResponseCallback)
//
//        viewModel?.getCovidGenderList(facility_id!!, covidGenderResponseCallback)
//
//
//        /*binding?.qucikPeriod!!.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN ->
//
//                        viewModel?.getCovidPeriodList(facility_id!!, covidPeriodResponseCallback)
//
//                }
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })
//
//        binding?.qucikGender!!.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN ->
//
//                        viewModel?.getCovidGenderList(facility_id!!, covidGenderResponseCallback)
//
//                }
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })*/
//
//        binding?.qucikPeriod?.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    val itemValue = parent!!.getItemAtPosition(0).toString()
//                    selectPeriodUuid =
//                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    val itemValue = parent!!.getItemAtPosition(position).toString()
//                    selectPeriodUuid =
//                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]
//
//
//
//
//                    if (binding!!.quickAge.text.toString() != "") {
//
//                        val datasize = binding!!.quickAge.text.toString().toInt()
//
//                        if (selectPeriodUuid == 4) {
//
//                            if (datasize > adultToAge) {
//
//                                binding!!.quickAge.error = "Age Year must be less than $adultToAge"
//
//                            } else {
//
//                                binding!!.quickAge.error = null
//
//                            }
//                        } else if (selectPeriodUuid == 2) {
//
//                            if (datasize > 12) {
//
//                                binding!!.quickAge.error = "Age (Month period) must be less than 12"
//                            } else {
//
//                                binding!!.quickAge.error = null
//
//                            }
//                        } else if (selectPeriodUuid == 3) {
//
//                            if (datasize > 31) {
//
//                                binding!!.quickAge.error = "Age (Day period) must be less than 31"
//
//                            } else {
//
//                                binding!!.quickAge.error = null
//
//                            }
//                        }
//
//                    }
//
//                    Log.e(
//                        "Period",
//                        binding?.qucikPeriod?.selectedItem.toString() + "-" + selectPeriodUuid
//                    )
//                }
//
//            }
//
//        binding?.qucikGender?.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    val itemValue = parent!!.getItemAtPosition(0).toString()
//                    selectGenderUuid =
//                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    val itemValue = parent!!.getItemAtPosition(position).toString()
//                    selectGenderUuid =
//                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]
//
//                    Log.e(
//                        "Gender",
//                        binding?.qucikGender?.selectedItem.toString() + "-" + selectGenderUuid
//                    )
//                }
//
//            }
//
//
//
//
//        binding!!.Checkbox1.setOnCheckedChangeListener { buttonView, isChecked ->
//
//
//            if (isChecked) {
//
//                binding!!.Checkbox3.isEnabled = false
//
//                binding!!.Checkbox3.isChecked = false
//
//                sariStatus = true
//                nosymptomsStatus = false
//
//            } else {
//
//                sariStatus = false
//                if (!binding!!.Checkbox2.isChecked) {
//
//                    binding!!.Checkbox3.isEnabled = true
//                }
//            }
//        }
//
//        binding!!.Checkbox2.setOnCheckedChangeListener { buttonView, isChecked ->
//
//            if (isChecked) {
//
//                binding!!.Checkbox3.isEnabled = false
//
//                binding!!.Checkbox3.isChecked = false
//
//                iliStatus = true
//                nosymptomsStatus = false
//            } else {
//
//                if (!binding!!.Checkbox1.isChecked) {
//
//                    binding!!.Checkbox3.isEnabled = true
//
//
//                }
//                iliStatus = false
//
//            }
//        }
//
//        binding!!.Checkbox3.setOnCheckedChangeListener { buttonView, isChecked ->
//
//
//            if (isChecked) {
//
//                binding!!.Checkbox1.isEnabled = false
//                binding!!.Checkbox2.isEnabled = false
//
//                binding!!.Checkbox1.isChecked = false
//                binding!!.Checkbox2.isChecked = false
//
//
//                sariStatus = false
//                iliStatus = false
//                nosymptomsStatus = true
//
//            } else {
//
//                binding!!.Checkbox1.isEnabled = true
//                binding!!.Checkbox2.isEnabled = true
//                nosymptomsStatus = false
//
//            }
//        }
//
//        binding?.qucikCountry?.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    val itemValue = parent!!.getItemAtPosition(0).toString()
//                    /* selectNationalityUuid =
//                         CovidNationalityList.filterValues { it == itemValue }.keys.toList()[0]*/
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//
//                    val itemValue = parent!!.getItemAtPosition(position).toString()
//                    selectNationalityUuid =
//                        CovidNationalityList.filterValues { it == itemValue }.keys.toList()[0]
//
//                    Log.e("NAtionality", selectNationalityUuid.toString())
//                }
//
//            }
//
//        binding?.qucikState?.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    val itemValue = parent!!.getItemAtPosition(0).toString()
//                    /*     selectStateUuid =
//                             CovidStateList.filterValues { it == itemValue }.keys.toList()[0]*/
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//
//                    val itemValue = parent!!.getItemAtPosition(position).toString()
//                    selectStateUuid =
//                        CovidStateList.filterValues { it == itemValue }.keys.toList()[0]
//
//                    //   selectDistictUuid=0
//
//                    viewModel!!.getDistrict(selectStateUuid, getDistictRetrofitCallback)
//
//                    Log.e("NAtionality", selectStateUuid.toString())
//                }
//
//            }
//        binding?.qucikDistrict?.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    val itemValue = parent!!.getItemAtPosition(0).toString()
///*                    selectDistictUuid =
//                        CovidDistictList.filterValues { it == itemValue }.keys.toList()[0]*/
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//
//                    val itemValue = parent!!.getItemAtPosition(position).toString()
//                    selectDistictUuid =
//                        CovidDistictList.filterValues { it == itemValue }.keys.toList()[0]
//
//                    //  selectBelongUuid=0
//
//                    viewModel!!.getBlock(selectDistictUuid, getBlockRetrofitCallback)
//
//                    Log.e("Distict", selectDistictUuid.toString())
//                }
//
//            }
//
//
//        binding?.qucikBlock?.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    val itemValue = parent!!.getItemAtPosition(0).toString()
//                    /*      selectBelongUuid =
//                              CovidBlockList.filterValues { it == itemValue }.keys.toList()[0]*/
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//
//                    val itemValue = parent!!.getItemAtPosition(position).toString()
//                    selectBelongUuid =
//                        CovidBlockList.filterValues { it == itemValue }.keys.toList()[0]
//
//
//                    Log.e("NAtionality", selectBelongUuid.toString())
//                }
//
//            }
//        simpleToggleButton?.setOnCheckedChangeListener { buttonView, isChecked ->
//            val msg = "Toggle Button is " + if (isChecked) "ON" else "OFF"
//            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
//        }
//
//
//        return binding!!.root
//    }
//
//
//    /*   val covidLocationResponseCallback = object : RetrofitCallback<LocationMasterResponseModelX> {
//           override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModelX>?) {
//
//               val data=responseBody!!.body()!!.responseContents
//
//               if(data.isNotEmpty()) {
//                   locationMasterX = data[0]
//               }
//
//
//
//           }
//
//           override fun onBadRequest(errorBody: Response<LocationMasterResponseModelX>?) {
//               val gson = GsonBuilder().create()
//               val responseModel: LocationMasterResponseModelX
//               try {
//                   responseModel = gson.fromJson(
//                       errorBody!!.errorBody()!!.string(),
//                       LocationMasterResponseModelX::class.java
//                   )
//                   utils?.showToast(
//                       R.color.negativeToast,
//                       binding?.mainLayout!!,
//                       responseModel.message!!
//                   )
//               } catch (e: Exception) {
//                   utils?.showToast(
//                       R.color.negativeToast,
//                       binding?.mainLayout!!,
//                       getString(R.string.something_went_wrong)
//                   )
//                   e.printStackTrace()
//               }
//
//           }
//
//           override fun onServerError(response: Response<*>?) {
//               utils?.showToast(
//                   R.color.negativeToast,
//                   binding?.mainLayout!!,
//                   getString(R.string.something_went_wrong)
//               )
//           }
//
//           override fun onUnAuthorized() {
//               utils?.showToast(
//                   R.color.negativeToast,
//                   binding?.mainLayout!!,
//                   getString(R.string.unauthorized)
//               )
//           }
//
//           override fun onForbidden() {
//               utils?.showToast(
//                   R.color.negativeToast,
//                   binding?.mainLayout!!,
//                   getString(R.string.something_went_wrong)
//               )
//           }
//
//           override fun onFailure(failure: String?) {
//               utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//           }
//
//           override fun onEverytime() {
//               viewModel!!.progress.value = 8
//           }
//       }*/
//
//    val covidgetReferenceResponseCallback = object : RetrofitCallback<GetReferenceResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<GetReferenceResponseModel>?) {
//            viewModel!!.getTotest(covidtestResponseCallback)
//
//            val data = responseBody!!.body()!!.responseContents
//
//            if (data.isNotEmpty()) {
//                getReference = data[0]
//            }
//
//
//        }
//
//        override fun onBadRequest(errorBody: Response<GetReferenceResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: GetReferenceResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    GetReferenceResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.statusCode.toString()
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    val covidtestResponseCallback = object : RetrofitCallback<GettestResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<GettestResponseModel>?) {
//
//            val data = responseBody!!.body()!!.responseContents
//
//            if (data.isNotEmpty()) {
//                gettest = data[0]
//
//                viewModel!!.gettolocationmapid(
//                    facility_id!!,
//                    gettest.uuid,
//                    covidMApResponseCallback
//                )
//
//
//            }
//
//
//        }
//
//        override fun onBadRequest(errorBody: Response<GettestResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: GettestResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    GettestResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.message
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    val covidMApResponseCallback = object : RetrofitCallback<MapListResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<MapListResponseModel>?) {
//
//            val data = responseBody!!.body()!!.responseContents
//
//            if (data != null) {
//
//                locationMapId = data.to_location_uuid
//
//            } else {
//
//                binding!!.saveOrderCardView.visibility = View.GONE
//
//            }
//
//
//            viewModel!!.getApplicationRules(getApplicationRulesResponseCallback)
//
//        }
//
//        override fun onBadRequest(errorBody: Response<MapListResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: GettestResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    GettestResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.message
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    val getApplicationRulesResponseCallback =
//        object : RetrofitCallback<GetApplicationRulesResponseModel> {
//            override fun onSuccessfulResponse(responseBody: Response<GetApplicationRulesResponseModel>?) {
//
//                val data = responseBody!!.body()!!.responseContents
//                if (data.isNotEmpty()) {
//
//                    for (i in data.indices) {
//
//                        if (data[i].field_name == "adultFromAge") {
//
//                            adultFromAge = data[i].field_value.toInt()
//                        }
//
//                        if (data[i].field_name == "adultToAge") {
//
//                            adultToAge = data[i].field_value.toInt()
//
//                            binding!!.quickAge.filters += InputFilter.LengthFilter(data[i].field_value.length)
//                        }
//
//                        if (data[i].field_name == "childToAge") {
//
//                            childToAge = data[i].field_value.toInt()
//
//                        }
//
//                    }
//
//                }
//
//
//            }
//
//            override fun onBadRequest(errorBody: Response<GetApplicationRulesResponseModel>?) {
//                val gson = GsonBuilder().create()
//                val responseModel: GetApplicationRulesResponseModel
//                try {
//                    responseModel = gson.fromJson(
//                        errorBody!!.errorBody()!!.string(),
//                        GetApplicationRulesResponseModel::class.java
//                    )
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        "Something Went Wrong"
//                    )
//                } catch (e: Exception) {
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        getString(R.string.something_went_wrong)
//                    )
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onServerError(response: Response<*>?) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onUnAuthorized() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )
//            }
//
//            override fun onForbidden() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onFailure(failure: String?) {
//                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//        }
//
//
//    val coviLabnameResponseCallback = object : RetrofitCallback<LabNameSearchResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<LabNameSearchResponseModel>?) {
//
//
//            labnameAdapter(responseBody!!.body()!!.responseContents)
//
//        }
//
//        override fun onBadRequest(errorBody: Response<LabNameSearchResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: LabNameSearchResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    LabNameSearchResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.status
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    private fun labnameAdapter(responseContents: ArrayList<LabName>) {
//
//        val responseContentAdapter = LabNameAdapter(
//            context!!,
//            R.layout.row_chief_complaint_search_result,
//            responseContents
//        )
//        binding!!.qucikLabName.threshold = 1
//
//        binding!!.qucikLabName.setAdapter(responseContentAdapter)
//
//        binding!!.qucikLabName.showDropDown()
//
//        binding!!.qucikLabName.setOnItemClickListener { parent, _, pos, id ->
//            val selectedPoi = parent.adapter.getItem(pos) as LabName?
//
//            selectLabNameID = selectedPoi!!.uuid
//
//            binding!!.qucikLabName.setText(selectedPoi.name)
//
//            binding!!.radiobutton2.isEnabled = false
//
//            binding!!.radiobutton1.isChecked = true
//
//            ispublic = false
//
//            radioid = rtpcrID
//
//            sampleid = NasopharyngealID!!
//
//
//            viewModel!!.gettolocationmapid(
//                selectedPoi.uuid,
//                gettest.uuid,
//                LocationMasterResponseCallback
//            )
////            viewModel!!.getLocationMaster(selectedPoi!!.uuid,LocationMasterResponseCallback)
//
//
//        }
//    }
///*
//    val LocationMasterResponseCallback = object : RetrofitCallback<LocationMasterResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModel>?) {
//            Log.i("","locationdata"+responseBody!!.body()!!.responseContents)
//            val data=responseBody!!.body()!!.responseContents
//            if(data.isNotEmpty()) {
//                locationId = data[0].uuid
//            }
//            //    labnameAdapter(responseBody!!.body()!!.responseContents)
//        }
//        override fun onBadRequest(errorBody: Response<LocationMasterResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: LocationMasterResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    LocationMasterResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.message!!
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//        override fun onEverytime() {
//
//            viewModel!!.progress.value = 8
//
//        }
//    }
//
//*/
//
//    val LocationMasterResponseCallback = object : RetrofitCallback<MapListResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<MapListResponseModel>?) {
//            Log.i("", "locationdata" + responseBody!!.body()!!.responseContents)
//            val data = responseBody.body()!!.responseContents
//            if (data != null) {
//                locationId = data.to_location_uuid
//            }
//        }
//
//        override fun onBadRequest(errorBody: Response<MapListResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: LocationMasterResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    LocationMasterResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.message
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//
//            viewModel!!.progress.value = 8
//
//        }
//    }
//    val GetLabNameListResponseCallback = object : RetrofitCallback<GetLabNameListResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<GetLabNameListResponseModel>?) {
//            Log.i("", "locationdata" + responseBody!!.body()!!.responseContent)
//            val dataList = responseBody.body()!!.responseContent
//            //   binding!!.qucikLabName.setText(responseBody!!.body()!!.responseContent.name)
//            if (dataList != null || dataList.uuid != 0) {
//                binding!!.qucikLabName.setText(dataList.name)
//            } else {
//                Log.i("null", "null")
//            }
//            //    labnameAdapter(responseBody!!.body()!!.responseContents)
//        }
//
//        override fun onBadRequest(errorBody: Response<GetLabNameListResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: GetLabNameListResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    GetLabNameListResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    "Something want wrong"
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//
//            viewModel!!.progress.value = 8
//
//
//        }
//    }
//
//
//    val covidPeriodResponseCallback = object : RetrofitCallback<CovidPeriodResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<CovidPeriodResponseModel>?) {
//
//            selectPeriodUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
//            setPeriod(responseBody.body()?.responseContents)
//        }
//
//        override fun onBadRequest(errorBody: Response<CovidPeriodResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: CovidPeriodResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    CovidPeriodResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.req!!
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    fun setPeriod(responseContents: List<PeriodresponseContent?>?) {
//        CovidPeriodList =
//            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
//
//        hashPeriodSpinnerList.clear()
//
//        for (i in responseContents.indices) {
//
//            hashPeriodSpinnerList[responseContents[i]!!.uuid!!] = i
//        }
//
//
//        val adapter = ArrayAdapter<String>(
//            context!!,
//            android.R.layout.simple_spinner_item,
//            CovidPeriodList.values.toMutableList()
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding?.qucikPeriod!!.adapter = adapter
//
//        binding!!.qucikPeriod.setSelection(2)
//
//    }
//
//    val covidGenderResponseCallback = object : RetrofitCallback<CovidGenderResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<CovidGenderResponseModel>?) {
//
//            selectGenderUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
//            setGender(responseBody.body()?.responseContents)
//
//            viewModel!!.getFaciltyLocation(facilityLocationResponseCallback)
//        }
//
//        override fun onBadRequest(errorBody: Response<CovidGenderResponseModel>?) {
//            val gson = GsonBuilder().create()
//            val responseModel: CovidGenderResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    errorBody!!.errorBody()!!.string(),
//                    CovidGenderResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.req!!
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//
//    fun setGender(responseContents: List<GenderresponseContent?>?) {
//        CovidGenderList =
//            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
//
//        hashGenderSpinnerList.clear()
//
//        for (i in responseContents.indices) {
//
//            hashGenderSpinnerList[responseContents[i]!!.uuid!!] = i
//        }
//
//        val adapter = ArrayAdapter<String>(
//            context!!,
//            android.R.layout.simple_spinner_item,
//            CovidGenderList.values.toMutableList()
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding?.qucikGender!!.adapter = adapter
//
//    }
//
//
//    val facilityLocationResponseCallback =
//        object : RetrofitCallback<FacilityLocationResponseModel> {
//            override fun onSuccessfulResponse(responseBody: Response<FacilityLocationResponseModel>?) {
//
//                if (responseBody!!.body()!!.responseContents != null) {
//
//                    facility_Name = responseBody.body()!!.responseContents.facility.name
//
//                    if (responseBody.body()!!.responseContents.country_master != null) {
//
//                        selectNationalityUuid =
//                            responseBody.body()!!.responseContents.country_master.uuid
//                    }
//
//                    if (responseBody.body()!!.responseContents.state_master != null) {
//                        selectStateUuid = responseBody.body()!!.responseContents.state_master.uuid
//
//                    }
//
//                    if (responseBody.body()!!.responseContents.district_master != null) {
//                        selectDistictUuid =
//                            responseBody.body()!!.responseContents.district_master.uuid
//                    }
//                    viewModel?.getCovidNationalityList(
//                        "nationality_type",
//                        covidNationalityResponseCallback
//                    )
//                } else {
//
//                    viewModel?.getCovidNationalityList(
//                        "nationality_type",
//                        covidNationalityResponseCallback
//                    )
//                }
//            }
//
//            override fun onBadRequest(errorBody: Response<FacilityLocationResponseModel>?) {
//                val gson = GsonBuilder().create()
//                val responseModel: FacilityLocationResponseModel
//                try {
//                    responseModel = gson.fromJson(
//                        errorBody!!.errorBody()!!.string(),
//                        FacilityLocationResponseModel::class.java
//                    )
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        "Something Wrong"
//                    )
//                } catch (e: Exception) {
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        getString(R.string.something_went_wrong)
//                    )
//                    e.printStackTrace()
//                }
//
//            }
//
//            override fun onServerError(response: Response<*>?) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onUnAuthorized() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )
//            }
//
//            override fun onForbidden() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onFailure(failure: String?) {
//                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//        }
//
//    var saveQuickRegistrationRetrofitCallback = object :
//        RetrofitCallback<QuickRegistrationSaveResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationSaveResponseModel>?) {
//
//            utils?.showToast(
//                R.color.positiveToast,
//                mainLayout!!,
//                "Register Success"
//            )
//
//            Log.i("", "" + responseBody?.body()?.responseContent)
//
//            appPreferences?.saveString(
//                AppConstants.LASTPIN,
//                responseBody?.body()?.responseContent?.uhid
//            )
//
//            val pdfRequestModel = PDFRequestModel()
//            pdfRequestModel.componentName = "quick"
//            pdfRequestModel.uuid = responseBody?.body()?.responseContent?.uuid
//            pdfRequestModel.uhid = responseBody?.body()?.responseContent?.uhid
//            pdfRequestModel.facilityName = facility_Name
//            pdfRequestModel.firstName = responseBody?.body()?.responseContent?.first_name
//            pdfRequestModel.period = CovidPeriodList[selectPeriodUuid]
//            pdfRequestModel.age = responseBody?.body()?.responseContent?.age
//            pdfRequestModel.registered_date = responseBody?.body()?.responseContent?.registered_date
//            pdfRequestModel.sari = sariStatus
//            pdfRequestModel.ili = iliStatus
//            pdfRequestModel.noSymptom = nosymptomsStatus
//
//            if (ispublic!!) {
//                pdfRequestModel.testMethod = "Rapid Diagnostic Test ( Blood for Covid 19 )"
//            } else {
//                pdfRequestModel.testMethod = "RT - PCR(Nasopharyngeal Swab for Covid 19)"
//            }
//
//            pdfRequestModel.gender = CovidGenderList[selectGenderUuid]
//            pdfRequestModel.mobile = binding!!.quickMobile.text.toString()
//            pdfRequestModel.addressDetails?.doorNum = binding!!.quickAddress.text.toString()
//            pdfRequestModel.addressDetails?.country = CovidNationalityList[selectNationalityUuid]
//            pdfRequestModel.addressDetails?.state = CovidStateList[selectStateUuid]
//            pdfRequestModel.addressDetails?.pincode = binding!!.quickPincode.text.toString()
//            pdfRequestModel.addressDetails?.district = CovidDistictList[selectDistictUuid]
//            pdfRequestModel.addressDetails?.block = CovidBlockList[selectBelongUuid]
//
//            val gson = Gson()
//            // val intent = Intent(requireContext(), DialogPDFViewerActivity::class.java)
//
//            val bundle = Bundle()
//            bundle.putParcelable(AppConstants.RESPONSECONTENT, pdfRequestModel)
//            bundle.putInt(AppConstants.RESPONSENEXT, 10)
//            bundle.putString("From", "")
//
//            val labtemplatedialog = DialogPDFViewerActivity()
//
//            labtemplatedialog.arguments = bundle
//
//            (activity as HomeActivity).replaceFragment(labtemplatedialog)
//
//
//        }
//
//        override fun onBadRequest(response: Response<QuickRegistrationSaveResponseModel>) {
//            Log.e("badreq", response.toString())
//            val gson = GsonBuilder().create()
//            val responseModel: QuickRegistrationSaveResponseModel
//            var mError = ErrorAPIClass()
//            try {
//                mError = gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)
//
//                Toast.makeText(context!!, mError.message, Toast.LENGTH_LONG).show()
//
//                if (mError.message == "Patient already exists") {
//
//                    saveAgain()
//                }
//
//            } catch (e: IOException) { // handle failure to read error
//            }
//        }
//
//        override fun onServerError(response: Response<*>) {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                "serverError"
//            )
//
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                "Forbidden"
//            )
//
//        }
//
//        override fun onFailure(failure: String) {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                failure
//            )
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//
//    }
//
//    var saveQuickOrderRegistrationRetrofitCallback = object :
//        RetrofitCallback<QuickRegistrationSaveResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationSaveResponseModel>?) {
//
//            patientUUId = responseBody!!.body()!!.responseContent!!.uuid
//
//
//
//            uhid = responseBody.body()!!.responseContent!!.uhid!!.toString()
//            appPreferences?.saveString(AppConstants.LASTPIN, uhid)
//
//            registerDate = responseBody.body()!!.responseContent!!.registered_date!!.toString()
//
//            viewModel!!.createEncounter(
//                createEncounterRetrofitCallback,
//                responseBody.body()!!.responseContent!!.uuid!!
//            )
//
//        }
//
//        override fun onBadRequest(response: Response<QuickRegistrationSaveResponseModel>) {
//            Log.e("badreq", response.toString())
//            val gson = GsonBuilder().create()
//            val responseModel: QuickRegistrationSaveResponseModel
//            var mError = ErrorAPIClass()
//            try {
//                mError = gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)
//
//                Toast.makeText(context!!, mError.message, Toast.LENGTH_LONG).show()
//
//                if (mError.message == "Patient already exists") {
//
//                    saveOrderAgain()
//                }
//
//            } catch (e: IOException) { // handle failure to read error
//            }
//        }
//
//        override fun onServerError(response: Response<*>) {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                "serverError"
//            )
//
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                "Forbidden"
//            )
//
//        }
//
//        override fun onFailure(failure: String) {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                failure
//            )
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//
//    }
//
//    private fun saveOrderAgain() {
//
//        alreadyExists = true
//
//        customdialog = Dialog(requireContext())
//        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        customdialog!!.setCancelable(false)
//        customdialog!!.setContentView(R.layout.duplicate_add_dialog)
//        val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
//        closeImageView.setOnClickListener {
//
//            customdialog?.dismiss()
//        }
//
//        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
//        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
//        yesBtn.setOnClickListener {
//
//            quickRegistrationSaveResponseModel.saveExists = alreadyExists
//
//            viewModel?.quickRegistrationSaveList(
//                quickRegistrationSaveResponseModel,
//                saveQuickOrderRegistrationRetrofitCallback
//            )
//
//            customdialog!!.dismiss()
//
//
//        }
//        noBtn.setOnClickListener {
//            customdialog!!.dismiss()
//
//            alreadyExists = false
//        }
//        customdialog!!.show()
//
//    }
//
//    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
//        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {
//
//            encounter_doctor_id =
//                response.body()!!.responseContents!!.encounterDoctor!!.uuid!!.toInt()
//
//            encounter_id = response.body()!!.responseContents!!.encounter!!.uuid
//
//            saveOrderAPICall()
//
//        }
//
//        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
//            Log.e("badreq", response.toString())
//            val gson = GsonBuilder().create()
//            val responseModel: QuickRegistrationSaveResponseModel
//            var mError = EncounterErrorAPIClass()
//            try {
//                mError = gson.fromJson(
//                    response.errorBody()!!.string(),
//                    EncounterErrorAPIClass::class.java
//                )
//
//
//                if (mError.code == 400) {
//
//                    encounter_doctor_id = mError.existingDetails.encounter_doctor_id
//
//                    encounter_id = mError.existingDetails.encounter_id
//
//                    saveOrderAPICall()
//
//                }
//
//            } catch (e: IOException) { // handle failure to read error
//            }
//        }
//
//        override fun onServerError(response: Response<*>) {
//            /* utils?.showToast(
//                 R.color.negativeToast,
//                 binding?.mainLayout!!,
//                 getString(R.string.something_went_wrong)
//             )*/
//        }
//
//        override fun onUnAuthorized() {
//            /*    utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )*/
//        }
//
//        override fun onForbidden() {
//            /*   utils?.showToast(
//                   R.color.negativeToast,
//                   binding?.mainLayout!!,
//                   getString(R.string.something_went_wrong)
//               )*/
//        }
//
//        override fun onFailure(failure: String) {
////            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    private fun saveOrderAPICall() {
//
//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//
//        val dateInString = sdf.format(Date())
//
//        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
//
//        val saveOrderRequestModel: SaveOrderRequestModel = SaveOrderRequestModel()
//
//        val header: Header = Header()
//        header.is_auto_accept = 1
//        header.patient_uuid = patientUUId!!
//        header.encounter_uuid = encounter_id!!
//        header.encounter_type_uuid = 1
//        header.lab_master_type_uuid = 1
//        header.encounter_doctor_uuid = encounter_doctor_id!!
//        header.doctor_uuid = userDataStoreBean?.uuid!!.toString()
//        header.facility_uuid = facility_id.toString()
//        header.department_uuid = departmentUUId.toString()
//        header.sub_department_uuid = 0
//        header.order_to_location_uuid = 1
//        header.consultation_uuid = 0
//        header.patient_treatment_uuid = 0
//        header.order_status_uuid = 0
//        header.treatment_plan_uuid = 0
//        header.assign_to_location_uuid = locationId!!
//        if (selectLabNameID != null) {
//            header.to_facility_uuid = selectLabNameID!!
//        }
//        header.tat_session_start = dateInString
//        header.tat_session_end = dateInString
//
//
//        val detailsList: ArrayList<Detail> = ArrayList()
//
//        val details: Detail = Detail()
//
//        details.profile_uuid = ""
//        details.test_master_uuid = gettest.uuid
//        details.is_profile = false
//        details.lab_master_type_uuid = 1
//        details.to_department_uuid = gettest.department_uuid
//        details.order_priority_uuid = getReference.uuid
//        details.to_location_uuid = locationMapId
//        details.sample_type_uuid = sampleid!!
//        details.type_of_method_uuid = radioid!!
//        details.group_uuid = 0
//        details.to_sub_department_uuid = 0
//        details.tat_session_start = dateInString
//        details.tat_session_end = dateInString
//        details.is_active = true
//        details.test_diseases_uuid = gettest.test_diseases_uuid
//        details.is_approval_requried = true
//        details.confidential_uuid = gettest.confidential_uuid
//
//        details.is_confidential = true
//
//        detailsList.add(details)
//
//        saveOrderRequestModel.header = header
//
//        saveOrderRequestModel.details = detailsList
//
//        viewModel!!.getSaveOrder(saveOrderRequestModel, saveOrderRegistrationRetrofitCallback)
//
//
//    }
//
//    var saveOrderRegistrationRetrofitCallback = object :
//        RetrofitCallback<SaveOrderResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<SaveOrderResponseModel>?) {
//
//            utils?.showToast(
//                R.color.positiveToast,
//                mainLayout!!,
//                "Register Success"
//
//            )
//
//
//            toLabActivity()
//
//        }
//
//        override fun onBadRequest(response: Response<SaveOrderResponseModel>) {
//            Log.e("badreq", response.toString())
//            val gson = GsonBuilder().create()
//            val responseModel: SaveOrderResponseModel
//            var mError = ErrorAPIClass()
//            try {
//                mError = gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)
//
//                Toast.makeText(context!!, mError.message, Toast.LENGTH_LONG).show()
//            } catch (e: IOException) { // handle failure to read error
//            }
//        }
//
//        override fun onServerError(response: Response<*>) {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                "serverError"
//            )
//
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                "Forbidden"
//            )
//
//        }
//
//        override fun onFailure(failure: String) {
//            utils?.showToast(
//                R.color.negativeToast,
//                mainLayout!!,
//                failure
//            )
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//
//    }
//
//
//    private fun saveAgain() {
//
//        alreadyExists = true
//
//        customdialog = Dialog(requireContext())
//        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        customdialog!!.setCancelable(false)
//        customdialog!!.setContentView(R.layout.duplicate_add_dialog)
//        val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
//        closeImageView.setOnClickListener {
//
//            customdialog?.dismiss()
//        }
//
//        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
//        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
//        yesBtn.setOnClickListener {
//
//            quickRegistrationSaveResponseModel.saveExists = alreadyExists
//
//            viewModel?.quickRegistrationSaveList(
//                quickRegistrationSaveResponseModel,
//                saveQuickRegistrationRetrofitCallback
//            )
//
//            customdialog!!.dismiss()
//
//
//        }
//        noBtn.setOnClickListener {
//            customdialog!!.dismiss()
//
//            alreadyExists = false
//        }
//        customdialog!!.show()
//    }
//
//
//    val covidNationalityResponseCallback =
//        object : RetrofitCallback<CovidNationalityResponseModel> {
//            override fun onSuccessfulResponse(responseBody: Response<CovidNationalityResponseModel>?) {
//
//
//                if (selectNationalityUuid != null && selectNationalityUuid != 0) {
//
//                    setNationality(responseBody?.body()?.responseContents!!)
//                    viewModel!!.getStateList(selectNationalityUuid, getStateRetrofitCallback)
//
//                } else {
//
//
//                    selectNationalityUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
//
//                    setNationality(responseBody.body()?.responseContents!!)
//                    viewModel!!.getStateList(selectNationalityUuid, getStateRetrofitCallback)
//                }
//
//            }
//
//            override fun onBadRequest(errorBody: Response<CovidNationalityResponseModel>?) {
//                /*    val gson = GsonBuilder().create()
//                    val responseModel: CovidNationalityResponseModel
//                    try {
//                        responseModel = gson.fromJson(
//                            errorBody!!.errorBody()!!.string(),
//                            CovidNationalityResponseModel::class.java
//                        )
//                        utils?.showToast(
//                            R.color.negativeToast,
//                            binding?.mainLayout!!,
//                            getString(R.string.something_went_wrong)
//                        )
//                    } catch (e: Exception) {
//                        utils?.showToast(
//                            R.color.negativeToast,
//                            binding?.mainLayout!!,
//                            getString(R.string.something_went_wrong)
//                        )
//                        e.printStackTrace()
//                    }*/
//
//            }
//
//            override fun onServerError(response: Response<*>?) {
//
//            }
//
//            override fun onUnAuthorized() {
//
//            }
//
//            override fun onForbidden() {
//
//            }
//
//            override fun onFailure(failure: String?) {
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//        }
//
//    fun setNationality(responseContents: List<NationalityresponseContent?>?) {
//        CovidNationalityList =
//            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
//
//        hashNationalitySpinnerList.clear()
//
//        for (i in responseContents.indices) {
//
//            hashNationalitySpinnerList[responseContents[i]!!.uuid!!] = i
//        }
//
//
//        val adapter = ArrayAdapter<String>(
//            requireContext(),
//            android.R.layout.simple_spinner_item,
//            CovidNationalityList.values.toMutableList()
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding?.qucikCountry!!.adapter = adapter
//
//        if (selectNationalityUuid != null) {
//            val checknationality =
//                hashNationalitySpinnerList.any { it.key == selectNationalityUuid }
//            if (checknationality) {
//                binding?.qucikCountry!!.setSelection(
//                    hashNationalitySpinnerList.get(
//                        selectNationalityUuid
//                    )!!
//                )
//            } else {
//                binding?.qucikCountry!!.setSelection(0)
//            }
//        }
//
//    }
//
//
//    val getStateRetrofitCallback = object : RetrofitCallback<StateListResponseModel> {
//        override fun onSuccessfulResponse(responseBody: Response<StateListResponseModel>?) {
//
//            try {
//
//
//                if (selectStateUuid != null && selectStateUuid != 0) {
//                    viewModel!!.getDistrict(selectStateUuid, getDistictRetrofitCallback)
//
//                    setState(responseBody?.body()?.responseContents!!)
//
//                } else {
//
//                    selectStateUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid
//
//                    viewModel!!.getDistrict(selectStateUuid, getDistictRetrofitCallback)
//
//                    setState(responseBody.body()?.responseContents!!)
//                }
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onBadRequest(errorBody: Response<StateListResponseModel>?) {
//
//            /*     val gson = GsonBuilder().create()
//                 val responseModel: StateListResponseModel
//                 try {
//                     utils?.showToast(
//                         R.color.negativeToast,
//                         binding?.mainLayout!!,
//                         ""
//                     )
//                 } catch (e: Exception) {
//                     utils?.showToast(
//                         R.color.negativeToast,
//                         binding?.mainLayout!!,
//                         getString(R.string.something_went_wrong)
//                     )
//                     e.printStackTrace()
//                 }*/
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//
//        }
//
//        override fun onUnAuthorized() {
//
//        }
//
//        override fun onForbidden() {
//
//        }
//
//        override fun onFailure(s: String?) {
//
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    private fun setState(responseContents: ArrayList<State>) {
//
//        CovidStateList = responseContents.map { it.uuid to it.name }.toMap().toMutableMap()
//
//        //hashStateSpinnerList
//
//        hashStateSpinnerList.clear()
//
//        for (i in responseContents.indices) {
//
//            hashStateSpinnerList[responseContents[i].uuid] = i
//        }
//
//        val adapter = ArrayAdapter<String>(
//            context!!,
//            android.R.layout.simple_spinner_item,
//            CovidStateList.values.toMutableList()
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding?.qucikState!!.adapter = adapter
//
//
//        if (selectStateUuid != null) {
//            val checkstate =
//                hashStateSpinnerList.any { it.key == selectStateUuid }
//            if (checkstate) {
//                binding?.qucikState!!.setSelection(hashStateSpinnerList.get(selectStateUuid)!!)
//            } else {
//                binding?.qucikState!!.setSelection(0)
//            }
//        }
//
//    }
//
//    val getDistictRetrofitCallback = object : RetrofitCallback<DistrictListResponseModel> {
//
//        override fun onSuccessfulResponse(responseBody: Response<DistrictListResponseModel>?) {
//
//            try {
//
//                if (selectDistictUuid != 0 && selectDistictUuid != null) {
//
//                    setDistict(responseBody?.body()?.responseContents!!)
//
//                    viewModel!!.getBlock(selectDistictUuid, getBlockRetrofitCallback)
//
//                } else {
//                    selectDistictUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid
//
//                    setDistict(responseBody.body()?.responseContents!!)
//
//                    viewModel!!.getBlock(selectDistictUuid, getBlockRetrofitCallback)
//
//                }
//
//
//            } catch (e: Exception) {
//
//
//            }
//
//        }
//
//        override fun onBadRequest(errorBody: Response<DistrictListResponseModel>?) {
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//
//        }
//
//        override fun onUnAuthorized() {
//
//        }
//
//        override fun onForbidden() {
//
//        }
//
//        override fun onFailure(s: String?) {
//
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    private fun setDistict(responseContents: ArrayList<District>) {
//
//        CovidDistictList =
//            responseContents.map { it.uuid to it.name }.toMap().toMutableMap()
//
//        hashDistrictSpinnerList.clear()
//
//        for (i in responseContents.indices) {
//
//            hashDistrictSpinnerList[responseContents[i].uuid] = i
//        }
//
//        val adapter = ArrayAdapter<String>(
//            requireContext(),
//            android.R.layout.simple_spinner_item,
//            CovidDistictList.values.toMutableList()
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding?.qucikDistrict!!.adapter = adapter
//
//
//        if (selectDistictUuid != null) {
//            val checkdistrict =
//                hashDistrictSpinnerList.any { it.key == selectDistictUuid }
//            if (checkdistrict) {
//                binding?.qucikDistrict!!.setSelection(hashDistrictSpinnerList.get(selectDistictUuid)!!)
//            } else {
//                binding?.qucikDistrict!!.setSelection(0)
//            }
//        }
//
//    }
//
//    val getBlockRetrofitCallback = object : RetrofitCallback<BlockZoneResponseModel> {
//
//        override fun onSuccessfulResponse(responseBody: Response<BlockZoneResponseModel>?) {
//
//            try {
//
//                if (selectBelongUuid != null && selectBelongUuid != 0) {
//
//                    setBlock(responseBody?.body()?.responseContents!!)
//                } else {
//                    selectBelongUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid
//
//                    setBlock(responseBody.body()?.responseContents!!)
//
//                }
//            } catch (e: Exception) {
//
//
//            }
//
//        }
//
//        override fun onBadRequest(errorBody: Response<BlockZoneResponseModel>?) {
//
//        }
//
//        override fun onServerError(response: Response<*>?) {
//
//        }
//
//        override fun onUnAuthorized() {
//
//        }
//
//        override fun onForbidden() {
//
//        }
//
//        override fun onFailure(s: String?) {
//
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    private fun setBlock(responseContents: List<BlockZone>) {
//
//        CovidBlockList = responseContents.map { it.uuid to it.name }.toMap().toMutableMap()
//
//        hashBlockSpinnerList.clear()
//
//        for (i in responseContents.indices) {
//
//            hashBlockSpinnerList[responseContents[i].uuid] = i
//        }
//
//        val adapter = ArrayAdapter<String>(
//            requireContext(),
//            android.R.layout.simple_spinner_item,
//            CovidBlockList.values.toMutableList()
//        )
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding?.qucikBlock!!.adapter = adapter
//
//    }
//
//    val getTestMethdCallBack =
//        object : RetrofitCallback<ResponseTestMethod> {
//            override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {
//                array_testmethod = response.body()?.responseContents
//                viewModel?.getTextMethod1(facility_id!!, getTestMethdCallBack1)
//
//
//            }
//
//            override fun onBadRequest(response: Response<ResponseTestMethod>) {
//                val gson = GsonBuilder().create()
//                val responseModel: ResponseTestMethod
//                try {
//
//                    responseModel = gson.fromJson(
//                        response.errorBody()!!.string(),
//                        ResponseTestMethod::class.java
//                    )
//
//                } catch (e: Exception) {
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        getString(R.string.something_went_wrong)
//                    )
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onServerError(response: Response<*>) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onUnAuthorized() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )
//            }
//
//            override fun onForbidden() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onFailure(failure: String) {
//                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//        }
//    val getTestMethdCallBack1 =
//        object : RetrofitCallback<ResponseTestMethod> {
//            @SuppressLint("SetTextI18n")
//            override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {
//                Log.i("", "" + response.body()?.responseContents)
//                Log.i("", "" + response.body()?.req)
//                array_testmethod1 = response.body()?.responseContents
//
//                for (i in array_testmethod!!.indices) {
//                    if (array_testmethod?.get(i)?.code?.equals("RT - PCR")!!) {
//                        rtpcrID = array_testmethod?.get(i)!!.uuid
//                        binding?.radiobutton1?.text = array_testmethod?.get(i)!!.name
//
//                    }
//
//                    if (array_testmethod?.get(i)?.code?.equals("Rapid Di")!!) {
//                        rtpcrID1 = array_testmethod?.get(i)!!.uuid
//                        binding?.radiobutton2?.text = array_testmethod?.get(i)!!.name
//
//                    }
//
//                }
//                for (k in array_testmethod1?.indices!!) {
//
//                    if (array_testmethod1?.get(k)?.code?.equals("Nasopharyngeal")!!) {
//                        NasopharyngealID = array_testmethod1?.get(k)!!.uuid
//                        binding?.radiobutton1?.text =
//                            binding?.radiobutton1?.text?.toString() + "(" + array_testmethod1?.get(
//                                k
//                            )!!.name + ")"
//
//                        binding?.radiobutton1?.isChecked = true
//                        ispublic = false
//
//                        radioid = rtpcrID
//                        sampleid = NasopharyngealID!!
//                    }
//                    if (array_testmethod1?.get(k)?.code?.equals("Blood")!!) {
//                        NasopharyngealID1 = array_testmethod1?.get(k)!!.uuid
//                        binding?.radiobutton2?.text =
//                            binding?.radiobutton2?.text?.toString() + "(" + array_testmethod1?.get(
//                                k
//                            )!!.name + ")"
//                    }
//                }
//
//
//
//                Log.i("", "" + rtpcrID)
//                Log.i("", "" + rtpcrID1)
//                Log.i("", "" + NasopharyngealID)
//                Log.i("", "" + NasopharyngealID1)
//            }
//
//            override fun onBadRequest(response: Response<ResponseTestMethod>) {
//                val gson = GsonBuilder().create()
//                val responseModel: ResponseTestMethod
//                try {
//
//                    responseModel = gson.fromJson(
//                        response.errorBody()!!.string(),
//                        ResponseTestMethod::class.java
//                    )
//
//                } catch (e: Exception) {
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        getString(R.string.something_went_wrong)
//                    )
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onServerError(response: Response<*>) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onUnAuthorized() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )
//            }
//
//            override fun onForbidden() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onFailure(failure: String) {
//                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//        }
//
//
//    val getPrivilageRetrofitCallback =
//        object : RetrofitCallback<ResponsePrivillageModule> {
//            override fun onSuccessfulResponse(response: Response<ResponsePrivillageModule>) {
//
//                val quickPrevilege: QuickPrevilege = QuickPrevilege()
//                val checksave =
//                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.save }
//
//                if (checksave) {
//                    binding?.saveCardView?.isClickable = true
//                } else {
//                    binding?.saveCardView?.alpha = .5f
//                    binding?.saveCardView?.isClickable = false
//
//                }
//
//                ///Save Order
//
//                val checksaveOrder =
//                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.savenext }
//
//                if (checksaveOrder) {
//                    binding?.saveOrderCardView?.isClickable = true
//                } else {
//                    binding?.saveOrderCardView?.alpha = .5f
//                    binding?.saveOrderCardView?.isClickable = false
//
//                }
//
//                //search
//
//                val checkseach =
//                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.search }
//
//                if (checkseach) {
//                    binding?.searchButton?.isClickable = true
//                } else {
//                    binding?.searchButton?.alpha = .5f
//                    binding?.searchButton?.isClickable = false
//
//                }
//
//                //Lab
//
//                //search
//
//                val checkLab =
//                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.lisLabToFacility }
//
//                if (checkLab) {
//                    binding?.labname?.visibility = View.VISIBLE
//
//                } else {
//                    binding?.labname?.visibility = View.GONE
//
//                }
//
//
//                val checkSymtoms =
//                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.Symtoms }
//
//                if (checkSymtoms) {
//                    binding?.symptomstextviewer?.visibility = View.VISIBLE
//                    binding?.symptomsviewer?.visibility = View.VISIBLE
//
//                } else {
//                    binding?.symptomstextviewer?.visibility = View.GONE
//
//                    binding?.symptomsviewer?.visibility = View.GONE
//
//                    radioid = 0
//
//                }
//
//
//                val checkLabtest =
//                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.lisSpecimenTest }
//
//                if (checkLabtest) {
//
//                    binding?.testMethodView?.visibility = View.VISIBLE
//
//                } else {
//
//                    binding?.testMethodView?.visibility = View.GONE
//
//                }
//
//
//            }
//
//            override fun onBadRequest(response: Response<ResponsePrivillageModule>) {
//                val gson = GsonBuilder().create()
//                Log.i("", "Badddddddd")
//            }
//
//            override fun onServerError(response: Response<*>) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onUnAuthorized() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )
//            }
//
//            override fun onForbidden() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onFailure(failure: String) {
//                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//        }
//
//
//    val patientSearchRetrofitCallBack = object : RetrofitCallback<QuickSearchResponseModel> {
//        override fun onSuccessfulResponse(response: Response<QuickSearchResponseModel>) {
//
//            if (response.body()?.responseContents?.isNotEmpty()!!) {
////                viewModel?.errorTextVisibility?.value = 8
//                updateId = 1
//
//                if (response.body()?.responseContents?.size == 1) {
//                    binding?.lastpin?.visibility = View.VISIBLE
//                    binding?.lastpinnumber?.visibility = View.VISIBLE
//                    binding?.lastpinnumber?.text = response.body()!!.responseContents!![0]!!.uhid
//                    binding?.quickName!!.setText(response.body()!!.responseContents!![0]!!.first_name)
//                    binding?.quickAge!!.setText(response.body()!!.responseContents!![0]!!.age!!.toString())
//                    binding?.quickMobile!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.mobile!!.toString())
//                    binding?.quickAddress!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.address_line1!!.toString())
//                    binding?.quickPincode!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.pincode!!.toString())
//                    //binding?.qucikLabName!!.setText(response.body()!!.responseContents!![0]!!.last_name!!.toString())
//
//                    binding?.Checkbox1!!.isChecked =
//                        response.body()!!.responseContents!![0]!!.patient_detail?.sari!!
//                    binding?.Checkbox2!!.isChecked =
//                        response.body()!!.responseContents!![0]!!.patient_detail?.ili!!
//                    binding?.Checkbox3!!.isChecked =
//                        response.body()!!.responseContents!![0]!!.patient_detail?.no_symptom!!
//                    sariStatus = response.body()!!.responseContents!![0]!!.patient_detail?.sari!!
//                    iliStatus = response.body()!!.responseContents!![0]!!.patient_detail?.ili!!
//                    nosymptomsStatus =
//                        response.body()!!.responseContents!![0]!!.patient_detail?.no_symptom!!
//
//                    if (response.body()!!.responseContents!![0]!!.patient_detail?.is_rapid_test == true) {
//                        binding?.radiobutton2!!.isChecked = true
//                        binding?.radiobutton1!!.isChecked = false
//                        ispublic = true
//
//                        radioid = rtpcrID1
//
//                        sampleid = NasopharyngealID1!!
//                    } else {
//                        binding?.radiobutton2!!.isChecked = false
//                        binding?.radiobutton1!!.isChecked = true
//                        ispublic = false
//
//                        radioid = rtpcrID
//                        sampleid = NasopharyngealID!!
//                    }
//                    if (response.body()!!.responseContents!![0]!!.gender_uuid != null) {
//                        val checkgender =
//                            hashGenderSpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.gender_uuid }
//                        if (checkgender) {
//                            binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(response.body()!!.responseContents!![0]!!.gender_uuid)!!)
//                        } else {
//                            binding?.qucikGender!!.setSelection(0)
//                        }
//                    }
//
//                    if (response.body()!!.responseContents!![0]!!.period_uuid != null) {
//                        val checkperiod =
//                            hashPeriodSpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.period_uuid }
//                        if (checkperiod) {
//                            binding?.qucikPeriod!!.setSelection(hashPeriodSpinnerList.get(response.body()!!.responseContents!![0]!!.period_uuid)!!)
//                        } else {
//                            binding?.qucikPeriod!!.setSelection(0)
//                        }
//                    }
//
//                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.country_uuid != null) {
//                        val checknationality =
//                            hashNationalitySpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.patient_detail!!.country_uuid }
//                        if (checknationality) {
//                            binding?.qucikCountry!!.setSelection(
//                                hashNationalitySpinnerList.get(
//                                    response.body()!!.responseContents!![0]!!.patient_detail!!.country_uuid
//                                )!!
//                            )
//                        } else {
//                            binding?.qucikCountry!!.setSelection(0)
//                        }
//                    }
//
//                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.state_uuid != null) {
//                        val checkstate =
//                            hashStateSpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.patient_detail!!.state_uuid }
//                        if (checkstate) {
//                            binding?.qucikState!!.setSelection(hashStateSpinnerList.get(response.body()!!.responseContents!![0]!!.patient_detail!!.state_uuid)!!)
//                        } else {
//                            binding?.qucikState!!.setSelection(0)
//                        }
//                    }
//                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.district_uuid != null) {
//                        val checkdistrict =
//                            hashDistrictSpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.patient_detail!!.district_uuid }
//                        if (checkdistrict) {
//                            binding?.qucikDistrict!!.setSelection(
//                                hashDistrictSpinnerList.get(
//                                    response.body()!!.responseContents!![0]!!.patient_detail!!.district_uuid
//                                )!!
//                            )
//                        } else {
//                            binding?.qucikDistrict!!.setSelection(0)
//                        }
//                    }
//                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.block_uuid != null) {
//                        val checkblockuuid =
//                            hashBlockSpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.patient_detail!!.block_uuid }
//                        if (checkblockuuid) {
//                            binding?.qucikBlock!!.setSelection(hashBlockSpinnerList.get(response.body()!!.responseContents!![0]!!.patient_detail!!.block_uuid)!!)
//                        } else {
//                            binding?.qucikBlock!!.setSelection(0)
//                        }
//                    }
//                    patientUUId = response.body()!!.responseContents!![0]!!.uuid
//
//                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.lab_to_facility_uuid != null) {
//
//                        selectLabNameID =
//                            response.body()!!.responseContents!![0]!!.patient_detail!!.lab_to_facility_uuid
//
//                        if (selectLabNameID != 0) {
//
//                            viewModel!!.getLabNameInList(
//                                selectLabNameID!!,
//                                GetLabNameListResponseCallback
//                            )
//
//                            //viewModel!!.getLocationMaster(selectLabNameID!!,LocationMasterResponseCallback)
//
//                            viewModel!!.gettolocationmapid(
//                                selectLabNameID!!,
//                                gettest.uuid,
//                                LocationMasterResponseCallback
//                            )
//
//                        }
//
//                    }
//                    if (response.body()!!.responseContents!![0]!!.uhid != null) {
//
//                        uhid = response.body()!!.responseContents!![0]!!.uhid!!
//                    }
//
//                    if (response.body()!!.responseContents!![0]!!.registered_date != "") {
//
//                        registerDate = response.body()!!.responseContents!![0]!!.registered_date!!
//                    }
//
//
//                } else {
//                    searchresponseData =
//                        response.body()?.responseContents!! as ArrayList<QuickSearchresponseContent?>
//
//                    val ft = childFragmentManager.beginTransaction()
//                    val dialog = SearchPatientDialogFragment()
//                    val bundle = Bundle()
//                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, searchresponseData)
//                    dialog.arguments = bundle
//                    dialog.show(ft, "Tag")
//                }
//
//
//            } else {
//
//                Toast.makeText(context!!, "No Record Found", Toast.LENGTH_SHORT).show()
//
//            }
//
//
//        }
//
//        override fun onBadRequest(response: Response<QuickSearchResponseModel>) {
//            val gson = GsonBuilder().create()
//            val responseModel: QuickSearchResponseModel
//            try {
//                responseModel = gson.fromJson(
//                    response.errorBody()!!.string(),
//                    QuickSearchResponseModel::class.java
//                )
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    responseModel.message!!
//                )
//            } catch (e: Exception) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//                e.printStackTrace()
//            }
//        }
//
//        override fun onServerError(response: Response<*>) {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onUnAuthorized() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.unauthorized)
//            )
//        }
//
//        override fun onForbidden() {
//            utils?.showToast(
//                R.color.negativeToast,
//                binding?.mainLayout!!,
//                getString(R.string.something_went_wrong)
//            )
//        }
//
//        override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
//        }
//
//        override fun onEverytime() {
//            viewModel!!.progress.value = 8
//        }
//    }
//
//    val updateQuickRegistrationRetrofitCallback =
//        object : RetrofitCallback<QuickRegistrationUpdateResponseModel> {
//            override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationUpdateResponseModel>?) {
//                Log.e("UpdateMSG", responseBody!!.body()?.message.toString())
//                Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()
//
//                val pdfRequestModel = PDFRequestModel()
//                pdfRequestModel.componentName = "quick"
//                pdfRequestModel.uuid = responseBody.body()?.responseContent?.uuid
//                pdfRequestModel.uhid = uhid
//                pdfRequestModel.facilityName = facility_Name
//                pdfRequestModel.firstName = responseBody.body()?.responseContent?.first_name
//                pdfRequestModel.period = CovidPeriodList[selectPeriodUuid]
//                pdfRequestModel.age = responseBody.body()?.responseContent?.age
//                pdfRequestModel.sari = sariStatus
//                pdfRequestModel.ili = iliStatus
//                pdfRequestModel.noSymptom = nosymptomsStatus
//
//                if (ispublic!!) {
//                    pdfRequestModel.testMethod = "Rapid Diagnostic Test ( Blood for Covid 19 )"
//                } else {
//                    pdfRequestModel.testMethod = "RT - PCR(Nasopharyngeal Swab for Covid 19)"
//                }
//
//                pdfRequestModel.gender = CovidGenderList[selectGenderUuid]
//                pdfRequestModel.mobile = binding!!.quickMobile.text.toString()
//                pdfRequestModel.addressDetails?.doorNum = binding!!.quickAddress.text.toString()
//                pdfRequestModel.addressDetails?.country =
//                    CovidNationalityList[selectNationalityUuid]
//                pdfRequestModel.addressDetails?.state = CovidStateList[selectStateUuid]
//                pdfRequestModel.addressDetails?.pincode = binding!!.quickPincode.text.toString()
//                pdfRequestModel.addressDetails?.district = CovidDistictList[selectDistictUuid]
//                pdfRequestModel.addressDetails?.block = CovidBlockList[selectBelongUuid]
//                pdfRequestModel.registered_date = registerDate
//
//                /*          val gson = Gson()
//                          val intent = Intent(context!!, DialogPDFViewerActivity::class.java)
//                          intent.putExtra(AppConstants.RESPONSECONTENT, gson.toJson(pdfRequestModel))
//                          intent.putExtra(AppConstants.RESPONSENEXT, 0)
//
//                          activity!!.finish()
//
//                          startActivity(intent)
//
//              */
//
//                val bundle = Bundle()
//                bundle.putParcelable(AppConstants.RESPONSECONTENT, pdfRequestModel)
//                bundle.putInt(AppConstants.RESPONSENEXT, 10)
//                bundle.putString("From", "")
//
//                val labtemplatedialog = DialogPDFViewerActivity()
//
//                labtemplatedialog.arguments = bundle
//
//                (activity as HomeActivity).replaceFragment(labtemplatedialog)
//
//
//            }
//
//            override fun onBadRequest(errorBody: Response<QuickRegistrationUpdateResponseModel>?) {
//
//                val gson = GsonBuilder().create()
//                val responseModel: QuickRegistrationUpdateResponseModel
//                try {
//                    responseModel = gson.fromJson(
//                        errorBody!!.errorBody()!!.string(),
//                        QuickRegistrationUpdateResponseModel::class.java
//                    )
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        responseModel.message!!
//                    )
//                } catch (e: Exception) {
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        getString(R.string.something_went_wrong)
//                    )
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onServerError(response: Response<*>) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onUnAuthorized() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )
//            }
//
//            override fun onForbidden() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onFailure(failure: String) {
//                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//
//        }
//
//    val updateQuicksaveorderRegistrationRetrofitCallback =
//        object : RetrofitCallback<QuickRegistrationUpdateResponseModel> {
//
//            override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationUpdateResponseModel>?) {
//                Log.e("UpdateMSG", responseBody!!.body()?.message.toString())
//
//                patientUUId = responseBody.body()!!.responseContent.uuid
//
//                viewModel!!.createEncounter(
//                    createEncounterRetrofitCallback,
//                    responseBody.body()!!.responseContent.uuid!!
//                )
//            }
//
//            override fun onBadRequest(errorBody: Response<QuickRegistrationUpdateResponseModel>?) {
//
//                val gson = GsonBuilder().create()
//                val responseModel: QuickRegistrationUpdateResponseModel
//                try {
//                    responseModel = gson.fromJson(
//                        errorBody!!.errorBody()!!.string(),
//                        QuickRegistrationUpdateResponseModel::class.java
//                    )
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        responseModel.message!!
//                    )
//                } catch (e: Exception) {
//                    utils?.showToast(
//                        R.color.negativeToast,
//                        binding?.mainLayout!!,
//                        getString(R.string.something_went_wrong)
//                    )
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onServerError(response: Response<*>) {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onUnAuthorized() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.unauthorized)
//                )
//            }
//
//            override fun onForbidden() {
//                utils?.showToast(
//                    R.color.negativeToast,
//                    binding?.mainLayout!!,
//                    getString(R.string.something_went_wrong)
//                )
//            }
//
//            override fun onFailure(failure: String) {
//                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
//            }
//
//            override fun onEverytime() {
//                viewModel!!.progress.value = 8
//            }
//
//        }
//
//
//    private fun toLabActivity() {
//
//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//
//        val dateInString = sdf.format(Date())
//
//        val pdfRequestModel = PDFRequestModel()
//        pdfRequestModel.componentName = "quick"
//        pdfRequestModel.uuid = patientUUId
//        pdfRequestModel.uhid = uhid
//        pdfRequestModel.facilityName = facility_Name
//        pdfRequestModel.firstName = binding!!.quickName.text.toString()
//        pdfRequestModel.period = CovidPeriodList[selectPeriodUuid]
//        pdfRequestModel.age = binding!!.quickAge.text.toString().toInt()
//        pdfRequestModel.registered_date = registerDate
//        pdfRequestModel.sari = sariStatus
//        pdfRequestModel.ili = iliStatus
//        pdfRequestModel.noSymptom = nosymptomsStatus
//
//        if (ispublic!!) {
//
//            pdfRequestModel.testMethod = "Rapid Diagnostic Test ( Blood for Covid 19 )"
//        } else {
//            pdfRequestModel.testMethod = "RT - PCR(Nasopharyngeal Swab for Covid 19)"
//        }
//
//        pdfRequestModel.gender = CovidGenderList[selectGenderUuid]
//        pdfRequestModel.mobile = binding!!.quickMobile.text.toString()
//        pdfRequestModel.addressDetails?.doorNum = binding!!.quickAddress.text.toString()
//        pdfRequestModel.addressDetails?.country = CovidNationalityList[selectNationalityUuid]
//        pdfRequestModel.addressDetails?.state = CovidStateList[selectStateUuid]
//        pdfRequestModel.addressDetails?.pincode = binding!!.quickPincode.text.toString()
//        pdfRequestModel.addressDetails?.district = CovidDistictList[selectDistictUuid]
//        pdfRequestModel.addressDetails?.block = CovidBlockList[selectBelongUuid]
//
///*
//        val gson = Gson()
//        val intent = Intent(context!!, DialogPDFViewerActivity::class.java)
//
//        intent.putExtra(AppConstants.RESPONSECONTENT, gson.toJson(pdfRequestModel))
//
//*/
//        val bundle = Bundle()
//
//        bundle.putParcelable(AppConstants.RESPONSECONTENT, pdfRequestModel)
//
//        bundle.putString("From", "")
//
//        if (selectLabNameID == 0) {
//
//            bundle.putInt(AppConstants.RESPONSENEXT, 0)
//
//        } else {
//
//            bundle.putInt(AppConstants.RESPONSENEXT, 1)
//
//        }
//
//        val labtemplatedialog = DialogPDFViewerActivity()
//
//        labtemplatedialog.arguments = bundle
//
//        (activity as HomeActivity).replaceFragment(labtemplatedialog)
//    }
//
//    override fun onAttachFragment(childFragment: Fragment) {
//        super.onAttachFragment(childFragment)
//
//        if (childFragment is SearchPatientDialogFragment) {
//            childFragment.setOnOrderProcessRefreshListener(this)
//        }
//
//    }
//
//    override fun onRefreshOrderList(searchData: QuickSearchresponseContent) {
//
//        updateId = 1
//        appPreferences?.saveString(AppConstants.LASTPIN, searchData.uhid)
//        binding?.lastpin?.visibility = View.VISIBLE
//        binding?.lastpinnumber?.visibility = View.VISIBLE
//        binding?.lastpinnumber?.text = searchData.uhid
//        binding?.quickName!!.setText(searchData.first_name)
//        binding?.quickAge!!.setText(searchData.age!!.toString())
//        binding?.quickMobile!!.setText(searchData.patient_detail?.mobile!!.toString())
//        binding?.quickAddress!!.setText(searchData.patient_detail.address_line1!!.toString())
//        binding?.quickPincode!!.setText(searchData.patient_detail.pincode!!.toString())
////binding?.qucikLabName!!.setText(searchData.toString())
//
//        binding?.Checkbox1!!.isChecked = searchData.patient_detail.sari!!
//        binding?.Checkbox2!!.isChecked = searchData.patient_detail.ili!!
//        binding?.Checkbox3!!.isChecked = searchData.patient_detail.no_symptom!!
//        sariStatus = searchData.patient_detail.sari
//        iliStatus = searchData.patient_detail.ili
//        nosymptomsStatus = searchData.patient_detail.no_symptom
//
//        if (searchData.patient_detail.is_rapid_test == true) {
//            binding?.radiobutton2!!.isChecked = true
//            binding?.radiobutton1!!.isChecked = false
//            ispublic = true
//
//            radioid = rtpcrID1
//
//            sampleid = NasopharyngealID1!!
//        } else {
//            binding?.radiobutton2!!.isChecked = false
//            binding?.radiobutton1!!.isChecked = true
//            ispublic = false
//
//            radioid = rtpcrID
//            sampleid = NasopharyngealID!!
//        }
//
//        patientUUId = searchData.uuid
//        if (searchData.gender_uuid != null) {
//            val checkgender = hashGenderSpinnerList.any { it.key == searchData.gender_uuid }
//            if (checkgender) {
//                binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(searchData.gender_uuid)!!)
//            } else {
//                binding?.qucikGender!!.setSelection(0)
//            }
//        }
//
//        if (searchData.period_uuid != null) {
//
//            val checkperiod = hashPeriodSpinnerList.any { it.key == searchData.period_uuid }
//
//            if (checkperiod) {
//                binding?.qucikPeriod!!.setSelection(hashPeriodSpinnerList.get(searchData.period_uuid)!!)
//            } else {
//                binding?.qucikPeriod!!.setSelection(0)
//            }
//        }
//
//        if (searchData.patient_detail.country_uuid != null) {
//            val checknationality =
//                hashNationalitySpinnerList.any { it.key == searchData.patient_detail.country_uuid }
//            if (checknationality) {
//                binding?.qucikCountry!!.setSelection(hashNationalitySpinnerList.get(searchData.patient_detail.country_uuid)!!)
//            } else {
//                binding?.qucikCountry!!.setSelection(0)
//            }
//        }
//
//        if (searchData.patient_detail.state_uuid != null) {
//            val checkstate =
//                hashStateSpinnerList.any { it.key == searchData.patient_detail.state_uuid }
//            if (checkstate) {
//                binding?.qucikState!!.setSelection(hashStateSpinnerList.get(searchData.patient_detail.state_uuid)!!)
//            } else {
//                binding?.qucikState!!.setSelection(0)
//            }
//        }
//        if (searchData.patient_detail.district_uuid != null) {
//            val checkdistrict =
//                hashDistrictSpinnerList.any { it.key == searchData.patient_detail.district_uuid }
//            if (checkdistrict) {
//                binding?.qucikDistrict!!.setSelection(hashDistrictSpinnerList.get(searchData.patient_detail.district_uuid)!!)
//            } else {
//                binding?.qucikDistrict!!.setSelection(0)
//            }
//        }
//        if (searchData.patient_detail.block_uuid != null) {
//            val checkblockuuid =
//                hashBlockSpinnerList.any { it.key == searchData.patient_detail.block_uuid }
//            if (checkblockuuid) {
//                binding?.qucikBlock!!.setSelection(hashBlockSpinnerList.get(searchData.patient_detail.block_uuid)!!)
//            } else {
//                binding?.qucikBlock!!.setSelection(0)
//            }
//        }
//
//        if (searchData.patient_detail.lab_to_facility_uuid != null) {
//
//            selectLabNameID = searchData.patient_detail.lab_to_facility_uuid
//
//            if (selectLabNameID != 0) {
//
//                viewModel!!.getLabNameInList(selectLabNameID!!, GetLabNameListResponseCallback)
//                viewModel!!.gettolocationmapid(
//                    selectLabNameID!!,
//                    gettest.uuid,
//                    LocationMasterResponseCallback
//                )
//
//            }
//        }
//        if (searchData.uhid != "") {
//
//            uhid = searchData.uhid.toString()
//        }
//
//        if (searchData.registered_date != "") {
//
//            registerDate = searchData.registered_date!!
//        }
//
//
//    }

}