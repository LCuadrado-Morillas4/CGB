package cgb.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.Role;

/**
 * Classe représentant la table des comptes dans la DB.
 * Possiblité de rajouter des requêtes comme vu en cours.
 * (il manque l'annotation de Repository)
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}

