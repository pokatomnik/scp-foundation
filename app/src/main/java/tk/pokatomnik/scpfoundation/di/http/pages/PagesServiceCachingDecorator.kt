package tk.pokatomnik.scpfoundation.di.http

import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.domain.PagedResponse
import java.util.concurrent.ConcurrentHashMap

private data class OnResponseCallbackArgs(
    val call: Call<PagedResponse>,
    val response: Response<PagedResponse>
)

class PagesServiceCachingDecorator(private val pagesService: PagesService) : PagesService {
    private val pagesCache: MutableMap<Int, OnResponseCallbackArgs> = ConcurrentHashMap()

    fun listPagesForce(pageNumber: Int): Call<PagedResponse> {
        val call = pagesService.listPages(pageNumber)
        return object : Call<PagedResponse> {
            override fun clone(): Call<PagedResponse> {
                return call.clone()
            }

            override fun execute(): Response<PagedResponse> {
                throw Throwable("Don't even try!")
            }

            override fun enqueue(callback: Callback<PagedResponse>) {
                call.enqueue(object : Callback<PagedResponse> {
                    override fun onResponse(
                        call: Call<PagedResponse>,
                        response: Response<PagedResponse>
                    ) {
                        pagesCache[pageNumber] = OnResponseCallbackArgs(call, response)
                        callback.onResponse(call, response)
                    }

                    override fun onFailure(call: Call<PagedResponse>, t: Throwable) {
                        return callback.onFailure(call, t)
                    }
                })
            }

            override fun isExecuted(): Boolean {
                return call.isExecuted
            }

            override fun cancel() {
                call.cancel()
            }

            override fun isCanceled(): Boolean {
                return call.isCanceled
            }

            override fun request(): Request {
                return call.request()
            }
        }
    }

    override fun listPages(pageNumber: Int): Call<PagedResponse> {
        val call = pagesService.listPages(pageNumber)
        return object : Call<PagedResponse> {
            override fun clone(): Call<PagedResponse> {
                return call.clone()
            }

            override fun execute(): Response<PagedResponse> {
                throw Throwable("Don't even try!")
            }

            override fun enqueue(callback: Callback<PagedResponse>) {
                val onResponseArgs = pagesCache[pageNumber]
                if (onResponseArgs != null) {
                    callback.onResponse(onResponseArgs.call, onResponseArgs.response)
                    return
                }
                call.enqueue(object : Callback<PagedResponse> {
                    override fun onResponse(
                        call: Call<PagedResponse>,
                        response: Response<PagedResponse>
                    ) {
                        pagesCache[pageNumber] = OnResponseCallbackArgs(call, response)
                        callback.onResponse(call, response)
                    }

                    override fun onFailure(call: Call<PagedResponse>, t: Throwable) {
                        return callback.onFailure(call, t)
                    }
                })
            }

            override fun isExecuted(): Boolean {
                return call.isExecuted
            }

            override fun cancel() {
                call.cancel()
            }

            override fun isCanceled(): Boolean {
                return call.isCanceled
            }

            override fun request(): Request {
                return call.request()
            }
        }
    }
}