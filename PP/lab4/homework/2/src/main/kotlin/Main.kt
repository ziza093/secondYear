package org.example

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Scanner


interface PaymentMethod{
    fun pay(fee: Double) : Boolean
}

class CashPayment(private var availableAmount: Double) : PaymentMethod{
    override fun pay(fee: Double): Boolean {
        return if (fee <= availableAmount) {
            availableAmount -= fee
            true
        }else {
            false
        }
    }
}

class CardPayment(private val bankAccount: BankAccount) : PaymentMethod{
    override fun pay(fee: Double) : Boolean{
        return bankAccount.updateAmount(fee)
    }
}

class BankAccount(
    private var availableAmount: Double,
    private val cardNumber: String,
    private val expirationDate: Date,
    private val cvvCode: Int,
    private val userName: String){

    fun updateAmount(value: Double) : Boolean{
        return if(availableAmount >= value){
            availableAmount -= value
            true
        }else {
            false
        }
    }

    fun getAvailableAmount() : Double = availableAmount
}

class Movie(
    val id: String,
    val title: String,
    val duration: Int,
    val genre: String,
    val description: String
) {
    fun getMovieDetails(): String {
        return "Film: $title, Durata: $duration minute, Gen: $genre\nDescriere: $description"
    }
}

class Ticket(
    val id: String,
    private val price: Double,
    private val movie: Movie,
    private val seat: String,
    private val dateTime: Date
) {
    fun getPrice(): Double = price

    fun getTicketInfo(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return """
            Bilet: $id
            Film: ${movie.title}
            Loc: $seat
            Data si ora: ${dateFormat.format(dateTime)}
            Pret: $price RON
        """.trimIndent()
    }
}

interface TicketRepository {
    fun createTicket(movieId: String, seat: String, dateTime: Date): Ticket?
    fun getMovies(): List<Movie>
    fun getMovieById(movieId: String): Movie?
}


class TicketRepositoryStorage : TicketRepository{
    private val movies = mutableListOf(
        Movie("1", "Inception", 148, "SF/Thriller", "Un hoț care fură secrete corporative prin tehnologia de a pătrunde în subconștientul țintelor."),
        Movie("2", "Interstellar", 169, "SF/Adventure", "O echipă de exploratori călătorește prin universuri pentru a asigura supraviețuirea omenirii."),
        Movie("3", "The Godfather", 175, "Crime/Drama", "Șeful unui imperiu al crimei transferă controlul imperiului său fiului său reluctant.")
    )

    private var ticketCounter = 0

    override fun createTicket(movieId: String, seat: String, dateTime: Date): Ticket? {
        val movie = getMovieById(movieId) ?: return null
        ticketCounter++
        return Ticket("B$ticketCounter", getTicketPrice(movie), movie, seat, dateTime)
    }

    override fun getMovies(): List<Movie> = movies.toList()

    override fun getMovieById(movieId: String): Movie? = movies.find { it.id == movieId }

    private fun getTicketPrice(movie: Movie): Double {
        // Determinare pret în functie de gen si durata
        //when in kotlin ~= switch
        return when (movie.genre.split("/")[0]) {
            "SF" -> 25.0 + (movie.duration / 60) * 5
            "Crime" -> 22.0 + (movie.duration / 60) * 4.0
            else -> 20.0 + (movie.duration / 60) * 3.0
        }
    }
}

class Cinema(private val ticketRepository: TicketRepository) {
    private var paymentMethod: PaymentMethod? = null

    fun setPaymentMethod(method: PaymentMethod) {
        this.paymentMethod = method
    }

    fun buyTicket(movieId: String, seat: String): Ticket? {
        if (paymentMethod == null) {
            println("Eroare: Nu a fost setata o metoda de plata!")
            return null
        }

        // Crearea biletului
        val ticket = ticketRepository.createTicket(movieId, seat, Date()) ?: return null

        // Procesarea plății
        val paymentSuccessful = paymentMethod?.pay(ticket.getPrice()) ?: false

        return if (paymentSuccessful) {
            println("Plata procesata cu succes! Bilet emis.")
            ticket
        } else {
            println("Plata a esuat! Fonduri insuficiente.")
            null
        }
    }

    fun getAvailableMovies(): List<Movie> {
        return ticketRepository.getMovies()
    }
}



fun main() {

    val ticketRepository = TicketRepositoryStorage()
    val cinema = Cinema(ticketRepository)

    println("FILME DISPONIBILE:")
    cinema.getAvailableMovies().forEach { movie ->
        println("${movie.id}. ${movie.title}")
        println(movie.getMovieDetails())
        println()
    }

    val bankAccount = BankAccount(100.0, "1234-5678-9012-3456", Date(), 123, "John Pork")
    val cardPayment = CardPayment(bankAccount)

    println("CUMPARARE BILET CU CARD")
    cinema.setPaymentMethod(cardPayment)
    val ticket = cinema.buyTicket("1", "R12L13")

    ticket?.let {
        print("\nBILETUL EMIS: ")
        println(it.getTicketInfo())
        println("\nSold ramas: ${bankAccount.getAvailableAmount()} RON")
    }

    println("\nCUMPARARE BILET CU CASH")
    val cashPayment = CashPayment(50.0)
    cinema.setPaymentMethod(cashPayment)
    val ticket2 = cinema.buyTicket("3", "R11L1")

    ticket2?.let {
        print("\nBILETUL EMIS: ")
        println(it.getTicketInfo())
    }
}