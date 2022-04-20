package tk.pokatomnik.scpfoundation.features.pageslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
internal fun NavigationButtons(
    onExplicitNavigate: (pageNumber: Int) -> Unit,
    loading: Boolean = false,
    currentPage: Int,
    maxPage: Int,
) {
    var dialogVisible by remember { mutableStateOf(false) }
    var directPageNumberInput by remember { mutableStateOf(currentPage.toString()) }

    if (dialogVisible) {
        AlertDialog(
            onDismissRequest = { dialogVisible = false },
            title = { Text("Перейти к странице") },
            text = {
                TextField(
                    value = directPageNumberInput,
                    onValueChange = { directPageNumberInput = it }
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            try {
                                onExplicitNavigate(directPageNumberInput.toInt())
                                dialogVisible = false
                                directPageNumberInput = currentPage.toString()
                            } catch (e: Exception) { }
                        }
                    ) {
                        Text("Перейти")
                    }
                }
            }
        )
    }

    Divider(modifier = Modifier.fillMaxWidth())
    Row(
        modifier = Modifier
            .height(64.dp)
            .requiredHeight(64.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { onExplicitNavigate(currentPage - 1) }, enabled = !loading) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Предыдущая страница")
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    enabled = !loading,
                    onClick = {
                        dialogVisible = true
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$currentPage из $maxPage")
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { onExplicitNavigate(currentPage + 1) }, enabled = !loading) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Следующая страница")
            }
        }
    }
}