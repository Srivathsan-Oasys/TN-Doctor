package com.hmis_tn.doctor.ui.dashboard.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogCovidRegistrationBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.covid.CustomTypeAdapter
import com.hmis_tn.doctor.ui.covid.SpecimanTypeAdapter
import com.hmis_tn.doctor.ui.covid.SymptomsTypeAdapter
import com.hmis_tn.doctor.ui.covid.addpatientrequest.*
import com.hmis_tn.doctor.ui.covid.addpatientresponse.AddPatientResponse
import com.hmis_tn.doctor.ui.dashboard.model.*
import com.hmis_tn.doctor.ui.dashboard.model.registration.*
import com.hmis_tn.doctor.ui.dashboard.view_model.CovidRegistrationViewModel
import com.hmis_tn.doctor.ui.dashboard.view_model.CovidRegistrationViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CovidRegistrationActivity : AppCompatActivity(),
    SearchPatientListDialogFragment.DialogListener {


    private var facility_id: Int? = 0
    var binding: DialogCovidRegistrationBinding? = null
    private var viewModel: CovidRegistrationViewModel? = null
    var appPreferences: AppPreferences? = null
    var arraydata_SymptomsTypeAdapter: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

    lateinit var mAdapter: SpecimanTypeAdapter

    lateinit var mAdapterCustomType: CustomTypeAdapter

    lateinit var mAdapterSymptoms: SymptomsTypeAdapter

    private val specimenList: MutableList<RecyclerDto> = ArrayList()
    private val clinicalList: MutableList<ClinicalSymptomsDto> = ArrayList()

    private var recyclerView: RecyclerView? = null

    private var clinicalAdapter: ClinicalSymptomsAdapter? = null
    private var medicalConditionAdapter: MedicalConditionAdapter? = null


    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var utils: Utils? = null

    private var CovidSalutationList = mutableMapOf<Int, String>()
    private var CovidGenderList = mutableMapOf<Int, String>()
    private var CovidPeriodList = mutableMapOf<Int, String>()
    private var CovidNationalityList = mutableMapOf<Int, String>()
    private var CovidMobileBelongsToList = mutableMapOf<Int, String>()
    private var CovidPatientCategoryList = mutableMapOf<Int, String>()
    private var CovidQuarantineTypeList = mutableMapOf<Int, String>()
    private var autocompleteNameTestResponse: List<CovidRegistrationSearchResponseContent>? = null
    private var Str_auto_id: Int? = 0
    private var CovidRepeatedResultList = mutableMapOf<Int, String>()
    private var CovidIntervalsList = mutableMapOf<Int, String>()

    private var A1selectSalutationUuid: Int = 0
    private var A1selectPeriodUuid: Int = 0
    private var A1selectGenderUuid: Int = 0
    private var A1selectNationalityUuid: Int = 0
    private var A1selectMobileBelongsTOUuid: Int = 0
    private var A4selectPatientCategoryUuid: Int = 0
    private var B2selectQuarantineTypeUuid: Int = 0
    private var A3selectRepeatResultUuid: Int = 0
    private var A3selectIntervalsUuid: Int = 0


    private val hashSalutationSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashPeriodSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashGenderSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashNationalitySpinnerList: HashMap<Int, Int> = HashMap()
    private val hashBelongToSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashRepeatedResultList: HashMap<Int, Int> = HashMap()
    private val hashIntervalsList: HashMap<Int, Int> = HashMap()

    private val hashPatientCatSpinnerList: HashMap<Int, Int> = HashMap()


    private var responseStateAdapter: StateSearchAdapter? = null

    private var responseDistictAdapter: DistictSearchAdapter? = null

    private var responseTalukAdapter: TalukSearchAdapter? = null

    private var responseVilliageAdapter: VillageSearchAdapter? = null


    private var responseStateAdapter1: StateSearchAdapter? = null

    private var responseDistictAdapter1: DistictSearchAdapter? = null

    private var responseTalukAdapter1: TalukSearchAdapter? = null

    private var responseVilliageAdapter1: VillageSearchAdapter? = null

    private var aTwoRequest: ATwoRequest = ATwoRequest()


    private var typeNamesList = mutableMapOf<Int, String>()

    private var requestByNamesList = mutableMapOf<Int, String>()

    var arraydata_SpecimanTypeAdapter: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

    var arraydata_CustomTypeAdapter: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null

    private var statusSave: Boolean = true

    private var uuid: Int = 0

    private var uhid: String = ""

    private var date: String = ""

    private var B1quarantineFromDate: String = ""

    private var searchresponseCovidForm: CovidRegistrationSearchResponseContent =
        CovidRegistrationSearchResponseContent()

    private var sameaddress: Boolean = false

    private var getspecimanresponse: ArrayList<Specimenlist> = ArrayList()

    private var symptomsStatus: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Utils(this).isTablet(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        binding = DataBindingUtil.setContentView(this, R.layout.dialog_covid_registration)
        viewModel = CovidRegistrationViewModelFactory(
            application

        ).create(CovidRegistrationViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this


        utils = Utils(this)
        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)

        setSpecimenAdapter()
        setClinicalSymptomsAdapter()
        setMedicalConditionAdapter()

//        prepareClinicalData()


        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        binding!!.toolbar.setNavigationOnClickListener { finish() }


        mAdapter =
            SpecimanTypeAdapter(this)
        val gridLayoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding?.specimentyperecyclerview?.layoutManager = gridLayoutManager
        binding?.specimentyperecyclerview?.adapter = mAdapter


        mAdapterCustomType =
            CustomTypeAdapter(this)
        val gridLayoutManagerCustom =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding?.medicalRecyclerview?.layoutManager = gridLayoutManagerCustom
        binding?.medicalRecyclerview?.adapter = mAdapterCustomType


        mAdapterSymptoms =
            SymptomsTypeAdapter(this)
        val gridLayoutManagerSymptoms =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding?.clinicalSymptomsRecyclerview?.layoutManager = gridLayoutManagerSymptoms
        binding?.clinicalSymptomsRecyclerview?.adapter = mAdapterSymptoms

        //RecyclerView layout manager
        val recyclerLayoutManager = LinearLayoutManager(this)
        binding?.specimentyperecyclerview!!.layoutManager = recyclerLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            binding?.specimentyperecyclerview!!.context,
            recyclerLayoutManager.orientation
        )
        binding?.specimentyperecyclerview!!.addItemDecoration(dividerItemDecoration)

        binding?.A3RepeateDateEditText!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding?.A3RepeateDateEditText!!.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.B1dateOfBirthEditText!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding?.B1dateOfBirthEditText!!.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.b5hospitiedDate!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding?.b5hospitiedDate!!.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.travelDurationFrom!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding?.travelDurationFrom!!.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.b3dateofOnset!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding?.b3dateofOnset!!.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.travelDurationTo!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding?.travelDurationTo!!.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.quarantineFromDate!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding?.quarantineFromDate!!.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )

                    B1quarantineFromDate = "" + year + "-" + String.format(
                        "%02d",
                        monthOfYear + 1
                    ) + "-" + String.format("%02d", dayOfMonth) + " 00:00:00"


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        responseStateAdapter = StateSearchAdapter(
            this,
            R.layout.row_chief_complaint_search_result,
            ArrayList()
        )

        responseDistictAdapter = DistictSearchAdapter(
            this,
            R.layout.row_chief_complaint_search_result,
            ArrayList()
        )


        responseTalukAdapter = TalukSearchAdapter(
            this,
            R.layout.row_chief_complaint_search_result,
            ArrayList()
        )

        responseVilliageAdapter = VillageSearchAdapter(
            this,
            R.layout.row_chief_complaint_search_result,
            ArrayList()
        )
        viewModel!!.getStateList(getStateRetrofitCallback)
        binding?.searchDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }

        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
        binding?.autoCompleteNameTextView?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {

                    viewModel?.getSearchNameCovidPatient(CovidSearchNameCallBack)

                }
            }
        })

        binding?.autoCompleteNameTextView!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteNameTextView?.setText(autocompleteNameTestResponse?.get(position)?.first_name)
            Str_auto_id = autocompleteNameTestResponse?.get(position)?.uuid
            Log.i("", "" + autocompleteNameTestResponse!!.get(position).first_name)


        }
        binding?.searchButton?.setOnClickListener {
            val Str_mobileNumber = binding?.autoCompleteMObileNumbTextView?.text?.trim().toString()
            val Str_Pin = binding?.autoCompletePinTextView?.text?.trim().toString()
            val Str_name = binding?.autoCompleteNameTextView?.text?.trim().toString()
            if (Str_mobileNumber.equals("") && Str_Pin.equals("") && Str_name.equals("")) {
                Toast.makeText(
                    this@CovidRegistrationActivity,
                    "Please Enter Mobile number or PIN",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val ft = supportFragmentManager.beginTransaction()
            val dialog = SearchPatientListDialogFragment()
            val bundle = Bundle()
            bundle.putString(AppConstants.SEARCHKEYMOBILE, Str_mobileNumber)
            bundle.putString(AppConstants.SEARCHKEYPIN, Str_Pin)
            bundle.putString(AppConstants.SEARCHNAME, Str_name)
            dialog.arguments = bundle
            dialog.show(ft, "Tag")


/*

            viewModel?.getSearchPinCovidPatient(CovidSearchPinCallBack)
            viewModel?.getSearchMobileNumCovidPatient(CovidSearchMobileNUmCallBack)
            val ft = supportFragmentManager.beginTransaction()
            val dialog = SearchPatientListDialogFragment()
            dialog.show(ft, "Tag")*/

        }



        binding?.salutationSpinner!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getCovidNameTitleList(
                        facility_id!!,
                        covidSalutationResponseCallback
                    )


            }

            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerGender!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getCovidGenderList(facility_id!!, covidGenderResponseCallback)

            }

            v?.onTouchEvent(event) ?: true
        }

        binding?.periodSpinner!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getCovidPeriodList(facility_id!!, covidPeriodResponseCallback)

            }

            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerNationality!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getCovidNationalityList(
                        "nationality_type",
                        facility_id!!,
                        covidNationalityResponseCallback
                    )
            }

            v?.onTouchEvent(event) ?: true
        }



        binding?.spinnerMbNoBelongs!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getCovidMobileBelongsToList(
                        "contact_type",
                        facility_id!!,
                        covidMobileBelongsToResponseCallback
                    )

            }

            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerPatientCategory!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getCovidPatientCategoryList(
                        "patient_category",
                        facility_id!!,
                        coviPatientCategoryResponseCallback
                    )

            }

            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerA3Result!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getRepeatedResult(
                        facility_id!!,
                        repeatedResultResponseCallback
                    )
            }
            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerA3Interval!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getIntervals(
                        facility_id!!,
                        IntervalsResponseCallback
                    )
            }
            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerA3Result?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A3selectRepeatResultUuid =
                        CovidRepeatedResultList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A3selectRepeatResultUuid =
                        CovidRepeatedResultList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Interval",
                        binding?.spinnerA3Result?.selectedItem.toString() + "-" + A3selectRepeatResultUuid
                    )
                }

            }

        binding?.spinnerA3Interval?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A3selectIntervalsUuid =
                        CovidIntervalsList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A3selectIntervalsUuid =
                        CovidIntervalsList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Interval",
                        binding?.spinnerA3Interval?.selectedItem.toString() + "-" + A3selectIntervalsUuid
                    )
                }

            }

        binding?.salutationSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectSalutationUuid =
                        CovidSalutationList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectSalutationUuid =
                        CovidSalutationList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Salutation",
                        binding?.salutationSpinner?.selectedItem.toString() + "-" + A1selectSalutationUuid
                    )
                }

            }

        binding?.periodSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectPeriodUuid =
                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectPeriodUuid =
                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Period",
                        binding?.periodSpinner?.selectedItem.toString() + "-" + A1selectPeriodUuid
                    )
                }

            }

        binding?.spinnerGender?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectGenderUuid =
                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectGenderUuid =
                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Gender",
                        binding?.spinnerGender?.selectedItem.toString() + "-" + A1selectGenderUuid
                    )
                }

            }

        binding?.spinnerNationality?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectNationalityUuid =
                        CovidNationalityList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (binding?.spinnerNationality?.selectedItem.toString().equals("INDIAN")) {
                        binding?.otherNationalityEditText!!.visibility = View.INVISIBLE
                        binding?.otherNationTextView!!.visibility = View.INVISIBLE
                    } else {
                        binding?.otherNationalityEditText!!.visibility = View.VISIBLE
                        binding?.otherNationTextView!!.visibility = View.VISIBLE
                    }

                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectNationalityUuid =
                        CovidNationalityList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e("NAtionality", A1selectNationalityUuid.toString())
                }

            }

        binding?.spinnerMbNoBelongs?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectMobileBelongsTOUuid =
                        CovidMobileBelongsToList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectMobileBelongsTOUuid =
                        CovidMobileBelongsToList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Mobilebelongs",
                        binding?.spinnerMbNoBelongs!!.selectedItem.toString() + "-" + A1selectMobileBelongsTOUuid
                    )
                }

            }

        binding?.spinnerPatientCategory?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A4selectPatientCategoryUuid =
                        CovidPatientCategoryList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A4selectPatientCategoryUuid =
                        CovidPatientCategoryList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "PatientCategory",
                        binding?.spinnerPatientCategory!!.selectedItem.toString() + "-" + A4selectPatientCategoryUuid
                    )
                }

            }

        binding?.quarantineTypeSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    B2selectQuarantineTypeUuid =
                        CovidQuarantineTypeList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    B2selectQuarantineTypeUuid =
                        CovidQuarantineTypeList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "PatientCategory",
                        binding?.quarantineTypeSpinner!!.selectedItem.toString() + "-" + B2selectQuarantineTypeUuid
                    )
                }

            }

        binding?.quarantineTypeSpinner!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel?.getCovidQuarantineTypeList(
                        facility_id!!,
                        covidQuarantineResponseCallback
                    )

            }
            v?.onTouchEvent(event) ?: true
        }


        binding?.outComeDate!!.setOnClickListener {


            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    binding!!.outComeDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)


                }, mYear!!, mMonth!!, mDay!!
            )
            datePickerDialog.show()

        }




        binding!!.spinnerstate.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as State?

            aTwoRequest.stateId = selectedPoi!!.uuid

            aTwoRequest.stateName = selectedPoi.name

            binding!!.spinnerstate.setText(selectedPoi.name)


            viewModel!!.getDistrict(selectedPoi.uuid, getDistictRetrofitCallback)

        }


        binding!!.districtSpinner.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as District?

            aTwoRequest.distictId = selectedPoi!!.uuid

            aTwoRequest.distictName = selectedPoi.name

            binding!!.districtSpinner.setText(selectedPoi.name)

            viewModel!!.getTaluk(selectedPoi.uuid, getTalukRetrofitCallback)

        }

        binding!!.talukSpinner.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as Taluk?

            aTwoRequest.talukId = selectedPoi!!.uuid
            aTwoRequest.talukName = selectedPoi.name

            binding!!.talukSpinner.setText(selectedPoi.name)

            viewModel!!.getVillage(selectedPoi.uuid, getVillageRetrofitCallback)

        }


        binding?.saveCardView?.setOnClickListener {


            /*A1 Validation*/
            if (!binding?.A1NameEditText!!.text.trim().toString().isEmpty()) {
                if (!binding?.A1AgeEditText!!.text.trim().toString().isEmpty()) {
                    if (!binding?.A1MobileEditText!!.text.toString().isEmpty()) {
                        if (binding?.A1MobileEditText!!.text.trim().toString().length == 10) {
                            if (A1selectSalutationUuid != 0) {
                                if (A1selectPeriodUuid != 0) {
                                    if (A1selectGenderUuid != 0) {
                                        if (A1selectNationalityUuid != 0) {
                                            if (A1selectMobileBelongsTOUuid != 0) {

                                                /*A4 Validation*/
                                                if (A4selectPatientCategoryUuid != 0) {

                                                    if (isEmailValid(
                                                            binding?.B1emailEditText!!.text.trim()
                                                                .toString()
                                                        )
                                                    ) {

                                                        if (!binding?.addressOne!!.text.trim()
                                                                .toString().isNullOrEmpty()
                                                        ) {


                                                            if (!binding?.addressTwo!!.text.trim()
                                                                    .toString().isNullOrEmpty()
                                                            ) {

                                                                if (!binding?.spinnerstate!!.text.trim()
                                                                        .toString().isNullOrEmpty()
                                                                ) {

                                                                    if (!binding?.districtSpinner!!.text.trim()
                                                                            .toString()
                                                                            .isNullOrEmpty()
                                                                    ) {

                                                                        if (!binding?.talukSpinner!!.text.trim()
                                                                                .toString()
                                                                                .isNullOrEmpty()
                                                                        ) {

                                                                            if (!binding?.pincode1!!.text.trim()
                                                                                    .toString()
                                                                                    .isNullOrEmpty()
                                                                            ) {

                                                                                if (statusSave) {

                                                                                    val covidAddPatientDetails: AddPatientDetailsRequest =
                                                                                        AddPatientDetailsRequest()
                                                                                    /*A1*/
                                                                                    covidAddPatientDetails.first_name =
                                                                                        binding?.A1NameEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.age =
                                                                                        (binding?.A1AgeEditText!!.text.trim()
                                                                                            .toString()).toInt()
                                                                                    covidAddPatientDetails.mobile =
                                                                                        binding?.A1MobileEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.alternate_number =
                                                                                        binding?.A1AlterNumberEditText!!.text.toString()
                                                                                    covidAddPatientDetails.title_uuid =
                                                                                        A1selectSalutationUuid
                                                                                    covidAddPatientDetails.period_uuid =
                                                                                        A1selectPeriodUuid
                                                                                    covidAddPatientDetails.gender_uuid =
                                                                                        A1selectGenderUuid
                                                                                    covidAddPatientDetails.nationality_type_uuid =
                                                                                        A1selectNationalityUuid


                                                                                    /*A4*/
                                                                                    covidAddPatientDetails.covid_patient_details?.patient_category_uuid =
                                                                                        A4selectPatientCategoryUuid

                                                                                    /*B1*/
                                                                                    covidAddPatientDetails.email =
                                                                                        binding?.B1emailEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.aadhaar_number =
                                                                                        binding?.B1aadharNumberEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.covid_patient_details?.passport_number =
                                                                                        binding?.B1passportNumberEditText!!.text.trim()
                                                                                            .toString()

                                                                                    /*B2*/

                                                                                    covidAddPatientDetails.airport_name =
                                                                                        binding?.B2airPortNameEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.quarentine_status_type_uuid =
                                                                                        B2selectQuarantineTypeUuid.toString()
                                                                                    covidAddPatientDetails.quarantine_status_date =
                                                                                        B1quarantineFromDate


                                                                                    arraydata_SymptomsTypeAdapter =
                                                                                        mAdapterSymptoms.getData()

                                                                                    arraydata_SpecimanTypeAdapter =
                                                                                        mAdapter.getData()

                                                                                    arraydata_CustomTypeAdapter =
                                                                                        mAdapterCustomType.getData()


                                                                                    var symptomsList: ArrayList<PatientSymptom?>? =
                                                                                        ArrayList()

                                                                                    symptomsList!!.clear()

                                                                                    for (i in arraydata_SymptomsTypeAdapter!!.indices) {

                                                                                        symptomsList[i]?.duration =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.othercommands.toInt()
                                                                                        symptomsList[i]?.is_active =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.isSelect
                                                                                        symptomsList[i]?.symptom_uuid =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.uuid


                                                                                    }

                                                                                    var customList: ArrayList<PatientConditionDetail?>? =
                                                                                        ArrayList()

                                                                                    customList!!.clear()

                                                                                    for (i in arraydata_SymptomsTypeAdapter!!.indices) {

                                                                                        customList[i]?.is_active =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.isSelect
                                                                                        customList[i]?.condition_uuid =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.uuid


                                                                                    }


                                                                                    var specimanopList: ArrayList<PatientSpecimenDetail?>? =
                                                                                        ArrayList()

                                                                                    specimanopList!!.clear()

                                                                                    for (i in arraydata_SymptomsTypeAdapter!!.indices) {

                                                                                        specimanopList[i]?.is_active =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.isSelect
                                                                                        specimanopList[i]?.collection_date =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.date
                                                                                        specimanopList[i]?.label =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.othercommands
                                                                                        specimanopList[i]?.specimen_type_uuid =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.uuid

                                                                                    }

                                                                                    covidAddPatientDetails.patient_symptoms =
                                                                                        symptomsList

                                                                                    covidAddPatientDetails.registred_facility_uuid =
                                                                                        facility_id.toString()

                                                                                    covidAddPatientDetails.department_uuid =
                                                                                        appPreferences?.getInt(
                                                                                            AppConstants.DEPARTMENT_UUID
                                                                                        ).toString()

                                                                                    covidAddPatientDetails.patient_specimen_details =
                                                                                        specimanopList

                                                                                    covidAddPatientDetails.patient_condition_details =
                                                                                        customList

                                                                                    //a2 serious data

                                                                                    covidAddPatientDetails.address_line1 =
                                                                                        aTwoRequest.Address1

                                                                                    covidAddPatientDetails.address_line2 =
                                                                                        aTwoRequest.Address2

                                                                                    covidAddPatientDetails.district_uuid =
                                                                                        aTwoRequest.distictId

                                                                                    covidAddPatientDetails.state_uuid =
                                                                                        aTwoRequest.stateId

                                                                                    covidAddPatientDetails.taluk_uuid =
                                                                                        aTwoRequest.talukId

                                                                                    covidAddPatientDetails.village_uuid =
                                                                                        aTwoRequest.villageId

                                                                                    covidAddPatientDetails.pincode =
                                                                                        aTwoRequest.pincode


                                                                                    covidAddPatientDetails.covid_patient_details!!.address_line_1 =
                                                                                        aTwoRequest.Address1

                                                                                    covidAddPatientDetails.covid_patient_details!!.address_line_2 =
                                                                                        aTwoRequest.Address2

                                                                                    covidAddPatientDetails.covid_patient_details!!.facility_pincode =
                                                                                        aTwoRequest.pincode

                                                                                    covidAddPatientDetails.covid_patient_details!!.district_uuid =
                                                                                        aTwoRequest.distictId

                                                                                    covidAddPatientDetails.covid_patient_details!!.taluk_uuid =
                                                                                        aTwoRequest.talukId

                                                                                    covidAddPatientDetails.covid_patient_details!!.village_uuid =
                                                                                        aTwoRequest.villageId

                                                                                    covidAddPatientDetails.covid_patient_details!!.state_uuid =
                                                                                        aTwoRequest.stateId

                                                                                    covidAddPatientDetails.covid_patient_details!!.pincode =
                                                                                        aTwoRequest.pincode


                                                                                    covidAddPatientDetails.covid_patient_details!!.hospitialized_date =
                                                                                        binding!!.b5hospitiedDate.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.diagnosis =
                                                                                        binding!!.b5diagnosis.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.differential_diagnosis =
                                                                                        binding!!.b5differentialdiagnosis.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.etiology_identified =
                                                                                        binding!!.b5etiologyidentified.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.hospital_phone_number =
                                                                                        binding!!.b5Hospitalno.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.hospital_name_address =
                                                                                        binding!!.b5hospitalName.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.institution_name =
                                                                                        binding!!.b5instutionName.text.trim()
                                                                                            .toString()


                                                                                    covidAddPatientDetails.covid_patient_details!!.country_uuid =
                                                                                        A1selectNationalityUuid


                                                                                    covidAddPatientDetails.covid_patient_details!!.same_address =
                                                                                        sameaddress

                                                                                    covidAddPatientDetails.symptoms =
                                                                                        symptomsStatus

                                                                                    viewModel?.addpatientDetails(
                                                                                        facility_id,
                                                                                        covidAddPatientDetails,
                                                                                        getAddPatientDetailsRetrofitCallBack
                                                                                    )
                                                                                } else {


                                                                                    val covidAddPatientDetails =
                                                                                        UpdatePatientDetailsRequest()
                                                                                    /*A1*/
                                                                                    covidAddPatientDetails.first_name =
                                                                                        binding?.A1NameEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.age =
                                                                                        (binding?.A1AgeEditText!!.text.trim()
                                                                                            .toString()).toInt()
                                                                                    covidAddPatientDetails.mobile =
                                                                                        binding?.A1MobileEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.alternate_number =
                                                                                        binding?.A1AlterNumberEditText!!.text.toString()
                                                                                    covidAddPatientDetails.title_uuid =
                                                                                        A1selectSalutationUuid
                                                                                    covidAddPatientDetails.period_uuid =
                                                                                        A1selectPeriodUuid
                                                                                    covidAddPatientDetails.gender_uuid =
                                                                                        A1selectGenderUuid
                                                                                    covidAddPatientDetails.nationality_type_uuid =
                                                                                        A1selectNationalityUuid

                                                                                    /*A4*/
                                                                                    covidAddPatientDetails.covid_patient_details?.patient_category_uuid =
                                                                                        A4selectPatientCategoryUuid

                                                                                    /*B1*/
                                                                                    covidAddPatientDetails.email =
                                                                                        binding?.B1emailEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.aadhaar_number =
                                                                                        binding?.B1aadharNumberEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.covid_patient_details?.passport_number =
                                                                                        binding?.B1passportNumberEditText!!.text.trim()
                                                                                            .toString()

                                                                                    /*B2*/

                                                                                    covidAddPatientDetails.airport_name =
                                                                                        binding?.B2airPortNameEditText!!.text.trim()
                                                                                            .toString()
                                                                                    covidAddPatientDetails.quarentine_status_type_uuid =
                                                                                        B2selectQuarantineTypeUuid.toString()
                                                                                    covidAddPatientDetails.quarantine_status_date =
                                                                                        binding?.quarantineFromDate!!.text.trim()
                                                                                            .toString()


                                                                                    arraydata_SymptomsTypeAdapter =
                                                                                        mAdapterSymptoms.getData()

                                                                                    arraydata_SpecimanTypeAdapter =
                                                                                        mAdapter.getData()

                                                                                    arraydata_CustomTypeAdapter =
                                                                                        mAdapterCustomType.getData()


                                                                                    var symptomsList: ArrayList<PatientSymptom?>? =
                                                                                        ArrayList()

                                                                                    symptomsList!!.clear()

                                                                                    for (i in arraydata_SymptomsTypeAdapter!!.indices) {

                                                                                        symptomsList[i]?.duration =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.othercommands.toInt()
                                                                                        symptomsList[i]?.is_active =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.isSelect
                                                                                        symptomsList[i]?.symptom_uuid =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.uuid


                                                                                    }

                                                                                    var customList: ArrayList<PatientConditionDetail?>? =
                                                                                        ArrayList()

                                                                                    customList!!.clear()

                                                                                    for (i in arraydata_SymptomsTypeAdapter!!.indices) {

                                                                                        customList[i]?.is_active =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.isSelect
                                                                                        customList[i]?.condition_uuid =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.uuid


                                                                                    }


                                                                                    var specimanopList: ArrayList<PatientSpecimenDetail?>? =
                                                                                        ArrayList()

                                                                                    specimanopList!!.clear()

                                                                                    for (i in arraydata_SymptomsTypeAdapter!!.indices) {

                                                                                        specimanopList[i]?.is_active =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.isSelect
                                                                                        specimanopList[i]?.collection_date =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.date
                                                                                        specimanopList[i]?.label =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.othercommands
                                                                                        specimanopList[i]?.specimen_type_uuid =
                                                                                            arraydata_SymptomsTypeAdapter!![i]!!.uuid

                                                                                    }

                                                                                    covidAddPatientDetails.patient_symptoms =
                                                                                        symptomsList

                                                                                    covidAddPatientDetails.registred_facility_uuid =
                                                                                        facility_id.toString()

                                                                                    covidAddPatientDetails.department_uuid =
                                                                                        appPreferences?.getInt(
                                                                                            AppConstants.DEPARTMENT_UUID
                                                                                        ).toString()

                                                                                    covidAddPatientDetails.patient_specimen_details =
                                                                                        specimanopList

                                                                                    covidAddPatientDetails.patient_condition_details =
                                                                                        customList

                                                                                    //a2 serious data

                                                                                    covidAddPatientDetails.address_line1 =
                                                                                        aTwoRequest.Address1

                                                                                    covidAddPatientDetails.address_line2 =
                                                                                        aTwoRequest.Address2

                                                                                    covidAddPatientDetails.district_uuid =
                                                                                        aTwoRequest.distictId

                                                                                    covidAddPatientDetails.state_uuid =
                                                                                        aTwoRequest.stateId

                                                                                    covidAddPatientDetails.taluk_uuid =
                                                                                        aTwoRequest.talukId

                                                                                    covidAddPatientDetails.village_uuid =
                                                                                        aTwoRequest.villageId

                                                                                    covidAddPatientDetails.pincode =
                                                                                        aTwoRequest.pincode

                                                                                    covidAddPatientDetails.covid_patient_details!!.address_line_1 =
                                                                                        aTwoRequest.Address1

                                                                                    covidAddPatientDetails.covid_patient_details!!.address_line_2 =
                                                                                        aTwoRequest.Address2

                                                                                    covidAddPatientDetails.covid_patient_details!!.facility_pincode =
                                                                                        aTwoRequest.pincode

                                                                                    covidAddPatientDetails.covid_patient_details!!.district_uuid =
                                                                                        aTwoRequest.distictId

                                                                                    covidAddPatientDetails.covid_patient_details!!.taluk_uuid =
                                                                                        aTwoRequest.talukId

                                                                                    covidAddPatientDetails.covid_patient_details!!.village_uuid =
                                                                                        aTwoRequest.villageId

                                                                                    covidAddPatientDetails.covid_patient_details!!.state_uuid =
                                                                                        aTwoRequest.stateId

                                                                                    covidAddPatientDetails.covid_patient_details!!.pincode =
                                                                                        aTwoRequest.pincode

                                                                                    covidAddPatientDetails.uuid =
                                                                                        uuid

                                                                                    covidAddPatientDetails.uhid =
                                                                                        uhid

                                                                                    covidAddPatientDetails.quarantine_status_date =
                                                                                        "2020-04-21 00:00:00"

                                                                                    covidAddPatientDetails.isWeb =
                                                                                        true

                                                                                    covidAddPatientDetails.repeat_test_date =
                                                                                        "2020-04-21 00:00:00"

                                                                                    covidAddPatientDetails.covid_patient_details!!.hospitialized_date =
                                                                                        binding!!.b5hospitiedDate.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.diagnosis =
                                                                                        binding!!.b5diagnosis.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.differential_diagnosis =
                                                                                        binding!!.b5differentialdiagnosis.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.etiology_identified =
                                                                                        binding!!.b5etiologyidentified.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.hospital_phone_number =
                                                                                        binding!!.b5Hospitalno.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.hospital_name_address =
                                                                                        binding!!.b5hospitalName.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.institution_name =
                                                                                        binding!!.b5instutionName.text.trim()
                                                                                            .toString()

                                                                                    covidAddPatientDetails.covid_patient_details!!.country_uuid =
                                                                                        A1selectNationalityUuid

                                                                                    covidAddPatientDetails.covid_patient_details!!.same_address =
                                                                                        sameaddress

                                                                                    covidAddPatientDetails.symptoms =
                                                                                        symptomsStatus

                                                                                    viewModel?.updatepatientDetails(
                                                                                        facility_id,
                                                                                        covidAddPatientDetails,
                                                                                        getUpdateatientDetailsRetrofitCallBack
                                                                                    )

                                                                                }

                                                                            } else {

                                                                                utils?.showToast(
                                                                                    R.color.negativeToast,
                                                                                    binding?.mainLayout!!,
                                                                                    "Enter valid PinCode in A2 field"
                                                                                )

                                                                                binding?.pincode1!!.error =
                                                                                    "Enter valid PinCode"

                                                                            }

                                                                        } else {

                                                                            utils?.showToast(
                                                                                R.color.negativeToast,
                                                                                binding?.mainLayout!!,
                                                                                "Enter valid Taluk in A2 field"
                                                                            )

                                                                            binding?.talukSpinner!!.error =
                                                                                "Enter valid Taluk"
                                                                        }


                                                                    } else {

                                                                        utils?.showToast(
                                                                            R.color.negativeToast,
                                                                            binding?.mainLayout!!,
                                                                            "Enter valid District in A2 field"
                                                                        )

                                                                        binding?.districtSpinner!!.error =
                                                                            "Enter valid District"
                                                                    }


                                                                } else {

                                                                    utils?.showToast(
                                                                        R.color.negativeToast,
                                                                        binding?.mainLayout!!,
                                                                        "Enter valid State in A2 field"
                                                                    )

                                                                    binding?.spinnerstate!!.error =
                                                                        "Enter valid State"

                                                                }
                                                            } else {

                                                                utils?.showToast(
                                                                    R.color.negativeToast,
                                                                    binding?.mainLayout!!,
                                                                    "Enter valid Address2 in A2 field"
                                                                )

                                                                binding?.addressTwo!!.error =
                                                                    "Enter valid Address"
                                                            }


                                                        } else {

                                                            utils?.showToast(
                                                                R.color.negativeToast,
                                                                binding?.mainLayout!!,
                                                                "Enter valid Address in A2 field"
                                                            )
                                                            binding?.addressOne!!.error =
                                                                "Enter valid Address"

                                                        }

                                                    } else {
                                                        utils?.showToast(
                                                            R.color.negativeToast,
                                                            binding?.mainLayout!!,
                                                            "Enter valid e-mail in B1 field"
                                                        )

                                                        binding?.B1emailEditText!!.error =
                                                            "Enter valid e-mail"
                                                    }


                                                } else {
                                                    utils?.showToast(
                                                        R.color.negativeToast,
                                                        binding?.mainLayout!!,
                                                        "Select the patient category in A4 field"
                                                    )
                                                }

                                            } else {
                                                utils?.showToast(
                                                    R.color.negativeToast,
                                                    binding?.mainLayout!!,
                                                    "Select the mobile number belongs to in A1 field"
                                                )
                                            }
                                        } else {
                                            utils?.showToast(
                                                R.color.negativeToast,
                                                binding?.mainLayout!!,
                                                "Select the nationality in A1 field"
                                            )
                                        }
                                    } else {
                                        utils?.showToast(
                                            R.color.negativeToast,
                                            binding?.mainLayout!!,
                                            "Select the gender in A1 field"
                                        )
                                    }
                                } else {
                                    utils?.showToast(
                                        R.color.negativeToast,
                                        binding?.mainLayout!!,
                                        "Select the period in A1 field"
                                    )
                                }

                            } else {
                                utils?.showToast(
                                    R.color.negativeToast,
                                    binding?.mainLayout!!,
                                    "Select the salutation in A1 field"
                                )
                            }

                        } else {
                            utils?.showToast(
                                R.color.negativeToast,
                                binding?.mainLayout!!,
                                "Enter valid 10 - digit mobile number in A1 field"
                            )
                            binding?.A1MobileEditText!!.error =
                                "Enter valid 10 - digit mobile number"
                        }
                    } else
                        utils?.showToast(
                            R.color.negativeToast,
                            binding?.mainLayout!!,
                            "Enter the mobile number in A1 field"
                        )
                    binding?.A1MobileEditText!!.error = "Enter the mobile number"
                } else
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Enter the age in A1 field"
                    )
                binding?.A1AgeEditText!!.error = "Enter the age"
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Enter the name in A1 field"
                )
                binding?.A1NameEditText!!.error = "Enter the name"
            }


        }

        binding?.clear?.setOnClickListener {

            binding?.autoCompleteExistsPinTextView!!.setText("")

            binding?.autoCompletePDSTextView!!.setText("")

            binding?.autoCompletePinTextView!!.setText("")

            binding?.autoCompleteMObileNumbTextView!!.setText("")

            binding?.autoCompleteNameTextView!!.setText("")

        }

        binding!!.clearCardView.setOnClickListener {

            finish()
            startActivity(intent)


        }

        binding!!.autoCompleteMObileNumbTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 10) {

                    binding!!.autoCompleteMObileNumbTextView.error = null
                } else {
                    binding!!.autoCompleteMObileNumbTextView.error =
                        "Mobile Number Must be 10 digit"
                }

            }
        })
    }

    private fun setSpecimenAdapter() {

        viewModel?.getSpecimanType(facility_id, getSpecimenListRetrofitCallBack)

        binding?.a3ParentLayout?.setOnClickListener {

            if (binding?.a3ResultLayout?.visibility == View.VISIBLE) {
                binding?.a3ResultLayout?.visibility = View.GONE

            } else {

                binding?.a3ResultLayout?.visibility = View.VISIBLE
            }
        }
        binding?.a1ParentLayout?.setOnClickListener {

            if (binding?.a1Resultlayout?.visibility == View.VISIBLE) {
                binding?.a1Resultlayout?.visibility = View.GONE

            } else {
                binding?.a1Resultlayout?.visibility = View.VISIBLE
            }
        }
        binding?.a2ParentLayout?.setOnClickListener {

            if (binding?.a2Resultlayout?.visibility == View.VISIBLE) {
                binding?.a2Resultlayout?.visibility = View.GONE

            } else {
                binding?.a2Resultlayout?.visibility = View.VISIBLE

                responseStateAdapter = StateSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )

                responseDistictAdapter = DistictSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )


                responseTalukAdapter = TalukSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )

                responseVilliageAdapter = VillageSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )


                responseStateAdapter1 = StateSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )

                responseDistictAdapter1 = DistictSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )


                responseTalukAdapter1 = TalukSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )

                responseVilliageAdapter1 = VillageSearchAdapter(
                    this,
                    R.layout.row_chief_complaint_search_result,
                    ArrayList()
                )

                viewModel!!.getStateList(getStateRetrofitCallback)

                binding!!.dateCheckbox.setOnClickListener(View.OnClickListener {
                    if (binding!!.dateCheckbox.isChecked) {

                        sameaddress = true

                        binding!!.presentAddressOne.setText(aTwoRequest.Address1)
                        binding!!.presentAddressTwo.setText(aTwoRequest.Address2)
                        binding!!.stateSpinner1.setText(aTwoRequest.stateName)
                        binding!!.districtSpinner1.setText(aTwoRequest.distictName)
                        binding!!.talukSpinner1.setText(aTwoRequest.talukName)
                        binding!!.villageSpinner1.setText(aTwoRequest.villageName)
                        binding!!.pincode2.setText(aTwoRequest.pincode)

                        binding!!.presentAddressOne.isEnabled = false
                        binding!!.presentAddressTwo.isEnabled = false
                        binding!!.stateSpinner1.isEnabled = false
                        binding!!.districtSpinner1.isEnabled = false
                        binding!!.talukSpinner1.isEnabled = false
                        binding!!.villageSpinner1.isEnabled = false
                        binding!!.pincode2.isEnabled = false

                        aTwoRequest.Present_stateId = aTwoRequest.stateId
                        aTwoRequest.Present_distictId = aTwoRequest.distictId
                        aTwoRequest.Present_talukId = aTwoRequest.talukId
                        aTwoRequest.Present_villageId = aTwoRequest.villageId

                    } else {

                        sameaddress = false

                        binding!!.presentAddressOne.setText("")
                        binding!!.presentAddressTwo.setText("")
                        binding!!.stateSpinner1.setText("")
                        binding!!.districtSpinner1.setText("")
                        binding!!.talukSpinner1.setText("")
                        binding!!.villageSpinner1.setText("")
                        binding!!.pincode2.setText("")

                        binding!!.presentAddressOne.isEnabled = true
                        binding!!.presentAddressTwo.isEnabled = true
                        binding!!.stateSpinner1.isEnabled = true
                        binding!!.districtSpinner1.isEnabled = true
                        binding!!.talukSpinner1.isEnabled = true
                        binding!!.villageSpinner1.isEnabled = true
                        binding!!.pincode2.isEnabled = true

                        aTwoRequest.Present_stateId = 0
                        aTwoRequest.Present_distictId = 0
                        aTwoRequest.Present_talukId = 0
                        aTwoRequest.Present_villageId = 0

                    }
                })

            }
        }
        binding?.a4ParentLayout?.setOnClickListener {

            if (binding?.a4ResultLayout?.visibility == View.VISIBLE) {
                binding?.a4ResultLayout?.visibility = View.GONE

            } else {
                binding?.a4ResultLayout?.visibility = View.VISIBLE
            }
        }
        binding?.b1ParentLayout?.setOnClickListener {

            if (binding?.b1ResultLayout?.visibility == View.VISIBLE) {
                binding?.b1ResultLayout?.visibility = View.GONE

            } else {
                binding?.b1ResultLayout?.visibility = View.VISIBLE
            }
        }
        binding?.b2ParentLayout?.setOnClickListener {

            if (binding?.b2ResultLayout?.visibility == View.VISIBLE) {
                binding?.b2ResultLayout?.visibility = View.GONE

            } else {
                binding?.b2ResultLayout?.visibility = View.VISIBLE
            }
        }
        binding?.b3ParentLayout?.setOnClickListener {

            if (binding?.b3ResultLayout?.visibility == View.VISIBLE) {
                binding?.b3ResultLayout?.visibility = View.GONE

                binding!!.clinicalSymptomsRecyclerview.visibility = View.GONE


            } else {
                binding?.b3ResultLayout?.visibility = View.VISIBLE


                binding?.switchSymptoms!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    // do something, the isChecked will be
                    // true if the switch is in the On position
                    if (isChecked) {

                        symptomsStatus = true
                        binding!!.clinicalSymptomsRecyclerview.visibility = View.VISIBLE
                        viewModel?.getSymptoms(facility_id, getSymptomsListRetrofitCallBack)
                    } else {
                        symptomsStatus = false


                        binding!!.clinicalSymptomsRecyclerview.visibility = View.GONE

                    }
                })


            }
        }
        binding?.b4ParentLayout?.setOnClickListener {

            if (binding?.b4ResultLayout?.visibility == View.VISIBLE) {
                binding?.b4ResultLayout?.visibility = View.GONE

            } else {
                binding?.b4ResultLayout?.visibility = View.VISIBLE

                viewModel?.getConditionType(facility_id, getcustomTypeListRetrofitCallBack)
            }
        }
        binding?.b5ParentLayout?.setOnClickListener {

            if (binding?.b5ResultLayout?.visibility == View.VISIBLE) {
                binding?.b5ResultLayout?.visibility = View.GONE

            } else {
                binding?.b5ResultLayout?.visibility = View.VISIBLE

                viewModel!!.getOutcome(getOutComeListRetrofitCallBack)

                viewModel!!.getTestRequestedBy(getRequestedByListRetrofitCallBack)

                binding!!.requestBySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            val itemValue = parent?.getItemAtPosition(0).toString()
                            val seletOutCome =
                                requestByNamesList.filterValues { it == itemValue }.keys.toList()[0]
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                        ) {
                            val itemValue = parent?.getItemAtPosition(pos).toString()

                            val seletOutCome =
                                requestByNamesList.filterValues { it == itemValue }.keys.toList()[0]

                            Log.i(
                                "LabType",
                                "name = " + itemValue + "uuid=" + requestByNamesList.filterValues { it == itemValue }.keys.toList()[0]
                            )
                        }

                    }

                binding!!.outcomeSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                            val itemValue = parent?.getItemAtPosition(0).toString()

                            val seletOutCome =
                                typeNamesList.filterValues { it == itemValue }.keys.toList()[0]
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                        ) {
                            val itemValue = parent?.getItemAtPosition(pos).toString()

                            val seletOutCome =
                                typeNamesList.filterValues { it == itemValue }.keys.toList()[0]

                            Log.i(
                                "LabType",
                                "name = " + itemValue + "uuid=" + typeNamesList.filterValues { it == itemValue }.keys.toList()[0]
                            )
                        }

                    }

            }
        }
        binding!!.switchCheck.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                binding!!.repeatedSampleHideLayout.visibility = View.VISIBLE
            } else {
                binding!!.repeatedSampleHideLayout.visibility = View.GONE
            }
        }

        binding!!.switchTravelHistory.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding?.travelHistoryLayout!!.visibility = View.VISIBLE
            } else {
                binding?.travelHistoryLayout!!.visibility = View.GONE
            }
        }

        binding!!.switchContactHistory.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding?.contactHistoryLayout!!.visibility = View.VISIBLE
            } else {
                binding?.contactHistoryLayout!!.visibility = View.GONE
            }
        }

        binding!!.switchQuarantineStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding?.quarantineStatus!!.visibility = View.VISIBLE
            } else {
                binding?.quarantineStatus!!.visibility = View.GONE
            }

        }

        binding!!.addressOne.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                aTwoRequest.Address1 = s.toString()
            }
        })

        binding!!.addressTwo.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                aTwoRequest.Address2 = s.toString()
            }
        })



        binding!!.presentAddressOne.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                aTwoRequest.Present_Address1 = s.toString()
            }
        })

        binding!!.presentAddressTwo.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                aTwoRequest.Present_Address2 = s.toString()
            }
        })


        binding!!.pincode1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length
                if (datasize == 6) {
                    aTwoRequest.pincode = s.toString()
                } else {
                    binding!!.pincode1.error = "Pin code Must be 6 digit"
                }
            }
        })

        binding!!.pincode2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 6) {
                    aTwoRequest.Present_pincode = s.toString()
                } else {
                    binding!!.pincode2.error = "Pin code Must be 6 digit"
                }

            }
        })

        binding!!.A1MobileEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 10) {

                    binding!!.A1MobileEditText.error = null
                } else {
                    binding!!.A1MobileEditText.error = "Mobile Number Must be 10 digit"
                }

            }
        })



        binding!!.A1AlterNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 10) {

                    binding!!.A1AlterNumberEditText.error = null
                } else {
                    binding!!.A1AlterNumberEditText.error = "Mobile Number Must be 10 digit"
                }

            }
        })


        binding!!.A3FacilityPinCode.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length
                if (datasize == 6) {
                    binding!!.A3FacilityPinCode.error = null
                } else {
                    binding!!.A3FacilityPinCode.error = "Pin code Must be 6 digit"
                }
            }
        })


    }
    /*
       ADd Patient Details
     */

    /*
 */
    val getAddPatientDetailsRetrofitCallBack =
        object : RetrofitCallback<AddPatientResponse> {
            override fun onSuccessfulResponse(response: Response<AddPatientResponse>) {
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)

                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Saved Successfully"
                )

                finish()
                startActivity(intent)

            }

            override fun onBadRequest(response: Response<AddPatientResponse>) {
                val gson = GsonBuilder().create()
                val responseModel: ResponseSpicemanType
                try {
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
//                viewModel!!.progress!!.value = 8
            }
        }


    val getUpdateatientDetailsRetrofitCallBack =
        object : RetrofitCallback<AddPatientResponse> {
            override fun onSuccessfulResponse(response: Response<AddPatientResponse>) {
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)
                Log.i("", "" + response.body()?.statusCode)

                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Update Successfully"
                )
                binding?.saveTextview?.text = "Save"
                statusSave = true
                uuid = 0
                uhid = ""

                finish()
                startActivity(intent)

            }

            override fun onBadRequest(response: Response<AddPatientResponse>) {
                val gson = GsonBuilder().create()
                val responseModel: ResponseSpicemanType
                try {
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
//                viewModel!!.progress!!.value = 8
            }
        }

    /*
     */
    val getSpecimenListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
            val responseContents = Gson().toJson(response.body()?.responseContents)
            Log.i("", "" + responseContents)
            mAdapter.setdata(response.body()?.responseContents)
        }

        override fun onBadRequest(response: Response<ResponseSpicemanType>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSpicemanType
            try {
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
//                viewModel!!.progress!!.value = 8
        }
    }

    private fun setClinicalSymptomsAdapter() {
        clinicalAdapter = ClinicalSymptomsAdapter(applicationContext, clinicalList)
        binding?.clinicalSymptomsRecyclerview?.adapter = clinicalAdapter
    }

    private fun setMedicalConditionAdapter() {
        medicalConditionAdapter = MedicalConditionAdapter(applicationContext, specimenList)
        binding?.medicalRecyclerview?.adapter = medicalConditionAdapter
    }

    val getcustomTypeListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
            val responseContents = Gson().toJson(response.body()?.responseContents)
            Log.i("", "" + responseContents)
            mAdapterCustomType.setdata(response.body()?.responseContents)
        }

        override fun onBadRequest(response: Response<ResponseSpicemanType>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSpicemanType
            try {
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
//                viewModel!!.progress!!.value = 8
        }


    }


    val getSymptomsListRetrofitCallBack =
        object : RetrofitCallback<ResponseSpicemanType> {
            override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)
                Log.i("", "" + responseContents)
                mAdapterSymptoms.setdata(response.body()?.responseContents)
            }

            override fun onBadRequest(response: Response<ResponseSpicemanType>) {
                val gson = GsonBuilder().create()
                val responseModel: ResponseSpicemanType
                try {
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
//                viewModel!!.progress!!.value = 8
            }


        }


    /*private fun prepareClinicalData() {
        var name = ClinicalSymptomsDto("Cough","6")
        clinicalList.add(name)
        name = ClinicalSymptomsDto("Cough","7")
        clinicalList.add(name)
        name = ClinicalSymptomsDto("Cough","7")
        clinicalList.add(name)
        name = ClinicalSymptomsDto("Cough","7")
        clinicalList.add(name)
        mAdapter!!.notifyDataSetChanged()
    }*/


    val getStateRetrofitCallback = object : RetrofitCallback<StateListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<StateListResponseModel>?) {

            try {
                responseStateAdapter!!.setData(responseBody!!.body()!!.responseContents)

                responseStateAdapter1!!.setData(responseBody.body()!!.responseContents)

                stateAdapter()

                stateAdapter2()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onBadRequest(errorBody: Response<StateListResponseModel>?) {

            val gson = GsonBuilder().create()
            val responseModel: StateListResponseModel
            try {
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


    private fun stateAdapter() {

        binding!!.spinnerstate.threshold = 1
        binding!!.spinnerstate.setAdapter(responseStateAdapter)
        binding!!.spinnerstate.showDropDown()


    }

    private fun stateAdapter2() {

        binding!!.stateSpinner1.threshold = 1
        binding!!.stateSpinner1.setAdapter(responseStateAdapter1)
        binding!!.stateSpinner1.showDropDown()

        binding!!.stateSpinner1.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as State?

            aTwoRequest.Present_stateId = selectedPoi!!.uuid

            aTwoRequest.Present_stateName = selectedPoi.name
            binding!!.stateSpinner1.setText(selectedPoi.name)



            viewModel!!.getDistrict1(selectedPoi.uuid, getDistictRetrofitCallback1)

        }
    }

    val getDistictRetrofitCallback = object : RetrofitCallback<DistrictListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DistrictListResponseModel>?) {

            responseDistictAdapter!!.setData(responseBody!!.body()!!.responseContents)

            distictAdapter()


        }

        override fun onBadRequest(errorBody: Response<DistrictListResponseModel>?) {

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

    val getDistictRetrofitCallback1 = object : RetrofitCallback<DistrictListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DistrictListResponseModel>?) {

            responseDistictAdapter1!!.setData(responseBody!!.body()!!.responseContents)

            distictAdapter1()


        }

        override fun onBadRequest(errorBody: Response<DistrictListResponseModel>?) {

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


    private fun distictAdapter() {

        binding!!.districtSpinner.threshold = 1

        binding!!.districtSpinner.setAdapter(responseDistictAdapter)

        binding!!.districtSpinner.showDropDown()

    }

    private fun distictAdapter1() {

        binding!!.districtSpinner1.threshold = 1

        binding!!.districtSpinner1.setAdapter(responseDistictAdapter1)

        binding!!.districtSpinner1.showDropDown()

        binding!!.districtSpinner1.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as District?

            aTwoRequest.Present_distictId = selectedPoi!!.uuid

            aTwoRequest.Present_distictName = selectedPoi.name

            binding!!.districtSpinner1.setText(selectedPoi.name)

            viewModel!!.getTaluk1(selectedPoi.uuid, getTalukRetrofitCallback1)

        }
    }


    val getTalukRetrofitCallback = object : RetrofitCallback<TalukListResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<TalukListResponseModel>?) {

            responseTalukAdapter!!.setData(responseBody!!.body()!!.responseContents)

            talukAdapter()

        }

        override fun onBadRequest(errorBody: Response<TalukListResponseModel>?) {

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

    val getsingleStateRetrofitCallback = object : RetrofitCallback<StateListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<StateListResponseModel>?) {

            try {
                responseStateAdapter!!.setData(responseBody!!.body()!!.responseContents)

                stateAdapter()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onBadRequest(errorBody: Response<StateListResponseModel>?) {

            val gson = GsonBuilder().create()
            val responseModel: StateListResponseModel
            try {
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

    val getTalukRetrofitCallback1 = object : RetrofitCallback<TalukListResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<TalukListResponseModel>?) {

            responseTalukAdapter1!!.setData(responseBody!!.body()!!.responseContents)

            talukAdapter1()

        }

        override fun onBadRequest(errorBody: Response<TalukListResponseModel>?) {

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

    private fun talukAdapter() {

        binding!!.talukSpinner.threshold = 1

        binding!!.talukSpinner.setAdapter(responseTalukAdapter)

        binding!!.talukSpinner.showDropDown()

    }

    private fun talukAdapter1() {

        binding!!.talukSpinner1.threshold = 1

        binding!!.talukSpinner1.setAdapter(responseTalukAdapter1)

        binding!!.talukSpinner1.showDropDown()

        binding!!.talukSpinner1.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as Taluk?

            aTwoRequest.Present_talukId = selectedPoi!!.uuid
            aTwoRequest.Present_talukName = selectedPoi.name
            binding!!.talukSpinner1.setText(selectedPoi.name)

            viewModel!!.getVillage1(selectedPoi.uuid, getVillageRetrofitCallback1)


        }
    }


    val getVillageRetrofitCallback = object : RetrofitCallback<VilliageListResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<VilliageListResponceModel>?) {

            responseVilliageAdapter!!.setData(responseBody!!.body()!!.responseContents)

            villageAdapter()
        }

        override fun onBadRequest(errorBody: Response<VilliageListResponceModel>?) {

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

    val getVillageRetrofitCallback1 = object : RetrofitCallback<VilliageListResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<VilliageListResponceModel>?) {

            responseVilliageAdapter1!!.setData(responseBody!!.body()!!.responseContents)

            villageAdapter1()
        }

        override fun onBadRequest(errorBody: Response<VilliageListResponceModel>?) {

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

    private fun villageAdapter() {

        binding!!.villageSpinner.threshold = 1

        binding!!.villageSpinner.setAdapter(responseVilliageAdapter)

        binding!!.villageSpinner.showDropDown()

        binding!!.villageSpinner.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as Villiage?

            binding!!.villageSpinner.setText(selectedPoi!!.name)

            aTwoRequest.villageId = selectedPoi.uuid

            aTwoRequest.villageName = selectedPoi.name


        }
    }

    private fun villageAdapter1() {

        binding!!.villageSpinner1.threshold = 1

        binding!!.villageSpinner1.setAdapter(responseVilliageAdapter1)

        binding!!.villageSpinner1.showDropDown()

        binding!!.villageSpinner1.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as Villiage?

            aTwoRequest.Present_villageId = selectedPoi!!.uuid
            aTwoRequest.Present_villageName = selectedPoi.name

            binding!!.villageSpinner1.setText(selectedPoi.name)
        }

    }

    val covidSalutationResponseCallback =
        object : RetrofitCallback<CovidSalutationTitleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidSalutationTitleResponseModel>?) {
                A1selectSalutationUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
                setSalutation(responseBody.body()?.responseContents)

            }

            override fun onBadRequest(errorBody: Response<CovidSalutationTitleResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidSalutationTitleResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidSalutationTitleResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.req!!
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
                viewModel!!.progressBar.value = 8
            }
        }

    fun setSalutation(responseContents: List<SalutationresponseContent?>?) {
        CovidSalutationList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashSalutationSpinnerList.clear()

        for (i in responseContents.indices) {

            hashSalutationSpinnerList[responseContents[i]!!.uuid!!] = i

        }


        Log.i("respose", "" + hashSalutationSpinnerList)

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidSalutationList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.salutationSpinner!!.adapter = adapter

    }

    val covidGenderResponseCallback = object : RetrofitCallback<CovidGenderResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidGenderResponseModel>?) {

            A1selectGenderUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
            setGender(responseBody.body()?.responseContents)
        }

        override fun onBadRequest(errorBody: Response<CovidGenderResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidGenderResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    CovidGenderResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.req!!
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setGender(responseContents: List<GenderresponseContent?>?) {
        CovidGenderList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashGenderSpinnerList.clear()

        for (i in responseContents.indices) {

            hashGenderSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidGenderList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerGender!!.adapter = adapter

    }

    val covidPeriodResponseCallback = object : RetrofitCallback<CovidPeriodResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidPeriodResponseModel>?) {

            A1selectPeriodUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
            setPeriod(responseBody.body()?.responseContents)
        }

        override fun onBadRequest(errorBody: Response<CovidPeriodResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidPeriodResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    CovidPeriodResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.req!!
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setPeriod(responseContents: List<PeriodresponseContent?>?) {
        CovidPeriodList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashPeriodSpinnerList.clear()

        for (i in responseContents.indices) {

            hashPeriodSpinnerList[responseContents[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidPeriodList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.periodSpinner!!.adapter = adapter

    }

    val covidNationalityResponseCallback =
        object : RetrofitCallback<CovidNationalityResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidNationalityResponseModel>?) {
                A1selectNationalityUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
                setNationality(responseBody.body()?.responseContents)
            }

            override fun onBadRequest(errorBody: Response<CovidNationalityResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidNationalityResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidNationalityResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
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
                viewModel!!.progressBar.value = 8
            }
        }

    fun setNationality(responseContents: List<NationalityresponseContent?>?) {
        CovidNationalityList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        hashNationalitySpinnerList.clear()

        for (i in responseContents.indices) {

            hashNationalitySpinnerList[responseContents[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidNationalityList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerNationality!!.adapter = adapter

    }

    val covidMobileBelongsToResponseCallback =
        object : RetrofitCallback<CovidMobileBelongsToResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidMobileBelongsToResponseModel>?) {
                A1selectMobileBelongsTOUuid =
                    responseBody?.body()?.responseContents?.get(0)!!.uuid!!
                setMobileBelongsTo(responseBody.body()?.responseContents)
            }

            override fun onBadRequest(errorBody: Response<CovidMobileBelongsToResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidMobileBelongsToResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidMobileBelongsToResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
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
                viewModel!!.progressBar.value = 8
            }
        }

    fun setMobileBelongsTo(responseContents: List<MobilebelongstoresponseContent?>?) {
        CovidMobileBelongsToList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidMobileBelongsToList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerMbNoBelongs!!.adapter = adapter

    }


    val coviPatientCategoryResponseCallback =
        object : RetrofitCallback<CovidPatientCategoryResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidPatientCategoryResponseModel>?) {

                A4selectPatientCategoryUuid =
                    responseBody?.body()?.responseContents?.get(0)!!.uuid!!
                setPatientCategory(responseBody.body()?.responseContents)
            }

            override fun onBadRequest(errorBody: Response<CovidPatientCategoryResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidPatientCategoryResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidPatientCategoryResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
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
                viewModel!!.progressBar.value = 8
            }
        }

    val CovidSearchNameCallBack = object : RetrofitCallback<CovidRegistrationSearchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidRegistrationSearchResponseModel>?) {
            autocompleteNameTestResponse = responseBody?.body()?.responseContents

/*
            name = responseBody?.body()?.responseContents?.get(1)?.first_name
*/
        }

        override fun onBadRequest(errorBody: Response<CovidRegistrationSearchResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidRegistrationSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    CovidRegistrationSearchResponseModel::class.java
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


    fun setPatientCategory(responseContents: List<PatientCategoryresponseContent?>?) {
        CovidPatientCategoryList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashPatientCatSpinnerList.clear()

        for (i in responseContents.indices) {

            hashPatientCatSpinnerList[responseContents[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidPatientCategoryList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerPatientCategory!!.adapter = adapter

    }

    val covidQuarantineResponseCallback =
        object : RetrofitCallback<CovidQuarantineTypeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidQuarantineTypeResponseModel>?) {

                setQuarantineType(responseBody?.body()?.responseContents)

            }

            override fun onBadRequest(errorBody: Response<CovidQuarantineTypeResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidQuarantineTypeResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidQuarantineTypeResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
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
                viewModel!!.progressBar.value = 8
            }

        }

    fun setQuarantineType(responseContents: List<QuarantinetyperesponseContent?>?) {
        CovidQuarantineTypeList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidQuarantineTypeList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.quarantineTypeSpinner!!.adapter = adapter

    }

    fun isEmailValid(email: String): Boolean {

        if (email.length == 0) {
            return true
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }

    override fun onFinishEditDialog(responseCovidForm: CovidRegistrationSearchResponseContent?) {
        Log.i("", "Callbackkkkkkkk" + responseCovidForm)

        searchresponseCovidForm = responseCovidForm!!

        viewModel?.getCovidNameTitleList(
            facility_id!!,
            covidSalutationUpdateResponseCallback
        )

        viewModel?.getCovidMobileBelongsToList(
            "contact_type",
            facility_id!!,
            covidMobileBelongsToResponseCallback
        )
//
        /*    viewModel?.getCovidPeriodList(facility_id!!, covidPeriodResponseCallback)

            viewModel?.getCovidGenderList(facility_id!!, covidGenderResponseCallback)



            viewModel?.getCovidNationalityList(
                "nationality_type",
                facility_id!!,
                covidNationalityResponseCallback
            )

            viewModel?.getCovidPatientCategoryList(
                "patient_category",
                facility_id!!,
                coviPatientCategoryResponseCallback
            )


            viewModel!!.getStateList(getsingleStateRetrofitCallback)*/

        try {


            statusSave = false

            binding?.A1NameEditText!!.setText(responseCovidForm.first_name)
            binding?.A1AgeEditText!!.setText(responseCovidForm.age.toString())
            binding?.A1MobileEditText!!.setText(responseCovidForm.patient_detail.mobile)

            if (responseCovidForm.patient_detail.alternate_number != null && responseCovidForm.patient_detail.alternate_number.toString().length > 0) {
                binding?.A1AlterNumberEditText?.setText(responseCovidForm.patient_detail.alternate_number.toString())
            }
            binding?.addressOne!!.setText(responseCovidForm.patient_detail.address_line1)
            binding?.addressTwo!!.setText(responseCovidForm.patient_detail.address_line2)
            binding?.pincode1!!.setText(responseCovidForm.patient_detail.pincode)
            binding?.B1dateOfBirthEditText!!.setText(responseCovidForm.dob)
            binding?.B1emailEditText!!.setText(responseCovidForm.patient_detail.email)
            binding?.B1aadharNumberEditText!!.setText(responseCovidForm.patient_detail.aadhaar_number)


            if (responseCovidForm.patient_detail.other_proof_number != null && responseCovidForm.patient_detail.other_proof_number.toString().length > 0) {
                binding?.B1passportNumberEditText!!.setText(responseCovidForm.patient_detail.other_proof_number.toString())
            }

            if (responseCovidForm.patient_detail.quarantine_status_date != null && responseCovidForm.patient_detail.quarantine_status_date.toString().length > 0) {
                binding?.quarantineFromDate!!.setText(responseCovidForm.patient_detail.quarantine_status_date.toString())
            }

            if (responseCovidForm.patient_detail.travel_history_date != null && responseCovidForm.patient_detail.travel_history_date.toString().length > 0) {
                binding?.travelDurationFrom!!.setText(responseCovidForm.patient_detail.travel_history_date.toString())
            }
            if (responseCovidForm.patient_detail.travel_history_to_date != null && responseCovidForm.patient_detail.travel_history_to_date.toString().length > 0) {
                binding?.travelDurationTo!!.setText(responseCovidForm.patient_detail.travel_history_to_date.toString())
            }

            A1selectSalutationUuid = responseCovidForm.title_uuid

            //   date= responseCovidForm.patient_detail.quarantine_status_date.toString()

            Log.i("", "" + hashSalutationSpinnerList)


            runOnUiThread {


/*
                binding?.salutationSpinner!!.setSelection(
                        hashSalutationSpinnerList[responseCovidForm.title_uuid]!!
                )

                binding?.periodSpinner!!.setSelection(
                    hashPeriodSpinnerList[responseCovidForm.period_uuid]!!
                )

                binding?.spinnerGender!!.setSelection(
                    hashGenderSpinnerList[responseCovidForm.gender_uuid]!!
                )

                binding?.spinnerNationality!!.setSelection(hashNationalitySpinnerList[responseCovidForm.patient_detail.country_uuid]!!)*/
            }


            A1selectNationalityUuid = responseCovidForm.patient_detail.country_uuid

            A1selectGenderUuid = responseCovidForm.gender_uuid


/*            runOnUiThread {

                binding?.spinnerGender!!.setSelection(
                        hashGenderSpinnerList[responseCovidForm.gender_uuid]!!
                )
            }*/
            A1selectPeriodUuid = responseCovidForm.period_uuid


/*            runOnUiThread {
                binding?.periodSpinner!!.setSelection(
                        hashPeriodSpinnerList[responseCovidForm.period_uuid]!!
                )

            }*/

            binding?.spinnerMbNoBelongs!!.setSelection(0)

            //   binding!!.spinnerstate.setText(responseStateAdapter!!.getName(responseCovidForm.patient_detail.state_uuid))

            aTwoRequest.pincode = responseCovidForm.patient_detail.pincode

            aTwoRequest.Address1 = responseCovidForm.patient_detail.address_line1

            aTwoRequest.Address2 = responseCovidForm.patient_detail.address_line2

            aTwoRequest.distictId = responseCovidForm.patient_detail.district_uuid

            aTwoRequest.stateId = responseCovidForm.patient_detail.state_uuid

            aTwoRequest.talukId = responseCovidForm.patient_detail.taluk_uuid

            aTwoRequest.villageId = responseCovidForm.patient_detail.village_uuid


            A4selectPatientCategoryUuid = responseCovidForm.patient_type_uuid

            uuid = responseCovidForm.uuid

            uhid = responseCovidForm.uhid

            binding?.saveTextview?.text = "Update"


            //           binding?.spinnerNationality!!.setSelection(hashNationalitySpinnerList[responseCovidForm.patient_detail.country_uuid]!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val getOutComeListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
            val responseContents = Gson().toJson(response.body()?.responseContents)
            Log.i("", "" + responseContents)
            setadapterTypeValue(response.body()?.responseContents)
        }

        override fun onBadRequest(response: Response<ResponseSpicemanType>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSpicemanType
            try {
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
//                viewModel!!.progress!!.value = 8
        }


    }

    val getRequestedByListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
            val responseContents = Gson().toJson(response.body()?.responseContents)
            Log.i("", "" + responseContents)
            setadapterRequestByValue(response.body()?.responseContents)
        }

        override fun onBadRequest(response: Response<ResponseSpicemanType>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSpicemanType
            try {
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
//                viewModel!!.progress!!.value = 8
        }


    }

    fun setadapterTypeValue(responseContents: List<ResponseSpicemanTypeContent?>?) {
        typeNamesList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            typeNamesList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.outcomeSpinner.adapter = adapter

    }

    fun setadapterRequestByValue(responseContents: List<ResponseSpicemanTypeContent?>?) {
        requestByNamesList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        val adapter2 = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            requestByNamesList.values.toMutableList()
        )
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.requestBySpinner.adapter = adapter2


    }


    //update


    val covidSalutationUpdateResponseCallback =
        object : RetrofitCallback<CovidSalutationTitleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidSalutationTitleResponseModel>?) {

                setUpadteSalutation(responseBody!!.body()?.responseContents)

                viewModel?.getCovidGenderList(facility_id!!, covidGenderUpdateResponseCallback)

            }

            override fun onBadRequest(errorBody: Response<CovidSalutationTitleResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidSalutationTitleResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidSalutationTitleResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.req!!
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
                viewModel!!.progressBar.value = 8
            }
        }

    fun setUpadteSalutation(responseContents: List<SalutationresponseContent?>?) {
        CovidSalutationList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashSalutationSpinnerList.clear()

        for (i in responseContents.indices) {

            hashSalutationSpinnerList[responseContents[i]!!.uuid!!] = i

        }


        Log.i("respose", "" + hashSalutationSpinnerList)

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidSalutationList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.salutationSpinner!!.adapter = adapter


        binding?.salutationSpinner!!.setSelection(
            hashSalutationSpinnerList[searchresponseCovidForm.title_uuid]!!
        )

    }

    //Gender

    val covidGenderUpdateResponseCallback = object : RetrofitCallback<CovidGenderResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidGenderResponseModel>?) {

            setUpdateGender(responseBody?.body()?.responseContents)

            viewModel?.getCovidPeriodList(facility_id!!, covidPeriodUpdateResponseCallback)
        }

        override fun onBadRequest(errorBody: Response<CovidGenderResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidGenderResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    CovidGenderResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.req!!
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setUpdateGender(responseContents: List<GenderresponseContent?>?) {
        CovidGenderList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashGenderSpinnerList.clear()

        for (i in responseContents.indices) {

            hashGenderSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidGenderList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerGender!!.adapter = adapter



        binding?.spinnerGender!!.setSelection(
            hashGenderSpinnerList[searchresponseCovidForm.gender_uuid]!!
        )

    }

    val covidPeriodUpdateResponseCallback = object : RetrofitCallback<CovidPeriodResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidPeriodResponseModel>?) {

            setUpadtePeriod(responseBody?.body()?.responseContents)


            viewModel?.getCovidNationalityList(
                "nationality_type",
                facility_id!!,
                covidNationalityUpdateResponseCallback
            )
        }

        override fun onBadRequest(errorBody: Response<CovidPeriodResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidPeriodResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    CovidPeriodResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.req!!
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setUpadtePeriod(responseContents: List<PeriodresponseContent?>?) {
        CovidPeriodList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashPeriodSpinnerList.clear()

        for (i in responseContents.indices) {

            hashPeriodSpinnerList[responseContents[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidPeriodList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.periodSpinner!!.adapter = adapter

        binding?.periodSpinner!!.setSelection(
            hashPeriodSpinnerList[searchresponseCovidForm.period_uuid]!!
        )
    }

    val covidNationalityUpdateResponseCallback =
        object : RetrofitCallback<CovidNationalityResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidNationalityResponseModel>?) {

                setUpdateNationality(responseBody!!.body()?.responseContents)

                viewModel!!.getStateList(getUpdateStateRetrofitCallback)
            }

            override fun onBadRequest(errorBody: Response<CovidNationalityResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidNationalityResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidNationalityResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
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
                viewModel!!.progressBar.value = 8
            }
        }

    fun setUpdateNationality(responseContents: List<NationalityresponseContent?>?) {
        CovidNationalityList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        hashNationalitySpinnerList.clear()

        for (i in responseContents.indices) {

            hashNationalitySpinnerList[responseContents[i]!!.uuid!!] = i
        }

        Log.i(
            "nationality",
            "" + hashNationalitySpinnerList + "" + searchresponseCovidForm.patient_detail.country_uuid
        )
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidNationalityList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerNationality!!.adapter = adapter

        try {
            binding?.spinnerNationality!!.setSelection(hashNationalitySpinnerList[searchresponseCovidForm.patient_detail.country_uuid]!!)
        } catch (e: Exception) {

            binding?.spinnerNationality!!.setSelection(0)

        }

    }


    val getUpdateStateRetrofitCallback = object : RetrofitCallback<StateListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<StateListResponseModel>?) {

            try {
                responseStateAdapter!!.setData(responseBody!!.body()!!.responseContents)

                updatestateAdapter()

                viewModel!!.getDistrict(
                    searchresponseCovidForm.patient_detail.state_uuid,
                    getDistictUpdateRetrofitCallback
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onBadRequest(errorBody: Response<StateListResponseModel>?) {

            val gson = GsonBuilder().create()
            val responseModel: StateListResponseModel
            try {
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


    private fun updatestateAdapter() {

        binding!!.spinnerstate.threshold = 1
        binding!!.spinnerstate.setAdapter(responseStateAdapter)
        binding!!.spinnerstate.showDropDown()

        binding!!.spinnerstate.setText(responseStateAdapter!!.getName(searchresponseCovidForm.patient_detail.state_uuid))
    }

    val getDistictUpdateRetrofitCallback = object : RetrofitCallback<DistrictListResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<DistrictListResponseModel>?) {

            responseDistictAdapter!!.setData(responseBody!!.body()!!.responseContents)

            binding!!.districtSpinner.threshold = 1

            binding!!.districtSpinner.setAdapter(responseDistictAdapter)

            binding!!.districtSpinner.showDropDown()


            binding!!.districtSpinner.setText(
                responseDistictAdapter!!.getName(
                    searchresponseCovidForm.patient_detail.district_uuid
                )
            )

            viewModel!!.getTaluk(
                searchresponseCovidForm.patient_detail.district_uuid,
                getTalukUpdateRetrofitCallback
            )

        }

        override fun onBadRequest(errorBody: Response<DistrictListResponseModel>?) {

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


    val getTalukUpdateRetrofitCallback = object : RetrofitCallback<TalukListResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<TalukListResponseModel>?) {

            responseTalukAdapter!!.setData(responseBody!!.body()!!.responseContents)

            talukUpdateAdapter()

            viewModel!!.getVillage(
                searchresponseCovidForm.patient_detail.taluk_uuid,
                getVillageUpdateRetrofitCallback
            )
        }

        override fun onBadRequest(errorBody: Response<TalukListResponseModel>?) {

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


    private fun talukUpdateAdapter() {

        binding!!.talukSpinner.threshold = 1

        binding!!.talukSpinner.setAdapter(responseTalukAdapter)

        binding!!.talukSpinner.showDropDown()

        binding!!.talukSpinner.setText(responseTalukAdapter!!.getName(searchresponseCovidForm.patient_detail.taluk_uuid))

    }

    val getVillageUpdateRetrofitCallback = object : RetrofitCallback<VilliageListResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<VilliageListResponceModel>?) {

            responseVilliageAdapter!!.setData(responseBody!!.body()!!.responseContents)

            viewModel!!.getSpecimenDetails(
                searchresponseCovidForm.uuid.toString(),
                getspecimenRetrofitCallback
            )

            villageAdapter()
        }

        override fun onBadRequest(errorBody: Response<VilliageListResponceModel>?) {

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

    val repeatedResultResponseCallback = object : RetrofitCallback<RepeatedResultResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<RepeatedResultResponseModel>?) {

            A3selectRepeatResultUuid = responseBody?.body()?.responseContents!!.get(0)!!.uuid!!

            setRepeatedResult(responseBody.body()?.responseContents)
        }

        override fun onBadRequest(errorBody: Response<RepeatedResultResponseModel>?) {

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

    fun setRepeatedResult(responseContents: List<RepeatedResultresponseContent?>?) {
        CovidRepeatedResultList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashRepeatedResultList.clear()

        for (i in responseContents.indices) {

            hashRepeatedResultList[responseContents[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidRepeatedResultList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerA3Result!!.adapter = adapter

    }

    val IntervalsResponseCallback = object : RetrofitCallback<RepeatedIntervalReponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<RepeatedIntervalReponseModel>?) {

            A3selectIntervalsUuid = responseBody?.body()?.responseContents!!.get(0)!!.uuid!!

            setIntervals(responseBody.body()?.responseContents)
        }

        override fun onBadRequest(errorBody: Response<RepeatedIntervalReponseModel>?) {

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

    fun setIntervals(responseContents: List<RepeatedIntervalresponseContent?>?) {
        CovidIntervalsList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashIntervalsList.clear()

        for (i in responseContents.indices) {

            hashIntervalsList[responseContents[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            CovidIntervalsList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerA3Interval!!.adapter = adapter

    }


    val getspecimenRetrofitCallback = object : RetrofitCallback<specimenResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<specimenResponseModel>?) {

            getspecimanresponse = responseBody!!.body()!!.responseContents

            viewModel?.getSpecimanType(facility_id, getSpecimenListUpdateRetrofitCallBack)

        }

        override fun onBadRequest(errorBody: Response<specimenResponseModel>?) {

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


    val getSpecimenListUpdateRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
            val responseContents = Gson().toJson(response.body()?.responseContents)
            Log.i("", "" + responseContents)

            var dataresponse = response.body()?.responseContents

            for (i in getspecimanresponse.indices) {

                for (j in dataresponse!!.indices) {

                    if (getspecimanresponse[i].specimen_type_uuid == dataresponse.get(j)!!.uuid) {

                        try {

                            dataresponse.get(j)!!.othercommands = getspecimanresponse[i].label

                            dataresponse.get(j)!!.date = getspecimanresponse[i].collection_date

                            dataresponse.get(j)!!.isSelect = getspecimanresponse[i].is_active
                        } catch (e: Exception) {

                        }

                    }
                }
            }

            Log.i("correct", "" + dataresponse)

            mAdapter.setdata(dataresponse)

            viewModel!!.getPatientsDetails(
                searchresponseCovidForm.uuid.toString(),
                getpataintUpdateRetrofitCallBack
            )


        }

        override fun onBadRequest(response: Response<ResponseSpicemanType>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSpicemanType
            try {
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
//                viewModel!!.progress!!.value = 8
        }
    }


    val getpataintUpdateRetrofitCallBack =
        object : RetrofitCallback<CovidRegisterPatientResponseModel> {
            override fun onSuccessfulResponse(response: Response<CovidRegisterPatientResponseModel>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)
                Log.i("", "" + responseContents)

                val check = response.body()?.responseContents?.get(0)?.same_address

                if (check!!) {

                    binding!!.dateCheckbox.isChecked = true
                    sameaddress = true
                    binding!!.presentAddressOne.setText(binding!!.addressOne.text.toString())
                    binding!!.presentAddressTwo.setText(binding!!.addressTwo.text.toString())
                    binding!!.stateSpinner1.setText(binding!!.spinnerstate.text.toString())
                    binding!!.districtSpinner1.setText(binding!!.districtSpinner.text.toString())
                    binding!!.talukSpinner1.setText(binding!!.talukSpinner.text.toString())
                    binding!!.villageSpinner1.setText(binding!!.villageSpinner.text.toString())
                    binding!!.pincode2.setText(binding!!.pincode1.text.toString())

                    binding!!.presentAddressOne.isEnabled = false
                    binding!!.presentAddressTwo.isEnabled = false
                    binding!!.stateSpinner1.isEnabled = false
                    binding!!.districtSpinner1.isEnabled = false
                    binding!!.talukSpinner1.isEnabled = false
                    binding!!.villageSpinner1.isEnabled = false
                    binding!!.pincode2.isEnabled = false

                }


                viewModel?.getConditionType(facility_id, getcustomTypeUpdateListRetrofitCallBack)


            }

            override fun onBadRequest(response: Response<CovidRegisterPatientResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CovidRegisterPatientResponseModel
                try {
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
//                viewModel!!.progress!!.value = 8
            }
        }


    val getcustomTypeUpdateListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
            val responseContents = Gson().toJson(response.body()?.responseContents)
            Log.i("", "" + responseContents)
            mAdapterCustomType.setdata(response.body()?.responseContents)


            viewModel!!.getConditionDetails(
                searchresponseCovidForm.uuid.toString(),
                getconditionListUpdateRetrofitCallBack
            )


        }

        override fun onBadRequest(response: Response<ResponseSpicemanType>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSpicemanType
            try {
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
//                viewModel!!.progress!!.value = 8
        }
    }


    val getconditionListUpdateRetrofitCallBack =
        object : RetrofitCallback<ConditionDetailsResponseModel> {
            override fun onSuccessfulResponse(response: Response<ConditionDetailsResponseModel>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)
                Log.i("", "" + responseContents)

                var dataresponse = response.body()?.responseContents

                var contionAdapterData = mAdapterCustomType.getData()

                for (i in contionAdapterData!!.indices) {

                    for (j in dataresponse!!.indices) {

                        if (dataresponse[i].uuid == contionAdapterData.get(j)!!.uuid) {

                            try {

                                contionAdapterData.get(j)!!.isSelect = dataresponse[i].is_active
                            } catch (e: Exception) {

                            }

                        }
                    }
                }

                Log.i("correct", "" + contionAdapterData)

                mAdapterCustomType.setdata(contionAdapterData)


                var checkPataient = searchresponseCovidForm.patient_detail.symptoms


                symptomsStatus = checkPataient

                if (checkPataient) {

                    viewModel?.getSymptoms(facility_id, getSymptomsTypeUpdateRetrofitCallBack)

                }

            }

            override fun onBadRequest(response: Response<ConditionDetailsResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: ConditionDetailsResponseModel
                try {
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
//                viewModel!!.progress!!.value = 8
            }
        }


    val getSymptomsTypeUpdateRetrofitCallBack =
        object : RetrofitCallback<ResponseSpicemanType> {
            override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)
                Log.i("", "" + responseContents)
                mAdapterSymptoms.setdata(response.body()?.responseContents)

                viewModel!!.getSymptomsDetails(
                    searchresponseCovidForm.uuid.toString(),
                    getsymptomListUpdateRetrofitCallBack
                )
            }

            override fun onBadRequest(response: Response<ResponseSpicemanType>) {
                val gson = GsonBuilder().create()
                val responseModel: ResponseSpicemanType
                try {
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
//                viewModel!!.progress!!.value = 8
            }


        }

    val getsymptomListUpdateRetrofitCallBack = object : RetrofitCallback<symptomResponseModel> {
        override fun onSuccessfulResponse(response: Response<symptomResponseModel>) {
            val responseContents = Gson().toJson(response.body()?.responseContents)
            Log.i("", "" + responseContents)

            var dataresponse = response.body()?.responseContents

            var contionAdapterData = mAdapterSymptoms.getData()

            for (i in contionAdapterData!!.indices) {

                for (j in dataresponse!!.indices) {

                    if (dataresponse[i].symptom_uuid == contionAdapterData.get(j)!!.uuid) {

                        try {

                            contionAdapterData.get(j)!!.isSelect = dataresponse[i].is_active

                            contionAdapterData.get(j)!!.othercommands = dataresponse[i].duration

                        } catch (e: Exception) {

                        }

                    }
                }
            }

            Log.i("correct", "" + contionAdapterData)

            mAdapterSymptoms.setdata(contionAdapterData)


        }

        override fun onBadRequest(response: Response<symptomResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: symptomResponseModel
            try {
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
//                viewModel!!.progress!!.value = 8
        }
    }
}