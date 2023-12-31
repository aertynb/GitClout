package fr.uge.gitclout.repository;

import fr.uge.gitclout.entity.Commiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommiterRepository extends JpaRepository<Commiter, Long> {
}
