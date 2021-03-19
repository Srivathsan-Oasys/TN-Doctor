package com.oasys.digihealth.doctor.ui.emr_workflow.certificate.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.toast
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogPrevCertificateBinding
import com.oasys.digihealth.doctor.databinding.FragmentCertificateChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.certificate.model.GetCertificateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.certificate.model.TemplatesResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.certificate.view_model.CertificateViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.certificate.view_model.CertificateViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request.Headers
import com.oasys.digihealth.doctor.ui.quick_reg.model.QuickRegistrationSaveResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.SampleErrorResponse
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PrevCertificateDialog : DialogFragment() {
    private var content: String? = null
    private var viewModel: CertificateViewModel? = null
    var binding: DialogPrevCertificateBinding? = null
    var binding1: FragmentCertificateChildBinding? = null

    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var certificateID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    val header: Headers = Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    var prevCertificateDialogAdapter: PrevCertificateDialogAdapter? = null
    var bitmap: Bitmap? = null
    private var listTemplatesItems: List<TemplatesResponseContent?> = ArrayList()
    var downloadFileFormat: String = ".pdf"

    var destinationFile: File? = null
    private var toDownload: Boolean = false

    var downloadZipFileTask: DownloadZipFileTask? = null

    companion object {
        const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 102

        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            val b = Bitmap.createBitmap(600, 200, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.draw(c)

            return b
        }
    }


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
            DataBindingUtil.inflate(inflater, R.layout.dialog_prev_certificate, container, false)
        viewModel = CertificateViewModelFactory(
            requireActivity().application
        ).create(CertificateViewModel::class.java)
        binding1 =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_certificate_child,
                container,
                false
            )


        binding?.viewModel = viewModel
        binding1?.viewModel = viewModel

        binding?.lifecycleOwner = this

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        val patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        binding?.viewModel?.getCertificateAll(
            facility_UUID!!,
            patient_UUID!!,
            getCertificateRetrofitCallback
        )

        utils = Utils(requireContext())
        trackPrevCertificateVisit(utils!!.getEncounterType())

        preparePatientLIstData()


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            dialog?.dismiss()
        }
        prevCertificateDialogAdapter?.setOnItemdowloadClickListener(object :
            PrevCertificateDialogAdapter.OnItemdownloadClickListener {
            override fun onItemParamClick(certificate_id: Int) {

                certificateID = certificate_id


                //convertPdf()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    runTimePermissionWriteExternalStorage()

                } else {
                    // selectFileFormat();
                    //createBitmap()
                    viewModel?.GetCertificatePDF(certificate_id, GetPDFRetrofitCallback)
                }


            }

        })




        return binding!!.root
    }


    val getCertificateRetrofitCallback = object : RetrofitCallback<GetCertificateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetCertificateResponseModel>?) {
            if (responseBody?.body()?.responseContent?.isNotEmpty()!!) {
                viewModel?.errorText?.value = 8.toString()
                prevCertificateDialogAdapter?.refreshList(responseBody.body()?.responseContent!!)

            } else {
                viewModel?.errorText?.value = 0.toString()
            }

        }

        override fun onBadRequest(errorBody: Response<GetCertificateResponseModel>?) {

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
            viewModel!!.progress.value = 8
        }
    }

    private fun preparePatientLIstData() {

        prevCertificateDialogAdapter =
            activity?.let {
                PrevCertificateDialogAdapter((requireActivity())) {
                    //convertPdf()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        runTimePermissionWriteExternalStorage()

                    } else {
                        // selectFileFormat();
                        //createBitmap()

                        //viewModel?.GetCertificatePDF(35,GetPDFRetrofitCallback)

                    }
                }
            }!!
        binding?.certificateRecyclerView!!.adapter = prevCertificateDialogAdapter
    }

    private fun runTimePermissionWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CertificateChildFragment.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        } else {
            // selectFileFormat()
            viewModel?.GetCertificatePDF(certificateID!!, GetPDFRetrofitCallback)
            return
        }
    }

    private fun createBitmap() {
        var viewLayout = binding1?.templateLayout
        // val viewLayout = binding1?.TemplateData
        viewLayout!!.isDrawingCacheEnabled = true
        viewLayout.buildDrawingCache()
        var height: Int = 2000
        if (listTemplatesItems.size > 15) {
            height = viewLayout.getChildAt(0).height
        }

        bitmap = loadBitmapFromView(
            viewLayout,
            viewLayout.getChildAt(0).width,
            height
        )

        createPdf()
    }

