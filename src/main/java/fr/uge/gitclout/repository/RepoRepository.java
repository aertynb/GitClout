package fr.uge.gitclout.repository;

import fr.uge.gitclout.entity.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoRepository extends JpaRepository<Repo, Long> { }
