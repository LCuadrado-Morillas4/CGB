package cgb.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.Account;

/**
 * Classe représentant la table des comptes dans la DB.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

	@Query("SELECT COUNT(*) FROM Account a JOIN Transfer t ON a.accountNumber = t.sourceAccountNumber WHERE a.accountNumber = :accountNumber")
	public int hasAccountSentTransfers(@Param("accountNumber") String accountNumber);
	
}

