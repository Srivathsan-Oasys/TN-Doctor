package com.hmis_tn.doctor.ui.emr_workflow.vitals.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentPreviousVitalsBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.previous_vitals.GetPrevPatientVitalResp
import com.hmis_tn.doctor.ui.emr_workflow.vitals.model.previous_vitals.PPV
import com.hmis_tn.doctor.ui.emr_workflow.vitals.view_model.VitalsViewModel
import com.hmis_tn.doctor.ui.emr_workflow.vitals.view_model.VitalsViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class PreviousVitalsFragment : Fragment() {
    private var binding: FragmentPreviousVitalsBinding? = null
    private var viewModel: VitalsViewModel? = null
    private var utils: Utils? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var patientId: Int? = null
    private var departmentUuid: Int? = null
    private var encounterType: Int? = null
    private var encounterDoctorUuid: Int? = null
    private var encounterUuid: Int? = null
    private var wardstoremasteruuId: Int? = null

    private val ppvList = ArrayList<PPV>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_previous_vitals,
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
        getPrevVitals()
    }

    private fun initViews() {
        binding?.rvPreviousVitals?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvPreviousVitals?.adapter = PreviousVitalsAdapter(ppvList)
    }

    private fun listeners() {

    }

    private fun getPrevVitals() {
        viewModel?.getPrevPatientVitals(
            facilityId!!,
            patientId!!,
            departmentUuid!!,
            getPrevPatientVitalRespRetrofitCallback
        )
    }

    private val getPrevPatientVitalRespRetrofitCallback =
        object : RetrofitCallback<GetPrevPatientVitalResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetPrevPatientVitalResp>?) {
                if (responseBody?.body()?.code == 200) {
                    ppvList.clear()
                    responseBody.body()?.responseContents?.PPV_list?.let { ppvList.addAll(it) }
                    binding?.rvPreviousVitals?.adapter?.notifyDataSetChanged()
                }
            }

            override fun onBadRequest(errorBody: Response<GetPrevPatientVitalResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }
}
