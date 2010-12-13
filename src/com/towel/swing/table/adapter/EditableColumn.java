package com.towel.swing.table.adapter;

import javax.swing.table.TableCellEditor;

/**
 * Represents an Editable Column in the table. For non-editable columns, users
 * can implement the {@link Column} interface directly.
 * 
 * @param <C> This class refers to the the elements in the table, not the value
 *        displayed by the column.
 *        
 * @author Vinicius Godoy
 */
public interface EditableColumn<C> extends Column<C>
{
    /**
     * Changes the value of the given <code>object</code>, according to the
     * given <code>value</code>.
     * 
     * @param object The value to change value.
     * @param value The new value.
     */
    void setValue(C object, Object value);

    TableCellEditor getEditor();
    
    boolean isEditable(C element);
}
