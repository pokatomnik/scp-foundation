package tk.pokatomnik.scpfoundation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

interface ClickedPart {
    val id: String?
    val url: String?
}

interface OpenableUrl : ClickedPart {
    fun openUrl()
}

data class TextPart(
    val text: String,
    override val id: String? = null,
    override val url: String? = null,
    val onClick: ((str: OpenableUrl) -> Unit)? = null,
) : ClickedPart

@Composable
fun LinkText(
    modifier: Modifier = Modifier,
    dataConsumer: (
        appendTextData: (textPart: TextPart) -> Unit
    ) -> Unit
) {
    val context = LocalContext.current
    val textData = mutableListOf<TextPart>()

    fun openUrl (url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val appendTextData: (textPart: TextPart) -> Unit = {
        textData.add(it)
    }

    dataConsumer(appendTextData)

    val annotatedString = createAnnotatedString(textData)

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
        onClick = { offset ->
            textData.forEach { annotatedStringData ->
                if (annotatedStringData.id != null && annotatedStringData.url != null) {
                    annotatedString.getStringAnnotations(
                        tag = annotatedStringData.id,
                        start = offset,
                        end = offset,
                    ).firstOrNull()?.let {
                        annotatedStringData.onClick?.invoke(object : OpenableUrl {
                            override val id = it.tag
                            override val url = it.item
                            override fun openUrl() {
                                openUrl(url)
                            }
                        })
                    }
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun createAnnotatedString(data: List<TextPart>): AnnotatedString {
    return buildAnnotatedString {
        data.forEach { linkTextData ->
            if (linkTextData.id != null && linkTextData.url != null) {
                pushStringAnnotation(
                    tag = linkTextData.id,
                    annotation = linkTextData.url,
                )
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline,
                    ),
                ) {
                    append(linkTextData.text)
                }
                pop()
            } else {
                append(linkTextData.text)
            }
        }
    }
}
