package com.example.viniloapp.network

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AlbumServiceUnitTest {

    private lateinit var albumService: AlbumService
    private val mockNetworkAdapter: NetworkServiceAdapter = mock()

    @Before
    fun setup() {
        albumService = AlbumService(mockNetworkAdapter)
    }

    @Test
    fun `getAlbums should return list of albums when successful`() = runTest {
        val mockAlbumsJson = """
            [
                {
                    "id": 1,
                    "name": "Thriller",
                    "cover": "https://example.com/thriller.jpg",
                    "releaseDate": "1982-11-30",
                    "description": "One of the best-selling albums of all time.",
                    "genre": "Pop",
                    "recordLabel": "Epic"
                },
                {
                    "id": 2,
                    "name": "Back in Black",
                    "cover": "https://example.com/backinblack.jpg",
                    "releaseDate": "1980-07-25",
                    "description": "AC/DC's best-selling album.",
                    "genre": "Rock",
                    "recordLabel": "Atlantic"
                }
            ]
        """.trimIndent()

        whenever(mockNetworkAdapter.get("albums")).thenReturn(mockAlbumsJson)

        val result = albumService.getAlbums()
        assertEquals(2, result.size)
        assertEquals("Thriller", result[0].name)
        assertEquals("Back in Black", result[1].name)
    }

    @Test
    fun `getAlbums should throw exception when error occurs`() = runTest {
        whenever(mockNetworkAdapter.get("albums")).thenThrow(RuntimeException("Network error"))

        val exception = assertFailsWith<RuntimeException> {
            albumService.getAlbums()
        }
        assertEquals("Network error", exception.message)
    }

    @Test
    fun `createAlbum should call post with correct parameters`() = runTest {
        val mockJsonBody = mock<org.json.JSONObject>()
        albumService.createAlbum(mockJsonBody)
        verify(mockNetworkAdapter).post(eq("albums"), eq(mockJsonBody))
    }
}