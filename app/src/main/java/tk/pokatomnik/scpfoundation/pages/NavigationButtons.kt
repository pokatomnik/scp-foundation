package tk.pokatomnik.scpfoundation.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationButtons(
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    loading: Boolean = false,
    pageNumber: Int,
) {
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
            IconButton(onClick = onPreviousClick, enabled = !loading) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Предыдущая страница")
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(pageNumber.toString())
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onNextClick, enabled = !loading) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Следующая страница")
            }
        }
    }
}