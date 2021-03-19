package com.oasys.digihealth.doctor.ui.helpdesk.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityTicketsBinding
import com.oasys.digihealth.doctor.ui.helpdesk.model.*
import com.oasys.digihealth.doctor.ui.helpdesk.view.*
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.TicketsViewModel
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.TicketsViewModelFactory
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AgentTicketListFragment : Fragment(), HelpDeskCallback {
    var binding: ActivityTicketsBinding? = null
    private var viewModel: TicketsViewModel? = null
    var utils: Utils? = null
    var gridLayoutManager: GridLayoutManager? = null
    private var ticketListAdpater: TicketsListAdapter? = null
    private var countListAdapter: CountListAdapter? = null
    var appPreferences: AppPreferences? = null
    private var facility_id: Int = 0
    private var callBack: HelpDeskCallback? = null
    private var pageNo: Int = 0
    private var paginationSize: Int = 10
    private var loginType: String? = null
    private var userUUID: String? = null
    private var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var search: String? = ""
    var listData: List<TicketListResponseContent?>? = listOf()
    var detailList: TicketListResponseContent? = null
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_tickets,
                container,
                false
            )


        viewModel = TicketsViewModelFactory(
            requireActivity().application
        )
            .create(TicketsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        utils = Utils(requireContext())

        val searchText =
            binding.searchView.findViewById(R.id.search_src_text) as TextView
        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        searchText.typeface = tf

        binding.searchDrawerCardView.setOnClickListener {
            binding.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding.drawerLayout.drawerElevation = 0f
        binding.drawerLayout.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        loginType = appPreferences?.getString(AppConstants.LOGINTYPE)!!
        userUUID = userDataStoreBean.uuid.toString()!!

        callBack = this

        gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.ticketsRecyclerView!!.layoutManager = gridLayoutManager
        ticketListAdpater = TicketsListAdapter(requireContext(), ArrayList(), callBack!!)
        binding.ticketsRecyclerView!!.adapter = ticketListAdpater

        gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.countRecyclerView!!.layoutManager = gridLayoutManager
        countListAdapter = CountListAdapter(requireContext(), ArrayList())
        binding.countRecyclerView!!.adapter = countListAdapter

        binding.clear!!.setOnClickListener {

        }
        binding.countLayout.setOnClickListener {
            if (binding.imgCountListArrow.tag.equals("close")!!) {
                binding.countRecyclerView!!.visibility = View.VISIBLE
                binding.imgCountListArrow.tag = "open"
                binding.imgCountListArrow.setImageResource(R.drawable.ic_up_arrow_black)
            } else {
                binding.countRecyclerView!!.visibility = View.GONE
                binding.imgCountListArrow.tag = "close"
                binding.imgCountListArrow.setImageResource(R.drawable.ic_down_arrow_black)
            }
        }

        binding.addNew!!.setOnClickListener {
            val op = AgentNewTicketFragment()
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.landfragment, op)
            fragmentTransaction?.commit()
        }

        binding.search!!.setOnClickListener {
            binding.drawerLayout!!.closeDrawer(GravityCompat.END)
            ticketListAdpater!!.clearAll()
            countListAdapter!!.clearAll()
            getTicketList()
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e("onQueryTextSubmit", query)
                callSearch(query)
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                Log.e("callSearch", "" + query)
                search = query

            }

        })

        binding.download!!.setOnClickListener {
            Log.e("download button", "inside")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermissionWriteExternalStorage()

            } else {
                //selectFileFormat()
                createBitmap()
            }
        }

        return binding!!.root
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
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        } else {
            //selectFileFormat()
            createBitmap()
            return
        }
    }

    override fun onResume() {
        super.onResume()
        getTicketList()
    }

    private fun selectFileFormat() {

        var customdialog = Dialog(requireContext())
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customdialog.setCancelable(false)
        customdialog.setContentView(R.layout.download_custom_dialog)
        val pdfLayout = customdialog.findViewById(R.id.pdf_layout) as TextView
        val excelLayout = customdialog.findViewById(R.id.excel_layout) as TextView
        val csvLayout = customdialog.findViewById(R.id.csv_layout) as TextView

        pdfLayout.setOnClickListener {
            customdialog.dismiss()
            downloadFileFormat = ".pdf"
            createBitmap()
        }

        excelLayout.setOnClickListener {
            customdialog.dismiss()
            downloadFileFormat = ".xlsx"
            createBitmap()
        }

        csvLayout.setOnClickListener {
            customdialog.dismiss()
            downloadFileFormat = ".csv"
            createBitmap()
        }

        customdialog.show()
    }

    private fun createBitmap() {
        var viewLayout = binding.nestedScrollView
        viewLayout!!.isDrawingCacheEnabled = true
        viewLayout.buildDrawingCache()
        var height: Int = 2000
        if (listData!!.size > 15) {
            height = viewLayout.getChildAt(0).height
        }

        bitmap = loadBitmapFromView(
            viewLayout,
            viewLayout.getChildAt(0).width,
            height
        )

        createPdf()
    }


    private fun createPdf() {
        Log.e("createPdf", "inside")
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
        val foldername = "Tickets"
        val folder =
            File(Environment.getExternalStorageDirectory().toString() + "/" + foldername)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val simpleDateFormat =
            SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
        val date =
            simpleDateFormat.format(Calendar.getInstance().time)
        return folder.toString() + File.separator + "Ticket_" + date + downloadFileFormat
    }


    private fun getTicketCount() {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_type", loginType)
            jsonBody.put("is_grievance_officer", "false")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.getTicketCount(jsonBody, ticketCountResponseRetrofitCallback)

    }


    private fun getTicketList() {
        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", pageNo)
            jsonBody.put("paginationSize", paginationSize)
            jsonBody.put("sortField", "modified_date")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("user_uuid", userUUID)
            jsonBody.put("user_type", loginType)
            jsonBody.put("is_grievance_officer", "false")
            jsonBody.put("search", search)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        viewModel?.getTicketList(jsonBody, ticketListResponseRetrofitCallback)

    }

    val ticketCountResponseRetrofitCallback = object {
        override fun onSuccessfulResponse(responseBody: Response<TicketCountResponseModel>?) {
            countListAdapter!!.clearAll()
            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            Log.e("ticketCount", "" + responsedata)
            countListAdapter!!.addAll(responseBody!!.body()!!.responseContents!!)
        }

        override fun onBadRequest(errorBody: Response<TicketCountResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: TicketCountResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    TicketCountResponseModel::class.java
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


    val ticketListResponseRetrofitCallback = object {
        override fun onSuccessfulResponse(responseBody: Response<TicketListResponseModel>?) {

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)
            Log.e("ticketList", "" + responsedata)
            listData = null
            listData = responseBody?.body()?.responseContents!!
            binding.txtTotalCount.text = responseBody.body()?.totalRecords.toString()
            if (responseBody.body()?.responseContents?.isNotEmpty()!!) {
                Log.e("list size", "" + responseBody.body()?.responseContents?.size)
                ticketListAdpater!!.clearAll()
                ticketListAdpater!!.addAll(responseBody.body()!!.responseContents!!)
                binding.progressbar!!.visibility = View.GONE

            } else {
                Toast.makeText(requireContext(), "No records found", Toast.LENGTH_LONG).show()
                binding.progressbar!!.visibility = View.GONE
                ticketListAdpater!!.clearAll()

            }

            getTicketCount()

        }

        override fun onBadRequest(errorBody: Response<TicketListResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: TicketListResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    TicketListResponseModel::class.java
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


    override fun OnDeleteClick(model: TicketListResponseContent?) {
        Log.e("OnDeleteClick", "inside")
    }

    override fun OnViewClick(model: TicketListResponseContent?) {
        Log.e("OnViewClick", "inside")

        val ft = childFragmentManager.beginTransaction()
        val managedialog = ViewTicketFragment()
        val bundle = Bundle()
        bundle.putInt("uuid", model?.uuid!!)
        managedialog.arguments = bundle
        managedialog.show(ft, "Tag")
    }

}