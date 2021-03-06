package com.hmis_tn.doctor.ui.quickregistration.ui

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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.PdfviewBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.NewLmisOrderModule
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.ui.LmisLabTechnicianFragment
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.QuickRegistrationSaveResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.SampleErrorResponse
import com.hmis_tn.doctor.ui.quick_reg.view.QuickRegistrationActivity
import com.hmis_tn.doctor.ui.quick_reg.view_model.PDFViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.PDFViewModelFactory
import com.hmis_tn.doctor.ui.quickregistration.model.PrintQuickRequest
import com.hmis_tn.doctor.utils.FileHelper
import com.hmis_tn.doctor.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*

class QuickDialogPDFViewerActivity : Fragment() {

    private var destinationFile: File? = null
    var binding: PdfviewBinding? = null
    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var viewModel: PDFViewModel? = null
    var downloadZipFileTask: DownloadZipFileTask? = null
    var pdfRequestModel: PrintQuickRequest = PrintQuickRequest()
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var from: String = ""
    var nextpageStatus: Int = 0
    var onNext: Int = 0

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
        )
            .create(PDFViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        binding!!.closeImageView.setOnClickListener {
            if (from == "Quick") {
                val labtemplatedialog = QuickRegistrationNew()
                val bundle: Bundle = Bundle()
                bundle.putInt("PIN", 1)
                labtemplatedialog.arguments = bundle
                (activity as HomeActivity).replaceFragmentNoBack(labtemplatedialog)
            } else {
                val labtemplatedialog = QuickRegistrationActivity()
                (activity as HomeActivity).replaceFragmentNoBack(labtemplatedialog)
            }
        }

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        val gson = Gson()
        val args = arguments
        if (args == null) {
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            pdfRequestModel = args.getParcelable(AppConstants.RESPONSECONTENT)!!
            appPreferences?.saveString(AppConstants.LASTPIN, pdfRequestModel.uhid)
            nextpageStatus = args.getInt(AppConstants.RESPONSENEXT)
            from = args.getString("From")!!
            onNext = args.getInt("next")
            Log.i("calback", "..................$pdfRequestModel")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermission()
            } else {
                if (pdfRequestModel.uuid != 0) {
                    viewModel?.GetPDFQuick(pdfRequestModel, GetPDFRetrofitCallback)
                }
            }
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
            viewModel?.GetPDFQuick(pdfRequestModel, GetPDFRetrofitCallback)
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
                viewModel?.GetPDFQuick(pdfRequestModel, GetPDFRetrofitCallback)
            } else {
                getCustomDialog()
            }
        }
    }

    private fun getCustomDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("App need this permission")
            .setCancelable(false)
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                runTimePermission()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Permission!!")
        alert.show()
    }

    val GetPDFRetrofitCallback = object : RetrofitCallback<ResponseBody> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseBody>?) {
            //you can now get your file in the InputStream
            downloadZipFileTask = DownloadZipFileTask()
            downloadZipFileTask!!.execute(responseBody?.body())

            if (onNext == 1) {
                // pdfRequestModel.uhid
                viewModel?.searchPatient(
                    "", pdfRequestModel.uhid,
                    0,
                    10,
                    "modified_date",
                    "DESC",
                    patientSearchRetrofitCallBack
                )
            }
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
                if (mError.statusCode == 422 && mError.printValidationError!!.field == "uhid") {

                    /*          activity!!.finish()

                              startActivity(Intent(context,LabTestActivity::class.java))
          */
                }
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

    val patientSearchRetrofitCallBack = object : RetrofitCallback<NewLmisOrderModule> {
        override fun onSuccessfulResponse(response: Response<NewLmisOrderModule>) {
            val responseContent = response.body()!!.responseContents?.get(0)!!
            if (responseContent.patient_visits?.size!! > 0) {
                appPreferences?.saveInt(
                    AppConstants.PATIENT_UUID,
                    responseContent.patient_visits?.get(0)?.patient_uuid!!
                )

                appPreferences?.saveInt(AppConstants.ENCOUNTER_TYPE, AppConstants.TYPE_OUT_PATIENT)
                val bundle = Bundle()
                bundle.putParcelable("patiendata", responseContent)
                val labTechnicianFragment = LmisLabTechnicianFragment()
                labTechnicianFragment.arguments = bundle//passing data to fragment
                (activity as HomeActivity).replaceFragmentNoBack(labTechnicianFragment)
            }
        }

        override fun onBadRequest(response: Response<NewLmisOrderModule>) {

        }

        override fun onServerError(response: Response<*>) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(failure: String) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
}
