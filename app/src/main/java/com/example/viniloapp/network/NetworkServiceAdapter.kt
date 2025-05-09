package com.example.viniloapp.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorDetail
import com.example.viniloapp.models.Comment
import com.example.viniloapp.models.Performer
import com.example.viniloapp.models.CollectorAlbum
import com.example.viniloapp.models.AlbumDetail
import com.example.viniloapp.models.Track
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object {
        const val BASE_URL = "https://backvynils-q6yc.onrender.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }

        fun get(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) {
        val request:StringRequest = StringRequest(
            Request.Method.GET,
            BASE_URL + path,
            responseListener,
            errorListener
        )
        requestQueue.add(request);
    }

    fun post(
        path: String,
        jsonBody: JSONObject,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val request:JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            BASE_URL + path,
            jsonBody,
            responseListener,
            errorListener
        )
        requestQueue.add(request);
    }

}