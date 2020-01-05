package com.omejc.instagram2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.omejc.instagram2.ImageFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.activity_scrolling.*
import org.json.JSONObject


class ScrollingActivity : AppCompatActivity(), OnListFragmentInteractionListener{
    val TAG = "ScrollingActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        NukeSSLCerts.nuke()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val imageData = getJsonObject("testStringBase", "testTitle")

        when(item.itemId){
            R.id.action_camera -> dispatchTakePictureIntent()//sendGetRequest()//makeRequest(imageData)
        }

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            //val smallBitmap = Bitmap.createScaledBitmap(imageBitmap, 120, 120, false)
            Log.d(TAG, "qwe big bitmap " + sizeOf(imageBitmap))
           // Log.d(TAG, "qwe small bitmap " + sizeOf(smallBitmap))



            val imageData = getJsonObject(Base64Helper.bitmapToBase64(imageBitmap), "testTitle1")

            Log.d(TAG, "qwe content size " + imageData.getString("content").length)


            Log.d(TAG, "image data:$imageData")
            val saveRequest  = ImageUploadRequest.getJsonSaveRequest(imageData)
            ImageUploadRequest.makeRequest(saveRequest, this)
            onRefresh()
        }
    }

    fun sizeOf(data: Bitmap): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            data.rowBytes * data.height
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            data.byteCount
        } else {
            data.allocationByteCount
        }
    }

    private fun getJsonObject(image: String, title: String): JSONObject{
        val params = HashMap<String,String>()
        Log.d("TAG", image)
        params["title"] = "testTitle"
        params["content"] = image
        return JSONObject(params as Map<*, *>)
    }

    override fun onListFragmentInteraction(item: Image?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun onRefresh(){
        val fragment = supportFragmentManager.findFragmentById(R.id.article_fragment) as ImageFragment
        fragment.refresh()
    }


}

