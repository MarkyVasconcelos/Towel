package com.towel.swing.combo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import com.towel.bean.DefaultFormatter;
import com.towel.bean.Formatter;



public class ObjectComboBoxModel<T> implements ComboBoxModel {
	private List<T> data;
	private T selectedItem;
	private Formatter formatter;
	private Map<Object, T> map = new HashMap<Object, T>();

	public ObjectComboBoxModel() {
		data = new ArrayList<T>();
		formatter = new DefaultFormatter();
	}

	public void setFormatter(Formatter formatter) {
		if (formatter == null) {
			System.out
					.println("Formatter can't be null. A default one will be set.");
			formatter = new DefaultFormatter();
		}

		this.formatter = formatter;
		map.clear();
		for (T t : data)
			map.put(formatter.format(t), t);
	}

	public void add(T obj) {
		data.add(obj);
		map.put(formatter.format(obj), obj);
	}

	public void clear() {
		data.clear();
		map.clear();
	}

	public void setData(List<T> list) {
		data = list;
		map.clear();
		for (T t : data)
			map.put(formatter.format(t), t);
	}

	public T getSelectedObject() {
		return selectedItem;
	}

	@Override
	public Object getSelectedItem() {
		return formatter.format(selectedItem);
	}

	@Override
	public void setSelectedItem(Object arg0) {
		selectedItem = map.get(arg0);
		
	}

	public void setSelectedObject(T obj) {
		selectedItem = obj;
	}

	@Override
	public Object getElementAt(int arg0) {
		return formatter.format(data.get(arg0));
	}

	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}
}
