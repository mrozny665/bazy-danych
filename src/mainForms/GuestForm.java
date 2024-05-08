package mainForms;

import connections.DatabaseConnectionGetLists;
import model.Film;
import model.Serial;

import javax.swing.*;
import java.util.List;

public class GuestForm {

    private JPanel panel1;
    private JList<String> list1;
    private JList<String> list2;

    public GuestForm() {
        List<Film> films = DatabaseConnectionGetLists.getFilms(false); //pobranie filmów z bazy danych
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Film film : films) listModel.addElement(STR."\{film.name} | \{film.director} | \{film.year}"); //tworzenie listy do wyświetlenia
        list1.setModel(listModel);
        list1.setSelectionModel(new NoSelectionModel()); //wyłączenie możliwości zaznaczania na liście

        List<Serial> serials = DatabaseConnectionGetLists.getSerials(false); //pobranie seriali z bazy danych
        DefaultListModel<String> listModel2 = new DefaultListModel<>();
        for (Serial serial : serials) listModel2.addElement(STR."\{serial.name} | \{serial.year} | \{serial.seasons}"); //tworzenie listy do wyświetlenia
        list2.setModel(listModel2);
        list2.setSelectionModel(new NoSelectionModel()); //wyłączenie możliwości zaznaczania na liście
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GuestForm");
        frame.setContentPane(new GuestForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static class NoSelectionModel extends DefaultListSelectionModel {

        public void setAnchorSelectionIndex(final int anchorIndex) {}

        public void setLeadAnchorNotificationEnabled(final boolean flag) {}

        public void setLeadSelectionIndex(final int leadIndex) {}
        public void setSelectionInterval(final int index0, final int index1) {}
    }
}
