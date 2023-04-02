package dev.joshhalvorson.data

import dev.joshhalvorson.data.gallery.AstroData
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadApi {
    @Streaming
    @GET
    suspend fun getData(@Url fileUrl: String): Response<AstroData>
}