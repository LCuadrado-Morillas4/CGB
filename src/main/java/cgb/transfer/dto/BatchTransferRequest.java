package cgb.transfer.dto;


import java.time.LocalDate;
import java.util.List;

import cgb.transfer.entity.Transfer;


/**
 * La classe de DTO d'un transfert.
 */
public class BatchTransferRequest {
	
	/**
	 * La référence du lot est basée sur la date + un numéro d’ordre
	 */
	private String refLot;
	/**
	 * Le numéro de compte dont provient le transfert.
	 */
    private String sourceAccountNumber;
    /**
     * La description que l'utilisateur à associé au transfert; peut être vide.
     */
    private String description;
    /**
     * Liste des transferts du lot
     */
    private List<Transfer> listTransfers;
    
    public String getRefLot() {
    	return refLot;
    }
    public void setRefLot(String refLot) {
    	this.refLot = refLot;
    }
	public String getSourceAccountNumber() {
		return sourceAccountNumber;
	}
	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Transfer> getListTransfers() {
		return listTransfers;
	}
	public void setListTransfer(List<Transfer> listTransfers) {
		this.listTransfers = listTransfers;
	}


    
}
