package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.Context.MODE_PRIVATE
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentSpecialitySketchChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.DietOrderrequest
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.DietOrderrequestDetail
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.request.DietOrderrequestHeaders
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.model.AddDocumentDetailsResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.ClearTemplateParticularPositionListener
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.viewmodel.SpecialitySketchViewModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//sri doubt

class SpecialitySketchChildFragment : Fragment(),
    SpecialitySketchFavouriteFragment.FavClickedListener, View.OnTouchListener,
    SpeciaitySketchPrevFragment.PrevClickedListener {

    private var arrayItemData: ArrayList<FavouritesModel?>? = null
    private var binding: FragmentSpecialitySketchChildBinding? = null
    lateinit var drugNmae: TextView
    private var viewModel: SpecialitySketchViewModel? = null
    private var utils: Utils? = null
    private var customdialog: Dialog? = null
    private var responseContents: String? = ""
    var speciality_sketch_uuid: Int = 0
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    var appPreferences: AppPreferences? = null
    var facility_id: Int? = null
    var searchposition: Int = 0
    var mCallbackLabFavFragment: ClearFavParticularPositionListener? = null
    var mCallbackLabTemplateFragment: ClearTemplateParticularPositionListener? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var fragmentBackClick: FragmentBackClick? = null
    val detailsList = ArrayList<DietOrderrequestDetail>()

    var FilePathURL: File? = null
    private var fileformat: String? = ""

    var selectPosition: Int = 0
    var dataSize: Int = 0
    var listData = ArrayList<SpecialitySketchDetail>()

    val header: DietOrderrequestHeaders? = DietOrderrequestHeaders()
    var mAdapter: SpecialityListAdapter? = null
    private var customProgressDialog: CustomProgressDialog? = null
    val emrRequestModel: DietOrderrequest? = DietOrderrequest()
    var prevOrder: Boolean = false
    var TAG: String = "SpecialitySketchChildFragment"
    var imagePath: String = ""

    //"http://dev02hmis.oasyshealth.co/assets/images/heart_outline.png"
    var imageBitmap: Bitmap? = null
    var mutableBitmap: Bitmap? = null
    var downX: Float = 0F
    var downY: Float = 0F
    var canvasPaint: Canvas? = null


    var paint: Paint? = null
    var paintView: View? = null
    var paintEnable: Int? = 0


    var patient_UUID: Int = 0

    var encounter_Type: Int = 0

    private var autocompleteTestResponse: List<SpecialitySketchFavMangeResponseContents>? = null


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_speciality_sketch_child,
                container,
                false
            )


        viewModel = SpecialitySketchViewModelFactory(
            requireActivity().application
        )
            .create(SpecialitySketchViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        trackSpecialitySketchVisit(utils?.getEncounterType())

        customProgressDialog = CustomProgressDialog(requireContext())

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )


        binding?.zoomControls1?.setOnZoomInClickListener {

            val x: Float = binding?.selectedImageview!!.scaleX
            val y: Float = binding?.selectedImageview!!.scaleY

            binding?.selectedImageview!!.scaleX = (x + 1)
            binding?.selectedImageview!!.scaleY = (y + 1)
        }

        binding?.zoomControls1?.setOnZoomOutClickListener {

            val x: Float = binding?.selectedImageview!!.scaleX
            val y: Float = binding?.selectedImageview!!.scaleY

            binding?.selectedImageview!!.scaleX = (x - 1)
            binding?.selectedImageview!!.scaleY = (y - 1)
        }

        setupViewPager(binding?.viewpager!!)
        appPreferences = AppPreferences.getInstance(
            requireContext(),
            AppConstants.SHARE_PREFERENCE_NAME
        )
        patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!
        val department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        val encounter_doctor_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager)

        Thread {
            convertURLToBitmap()
        }.start()


        binding?.saveCardView!!.setOnClickListener {
//            trackSpecialitySketchSaveStart(utils?.getEncounterType())

            if (speciality_sketch_uuid != 0) {

                isLoading(true)

                viewModel?.getEncounter(
                    facility_id!!,
                    patient_UUID,
                    encounter_Type,
                    fetchEncounterRetrofitCallBack
                )
            } else {
                Toast.makeText(requireContext(), "Please select any item", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding?.clearCardView?.setOnClickListener {

            clearUI()
        }


        /*     Picasso.get().load(imagePath)
                 .into(binding?.selectedImageview)


            */

        binding?.cropImage?.setOnClickListener {

            if (imagePath != "")
                performCrop(imagePath)

        }
        binding?.zoomImage?.setOnClickListener {
            if (binding?.zoomLayout!!.isVisible) {
                binding?.zoomLayout?.visibility = View.GONE
            } else {
                binding?.zoomLayout?.visibility = View.VISIBLE
            }
        }
        binding?.editImage?.setOnClickListener {

        }
        binding?.textImage?.setOnClickListener {
            textOnImage()
        }
        binding?.paintImage?.setOnClickListener {
            if (paintEnable == 0) {
                paintEnable = 1
                Toast.makeText(requireContext(), "Click image to fill colour", Toast.LENGTH_LONG)
                    .show()
            } else {
                paintEnable = 0
            }
        }

        binding?.selectedImageview?.setOnTouchListener(this)



        binding?.autoCompleteTextName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }


            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    viewModel?.getSpecialitySketchName(
                        s.toString(),
                        favtSpecialitySketchNameCallBack
                    )

                }
            }
        })


        binding?.autoCompleteTextName!!.setOnItemClickListener { parent, _, position, id ->


            binding?.saveCardView?.visibility = View.VISIBLE
            binding?.clearCardView?.visibility = View.VISIBLE

            binding?.autoCompleteTextName?.setText(autocompleteTestResponse?.get(position)?.s_name)

            Log.i("", "" + autocompleteTestResponse!!.get(position).s_name)

            speciality_sketch_uuid = autocompleteTestResponse!!.get(position).s_uuid

            mCallbackLabFavFragment?.clearDataUsingDrugid(speciality_sketch_uuid)

            isLoading(true)


            viewModel?.getSpecialitySketchId(
                speciality_sketch_uuid,
                favtSpecialitySketchIdCallBack
            )

        }


        var linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding?.sessionlist!!.layoutManager = linearLayoutManager

        mAdapter = SpecialityListAdapter(requireContext(), ArrayList())
        binding?.sessionlist!!.adapter = mAdapter


        mAdapter!!.setOnImageClickListener(object : SpecialityListAdapter.OnImageClickListener {
            override fun onClick(bitmap: Bitmap) {

                binding?.selectedImageview?.setImageBitmap(bitmap)

                convertURLToBitmap(bitmap)


            }
        })

        return binding!!.root
    }


    fun save(doctor_uuid: String, encounterId: String) {


        val mTimeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mImageName = "snap_$mTimeStamp"
        val wrapper = ContextWrapper(requireContext())
        FilePathURL = wrapper.getDir("Images", MODE_PRIVATE)
        FilePathURL = File(FilePathURL, "savebitmap.jpg")
        try {
            var stream: OutputStream? = null
            stream = FileOutputStream(FilePathURL)
            mutableBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        val requestFile = RequestBody.create(
            MediaType.parse("image/png"),
            FilePathURL
        )
        val body =
            MultipartBody.Part.createFormData("file", "specialskech.png", requestFile)


        viewModel?.addSpecialty(
            body,
            speciality_sketch_uuid,
            doctor_uuid,
            encounterId,
            saveRetrofitCallback
        )

    }

    fun clearUI() {

        binding?.autoCompleteTextName?.setText("")

        mCallbackLabFavFragment?.ClearAllData()
        binding?.selectedImageview?.invalidate()
        binding?.selectedImageview?.setImageBitmap(null)
        mAdapter?.clear()
        speciality_sketch_uuid = 0
    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(SpecialitySketchFavouriteFragment(), "Favourite")
        adapter.addFragment(SpeciaitySketchPrevFragment(), "Prev.Sketch")

        viewPager.adapter = adapter

    }

    private fun textOnImage() {

        var dialogBuilder = AlertDialog.Builder(requireContext())
        var et = EditText(dialogBuilder.context)
        et.inputType = InputType.TYPE_CLASS_TEXT
        et.layoutParams = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        et.hint = "Description"

        dialogBuilder.setView(et)
        dialogBuilder          //.setMessage("Add Message")
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener { dialog, id ->
                val str: String = et.text.toString().trim()
                val v: View = CanvasWithText(requireActivity(), str)
                if (imageBitmap != null) {
                    val mutableBitmap: Bitmap = imageBitmap!!.copy(Bitmap.Config.ARGB_8888, true)
                    val canvas = Canvas(mutableBitmap)
                    v.draw(canvas)
                    var b = mutableBitmap //for saving "b" to the sdcard
                    binding?.selectedImageview?.setImageBitmap(mutableBitmap)
                }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Add Text")
        alert.show()
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = java.util.ArrayList<Fragment>()
        private val mFragmentTitleList = java.util.ArrayList<String>()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    val saveRetrofitCallback =
        object : RetrofitCallback<SimpleResponseModel> {
            override fun onSuccessfulResponse(response: Response<SimpleResponseModel>) {

                Toast.makeText(
                    requireContext(),
                    "Speciality Sketch Saved Sucessfully",
                    Toast.LENGTH_SHORT
                ).show()

                clearUI()


                isLoading(false)

            }

            override fun onBadRequest(response: Response<SimpleResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: SimpleResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        SimpleResponseModel::class.java
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


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is SpecialitySketchFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }

        if (childFragment is SpeciaitySketchPrevFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackLabFavFragment = childFragment
        }


    }

    override fun sendData(favmodel: FavouritesModel?, position: Int, selected: Boolean) {
//        favmodel?.isFavposition = position


        if (speciality_sketch_uuid != favmodel?.speciality_sketch_id!!) {

            mCallbackLabFavFragment?.clearDataUsingDrugid(favmodel.speciality_sketch_id!!)
            binding?.drawerLayout?.closeDrawer(GravityCompat.END)


            speciality_sketch_uuid = favmodel.speciality_sketch_id!!


            isLoading(true)

            viewModel?.getSpecialitySketchId(
                favmodel.speciality_sketch_id!!,
                favtSpecialitySketchIdCallBack
            )

        } else {

            clearUI()

        }

    }

    private fun performCrop(imagePath: String) {
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")

            val bytes = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String =
                MediaStore.Images.Media.insertImage(
                    requireActivity().contentResolver,
                    imageBitmap!!,
                    "Title",
                    null
                )
            Log.e("path", path.toString())
            var contentUri = Uri.parse(path)
            Log.e("contentUri", contentUri.toString())
            cropIntent.setDataAndType(contentUri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("outputX", 280)
            cropIntent.putExtra("outputY", 280)
            cropIntent.putExtra("return-data", true)
            startActivityForResult(cropIntent, 100)
        } catch (anfe: ActivityNotFoundException) {
            val errorMessage = "your device doesn't support the crop action!"
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val extras = data.extras
                    if (extras != null) {
                        val selectedBitmap: Bitmap? = extras.getParcelable("data")
                        binding?.selectedImageview?.invalidate()
                        binding?.selectedImageview?.setImageBitmap(selectedBitmap)
                        Log.e("selectedBitmap", selectedBitmap.toString())

                    }
                }
            }
        }
    }

    private fun convertURLToBitmap() {
        try {
            val url = URL(imagePath)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            imageBitmap = BitmapFactory.decodeStream(input)
            //var bm = downloadImage(src)
            Log.e("bitmap", imageBitmap.toString())


            mutableBitmap = imageBitmap!!.copy(Bitmap.Config.ARGB_8888, true)
            canvasPaint = Canvas(mutableBitmap!!)

            Log.e("drawBitmap", "inside")

        } catch (e: IOException) {
            Log.e("convertURLToBitmap", e.toString())
            e.printStackTrace()
        }
    }

    class CanvasWithText(context: Context?, private val str: String) :
        View(context) {
        private val pBackground: Paint
        private val pText: Paint
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            pBackground.color = Color.WHITE
            canvas.drawRect(0F, 0F, 100F, 100F, pBackground)
            pText.color = Color.BLUE
            pText.textSize = 30F
            canvas.drawText(str, 100F, 20F, pText)
        }

        init {
            pBackground = Paint()
            pText = Paint()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        when (event?.action) {
            /* MotionEvent.ACTION_DOWN ->{
                 downX = event?.getX()!!
                 downY = event?.getY()!!
                }*/
            MotionEvent.ACTION_MOVE -> {
                downX = event.x
                downY = event.y
            }

            /*   MotionEvent.ACTION_UP ->{
                   downX = event?.getX()!!
                   downY = event?.getY()!!
               }*/
        }

        if (paintEnable == 1) {
            val view: View = CanvasWithPaint(requireActivity(), downX, downY) //paintView
            view.draw(canvasPaint)
            binding?.selectedImageview?.setImageBitmap(mutableBitmap)
        }
        return true

    }

    class CanvasWithPaint(context: Context?, private val downX: Float, private val downY: Float) :
        View(context) {
        private val pText: Paint
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            pText.color = Color.RED
            pText.strokeWidth = 20F
            pText.isAntiAlias = true
            pText.isFilterBitmap = true
            pText.isDither = true
            if (downX > 0 && downY > 0) {
                canvas.drawCircle(downX, downY, 30F, pText)
            }

        }

        init {
            pText = Paint()
        }
    }

    private fun trackSpecialitySketchVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackSpecialitySketchVisit(type)
    }

    private fun trackSpecialitySketchSaveStart(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackSpecialitySketchSaveStart(type)
    }

    private fun trackSpecialitySketchSaveComplete(
        type: String?,
        status: String?,
        message: String?
    ) {
        AnalyticsManager.getAnalyticsManager().trackSpecialitySketchSaveComplete(
            type,
            status,
            message
        )
    }


    val favtSpecialitySketchNameCallBack =
        object : RetrofitCallback<SpecialitySketchFavMangeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<SpecialitySketchFavMangeResponseModel>?) {
                Log.i("", "" + responseBody?.body()?.responseContents)

                autocompleteTestResponse = responseBody?.body()?.responseContents
                val responseContentAdapter = SpecialityNameSearchResultAdapter(
                    context!!,
                    R.layout.row_chief_complaint_search_result,
                    responseBody?.body()?.responseContents!!
                )
                binding?.autoCompleteTextName?.threshold = 1
                binding?.autoCompleteTextName?.setAdapter(responseContentAdapter)
                binding?.autoCompleteTextName?.showDropDown()

            }

            override fun onBadRequest(errorBody: Response<SpecialitySketchFavMangeResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: SpecialitySketchFavMangeResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody?.errorBody()!!.string(),
                        SpecialitySketchFavMangeResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        ""
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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }

        }


    val favtSpecialitySketchIdCallBack = object : RetrofitCallback<SpecalityListResponce> {
        override fun onSuccessfulResponse(response: Response<SpecalityListResponce>) {

            if (isAdded)
                if (response.body()?.responseContents != null) {


                    mAdapter!!.setData(response.body()?.responseContents!!.speciality_sketch_details as ArrayList<SpecialitySketchDetail>)

                    dataSize =
                        (response.body()?.responseContents!!.speciality_sketch_details as ArrayList<SpecialitySketchDetail>).size

                    listData =
                        response.body()?.responseContents!!.speciality_sketch_details as ArrayList<SpecialitySketchDetail>

                    selectPosition = 0

                    dowloadimage(listData[0].sketch_path, selectPosition)


                } else {

                    isLoading(false)


                }


        }

        override fun onBadRequest(response: Response<SpecalityListResponce>) {

            /* utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 response.message()
             )*/
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


    private fun dowloadimage(filepath: String?, position: Int) {
        fileformat =
            filepath!!.substring(filepath.lastIndexOf(".") + 1) // Without dot jpg, png
        viewModel?.getImage(
            filepath,
            downloadimagecallback,
            facility_id
        )
    }

    val downloadimagecallback =
        object : RetrofitCallback<ResponseBody> {
            override fun onSuccessfulResponse(response: Response<ResponseBody>) {
                val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())


                mAdapter?.setImage(bmp, selectPosition)

                if (selectPosition == 0) {


                    binding?.selectedImageview?.setImageBitmap(bmp)

                    convertURLToBitmap(bmp)

                }

                selectPosition++

                if (selectPosition < dataSize) {

                    dowloadimage(
                        listData[selectPosition].sketch_path,
                        selectPosition
                    )
                } else {


                    isLoading(false)

                }


            }

            override fun onBadRequest(response: Response<ResponseBody>) {
                val responseModel: AddDocumentDetailsResponseModel
                try {
                    responseModel = GsonBuilder().create().fromJson(
                        response.errorBody()!!.string(),
                        AddDocumentDetailsResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        response.message()
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


    private fun convertURLToBitmap(bitmap: Bitmap) {
        try {

            imageBitmap = bitmap
            //var bm = downloadImage(src)
            Log.e("bitmap", imageBitmap.toString())


            mutableBitmap = imageBitmap!!.copy(Bitmap.Config.ARGB_8888, true)
            canvasPaint = Canvas(mutableBitmap!!)

            Log.e("drawBitmap", "inside")

        } catch (e: IOException) {
            Log.e("convertURLToBitmap", e.toString())
            e.printStackTrace()
        }
    }


    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    var encounterResponseContent = response.body()?.responseContents!!
                    var doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.doctor_uuid!!
                    var encounter_uuid = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)
                    save(doctor_en_uuid.toString(), encounter_uuid.toString())

                } else {
                    viewModel?.createEncounter(
                        patient_UUID,
                        encounter_Type,
                        createEncounterRetrofitCallback
                    )
                }
            }

            override fun onBadRequest(response: Response<FectchEncounterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FectchEncounterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FectchEncounterResponseModel::class.java
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


    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


            var doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.doctor_uuid!!
            var encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            var patient_UUID =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            save(doctor_en_uuid.toString(), encounter_uuid.toString())
        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CreateEncounterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CreateEncounterResponseModel::class.java
                )
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       responseModel.message!!
                   )*/
            } catch (e: Exception) {
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       getString(R.string.something_went_wrong)
                   )*/
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            /* utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 getString(R.string.something_went_wrong)
             )*/
        }

        override fun onUnAuthorized() {
            /*    utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )*/
        }

        override fun onForbidden() {
            /*   utils?.showToast(
                   R.color.negativeToast,
                   binding?.mainLayout!!,
                   getString(R.string.something_went_wrong)
               )*/
        }

        override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    override fun sendPrevToMain(prevdata: SpecialitySketchPrevContent) {

        binding?.drawerLayout?.closeDrawer(GravityCompat.END)


        binding?.saveCardView?.visibility = View.GONE
        binding?.clearCardView?.visibility = View.GONE

        if (prevdata.sketch_path != "") {
            isLoading(true)
            viewModel?.getImage(
                prevdata.sketch_path!!,
                downloadimageViewcallback,
                facility_id
            )
        }

    }


    val downloadimageViewcallback =
        object : RetrofitCallback<ResponseBody> {
            override fun onSuccessfulResponse(response: Response<ResponseBody>) {
                val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())

                binding?.selectedImageview?.setImageBitmap(bmp)


                isLoading(false)

            }

            override fun onBadRequest(response: Response<ResponseBody>) {
                val responseModel: AddDocumentDetailsResponseModel
                try {
                    responseModel = GsonBuilder().create().fromJson(
                        response.errorBody()!!.string(),
                        AddDocumentDetailsResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        response.message()
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


    fun isLoading(st: Boolean) {

        if (st) {

            customProgressDialog!!.show()

        } else {

            customProgressDialog!!.dismiss()
        }


    }

}
