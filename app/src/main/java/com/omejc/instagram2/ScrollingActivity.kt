package com.omejc.instagram2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.omejc.instagram2.ImageFragment.OnListFragmentInteractionListener
import com.omejc.instagram2.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_scrolling.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*


class ScrollingActivity : AppCompatActivity(), OnListFragmentInteractionListener{
    val TAG = "ScrollingActivity";
    val url = "https://34.76.186.88/image-upload/api/images/saveImage"
    //val url = "http://34.76.186.88/image-upload/api/images"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        NukeSSLCerts.nuke()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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
            R.id.action_camera -> makeRequest(imageData)//dispatchTakePictureIntent()//sendGetRequest()//makeRequest(imageData)
        }

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageData = getJsonObject(bitmapToBase64(imageBitmap), "testTitle")
            Log.d(TAG, "image data:$imageData")

            makeRequest(imageData)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val asd:String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return asd
    }

    private fun makeRequest(imageData: JSONObject){

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = getJsonObjectRequest(imageData)
        queue.add(jsonObjectRequest)
    }

    private fun getJsonObjectRequest(imageData: JSONObject): JsonObjectRequest{
        return object : JsonObjectRequest(
            Method.POST, url, imageData,
            Response.Listener { response -> Log.d("TAG", response.toString()) },
            Response.ErrorListener { error -> Log.e("TAG", error.message, error) }) {
            //no semicolon or coma
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> =
                    HashMap()
                params["Content-Type"] = "application/json; charset=utf-8";
                return params
            }
        }
    }

    private fun getJsonObject(image: String, title: String): JSONObject{
        val params = HashMap<String,String>()
        Log.d("TAG", image)
        params["title"] = "testTitle"
        params["content"] = image
        return JSONObject(params as Map<*, *>)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
