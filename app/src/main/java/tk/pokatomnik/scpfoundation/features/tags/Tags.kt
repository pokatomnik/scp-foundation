package tk.pokatomnik.scpfoundation.features.tags

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.pokatomnik.scpfoundation.components.ChipComponent
import tk.pokatomnik.scpfoundation.components.ChipComponentProps
import tk.pokatomnik.scpfoundation.di.http.rememberHttpClient
import tk.pokatomnik.scpfoundation.features.pages.PageTitle

@Composable
fun Tags() {
    val scrollRefreshState = rememberSwipeRefreshState(false)
    val httpClient = rememberHttpClient()

    val tags = remember { mutableStateListOf<String>() }

    DisposableEffect(Unit) {
        val request = httpClient.tagsService.listTags()
        request.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                tags.clear()
                val tagsList = response.body() ?: listOf()
                tags.addAll(tagsList)
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                val error = t
            }

        })
        onDispose {
            request.cancel()
        }
    }

    SwipeRefresh(
        state = scrollRefreshState,
        onRefresh = { /*TODO*/ },
        modifier = Modifier.fillMaxSize(),
        swipeEnabled = true,
        indicatorPadding = PaddingValues(top = 80.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .requiredHeight(64.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PageTitle(title = "Поиск тегов")
            }
            FlowRow(modifier = Modifier.padding(vertical = 10.dp)) {
                for (tag in tags) {
                    Column(modifier = Modifier.padding(5.dp)) {
                        ChipComponent(
                            props = ChipComponentProps(
                                text = tag
                            )
                        )
                    }
                }
            }
        }
    }
}