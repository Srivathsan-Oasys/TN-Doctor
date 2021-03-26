package com.hmis_tn.doctor

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.collection.LruCache
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class FileHelperReports(private val context: Context?) {

    companion object {
        const val PARENT_FOLDER = "HMIS"
    }

    private var mFile: File? = null

    fun getScreenshotFromRecyclerView(view: RecyclerView?): Bitmap? {
        val adapter = view?.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var height = 0
            val paint = Paint()
            var iHeight = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmapCache: LruCache<String, Bitmap> = LruCache(cacheSize)
            for (i in 0 until size) {
                val holder =
                    adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        view.width,
                        View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                        0,
                        View.MeasureSpec.UNSPECIFIED
                    )
                )
                holder.itemView.layout(
                    0,
                    0,
                    holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight
                )
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {
                    bitmapCache.put(i.toString(), drawingCache)
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.measuredHeight
            }
            bigBitmap =
                Bitmap.createBitmap(view.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)
            bigCanvas.drawColor(Color.WHITE)
            for (i in 0 until size) {
                val bitmap: Bitmap? = bitmapCache.get(i.toString())
                bitmap?.let { bigCanvas.drawBitmap(it, 0f, iHeight.toFloat(), paint) }
                iHeight += bitmap?.height ?: 0
                bitmap?.recycle()
            }
        }

        return bigBitmap
    }

    fun saveImageToPDF(
        title: View,
        bitmap: Bitmap,
        folder: File,
        filename: String
    ) {
        if (!folder.exists()) folder.mkdirs()

        mFile = File(folder, "$filename.pdf")
        if (!mFile!!.exists()) {
            val height = title.height + bitmap.height
            val document = PdfDocument()
            val pageInfo = PageInfo.Builder(bitmap.width, height, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            title.draw(canvas)
            canvas.drawBitmap(
                bitmap,
                null,
                Rect(0, title.height, bitmap.width, bitmap.height),
                null
            )
            document.finishPage(page)
            try {
                mFile!!.createNewFile()
                val out: OutputStream = FileOutputStream(mFile)
                document.writeTo(out)
                document.close()
                out.close()

                context?.let {
                    Toast.makeText(
                        context,
                        "Downloaded successfully\n" + mFile?.path,
                        Toast.LENGTH_LONG
                    ).show()
                }

                val file = File(Environment.getExternalStorageDirectory().absolutePath+"/"+filename)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.fromFile(file),"application/pdf")
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

                val openData = Intent.createChooser(intent, "Open File")
                try{
                    context!!.startActivity(openData)
                }catch (e: Exception){
                    e.printStackTrace()
                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}