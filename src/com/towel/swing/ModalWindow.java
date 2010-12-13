package com.towel.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ModalWindow {
	public static JDialog createDialog(Component parent, Container content,
			String title) {
		JDialog dialog = null;
		try {
			Method method = JOptionPane.class.getDeclaredMethod(
					"getWindowForComponent", Component.class);
			method.setAccessible(true);
			Window window = (Window) method.invoke(null, parent);
			if (window instanceof Frame)
				dialog = new JDialog((Frame) window, title, true);
			else
				dialog = new JDialog((Dialog) window, title, true);

			dialog.setComponentOrientation(parent.getComponentOrientation());
			dialog.setContentPane(content);
			dialog.pack();
			dialog.setLocationRelativeTo(parent);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return dialog;
	}

	public static JDialog createDialog(Component parent, String title) {
		JDialog dialog = null;
		try {
			Method method = JOptionPane.class.getDeclaredMethod(
					"getWindowForComponent", Component.class);
			method.setAccessible(true);
			Window window = (Window) method.invoke(null, parent);
			if (window instanceof Frame)
				dialog = new JDialog((Frame) window, title, true);
			else
				dialog = new JDialog((Dialog) window, title, true);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return dialog;
	}
}
