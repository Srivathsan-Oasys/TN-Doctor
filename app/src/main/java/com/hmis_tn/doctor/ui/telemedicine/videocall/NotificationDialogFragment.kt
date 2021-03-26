package com.hmis_tn.doctor.ui.telemedicine.videocall

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.hmis_tn.doctor.R
import kotlinx.android.synthetic.main.fragment_notification_dialog.*

class NotificationDialogFragment : DialogFragment(), View.OnClickListener {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var onClickIncomingNotification: OnClickIncomingNotification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.also {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (it.window != null)
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playRingTone()
        initUI()
    }

    private fun initUI() {
        clickListener()
        tvNotificationPatientName.text = "Daniel Jack"
    }

    private fun clickListener() {
        ivNotificationEndCall.setOnClickListener(this)
        ivNotificationAcceptCall.setOnClickListener(this)
        ivNotificationVideoMode.setOnClickListener(this)
        ivCloseDialog.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivNotificationEndCall -> {
                stopRingTone()
                onClickIncomingNotification.onClickRejectCall()
            }
            R.id.ivNotificationAcceptCall -> {
                stopRingTone()
                onClickIncomingNotification.onClickAcceptCall()
            }
            R.id.ivNotificationVideoMode -> ivNotificationEndCall.performClick()
            R.id.ivCloseDialog -> ivNotificationEndCall.performClick()
        }
    }

    private fun playRingTone() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val player: MediaPlayer = MediaPlayer.create(requireContext(), notification)
        if (mediaPlayer == null)
            mediaPlayer = player
        player.isLooping = true
        player.start()
    }

    private fun stopRingTone() {
        mediaPlayer?.let {
            it.isLooping = false
            it.stop()
            it.reset()
            dialog?.dismiss()
        }
    }

    fun setCallBackActionMethod(onClickIncomingNotification: OnClickIncomingNotification) {
        this.onClickIncomingNotification = onClickIncomingNotification
    }

    interface OnClickIncomingNotification {
        fun onClickAcceptCall()
        fun onClickRejectCall()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NotificationDialogFragment().apply {
                arguments = bundleOf()
            }
    }
}