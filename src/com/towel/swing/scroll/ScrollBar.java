package com.towel.swing.scroll;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.towel.collections.ListNavigator;
import com.towel.collections.Navigator;



public class ScrollBar<T> extends JPanel {
	private Navigator<T> data;
	private List<ScrollEventListener> listeners;
	private T currentValue;

	public ScrollBar() {
		listeners = new ArrayList<ScrollEventListener>();

		setBorder(new LineBorder(Color.BLACK, 1));
		setLayout(new FlowLayout());
		JButton previous = new JButton("<");
		JButton next = new JButton("<");
		previous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentValue = data.previous();
				fireScrollEvent();
			}
		});
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentValue = data.next();
				fireScrollEvent();
			}
		});
		add(previous);
		add(next);
	}

	public void setData(List<T> list) {
		this.data = new ListNavigator<T>(list);
	}

	public void setData(Navigator<T> navigator) {
		this.data = navigator;
	}

	public void addScrollEventListener(ScrollEventListener listener) {
		listeners.add(listener);
	}

	public void fireScrollEvent() {
		ScrollEvent event = new ScrollEvent(currentValue, this);
		for (ScrollEventListener listener : listeners)
			listener.scrollPerformed(event);
	}

	public T getCurrentValue() {
		return currentValue;
	}
}
