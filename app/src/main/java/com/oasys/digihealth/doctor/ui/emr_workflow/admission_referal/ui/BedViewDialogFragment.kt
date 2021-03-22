package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogBedViewFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.nurse_desk.BedDetailsResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.nurse_desk.BedManagementPatientListResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.nurse_desk.BedManagementPatientListResponseMosel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.view_model.BedViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.BedViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.util.*

class BedViewDialogFragment : DialogFragment() {

    private var viewModel: BedViewModel? = null
    var binding: DialogBedViewFragmentBinding? = null
    var gridLayoutManager: GridLayoutManager? = null
    var gridFloorBedLayoutManager: GridLayoutManager? = null
    var gridroomBedLayoutManager: GridLayoutManager? = null


    private var bedManagementAdapter: ManageBedAllocateAdapter? = null
    private var manageFloorBedAllocateAdapter: ManageFloorBedAllocateAdapter? = null
    private var manageRoomBedAllocateAdapter: ManageRoomBedAllocateAdapter? = null
    var data: BedManagementPatientListResponseContent = BedManagementPatientListResponseContent()
    private var bedalocated: Boolean? = null
    private var utils: Utils? = null

    var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var wardUUID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_bed_view_fragment,
                container,
                false
            )
        viewModel = BedViewModelFactory(
            requireActivity().application
        )
            .create(BedViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        //utils = Utils(requireContext())
        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        //wardUUID = appPreferences?.getInt(AppConstants.WARDUUID)

        utils = Utils(requireContext())

        val args = arguments
        if (args != null) {
            wardUUID = args.getInt("wardId")
            viewModel!!.getBedDetails(wardUUID!!, getbedListRetrofitCallBack)
        }

        binding?.clearCardView?.setOnClickListener {
            dialog!!.dismiss()
        }

        binding?.closeImageView?.setOnClickListener {
            dialog!!.dismiss()
        }

        gridLayoutManager =
            GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false)

        gridFloorBedLayoutManager =
            GridLayoutManager(requireContext(), 5, GridLayoutManager.VERTICAL, false)

        gridroomBedLayoutManager =
            GridLayoutManager(requireContext(), 5, GridLayoutManager.VERTICAL, false)

        binding?.bedManagementAllocationRecyclerView?.layoutManager = gridLayoutManager
        bedManagementAdapter = ManageBedAllocateAdapter(requireContext(), ArrayList())
        binding?.bedManagementAllocationRecyclerView?.adapter = bedManagementAdapter


        binding?.floorBedRecyclerView?.layoutManager = gridFloorBedLayoutManager
        manageFloorBedAllocateAdapter = ManageFloorBedAllocateAdapter(requireContext(), ArrayList())
        binding?.floorBedRecyclerView?.adapter = manageFloorBedAllocateAdapter

        val roomBedlayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        binding?.roomBedRecyclerView?.layoutManager = gridroomBedLayoutManager
        manageRoomBedAllocateAdapter = ManageRoomBedAllocateAdapter(requireContext(), ArrayList())
        binding?.roomBedRecyclerView?.adapter = manageRoomBedAllocateAdapter

        return binding?.root
    }

    val getbedListRetrofitCallBack =
        object : RetrofitCallback<BedDetailsResponseModel> {
            override fun onSuccessfulResponse(response: Response<BedDetailsResponseModel>) {
                val responseContents = response.body()?.responseContents

                viewModel!!.progress.value = 0

                bedManagementAdapter!!.addAll(responseContents!!.ward_bed_info)
                manageFloorBedAllocateAdapter!!.addAll(responseContents.ward_floor_bed_info)
                manageRoomBedAllocateAdapter!!.addAll(responseContents.ward_room_bed_info)

                var av: Int = 0

                var nav: Int = 0

                var flav: Int = 0

                var flnav: Int = 0

                var rmav: Int = 0

                var rmnav: Int = 0

                val wadbed = responseContents.ward_bed_info

                val floorbed = responseContents.ward_floor_bed_info

                val roombed = responseContents.ward_room_bed_info

                for (i in wadbed.indices) {

                    if (wadbed[i].is_occupied) {

                        av = +1

                    } else {

                        nav += 1
                    }
                }

                for (j in floorbed.indices) {

                    if (floorbed[j].is_occupied) {

                        flav = +1

                    } else {

                        flnav += 1
                    }
                }

                for (k in roombed.indices) {

                    if (roombed[k].is_occupied) {

                        rmav = +1

                    } else {

                        rmnav += 1
                    }
                }

                binding?.available?.text = "Available(${nav + rmnav + flnav})"

                val avdata = nav + rmnav + flnav

                var totaldata = wadbed.size + floorbed.size + roombed.size

                binding?.notavailable?.text = "Not Available(${totaldata - avdata})"

                binding?.shuffleCardView?.alpha = 0.2f
                binding?.shuffleCardView?.isEnabled = false

                binding?.saveCardView?.alpha = 0.2f
                binding?.saveCardView?.isEnabled = false

                if (data.wbm_bed_number != null && data.wbm_bed_number != 0) {

                    bedalocated = true


                    bedManagementAdapter?.allocated(bedalocated!!)

                    manageFloorBedAllocateAdapter?.allocated(bedalocated!!)

                    manageRoomBedAllocateAdapter?.allocated(bedalocated!!)

                    val wardList = responseContents.ward_bed_info

                    val floorList = responseContents.ward_floor_bed_info

                    val roomList = responseContents.ward_room_bed_info

                    val wardcheck = wardList.any { it.bed_number == data.wbm_bed_number }

                    val floorcheck = floorList.any { it.bed_number == data.wbm_bed_number }

                    val roomcheck = roomList.any { it.bed_number == data.wbm_bed_number }


                    if (wardcheck) {

                        bedManagementAdapter?.setSelecetedData(data.wbm_bed_number)
                    } else if (floorcheck) {

                        manageFloorBedAllocateAdapter?.setSelecetedData(data.wbm_bed_number)

                    } else if (roomcheck) {

                        manageRoomBedAllocateAdapter!!.setSelecetedData(data.wbm_bed_number)

                    } else {


                    }
                } else {

                    bedalocated = false

                    bedManagementAdapter?.allocated(bedalocated!!)

                    manageFloorBedAllocateAdapter?.allocated(bedalocated!!)

                    manageRoomBedAllocateAdapter?.allocated(bedalocated!!)
                    /*    binding?.shuffleCardView?.alpha = 0.2f
                        binding?.shuffleCardView?.isEnabled = false*/
                }

                viewModel!!.progress.value = 8

            }

            override fun onBadRequest(response: Response<BedDetailsResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: BedManagementPatientListResponseMosel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        BedManagementPatientListResponseMosel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message
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
}