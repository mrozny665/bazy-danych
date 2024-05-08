package reactionForms;

import connections.DatabaseConnection;

import javax.swing.*;

public class AddNewClient {
    private JTextField firstNameField;
    private JPanel panel1;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JButton addButton;
    private JButton cancelButton;
    private static JFrame frame;

    public AddNewClient() {
        addButton.addActionListener(_ -> {
            if (phoneField.getText().matches("[0-9]{9}") || phoneField.getText().isEmpty()) { //numer telefonu musi składać się z 9 cyfr lub może nie być go wcale
                DatabaseConnection.addNewClient(firstNameField.getText(), lastNameField.getText(), phoneField.getText());
            } else {
                JDialog dialog = new ErrorDialog("Numer telefonu zawiera błędne znaki lub jest złej długości."); //otwarcie okna błędu
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        cancelButton.addActionListener(_ -> frame.dispose());
    }

    public static void main(String[] args) {
        frame = new JFrame("AddNewClient");
        frame.setContentPane(new AddNewClient().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
