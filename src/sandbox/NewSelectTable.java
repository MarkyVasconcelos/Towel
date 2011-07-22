package sandbox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.towel.collections.paginator.ListPaginator;
import com.towel.collections.paginator.Paginator;
import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.io.Closable;
import com.towel.swing.ModalWindow;
import com.towel.swing.event.ObjectSelectListener;
import com.towel.swing.event.SelectEvent;
import com.towel.swing.table.ObjectTableModel;

public class NewSelectTable<T> {
	public static final int SINGLE = 0;
	public static final int LIST = 1;

	private List<ObjectSelectListener> listeners;
	private List<Closable> closableHook;

	private JTable table;
	private Paginator<T> data;
	private ObjectTableModel<T> model;
	private JFrame frame;
	private JPanel content;
	private TableRowSorter<ObjectTableModel<T>> rowSorter;
	private JLabel clmSearch;
	private JButton searchButton, selectButton, closeButton;
	private JTextField filterText;
	private int colFilterIndex;
	private JLabel pageLabel;
	private int selectType;

	private Object selected;

	public NewSelectTable(FieldResolver cols[], java.util.List<T> data) {
		this(cols, new ListPaginator<T>(data, 25));
	}

	public NewSelectTable(AnnotationResolver resolver, String fields,
			Paginator<T> paginator) {
		this(resolver.resolve(fields), paginator, SINGLE, 400);
	}

	public NewSelectTable(FieldResolver cols[], Paginator<T> paginator) {
		this(cols, paginator, SINGLE, 400);
	}

	public NewSelectTable(FieldResolver cols[], Paginator<T> paginator, int w) {
		this(cols, paginator, SINGLE, w);
	}

	public NewSelectTable(ObjectTableModel<T> model, Paginator<T> paginator) {
		colFilterIndex = 0;
		listeners = new ArrayList<ObjectSelectListener>();
		this.model = model;
		data = paginator;
		model.setData(data.nextResult());
		table = new JTable(model);
		closableHook = new ArrayList<Closable>();
		frame = new JFrame("Select");
		content = new JPanel();
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(table);
		pane.setPreferredSize(new Dimension(120, 400));
		pane.setMinimumSize(new Dimension(120, 400));
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		rowSorter = new TableRowSorter<ObjectTableModel<T>>(model);
		table.setRowSorter(rowSorter);
		table.getTableHeader().addMouseListener(new ColumnListener());
		clmSearch = new JLabel();
		clmSearch.setText((new StringBuilder(String.valueOf(model
				.getColumnName(colFilterIndex)))).append(":").toString());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(clmSearch, "West");
		panel.add(getJTextFieldFilter(), "East");
		JPanel buttons = new JPanel();
		buttons.setAlignmentX(.5f);
		selectButton = new JButton("Select");
		closeButton = new JButton("Close");
		buttons.add(selectButton);
		buttons.add(closeButton);
		content.add(panel);
		content.add(pane);
		content.add(getResultScrollPane());
		content.add(buttons);

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
	}

	public NewSelectTable(FieldResolver cols[], Paginator<T> paginator,
			int selectType, int width) {
		
	}

	public JTable getTable() {
		return table;
	}

	public void closeOnDispose(Closable close) {
		closableHook.add(close);
	}

	private boolean closed = false;

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

	public void setSearchButtonText(String text) {
		searchButton.setText(text);
	}

	public void setCloseButtonText(String text) {
		closeButton.setText(text);
	}

	public void setButtonsText(String search, String select, String close) {
		setSelectButtonText(select);
		setSearchButtonText(search);
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

	public int getSelectType() {
		return selectType;
	}

	public void dispose() {
		close();
		if (frame != null) {
			frame.dispose();
		}
	}

	private JTextField getJTextFieldFilter() {
		filterText = new JTextField(30);
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				filter(filterText.getText());
			}

			public void insertUpdate(DocumentEvent e) {
				filter(filterText.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				filter(filterText.getText());
			}
		});

		searchButton = new JButton("Search");
		searchButton.setBackground(null);

		filterText.setLayout(new BorderLayout());
		filterText.add(searchButton, BorderLayout.EAST);

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				data.filter(filterText.getText(),
						model.getColumnResolver(colFilterIndex));
				firstResult();
			}
		});

		return filterText;
	}

	private void filter(String text) {
		RowFilter<ObjectTableModel<T>, Integer> filter = RowFilter.regexFilter(
				(new StringBuilder("(?i)")).append(text).toString(),
				new int[] { colFilterIndex });
		rowSorter.setRowFilter(filter);
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
		if (rowSorter != null) {
			if (selectType == SINGLE) {
				int objIndex = rowSorter.convertRowIndexToModel(table
						.getSelectedRows()[0]);
				selected = model.getValue(objIndex);
				notifyListeners(new SelectEvent(this, model.getValue(objIndex)));
			} else {
				int ids[] = table.getSelectedRows();
				for (int i = 0; i < ids.length; i++) {
					ids[i] = rowSorter.convertRowIndexToModel(ids[i]);
				}
				selected = model.getList(ids);
				notifyListeners(new SelectEvent(this, model.getList(ids)));
			}
		} else {
			int objIndex = table.getSelectedRows()[0];
			selected = model.getValue(objIndex);
			notifyListeners(new SelectEvent(this, model.getValue(objIndex)));
		}
		dispose();
	}

	public void notifyDataChanged() {
		model.fireTableDataChanged();
	}

	private class ColumnListener extends MouseAdapter {
		public void mouseClicked(MouseEvent arg0) {
			colFilterIndex = table.columnAtPoint(arg0.getPoint());
			clmSearch.setText((new StringBuilder(String.valueOf(model
					.getColumnName(colFilterIndex)))).append(":").toString());
			filterText.setText("");
		}
	}

	private class SelectionListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				updateSelectedObject();
				dispose();
			}
		}
	}

	public Object getSelectedObject() {
		return selected;
	}

	public TableModel getModel() {
		return model;
	}
}