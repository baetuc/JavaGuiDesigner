package View;

import javax.swing.JScrollPane;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Cip on 18-May-16.
 */
public class MyInternalFrame extends JInternalFrame {
    private ComponentsPane componentsPane;
    private Component component;
    private JTable table;

    static int openFrameCount = 1;
    static final int xOffset = 30, yOffset = 30;

    public MyInternalFrame(ComponentsPane componentsPane, Component component) {
        super("Component information", true, true, true, true);
        this.componentsPane = componentsPane;
        this.component = component;
        this.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
        ++openFrameCount;
        this.setSize(300, 300);
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                --openFrameCount;
            }
        });

        JScrollPane tablePane = new JScrollPane();
        JTable table = new JTable();
        tablePane.add(table);
        tablePane.setViewportView(table);
        try {
            table.setModel(new MyJTableModel(component));
//            table.setDefaultRenderer(Component.class, new TableRenderer(table.getDefaultRenderer(Component.class)));
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException |
                ClassNotFoundException | InstantiationException e) {
            ErrorMessage.showErrorMessage(this.componentsPane.frame, e.getMessage());
            e.printStackTrace();
        }
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.BOLD, 17));

        this.add(tablePane);
    }



}
