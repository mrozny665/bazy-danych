package mainForms;

import reactionForms.ErrorDialog;

import javax.swing.*;
import java.util.Objects;

public class MainForm {
    public JPanel panel1;
    private JTextField loginField;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton guestLoginButton;

    private static JFrame frame;

    private final String login = "employee1";
    private final String password = "Qwerty123";

    public MainForm(){
        loginButton.addActionListener(_ -> {
            String stringPassword = new String(passwordField1.getPassword());
            if (Objects.equals(loginField.getText(), login) && Objects.equals(stringPassword, password)) { //sprawdzenie poprawności loginu i hasła
            //if (true) {
                EmployeeForm.main(null); //przejście do okna pracownika
                frame.dispose();
            } else {
                JDialog dialog = new ErrorDialog("Nieprawidłowy login lub hasło"); //okienko dialogowe z błędem
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        guestLoginButton.addActionListener(_ -> {
            GuestForm.main(null); //przejście do okna gościa
            frame.dispose();
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
