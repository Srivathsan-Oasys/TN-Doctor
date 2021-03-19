package com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentTempleteBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.LabViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseLabGetTemplateDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.saveTemplate.ManageLabSaveTemplateFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model.LabViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.LabDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TemplatesLab
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class LabTempleteFragment : Fragment(), ClearTemplateParticularPositionListener,
    ManageLabTemplateFragment.OnTemplateRefreshListener,
    ManageLabSaveTemplateFragment.OnSaveTemplateRefreshListener {

    private var customdialog: Dialog? = null

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentTempleteBinding? = null
    private var viewModel: LabViewModel? = null
    lateinit var templeteAdapter: LabTempleteAdapter
    var mCallback: TempleteClickedListener? = null
    private var utils: Utils? = null
    var labadapter: LabAdapter? = null
    var appPreferences: AppPreferences? = null
    private var department_uuid: Int? = null

    private var facility_UUID: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_templete,
                container,
                false
            )
        viewModel = LabViewModelFactory(
            requireActivity().application
        ).create(LabViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        utils = Utils(requireContext())
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
                templeteAdapter.getFilter().filter(query)
            }
        })
        binding?.manageTemplateCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val manageLabTemplateFragment = ManageLabTemplateFragment()
            manageLabTemplateFragment.show(ft, "Tag")
        }
        initTempleteAdapter()
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
        return binding!!.root
    }

    private fun initTempleteAdapter() {
        templeteAdapter =
            LabTempleteAdapter(requireContext())

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        var gridLayoutManager: GridLayoutManager? = null
        if (tabletSize) {
            gridLayoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        } else {
            gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        /* val gridLayoutManager =
             GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)*/
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = templeteAdapter
        templeteAdapter.setOnItemClickListener(object :
            LabTempleteAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: List<LabDetail?>?,
                position: Int,
                selected: Boolean,
                id: Int
            ) {
                templeteAdapter.updateSelectStatus(2, position, selected)
                mCallback?.sendTemplete(responseContent!!, position, selected, id)
            }
        })

        templeteAdapter.setOnItemViewClickListener(object :
            LabTempleteAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: TempDetails?) {

                viewModel?.getTemplateDetails(
                    responseContent?.template_id,
                    facility_UUID,
                    department_uuid,
                    getTemplateRetrofitResponseCallback
                )
            }
        })
        templeteAdapter.setOnItemDeleteClickListener(object :
            LabTempleteAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: TemplatesLab?
            ) {
                viewModel!!.deleteTemplate(
                    facility_UUID,
                    responseContent?.temp_details!!.template_id!!,
                    deleteRetrofitResponseCallback
                )
            }
        })
    }

    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)

                templeteAdapter.refreshList(response.body()?.responseContents?.templates_lab_list)
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

    //defining Interface
    interface TempleteClickedListener {
        fun sendTemplete(
            templeteDetails: List<LabDetail?>?,
            position: Int,
            selected: Boolean,
            id: Int
        )
    }

    fun setOnTextClickedListener(callback: TempleteClickedListener) {
        this.mCallback = callback
    }

    override fun ClearTemplateParticularPosition(position: Int) {
        templeteAdapter.refreshParticularData(position)
    }


    override fun ClearAllData() {

        templeteAdapter.refreshAllData()

    }

    override fun GetTemplateDetails() {
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
    }

    override fun updatestaus(favitem: Int, position: Int, select: Boolean) {
        templeteAdapter.updateSelectStatus(favitem, position, select)
    }

    override fun checkanduncheck(dataList: ArrayList<Int>) {
        templeteAdapter.updateData(dataList)
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)
            Toast.makeText(requireContext(), responseBody?.body()?.message, Toast.LENGTH_SHORT)
                .show()

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

    override fun onTemplateRefreshList() {
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
    }


    override fun onRefreshList() {
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageLabTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
        if (childFragment is ManageLabSaveTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
    }

    /*
      Get Template
     */
    var getTemplateRetrofitResponseCallback =
        object : RetrofitCallback<ResponseLabGetTemplateDetails> {
            override fun onSuccessfulResponse(responseBody: Response<ResponseLabGetTemplateDetails>?) {

                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = ManageLabTemplateFragment()
                val bundle = Bundle()
                bundle.putParcelable(
                    AppConstants.RESPONSECONTENT,
                    responseBody?.body()?.responseContent
                )
                labtemplatedialog.arguments = bundle
                labtemplatedialog.show(ft, "Tag")

            }

            override fun onBadRequest(errorBody: Response<ResponseLabGetTemplateDetails>?) {

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


}


