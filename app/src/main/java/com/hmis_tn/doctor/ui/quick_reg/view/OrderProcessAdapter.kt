package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants

class OrderProcessAdapter(context: Context, private var labTestList: ArrayList<orderList>) :
    RecyclerView.Adapter<OrderProcessAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null

    private var onCommandClickListener: OnCommandClickListener? = null

    //private var spinnerData = mutableMapOf<Int, String>()

//    private var spinnerData= mutableMapOf(1 to "Negative", 2 to "Positive", 3 to "Equivocal")

    // private var spinnerData= mutableMapOf<Int,String>()

    private var AlphanumericCode = "Alphanumeric"

//    private var spinner= mutableMapOf<String,Int>()


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialNumberTextView: TextView = view.findViewById(R.id.serialNumberTextView)
        var textProcess: TextView = view.findViewById(R.id.textProcess)
        var result_text: Spinner = view.findViewById(R.id.result_spinner)
        var uom_text: TextView = view.findViewById(R.id.uom_text)
        var normal_val_text: TextView = view.findViewById(R.id.normal_val_text)
        var mainLinearLayout: LinearLayout = view.findViewById(R.id.mainLinearLayout)
        var result: EditText = view.findViewById(R.id.resultData)
        var resultalphanumerioc: EditText = view.findViewById(R.id.resultDataAlphanumeric)
        var resultspecialCase: EditText = view.findViewById(R.id.resultDataSplcase)
        var resultnonEdiable: EditText = view.findViewById(R.id.resultDataNoneditable)
        var resultLongText: EditText = view.findViewById(R.id.resultLongText)
        var resultnotetemplate: EditText = view.findViewById(R.id.resultLongText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_order_process_list, parent, false) as LinearLayout
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor", "NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        orderNumString = labTestList[position].toString()
        holder.serialNumberTextView.text = (position + 1).toString()

        val movie = labTestList[position]
        holder.textProcess.text = movie.title

        holder.uom_text.text = movie.umo

        holder.normal_val_text.text = movie.value


        var spinnerData: MutableMap<Int, String> = movie.spinnerData
        var spinner: MutableMap<String, Int> = movie.spinner

        if (labTestList[position].type == AppConstants.DROPDOWNCODE) {

            holder.result_text.visibility = View.VISIBLE

            if (!labTestList[position].spinnerdata.isNullOrEmpty()) {

                //   spinnerData.clear()


                val adapter = ArrayAdapter<String>(
                    this.mContext,
                    R.layout.spinner_item,
                    spinnerData.values.toMutableList()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                holder.result_text.adapter = adapter

                labTestList[position].name

                if (labTestList[position].name != "" && labTestList[position].name != null) {

                    holder.result_text.setSelection(spinner[labTestList[position].name]!!)
                }
/*
                if(labTestList[position].code=="COVID") {

                    for (i in labTestList[position].spinnerdata.indices) {

                        val data = labTestList[position].spinnerdata[i]

                        when (data) {
                            "Positive" -> {

                                spinnerData[2] = data
                            }
                            "Negative" -> {

                                spinnerData[1] = data
                            }
                            "Equivocal" -> {

                                spinnerData[3] = data
                            }
                        }

                        spinner[data]=i


                    }

                    val adapter = ArrayAdapter<String>(
                        this.mContext,
                        R.layout.spinner_item,
                        spinnerData.values.toMutableList()
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    holder!!.result_text!!.adapter = adapter

                    labTestList[position].name

                    if(labTestList[position].name!="" && labTestList[position].name!=null ){

                        holder.result_text.setSelection(spinner[labTestList[position].name]!!)
                    }


                }
                else{
                    for (i in labTestList[position].spinnerdata.indices) {

                        val data = labTestList[position].spinnerdata[i]

                        spinnerData[i]=data

                        spinner[data]=i

                    }

                    val adapter = ArrayAdapter<String>(
                        this.mContext,
                        R.layout.spinner_item,
                        spinnerData.values.toMutableList()
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    holder!!.result_text!!.adapter = adapter

                    if(labTestList[position].name!="" && labTestList[position].name!=null ){

                        holder.result_text.setSelection(spinner[labTestList[position].name]!!)
                    }
                }*/
            }


        } else if (labTestList[position].type == AppConstants.NUMERICCODE) {

            holder.result.setText(labTestList[position].name)

            holder.result.visibility = View.VISIBLE


        } else if (labTestList[position].type == AppConstants.CALCULATIOCODE) {

            holder.resultnonEdiable.setText(labTestList[position].name)

            holder.resultnonEdiable.visibility = View.VISIBLE

        } else if (labTestList[position].type == AppConstants.ALPANUMBERCODE) {

            holder.resultalphanumerioc.setText(labTestList[position].name)

            holder.resultalphanumerioc.visibility = View.VISIBLE

        } else if (labTestList[position].type == AppConstants.APLASPLCODE) {

            holder.resultspecialCase.setText(labTestList[position].name)

            holder.resultspecialCase.visibility = View.VISIBLE

        } else if (labTestList[position].type == AppConstants.NOTETEMPLATECODE) {

            val templateText =
                Html.fromHtml(labTestList[position].name ?: "")
                    .toString()

            holder.resultnotetemplate.isFocusable = false

            holder.resultnotetemplate.setText(templateText)

            holder.resultnotetemplate.visibility = View.VISIBLE

        } else if (labTestList[position].type == AppConstants.LONGTEXTCODE) {

            holder.resultLongText.setText(labTestList[position].name)

            holder.resultLongText.visibility = View.VISIBLE

        }

        holder.resultLongText.setOnClickListener {


            onCommandClickListener!!.onCommandClick(
                position,
                labTestList[position].name ?: "",
                labTestList[position].note_template_uuid
            )


            /*    val richTextEditorDialog = RichTextEditorDialogFragment(
                    mContext = this.mContext,
                    title = "Template",
                    body = labTestList[position].name ?: "",
                    stringAsHtml = { stringAsHtml ->
                        //returns HTML when save is clicked
    
                        labTestList[position].name=stringAsHtml
                     //   println(stringAsHtml)
                    }
                )
                richTextEditorDialog.show(ft, "Tag")
    */

        }

        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }

        /*  holder.resultnonEdiable.setOnClickListener {
  
              val formulapoistion=labTestList[position].formulapostion
  
              val formula=labTestList[position].formula
  
              if(formulapoistion.isNotEmpty() && formula!=""){
  
                  when (formula) {
                      "+" -> {
  
                          var resut:String=""
  
                          Log.i("",""+formulapoistion)
                          for(i in formulapoistion.indices){
  
                              if(i==0){
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=postData.toString()
  
                                  }
  
  
                              }
                              else{
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=((resut.toInt().toDouble())+postData).toString()
  
                                  }
  
                              }
  
  
  
                          }
  
                          labTestList[position].name=resut
  
                          holder.resultnonEdiable.setText(resut)
  
                      }
                      "-" -> {
  
  
                          var resut:String=""
  
                          Log.i("",""+formulapoistion)
                          for(i in formulapoistion.indices){
  
                              if(i==0){
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=postData.toString()
  
                                  }
  
                              }
                              else{
  
  
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=((resut.toInt().toDouble())-postData).toString()
  
                                  }
  
                                  //
  
                              }
  
  
  
                          }
  
                          labTestList[position].name=resut
  
                          holder.resultnonEdiable.setText(resut)
  
                      }
                      "*" -> {
  
  
                          var resut:String=""
  
                          Log.i("",""+formulapoistion)
                          for(i in formulapoistion.indices){
  
                              if(i==0){
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=postData.toString()
  
                                  }
  
  
                              }
                              else{
  
  
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=((resut.toInt().toDouble())*postData).toString()
  
                                  }
  
                                  //
  
                              }
  
  
  
                          }
  
                          labTestList[position].name=resut
  
                          holder.resultnonEdiable.setText(resut)
  
                      }
                      "/" -> {
  
                          var resut:String=""
  
                          Log.i("",""+formulapoistion)
  
                          for(i in formulapoistion.indices){
  
                              if(i==0){
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=postData.toString()
  
                                  }
                              }
                              else{
  
                                  if(labTestList[formulapoistion[i]].name!=""){
  
                                      var postData=labTestList[formulapoistion[i]].name!!.toInt()
  
                                      resut=((resut.toInt().toDouble())/postData).toString()
  
                                  }
                              }
  
  
  
                          }
  
                          labTestList[position].name=resut
  
                          holder.resultnonEdiable.setText(resut)
  
                      }
                  }
              }
  
  
  
          }
  */
        holder.result_text.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    labTestList[position].id =
                        spinnerData.filterValues { it == itemValue }.keys.toList()[0]

                    labTestList[position].name =
                        spinnerData.filterValues { it == itemValue }.values.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    labTestList[position].id =
                        spinnerData.filterValues { it == itemValue }.keys.toList()[0]

                    labTestList[position].name =
                        spinnerData.filterValues { it == itemValue }.values.toList()[0]

                }

            }


        holder.result.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() != "") {


                    labTestList[position].id = 2

                    labTestList[position].name = s.toString()


                    for (j in labTestList.indices) {


                        if (labTestList[j].formula != "" && labTestList[j].formula != null) {

                            val formulapoistion = labTestList[j].formulapostion

                            val formula = labTestList[j].formula

                            if (formulapoistion.isNotEmpty() && formula != "") {

                                when (formula) {
                                    "+" -> {

                                        var resut: String = ""

                                        Log.i("", "" + formulapoistion)

                                        for (i in formulapoistion.indices) {

                                            if (i == 0) {

                                                if (labTestList[formulapoistion[i]].name != "") {

                                                    var postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()

                                                    resut = postData.toString()

                                                }
                                            } else {

                                                if (labTestList[formulapoistion[i]].name != "" && labTestList[formulapoistion[i]].name != null) {

                                                    var postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()

                                                    resut = ((resut.toInt()
                                                        .toDouble()) + postData).toString()

                                                } else {
                                                    resut =
                                                        ((resut.toInt().toDouble()) + 0).toString()
                                                }
                                            }


                                        }

                                        labTestList[j].name = resut

                                        //     holder.resultnonEdiable.setText(resut)

                                    }
                                    "-" -> {

                                        var resut: String = ""

                                        Log.i("", "" + formulapoistion)

                                        for (i in formulapoistion.indices) {

                                            if (i == 0) {

                                                if (labTestList[formulapoistion[i]].name != "") {

                                                    var postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()

                                                    resut = postData.toString()

                                                }
                                            } else {

                                                if (labTestList[formulapoistion[i]].name != "" && labTestList[formulapoistion[i]].name != null) {

                                                    var postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()

                                                    resut = ((resut.toInt()
                                                        .toDouble()) - postData).toString()

                                                } else {
                                                    resut =
                                                        ((resut.toInt().toDouble()) - 0).toString()
                                                }
                                            }
                                        }
                                        labTestList[j].name = resut
                                        //     holder.resultnonEdiable.setText(resut)
                                    }
                                    "*" -> {
                                        var resut: String = ""
                                        Log.i("", "" + formulapoistion)
                                        for (i in formulapoistion.indices) {
                                            if (i == 0) {
                                                if (labTestList[formulapoistion[i]].name != "") {
                                                    val postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()
                                                    resut = postData.toString()
                                                }
                                            } else {
                                                if (labTestList[formulapoistion[i]].name != "" &&
                                                    labTestList[formulapoistion[i]].name != null
                                                ) {
                                                    val postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()
                                                    resut = ((resut.toInt()
                                                        .toDouble()) * postData).toString()
                                                } else {
                                                    resut =
                                                        ((resut.toInt().toDouble()) * 0).toString()
                                                }
                                            }
                                        }
                                        labTestList[j].name = resut
                                        //     holder.resultnonEdiable.setText(resut)
                                    }
                                    "/" -> {
                                        var resut: String = ""
                                        Log.i("", "" + formulapoistion)
                                        for (i in formulapoistion.indices) {
                                            if (i == 0) {
                                                if (labTestList[formulapoistion[i]].name != "") {
                                                    val postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()
                                                    resut = postData.toString()
                                                }
                                            } else {
                                                if (labTestList[formulapoistion[i]].name != "" &&
                                                    labTestList[formulapoistion[i]].name != null
                                                ) {
                                                    val postData =
                                                        labTestList[formulapoistion[i]].name!!.toInt()
                                                    resut = ((resut.toInt()
                                                        .toDouble()) / postData).toString()
                                                } else {
                                                    resut =
                                                        ((resut.toInt().toDouble()) / 0).toString()
                                                }
                                            }
                                        }
                                        labTestList[j].name = resut
                                    }
                                }
                            }
                            notifyItemChanged(j)
                        }
                    }
                    labTestList[position].name = s.toString()
                } else {
                    labTestList[position].name = ""
                }
            }
        })

        holder.resultalphanumerioc.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() != "") {
                    labTestList[position].id = 2
                    labTestList[position].name = s.toString()
                } else {
                    labTestList[position].name = ""
                }
            }
        })

        holder.resultspecialCase.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() != "") {
                    labTestList[position].id = 2
                    labTestList[position].name = s.toString()
                } else {
                    labTestList[position].name = ""
                }
            }
        })

        holder.resultLongText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() != "") {
                    labTestList[position].id = 2
                    labTestList[position].name = Html.toHtml(s)
                } else {
                    labTestList[position].name = Html.toHtml(SpannableStringBuilder(""))
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return labTestList.size
    }

    fun setData(size: Int) {
        for (i in 0..size) {
            labTestList.add(orderList())
        }
        notifyDataSetChanged()
    }

    fun setAll(list: ArrayList<orderList>) {
        this.labTestList = list
        notifyDataSetChanged()
    }

    fun getAll(): ArrayList<orderList> {
        return this.labTestList
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.setIsRecyclable(false)
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.setIsRecyclable(true)
    }

    interface OnCommandClickListener {
        fun onCommandClick(
            position: Int,
            Command: String,
            noteTemplateUuid: Int
        )
    }

    fun setOnCommandClickListener(onCommandClickListener: OnCommandClickListener) {
        this.onCommandClickListener = onCommandClickListener
    }

    fun setTemplateData(position: Int, stringAsHtml: String) {
        this.labTestList[position].name = stringAsHtml
        notifyItemChanged(position)
    }
}