package reactionForms;

import connections.DatabaseConnection;

import javax.swing.*;

public class AddNewFilm {
    private JTextField nameField;
    private JPanel panel1;
    private JTextField yearField;
    private JTextField seasonsField;
    private JButton addButton;
    private JButton cancelButton;

    static JFrame frame;

    public AddNewFilm() {
        addButton.addActionListener(_ -> {
            addNewFilm(nameField.getText(), yearField.getText(), seasonsField.getText());
        });
        cancelButton.addActionListener(_ -> frame.dispose());
    }

    public static void main(String[] args) {
        frame = new JFrame("AddNewFilm");
        frame.setContentPane(new AddNewFilm().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public boolean addNewFilm(String filmName, String directorName, String prodYear){
        try {
            int prodYear2 = Integer.parseInt(prodYear);
            if (prodYear2 < 1900 || prodYear2 > 2024) throw new NumberFormatException();
            if (DatabaseConnection.addFilm(filmName, directorName, prodYear2)){
                frame.dispose();
                return true;
            }
            else {
                JDialog dialog = new ErrorDialog("Wystąpił błąd połączenia z bazą danych");
                dialog.pack();
                dialog.setVisible(true);
                return false;
            }
        } catch (NumberFormatException ex) {
            JDialog dialog = new ErrorDialog("Błąd: Podany rok jest nieprawidłowy");
            dialog.pack();
            dialog.setVisible(true);
            return false;
        }
    }
}
