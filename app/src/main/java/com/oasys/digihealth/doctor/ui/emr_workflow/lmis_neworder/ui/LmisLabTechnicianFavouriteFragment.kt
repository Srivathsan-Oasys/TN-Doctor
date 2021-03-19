package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.ui


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
import com.oasys.digihealth.doctor.databinding.FragmentLmisLabFavouriteBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.view_model.LmisNewOrderViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.view_model.LmisNewOrderViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class LmisLabTechnicianFavouriteFragment : Fragment(), ClearLmisFavParticularPositionListener,
    ManageLmisLabFavouriteFragment.OnFavRefreshListener {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0
    private var favouritesAdapter: LmisLabTechnicianFavouritesAdapter? = null

    var mCallback: FavClickedListener? = null
    var Lab_UUID: Int? = 0


    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentLmisLabFavouriteBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: LmisNewOrderViewModel? = null


    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_lmis_lab_favourite,
                container,
                false
            )

        viewModel = LmisNewOrderViewModelFactory(
            requireActivity().application
        ).create(LmisNewOrderViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        Lab_UUID = appPreferences?.getInt(AppConstants.LAB_UUID)

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
                favouritesAdapter!!.getFilter().filter(query)
            }

        })
        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val managedialog = ManageLmisLabFavouriteFragment()
            managedialog.setOnFavRefreshListener(this)
            managedialog.show(ft, "Tag")

        }
        initFavouritesAdapter()
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, Lab_UUID)
        return binding!!.root
    }

    private fun initFavouritesAdapter() {
        favouritesAdapter =
            LmisLabTechnicianFavouritesAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = favouritesAdapter
        favouritesAdapter!!.setOnItemClickListener(object :
            LmisLabTechnicianFavouritesAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: FavouritesModel?,
                position: Int,
                selected: Boolean
            ) {
                favouritesAdapter!!.updateSelectStatus(position, selected)
                mCallback?.sendFavAddInLab(responseContent, position, selected)
            }
        })
        favouritesAdapter!!.setOnItemDeleteClickListener(object :
            LmisLabTechnicianFavouritesAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: FavouritesModel?
            ) {
                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text} '" + responseContent?.test_master_name + "' Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {

                    viewModel!!.deleteFavourite(
                        facility_UUID,
                        responseContent?.favourite_id!!,
                        deleteRetrofitResponseCallback
                    )

                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })
        favouritesAdapter?.setOnItemViewClickListener(object :
            LmisLabTechnicianFavouritesAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: FavouritesModel?) {
                val ft = childFragmentManager.beginTransaction()
                val managedialog = ManageLmisLabFavouriteFragment()
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
                var responseContents = Gson().toJson(response.body()?.responseContents)
                favouritesAdapter!!.refreshList(response.body()?.responseContents!!)
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


    override fun onRefreshList() {
        viewModel!!.getFavourites(getFavouritesRetrofitCallBack, Lab_UUID)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageLmisLabFavouriteFragment) {
            childFragment.setOnFavRefreshListener(this)
        }
    }

    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getFavourites(getFavouritesRetrofitCallBack, Lab_UUID)
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

    override fun ClearFavParticularPosition(position: Int) {
        favouritesAdapter!!.refreshFavParticularData(position)
    }

    override fun ClearAllData() {
        favouritesAdapter!!.refreshAllData()
    }

    override fun checkanduncheck(position: Int, isSelect: Boolean) {
        TODO("Not yet implemented")
    }

    override fun drugIdPresentCheck(drug_id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun clearDataUsingDrugid(drug_id: Int) {
        TODO("Not yet implemented")
    }

    override fun favouriteID(favouriteID: Int) {
        TODO("Not yet implemented")
    }


}


