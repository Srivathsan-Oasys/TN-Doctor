package com.oasys.digihealth.doctor.ui.quick_reg.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogSearchListBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.dashboard.model.CovidSalutationTitleResponseModel
import com.oasys.digihealth.doctor.ui.dashboard.model.SalutationresponseContent
import com.oasys.digihealth.doctor.ui.quick_reg.model.QuickSearchResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.QuickSearchresponseContent
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.QuickRegistrationViewModel
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.QuickRegistrationViewModelFactory
import com.oasys.digihealth.doctor.ui.quickregistration.model.response.PDSResponseModule
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class SearchPatientDialogFragment : DialogFragment() {

    private val arraylistresponse: QuickSearchresponseContent = QuickSearchresponseContent()
    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: QuickRegistrationViewModel? = null
    var binding: DialogSearchListBinding? = null

    var salutaioData: ArrayList<SalutationresponseContent> = ArrayList()

    var professtionalData: ArrayList<SalutationresponseContent> = ArrayList()

    private var SalutaionList = mutableMapOf<Int, String>()

    private var ProfestioanlList = mutableMapOf<Int, String>()
    var callbackOrderProcess: OnOrderProcessListener? = null
    private var mAdapter: SearchPatientAdapter? = null
    var dialogListener: DialogListener? = null
    var arrayListPatientList: ArrayList<QuickSearchresponseContent?>? = ArrayList()
    var mobilenumber: String? = ""
    var pinumber: String? = ""
    var querysearch: String? = ""
    var PDSNumber: String? = ""
    var aatherNo: String? = ""

    /////Pagination

    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var isLoadingPaginationAdapterCallback: Boolean = false
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_search_list, container, false)
        viewModel = QuickRegistrationViewModelFactory(
            requireActivity().application

        )
            .create(QuickRegistrationViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.searchListrecycleview!!.layoutManager = layoutmanager
        mAdapter = SearchPatientAdapter(requireContext(), ArrayList())
        binding?.searchListrecycleview!!.adapter = mAdapter



        binding?.searchListrecycleview?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            viewModel?.searchPatientSecond(
                                querysearch!!,
                                pinumber!!,
                                mobilenumber!!, pageSize, currentPage,
                                patientSearchSecondRetrofitCallBack
                            )

                        }

                    }

                }
            }
        })


        viewModel?.getCovidNameTitleList(
            facilitylevelID!!,
            covidSalutationResponseCallback
        )


        val args = arguments
        if (args == null) {

            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {

            PDSNumber = args.getString("PDS")
            if (PDSNumber != null) {
                if (PDSNumber?.length!! > 0) {
                    viewModel?.searchPDSPatient(
                        PDSNumber!!,
                        patientPDSSearchRetrofitCallBack
                    )
                }
            } else {
                mobilenumber = args.getString("mobile")
                pinumber = args.getString("PIN")
                querysearch = args.getString("search")
                aatherNo = args.getString("aatherNo")

                viewModel?.searchPatient(
                    querysearch!!,
                    pinumber!!,
                    mobilenumber!!, pageSize, currentPage, aatherNo!!,
                    patientSearchRetrofitCallBack
                )
            }


            /*        val responseDataQuickSearch = args.getParcelableArrayList<QuickSearchresponseContent>(AppConstants.RESPONSECONTENT)
                    binding?.patientCount?.setText("Patient(s) " + responseDataQuickSearch!!.size)
                   if(responseDataQuickSearch!=null)
                    {
                        arrayListPatientList = responseDataQuickSearch
                    }
                   mAdapter?.setData(arrayListPatientList)
       */
        }

        mAdapter!!.setOnItemClickListener(object :
            SearchPatientAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: QuickSearchresponseContent) {
                Log.i("", "" + responseContent)
                /*    val dialogListener = activity as DialogListener?
                    dialogListener!!.onFinishEditDialog(responseContent)*/

                callbackOrderProcess?.onRefreshOrderList(responseContent)
                dismiss()
            }
        })
        return binding?.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }

    interface DialogListener {
        fun onFinishEditDialog(responseData: QuickSearchresponseContent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { /* this line is main difference for fragment to fragment communication & fragment to activity communication
            fragment to fragment: onInputListener = (OnInputListener) getTargetFragment();
            fragment to activity: onInputListener = (OnInputListener) getActivity();
             */
            dialogListener = targetFragment as DialogListener?
        } catch (e: ClassCastException) {
        }
    }


    fun setOnOrderProcessRefreshListener(callback: OnOrderProcessListener) {
        callbackOrderProcess = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnOrderProcessListener {
        fun onRefreshOrderList(responseData: QuickSearchresponseContent)
    }

    val patientSearchRetrofitCallBack = object : RetrofitCallback<QuickSearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<QuickSearchResponseModel>) {

            if (response.body()?.responseContents?.isNotEmpty()!!) {
//                viewModel?.errorTextVisibility?.value = 8

                binding?.patientCount?.text = "Patient(s)" + response.body()?.totalRecords

                TOTAL_PAGES =
                    Math.ceil(response.body()!!.totalRecords!!.toDouble() / 10).toInt()
                val arraylistsearchdata = response.body()?.responseContents


                if (response.body()!!.responseContents!!.isNotEmpty()) {
                    isLoadingPaginationAdapterCallback = false
                    mAdapter!!.addAll(response.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        binding?.progressbar!!.visibility = View.VISIBLE
                        mAdapter!!.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        binding?.progressbar!!.visibility = View.GONE
                        mAdapter!!.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }

                } else {
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
                if (response.body()!!.totalRecords!! < 11) {
                    binding?.progressbar!!.visibility = View.GONE
                }


            } else {
                Toast.makeText(context!!, "No Record Found", Toast.LENGTH_SHORT).show()
                binding?.progressbar!!.visibility = View.GONE
            }


        }

        override fun onBadRequest(response: Response<QuickSearchResponseModel>) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: QuickSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    QuickSearchResponseModel::class.java
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
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            isLoadingPaginationAdapterCallback = false
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
            binding?.progressbar!!.visibility = View.GONE

        }
    }


    val patientSearchSecondRetrofitCallBack = object : RetrofitCallback<QuickSearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<QuickSearchResponseModel>) {

            if (response.body()?.responseContents?.isNotEmpty()!!) {
                if (response.body()?.responseContents!!.isNotEmpty()) {
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLoadingPaginationAdapterCallback = false

                    mAdapter?.addAll(response.body()!!.responseContents)

                    println("testing for two  = $currentPage--$TOTAL_PAGES")

                    if (currentPage < TOTAL_PAGES) {
                        binding?.progressbar!!.visibility = View.VISIBLE
                        mAdapter?.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                        println("testing for four  = $currentPage--$TOTAL_PAGES")
                    } else {
                        isLastPage = true
                        binding?.progressbar!!.visibility = View.GONE
//                    visitHistoryAdapter.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                        println("testing for five  = $currentPage--$TOTAL_PAGES")
                    }

                } else {
                    binding?.progressbar!!.visibility = View.GONE
                    println("testing for six  = $currentPage--$TOTAL_PAGES")
                    mAdapter?.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }

            } else {

                binding?.progressbar!!.visibility = View.GONE
            }


        }

        override fun onBadRequest(response: Response<QuickSearchResponseModel>) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: QuickSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    QuickSearchResponseModel::class.java
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
            binding?.progressbar!!.visibility = View.GONE

        }
    }

    ////PDS

    val patientPDSSearchRetrofitCallBack = object : RetrofitCallback<PDSResponseModule> {
        override fun onSuccessfulResponse(response: Response<PDSResponseModule>) {

            if (response.body()?.pdsResponseContents?.isNotEmpty()!!) {
//                viewModel?.errorTextVisibility?.value = 8
                binding?.patientCount?.text = "Patient's " + response.body()?.totalRecords
                binding?.pinheader?.text = "PDS"
                binding?.dateheader?.visibility = View.GONE
                for (i in response.body()?.pdsResponseContents!!.indices) {
                    val quickregisterresponsecontent: QuickSearchresponseContent =
                        QuickSearchresponseContent()
                    quickregisterresponsecontent.uhid =
                        response.body()?.pdsResponseContents?.get(i)?.ufc
                    quickregisterresponsecontent.first_name =
                        response.body()?.pdsResponseContents?.get(i)?.name
                    quickregisterresponsecontent.gender =
                        response.body()?.pdsResponseContents?.get(i)?.gender
                    quickregisterresponsecontent.age =
                        response.body()?.pdsResponseContents?.get(i)?.age?.toInt()
                    arrayListPatientList!!.add(quickregisterresponsecontent)
                }
                mAdapter!!.setDataListAdd(true, arrayListPatientList)
            } else {
                Toast.makeText(context!!, "No Record Found", Toast.LENGTH_SHORT).show()
                binding?.progressbar!!.visibility = View.GONE
            }


        }

        override fun onBadRequest(response: Response<PDSResponseModule>) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: PDSResponseModule
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PDSResponseModule::class.java
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
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            isLoadingPaginationAdapterCallback = false
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
            binding?.progressbar!!.visibility = View.GONE

        }
    }


    val covidSalutationResponseCallback =
        object : RetrofitCallback<CovidSalutationTitleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidSalutationTitleResponseModel>?) {

                val res = responseBody?.body()?.responseContents
                //     A1selectSalutationUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!


                salutaioData.clear()

                professtionalData.clear()


                var defult: SalutationresponseContent = SalutationresponseContent()

                defult.uuid = 0
                defult.name = ""

                val suldefult: SalutationresponseContent = SalutationresponseContent()

                suldefult.uuid = 0
                suldefult.name = ""


                salutaioData.add(suldefult)
                professtionalData.add(defult)
                for (i in res!!.indices) {


                    if (res[i]?.type_code == "1") {
                        salutaioData.add(res[i]!!)
                    } else {

                        professtionalData.add(res[i]!!)
                    }
                }

                SalutaionList =
                    salutaioData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()

                mAdapter?.setTitle(SalutaionList)

                ProfestioanlList =
                    professtionalData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()


            }

            override fun onBadRequest(errorBody: Response<CovidSalutationTitleResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidSalutationTitleResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidSalutationTitleResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.req!!
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

            override fun onFailure(failure: String?) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

}

