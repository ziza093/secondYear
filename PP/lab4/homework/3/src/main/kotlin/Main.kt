package org.example
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.NoSuchElementException
import kotlin.system.exitProcess

class Note(
    private val author: String,
    private val title: String,
    private val data: Date,
    private val content: String){

    fun printNotesContent(){
        println("The author is: $author")
        println("The title is: $title")
        println("The date and time it was created: $data")
        println("The notes content is: $content")
    }

    fun getTitle() : String{
        return title
    }
}

class User(private val userName: String){

    fun getUserName() = userName

    fun createNote(){
        val scanner = Scanner(System.`in`)

        println("Title: ")
        val title = scanner.nextLine()

        println("Content: ")
        val content = scanner.nextLine()

        val notesFile = File("notes.txt")
        notesFile.appendText("Author: $userName\nThe title is: $title\nThe date and time it was created: ${Date()}\nThe notes content is: $content\n------------------------\n")
    }
}

class Menu{

    private var menu: MutableMap<User, MutableList<Note>> = mutableMapOf()

    private fun loadUsers(){
        val usersFromFile = File("users.txt")

        if(usersFromFile.exists() && usersFromFile.isFile){
            usersFromFile.forEachLine { line ->
                val user = User(line)
                menu.putIfAbsent(user, mutableListOf())
            }
        }
    }

    private fun loadNotes(){
        val notesFromFile = File("notes.txt")

        if(notesFromFile.exists() && notesFromFile.isFile){
            val notesList = notesFromFile.readText().split("------------------------")

            for (noteBlock in notesList.map { it.trim() }.filter { it.isNotEmpty() }) {
                val lines = noteBlock.lines().map { it.trim() }

                if (lines.size < 4){
                    continue
                }

                val author = lines[0].removePrefix("Author: ").trim()
                val title = lines[1].removePrefix("The title is: ").trim()
                val date = lines[2].removePrefix("The date and time it was created: ").trim()
                val content = lines[3].removePrefix("The notes content is: ").trim()


                val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

                val note = Note(author, title, formatter.parse(date), content)

                for( (user, noteList) in menu){
                    if(user.getUserName() == author){
                        if(!noteList.contains(note)){
                            noteList.add(note)
                        }
                    }
                }
            }
        }
    }

    private fun printAllNotes() {
        for( (_, notesList) in menu){
            notesList.forEach {it.printNotesContent()}
        }
    }

    private fun addUser(){
        val scanner = Scanner(System.`in`)
        println("Read the username:")

        val usersFile = File("users.txt")

        usersFile.appendText(scanner.nextLine()+"\n")
    }

    private fun getNote(userName: String, index: Int){
        try {
            var userNotes = mutableListOf<Note>()

            for( (user, noteList) in menu){
                if(user.getUserName() == userName){
                    userNotes = noteList
                }
            }

            if(userNotes.isNotEmpty())
                userNotes[index].printNotesContent()

        }catch (e: NoSuchElementException){
            println("User doesnt exist!")
        }
    }

    private fun addNote(userName: String){
        try {
            val user = menu.keys.find {it.getUserName() == userName} ?: throw NoSuchElementException()
            user.createNote()
        }catch (e: NoSuchElementException){
            println("User doesnt exist!")
            println("Do you want to create one? (y/*)")
            val scanner = Scanner(System.`in`)
            if (scanner.nextLine() == "y")
                addUser()
        }
    }

    private fun deleteNote(userName: String, index: Int){
        for( (user, noteList) in menu){
            if(user.getUserName() == userName){
                if(noteList.isNotEmpty())
                    noteList.removeAt(index)
                else
                    println("The user doesnt have any notes!")
            }
        }
    }

    private fun printAllUserNotes(userName: String){

        for( (user, noteList) in menu){
            if(user.getUserName() == userName){
                if(noteList.isEmpty()){
                    println("The user doesnt have any notes!")
                }
                else
                    noteList.forEachIndexed { index, note -> println("$index -> " + note.getTitle()) }
            }
        }
    }

    fun cliMenu(){

        println("Welcome to my notes app!")
        println("Please, choose something from the menu!")
        println("Welcome to my notes app!")
        println("0. EXIT")
        println("1. Print the notes list")
        println("2. Add a note")
        println("3. Delete a note")
        println("4. Print a note")

        val scanner = Scanner(System.`in`)
        var choice = scanner.nextLine().toInt()


        do {

            loadUsers()
            loadNotes()

            when (choice) {
                0 -> {
                    println("Exiting program...")
                    exitProcess(0)
                }
                1 -> printAllNotes()
                2 -> {
                    println("Select the user which adds the note:")
                    addNote(scanner.nextLine())
                }
                3 -> {
                    println("Read the user of which note you want to delete!")
                    val userName = scanner.nextLine()

                    println("Select the note you want to delete!")
                    printAllUserNotes(userName)

                    val index = scanner.nextLine().toInt()
                    deleteNote(userName, index)
                }
                4 -> {
                    println("Read the user of which note you want read!")
                    val userName = scanner.nextLine()

                    println("Select the note you want to read!")
                    printAllUserNotes(userName)

                    val index = scanner.nextLine().toInt()
                    getNote(userName, index)
                }
                else -> println("Wrong input!")
            }

            println("Welcome to my notes app!")
            println("0. EXIT")
            println("1. Print the notes list")
            println("2. Add a note")
            println("3. Delete a note")
            println("4. Print a note\n")
            choice = scanner.nextLine().toInt()

        } while (choice in 1..4)

    }
}

fun main() {

    val menu = Menu()
    menu.cliMenu()
}