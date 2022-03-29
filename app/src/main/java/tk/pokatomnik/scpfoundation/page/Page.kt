package tk.pokatomnik.scpfoundation.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Page(
    url: String?
) {
    Column {
        Row {
            Text("This is a page component")
        }
        Row {
            url?.let { Text(it) }
        }
    }
}