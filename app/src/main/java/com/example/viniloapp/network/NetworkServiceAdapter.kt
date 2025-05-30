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


class NetworkServiceAdapter(context: Context) {
    companion object {
        const val BASE_URL = "http://backvynils-q6yc.onrender.com/"
        //const val BASE_URL = "http://10.0.2.2:3000/"
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
        requestQueue.add(
            StringRequest(
                Request.Method.GET,
                BASE_URL + path,
                { response -> cont.resume(response) },
                { error -> cont.resumeWithException(error) }
            )
        )
    }

    suspend fun post(
        path: String,
        jsonBody: JSONObject
    ): JSONObject = suspendCoroutine { cont ->
        requestQueue.add(
            JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + path,
                jsonBody,
                { response -> cont.resume(response) },
                { error -> cont.resumeWithException(error) }
            )

        )

    }

}