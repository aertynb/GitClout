package fr.uge.gitclout.controller;


import fr.uge.gitclout.entity.*;
import fr.uge.gitclout.service.*;
import fr.uge.gitclout.utilities.Analyzer;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    public ResponseEntity<String> getRepo(@RequestBody @NotNull String link) throws GitAPIException, IOException {
        var start = System.currentTimeMillis();
        run(link);
        var end = System.currentTimeMillis();
        System.out.println("time in ms : " + (end - start));
        rmFiles(new File("ressources/repo"));
        return ResponseEntity.ok(link);
    }

    private void saveInDB(@NotNull Repo repo, @NotNull HashSet<Commiter> commiters, @NotNull List<Commit> commits, @NotNull List<Tag> tags,
                          @NotNull List<Contribution> contributions) {
        repo = repoService.save(repo);
        commitService.saveAll(commits);
        commiterService.saveAll(commiters);
        tagService.saveAll(tags);
        repo.setCommiters(commiterService.findAll());
        repo.setCommits(commitService.findAll());
        repo.setTags(tagService.findAll());
    }

    public void run(@NotNull String link) throws GitAPIException, IOException {
        try (var git = initRepository(link)) {
            var start = System.currentTimeMillis();
            var repo = repoService.addRepo(link);
            var revWalk = new RevWalk(git.getRepository());
            revWalk.markStart(revWalk.parseCommit(git.getRepository().resolve("HEAD")));
            var commiters = new HashSet<Commiter>();
            var commits = new ArrayList<Commit>();
            for (var revCommit : revWalk) {
                var commiter = commiterService.addCommiter(revCommit, repo);
                commiters.add(commiter);
                commits.add(commitService.addCommit(commiter, revCommit, repo));
            }
            var refs = git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS);
            var tags = tagService.addTags(refs, repo);
            var end = System.currentTimeMillis();
            System.out.println("time for parse in ms : " + (end - start));
            var analyzer = new Analyzer(git, tags, commiters, contributionService, revWalk);
            var contributions = analyzer.analyze(revWalk);
            saveInDB(repo, commiters, commits, tags, contributions);
            //System.out.println(contributions);
        }
    }
}
