package com.towel.swing.table.adapter;

import javax.swing.table.TableCellRenderer;

/**
 * Represents a non-editable column in a JTable. For editable columns, see
 * the {@link EditableColumn} interface.
 * 
 * @param <C> This class refers to the the elements in the table, not the value
 *        displayed by the column.
 *        
 * @author Vinicius Godoy
 */
public interface Column<C> extends SimpleColumn
{
    /**
     * Users implementing this interface must return the value that will be
     * displayed in the column, according to the given element. Just like in
     * getValueAt of TableModel.
     * 
     * @param element Element whose the value will be extracted
     * @return The value.
     */
    Object getValue(C element);

    /**
     * Returns the class of this column. If there's no renderer in this column,
     * the class will be redendered according to table default renderer.
     * 
     * @return The class of the column.
     * @see #getRenderer()
     */
    Class< ? > getColumnClass();

    /**
     * Returns the cell renderer for this column. This redenrer is prefered over
     * the DefaultRenderer by the table.
     * 
     * @return The TableCellRenderer for the column, or null, if the default
     *         renderer should be used.
     */
    TableCellRenderer getRenderer();
}
