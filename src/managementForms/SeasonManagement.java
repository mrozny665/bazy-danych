package managementForms;

import connections.DatabaseConnection;
import model.Serial;

import javax.swing.*;

public class SeasonManagement {
    private JPanel panel1;
    private JList<String> list1;
    private JButton newSeasonButton;
    private JButton newDvdButton;
    private JButton refreshButton;
    private static Serial serial;

    public SeasonManagement() {
        refreshList();
        list1.addListSelectionListener(_ -> newDvdButton.setEnabled(true));
        newSeasonButton.addActionListener(_ -> {
            DatabaseConnection.addNewSeason(serial);
            serial.seasons += 1;
            refreshList();
        });
        newDvdButton.addActionListener(_ -> {
            long id = DatabaseConnection.getSerialId(serial);
            DatabaseConnection.addNewSerialDvd(id, list1.getSelectedIndex()+1);
        });
        refreshButton.addActionListener(_ -> refreshList());
    }

    public void refreshList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < serial.seasons; i++) {
            listModel.addElement(STR."Sezon \{i+1}");
        }
        list1.setModel(listModel);
    }

    public static void run(Serial serial) {
        SeasonManagement.serial = serial;
        JFrame frame = new JFrame("SeasonManagement");
        frame.setContentPane(new SeasonManagement().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
