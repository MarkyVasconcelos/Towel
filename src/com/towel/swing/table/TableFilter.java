package com.towel.swing.table;

import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.towel.cfg.TowelConfig;
import com.towel.swing.GuiUtils;
import com.towel.swing.TextUtils;
import com.towel.swing.table.adapter.TableColumnModelAdapter;
import com.towel.swing.table.headerpopup.HeaderButtonListener;
import com.towel.swing.table.headerpopup.HeaderPopupEvent;
import com.towel.swing.table.headerpopup.HeaderPopupListener;
import com.towel.swing.table.headerpopup.TableHeaderPopup;

/**
 * TableFilter is a decorator for TableModel adding auto-filter functionality to
 * a supplied TableModel. TODO javadoc me
 * 
 * @author Vinicius Godoy
 */
public class TableFilter extends AbstractTableModel {
	private static final String POPUP_ITM_SORT_DESC_ATTR = "popup_itm_sort_desc_attr";
	private static final String POPUP_ITM_SORT_ASC_ATTR = "popup_itm_sort_asc_attr";
	private static final String POPUP_CUSTOMIZE_ATTR = "popup_customize_attr";
	private static final String POPUP_EMPTY_ATTR = "popup_empty_attr";
	private static final String POPUP_ITM_ALL_ATTR = "popup_itm_all_attr";
	private static final String POPUP_TEXT_ATTR = "popup_text";

	private String popup_itm_sort_desc;
	private String popup_itm_sort_asc;
	private String popup_customize;
	private String popup_empty;
	private String popup_itm_all;
	private String popup_text;

	private static final int NO_COLUMN = -1;

	private Map<Integer, Filter> filters = null;
	private Map<Integer, List<Integer>> filterByColumn = null;
	private TableModel tableModel;
	private List<Integer> filteredRows;

	private TableHeaderPopup tableHeaderPopup;
	private HeaderPopupListener listener;

	private Set<Integer> disableColumns;
	private Set<Integer> sortedOnlyColumn;
	private Set<Integer> upToDateColumns;

	private Integer sortingColumn = NO_COLUMN;
	private Sorting order = Sorting.NONE;

	private JTableHeader header;

	/**
	 * Table filter constructor. This is the only way to set a TableHeader and a
	 * TableModel to this TableFilter.The class will use TableHeader to create a
	 * PopUp menu and draw a Button on Table, and the TableModel to get the
	 * table data.
	 * 
	 * @param table
	 *            JTable to create the Filter on.
	 */
	public TableFilter(JTable table) {
		this(table.getTableHeader(), table.getModel());
		table.setModel(this);
	}

	/**
	 * Table filter constructor. This is the only way to set a TableHeader and a
	 * TableModel to this TableFilter. The class will use TableHeader to create
	 * a PopUp menu and draw a Button on Table, and the TableModel to get the
	 * table data.
	 * 
	 * @param tableHeader
	 *            Header from JTable where will be draw a Button and display the
	 *            PopUp menu.
	 * @param tableModel
	 *            TableModel that return the data to display on table.
	 */
	public TableFilter(JTableHeader tableHeader, TableModel tableModel) {
		this.filters = new HashMap<Integer, Filter>();
		this.filteredRows = new ArrayList<Integer>();

		this.disableColumns = new TreeSet<Integer>();
		this.sortedOnlyColumn = new TreeSet<Integer>();
		this.upToDateColumns = new HashSet<Integer>();
		this.filterByColumn = new HashMap<Integer, List<Integer>>();

		this.header = tableHeader;
		tableHeader.getColumnModel().addColumnModelListener(
				new TableColumnModelAdapter() {
					@Override
					public void columnAdded(TableColumnModelEvent e) {
						int modelIndex = header.getColumnModel()
								.getColumn(e.getToIndex()).getModelIndex();
						refreshHeader(modelIndex);
					}
				});

		setTableValues(tableHeader, tableModel);

		setLocale(TowelConfig.getInstance().getDefaultLocale());
	}

	/**
	 * @param modelIndex
	 */
	private void refreshHeader(int column) {
		tableHeaderPopup.getPopup(column).removeAllElements();

		if (disableColumns.contains(column))
			return;

		tableHeaderPopup.getPopup(column).addElement(0, null);
	}

