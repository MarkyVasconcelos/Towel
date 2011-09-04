package com.towel.swing.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import com.towel.collections.paginator.ListPaginator;
import com.towel.collections.paginator.Paginator;
import com.towel.io.Closable;
import com.towel.swing.ModalWindow;
import com.towel.swing.event.ObjectSelectListener;
import com.towel.swing.event.SelectEvent;

public class SelectTable<T> {
	private List<ObjectSelectListener> listeners;
	private JTable table;
	private Paginator<T> data;
	private ObjectTableModel<T> model;
	private JFrame frame;
	private JPanel content;
	private JButton selectButton, closeButton;
	private JLabel pageLabel;
	private List<Closable> closableHook;
	private TableFilter filter;

	public static final int SINGLE = 0;
	public static final int LIST = 1;

	private int selectType;

	public SelectTable(ObjectTableModel<T> model2, List<T> list2) {
		this(model2, new ListPaginator<T>(list2), SINGLE);
	}

	public SelectTable(ObjectTableModel<T> model2, Paginator<T> list2) {
		this(model2, list2, SINGLE);
	}

	public SelectTable(ObjectTableModel<T> model, Paginator<T> list,
			int selectType) {
		listeners = new ArrayList<ObjectSelectListener>();
		closableHook = new ArrayList<Closable>();

		model.setEditableDefault(false);

		data = list;
		this.model = model;
		model.setData(data.nextResult());

		buildBody();

		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					updateSelectedObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		table.addMouseListener(new SelectionListener());

		filter = new TableFilter(table);

		setSelectionType(selectType);
	}

	private void buildBody() {
		table = new JTable(model);
		frame = new JFrame("Select");

		content = new JPanel();
		pane = new JScrollPane();
		pane.setViewportView(table);
		setSize(200, 400);

		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		content.add(pane);
		content.add(getResultScrollPane());
		content.add(createCommandButtons());
	}

	private JPanel createCommandButtons() {
		JPanel buttons = new JPanel();
		buttons.setAlignmentX(.5f);
		selectButton = new JButton("Select");
		closeButton = new JButton("Close");
		buttons.add(selectButton);
		buttons.add(closeButton);
		return buttons;
	}

	public void setSize(int width, int height) {
		pane.setPreferredSize(new Dimension(width, height));
		pane.setMinimumSize(new Dimension(width, height));
	}

	public JTable getTable() {
		return table;
	}

	public void closeOnDispose(Closable close) {
		closableHook.add(close);
	}

	private boolean closed = false;
	private JScrollPane pane;

	public void close() {
		if (closed)
			return;
		closed = true;
		for (Closable closable : closableHook)
			closable.close();
	}

	public void setSelectButtonText(String text) {
		selectButton.setText(text);
	}

	public void setCloseButtonText(String text) {
		closeButton.setText(text);
	}

	public void setButtonsText(String select, String close) {
		setSelectButtonText(select);
		setCloseButtonText(close);
	}

	public void addObjectSelectListener(ObjectSelectListener listener) {
		listeners.add(listener);
	}

	public Container getContent() {
		return content;
	}

	public void showModal(Component parent) {
		try {
			final JDialog dialog = ModalWindow.createDialog(parent,
					getContent(), "Select");
			closeOnDispose(new Closable() {
				@Override
				public void close() {
					dialog.dispose();
				}
			});
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void showSelectTable() {
		showSelectTable("Select");
	}

	public void showSelectTable(String title) {
		frame = new JFrame(title);
		frame.setContentPane(content);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(2);
		frame.setVisible(true);
		closeOnDispose(new Closable() {
			@Override
			public void close() {
				frame.dispose();
			}

		});
	}

	private void notifyListeners(SelectEvent evt) {
		for (ObjectSelectListener listener : listeners)
			listener.notifyObjectSelected(evt.clone());
	}

	public void setSelectionType(int selectType) {
		this.selectType = selectType;
		table.setSelectionMode(this.selectType == SINGLE ? ListSelectionModel.SINGLE_SELECTION
				: ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public int getSelectType() {
		return selectType;
	}

	public void dispose() {
		close();
		if (frame != null) {
			frame.dispose();
		}
	}

	public JPanel getResultScrollPane() {
		JPanel container = new JPanel();
		pageLabel = new JLabel((new StringBuilder("1/")).append(
				data.getMaxPage() + 1).toString());
		JButton first = new JButton("<<");
		JButton previous = new JButton("<");
		JButton next = new JButton(">");
		JButton last = new JButton(">>");
		container.add(first);
		container.add(previous);
		container.add(pageLabel);
		container.add(next);
		container.add(last);
		first.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				firstResult();
			}
		});
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previousResult();
			}
		});
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextResult();
			}
		});
		last.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lastResult();
			}
		});
		return container;
	}

	private void firstResult() {
		data.setCurrentPage(0);
		model.setData(data.nextResult());
		pageLabel.setText((new StringBuilder("1/")).append(
				data.getMaxPage() + 1).toString());
	}

	private void previousResult() {
		if (data.getCurrentPage() - 2 < 0) {
			return;
		} else {
			data.setCurrentPage(data.getCurrentPage() - 2);
			model.setData(data.nextResult());
			pageLabel.setText((new StringBuilder(String.valueOf(String
					.valueOf(data.getCurrentPage())))).append("/")
					.append(data.getMaxPage() + 1).toString());
			return;
		}
	}

	private void nextResult() {
		try {
			if (data.getCurrentPage() >= data.getMaxPage()) {
				data.setCurrentPage(data.getMaxPage());
			}
			model.setData(data.nextResult());
			pageLabel.setText((new StringBuilder(String.valueOf(String
					.valueOf(data.getCurrentPage())))).append("/")
					.append(data.getMaxPage() + 1).toString());
		} catch (Exception e) {
			return;
		}
	}

	private void lastResult() {
		data.setCurrentPage(data.getMaxPage());
		model.setData(data.nextResult());
		pageLabel.setText((new StringBuilder(String.valueOf(String.valueOf(data
				.getCurrentPage())))).append("/").append(data.getMaxPage() + 1)
				.toString());
	}

	public void updateSelectedObject() {
		int[] objIndex = table.getSelectedRows();
		int[] realIdx = filter.getModelRows(objIndex);

		if (objIndex.length == 1)
			notifyListeners(new SelectEvent(this, model.getValue(realIdx[0])));
		else {
			List<T> selected = new ArrayList<T>();
			for (int i : realIdx)
				selected.add(model.getValue(i));
			notifyListeners(new SelectEvent(this, selected));
		}
		dispose();
	}

	public void notifyDataChanged() {
		model.fireTableDataChanged();
	}

	private class SelectionListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				updateSelectedObject();
				dispose();
			}
		}
	}

	public TableModel getModel() {
		return model;
	}

	public void setFont(Font font) {
		table.setFont(font);
	}
}