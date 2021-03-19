package com.oasys.digihealth.doctor.utils

import android.os.Build
import android.text.Html
import android.widget.TextView

object ViewUtils {

    fun TextView.setMandatoryText(text: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            this.text = Html.fromHtml(
                "<font>${text}</font><font color=\"#FF0000\">*</font>",
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            this.text = Html.fromHtml(
                "<font>${text}</font><font color=\"#FF0000\">*</font>"
            )
        }
    }

}