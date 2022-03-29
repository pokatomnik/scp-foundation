package tk.pokatomnik.scpfoundation.di.http

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import retrofit2.Converter
import retrofit2.Retrofit
import tk.pokatomnik.scpfoundation.pages.PageInfo
import tk.pokatomnik.scpfoundation.pages.PageInfoImpl
import java.lang.Exception
import java.lang.reflect.Type

fun rowToPage(tr: Element): PageInfo? {
    val cells = try {
        tr.select("td")
    } catch (e: Exception) { return null }

    lateinit var nameCell: Element
    lateinit var ratingCell: Element
    lateinit var authorCell: Element
    lateinit var dateCell: Element
    try {
        nameCell = cells[0]
        ratingCell = cells[1]
        authorCell = cells[2]
        dateCell = cells[3]
    } catch (e: Exception) {
        return null
    }
    val (href, name) = try {
        val anchor = nameCell.select("a")
        val href = anchor.attr("href")
        val name = anchor.text()
        listOf(href, name)
    } catch (e: Exception) {
        return null
    }

    val rating = try {
        ratingCell.text().toInt()
    } catch (e: Exception) { null }
    val author = try {
        authorCell.select("span").select("a")[1].text()
    } catch (e: Exception) { null }
    val date = try {
        dateCell.select("span").text()
    } catch (e: Exception) { null }

    return PageInfoImpl(
        name = name,
        url = href,
        rating = rating,
        author = author,
        date = date
    )
}

/**
 * @param html page html code
 * @return List with pages if success, null if error (must retry), or empty array if end reached
 */
fun htmlToPages(html: String): List<PageInfo>? {
    val rows = try {
        val document = Jsoup.parse(html)
        document.select("tr")
    } catch (e: Exception) { return null }

    // TODO check if next page is the same as previous, It means that end reached.
    return rows.iterator()
        .asSequence()
        .drop(1)
        .fold(mutableListOf<PageInfo>()) { acc, current ->
            val pageInfo = rowToPage(current)
            if (pageInfo != null) {
                acc.add(pageInfo)
            }
            acc
        }
        .toList()
}

class PagesConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, List<PageInfo>> {
        return Converter { value ->
            htmlToPages(value.string())
        }
    }
}