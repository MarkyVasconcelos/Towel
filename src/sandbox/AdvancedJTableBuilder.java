package sandbox;

import java.util.List;

import javax.swing.JTable;

import com.towel.collections.paginator.ListPaginator;
import com.towel.collections.paginator.Paginator;
import com.towel.swing.table.ObjectTableModel;

public class AdvancedJTableBuilder<T> {
	private ObjectTableModel<T> model;
	private Paginator<T> paginator;
	private JTable table;

	public AdvancedJTableBuilder<T> buildWithModel(ObjectTableModel<T> model) {
		this.model = model;
		return this;
	}

	@SuppressWarnings("unchecked")
	public AdvancedJTableBuilder<T> buildFor(JTable table) {
		this.table = table;
		this.model = (ObjectTableModel<T>) table.getModel();
		return this;
	}

	/**
	 * 
	 * Create a paginator to the SelectTable, with 30 items per page.
	 * 
	 * @param list
	 *            the current data
	 * @return itself
	 */
	public AdvancedJTableBuilder<T> withData(List<T> list) {
		return withData(list, 30);
	}

	/**
	 * Create a paginator to the SelectTable
	 * 
	 * @param list
	 *            the content data
	 * @param resultsPerPage
	 *            Number of items to be displayed per page
	 * @return itself
	 */
	public AdvancedJTableBuilder<T> withData(List<T> list, int resultsPerPage) {
		checkNull(model, "You should call some method to create a TableModel!");
		this.paginator = new ListPaginator<T>(list, resultsPerPage);
		return this;
	}

	public AdvancedJTableBuilder<T> withData(Paginator<T> paginator) {
		checkNull(model, "You should call some method to create a TableModel!");
		this.paginator = paginator;
		return this;
	}

	public NewSelectTable<T> create() {
		checkNull(model, "You should call some method to create a TableModel!");
		checkNull(paginator,
				"You should call some method to create a Paginator!");

		return new NewSelectTable<T>(model, paginator);
	}

	private void checkNull(Object any, String illegalStateMessage) {
		if (any == null)
			throw new IllegalStateException(illegalStateMessage);
	}

}
