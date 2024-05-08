package reactionForms;

import connections.DatabaseConnection;
import managementForms.FilmManagement;
import model.Film;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectTest {
    static AddNewFilm anf;
    static String[][] data;

    @BeforeAll
    static void setUp() {
        anf = new AddNewFilm();
        AddNewFilm.frame = new JFrame("AddNewFilm");
        data = new String[][]{
                {"qwe", "qwe", "2001"},
                {"asd", "asd", "2030"},
                {"qwe", "qwe", "2001"}
        };
    }

    @ParameterizedTest
    @MethodSource("provideArgsForTest")
    @Order(1)
    void addNewFilmTest(String name, String director, String year, boolean assertion) {
        assertEquals(assertion, anf.addNewFilm(name,director,year));
    }

    @Test
    @Order(2)
    void getByIdTest(){
        Film film = new Film(data[0][0], data[0][1], Integer.parseInt(data[0][2]));
        long id = DatabaseConnection.getFilmId(film);
        Film newFilm = DatabaseConnection.getFilmById(id);
        assertEquals(film.name, newFilm.name);
        assertEquals(film.director, newFilm.director);
        assertEquals(film.year, newFilm.year);
    }

    @Test
    @Order(3)
    void addNewFilmDvdTest(){
        FilmManagement fm = new FilmManagement();
        Film film = new Film(data[0][0], data[0][1], Integer.parseInt(data[0][2]));
        assertTrue(fm.addNewDvd(film));
    }

    private static Stream<Arguments> provideArgsForTest(){
        return Stream.of(
                Arguments.of(data[0][0], data[0][1], data[0][2], true),
                Arguments.of(data[1][0], data[1][1], data[1][2], false),
                Arguments.of(data[2][0], data[2][1], data[2][2], false)
        );
    }
}