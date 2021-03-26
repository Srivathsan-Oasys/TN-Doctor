package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentNameresponseContent

class TreatmentDiagnosisSearchResultAdapter(
    context: Context, @LayoutRes private val layoutResource: Int,
    private val allPois: List<TreatmentNameresponseContent?>?
) :
    ArrayAdapter<TreatmentNameresponseContent>(context, layoutResource, allPois!!),
    Filterable {
    private var mPois: List<TreatmentNameresponseContent?>? = allPois
    override fun getCount(): Int {
        return mPois!!.size
    }

    override fun getItem(p0: Int): TreatmentNameresponseContent? {
        return mPois!!.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return mPois!!.get(p0)!!.treatment_kit_id?.toLong()!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(layoutResource, parent, false)
        val name = view!!.findViewById<View>(R.id.nameTextView) as TextView
        val responseContent = getItem(position)
        name.text = responseContent!!.treatment_name

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: Filter.FilterResults
            ) {
                mPois = filterResults.values as List<TreatmentNameresponseContent>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allPois
                else
                    allPois!!.filter {
                        it!!.treatment_name?.toLowerCase()?.contains(queryString)!!
                    }
                return filterResults
            }
        }
    }
}

