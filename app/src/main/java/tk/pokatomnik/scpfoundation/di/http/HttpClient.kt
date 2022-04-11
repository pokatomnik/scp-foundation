package tk.pokatomnik.scpfoundation.di.http

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import tk.pokatomnik.scpfoundation.di.http.pages.PagesConverterFactory
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

    private val tagsRetrofitService = Retrofit
        .Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val pagesService = PagesServiceCachingDecorator(
        pagesRetrofitClient.create(PagesService::class.java)
    )

    val tagsService = tagsRetrofitService.create(TagsService::class.java)

    private companion object {
        private const val WEBSITE_URL = "http://scp-ru.wikidot.com/"
        private const val API_URL = "https://m.scpfoundation.net/"
    }
}