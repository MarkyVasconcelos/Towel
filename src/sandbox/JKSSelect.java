package sandbox;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.*;

@SuppressWarnings("serial")
public class JKSSelect extends JComponent {

	private Point point;
	@SuppressWarnings("unused")
	private JComponent component;
	private Vector<Selectable> unselected;
	private Vector<Selectable> selected;

	@SuppressWarnings("unchecked")
	public JKSSelect(JComponent component,
			Vector<? extends Selectable> selection) {

		super();
		this.component = component;
		this.unselected = (Vector<Selectable>) selection;
		this.selected = new Vector<Selectable>();
		this.containerAction(component);
		point = new Point();
		this.setBorder(BorderFactory.createTitledBorder(""));
		component.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent e) {
				if (e.getChild() instanceof Selectable) {
					e.getChild().addMouseListener(new JKSClick());
				}
			}
		});

	}

	private void makeSquare(Point a, Point b) {
		Point aux;
		if (a == null || b == null) {
			a = new Point(0, 0);
			b = new Point(0, 0);
		}
		if (a.x == 0 && a.y == 0 && b.x == 0 && b.y == 0) {
			this.setLocation(0, 0);
			this.setSize(0, 0);
		}
		if ((a.x >= b.x && a.y > b.y) || (a.x < b.x && a.y >= b.y)) {
			aux = a;
			a = b;
			b = aux;
		}
		if (a.x <= b.x && a.y < b.y) {
			this.setLocation((int) (a.x), (int) (a.y));
			this.setSize((int) (b.x - a.x), (int) (b.y - a.y));
		} else if (a.x > b.x && a.y <= b.y) {
			this.setLocation((int) (b.x), (int) (a.y));
			this.setSize((int) (a.x - b.x), (int) (b.y - a.y));
		}
	}

	public void containerAction(JComponent container) {
		container.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				unselectAll();

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if (!arg0.isControlDown())
					unselectAll();
				point = arg0.getPoint();
				setVisible(true);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				setVisible(false);
				for (int x = 0; x < unselected.size(); x++) {
					if ((new Rectangle2D.Double(getLocation().getX(),
							getLocation().getY(), getSize().getWidth(),
							getSize().getHeight())).contains(unselected.get(x)
							.getLocation())) {
						unselected.get(x).setSelected(true);
						selected.add(unselected.remove(x));
						x--;
					}
				}
				makeSquare(new Point(0, 0), new Point(0, 0));
			}
		});

		container.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				makeSquare(point, arg0.getPoint());
			}
		});

	}

	public void unselectAll() {
		while (selected.size() != 0) {
			selected.get(0).setSelected(false);
			unselected.add(selected.remove(0));
		}
	}

	public void selectAll() {
		while (unselected.size() != 0) {
			unselected.get(0).setSelected(true);
			selected.add(unselected.remove(0));
		}
	}

	public Vector<? extends Selectable> getSelectedArray() {
		return this.selected;
	}

	public Vector<? extends Selectable> getUnselectedArray() {
		return this.unselected;
	}

	class JKSClick extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.printf("Clicado");
			if (!e.isControlDown())
				unselectAll();
			if (selected.remove((Selectable) e.getComponent())) {
				unselected.add((Selectable) e.getComponent());
				((Selectable) e.getComponent()).setSelected(false);
			} else if (unselected.remove((Selectable) e.getComponent())) {
				selected.add((Selectable) e.getComponent());
				((Selectable) e.getComponent()).setSelected(true);
			}

		}
	}
}
