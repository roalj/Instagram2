package com.omejc.instagram2.requests

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.omejc.instagram2.helpers.Base64Helper
import com.omejc.instagram2.helpers.Constants
import org.json.JSONArray
import org.json.JSONObject

class FilterRequest {
    companion object {
        private val TAG = CatalogRequest::class.java.name

        /*public fun makeRequest(mongoId: String,context: Context?, myListener: Response.Listener<JSONObject>) {
            val requestQueue = Volley.newRequestQueue(context)
            val jsonObjectRequest = getJsonFilterRequest(getMongoIdJsonObject(mongoId))
            requestQueue.add(jsonObjectRequest)
        }*/

        public fun getJsonFilterRequest(imageData: JSONObject, imageView: ImageView): JsonObjectRequest {
            return object : JsonObjectRequest(
                Method.POST, Constants.GET_FILTER_URL_IMAGE_FILTERS, imageData,
                Response.Listener { response ->
                    Log.d("TAG", "response LOAD: $response")
                    val content = response.getString("content")
                    Log.d("TAG", "content$content")
                    val bitmap =
                        Base64Helper.base64ToBitmap(
                                content
                        )
                    imageView.setImageBitmap(bitmap)
                },
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
        public fun getMongoIdJsonObject(mongoId: String, filter: String): JSONObject{
            val params = HashMap<String,String>()
            params["mongoId"] = mongoId
            params["filter"] = filter
            return JSONObject(params as Map<*, *>)
        }
    }

}