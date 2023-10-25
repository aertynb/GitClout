package fr.uge.gitclout;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommiterRepository extends CrudRepository<Commiter, Long> {
    List<Commiter> findByLastName(String lastName);

    Commiter findById(long id);
}