/*
    private fun downloadPdf() {
        viewModel?.progress?.value = 0
        val folderName = "${FileHelperReports.PARENT_FOLDER}/Reports"
        val simpleDateFormat = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
        val date = simpleDateFormat.format(Calendar.getInstance().time)

        val fileHelper = FileHelperReports(context)
        val bitmap = fileHelper.getScreenshotFromRecyclerView(binding1?.dayWisePLRecyclerView)
        bitmap?.let {
            fileHelper.saveImageToPDF(
                title = binding1?.templateLayout!!,
                bitmap = bitmap,
                folder = File(
                    Environment.getExternalStorageDirectory().toString() + "/" + folderName
                ),
                filename = "AdmissionDoctorWise$date"
            )
        }
        toDownload = false
        viewModel?.progress?.value = 8
    }
*/


    private fun createPdf() {

        val wm = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displaymetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displaymetrics)

        val hight = displaymetrics.heightPixels.toFloat()
        val width = displaymetrics.widthPixels.toFloat()

        val convertHighet = hight.toInt()
        val convertWidth = width.toInt()

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()
        canvas.drawPaint(paint)

        bitmap = Bitmap.createScaledBitmap(bitmap!!, convertWidth, convertHighet, true)
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        document.finishPage(page)
        try {
            document.writeTo(FileOutputStream(createPDFPath() ?: ""))
            if (downloadFileFormat.equals(".pdf")) {
                requireContext().toast("PDF downloaded successfully\n" + createPDFPath())

                destinationFile =
                    File(
                        requireContext().getExternalFilesDir(null)
                            .toString() + "/" + createPDFPath().toString()
                    )
                val testIntent = Intent(Intent.ACTION_VIEW)
                testIntent.type = "application/pdf"
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val uri: Uri = FileProvider.getUriForFile(
                    requireContext(), requireContext().applicationContext
                        .packageName.toString() + ".provider", destinationFile!!
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(uri, "application/pdf")
                startActivity(intent)
            } else {
                requireContext().toast("Excel downloaded successfully\n" + createPDFPath())
            }

        } catch (e: IOException) {

        }
        document.close()
    }


    fun createPDFPath(): String? {
        Log.e("createPDFPath", "inside")
        val foldername = "Certificate"
        val folder =
            File(Environment.getExternalStorageDirectory().toString() + "/" + foldername)


        if (!folder.exists()) {
            folder.mkdirs()
        }
        val simpleDateFormat =
            SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
        val date =
            simpleDateFormat.format(Calendar.getInstance().time)
        return "certificate" + date + downloadFileFormat
    }


    fun trackPrevCertificateVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackPrevCertificateVisit(type)
    }


    val GetPDFRetrofitCallback = object : RetrofitCallback<ResponseBody> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseBody>?) {
            //you can now get your file in the InputStream
            Log.e("PdfResponse", responseBody.toString())
            downloadZipFileTask = DownloadZipFileTask()
            downloadZipFileTask!!.execute(responseBody?.body())


        }

        override fun onBadRequest(response: Response<ResponseBody>?) {

            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: QuickRegistrationSaveResponseModel
            var mError = SampleErrorResponse()

        }

        override fun onServerError(response: Response<*>) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    inner class DownloadZipFileTask :
        AsyncTask<ResponseBody?, Pair<Int?, Long?>?, String?>() {

        fun doProgress(progressDetails: Pair<Int?, Long?>?) {
            publishProgress(progressDetails)
        }

        override fun onPostExecute(result: String?) {
            //binding?.pr!!.setVisibility(View.GONE);

            /* binding?.pdfView!!.fromFile(destinationFile)
                 .password(null)
                 .defaultPage(0)
                 .enableSwipe(true)
                 .swipeHorizontal(false)
                 .enableDoubletap(true)
                 .onPageError { page, _ ->
                     Toast.makeText(
                         context,
                         "Error at page: $page", Toast.LENGTH_LONG
                     ).show()
                 }.load()*/

            val root_sd = Environment.getExternalStorageDirectory()

            destinationFile = File("$root_sd/Certificate")
            Toast.makeText(
                context,
                "Storage path:" + createPDFPath().toString() + "\n Downloaded Successfully",
                Toast.LENGTH_LONG
            ).show()


        }

        override fun doInBackground(vararg params: ResponseBody?): String? {
            saveToDisk(params[0]!!, createPDFPath().toString())

            return null
        }
    }


    private fun saveToDisk(body: ResponseBody, filename: String) {
        try {
            destinationFile = File(
                Environment.getExternalStorageDirectory(),
                "/Certificate/" + filename
            )
            val inputstream: InputStream? = body.byteStream()
            val os: OutputStream = FileOutputStream(destinationFile!!)
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

}

