package com.alpha.books_explorer.data.remote

import androidx.room.util.query
import com.google.common.truth.Truth.assertThat
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var api: BookApiService

    @Before fun setup() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApiService::class.java)
    }

    @After fun tearDown() { server.shutdown() }

    @Test
    fun `searchBooks returns items`() = kotlinx.coroutines.test.runTest {
        val body = """
          { "items": [
            { "id": "id1", "volumeInfo": { "title": "T1", "authors": ["A1"] } },
            { "id": "id2", "volumeInfo": { "title": "T2", "authors": ["A2"] } }
          ]}
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val resp = api.searchBooks(query = "android", startIndex = 0, maxResults = 20)

        assertThat(resp.items).isNotNull()
        assertThat(resp.items!!.size).isEqualTo(2)
        assertThat(resp.items[0].id).isEqualTo("id1")
    }

    @Test
    fun `getBookById returns item`() = kotlinx.coroutines.test.runTest {
        val body = """
            { "id": "id1", "volumeInfo": { "title": "T1", "authors": ["A1"] } }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val resp = api.getBookById("id1")

        assertThat(resp).isNotNull()
        assertThat(resp.id).isEqualTo("id1")
        assertThat(resp.volumeInfo.title).isEqualTo("T1")
    }
}
