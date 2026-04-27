package cgb.transfer.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Role {

	/**
	 * Identifiant du rôle
	 */
	@Id
	private Long id;
	
	/**
	 * Nom du rôle
	 */
	private String nom;
	
	/**
	 * Liste des utilisateurs ayant ce rôle
	 */
	@OneToMany(mappedBy = "role")
	private List<UserCGB> listUsers = new ArrayList<UserCGB>();;

	public Long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}
	
	public List<UserCGB> getListUsers() {
		return listUsers;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public void setListUsers(List<UserCGB> listUsers) {
		this.listUsers = listUsers;
	}
	
	public void addUserCGB(UserCGB user) {
		this.listUsers.add(user);
	}
	
}
