package tk.pokatomnik.scpfoundation.features.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tk.pokatomnik.scpfoundation.components.LinkText
import tk.pokatomnik.scpfoundation.components.TextPart

@Composable
fun AboutDialog(
    open: Boolean,
    close: () -> Unit,
) {
    if (!open) {
        return
    }
    AlertDialog(
        onDismissRequest = close,
        title = { Text(text = "О приложении", fontWeight = FontWeight.Bold) },
        text = {
            LinkText { append ->
                append(TextPart("SCP Documents Reader - приложения для чтения статей с сайта "))
                append(TextPart(
                    text = "scp-ru.wikidot.com",
                    id = "websiteUrl",
                    url = "https://scp-ru.wikidot.com"
                ) {
                    it.openUrl()
                })
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = close, modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Понятно")
                }
            }
        }
    )
}