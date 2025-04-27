package com.example.viniloapp.network

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import com.android.volley.VolleyError
import com.example.viniloapp.models.Collector

class CollectorServiceUnitTest {

    private lateinit var collectorService: CollectorService
    private val mockNetworkAdapter: NetworkServiceAdapter = mock()

    @Before
    fun setup() {
        collectorService = CollectorService(mockNetworkAdapter)
    }

    @Test
    fun `getCollectors should return list of collectors`() = runTest {
        val fakeCollectors = listOf(
            Collector(
                id = 1,
                name = "Carlos Santana",
                telephone = "123456789",
                email = "carlos@example.com"
            ),
            Collector(
                id = 2,
                name = "Rosario Flores",
                telephone = "987654321",
                email = "rosario@example.com"
            )
        )

        whenever(mockNetworkAdapter.getCollectors(any(), any())).thenAnswer { invocation ->
            val onSuccess = invocation.getArgument<(List<Collector>) -> Unit>(0)
            onSuccess(fakeCollectors)
        }

        val result = collectorService.getCollectors()

        assertEquals(2, result.size)
        assertEquals("Carlos Santana", result[0].name)
        assertEquals("rosario@example.com", result[1].email)
    }

    @Test
    fun `getCollectors should throw exception when error occurs`() = runTest {
        val volleyError = VolleyError("Simulated network error")

        whenever(mockNetworkAdapter.getCollectors(any(), any())).thenAnswer { invocation ->
            val onError = invocation.getArgument<(VolleyError) -> Unit>(1)
            onError(volleyError)
        }

        val exception = assertFailsWith<Exception> {
            collectorService.getCollectors()
        }

        assertEquals("Simulated network error", exception.message)
    }

    @Test
    fun `getCollectorDetail should throw exception when error occurs`() = runTest {
        val error = VolleyError("Detail error")

        whenever(mockNetworkAdapter.getCollectorDetail(any(), any(), any())).thenAnswer {
            val onError = it.getArgument<(VolleyError) -> Unit>(2)
            onError(error)
        }

        val exception = assertFailsWith<VolleyError> {
            collectorService.getCollectorDetail(1)
        }

        assertEquals("Detail error", exception.message)
    }
}