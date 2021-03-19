package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui

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
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentRadiologyFavouriteBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RadiologyFavData
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RadiologyFavResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.RadiologyViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model.RadiologyViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class RadiologyFavouriteFragment : Fragment(), ClearFavParticularPositionListener,
    ManageRadiolgyFavouriteFragment.OnFavRadiologyRefreshListener {
    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentRadiologyFavouriteBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: RadiologyViewModel? = null
    lateinit var favouritesAdapter: RadiologyFavouritesAdapter

    var mCallback: RadiologyFavClickedListener? = null


    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_radiology_favourite,
                container,
                false
            )

        viewModel = RadiologyViewModelFactory(
            requireActivity().application
        ).create(RadiologyViewModel::class.java)
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
                favouritesAdapter.getFilter().filter(query)
            }

        })

        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog = ManageRadiolgyFavouriteFragment()
            dialog.show(ft, "Tag")
        }
        initFavouritesAdapter()
        viewModel!!.getRadiologyFavourites(getFavouritesRetrofitCallBack, department_uuid)

        return binding!!.root
    }

    private fun initFavouritesAdapter() {
        favouritesAdapter =
            RadiologyFavouritesAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = favouritesAdapter
        favouritesAdapter.setOnItemClickListener(object :
            RadiologyFavouritesAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: RadiologyFavData?,
                position: Int,
                selected: Boolean
            ) {

                favouritesAdapter.updateSelectStatus(1, position, selected)
                mCallback?.sendFavAddInLab(responseContent, position, selected)

            }
        })

        favouritesAdapter.setOnItemDeleteClickListener(object :
            RadiologyFavouritesAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: RadiologyFavData?
            ) {

                viewModel!!.deleteFavourite(
                    facility_UUID,
                    responseContent?.favourite_id!!,
                    deleteRetrofitResponseCallback
                )
                /* customdialog = Dialog(requireContext())
                 customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                 customdialog!! .setCancelable(false)
                 customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                 val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                 closeImageView.setOnClickListener {
                     customdialog!!.dismiss()
                 }
                 val drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                 //drugNmae.text = responseContent?.test_master_name
                 drugNmae.text ="${drugNmae.text} '"+responseContent?.test_master_name+"' Record ?"
                 val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                 val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                 yesBtn.setOnClickListener {
                     viewModel!!.deleteFavourite(
                         facility_UUID,
                         responseContent?.favourite_id!!,
                         deleteRetrofitResponseCallback
                     )                }
                 noBtn.setOnClickListener {
                     customdialog!! .dismiss()
                 }
                 customdialog!! .show()*/
            }

        })
        favouritesAdapter.setOnItemViewClickListener(object :
            RadiologyFavouritesAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: RadiologyFavData?) {

                val ft = childFragmentManager.beginTransaction()
                val dialog = ManageRadiolgyFavouriteFragment()
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, responseContent)
                dialog.arguments = bundle
                dialog.show(ft, "Tag")
            }

        })
    }


    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<RadiologyFavResponse> {
            override fun onSuccessfulResponse(response: Response<RadiologyFavResponse>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
                favouritesAdapter.refreshList(response.body()?.responseContents!!)
            }

            override fun onBadRequest(response: Response<RadiologyFavResponse>) {

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

    //defining Interface
    interface RadiologyFavClickedListener {
        fun sendFavAddInLab(
            favmodel: RadiologyFavData?,
            position: Int,
            selected: Boolean
        )
    }

    fun setOnTextClickedListener(callback: RadiologyFavClickedListener) {
        this.mCallback = callback
    }

    override fun ClearFavParticularPosition(position: Int) {
        favouritesAdapter.refreshFavParticularData(position)
    }

    override fun ClearAllData() {
        favouritesAdapter.refreshAllData()
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getRadiologyFavourites(getFavouritesRetrofitCallBack, department_uuid)
            //   customdialog!!.dismiss()
            Toast.makeText(requireContext(), responseBody?.body()?.message, Toast.LENGTH_SHORT)
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

        }

    }

    override fun checkanduncheck(position: Int, isSelect: Boolean) {

    }

    override fun checkanduncheck(position: ArrayList<Int>, isSelect: Boolean) {
        favouritesAdapter.updateData(position, isSelect)

    }

    override fun drugIdPresentCheck(drug_id: Int): Boolean {
        return false
    }

    override fun clearDataUsingDrugid(drug_id: Int) {

    }

    override fun favouriteID(favouriteID: Int) {

    }

    override fun updateSelectStatus(favOrTem: Int, position: Int, selected: Boolean) {
        favouritesAdapter.updateSelectStatus(1, position, selected)
    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageRadiolgyFavouriteFragment) {
            childFragment.setOnFavRefreshListener(this)
        }
    }

    override fun onFavRadiologyID(position: Int) {
        viewModel!!.getRadiologyFavourites(getFavouritesRetrofitCallBack, department_uuid)

    }

    override fun onRefreshList() {
        viewModel!!.getRadiologyFavourites(getFavouritesRetrofitCallBack, department_uuid)
    }
}