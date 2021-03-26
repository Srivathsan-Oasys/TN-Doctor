package com.hmis_tn.doctor.ui.emr_workflow.discharge_medication.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DischaargeMedicationChildfragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.discharge_medication.viewmodel.DischargeMedicationViewModel
import com.hmis_tn.doctor.ui.emr_workflow.discharge_medication.viewmodel.DischargeMedicationViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempDetails
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.*
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.SaveTemplateDialogFragment
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class DischargeMedicationChildFragment : Fragment(),
    PrescriptionFavouriteFragment.FavClickedListener,
    PrescriptionTempleteFragment.TempleteClickedListener,
    PrevPrescriptionFragment.PrevClickedListener,
    CommentDialogFragment.CommandClickedListener {
    private var arrayItemData: ArrayList<FavouritesModel?>? = null
    private var facility_id: Int? = 0
    lateinit var drugNmae: TextView
    private var binding: DischaargeMedicationChildfragmentBinding? = null
    private var customdialog: Dialog? = null
    private var viewModel: DischargeMedicationViewModel? = null
    var prescriptionAdapter: DischargeMedicationAdapter? = null
    private var utils: Utils? = null

    // create boolean for fetching data
    private var isViewShown = false
    val header: Header? = Header()
    val emrPrescriptionRequestModel: emr_prescription_postalldata_requestmodel? =
        emr_prescription_postalldata_requestmodel()
    private var responseContents: String? = ""
    var mCallbackPresFavFragment: ClearFavParticularPositionListener? = null
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    var mCallbackPrevTemplateFragment: ClearTemplateParticularPositionListener? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var customProgressDialog: CustomProgressDialog? = null
    val detailsList = ArrayList<Detail>()
    private var selectedSearchPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dischaarge_medication_childfragment,
                container,
                false
            )
        viewModel = DischargeMedicationViewModelFactory(
            requireActivity().application
        ).create(DischargeMedicationViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this


        if (userVisibleHint) {
            fetchData()
        }
        return binding!!.root
    }

    private fun fetchData() {
        utils = Utils(requireContext())

        trackDischargeMedicationVisit(utils?.getEncounterType())


        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val labMasterTypeUUID = appPreferences?.getInt(AppConstants.LAB_MASTER_TYPE_ID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        val patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
//
        viewModel?.getFrequencyDetails(getFrequencyRetrofitCallback, facility_id)
        setADapter()
        binding?.clearCardView?.setOnClickListener {
            mCallbackPresFavFragment?.ClearAllData()
            mCallbackPrevTemplateFragment?.ClearAllData()

            prescriptionAdapter?.clearall()
            prescriptionAdapter?.addRow(FavouritesModel())
            prescriptionAdapter?.addTempleteRow(TempDetails())
        }

        binding?.saveCardView!!.setOnClickListener {
            arrayItemData = prescriptionAdapter?.getItems()
            if (arrayItemData?.size!! > 0) {
                arrayItemData!!.removeAt(arrayItemData!!.size - 1)
            }
            detailsList.clear()

            if (arrayItemData?.size!! > 0) {

                for (i in arrayItemData?.indices!!) {
                    val details: Detail = Detail()

                    var prescribed_quantity_Data: Int = 0

                    val period = arrayItemData?.get(i)?.PrescriptiondurationPeriodId

                    var prescribed_quantity = arrayItemData?.get(i)?.perdayquantityPrescription!!

                    if (prescribed_quantity.contains(".")) {
                        prescribed_quantity =
                            prescribed_quantity.substring(0, prescribed_quantity.indexOf('.'))
                    }

                    when (period) {
                        1 -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * arrayItemData?.get(i)?.duration!!.toInt()
                        }
                        2 -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * arrayItemData?.get(i)?.duration!!.toInt() * 7
                        }
                        else -> {
                            prescribed_quantity_Data =
                                prescribed_quantity.toInt() * arrayItemData?.get(i)?.duration!!.toInt() * 30
                        }
                    }


                    details.drug_route_uuid = arrayItemData?.get(i)?.selectRouteID.toString()
                    details.drug_frequency_uuid =
                        arrayItemData?.get(i)?.selecteFrequencyID.toString()
                    details.duration_period_uuid =
                        arrayItemData?.get(i)?.PrescriptiondurationPeriodId.toString()
                    details.drug_instruction_uuid = arrayItemData?.get(i)?.selectInvestID.toString()
                    details.duration = arrayItemData?.get(i)?.duration
                    details.prescribed_quantity = prescribed_quantity_Data.toString()
                    details.item_master_uuid = arrayItemData?.get(i)?.test_master_id
                    details.comments = arrayItemData?.get(i)?.commands
                    details.is_emar = false
                    detailsList.add(details)
                }
                header?.department_uuid = department_UUID.toString()
                header?.doctor_uuid = userDataStoreBean?.uuid.toString()
                header?.encounter_type_uuid = encounter_Type!!.toInt()
                header?.encounter_uuid = encounter_uuid!!.toInt()
                header?.dispense_status_uuid = 1
                header?.patient_uuid = patient_UUID.toString()
                header?.prescription_status_uuid = 2
                header?.store_master_uuid = "1"
                header?.notes = ""
                header?.is_digitally_signed = true

                emrPrescriptionRequestModel?.header = this.header!!
                emrPrescriptionRequestModel?.details = this.detailsList
                viewModel?.prescriptiondataInsert(
                    facility_id,
                    emrPrescriptionRequestModel!!,
                    emrprescriptionpostRetrofitCallback
                )

            }
        }
        binding?.zeroStockCardView?.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            val dialog = ZeroStockDialogFragment()
            dialog.show(ft, "Tag")
        }
        binding?.saveTemplateCardView?.setOnClickListener {
            //    val ft = requireActivity().supportFragmentManager.beginTransaction()
            val dialog = SaveTemplateDialogFragment()
            //    dialog.show(ft, "Tag")
            val ft = childFragmentManager.beginTransaction()
            //val dialog = ManageLabFavouriteFragment()
            val bundle = Bundle()
            val saveArray = prescriptionAdapter!!.getItems()
            bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArray)
            dialog.arguments = bundle
            dialog.show(ft, "Tag")

        }
        val bundle = Bundle()
