package com.oasys.digihealth.doctor.ui.tutorial.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityVideoTutorialBinding
import com.oasys.digihealth.doctor.ui.tutorial.model.RoleControlActivityResponseContent
import com.oasys.digihealth.doctor.ui.tutorial.model.UserManualDeleteResponseModel
import com.oasys.digihealth.doctor.ui.tutorial.model.UserManualResponseModel
import com.oasys.digihealth.doctor.ui.tutorial.model.UserModuleResponseContent
import com.oasys.digihealth.doctor.ui.tutorial.viewmodel.VideoTutorialViewModel
import com.oasys.digihealth.doctor.ui.tutorial.viewmodel.VideoTutorialViewModelFactory
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.*
import java.util.*

class VideoTutorialActivity : Fragment(), TutorialCallback {
    var binding: ActivityVideoTutorialBinding? = null
    private var viewModel: VideoTutorialViewModel? = null
    var utils: Utils? = null
    var gridLayoutManager: GridLayoutManager? = null
    private var userProfileAdpater: VideoTutorialAdapter? = null
    private var roleAdapter: VideoTutorialAdapter? = null
    private var bloodIssueAdapter: VideoTutorialAdapter? = null
    var appPreferences: AppPreferences? = null
    var roleControlList: List<RoleControlActivityResponseContent?>? = null
    private var facility_id: Int = 0
    private var clickListener: TutorialCallback? = null
    var downloadFilePath: String? = null
    var uuid: Int? = 0
    var downloadFileName: String? = null
    private var destinationFile: File? = null
    var downloadZipFileTask: DownloadZipFileTask? = null
    var userManuvel: VideoManualMainAdapter? = null

    private var customProgressDialog: CustomProgressDialog? = null

    companion object {
        const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 102
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_video_tutorial,
                container,
                false
            )

        viewModel = VideoTutorialViewModelFactory(
            requireActivity().application
        )
            .create(VideoTutorialViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        utils = Utils(requireContext())

        clickListener = this

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!

        customProgressDialog = CustomProgressDialog(requireContext())

        binding.userManualRecyclerView.layoutManager = LinearLayoutManager(context)

        userManuvel = VideoManualMainAdapter(
            requireContext(), ArrayList(),
            clickListener as VideoTutorialActivity, viewModel!!
        )

        binding.userManualRecyclerView.adapter = userManuvel


        getTutoriaList()

        return binding!!.root
    }

    private fun getTutoriaList() {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("sortField", "name")
            jsonBody.put("sortOrder", "Asc")
            jsonBody.put("tutorial_type_uuid", 2)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.getUserManualList(jsonBody, tutorialListResponseRetrofitCallback)

    }

    val tutorialListResponseRetrofitCallback = object {
        override fun onSuccessfulResponse(responseBody: Response<UserManualResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            Log.e("tutorialList", "" + responsedata)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                Log.e("list size", "" + responseBody.body()?.responseContents?.size)

                userManuvel!!.addAll(responseBody.body()!!.responseContents)

                binding.progressbar!!.setVisibility(View.GONE)

            } else {
                Toast.makeText(context!!, "No records found", Toast.LENGTH_LONG).show()

                binding.progressbar!!.setVisibility(View.GONE)

            }

        }

        override fun onBadRequest(errorBody: Response<UserManualResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: UserManualResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    UserManualResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    override fun OnDeleteClick(model: UserModuleResponseContent?) {
        Log.e("OnDeleteClick", "inside")
        deleteFile(model?.uuid!!)
    }

    override fun OnDownloadClick(model: UserModuleResponseContent?) {
        Log.e("OnDownloadClick", "inside")
        uuid = model?.uuid
        downloadFilePath = model?.tutorial_url!!
        downloadFileName = getFileName(model.tutorial_url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            runTimePermissionWriteExternalStorage(downloadFilePath!!)

        } else {

            showLoading(true)
            viewModel?.getDownload(downloadFilePath!!, downloadimagecallback)
        }
    }

    private fun getFileName(wholePath: String): String {
        var name: String? = null
        val start: Int
        val end: Int
        start = wholePath.lastIndexOf('/')
        end = wholePath.length
        name = wholePath.substring((start + 1), end)
        return name
    }


    private fun runTimePermissionWriteExternalStorage(filepath: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                UserManualActivity.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        } else {

            showLoading(true)
            viewModel?.getDownload(filepath, downloadimagecallback)

            return
        }
    }

