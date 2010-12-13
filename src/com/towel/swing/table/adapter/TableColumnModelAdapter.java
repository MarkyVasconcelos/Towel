package com.towel.swing.table.adapter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/**
 * @author Vinicius Godoy
 */
public abstract class TableColumnModelAdapter implements TableColumnModelListener
{
    public void columnAdded(TableColumnModelEvent e) {}
    public void columnMarginChanged(ChangeEvent e) {}
    public void columnMoved(TableColumnModelEvent e) {}
    public void columnRemoved(TableColumnModelEvent e) {}
    public void columnSelectionChanged(ListSelectionEvent e) {}
}
