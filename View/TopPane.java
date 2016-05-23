package View;

import ClassLoaders.MyClassLoader;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Cip on 18-May-16.
 */
public class TopPane extends JPanel {
    private MyFrame frame;
    private JLabel classLabel;
    private JTextField classText;
    private JButton submitButton;
    private JButton importButton;
    private JButton exportButton;
    private JButton addClass;
    private JFileChooser fileChooser;

    public TopPane(MyFrame frame) {
        this.frame = frame;
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        this.setBorder(padding);

        this.setLayout(new BorderLayout(10, 10));
        this.classLabel = new JLabel("Numele clasei:");
        this.classText = new JTextField();
        this.submitButton = new JButton("Adaugă compnentă");
        this.importButton = new JButton("Importă");
        this.exportButton = new JButton("Exportă");
        this.addClass = new JButton("Adaugă clasă");

        this.add(classLabel, BorderLayout.WEST);
        this.add(classText, BorderLayout.CENTER);
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 10));
        panel.add(submitButton);
        panel.add(importButton);
        panel.add(exportButton);
        panel.add(addClass);
        this.add(panel, BorderLayout.EAST);


        this.classLabel.setFont(this.getFont().deriveFont(20f));
        this.classLabel.setBounds(0, 0, 30, 30);

        this.submitButton.setFont(this.getFont().deriveFont(20f));
        this.classText.setFont(this.getFont().deriveFont(20f));
        this.importButton.setFont(this.getFont().deriveFont(20f));
        this.exportButton.setFont(this.getFont().deriveFont(20f));
        this.addClass.setFont(this.getFont().deriveFont(20f));

        this.submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendNewComponent(classText.getText());
            }
        });

        importButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    frame.importFile(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        exportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
                int returnVal = fileChooser.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    frame.exportFile(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        addClass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                URLClassLoader oldClassLoader = (URLClassLoader) this.getClass().getClassLoader();
                fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        URLClassLoader classLoader = new URLClassLoader(new URL[]{fileChooser.getSelectedFile().toURI().toURL()},
                                frame.getClass().getClassLoader());
                    } catch (MalformedURLException e1) {
                        ErrorMessage.showErrorMessage(frame, e1.getMessage());
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendNewComponent(String className) {
        this.frame.addNewComponent(className);
    }


}
