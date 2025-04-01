package org.example
import java.io.File
import java.time.Instant

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class HistoryLogRecord(private val command: String, private val timestamp: Instant) : Comparable<HistoryLogRecord>{
    fun getCommand():String{
        return command
    }

    fun getTimestamp():Instant{
        return timestamp
    }

    override fun compareTo(other: HistoryLogRecord): Int{
        return this.timestamp.compareTo(other.timestamp)
    }
}


fun <T : Comparable<T>>max(first: T, second: T): T {
//    val k = first.compareTo(second)
    return if (first <= second) second else first
}

fun findAndReplace(find: HistoryLogRecord, replace: HistoryLogRecord, map: MutableMap<Instant, HistoryLogRecord>){
    var keyToReplace = Instant.now()
    map.forEach{ if(it.value.getTimestamp() == find.getTimestamp()) { println("${it.value.getTimestamp()} == ${find.getTimestamp()}"); keyToReplace = it.key } }

    if(keyToReplace != null){
        map[keyToReplace] = replace
    }
}


fun main() {

//    val MutableMap<> map=
    var startDate: String
    var commandLine: String

    val MutableHashMap: MutableMap<Instant, HistoryLogRecord> = mutableMapOf()

    if(!File("parsedFile.txt").exists()) {
        File("history.log").forEachLine {

            startDate = ""
            commandLine = ""

            if (it.contains("Start-Date:")) {
                startDate = it.substringAfter("Start-Date: ")
                File("parsedFile.txt").appendText(startDate + "\n")
            } else {
                if (it.contains("Commandline:")) {
                    commandLine = it.substringAfter("Commandline: ")
                    File("parsedFile.txt").appendText(commandLine + "\n\n")
                }
            }
        }
    }

    File("parsedFile.txt").readLines().filter{ it.isNotBlank() }.chunked(2) { (startDate, commandLine) ->

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")
        val localDateTime = LocalDateTime.parse(startDate, formatter)
        val timestamp = localDateTime.toInstant(ZoneOffset.UTC)

        val obj = HistoryLogRecord(commandLine, timestamp)
        MutableHashMap.putIfAbsent(timestamp, obj)
    }

    var timestamp = LocalDateTime.parse("2025-03-19  12:44:14", DateTimeFormatter.ofPattern(("yyyy-MM-dd  HH:mm:ss"))).toInstant(ZoneOffset.UTC)
    val find = HistoryLogRecord("apt install libssl-dev", timestamp)


    val replace = HistoryLogRecord("apt upgrade", LocalDateTime.parse("2025-03-19  12:25:42", DateTimeFormatter.ofPattern(("yyyy-MM-dd  HH:mm:ss"))).toInstant(ZoneOffset.UTC))


    findAndReplace(find, replace, MutableHashMap)
    MutableHashMap.forEach { println(it.key); println(it.value.getCommand()) }


}