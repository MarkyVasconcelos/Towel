package sandbox;

/**
 Java Swing, 2nd Edition
 By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
 ISBN: 0-596-00408-7
 Publisher: O'Reilly
 */
// PagingTester.java
//A quick application that demonstrates the PagingModel.
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class PagingTable extends JFrame {

	public PagingTable() {
		super("Paged JTable Test");
		setSize(300, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		PagingModel pm = new PagingModel();
		JTable jt = new JTable(pm);

		// Use our own custom scrollpane.
		JScrollPane jsp = PagingModel.createPagingScrollPaneForTable(jt);
		getContentPane().add(jsp, BorderLayout.CENTER);
	}

	public static void main(String args[]) {
		PagingTable pt = new PagingTable();
		pt.setVisible(true);
	}
}

// PagingModel.java
// A larger table model that performs "paging" of its data. This model
// reports a small number of rows (like 100 or so) as a "page" of data. You
// can switch pages to view all of the rows as needed using the pageDown()
// and pageUp() methods. Presumably, access to the other pages of data is
// dictated by other GUI elements such as up/down buttons, or maybe a text
// field that allows you to enter the page number you want to display.
//

class PagingModel extends AbstractTableModel {

	protected int pageSize;

	protected int pageOffset;

	protected Record[] data;

	public PagingModel() {
		this(10000, 100);
	}

	public PagingModel(int numRows, int size) {
		data = new Record[numRows];
		pageSize = size;

		// Fill our table with random data (from the Record() constructor).
		for (int i = 0; i < data.length; i++) {
			data[i] = new Record();
		}
	}

	// Return values appropriate for the visible table part.
	public int getRowCount() {
		return Math.min(pageSize, data.length);
	}

	public int getColumnCount() {
		return Record.getColumnCount();
	}

	// Work only on the visible part of the table.
	public Object getValueAt(int row, int col) {
		int realRow = row + (pageOffset * pageSize);
		return data[realRow].getValueAt(col);
	}

	public String getColumnName(int col) {
		return Record.getColumnName(col);
	}

	// Use this method to figure out which page you are on.
	public int getPageOffset() {
		return pageOffset;
	}

	public int getPageCount() {
		return (int) Math.ceil((double) data.length / pageSize);
	}

	// Use this method if you want to know how big the real table is . . . we
	// could also write "getRealValueAt()" if needed.
	public int getRealRowCount() {
		return data.length;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int s) {
		if (s == pageSize) {
			return;
		}
		int oldPageSize = pageSize;
		pageSize = s;
		pageOffset = (oldPageSize * pageOffset) / pageSize;
		fireTableDataChanged();
		/**
		 * if (pageSize < oldPageSize) { fireTableRowsDeleted(pageSize,
		 * oldPageSize - 1); } else { fireTableRowsInserted(oldPageSize,
		 * pageSize - 1); }
		 */
	}

	// Update the page offset and fire a data changed (all rows).
	public void pageDown() {
		if (pageOffset < getPageCount() - 1) {
			pageOffset++;
			fireTableDataChanged();
		}
	}

	// Update the page offset and fire a data changed (all rows).
	public void pageUp() {
		if (pageOffset > 0) {
			pageOffset--;
			fireTableDataChanged();
		}
	}

	// We provide our own version of a scrollpane that includes
	// the page up and page down buttons by default.
	public static JScrollPane createPagingScrollPaneForTable(JTable jt) {
		JScrollPane jsp = new JScrollPane(jt);
		TableModel tmodel = jt.getModel();

		// Don't choke if this is called on a regular table . . .
		if (!(tmodel instanceof PagingModel)) {
			return jsp;
		}

		// Okay, go ahead and build the real scrollpane
		final PagingModel model = (PagingModel) tmodel;
		final JButton upButton = new JButton(new ArrowIcon(ArrowIcon.UP));
		upButton.setEnabled(false); // starts off at 0, so can't go up
		final JButton downButton = new JButton(new ArrowIcon(ArrowIcon.DOWN));
		if (model.getPageCount() <= 1) {
			downButton.setEnabled(false); // One page...can't scroll down
		}

		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				model.pageUp();

				// If we hit the top of the data, disable the up button.
				if (model.getPageOffset() == 0) {
					upButton.setEnabled(false);
				}
				downButton.setEnabled(true);
			}
		});

		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				model.pageDown();

				// If we hit the bottom of the data, disable the down button.
				if (model.getPageOffset() == (model.getPageCount() - 1)) {
					downButton.setEnabled(false);
				}
				upButton.setEnabled(true);
			}
		});

		// Turn on the scrollbars; otherwise we won't get our corners.
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Add in the corners (page up/down).
		jsp.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, upButton);
		jsp.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, downButton);

		return jsp;
	}
}

// Record.java
// A simple data structure for use with the PagingModel demo.
//

class Record {
	static String[] headers = { "Record Number", "Batch Number", "Reserved" };

	static int counter;

	String[] data;

	public Record() {
		data = new String[] { "" + (counter++),
				"" + System.currentTimeMillis(), "Reserved" };
	}

	public String getValueAt(int i) {
		return data[i];
	}

	public static String getColumnName(int i) {
		return headers[i];
	}

	public static int getColumnCount() {
		return headers.length;
	}
}

// ArrowIcon.java
// A simple implementation of the Icon interface that can make
// Up and Down arrows.
//

class ArrowIcon implements Icon {

	public static final int UP = 0;

	public static final int DOWN = 1;

	private int direction;

	private Polygon pagePolygon = new Polygon(new int[] { 2, 4, 4, 10, 10, 2 },
			new int[] { 4, 4, 2, 2, 12, 12 }, 6);

	private int[] arrowX = { 4, 9, 6 };

	private Polygon arrowUpPolygon = new Polygon(arrowX,
			new int[] { 10, 10, 4 }, 3);

	private Polygon arrowDownPolygon = new Polygon(arrowX,
			new int[] { 6, 6, 11 }, 3);

	public ArrowIcon(int which) {
		direction = which;
	}

	public int getIconWidth() {
		return 14;
	}

	public int getIconHeight() {
		return 14;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(Color.black);
		pagePolygon.translate(x, y);
		g.drawPolygon(pagePolygon);
		pagePolygon.translate(-x, -y);
		if (direction == UP) {
			arrowUpPolygon.translate(x, y);
			g.fillPolygon(arrowUpPolygon);
			arrowUpPolygon.translate(-x, -y);
		} else {
			arrowDownPolygon.translate(x, y);
			g.fillPolygon(arrowDownPolygon);
			arrowDownPolygon.translate(-x, -y);
		}
	}
}
