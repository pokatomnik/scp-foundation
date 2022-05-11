package tk.pokatomnik.scpfoundation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SCPCard(
    onClick: (() -> Unit)? = null,
    picture: @Composable () -> Unit,
    headerText: String,
    descriptionText: @Composable () -> Unit
) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick?.invoke() },
                enabled = onClick != null,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                picture()
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = headerText,
                        style = MaterialTheme.typography.h6
                    )
                }
                Row {
                    descriptionText()
                }
            }
        }
    }
}

@Preview
@Composable
private fun SCPCard_example0() {
    return SCPCard(
        onClick = {},
        picture = {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Outlined.Info,
                contentDescription = ""
            )
        },
        headerText = "О приложении",
        descriptionText = { Text("Описание") }
    )
}

@Preview
@Composable
private fun SCPCard_example1() {
    return SCPCard(
        onClick = {},
        picture = {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Outlined.Info,
                contentDescription = ""
            )
        },
        headerText = "Очень очень очень очень очень очень длинный заголовок",
        descriptionText = { Text("Очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень длинное описание") }
    )
}