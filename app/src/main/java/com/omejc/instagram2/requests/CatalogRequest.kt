package com.omejc.instagram2.requests

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.omejc.instagram2.helpers.Constants
import org.json.JSONArray

class CatalogRequest {

    companion object {
        private val TAG = CatalogRequest::class.java.name
        fun makeRequestGetAllImages(context: Context?, myListener:Response.Listener<JSONArray>) {
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

        fun makeRequestDeleteImage(context: Context?, id:String) {
            val requestQueue = Volley.newRequestQueue(context)
            // Initialize a new JsonArrayRequest instance
            val stringRequest = object : JsonObjectRequest(
                Method.DELETE,
                Constants.ALL_IMAGES_URL_IMAGE_CATALOG +"/"+ id, null,
                null,
                Response.ErrorListener {
                        error -> Log.e("TAG", error.message, error)
                }) {
                //no semicolon or coma
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["Content-Type"] = "application/json; charset=utf-8";
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }
    }
}
