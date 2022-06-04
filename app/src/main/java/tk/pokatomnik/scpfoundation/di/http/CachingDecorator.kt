package tk.pokatomnik.scpfoundation.di.http

import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap

private data class OnResponseCallbackArgs<T : Any>(
    val call: Call<T>,
    val response: Response<T>
)

interface DataFetchingService<P : Any, R: Any> {
    fun getData(params: P) : Call<R>
    fun serializeParams(params: P): String
}

interface DataFetchingCachingService<P : Any, R : Any> : DataFetchingService<P, R> {
    fun getDataForce(params: P): Call<R>
}

class CachingDecorator<P: Any, R : Any>(
    private val dataFetchingService: DataFetchingService<P, R>,
) : DataFetchingCachingService<P, R> {
    private val cache: MutableMap<String, OnResponseCallbackArgs<R>> = ConcurrentHashMap()

    override fun getDataForce(params: P): Call<R> {
        val call = dataFetchingService.getData(params)
        return object : Call<R> {
            override fun clone(): Call<R> {
                return call.clone()
            }

            override fun execute(): Response<R> {
                throw Throwable("Don't even try!")
            }

            override fun enqueue(callback: Callback<R>) {
                call.enqueue(object : Callback<R> {
                    override fun onResponse(
                        call: Call<R>,
                        response: Response<R>
                    ) {
                        cache[serializeParams(params)] = OnResponseCallbackArgs(call, response)
                        callback.onResponse(call, response)
                    }

                    override fun onFailure(call: Call<R>, t: Throwable) {
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

    override fun getData(params: P): Call<R> {
        val call = dataFetchingService.getData(params)
        return object : Call<R> {
            override fun clone(): Call<R> {
                return call.clone()
            }

            override fun execute(): Response<R> {
                throw Throwable("Don't even try!")
            }

            override fun enqueue(callback: Callback<R>) {
                val onResponseArgs = cache[serializeParams(params)]
                if (onResponseArgs != null) {
                    callback.onResponse(onResponseArgs.call, onResponseArgs.response)
                    return
                }
                call.enqueue(object : Callback<R> {
                    override fun onResponse(
                        call: Call<R>,
                        response: Response<R>
                    ) {
                        cache[serializeParams(params)] = OnResponseCallbackArgs(call, response)
                        callback.onResponse(call, response)
                    }

                    override fun onFailure(call: Call<R>, t: Throwable) {
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

    override fun serializeParams(params: P): String {
        return dataFetchingService.serializeParams(params)
    }
}