package mainForms;

import managementForms.*;

import javax.swing.*;

public class EmployeeForm {
    private JButton filmButton;
    private JButton dvdButton;
    public JPanel panel1;
    private JButton serialButton;
    private JButton clientButton;
    private JButton rentalButton;
    private JButton paymentButton;

    public EmployeeForm() {
        filmButton.addActionListener(_ -> FilmManagement.main(null));
        serialButton.addActionListener(_ -> SerialManagement.main(null));
        dvdButton.addActionListener(_ -> DvdManagement.main(null));
        clientButton.addActionListener(_ -> ClientManagement.main(null));
        rentalButton.addActionListener(_ -> RentalManagement.main(null));
        paymentButton.addActionListener(_ -> PaymentManagement.main(null));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EmployeeForm");
        frame.setContentPane(new EmployeeForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
