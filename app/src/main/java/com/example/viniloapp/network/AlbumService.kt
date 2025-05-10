package com.example.viniloapp.network

import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.AlbumDetail
import com.example.viniloapp.models.Track
import com.example.viniloapp.network.NetworkServiceAdapter.Companion.BASE_URL
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AlbumService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {


    suspend fun getAlbums(): List<Album> {
        val response = networkServiceAdapter.get("albums")
        Log.d("AlbumService", "Parsing")
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
        return list
    }

    suspend fun getAlbumDetail(albumId: Int):AlbumDetail {
        val response = networkServiceAdapter.get("albums/$albumId")
        val albumJson = JSONObject(response)
        val tracksArray = albumJson.getJSONArray("tracks")
        val tracks = mutableListOf<Track>()

        for (i in 0 until tracksArray.length()) {
            val trackJson = tracksArray.getJSONObject(i)
            val track = Track(
                id = trackJson.getInt("id"),
                name = trackJson.getString("name"),
                duration = trackJson.getString("duration")
            )
            Log.d("NetworkServiceAdapter", "Processing track: ${track.name}")
            tracks.add(track)
        }

        Log.d("NetworkServiceAdapter", "Total tracks processed: ${tracks.size}")

        val albumDetail = AlbumDetail(
            id = albumJson.getInt("id"),
            name = albumJson.getString("name"),
            cover = albumJson.getString("cover"),
            releaseDate = albumJson.getString("releaseDate"),
            description = albumJson.getString("description"),
            genre = albumJson.getString("genre"),
            recordLabel = albumJson.getString("recordLabel"),
            tracks = tracks
        )

        return albumDetail
    }

    suspend fun createAlbum(jsonBody: JSONObject) {
        networkServiceAdapter.post("albums", jsonBody)
    }

} 