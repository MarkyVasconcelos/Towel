package com.towel.swing.table.adapter;

import javax.swing.table.TableCellRenderer;

/**
 * @author Vinicius Godoy
 */
public abstract class AbstractColumn<C> implements Column<C>
{
    private static final int DEFAULT_WIDTH = 20;

    private int width;
    private int index;
    private TableCellRenderer renderer;
    private String name;
    private Class< ? > columnClass;

    public AbstractColumn(String name, int index)
    {
        this(name, index, Object.class, DEFAULT_WIDTH, null);
    }

    public AbstractColumn(String name, int index, Class< ? > columnClass)
    {
        this(name, index, columnClass, DEFAULT_WIDTH, null);
    }

    public AbstractColumn(String name, int index, int width)
    {
        this(name, index, Object.class, width, null);
    }

    public AbstractColumn(String name, int index, Class< ? > columnClass,
            int width)
    {
        this(name, index, columnClass, width, null);
    }

    public AbstractColumn(String name, int index, Class< ? > columnClass,
            int width, TableCellRenderer renderer)
    {
        this.width = width;
        this.index = index;
        this.renderer = renderer;
        this.name = name;
        this.columnClass = columnClass;
    }

    public Class< ? > getColumnClass()
    {
        return columnClass;
    }

    public String getName()
    {
        return name;
    }

    public TableCellRenderer getRenderer()
    {
        return renderer;
    }

    public int getModelIndex()
    {
        return index;
    }

    public int getWidth()
    {
        return width;
    }
}
