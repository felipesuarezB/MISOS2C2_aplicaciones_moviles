package com.example.viniloapp.network

import android.util.Log
import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.AlbumDetail
import com.example.viniloapp.models.Track
import org.json.JSONArray
import org.json.JSONObject

class AlbumService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {


    suspend fun getAlbums(): List<Album> {
        Log.d("AlbumService", "Parsing")
        val resp = JSONArray(networkServiceAdapter.get("albums"))
        val list = mutableListOf<Album>()

        for (i in 0 until resp.length()) {
            list.add(
                Album(
                    id = resp.getJSONObject(i).getInt("id"),
                    name = resp.getJSONObject(i).getString("name"),
                    cover = resp.getJSONObject(i).getString("cover"),
                    recordLabel = resp.getJSONObject(i).getString("recordLabel"),
                    releaseDate = resp.getJSONObject(i).getString("releaseDate"),
                    genre = resp.getJSONObject(i).getString("genre"),
                    description = resp.getJSONObject(i).getString("description")
                )
            )
        }
        return list
    }

    suspend fun getAlbumDetail(albumId: Int):AlbumDetail {
        val albumJson = JSONObject(networkServiceAdapter.get("albums/$albumId"))
        val tracksArray = albumJson.getJSONArray("tracks")
        val tracks = mutableListOf<Track>()

        for (i in 0 until tracksArray.length()) {
            Log.d("NetworkServiceAdapter", "Processing track: ${tracksArray.getJSONObject(i).getString("name")}")
            tracks.add(
                Track(
                    id = tracksArray.getJSONObject(i).getInt("id"),
                    name = tracksArray.getJSONObject(i).getString("name"),
                    duration = tracksArray.getJSONObject(i).getString("duration")
                )
            )
        }

        Log.d("NetworkServiceAdapter", "Total tracks processed: ${tracks.size}")

        return AlbumDetail(
            id = albumJson.getInt("id"),
            name = albumJson.getString("name"),
            cover = albumJson.getString("cover"),
            releaseDate = albumJson.getString("releaseDate"),
            description = albumJson.getString("description"),
            genre = albumJson.getString("genre"),
            recordLabel = albumJson.getString("recordLabel"),
            tracks = tracks
        )
    }

    suspend fun createAlbum(jsonBody: JSONObject) {
        networkServiceAdapter.post("albums", jsonBody)
    }

} 