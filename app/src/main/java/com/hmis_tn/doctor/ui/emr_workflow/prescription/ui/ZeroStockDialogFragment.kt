package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ZeroStockDialogFragmentBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.ZeroStockResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model.ZeroStockViewModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model.ZeroStockViewModelFactory
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class ZeroStockDialogFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: ZeroStockViewModel? = null
    var binding: ZeroStockDialogFragmentBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    var zeroStockAdapter: ZeroStockAdapter? = null
    private var isLoadingPaginationAdapterCallback: Boolean = false
    private var currentPage = 0
    private var pageSize = 10
    private var customProgressDialog: CustomProgressDialog? = null
    private var TOTAL_PAGES: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (dialog.window != null)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.zero_stock_dialog_fragment, container, false)
        viewModel = ZeroStockViewModelFactory(
            requireActivity().application
        ).create(ZeroStockViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        viewModel?.getZeroStock(facility_UUID.toString(), zeroStockCallBack)
        zeroStockAdapter =
            ZeroStockAdapter(
                requireActivity(),
                ArrayList()
            )



        customProgressDialog = CustomProgressDialog(requireContext())

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.zeroStockRecyclerView?.layoutManager = linearLayoutManager
        binding?.zeroStockRecyclerView?.adapter = zeroStockAdapter


        binding?.zeroStockRecyclerView?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()

                            isLoader(true)

                            viewModel?.getZeroStockLoad(
                                currentPage,
                                pageSize,
                                zeroStockLoadCallBack
                            )

                        }
                    }
                }
            }
        })


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancel!!.setOnClickListener {
            dialog!!.dismiss()
        }


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
                zeroStockAdapter!!.getFilter().filter(query)
            }

        })


        return binding!!.root
    }

    val zeroStockCallBack = object : RetrofitCallback<ZeroStockResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<ZeroStockResponseModel>?) {
            if (isAdded)
                if (!responseBody?.body()?.responseContents.isNullOrEmpty()) {

                    TOTAL_PAGES = responseBody?.body()?.totalRecords!!
                    binding?.zeroStockRecyclerView?.visibility = View.VISIBLE
                    binding?.noDataText?.visibility = View.GONE

                    responseBody.body()?.responseContents?.let { zeroStockAdapter?.setFavAddItem(it) }

                    isLoadingPaginationAdapterCallback = false

                } else {
                    binding?.zeroStockRecyclerView?.visibility = View.GONE
                    binding?.noDataText?.visibility = View.VISIBLE
                }
            Log.e("zerStock", responseBody?.body()?.responseContents.toString())
        }

        override fun onBadRequest(errorBody: Response<ZeroStockResponseModel>?) {

            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: ZeroStockResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ZeroStockResponseModel::class.java
                )

                Log.e("zerStock", "BadRequest" + responseModel.message)

            } catch (e: Exception) {
                Log.e("zerStock", "BadRequest")
                e.printStackTrace()
            }

            // Log.e("postAllergyData", "BadRequest")

        }

        override fun onServerError(response: Response<*>?) {

            isLoadingPaginationAdapterCallback = false

            Log.e("zerStock", "server")
        }

        override fun onUnAuthorized() {
            isLoadingPaginationAdapterCallback = false
            Log.e("zerStock", "UnAuth")
        }

        override fun onForbidden() {
            isLoadingPaginationAdapterCallback = false
            Log.e("postAllergyData", "ForBidd")
        }

        override fun onFailure(s: String?) {
            isLoadingPaginationAdapterCallback = false
            Log.e("zerStock", s.toString())
        }

        override fun onEverytime() {
            isLoadingPaginationAdapterCallback = false


        }

    }


    val zeroStockLoadCallBack = object : RetrofitCallback<ZeroStockResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<ZeroStockResponseModel>?) {

            isLoader(false)
            if (isAdded)
                if (!responseBody?.body()?.responseContents.isNullOrEmpty()) {

                    responseBody?.body()?.responseContents?.let { zeroStockAdapter?.AddList(it) }

                    isLoadingPaginationAdapterCallback = false
                } else {

                }
            Log.e("zerStock", responseBody?.body()?.responseContents.toString())
        }

        override fun onBadRequest(errorBody: Response<ZeroStockResponseModel>?) {
            isLoader(false)
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: ZeroStockResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ZeroStockResponseModel::class.java
                )

                Log.e("zerStock", "BadRequest" + responseModel.message)

            } catch (e: Exception) {
                Log.e("zerStock", "BadRequest")
                e.printStackTrace()
            }

            // Log.e("postAllergyData", "BadRequest")

        }

        override fun onServerError(response: Response<*>?) {
            isLoader(false)
            isLoadingPaginationAdapterCallback = false
            Log.e("zerStock", "server")
        }

        override fun onUnAuthorized() {
            isLoader(false)
            isLoadingPaginationAdapterCallback = false
            Log.e("zerStock", "UnAuth")
        }

        override fun onForbidden() {
            isLoader(false)
            isLoadingPaginationAdapterCallback = false
            Log.e("postAllergyData", "ForBidd")
        }

        override fun onFailure(s: String?) {
            isLoader(false)
            isLoadingPaginationAdapterCallback = false
            Log.e("zerStock", s.toString())
        }

        override fun onEverytime() {
            isLoader(false)
            isLoadingPaginationAdapterCallback = false

        }

    }

    fun isLoader(process: Boolean) {

        /*   if (process) {
               customProgressDialog!!.show()
           } else {
               customProgressDialog!!.dismiss()
           }*/
    }

}