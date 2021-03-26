package com.hmis_tn.doctor.ui.tutorial.view

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
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityUserManualBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.tutorial.model.*
import com.hmis_tn.doctor.ui.tutorial.viewmodel.UserManualViewModel
import com.hmis_tn.doctor.ui.tutorial.viewmodel.UserManualViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.*
import java.util.*

class UserManualActivity : Fragment(), TutorialCallback {
    var binding: ActivityUserManualBinding? = null
    private var viewModel: UserManualViewModel? = null
    var utils: Utils? = null
    var gridLayoutManager: GridLayoutManager? = null
    private var userProfileAdpater: UserManualTutorialAdapter? = null
    private var departmentAdapter: UserManualTutorialAdapter? = null
    private var institutionAdapter: UserManualTutorialAdapter? = null
    var appPreferences: AppPreferences? = null
    var roleControlList: List<RoleControlActivityResponseContent?>? = null
    private var facility_id: Int = 0
    private var clickListener: TutorialCallback? = null
    var downloadZipFileTask: DownloadZipFileTask? = null
    private var destinationFile: File? = null
    private var attacheddate: String? = null
    var downloadFilePath: String? = null
    var downloadFileName: String? = null

    var userManuvel: UserManualMainAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_user_manual,
                container,
                false
            )

        viewModel = UserManualViewModelFactory(
            requireActivity().application
        )
            .create(UserManualViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        clickListener = this

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!


        binding?.userManualRecyclerView?.layoutManager = LinearLayoutManager(context)

        userManuvel = UserManualMainAdapter(
            requireContext(), ArrayList(),
            clickListener as UserManualActivity
        )

        binding?.userManualRecyclerView?.adapter = userManuvel


        getTutoriaList()

        return binding!!.root
    }

    private fun getTutoriaList() {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("sortField", "name")
            jsonBody.put("sortOrder", "Asc")
            jsonBody.put("tutorial_type_uuid", 1)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.getUserManualList(jsonBody, tutorialListResponseRetrofitCallback)

    }


    val tutorialListResponseRetrofitCallback = object : RetrofitCallback<UserManualResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<UserManualResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            Log.e("tutorialList", "" + responsedata)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                Log.e("list size", "" + responseBody.body()?.responseContents?.size)

                userManuvel!!.addAll(responseBody.body()!!.responseContents)

                binding?.progressbar!!.visibility = View.GONE

            } else {
                Toast.makeText(context!!, "No records found", Toast.LENGTH_LONG).show()
                binding?.progressbar!!.visibility = View.GONE
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


/*
    val tutorialListResponseRetrofitCallback = object  :
        RetrofitCallback<UserManualResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<UserManualResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            Log.e("tutorialList", "" + responsedata)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                Log.e("list size", "" + responseBody!!.body()?.responseContents?.size);

                userProfileAdpater!!.addAll(responseBody!!.body()!!.responseContents?.get(0)!!.module_arr!!)
                if(responseBody!!.body()!!.responseContents?.get(1)!!.module_arr?.size!!>0){
                    departmentAdapter!!.addAll(responseBody!!.body()!!.responseContents?.get(1)!!.module_arr!!)
                    binding?.departmentLayout!!.setVisibility(View.VISIBLE)
                }else{
                    binding?.departmentLayout!!.setVisibility(View.GONE)
                }

                if(responseBody!!.body()!!.responseContents?.get(2)!!.module_arr?.size!!>0){
                    institutionAdapter!!.addAll(responseBody!!.body()!!.responseContents?.get(2)!!.module_arr!!)
                    binding?.institutionLayout!!.setVisibility(View.VISIBLE)
                }else{
                    binding?.institutionLayout!!.setVisibility(View.GONE)
                }

                binding?.progressbar!!.setVisibility(View.GONE);

            } else {
                Toast.makeText(context!!, "No records found", Toast.LENGTH_LONG).show()
                binding?.progressbar!!.setVisibility(View.GONE)
                binding?.departmentLayout!!.setVisibility(View.GONE)
                binding?.institutionLayout!!.setVisibility(View.GONE)
                userProfileAdpater!!.clearAll()
                departmentAdapter!!.clearAll()
                institutionAdapter!!.clearAll()
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
*/

    private fun getRoleControlActivityCode() {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("activityCode", "User Manual")
            jsonBody.put("roleId", "1790,1859")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.getRoleControlActivity(jsonBody, RoleControlActivityResponseRetrofitCallback)

    }

    val RoleControlActivityResponseRetrofitCallback = object :
        RetrofitCallback<RoleControlActivityResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<RoleControlActivityResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            Log.e("tutorialList", "" + responsedata)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                Log.e("list size", "" + responseBody.body()?.responseContents?.size)
                roleControlList = responseBody.body()?.responseContents!!

            } else {
                Toast.makeText(context!!, "No records found", Toast.LENGTH_LONG).show()
                binding?.progressbar!!.visibility = View.GONE
            }

        }

        override fun onBadRequest(errorBody: Response<RoleControlActivityResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: RoleControlActivityResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    RoleControlActivityResponseModel::class.java
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

    override fun OnDeleteClick(model: UserModuleResponseContent?) {
        Log.e("OnDeleteClick", "inside")
        deleteFile(model?.uuid!!)
    }

    override fun OnDownloadClick(model: UserModuleResponseContent?) {
        Log.e("OnDownloadClick", "inside")
        downloadFilePath = model?.tutorial_url!!
        downloadFileName = getFileName(model.tutorial_url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            runTimePermissionWriteExternalStorage(downloadFilePath!!)

        } else {
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
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        } else {
            viewModel?.getDownload(filepath, downloadimagecallback)

            return
        }
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

    val deleteAPIResponseRetrofitCallback = object :
        RetrofitCallback<UserManualDeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<UserManualDeleteResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()!!)
            Log.e("tutorialList", "" + responsedata)
            Toast.makeText(context!!, responseBody.body()?.msg, Toast.LENGTH_LONG).show()
            binding?.progressbar!!.visibility = View.VISIBLE
            binding?.departmentLayout!!.visibility = View.GONE
            binding?.institutionLayout!!.visibility = View.GONE
            userProfileAdpater!!.clearAll()
            departmentAdapter!!.clearAll()
            institutionAdapter!!.clearAll()

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
                    binding?.mainLayout!!,
                    responseModel.msg!!
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

    val downloadimagecallback =
        object : RetrofitCallback<ResponseBody> {
            override fun onSuccessfulResponse(response: Response<ResponseBody>) {
                Log.i("", "message")

                downloadZipFileTask = DownloadZipFileTask()
                downloadZipFileTask!!.execute(response.body())

            }

            override fun onBadRequest(response: Response<ResponseBody>) {
                val gson = GsonBuilder().create()
                try {
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

    inner class DownloadZipFileTask :
        AsyncTask<ResponseBody?, Pair<Int?, Long?>?, String?>() {

        fun doProgress(progressDetails: Pair<Int?, Long?>?) {
            publishProgress(progressDetails)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(result: String?) {
            // binding?.progressbar!!.setVisibility(View.GONE);


/*

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(destinationFile.toString()), "application/pdf")
            startActivity(intent)
*/

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri: Uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName.toString() + ".provider",
                destinationFile!!
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/pdf")
            startActivity(intent)

            Toast.makeText(
                requireContext(),
                "Storage path: $destinationFile", Toast.LENGTH_LONG
            ).show()


            //  show_Notification()
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
}