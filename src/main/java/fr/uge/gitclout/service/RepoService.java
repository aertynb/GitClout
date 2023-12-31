package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.repository.RepoRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {
    private final RepoRepository repoRepository;

    public RepoService(@NotNull RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public Repo findRepoById(Long id) {
        return repoRepository.findById(id).orElseThrow();
    }
    public List<Repo> findAll() {
        return repoRepository.findAll();
    }

    public Repo save(@NotNull Repo repo) {
        return repoRepository.save(repo);
    }

    public boolean contains(@NotNull String name, int nbTags) {
        return repoRepository.findAll().stream()
                .anyMatch(repo -> repo.getName().equals(name) && repo.getTags().size() == nbTags);
    }
}
