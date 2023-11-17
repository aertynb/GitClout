package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Commit;
import fr.uge.gitclout.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api/commits") // requête POST
public class CommitController {

    @Autowired
    private CommitService commitService;

    @GetMapping
    public List<Commit> getCommits() {
        return commitService.findAll();
    }

    @PostMapping("/{commiterId}")
    public ResponseEntity<Commit> addCommit(@PathVariable Long commiterId, @RequestBody Commit commit) throws URISyntaxException {
        Commit newCommit = commitService.addCommit(commitService.createCommitWithCommiter(commiterId, commit));
        return ResponseEntity.created(new URI("/api/commits/" + newCommit.getId())).body(newCommit);
    }
}
