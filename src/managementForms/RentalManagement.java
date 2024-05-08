package managementForms;

import connections.DatabaseConnection;
import connections.DatabaseConnectionGetLists;
import model.Client;
import model.Dvd;
import model.Rental;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class RentalManagement {
    private JPanel panel1;
    private JList<String> list1;
    private JButton returnButton;
    private JButton refreshButton;
    private List<Rental> rentals;

    public RentalManagement() {
        refreshList();
        returnButton.addActionListener(_ -> {
            Rental rental = rentals.get(list1.getSelectedIndex());
            DatabaseConnection.addNewReturn(rental);
            if (LocalDate.now().isAfter(rental.end)){
                DatabaseConnection.addNewPayment(rental, DAYS.between(rental.end, LocalDate.now()));
            }
            refreshList();
        });
        refreshButton.addActionListener(_ -> refreshList());
        list1.addListSelectionListener(_ -> returnButton.setEnabled(list1.getSelectedIndex() >= 0));
    }

    private void refreshList() {
        rentals = DatabaseConnectionGetLists.getRentals();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Rental rental : rentals) {
            boolean returned = DatabaseConnection.checkIfReturned(rental.id);
            Dvd dvd = DatabaseConnection.getDvdById(rental.dvdId);
            Client client = DatabaseConnection.getClientById(rental.clientId);
            if (!returned) {
                listModel.addElement(STR."\{dvd.title} | \{client.firstName} \{client.lastName} | \{rental.start} \{rental.end}");
            } else {
                LocalDate returnDate = DatabaseConnection.getReturnDate(rental.id);
                listModel.addElement(STR."\{dvd.title} | \{client.firstName} \{client.lastName} | \{rental.start} \{rental.end} | Zwrócono: \{returnDate}");
            }
        }
        list1.setModel(listModel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RentalManagement");
        frame.setContentPane(new RentalManagement().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
