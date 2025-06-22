import java.io.File

fun main(args: Array<String>) {
    val filePath = "input.txt"
    val offset = 3

    try {
        val fileContent = File(filePath).readText()
        val processedContent = processCaesarCipher(fileContent, offset)
        File("output.txt").writeText(processedContent)
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun processCaesarCipher(text: String, offset: Int): String {
    return text.split(Regex("\\s+")).map { word ->
        if (word.length in 4..7) {
            encryptWithCaesar(word, offset)
        } else {
            word
        }
    }.joinToString(" ")
}

fun encryptWithCaesar(word: String, offset: Int): String {
    val shiftChar: (Char) -> Char = { char ->
        when {
            char.isLowerCase() -> 'a' + (char - 'a' + offset) % 26
            char.isUpperCase() -> 'A' + (char - 'A' + offset) % 26
            else -> char
        }
    }

    return word.map { shiftChar(it) }.joinToString("")
}