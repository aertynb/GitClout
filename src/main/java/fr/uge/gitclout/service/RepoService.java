package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.repository.RepoRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {
    private RepoRepository repoRepository;

    public RepoService(@NotNull RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public List<Repo> findAll() {
        return repoRepository.findAll();
    }

    public Repo addRepo(Repo repo) {
        return repoRepository.save(repo);
    }
}
