package com.oasys.digihealth.doctor.ui.emr_workflow.documents


import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import com.oasys.digihealth.doctor.ui.emr_workflow.documents.ui.DocumentChildFragment

object FileUtils {

    fun getPath(activity: Context, uri: Uri): String {
        try {

            var uri = uri
            var selection: String? = null
            var selectionArgs: Array<String>? = null
// Uri is different in versions after KITKAT (Android 4.4), we need to
            if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(activity, uri)) {
                when {
                    DocumentChildFragment.isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":".toRegex()).toTypedArray()
                        return Environment.getExternalStorageDirectory()
                            .toString() + "/" + split[1]
                    }
                    DocumentChildFragment.isDownloadsDocument(uri) -> {
                        val id = DocumentsContract.getDocumentId(uri)
                        if (!TextUtils.isEmpty(id)) {
                            if (id.startsWith("raw:")) {
                                id.replaceFirst("raw:", "")
                            }
                        }
                        uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            id.toLong()

                        )
                    }
                    DocumentChildFragment.isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":".toRegex()).toTypedArray()
                        val type = split[0]
                        when (type) {
                            "image" -> {
                                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        selection = "_id=?"
                        selectionArgs = arrayOf(split[1])
                    }
                }
            }
            if ("content".equals(uri.scheme, ignoreCase = true)) {
                val projection =
                    arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor?
                try {
                    cursor = activity.contentResolver
                        .query(uri, projection, selection, selectionArgs, null)
                    val columnIndex: Int
                    if (cursor != null) {
                        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        if (cursor.moveToFirst()) {
                            return cursor.getString(columnIndex)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path.toString()
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }
}