package reactionForms;

import connections.DatabaseConnection;

import javax.swing.*;

public class AddNewSerial {

    private JTextField nameField;
    private JPanel panel1;
    private JTextField yearField;
    private JTextField seasonsField;
    private JButton dodajButton;
    private JButton anulujButton;

    private static JFrame frame;

    public AddNewSerial() {
        dodajButton.addActionListener(_ -> {
            String serialName = nameField.getText();
            String premYear = yearField.getText();
            String seasonCount = seasonsField.getText();
            try {
                int premYear2 = Integer.parseInt(premYear);
                if (premYear2 < 1900 || premYear2 > 2024) throw new NumberFormatException();
                int seasonCount2 = Integer.parseInt(seasonCount);
                if (DatabaseConnection.addSerial(serialName, premYear2, seasonCount2)){
                    frame.dispose();
                }
                else {
                    JDialog dialog = new ErrorDialog("Wystąpił błąd połączenia z bazą danych");
                    dialog.pack();
                    dialog.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                JDialog dialog = new ErrorDialog("Błąd: Podany rok jest nieprawidłowy");
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        anulujButton.addActionListener(_ -> frame.dispose());
    }

    public static void main(String[] args) {
        frame = new JFrame("AddNewSerial");
        frame.setContentPane(new AddNewSerial().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
