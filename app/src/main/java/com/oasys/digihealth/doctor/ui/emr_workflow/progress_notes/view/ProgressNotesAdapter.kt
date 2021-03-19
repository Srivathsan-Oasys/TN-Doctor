package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.view

import android.app.Application
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model.ResponseContent
import com.oasys.digihealth.doctor.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_progress_notes.view.*
import java.text.SimpleDateFormat
import java.util.*

class ProgressNotesAdapter(
    private val list: ArrayList<ResponseContent>,
    private val editNote: (note: ResponseContent) -> Unit,
    private val deleteNote: (note: ResponseContent) -> Unit
) :
    RecyclerView.Adapter<ProgressNotesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appPreferences: AppPreferences? =
            AppPreferences.getInstance(view.context, AppConstants.SHARE_PREFERENCE_NAME)
        var encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress_notes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val utils = Utils(holder.itemView.context)
        val note = list[position]
        holder.itemView.tvDailyName.text = "${note.uFirstName} - ${note.dName}"
        holder.itemView.tvDailyTime.text =
            note.pCreatedDate?.let { utils.displayDate(it, "yyyy-MM-dd HH:mm:ss") }
        holder.itemView.tvDailyNotes.text = note.pDailyNote
        holder.itemView.tvSpecialName.text = "${note.uFirstName} - ${note.dName}"
        holder.itemView.tvSpecialTime.text =
            note.pCreatedDate?.let { utils.displayDate(it, "yyyy-MM-dd HH:mm:ss") }
        holder.itemView.tvSpecialNotes.text = note.pSpecialNote

        val userDetailsRoomRepository =
            UserDetailsRoomRepository(holder.itemView.context.applicationContext as Application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val doctorUuid = userDataStoreBean?.uuid

        if (isToday(note.pCreatedDate) &&
            isCurrentUser(doctorUuid ?: 0, note.uUuid ?: 1)
        ) {
            makeEditDeleteVisible(holder.itemView.imgDailyEdit, View.VISIBLE)
            makeEditDeleteVisible(holder.itemView.imgDailyDelete, View.VISIBLE)
            makeEditDeleteVisible(holder.itemView.imgSpecialEdit, View.VISIBLE)
            makeEditDeleteVisible(holder.itemView.imgSpecialDelete, View.VISIBLE)
        } else {
            makeEditDeleteVisible(holder.itemView.imgDailyEdit, View.GONE)
            makeEditDeleteVisible(holder.itemView.imgDailyDelete, View.GONE)
            makeEditDeleteVisible(holder.itemView.imgSpecialEdit, View.GONE)
            makeEditDeleteVisible(holder.itemView.imgSpecialDelete, View.GONE)
        }

        Picasso.get()
            .load(note.uUserImgUrl)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(holder.itemView.imgUser1)
        Picasso.get()
            .load(note.uUserImgUrl)
            .error(R.drawable.ic_user)
            .placeholder(R.drawable.ic_user)
            .into(holder.itemView.imgUser2)

        holder.itemView.imgDailyEdit.setOnClickListener {
            editNotes(note)
        }

        holder.itemView.imgSpecialEdit.setOnClickListener {
            editNotes(note)
        }

        holder.itemView.imgDailyDelete.setOnClickListener {
            deleteNotes(note)
        }

        holder.itemView.imgSpecialDelete.setOnClickListener {
            deleteNotes(note)
        }

        makeNotesVisibleOnlyIfValuePresent(holder, note)
    }

    private fun editNotes(note: ResponseContent) {
        editNote(note)
    }

    private fun deleteNotes(note: ResponseContent) {
        deleteNote(note)
    }

    private fun isToday(date: String?): Boolean {
        date?.let {
            try {
                val givenDateString = date
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val mDate: Date? = sdf.parse(givenDateString)
                val timeInMilliseconds = mDate?.time
                return DateUtils.isToday(timeInMilliseconds ?: 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
        return false
    }

    private fun makeEditDeleteVisible(view: View, visibility: Int) {
        view.visibility = visibility
    }

    private fun isCurrentUser(doctorUuid: Int, uUuidFromApi: Int): Boolean {
        return doctorUuid == uUuidFromApi
    }

    private fun makeNotesVisibleOnlyIfValuePresent(holder: MyViewHolder, note: ResponseContent) {
        if (note.pDailyNote?.isNotEmpty() == true) {
            holder.itemView.llDailyNote.visibility = View.VISIBLE
        } else {
            holder.itemView.llDailyNote.visibility = View.GONE
        }

        if (note.pSpecialNote?.isNotEmpty() == true) {
            holder.itemView.llSpecialNote.visibility = View.VISIBLE
        } else {
            holder.itemView.llSpecialNote.visibility = View.GONE
        }
    }
}
