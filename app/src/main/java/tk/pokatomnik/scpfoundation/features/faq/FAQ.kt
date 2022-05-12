package tk.pokatomnik.scpfoundation.features.faq

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tk.pokatomnik.scpfoundation.components.SCPCardTextOnly
import tk.pokatomnik.scpfoundation.features.pageslist.PageTitle

data class FAQItem (
    val question: String,
    val answer: String,
)

val faqList: List<FAQItem> = listOf(
    FAQItem(
        "Что это за приложение?",
        "Это коллективный научно-фантастический литературный проект, не имеющий единой линии повествования, но находящийся в едином сеттинге. Основные идеи проекта — неизученные явления, способы взятия их под контроль, загадки и вопросы, ответы на которые обрывочны и не всегда однозначны."
    ),
    FAQItem(
        "Это всё настоящее?",
        "Нет, не настоящее."
    ),
    FAQItem(
        "Это ролевая игра?",
        "Нет. Некоторые персонажи выписаны довольно подробно, кое у кого есть аватары с таким же именем, но это просто персонажи, созданные для статей и рассказов. Совпадение имён не означает отыгрыша персонажа. Автор английского FAQ — Брайт, а не д-р Брайт. А подписывается он так, потому что не хочет раскрывать настоящее имя. Если вкратце: доктор/агент/иная должность (Имярек) — это персонаж, а (Имярек) это автор. Соответственно, не стоит выписывать себя в качестве персонажа или пытаться отыгрывать какую-либо роль."
    ),
    FAQItem(
        "Почему не уничтожить объект?",
        "Некоторые объекты, несмотря на опасность, могут принести большую пользу человечеству, другие просто невозможно или крайне сложно уничтожить в силу их свойств. Наконец, часть объектов может пригодиться, если возникнет крайняя необходимость уничтожить другой объект."
    ),
    FAQItem(
        "Какие классы объектов есть?",
        "Безопасный, Евклид, Кетер, Таумиэль, Аполлион, Архонт"
    ),
)

@Composable
fun FAQ() {
    val (expandedIndex, setExpandedIndex) = remember { mutableStateOf<Int?>(null) }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .requiredHeight(64.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PageTitle(title = "ЧаВо")
        }
        Modifier.height(8.dp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            faqList.forEachIndexed { index, item ->
                val collapsed = index != expandedIndex
                SCPCardTextOnly(
                    headerText = item.question,
                    collapsed = collapsed,
                    onClick = {
                        val newExpandedIndex = when (expandedIndex) {
                            index -> null
                            else -> index
                        }
                        setExpandedIndex(newExpandedIndex)
                    }
                ) {
                    Text(
                        text = item.answer,
                        maxLines = if (collapsed) 2 else Int.MAX_VALUE,
                        overflow = if (collapsed) TextOverflow.Ellipsis else TextOverflow.Clip
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}