package cgb.transfer.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.Transfer;

/**
 * Classe représentant la table des tranferts dans la DB.
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

	/**
	 * Renvoie la liste de transferts liée à un lot
	 * 
	 * @param refLot Numéro unique du transfer par lot
	 * 
	 * @return Liste des transferts
	 */
	@Query("SELECT t FROM Transfer t JOIN BatchTransfer b ON t.batch_id = b WHERE b.refLot = :refLot")
    public List<Transfer> findTransferByBatch(@Param("refLot") String refLot);
	
	/**
	 * Renvoie la liste de transferts liée en échec d'un lot
	 * 
	 * @param refLot Numéro unique du transfer par lot
	 * 
	 * @return Liste des transferts
	 */
	@Query("Select t FROM Transfer t JOIN BatchTransfer b ON t.batch_id = b WHERE b.refLot = :refLot AND t.state != 'success'")
	public List<Transfer> findByRefLotAndNotSuccess(@Param("refLot") String refLot);
	
	/**
	 * Renvoie la liste de transferts en échec sur une intervalle de date
	 * 
	 * @param start Date de début de l'intervalle
	 * @param end   Date de fin de l'intervalle
	 * 
	 * @return Liste des transferts
	 */
	@Query("Select t FROM Transfer t WHERE t.transferDate BETWEEN :start AND :end AND t.state != 'success'")
	public List<Transfer> findByDateIntervalAndNotSuccess(@Param("start") LocalDate start, @Param("end") LocalDate end);
	
	/**
	 * Renvoie la liste de transferts en échec liée au compte destinataire
	 * 
	 * @param destinationAccontNumber IBAN du compte destinataire
	 * 
	 * @return Liste des transferts
	 */
	@Query("Select t FROM Transfer t WHERE t.destinationAccountNumber = :destinationAccountNumber AND t.state != 'success'")
	public List<Transfer> findByDestAccountAndNotSuccess(@Param("destinationAccountNumber") String destinationAccountNumber);
	
	@Query("SELECT t FROM Transfer t JOIN BatchTransfer b ON t.batch_id = b WHERE b.refLot = :refLot AND t.state = 'canceled'")
	public List<Transfer> findCancelledTransferFromBatch(@Param("refLot") String refLot);
	
}