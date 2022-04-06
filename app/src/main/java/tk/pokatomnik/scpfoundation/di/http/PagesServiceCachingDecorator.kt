package tk.pokatomnik.scpfoundation.di.http

import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.pages.PageInfo
import java.util.concurrent.ConcurrentHashMap

data class OnResponseCallbackArgs(
    val call: Call<List<PageInfo>>,
    val response: Response<List<PageInfo>>
)

class PagesServiceCachingDecorator(private val pagesService: PagesService) : PagesService {
    private val pagesCache: MutableMap<Int, OnResponseCallbackArgs> = ConcurrentHashMap()

    fun listPagesForce(pageNumber: Int): Call<List<PageInfo>> {
        val call = pagesService.listPages(pageNumber)
        return object : Call<List<PageInfo>> {
            override fun clone(): Call<List<PageInfo>> {
                return call.clone()
            }

            override fun execute(): Response<List<PageInfo>> {
                throw Throwable("Don't even try!")
            }

            override fun enqueue(callback: Callback<List<PageInfo>>) {
                call.enqueue(object : Callback<List<PageInfo>> {
                    override fun onResponse(
                        call: Call<List<PageInfo>>,
                        response: Response<List<PageInfo>>
                    ) {
                        pagesCache[pageNumber] = OnResponseCallbackArgs(call, response)
                        callback.onResponse(call, response)
                    }

                    override fun onFailure(call: Call<List<PageInfo>>, t: Throwable) {
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

    override fun listPages(pageNumber: Int): Call<List<PageInfo>> {
        val call = pagesService.listPages(pageNumber)
        return object : Call<List<PageInfo>> {
            override fun clone(): Call<List<PageInfo>> {
                return call.clone()
            }

            override fun execute(): Response<List<PageInfo>> {
                throw Throwable("Don't even try!")
            }

            override fun enqueue(callback: Callback<List<PageInfo>>) {
                val onResponseArgs = pagesCache[pageNumber]
                if (onResponseArgs != null) {
                    callback.onResponse(onResponseArgs.call, onResponseArgs.response)
                    return
                }
                call.enqueue(object : Callback<List<PageInfo>> {
                    override fun onResponse(
                        call: Call<List<PageInfo>>,
                        response: Response<List<PageInfo>>
                    ) {
                        pagesCache[pageNumber] = OnResponseCallbackArgs(call, response)
                        callback.onResponse(call, response)
                    }

                    override fun onFailure(call: Call<List<PageInfo>>, t: Throwable) {
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