	/**
	 * Sets if the column auto-filter is enabled or not.
	 * 
	 * @param column
	 *            Index from column that will be disabled.
	 * @param enabled
	 *            True if the column auto-filter is enabled.
	 */
	public void setColumnFilterEnabled(Integer column, boolean enabled) {
		if (!enabled)
			disableColumns.add(column);
		else
			disableColumns.remove(column);
		refreshHeader(column);
		updateFilter();
	}

	public void setColumnSortedOnly(Integer column, boolean onlySorted) {
		if (onlySorted)
			sortedOnlyColumn.add(column);
		else
			sortedOnlyColumn.remove(column);
		updateFilter();
	}

	/**
	 * Return all filter options from a specific column.
	 * 
	 * @param columnIndex
	 * @return All possible values of the column.
	 */
	public Set<Object> getFilterOptions(int columnIndex) {
		Set<Object> set = new TreeSet<Object>(getColumnComparator(columnIndex));

		if (!isFiltering()) {
			for (int i = 0; i < getRowCount(); i++)
				set.add(getValueAt(i, columnIndex));

			return set;
		}

		List<Integer> itens = new ArrayList<Integer>();
		processFilter(itens, columnIndex);
		for (int row : itens)
			set.add(tableModel.getValueAt(row, columnIndex));

		return set;
	}

	/**
	 * Sets a filter a table column. Only row with the value equals from filter
	 * will be shown.
	 * 
	 * @param columnIndex
	 * @param filter
	 *            Filter value.
	 */
	public void setFilter(int columnIndex, Filter filter) {
		filters.put(columnIndex, filter);
		tableHeaderPopup.setModified(columnIndex, true);
		updateFilter();
	}

	public String getFilterString(int columnIndex) {
		if (filters.containsKey(columnIndex))
			return filters.get(columnIndex).toString();
		return null;
	}

	public Filter getFilter(int columnIndex) {
		return filters.get(columnIndex);
	}

	public void setFilterByString(int columnIndex, String filter) {
		setFilter(columnIndex, new StringFilter(filter));
	}

	public void setFilterByRegex(int columnIndex, String filter) {
		setFilter(columnIndex, new RegexFilter(filter));
	}

	/**
	 * Removes a filter from column.
	 * 
	 * @param columnIndex
	 */
	public void removeFilter(int columnIndex) {
		filters.remove(columnIndex);
		updateFilter();
	}

	private void updateFilter() {
		updateFilter(true);
	}

	/**
	 * Updates the table using the filter values. Only row filtered will be
	 * shown.
	 */
	private void updateFilter(boolean fireDataChanged) {
		generateColumnsIndices();
		processFilter();
		sortColumn();
		upToDateColumns.clear();

		if (fireDataChanged)
			fireTableDataChanged();
	}

	private void generateColumnsIndices() {
		filterByColumn.clear();

		for (int column = 0; column < tableModel.getColumnCount(); column++) {
			List<Integer> columnFilter = new ArrayList<Integer>();
			for (int i = 0; i < tableModel.getRowCount(); i++)
				columnFilter.add(i);
			filterByColumn.put(column, columnFilter);

			if (filters.get(column) == null)
				continue;

			Iterator<Integer> it = columnFilter.iterator();
			while (it.hasNext()) {
				int row = it.next();
				Object obj = tableModel.getValueAt(row, column);
				if (!filters.get(column).doFilter(obj))
					it.remove();
			}
		}
	}

	private void processFilter() {
		if (!isFiltering())
			return;

		processFilter(filteredRows, NO_COLUMN);
	}

	private void processFilter(List<Integer> filter, int except) {

		filter.clear();
		for (int i = 0; i < tableModel.getRowCount(); i++)
			filter.add(i);

		for (int i = 0; i < filterByColumn.size(); i++) {
			if (i != except)
				filter.retainAll(filterByColumn.get(i));
		}
	}

