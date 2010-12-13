package com.towel.swing.table.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/**
 * A TableModel that bases in a list of objects and displays them according to a
 * list of columns. Several useful methods such as add, addAll, remove,
 * removeAll are already supplied, all of them firing the appropriate events to
 * the table.
 * 
 * @param <T> The class that the tables works with.
 * 
 * @author Vinicius Godoy
 */
public class ColumnTableModel<T> extends AbstractTableModel
{
    @SuppressWarnings("unchecked")
    public static <K, V> ColumnTableModel<Map.Entry<K, V>> createMapModel(
            Map<K, V> map, String keyHeader, String valueHeader)
    {
        Column<Map.Entry<K, V>> keyColumn = new AbstractColumn<Map.Entry<K, V>>(
                keyHeader, 1)
        {
            public Object getValue(Entry<K, V> element)
            {
                return element.getKey();
            }
        };

        Column<Map.Entry<K, V>> valueColumn = new AbstractColumn<Map.Entry<K, V>>(
                valueHeader, 1)
        {
            public Object getValue(Entry<K, V> element)
            {
                return element.getValue();
            }
        };

        return new ColumnTableModel<Map.Entry<K, V>>(map.entrySet(), keyColumn,
                valueColumn);
    }

    /**
     * Apply the column information to the table. New table columns will be
     * created and added, with the properties specified in the columns list.
     * <p>
     * This method ignore the modelIndex() property of the column, and replace
     * it by the list order itself.
     * 
     * @param table The table to update.
     * @param columns Columns to be created.
     */
    public static void applyToTable(JTable table,
            List< ? extends Column< ? >> columns)
    {
        int i = 0;
        for (Column<?> c : columns)
        {
            TableColumn col = new TableColumn(i++, c.getWidth());
            col.setHeaderValue(c.getName());
            if (c.getRenderer() != null)
                col.setCellRenderer(c.getRenderer());
            if (c instanceof EditableColumn)
            {
                EditableColumn<?> ec = (EditableColumn<?>) c;
                if (ec.getEditor() != null)
                    col.setCellEditor(ec.getEditor());
            }
            table.addColumn(col);
        }
    }

    /**
     * Apply the column information to the table. New table columns will be
     * created and added, with the properties specified in the columns list.
     * <p>
     * This method ignore the modelIndex() property of the column, and replace
     * it by the list order itself.
     * 
     * @param table The table to update.
     * @param columns Columns to be created.
     */
    public static void applyToTable(JTable table, Column< ? >... columns)
    {
        applyToTable(table, Arrays.asList(columns));
    }

    private List<Column<T>> columns;
    private List<T> values;
    private boolean isReadOnly = false;

    /**
     * Create a new column table model.
     * 
     * @param values Values in the model.
     * @param columns Columns of the model.
     */
    public ColumnTableModel(Collection<T> values,
            List< ? extends Column<T>> columns)
    {
        if (columns == null)
            throw new IllegalArgumentException("Columns cannot be null!");
        if (columns.size() == 0)
            throw new IllegalArgumentException(
                    "You must provide at least one column!");

        if (values == null)
            throw new IllegalArgumentException("Values can't be null!");

        this.columns = new ArrayList<Column<T>>(columns);
        this.values = new ArrayList<T>(values);
    }

    /**
     * Create a new column table model.
     * 
     * @param values Values in the model.
     * @param columns Columns of the model.
     */
    public ColumnTableModel(Collection<T> values, Column<T>... columns)
    {
        this(values, Arrays.asList(columns));
    }

    public ColumnTableModel(Column<T>... columns)
    {
        this(new ArrayList<T>(), Arrays.asList(columns));
    }

    public int getColumnCount()
    {
        return columns.size();
    }

    public int getRowCount()
    {
        return values.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return columns.get(columnIndex).getValue(values.get(rowIndex));
    }

    @Override
    public Class< ? > getColumnClass(int columnIndex)
    {
        return columns.get(columnIndex).getColumnClass();
    }

