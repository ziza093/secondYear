package org.example
import org.jsoup.Jsoup
import org.jsoup.*
import org.jsoup.HttpStatusException
import org.jsoup.parser.Parser
import kotlin.collections.*
import java.io.IOException
import java.io.File

// clasa de noduri pentru a reprezenta ierarhizarea link-urilor
class LinkNode(val url: String) {
    val children: MutableList<LinkNode> = mutableListOf()

    fun addChild(child: LinkNode) {
        children.add(child)
    }

    override fun toString(): String {
        return url
    }
}

//functie pentru a crea arbore din map
fun buildTree(rootUrl: String, linksMap: Map<String, List<String>>): LinkNode {
    val domain = extractDomain(rootUrl)
    val rootNode = LinkNode(domain)

    //adauga primul nivel de link-uri ca si copii ai radacinii
    for (link in linksMap.keys) {
        val firstLevelNode = LinkNode(link)
        rootNode.addChild(firstLevelNode)

        //adauga al doilea nivel de link-uri
        for (secondLevelLink in linksMap[link] ?: emptyList()) {
            val secondLevelNode = LinkNode(secondLevelLink)
            firstLevelNode.addChild(secondLevelNode)
        }
    }

    return rootNode
}

//functie pentru serializare cu identare pentru vizualizare
fun serializeTree(node: LinkNode, indent: String = ""): String {
    val builder = StringBuilder()
    builder.append("$indent${node.url}\n")

    for (child in node.children) {
        builder.append(serializeTree(child, "$indent  "))
    }

    return builder.toString()
}

//serializare alternativa pentru scriere in fisier cu format "custom" (0/1/2 si |)
fun serializeTreeToFile(node: LinkNode, file: File) {
    file.writeText(serializeTreeFormat(node))
}

//functia custom precizata mai sus
fun serializeTreeFormat(node: LinkNode, level: Int = 0): String {
    val builder = StringBuilder()
    builder.append("$level|${node.url}\n")

    for (child in node.children) {
        builder.append(serializeTreeFormat(child, level + 1))
    }

    return builder.toString()
}

//functie de deserializare pentru a reconstrui arborele
fun deserializeTree(serializedString: String): LinkNode? {
    val lines = serializedString.trim().split("\n")
    if (lines.isEmpty()) return null

    //extrage linia cu radacina
    val rootLine = lines[0]
    val rootParts = rootLine.split("|")
    if (rootParts.size != 2 || rootParts[0] != "0") return null

    val rootNode = LinkNode(rootParts[1])
    val nodeStack = mutableListOf(rootNode)
    val levelStack = mutableListOf(0)

    for (i in 1 until lines.size) {
        val line = lines[i]
        val parts = line.split("|")
        if (parts.size != 2) continue

        val level = parts[0].toIntOrNull() ?: continue
        val url = parts[1]

        //gaseste parintele nodului
        while (levelStack.last() >= level) {
            nodeStack.removeAt(nodeStack.size - 1)
            levelStack.removeAt(levelStack.size - 1)
        }

        val newNode = LinkNode(url)
        nodeStack.last().addChild(newNode)

        nodeStack.add(newNode)
        levelStack.add(level)
    }

    return rootNode
}

//deserializare din fisier
fun deserializeTreeFromFile(file: File): LinkNode? {
    if (!file.exists()) return null
    return deserializeTree(file.readText())
}

//afisare arbore cu indentare pentru vizualizare
fun printTree(node: LinkNode, indent: String = "") {
    println("$indent${node.url}")
    for (child in node.children) {
        printTree(child, "$indent  ")
    }
}

fun getLinks(url: String) : List<String> {
    return try{
        val doc = Jsoup.connect(url).parser(Parser.htmlParser()).get()
        val body = doc.select("body").first() ?: throw Exception("No link found")

        val urlRegex = """https?://.*"""                                     //actual url regex

        val links: List<String> = body.select("a[href]").map { it.attr("href") }

        links.filter { it.matches(urlRegex.toRegex()) }

    } catch (e: HttpStatusException) {
        println("HTTP error: ${e.statusCode} - ${e.message}")
        emptyList() // Return an empty list in case of HTTP errors (like 404)
    } catch (e: IOException) {
        println("Network error: ${e.message}")
        emptyList() // Return empty list if network request fails
    } catch (e: Exception) {
        println("Unexpected error: ${e.message}")
        emptyList()
    }
}

fun extractDomain(url: String): String {
    // Remove protocol (http://, https://)
    val withoutProtocol = url.replace(Regex("^(https?://)(www\\.)?"), "")

    // Get domain (everything up to the first '/' or ':')
    val domain = withoutProtocol.split("/", ":")[0]

    return domain
}


fun createDomainRegex(domain: String): String {
    // Escape special regex characters in domain
    val escapedDomain = domain.replace(".", "\\.")
        .replace("-", "\\-")

    // Create regex pattern that matches URLs with this domain
    return "^https?://(www\\.)?$escapedDomain(/.*)?$"
}

fun main() {
    val url = "https://en.wikipedia.org/wiki/Romania"
    val firstLevelLinks: List<String> = getLinks(url)

    println("Found ${firstLevelLinks.size} first-level links")

    val links: MutableMap<String, MutableList<String>> = mutableMapOf()

    //proceseaza fiecarel link de nivel 1 pentru a extrage link de nivel 2
    var processedLinks = 0
    for (link in firstLevelLinks) {
        var domain = extractDomain(link)
        domain = createDomainRegex(domain)

        try {
            var secondLevelLinks: List<String> = getLinks(link)
            secondLevelLinks = secondLevelLinks.filter { it.matches(domain.toRegex()) }
            links.getOrPut(link) { mutableListOf() }.addAll(secondLevelLinks)

            processedLinks++
            println("Processed $processedLinks/${firstLevelLinks.size}: $link - Found ${secondLevelLinks.size} links")

            //nr de link-uri pe care se le extrag din link-ul radacina
            if (processedLinks >= 20) break

        } catch (e: Exception) {
            println("Error processing $link: ${e.message}")
        }
    }

    //creaza arbore
    val linkTree = buildTree(url, links)

    //afiseaza arbore
    println("\nTree structure:")
    printTree(linkTree)

    //serializare catre un fisier
    val outputFile = File("link_tree.txt")
    serializeTreeToFile(linkTree, outputFile)

    //deserializeaza dn fisier
    val deserializedTree = deserializeTreeFromFile(outputFile)
    if (deserializedTree != null) {
        println("\nDeserialized tree structure:")
        printTree(deserializedTree)
    }
}