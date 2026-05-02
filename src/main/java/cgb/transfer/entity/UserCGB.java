package cgb.transfer.entity;

import jakarta.persistence.*;

@Entity
public class UserCGB {

	/**
	 * Identifiant de l'utilisateur
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Username de l'utilisateur
	 */
	private String username;
	
	/**
	 * Mot de passe de l'utilisateur
	 */
	private String password;
	
	/** 
	 * Email de l'utilisateur
	 */
	private String email;
	
	/**
	 * Rôle de l'utilisateur
	 */
	@Enumerated(EnumType.STRING)
	private Role role;
	
	/**
	 * Entreprise de l'utilisateur
	 */
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "customer_id")
	private Customer company;
	
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
	
	public Role getRole() {
		return role;
	}
	
	public Customer getCompany() {
		return company;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public void setCompany(Customer company) {
		this.company = company;
	}
		
}
