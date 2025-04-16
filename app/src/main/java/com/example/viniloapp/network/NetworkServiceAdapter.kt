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
                            name = performerObj.getString("name"),
                            image = performerObj.getString("image"),
                            description = performerObj.getString("description"),
                            birthDate = performerObj.getString("birthDate")
                        )
                    )
                }

                // Parse de CollectorAlbums
                val albumsJson = item.getJSONArray("collectorAlbums")
                val collectorAlbums = mutableListOf<CollectorAlbum>()
                for (i in 0 until albumsJson.length()) {
                    val albumObj = albumsJson.getJSONObject(i)

                    collectorAlbums.add(
                        CollectorAlbum(
                            id = albumObj.getInt("id"),
                            price = albumObj.getInt("price"),
                            status = albumObj.getString("status")

                        )
                    )
                }

                val collectorDetail = CollectorDetail(
                    id = collector.id,
                    name = collector.name,
                    telephone = collector.telephone,
                    email = collector.email,
                    comments = comments,
                    favoritePerformers = performers,
                    collectorAlbums = collectorAlbums
                )

                onComplete(collectorDetail)
            },
            { error ->
                Log.e("NetworkServiceAdapter", "Error fetching collector detail", error)
                onError(error)
            }
        ))
    }
}