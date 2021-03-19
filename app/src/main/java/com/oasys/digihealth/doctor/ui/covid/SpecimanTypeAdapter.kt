package com.oasys.digihealth.doctor.ui.covid

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.dashboard.model.ResponseSpicemanTypeContent
import java.util.*
import kotlin.collections.ArrayList


class SpecimanTypeAdapter(
        private val context: Context
) :
        RecyclerView.Adapter<SpecimanTypeAdapter.MyViewHolder>() {
    private var specimalTypeModel: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

    private var SelectedspecimalTypeModel: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null

    private var commands:String?=""



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.specimen_recycler_list, parent, false)
        var checkboxTextview: TextView
        var checkBox : CheckBox
        return MyViewHolder(view)
    }
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = specimalTypeModel!![position]
        holder.checkboxTextview.setText(response?.name)
        holder.checkBox.isChecked = response!!.isSelect
        /*
        checkbox on click
         */

        holder.checkBox.isChecked = response!!.isSelect


        if(response!!.isSelect){

            holder.checkBox.isChecked = response!!.isSelect

            holder.calenderEditTest.setText(response.date)

            holder.labels.isEnabled=response!!.isSelect

            holder.labels.setText(response.othercommands)

            val datasize=response.othercommands.trim().length

            val datasize2=response.date.trim().length

            if(datasize2!=0){
                holder.calenderEditTest.setText(response.date)
            }
            else{
                holder.calenderEditTest.error="Date must be enter"
            }

            if(datasize!=0){
                holder.labels.setText(response.othercommands)
            }
            else{
                holder.labels.error="Label must be enter"
            }
        }

        else{


            holder.checkBox.isChecked = response!!.isSelect

            specimalTypeModel!![position]!!.date=""

            specimalTypeModel!![position]!!.othercommands=""

            holder.calenderEditTest.setText(response.date)

            holder.labels.isEnabled=response!!.isSelect

            holder.labels.setText("")

        }


        holder.labels.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                val responseSpicemanTypeContent = specimalTypeModel!![position]
                responseSpicemanTypeContent!!.othercommands = s.toString()


            }
        })




        holder.checkBox.setOnClickListener {
            val myCheckBox = it as CheckBox
            val responseSpicemanTypeContent = specimalTypeModel!![position]
            if (myCheckBox.isChecked) {


                responseSpicemanTypeContent!!.isSelect = true
                responseSpicemanTypeContent.othercommands
                SelectedspecimalTypeModel?.add(responseSpicemanTypeContent)

                holder.labels.isEnabled=true

                val datasize=response.othercommands.trim().length

                val datasize2=response.date.trim().length

                if(datasize2!=0){
                    holder.calenderEditTest.setText(response.date)
                }
                else{
                    holder.calenderEditTest.error="Date must be enter"
                }

                if(datasize!=0){
                    holder.labels.setText(response.othercommands)
                }
                else{
                    holder.labels.error="Label must be enter"
                }

            } else if (!myCheckBox.isChecked) {

                responseSpicemanTypeContent!!.isSelect = false
                responseSpicemanTypeContent.othercommands
                SelectedspecimalTypeModel?.remove(responseSpicemanTypeContent)
                holder.labels.isEnabled=false

                holder.labels.error=null

                holder.calenderEditTest.error=null
            }
        }

        holder.calenderEditTest.setOnClickListener {

            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                    this.context,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        holder.calenderEditTest.setText(""+dayOfMonth+"/"+(monthOfYear!! +1)+"/"+year)

                        specimalTypeModel!![position]!!.date=""+dayOfMonth+"/"+(monthOfYear!! +1)+"/"+year

                        holder.calenderEditTest.error=null

                    }, mYear!!, mMonth!!, mDay!!
            )
            datePickerDialog.show()
        }

    }
    override fun getItemCount(): Int {
        return specimalTypeModel!!.size
    }
    fun setdata(responseContents: ArrayList<ResponseSpicemanTypeContent?>?) {
        specimalTypeModel = responseContents
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<ResponseSpicemanTypeContent?>? {

        return SelectedspecimalTypeModel
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val checkboxTextview: TextView = itemView.findViewById(R.id.checkboxTextview)
        internal val checkBox : CheckBox = itemView.findViewById(R.id.dateCheckbox)

        internal val calenderEditTest : EditText = itemView.findViewById(R.id.calendarEditText)

        internal val labels : EditText = itemView.findViewById(R.id.label)


    }}