package cgb.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.Customer;

/**
 * Classe représentant la table des entreprises partenaires dans la DB.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}

