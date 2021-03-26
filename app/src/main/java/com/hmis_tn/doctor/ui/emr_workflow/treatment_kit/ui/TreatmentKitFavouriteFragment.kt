package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.TreatmentKitFragmentFavouriteBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.FavAddToListresponseContents
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.FavouriteAddToListResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitViewModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class TreatmentKitFavouriteFragment : Fragment(),
    ClearFavParticularPositionListener,
    FavaddTreatmentKitDialog.OnFavRefreshListener {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    var binding: TreatmentKitFragmentFavouriteBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: TreatmentKitViewModel? = null
    private var responseContents: String? = ""

    private var positionF: Int? = 0
    private var selectedF: Boolean = false
    private var id: Int? = 0

    lateinit var treatmentFavouritesAdapter: TreatmentKitMainFavouritesAdapter

    var mCallback: FavClickedListener? = null

    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var favouriteName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.treatment_kit_fragment_favourite,
                container,
                false
            )

        viewModel = TreatmentKitViewModelFactory(
            requireActivity().application
        ).create(TreatmentKitViewModel::class.java)
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
                treatmentFavouritesAdapter.getFilter().filter(query)
            }

        })
        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val managedialog = FavaddTreatmentKitDialog()
            managedialog.show(ft, "Tag")

        }
        initFavouritesAdapter()
        viewModel!!.getdiagnosisFavourites(department_uuid, getFavouritesRetrofitCallBack)
        //viewModel!!.getPrescriptionFavourites(department_uuid,getFavouritesRetrofitCallBack)


        return binding!!.root
    }

    private fun initFavouritesAdapter() {
        treatmentFavouritesAdapter =
            TreatmentKitMainFavouritesAdapter(requireContext())

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        var gridLayoutManager: GridLayoutManager? = null
        if (tabletSize) {
            gridLayoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        } else {
            gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        /* val gridLayoutManager =
             GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)*/
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = treatmentFavouritesAdapter
        treatmentFavouritesAdapter.setOnItemClickListener(
            object : TreatmentKitMainFavouritesAdapter.OnItemClickListener {
                override fun onItemClick(
                    favouritesModel: FavouritesModel?,
                    position: Int,
                    selected: Boolean,
                    favouriteId: Int
                ) {
                    treatmentFavouritesAdapter.updateSelectStatus(1, position, selected)
                    positionF = position
                    selectedF = selected
                    id = favouriteId
                    viewModel?.getFavouritesToList(
                        favouritesModel?.treatment_kit_id,
                        favouritesModel?.favourite_id,
                        favAddtoListRetrofitCallback
                    )

                }
            })

        treatmentFavouritesAdapter.setOnItemDeleteClickListener(
            object : TreatmentKitMainFavouritesAdapter.OnItemDeleteClickListner {
                override fun onItemClick(
                    responseContent: FavouritesModel?
                ) {
                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                    }
                    val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "${drugNmae.text} '" + responseContent?.favourite_name.toString() + "/" +
                                responseContent?.favourite_code + "' Record ?"

                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                    yesBtn.setOnClickListener {
                        viewModel!!.deleteFavourite(
                            facility_UUID,
                            responseContent?.favourite_id!!,
                            deleteRetrofitResponseCallback
                        )
                        favouriteName = responseContent.favourite_name ?: ""
                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()
                    }
                    customdialog!!.show()
                }
            })
        treatmentFavouritesAdapter.setOnItemViewClickListener(object :
            TreatmentKitMainFavouritesAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: FavouritesModel?) {

                val ft = childFragmentManager.beginTransaction()
                val managedialog = FavaddTreatmentKitDialog()
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, responseContent)
                managedialog.arguments = bundle
                managedialog.show(ft, "Tag")
            }

        })
    }

    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<FavouritesResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavouritesResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    treatmentFavouritesAdapter.refreshList(response.body()?.responseContents)
                }
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
        fun sendFavAddInLab(
            faverAddmodel: FavAddToListresponseContents?,
            position: Int,
            selected: Boolean,
            favouriteID: Int
        )
    }

    fun setOnTextClickedListener(callback: FavClickedListener) {
        this.mCallback = callback
    }

    override fun ClearFavParticularPosition(position: Int) {
        treatmentFavouritesAdapter.refreshFavParticularData(position)
    }

    override fun ClearAllData() {
        treatmentFavouritesAdapter.refreshAllData()
    }

    override fun checkanduncheck(position: Int, isSelect: Boolean) {

    }

    override fun checkanduncheck(position: ArrayList<Int>, isSelect: Boolean) {

    }

    override fun drugIdPresentCheck(drug_id: Int): Boolean {
        return false
    }

    override fun clearDataUsingDrugid(drug_id: Int) {

    }

    override fun favouriteID(favouriteID: Int) {
        //viewModel!!.getdiagnosisFavourites(department_uuid,getFavouritesRetrofitCallBack)
        //viewModel!!.getPrescriptionFavourites(department_uuid,getFavouritesRetrofitCallBack)

    }

    override fun updateSelectStatus(favOrTem: Int, position: Int, selected: Boolean) {
        treatmentFavouritesAdapter.updateSelectStatus(1, position, selected)
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getdiagnosisFavourites(department_uuid, getFavouritesRetrofitCallBack)
            //viewModel!!.getPrescriptionFavourites(department_uuid,getFavouritesRetrofitCallBack)

            customdialog!!.dismiss()
            Log.e("DeleteResponse", responseBody?.body().toString())
            Toast.makeText(
                requireContext(),
                "$favouriteName Deleted Successfully",
                Toast.LENGTH_SHORT
            ).show()
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


    val favAddtoListRetrofitCallback = object : RetrofitCallback<FavouriteAddToListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<FavouriteAddToListResponseModel>?) {

            //Log.e("FavToListData",responseBody?.body()?.responseContents.toString())

            mCallback?.sendFavAddInLab(
                responseBody?.body()?.responseContents,
                positionF!!,
                selectedF,
                id!!
            )
        }

        override fun onBadRequest(response: Response<FavouriteAddToListResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: FavouriteAddToListResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    FavouriteAddToListResponseModel::class.java
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

    override fun onFavID(position: Int) {
        viewModel!!.getdiagnosisFavourites(department_uuid, getFavouritesRetrofitCallBack)
        //viewModel!!.getPrescriptionFavourites(department_uuid,getFavouritesRetrofitCallBack)

    }

    override fun onRefreshList() {
        viewModel!!.getdiagnosisFavourites(department_uuid, getFavouritesRetrofitCallBack)
        //viewModel!!.getPrescriptionFavourites(department_uuid,getFavouritesRetrofitCallBack)

    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is FavaddTreatmentKitDialog) {
            childFragment.setOnFavRefreshListener(this)
        }
    }
}