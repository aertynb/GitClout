package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Commiter;
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

    public Repo addRepo(String name, List<Commiter> commiters) {
        var repo = new Repo(name.split("/")[3]);
        repo = repoRepository.save(repo);
        if (repo.getCommiters() == null) {
            repo.setCommiters(commiters);
        }
        return repo;
    }
}
