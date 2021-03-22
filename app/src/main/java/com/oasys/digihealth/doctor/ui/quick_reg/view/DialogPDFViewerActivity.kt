package com.oasys.digihealth.doctor.ui.quick_reg.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.PdfviewBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.quick_reg.model.PDFRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.QuickRegistrationSaveResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.SampleErrorResponse
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.PDFViewModel
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.PDFViewModelFactory
import com.oasys.digihealth.doctor.ui.quickregistration.ui.QuickRegistrationNew
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*


class DialogPDFViewerActivity : Fragment() {
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

    companion object {
        const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101

    }

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

            nextpageStatus = args.getInt(AppConstants.RESPONSENEXT)

            from = args.getString("From")!!

            Log.i("calback", "..................$pdfRequestModel")


/*            if(nextpageStatus==0){

                var Lab = LabTestActivity()

                (activity as HomeActivity).replaceFragmentNoBack(Lab)

            }*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermission()

            } else {

                if (pdfRequestModel.uuid != 0) {
                    viewModel?.GetPDFf(pdfRequestModel, GetPDFRetrofitCallback)
                }
            }
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
            viewModel?.GetPDFf(pdfRequestModel, GetPDFRetrofitCallback)
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
                viewModel?.GetPDFf(pdfRequestModel, GetPDFRetrofitCallback)

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

            if (nextpageStatus == 0) {

                var Lab = LabTestActivity()

                (activity as HomeActivity).replaceFragmentNoBack(Lab)

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

                    activity!!.finish()

                    startActivity(Intent(context, LabTestActivity::class.java))

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

            binding?.pdfView!!.fromFile(destinationFile)
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
                }
                .load()
            Toast.makeText(
                context,
                "Storage path: $destinationFile", Toast.LENGTH_LONG
            ).show()


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