    val downloadimagecallback =
        object {
            override fun onSuccessfulResponse(response: Response<ResponseBody>) {

                Log.i("", "message")

                showLoading(false)

                val f = File(Environment.getExternalStorageDirectory().toString() + "/Tutorial")
                if (f.isDirectory) {

                    downloadZipFileTask = DownloadZipFileTask()
                    downloadZipFileTask!!.execute(response.body())


                } else {

                    f.mkdirs()

                    downloadZipFileTask = DownloadZipFileTask()
                    downloadZipFileTask!!.execute(response.body())

                }

            }

            override fun onBadRequest(response: Response<ResponseBody>) {
                val gson = GsonBuilder().create()
                try {

                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout!!,
                        response.message()
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
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

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(result: String?) {
            // binding?.progressbar!!.setVisibility(View.GONE);
            Toast.makeText(
                requireContext(),
                "Storage path: $destinationFile", Toast.LENGTH_LONG
            ).show()
            uploadDownloadCount(uuid!!)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(destinationFile.toString()), "video/mp4")
            startActivity(intent)

        }

        override fun doInBackground(vararg params: ResponseBody?): String? {

            saveToDisk(params[0]!!, downloadFileName!!)

            return null
        }
    }

    private fun saveToDisk(body: ResponseBody, filename: String) {
        try {
            val foldername = "Tutorial"

            destinationFile = File(
                Environment.getExternalStorageDirectory().toString() + "/" + foldername,
                filename
            )
            if (destinationFile!!.exists()) {
                destinationFile!!.mkdirs()
            }

            /*    destinationFile = File(
               Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename)*/
            val inputstream: InputStream? = body.byteStream()
            val os: OutputStream = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            //read from is to buffer
            while (inputstream!!.read(buffer).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            inputstream.close()
            os.flush()
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    private fun uploadDownloadCount(uuid: Int) {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("uuid", uuid)
            jsonBody.put("viewflag", false)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.uploadDownloadCount(jsonBody, updateDownloadCountResponseRetrofitCallback)

    }


    private fun deleteFile(uuid: Int) {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("uuid", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.deleteFile(jsonBody, deleteAPIResponseRetrofitCallback)

    }

    val deleteAPIResponseRetrofitCallback = object {
        override fun onSuccessfulResponse(responseBody: Response<UserManualDeleteResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()!!)
            Log.e("tutorialList", "" + responsedata)
            Toast.makeText(context!!, responseBody.body()?.msg, Toast.LENGTH_LONG).show()
            binding.progressbar!!.setVisibility(View.VISIBLE)
            binding.roleLayout!!.setVisibility(View.GONE)
            binding.bloodIssueLayout!!.setVisibility(View.GONE)
            userProfileAdpater!!.clearAll()
            bloodIssueAdapter!!.clearAll()
            roleAdapter!!.clearAll()
            getTutoriaList()
        }

        override fun onBadRequest(errorBody: Response<UserManualDeleteResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: UserManualDeleteResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    UserManualDeleteResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    responseModel.msg!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    val updateDownloadCountResponseRetrofitCallback = object {
        override fun onSuccessfulResponse(responseBody: Response<UserManualDeleteResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()!!)
            Log.e("Download response", "" + responsedata)
        }

        override fun onBadRequest(errorBody: Response<UserManualDeleteResponseModel>?) {
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


    private fun showLoading(isLoading: Boolean) {
//        binding?.pbDischargeSummaryLL?.isVisible = isLoading
        if (isLoading) {
            customProgressDialog!!.show()
        } else {
            customProgressDialog!!.dismiss()

        }
    }


}