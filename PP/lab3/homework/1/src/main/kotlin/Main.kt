package org.example
import kotlin.*
import java.io.*
import khttp.*
import org.jsoup.*
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.lang.*
import java.net.URL.*

data class RssItem(
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String
)

data class RssFeed(
    val items: List<RssItem>
)

fun parseRssFeed(url: String): RssFeed {
    val doc = Jsoup.connect(url).parser(Parser.xmlParser()).get()
    val channel = doc.select("channel").first() ?: throw Exception("Channel not found")

    val items = channel.select("item").map {element ->
        val title = element.select("title").first()?.text() ?: "N/A"
        val link = element.select("link").first()?.text() ?: "N/A"
        val description = element.select("description").first()?.text() ?: "N/A"
        val pubDate = element.select("pubDate").first()?.text() ?: "N/A"
        RssItem(title, link, description, pubDate)
    }

    return RssFeed(items)
}

fun main() {
    val url = "http://rss.cnn.com/rss/edition.rss"
    val feed = parseRssFeed(url)

    feed.items.forEach { item ->
        println("Titlu: " + item.title)
        println("Link: " + item.link)
        println("\n")
    }
}