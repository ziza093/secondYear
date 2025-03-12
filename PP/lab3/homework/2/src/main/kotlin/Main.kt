import java.io.File
import java.nio.charset.Charset
import java.util.regex.Pattern

fun main() {
    val inputPath = "ebook.txt"
    val outputPath = "processedEbook.txt"

    val processor = EbookProcessor()
    try {
        val text = File(inputPath).readText(Charset.defaultCharset())
        val processedText = processor.processText(text)
        File(outputPath).writeText(processedText, Charset.forName("UTF-8"))
    } catch (e: Exception) {
        println("Eroare la procesarea fișierului: ${e.message}")
        e.printStackTrace()
    }
}

class EbookProcessor {
    //set nume de capitole si parti
    private val commonChapterNames = setOf(
        "chapter", "part", "section", "book", "prologue", "epilogue", "introduction",
        "conclusion", "appendix", "foreword", "afterword", "preface"
    )


    fun processText(text: String): String {
        var processedText = text

        //eliminarea spatiilor multiple
        processedText = processedText.replace(Regex("\\s{2,}"), " ")

        //eliminarea salturilor la linie noua multiple
        processedText = processedText.replace(Regex("\n{2,}"), "\n")

        //detectarea si eliminarea numarului de pagina
        processedText = processedText.replace(Regex("\\s+\\d+\\s+"), " ")
        processedText = processedText.replace(Regex("\\n\\d+\\s+"), "\n")
        processedText = processedText.replace(Regex("\\s+\\d+\\n"), "\n")

        //detectarea si eliminarea numelui autorului
        processedText = processedText.replace(Regex("(?i)By\\s+[A-Za-z\\s.]+"), "By")
        processedText = processedText.replace(Regex("(?i)Author:\\s+[A-Za-z\\s.]+"), "Author")
        processedText = processedText.replace(Regex("(?i)Written\\s+by\\s+[A-Za-z\\s.]+"), "Written by")

        //detectarea si eliminarea numelui capitolului
        val chapterPattern = Regex("(?i)(\\s*\\n*)(${commonChapterNames.joinToString("|")})\\s+[IVXLCDM0-9]+[.:]?\\s*\\n*", RegexOption.IGNORE_CASE)
        processedText = processedText.replace(chapterPattern, "\n")

        return processedText
    }
}