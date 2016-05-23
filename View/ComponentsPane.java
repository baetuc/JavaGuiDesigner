package View;

import javax.swing.JDesktopPane;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * Created by Cip on 18-May-16.
 */
public class ComponentsPane extends JDesktopPane {
    private static final int COMPONENT_WIDTH = 150;
    private static final int COMPONENT_HEIGHT = 100;
    private static final int NUMBER_OF_POSITIONING_RETRIES = 100;
    MyFrame frame;

    public ComponentsPane(MyFrame frame) {
        this.frame = frame;
        this.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        this.setLayout(null);
        this.setBackground(new Color(200, 200, 200));
    }

    void addNewComponent(String className) {
        try {

            Class clazz = Class.forName(className);
            Component component = (Component) clazz.newInstance();
            component.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    displayProperties(component);
                }
            });
            setComponentText(component, "Cip");
            setComponentBoundsAndRepaint(component);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            ErrorMessage.showErrorMessage(frame, e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            ErrorMessage.showErrorMessage(frame, e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            ErrorMessage.showErrorMessage(frame, e.getMessage());
        }
    }

    private boolean intersectsComponents(Component newComponent, Component[] oldComponents) {
        for (Component component : oldComponents) {
            if (component.getBounds().intersects(newComponent.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private void setComponentText(Component component, String componentText)
            throws InvocationTargetException, IllegalAccessException {
        Method[] methods = component.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("setText")) {
                method.invoke(component, componentText);
            }
        }
    }

    private void setComponentBoundsAndRepaint(Component component) {
        Random random = new Random();

        component.setBounds(random.nextInt(this.getWidth() - COMPONENT_WIDTH),
                random.nextInt(this.getHeight() - COMPONENT_HEIGHT), COMPONENT_WIDTH,
                COMPONENT_HEIGHT);
        for (int tries = 0; tries < NUMBER_OF_POSITIONING_RETRIES; ++tries) {
            if (intersectsComponents(component, this.getComponents())) {
                component.setBounds(random.nextInt(this.getWidth() - COMPONENT_WIDTH),
                        random.nextInt(this.getHeight() - COMPONENT_HEIGHT), COMPONENT_WIDTH,
                        COMPONENT_HEIGHT);
            } else {
                this.add(component);
                this.repaint();
                break;
            }
        }
    }

    private void displayProperties(Component component) {
        MyInternalFrame internalFrame = new MyInternalFrame(this, component);
        internalFrame.setVisible(true);
        this.add(internalFrame);
        try {
            internalFrame.setSelected(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    void importFile(String fileName) {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean stop = false;
        while (!stop) {
            try {
                this.add((Component) decoder.readObject());
            } catch (ArrayIndexOutOfBoundsException e) {
                stop = true;
            }
        }
        if (decoder != null) {
            decoder.close();
        }
    }

    void exportFile(String fileName) {
        XMLEncoder encoder = null;

        try {
            encoder = new XMLEncoder(new FileOutputStream(fileName));
            for (Component component : this.getComponents()) {
                encoder.writeObject(component);
            }
        } catch (FileNotFoundException e) {
            ErrorMessage.showErrorMessage(frame, e.getMessage());
            e.printStackTrace();
        }
        if (encoder != null) {
            encoder.close();
        }
    }
}
