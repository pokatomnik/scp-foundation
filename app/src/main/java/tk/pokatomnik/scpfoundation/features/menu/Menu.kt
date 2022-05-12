package tk.pokatomnik.scpfoundation.features.menu

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.HdrStrong
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LiveHelp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tk.pokatomnik.scpfoundation.components.SCPCardWithPicture
import tk.pokatomnik.scpfoundation.features.pageslist.PageTitle

@Composable
fun Menu(
    onNavigateToFAQ: () -> Unit,
    onNavigateToObjectClasses: () -> Unit
) {
    val context = LocalContext.current
    val (aboutDialogOpen, setAboutDialogOpen) = remember { mutableStateOf(false) }

    fun openSourceCodePage() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pokatomnik/scp-foundation"))
        context.startActivity(intent)
    }

    AboutDialog(open = aboutDialogOpen) {
        setAboutDialogOpen(false)
    }

    Column(
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
                .padding(horizontal = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            SCPCardWithPicture(
                onClick = { setAboutDialogOpen(true) },
                picture = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "О приложении"
                    )
                },
                headerText = "О приложении"
            ) {
                Text("SCP Documents Reader.")
            }
            SCPCardWithPicture(
                onClick = { onNavigateToFAQ() },
                picture = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Outlined.LiveHelp,
                        contentDescription = "Часто задаваемые вопросы"
                    )
                },
                headerText = "ЧАВО"
            ) {
                Text("Часто задаваемые вопросы")
            }
            SCPCardWithPicture(
                onClick = { onNavigateToObjectClasses() },
                picture = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Outlined.HdrStrong,
                        contentDescription = "Классы объектов"
                    )
                },
                headerText = "Классы объектов",
            ) {
                Text("Безопасный, Евклид, Кетер...")
            }
            SCPCardWithPicture(
                onClick = { openSourceCodePage() },
                picture = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Outlined.Code,
                        contentDescription = "Исходный код"
                    )
                },
                headerText = "Исходный код"
            ) {
                Text("Приложения")
            }
        }
    }
}

@Preview
@Composable
private fun Menu_example0() {
    return Menu(
        onNavigateToFAQ = {},
        onNavigateToObjectClasses = {}
    )
}