    @Override
    public String getColumnName(int column)
    {
        return columns.get(column).getName();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return !isReadOnly
                && columns.get(columnIndex) instanceof EditableColumn
                && ((EditableColumn<T>) columns.get(columnIndex)).isEditable(values.get(rowIndex));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (aValue == getValueAt(rowIndex, columnIndex))
            return;

        ((EditableColumn<T>) (columns.get(columnIndex))).setValue(
                values.get(rowIndex), aValue);

        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public List<T> getValues()
    {
        return Collections.unmodifiableList(values);
    }

    public List<Column<T>> getColumns()
    {
        return Collections.unmodifiableList(columns);
    }

    /**
     * Clears all data in the model.
     */
    public void clear()
    {
        values.clear();
        fireTableDataChanged();
    }

    /**
     * Adds a new element to the model.
     * 
     * @param The element to add.
     */
    public void add(T element)
    {
        values.add(element);
        fireTableRowsInserted(values.size() - 1, values.size() - 1);
    }

    /**
     * Adds all the given elements to the model.
     * 
     * @param elements Elements to add.
     */
    public void addAll(T... elements)
    {
        addAll(Arrays.asList(elements));
    }

    /**
     * Adds all the given elements to the model.
     * 
     * @param elements Elements to add.
     */
    public void addAll(Collection< ? extends T> elements)
    {
        for (T t : elements)
            add(t);
    }

    /**
     * Remove the element from the model.
     * 
     * @param element The element to remove
     * @return True if the model was changed, false if not.
     */
    public boolean remove(T element)
    {
        int index = values.indexOf(element);
        if (index == -1)
            return false;

        remove(index);
        return true;
    }

    /**
     * Remove the element from the given model row.
     * 
     * @param element The row of the element to remove
     * @return The removed element.
     * @throws IndexOutOfBoundException If the row is outside the model
     *         boundaries.
     */
    public T remove(int row)
    {
        T value = values.remove(row);
        fireTableRowsDeleted(row, row);
        return value;
    }

    /**
     * Remove all the indices from the model. There's no need to sort the
     * indices vector prior to calling this method.
     * 
     * @param indices Indices to remove.
     * @return All removed elements.
     * @throws IndexOutOfBoundsException If any index is out of the table
     *         boundaries.
     */    
    public List<T> removeAll(int... indices)
    {
        for (int index : indices)
            if (index < 0 || index > values.size())
                throw new IndexOutOfBoundsException("Index " + index
                        + " out of bounds");

        SortedSet<Integer> indexes = new TreeSet<Integer>(
                new Comparator<Integer>()
                {
                    public int compare(Integer o1, Integer o2)
                    {
                        return o2.compareTo(o1);
                    }
                });

        for (int index : indices)
            indexes.add(index);

        List<T> elements = new ArrayList<T>();
        for (Integer index : indexes)
            elements.add(0, remove(index));
        return elements;
    }

    /**
     * @return the number of rows in this table model.
     */
    public int getSize()
    {
        return values.size();
    }

    /**
     * Indicate if data modifications are allowed for this model.
     * 
     * @return True if the model is read-only, false if not.
     */
    public boolean isReadOnly()
    {
        return isReadOnly;
    }

    /**
     * Make this table model read-only.
     * 
     * @param isReadOnly True to make the model read-only, false to allow
     *        editing.
     */
    public void setReadOnly(boolean isReadOnly)
    {
        this.isReadOnly = isReadOnly;
    }

    /**
     * @param user
     * @return
     */
    public int indexOf(T element)
    {
        return values.indexOf(element);
    }

    public T get(int row)
    {
        return values.get(row);
    }

    /**
     * @param row
     * @param testPlan
     */
    public void replace(int row, T element)
    {
        values.set(row, element);
        fireTableRowsUpdated(row, row);

    }

    public void replaceAll(List< ? extends T> elements)
    {
        clear();
        addAll(elements);
    }

    public boolean contains(T element)
    {
        return values.contains(element);
    }

    public void fireChanged(T element)
    {
        int indexOf = values.indexOf(element);
        if (indexOf == -1)
            return;
        fireTableRowsUpdated(indexOf, indexOf);
    }

    public void add(int index, T element)
    {
        values.add(index, element);
        fireTableRowsInserted(index, index);
    }
}
