import java.io.*
import java.io.IO.println
import java.util.Scanner
import kotlin.collections.*

class Birth(val year: Int, val Month: Int, val Day: Int){
    override fun toString() : String{
        return "($Day.$Month.$year)"
    }
}
class Contact(val Name: String, var Phone: String, val BirthDate: Birth){
    fun Print() {
        println("Name: $Name, Mobile: $Phone, Date: $BirthDate")
    }
}

fun main(args : Array<String>){
    val agenda = mutableListOf<Contact>()
    agenda.add(Contact("Mihai", "0744321987", Birth(1900, 11, 25)))
    agenda += Contact("George", "0761332100", Birth(2002, 3, 14))
    agenda += Contact("Liviu" , "0744321987", Birth(1999, 7, 30))
    agenda += Contact("Popescu", "0211342787", Birth(1955, 5, 12))

    agenda.forEach{(it.Print())}

    val scanner = Scanner(System.`in`)
    val phone = scanner.next()

    println("Afisare agenda in functie de nr de telefon citit\n")
    agenda.filter {it.Phone == phone}.forEach{it.Print()}

    agenda.filter{it.Phone == phone}.forEach{it.Phone = "Nr de telefon nou, idk"}

    println("\n")
    agenda.forEach{it.Print()}

}