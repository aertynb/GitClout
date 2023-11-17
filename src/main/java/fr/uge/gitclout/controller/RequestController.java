package fr.uge.gitclout.controller;


import fr.uge.gitclout.gitclone.Cloner;
import fr.uge.gitclout.service.CommitService;
import fr.uge.gitclout.service.CommiterService;
import fr.uge.gitclout.service.RepoService;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Objects;

@RestController
@RequestMapping("clone-repo")
public class RequestController {

    private final CommiterService commiterService;
    private final CommitService commitService;
    private final RepoService repoService;

    public RequestController(@NotNull CommiterService commiterService, @NotNull CommitService commitService, @NotNull RepoService repoService) {
        Objects.requireNonNull(commiterService);
        Objects.requireNonNull(commitService);
        Objects.requireNonNull(repoService);
        this.commiterService = commiterService;
        this.commitService = commitService;
        this.repoService = repoService;
    }

    @PostMapping("/data")
    public ResponseEntity<String> getRepo(@RequestBody String link) throws GitAPIException {
        Objects.requireNonNull(link);
        run(link);
        return ResponseEntity.ok(link);
    }

    public void run(String link) throws GitAPIException {
        var repo = new Cloner();
        try (var git = repo.initRepository(link)) {
            commiterService.addAllCommiter(git);
        }
        repo.rmFiles(new File("ressources/repo"));
    }
}
