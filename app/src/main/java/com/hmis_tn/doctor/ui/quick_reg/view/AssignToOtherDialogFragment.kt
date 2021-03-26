package com.hmis_tn.doctor.ui.quick_reg.view


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
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.DialogFragment
import androidx.databinding.DataBindingUtil
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.utils.Utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogAssignListBinding
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.ui.quick_reg.model.LocationMaster
import com.hmis_tn.doctor.ui.quick_reg.model.LocationMasterResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.Assigntoother
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.AssigntootherRequest
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.UserProfileResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.request.LabNameSearchResponseModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestViewModelFactory
import retrofit2.Response


class AssignToOtherDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: LabTestViewModel? = null
    var binding: DialogAssignListBinding? = null
    private val labTestList: MutableList<RecyclerDto> = ArrayList()
    private val searchList: MutableList<RecyclerDto> = ArrayList()
    private var mAdapter: AssignToAdapter? = null

    var callbackOnAssignToOtherProcess: OnAssignToOtherListener? = null



    var StatusSingle:Boolean? = null

    var currentPosition:Int=0

    private var instutionView: AppCompatAutoCompleteTextView?=null

    private var labView: AppCompatAutoCompleteTextView?=null

    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null

    private  var favouriteData:ArrayList<SendIdList> =ArrayList()

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
            DataBindingUtil.inflate(inflater, R.layout.dialog_assign_list, container, false)
        viewModel = LabTestViewModelFactory(
            requireActivity().application

        )
            .create(LabTestViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)



        binding?.closeImageView?.setOnClickListener {

            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {

            dialog?.dismiss()
        }
        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.assignListrecycleview!!.layoutManager = layoutmanager
        mAdapter = AssignToAdapter(requireContext(), ArrayList())


        binding?.assignListrecycleview !!.adapter = mAdapter

        mAdapter!!.setOnSearchInitiatedListener(object :AssignToAdapter.OnSearchInitiatedListener{
            override fun onSearchInitiated(
                query: String,
                view: AppCompatAutoCompleteTextView,
                position: Int,
                lab: AppCompatAutoCompleteTextView
            ) {

                instutionView=view

                labView=lab

                currentPosition=position

                viewModel!!.getLabName(query,LabnameResponseCallback)


            }

        })

        binding!!.saveCardView.setOnClickListener {

            val data=mAdapter!!.getAll()

            val check= data.any{ it!!.to_facility == 0}

            val check2= data.any{ it!!.to_location_uuid == ""}

            if(check||check2){

                Toast.makeText(this.context,"Please Fill all field",Toast.LENGTH_SHORT).show()

            }
            else {

                Log.e("Save","save")

                val request: AssigntootherRequest = AssigntootherRequest()

                var details: ArrayList<Assigntoother> = ArrayList()


                if(StatusSingle!!){

                    for(i in favouriteData.indices){

                        val dat:Assigntoother=Assigntoother()
                        dat.facility_uuid=data[0].facility_uuid
                        dat.id=data[0].id
                        dat.testname=data[0].testname
                        dat.to_facility=data[0].to_facility
                        dat.to_location_uuid=data[0].to_location_uuid

                        details.add(dat)

                    }

                }
                else {
                    for(i in favouriteData.indices){

                        val dat:Assigntoother=Assigntoother()
                        dat.facility_uuid=data[i].facility_uuid
                        dat.id=data[i].id
                        dat.testname=data[i].testname
                        dat.to_facility=data[i].to_facility
                        dat.to_location_uuid=data[i].to_location_uuid

                        details.add(dat)

                    }

                }




                request.details = details

                viewModel!!.assigntoOther(request, saveRetrofitCallback)

            }
        }

        mAdapter!!.setOnSearch(object :AssignToAdapter.OnSearch{
            override fun onSearchFunction(
                data: Int,
                dropdownReferenceView: AppCompatAutoCompleteTextView,
                searchposition: Int
            ) {

                labView=dropdownReferenceView

                currentPosition=searchposition

                viewModel!!.getLocationMaster(data,LocationMasterResponseCallback)

            }
        })

        val args = arguments

        if (args == null) {

        } else {
            // get value from bundle..
            favouriteData = args.getParcelableArrayList<SendIdList>(AppConstants.RESPONSECONTENT)!!

            StatusSingle = args.getBoolean(AppConstants.RESPONSENEXT)!!

            val list:ArrayList<Assigntoother> = ArrayList()

            if(StatusSingle!!){

                val data: Assigntoother=Assigntoother()

                data.id=favouriteData[0].Id

                data.testname=favouriteData[0].name

                list.add(data)

            }
            else{


                for(i in favouriteData.indices){

                val data: Assigntoother=Assigntoother()

                data.id=favouriteData[i].Id

                data.testname=favouriteData[i].name

                list.add(data)

                    }

            }






            mAdapter!!.setData(list)

        }


        return binding?.root
    }


    val saveRetrofitCallback = object : RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {


            Toast.makeText(context,responseBody?.body()?.message,Toast.LENGTH_SHORT).show()

            callbackOnAssignToOtherProcess!!.onRefreshAssignToOrderList()
            try {
                dialog!!.dismiss()
            }catch (e: Exception)
            {

            }



        }

        override fun onBadRequest(errorBody: Response<SimpleResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: UserProfileResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    UserProfileResponseModel::class.java
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

    val LocationMasterResponseCallback = object : RetrofitCallback<LocationMasterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModel>?) {
            Log.i("","locationdata"+responseBody!!.body()!!.responseContents)
            val data=responseBody!!.body()!!.responseContents

            if(data.isNotEmpty()) {

                mAdapter!!.setlabAdapter(labView!!,data as ArrayList<LocationMaster>,currentPosition)
            }

            //    labnameAdapter(responseBody!!.body()!!.responseContents)
        }
        override fun onBadRequest(errorBody: Response<LocationMasterResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LocationMasterResponseModel::class.java
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

    val LabnameResponseCallback = object : RetrofitCallback<LabNameSearchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabNameSearchResponseModel>?) {

            mAdapter!!.setAdapter(instutionView!!,responseBody!!.body()!!.responseContents,currentPosition,labView!!)

        }

        override fun onBadRequest(errorBody: Response<LabNameSearchResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LabNameSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabNameSearchResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.status!!
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

    fun setOnAssignToOtherRefreshListener(callback: OnAssignToOtherListener) {
        callbackOnAssignToOtherProcess = callback
    }
    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnAssignToOtherListener {
        fun onRefreshAssignToOrderList()
    }

}

