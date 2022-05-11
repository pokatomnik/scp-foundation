package tk.pokatomnik.scpfoundation.features.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tk.pokatomnik.scpfoundation.components.SCPCard
import tk.pokatomnik.scpfoundation.features.pageslist.PageTitle

@Preview
@Composable
fun Menu() {
    val (aboutDialogOpen, setAboutDialogOpen) = remember { mutableStateOf(false) }

    AboutDialog(open = aboutDialogOpen) {
        setAboutDialogOpen(false)
    }

    return Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .requiredHeight(64.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PageTitle(title = "Меню")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SCPCard(
                onClick = { setAboutDialogOpen(true) },
                picture = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Outlined.Info,
                        contentDescription = ""
                    )
                },
                headerText = "О приложении",
                descriptionText = {
                    Text("SCP Documents Reader.")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}