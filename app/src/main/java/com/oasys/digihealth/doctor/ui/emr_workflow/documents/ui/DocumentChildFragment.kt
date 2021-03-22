package com.oasys.digihealth.doctor.ui.emr_workflow.documents.ui

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.*
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.api_service.APIService
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentDocumentChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.ImageviewFragemnt
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.DocumentViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.view_model.DocumentViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DocumentChildFragment : Fragment() {

    private var documentDetails: Attachment? = null
    private var Str_date: String? = ""
    private var getDirectoryPath: String? = ""
    private var file: File? = null
    private var encounter_uuid: Int? = null
    private var patient_uuid: Int? = null
    private var attacheddate: String? = null
    private var attachedname: String? = null
    private var fileformat: String? = null
    private var selectedImagePath: Uri? = null
    private var apiService: APIService? = null
    private var aiiceApplication: HmisApplication = HmisApplication()
    private var binding: FragmentDocumentChildBinding? = null
    private var viewModel: DocumentViewModel? = null
    private var destinationFile: File? = null
    var enableeye: Boolean? = true
    var tempUri: Uri? = null
    var uripath: Uri? = null
    private var deletefavouriteID: Int? = 0
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private val dataList: MutableList<RecyclerDto> = ArrayList()
    private var mAdapter: DocumentAdapter? = null
    private var typeList = mutableMapOf<Int, String>()
    private var selectPeriodUuid: Int? = 0
    private var facility_id: Int? = 0
    private var fromCalenderDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var toCalenderDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val fromCalender = Calendar.getInstance()
    private val toCalender = Calendar.getInstance()
    var startDate: String? = null
    var endDate: String? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    val PERMISSION_REQUEST_CODE = 1001
    val PICK_IMAGE_REQUEST = 900
    lateinit var filePath: Uri
    private var customdialog: Dialog? = null
    var fileNmae: TextView? = null
    private var mCurrentPhotoPath: String? = null
    private var customProgressDialog: CustomProgressDialog? = null
    var downloadZipFileTask: DownloadZipFileTask? =
        null
    var FilePathURL: File? = null
    val items = arrayOf<CharSequence>(
        "Take Photo",
        "Choose from Library",
        "Cancel"
    )

    companion object {
        const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 102
        const val PERMISSION_REQUEST_CAMERA = 103
        const val LOAD_IMAGE_RESULTS = 1001
        const val REQUEST_CAMERA = 1002
        const val RequestPermissionCode = 7

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri?): Boolean {
            return "com.android.externalstorage.documents" == uri!!.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri?): Boolean {
            return "com.android.providers.downloads.documents" == uri!!.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri?): Boolean {
            return "com.android.providers.media.documents" == uri!!.authority
        }
    }

    var prevOrder: Boolean = false

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Utils(requireContext()).setCalendarLocale("en", requireContext())
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_document_child,
                container,
                false
            )
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patient_uuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        aiiceApplication = HmisApplication.get(requireContext())
        apiService = aiiceApplication.getRetrofitService()
        customProgressDialog = CustomProgressDialog(requireContext())
        appPreferences?.saveInt(AppConstants.LAB_MASTER_TYPE_ID, 1)

        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this

        utils = Utils(requireContext())
        trackDocumentVisit(utils!!.getEncounterType())
        viewModel = DocumentViewModelFactory(
            requireActivity().application
        )
            .create(DocumentViewModel::class.java)
        ///Config
        viewModel!!.progress.observe(requireActivity(), Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (progress == View.GONE) {
                customProgressDialog!!.dismiss()
            }
        })

        val sdf =
            SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.calendarEditText?.setText(currentDateandTime)

        binding?.clearCardView?.setOnClickListener {
            binding?.editfilename?.setText("")
            binding?.comments?.setText("")
            binding?.edtFileuploadName?.setText("")
            if (binding?.searchView?.length()!! > 0 || binding?.calendarEditText1?.length()!! > 0) {
                binding?.searchView?.setText("")
                binding?.calendarEditText1?.setText("")
                viewModel?.getAddDocumentTypeList(facility_id!!, addDocumentDeatilsRetrofitCallback)
            }
        }

        binding?.previewLinearLayout?.setOnClickListener {
            if (binding?.resultLinearLayout?.visibility == View.VISIBLE) {
                binding?.resultLinearLayout?.visibility = View.GONE
            } else {
                binding?.resultLinearLayout?.visibility = View.VISIBLE
            }
        }

        viewModel?.getDocumentTypeList(facility_id!!, documentTypeResponseCallback)
        viewModel?.getAddDocumentTypeList(facility_id!!, addDocumentDeatilsRetrofitCallback)

        prepareDocumentLIstData()

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.documentRecyclerView!!.layoutManager = layoutmanager
        binding?.documentRecyclerView!!.adapter = mAdapter

        binding?.calendarEditText1?.setOnClickListener {
            showFromDatePickerDialog()
        }

        binding?.imgview?.setOnClickListener {
            val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val dialog = ImageviewFragemnt()
            val bundle = Bundle()
            bundle.putString(AppConstants.ImageURI, tempUri!!.toString())
            dialog.arguments = bundle
            dialog.show(ft, "Tag")
        }

        binding?.calendarEditText?.setOnClickListener {
            val c: Calendar = Calendar.getInstance(Locale.ENGLISH)
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val c1 = Calendar.getInstance()
                    val mHour = c1[Calendar.HOUR_OF_DAY]
                    val mMinute = c1[Calendar.MINUTE]
                    val mSeconds = c1[Calendar.SECOND]
                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            binding?.calendarEditText?.setText(
                                String.format(
                                    "%02d",
                                    dayOfMonth
                                ) + "-" + String.format(
                                    "%02d",
                                    monthOfYear + 1
                                ) + "-" + year + " " + String.format(
                                    "%02d",
                                    hourOfDay
                                ) + ":" + String.format(
                                    "%02d",
                                    minute
                                ) + ":" + String.format(
                                    "%02d", mSeconds
                                )
                            )
                        },
                        mHour,
                        mMinute,
                        false
                    )
                    timePickerDialog.show()
                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = c.timeInMillis
            //datePickerDialog?.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.saveCardView?.setOnClickListener {
            trackDocumentAddStart(utils!!.getEncounterType())
            if (binding?.calendarEditText!!.text.trim().toString().isEmpty()) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please Enter Date"
                )
                return@setOnClickListener
            }
            if (binding?.editfilename!!.text.trim().toString().isEmpty()) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please Enter file name"
                )
                return@setOnClickListener
            }

            if (binding?.comments!!.text.trim().toString().isEmpty()) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please Enter Comments"
                )
                return@setOnClickListener
            }
            if (binding?.edtFileuploadName!!.text.trim().toString().isEmpty()) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please upload file"
                )
                return@setOnClickListener
            }
            Str_date = utils!!.getDocumentDate(binding!!.calendarEditText.text.trim().toString())
            val requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                FilePathURL
            ) //allow image and any other file
            // MultipartBody.Part is used to send also the actual file name
            val body =
                MultipartBody.Part.createFormData("upfile", FilePathURL!!.name, requestFile)
            val comment = binding?.comments?.text?.toString()
            Log.i("", "selectPeriodUuid" + selectPeriodUuid)

            viewModel?.getUploadFile(
                facility_id!!,
                body,
                getDirectoryPath!!,
                Str_date!!,
                selectPeriodUuid.toString(),
                comment!!,
                getDirectoryPath!!,
                encounter_uuid,
                patient_uuid,
                adduploadCallback
            )
        }

        fromCalenderDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                fromCalender.set(Calendar.YEAR, year)
                fromCalender.set(Calendar.MONTH, monthOfYear)
                fromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setFromDate()
            }

        toCalenderDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                toCalender.set(Calendar.YEAR, year)
                toCalender.set(Calendar.MONTH, monthOfYear)
                toCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setToDate()
            }

        mAdapter?.setOnDeleteClickListener(object : DocumentAdapter.OnItemClickListener {
            override fun onItemParamClick(documentId: Attachment) {
                deletefavouriteID = documentId.uuid
                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)

                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                fileNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                fileNmae!!.text = "${fileNmae!!.text} '" + documentId.attachment_name + "' Record ?"
                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    viewModel!!.deleteAttachments(
                        facility_id,
                        deletefavouriteID, deleteRetrofitResponseCallback
                    )
                    customdialog!!.dismiss()
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })

        mAdapter?.setOnItemdowloadClickListener(object :
            DocumentAdapter.OnItemdownloadClickListener {
            override fun onItemParamClick(documentId: Attachment) {
                trackDocumentDownloadstart(utils!!.getEncounterType())
                documentDetails = documentId

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    runTimePermissionWriteExternalStorage()
                } else {
                    fileformat =
                        documentId.file_path.substring(documentId.file_path.lastIndexOf(".") + 1) // Without dot jpg, png
                    attacheddate = documentId.attached_date
                    attachedname = documentId.attachment_name
                    viewModel?.getDownload(documentId, facility_id, downloadimagecallback)
                }
            }
        })

        binding?.searchView?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                mAdapter!!.getFilter().filter(s.toString())
            }
        })

        binding?.edtFileuploadName?.setOnClickListener {
            RequestMultiplePermission()
        }

        binding?.typeSpinner!!.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemValue = parent.getItemAtPosition(position).toString()
                selectPeriodUuid = typeList.filterValues { it == itemValue }.keys.toList()[0]
                Log.i("", "" + selectPeriodUuid)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return binding!!.root
    }

    private fun RequestMultiplePermission() {
        // Creating String Array with Permissions.
        requestPermissions(
            arrayOf<String>(
                CAMERA,
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE
            ), RequestPermissionCode
        )
    }

    private fun runTimeCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA
                ), PERMISSION_REQUEST_CAMERA
            )
            return
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermission()
            } else {
                AlertDialogView()
            }
            return
        }
    }

    private fun AlertDialogView() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Take Photo") {

                val pictureIntent = Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE
                )
                if (pictureIntent.resolveActivity(requireContext().packageManager) != null) {
                    startActivityForResult(
                        pictureIntent,
                        REQUEST_CAMERA
                    )
                }
                /*  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                  startActivityForResult(intent, REQUEST_CAMERA)*/
            } else if (items[item] == "Choose from Library") {
                openDocument()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",  // suffix
            storageDir // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }

    private fun runTimePermissionWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )

            return
        } else {
            fileformat =
                documentDetails!!.file_path.substring(documentDetails!!.file_path.lastIndexOf(".") + 1) // Without dot jpg, png
            attacheddate = documentDetails!!.attached_date
            attachedname = documentDetails!!.attachment_name

            viewModel?.getDownload(documentDetails!!, facility_id, downloadimagecallback)
            return
        }
    }

    private fun runTimePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
            )
            return
        } else {
            AlertDialogView()

            return
        }
    }

    // Calling override method.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RequestPermissionCode -> if (grantResults.size > 0) {
                val CameraPermission =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                val WritePermission =
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                val ReadPermission =
                    grantResults[2] == PackageManager.PERMISSION_GRANTED

                if (CameraPermission && WritePermission && ReadPermission) {
                    Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_LONG)
                        .show()
                    AlertDialogView()
                } else {
                    getCustomDialog()
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getCustomDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(requireContext())
        // set message of alert dialog
        dialogBuilder.setMessage("App need this permission")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                runTimePermission()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Permission!!")
        // show alert dialog
        alert.show()
    }

    private fun showToDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.TimePickerTheme,
            toCalenderDateSetListener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun showFromDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.TimePickerTheme,
            fromCalenderDateSetListener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun setFromDate() {
        val dateMonthAndYear =
            SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        startDate = dateMonthAndYear.format(fromCalender.time)

        showToDatePickerDialog()
    }

    private fun setToDate() {
        val dateMonthAndYear =
            SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        endDate = dateMonthAndYear.format(toCalender.time)
        binding?.calendarEditText1?.setText(startDate + "~" + endDate)
        mAdapter!!.setDate(startDate, endDate)
        mAdapter!!.getFilter().filter(binding!!.searchView.text.trim().toString())
    }

    fun setDocumentType(responseContents: List<DocumentTypeResponseContent?>?) {
        typeList =
            responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            typeList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.typeSpinner!!.adapter = adapter
    }

    private fun prepareDocumentLIstData() {
        mAdapter = DocumentAdapter((requireActivity()), ArrayList())
        binding?.documentRecyclerView!!.adapter = mAdapter
    }

    private val documentTypeResponseCallback =
        object : RetrofitCallback<DocumentTypeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<DocumentTypeResponseModel>?) {
                selectPeriodUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid
                setDocumentType(responseBody.body()?.responseContents)
            }

            override fun onBadRequest(errorBody: Response<DocumentTypeResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: DocumentTypeResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        DocumentTypeResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.req
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
    private val addDocumentDeatilsRetrofitCallback =
        object : RetrofitCallback<AddDocumentDetailsResponseModel> {
            override fun onSuccessfulResponse(response: Response<AddDocumentDetailsResponseModel>) {

                mAdapter?.refreshList(response.body()?.responseContents?.attachment as ArrayList<Attachment>?)
            }

            override fun onBadRequest(response: Response<AddDocumentDetailsResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: AddDocumentDetailsResponseModel
                try {
                    responseModel = gson.fromJson(
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
    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteDocumentResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteDocumentResponseModel>?) {
            viewModel?.getAddDocumentTypeList(facility_id!!, addDocumentDeatilsRetrofitCallback)
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )
        }

        override fun onBadRequest(errorBody: Response<DeleteDocumentResponseModel>?) {
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

    private fun getPathname(selectedImagePath: Uri?): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(this.requireContext(), selectedImagePath!!, data, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    val downloadimagecallback =
        object : RetrofitCallback<ResponseBody> {
            override fun onSuccessfulResponse(response: Response<ResponseBody>) {
                Log.i("", "message")
                trackDocumentAddComplete(utils?.getEncounterType(), "success", "")
                downloadZipFileTask = DownloadZipFileTask()
                downloadZipFileTask!!.execute(response.body())
            }

            override fun onBadRequest(response: Response<ResponseBody>) {
                trackDocumentAddComplete(utils!!.getEncounterType(), "failure", response.message())
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
                trackDocumentAddComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                trackDocumentAddComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.unauthorized)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                trackDocumentAddComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                trackDocumentAddComplete(utils!!.getEncounterType(), "failure", failure)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    @SuppressLint("StaticFieldLeak")
    inner class DownloadZipFileTask :
        AsyncTask<ResponseBody?, Pair<Int?, Long?>?, String?>() {
        fun doProgress(progressDetails: Pair<Int?, Long?>?) {
            publishProgress(progressDetails)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(result: String?) {
            // binding?.progressbar!!.setVisibility(View.GONE);
            Toast.makeText(
                requireContext(),
                "Storage path: $destinationFile", Toast.LENGTH_LONG
            ).show()
            show_Notification()
        }

        override fun doInBackground(vararg params: ResponseBody?): String? {
            saveToDisk(params[0]!!, "${attacheddate}." + fileformat)
            return null
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun show_Notification() {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_MAX
            )
            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder: Notification.Builder =
            Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setTicker("Hearty365") //     .setPriority(Notification.PRIORITY_MAX)
            .setContentTitle("Default notification")
            .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
            .setContentInfo("Info")
        notificationManager.notify( /*notification id*/1, notificationBuilder.build())
    }

    private fun saveToDisk(body: ResponseBody, filename: String) {
        try {
            destinationFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
            val inputstream: InputStream? = body.byteStream()
            val os: OutputStream = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            //read from is to buffer
            while (inputstream!!.read(buffer).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            inputstream.close()
            //flush OutputStream to write any buffered data to file
            os.flush()
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val adduploadCallback =
        object : RetrofitCallback<FileUploadResponseModel> {
            override fun onSuccessfulResponse(response: Response<FileUploadResponseModel>) {
                trackDocumentDownloadComplete(utils?.getEncounterType(), "success", "")
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Document upload Success"
                )
                val response = Gson().toJson(response.body())
                Log.i("", "response" + response)
                binding?.editfilename?.setText("")
                binding?.edtFileuploadName?.setText("")
                binding?.comments?.setText("")
                viewModel?.getAddDocumentTypeList(facility_id!!, addDocumentDeatilsRetrofitCallback)
            }

            override fun onBadRequest(response: Response<FileUploadResponseModel>) {
                trackDocumentDownloadComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    response.message()
                )
                val gson = GsonBuilder().create()
                val responseModel: AddDocumentDetailsResponseModel
                try {
                    responseModel = gson.fromJson(
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
                trackDocumentDownloadComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                trackDocumentDownloadComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.unauthorized)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                trackDocumentDownloadComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                trackDocumentDownloadComplete(utils!!.getEncounterType(), "failure", failure)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {                    // When an Image is picked
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK) {
            if (data != null) {
                Log.e("test", "data" + data.data)
                selectedImagePath = data.data
                Log.i("test____", "test" + selectedImagePath)
                if (selectedImagePath != null)
                    try {
                        binding?.imgview?.setImageURI(null)
                        binding?.imgview?.setImageURI(selectedImagePath)
                        tempUri = selectedImagePath
                        FilePathURL = FileUtil.from(requireContext(), this.selectedImagePath!!)
                        val strFileName: String = FilePathURL!!.name
                        val fileuploadname: String = selectedImagePath.toString()
                        val fileuploadName: String =
                            fileuploadname.substring(fileuploadname.lastIndexOf("/") + 1)
                        getDirectoryPath = fileuploadName
                        Log.e("getDirectoryPath", "___" + getDirectoryPath)
                        Log.i("", "" + fileuploadName)
                        Log.e("fileuploadName", "data" + fileuploadName.toString())
                        Log.e("strFileName", "data$strFileName")
                        binding?.editfilename?.setText(strFileName)
                        binding?.edtFileuploadName?.setText(fileuploadName)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
            }
        }// When an Video is picked
        else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data) {
            if (data != null && data.extras != null) {
                val imageBitmap = data.extras!!["data"] as Bitmap?
                binding?.imgview?.setImageBitmap(null)
                binding?.imgview!!.setImageBitmap(imageBitmap)
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                tempUri = this.getImageUri(requireContext(), imageBitmap!!)!!
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                val finalFile = File(getRealPathFromURI(tempUri))
                Log.i("", "" + finalFile)
                val strFileName: String = finalFile.name
                val fileuploadname: String = finalFile.toString()
                FilePathURL = finalFile
                binding?.editfilename?.setText(strFileName)
                binding?.edtFileuploadName?.setText(fileuploadname)
            }
        } else {
            Toast.makeText(requireContext(), "You haven't picked Image/Video", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        val cursor: Cursor =
            requireContext().contentResolver.query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    private fun openDocument() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select File"
            ), LOAD_IMAGE_RESULTS
        )
    }

    private fun trackDocumentVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackDocumentVisit(type)
    }

    private fun trackDocumentAddStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackDocumentAddStart(type)
    }

    fun trackDocumentAddComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackDocumentAddComplete(type, status, message)
    }

    fun trackDocumentDownloadstart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackDocumentDownloadstart(type)
    }

    fun trackDocumentDownloadComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackDocumentDownloadComplete(type, status, message)
    }
}