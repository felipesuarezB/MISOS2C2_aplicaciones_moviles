package com.example.viniloapp.network

import com.example.viniloapp.models.Album
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumServiceUnitTest {

    private lateinit var albumService: AlbumService
    private val mockNetworkAdapter: NetworkServiceAdapter = mock()

    @Before
    fun setup() {
        NetworkServiceAdapter.instance = mockNetworkAdapter
        albumService = AlbumService()
    }

    @Test
    fun `getAlbums should return list of albums when successful`() = runTest {
        val mockAlbums = listOf(
            Album(
                id = 1,
                name = "Thriller",
                cover = "https://example.com/thriller.jpg",
                releaseDate = "1982-11-30",
                description = "One of the best-selling albums of all time.",
                genre = "Pop",
                recordLabel = "Epic"
            ),
            Album(
                id = 2,
                name = "Back in Black",
                cover = "https://example.com/backinblack.jpg",
                releaseDate = "1980-07-25",
                description = "AC/DC's best-selling album.",
                genre = "Rock",
                recordLabel = "Atlantic"
            )
        )

        whenever(mockNetworkAdapter.getAlbums(any(), any())).thenAnswer {
            val onComplete = it.getArgument<(List<Album>) -> Unit>(0)
            onComplete(mockAlbums)
        }

        val result = albumService.getAlbums()
        assertEquals(2, result.size)
        assertEquals("Album 1", result[0].name)
    }

    @Test
    fun `getAlbums should throw exception when error occurs`() = runTest {
        whenever(mockNetworkAdapter.getAlbums(any(), any())).thenAnswer {
            val onError = it.getArgument<(Exception) -> Unit>(1)
            onError(Exception("Network error"))
        }

        val exception = assertFailsWith<Exception> {
            albumService.getAlbums()
        }

        assertEquals("Network error", exception.message)
    }
}