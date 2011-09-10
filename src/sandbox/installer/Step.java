package sandbox.installer;

public interface Step {
	public void init(String... args);

	public void doStep();

	public void close();
}
