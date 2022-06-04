package tk.pokatomnik.scpfoundation.di.http

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tk.pokatomnik.scpfoundation.domain.Configuration
import tk.pokatomnik.scpfoundation.domain.PageByTags
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import tk.pokatomnik.scpfoundation.domain.Tag
import tk.pokatomnik.scpfoundation.utils.joinURLParts
import java.util.concurrent.TimeUnit

class HttpClient {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofitClient = Retrofit
        .Builder()
        .baseUrl("https://scp-reader-api.vercel.app/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val httpServiceAllDocuments: ApiServiceAllDocuments = retrofitClient.create(
        ApiServiceAllDocuments::class.java
    )
    private val httpServiceRecentDocuments: ApiServiceRecentDocuments = retrofitClient.create(
        ApiServiceRecentDocuments::class.java
    )
    private val httpServiceTags: ApiServiceTags = retrofitClient.create(
        ApiServiceTags::class.java
    )
    private val httpServiceDocumentsByTags: ApiServiceDocumentsByTags = retrofitClient.create(
        ApiServiceDocumentsByTags::class.java
    )
    private val httpServiceConfiguration: ApiServiceConfiguration = retrofitClient.create(
        ApiServiceConfiguration::class.java
    )

    private val allPagesService = CachingDecorator(
        object : DataFetchingService<Int, PagedResponse> {
            private fun getUrl(page: Int): String {
                return joinURLParts("v1", "documents", "all", page.toString())
            }
            override fun getData(params: Int): Call<PagedResponse> {
                return httpServiceAllDocuments.getAllDocumentsByPageNumber(getUrl(params))
            }
            override fun serializeParams(params: Int): String {
                return params.toString()
            }
        }
    )

    fun getAllDocumentsByPageNumber(pageNumber: Int): Call<PagedResponse> {
        return allPagesService.getData(pageNumber)
    }

    fun getAllDocumentsByPageNumberForce(pageNumber: Int): Call<PagedResponse> {
        return allPagesService.getDataForce(pageNumber)
    }

    private val recentPagesService = CachingDecorator(
        object : DataFetchingService<Int, PagedResponse> {
            private fun getUrl(page: Int): String {
                return joinURLParts("v1","documents", "recent", page.toString())
            }

            override fun getData(params: Int): Call<PagedResponse> {
                return httpServiceRecentDocuments.getRecentDocumentsByPageNumber(getUrl(params))
            }

            override fun serializeParams(params: Int): String {
                return params.toString()
            }
        }
    )

    fun getRecentDocumentsByPageNumber(pageNumber: Int): Call<PagedResponse> {
        return recentPagesService.getData(pageNumber)
    }

    fun getRecentDocumentsByPageNumberForce(pageNumber: Int): Call<PagedResponse> {
        return recentPagesService.getDataForce(pageNumber)
    }

    private val tagsService = CachingDecorator(
        object : DataFetchingService<Unit, List<Tag>> {
            private fun getUrl(): String {
                return joinURLParts("v1", "tags")
            }
            override fun getData(params: Unit): Call<List<Tag>> {
                return httpServiceTags.getAllTags(getUrl())
            }

            override fun serializeParams(params: Unit): String {
                return "SINGLE"
            }
        }
    )

    fun getAllTags(): Call<List<Tag>> {
        return tagsService.getData(Unit)
    }

    fun getAllTagsForce(): Call<List<Tag>> {
        return tagsService.getDataForce(Unit)
    }

    private val documentsByTagsService = CachingDecorator(
        object : DataFetchingService<Array<String>, List<PageByTags>> {
            private fun joinTags(tags: Array<String>): String {
                return tags.joinToString("|")
            }

            private fun getUrl(tags: Array<String>): String {
                val tagsJoined = joinTags(tags)
                return joinURLParts("v1", "documents", "tags", tagsJoined)
            }

            override fun getData(params: Array<String>): Call<List<PageByTags>> {
                return httpServiceDocumentsByTags.getDocumentsByTags(getUrl(params))
            }
            override fun serializeParams(params: Array<String>): String {
                return joinTags(params)
            }
        }
    )

    fun getDocumentsByTags(tags: Array<String>): Call<List<PageByTags>> {
        return documentsByTagsService.getData(tags)
    }

    fun getDocumentsByTagsForce(tags: Array<String>): Call<List<PageByTags>> {
        return documentsByTagsService.getDataForce(tags)
    }

    private val configurationService = CachingDecorator(
        object : DataFetchingService<Unit, Configuration> {
            private fun getUrl(): String {
                return joinURLParts("v1", "configuration")
            }
            override fun getData(params: Unit): Call<Configuration> {
                return httpServiceConfiguration.getConfiguration(getUrl())
            }
            override fun serializeParams(params: Unit): String {
                return "SINGLE"
            }
        }
    )

    fun getConfiguration(): Call<Configuration> {
        return configurationService.getData(Unit)
    }

    fun getConfigurationForce(): Call<Configuration> {
        return configurationService.getDataForce(Unit)
    }
}