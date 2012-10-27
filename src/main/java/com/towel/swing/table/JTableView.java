package com.towel.swing.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class JTableView extends JPanel {
	private TableModel mainModel;
	private AggregateModel footerModel;
	private JTable mainTable;

	public JTableView(TableModel model) {
		super(new BorderLayout());

		this.setMainModel(model);
		this.setFooterModel(new AggregateModel(model));

		setMainTable(new JTable(model));
		mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		final JTable footerTable = new JTable(getFooterModel());
		footerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		footerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scroll = new JScrollPane(mainTable);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JScrollPane fixedScroll = new JScrollPane(footerTable) {
			public void setColumnHeaderView(Component view) {
			} // work around
		};

		fixedScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		JScrollBar bar = fixedScroll.getVerticalScrollBar();
		JScrollBar dummyBar = new JScrollBar() {
			public void paint(Graphics g) {
			}
		};
		dummyBar.setPreferredSize(bar.getPreferredSize());
		fixedScroll.setVerticalScrollBar(dummyBar);

		final JScrollBar bar1 = scroll.getHorizontalScrollBar();
		JScrollBar bar2 = fixedScroll.getHorizontalScrollBar();
		bar2.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				bar1.setValue(e.getValue());
			}
		});

		getMainTable().getColumnModel().addColumnModelListener(
				new TableColumnModelListener() {
					@Override
					public void columnSelectionChanged(ListSelectionEvent e) {
						footerTable.columnSelectionChanged(e);
					}

					@Override
					public void columnRemoved(TableColumnModelEvent e) {
						footerTable.columnRemoved(e);
					}

					@Override
					public void columnMoved(TableColumnModelEvent e) {
						footerTable.getColumnModel().moveColumn(
								e.getFromIndex(), e.getToIndex());
					}

					@Override
					public void columnMarginChanged(ChangeEvent e) {
						footerTable.columnMarginChanged(e);
					}

					@Override
					public void columnAdded(TableColumnModelEvent e) {
						footerTable.columnAdded(e);
					}
				});

		getMainModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				footerModel.fireTableDataChanged();
			}
		});

		JTableHeader mainHeader = getMainTable().getTableHeader();
		JTableHeader footerHeader = footerTable.getTableHeader();

		MouseEventDispatcher adapter = new MouseEventDispatcher(footerHeader);
		mainHeader.addMouseListener(adapter);
		mainHeader.addMouseMotionListener(adapter);
		
		// for (MouseListener list : mainHeader.getMouseListeners())
		// footerHeader.addMouseListener(list);
		//
		// for (MouseMotionListener list : mainHeader.getMouseMotionListeners())
		// footerHeader.addMouseMotionListener(list);

		// scroll.setPreferredSize(new Dimension(500, 80));
		// fixedScroll.setPreferredSize(new Dimension(400, 52));
		fixedScroll.setPreferredSize(new Dimension(0, 40));
		// fixedScroll.setMinimumSize(new Dimension(20, 0));
		// fixedScroll.setMaximumSize(new Dimension(20, 1024));
		add(scroll, BorderLayout.CENTER);
		add(fixedScroll, BorderLayout.SOUTH);
	}

	private void setMainModel(TableModel mainModel) {
		this.mainModel = mainModel;
	}

	public TableModel getMainModel() {
		return mainModel;
	}

	private void setFooterModel(AggregateModel footerModel) {
		this.footerModel = footerModel;
	}

	public AggregateModel getFooterModel() {
		return footerModel;
	}

	private void setMainTable(JTable mainTable) {
		this.mainTable = mainTable;
	}

	public JTable getMainTable() {
		return mainTable;
	}

	public class MouseEventDispatcher implements MouseListener,
			MouseMotionListener {
		JComponent comp;

		public MouseEventDispatcher(JComponent comp) {
			this.comp = comp;
			System.out.println("MouseListeners: "+ comp.getMouseListeners().length);
			System.out.println("MouseMotionListeners: "+ comp.getMouseMotionListeners().length);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			for (MouseMotionListener listener : comp.getMouseMotionListeners())
				listener.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			for (MouseMotionListener listener : comp.getMouseMotionListeners())
				listener.mouseMoved(e);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			for (MouseListener listener : comp.getMouseListeners())
				listener.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			for (MouseListener listener : comp.getMouseListeners())
				listener.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			for (MouseListener listener : comp.getMouseListeners())
				listener.mouseReleased(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			for (MouseListener listener : comp.getMouseListeners())
				listener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			for (MouseListener listener : comp.getMouseListeners())
				listener.mouseExited(e);
		}

	}
}
