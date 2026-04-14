package cgb.transfer.entity;

public enum State {

	WAITING("waiting"),
	FAILURE("failure"),
	SUCCESS("success"),
	CANCELLED("canceled"),
	RECEIVED("received"),
	CLOSED("closed");
	
	private final String nom;
	
	State(String nom) {
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
}
