package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentRadiologyTempleteBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseLabGetTemplateDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.LabDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TemplatesLab
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.saveTemplate.ManageRadiologySaveTemplateFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.RadiologyViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.RadiologyViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class RadiologyTempleteFragment : Fragment(), ClearTemplateParticularPositionListener,
    ManageRadiologyTemplateFragment.OnTemplateRefreshListener,
    ManageRadiologySaveTemplateFragment.OnSaveTemplateRefreshListener {
    private var department_uuid: Int? = 0
    private var dialog: Dialog? = null

    @SuppressLint("ClickableViewAccessibility")
    private var customdialog: Dialog? = null
    var binding: FragmentRadiologyTempleteBinding? = null
    private var viewModel: RadiologyViewModel? = null
    lateinit var templeteAdapter: RadiologyTempleteAdapter
    var mCallback: RadiologyTempleteClickedListener? = null
    private var utils: Utils? = null
    var labadapter: RadiologyAdapter? = null

    var appPreferences: AppPreferences? = null

    private var facility_UUID: Int? = 0


    var displayOrder: ArrayList<Int> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_radiology_templete,
                container,
                false
            )
        viewModel = RadiologyViewModelFactory(
            requireActivity().application
        ).create(RadiologyViewModel::class.java)
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
                binding!!.searchView.clearFocus()
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
        binding!!.manageRadioTeplateCardView.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val managedialog = ManageRadiologyTemplateFragment()
            managedialog.show(ft, "Tag")
        }
        initTempleteAdapter()
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
        return binding!!.root
    }

    private fun initTempleteAdapter() {
        templeteAdapter =
            RadiologyTempleteAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = templeteAdapter
        templeteAdapter.setOnItemClickListener(object :
            RadiologyTempleteAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: List<LabDetail?>?,
                position: Int,
                selected: Boolean,
                id: Int
            ) {
                // templeteAdapter.updateSelectStatus(2,position, selected)
                mCallback?.sendTemplete(responseContent, position, selected, id)
            }
        })

        templeteAdapter.setOnItemViewClickListener(object :
            RadiologyTempleteAdapter.OnItemViewClickListner {
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
            RadiologyTempleteAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: TemplatesLab?
            ) {
                viewModel!!.deleteTemplate(
                    facility_UUID,
                    responseContent?.temp_details!!.template_id!!,
                    deleteRetrofitResponseCallback
                )
                /*   customdialog = Dialog(requireContext())
                   customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                   customdialog!!.setCancelable(false)
                   customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                   val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageViewcustomdialog

                   closeImageView.setOnClickListener {
                       customdialog!!.dismiss()
                   }
                   val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                   drugNmae.text =
                       "${drugNmae.text.toString()} '" + responseContent!!.temp_details?.template_name + "' Record ?"

                   val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                   val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                   yesBtn.setOnClickListener {
                       viewModel!!.deleteTemplate(
                           facility_UUID,
                           responseContent.temp_details!!.template_id!!,
                           deleteRetrofitResponseCallback
                       )
                   }
                   noBtn.setOnClickListener {
                       customdialog!!.dismiss()
                   }
                   customdialog!!.show()*/
            }

        })
    }

    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)


                val resposeData = response.body()?.responseContents?.templates_radiology_list

                displayOrder.clear()


                for (i in resposeData!!.indices) {

                    displayOrder.add(resposeData[i]!!.temp_details!!.template_displayorder!!)

                }

                templeteAdapter.refreshList(response.body()?.responseContents?.templates_radiology_list)
            }

            override fun onBadRequest(response: Response<TempleResponseModel>) {

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
    interface RadiologyTempleteClickedListener {
        fun sendTemplete(
            templeteDetails: List<LabDetail?>?,
            position: Int,
            selected: Boolean,
            id: Int
        )
    }

    fun setOnTextClickedListener(callback: RadiologyTempleteClickedListener) {
        this.mCallback = callback
    }

    override fun ClearTemplateParticularPosition(position: Int) {
        templeteAdapter.refreshParticularData(position)
    }

    override fun ClearAllData() {

    }

    override fun GetTemplateDetails() {

    }

    override fun updatestaus(favitem: Int, position: Int, select: Boolean) {

    }


    override fun checkanduncheck(dataList: ArrayList<Int>) {
        templeteAdapter.updateData(dataList)
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)
            //    customdialog!!.dismiss()

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

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageRadiologyTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
        if (childFragment is ManageRadiologySaveTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
    }


    /*
        override fun onAttachFragment(childFragment: Fragment) {
            super.onAttachFragment(childFragment)
            if (childFragment is ManageRadiologyTemplateFragment) {
                childFragment.setOnTemplateRefreshListener(this)
            }
            if (childFragment is ManageRadiologySaveTemplateFragment) {
                childFragment.setOnTemplateRefreshListener(this)
            }

        }
    */
    /*
      Get Template
     */
    var getTemplateRetrofitResponseCallback =
        object : RetrofitCallback<ResponseLabGetTemplateDetails> {
            override fun onSuccessfulResponse(responseBody: Response<ResponseLabGetTemplateDetails>?) {
                val ft = childFragmentManager.beginTransaction()
                Log.i("", "" + responseBody?.body()?.responseContent)
                val radiologytemplatedialog = ManageRadiologyTemplateFragment()
                val bundle = Bundle()
                bundle.putParcelable(
                    AppConstants.RESPONSECONTENT,
                    responseBody?.body()?.responseContent
                )
                bundle.putBoolean("status", false)
                bundle.putIntegerArrayList("DisplayOrder", displayOrder)
                radiologytemplatedialog.arguments = bundle
                radiologytemplatedialog.show(ft, "Tag")
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

    override fun onRefreshList() {
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
    }

    fun refresh() {
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
    }
}