package com.hmis_tn.doctor.ui.emr_workflow.investigation.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentInvestigationFavouriteBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model.InvestigationViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class InvestigationFavouriteFragment : Fragment(), ClearFavParticularPositionListener,
    ManageInvestFavouriteFragment.OnFavRefreshListener {

    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var customdialog: Dialog? = null
    lateinit var drugNmae: TextView

    private var facility_UUID: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentInvestigationFavouriteBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: InvestigationViewModel? = null
    lateinit var favouritesAdapter: InvestigationFavouritesAdapter
    private var drugName: String? = ""

    var displayOrder: ArrayList<Int> = ArrayList()
    private var customProgressDialog: CustomProgressDialog? = null


    var mCallback: FavClickedListener? = null

    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_investigation_favourite,
                container,
                false
            )

        viewModel = InvestigationViewModelFactory(
            requireActivity().application
        ).create(InvestigationViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        utils = Utils(requireContext())

        customProgressDialog = CustomProgressDialog(requireContext())

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
                favouritesAdapter.getFilter().filter(query)
            }

        })

        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog = ManageInvestFavouriteFragment()


            val bundle = Bundle()
            bundle.putBoolean("status", false)
            bundle.putIntegerArrayList("DisplayOrder", displayOrder)
            dialog.arguments = bundle
            dialog.show(ft, "Tag")

        }
        initFavouritesAdapter()
        isLoading(true)
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)

        return binding!!.root
    }

    private fun initFavouritesAdapter() {
        favouritesAdapter =
            InvestigationFavouritesAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = favouritesAdapter
        favouritesAdapter.setOnItemClickListener(object :
            InvestigationFavouritesAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: FavouritesModel?,
                position: Int,
                selected: Boolean
            ) {

                if (isTablet(requireContext())) {
                    favouritesAdapter.updateSelectStatus(position, selected)
                }
                mCallback?.sendData(responseContent, position, selected)

            }
        })

        favouritesAdapter.setOnItemDeleteClickListener(object :
            InvestigationFavouritesAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: FavouritesModel?
            ) {


                drugName = responseContent?.test_master_name

                isLoading(true)
                viewModel!!.deleteFavourite(
                    facility_UUID,
                    responseContent?.favourite_id!!,
                    deleteRetrofitResponseCallback
                )


                /*         customdialog = Dialog(requireContext())
                         customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                         customdialog!! .setCancelable(false)
                         customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                         val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                         closeImageView.setOnClickListener {
                             customdialog?.dismiss()
                         }
                         drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                         drugNmae.text ="${drugNmae.text} '"+responseContent?.test_master_name+"' Record ?"
                         val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                         val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                         yesBtn.setOnClickListener {
                                     }
                         noBtn.setOnClickListener {
                             customdialog!! .dismiss()
                         }
                         customdialog!! .show()*/
            }

        })
        favouritesAdapter.setOnItemViewClickListener(object :
            InvestigationFavouritesAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: FavouritesModel?) {

                val ft = childFragmentManager.beginTransaction()
                val managedialog = ManageInvestFavouriteFragment()
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, responseContent)

                bundle.putBoolean("status", true)
                bundle.putIntegerArrayList("DisplayOrder", displayOrder)
                managedialog.arguments = bundle

                managedialog.show(ft, "Tag")
            }

        })

    }


    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<FavouritesResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavouritesResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)


                val resposeData = response.body()?.responseContents

                displayOrder.clear()


                for (i in resposeData!!.indices) {

                    displayOrder.add(resposeData[i]!!.favourite_display_order!!)

                }

                favouritesAdapter.refreshList(response.body()?.responseContents!!)

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
                viewModel!!.progress.value = 8
                isLoading(false)
            }
        }

    //defining Interface
    interface FavClickedListener {
        fun sendData(
            favmodel: FavouritesModel?,
            position: Int,
            selected: Boolean
        )
    }

    fun setOnTextClickedListener(callback: FavClickedListener) {
        this.mCallback = callback
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
            //     customdialog!!.dismiss()

            Toast.makeText(requireContext(), "$drugName Deleted Successfully", Toast.LENGTH_SHORT)
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

            isLoading(false)

        }

    }

    /* override fun onRefreshList() {
         viewModel!!.getFavourites(getFavouritesRetrofitCallBack,department_uuid)
     }

     override fun onAttachFragment(childFragment: Fragment) {
         super.onAttachFragment(childFragment)
         if (childFragment is ManageInvestFavouriteFragment) {
             childFragment.setOnFavRefreshListener(this)
         }
     }*/

    override fun onFavID(position: Int) {
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
    }

    override fun onRefreshList() {
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is ManageInvestFavouriteFragment) {
            childFragment.setOnFavRefreshListener(this)
        }
    }

    override fun ClearFavParticularPosition(position: Int) {
        favouritesAdapter.refreshFavParticularData(position)
    }

    override fun ClearAllData() {
        favouritesAdapter.refreshAllData()
    }

    override fun checkanduncheck(position: Int, isSelect: Boolean) {

        if (isTablet(requireContext())) {
            favouritesAdapter.checkunchecktempleteData(position, isSelect)
        } else {

            /*         if(favouritesAdapter?.checkData(position)){

                         favouritesAdapter?.updateformList(position,isSelect)

                     }*/
        }
    }

    override fun checkanduncheck(position: ArrayList<Int>, isSelect: Boolean) {

        favouritesAdapter.updateData(position, isSelect)
    }

    override fun drugIdPresentCheck(drug_id: Int): Boolean {
        return favouritesAdapter.isCheckAlreadyExist(drug_id)
    }

    override fun clearDataUsingDrugid(drug_id: Int) {
        favouritesAdapter.clearDataDrugid(drug_id)
    }

    override fun favouriteID(favouriteID: Int) {
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, department_uuid)
    }

    override fun updateSelectStatus(favOrTem: Int, position: Int, selected: Boolean) {
        favouritesAdapter.updateSelectStatus(position, selected)
    }


    fun isLoading(st: Boolean) {

        if (st) {

            customProgressDialog!!.show()

        } else {

            customProgressDialog!!.dismiss()
        }


    }

}