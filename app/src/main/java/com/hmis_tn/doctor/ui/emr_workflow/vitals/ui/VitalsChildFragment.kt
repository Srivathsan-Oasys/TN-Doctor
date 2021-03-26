package com.hmis_tn.doctor.ui.emr_workflow.vitals.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentVitalsChildBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.*
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.response.VitalSaveResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.response.VitalSearchListResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.responseuommodule.ResponseUOMModules
import com.hmis_tn.doctor.ui.emr_workflow.vitals.view_model.VitalsViewModel
import com.hmis_tn.doctor.ui.emr_workflow.vitals.view_model.VitalsViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VitalsChildFragment : Fragment() {

    private var FaciltyID: Int? = 0
    private var encounter_id: Int? = 0
    private var encounter_type: Int? = 0
    private var searchPosition: Int? = 0
    private var customdialog: Dialog? = null
    private var binding: FragmentVitalsChildBinding? = null
    lateinit var vitalsTemplatesAdapter: VitalsTemplateAdapter
    var vitalsAdapter: VitalsAdapter? = null
    private var responseContents: String? = ""
    private var viewModel: VitalsViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var department_uuid: Int? = null
    private var customProgressDialog: CustomProgressDialog? = null

    private var patient_Uuid: Int? = null
    private var doctor_en_uuid: Int = 0

    lateinit var autoCompleteTextView: AppCompatAutoCompleteTextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_vitals_child,
                container,
                false
            )
        viewModel = VitalsViewModelFactory(
            requireActivity().application
        ).create(VitalsViewModel::class.java)
        binding?.viewModel = viewModel

        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        customProgressDialog = CustomProgressDialog(requireContext())

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
        binding?.clearCardView?.isEnabled = true

        utils = Utils(requireContext())

        trackVitalsVisit(utils!!.getEncounterType())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        FaciltyID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patient_Uuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        encounter_type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        viewModel!!.getVitalsName(FaciltyID, getVitalsListRetrofitCallback)


        viewModel!!.progress.observe(requireActivity(),
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })
        viewModel!!.errorText.observe(requireActivity(),
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        setAdapter()

//        viewModel!!.getUmoList(FaciltyID,getUmoListRetrofitCallback)
        viewModel!!.getUmoList(FaciltyID, getUmoListRetrofitCallback)


        initViews()
        listeners()

        return binding?.root
    }

    private fun initViews() {
        binding?.tabs?.setupWithViewPager(binding?.viewPagerVitals)
        setupViewPager(binding?.viewPagerVitals)
        binding?.viewPagerVitals?.beginFakeDrag()
    }

    private fun listeners() {
        binding?.saveCardView?.setOnClickListener {
            trackVitalsSaveStart(utils!!.getEncounterType())

            if (vitalsAdapter?.getall()!!.size > 1) {

                viewModel?.getEncounter(
                    FaciltyID!!,
                    patient_Uuid!!,
                    encounter_type!!,
                    fetchEncounterRetrofitCallBack
                )
            }

        }

        binding?.clearCardView?.setOnClickListener {
            vitalsAdapter!!.clearAll()
            viewModel!!.getVitalsName(FaciltyID, getVitalsListRetrofitCallback)
        }
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        val templateFragment = VitalTemplateFragment(
            adapterAddRow = {
                vitalsAdapter!!.addRow(it)
            },
            adapterDeleteRow = {
                vitalsAdapter!!.deleteRowItem(it)
            }
        )
        val previousVitalsFragment = PreviousVitalsFragment()
        adapter.addFragment(templateFragment, "Templates")
        adapter.addFragment(previousVitalsFragment, "Previous Vitals")
        viewPager?.adapter = adapter
    }

    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.savevitalsRecyclerView!!.layoutManager = linearLayoutManager
        vitalsAdapter = VitalsAdapter(requireActivity(), ArrayList())
        binding?.savevitalsRecyclerView?.adapter = vitalsAdapter


        //Delete
        vitalsAdapter!!.setOnDeleteClickListener(object :
            VitalsAdapter.OnDeleteClickListener {
            override fun onDeleteClick(templateDetail: TemplateDetail, position: Int) {

                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
                closeImageView.setOnClickListener {
                    customdialog!!.dismiss()
                }
                val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text} '" + templateDetail.name + "' Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    //vitalsAdapter?.deleteItem(position)
                    customdialog!!.dismiss()
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })


        vitalsAdapter!!.setOnSearchClickListener(object : VitalsAdapter.OnSearchClickListener {
            override fun onSearchClick(
                autoCompleteTextView1: AppCompatAutoCompleteTextView,
                position: Int
            ) {

                autoCompleteTextView = autoCompleteTextView1

                searchPosition = position

                viewModel!!.searchList(vitalSearchRetrofitCallback, FaciltyID)

            }
        })
    }

    val vitalSearchRetrofitCallback = object : RetrofitCallback<VitalSearchListResponseModel> {

        override fun onSuccessfulResponse(response: Response<VitalSearchListResponseModel>) {
            responseContents = Gson().toJson(response.body()?.responseContents)

            vitalsAdapter!!.setAdapter(
                autoCompleteTextView,
                response.body()?.responseContents!!.getVitals,
                searchPosition
            )


        }

        override fun onBadRequest(response: Response<VitalSearchListResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: VitalSearchListResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    VitalSearchListResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!, ""
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

    val vitalsSaveRetrofitCallback = object : RetrofitCallback<VitalSaveResponseModel> {
        override fun onSuccessfulResponse(response: Response<VitalSaveResponseModel>) {
            responseContents = Gson().toJson(response.body()?.responseContents)
            trackVitalsSaveComplete(utils?.getEncounterType(), "success", "")
            Log.e("VitalCreated", response.body()?.responseContents.toString())

            Toast.makeText(activity, response.body()?.message!!, Toast.LENGTH_LONG).show()
            vitalsAdapter!!.clearAll()
            viewModel!!.getVitalsName(FaciltyID, getVitalsListRetrofitCallback)

        }

        override fun onBadRequest(response: Response<VitalSaveResponseModel>) {
            trackVitalsSaveComplete(utils!!.getEncounterType(), "failure", response.message())
            val gson = GsonBuilder().create()
            val responseModel: VitalSaveResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    VitalSaveResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!, ""
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
            trackVitalsSaveComplete(
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
            trackVitalsSaveComplete(
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
            trackVitalsSaveComplete(
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
            trackVitalsSaveComplete(utils!!.getEncounterType(), "failure", failure)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    val getUmoListRetrofitCallback = object : RetrofitCallback<ResponseUOMModules> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseUOMModules>?) {

            if (responseBody?.body()?.responseContents?.isNotEmpty()!!) {

                if (responseBody.body()!!.responseContents!!.size != 0) {

                    vitalsAdapter!!.setTypeValue(responseBody.body()!!.responseContents)

                    Log.i("", "" + responseBody.body())
                }
            }

        }

        override fun onBadRequest(response: Response<ResponseUOMModules>) {
            val gson = GsonBuilder().create()
            val responseModel: VitalsTemplateResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    VitalsTemplateResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!, ""
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
    val getVitalsListRetrofitCallback = object : RetrofitCallback<MainVItalsListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<MainVItalsListResponseModel>?) {

            if (responseBody?.body()?.responseContents?.getVitals?.isNotEmpty()!!) {

                if (responseBody.body()!!.responseContents?.getVitals?.isNotEmpty() == true) {

                    //Log.e("VitalList",responseBody!!.body()?.responseContents.toString())

                    vitalsAdapter!!.addFavouritesInRow(responseBody.body()?.responseContents?.getVitals as ArrayList<TemplateDetail>)


                }
            }

        }

        override fun onBadRequest(response: Response<MainVItalsListResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: VitalsListResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    VitalsListResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!, ""
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

    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
//                vitalsTemplatesAdapter.refreshList(response?.body()?.responseContents)
            }

            override fun onBadRequest(response: Response<TempleResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TempleResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TempleResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.bad)
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

    fun trackVitalsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackVitalVisit(type)
    }


    fun trackVitalsSaveStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackVitalSaveStart(type)
    }


    fun trackVitalsSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackVitalSaveComplete(type, status, message)
    }


    fun saveVitals() {
        val saveData: ArrayList<VitalSaveRequestModel> = ArrayList()
        val allDAta = vitalsAdapter!!.getall()
        val datasize: Int = allDAta.size
        var saveOk: Boolean? = false
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val dateInString = sdf.format(Date())
        if (datasize > 1) {
            for (i in 0..(datasize - 2)) {
                if (allDAta[i].vital_value.trim().length <= 0) {
                    saveOk = false
                    break

                } else {
                    saveOk = true
                    val vitalSaveRequestModel = VitalSaveRequestModel()
                    vitalSaveRequestModel.facility_uuid = FaciltyID.toString()
                    vitalSaveRequestModel.department_uuid = department_uuid.toString()
                    vitalSaveRequestModel.patient_uuid = patient_Uuid.toString()
                    vitalSaveRequestModel.encounter_uuid = encounter_id!!
                    vitalSaveRequestModel.encounter_type_uuid = encounter_type!!

                    vitalSaveRequestModel.performed_date = dateInString + "Z"
                    vitalSaveRequestModel.tat_start_time = dateInString + "Z"
                    vitalSaveRequestModel.tat_end_time = dateInString + "Z"

                    vitalSaveRequestModel.vital_group_uuid = 1
                    vitalSaveRequestModel.vital_type_uuid = 1
                    vitalSaveRequestModel.vital_qualifier_uuid = 1
                    vitalSaveRequestModel.patient_vital_status_uuid = 1
                    vitalSaveRequestModel.vital_value_type_uuid = 1
                    vitalSaveRequestModel.vital_master_uuid = allDAta[i].uuid
                    vitalSaveRequestModel.vital_value = allDAta[i].vital_value
                    vitalSaveRequestModel.vital_uom_uuid = allDAta[i].uom_master_uuid
                    saveData.add(vitalSaveRequestModel)

                }
            }
            if (saveOk!!) {
                val gson = Gson()
                val json = gson.toJson(saveData)
                Log.e("SaveRequest", json)
                Log.e("Save", "Saveee")
                viewModel!!.vitalSave(FaciltyID, vitalsSaveRetrofitCallback, saveData)
            } else {
                Toast.makeText(context, "please enter required fields!!", Toast.LENGTH_LONG).show()
            }

        } else {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select any one item"
            )
        }
    }


    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    var encounterResponseContent = response.body()?.responseContents!!
                    doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_id = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_id!!)
                    saveVitals()

                } else {
                    viewModel?.createEncounter(
                        patient_Uuid!!,
                        encounter_type!!,
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
            encounter_id = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patient_Uuid =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveVitals()
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


    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

    override fun onResume() {
        super.onResume()
        binding?.root?.isFocusableInTouchMode = true
        binding?.root?.requestFocus()
        binding?.root?.setOnKeyListener { v, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
    }
}