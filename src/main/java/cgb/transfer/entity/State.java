package cgb.transfer.entity;

/**
 * Énumération des états possibles d'un processus ou d'une opération.
 */
public enum State {

	/**
	 * État d'attente : l'opération est en cours ou en file d'attente
	 */
	WAITING("waiting"),
	/**
	 * État d'échec : l'opération s'est terminée avec une erreur
	 */
	FAILURE("failure"),
	/**
	 * État de succès : l'opération s'est terminée correctement
	 */
	SUCCESS("success"),
	CANCELED("canceled"),
	RECEIVED("received"),
	/**
	 * État fermé : l'opération est définitivement terminée
	 */
	CLOSED("closed");
	
	/**
	 * Nom lisible de l'état en minuscule
	 */
	private final String nom;
	
	State(String nom) {
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
}
