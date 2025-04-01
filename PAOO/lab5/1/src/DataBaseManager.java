import java.sql.*;
import java.util.Scanner;

public class DataBaseManager {
    private Connection conexiune;
    private Scanner scanner;

    public DataBaseManager(String dbFile) {
        try {
            // Realizarea conexiunii la baza de date
            Class.forName("org.sqlite.JDBC");
            conexiune = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            System.out.println("Conexiune realizată cu succes!");
            scanner = new Scanner(System.in);
        } catch (Exception e) {
            System.err.println("Eroare la conectare: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afiseazaMeniu() {
        int optiune = -1;

        while (optiune != 0) {
            System.out.println("\n=== MENIU OPERAȚII BAZA DE DATE ===");
            System.out.println("1- Adăugare înregistrare");
            System.out.println("2- Ștergere înregistrare");
            System.out.println("3- Modificare înregistrare");
            System.out.println("4- Ordonare descrescătoare după rată");
            System.out.println("5- Afișare conținut tabelă");
            System.out.println("0- Terminare Operații");
            System.out.print("Alegeți opțiunea: ");

            try {
                optiune = scanner.nextInt();
                scanner.nextLine(); // Consumă newline

                switch (optiune) {
                    case 1:
                        adaugaInregistrare();
                        break;
                    case 2:
                        stergeInregistrare();
                        break;
                    case 3:
                        modificaInregistrare();
                        break;
                    case 4:
                        afiseazaOrdoneazaDescrescator();
                        break;
                    case 5:
                        afiseazaTabela();
                        break;
                    case 0:
                        System.out.println("Programul se închide...");
                        break;
                    default:
                        System.out.println("Opțiune invalidă! Încercați din nou.");
                }
            } catch (Exception e) {
                System.err.println("Eroare la citirea opțiunii: " + e.getMessage());
                scanner.nextLine(); // Consumă input invalid
                optiune = -1;
            }
        }
    }

    public void adaugaInregistrare() {
        try {
            System.out.println("\n=== ADĂUGARE ÎNREGISTRARE ===");

            System.out.print("Introduceți poziția pentru May 2019: ");
            int thisYear = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Introduceți poziția pentru May 2018: ");
            int lastYear = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Introduceți schimbarea (format: +/-X.XX%): ");
            String change = scanner.nextLine();

            System.out.print("Introduceți numele limbajului de programare: ");
            String prgLang = scanner.nextLine();

            System.out.print("Introduceți rating-ul (format: X.XXX%): ");
            String ratings = scanner.nextLine();

            PreparedStatement pstmt = conexiune.prepareStatement(
                    "INSERT INTO Ranking (May2019, May2018, Change, PrgLang, Ratings) VALUES (?, ?, ?, ?, ?)");
            pstmt.setInt(1, thisYear);
            pstmt.setInt(2, lastYear);
            pstmt.setString(3, change);
            pstmt.setString(4, prgLang);
            pstmt.setString(5, ratings);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " înregistrare adăugată cu succes!");
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Eroare la adăugarea înregistrării: " + e.getMessage());
        }
    }

    public void stergeInregistrare() {
        try {
            System.out.println("\n=== ȘTERGERE ÎNREGISTRARE ===");

            System.out.print("Introduceți ID-ul înregistrării de șters: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement pstmt = conexiune.prepareStatement("DELETE FROM Ranking WHERE rowid = ?");
            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Înregistrarea cu ID-ul " + id + " a fost ștearsă cu succes!");
            } else {
                System.out.println("Nu există înregistrare cu ID-ul " + id);
            }
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Eroare la ștergerea înregistrării: " + e.getMessage());
        }
    }

    public void modificaInregistrare() {
        try {
            System.out.println("\n=== MODIFICARE ÎNREGISTRARE ===");

            System.out.print("Introduceți ID-ul înregistrării de modificat: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // Verificăm dacă înregistrarea există
            PreparedStatement checkStmt = conexiune.prepareStatement("SELECT * FROM Ranking WHERE rowid = ?");
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Nu există înregistrare cu ID-ul " + id);
                rs.close();
                checkStmt.close();
                return;
            }

            rs.close();
            checkStmt.close();

            System.out.println("Care câmp doriți să modificați?");
            System.out.println("1- May 2019");
            System.out.println("2- May 2018");
            System.out.println("3- Change");
            System.out.println("4- Programming Language");
            System.out.println("5- Ratings");
            System.out.print("Alegeți opțiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            String columnName;
            switch (optiune) {
                case 1: columnName = "May2019"; break;
                case 2: columnName = "May2018"; break;
                case 3: columnName = "Change"; break;
                case 4: columnName = "PrgLang"; break;
                case 5: columnName = "Ratings"; break;
                default:
                    System.out.println("Opțiune invalidă!");
                    return;
            }

            System.out.print("Introduceți noua valoare: ");
            String newValue = scanner.nextLine();

            PreparedStatement pstmt;
            if (optiune == 1 || optiune == 2) {
                // Pentru valori numerice
                int numericValue = Integer.parseInt(newValue);
                pstmt = conexiune.prepareStatement("UPDATE Ranking SET " + columnName + " = ? WHERE rowid = ?");
                pstmt.setInt(1, numericValue);
            } else {
                // Pentru valori text
                pstmt = conexiune.prepareStatement("UPDATE Ranking SET " + columnName + " = ? WHERE rowid = ?");
                pstmt.setString(1, newValue);
            }

            pstmt.setInt(2, id);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " înregistrare actualizată cu succes!");
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Eroare la modificarea înregistrării: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Valoare numerică invalidă!");
        }
    }

    public void afiseazaTabela() {
        try {
            Statement stmt = conexiune.createStatement();
            String sql = "SELECT rowid, * FROM Ranking";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n=== CONȚINUT TABELĂ ===");
            System.out.println("ID, May2019, May2018, Change, PrgLang, Ratings");

            while (rs.next()) {
                int id = rs.getInt("rowid");
                int thisYear = rs.getInt("May2019");
                int lastYear = rs.getInt("May2018");
                String change = rs.getString("Change");
                String prgLang = rs.getString("PrgLang");
                String ratings = rs.getString("Ratings");

                System.out.println(id + ", " + thisYear + ", " + lastYear + ", " +
                        change + ", " + prgLang + ", " + ratings);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Eroare la afișarea tabelei: " + e.getMessage());
        }
    }

    public void afiseazaLimbajeInCrestere() {
        try {
            Statement stmt = conexiune.createStatement();
            String sql = "SELECT * FROM Ranking WHERE Change LIKE '+%'";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n=== LIMBAJE ÎN CREȘTERE ===");
            while (rs.next()) {
                int thisYear = rs.getInt("May2019");
                int lastYear = rs.getInt("May2018");
                String change = rs.getString("Change");
                String prgLang = rs.getString("PrgLang");
                String ratings = rs.getString("Ratings");

                System.out.println(thisYear + ", " + lastYear + ", " + change + ", " + prgLang + ", " + ratings);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Eroare la afișarea limbajelor în creștere: " + e.getMessage());
        }
    }

    public void afiseazaOrdoneazaDescrescator() {
        try {
            Statement stmt = conexiune.createStatement();
            // Ordonăm după Change, dar trebuie să eliminăm % și + pentru a compara ca valori numerice
            String sql = "SELECT * FROM Ranking ORDER BY CAST(REPLACE(REPLACE(Change, '+', ''), '%', '') AS REAL) DESC";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n=== LIMBAJE ORDONATE DUPĂ RATĂ DESCRESCĂTOR ===");
            while (rs.next()) {
                int thisYear = rs.getInt("May2019");
                int lastYear = rs.getInt("May2018");
                String change = rs.getString("Change");
                String prgLang = rs.getString("PrgLang");
                String ratings = rs.getString("Ratings");

                System.out.println(thisYear + ", " + lastYear + ", " + change + ", " + prgLang + ", " + ratings);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Eroare la ordonarea descrescătoare: " + e.getMessage());
        }
    }

    public void inchideConexiune() {
        try {
            if (conexiune != null && !conexiune.isClosed()) {
                conexiune.close();
                System.out.println("Conexiune închisă cu succes!");
            }
            if (scanner != null) {
                scanner.close();
            }
        } catch (SQLException e) {
            System.err.println("Eroare la închiderea conexiunii: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DataBaseManager dbManager = new DataBaseManager("tiobe.db");
        dbManager.afiseazaMeniu();
        dbManager.inchideConexiune();
    }
}