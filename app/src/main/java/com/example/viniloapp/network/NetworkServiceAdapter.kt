package com.example.viniloapp.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
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

    fun getAlbums(onComplete: (resp: List<Album>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(
            getRequest(
                "albums",
                { response ->
                    try {
                        val resp = JSONArray(response)
                        val list = mutableListOf<Album>()
                        for (i in 0 until resp.length()) {
                            val item = resp.getJSONObject(i)
                            list.add(
                                Album(
                                    id = item.getInt("id"),
                                    name = item.getString("name"),
                                    cover = item.getString("cover"),
                                    recordLabel = item.getString("recordLabel"),
                                    releaseDate = item.getString("releaseDate"),
                                    genre = item.getString("genre"),
                                    description = item.getString("description")
                                )
                            )
                        }
                        onComplete(list)
                    } catch (e: Exception) {
                        Log.e("NetworkServiceAdapter", "Error parsing albums", e)
                        onError(VolleyError(e))
                    }
                },
                { error ->
                    Log.e("NetworkServiceAdapter", "Error fetching albums", error)
                    onError(error)
                })
        )
    }

    fun createAlbum(
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String,
        onComplete: () -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val jsonBody = JSONObject().apply {
            put("name", name)
            put("cover", cover)
            put("releaseDate", releaseDate)
            put("description", description)
            put("genre", genre)
            put("recordLabel", recordLabel)
        }
        val request = JsonObjectRequest(
            Request.Method.POST,
            BASE_URL + "albums",
            jsonBody,
            { _ -> onComplete() },
            { error ->
                val responseData = error.networkResponse?.data
                if (responseData == null) {
                    onError(error)
                } else {
                    val jsonResponse = JSONObject(String(responseData, Charsets.UTF_8))
                    val stringError = jsonResponse.optString("message", "unknown error")
                    Log.d("Network-CreateAlbum", stringError)
                    onError(VolleyError(stringError))
                }
            }
        )
        requestQueue.add(request)
    }

    fun getCollectors(
        onComplete: (resp: List<Collector>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest(
                "collectors",
                { response ->
                    try {
                        val resp = JSONArray(response)
                        val list = mutableListOf<Collector>()
                        for (i in 0 until resp.length()) {
                            val item = resp.getJSONObject(i)
                            list.add(
                                Collector(
                                    id = item.getInt("id"),
                                    name = item.getString("name"),
                                    telephone = item.getString("telephone"),
                                    email = item.getString("email")
                                )
                            )
                        }
                        onComplete(list)
                    } catch (e: Exception) {
                        Log.e("NetworkServiceAdapter", "Error parsing collectors", e)
                        onError(VolleyError(e))
                    }
                },
                { error ->
                    Log.e("NetworkServiceAdapter", "Error fetching collectors", error)
                    onError(error)
                })
        )
    }

    fun getCollectorDetail(
        collectorId: Int,
        onComplete: (CollectorDetail) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest(
            "collectors/$collectorId",
            { response ->
                val item = JSONObject(response)
                // Parse básico del Collector
                val collector = Collector(
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    telephone = item.getString("telephone"),
                    email = item.getString("email")
                )
                // Parse de Comments
                val commentsJson = item.getJSONArray("comments")
                val comments = mutableListOf<Comment>()
                for (i in 0 until commentsJson.length()) {
                    val commentObj = commentsJson.getJSONObject(i)
                    comments.add(
                        Comment(
                            id = commentObj.getInt("id"),
                            description = commentObj.getString("description"),
                            rating = commentObj.getInt("rating")
                        )
                    )
                }

                // Parse de Favorite Performers
                val performersJson = item.getJSONArray("favoritePerformers")
                val performers = mutableListOf<Performer>()
                for (i in 0 until performersJson.length()) {
                    val performerObj = performersJson.getJSONObject(i)
                    performers.add(
                        Performer(
                            id = performerObj.getInt("id"),
                            name = performerObj.optString("name"),
                            image = performerObj.optString("image"),
                            description = performerObj.optString("description"),
                            birthDate = performerObj.optString("birthDate")
                        )
                    )
                }

                // Parse de CollectorAlbums
                val albumsJson = item.getJSONArray("collectorAlbums")
                val collectorAlbums = mutableListOf<CollectorAlbum>()
                val albumCount = albumsJson.length()
                if (albumCount == 0) {
                    onComplete(
                        CollectorDetail(
                            id = collector.id,
                            name = collector.name,
                            telephone = collector.telephone,
                            email = collector.email,
                            comments = comments,
                            favoritePerformers = performers,
                            collectorAlbums = collectorAlbums
                        )
                    )
                    return@getRequest
                }

                var loadedCount = 0
                for (i in 0 until albumCount) {
                    val albumObj = albumsJson.getJSONObject(i)
                    val albumId = albumObj.getInt("id")
                    val price = albumObj.getInt("price")
                    val status = albumObj.getString("status")

                    // Fetch full album info
                    requestQueue.add(getRequest(
                        "albums/$albumId",
                        { albumResponse ->
                            try {
                                val albumJson = JSONObject(albumResponse)
                                // You can parse more fields if needed from albumJson
                                val album = Album(
                                    id = albumJson.getInt("id"),
                                    name = albumJson.optString("name"),
                                    cover = albumJson.optString("cover"),
                                    releaseDate = albumJson.optString("releaseDate"),
                                    description = albumJson.optString("description"),
                                    genre = albumJson.optString("genre"),
                                    recordLabel = albumJson.optString("recordLabel")
                                )
                                val fullAlbum = CollectorAlbum(
                                    id = albumId,
                                    price = price,
                                    status = status,
                                    album = album
                                )
                                collectorAlbums.add(fullAlbum)
                            } catch (e: Exception) {
                                Log.e("NetworkServiceAdapter", "Error parsing album $albumId", e)
                            }

                            loadedCount++
                            if (loadedCount == albumCount) {
                                // All albums fetched
                                onComplete(
                                    CollectorDetail(
                                        id = collector.id,
                                        name = collector.name,
                                        telephone = collector.telephone,
                                        email = collector.email,
                                        comments = comments,
                                        favoritePerformers = performers,
                                        collectorAlbums = collectorAlbums
                                    )
                                )
                            }
                        },
                        { error ->
                            Log.e("NetworkServiceAdapter", "Error fetching album $albumId", error)
                            loadedCount++
                            if (loadedCount == albumCount) {
                                onComplete(
                                    CollectorDetail(
                                        id = collector.id,
                                        name = collector.name,
                                        telephone = collector.telephone,
                                        email = collector.email,
                                        comments = comments,
                                        favoritePerformers = performers,
                                        collectorAlbums = collectorAlbums
                                    )
                                )
                            }
                        }
                    ))
                }

            },
            { error ->
                Log.e("NetworkServiceAdapter", "Error fetching collector detail", error)
                onError(error)
            }
        ))
    }
}