package com.example.viniloapp.network

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ArtistServiceUnitTest {

    private lateinit var artistService: ArtistService
    private val mockNetworkAdapter: NetworkServiceAdapter = mock()

    @Before
    fun setup() {
        artistService = ArtistService(mockNetworkAdapter)
    }

    @Test
    fun `getArtists should return list of artists when successful`() = runTest {
        val mockArtistsJson = """
            [
                {
                    "id": 1,
                    "name": "Michael Jackson",
                    "image": "https://example.com/mj.jpg",
                    "description": "King of Pop"
                },
                {
                    "id": 2,
                    "name": "AC/DC",
                    "image": "https://example.com/acdc.jpg",
                    "description": "Legendary rock band"
                }
            ]
        """.trimIndent()

        whenever(mockNetworkAdapter.get("artists")).thenReturn(mockArtistsJson)

        val result = artistService.getArtists()
        assertEquals(2, result.size)
        assertEquals("Michael Jackson", result[0].name)
        assertEquals("AC/DC", result[1].name)
    }

    @Test
    fun `getArtists should throw exception when error occurs`() = runTest {
        whenever(mockNetworkAdapter.get("artists")).thenThrow(RuntimeException("Network error"))

        val exception = assertFailsWith<RuntimeException> {
            artistService.getArtists()
        }
        assertEquals("Network error", exception.message)
    }

    @Test
    fun `createArtist should call post with correct parameters`() = runTest {
        val mockJsonBody = mock<org.json.JSONObject>()
        artistService.createArtist(mockJsonBody)
        verify(mockNetworkAdapter).post(eq("artists"), eq(mockJsonBody))
    }
}
