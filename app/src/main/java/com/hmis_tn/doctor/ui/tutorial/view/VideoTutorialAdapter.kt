package com.hmis_tn.doctor.ui.tutorial.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.tutorial.model.UserModuleResponseContent
import com.hmis_tn.doctor.ui.tutorial.viewmodel.VideoTutorialViewModel
import okhttp3.ResponseBody
import retrofit2.Response


class VideoTutorialAdapter(
    context: Context,
    private var userManualTutotrial: ArrayList<UserModuleResponseContent?>?,
    private var clickListener: TutorialCallback,
    private var viewModel: VideoTutorialViewModel
) :
    RecyclerView.Adapter<VideoTutorialAdapter.MyViewHolder>() {   //, private var roleControlActivityList: List<RoleControlActivityResponseContent?>?
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    private var isLoadingAdded = false
    private var customdialog: Dialog? = null
    private var myholder: MyViewHolder? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var moreImageView: ImageView
        var image: ImageView
        var nameTextView: TextView

        init {
            moreImageView = view.findViewById<View>(R.id.ImageViewMore) as ImageView
            image = view.findViewById<View>(R.id.image) as ImageView
            nameTextView = view.findViewById<View>(R.id.nameTextView) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_video_tutorial, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(
            itemLayout
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myholder = holder
        val reportData = userManualTutotrial!![position]
        holder.nameTextView.text = getFileName(reportData?.tutorial_url!!)

        //       viewModel?.getDownload(reportData?.thumbnail_url!!,downloadimagecallback)

        holder.moreImageView.setOnClickListener(View.OnClickListener {
            val popup =
                PopupMenu(mContext, holder.moreImageView)
            popup.inflate(R.menu.tutorial_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        customdialog = Dialog(mContext)
                        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        customdialog!!.setCancelable(false)
                        customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                        val closeImageView =
                            customdialog!!.findViewById(R.id.closeImageView) as ImageView

                        closeImageView.setOnClickListener {
                            customdialog?.dismiss()
                        }
                        var filename = customdialog!!.findViewById(R.id.addDeleteName) as TextView

                        filename.text =
                            "${filename.text} '" + holder.nameTextView.text.toString()
                        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                        yesBtn.setOnClickListener {
                            customdialog!!.dismiss()
                            clickListener.OnDeleteClick(reportData)
                        }
                        noBtn.setOnClickListener {
                            customdialog!!.dismiss()
                        }
                        customdialog!!.show()

                        return@setOnMenuItemClickListener true
                    }
                    R.id.download -> {
                        clickListener.OnDownloadClick(reportData)
                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
            false
            popup.show()
        })

    }

    override fun getItemCount(): Int {
        return userManualTutotrial!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }


    fun addAll(responseContent: List<UserModuleResponseContent?>?) {

        this.userManualTutotrial!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.userManualTutotrial?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(UserModuleResponseContent())
    }

    fun add(r: UserModuleResponseContent) {
        userManualTutotrial!!.add(r)
        notifyItemInserted(userManualTutotrial!!.size - 1)
    }

    fun getItem(position: Int): UserModuleResponseContent? {
        return userManualTutotrial!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = userManualTutotrial!!.size - 1
        val result = getItem(position)
        if (result != null) {
            userManualTutotrial!!.removeAt(position)
            notifyItemRemoved(position)
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

    val downloadimagecallback =
        object:RetrofitCallback<ResponseBody> {
            override fun onSuccessfulResponse(response: Response<ResponseBody>) {
                Log.i("", "message")
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        val bmp: Bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                        myholder?.image?.setImageBitmap(bmp)
                    } else {
                        myholder?.image?.setImageResource(R.drawable.ic_videoplayer)
                    }
                } else {
                    myholder?.image?.setImageResource(R.drawable.ic_videoplayer)
                }

            }

            override fun onBadRequest(response: Response<ResponseBody>) {
                val gson = GsonBuilder().create()
                Log.e("onBadRequest", "inside")
            }

            override fun onServerError(response: Response<*>) {
                Log.e("onServerError", "inside")
            }

            override fun onUnAuthorized() {
                Log.e("onUnAuthorized", "inside")
            }

            override fun onForbidden() {
                Log.e("onForbidden", "inside")
            }

            override fun onFailure(failure: String) {
                Log.e("onFailure", "inside")
            }

            override fun onEverytime() {
                Log.e("onEverytime", "inside")
                viewModel.progress.value = 8
            }
        }

}