package reactionForms;

import connections.DatabaseConnection;
import connections.DatabaseConnectionGetLists;
import model.Dvd;
import model.Rental;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class RentalsByClient {
    private JPanel panel1;
    private JList<String> list1;

    public RentalsByClient(long clientId) {
        List<Rental> rentals = DatabaseConnectionGetLists.getRentalsByClient(clientId);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Rental rental : rentals) {
            boolean returned = DatabaseConnection.checkIfReturned(rental.id);
            Dvd dvd = DatabaseConnection.getDvdById(rental.dvdId);
            if (!returned) {
                listModel.addElement(STR."\{dvd.title} | \{rental.start} \{rental.end}");
            } else {
                LocalDate returnDate = DatabaseConnection.getReturnDate(rental.id);
                listModel.addElement(STR."\{dvd.title} | \{rental.start} \{rental.end} | Zwrócono: \{returnDate}");
            }
        }
        list1.setModel(listModel);
    }

    public static void run(long clientId) {
        JFrame frame = new JFrame("RentalsByClient");
        frame.setContentPane(new RentalsByClient(clientId).panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
