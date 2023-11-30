package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/repository")
public class RepoController {
    private final RepoService repoService;

    @Autowired
    public RepoController(RepoService repoService) {
        Objects.requireNonNull(repoService);
        this.repoService = repoService;
    }

    @GetMapping
    public List<Repo> getRepo() {
        return repoService.findAll();
    }

    @PostMapping
    public ResponseEntity<Repo> addRepo(@RequestBody String name, @RequestBody List<Commiter> commiters) throws URISyntaxException {
        Repo newRepo = repoService.addRepo(name, commiters);
        return ResponseEntity.created(new URI("/api/repository/" + newRepo.getId())).body(newRepo);
    }
}