	/**
	 * Sorts a column by descending or ascending order.
	 */
	private void sortColumn() {
		if (!isSorting())
			return;

		Collections.sort(filteredRows, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				Object obj1 = tableModel.getValueAt(o1, sortingColumn);
				Object obj2 = tableModel.getValueAt(o2, sortingColumn);

				if (order == Sorting.ASCENDING)
					return getColumnComparator(sortingColumn).compare(obj1,
							obj2);

				return getColumnComparator(sortingColumn).compare(obj2, obj1);
			}

		});
	}

	/**
	 * Returns the comparator from column. The comparator must be
	 * <code>COMPARABLE_COMPARATOR</code> or <code>LEXICAL_COMPARATOR</code>.
	 * 
	 * @param column
	 * @return
	 */
	private Comparator<Object> getColumnComparator(Integer column) {
		if (Comparable.class
				.isAssignableFrom(tableModel.getColumnClass(column)))
			return COMPARABLE_COMPARATOR;

		return LEXICAL_COMPARATOR;
	}

	/**
	 * Update the specific column popup menu from table header. Will be inserted
	 * into popup menu the sorting itens and the possible values for filter.
	 * 
	 * @param column
	 *            Column index.
	 */
	private void updateColumnPopup(int column) {
		if (upToDateColumns.contains(column))
			return;

		upToDateColumns.add(column);

		tableHeaderPopup.getPopup(column).removeAllElements();

		if (disableColumns.contains(column))
			return;

		tableHeaderPopup.getPopup(column).addElement(popup_itm_sort_asc,
				getHeaderPopupListener());
		tableHeaderPopup.getPopup(column).addElement(popup_itm_sort_desc,
				getHeaderPopupListener());
		tableHeaderPopup.getPopup(column).addElement(popup_customize,
				getHeaderPopupListener());
		tableHeaderPopup.getPopup(column).addElement(popup_empty,
				getHeaderPopupListener());
		tableHeaderPopup.getPopup(column).addListSeparator();

		tableHeaderPopup.getPopup(column).addElement(popup_itm_all,
				getHeaderPopupListener());

		if (sortedOnlyColumn.contains(column))
			return;

		Set<Object> filterOptions = getFilterOptions(column);
		for (Object obj : filterOptions)
			tableHeaderPopup.getPopup(column).addElement(obj,
					getHeaderPopupListener());
	}

	public void setSorting(int index, Sorting order) {
		if (order == Sorting.NONE) {
			if (sortingColumn == index) {
				sortingColumn = NO_COLUMN;
				this.order = Sorting.NONE;
			}
			return;
		}

		if (sortingColumn != NO_COLUMN && !filters.containsKey(sortingColumn))
			tableHeaderPopup.setModified(sortingColumn, false);

		sortingColumn = index;
		this.order = order;
		tableHeaderPopup.setModified(index, true);
		updateFilter();
	}

	/**
	 * Creates by lazy creation and return a listener for Header Popup.
	 * 
	 * @return The created listener.
	 */
	private HeaderPopupListener getHeaderPopupListener() {
		if (listener == null) {
			listener = new HeaderPopupListener() {
				public void elementSelected(HeaderPopupEvent e) {
					if (e.getSource().equals(popup_itm_sort_asc)) {
						setSorting(e.getModelIndex(), Sorting.ASCENDING);
					} else if (e.getSource().equals(popup_itm_sort_desc)) {
						setSorting(e.getModelIndex(), Sorting.DESCENDING);
					} else if (e.getSource().equals(popup_itm_all)) {
						setSorting(e.getModelIndex(), Sorting.NONE);
						removeFilter(e.getModelIndex());
						tableHeaderPopup.setModified(e.getModelIndex(), false);
					} else if (e.getSource().equals(popup_customize)) {
						String text = "";
						if (filters.get(e.getModelIndex()) instanceof RegexFilter)
							text = ((RegexFilter) filters
									.get(e.getModelIndex())).getRegex();

						String value = JOptionPane.showInputDialog(
								GuiUtils.getOwnerWindow(header), popup_text,
								text);

						if (value == null)
							return;

						setFilterByRegex(e.getModelIndex(), value);
					} else if (e.getSource().equals(popup_empty)) {
						setFilterByString(e.getModelIndex(), "");
					} else {
						setFilterByString(e.getModelIndex(), e.getSource()
								.toString());
					}
				}
			};
		}

		return listener;
	}

	public int getColumnCount() {
		return tableModel.getColumnCount();
	}

	public int getRowCount() {
		if (isFiltering())
			return filteredRows.size();
		return tableModel.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(getModelRow(rowIndex), columnIndex);
	}

	/**
	 * Sets the TableHeader and TableModel on this TableFilter. This method must
	 * be used only on contructor.
	 * 
	 * @param header
	 * @param tableModel
	 */
	private void setTableValues(JTableHeader header, TableModel tableModel) {
		this.tableModel = tableModel;
		this.tableHeaderPopup = new TableHeaderPopup(header, tableModel);

		tableHeaderPopup.addButtonListener(new HeaderButtonListener() {
			public void buttonClicked(HeaderPopupEvent e) {
				updateColumnPopup(e.getModelIndex());
			}
		});

		tableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				onTableChanged(e);
			}
		});

		updateFilter();
	}

	/**
	 * Method called on TableChanged event generated from TableModel. This is a
	 * very important method the TableModel is adding or removing an object from
	 * table. If the TableModel is adding an object to table, this object will
	 * be shown independent of the filter values.
	 * 
	 * @param e
	 */
	private void onTableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.INSERT) {
			int first = filteredRows.size();
			int last = filteredRows.size();

			for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
				filteredRows.add(row);
				last++;
			}

			fireTableRowsInserted(first, last - 1);
			upToDateColumns.clear(); // invalidate header popup
		} else if (e.getType() == TableModelEvent.DELETE) {
			if (!isFiltering()) {
				fireTableRowsDeleted(e.getFirstRow(), e.getLastRow());
				upToDateColumns.clear(); // invalidate header popup
				return;
			}

			for (int row = e.getLastRow(); row >= e.getFirstRow(); row--) {
				int index = filteredRows.indexOf(row);

				if (index != -1) {
					filteredRows.remove(index);
					fireTableRowsDeleted(index, index);
				}
			}

			// shift up nRemoved times the index of filteredRows
			int nRemoved = e.getLastRow() - e.getFirstRow() + 1;
			for (int i = 0; i < filteredRows.size(); i++) {
				if (filteredRows.get(i) > e.getLastRow())
					filteredRows.set(i, filteredRows.get(i) - nRemoved);
			}

			upToDateColumns.clear(); // invalidate header popup
		} else if (e.getType() == TableModelEvent.UPDATE) {
			if (e.getColumn() == TableModelEvent.ALL_COLUMNS) {
				if (!isFiltering()) {
					fireTableDataChanged();
					upToDateColumns.clear(); // invalidate header popup
					return;
				}

				if (e.getLastRow() == Integer.MAX_VALUE) {
					// TableDataChanged!
					Integer currentSortingColumn = sortingColumn;
					Sorting currentOrder = order;
					Map<Integer, Filter> currentFilters = new HashMap<Integer, Filter>(
							filters);

					sortingColumn = NO_COLUMN;
					order = Sorting.NONE;
					filters.clear();
					updateFilter(false);

					sortingColumn = currentSortingColumn;
					order = currentOrder;
					filters.putAll(currentFilters);
					updateFilter();
					return;
				}

				for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
					int index = filteredRows.indexOf(row);
					if (index != -1)
						fireTableRowsUpdated(index, index);
				}
				upToDateColumns.clear(); // invalidate header popup
			} else {
				fireTableCellUpdated(e.getFirstRow(), e.getColumn());

				// invalidate header popup from specific column
				upToDateColumns.remove(e.getColumn());
			}
		}
	}

	/**
	 * Converts the view index to table model index.
	 * 
	 * @param viewRow
	 * @return Table model index.
	 */
	public int getModelRow(int viewRow) {
		if (viewRow == -1)
			return -1;

		if (!isFiltering())
			return viewRow;

		return filteredRows.get(viewRow);
	}

	public int[] getModelRows(int[] viewRows) {
		int[] modelRows = new int[viewRows.length];
		for (int i = 0; i < viewRows.length; i++)
			modelRows[i] = getModelRow(viewRows[i]);

		return modelRows;
	}

	public int getViewRow(int modelRow) {
		if (modelRow == -1)
			return -1;

		if (!isFiltering())
			return modelRow;

		for (int i = 0; i < filteredRows.size(); i++)
			if (modelRow == filteredRows.get(i).intValue())
				return i;

		return -1;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return tableModel.getColumnClass(columnIndex);
	}

	@Override
	public String getColumnName(int column) {
		return tableModel.getColumnName(column);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return tableModel.isCellEditable(getModelRow(rowIndex), columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		tableModel.setValueAt(aValue, getModelRow(rowIndex), columnIndex);
	}

	/**
	 * @return the Table Model.
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Get filtered rows.
	 * 
	 * @return A list of rows.
	 */
	public List<Integer> getFilteredRows() {
		return Collections.unmodifiableList(filteredRows);
	}

	public void setLocale(Locale locale) {
		InputStream is = getClass().getResourceAsStream(
				"/res/strings_" + locale.toString() + ".properties");
		Properties props = new Properties();
		try {
			props.load(is);
			is.close();
			setOptions(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setOptions(Properties props) {
		popup_itm_sort_desc = props.getProperty(POPUP_ITM_SORT_DESC_ATTR);
		popup_itm_sort_asc = props.getProperty(POPUP_ITM_SORT_ASC_ATTR);
		popup_customize = props.getProperty(POPUP_CUSTOMIZE_ATTR);
		popup_empty = props.getProperty(POPUP_EMPTY_ATTR);
		popup_itm_all = props.getProperty(POPUP_ITM_ALL_ATTR);
		popup_text = props.getProperty(POPUP_TEXT_ATTR);
	}

	public boolean isFiltering() {
		return !filters.isEmpty() || isSorting();
	}

	public boolean isSorting() {
		return sortingColumn != NO_COLUMN && order != Sorting.NONE;
	}

	public Sorting getOrder() {
		return order;
	}

	public Integer getSortingColumn() {
		return sortingColumn;
	}

	public static interface Filter {
		boolean doFilter(Object obj);
	}

	public static class StringFilter implements Filter {
		private String string = "";

		public StringFilter() {
		}

		public StringFilter(String str) {
			this.string = str;
		}

		public boolean doFilter(Object obj) {
			String objStr = obj == null ? "" : obj.toString();
			return string.equals(objStr);
		}

		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}
	}

	public static class RegexFilter implements Filter {
		private String regex = "";

		public RegexFilter() {
		}

		public RegexFilter(String regex) {
			this.regex = regex;
		}

		public boolean doFilter(Object obj) {
			String regex = TextUtils.generateEscapeRegex(this.regex
					.toLowerCase());
			regex = regex.replaceAll("\\\\\\*", ".*");
			regex = regex.replaceAll("\\\\\\?", ".");

			String objStr = obj == null ? "" : obj.toString().toLowerCase();
			return Pattern.matches(regex, objStr);
		}

		public String getRegex() {
			return regex;
		}

		public void setRegex(String regex) {
			this.regex = regex;
		}
	}

	public enum Sorting {
		NONE, ASCENDING, DESCENDING
	};

	/**
	 * Default comparator using objects that implements <code>Comparable</code>
	 * interface
	 */
	private static final Comparator<Object> COMPARABLE_COMPARATOR = new Comparator<Object>() {
		@SuppressWarnings("unchecked")
		public int compare(Object o1, Object o2) {
			if (o1 == o2)
				return 0;

			if (o1 == null)
				return 1;

			if (o2 == null)
				return -1;

			if (o1 instanceof String)
				return Collator.getInstance().compare(o1, o2);

			return ((Comparable<Object>) o1).compareTo(o2);
		}
	};

	/** Default comparator using <code>toString</code> method from objects */
	private static final Comparator<Object> LEXICAL_COMPARATOR = new Comparator<Object>() {
		public int compare(Object o1, Object o2) {
			if (o1 == o2)
				return 0;

			if (o1 == null)
				return 1;

			if (o2 == null)
				return -1;

			return Collator.getInstance().compare(o1.toString(), o2.toString());
		}
	};
}
