package tk.pokatomnik.scpfoundation.features.search

import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SearchButtonInternal(
    searchEnabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .requiredWidth(64.dp)
            .width(64.dp),
    ) {
        Icon(
            imageVector = if (searchEnabled) Icons.Filled.SearchOff else Icons.Filled.Search,
            contentDescription = "Поиск"
        )
    }
}