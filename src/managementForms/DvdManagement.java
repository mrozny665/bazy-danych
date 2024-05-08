package managementForms;

import connections.DatabaseConnection;
import connections.DatabaseConnectionGetLists;
import model.Dvd;
import reactionForms.RentDvd;

import javax.swing.*;
import java.util.List;

public class DvdManagement {
    private JPanel panel1;
    private JButton deleteButton;
    private JButton rentButton;
    private JButton refreshButton;
    private JList<String> list1;
    private List<Dvd> dvds;

    public DvdManagement() {
        refreshList();
        refreshButton.addActionListener(_ -> refreshList());
        deleteButton.addActionListener(_ -> {
            DatabaseConnection.removeDvd(dvds.get(list1.getSelectedIndex()));
            refreshList();
        });
        rentButton.addActionListener(_ -> {
            RentDvd.run(dvds.get(list1.getSelectedIndex()));
            refreshList();
        });
        list1.addListSelectionListener(_ -> {
            if (list1.getSelectedIndex() >= 0) {
                if (dvds.get(list1.getSelectedIndex()).availability) {
                    deleteButton.setEnabled(true);
                    rentButton.setEnabled(true);
                } else {
                    deleteButton.setEnabled(false);
                    rentButton.setEnabled(false);
                }
            } else {
                deleteButton.setEnabled(false);
                rentButton.setEnabled(false);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DvdManagement");
        frame.setContentPane(new DvdManagement().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void refreshList(){
        dvds = DatabaseConnectionGetLists.getDvds();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Dvd dvd : dvds) {
            if (dvd.isFilm){
                if (dvd.availability) listModel.addElement(STR."\{dvd.title} | Dostępne");
                else listModel.addElement(STR."\{dvd.title} | Niedostępne");
            } else {
               if (dvd.availability) listModel.addElement(STR."\{dvd.title} | Sezon \{dvd.seasonNumber} | Dostępne");
               else listModel.addElement(STR."\{dvd.title} | Sezon \{dvd.seasonNumber} | Niedostępne");
            }
        }
        list1.setModel(listModel);
    }
}
