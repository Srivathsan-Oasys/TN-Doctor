package com.hmis_tn.doctor.ui.emr_workflow.mrd.ui


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
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.MrdPdfviewBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.DischargePDFRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.mrd.view_model.MRDViewModel
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.QuickRegistrationSaveResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.SampleErrorResponse
import com.hmis_tn.doctor.ui.quickregistration.ui.QuickRegistrationNew
import com.hmis_tn.doctor.utils.FileHelper
import com.hmis_tn.doctor.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*


class DialogMrdPDFViewerActivity : Fragment() {
    private var destinationFile: File? = null
    var binding: MrdPdfviewBinding? = null
    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var patientID: Int? = null
    private var encounter_type: Int? = null
    private var encounter_uuid: Int? = null


    private var viewModel: MRDViewModel? = null

    var downloadZipFileTask: DownloadZipFileTask? =
        null
    var pdfRequestModel: DischargePDFRequestModel = DischargePDFRequestModel()
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
                R.layout.mrd_pdfview,
                container,
                false
            )

        viewModel = ViewModelProvider(this)[MRDViewModel::class.java]
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())


        binding!!.closeImageView.setOnClickListener {


            if (from == "Quick") {
                val mrdtemplatedialog = QuickRegistrationNew()

                (activity as HomeActivity).replaceFragmentNoBack(mrdtemplatedialog)
            } else {
                val mrdtemplatedialog = MRDChildFragment()

                (activity as HomeActivity).replaceFragmentNoBack(mrdtemplatedialog)
            }
        }

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)

        encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        encounter_type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            runTimePermission()

        } else {


            viewModel?.GetMrdPDFf(
                patientID,
                facilitylevelID,
                encounter_uuid,
                encounter_type,
                GetPDFRetrofitCallback
            )

        }


/*


        pdfRequestModel = gson.fromJson<PDFRequestModel>(getActivity()!!.getIntent().getStringExtra(AppConstants?.RESPONSECONTENT),PDFRequestModel::class.java)

        nextpageStatus=getActivity()!!.getIntent().getIntExtra(AppConstants?.RESPONSENEXT,0)


*/








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
            viewModel?.GetMrdPDFf(
                patientID,
                facilitylevelID,
                encounter_uuid,
                encounter_type,
                GetPDFRetrofitCallback
            )
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


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // now, you have permission go ahead
                viewModel?.GetMrdPDFf(
                    patientID,
                    facilitylevelID,
                    encounter_uuid,
                    encounter_type,
                    GetPDFRetrofitCallback
                )

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
            Log.e("PdfResponse", responseBody.toString())
            downloadZipFileTask = DownloadZipFileTask()
            downloadZipFileTask!!.execute(responseBody?.body())


        }

        override fun onBadRequest(response: Response<ResponseBody>?) {

            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: QuickRegistrationSaveResponseModel
            var mError = SampleErrorResponse()
            Toast.makeText(activity, response?.message(), Toast.LENGTH_LONG).show()

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
            saveToDisk(params[0]!!, "${"Discharge_Document"} ${pdfRequestModel.patient_uuid}.pdf")

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
