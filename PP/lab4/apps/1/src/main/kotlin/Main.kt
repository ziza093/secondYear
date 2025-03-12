package org.example

class Content(
    private var author: String,
    private var text: String,
    private var name: String,
    private var publisher: String){

    public fun getAuthor(): String{
        return author
    }

    public fun setAuthor(author: String){
        this.author = author
    }
     public fun getText(): String{
         return text
     }

    public fun setText(text: String){
        this.text = text
    }

    public fun getName(): String{
        return name
    }

    public fun setName(name: String){
        this.name = name
    }

    public fun getPublisher(): String{
        return publisher
    }

    public fun setPublisher(publisher: String){
        this.publisher = publisher
    }
}


class Book(private val data: Content){

    override fun toString(): String = "${data.getName()} by ${data.getAuthor()}"

    fun getName():String{
        return data.getName()
    }

    public fun getAuthor():String{
        return data.getAuthor()
    }

    public fun getPublisher():String{
        return data.getPublisher()
    }

    public fun getContent(): String{
        return data.getText()
    }

    public fun hasAuthor(author: String): Boolean {
        return author == data.getAuthor()
    }

    public fun hasTitle(title: String): Boolean {
        return title == data.getName()
    }

    public fun isPublishedBy(publisher: String): Boolean {
        return publisher == data.getPublisher()
    }
}

class Library{

    private val books: MutableSet<Book> = mutableSetOf()

    public fun getBooks(): Set<Book> {
        return books
    }

    public fun addBook(book: Book){
        books.add(book)
    }

    public fun findAllByAuthor(author: String): Set<Book> {
        return books.filter{ it.hasAuthor(author) }.toSet()
    }

    public fun findAllByName(name: String): Set<Book> {
        return books.filter { it.hasTitle(name) }.toSet()
    }

    public fun findAllByPublisher(publisher: String): Set<Book> {
        return books.filter{it.isPublishedBy(publisher) }.toSet()
    }
}

class LibraryPrinter {
    fun printBooksRaw(books: Set<Book>) {
        books.forEach { book ->
            println("${book.getName()} by ${book.getAuthor()}, published by ${book.getPublisher()}")
        }
    }

    fun printHTML(books: Set<Book>) {
        println("<html>")
        println("<body>")
        println("<ul>")
        books.forEach { book ->
            println("<li><strong>${book.getName()}</strong> by ${book.getAuthor()}, " +
                    "published by ${book.getPublisher()}</li>")
        }
        println("</ul>")
        println("</body>")
        println("</html>")
    }

    fun printJSON(books: Set<Book>) {
        println("[")
        books.forEachIndexed { index, book ->
            println("  {")
            println("    \"title\": \"${book.getName()}\",")
            println("    \"author\": \"${book.getAuthor()}\",")
            println("    \"publisher\": \"${book.getPublisher()}\"")
            print("  }")
            if (index < books.size - 1) println(",") else println("")
        }
        println("]")
    }
}

.fun main() {

    val content1 = Content("George Orwell", "It was a bright cold day in April...", "1984", "Penguin Books")
    val content2 = Content("J.K. Rowling", "Mr. and Mrs. Dursley of number four...", "Harry Potter", "Bloomsbury")
    val content3 = Content("George Orwell", "All animals are equal...", "Animal Farm", "Penguin Books")

    val book1 = Book(content1)
    val book2 = Book(content2)
    val book3 = Book(content3)

    val library = Library()
    library.addBook(book1)
    library.addBook(book2)
    library.addBook(book3)

    println("All books in library:")
    println(library.getBooks().map { it.toString() })

    println("\nBooks by George Orwell:")
    val orwellBooks = library.findAllByAuthor("George Orwell")
    println(orwellBooks.map { it.toString() })

    println("\nBooks published by Penguin Books:")
    val penguinBooks = library.findAllByPublisher("Penguin Books")
    println(penguinBooks.map { it.toString() })

    val printer = LibraryPrinter()
    println("\nPrinting books in raw format:")
    printer.printBooksRaw(library.getBooks())

    println("\nPrinting books in HTML format:")
    printer.printHTML(library.getBooks())

    println("\nPrinting books in JSON format:")
    printer.printJSON(library.getBooks())
}