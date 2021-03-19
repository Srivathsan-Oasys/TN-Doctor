package com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentDietChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.DietOrderrequest
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.DietOrderrequestDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.DietOrderrequestHeaders
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.view_model.DietViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.view_model.DietViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.GetOpNotesEncounterByDocAndPatientIdResp
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DietChildFragment : Fragment(), DietFavouriteFragment.FavClickedListener,
    DietTempleteFragment.TempleteClickedListener, DietCallback {

    private var arrayItemData: ArrayList<FavouritesModel?>? = null
    private var binding: FragmentDietChildBinding? = null
    var dietAdapter: DietAdapter? = null
    lateinit var drugNmae: TextView
    private var viewModel: DietViewModel? = null
    private var utils: Utils? = null
    private var customdialog: Dialog? = null
    private var responseContents: String = ""
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView

    var appPreferences: AppPreferences? = null
    var facility_id: Int = 0
    private var encounterType: Int? = 0

    var searchposition: Int = 0
    var mCallbackLabFavFragment: ClearFavParticularPositionListener? = null
    var mCallbackLabTemplateFragment: ClearTemplateParticularPositionListener? = null
    private var facility_UUID: Int? = 0
    private var department_UUID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var fragmentBackClick: FragmentBackClick? = null
    val detailsList = ArrayList<DietOrderrequestDetail>()
    val header: DietOrderrequestHeaders = DietOrderrequestHeaders()
    private var customProgressDialog: CustomProgressDialog? = null
    val emrRequestModel: DietOrderrequest = DietOrderrequest()
    var prevOrder: Boolean = false

    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_diet_child,
            container,
            false
        )
        initViewModel()
        initPerefernces()
        initDrawerLayout()
        setAdapter()
        setupViewPager(binding?.viewpager!!)
        showProgressDialog()
        listeners()

        binding?.viewModel?.getDietCategory(facility_id, favDietCategoryCallBack)

        return binding!!.root
    }

    private fun initViewModel() {
        viewModel = DietViewModelFactory(
            requireActivity().application
        )
            .create(DietViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
    }

    private fun initPerefernces() {
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID) ?: 0
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
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

    private fun setAdapter() {
        dietAdapter = DietAdapter(requireActivity(), ArrayList(), ArrayList())
        binding?.savelabRecyclerView?.adapter = dietAdapter

        dietAdapter?.addRow(FavouritesModel())
        dietAdapter?.addTempleteRow(TempDetails())

        dietAdapter?.setOnDeleteClickListener(object : DietAdapter.OnDeleteClickListener {

            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
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
                    "${drugNmae.text} '" + responseContent?.test_master_name + "(" + responseContent?.test_master_code + ")" + "'Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    val check = dietAdapter?.deleteRow(position)
                    if (responseContent?.viewLabstatus == 1) {
                        mCallbackLabFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)
                    } else if (responseContent?.viewLabstatus == 2) {
                        if (check!!) {
                            mCallbackLabTemplateFragment?.ClearTemplateParticularPosition(
                                responseContent.isTemposition
                            )
                        }
                        //template_id
                    }
                    customdialog!!.dismiss()
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })

        dietAdapter?.setOnSearchInitiatedListener(object : DietAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int
            ) {
                dropdownReferenceView = view
                searchposition = position
                viewModel?.getSearchDietViewModel(facility_id, query, dietSearchRetrofitCallback)
            }
        })
    }

    private fun listeners() {
        binding?.saveCardView!!.setOnClickListener {
            getEncounter()
        }

        binding?.clearCardView?.setOnClickListener {
            mCallbackLabFavFragment?.ClearAllData()
            dietAdapter?.clearAll()
            dietAdapter?.addRow(FavouritesModel())
            dietAdapter?.addTempleteRow(TempDetails())
        }

        binding?.saveAsTempCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val labtemplatedialog = ManageDietTemplateFragment()
            val templateDetailsResponseContent = TemplateDetailsResponseContent()
            val list = ArrayList<Templates>()

            val arrayListResponse: ArrayList<TemplateDietDetail> = ArrayList()
            for (i in 0 until (dietAdapter?.itemCount ?: 0)) {
                val templateDietDetail = TemplateDietDetail(
                    template_details_uuid = dietAdapter?.getAll()?.get(i)?.template_details_uuid
                        ?: 0,
                    template_details_displayorder = dietAdapter?.getAll()
                        ?.get(i)?.template_details_displayorder ?: 0,
                    diet_name = dietAdapter?.getAll()?.get(i)?.diet_master_name ?: "",
                    diet_id = dietAdapter?.getAll()?.get(i)?.diet_master_id ?: 0,
                    diet_code = dietAdapter?.getAll()?.get(i)?.diet_master_code ?: "",
                    quantity = dietAdapter?.getAll()?.get(i)?.diet_quantity ?: 0,
                    diet_display_order = 0,
                    diet_category_id = dietAdapter?.getAll()?.get(i)?.diet_category_id ?: 0,
                    diet_category_name = dietAdapter?.getAll()?.get(i)?.diet_category_name ?: "",
                    diet_category_code = dietAdapter?.getAll()?.get(i)?.diet_category_code ?: "",
                    diet_frequency_id = dietAdapter?.getAll()?.get(i)?.diet_frequency_id ?: 0,
                    diet_frequency_name = dietAdapter?.getAll()?.get(i)?.diet_frequency_name ?: "",
                    diet_frequency_code = dietAdapter?.getAll()
                        ?.get(i)?.diet_frequency_code?.toString() ?: ""
                )
                arrayListResponse.add(templateDietDetail)
            }

            val templates = Templates(
                diet_details = arrayListResponse,
                temp_details = null
            )

            list.add(templates)

            templateDetailsResponseContent.templates_list = list
            val bundle = Bundle()
            bundle.putInt("TAG", 1)
            bundle.putParcelable(
                AppConstants.RESPONSECONTENT,
                templateDetailsResponseContent
            )
            labtemplatedialog.arguments = bundle
            labtemplatedialog.show(ft, "Tag")
        }
    }

    private fun saveData() {
        val patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        val encounter_doctor_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
        arrayItemData = dietAdapter?.getItems()

        if (prevOrder) {
            detailsList.clear()
            for (i in arrayItemData?.indices!!) {
                val data = arrayItemData!!.size
                if (i != data - 1) {
                    val details = DietOrderrequestDetail()
                    details.diet_master_uuid = arrayItemData?.get(i)?.diet_master_id!!
                    details.diet_order_category_uuid = arrayItemData?.get(i)?.diet_category_id!!
                    details.diet_frequency_uuid = arrayItemData?.get(i)?.diet_frequency_id!!
                    details.quantity = arrayItemData?.get(i)?.diet_quantity!!
                    detailsList.add(details)
                }
            }
            header.department_uuid = department_UUID.toString()
            header.encounter_doctor_uuid = encounter_doctor_uuid!!
            header.encounter_type_uuid = encounter_Type!!.toString()
            header.encounter_uuid = encounter_uuid!!.toInt()
            header.facility_uuid = facility_id.toString()
            header.is_active = true
            header.patient_uuid = patient_UUID.toString()
            header.order_date = ""
            emrRequestModel.details = this.detailsList
            emrRequestModel.headers = this.header
            viewModel?.dietInsert(facility_id, emrRequestModel, emrpostRetrofitCallback)
        } else {
            if (arrayItemData?.size!! > 0) {
                arrayItemData!!.removeAt(arrayItemData!!.size - 1)
            }
            detailsList.clear()
            if (arrayItemData?.size!! > 0) {
                for (i in arrayItemData?.indices!!) {
                    val details = DietOrderrequestDetail()
                    details.diet_master_uuid = arrayItemData?.get(i)?.diet_master_id!!
                    details.diet_order_category_uuid = arrayItemData?.get(i)?.diet_category_id!!
                    details.diet_frequency_uuid = arrayItemData?.get(i)?.diet_frequency_id!!
                    details.quantity = arrayItemData?.get(i)?.diet_quantity!!
                    detailsList.add(details)
                }
                header.department_uuid = department_UUID.toString()
                header.encounter_doctor_uuid = encounter_doctor_uuid!!
                header.encounter_type_uuid = encounter_Type!!.toString()
                header.encounter_uuid = encounter_uuid!!.toInt()
                header.facility_uuid = facility_id.toString()
                header.is_active = true
                header.patient_uuid = patient_UUID.toString()
                val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                header.order_date = sdf1.format(Date())
                emrRequestModel.details = this.detailsList
                emrRequestModel.headers = this.header
                viewModel?.dietInsert(facility_id, emrRequestModel, emrpostRetrofitCallback)
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
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(DietFavouriteFragment(), "  Favourite")
        adapter.addFragment(DietTempleteFragment(), "  Template")
        adapter.addFragment(PrevDietFragment(), "Prev Diet Details")
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

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    override fun sendData(favmodel: FavouritesModel?, position: Int, selected: Boolean) {
        //  favmodel?.viewLabstatus = 1
        favmodel?.isFavposition = position
        if (!selected) {
            dietAdapter!!.addFavouritesInRow(favmodel, 1)
        } else {
            dietAdapter!!.deleteRowFromTemplate(favmodel?.test_master_id, 1)
        }
    }

    override fun sendTemplete(
        templeteDetails: List<DietTemplateDeatils?>?,
        position: Int,
        selected: Boolean,
        id: Int
    ) {
        if (!selected) {
            for (i in templeteDetails!!.indices) {
                val favmodel = FavouritesModel()
                // favmodel?.viewLabstatus = 2
                favmodel.isTemposition = position
                favmodel.diet_master_name = templeteDetails[i]!!.diet_name
                favmodel.diet_master_id = templeteDetails[i]!!.diet_id
                favmodel.diet_master_code = templeteDetails[i]!!.diet_code
                favmodel.diet_quantity = templeteDetails[i]!!.quantity
                favmodel.diet_category_code = templeteDetails[i]!!.diet_category_code
                favmodel.diet_category_name = templeteDetails[i]!!.diet_category_name
                favmodel.diet_category_id = templeteDetails[i]!!.diet_category_id
                favmodel.diet_frequency_code = templeteDetails[i]!!.diet_frequency_code.toInt()
                favmodel.diet_frequency_name = templeteDetails[i]!!.diet_frequency_name
                favmodel.diet_frequency_id = templeteDetails[i]!!.diet_frequency_id
                favmodel.template_details_uuid = templeteDetails[i]!!.template_details_uuid
                favmodel.template_details_displayorder =
                    templeteDetails[i]!!.template_details_displayorder
                favmodel.template_id = id
                dietAdapter!!.addFavouritesInRow(favmodel, 2)
            }
        } else {
            for (i in templeteDetails!!.indices) {
                dietAdapter!!.deleteRowFromTemplate(templeteDetails[i]!!.diet_id, 2)
            }
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is DietFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is DietTempleteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackLabFavFragment = childFragment
        }
        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackLabTemplateFragment = childFragment
        }
        if (childFragment is PrevDietFragment) {
            childFragment.setCallbackListener(this)
        }
        /* if (childFragment is ManageLabSaveTemplateFragment) {
             childFragment.setOnClickedListener(this)
         }
       */
    }

    override fun OnviewClick(prevdiet: PatientDietOrder?) {
        val model = FavouritesModel()
        model.diet_master_id = prevdiet?.diet_master_uuid!!
        model.diet_master_code = prevdiet.diet_code!!
        model.itemAppendString = prevdiet.diet_name!!
        model.template_id = 0
        model.diet_quantity = prevdiet.quantity!!
        model.diet_category_id = prevdiet.diet_category_uuid!!
        model.diet_category_name = prevdiet.diet_category_name!!
        model.diet_frequency_id = prevdiet.diet_frequency_uuid!!
        model.diet_frequency_name = prevdiet.diet_frequency_name!!

        dietAdapter?.clearAll()
        dietAdapter?.addRow(model)
        dietAdapter?.notifyDataSetChanged()

    }

    private fun getEncounter() {
        viewModel?.getEncounter(
            facility_UUID ?: 0,
            patientUuid,
            department_UUID ?: 0,
            encounterType ?: 0,
            getOpNotesEncounterByDocAndPatientIdRespCallback
        )
    }


    private val getOpNotesEncounterByDocAndPatientIdRespCallback =
        object : RetrofitCallback<GetOpNotesEncounterByDocAndPatientIdResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOpNotesEncounterByDocAndPatientIdResp>?) {
                responseBody?.body()?.let { getOpNotesEncounterByDocAndPatientIdResp ->
                    if (getOpNotesEncounterByDocAndPatientIdResp.responseContents?.isNotEmpty() == true) {
                        getOpNotesEncounterByDocAndPatientIdResp.responseContents?.get(0)
                            .let { responseContentXX ->
                                doctor_en_uuid =
                                    responseContentXX?.encounter_doctors?.get(0)?.uuid ?: 0
                                encounter_uuid = responseContentXX?.uuid ?: 0
                                patientUuid = responseContentXX?.patient_uuid ?: 0
                                appPreferences?.saveInt(
                                    AppConstants.ENCOUNTER_DOCTOR_UUID,
                                    doctor_en_uuid
                                )
                                appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)

                                saveData()
                            }
                    } else {
                        viewModel?.createEncounter(
                            patientUuid,
                            encounterType ?: 0,
                            createEncounterRetrofitCallback
                        )
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOpNotesEncounterByDocAndPatientIdResp>?) {
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

    private val createEncounterRetrofitCallback =
        object : RetrofitCallback<CreateEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {
                doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
                encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
                patientUuid =
                    response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

                saveData()
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

    val emrpostRetrofitCallback = object : RetrofitCallback<EmrResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<EmrResponceModel>?) {
            Toast.makeText(requireContext(), responseBody?.message(), Toast.LENGTH_LONG).show()
            mCallbackLabFavFragment?.ClearAllData()
            mCallbackLabTemplateFragment?.ClearAllData()
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
            dietAdapter?.clearAll()
            dietAdapter?.addRow(FavouritesModel())
            dietAdapter?.addTempleteRow(TempDetails())
            prevOrder = false
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

    val dietSearchRetrofitCallback =
        object : RetrofitCallback<SearchDietResponse> {
            override fun onSuccessfulResponse(response: Response<SearchDietResponse>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    dietAdapter?.setAdapter(
                        dropdownReferenceView, response.body()?.responseContents, searchposition
                    )
                }
            }

            override fun onBadRequest(response: Response<SearchDietResponse>) {
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


    private val favDietCategoryCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            dietAdapter?.setadapterCategoryValue(responseBody?.body()?.responseContents)
            showProgressDialog()
            binding?.viewModel?.getDietFrequency(facility_id, favDietFrequencyCallBack)
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
            hideProgressDialog()
            viewModel!!.progress.value = 8
        }
    }

    val favDietFrequencyCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            dietAdapter?.setadapterFrequencyValue(responseBody?.body()?.responseContents)
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
            hideProgressDialog()
            viewModel!!.progress.value = 8
        }
    }

    fun showProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.show()
        }
    }

    fun hideProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
        }
    }
}