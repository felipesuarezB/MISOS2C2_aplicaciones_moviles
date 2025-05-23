package com.example.viniloapp.network

import org.junit.Before
import org.mockito.kotlin.mock

class ArtistServiceUnitTest {

    private lateinit var artistService: ArtistService
    private val mockNetworkAdapter: NetworkServiceAdapter = mock()

    @Before
    fun setup() {
        artistService = ArtistService(mockNetworkAdapter)
    }
}