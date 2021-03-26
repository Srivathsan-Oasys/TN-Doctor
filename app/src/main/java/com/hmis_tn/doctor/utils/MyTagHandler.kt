package com.hmis_tn.doctor.utils

import android.text.Editable
import android.text.Html.TagHandler
import android.text.Spannable
import android.text.style.StrikethroughSpan
import org.xml.sax.XMLReader


/**
 * Implements support for ordered (`<ol>`) and unordered (`<ul>`) lists in to Android TextView.
 *
 *
 * This can be used as follows:<br></br>
 * `textView.setText(Html.fromHtml("<ul><li>item 1</li><li>item 2</li></ul>", null, new HtmlListTagHandler()));`
 *
 *
 * Implementation based on code by Juha Kuitunen (https://bitbucket.org/Kuitsi/android-textview-html-list),
 * released under Apache License v2.0. Refactored & improved by Matthias Stevens (InThePocket.mobi).
 *
 *
 * **Known issues:**
 *  * The indentation on nested `<ul>`s isn't quite right (TODO fix this)
 *  * the `start` attribute of `<ol>` is not supported. Doing so is tricky because
 * [Html.TagHandler.handleTag] does not expose tag attributes.
 * The only way to do it would be to use reflection to access the attribute information kept by the XMLReader
 * (see: http://stackoverflow.com/a/24534689/1084488).
 *
 */
class MyHtmlTagHandler : TagHandler {
    override fun handleTag(
        opening: Boolean, tag: String, output: Editable,
        xmlReader: XMLReader?
    ) {
        if (tag.equals("strike", ignoreCase = true) || tag == "s") {
            processStrike(opening, output)
        }
    }

    private fun processStrike(opening: Boolean, output: Editable) {
        val len = output.length
        if (opening) {
            output.setSpan(StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK)
        } else {
            val obj = getLast(output, Any::class.java)
            val where = output.getSpanStart(obj)
            output.removeSpan(obj)
            if (where != len) {
                output.setSpan(StrikethroughSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    private fun getLast(text: Editable, kind: Class<Any>): Any? {
        val objs = text.getSpans<Any>(0, text.length, kind)
        return if (objs.size == 0) {
            null
        } else {
            for (i in objs.size downTo 1) {
                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i - 1]
                }
            }
            null
        }
    }
}

