package cgb.transfer.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Classe permettant le mapping d'un tranfert par lot entre la BDD et l'API.
 */
@Entity
public class BatchTransfer {

	/**
	 * L'identifiant unique d'un lot
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Numéro unique du lot, composé de la date de création et du n° du lot
	 */
	private String refLot;
	
	/**
	 * L'identifiant unique du compte dont le transfert provient
	 */
	private String sourceAccountNumber;
	
	/**
	 * La description associée au lot
	 */
	private String description;
	
	/**
	 * La date du lot
	 */
	private LocalDate date;
	
	/**
	 * État du transfert
	 */
	private String state;
	
	@OneToMany(mappedBy = "batch_id", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Transfer> listTransfers = new ArrayList<Transfer>();;

	//Getters & Setters
	public Long getId() {
		return id;
	}

	public String getRefLot() {
		return refLot;
	}

	public String getSourceAccountNumber() {
		return sourceAccountNumber;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getState() {
		return state;
	}

	public List<Transfer> getListTransfers() {
		return listTransfers;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRefLot(String refLot) {
		this.refLot = refLot;
	}

	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setListTransfers(List<Transfer> listTransfers) {
		this.listTransfers = listTransfers;
	}
	
	public void addTransfer(Transfer transfer) {
		this.listTransfers.add(transfer);
	}
}
