package tk.pokatomnik.scpfoundation.pages

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Title() {
    Text(
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center,
        maxLines = 1,
        text = "Список документов"
    )
}