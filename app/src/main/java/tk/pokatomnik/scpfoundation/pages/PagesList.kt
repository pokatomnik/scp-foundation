package tk.pokatomnik.scpfoundation.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.components.LazyList

val testList = arrayListOf<PageInfo>(
    PageInfoImpl(
        name = "First",
        url = "https://example.com/1",
        rating = 100,
        author = "John Doe",
        date = "Today"
    ),
    PageInfoImpl(
        name = "Second",
        url = "https://example.com/2",
        rating = 100,
        author = "Jane Doe",
        date = "Today"
    ),
    PageInfoImpl(
        name = "Third",
        url = "https://example.com/3",
        rating = 100,
        author = "John Doe",
        date = "Today"
    )
)

@Composable
fun PagesList(
    onSelectURL: (url: String) -> Unit
) {
    val pagesViewModel = hiltViewModel<PagesViewModel>()
    val (pages, setPages) = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        pagesViewModel.httpClient.pagesService.listPages(0).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let{
                    val body = response.body()
                    setPages(it)
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                val err = t;
            }
        })
    }
    LazyList(
        list = testList,
        onClick = { onSelectURL(it.url) }
    ) {
        Column {
            Row { Text(it.name) }
            Row { Text(it.author ?: "") }
        }
    }
}