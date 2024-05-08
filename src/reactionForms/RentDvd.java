package reactionForms;

import connections.DatabaseConnection;
import connections.DatabaseConnectionGetLists;
import model.Client;
import model.Dvd;
import model.Film;
import model.Serial;

import javax.swing.*;
import java.util.List;

public class RentDvd {
    private JPanel panel1;
    private JList<String> list1;
    private JButton rentButton;
    private JButton cancelButton;
    private JLabel infoLabel1;
    private JLabel infoLabel2;
    private JLabel infoLabel3;
    public static JFrame frame;
    private List<Client> clients;

    public RentDvd(Dvd dvd) {
        refreshList();
        if (dvd.isFilm) {
            Film film = DatabaseConnection.getFilmById(dvd.filmSerialid);
            infoLabel1.setText(STR."Tytuł filmu: \{dvd.title}");
            infoLabel2.setText(STR."Rok produkcji: \{film.year}");
            infoLabel3.setText(STR."Reżyser: \{film.director}");
        } else {
            Serial serial = DatabaseConnection.getSerialById(dvd.filmSerialid);
            infoLabel1.setText(STR."Tytuł serialu: \{dvd.title}");
            infoLabel2.setText(STR."Rok początku emisji: \{serial.year}");
            infoLabel3.setText(STR."Numer sezonu: \{dvd.seasonNumber}");
        }
        cancelButton.addActionListener(_ -> frame.dispose());
        rentButton.addActionListener(_ -> {
            DatabaseConnection.addnewRental(dvd, clients.get(list1.getSelectedIndex()));
            frame.dispose();
        });
        list1.addListSelectionListener(_ -> rentButton.setEnabled(true));
    }

    public void refreshList() {
        clients = DatabaseConnectionGetLists.getClients();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Client client : clients) listModel.addElement(STR."\{client.firstName} | \{client.lastName} | \{client.phoneNumber}");
        list1.setModel(listModel);
    }

    public static void run(Dvd dvd) {
        frame = new JFrame("RentDvd");
        frame.setContentPane(new RentDvd(dvd).panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
