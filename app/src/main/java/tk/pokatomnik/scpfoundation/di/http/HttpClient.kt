package tk.pokatomnik.scpfoundation.di.http

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tk.pokatomnik.scpfoundation.di.http.converters.LatestPagesConverterFactory
import tk.pokatomnik.scpfoundation.di.http.converters.PagesConverterFactory
import tk.pokatomnik.scpfoundation.domain.PageByTagsImpl
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import java.util.concurrent.TimeUnit

interface PagesService {
    @GET("fragment%3Atop-rated-by-year-0/p/{pageNumber}")
    fun listPages(@Path("pageNumber") pageNumber: Int): Call<PagedResponse>
}

interface TagsService {
    @GET("_api/wikidot_tags_search/list?wiki=scp-ru")
    fun listTags(): Call<List<String>>
}

interface PagesByTagsService {
    @GET("_api/wikidot_tags_search/find?wiki=scp-ru")
    fun listPagesByTags(@Query("tag") vararg tag: String): Call<List<PageByTagsImpl>>
}

interface LatestPagesService {
    @GET("most-recently-created/p/{pageNumber}")
    fun listPages(@Path("pageNumber") pageNumber: Int): Call<PagedResponse>
}

class HttpClient {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val pagesRetrofitClient = Retrofit
        .Builder()
        .baseUrl(WEBSITE_URL)
        .client(okHttpClient)
        .addConverterFactory(PagesConverterFactory())
        .build()

    private val latestPagesRetrofitClient = Retrofit
        .Builder()
        .baseUrl(WEBSITE_URL)
        .client(okHttpClient)
        .addConverterFactory(LatestPagesConverterFactory())
        .build()

    private val tagsRetrofitClient = Retrofit
        .Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val pagesByTagsRetrofitClient = Retrofit
        .Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val pagesService = PagesServiceCachingDecorator(
        object : DataFetchingService<Int, PagedResponse> {
            private val client = pagesRetrofitClient.create(PagesService::class.java)

            override fun getData(params: Int): Call<PagedResponse> {
                return client.listPages(params)
            }

            override fun serializeParams(params: Int): String {
                return params.toString()
            }
        }
    )

    val latestPagesService = PagesServiceCachingDecorator(
        object : DataFetchingService<Int, PagedResponse> {
            private val client = latestPagesRetrofitClient.create(LatestPagesService::class.java)

            override fun getData(params: Int): Call<PagedResponse> {
                return client.listPages(params)
            }

            override fun serializeParams(params: Int): String {
                return params.toString()
            }
        }
    )

    val tagsService = PagesServiceCachingDecorator(
        object : DataFetchingService<Unit, List<String>> {
            private val client = tagsRetrofitClient.create(TagsService::class.java)

            override fun getData(params: Unit): Call<List<String>> {
                return client.listTags()
            }

            override fun serializeParams(params: Unit): String {
                return "SINGLE"
            }
        }
    )

    val pagesByTagsService = PagesServiceCachingDecorator(
        object : DataFetchingService<Array<String>, List<PageByTagsImpl>> {
            private val client = pagesByTagsRetrofitClient.create(PagesByTagsService::class.java)

            override fun getData(params: Array<String>): Call<List<PageByTagsImpl>> {
                return client.listPagesByTags(*params)
            }

            override fun serializeParams(params: Array<String>): String {
                return params.joinToString("|")
            }
        }
    )

    companion object {
        const val WEBSITE_URL = "http://scp-ru.wikidot.com/"
        private const val API_URL = "https://m.scpfoundation.net/"
    }
}