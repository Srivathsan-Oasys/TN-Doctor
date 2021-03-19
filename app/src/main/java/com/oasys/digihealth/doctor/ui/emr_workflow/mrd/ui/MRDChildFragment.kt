package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.MrdChildFragmentBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.DischargeResult
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.CaseSheetResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models.MRDResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.view_model.MRDViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.view_model.MRDViewModelFactory
import com.oasys.digihealth.doctor.ui.landingscreen.MainLandScreenActivity
import com.oasys.digihealth.doctor.utils.Utils
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MRDChildFragment : Fragment() {
    private var customdialog: Dialog? = null
    lateinit var drugNmae: TextView
    private var jsonObj_: JSONObject? = null
    private var gsonObject: JsonObject? = null
    private var facility_id: Int = 0
    private var Str_auto_id: Int? = 0
    var binding: MrdChildFragmentBinding? = null
    private var departmentID: Int? = 0
    private var searchPosition: Int? = 0
    private var utils: Utils? = null
    private var responseContents: String? = ""
    var searchposition: Int = 0
    private var viewModel: MRDViewModel? = null
    private var allergyAdapter: MrdAllergyAdapter? = null
    private var prescriptionAdapter: MrdPrescriptionAdapter? = null
    private var radiologyAdapter: MrdRadilogyAdapter? = null
    private var labAdapter: MrdLabAdapter? = null
    private var vitalsAdapter: MrdVitalsAdapter? = null
    private var diagnosisAdapter: MrdDiagnosisAdapter? = null
    private var mrdInvestigationAdapter: MrdInvestigationAdapter? = null
    private var mrdChiefComplaintsAdapter: MrdChiefComplaintsAdapter? = null
    private var patientID: Int? = null
    private var encounter_uuid: Int? = null
    private var encounter_type: Int? = null
    private var data: CaseSheetResponseContent? = null


    var listData: List<DischargeResult?>? = listOf()
    var downloadFileFormat: String = ".pdf"
    var bitmap: Bitmap? = null

    companion object {
        const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 102

        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.draw(c)

            return b
        }
    }


    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var selectedSearchPosition = -1
    private var appPreferences: AppPreferences? = null
    private var deparment_UUID: Int? = 0
    var gson: Gson? = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mrd_child_fragment,
            container,
            false
        )

        viewModel = MRDViewModelFactory(
            requireActivity().application
        ).create(MRDViewModel::class.java)

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        var userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        encounter_type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)


        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        utils = Utils(requireContext())
        if (arguments != null) {
            data = requireArguments().getParcelable("Details")
        }
        allergyAdapter()
        prescriptionAdapter()
        RadiologyAdapter()
        LabAdapter()
        vitalsAdapter()
        diagnosissAdapter()
        investigationAdapter()
        chiefComplaintAdapter()
        trackMrdVisit(utils?.getEncounterType())

        viewModel?.getMrd_Records(
            data?.created_date,
            patientID,
            facility_id,
            data?.encounter_uuid,
            encounter_type,
            prevMrdrecordsRetrofitCallback
        )



        binding?.cvAllergyTitle?.setOnClickListener {

            if (binding?.cvAllergyView?.visibility == View.VISIBLE) {
                binding?.cvAllergyView?.visibility = View.GONE

            } else {
                binding?.cvAllergyView?.visibility = View.VISIBLE


            }
        }
        binding?.cvDiagnosisTitle?.setOnClickListener {

            if (binding?.cvDiagnosisTitleView?.visibility == View.VISIBLE) {
                binding?.cvDiagnosisTitleView?.visibility = View.GONE

            } else {
                binding?.cvDiagnosisTitleView?.visibility = View.VISIBLE


            }
        }



        binding?.cvPrescriptionTitle?.setOnClickListener {

            if (binding?.cvPrescriptionView?.visibility == View.VISIBLE) {
                binding?.cvPrescriptionView?.visibility = View.GONE

            } else {
                binding?.cvPrescriptionView?.visibility = View.VISIBLE


            }
        }


        binding?.cvRadiologyTitle?.setOnClickListener {

            if (binding?.cvRadiologyView?.visibility == View.VISIBLE) {
                binding?.cvRadiologyView?.visibility = View.GONE

            } else {
                binding?.cvRadiologyView?.visibility = View.VISIBLE


            }
        }

        binding?.cvLabTitle?.setOnClickListener {

            if (binding?.cvLabView?.visibility == View.VISIBLE) {
                binding?.cvLabView?.visibility = View.GONE

            } else {
                binding?.cvLabView?.visibility = View.VISIBLE


            }
        }

        binding?.cvVitalsTitle?.setOnClickListener {

            if (binding?.cvVitalsView?.visibility == View.VISIBLE) {
                binding?.cvVitalsView?.visibility = View.GONE

            } else {
                binding?.cvVitalsView?.visibility = View.VISIBLE


            }
        }
        binding?.cvChiefComplientsTitle?.setOnClickListener {

            if (binding?.cvChiefComplientsView?.visibility == View.VISIBLE) {
                binding?.cvChiefComplientsView?.visibility = View.GONE

            } else {
                binding?.cvChiefComplientsView?.visibility = View.VISIBLE


            }
        }
        binding?.cvInvestigationTitle?.setOnClickListener {

            if (binding?.cvInvestigationView?.visibility == View.VISIBLE) {
                binding?.cvInvestigationView?.visibility = View.GONE

            } else {
                binding?.cvInvestigationView?.visibility = View.VISIBLE


            }
        }




        binding?.Print!!.setOnClickListener {
            pdfDownload()
//            trackMrdPrint(utils?.getEncounterType())
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                runTimePermissionWriteExternalStorage()
//
//            }else{
//                // selectFileFormat();
//                createBitmap()
//            }
        }


        return binding!!.root
    }

    private fun pdfDownload() {

        val mrdtemplatedialog = DialogMrdPDFViewerActivity()

        (activity as MainLandScreenActivity).replaceFragment(mrdtemplatedialog)
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
                MRDChildFragment.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        } else {
            // selectFileFormat()
            createBitmap()
            return
        }
    }

    private fun createBitmap() {
        var viewLayout = binding?.nestedScrollView
        viewLayout!!.isDrawingCacheEnabled = true
        viewLayout.buildDrawingCache()
        var height: Int = 2000
        if (listData!!.size > 15) {
            height = viewLayout.getChildAt(0).height
        }

        bitmap = MRDChildFragment.loadBitmapFromView(
            viewLayout,
            viewLayout.getChildAt(0).width,
            height
        )

        createPdf()
    }

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
            document.writeTo(FileOutputStream(createPDFPath()))
            if (downloadFileFormat.equals(".pdf")) {
                Toast.makeText(
                    requireContext(),
                    "PDF downloaded successfully\n" + createPDFPath(),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Excel downloaded successfully\n" + createPDFPath(),
                    Toast.LENGTH_LONG
                ).show()
            }

        } catch (e: IOException) {
            Log.e("IOException", e.toString())
        }
        document.close()
    }

    fun createPDFPath(): String? {
        Log.e("createPDFPath", "inside")
        val foldername = "Reports"
        val folder =
            File(Environment.getExternalStorageDirectory().toString() + "/" + foldername)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val simpleDateFormat =
            SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
        val date =
            simpleDateFormat.format(Calendar.getInstance().time)
        return folder.toString() + File.separator + "MrdList" + date + downloadFileFormat
    }


    private fun allergyAdapter() {
        allergyAdapter = MrdAllergyAdapter((requireActivity()))
        binding?.mrdRecyclerView!!.adapter = allergyAdapter


    }

    private fun prescriptionAdapter() {
        prescriptionAdapter = MrdPrescriptionAdapter((requireActivity()))
        binding?.PrescriptionRecyclerView!!.adapter = prescriptionAdapter


    }

    private fun RadiologyAdapter() {
        radiologyAdapter = MrdRadilogyAdapter((requireActivity()))
        binding?.radiologyRecyclerView!!.adapter = radiologyAdapter


    }

    private fun LabAdapter() {
        labAdapter = MrdLabAdapter((requireActivity()))
        binding?.labRecyclerView!!.adapter = labAdapter


    }

    private fun vitalsAdapter() {
        vitalsAdapter = MrdVitalsAdapter((requireActivity()))
        binding?.vitalsRecyclerView!!.adapter = vitalsAdapter


    }

    private fun diagnosissAdapter() {
        diagnosisAdapter = MrdDiagnosisAdapter((requireActivity()))
        binding?.diagnosisRecyclerView!!.adapter = diagnosisAdapter


    }

    private fun investigationAdapter() {
        mrdInvestigationAdapter = MrdInvestigationAdapter((requireActivity()))
        binding?.investigationRecyclerView!!.adapter = mrdInvestigationAdapter


    }

    private fun chiefComplaintAdapter() {
        mrdChiefComplaintsAdapter = MrdChiefComplaintsAdapter((requireActivity()))
        binding?.chiefComplaintRecyclerView!!.adapter = mrdChiefComplaintsAdapter


    }


    val prevMrdrecordsRetrofitCallback = object : RetrofitCallback<MRDResponseModel> {
        override fun onSuccessfulResponse(response: Response<MRDResponseModel>) {
            val data = response.body()?.discharge_result
            if (data?.allergy?.allergy_details?.isEmpty()!!) {
                binding?.cvAllergy?.visibility = View.GONE
            } else {
                binding?.cvAllergy?.visibility = View.VISIBLE
                allergyAdapter?.refreshList(response.body()?.discharge_result?.allergy?.allergy_details)
            }


            if (data.prescription.prescription_details.isEmpty()) {
                binding?.cvPrescription?.visibility = View.GONE
            } else {
                binding?.cvPrescription?.visibility = View.VISIBLE
                prescriptionAdapter?.refreshList(response.body()?.discharge_result?.prescription?.prescription_details)

            }

            if (data.radiology.radiology_details.isEmpty()) {
                binding?.cvRadiology?.visibility = View.GONE

            } else {
                binding?.cvRadiology?.visibility = View.VISIBLE


                radiologyAdapter?.refreshList(response.body()?.discharge_result?.radiology?.radiology_details!!)

            }

            if (data.lab.lab_details.isEmpty()) {
                binding?.cvLab?.visibility = View.GONE

            } else {
                binding?.cvLab?.visibility = View.VISIBLE

                labAdapter?.refreshList(response.body()?.discharge_result?.lab?.lab_details!!)

            }

            if (data.diagnosis.diagnosis_details.isEmpty()) {
                binding?.cvDiagnosis?.visibility = View.GONE

            } else {
                binding?.cvDiagnosis?.visibility = View.VISIBLE

                diagnosisAdapter?.refreshList(response.body()?.discharge_result?.diagnosis?.diagnosis_details!!)

            }

            if (data.vitals.vital_details.isEmpty()) {
                binding?.cvVitals?.visibility = View.GONE

            } else {
                binding?.cvVitals?.visibility = View.VISIBLE

                vitalsAdapter?.refreshList(response.body()?.discharge_result?.vitals?.vital_details!!)

            }

            if (data.investigation.investigation_details.isEmpty()) {
                binding?.cvInvestigation?.visibility = View.GONE

            } else {
                binding?.cvInvestigation?.visibility = View.VISIBLE

                mrdInvestigationAdapter?.refreshList(response.body()?.discharge_result?.investigation?.investigation_details!!)

            }
            if (data.cheif_complaints.cheif_complaints_details.isEmpty()) {
                binding?.cvChiefComplients?.visibility = View.GONE

            } else {
                binding?.cvChiefComplients?.visibility = View.VISIBLE
                mrdChiefComplaintsAdapter?.refreshList(response.body()?.discharge_result?.cheif_complaints?.cheif_complaints_details!!)

            }

        }

        override fun onBadRequest(response: Response<MRDResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: MRDResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    MRDResponseModel::class.java
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


    private fun trackMrdVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackMrdVisit(type)
    }

    private fun trackMrdPrint(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackMrdPrint(type)
    }


}