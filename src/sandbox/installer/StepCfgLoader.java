package sandbox.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StepCfgLoader {
	private List<String> lines;
	private int currentStep = 0;

	public StepCfgLoader(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}

	public StepCfgLoader(InputStream is) {
		Scanner sc = new Scanner(is);
		lines = new ArrayList<String>();
		while (sc.hasNext()) {
			lines.add(sc.nextLine());
		}
	}

	public String getNextStep() {
		return lines.get(currentStep++);
	}

}
