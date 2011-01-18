package sandbox;

import javax.accessibility.AccessibleComponent;

public interface Selectable extends AccessibleComponent {
	public void setSelected(boolean selected);

	public boolean isSelected();
}
