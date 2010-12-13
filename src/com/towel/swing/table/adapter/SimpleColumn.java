package com.towel.swing.table.adapter;

/**
 * Represents a non-editable column in a JTable. For editable columns, see the
 * {@link EditableColumn} interface.
 * 
 * @param <C> This class refers to the the elements in the table, not the value
 *        displayed by the column.
 *        
 * @author Vinicius Godoy
 */
public interface SimpleColumn
{
    /**
     * The index in model that this column refer to.
     * 
     * @return The model index.
     */
    public int getModelIndex();

    /**
     * @return The prefered width of the column in the table.
     */
    public int getWidth();

    /**
     * Returns the name of this column. The name will be displayed in the table
     * header.
     * 
     * @return The name of this column.
     */
    String getName();
}
