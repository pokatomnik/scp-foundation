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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chargemap.compose.numberpicker.NumberPicker

@Composable
internal fun NavigationButtons(
    onExplicitNavigate: (pageNumber: Int) -> Unit,
    loading: Boolean = false,
    currentPage: Int,
    maxPage: Int,
) {
    var dialogVisible by remember { mutableStateOf(false) }
    var directPageNumberInput by remember { mutableStateOf(currentPage) }

    fun handleNavigationDone() {
        onExplicitNavigate(directPageNumberInput)
        dialogVisible = false
    }

    if (dialogVisible) {
        Dialog(onDismissRequest = { dialogVisible = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Перейти к странице",
                    fontWeight = FontWeight.Bold
                )
                NumberPicker(value = directPageNumberInput, onValueChange = { directPageNumberInput = it }, range = 1..maxPage)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = { handleNavigationDone() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text("Перейти")
                    }
                }
            }
        }
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