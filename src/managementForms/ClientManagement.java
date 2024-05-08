package managementForms;

import connections.*;
import model.Client;
import reactionForms.AddNewClient;
import reactionForms.RentalsByClient;

import javax.swing.*;
import java.util.List;

public class ClientManagement {
    private JPanel panel1;
    private JList<String> list1;
    private JButton newClientButton;
    private JButton removeClientButton;
    private JButton dvdsButton;
    private JButton refreshButton;
    private List<Client> clients;

    public ClientManagement() {
        refreshList();
        newClientButton.addActionListener(_ -> AddNewClient.main(null));
        removeClientButton.addActionListener(_ -> DatabaseConnection.removeClient(clients.get(list1.getSelectedIndex())));
        dvdsButton.addActionListener(_ -> RentalsByClient.run(clients.get(list1.getSelectedIndex()).id));
        refreshButton.addActionListener(_ -> refreshList());
        list1.addListSelectionListener(_ -> {
            removeClientButton.setEnabled(list1.getSelectedIndex() >= 0);
            dvdsButton.setEnabled(list1.getSelectedIndex() >= 0);
        });
    }

    public void refreshList() {
        clients = DatabaseConnectionGetLists.getClients();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Client client : clients) listModel.addElement(STR."\{client.firstName} | \{client.lastName} | \{client.phoneNumber}");
        list1.setModel(listModel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClientManagement");
        frame.setContentPane(new ClientManagement().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
