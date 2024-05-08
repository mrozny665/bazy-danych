package managementForms;

import connections.DatabaseConnection;
import connections.DatabaseConnectionGetLists;
import model.Client;
import model.Payment;

import javax.swing.*;
import java.util.List;

public class PaymentManagement {
    private JPanel panel1;
    private JList<String> list1;

    public PaymentManagement() {
        List<Payment> payments = DatabaseConnectionGetLists.getPayments();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Payment payment : payments){
            Client client = DatabaseConnection.getClientById(payment.clientId);
            listModel.addElement(STR."\{client.firstName} \{client.lastName} | \{payment.payment}");
        }
        list1.setModel(listModel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PaymentManagement");
        frame.setContentPane(new PaymentManagement().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
