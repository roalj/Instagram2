package com.omejc.instagram2

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class RequestHelper {

    companion object {
        private val TAG = RequestHelper::class.java.name
        fun makeRequest(context: Context?, myListener:Response.Listener<JSONArray>) {
            val requestQueue = Volley.newRequestQueue(context)
            // Initialize a new JsonArrayRequest instance
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                Constants.ALL_IMAGES_URL_IMAGE_CATALOG,
                null,
                myListener,
                Response.ErrorListener { error ->
                    // Do something when error occurred
                    Log.d(TAG, "on error" + error.message)
                }
            )
            // Add JsonArrayRequest to the RequestQueue
            requestQueue.add(jsonArrayRequest)
        }
    }
}
