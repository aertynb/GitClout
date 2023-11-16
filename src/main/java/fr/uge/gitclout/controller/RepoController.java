package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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

    @PostMapping
    public ResponseEntity<Repo> addRepo(@RequestBody Repo repo) throws URISyntaxException {
        Repo newRepo = repoService.addRepo(repo);
        return ResponseEntity.created(new URI("/api/repo/" + newRepo.getId())).body(newRepo);
    }
}
