package connections;

import model.*;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseConnection {

    public static final String url = "jdbc:postgresql://localhost/ProjectDB";
    public static final String user = "employee";
    public static final String guestUser = "guest";

    public static Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, user);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public static Connection connectGuest(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, guestUser, guestUser);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static boolean addFilm(String title, String director, int year) {
        String SQL = """ 
                INSERT INTO filmy(nazwa_filmu, nazwisko_rezysera, rok_produkcji)
                VALUES (?, ?, ?)
                """; //wywołanie SQL, znaki zapytania oznaczają dane do wypełnienia
        try (Connection conn = connect(); //połączenie z bazą
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);)
        {
            pstmt.setString(1, title); //wypełnienie pierwszego znaku zapytanie
            pstmt.setString(2, director);
            pstmt.setInt(3, year);
            pstmt.executeUpdate(); //wywołanie zapytania
            return true; //jeżeli nie przechwycono żadnego wyjątku - zgłoszenie pomyślnego wykonania zadania
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false; //jeżeli przechwycono wyjątek - zgłoszenie problemu
        }
    }

    public static boolean addSerial(String title, int year, int seasons) {
        String SQL = """
                INSERT INTO seriale(nazwa_serialu, rok_poczatku_emisji, liczba_sezonow)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, year);
            pstmt.setInt(3, seasons);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean addNewFilmDvd(long filmId){
        long id = addNewDvd();
        String SQL = """
                INSERT INTO plyty_filmow(id_filmu, id_plyty)
                VALUES (?, ?)
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, filmId);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean addNewSerialDvd(long serialId, int season){
        long id = addNewDvd();
        String SQL = """
                INSERT INTO plyty_seriali(id_serialu, id_plyty, numer_sezonu)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, serialId);
            pstmt.setLong(2, id);
            pstmt.setInt(3, season);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static long addNewDvd() {
        String SQL = "INSERT INTO plyty(dostepnosc) VALUES (true)";
        long id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }

    public static long getFilmId(Film film) {
        String SQL = """
                    SELECT id_filmu FROM filmy
                    WHERE nazwa_filmu = ? AND nazwisko_rezysera = ? AND rok_produkcji = ?
                    """;
        long id = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, film.name);
            pstmt.setString(2, film.director);
            pstmt.setInt(3, film.year);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            id = rs.getLong(1);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return id;
    }

    public static long getSerialId(Serial serial) {
        String SQL = """
                SELECT id_serialu FROM seriale
                WHERE nazwa_serialu = ? AND rok_poczatku_emisji = ? AND liczba_sezonow = ?
                """;
        long id = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, serial.name);
            pstmt.setInt(2, serial.year);
            pstmt.setInt(3, serial.seasons);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            id = rs.getLong(1);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return id;
    }

    public static long getClientId(Client client) {
        String SQL = """
                    SELECT id_klienta FROM klienci
                    WHERE imie = ? AND nazwisko = ?
                    """;

        long id = 0;
        try (Connection con = connect();
            PreparedStatement pstmt = con.prepareStatement(SQL)){
            pstmt.setString(1, client.firstName);
            pstmt.setString(2, client.lastName);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            id = rs.getLong(1);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return id;
    }

    public static void addNewSeason(Serial serial) {
        long id = getSerialId(serial);
        String SQL = """
                    UPDATE seriale
                    SET liczba_sezonow = ?
                    WHERE id_serialu = ?
                    """;

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, serial.seasons + 1);
            pstmt.setLong(2, id);
            pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addNewClient(String firstName, String lastName, String phoneNumber){
        String SQL = """
                    INSERT INTO klienci(imie, nazwisko, numer_telefonu)
                    VALUES (?, ?, ?)
                    """;

        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);
            pstmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void removeClient(Client client){
        long id = getClientId(client);
        String SQL = "DELETE FROM klienci WHERE id_klienta = ?";

        try (Connection con = connect();
            PreparedStatement pstmt = con.prepareStatement(SQL)){
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void removeDvd(Dvd dvd) {
        String SQL = "DELETE FROM ? WHERE id_plyty = ?";
        String SQL2 = "DELETE FROM plyty WHERE id_plyty = ?";
        try (Connection con = connect();
            PreparedStatement pstmt = con.prepareStatement(SQL)){
            if (dvd.isFilm) {
                pstmt.setString(1, "plyty_filmow");
            } else {
                pstmt.setString(1, "plyty_seriali");
            }
            pstmt.setLong(2,dvd.id);
            pstmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        try (Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(SQL2)){
            pstmt.setLong(1,dvd.id);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean addnewRental(Dvd dvd, Client client) {
        String SQL = """
                    INSERT INTO wypozyczenia(data_wypozyczenia, termin_oddania, id_plyty, id_klienta)
                    VALUES (?, ?, ?, ?)
                    """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setDate(2, Date.valueOf(LocalDate.now().plusDays(14)));
            pstmt.setLong(3, dvd.id);
            pstmt.setLong(4, client.id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Dvd getDvdById(long id){
        Dvd dvd = null;
        String SQL = """
                SELECT * FROM widok_dvd w
                WHERE w.id_plyty = ?;
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.getObject("id_serialu") == null) {
                dvd = new Dvd(id, false, rs.getString("nazwa_filmu"),
                        rs.getLong("id_filmu"), true, 0);
            } else {
                dvd = new Dvd(id, false, rs.getString("nazwa_serialu"),
                        rs.getLong("id_serialu"), false, rs.getInt("numer_sezonu"));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dvd;
    }

    public static Client getClientById(long id){
        Client client = null;
        String SQL = """
                SELECT * FROM klienci
                WHERE id_klienta = ?
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            client = new Client(id, rs.getString("imie"), rs.getString("nazwisko"), rs.getString("numer_telefonu"));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return client;
    }

    public static boolean checkIfReturned(long id) {
        String SQL = """
                SELECT COUNT(1)
                FROM zwroty
                WHERE id_wypozyczenia = ?;
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return (rs.getInt("count") == 1);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static LocalDate getReturnDate(long id){
        String SQL = """
                SELECT data_zwrotu
                FROM zwroty
                WHERE id_wypozyczenia = ?;
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getDate("data_zwrotu").toLocalDate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Film getFilmById(long id){
        Film film = null;
        String SQL = """
                SELECT * FROM filmy
                WHERE id_filmu = ?
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            film = new Film(rs.getString("nazwa_filmu"), rs.getString("nazwisko_rezysera"),
                    rs.getInt("rok_produkcji"));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return film;
    }

    public static Serial getSerialById(long id){
        Serial serial = null;
        String SQL = """
                SELECT * FROM seriale
                WHERE id_serialu = ?
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            serial = new Serial(rs.getString("nazwa_serialu"), rs.getInt("rok_poczatku_emisji"), rs.getInt("liczba_sezonow"));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return serial;
    }

    public static void addNewReturn(Rental rental) {
        String SQL = """
                INSERT INTO zwroty(id_wypozyczenia, id_plyty, id_klienta, data_zwrotu)
                VALUES (?, ?, ?, ?);
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, rental.id);
            pstmt.setLong(2, rental.dvdId);
            pstmt.setLong(3, rental.clientId);
            pstmt.setDate(4, Date.valueOf(LocalDate.now()));
            pstmt.executeQuery();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void addNewPayment(Rental rental, long between) {
        String SQL = """
                INSERT INTO oplaty(id_wypozyczenia, id_klienta, kwota_oplaty)
                VALUES (?, ?, ?);
                """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setLong(1, rental.id);
            pstmt.setLong(2, rental.clientId);
            pstmt.setDouble(3,(double) between * 0.4);
            pstmt.executeQuery();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
