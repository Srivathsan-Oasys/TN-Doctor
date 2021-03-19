package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.CheifComplaintFavouriteFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.ui.FavaddCheifcomplaintDialog
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.ChiefComplaintViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.ChiefComplaintViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class ChiefComplaintFavouriteFragment : Fragment(), ClearFavParticularPositionListener,
    FavaddCheifcomplaintDialog.OnFavRefreshListener {
    private var customdialog: Dialog? = null
    private var binding: CheifComplaintFavouriteFragmentBinding? = null
    lateinit var chiefComplaintFavouritesAdapter: Chief_Complaint_FavouritesAdapter
    var mCallback: FavClickedListener? = null
    private var department_uuid: Int? = null

    private var facility_UUID: Int? = 0
    private var viewModel: ChiefComplaintViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var responseContents: String? = ""
    lateinit var drugNmae: TextView
    var displayOrder: ArrayList<Int> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.cheif_complaint_favourite_fragment,
                container,
                false
            )
        viewModel = ChiefComplaintViewModelFactory(
            requireActivity().application
        ).create(ChiefComplaintViewModel::class.java)
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
                chiefComplaintFavouritesAdapter.getFilter().filter(query)
            }

        })

        initFavouritesAdapter()
        viewModel?.getFavourites(department_uuid, getFavouritesRetrofitCallBack)
        binding!!.manageFavouritesCardView.setOnClickListener {

            val ft = childFragmentManager.beginTransaction()
            val dialog = FavaddCheifcomplaintDialog()
            val bundle = Bundle()
            bundle.putBoolean("status", true)
            bundle.putIntegerArrayList("DisplayOrder", displayOrder)
            dialog.arguments = bundle

            dialog.show(ft, "Tag")
        }

        return binding!!.root
    }

    private fun initFavouritesAdapter() {
        chiefComplaintFavouritesAdapter =
            Chief_Complaint_FavouritesAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = chiefComplaintFavouritesAdapter
        chiefComplaintFavouritesAdapter.setOnItemClickListener(object :
            Chief_Complaint_FavouritesAdapter.OnItemClickListener {
            override fun onItemClick(
                favouritesModel: FavouritesModel?,
                position: Int,
                selected: Boolean
            ) {
                chiefComplaintFavouritesAdapter.updateSelectStatus(1, position, selected)
                mCallback?.sendFavAddInChiefFav(favouritesModel, position, selected)
            }
        })
        chiefComplaintFavouritesAdapter.setOnItemViewClickListener(object :
            Chief_Complaint_FavouritesAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: FavouritesModel?) {

                val ft = childFragmentManager.beginTransaction()
                val managedialog = FavaddCheifcomplaintDialog()
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, responseContent)
                bundle.putBoolean("status", false)
                bundle.putIntegerArrayList("DisplayOrder", displayOrder)
                managedialog.arguments = bundle
                managedialog.show(ft, "Tag")
            }

        })


        chiefComplaintFavouritesAdapter.setOnItemDeleteClickListener(object :
            Chief_Complaint_FavouritesAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: FavouritesModel?
            ) {
                Log.i("", "" + responseContent)

                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text} '" + responseContent!!.chief_complaint_name + "' Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    viewModel!!.deleteFavourite(
                        facility_UUID,
                        responseContent.favourite_id!!,
                        deleteRetrofitResponseCallback
                    )
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()


            }

        })
    }

    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<FavouritesResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavouritesResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)

                val resposeData = response.body()?.responseContents

                displayOrder.clear()


                for (i in resposeData!!.indices) {

                    displayOrder.add(resposeData[i]!!.favourite_display_order!!)

                }

                chiefComplaintFavouritesAdapter.refreshList(response.body()?.responseContents)
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
            }
        }

    //defining Interface
    interface FavClickedListener {
        fun sendFavAddInChiefFav(
            favmodel: FavouritesModel?,
            position: Int,
            selected: Boolean
        )
    }

    fun setOnTextClickedListener(callback: FavClickedListener) {
        this.mCallback = callback
    }

    override fun ClearFavParticularPosition(position: Int) {
        chiefComplaintFavouritesAdapter.refreshFavParticularData(position)
    }

    override fun checkanduncheck(drug_id: Int, isSelect: Boolean) {

        chiefComplaintFavouritesAdapter.checkunchecktempleteData(drug_id, isSelect)

    }

    override fun checkanduncheck(position: ArrayList<Int>, isSelect: Boolean) {

    }

    //template and fav Compare

    override fun drugIdPresentCheck(drug_id: Int): Boolean {

        return chiefComplaintFavouritesAdapter.isCheckAlreadyExist(drug_id)

    }

    override fun clearDataUsingDrugid(drug_id: Int) {

        chiefComplaintFavouritesAdapter.clearDataDrugid(drug_id)

    }

    override fun favouriteID(favouriteID: Int) {
        viewModel!!.getFavourites(department_uuid, getFavouritesRetrofitCallBack)

    }

    override fun updateSelectStatus(favOrTem: Int, position: Int, selected: Boolean) {
        chiefComplaintFavouritesAdapter.updateSelectStatus(1, position, selected)
    }

    override fun ClearAllData() {
        chiefComplaintFavouritesAdapter.refreshAllData()
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {

            viewModel!!.getFavourites(department_uuid, getFavouritesRetrofitCallBack)
            customdialog!!.dismiss()

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
        viewModel?.getFavourites(department_uuid, getFavouritesRetrofitCallBack)
    }

    override fun onRefreshList() {
        viewModel?.getFavourites(department_uuid, getFavouritesRetrofitCallBack)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is FavaddCheifcomplaintDialog) {
            childFragment.setOnFavRefreshListener(this)
        }
    }
}