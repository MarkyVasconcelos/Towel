package sandbox.installer.steps;

import java.io.File;
import java.io.IOException;

import sandbox.installer.Step;




public class MkDirStep implements Step {
	private String mkPath;

	@Override
	public void doStep() {
		try {
			new File(mkPath).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {

	}

	@Override
	public void init(String... args) {
		mkPath = args[0];
	}

}
