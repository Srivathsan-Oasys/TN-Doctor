package com.hmis_tn.doctor.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.databinding.DialogImageviewBinding

class ImageviewFragemnt : DialogFragment() {

    private var content: String? = null
    var binding: DialogImageviewBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_imageview, container, false)

        binding?.lifecycleOwner = this
        val args = arguments
        if (args != null) {
            val imageuri = args.getString(AppConstants.ImageURI)
            val uri = Uri.parse(imageuri)
            binding!!.imgView.setImageURI(uri)
        }
        return binding?.root
    }
}