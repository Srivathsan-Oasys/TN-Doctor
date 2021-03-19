package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui

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
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.PrescriptionFavouriteFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.ManagePreFavEdit
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.PrescriptionViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model.PrescriptionViewModelFactory
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class PrescriptionFavouriteFragment : Fragment(), ClearFavParticularPositionListener,
    ManagePreFavEdit.OnFavRefreshListener {
    private var customdialog: Dialog? = null
    private var binding: PrescriptionFavouriteFragmentBinding? = null
    lateinit var prescriptionFavouritesAdapter: PrescriptionFavouriteAdpter
    var mCallback: FavClickedListener? = null
    private var department_uuid: Int? = null

    private var facility_UUID: Int? = 0
    private var viewModel: PrescriptionViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var drugName: String? = null
    private var customProgressDialog: CustomProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prescription_favourite_fragment,
                container,
                false
            )
        viewModel = PrescriptionViewModelFactory(
            requireActivity().application
        ).create(PrescriptionViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        customProgressDialog = CustomProgressDialog(requireContext())
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
                prescriptionFavouritesAdapter.getFilter().filter(query)
            }

        })

        initFavouritesAdapter()


        isLoading(true)
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog = ManagePreFavEdit()
            dialog.show(ft, "Tag")

        }


        return binding!!.root
    }

    private fun initFavouritesAdapter() {
        prescriptionFavouritesAdapter =
            PrescriptionFavouriteAdpter(
                requireContext()
            )


        val tabletSize = resources.getBoolean(R.bool.isTablet)
        var gridLayoutManager: GridLayoutManager? = null
        if (tabletSize) {
            gridLayoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        } else {
            gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }

/*
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)*/
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = prescriptionFavouritesAdapter
        prescriptionFavouritesAdapter.setOnItemClickListener(object :
            PrescriptionFavouriteAdpter.OnItemClickListener {
            override fun onItemClick(
                responseContent: FavouritesModel?,
                position: Int,
                selected: Boolean
            ) {
                //  prescriptionFavouritesAdapter.updateSelectStatus(1,position, selected)
                //    responseContent?.viewPrescriptionstatus = 1
                mCallback?.sendFavAddInLab(responseContent, position, selected)

            }
        })
        prescriptionFavouritesAdapter.setOnItemViewClickListener(object :
            PrescriptionFavouriteAdpter.OnItemViewClickListner {

            override fun onItemClick(responseContent: FavouritesModel?) {
                val ft = childFragmentManager.beginTransaction()
                val dialog = ManagePreFavEdit()
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, responseContent)
                dialog.arguments = bundle
                dialog.show(ft, "Tag")
            }
        })

        prescriptionFavouritesAdapter.setOnItemDeleteClickListener(object :
            PrescriptionFavouriteAdpter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: FavouritesModel?
            ) {


                drugName = responseContent?.drug_name


                viewModel!!.deleteFavourite(
                    facility_UUID,
                    responseContent?.favourite_id!!,
                    deleteRetrofitResponseCallback
                )
                /* Log.i("",""+responseContent)
                 customdialog = Dialog(requireContext())
                 customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                 customdialog!! .setCancelable(false)
                 customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                 val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                 closeImageView.setOnClickListener {
                     customdialog!!.dismiss()
                 }


                 closeImageView.setOnClickListener {
                     customdialog?.dismiss()
                 }
                 val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                 drugNmae.text ="${drugNmae.text} '"+responseContent?.drug_name+"' Record ?"
                 val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                 val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                 yesBtn.setOnClickListener {
                     viewModel!!.deleteFavourite(
                         facility_UUID,
                         responseContent?.favourite_id!!,
                         deleteRetrofitResponseCallback
                     )                }
                 noBtn.setOnClickListener {
                     customdialog!!.dismiss()
                 }
                 customdialog!!.show()*/
            }

        })
    }

    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<FavouritesResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavouritesResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
                prescriptionFavouritesAdapter.refreshList(response.body()?.responseContents!!)
            }

            override fun onBadRequest(response: Response<FavouritesResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
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
                isLoading(false)
                viewModel!!.progress.value = 8
            }
        }

    //defining Interface
    interface FavClickedListener {
        fun sendFavAddInLab(
            favmodel: FavouritesModel?,
            position: Int,
            selected: Boolean
        )
    }

    fun setOnTextClickedListener(callback: FavClickedListener) {
        this.mCallback = callback
    }

    override fun ClearFavParticularPosition(position: Int) {
        prescriptionFavouritesAdapter.refreshFavParticularData(position)
    }

    override fun checkanduncheck(drug_id: Int, isSelect: Boolean) {

        prescriptionFavouritesAdapter.checkunchecktempleteData(drug_id, isSelect)

    }

    override fun checkanduncheck(position: ArrayList<Int>, isSelect: Boolean) {

        prescriptionFavouritesAdapter.updateData(position, isSelect)
    }

    //template and fav Compare

    override fun drugIdPresentCheck(drug_id: Int): Boolean {

        return prescriptionFavouritesAdapter.isCheckAlreadyExist(drug_id)

    }

    override fun clearDataUsingDrugid(drug_id: Int) {

        prescriptionFavouritesAdapter.clearDataDrugid(drug_id)

    }

    override fun favouriteID(favouriteID: Int) {

    }

    override fun updateSelectStatus(favOrTem: Int, position: Int, selected: Boolean) {
        prescriptionFavouritesAdapter.updateSelectStatus(1, position, selected)
    }


    override fun ClearAllData() {
        prescriptionFavouritesAdapter.refreshAllData()
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
            //        customdialog!! .dismiss()

            Toast.makeText(requireContext(), "$drugName Deleted Successfully", Toast.LENGTH_SHORT)
                .show()

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

    override fun onFavID(position: Int) {
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
    }

    override fun onRefreshList() {
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManagePreFavEdit) {
            childFragment.setOnFavRefreshListener(this)
        }
    }


    fun isLoading(st: Boolean) {

        if (st) {

            customProgressDialog!!.show()

        } else {

            customProgressDialog!!.dismiss()
        }


    }

}