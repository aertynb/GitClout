package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.service.CommiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/commiters") // requête POST
public class CommiterController {
    private final CommiterService commiterService;

    @Autowired
    public CommiterController(CommiterService commiterService) {
        this.commiterService = commiterService;
    }

    @GetMapping
    public List<Commiter> getCommiters() {
        return commiterService.findAll();
    }

    @PostMapping
    public ResponseEntity<Commiter> addCommiter(@RequestBody Commiter commiter) throws URISyntaxException {
        Commiter newCommiter = commiterService.addCommiter(commiter);
        return ResponseEntity.created(new URI("/api/commiters/" + newCommiter.getId())).body(newCommiter);
    }
}
