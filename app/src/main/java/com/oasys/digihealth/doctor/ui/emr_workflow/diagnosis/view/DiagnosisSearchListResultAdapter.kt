package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.DiagnosisResponseContent

class DiagnosisSearchListResultAdapter(
    context: Context, @LayoutRes private val layoutResource: Int,
    private val allPois: List<DiagnosisResponseContent?>
) :
    ArrayAdapter<DiagnosisResponseContent?>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<DiagnosisResponseContent?> = allPois

    override fun getCount(): Int {
        Log.e("getCount", "" + mPois.size)
        return mPois.size
    }

    override fun getItem(p0: Int): DiagnosisResponseContent? {
        return mPois.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return mPois.get(p0)?.uuid?.toLong()!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(layoutResource, parent, false)
        val name = view!!.findViewById<View>(R.id.nameTextView) as TextView
        val responseContent = getItem(position)
        if (responseContent!!.name!!.isNotEmpty()) {
            name.text = responseContent.name
        } else {
            //name.text = responseContent?.user_name
        }
        return view
    }
}