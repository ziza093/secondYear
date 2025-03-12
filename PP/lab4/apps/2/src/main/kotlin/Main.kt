// Definim interfețe pentru a respecta principiul dependenței inverse
interface BookContent {
    fun getAuthor(): String
    fun getName(): String
    fun getPublisher(): String
    fun getText(): String
}

interface PricedItem {
    fun getPrice(): Double
}

// Interfață combinată pentru Book, implementează ambele interfețe
interface IBook : BookContent, PricedItem {
    fun hasAuthor(author: String): Boolean
    fun hasTitle(title: String): Boolean
    fun isPublishedBy(publisher: String): Boolean
}

// Interface pentru Library, folosit de LibraryPrinter
interface ILibrary {
    fun getBooks(): Set<IBook>
    fun findAllByAuthor(author: String): Set<IBook>
    fun findAllByName(name: String): Set<IBook>
    fun findAllByPublisher(publisher: String): Set<IBook>
}

// Content class cu implementare extinsă - adăugăm price
class Content(
    private var author: String,
    private var text: String,
    private var name: String,
    private var publisher: String,
    private var price: Double = 0.0
) : BookContent, PricedItem {
    override fun getAuthor(): String = author
    fun setAuthor(author: String) { this.author = author }

    override fun getText(): String = text
    fun setText(text: String) { this.text = text }

    override fun getName(): String = name
    fun setName(name: String) { this.name = name }

    override fun getPublisher(): String = publisher
    fun setPublisher(publisher: String) { this.publisher = publisher }

    override fun getPrice(): Double = price
    fun setPrice(price: Double) { this.price = price }
}

// Book class acum implementează IBook
class Book(
    private val data: Content  // Folosim Content direct, care implementează ambele interfețe
) : IBook {
    override fun toString(): String = "${data.getName()} by ${data.getAuthor()} - $${getPrice()}"

    override fun getName(): String = data.getName()
    override fun getAuthor(): String = data.getAuthor()
    override fun getPublisher(): String = data.getPublisher()
    override fun getText(): String = data.getText()
    override fun getPrice(): Double = data.getPrice()

    override fun hasAuthor(author: String): Boolean = data.getAuthor() == author
    override fun hasTitle(title: String): Boolean = data.getName() == title
    override fun isPublishedBy(publisher: String): Boolean = data.getPublisher() == publisher
}

// Library class acum implementează ILibrary
class Library : ILibrary {
    private val books: MutableSet<IBook> = mutableSetOf()

    override fun getBooks(): Set<IBook> = books

    fun addBook(book: IBook) {
        books.add(book)
    }

    override fun findAllByAuthor(author: String): Set<IBook> {
        return books.filter { it.hasAuthor(author) }.toSet()
    }

    override fun findAllByName(name: String): Set<IBook> {
        return books.filter { it.hasTitle(name) }.toSet()
    }

    override fun findAllByPublisher(publisher: String): Set<IBook> {
        return books.filter { it.isPublishedBy(publisher) }.toSet()
    }
}

// LibraryPrinter class acum depinde de interfață, nu de implementare
class LibraryPrinter {
    fun printBooksRaw(books: Set<IBook>) {
        books.forEach { book ->
            println("${book.getName()} by ${book.getAuthor()}, published by ${book.getPublisher()}, price: $${book.getPrice()}")
        }
    }

    fun printHTML(books: Set<IBook>) {
        println("<html>")
        println("<body>")
        println("<ul>")
        books.forEach { book ->
            println("<li><strong>${book.getName()}</strong> by ${book.getAuthor()}, " +
                    "published by ${book.getPublisher()}, price: $${book.getPrice()}</li>")
        }
        println("</ul>")
        println("</body>")
        println("</html>")
    }

    fun printJSON(books: Set<IBook>) {
        println("[")
        books.forEachIndexed { index, book ->
            println("  {")
            println("    \"title\": \"${book.getName()}\",")
            println("    \"author\": \"${book.getAuthor()}\",")
            println("    \"publisher\": \"${book.getPublisher()}\",")
            println("    \"price\": ${book.getPrice()}")
            print("  }")
            if (index < books.size - 1) println(",") else println("")
        }
        println("]")
    }
}

// Pentru extindere, putem crea o implementare alternativă a IBook
class PremiumBook(
    private val baseBook: IBook,
    private val bonusContent: String
) : IBook {
    override fun getAuthor(): String = baseBook.getAuthor()
    override fun getName(): String = "${baseBook.getName()} (Premium Edition)"
    override fun getPublisher(): String = baseBook.getPublisher()
    override fun getText(): String = "${baseBook.getText()}\n\nBONUS CONTENT: $bonusContent"
    override fun getPrice(): Double = baseBook.getPrice() * 1.5 // Premium price

    override fun hasAuthor(author: String): Boolean = baseBook.hasAuthor(author)
    override fun hasTitle(title: String): Boolean = baseBook.getName().contains(title)
    override fun isPublishedBy(publisher: String): Boolean = baseBook.isPublishedBy(publisher)
}

// Exemplu de utilizare
fun main() {
    // Creare cărți cu preț
    val content1 = Content("George Orwell", "It was a bright cold day in April...", "1984", "Penguin Books", 19.99)
    val content2 = Content("J.K. Rowling", "Mr. and Mrs. Dursley of number four...", "Harry Potter", "Bloomsbury", 24.99)
    val content3 = Content("George Orwell", "All animals are equal...", "Animal Farm", "Penguin Books", 14.99)

    val book1 = Book(content1)
    val book2 = Book(content2)
    val book3 = Book(content3)

    // Creem o carte premium (demonstrează extensibilitatea)
    val premiumBook = PremiumBook(book1, "Author interviews and deleted scenes")

    // Creare și populare bibliotecă
    val library = Library()
    library.addBook(book1)
    library.addBook(book2)
    library.addBook(book3)
    library.addBook(premiumBook)

    // Utilizare metode bibliotecă
    println("Toate cărțile din bibliotecă:")
    println(library.getBooks().map { it.toString() })

    println("\nCărți de George Orwell:")
    val orwellBooks = library.findAllByAuthor("George Orwell")
    println(orwellBooks.map { it.toString() })

    println("\nCărți publicate de Penguin Books:")
    val penguinBooks = library.findAllByPublisher("Penguin Books")
    println(penguinBooks.map { it.toString() })

    // Utilizare printer
    val printer = LibraryPrinter()
    println("\nAfișare cărți în format raw:")
    printer.printBooksRaw(library.getBooks())

    println("\nAfișare cărți în format HTML:")
    printer.printHTML(library.getBooks())

    println("\nAfișare cărți în format JSON:")
    printer.printJSON(library.getBooks())
}