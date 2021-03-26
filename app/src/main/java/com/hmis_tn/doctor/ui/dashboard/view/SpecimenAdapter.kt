package com.hmis_tn.doctor.ui.dashboard.view

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import java.text.SimpleDateFormat
import java.util.*


class SpecimenAdapter(context: Context, private val moviesList: List<RecyclerDto>) :
    RecyclerView.Adapter<SpecimenAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    var orderNumString: String? = null


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkboxTextview: TextView
        var calendarEditText: EditText


        init {
            checkboxTextview = view.findViewById<View>(R.id.checkboxTextview) as TextView
            calendarEditText = view.findViewById<View>(R.id.calendarEditText) as EditText

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.specimen_recycler_list, parent, false) as ConstraintLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = moviesList[position].toString()
        val movie = moviesList[position]
        holder.checkboxTextview.text = movie.title

        holder.calendarEditText.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                mContext,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val myFormat = "dd-MM-yyyy"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    holder.calendarEditText.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )


                }, mYear, mMonth, mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

            datePickerDialog.show()
        }

    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

}