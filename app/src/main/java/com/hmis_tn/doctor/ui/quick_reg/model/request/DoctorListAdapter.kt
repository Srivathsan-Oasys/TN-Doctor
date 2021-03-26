package com.hmis_tn.doctor.ui.quick_reg.model.request

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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.UserProfileResponse

class DoctorListAdapter(
    context: Context, @LayoutRes
    private val layoutResource: Int,
    private var allPois: ArrayList<UserProfileResponse>
) :
    ArrayAdapter<UserProfileResponse>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<UserProfileResponse> = allPois

    override fun getCount(): Int {
        return mPois.size
    }

    override fun getItem(p0: Int): UserProfileResponse? {
        return mPois.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return mPois.get(p0).uuid?.toLong()!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(layoutResource, parent, false)
        val name = view!!.findViewById<View>(R.id.nameTextView) as TextView
        val responseContent = getItem(position)
        name.text = responseContent!!.first_name

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: Filter.FilterResults
            ) {
                mPois = filterResults.values as List<UserProfileResponse>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.first_name?.toLowerCase()?.contains(queryString)!!
                    }
                return filterResults
            }
        }
    }

    fun setData(allData: ArrayList<UserProfileResponse>){

        this!!.allPois=allData

        notifyDataSetChanged()

    }

    fun getName(uuid:Int):String{


        var stateName=""

        for(i in this!!.allPois.indices){

            if(this!!.allPois[i].uuid==uuid){

                stateName=this!!.allPois[i].first_name

                break
            }

        }

        Log.i("distictid",""+uuid+"name :"+stateName+" array "+this!!.allPois)
        return stateName
    }

    fun getAll():ArrayList<UserProfileResponse>{
        return  this!!.allPois
    }
}