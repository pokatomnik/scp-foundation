package tk.pokatomnik.scpfoundation.di.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tk.pokatomnik.scpfoundation.domain.Configuration
import tk.pokatomnik.scpfoundation.domain.PageByTags
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.domain.Tag

interface ApiServiceAllDocuments {
    @GET("api")
    fun getAllDocumentsByPageNumber(@Query("p") p: String): Call<PagedResponse>
}

interface ApiServiceRecentDocuments {
    @GET("api")
    fun getRecentDocumentsByPageNumber(@Query("p") p: String): Call<PagedResponse>
}

interface ApiServiceTags {
    @GET("api")
    fun getAllTags(@Query("p") p: String): Call<List<Tag>>
}

interface ApiServiceDocumentsByTags {
    @GET("api")
    fun getDocumentsByTags(@Query("p") p: String): Call<List<PageByTags>>
}

interface ApiServiceConfiguration {
    @GET("api")
    fun getConfiguration(@Query("p") p: String): Call<Configuration>
}

interface ApiService :
    ApiServiceAllDocuments,
    ApiServiceRecentDocuments,
    ApiServiceTags,
    ApiServiceDocumentsByTags,
    ApiServiceConfiguration