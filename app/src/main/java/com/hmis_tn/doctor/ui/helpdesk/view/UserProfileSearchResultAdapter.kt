package com.hmis_tn.doctor.ui.helpdesk.view

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.helpdesk.model.VendorResponseContent


class UserProfileSearchResultAdapter(
    context: Context, @LayoutRes private val layoutResource: Int,
    private val allPois: List<VendorResponseContent?>
) :
    ArrayAdapter<VendorResponseContent?>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<VendorResponseContent?> = allPois

    override fun getCount(): Int {
        Log.e("getCount", "" + mPois.size)
        return mPois.size
    }

    override fun getItem(p0: Int): VendorResponseContent? {
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
        name.text = responseContent!!.first_name

        return view
    }
}