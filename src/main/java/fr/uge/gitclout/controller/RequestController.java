package fr.uge.gitclout.controller;


import fr.uge.gitclout.entity.*;
import fr.uge.gitclout.service.*;
import fr.uge.gitclout.utilities.Analyzer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
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
    @Operation(summary = "Clone and store repository data",
            description = "Clones a repository from the provided link, analyzes its commits, tags, and contributors, and stores the data in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Repository data processed and stored successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<String> getRepo(@RequestBody @NotNull String link) throws GitAPIException, IOException {
        try (var git = initRepository(link)) {
            if (repoService.contains(link.split("/")[3], git.tagList().call().size())) {
                return ResponseEntity.ok("Repository already in DB");
            }
            run(link, git);
            rmFiles(new File("ressources/repo"));
        }
        return ResponseEntity.ok(link);
    }

    private void saveInDB(@NotNull Repo repo, @NotNull HashSet<Commiter> commiters, @NotNull List<Commit> commits,
                          @NotNull List<Tag> tags,
                          @NotNull List<Contribution> contributions) {
        repo = repoService.save(repo);
        commitService.saveAll(commits);
        commiterService.saveAll(commiters);
        tagService.saveAll(tags);
        contributionService.saveAll(contributions);
        repo.setCommiters(commiterService.findAll());
        repo.setCommits(commitService.findAll());
        repo.setTags(tagService.findAll());
    }

    private RevWalk createRevWalk(@NotNull Repository repository) throws IOException {
        var revWalk = new RevWalk(repository);
        revWalk.markStart(revWalk.parseCommit(repository.resolve("HEAD")));
        return revWalk;
    }

    private void run(@NotNull String link, @NotNull Git git) throws GitAPIException, IOException {
        var repo = new Repo(link.split("/")[3]);
        var revWalk = createRevWalk(git.getRepository());
        var commiters = new HashSet<Commiter>();
        var commits = new ArrayList<Commit>();
        for (var revCommit : revWalk) {
            var commiter = new Commiter(revCommit.getAuthorIdent().getName(), revCommit.getAuthorIdent().getEmailAddress(), repo);
            commiters.add(commiter);
            commits.add(new Commit(revCommit.getFullMessage(), commiter, repo));
        }
        var tags = tagService.addTags(git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS), repo);
        var analyzer = new Analyzer(git, tags, commiters, contributionService, revWalk);
        saveInDB(repo, commiters, commits, tags, analyzer.analyze(revWalk));
    }
}
