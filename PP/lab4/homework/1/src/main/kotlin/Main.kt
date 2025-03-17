import org.jsoup.Jsoup
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml
import javax.xml.parsers.DocumentBuilderFactory
import java.io.StringReader
import org.xml.sax.InputSource
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * HTTP Response class to replace khttp dependency
 */
class HttpResponse(val text: String, val statusCode: Int)

/**
 * Main crawler class that retrieves resources from the web
 */
class Crawler(val url: String) {
    /**
     * Retrieves the resource from the specified URL
     * @return HTTP response object
     */
    fun getResource(): HttpResponse {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("User-Agent", "Mozilla/5.0")

        val statusCode = connection.responseCode
        val inputStream = if (statusCode >= 400) connection.errorStream else connection.inputStream

        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        connection.disconnect()

        return HttpResponse(response.toString(), statusCode)
    }

    /**
     * Processes the content based on the content type
     * @param contentType The type of content to process (e.g., "json", "xml", "yaml")
     * @return The processed content as a string
     */
    fun processContent(contentType: String): String {
        val response = getResource()

        return when (contentType.lowercase()) {
            "json" -> response.text
            "xml" -> response.text
            "yaml" -> response.text
            "html" -> Jsoup.parse(response.text).html()
            else -> response.text
        }
    }
}

/**
 * Interface that defines the parsing behavior
 */
interface Parser {
    /**
     * Parses text into a Map structure
     * @param text The text to parse
     * @return Map containing the parsed data
     */
    fun parse(text: String): Map<String, Any>
}

/**
 * Implementation of Parser for JSON content
 */
class JsonParser : Parser {
    override fun parse(text: String): Map<String, Any> {
        val jsonObject = JSONObject(text)
        return jsonToMap(jsonObject)
    }

    /**
     * Helper method to convert JSONObject to Map
     */
    private fun jsonToMap(json: JSONObject): Map<String, Any> {
        val result = mutableMapOf<String, Any>()

        json.keys().forEach { key ->
            val value = json.get(key)
            result[key] = when (value) {
                is JSONObject -> jsonToMap(value)
                is org.json.JSONArray -> {
                    val list = mutableListOf<Any>()
                    for (i in 0 until value.length()) {
                        val element = value.get(i)
                        when (element) {
                            is JSONObject -> list.add(jsonToMap(element))
                            else -> list.add(element)
                        }
                    }
                    list
                }
                else -> value
            }
        }

        return result
    }
}

/**
 * Implementation of Parser for XML content
 */
class XmlParser : Parser {
    override fun parse(text: String): Map<String, Any> {
        val docBuilderFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = docBuilderFactory.newDocumentBuilder()
        val document = docBuilder.parse(InputSource(StringReader(text)))
        document.documentElement.normalize()

        return xmlToMap(document.documentElement)
    }

    /**
     * Helper method to convert XML Element to Map
     */
    private fun xmlToMap(element: org.w3c.dom.Element): Map<String, Any> {
        val result = mutableMapOf<String, Any>()

        // Process attributes
        val attributes = element.attributes
        for (i in 0 until attributes.length) {
            val attr = attributes.item(i)
            result["@${attr.nodeName}"] = attr.nodeValue
        }

        // Process child elements
        val childNodes = element.childNodes
        for (i in 0 until childNodes.length) {
            val node = childNodes.item(i)

            if (node.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                val childElement = node as org.w3c.dom.Element
                val nodeName = childElement.nodeName

                // Check if we already have this element
                if (result.containsKey(nodeName)) {
                    val current = result[nodeName]
                    if (current is List<*>) {
                        // Already a list, add to it
                        @Suppress("UNCHECKED_CAST")
                        (current as MutableList<Any>).add(xmlToMap(childElement))
                    } else {
                        // Convert to list
                        result[nodeName] = mutableListOf(current, xmlToMap(childElement))
                    }
                } else {
                    // First occurrence of this element
                    // Check if it has child elements
                    if (childElement.childNodes.length > 0 && hasElementChildren(childElement)) {
                        result[nodeName] = xmlToMap(childElement)
                    } else {
                        // Just text content
                        result[nodeName] = childElement.textContent
                    }
                }
            }
        }

        return result
    }

    /**
     * Helper method to check if element has element children
     */
    private fun hasElementChildren(element: org.w3c.dom.Element): Boolean {
        val childNodes = element.childNodes
        for (i in 0 until childNodes.length) {
            if (childNodes.item(i).nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                return true
            }
        }
        return false
    }
}

/**
 * Implementation of Parser for YAML content
 */
class YamlParser : Parser {
    override fun parse(text: String): Map<String, Any> {
        val yaml = Yaml()
        @Suppress("UNCHECKED_CAST")
        return yaml.load(text) as Map<String, Any>
    }
}

/**
 * Example usage
 */
fun main() {
    // Example URL with JSON response
    val jsonUrl = "https://jsonplaceholder.typicode.com/posts/1"
    val crawler = Crawler(jsonUrl)
    val jsonContent = crawler.processContent("json")
    val jsonParser = JsonParser()
    val jsonResult = jsonParser.parse(jsonContent)
    println("JSON Result: $jsonResult")

    // Example URL with XML response
    val xmlUrl = "https://www.w3schools.com/xml/simple.xml"
    val xmlCrawler = Crawler(xmlUrl)
    val xmlContent = xmlCrawler.processContent("xml")
    val xmlParser = XmlParser()
    val xmlResult = xmlParser.parse(xmlContent)
    println("XML Result: $xmlResult")

    // For YAML, you would typically need a URL that returns YAML content
    // This is just for demonstration
    val yamlContent = """
        name: John Smith
        age: 33
        address:
          city: New York
          country: USA
    """.trimIndent()
    val yamlParser = YamlParser()
    val yamlResult = yamlParser.parse(yamlContent)
    println("YAML Result: $yamlResult")
}