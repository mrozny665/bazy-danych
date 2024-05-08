package managementForms;

import connections.DatabaseConnectionGetLists;
import model.Serial;
import reactionForms.AddNewSerial;

import javax.swing.*;
import java.util.List;

public class SerialManagement {
    private JPanel panel1;
    private JList<String> list1;
    private JButton newSerialButton;
    private JButton refreshButton;
    private JButton seasonListButton;

    private List<Serial> serials;

    public SerialManagement() {
        refreshList();
        list1.addListSelectionListener(_ -> seasonListButton.setEnabled(true));
        newSerialButton.addActionListener(_ -> AddNewSerial.main(null));
        refreshButton.addActionListener(_ -> refreshList());
        seasonListButton.addActionListener(_ -> SeasonManagement.run(serials.get(list1.getSelectedIndex())));
    }

    public void refreshList() {
        serials = DatabaseConnectionGetLists.getSerials(true);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Serial serial : serials) listModel.addElement(STR."\{serial.name} | \{serial.year} | \{serial.seasons}");
        list1.setModel(listModel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SerialManagement");
        frame.setContentPane(new SerialManagement().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
