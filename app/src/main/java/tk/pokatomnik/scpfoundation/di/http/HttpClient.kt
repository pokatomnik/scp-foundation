package tk.pokatomnik.scpfoundation.di.http

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

interface PagesService {
    @GET("fragment%3Atop-rated-by-year-0/p/{pageNumber}")
    fun listPages(@Path ("pageNumber") pageNumber: Int): Call<String>
}

class PagesConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, String> {
        return Converter { value ->
            "Hello!"
        }
    }
}

class HttpClient {
    private val retrofit = Retrofit
        .Builder()
        .baseUrl("http://scp-ru.wikidot.com/")
        .addConverterFactory(PagesConverterFactory())
        .build()

    val pagesService: PagesService = retrofit.create(PagesService::class.java)
}