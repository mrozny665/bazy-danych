package managementForms;

import connections.*;
import model.Film;
import reactionForms.AddNewFilm;

import javax.swing.*;
import java.util.List;

public class FilmManagement {
    public JPanel panel1;
    private JList<String> list1;
    private JButton refreshButton;
    private JButton newFilmButton;
    private JButton newDvdButton;
    private List<Film> films;

    public FilmManagement() {
        refreshList();
        list1.addListSelectionListener(_ -> newDvdButton.setEnabled(true));
        newFilmButton.addActionListener(_ -> AddNewFilm.main(null));
        refreshButton.addActionListener(_ -> refreshList());
        newDvdButton.addActionListener(_ -> {
            int i = list1.getSelectedIndex();
            addNewDvd(films.get(i));
        });
    }

    public void refreshList() {
        films = DatabaseConnectionGetLists.getFilms(true);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Film film : films) listModel.addElement(STR."\{film.name} | \{film.director} | \{film.year}");
        list1.setModel(listModel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FilmManagement");
        frame.setContentPane(new FilmManagement().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public boolean addNewDvd(Film film){
        long id = DatabaseConnection.getFilmId(film);
        if (id != 0){
            return DatabaseConnection.addNewFilmDvd(id);
        }
        return false;
    }
}
