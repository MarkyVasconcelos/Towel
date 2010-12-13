package com.towel.login;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.towel.awt.Action;
import com.towel.awt.ActionManager;
import com.towel.other.Pair;
import com.towel.role.RoleManager;
import com.towel.swing.ModalWindow;



public class LoginScreen {
	private JDialog screen;
	private JTextField username;
	private JPasswordField password;
	private JButton login, close;
	private LoginListener listener;
	private List<Pair<Manager, Object>> list;
	private WindowListener wListener = new WindowAdapter(){
		@Override
		public void windowClosed(WindowEvent arg0) {
			listener.close();
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			listener.close();
		}
	};

	private ActionManager manager;

	public LoginScreen(Component parent) {
		list = new ArrayList<Pair<Manager, Object>>();
		screen = ModalWindow.createDialog(parent, "Login");
		JPanel content = new JPanel(new GridLayout(3, 2));
		content.add(new JLabel("Username:"));
		content.add(username = new JTextField(12));
		content.add(new JLabel("Password:"));
		content.add(password = new JPasswordField(12));
		content.add(login = new JButton("Login"));
		content.add(close = new JButton("Close"));
		screen.setContentPane(content);
		screen.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		screen.pack();

		screen.getRootPane().setDefaultButton(login);

		manager = new ActionManager();
		manager.manage(login, loginAction);
		manager.manage(close, closeAction);

		screen.addWindowListener(wListener);
	}

	public void whenLogin(Manager manager, Object instance) {
		list.add(new Pair<Manager, Object>(manager, instance));
	}

	public void showDialog() {
		screen.setLocationRelativeTo(screen.getParent());
		screen.setVisible(true);
	}

	public void setLoginListener(LoginListener listener) {
		this.listener = listener;
	}

	private Action closeAction = new Action() {
		@Override
		public void doAction() {
			listener.close();
		}
	};

	private Action loginAction = new Action() {
		@Override
		public void doAction() {
			if (listener == null)
				return;
			try {
				User user = listener.login(username.getText(), new String(
						password.getPassword()));

				RoleManager manager = new RoleManager(user);
				for (Pair<Manager, Object> pair : list)
					pair.getFirst().manage(manager, pair.getSecond());
				screen.removeWindowListener(wListener);
				screen.dispose();
			} catch (CannotLoginException e) {
				JOptionPane.showMessageDialog(screen, e.getMessage());
			}
		}
	};

	public enum Manager {
		Annotated {
			public void manage(RoleManager manager, Object instance) {
				manager.manageAnnotated(instance);
			}
		},
		NamedComps {
			public void manage(RoleManager manager, Object instance) {
				manager.manageNamedComps((Container) instance);
			}
		};

		public abstract void manage(RoleManager manager, Object instance);
	}
}
