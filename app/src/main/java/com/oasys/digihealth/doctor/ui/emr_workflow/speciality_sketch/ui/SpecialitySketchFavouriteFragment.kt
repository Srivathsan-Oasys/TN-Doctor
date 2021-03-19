package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui

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
import com.oasys.digihealth.doctor.databinding.FragmentSpecialitySketchFavouriteBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.viewmodel.SpecialitySketchViewModel
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class SpecialitySketchFavouriteFragment : Fragment(), ClearFavParticularPositionListener,
    ManageSpecialitySketchFavouriteFragment.OnFavRefreshListener {

    @SuppressLint("ClickableViewAccessibility")
    private var binding: FragmentSpecialitySketchFavouriteBinding? = null
    lateinit var drugNmae: TextView
    private var facility_UUID: Int? = 0
    private var department_uuid: Int? = null
    private var viewModel: SpecialitySketchViewModel? = null
    lateinit var favouritesAdapter: SpecialitySketchFavouritesAdapter
    var mCallback: FavClickedListener? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var customdialog: Dialog? = null

    var drugName: String = ""

    private var customProgressDialog: CustomProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_speciality_sketch_favourite,
            container,
            false
        )

        viewModel = SpecialitySketchViewModelFactory(
            requireActivity().application
        ).create(SpecialitySketchViewModel::class.java)
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
        initFavouritesAdapter()

        isLoading(true)
        viewModel!!.getSpecialitySketchFavourites(getFavouritesRetrofitCallBack)



        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e("onQueryTextSubmit", query)
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                Log.e("callSearch", "" + query)
                favouritesAdapter.getFilter().filter(query)
            }

        })

        binding?.manageFavouritesCardView?.setOnClickListener {

            val ft = childFragmentManager.beginTransaction()
            val managedialog = ManageSpecialitySketchFavouriteFragment()
            managedialog.show(ft, "Tag")

        }

        return binding!!.root
    }


    private fun initFavouritesAdapter() {
        favouritesAdapter =
            SpecialitySketchFavouritesAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = favouritesAdapter
        favouritesAdapter.setOnItemClickListener(object :
            SpecialitySketchFavouritesAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: FavouritesModel?,
                position: Int,
                selected: Boolean
            ) {
//                favouritesAdapter.updateSelectStatus(1,position, selected)
                mCallback?.sendData(responseContent, position, selected)

            }
        })

        favouritesAdapter.setOnItemDeleteClickListener(object :
            SpecialitySketchFavouritesAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: FavouritesModel?
            ) {


                drugName = responseContent?.favourite_name ?: ""

                isLoading(true)
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
                     customdialog?.dismiss()
                 }
                 drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                 drugNmae.text ="${drugNmae.text} '"+responseContent?.favourite_name+"' Record ?"
                 val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                 val noBtn = customdialog!! .findViewById(R.id.no) as CardView
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
            SpecialitySketchFavouritesAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: FavouritesModel?) {
                val ft = childFragmentManager.beginTransaction()
                val managedialog = ManageSpecialitySketchFavouriteFragment()
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, responseContent)
                Log.e("edit Response content", responseContent.toString())
                managedialog.arguments = bundle
                managedialog.show(ft, "Tag")
            }
        })
    }


    val getFavouritesRetrofitCallBack =
        object : RetrofitCallback<FavouritesResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavouritesResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    favouritesAdapter.refreshList(response.body()?.responseContents!!)
                    favouritesAdapter.notifyDataSetChanged()
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
                isLoading(false)
            }
        }


    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {

            isLoading(true)
            viewModel!!.getSpecialitySketchFavourites(getFavouritesRetrofitCallBack)
            //       customdialog!!.dismiss()

            toast("$drugName Deleted Successfully")
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

    private fun toast(s: String) {

        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()

    }

    override fun ClearFavParticularPosition(position: Int) {
        favouritesAdapter.refreshFavParticularData(position)
    }

    override fun ClearAllData() {
        favouritesAdapter.refreshAllData()
    }

    override fun checkanduncheck(position: Int, isSelect: Boolean) {

    }

    override fun checkanduncheck(position: ArrayList<Int>, isSelect: Boolean) {

    }

    override fun drugIdPresentCheck(drug_id: Int): Boolean {


        return false
    }

    override fun clearDataUsingDrugid(drug_id: Int) {

        Log.i("send", "$drug_id")
        Log.i("check", "${favouritesAdapter.checkData(drug_id)}")

        if (favouritesAdapter.checkData(drug_id)) {

            favouritesAdapter.refreshAllData()

            favouritesAdapter.updateformList(drug_id)

        }


    }

    override fun favouriteID(favouriteID: Int) {
        viewModel!!.getSpecialitySketchFavourites(getFavouritesRetrofitCallBack)
    }

    override fun updateSelectStatus(favOrTem: Int, position: Int, selected: Boolean) {
        favouritesAdapter.updateSelectStatus(1, position, selected)
    }


    override fun onFavID(position: Int) {
        Log.e("onRefreshList", "inside")
        viewModel!!.getSpecialitySketchFavourites(getFavouritesRetrofitCallBack)
    }

    override fun onRefreshList() {
        Log.e("onRefreshList", "inside")
        viewModel!!.getSpecialitySketchFavourites(getFavouritesRetrofitCallBack)
    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageSpecialitySketchFavouriteFragment) {
            childFragment.setOnFavRefreshListener(this)
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


    fun isLoading(st: Boolean) {

        if (st) {
            customProgressDialog!!.show()
        } else {
            customProgressDialog!!.dismiss()
        }
    }
}