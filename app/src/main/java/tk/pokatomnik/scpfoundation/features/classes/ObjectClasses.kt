package tk.pokatomnik.scpfoundation.features.classes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tk.pokatomnik.scpfoundation.features.pageslist.PageTitle

@Preview
@Composable
fun ObjectClasses() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .requiredHeight(64.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PageTitle(title = "Классы объектов")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Безопасный"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Объекты класса «Безопасный» — это аномалии, которые либо достаточно хорошо изучены для полноценного и надёжного долговременного содержания, либо не проявляют аномального воздействия без определённого внешнего стимула.")
            Text("Назначение аномалии класса «Безопасный» не значит, что активация или работа с ней не несёт угрозы; всем сотрудникам следует помнить, что все особые условия содержания и протоколы безопасности следует соблюдать неукоснительно.")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Евклид"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("К объектам класса «Евклид» относятся недостаточно изученные или изначально непредсказуемые аномалии, надёжное содержание которых не всегда возможно, но уровень угрозы недостаточен для присвоения им класса «Кетер». Подавляющему большинству аномалий, содержащихся Фондом, изначально присваивается класс «Евклид», который затем может быть изменён, если объект достаточно хорошо изучен или уровень представляемой им опасности слишком велик.")
            Text("В частности, всем аномалиям, которые можно назвать автономными и/или разумными, чаще всего присваивается класс не ниже «Евклида», поскольку объект, наделённый собственной волей или мышлением, по сути непредсказуем.")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Кетер"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Объекты класса «Кетер» — аномалии, которые настроены враждебно, представляют опасность для сотрудников Фонда и всего остального человечества, а их содержание сопряжено с большими затратами и сложными процедурами сдерживания, либо такие, полноценное содержание которых с помощью имеющихся у Фонда знаний и средств невозможно.")
            Text("Как правило, такие аномалии считаются самыми опасными из всех, находящихся на содержании, а научная деятельность по таким объектам направлена исключительно на разработку более надёжных средств сдерживания, либо, в качестве крайней меры, на своевременную нейтрализацию или уничтожение их аномального эффекта.")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Таумиэль"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Объекты класса «Таумиэль» имеют высокую степень секретности, встречаются крайне редко и применяются Фондом для содержания или противодействия другим аномалиям, представляющим большую опасность, в особенности — объектам класса «Кетер». Сама информация о существовании объектов класса «Таумиэль» является секретной и доступна только высокопоставленным сотрудникам Фонда, а информация о местонахождении, функциях и актуальном состоянии таких объектов не известна практически никому, кроме Совета О5.")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Нейтрализованный"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Нейтрализованные аномалии утратили свою аномальность из-за намеренного или случайного разрушения, отключения либо по какой-то иной причине.")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Аполлион"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Объекты класса «Аполлион» являются аномалиями, которые или невозможно содержать, или они неминуемо нарушат условия содержания, или предполагается развитие событий по схожему сценарию. Такие аномалии обычно связаны с угрозами конца света или Событиями класса К и требуют от Фонда значительных усилий для их устранения.")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Архонт"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Объекты класса «Архонт» являются аномалиями, чье содержание теоретически возможно, но по каким-либо причинам их лучше не содержать. Объекты класса «Архонт» могут быть частью общепризнанной реальности, которую затруднительно полноценно поставить на содержание, или же их содержание может быть чревато отрицательными последствиями. Эти аномалии не относятся к тем, чье содержание невозможно — основная особенность класса состоит в том, что Фонд решил не ставить аномалию на содержание, хотя мог бы.")
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}