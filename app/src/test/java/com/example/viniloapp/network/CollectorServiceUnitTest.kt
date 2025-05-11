package com.example.viniloapp.network

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CollectorServiceUnitTest {

    private lateinit var collectorService: CollectorService
    private val mockNetworkAdapter: NetworkServiceAdapter = mock()

    @Before
    fun setup() {
        collectorService = CollectorService(mockNetworkAdapter)
    }

    @Test
    fun `getCollectors should return list of collectors`() = runTest {
        val mockCollectorsJson = """
            [
                {
                    "id": 1,
                    "name": "Carlos Santana",
                    "telephone": "123456789",
                    "email": "carlos@example.com"
                },
                {
                    "id": 2,
                    "name": "Rosario Flores",
                    "telephone": "987654321",
                    "email": "rosario@example.com"
                }
            ]
        """.trimIndent()
        whenever(mockNetworkAdapter.get("collectors")).thenReturn(mockCollectorsJson)

        val result = collectorService.getCollectors()
        assertEquals(2, result.size)
        assertEquals("Carlos Santana", result[0].name)
        assertEquals("rosario@example.com", result[1].email)
    }

    @Test
    fun `getCollectors should throw exception when error occurs`() = runTest {
        whenever(mockNetworkAdapter.get("collectors")).thenThrow(RuntimeException("Simulated network error"))

        val exception = assertFailsWith<RuntimeException> {
            collectorService.getCollectors()
        }
        assertEquals("Simulated network error", exception.message)
    }

    @Test
    fun `getCollectorDetail should throw exception when error occurs`() = runTest {
        whenever(mockNetworkAdapter.get("collectors/1")).thenThrow(RuntimeException("Detail error"))

        val exception = assertFailsWith<RuntimeException> {
            collectorService.getCollectorDetail(1)
        }
        assertEquals("Detail error", exception.message)
    }
}