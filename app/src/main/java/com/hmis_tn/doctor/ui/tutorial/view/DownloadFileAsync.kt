package com.hmis_tn.doctor.ui.tutorial.view

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DownloadFileAsync(path: String) : AsyncTask<String, String, String>() {
    var path: String? = null

    lateinit var fpath: String
    var show = false


    init {
        this.path = path
    }


    override fun onPreExecute() {
        super.onPreExecute()
    }


    override fun doInBackground(vararg params: String?): String? {

        try {

            fpath = getFileName(this.path!!)
            Log.e("fpath", fpath)
            val url = URL(this.path!!)
            val conexion = url.openConnection()
            conexion.connect()
            val lenghtOfFile = conexion.contentLength
            val input = BufferedInputStream(url.openStream(), 512)
            val file = File(
                Environment.getExternalStorageDirectory().path.plus(File.separator).plus(fpath)
            )
            if (!file.exists()) file.createNewFile()
            val output = FileOutputStream(file)
            val data = ByteArray(512)
            output.write(data, 0, fpath.length)
            show = true
            output.flush()
            output.close()
            input.close()
            Log.e("fpath", file.absolutePath)
            return file.absolutePath
        } catch (e: Exception) {
            Log.e("Exception", "" + e)
        }
        return fpath
    }

    override fun onPostExecute(result: String) {
        Log.e("onPostExecute", "PDF downloaded")
        // Toast.makeText(mcontext!!, "PDF downloaded successfully", Toast.LENGTH_LONG).show()
    }

    private fun getFileName(wholePath: String): String {
        var name: String? = null
        val start: Int
        val end: Int
        start = wholePath.lastIndexOf('/')
        end = wholePath.length
        name = wholePath.substring((start + 1), end)
        return name
    }
}