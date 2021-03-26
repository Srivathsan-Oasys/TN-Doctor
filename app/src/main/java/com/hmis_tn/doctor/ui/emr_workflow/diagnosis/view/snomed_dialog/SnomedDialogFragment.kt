package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog

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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.databinding.SnomedDialogLayoutBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedChildDataResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedData
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedDataResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedParentDataResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class SnomedDialogFragment : DialogFragment() {


    private var content: String? = null

    private var binding: SnomedDialogLayoutBinding? = null

    private var viewModel: SnomedDialogviewModel? = null
    private var customProgressDialog: CustomProgressDialog? = null


    private var IsTablet = false
    private var utils: Utils? = null

    private var snomedAdapter: SnomedAdapter? = null

    private var snomedMobAdapter: SnomedMainMobAdapter? = null

    private var snomedChildAdapter: SnomedAdapter? = null

    private var snomedParentAdapter: SnomedAdapter? = null
    private var snomedMobChildAdapter: SnomedMainMobParentAdapter? = null

    private var snomedMobParentAdapter: SnomedMainMobParentAdapter? = null

    private var callbackSnomed: OnSnomedListener? = null

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
            DataBindingUtil.inflate(inflater, R.layout.snomed_dialog_layout, container, false)
        viewModel = SnomedDialogviewModelFactory(
            requireActivity().application
        )
            .create(SnomedDialogviewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(context)


        IsTablet = resources.getBoolean(R.bool.isTablet)

        binding?.clearCardView!!.setOnClickListener({
            dialog?.dismiss()
        })
        binding?.closeImageView!!.setOnClickListener {

            dialog?.dismiss()
        }

        viewModel!!.progress.observe(requireActivity(),
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })



        binding?.snoomed?.setOnClickListener {
            val snomedText = binding?.searchText?.text?.toString()
            if (snomedText?.isNotEmpty()!!) {
                viewModel!!.progress.observe(requireActivity(),
                    Observer { progress ->
                        if (progress == View.VISIBLE) {
                            customProgressDialog!!.show()
                        } else if (progress == View.GONE) {
                            customProgressDialog!!.dismiss()
                        }
                    })
                viewModel?.searchSnoomed(
                    snomedText,
                    searchsn0omedRetrofitCallback
                )
            }
        }

        //utils= Utils(context)
/*
        binding?.searchView?.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
            fun callSearch(query: String) {




                viewModel?.searchSnoomed(
                        query,
                        searchsn0omedRetrofitCallback
                )
            }

        })*/

/*
        binding?.snoomed?.setOnClickListener {

            viewModel?.searchSnoomed(
                binding?.searchView?.query.toString(),
                searchsn0omedRetrofitCallback
            )
        }

*/


        if (IsTablet) {
            snomedAdapter = SnomedAdapter(requireActivity(), ArrayList())

            val linearLayoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            binding?.mainList?.layoutManager = linearLayoutManager

            binding?.mainList?.adapter = snomedAdapter

            snomedAdapter!!.setOnViewClickListener(object : SnomedAdapter.OnViewClickListener {
                override fun onViewClick(data: SnomedData?) {


                    Log.i("click data", "" + data)
                    viewModel!!.searchParentSnoomed(data!!.ConceptId, searchParentRetrofitCallback)

                    viewModel!!.searchChildSnoomed(data.ConceptId, searchChildRetrofitCallback)


                }
            })


            val linearLayoutManager2 =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            snomedParentAdapter = SnomedAdapter(requireActivity(), ArrayList())

            binding?.parentList?.layoutManager = linearLayoutManager2

            binding?.parentList?.adapter = snomedParentAdapter

            snomedParentAdapter!!.setOnViewClickListener(object :
                SnomedAdapter.OnViewClickListener {

                override fun onViewClick(data: SnomedData?) {

                    viewModel!!.searchParentSnoomed(data!!.ConceptId, searchParentRetrofitCallback)

                    viewModel!!.searchChildSnoomed(data.ConceptId, searchChildRetrofitCallback)


                }
            })


            val linearLayoutManager3 =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            snomedChildAdapter = SnomedAdapter(requireActivity(), ArrayList())

            binding?.childList?.layoutManager = linearLayoutManager3

            binding?.childList?.adapter = snomedChildAdapter

            snomedChildAdapter!!.setOnViewClickListener(object : SnomedAdapter.OnViewClickListener {

                override fun onViewClick(data: SnomedData?) {

                    viewModel!!.searchParentSnoomed(data!!.ConceptId, searchParentRetrofitCallback)

                    viewModel!!.searchChildSnoomed(data.ConceptId, searchChildRetrofitCallback)


                }
            })


        } else {
            snomedMobAdapter = SnomedMainMobAdapter(requireActivity(), ArrayList())

            val linearLayoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            binding?.mainList?.layoutManager = linearLayoutManager

            binding?.mainList?.adapter = snomedMobAdapter

            snomedMobAdapter!!.setOnViewClickListener(object :
                SnomedMainMobAdapter.OnViewClickListener {
                override fun onViewClick(data: SnomedData?) {


                    Log.i("click data", "" + data)
                    viewModel!!.searchParentSnoomed(data!!.ConceptId, searchParentRetrofitCallback)

                    viewModel!!.searchChildSnoomed(data.ConceptId, searchChildRetrofitCallback)


                }
            })


            val linearLayoutManager2 =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)


            snomedMobParentAdapter = SnomedMainMobParentAdapter(requireActivity(), ArrayList())

            binding?.parentList?.layoutManager = linearLayoutManager2

            binding?.parentList?.adapter = snomedMobParentAdapter

            snomedMobParentAdapter!!.setOnViewClickListener(object :
                SnomedMainMobParentAdapter.OnViewClickListener {

                override fun onViewClick(data: SnomedData?) {

                    viewModel!!.searchParentSnoomed(data!!.ConceptId, searchParentRetrofitCallback)

                    viewModel!!.searchChildSnoomed(data.ConceptId, searchChildRetrofitCallback)


                }
            })


            val linearLayoutManager3 =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            snomedMobChildAdapter = SnomedMainMobParentAdapter(requireActivity(), ArrayList())

            binding?.childList?.layoutManager = linearLayoutManager3

            binding?.childList?.adapter = snomedMobChildAdapter

            snomedMobChildAdapter!!.setOnViewClickListener(object :
                SnomedMainMobParentAdapter.OnViewClickListener {

                override fun onViewClick(data: SnomedData?) {

                    viewModel!!.searchParentSnoomed(data!!.ConceptId, searchParentRetrofitCallback)

                    viewModel!!.searchChildSnoomed(data.ConceptId, searchChildRetrofitCallback)


                }
            })


        }

        binding!!.save.setOnClickListener {

            val arraysize = snomedParentAdapter?.getitemsize()
            if (arraysize != 0) {

                if (IsTablet) {
                    val getData = snomedParentAdapter!!.getFirstData()


                    Log.i("", "" + getData)

                    callbackSnomed!!.onSnomeddata(getData)

                    dialog?.dismiss()
                } else {


                    if (snomedMobAdapter!!.checkSelect()) {
                        val getData = snomedMobAdapter!!.getFirstData()


                        Log.i("", "" + getData)

                        callbackSnomed!!.onSnomeddata(getData)

                        dialog?.dismiss()


                    } else {

                        Toast.makeText(requireContext(), "Please select Any one", Toast.LENGTH_LONG)
                            .show()
                    }

                }


            } else {
                Toast.makeText(requireContext(), "List is empty", Toast.LENGTH_LONG).show()
            }

        }


        return binding?.root
    }

    val searchsn0omedRetrofitCallback = object : RetrofitCallback<SnomedDataResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<SnomedDataResponseModel>?) {

            Log.i("response", "" + responseBody!!.body()!!.Snomed_data)


            if (IsTablet) {
                snomedAdapter!!.setData(responseBody.body()!!.Snomed_data)

            } else {


                val datasize = responseBody.body()!!.Snomed_data.size

                if (datasize != 0) {

                    binding?.mainData?.visibility = View.GONE
                } else {

                    binding?.mainData?.visibility = View.VISIBLE
                    binding?.parentData?.visibility = View.VISIBLE
                    binding?.childData?.visibility = View.VISIBLE

                    snomedMobParentAdapter!!.setData(ArrayList())
                    snomedMobChildAdapter!!.setData(ArrayList())
                }

                snomedMobAdapter!!.setData(responseBody.body()!!.Snomed_data)
            }

        }

        override fun onBadRequest(response: Response<SnomedDataResponseModel>) {
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

    val searchParentRetrofitCallback = object : RetrofitCallback<SnomedParentDataResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<SnomedParentDataResponseModel>?) {

            Log.i("responsePar", "" + responseBody!!.body())

            if (IsTablet)
                snomedParentAdapter!!.setData(responseBody.body()!!.Snomed_Parent_data)
            else {


                val datasize = responseBody.body()!!.Snomed_Parent_data.size

                if (datasize != 0) {

                    binding?.parentData?.visibility = View.GONE
                } else {


                    binding?.parentData?.visibility = View.VISIBLE
                    binding?.childData?.visibility = View.VISIBLE

                    snomedMobChildAdapter!!.setData(ArrayList())
                }
                snomedMobParentAdapter!!.setData(responseBody.body()!!.Snomed_Parent_data)
            }

        }

        override fun onBadRequest(response: Response<SnomedParentDataResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: SnomedParentDataResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    SnomedParentDataResponseModel::class.java
                )
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


    val searchChildRetrofitCallback = object : RetrofitCallback<SnomedChildDataResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<SnomedChildDataResponseModel>?) {

            Log.i("response", "" + responseBody!!.body()!!.Snomed_Children_data)

            if (IsTablet)
                snomedChildAdapter!!.setData(responseBody.body()!!.Snomed_Children_data)
            else {


                val datasize = responseBody.body()!!.Snomed_Children_data.size

                if (datasize != 0) {

                    binding?.childData?.visibility = View.GONE
                } else {

                    binding?.childData?.visibility = View.VISIBLE

                }
                snomedMobChildAdapter!!.setData(responseBody.body()!!.Snomed_Children_data)
            }

        }

        override fun onBadRequest(response: Response<SnomedChildDataResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: SnomedChildDataResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    SnomedChildDataResponseModel::class.java
                )
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

    fun setOnSnomedRefreshListener(callback: OnSnomedListener) {
        this.callbackSnomed = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnSnomedListener {
        fun onSnomeddata(position: SnomedData)
    }
}