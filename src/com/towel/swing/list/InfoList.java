package com.towel.swing.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import com.towel.awt.Action;
import com.towel.awt.ActionManager;
import com.towel.swing.ButtonLabel;



public class InfoList extends JComponent {
	private String prefix;
	private List<ButtonLabel> labels;
	private ActionManager manager;

	public InfoList() {
		labels = new ArrayList<ButtonLabel>();
		manager = new ActionManager();
		prefix = "";
	}

	private void updateSize() {
		if (labels.size() == 0)
			return;
		setPreferredSize(getLayout().preferredLayoutSize(this));
	}

	public void setPrefix(String prefix) {
		String oldPrefix = this.prefix;
		this.prefix = prefix;
		if (oldPrefix.length() > 0)
			for (ButtonLabel label : labels)
				label.setText(label.getText().replace(oldPrefix, this.prefix));
		else
			for (ButtonLabel label : labels)
				label.setText(this.prefix + label.getText());
		updateSize();
	}

	private void replace() {
		removeAll();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		for (ButtonLabel label : labels)
			add(label);
	}

	public void add(String s) {
		labels.add(new ButtonLabel(prefix + s));
		replace();
		updateSize();
	}

	public void addAll(Collection<String> coll) {
		for (String s : coll)
			add(s);
		updateSize();
	}

	public void remove(String s) {
		int idx = indexOf(s);
		if (idx > -1)
			labels.remove(idx);
		replace();
		updateSize();
	}

	public void removeAll(Collection<String> coll) {
		for (String s : coll)
			remove(s);
		updateSize();
	}

	public int indexOf(String item) {
		int idx = -1;
		for (int i = 0; i < labels.size(); i++)
			if (labels.get(i).getText().equals(prefix + item))
				idx = i;
		return idx;
	}

	public void addAction(String item, Action action) {
		int idx = indexOf(item);
		manager.manage(labels.get(idx), action);
	}
}
