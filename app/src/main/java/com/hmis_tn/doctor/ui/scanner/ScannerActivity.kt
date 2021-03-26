package com.hmis_tn.doctor.ui.scanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.budiyev.android.codescanner.*
import com.hmis_tn.doctor.R

class ScannerActivity : Activity() {

    private lateinit var codeScanner: CodeScanner
    internal lateinit var loading: TextView
    private lateinit var scannerView: CodeScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        loading = findViewById<TextView>(R.id.tv_loading)

        // Parameters (default values)
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val intent = Intent()
                intent.putExtra("MESSAGE", it.text)
                setResult(144, intent)
                finish()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        delayCameraLoading()
        if (!haveCameraPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        } else {
            qrScanner()
        }
    }

    fun qrScanner() {
        if (codeScanner == null) {
            codeScanner = CodeScanner(this, scannerView)
        }
        codeScanner.startPreview()
    }

    private fun delayCameraLoading() {
        val handler = Handler()
        handler.postDelayed({
            // frameLayout!!.visibility = View.VISIBLE
            loading.visibility = View.INVISIBLE
        }, 1000)
    }

    private fun haveCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT < 23) true else checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    companion object {
        val PERMISSION_REQUEST_CAMERA = 1
    }
}