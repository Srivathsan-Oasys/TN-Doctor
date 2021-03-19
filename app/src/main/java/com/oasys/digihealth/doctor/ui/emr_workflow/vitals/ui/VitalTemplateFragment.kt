package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentVitalsTemplateBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.TemplateDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalsTemplateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.responseedittemplatevitual.ResponseEditTemplate
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.view_model.VitalsViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.view_model.VitalsViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class VitalTemplateFragment(
    private val adapterAddRow: (TemplateDetail) -> Unit,
    private val adapterDeleteRow: (Int) -> Unit
) : Fragment(), ManageVitalTemplateFragment.OnTemplateRefreshListener {

    private var binding: FragmentVitalsTemplateBinding? = null
    private var viewModel: VitalsViewModel? = null
    private var utils: Utils? = null

    private var customdialog: Dialog? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var patientId: Int? = null
    private var departmentUuid: Int? = null
    private var encounterType: Int? = null
    private var encounterDoctorUuid: Int? = null
    private var encounterUuid: Int? = null
    private var wardstoremasteruuId: Int? = null

    private var responseContents: String? = ""
    lateinit var vitalsTemplatesAdapter: VitalsTemplateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_vitals_template,
            container,
            false
        )
        viewModel = VitalsViewModelFactory(
            requireActivity().application
        ).create(VitalsViewModel::class.java)

        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        encounterUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
        wardstoremasteruuId = appPreferences?.getInt(AppConstants.Ward_MASTER_UUID)

        initViews()
        listeners()

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        getVitalsTemplate()
    }

    private fun initViews() {
        initFavouritesAdapter()
    }

    private fun listeners() {
        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog = ManageVitalTemplateFragment()
            dialog.show(ft, "Tag")
        }


    }

    private fun initFavouritesAdapter() {
        vitalsTemplatesAdapter =
            VitalsTemplateAdapter(requireContext())
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        var gridLayoutManager: GridLayoutManager? = null
        if (tabletSize) {
            gridLayoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        } else {
            gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        /*val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)*/
        binding?.vitalsrecyclerView?.layoutManager = gridLayoutManager
        binding?.vitalsrecyclerView?.adapter = vitalsTemplatesAdapter

        vitalsTemplatesAdapter.setOnItemViewClickListener(object :
            VitalsTemplateAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: TemplateDetail?) {
                Log.i("", "" + responseContent)
                viewModel?.getTemplateDetails(
                    responseContent?.uuid,
                    facilityId,
                    departmentUuid,
                    getTemplateRetrofitResponseCallback
                )
            }
        })

        vitalsTemplatesAdapter.setOnItemDeleteClickListener(object :
            VitalsTemplateAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: TemplateDetail?
            ) {
                Log.i("", "" + responseContent)
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
                    "${drugNmae.text} '" + responseContent?.name + "' Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {

                    viewModel!!.deleteTemplate(
                        facilityId,
                        responseContent?.uuid!!,
                        deleteRetrofitResponseCallback
                    )
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })

        vitalsTemplatesAdapter.setOnItemClickListener(object :
            VitalsTemplateAdapter.OnItemClickListener {
            override fun onItemClick(
                templateLists: ArrayList<TemplateDetail>,
                position: Int,
                selected: Boolean,
                templatesId: Int
            ) {
                vitalsTemplatesAdapter.updateSelectStatus(position, selected)
                if (!selected) {
                    for (i in templateLists[position].template_master_details.indices) {

                        val templateDetail: TemplateDetail = TemplateDetail()
                        templateDetail.name =
                            templateLists[position].template_master_details[i].vital_master.name
                        templateDetail.uuid =
                            templateLists[position].template_master_details[i].vital_master.uuid
                        templateDetail.uom_master_uuid =
                            templateLists[position].template_master_details[i].vital_master.uom_master_uuid
                        adapterAddRow(templateDetail)

                    }
                } else {

                    for (i in templateLists[position].template_master_details.indices) {
                        adapterDeleteRow(templateLists[position].template_master_details[i].vital_master.uuid)
                    }
                }
            }
        })


        val searchText =
            binding?.searchView?.findViewById(R.id.search_src_text) as TextView
        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        searchText.typeface = tf
        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                vitalsTemplatesAdapter.getFilter().filter(query)
            }

        })
    }

    private fun getVitalsTemplate() {
        viewModel!!.getvitalsTemplate(
            facilityId,
            departmentUuid,
            vitalsTemplateRetrofitCallBack
        )
    }

    private val vitalsTemplateRetrofitCallBack =
        object : RetrofitCallback<VitalsTemplateResponseModel> {

            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(response: Response<VitalsTemplateResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                vitalsTemplatesAdapter.refreshList(response.body()?.responseContents?.templateDetails)

                Log.e("TempList", response.body()?.responseContents.toString())

            }

            override fun onBadRequest(response: Response<VitalsTemplateResponseModel>) {
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

    /*
    Get Template
   */
    var getTemplateRetrofitResponseCallback = object : RetrofitCallback<ResponseEditTemplate> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseEditTemplate>?) {

            //Log.e("ManiCheck",""+responseBody?.body()?.responseContent)
            val ft = childFragmentManager.beginTransaction()
            val labtemplatedialog = ManageVitalTemplateFragment()
            val bundle = Bundle()
            bundle.putParcelable(
                AppConstants.RESPONSECONTENT,
                responseBody?.body()?.responseContent?.get(0)
            )
            labtemplatedialog.arguments = bundle
            labtemplatedialog.show(ft, "Tag")
        }

        override fun onBadRequest(errorBody: Response<ResponseEditTemplate>?) {


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

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
//            viewModel!!.getvitalsTemplate(
//                FaciltyID,
//                department_uuid,
//                vitalsTemplateRetrofitCallBack
//            )

            customdialog!!.dismiss()

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

    override fun onTemplateID(position: Int) {

    }

    override fun onRefreshList() {
        getVitalsTemplate()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageVitalTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
    }
}
