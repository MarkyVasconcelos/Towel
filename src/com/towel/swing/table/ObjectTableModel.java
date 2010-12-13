package com.towel.swing.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;



/**
 *A TableModel based on reflection.
 * 
 *@author Marcos Vasconcelos
 */
public class ObjectTableModel<T> extends AbstractTableModel {
	private List<T> data;
	private FieldResolver fields[];
	private boolean editDefault;
	private Boolean editableCol[];

	public ObjectTableModel(AnnotationResolver resolver, String cols) {
		data = new ArrayList<T>();
		this.fields = (FieldResolver[]) resolver.resolve(cols).clone();
		editDefault = false;
		editableCol = new Boolean[fields.length];
	}

	public ObjectTableModel(FieldResolver fields[]) {
		data = new ArrayList<T>();
		this.fields = (FieldResolver[]) fields.clone();
		editDefault = false;
	}

	public void setEditableDefault(boolean editable) {
		editDefault = editable;
	}

	public void setColEditable(int col, boolean editable) {
		editableCol[col] = editable;
	}

	public boolean isCellEditable(int i, int k) {
		if (editableCol == null || editableCol[k] == null)
			return editDefault;
		return editableCol[k];
	}

	public int getColumnCount() {
		return fields.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int arg0, int arg1) {
		try {
			Object obj = data.get(arg0);
			Object obj2 = fields[arg1].getValue(obj);
			return obj2;
		} catch (Exception e) {
			return null;
		}
	}

	public void setValueAt(Object value, int arg0, int arg1) {
		try {
			Object obj = data.get(arg0);
			fields[arg1].setValue(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public T getValue(int arg0) {
		return data.get(arg0);
	}

	public String getColumnName(int row) {
		return fields[row].getName();
	}

	public void add(T obj) {
		data.add(obj);
		fireTableDataChanged();
	}

	public void clear() {
		data = new ArrayList<T>();
		fireTableDataChanged();
	}

	public void setData(List<T> data) {
		this.data = data;
		fireTableDataChanged();
	}

	public void remove(int row) {
		data.remove(row);
		fireTableDataChanged();
	}

	public List<T> getData() {
		return new ArrayList<T>(data);
	}

	public void remove(int idx[]) {
		for (int i : idx)
			remove(i);
	}

	public void remove(List<T> objs) {
		for (T t : objs)
			remove(indexOf(t));
	}

	public void remove(T obj) {
		remove(indexOf(obj));
	}

	public void addAll(Collection<T> coll) {
		for (T t : coll)
			add(t);
	}

	public List<T> getList(int idx[]) {
		List<T> list = new ArrayList<T>();
		int size = idx.length;
		for (int j = 0; j < size; j++)
			list.add(getValue(idx[j]));

		return list;
	}

	public int indexOf(T obj) {
		return data.indexOf(obj);
	}
	
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	public FieldResolver getColumnResolver(int colIndex) {
		return fields[colIndex];
	}
	
	public Class<?> getColumnClass(int col){
		return getColumnResolver(col).getFieldType();
	}
}