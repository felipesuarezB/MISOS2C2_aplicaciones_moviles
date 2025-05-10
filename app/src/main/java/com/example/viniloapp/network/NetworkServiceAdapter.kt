package com.example.viniloapp.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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

    suspend fun get(path: String): String = suspendCoroutine { cont ->
        val request = StringRequest(
            Request.Method.GET,
            BASE_URL + path,
            { response -> cont.resume(response) },
            { error -> cont.resumeWithException(error) }
        )
        requestQueue.add(request)
    }

    suspend fun post(
        path: String,
        jsonBody: JSONObject
    ): JSONObject = suspendCoroutine { cont ->
        val request = JsonObjectRequest(
            Request.Method.POST,
            BASE_URL + path,
            jsonBody,
            { response -> cont.resume(response) },
            { error -> cont.resumeWithException(error) }
        )
        requestQueue.add(request);
    }

}