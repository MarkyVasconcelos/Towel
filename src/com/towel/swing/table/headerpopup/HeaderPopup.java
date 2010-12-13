package com.towel.swing.table.headerpopup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

/**
 * @author Vinicius Godoy
 */
public class HeaderPopup extends JPopupMenu
{
    private JScrollPane scrPane = null;
    private JList list = null;

    private JTableHeader header = null;
    private int modelndex;
    private PopupListModel listModel;

    private static class PopupListModel extends AbstractListModel
    {
        private List<Object> elements = new ArrayList<Object>();

        public Object getElementAt(int index)
        {
            return elements.get(index);
        }

        public int getSize()
        {
            return elements.size();
        }

        public void add(Object o)
        {
            elements.add(o);
            fireIntervalAdded(this, elements.indexOf(o), elements.indexOf(o));
        }

        public void remove(Object o)
        {
            int index = elements.indexOf(o);
            elements.remove(o);
            fireIntervalRemoved(this, index, index);
        }

        public void removeAllElements()
        {
            int size = elements.size();
            elements.clear();
            if (size > 0)
                fireIntervalRemoved(this, 0, size-1);
        }

        public boolean isEmpty()
        {
            return elements.isEmpty();
        }

        public Object get(int index)
        {
            return elements.get(index);
        }
    }

    public HeaderPopup(JTableHeader header, int modelIndex)
    {
        this.header = header;
        this.modelndex = modelIndex;
        this.listModel = new PopupListModel();

        initialize();
    }

    private void initialize()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorderPainted(true);
        this.setBorder(new LineBorder(Color.BLACK, 1));
        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.setFocusable(false);
        this.add(getScrPane());
    }

    private JScrollPane getScrPane()
    {
        if (scrPane == null)
        {
            scrPane = new JScrollPane(getList(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrPane.getVerticalScrollBar().setFocusable(false);
            scrPane.setFocusable(false);
            scrPane.setBorder(null);
        }

        return scrPane;
    }

    private JList getList()
    {
        if (list == null)
        {
            list = new JList();
            list.setModel(listModel);
            list.setFont(header.getFont());
            list.setForeground(header.getForeground());
            list.setBackground(header.getBackground());
            list.setSelectionForeground(UIManager.getColor("ComboBox.selectionForeground"));
            list.setSelectionBackground(UIManager.getColor("ComboBox.selectionBackground"));
            list.setBorder(null);
            list.setFocusable(false);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addMouseMotionListener(new MouseMotionListener()
            {
                public void mouseDragged(MouseEvent e)
                {
                }

                public void mouseMoved(MouseEvent e)
                {
                    if (e.getSource() == list)
                    {
                        updateListBoxSelectionForEvent(e);
                    }
                }
            });
            list.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    Element element = (Element) list.getSelectedValue();
                    if (element == null)
                        return;
                    
                    element.listener.elementSelected(new HeaderPopupEvent(
                            element.getObject(), modelndex));

                    setVisible(false);
                }
            });
            list.setCellRenderer(new DefaultListCellRenderer()
            {
                private final Component SEPARATOR = new SeparatorComponent();
                
                @Override
                public Component getListCellRendererComponent( JList list,
                  Object value, int index, boolean isSelected, boolean cellHasFocus)
                {
                  if ( value == null )
                  {
                    return SEPARATOR;
                  }
                  return super.getListCellRendererComponent( list, value, index, 
                    isSelected, cellHasFocus );
                }
            });
        }

        return list;
    }

    private static class SeparatorComponent extends JComponent
    {
      private static final Dimension PREFERRED_SIZE = new Dimension( 5, 9 );
      private static final int LINE_POS = 4;
      
      private SeparatorComponent()
      {
        setOpaque( false );
      }
      
      @Override
    public Dimension getPreferredSize()
      {
        return PREFERRED_SIZE;
      }
    
      @Override
    public void paintComponent( Graphics g )
      {
        g.setColor( Color.BLACK );    // @TODO: That's evil.
        g.drawLine( 0, LINE_POS, getWidth(), LINE_POS );
      }
    }
    
    private void updateListBoxSelectionForEvent(MouseEvent e)
    {
        Point location = e.getPoint();

        Rectangle r = new Rectangle();
        list.computeVisibleRect(r);

        if (r.contains(location))
        {
            if (list == null)
                return;

            int index = list.locationToIndex(location);

            if (index == -1)
                return;

            if (listModel.get(index) == null)
                index++;

            if (list.getSelectedIndex() != index)
                list.setSelectedIndex(index);
        }
    }

    @Override
    public boolean isFocusTraversable()
    {
        return false;
    }

    public void show(int columnIndex)
    {
        list.clearSelection();
        Rectangle rect = header.getHeaderRect(columnIndex);

        Dimension d = null;
        if (rect.getWidth() < 180)
        {
            d = new Dimension(180, (int) list.getPreferredSize().getHeight());

            if ((int) (rect.getX() - 1) - (180 - rect.getWidth()) > 0)
                rect.setBounds(
                        (int) ((rect.getX() - 1) - (180 - rect.getWidth())),
                        (int) rect.getY(), (int) rect.getWidth(),
                        (int) rect.getHeight());
            else
                rect.setBounds(0, (int) rect.getY(), (int) rect.getWidth(),
                        (int) rect.getHeight());

        }
        else
            d = new Dimension((int) rect.getWidth() - 1,
                    (int) list.getPreferredSize().getHeight());

        if (d.height > 300)
            d.height = 300;

        scrPane.setPreferredSize(d);
        scrPane.setMinimumSize(d);
        scrPane.setMaximumSize(d);

        super.show(header, (int) rect.getX() - 1,
                (int) (rect.getY() + rect.getHeight()) - 1);
    }

    public int getModelIndex()
    {
        return modelndex;
    }

    private class Element
    {
        private Object obj;
        private HeaderPopupListener listener;

        public Element(Object obj, HeaderPopupListener listener)
        {
            this.obj = obj;
            this.listener = listener;
        }

        public HeaderPopupListener getListener()
        {
            return listener;
        }

        public Object getObject()
        {
            return obj;
        }

        @Override
        public String toString()
        {
            if (obj == null)
                return "";
            
            return obj.toString();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
                return false;

            if (obj == this)
                return true;

            if (!(obj instanceof Element))
                return false;

            Element other = (Element)obj;
            
            if (this.obj == other.obj)
                return true;
            
            if (this.obj == null || other.obj == null)
                return false;
            
            return this.obj.equals(((Element) obj).getObject());
        }

        @Override
        public int hashCode()
        {
            return obj.hashCode();
        }
    }

    public void addElement(Object element, HeaderPopupListener listener)
    {
        listModel.add(new Element(element, listener));
    }

    public void removeElement(Object element)
    {
        listModel.remove(new Element(element, null));
    }

    public void removeAllElements()
    {
        listModel.removeAllElements();
    }

    public boolean isEmpty()
    {
        return listModel.isEmpty();
    }

    public void addListSeparator()
    {
        listModel.add(null);
    }
}
