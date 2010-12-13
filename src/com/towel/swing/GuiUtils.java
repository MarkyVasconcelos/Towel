package com.towel.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.JViewport;

public class GuiUtils
{
    public static void scrollToVisible(JTable table, int rowIndex)
    {
        scrollToVisible(table, rowIndex, 0);
    }

    public static void scrollToVisible(JTable table, int rowIndex, int vColIndex)
    {
        if (!(table.getParent() instanceof JViewport))
            return;

        setViewPortPosition((JViewport) table.getParent(), table.getCellRect(
                rowIndex, vColIndex, true));
    }

    public static Collection<Integer> getReverseSelectedRows(JTable table)
    {
        Set<Integer> rows = new TreeSet<Integer>(new Comparator<Integer>()
        {
            public int compare(Integer o1, Integer o2)
            {
                return o2.compareTo(o1);
            }
        });

        for (int r : table.getSelectedRows())
            rows.add(r);

        return rows;
    }

    public static void selectAndScroll(JTable table, int rowIndex)
    {
        table.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
        scrollToVisible(table, rowIndex);
    }

    public static void scrollToSelection(JTree tree)
    {
        if (!(tree.getParent() instanceof JViewport))
            return;

        setViewPortPosition((JViewport) tree.getParent(),
                tree.getPathBounds(tree.getSelectionPath()));

    }

    private static void setViewPortPosition(JViewport viewport,
            Rectangle position)
    {
        // The location of the viewport relative to the object
        Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        position.setLocation(position.x - pt.x, position.y - pt.y);

        // Scroll the area into view
        viewport.scrollRectToVisible(position);
    }

    public static void expandAllNodes(JTree tree)
    {
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }

    public static void expandFirstNodes(JTree tree)
    {
        for (int i = tree.getRowCount() - 1; i >= 0; i--)
            tree.expandRow(i);
    }

    /**
     * Encode a color in a string in RGB format. This string is compatible to
     * HTML format.
     * 
     * @param color The color to encode (e.g. Color.RED)
     * @return The encoded color (e.g. FF0000)
     */
    public static String encodeColor(Color color)
    {
        if (color == null)
            return "000000";

        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(),
                color.getBlue());
    }

    public static Component getOwnerWindow(Component component)
    {
        Component parent = component;
        while (parent != null && !(parent instanceof Frame)
                && !(parent instanceof Dialog))
            parent = parent.getParent();
        return parent;
    }

}
