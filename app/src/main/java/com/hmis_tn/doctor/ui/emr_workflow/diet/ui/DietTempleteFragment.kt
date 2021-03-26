package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

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
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentDietTempleteBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.DietTemplateDeatils
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.ResponseDietGetTemplateDetails
import com.hmis_tn.doctor.ui.emr_workflow.diet.view_model.DietViewModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.view_model.DietViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempDetails
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class DietTempleteFragment : Fragment() {

    private var customdialog: Dialog? = null

    @SuppressLint("ClickableViewAccessibility")
    private var binding: FragmentDietTempleteBinding? = null
    private var viewModel: DietViewModel? = null
    lateinit var templeteAdapter: DietTempleteAdapter
    var mCallback: TempleteClickedListener? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var department_uuid: Int? = null
    private var facility_UUID: Int? = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_diet_templete,
            container,
            false
        )
        viewModel = DietViewModelFactory(
            requireActivity().application
        ).create(DietViewModel::class.java)
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

        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val manageLabTemplateFragment = ManageDietTemplateFragment()
            manageLabTemplateFragment.show(ft, "Tag")
        }
        initTempleteAdapter()
        viewModel!!.getTemplete(getTempleteRetrofitCallBack)
        return binding!!.root
    }


    private fun initTempleteAdapter() {
        templeteAdapter =
            DietTempleteAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = templeteAdapter
        templeteAdapter.setOnItemClickListener(object :
            DietTempleteAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: List<DietTemplateDeatils?>?,
                position: Int,
                selected: Boolean,
                id: Int
            ) {
                templeteAdapter.updateSelectStatus(position, selected)
                mCallback?.sendTemplete(responseContent!!, position, selected, id)
            }
        })

        templeteAdapter.setOnItemViewClickListener(object :
            DietTempleteAdapter.OnItemViewClickListner {
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
            DietTempleteAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: TempDetails?
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
                    "${drugNmae.text} '" + responseContent?.template_name + "' Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    viewModel!!.deleteTemplate(
                        facility_UUID,
                        responseContent!!.template_id!!,
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
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel>) {
                val responseContents = Gson().toJson(response.body()?.responseContents)
                Log.i("", "" + responseContents)
                templeteAdapter.refreshList(response.body()?.responseContents?.templates_list)
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


    interface TempleteClickedListener {
        fun sendTemplete(
            templeteDetails: List<DietTemplateDeatils?>?,
            position: Int,
            selected: Boolean,
            id: Int
        )
    }

    fun setOnTextClickedListener(callback: DietTempleteFragment.TempleteClickedListener) {
        this.mCallback = callback
    }


    /*  override fun ClearTemplateParticularPosition(position: Int) {
          templeteAdapter.refreshParticularData(position)
          //  templeteAdapter.refreshParticularData(position)
      }

      override fun ClearAllData() {

          templeteAdapter.refreshAllData()

      }

      override fun GetTemplateDetails() {

          viewModel!!.getTemplete(getTempleteRetrofitCallBack)
      }*/

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)
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


    /*
     Get Template
    */
    var getTemplateRetrofitResponseCallback =
        object : RetrofitCallback<ResponseDietGetTemplateDetails> {
            override fun onSuccessfulResponse(responseBody: Response<ResponseDietGetTemplateDetails>?) {

                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = ManageDietTemplateFragment()
                val bundle = Bundle()
                bundle.putParcelable(
                    AppConstants.RESPONSECONTENT,
                    responseBody?.body()?.responseContent
                )
                labtemplatedialog.arguments = bundle
                labtemplatedialog.show(ft, "Tag")

            }

            override fun onBadRequest(errorBody: Response<ResponseDietGetTemplateDetails>?) {

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


    /*  override fun onTemplateID(position: Int) {

      }

      override fun onTemplateRefreshList() {

          viewModel!!.getTemplete(getTempleteRetrofitCallBack)
      }


      override fun onRefreshList() {
          viewModel!!.getTemplete(getTempleteRetrofitCallBack)
      }*/

}