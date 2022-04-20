package tk.pokatomnik.scpfoundation.features.pageslist

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
internal fun NavigationButtons(
    onExplicitNavigate: (pageNumber: Int) -> Unit,
    loading: Boolean = false,
    currentPage: Int,
    maxPage: Int,
) {
    val context = LocalContext.current
    var dialogVisible by remember { mutableStateOf(false) }
    var directPageNumberInput by remember { mutableStateOf(currentPage.toString()) }

    fun handleNavigationDone() = try {
        onExplicitNavigate(directPageNumberInput.toInt())
        dialogVisible = false
        directPageNumberInput = currentPage.toString()
    } catch (e: Exception) {
        Toast.makeText(context, "Введите целое число", Toast.LENGTH_SHORT).show()
    }

    if (dialogVisible) {
        AlertDialog(
            onDismissRequest = { dialogVisible = false },
            text = {
                Column(modifier = Modifier.fillMaxWidth().width(IntrinsicSize.Min).padding(horizontal = 8.dp)) {
                    Text(
                        text = "Перейти к странице",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Номер страницы") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { handleNavigationDone() }
                        ),
                        value = directPageNumberInput,
                        onValueChange = { directPageNumberInput = it },
                    )
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = { handleNavigationDone() },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
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