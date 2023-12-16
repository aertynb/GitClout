package fr.uge.gitclout.controller;


import fr.uge.gitclout.gitclone.Cloner;
import fr.uge.gitclout.service.CommitService;
import fr.uge.gitclout.service.CommiterService;
import fr.uge.gitclout.service.RepoService;
import fr.uge.gitclout.service.TagService;
import fr.uge.gitclout.utilities.Analyzer;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static fr.uge.gitclout.utilities.Cloner.initRepository;
import static fr.uge.gitclout.utilities.Cloner.rmFiles;

@RestController
@RequestMapping("clone-repo")
public class RequestController {

    private final CommiterService commiterService;
    private final CommitService commitService;
    private final RepoService repoService;
    private final TagService tagService;

    public RequestController(@NotNull CommiterService commiterService, @NotNull CommitService commitService, @NotNull RepoService repoService, @NotNull TagService tagService) {
        this.commiterService = commiterService;
        this.commitService = commitService;
        this.repoService = repoService;
        this.tagService = tagService;
    }

    @PostMapping("/data")
    public ResponseEntity<String> getRepo(@RequestBody String link) throws GitAPIException, IOException {
        Objects.requireNonNull(link);
        run(link);
        rmFiles(new File("ressources/repo"));
        return ResponseEntity.ok(link);
    }

    public void run(String link) throws GitAPIException, IOException {
        try (var git = initRepository(link)) {
            var analyzer = new Analyzer(git);
            var revWalk = new RevWalk(git.getRepository());
            revWalk.markStart(revWalk.parseCommit(git.getRepository().resolve("HEAD")));
            for (var revCommit : revWalk) {
                var commiter = commiterService.addCommiter(revCommit);
                commitService.addCommit(commiter, revCommit);
            }
            var refs = git.tagList().call();
            tagService.addTags(refs);
            repoService.addRepo(link, commiterService.findAll());
            analyzer.analyze(refs, revWalk);
        }
    }
}
