package tk.pokatomnik.scpfoundation.di.http

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import tk.pokatomnik.scpfoundation.domain.PageInfo
import tk.pokatomnik.scpfoundation.domain.PagedResponse

interface PagesService {
    @GET("fragment%3Atop-rated-by-year-0/p/{pageNumber}")
    fun listPages(@Path("pageNumber") pageNumber: Int): Call<PagedResponse>
}

class HttpClient {
    private val pagesRetrofitClient = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(PagesConverterFactory())
        .build()

    val pagesService = PagesServiceCachingDecorator(
        pagesRetrofitClient.create(PagesService::class.java)
    )

    private companion object {
        private const val BASE_URL = "http://scp-ru.wikidot.com/"
    }
}