package com.example.viniloapp.network

import android.util.Log
import com.android.volley.VolleyError
import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.AlbumDetail
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorAlbum
import com.example.viniloapp.models.CollectorDetail
import com.example.viniloapp.models.Comment
import com.example.viniloapp.models.Performer
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CollectorService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {

    suspend fun getCollectors(): List<Collector> {
        val response = networkServiceAdapter.get("collectors")
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
        return list
    }

    suspend fun getCollectorDetail(
        collectorId: Int,
    ):CollectorDetail {
        val response = networkServiceAdapter.get("collectors/$collectorId")
        val item = JSONObject(response)
        val collector = Collector(
            id = item.getInt("id"),
            name = item.getString("name"),
            telephone = item.getString("telephone"),
            email = item.getString("email")
        )

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

        val albumsJson = item.getJSONArray("collectorAlbums")
        val albumCount = albumsJson.length()
        if (albumCount == 0) {
            return CollectorDetail(
                    id = collector.id,
                    name = collector.name,
                    telephone = collector.telephone,
                    email = collector.email,
                    comments = comments,
                    favoritePerformers = performers,
                    collectorAlbums = mutableListOf<CollectorAlbum>()
                )
        }

        val albumsDetails  = (0 until albumCount).map { index ->
            val albumObj = albumsJson.getJSONObject(index)
            val albumId = albumObj.getInt("id")
            val albumResponse = networkServiceAdapter.get("albums/$albumId")
            JSONObject(albumResponse)
        }


        val collectorAlbums = (0 until albumCount).map { index ->
            val albumObj = albumsJson.getJSONObject(index)
            val albumDetailObj = albumsDetails[index]
            val albumId = albumObj.getInt("id")
            val price = albumObj.getInt("price")
            val status = albumObj.getString("status")

            val album = Album(
                id = albumDetailObj.getInt("id"),
                name = albumDetailObj.optString("name"),
                cover = albumDetailObj.optString("cover"),
                releaseDate = albumDetailObj.optString("releaseDate"),
                description = albumDetailObj.optString("description"),
                genre = albumDetailObj.optString("genre"),
                recordLabel = albumDetailObj.optString("recordLabel")
            )

            val fullAlbum = CollectorAlbum(
                id = albumId,
                price = price,
                status = status,
                album = album
            )

            fullAlbum
        }

        return CollectorDetail(
            id = collector.id,
            name = collector.name,
            telephone = collector.telephone,
            email = collector.email,
            comments = comments,
            favoritePerformers = performers,
            collectorAlbums = collectorAlbums
        )
    }
}