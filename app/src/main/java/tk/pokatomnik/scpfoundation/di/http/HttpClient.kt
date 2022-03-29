package tk.pokatomnik.scpfoundation.di.http

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface PagesService {
    @GET("fragment%3Atop-rated-by-year-0/p/{pageNumber}")
    fun listPages(@Path ("pageNumber") pageNumber: Int): Call<String>
}

class HttpClient {
    private val retrofit = Retrofit
        .Builder()
        .baseUrl("http://scp-ru.wikidot.com/")
        .addConverterFactory(PagesConverterFactory())
        .build()

    val pagesService: PagesService = retrofit.create(PagesService::class.java)
}