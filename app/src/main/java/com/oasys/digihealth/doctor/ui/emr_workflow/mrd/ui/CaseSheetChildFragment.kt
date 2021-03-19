package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.CaseSheetChildFragmentBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.CaseSheetResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.CaseSheetResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.view_model.MRDViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.view_model.MRDViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class CaseSheetChildFragment : Fragment() {
    lateinit var drugNmae: TextView

    private var facility_id: Int = 0
    private var patient_uhid: String = ""
    var binding: CaseSheetChildFragmentBinding? = null
    private var departmentID: Int? = 0
    private var searchPosition: Int? = 0
    private var utils: Utils? = null
    private var responseContents: String? = ""
    var searchposition: Int = 0
    private var viewModel: MRDViewModel? = null
    private var allergyAdapter: CaseSheetAdapter? = null
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var isLoadingPaginationAdapterCallback: Boolean = false


    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var selectedSearchPosition = -1
    private var appPreferences: AppPreferences? = null
    private var deparment_UUID: Int? = 0
    var gson: Gson? = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.case_sheet_child_fragment,
            container,
            false
        )

        viewModel = MRDViewModelFactory(
            requireActivity().application
        ).create(MRDViewModel::class.java)

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        var userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        patient_uhid = appPreferences?.getString(AppConstants.PATIENT_UHID)!!
        utils = Utils(requireContext())
        allergyAdapter()



        trackMrdVisit(utils?.getEncounterType())
        viewModel?.getCaseSheet_Records(
            patient_uhid,
            currentPage,
            pageSize,
            "modified_date",
            "DESC",
            prevMrdrecordsRetrofitCallback
        )

        binding?.mrdRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()

                            viewModel?.getPatientListNextPage(
                                "",
                                currentPage,
                                pageSize,
                                "modified_date",
                                "DESC",
                                patientSearchNextRetrofitCallBack
                            )

                        }

                    }

                }
            }
        })

        allergyAdapter!!.setOnItemClickListener(object :
            CaseSheetAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: CaseSheetResponseContent?, position: Int) {

                val op = MRDChildFragment()

                val bundle = Bundle()

                bundle.putParcelable("Details", responseContent)

                op.arguments = bundle

                val fragmentTransaction = fragmentManager?.beginTransaction()

                fragmentTransaction!!.replace(R.id.labRestultfragmentContainer, op)

                fragmentTransaction.addToBackStack("")

                fragmentTransaction.commit()

            }
        })





        return binding!!.root
    }


    private fun allergyAdapter() {
        allergyAdapter = CaseSheetAdapter((requireActivity()))
        binding?.mrdRecyclerView!!.adapter = allergyAdapter
    }


    val prevMrdrecordsRetrofitCallback = object : RetrofitCallback<CaseSheetResponseModel> {
        override fun onSuccessfulResponse(response: Response<CaseSheetResponseModel>) {
            allergyAdapter?.refreshList(response.body()?.responseContents)


        }

        override fun onBadRequest(response: Response<CaseSheetResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CaseSheetResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CaseSheetResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    response.message()
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


    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<CaseSheetResponseModel> {
        override fun onSuccessfulResponse(response: Response<CaseSheetResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()) {

                allergyAdapter?.removeLoadingFooter()
//                isLoading = false
                setLoading(false)

                allergyAdapter?.refreshList(response.body()!!.responseContents)

                println("testing for two  = $currentPage--$TOTAL_PAGES")

                if (currentPage < TOTAL_PAGES) {
                    allergyAdapter?.addLoadingFooter()
//                    isLoading = true
                    setLoading(true)
                    isLastPage = false
                    println("testing for four  = $currentPage--$TOTAL_PAGES")
                } else {
                    isLastPage = true
//                    visitHistoryAdapter.removeLoadingFooter()
//                    isLoading = false
                    setLoading(false)
                    isLastPage = true
                    println("testing for five  = $currentPage--$TOTAL_PAGES")
                }
            } else {
                println("testing for six  = $currentPage--$TOTAL_PAGES")
                allergyAdapter?.removeLoadingFooter()
//                isLoading = false
                setLoading(false)
                isLastPage = true
            }
        }

        override fun onBadRequest(response: Response<CaseSheetResponseModel>?) {
            allergyAdapter?.removeLoadingFooter()
//            isLoading = false
            setLoading(false)
            isLastPage = true

        }

        override fun onServerError(response: Response<*>) {
            viewModel!!.progress.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            viewModel!!.progress.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onForbidden() {
            viewModel!!.progress.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    private fun trackMrdVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackMrdVisit(type)
    }


    private fun setLoading(toLoad: Boolean) {
        binding?.progressBar?.visibility = if (toLoad) View.VISIBLE else View.GONE
    }


}