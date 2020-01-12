package com.omejc.instagram2.requests

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.omejc.instagram2.helpers.Base64Helper
import com.omejc.instagram2.helpers.Constants
import org.json.JSONObject

class ImageUploadRequest {

    companion object {

        /*public fun makeRequest(imageData: JSONObject, context: Context) {

            val queue = Volley.newRequestQueue(context)
            val jsonObjectRequest = getJsonObjectRequest(imageData)
            queue.add(jsonObjectRequest)
        }*/

        public fun getJsonLoadRequest(mongoId: String, imageView: ImageView): JsonObjectRequest {
            return object : JsonObjectRequest(
                Method.GET, Constants.GET_IMAGE_URL_IMAGE_UPLOAD +"/"+mongoId, null,
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


        public fun getJsonSaveRequest(imageData: JSONObject): JsonObjectRequest {
            return object : JsonObjectRequest(
                Method.POST,
                Constants.SAVE_IMAGE_URL_IMAGE_UPLOAD, imageData,
                Response.Listener {
                        response -> Log.d("TAG", response.toString())
                },
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
        }

        public fun makeRequest(jsonObjectRequest: JsonObjectRequest, context: Context) {
            val queue = Volley.newRequestQueue(context)
            queue.add(jsonObjectRequest)
        }


    }




}