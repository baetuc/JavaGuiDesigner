package View;

import javax.swing.JPanel;

import javax.swing.JFrame;
import java.awt.*;

/**
 * Created by Cip on 18-May-16.
 */
public class MyFrame extends JFrame {
    TopPane topPane;
    private ComponentsPane componentsPane;


    public MyFrame(String title) {
        super(title);
        setBackground(Color.gray);

        JPanel frontPanel = new JPanel(new BorderLayout());
        this.getContentPane().add(frontPanel);

        this.topPane = new TopPane(this);
        this.componentsPane = new ComponentsPane(this);

        frontPanel.add(topPane, BorderLayout.NORTH);
        frontPanel.add(componentsPane, BorderLayout.CENTER);


        this.setVisible(true);
    }

    void addNewComponent(String className) {
        this.componentsPane.addNewComponent(className);
    }

    void exportFile(String fileName) {
        this.componentsPane.exportFile(fileName);
    }

    void importFile(String fileName) {
        this.componentsPane.importFile(fileName);
    }
}
