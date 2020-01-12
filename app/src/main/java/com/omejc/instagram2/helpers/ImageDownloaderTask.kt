package com.omejc.instagram2.helpers

import android.R
import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import java.lang.ref.WeakReference


internal class ImageDownloaderTask(imageView: ImageView) :
    AsyncTask<String?, Void?, Bitmap>() {
    private val imageViewReference: WeakReference<ImageView>?

    override fun doInBackground(vararg params: String?): Bitmap? {
        //return downloadBitmap(params[0])
        return null
    }

    override fun onPostExecute(bitmap: Bitmap) {
        var bitmap: Bitmap? = bitmap
        if (isCancelled) {
            bitmap = null
        }
        if (imageViewReference != null) {
            val imageView = imageViewReference.get()
            if (imageView != null) {
                if (bitmap != null) {

                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    init {
        imageViewReference = WeakReference(imageView)
    }

}