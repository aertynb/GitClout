package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {

    @Autowired
    private RepoRepository repoRepository;

    public List<Repo> findAll() {
        return repoRepository.findAll();
    }

    public Repo addRepo(Repo repo) {
        return repoRepository.save(repo);
    }
}
