package View;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Created by Cip on 19-May-16.
 */
public class ErrorMessage {
    public static void showErrorMessage(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
