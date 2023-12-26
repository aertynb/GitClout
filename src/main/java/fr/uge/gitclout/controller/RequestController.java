package fr.uge.gitclout.controller;


import fr.uge.gitclout.service.*;
import fr.uge.gitclout.utilities.Analyzer;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevWalk;
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
    private final ContributionService contributionService;

    public RequestController(@NotNull CommiterService commiterService, @NotNull CommitService commitService, @NotNull RepoService repoService,
                             @NotNull TagService tagService, @NotNull ContributionService contributionService) {
        this.commiterService = commiterService;
        this.commitService = commitService;
        this.repoService = repoService;
        this.tagService = tagService;
        this.contributionService = contributionService;
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
            var repo = repoService.addRepo(link);
            var revWalk = new RevWalk(git.getRepository());
            revWalk.markStart(revWalk.parseCommit(git.getRepository().resolve("HEAD")));
            for (var revCommit : revWalk) {
                var commiter = commiterService.addCommiter(revCommit, repo);
                commitService.addCommit(commiter, revCommit, repo);
            }
            var refs = git.tagList().call();
            var tags = tagService.addTags(refs, repo);
            repo.setCommiters(commiterService.findAll());
            repo.setCommits(commitService.findAll());
            repo.setTags(tags);
            var analyzer = new Analyzer(git, repo, tags, commiterService.findAll(), contributionService);
            analyzer.analyze(refs, revWalk);
        }
    }
}
