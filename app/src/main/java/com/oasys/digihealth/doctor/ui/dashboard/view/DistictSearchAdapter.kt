package com.oasys.digihealth.doctor.ui.dashboard.view

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.ui.dashboard.model.registration.District

class DistictSearchAdapter(
    context: Context, @LayoutRes
    private val layoutResource: Int,
    private var allPois: ArrayList<District>
) :
    ArrayAdapter<District>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<District> = allPois

    override fun getCount(): Int {
        return mPois.size
    }

    override fun getItem(p0: Int): District? {
        return mPois.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return mPois.get(p0).uuid.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(layoutResource, parent, false)
        val name = view!!.findViewById<View>(R.id.nameTextView) as TextView
        val responseContent = getItem(position)
        name.text = responseContent!!.name

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: Filter.FilterResults
            ) {
                mPois = filterResults.values as List<District>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.name.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }

    fun setData(allData: ArrayList<District>) {

        this.allPois = allData

        notifyDataSetChanged()

    }

    fun getName(uuid: Int): String {


        var stateName = ""

        for (i in this.allPois.indices) {

            if (this.allPois[i].uuid == uuid) {

                stateName = this.allPois[i].name

                break
            }

        }

        Log.i("distictid", "" + uuid + "name :" + stateName + " array " + this.allPois)
        return stateName
    }

    fun getAll(): ArrayList<District> {
        return this.allPois
    }
}