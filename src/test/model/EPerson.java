package test.model;


public class EPerson extends Person {
	private String superPower;
	
	public EPerson(String str, double d) {
		super(str, d);
	}

	public void setSuperPower(String superPower) {
		this.superPower = superPower;
	}

	public String getSuperPower() {
		return superPower;
	}
}
