package connections;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionGetLists {
    public static List<Film> getFilms(boolean isEmployee) {
        String SQL = "SELECT * FROM filmy"; //zapytanie SQL
        ArrayList<Film> films = new ArrayList<>();
        if (isEmployee) { //sprawdzenie czy o listę pyta pracownik czy gość
            try (Connection conn = DatabaseConnection.connect(); //połączenie z bazą danych
                 Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(SQL)) { //wywołanie zapytania
                while (rs.next()) {
                    films.add(new Film(rs.getString("nazwa_filmu"), rs.getString("nazwisko_rezysera"), rs.getInt("rok_produkcji")));
                    //utworzenie nowego obiektu Film, przypisanie mu odpowiednich pól oraz dodanie do listy
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try (Connection conn = DatabaseConnection.connectGuest();
                 Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(SQL)) {
                while (rs.next()) {
                    films.add(new Film(rs.getString("nazwa_filmu"), rs.getString("nazwisko_rezysera"), rs.getInt("rok_produkcji")));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return films; //zwrócenie listy obiektów typu Film
    }

    public static List<Serial> getSerials(boolean isEmployee) {
        String SQL = "SELECT * FROM seriale";
        ArrayList<Serial> serials = new ArrayList<>();
        if (isEmployee) {
            try (Connection conn = DatabaseConnection.connect();
                 Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(SQL)) {
                while (rs.next()) {
                    serials.add(new Serial(rs.getString("nazwa_serialu"), rs.getInt("rok_poczatku_emisji"), rs.getInt("liczba_sezonow")));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try (Connection conn = DatabaseConnection.connectGuest();
                 Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(SQL)) {
                while (rs.next()) {
                    serials.add(new Serial(rs.getString("nazwa_serialu"), rs.getInt("rok_poczatku_emisji"), rs.getInt("liczba_sezonow")));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return serials;
    }

    public static List<Dvd> getDvds() {
        String SQL = """
                SELECT p.id_plyty, p.dostepnosc, f.id_filmu, s.id_serialu, s.numer_sezonu, a.nazwa_filmu, b.nazwa_serialu
                FROM plyty p
                LEFT JOIN plyty_filmow f ON p.id_plyty = f.id_plyty
                LEFT JOIN plyty_seriali s ON p.id_plyty = s.id_plyty
                LEFT JOIN filmy a ON f.id_filmu = a.id_filmu
                LEFT JOIN seriale b ON s.id_serialu = b.id_serialu;
                """;
        ArrayList<Dvd> dvds = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SQL)) {
            while (rs.next()) {
                Dvd dvd;
                if (rs.getInt("id_serialu") == 0){
                    dvd = new Dvd(rs.getLong("id_plyty"), rs.getBoolean("dostepnosc"),
                            rs.getString("nazwa_filmu"), rs.getLong("id_filmu"),
                            true, 0);
                } else {
                    dvd = new Dvd(rs.getLong("id_plyty"), rs.getBoolean("dostepnosc"),
                            rs.getString("nazwa_serialu"),rs.getLong("id_serialu"),
                            false, rs.getInt("numer_sezonu"));
                }
                dvds.add(dvd);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dvds;
    }

    public static List<Rental> getRentals() {
        ArrayList<Rental> rentals = new ArrayList<>();
        String SQL = "SELECT * FROM wypozyczenia";

        try (Connection conn = DatabaseConnection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SQL)) {
            while (rs.next()) {
                rentals.add(new Rental(rs.getLong("id_wypozyczenia"),
                        rs.getDate("data_wypozyczenia").toLocalDate(),
                        rs.getDate("termin_oddania").toLocalDate(),
                        rs.getLong("id_plyty"), rs.getLong("id_klienta")));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return rentals;
    }

    public static List<Client> getClients() {
        String SQL = "SELECT * FROM klienci";
        ArrayList<Client> clients = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SQL)) {
            while (rs.next()) {
                clients.add(new Client(rs.getLong("id_klienta"), rs.getString("imie"), rs.getString("nazwisko"), rs.getString("numer_telefonu")));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return clients;
    }

    public static List<Payment> getPayments() {
        String SQL = "SELECT * FROM oplaty";
        ArrayList<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SQL)) {
            while (rs.next()) {
                payments.add(new Payment(rs.getLong("id_wypozyczenia"), rs.getLong("id_klienta"), rs.getDouble("kwota_oplaty")));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return payments;
    }

    public static List<Rental> getRentalsByClient(long clientId) {
        ArrayList<Rental> rentals = new ArrayList<>();
        String SQL = """
            SELECT * FROM wypozyczenia
            WHERE id_klienta = ?;
            """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setLong(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rentals.add(new Rental(rs.getLong("id_wypozyczenia"),
                        rs.getDate("data_wypozyczenia").toLocalDate(),
                        rs.getDate("termin_oddania").toLocalDate(),
                        rs.getLong("id_plyty"), rs.getLong("id_klienta")));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return rentals;
    }
}
