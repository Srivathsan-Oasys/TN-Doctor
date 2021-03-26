package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view

import android.app.Dialog
import android.content.res.Configuration
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
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.callbacks.FragmentBackClick
import com.hmis_tn.doctor.component.extention.hide
import com.hmis_tn.doctor.component.extention.isvisible
import com.hmis_tn.doctor.component.extention.show
import com.hmis_tn.doctor.component.extention.slideDown
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DiagnosisChildFragmentBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.*
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.DiagonosisSearchResponse
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.ResponseContentsdiagonosissearch
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.SnomedDialogFragment
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedData
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view_model.DiagnosisViewModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view_model.DiagnosisViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.history.diagnosis.model.DiagnosisSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.history.diagnosis.model.DiagresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.CommentDialogFragment
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.hmis_tn.doctor.utils.Utils
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DiagnosisChildFragment : Fragment(), FavaddDiagonosisDialog.OnFavRefreshListener,
    SnomedDialogFragment.OnSnomedListener, DiagnosisFavouriteFragment.FavClickedListener,
    PreDiagnosisFragment.CancelListener, CommentDialogFragment.CommandClickedListener {

    private var starttime: String? = null
    private var customdialog: Dialog? = null
    private var sdf: SimpleDateFormat? = null
    lateinit var drugNmae: TextView
    private var jsonObj_: JSONObject? = null
    private var gsonObject: JsonObject? = null
    private var facility_id: Int = 0
    private var UpdatePosition: Int = 0
    private var Str_auto_id: Int? = 0
    private var fragmentBackClick: FragmentBackClick? = null
    var mCallbackdiagnosisFavFragment: ClearFavParticularPositionListener? = null
    private var callbackPrevDiagnosis: CallBackPreviousDiagnosis? = null
    var binding: DiagnosisChildFragmentBinding? = null
    private var departmentID: Int? = 0
    var diagnosisFavouritesAdapter: DiagnosisFavouritesAdapter? = null
    private var utils: Utils? = null
    private var responseContents: String? = ""
    private var viewModel: DiagnosisViewModel? = null
    private var dignoisisAdapter: DiagnosisAdapter? = null
    private var dignoisisMobAdapter: DiagnosisMobileAdapter? = null
    private var selectedSearchPosition = -1
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    private var appPreferences: AppPreferences? = null
    private var deparment_UUID: Int? = 0
    var gson: Gson? = Gson()
    val diagnosisRequestArrayList = ArrayList<DiagnosisRequest>()

    private var encounterType: Int = 0
    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0

    private var IsTablet: Boolean = false
    private var isUpdate: Boolean = false
    var AddNew = DiagnosisNewData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val languageToLoad = language // your language

        val locale = Locale("en")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireContext().resources.updateConfiguration(
            config,
            requireContext().resources.displayMetrics
        )

        Utils(requireContext()).setCalendarLocale("en", requireContext())

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.diagnosis_child_fragment,
                container,
                false
            )

        viewModel = DiagnosisViewModelFactory(
            requireActivity().application
        ).create(DiagnosisViewModel::class.java)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!

        sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")


        // initFavouritesAdapter()

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


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        starttime = sdf.format(Date())
        binding?.ICDdrawerLayout?.drawerElevation = 0f

        binding?.ICDdrawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        utils = Utils(requireContext())
        trackDiagnosisVisit(utils!!.getEncounterType())
        val patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!

        IsTablet = resources.getBoolean(R.bool.isTablet)


        if (IsTablet) {

            tabletfunctions()

        } else {


            mobilefunctions()


        }


        return binding!!.root
    }

    fun mobilefunctions() {


        binding?.llDropDownView?.hide()
        binding?.rlHeader?.setOnClickListener {
            if (binding?.llDropDownView?.isvisible()!!) {
                hideDropDown()
            } else {
                showDropDown()
            }
        }

        dignoisisMobAdapter =
            DiagnosisMobileAdapter(
                requireActivity(),
                ArrayList()
            )
        binding?.saveRecyclerView?.adapter = dignoisisMobAdapter

        binding?.snomedGt!!.setOnClickListener {

            val ft = childFragmentManager.beginTransaction()
            val dialog = SnomedDialogFragment()
            dialog.show(ft, "Tag")
        }

        dignoisisMobAdapter?.setOnCommandClickListener(object :
            DiagnosisMobileAdapter.OnCommandClickListener {


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


        dignoisisMobAdapter!!.setOnDeleteClickListener(object :
            DiagnosisMobileAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesModel: DiagnosisListData?, position: Int) {


                dignoisisMobAdapter!!.deleteRow(favouritesModel, position)

                if (favouritesModel?.viewDiagnosisstatus == 1) {
                    mCallbackdiagnosisFavFragment?.ClearFavParticularPosition(favouritesModel.isFavposition)

                }

                toast("${favouritesModel?.diagnosis_name} Deleted Successfully")

                /*         customdialog = Dialog(requireContext())
                         customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                         customdialog!! .setCancelable(false)
                         customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                         val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                         closeImageView.setOnClickListener {
                             customdialog?.dismiss()
                         }
                         drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                         drugNmae.text ="${drugNmae.text.toString()} '"+favouritesModel?.diagnosis_name+"' Record ?"
                         val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                         val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                         yesBtn.setOnClickListener {

                             customdialog!!.dismiss()


                         }
                         noBtn.setOnClickListener {
                             customdialog!! .dismiss()
                         }
                         customdialog!! .show()*/


            }
        })

        dignoisisMobAdapter?.onItemClick(object : DiagnosisMobileAdapter.OnSelectItemCommunication {
            override fun onClick(
                position: Int,
                selectedItem: Boolean,
                favouritesModel: DiagnosisListData?
            ) {
                dignoisisMobAdapter?.updateSelectStatus(position, selectedItem)

                AddNew = setNewData(favouritesModel)

                binding?.actCode?.setText(AddNew.diagnosis_code)

                binding?.actTestName?.setText(AddNew.diagnosis_name)


                if (favouritesModel!!.is_snomed == 0) {

                    binding?.diagnosisType?.setText("ICD 10")

                } else {

                    binding?.diagnosisType?.setText("Snomed")
                }

                if (selectedItem) {
                    clearAll()
                    hideDropDown()
                    isUpdate = false
                } else {

                    isUpdate = true
                    UpdatePosition = position
                    showDropDown()

                }
            }

        })


        binding?.actIcd?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2) {

                    /*      viewModel!!.getdiagnosisSearchResult(s.toString(),

                              facility_id, getComplaintSearchSnodRetrofitCallBack)
      */
                    viewModel?.getDiagnosisList(s.toString(), getICDSearchRerofitCallback)


                }
            }
        })

        binding?.actTestName?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2) {

                    /*      viewModel!!.getdiagnosisSearchResult(s.toString(),

                              facility_id, getComplaintSearchSnodRetrofitCallBack)
      */
                    viewModel?.getDiagnosis(
                        facility_id,
                        s.toString(),
                        getDignosisSearchRetrofitCallBack
                    )


                }
            }
        })


        binding?.actCode?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2 && s.length < 5) {

                    viewModel!!.getCodeSearchResult(
                        facility_id, s.toString(),

                        getCodeSearchRerofitCallback
                    )

                }
            }
        })


        binding?.add?.setOnClickListener {


            if (AddNew.diagnosis_id == "") {

                toast("Please select fill all items")

            }

            if (isUpdate) {

                dignoisisMobAdapter?.UpdateRow(UpdatePosition, setListData(AddNew))

                isUpdate = false

                hideDropDown()

                clearAll()

            } else {

                dignoisisMobAdapter?.addRow(setListData(AddNew))

                hideDropDown()

                clearAll()

            }
        }


        binding?.clear?.setOnClickListener {

            isUpdate = false

            clearAll()
        }


        binding?.clearCardView?.setOnClickListener {

            isUpdate = false

            hideDropDown()

            dignoisisMobAdapter?.clearall()

            clearAll()
        }

        binding!!.saveCardView!!.setOnClickListener {
            diagnosisRequestArrayList.clear()
            trackDiagnosisSaveStart(utils!!.getEncounterType())
            val arrayItemData = dignoisisMobAdapter!!.getall()
            val datasize: Int = arrayItemData.size

            if (arrayItemData.size > 0) {
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

    }

    private fun clearAll() {
        AddNew = DiagnosisNewData()

        binding?.actTestName?.setText("")
        binding?.actCode?.setText("")
        binding?.diagnosisType?.setText("")

    }

    private fun setNewData(responseContent: DiagnosisListData?): DiagnosisNewData {


        return DiagnosisNewData(

            drug_active = responseContent!!.drug_active,
            diagnosis_description = responseContent.diagnosis_description,
            diagnosis_code = responseContent.diagnosis_code,
            diagnosis_id = responseContent.diagnosis_id,
            diagnosis_name = responseContent.diagnosis_name,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active!!,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            isModifiy = responseContent.isModifiy,
            isReadyForSave = responseContent.isReadyForSave,
            viewDiagnosisstatus = responseContent.viewDiagnosisstatus,
            isFavposition = responseContent.isFavposition,
            is_snomed = responseContent.is_snomed,
            commands = responseContent.commands
        )
    }


    private fun setfavtoListData(responseContent: FavouritesModel?): DiagnosisListData {


        return DiagnosisListData(

            drug_active = responseContent!!.drug_active,
            diagnosis_description = responseContent.diagnosis_description,
            diagnosis_code = responseContent.diagnosis_code,
            diagnosis_id = responseContent.diagnosis_id,
            diagnosis_name = responseContent.diagnosis_name,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active!!,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            isModifiy = responseContent.isModifiy,
            isReadyForSave = responseContent.isReadyForSave,
            viewDiagnosisstatus = responseContent.viewDiagnosisstatus,
            isFavposition = responseContent.isFavposition,
            is_snomed = responseContent.is_snomed,
            commands = responseContent.commands
        )
    }

    private fun setListData(responseContent: DiagnosisNewData?): DiagnosisListData {


        return DiagnosisListData(

            drug_active = responseContent!!.drug_active,
            diagnosis_description = responseContent.diagnosis_description,
            diagnosis_code = responseContent.diagnosis_code,
            diagnosis_id = responseContent.diagnosis_id,
            diagnosis_name = responseContent.diagnosis_name,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active!!,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            isModifiy = responseContent.isModifiy,
            isReadyForSave = responseContent.isReadyForSave,
            viewDiagnosisstatus = responseContent.viewDiagnosisstatus,
            isFavposition = responseContent.isFavposition,
            is_snomed = responseContent.is_snomed,
            commands = responseContent.commands
        )
    }

    fun tabletfunctions() {

        setAdapter()


        binding?.snomedCardView!!.setOnClickListener {

            val ft = childFragmentManager.beginTransaction()
            val dialog = SnomedDialogFragment()
            dialog.show(ft, "Tag")
        }
        binding!!.saveDiagonsis!!.setOnClickListener {
            diagnosisRequestArrayList.clear()
            trackDiagnosisSaveStart(utils!!.getEncounterType())
            val arrayItemData = dignoisisAdapter!!.getall()
            val datasize: Int = arrayItemData.size

            if (arrayItemData.size > 1) {
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
        binding?.diagnosisSearchSnod?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2) {

                    viewModel?.getDiagnosisList(s.toString(), getICDSearchRerofitCallback)


                }
            }
        })



        binding?.clearCardView!!.setOnClickListener {
            dignoisisAdapter!!.clearall()
            dignoisisAdapter!!.addRow(FavouritesModel())
            mCallbackdiagnosisFavFragment?.ClearAllData()
            callbackPrevDiagnosis?.onRefresh()
        }


    }


    fun setdiagnosisAdapter(
        responseContents: List<ResponseContentsdiagonosissearch?>?,
        selectedSearchPosition: Int
    ) {

        val responseContentAdapter = DianosisSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.diagnosisSearchSnod!!.threshold = 1
        binding!!.diagnosisSearchSnod!!.setAdapter(responseContentAdapter)
        binding!!.diagnosisSearchSnod!!.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ResponseContentsdiagonosissearch?

            val favmodel: FavouritesModel = FavouritesModel()
            favmodel.diagnosis_id = selectedPoi?.uuid?.toString()
            favmodel.diagnosis_name = selectedPoi?.name
            favmodel.diagnosis_code = selectedPoi?.code

            dignoisisAdapter?.addFavouritesInRow(favmodel)
            binding!!.diagnosisSearchSnod?.setText("")


        }
    }


    private fun setAdapter() {

        dignoisisAdapter =
            DiagnosisAdapter(
                requireActivity(),
                ArrayList()
            )
        binding?.saveRecyclerView?.adapter = dignoisisAdapter
        dignoisisAdapter?.addRow(FavouritesModel())

        dignoisisAdapter!!.setOnDeleteClickListener(object :
            DiagnosisAdapter.OnDeleteClickListener {
            override fun onDeleteClick(favouritesModel: FavouritesModel?, position: Int) {


                dignoisisAdapter!!.deleteRow(favouritesModel, position)

                if (favouritesModel?.viewDiagnosisstatus == 1) {
                    mCallbackdiagnosisFavFragment?.ClearFavParticularPosition(favouritesModel.isFavposition)

                }

                toast("${favouritesModel?.diagnosis_name} Deleted Successfully")
                /*   customdialog = Dialog(requireContext())
                   customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                   customdialog!! .setCancelable(false)
                   customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                   val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                   closeImageView.setOnClickListener {
                       customdialog?.dismiss()
                   }
                   drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                   drugNmae.text ="${drugNmae.text.toString()} '"+favouritesModel?.diagnosis_name+"' Record ?"
                   val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                   val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                   yesBtn.setOnClickListener {


                       customdialog!!.dismiss()


                   }
                   noBtn.setOnClickListener {
                       customdialog!! .dismiss()
                   }
                   customdialog!! .show()
   */

            }
        })


        dignoisisAdapter!!.setOnSearchInitiatedListener(object :
            DiagnosisAdapter.OnSearchDignisisInitiatedListener {
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int
            ) {
                selectedSearchPosition = position
                dropdownReferenceView = view
                viewModel?.getDiagnosis(facility_id, query, getDignosisSearchRetrofitCallBack)
            }
        })

        dignoisisAdapter?.setOnCommandClickListener(object :
            DiagnosisAdapter.OnCommandClickListener {
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

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(DiagnosisFavouriteFragment(), "Favourite")
        adapter.addFragment(PreDiagnosisFragment(), "Prev.diagnosis")

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


    val insertDiagnoisisRetrofitCallback = object : RetrofitCallback<DiagnosisResponseModel> {
        override fun onSuccessfulResponse(response: Response<DiagnosisResponseModel>?) {
            Log.i("res", "" + response?.body()?.message)
            trackDiagnosisSaveComplete(utils?.getEncounterType(), "success", "")
/*
           utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,response!!.body()!!.message!!
            )*/

            toast("Diagnosis Added Successfully")
            // diagnosisFavouritesAdapter!!.clearFacusData()

            if (IsTablet) {

                dignoisisAdapter!!.clearall()
                dignoisisAdapter!!.addRow(FavouritesModel())
                mCallbackdiagnosisFavFragment?.ClearAllData()
                callbackPrevDiagnosis?.onRefresh()


            } else {

                dignoisisMobAdapter!!.clearall()
                mCallbackdiagnosisFavFragment?.ClearAllData()
                callbackPrevDiagnosis?.onRefresh()

                isUpdate = false
                clearAll()
                hideDropDown()
            }
        }

        override fun onBadRequest(response: Response<DiagnosisResponseModel>) {
            val gson = GsonBuilder().create()
            trackDiagnosisSaveComplete(utils!!.getEncounterType(), "failure", response.message())
            val responseModel: DiagnosisResponseModel
            if (response.code() == 400) {
                val gson = GsonBuilder().create()
                var mError = ErrorAPIClass()
                try {
                    mError =
                        gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)

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
                    DiagnosisResponseModel::class.java
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
            trackDiagnosisSaveComplete(
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
            trackDiagnosisSaveComplete(
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
            trackDiagnosisSaveComplete(
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
            trackDiagnosisSaveComplete(utils!!.getEncounterType(), "failure", failure)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    var getDignosisSearchRetrofitCallBack = object : RetrofitCallback<DiagonosisSearchResponse> {
        override fun onSuccessfulResponse(response: Response<DiagonosisSearchResponse>?) {

            responseContents = Gson().toJson(response?.body()?.responseContents)
            if (response?.body()?.responseContents?.isNotEmpty()!!) {


                if (IsTablet) {
                    dignoisisAdapter?.setAdapter(
                        dropdownReferenceView,
                        response.body()?.responseContents!!,
                        selectedSearchPosition
                    )


                } else {


                    val responseContentAdapter = DianosisSearchResultAdapter(
                        requireContext(),
                        R.layout.row_chief_complaint_search_result,
                        response.body()?.responseContents
                    )
                    binding?.actTestName!!.threshold = 1
                    binding?.actTestName!!.setAdapter(responseContentAdapter)
                    binding?.actTestName!!.showDropDown()
                    binding?.actTestName!!.setOnItemClickListener { parent, _, position, id ->

                        val selectedPoi =
                            parent.adapter.getItem(position) as ResponseContentsdiagonosissearch?


                        var getData = dignoisisMobAdapter?.getall()

                        val check =
                            getData!!.any { it!!.diagnosis_id == selectedPoi?.uuid.toString() }

                        if (!check) {

                            binding?.actTestName!!.setText(selectedPoi?.name)
                            binding?.actCode?.setText(selectedPoi?.code)

                            binding?.diagnosisType?.setText("ICD 10")

                            //   var AddNew=DiagnosisNewData()

                            AddNew.diagnosis_name = selectedPoi?.name
                            AddNew.is_snomed = 0
                            AddNew.diagnosis_id = selectedPoi?.uuid.toString()
                            AddNew.diagnosis_code = selectedPoi?.code.toString()


                        } else {
                            binding?.actTestName!!.setText("")

                            Toast.makeText(
                                context,
                                "Already Item available in the list",
                                Toast.LENGTH_LONG
                            )?.show()

                        }
                    }


                }

            }
        }

        override fun onBadRequest(response: Response<DiagonosisSearchResponse>) {
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
    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            //  viewModel!!.getFavourites(deparment_UUID,getFavouritesRetrofitCallBack)
            customdialog!!.dismiss()
            Log.e("DeleteResponse", responseBody?.body().toString())
        }

        override fun onBadRequest(errorBody: Response<DeleteResponseModel>?) {

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
    val getComplaintSearchSnodRetrofitCallBack =
        object : RetrofitCallback<DiagonosisSearchResponse> {
            override fun onSuccessfulResponse(response: Response<DiagonosisSearchResponse>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    setdiagnosisAdapter(response.body()?.responseContents, 0)
                }
            }

            override fun onBadRequest(response: Response<DiagonosisSearchResponse>) {
                val gson = GsonBuilder().create()
                val responseModel: DiagonosisSearchResponse
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        DiagonosisSearchResponse::class.java
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


    override fun onFavID(position: Int) {

    }

    override fun onRefreshList() {
//        viewModel!!.getFavourites(departmentID,getFavouritesRetrofitCallBack)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is DiagnosisFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackdiagnosisFavFragment = childFragment
        }

        if (childFragment is SnomedDialogFragment) {
            childFragment.setOnSnomedRefreshListener(this)
        }

        if (childFragment is CallBackPreviousDiagnosis) {
            callbackPrevDiagnosis = childFragment
        }
        if (childFragment is PreDiagnosisFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is CommentDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }


    }

    interface CallBackPreviousDiagnosis {
        fun onRefresh()
    }

    override fun onDetach() {
        super.onDetach()
        mCallbackdiagnosisFavFragment = null
    }

    override fun onSnomeddata(data: SnomedData) {


        if (IsTablet) {

            val favouritesModel: FavouritesModel = FavouritesModel()

            favouritesModel.itemAppendString = data.ConceptName

            favouritesModel.diagnosis_name = data.ConceptName

            favouritesModel.diagnosis_code = data.ConceptId

            favouritesModel.diagnosis_id = data.ConceptId

            favouritesModel.is_snomed = 1

            dignoisisAdapter!!.addFavouritesInRow(favouritesModel)

        } else {

            dignoisisMobAdapter!!.addFavouritesInRow(
                DiagnosisListData(
                    diagnosis_name = data.ConceptName,
                    diagnosis_code = data.ConceptId,
                    diagnosis_id = data.ConceptId,
                    is_snomed = 1
                )
            )

        }

    }

    override fun sendFavAddInChiefFav(
        favmodel: FavouritesModel?,
        position: Int,
        selected: Boolean
    ) {
        favmodel?.viewDiagnosisstatus = 1
        favmodel?.isFavposition = position
        favmodel?.is_snomed = 0
        favmodel?.drug_active = false
        if (!selected) {

            if (IsTablet) {
                dignoisisAdapter?.addFavouritesInRow(favmodel)
            } else {

                dignoisisMobAdapter?.addFavouritesInRow(setfavtoListData(favmodel))

            }

        } else {

            if (IsTablet) {
                dignoisisAdapter!!.deleteRowFromFavourites(favmodel, position)

            } else {

                dignoisisMobAdapter!!.deleteRowFromFavourites(setfavtoListData(favmodel), position)
            }
        }
    }

    fun trackDiagnosisVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackDiagnosisVisit(type)
    }


    fun trackDiagnosisSaveStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackDiagnosisSaveStart(type)
    }


    fun trackDiagnosisSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackDiagnosisSaveComplete(type, status, message)
    }

    override fun cancelfunction() {
        if (IsTablet)
            binding?.ICDdrawerLayout!!.closeDrawer(GravityCompat.END)
        else
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
                    saveDiagnosis()

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

            saveDiagnosis()
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

    private fun saveDiagnosis() {
        val endTime = sdf?.format(Date())

        if (IsTablet) {

            val arrayItemData = dignoisisAdapter!!.getall()


            val datasize: Int = arrayItemData.size
            if (datasize > 1) {

                for (i in arrayItemData.indices) {
                    val data = arrayItemData.size
                    if (i != data - 1) {
                        val diagnosisRequest: DiagnosisRequest? = DiagnosisRequest()
                        diagnosisRequest!!.facility_uuid = facility_id.toString()
                        diagnosisRequest.department_uuid = deparment_UUID.toString()
                        diagnosisRequest.patient_uuid = patientUuid.toString()
                        diagnosisRequest.test_master_type_uuid = 1
                        diagnosisRequest.encounter_uuid = encounter_uuid
                        diagnosisRequest.encounter_type_uuid = encounterType
                        diagnosisRequest.other_diagnosis = arrayItemData[i]!!.itemAppendString!!
                        diagnosisRequest.diagnosis_uuid =
                            arrayItemData.get(i)!!.diagnosis_id!!.toString()
                        diagnosisRequest.tat_start_time = starttime!!
                        diagnosisRequest.tat_end_time = endTime!!
                        diagnosisRequest.is_snomed = arrayItemData[i]!!.is_snomed
                        diagnosisRequest.comments = arrayItemData[i]!!.commands
                        diagnosisRequestArrayList.add(diagnosisRequest)
                    }
                }

                val request = Gson().toJson(diagnosisRequestArrayList)

                Log.i("", "" + request)
                viewModel!!.InsertDiagnosis(
                    diagnosisRequestArrayList,
                    insertDiagnoisisRetrofitCallback,
                    facility_id
                )
            } else {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Please select any one item"
                )
            }

        } else {
            val arrayItemData = dignoisisMobAdapter!!.getall()

            var diagnosisMobileRequest: ArrayList<DiagnosisRequest> = ArrayList()

            diagnosisMobileRequest.clear()

            for (i in arrayItemData.indices) {

                diagnosisMobileRequest.add(
                    DiagnosisRequest(
                        facility_uuid = facility_id.toString(),
                        department_uuid = deparment_UUID.toString(),
                        patient_uuid = patientUuid.toString(),
                        test_master_type_uuid = 1,
                        encounter_uuid = encounter_uuid,
                        encounter_type_uuid = encounterType,
                        other_diagnosis = arrayItemData[i]!!.diagnosis_name!!,
                        diagnosis_uuid = arrayItemData.get(i)!!.diagnosis_id!!.toString(),
                        tat_start_time = starttime!!,
                        tat_end_time = endTime!!,
                        is_snomed = arrayItemData[i]!!.is_snomed,
                        comments = arrayItemData[i]!!.commands
                    )
                )
            }

            viewModel!!.InsertDiagnosis(
                diagnosisMobileRequest,
                insertDiagnoisisRetrofitCallback,
                facility_id
            )


        }
    }

    override fun sendCommandPosData(position: Int, command: String) {

        if (IsTablet)
            dignoisisAdapter!!.addCommands(position, command)
        else
            dignoisisMobAdapter!!.addCommands(position, command)
    }


    val getCodeSearchRerofitCallback = object : RetrofitCallback<DiagnosisSearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<DiagnosisSearchResponseModel>) {


            setSearchCodeData(response.body()?.responseContents)

        }

        override fun onBadRequest(response: Response<DiagnosisSearchResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: DiagnosisSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    DiagnosisSearchResponseModel::class.java
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

    private fun setSearchCodeData(responseData: List<DiagresponseContent?>?) {

        val codeAdapter = DiagnosisCodeSearchAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseData
        )
        binding!!.actCode!!.threshold = 1
        binding!!.actCode!!.setAdapter(codeAdapter)

        if (!isUpdate)
            binding?.actCode!!.showDropDown()

        binding!!.actCode!!.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as DiagresponseContent?


            var getData = dignoisisMobAdapter?.getall()

            val check = getData!!.any { it!!.diagnosis_id == selectedPoi?.uuid.toString() }

            if (!check) {

                binding?.actCode?.setText(selectedPoi?.code)
                binding?.actTestName?.setText(selectedPoi?.name)
                binding?.diagnosisType?.setText("ICD 10")

                AddNew.diagnosis_id = selectedPoi?.uuid?.toString()
                AddNew.diagnosis_name = selectedPoi?.name
                AddNew.diagnosis_code = selectedPoi?.code
            } else {

                binding?.actCode!!.setText("")

                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()


            }

        }


    }


    val getICDSearchRerofitCallback = object : RetrofitCallback<DiagnosisListResponseModel> {
        override fun onSuccessfulResponse(response: Response<DiagnosisListResponseModel>) {

            if (IsTablet)
                setSearchICDDataTab(response.body()?.responseContents)
            else
                setSearchICDData(response.body()?.responseContents)

        }

        override fun onBadRequest(response: Response<DiagnosisListResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: DiagnosisListResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    DiagnosisListResponseModel::class.java
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

    private fun setSearchICDData(responseData: List<DiagnosisResponseContent?>?) {
        val codeAdapter = DiagnosisSearchListResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseData!!
        )
        binding!!.actIcd!!.threshold = 1
        binding!!.actIcd!!.setAdapter(codeAdapter)
        binding?.actIcd!!.showDropDown()
        binding!!.actIcd!!.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as DiagnosisResponseContent?

            var getData = dignoisisMobAdapter?.getall()

            val check = getData!!.any { it!!.diagnosis_id == selectedPoi?.uuid.toString() }

            if (!check) {


                binding?.actIcd?.setText("")

                dignoisisMobAdapter!!.addRow(
                    DiagnosisListData(
                        diagnosis_id = selectedPoi?.uuid?.toString(),
                        diagnosis_name = selectedPoi?.name,
                        diagnosis_code = selectedPoi?.code
                    )
                )

                hideDropDown()
            } else {

                binding?.actIcd?.setText("")

                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()


            }

        }


    }

    private fun setSearchICDDataTab(responseData: List<DiagnosisResponseContent?>?) {
        val codeAdapter = DiagnosisSearchListResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseData!!
        )
        binding!!.diagnosisSearchSnod!!.threshold = 1
        binding!!.diagnosisSearchSnod!!.setAdapter(codeAdapter)
        binding?.diagnosisSearchSnod!!.showDropDown()
        binding!!.diagnosisSearchSnod!!.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as DiagnosisResponseContent?


            val favmodel: FavouritesModel = FavouritesModel()
            favmodel.diagnosis_id = selectedPoi?.uuid?.toString()
            favmodel.diagnosis_name = selectedPoi?.name
            favmodel.diagnosis_code = selectedPoi?.code

            dignoisisAdapter?.addFavouritesInRow(favmodel)
            binding!!.diagnosisSearchSnod?.setText("")
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

    private fun toast(s: String) {

        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()

    }
}