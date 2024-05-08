package reactionForms;

import javax.swing.*;

public class ErrorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel label1;

    public ErrorDialog(String text) {
        label1.setText(text);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(_ -> onOK());
    }

    private void onOK() {
        dispose();
    }
}
