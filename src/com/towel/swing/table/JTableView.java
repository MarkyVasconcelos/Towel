package com.towel.swing.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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

		JTable footerTable = new JTable(model);
		footerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		footerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setMainTable(new JTable(getFooterModel()));
		getMainTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scroll = new JScrollPane(footerTable);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JScrollPane fixedScroll = new JScrollPane(getMainTable()) {
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

		JTableHeader mainHeader = getMainTable().getTableHeader();
		JTableHeader footerHeader = footerTable.getTableHeader();

		for (MouseListener list : mainHeader.getMouseListeners())
			footerHeader.addMouseListener(list);
		
		for (MouseMotionListener list : mainHeader.getMouseMotionListeners())
			footerHeader.addMouseMotionListener(list);

		// scroll.setPreferredSize(new Dimension(500, 80));
		fixedScroll.setPreferredSize(new Dimension(400, 52));
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
}
