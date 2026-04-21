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
 * Possiblité de rajouter des requêtes comme vu en cours.
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

	@Query("SELECT t FROM Transfer t JOIN BatchTransfer b ON t.batch_id = b WHERE b.refLot = :refLot")
    public List<Transfer> findTransferByBatch(@Param("refLot") String refLot);
	
	@Query("Select t FROM Transfer t JOIN BatchTransfer b ON t.batch_id = b WHERE b.refLot = :refLot AND t.state != 'success'")
	public List<Transfer> findByRefLotAndNotSuccess(@Param("refLot") String refLot);
	
	@Query("SELECT t FROM Transfer t WHERE t.transferDate BETWEEN :start AND :end AND t.state != 'success'")
	public List<Transfer> findByDateIntervalAndNotSuccess(@Param("start") LocalDate start, @Param("end") LocalDate end);
	
	@Query("SELECT t FROM Transfer t WHERE t.destinationAccountNumber = :destinationAccountNumber AND t.state != 'success'")
	public List<Transfer> findByDestAccountAndNotSuccess(@Param("destinationAccountNumber") String destinationAccountNumber);
	
	@Query("SELECT t FROM Transfer t JOIN BatchTransfer b ON t.batch_id = b WHERE b.refLot = :refLot AND t.state = 'canceled'")
	public List<Transfer> findCancelledTransferFromBatch(@Param("refLot") String refLot);
	
}