/*
        bundle.putParcelable("Drug Name", details)
*/
    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(PrescriptionFavouriteFragment(), "Favourite")
        adapter.addFragment(PrescriptionTempleteFragment(), "Templete")
        adapter.addFragment(PrevPrescriptionFragment(), "Prev.Prescription")

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


    private fun setADapter() {
        prescriptionAdapter =
            DischargeMedicationAdapter(requireActivity(), ArrayList(), ArrayList())
        binding?.savePrescriptionRecyclerView?.adapter = prescriptionAdapter
        prescriptionAdapter?.addRow(FavouritesModel())
        prescriptionAdapter?.addTempleteRow(TempDetails())
        prescriptionAdapter?.setOnDeleteClickListener(object :
            DischargeMedicationAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                ////////////////////////
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
                    "${drugNmae.text} '" + responseContent?.drug_name + "(" + responseContent?.drug_code + ")" + "'Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    val check = prescriptionAdapter?.deleteRow(position)
//                val check=labAdapter?.checkData(responseContent!!)

                    if (responseContent?.viewPrescriptionstatus == 1) {

                        mCallbackPresFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)

                    } else if (responseContent?.viewPrescriptionstatus == 2) {
                        val ischeckIdPresent =
                            mCallbackPresFavFragment!!.drugIdPresentCheck(responseContent.test_master_id!!)
                        if (ischeckIdPresent) {
                            mCallbackPresFavFragment?.clearDataUsingDrugid(responseContent.drug_id!!)
                        }
                        if (check!!) {
                            mCallbackPrevTemplateFragment?.ClearTemplateParticularPosition(
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
        prescriptionAdapter?.setOnSearchInitiatedListener(object :
            DischargeMedicationAdapter.OnSearchInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int
            ) {
                dropdownReferenceView = view
                selectedSearchPosition = position
                viewModel?.getComplaintSearchResult(
                    query,
                    getComplaintSearchRetrofitCallBack,
                    facility_id
                )
            }
        })


        prescriptionAdapter?.setOnCommandClickListener(object :
            DischargeMedicationAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int) {

                //     val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val dialog = CommentDialogFragment()

                val ft = childFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("position", position)

                dialog.arguments = bundle
                dialog.show(ft, "Tag")


            }
        })

        setupViewPager(binding?.viewpager!!)
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)
        viewModel?.getPrescriptionDuration(getPrescriptionDurationRetrofitCallBack, facility_id)

    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is PrescriptionFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is PrescriptionTempleteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackPresFavFragment = childFragment
        }
        if (childFragment is ClearTemplateParticularPositionListener) {
            mCallbackPrevTemplateFragment = childFragment
        }
        if (childFragment is PrevPrescriptionFragment) {
            childFragment.setOnTextClickedListener(this)
        }

        if (childFragment is CommentDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }


    }

    val getFrequencyRetrofitCallback =
        object : RetrofitCallback<PrescriptionFrequencyResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<PrescriptionFrequencyResponseModel>?) {

                prescriptionAdapter?.setFrequencyList(responseBody?.body()?.responseContents)
                viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)
            }

            override fun onBadRequest(errorBody: Response<PrescriptionFrequencyResponseModel>?) {
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

    val getRouteRetrofitCallback = object : RetrofitCallback<PrescriptionRoutResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<PrescriptionRoutResponseModel>?) {

            prescriptionAdapter?.setadapterTypeValue(responseBody?.body()?.responseContents)
            viewModel?.getInstructionDetails(getInstructionRetrofitCallback, facility_id)

        }

        override fun onBadRequest(errorBody: Response<PrescriptionRoutResponseModel>?) {

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
    val getInstructionRetrofitCallback = object : RetrofitCallback<PresInstructionResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<PresInstructionResponseModel>?) {

            prescriptionAdapter?.setInstructionList(responseBody?.body()?.responseContents)

        }

        override fun onBadRequest(errorBody: Response<PresInstructionResponseModel>?) {
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
    val getComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<PrescriptionSearchResponseModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionSearchResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    prescriptionAdapter?.setPrescriptionAdapter(
                        dropdownReferenceView,
                        response.body()?.responseContents!!, selectedSearchPosition
                    )

                }
            }

            override fun onBadRequest(response: Response<PrescriptionSearchResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionSearchResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionSearchResponseModel::class.java
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
    private val getPrescriptionDurationRetrofitCallBack =
        object : RetrofitCallback<PrescriptionDurationResponseModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionDurationResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    prescriptionAdapter?.setDuration(response.body()?.responseContents!!)

                }
            }

            override fun onBadRequest(response: Response<PrescriptionDurationResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionDurationResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionDurationResponseModel::class.java
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
        favmodel?.viewPrescriptionstatus = 1
        favmodel?.isFavposition = position
        if (!selected) {
            prescriptionAdapter!!.addFavouritesInRow(favmodel)
        } else {
            prescriptionAdapter!!.deleteRowFromFav(favmodel?.drug_id!!, position)
        }
    }


    override fun sendTemplete(
        templeteDrugDetails: List<DrugDetail>?,
        position: Int,
        selected: Boolean,
        templateID: Int
    ) {
        if (!selected) {

            for (i in templeteDrugDetails!!.indices) {
                val favmodel: FavouritesModel? = FavouritesModel()
                favmodel?.viewPrescriptionstatus = 2
                favmodel?.isTemposition = position
                favmodel?.test_master_name = templeteDrugDetails[i].drug_name
                favmodel?.test_master_id = templeteDrugDetails[i].drug_id
                favmodel?.test_master_code = templeteDrugDetails[i].drug_code
                favmodel?.template_id = templateID
                favmodel?.itemAppendString = templeteDrugDetails[i].drug_name
                favmodel?.selectInvestID = templeteDrugDetails[i].drug_instruction_id
                favmodel?.selectRouteID = templeteDrugDetails[i].drug_route_id
                favmodel?.selecteFrequencyID = templeteDrugDetails[i].drug_frequency_id
                favmodel?.duration = templeteDrugDetails[i].drug_duration.toString()

                //check drag_id present in Favorite callback to favorite

                val checkIsPresent =
                    prescriptionAdapter!!.isCheckAlreadyExist(templeteDrugDetails[i].drug_id)

                if (checkIsPresent) {

                    prescriptionAdapter!!.addTemplatesInRow(favmodel)

                }


                mCallbackPresFavFragment!!.checkanduncheck(templeteDrugDetails[i].drug_id, true)

            }

        } else {
            for (i in templeteDrugDetails!!.indices) {

                prescriptionAdapter!!.deleteRowFromTemplate(
                    templeteDrugDetails[i].drug_id,
                    position
                )

                mCallbackPresFavFragment!!.checkanduncheck(templeteDrugDetails[i].drug_id, false)

            }
        }
    }


    val emrprescriptionpostRetrofitCallback =
        object : RetrofitCallback<PrescriptionPostAllDataResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<PrescriptionPostAllDataResponseModel>?) {
                mCallbackPresFavFragment?.ClearAllData()
                mCallbackPrevTemplateFragment?.ClearAllData()
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    responseBody?.body()?.msg!!
                )
                prescriptionAdapter?.clearall()
                prescriptionAdapter?.addRow(FavouritesModel())
                prescriptionAdapter?.addTempleteRow(TempDetails())


            }

            override fun onBadRequest(response: Response<PrescriptionPostAllDataResponseModel>) {


                val gson = GsonBuilder().create()
                val responseModel: PrescriptionPostAllDataResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionPostAllDataResponseModel::class.java
                    )

                    Log.e("BadRequest", response.toString())

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

    override fun sendPrevInPres(
        responseContent: List<PrescriptionDetail?>?,
        IsModify: Boolean,
        Injection: PrevRow?
    ) {

        Log.i("datas", "" + responseContent)

        Log.i("datas", "" + responseContent)
        Log.i("datas", "" + responseContent)

        prescriptionAdapter!!.clearall()

        for (i in responseContent!!.indices) {

            var favouritesModel = FavouritesModel()

            favouritesModel.drug_name = responseContent.get(i)!!.item_master!!.name
            favouritesModel.itemAppendString = responseContent.get(i)!!.item_master!!.name
            favouritesModel.test_master_id = responseContent.get(i)!!.uuid
            favouritesModel.drug_period_code = responseContent.get(i)!!.item_master!!.code

            favouritesModel.selecteFrequencyID = responseContent.get(i)!!.drug_frequency_uuid!!

            prescriptionAdapter!!.addRow(favouritesModel)

        }

        prescriptionAdapter!!.addRow(FavouritesModel())
    }

    override fun sendCommandPosData(position: Int, command: String) {

        Log.i("comanda", "" + position + "" + command)
        prescriptionAdapter!!.addCommands(position, command)

    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            // post your code
        }
    }

    override fun onStart() {
        super.onStart()
        userVisibleHint = true
    }

    private fun trackDischargeMedicationVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackDischargeMedicationVisit(type)
    }

}