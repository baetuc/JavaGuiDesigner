package View;

import org.apache.commons.lang3.ClassUtils;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Cip on 19-May-16.
 */
public class MyJTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Proprietate", "Valoare"};
    private Component component;
    private Object[][] data;
    private boolean[] isEdit;

    public MyJTableModel(Component component) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        this.component = component;
        BeanInfo beanInfo = Introspector.getBeanInfo(this.component.getClass());
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        this.data = new Object[descriptors.length][columnNames.length];
        this.isEdit = new boolean[descriptors.length];
        int i = 0;
        for (PropertyDescriptor descriptor : descriptors) {
            data[i][0] = descriptor.getName();
            Class clazz = descriptor.getPropertyType();
            Method method = descriptor.getReadMethod();
            if (method != null) {
                Object propertyValue = method.invoke(component);
                if (propertyValue != null) {
                    if (clazz.isPrimitive()) {
                        clazz = ClassUtils.primitiveToWrapper(clazz);
                    }
                    if (clazz.equals(Class.forName("java.lang.String")) || clazz.equals(Class.forName("java.lang.Integer")) ||
                            clazz.equals(Class.forName("java.lang.Float")) || clazz.equals(Class.forName("java.lang.Double"))) {
                        isEdit[i] = true;
                    } else {
                        isEdit[i] = false;
                    }
                    data[i][1] = propertyValue.toString();
                } else {
                    isEdit[i] = false;
                    data[i][1] = "";
                }
            } else {
                isEdit[i] = false;
                data[i][1] = "";
            }

            ++i;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column != 1) {
            return false;
        }
        return isEdit[row];
    }

    @Override
    public int getRowCount() {
        return data == null ? 0 : data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames == null ? 0 : columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(this.component.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor descriptor = beanInfo.getPropertyDescriptors()[row];
        Method method = descriptor.getWriteMethod();
        if(method != null) {
            Class clazz = descriptor.getPropertyType();
            if (clazz.isPrimitive()) {
                clazz = ClassUtils.primitiveToWrapper(clazz);
            }
            try {
                if (clazz.equals(Class.forName("java.lang.String"))) {
                    method.invoke(component, value.toString());
                } else if (clazz.equals(Class.forName("java.lang.Integer"))) {
                    method.invoke(component, Integer.parseInt(value.toString()));
                } else if (clazz.equals(Class.forName("java.lang.Double"))) {
                    method.invoke(component, Double.parseDouble(value.toString()));
                } else if (clazz.equals(Class.forName("java.lang.Float"))) {
                    method.invoke(component, Float.parseFloat(value.toString()));
                }

            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            fireTableCellUpdated(row, col);
        }
    }

}
