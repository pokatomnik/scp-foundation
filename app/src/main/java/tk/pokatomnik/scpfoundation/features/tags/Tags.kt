package tk.pokatomnik.scpfoundation.features.tags

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

fun groupTags(list: Collection<String>): Map<String, List<String>> {
    return list.fold(mutableMapOf<String, MutableList<String>>()) { map, current ->
        if (current.isBlank()) map else {
            map.apply {
                val firstCharKey = current[0].uppercase()
                map[firstCharKey] = (map[firstCharKey] ?: mutableListOf()).apply { add(current) }
            }
        }
    }
}

@Composable
fun Tags(onSelectTags: (tags: Set<String>) -> Unit) {
    val context = LocalContext.current
    val httpClient = rememberHttpClient()

    val (tagsList, setTagsList) = remember { mutableStateOf(setOf<String>()) }
    val (selectedTags, setSelectedTags) = remember { mutableStateOf(setOf<String>()) }
    val heightAnimated by animateDpAsState(targetValue = if (selectedTags.isNotEmpty()) 48.dp else 0.dp)

    val (loadingState, setLoadingState) = remember { mutableStateOf(false) }

    val scrollRefreshState = rememberSwipeRefreshState(loadingState)

    val tagsGrouped = remember(tagsList) { groupTags(tagsList) }

    fun loadTags(force: Boolean): DisposableEffectResult {
        if (loadingState) {
            return object : DisposableEffectResult {
                override fun dispose() {}
            }
        }
        setLoadingState(true)

        val request = if (force) {
            httpClient.tagsService.getDataForce(Unit)
        } else {
            httpClient.tagsService.getData(Unit)
        }

        request.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val pairs = (response.body() ?: listOf()).toSet()
                tagsList.apply {
                    setTagsList(pairs)
                }
                setLoadingState(false)
                setSelectedTags(setOf())
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Невозможно загрузить список тегов, попробуйте позднее",
                    Toast.LENGTH_SHORT
                ).show()
                setLoadingState(false)
            }

        })
        return object : DisposableEffectResult {
            override fun dispose() {
                return request.cancel()
            }
        }
    }

    DisposableEffect(Unit) { loadTags(false) }

    Column(modifier = Modifier.fillMaxSize()) {
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
        Row(
            modifier = Modifier
                .height(heightAnimated)
                .requiredHeight(heightAnimated)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            for (tag in selectedTags) {
                ChipComponent(
                    props = ChipComponentProps(
                        modifier = Modifier.padding(5.dp),
                        maxChars = 50,
                        text = "#$tag",
                        onClick = {
                            setSelectedTags(selectedTags.toMutableSet().apply { remove(tag) })
                            setTagsList(tagsList.toMutableSet().apply { add(tag) })
                        }
                    )
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
        Divider(modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            SwipeRefresh(
                state = scrollRefreshState,
                onRefresh = { loadTags(true) },
                modifier = Modifier.fillMaxHeight(),
                swipeEnabled = !loadingState,
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(tagsGrouped.entries.toList()) {
                        val (key, tagsByKey) = it
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(text = key)
                                Divider(modifier = Modifier.padding(bottom = 10.dp))
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                ) {
                                    for (tag in tagsByKey) {
                                        ChipComponent(
                                            props = ChipComponentProps(
                                                modifier = Modifier.padding(5.dp),
                                                maxChars = 50,
                                                text = "#$tag",
                                                onClick = {
                                                    setSelectedTags(
                                                        selectedTags.toMutableSet()
                                                            .apply { add(tag) })
                                                    setTagsList(
                                                        tagsList.toMutableSet()
                                                            .apply { remove(tag) })
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .height(heightAnimated)
                .requiredHeight(heightAnimated)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(0.dp),
                onClick = { onSelectTags(selectedTags) },
            ) {
                Text(text = "ПОКАЗАТЬ")
            }
        }
    }
}