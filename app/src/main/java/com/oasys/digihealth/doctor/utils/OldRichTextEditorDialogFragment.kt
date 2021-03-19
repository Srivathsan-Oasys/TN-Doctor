package com.oasys.digihealth.doctor.utils

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.oasys.digihealth.doctor.R

class OldRichTextEditorDialogFragment(
    private val mContext: Context,
    private val title: String? = null,
    private val body: String?,
    private val stringAsHtml: (String) -> Unit
) : DialogFragment() {

    private var tvTitle: TextView? = null
    private var viewtext: TextView? = null
    private var ivClose: ImageView? = null
    val FROM_HTML_MODE_COMPACT = 63
    val FROM_HTML_MODE_LEGACY = 0
    val FROM_HTML_OPTION_USE_CSS_COLORS = 256
    val FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32
    val FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16
    val FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2
    val FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8
    val FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4
    val FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1
    val TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0
    val TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1

    //    private var editor: Editor? = null
    private var saveCardView: CardView? = null
    private var clearCardView: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.dialog_rich_text_editor, container, false)

        initViews(view)
        listeners(view)

        return view
    }

    private fun initViews(view: View) {
        tvTitle = view.findViewById(R.id.tvTitle)
        viewtext = view.findViewById(R.id.viewtext)
        ivClose = view.findViewById(R.id.ivClose)
//        editor = view.findViewById(R.id.editor)
        saveCardView = view.findViewById(R.id.saveCardView)
        clearCardView = view.findViewById(R.id.clearCardView)

        title?.let { tvTitle?.text = it }
//        editor?.clearAllContents()
        body?.let {
            val html = Html.fromHtml(body)
//            editor?.render(body)
            viewtext!!.text = Html.fromHtml(body, null, MyHtmlTagHandler())

        }
    }

    private fun listeners(view: View) {
//        editorListeners(view)

        ivClose?.setOnClickListener {
            dismiss()
        }

        /* clearCardView?.setOnClickListener {
             editor?.clearAllContents()
         }*/

        saveCardView?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 24) {
                stringAsHtml(
                    Html.fromHtml(viewtext!!.text.toString(), FROM_HTML_MODE_COMPACT).toString()
                )
                dismiss()
            } else {
                stringAsHtml(Html.fromHtml(viewtext!!.text.toString()).toString())
                dismiss()
            }

        }
    }

    /* private fun editorListeners(view: View) {
         view.findViewById<View>(R.id.action_h1).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.H1)
         }

         view.findViewById<View>(R.id.action_h2).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.H2)
         }

         view.findViewById<View>(R.id.action_h3).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.H3)
         }

         view.findViewById<View>(R.id.action_bold).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.BOLD)
         }

         view.findViewById<View>(R.id.action_Italic).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.ITALIC)
         }

         view.findViewById<View>(R.id.action_indent).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.INDENT)
         }

         view.findViewById<View>(R.id.action_outdent).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.OUTDENT)
         }

         view.findViewById<View>(R.id.action_bulleted).setOnClickListener {
             editor?.insertList(false)
         }

         view.findViewById<View>(R.id.action_color).setOnClickListener {
             editor?.updateTextColor("#FF3333")
         }

         view.findViewById<View>(R.id.action_unordered_numbered).setOnClickListener {
             editor?.insertList(true)
         }

         view.findViewById<View>(R.id.action_hr).setOnClickListener {
             editor?.insertDivider()
         }

         view.findViewById<View>(R.id.action_insert_image).setOnClickListener {
             editor?.openImagePicker()
         }

         view.findViewById<View>(R.id.action_insert_link).setOnClickListener {
             editor?.insertLink()
         }

         view.findViewById<View>(R.id.action_erase).setOnClickListener {
             editor?.clearAllContents()
         }

         view.findViewById<View>(R.id.action_blockquote).setOnClickListener {
             editor?.updateTextStyle(EditorTextStyle.BLOCKQUOTE)
         }

         editor?.render()
     }*/
}