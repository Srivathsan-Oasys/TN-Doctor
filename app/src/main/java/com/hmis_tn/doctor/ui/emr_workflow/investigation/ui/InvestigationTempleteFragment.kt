package com.hmis_tn.doctor.ui.emr_workflow.investigation.ui


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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentInvTempleteBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestDetail
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationGetTemplateDetailsResponse
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationTemplateResponse
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.TemplatesInvest
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class InvestigationTempleteFragment : Fragment(), ClearTemplateParticularPositionListener,
    ManageInvestTemplateFragment.OnTemplateRefreshListener,
    ManageISavenvestTemplateFragment.OnTemplateRefreshListener {
    @SuppressLint("ClickableViewAccessibility")
    private var customdialog: Dialog? = null

    var binding: FragmentInvTempleteBinding? = null
    private var viewModel: InvestigationViewModel? = null
    lateinit var templeteAdapter: InvestigationTempleteAdapter
    var mCallback: TempleteClickedListener? = null
    private var utils: Utils? = null
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
                R.layout.fragment_inv_templete,
                container,
                false
            )

        viewModel = InvestigationViewModelFactory(
            requireActivity().application
        ).create(InvestigationViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)


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
            val manageLabTemplateFragment = ManageInvestTemplateFragment()
            manageLabTemplateFragment.show(ft, "Tag")
        }

        initTempleteAdapter()
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
        return binding!!.root
    }

    private fun initTempleteAdapter() {
        templeteAdapter =
            InvestigationTempleteAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = templeteAdapter
        templeteAdapter.setOnItemClickListener(object :
            InvestigationTempleteAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: List<InvestDetail?>?,
                position: Int,
                selected: Boolean
            ) {

                if (isTablet(requireContext())) {
                    templeteAdapter.updateSelectStatus(2, position, selected)
                }
                mCallback?.sendTemplete(responseContent!!, position, selected, id)
            }
        })

        templeteAdapter.setOnItemViewClickListener(object :
            InvestigationTempleteAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: com.hmis_tn.doctor.ui.emr_workflow.investigation.model.TempDetails?) {

                viewModel?.getTemplateDetails(
                    responseContent?.template_id,
                    facility_UUID,
                    department_uuid,
                    getTemplateRetrofitResponseCallback
                )
            }
        })


        templeteAdapter.setOnItemDeleteClickListener(object :
            InvestigationTempleteAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: TemplatesInvest?
            ) {
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
                    "${drugNmae.text} '" + responseContent?.temp_details?.template_name + "' Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    viewModel!!.deleteTemplate(
                        facility_UUID,
                        responseContent?.temp_details!!.template_id,
                        deleteRetrofitResponseCallback
                    )
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })
    }

    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<InvestigationTemplateResponse> {
            override fun onSuccessfulResponse(response: Response<InvestigationTemplateResponse>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.templates_invest_list?.isNotEmpty()!!) {
                    templeteAdapter.refreshList(response.body()?.responseContents?.templates_invest_list)
                }
            }

            override fun onBadRequest(response: Response<InvestigationTemplateResponse>) {
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
            templeteDetails: List<InvestDetail?>?,
            position: Int,
            selected: Boolean,
            id: Int
        )
    }

    fun setOnTextClickedListener(callback: TempleteClickedListener) {
        this.mCallback = callback
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)

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


    var getTemplateRetrofitResponseCallback =
        object : RetrofitCallback<InvestigationGetTemplateDetailsResponse> {
            override fun onSuccessfulResponse(responseBody: Response<InvestigationGetTemplateDetailsResponse>?) {

                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = ManageInvestTemplateFragment()
                val bundle = Bundle()
                bundle.putString("status", "Edit")
                bundle.putParcelable(
                    AppConstants.RESPONSECONTENT,
                    responseBody?.body()?.responseContent
                )
                labtemplatedialog.arguments = bundle
                labtemplatedialog.show(ft, "Tag")

            }

            override fun onBadRequest(errorBody: Response<InvestigationGetTemplateDetailsResponse>?) {

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
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageInvestTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
        if (childFragment is ManageISavenvestTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
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

}