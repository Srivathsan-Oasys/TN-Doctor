package com.hmis_tn.doctor.ui.emr_workflow.certificate.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.irshulx.models.EditorTextStyle
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.callbacks.FragmentBackClick
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentCertificateChildBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.certificate.model.*
import com.hmis_tn.doctor.ui.emr_workflow.certificate.view_model.CertificateViewModel
import com.hmis_tn.doctor.ui.emr_workflow.certificate.view_model.CertificateViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.model.EMR_Request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.model.EMR_Request.Header
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CertificateChildFragment : Fragment() {
    lateinit var drugNmae: TextView
    private var facility_id: Int? = 0

    private var binding: FragmentCertificateChildBinding? = null
    private var viewModel: CertificateViewModel? = null
    private var utils: Utils? = null
    private var responseContents: String? = ""
    private var responsedata: Int? = 0

    private var favmodel: FavouritesModel? = null
    private var listTemplatesItems: ArrayList<TemplatesResponseContent?> = ArrayList()
    private var templateResponseMap = mutableMapOf<Int, String>()


    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    private var fragmentBackClick: FragmentBackClick? = null
    var appPreferences: AppPreferences? = null
    val detailsList = ArrayList<Detail>()
    val header: Header? = Header()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var prevCertificateDialogAdapter: PrevCertificateDialogAdapter? = null
    var bitmap: Bitmap? = null
    var downloadFileFormat: String = ".pdf"


    private var doctor_en_uuid: Int = 0
    private var encounter_uuid: Int = 0

    companion object {
        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.draw(c)

            return b
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_certificate_child,
                container,
                false
            )
        viewModel = CertificateViewModelFactory(
            requireActivity().application
        )
            .create(CertificateViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        trackCertificateVisit(utils!!.getEncounterType())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        appPreferences?.saveInt(AppConstants.LAB_MASTER_TYPE_ID, 2)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)


        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        viewModel?.getNoteTemplate(facility_id!!, noteTemplateRetrofitCallback)


        binding?.saveCardView?.setOnClickListener {
            if (isValidData()) {

                //saveNext()
                val patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
                val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
                viewModel?.getEncounter(
                    facility_id!!,
                    patient_UUID!!,
                    encounter_Type!!,
                    fetchEncounterRetrofitCallBack
                )

                trackCertificateSaveStart(utils!!.getEncounterType())
            } else {
                alertDialog("Please enter required fields")
            }
        }

        binding?.PrevCertificate?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog = PrevCertificateDialog()
            dialog.show(ft, "Tag")

        }

        binding?.clearCardView?.setOnClickListener {
            binding?.opNotesSpinner?.setSelection(0)
            binding?.TemplateData?.clearAllContents()

        }
        binding!!.TemplateData.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (binding!!.TemplateData.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return true
                        }
                    }
                }
                return false
            }
        })

        templateListeners(binding?.root)


        return binding!!.root
    }

    private fun templateListeners(view: View?) {
        val editor = binding?.TemplateData!!

        view?.findViewById<View>(R.id.action_h1)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.H1)
        }

        view?.findViewById<View>(R.id.action_h2)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.H2)
        }

        view?.findViewById<View>(R.id.action_h3)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.H3)
        }

        view?.findViewById<View>(R.id.action_bold)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.BOLD)
        }

        view?.findViewById<View>(R.id.action_Italic)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.ITALIC)
        }

        view?.findViewById<View>(R.id.action_indent)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.INDENT)
        }

        view?.findViewById<View>(R.id.action_outdent)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.OUTDENT)
        }

        view?.findViewById<View>(R.id.action_bulleted)?.setOnClickListener {
            editor.insertList(false)
        }

        view?.findViewById<View>(R.id.action_color)?.setOnClickListener {
            editor.updateTextColor("#FF3333")

//            ColorPickerPopup.Builder(requireActivity())
//                .initialColor(Color.RED) // Set initial color
//                .enableAlpha(true) // Enable alpha slider or not
//                .okTitle("Choose")
//                .cancelTitle("Cancel")
//                .showIndicator(true)
//                .showValue(true)
//                .build()
//                .show(activity?.findViewById(android.R.id.content), object :
//                    ColorPickerPopup.ColorPickerObserver {
//                    override fun onColorPicked(color: Int) {
//                        Toast.makeText(
//                            requireContext(),
//                            "picked" + colorHex(color),
//                            Toast.LENGTH_LONG
//                        ).show()
//                        editor.updateTextColor(colorHex(color))
//                    }
//
//                    override fun onColor(color: Int, fromUser: Boolean) {}
//                })
        }

        view?.findViewById<View>(R.id.action_unordered_numbered)?.setOnClickListener {
            editor.insertList(true)
        }

        view?.findViewById<View>(R.id.action_hr)?.setOnClickListener {
            editor.insertDivider()
        }

        view?.findViewById<View>(R.id.action_insert_image)?.setOnClickListener {
            editor.openImagePicker()
        }

        view?.findViewById<View>(R.id.action_insert_link)?.setOnClickListener {
            editor.insertLink()
        }

        view?.findViewById<View>(R.id.action_erase)?.setOnClickListener {
            editor.clearAllContents()
        }

        view?.findViewById<View>(R.id.action_blockquote)?.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE)
        }

        editor.render()
    }

    private fun saveNext() {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        //val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        //val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val dateInString = sdf.format(Date())

        val data_template = binding?.TemplateData?.contentAsHTML
        val certificateRequestModel: CertificateRequestModel = CertificateRequestModel()
        certificateRequestModel.patient_uuid = patient_UUID!!.toString()
        certificateRequestModel.encounter_uuid = encounter_uuid
        certificateRequestModel.ward_master_uuid = 1
        certificateRequestModel.doctor_uuid = userDataStoreBean?.uuid!!.toString()
        certificateRequestModel.facility_uuid = facility_id.toString()
        certificateRequestModel.department_uuid = department_UUID.toString()
        certificateRequestModel.admission_status_uuid = 1
        certificateRequestModel.discharge_type_uuid = 1
        if (data_template != null) {
            certificateRequestModel.data_template = data_template
        }
        certificateRequestModel.note_template_uuid = 1
        certificateRequestModel.note_template_uuid = responsedata!!
        certificateRequestModel.note_type_uuid = 1
        certificateRequestModel.certified_by = userDataStoreBean.uuid.toString()
        certificateRequestModel.certificate_status_uuid = 1
        certificateRequestModel.released_to_patient = 1
        certificateRequestModel.released_by = userDataStoreBean.uuid.toString()
        certificateRequestModel.aproved_by = userDataStoreBean.uuid.toString()
        certificateRequestModel.approved_on = dateInString
        viewModel!!.getSaveNext(certificateRequestModel, NextSAveRetrofitCallback)
    }

    private fun colorHex(color: Int): String {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b)
    }

    private fun isValidData(): Boolean {
        var isValid = true
        binding?.also {
            if (it.opNotesSpinner.selectedItemPosition == 0)
                isValid = false
        }

        return isValid
    }

    val noteTemplateRetrofitCallback = object : RetrofitCallback<TemplatesResponseModel> {
        override fun onSuccessfulResponse(response: Response<TemplatesResponseModel>) {
            if (!response.body()?.responseContents.isNullOrEmpty()) {
                response.body()?.responseContents?.let {
                    val facility =
                        TemplatesResponseContent(nt_uuid = 0, nt_name = "Select template")
                    listTemplatesItems.add(0, facility)
                    it.forEach {
                        listTemplatesItems.add(it)
                    }

                    templateResponseMap =
                        listTemplatesItems.map { it?.nt_uuid!! to it.nt_name }.toMap()
                            .toMutableMap()
                    val adapter =
                        ArrayAdapter<String>(
                            activity!!,
                            R.layout.spinner_item,
                            templateResponseMap.values.toMutableList()
                        )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.opNotesSpinner!!.adapter = adapter
                    adapterCall(response.body()?.responseContents!!)
                }
            }
        }

        override fun onBadRequest(response: Response<TemplatesResponseModel>) {


            val gson = GsonBuilder().create()
            val responseModel: TemplatesResponseModel
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


    private fun alertDialog(msg: String) {
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        //builder.setTitle(getString(R.string.app_name))
        builder.setMessage(msg)
        builder.setPositiveButton("OK") { d, _ -> d?.dismiss() }
        val alert: AlertDialog = builder.create()
        alert.setOnShowListener {
            val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
            btnPositive.textSize = 16F
            btnPositive.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        }
        alert.show()
    }

    private fun adapterCall(responseContents: List<TemplatesResponseContent>) {

        val adapter = ArrayAdapter<String>(
            this.requireActivity(),
            android.R.layout.simple_spinner_item,
            templateResponseMap.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.opNotesSpinner!!.adapter = adapter

        binding?.opNotesSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()

                    //val selectedPoi = parent!!.adapter.getItem(0) as OpNotesResponseContent?

                    responsedata =
                        templateResponseMap.filterValues { it == itemValue }.keys.toList()[0]

                    viewModel?.getItemTemplate(
                        facility_id,
                        notesTemplatesRetrofitCallback,
                        responsedata!!
                    )

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()


                    responsedata =
                        templateResponseMap.filterValues { it == itemValue }.keys.toList()[0]


                    viewModel?.getItemTemplate(
                        facility_id,
                        notesTemplatesRetrofitCallback,
                        responsedata!!
                    )

                }
            }
    }

    val notesTemplatesRetrofitCallback =
        object : RetrofitCallback<TemplateItemResponseModel> {
            override fun onSuccessfulResponse(response: Response<TemplateItemResponseModel>) {

                if (response.body()?.responseContents != null) {
                    binding?.TemplateData?.clearAllContents()
                    binding?.TemplateData?.render(response.body()?.responseContents?.data_template)
                    responseContents = binding?.TemplateData?.contentAsHTML
                } else {
                    binding?.TemplateData?.render("Comment Here").toString()
                    responseContents = binding?.TemplateData?.contentAsHTML
                }

            }

            override fun onBadRequest(response: Response<TemplateItemResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TemplateItemResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TemplateItemResponseModel::class.java
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

    var NextSAveRetrofitCallback = object :
        RetrofitCallback<CertificateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CertificateResponseModel>?) {

            trackCertificateSaveComplete(utils?.getEncounterType(), "success", "")
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Certificate Created & Saved Successfully"
            )

            binding?.opNotesSpinner!!.setSelection(0)
            binding?.TemplateData?.clearAllContents()
        }

        override fun onBadRequest(response: Response<CertificateResponseModel>) {

            trackCertificateSaveComplete(utils!!.getEncounterType(), "failure", response.message())

        }

        override fun onServerError(response: Response<*>) {
            trackCertificateSaveComplete(
                utils!!.getEncounterType(),
                "failure",
                getString(R.string.something_went_wrong)
            )
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "serverError"
            )

        }

        override fun onUnAuthorized() {
            trackCertificateSaveComplete(
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
            trackCertificateSaveComplete(
                utils!!.getEncounterType(),
                "failure",
                getString(R.string.something_went_wrong)
            )
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "Forbidden"
            )

        }

        override fun onFailure(failure: String) {
            trackCertificateSaveComplete(utils!!.getEncounterType(), "failure", failure)
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                failure
            )
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    val encounterResponseContent = response.body()?.responseContents!!
                    doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_uuid = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)
                    saveNext()

                } else {
                    val patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
                    val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)

                    patient_UUID?.let {
                        if (encounter_Type != null) {
                            viewModel?.createEncounter(
                                it,
                                encounter_Type,
                                createEncounterRetrofitCallback
                            )
                        }
                    }
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

    private fun scanForActivity(cont: Context?): Activity? {
        if (cont == null)
            return null
        else if (cont is Activity)
            return cont
        else if (cont is ContextWrapper)
            return scanForActivity(
                cont.baseContext
            )
        return null
    }


    fun trackCertificateVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackCertificateVisit(type)
    }


    fun trackCertificateSaveStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackCertificateSaveStart(type)
    }


    fun trackCertificateSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackCertificateSaveComplete(type, status, message)
    }

    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {
            doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            saveNext()

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
}
