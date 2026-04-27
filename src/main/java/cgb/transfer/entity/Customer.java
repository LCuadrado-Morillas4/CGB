package cgb.transfer.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Customer {

	/**
	 * Identifiant de client
	 */
	@Id
	private Long id;
	
	/**
	 * Nom du client
	 */
	private String name;
	
	/**
	 * Adresse du client
	 */
	private String address;
	
	/**
	 * LEI (Legal Entity Identifier) du client
	 * Le LEI est un code unique de 20 caractères pour identifier les entités juridiques participants à des transactions financières.
	 */
	private String LEI;
	
	/**
	 * Liste des utilisateurs de l'entreprise
	 */
	@OneToMany(mappedBy = "company")
	private List<UserCGB> listUsers = new ArrayList<UserCGB>();
	
	/**
	 * Liste des comptes de l'entreprise
	 */
	@OneToMany(mappedBy = "company")
	private List<Account> listAccounts = new ArrayList<Account>();
	
	/**
	 * 
	 */
	@ManyToMany(mappedBy = "recipientAccounts")
	// TODO DONNER UN NOM
	private List<Account> list = new ArrayList<Account>();
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getLEI() {
		return LEI;
	}
	
	public List<UserCGB> getListUsers() {
		return listUsers;
	}
	
	public List<Account> getListAccounts() {
		return listAccounts;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLEI(String lEI) {
		LEI = lEI;
	}
	
	public void setListUsers(List<UserCGB> listUsers) {
		this.listUsers = listUsers;
	}
	
	public void setListAccounts(List<Account> listAccounts) {
		this.listAccounts = listAccounts;
	}
	
	public void addUser(UserCGB user) {
		this.listUsers.add(user);
	}
	
	public void addAccount(Account account) {
		this.listAccounts.add(account);
	}
	
	public void add(Account account) {
		this.list.add(account);
	}
	
}
