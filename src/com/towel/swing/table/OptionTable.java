package com.towel.swing.table;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.towel.collections.paginator.ListPaginator;
import com.towel.collections.paginator.Paginator;
import com.towel.el.FieldResolver;
import com.towel.swing.event.ObjectSelectListener;
import com.towel.swing.event.SelectEvent;

public class OptionTable<T> implements ActionListener {
	public static class OptionButton {

		private int actionId;
		private JButton button;

		public int getActionId() {
			return actionId;
		}

		public JButton getButton() {
			return button;
		}

		public OptionButton(JButton button, int actionId) {
			this.actionId = actionId;
			this.button = button;
		}
	}

	private SelectTable<T> table;
	private List<ObjectSelectListener> listeners;
	private Object selected;
	private List<T> selecteds;
	private Container pane;
	private List<OptionButton> buttons;
	private JPanel bPanel;
	private Paginator<T> data;

	public OptionTable(FieldResolver cols[], Paginator<T> data) {
		this(cols, data, 0);
	}

	public OptionTable(FieldResolver cols[], Paginator<T> data, int selectType) {
		this.data = data;
		table = new SelectTable<T>(new ObjectTableModel<T>(cols), data,
				selectType);
		listeners = new ArrayList<ObjectSelectListener>();
		buttons = new ArrayList<OptionButton>();
		table.addObjectSelectListener(new ObjectSelectListener() {
			@SuppressWarnings("unchecked")
			public void notifyObjectSelected(SelectEvent e) {
				if (((SelectTable<T>) e.getSource()).getSelectType() == SelectTable.SINGLE) {
					selected = e.getObject();
				} else {
					selecteds = (java.util.List<T>) e.getObject();
				}
			}
		});
		pane = new JPanel();
		JPanel panel = (JPanel) pane;
		panel.setLayout(new BorderLayout());
		bPanel = new JPanel();
		panel.add(table.getContent(), "North");
		panel.add(bPanel, "South");
	}

	public OptionTable(FieldResolver cols[], java.util.List<T> data) {
		this(cols, new ListPaginator<T>(data, 25));
	}

	public Container getContent() {
		return pane;
	}

	public void addObjectSelectListener(ObjectSelectListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners(SelectEvent event) {
		for (ObjectSelectListener listener : listeners)
			listener.notifyObjectSelected(event.clone());
	}

	public void actionPerformed(ActionEvent arg0) {
		table.updateSelectedObject();
		for (OptionButton ot : buttons) {
			if (ot.getButton().equals(arg0.getSource())) {
				if (table.getSelectType() == SelectTable.SINGLE) {
					notifyListeners(new SelectEvent(this, selected,
							ot.getActionId()));
				} else {
					notifyListeners(new SelectEvent(this, selecteds,
							ot.getActionId()));
				}
				return;
			}
		}
	}

	public void addOptionButton(OptionButton opButton) {
		buttons.add(opButton);
		opButton.getButton().addActionListener(this);
		bPanel.removeAll();
		for (OptionButton button : buttons)
			bPanel.add(button.getButton());

		bPanel.revalidate();
	}

	public Paginator<T> getData() {
		return data;
	}

	public void reload() {
		table.notifyDataChanged();
	}

}