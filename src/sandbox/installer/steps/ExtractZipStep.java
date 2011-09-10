package sandbox.installer.steps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import sandbox.installer.Step;




@SuppressWarnings("all")
public class ExtractZipStep implements Step {
	private String zipFile;
	private String dest;

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doStep() {
		try {
			ZipInputStream in = new ZipInputStream(new FileInputStream(
					new File(zipFile)));
			for (ZipEntry entry = null; in.available() == 1; entry = in
					.getNextEntry()) {
				int method = entry.getMethod();
				byte[] b = new byte[(int) entry.getCompressedSize()];
				in.read(b);
				Inflater infl = new Inflater();
				infl.setInput(b);
				byte[] result = new byte[100];
				infl.inflate(result);
				infl.end();

				in.closeEntry();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(String... args) {
		zipFile = args[0];
		dest = args[1];
	}

}
