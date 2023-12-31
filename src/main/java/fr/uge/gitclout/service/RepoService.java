package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.repository.RepoRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Repositories.
 */
@Service
public class RepoService {
    private final RepoRepository repoRepository;

    /**
     * Constructs a RepoService with the provided RepoRepository.
     *
     * @param repoRepository The repository for Repositories.
     */
    public RepoService(@NotNull RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    /**
     * Finds a Repo by its ID.
     *
     * @param id The ID of the Repo to find.
     * @return The found Repo.
     * @throws RuntimeException if the Repo is not found.
     */
    public Repo findRepoById(Long id) {
        return repoRepository.findById(id).orElseThrow();
    }

    /**
     * Retrieves all Repositories from the repository.
     *
     * @return A list of all Repositories.
     */
    public List<Repo> findAll() {
        return repoRepository.findAll();
    }

    /**
     * Saves a Repo into the repository.
     *
     * @param repo The Repo to be saved.
     * @return The saved Repo.
     */
    public Repo save(@NotNull Repo repo) {
        return repoRepository.save(repo);
    }

    /**
     * Checks if a Repository with a given name and number of tags exists in the repository.
     *
     * @param name   The name of the Repository.
     * @param nbTags The number of tags associated with the Repository.
     * @return true if a Repository with the specified name and number of tags exists, false otherwise.
     */
    public boolean contains(@NotNull String name, int nbTags) {
        return repoRepository.findAll().stream()
                .anyMatch(repo -> repo.getName().equals(name) && repo.getTags().size() == nbTags);
    }
}
