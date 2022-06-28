package tk.pokatomnik.scpfoundation.features.pageslist

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
internal fun PageTitle(title: String) {
    Text(
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h5,
        textAlign = TextAlign.Center,
        maxLines = 1,
        text = title
    )
}