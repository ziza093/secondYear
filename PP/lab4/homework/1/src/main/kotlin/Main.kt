package org.example
import khttp.responses.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.yaml.snakeyaml.Yaml
import org.json.JSONObject

interface Parser{
    fun parse(text:String) : Map<String, any>
}

class Crawler(private val url: String){

    fun getResource() : HttpResponse{
        return khttp.get(url)
    }

    fun proccesContent(contentType:String){
        val response = getResource()
        
    }
}

class JsonParser() : Parser{
    override fun parse(text: String) : Map{
        return
    }
}

class XmlParser() : Parser{
    override fun parse(text: String) : Map{
        return
    }
}

class YamlParser() : Parser{
    override fun parse(text: String) : Map{
        return
    }
}


fun main() {
    val crawler = Crawler("https://en.wikipedia.org/wiki/Romania")
    val response = crawler.getResource()
    crawler.proccesContent("Romania")

}