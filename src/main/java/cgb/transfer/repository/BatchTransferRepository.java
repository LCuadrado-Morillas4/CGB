package cgb.transfer.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.BatchTransfer;

/**
 * Classe représentant la table des tranferts par lot dans la BDD.
 */
@Repository
public interface BatchTransferRepository extends JpaRepository<BatchTransfer, Long> {

	/**
	 * Renvoie un transfert par lot avec sa référence unique 
	 * 
	 * @param refLot L'identifiant unique du transfert par lot
	 *
	 * @return Le lot
	 */
	@Query("SELECT b FROM BatchTransfer b WHERE b.refLot = :refLot")
	public BatchTransfer findBatchByRefLot(@Param("refLot") String refLot);
	
	/**
	 * Renvoie le nombre de lots effectués sur une date précise
	 * 
	 * @param date
	 * 
	 * @return int
	 */
	@Query("SELECT COUNT(id) FROM BatchTransfer WHERE date = :date")
	public int countBatchTransfers(@Param("date") LocalDate date);
	
}
