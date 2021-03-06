package com.hmis_tn.doctor.ui.resultdispatch.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.PdfviewBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.PDFRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.QuickRegistrationSaveResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.SampleErrorResponse
import com.hmis_tn.doctor.ui.quick_reg.view_model.PDFViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.PDFViewModelFactory
import com.hmis_tn.doctor.ui.resultdispatch.request.Requestpdf
import com.hmis_tn.doctor.utils.FileHelper
import com.hmis_tn.doctor.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*

class ResultPDFViewerActivity : Fragment() {
    private var PDFID: Int? = 0
    private var destinationFile: File? = null
    var binding: PdfviewBinding? = null
    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var viewModel: PDFViewModel? = null
    var downloadZipFileTask: DownloadZipFileTask? =
        null
    var pdfRequestModel: PDFRequestModel = PDFRequestModel()
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null

    var from: String = ""

    var nextpageStatus: Int = 0

    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.pdfview,
                container,
                false
            )
        viewModel = PDFViewModelFactory(
            requireActivity().application
        ).create(PDFViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        binding?.headertext?.text = "Result view"
        val args = arguments
        if (args == null) {
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            val bundle = this.arguments
            if (bundle != null) {
                PDFID = bundle.getInt("pdfid")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermission()
            } else {
                if (PDFID != 0) {
                    val requestpdf: Requestpdf = Requestpdf()
                    requestpdf.Id = listOf(PDFID)
                    viewModel?.GetPDFdownload(requestpdf, GetPDFRetrofitCallback)
                }
            }
        }

        binding!!.closeImageView.setOnClickListener {
            val labtemplatedialog = ResultDispatchActivity()
            (activity as HomeActivity).replaceFragmentNoBack(labtemplatedialog)
        }

        return binding!!.root
    }

    private fun runTimePermission() {
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
            val requestpdf: Requestpdf = Requestpdf()
            requestpdf.Id = listOf(PDFID)
            viewModel?.GetPDFdownload(requestpdf, GetPDFRetrofitCallback)
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            Log.i("", "" + grantResults)

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // now, you have permission go ahead
                val requestpdf: Requestpdf = Requestpdf()
                requestpdf.Id = listOf(PDFID)
                viewModel?.GetPDFdownload(requestpdf, GetPDFRetrofitCallback)

            } else {

                getCustomDialog()
            }

        }

    }

    private fun getCustomDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(context)
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

    val GetPDFRetrofitCallback = object : RetrofitCallback<ResponseBody> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseBody>?) {
            //you can now get your file in the InputStream
            downloadZipFileTask = DownloadZipFileTask()
            downloadZipFileTask!!.execute(responseBody?.body())


        }

        override fun onBadRequest(response: Response<ResponseBody>?) {

            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: QuickRegistrationSaveResponseModel
            var mError = SampleErrorResponse()
            try {
                mError = gson.fromJson(
                    response!!.errorBody()!!.string(),
                    SampleErrorResponse::class.java
                )


            } catch (e: IOException) { // handle failure to read error
            }
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
            binding?.progressbar!!.visibility = View.GONE

//            binding?.pdfView!!.fromFile(destinationFile)
//                .password(null)
//                .defaultPage(0)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .enableDoubletap(true)
//                .onPageError { page, _ ->
//                    Toast.makeText(
//                        context,
//                        "Error at page: $page", Toast.LENGTH_LONG
//                    ).show()
//                }
//                .load()
//            Toast.makeText(
//                context,
//                "Storage path: $destinationFile", Toast.LENGTH_LONG
//            ).show()

            destinationFile?.let { FileHelper(context).showNotification(it) }
        }

        override fun doInBackground(vararg params: ResponseBody?): String? {
            saveToDisk(params[0]!!, "${pdfRequestModel.firstName} ${pdfRequestModel.uuid}.pdf")
            return null
        }
    }

    private fun saveToDisk(body: ResponseBody, filename: String) {
        try {
            destinationFile = File(
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
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
}
