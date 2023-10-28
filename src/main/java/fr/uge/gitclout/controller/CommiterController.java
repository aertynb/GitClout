package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.service.CommiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commiter") // requête POST
public class CommiterController {
    private final CommiterService commiterService;

    @Autowired
    public CommiterController(CommiterService commiterService) {
        this.commiterService = commiterService;
    }

    @PostMapping
    public ResponseEntity<Commiter> addCommiter(@RequestBody Commiter commiter) {
        Commiter newCommiter = commiterService.addCommiter(commiter);
        return new ResponseEntity<>(newCommiter, HttpStatus.CREATED);
    }
}
