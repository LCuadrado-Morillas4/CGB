package cgb.transfer.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


/**
 * Classe permettant le mapping d'un compte entre la DB et l'API.
 */
@Entity
public class Account {
	
    /**
     * L'identifiant unique d'un compte.
     */
	@Id
    private String accountNumber;
	
    /**
     * Le solde du compte.
     */
	private Double solde;
	
	/**
	 * Entreprise détenant le compte
	 */
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer company;
	
	/**
	 * Liste des comptes bénéficiaires
	 */
	//TODO METTRE LA RÉFÉRENCE À L'OBJET CUSTOMER
	@ManyToMany
	@JoinTable(
			name = "recipient_accounts",
			joinColumns = @JoinColumn(name = "accountNumber"),
			inverseJoinColumns = @JoinColumn(name = "customer_id")
			)
	private List<Customer> recipientAccounts = new ArrayList<Customer>();

    // Getters and Setters obtenus grace à Data
	
	public Double getSolde() {
		return solde;
	}
	
    public String getAccountNumber() {
		return accountNumber;
	}
    
    public Customer getCompany() {
    	return company;
    }
	
	public void setSolde(Double solde) {
		this.solde = solde;
	}
    
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public void setCompany(Customer company) {
		this.company = company;
	}
	
}