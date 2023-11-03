package fr.uge.gitclout.repository;

import fr.uge.gitclout.entity.Commiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommiterRepository extends JpaRepository<Commiter, Long> {
}
